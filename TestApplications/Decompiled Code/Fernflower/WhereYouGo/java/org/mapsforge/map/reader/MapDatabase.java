package org.mapsforge.map.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileHeader;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.reader.header.SubFileParameter;

public class MapDatabase {
   private static final long BITMASK_INDEX_OFFSET = 549755813887L;
   private static final long BITMASK_INDEX_WATER = 549755813888L;
   private static final String DEBUG_SIGNATURE_BLOCK = "block signature: ";
   private static final String DEBUG_SIGNATURE_POI = "POI signature: ";
   private static final String DEBUG_SIGNATURE_WAY = "way signature: ";
   private static final int INDEX_CACHE_SIZE = 64;
   private static final String INVALID_FIRST_WAY_OFFSET = "invalid first way offset: ";
   private static final Logger LOGGER = Logger.getLogger(MapDatabase.class.getName());
   private static final int MAXIMUM_WAY_NODES_SEQUENCE_LENGTH = 8192;
   private static final int MAXIMUM_ZOOM_TABLE_OBJECTS = 65536;
   private static final int POI_FEATURE_ELEVATION = 32;
   private static final int POI_FEATURE_HOUSE_NUMBER = 64;
   private static final int POI_FEATURE_NAME = 128;
   private static final int POI_LAYER_BITMASK = 240;
   private static final int POI_LAYER_SHIFT = 4;
   private static final int POI_NUMBER_OF_TAGS_BITMASK = 15;
   private static final String READ_ONLY_MODE = "r";
   private static final byte SIGNATURE_LENGTH_BLOCK = 32;
   private static final byte SIGNATURE_LENGTH_POI = 32;
   private static final byte SIGNATURE_LENGTH_WAY = 32;
   private static final String TAG_KEY_ELE = "ele";
   private static final String TAG_KEY_HOUSE_NUMBER = "addr:housenumber";
   private static final String TAG_KEY_NAME = "name";
   private static final String TAG_KEY_REF = "ref";
   private static final int WAY_FEATURE_DATA_BLOCKS_BYTE = 8;
   private static final int WAY_FEATURE_DOUBLE_DELTA_ENCODING = 4;
   private static final int WAY_FEATURE_HOUSE_NUMBER = 64;
   private static final int WAY_FEATURE_LABEL_POSITION = 16;
   private static final int WAY_FEATURE_NAME = 128;
   private static final int WAY_FEATURE_REF = 32;
   private static final int WAY_LAYER_BITMASK = 240;
   private static final int WAY_LAYER_SHIFT = 4;
   private static final int WAY_NUMBER_OF_TAGS_BITMASK = 15;
   private IndexCache databaseIndexCache;
   private long fileSize;
   private RandomAccessFile inputFile;
   private MapFileHeader mapFileHeader;
   private ReadBuffer readBuffer;
   private String signatureBlock;
   private String signaturePoi;
   private String signatureWay;
   private double tileLatitude;
   private double tileLongitude;

   private void decodeWayNodesDoubleDelta(GeoPoint[] var1) {
      double var2 = this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
      double var4 = this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
      var1[0] = new GeoPoint(var2, var4);
      double var6 = 0.0D;
      double var8 = 0.0D;

      for(int var10 = 1; var10 < var1.length; ++var10) {
         double var11 = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         double var13 = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         var6 += var11;
         var8 += var13;
         var2 += var6;
         var4 += var8;
         var1[var10] = new GeoPoint(var2, var4);
      }

   }

   private void decodeWayNodesSingleDelta(GeoPoint[] var1) {
      double var2 = this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
      double var4 = this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
      var1[0] = new GeoPoint(var2, var4);

      for(int var6 = 1; var6 < var1.length; ++var6) {
         var2 += CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         var4 += CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         var1[var6] = new GeoPoint(var2, var4);
      }

   }

   private void logDebugSignatures() {
      if (this.mapFileHeader.getMapFileInfo().debugFile) {
         LOGGER.warning("way signature: " + this.signatureWay);
         LOGGER.warning("block signature: " + this.signatureBlock);
      }

   }

   private void prepareExecution() {
      if (this.databaseIndexCache == null) {
         this.databaseIndexCache = new IndexCache(this.inputFile, 64);
      }

   }

   private PoiWayBundle processBlock(QueryParameters var1, SubFileParameter var2) {
      Object var3 = null;
      PoiWayBundle var4;
      if (!this.processBlockSignature()) {
         var4 = (PoiWayBundle)var3;
      } else {
         int[][] var5 = this.readZoomTable(var2);
         var4 = (PoiWayBundle)var3;
         if (var5 != null) {
            int var6 = var1.queryZoomLevel - var2.zoomLevelMin;
            int var7 = var5[var6][0];
            var6 = var5[var6][1];
            int var8 = this.readBuffer.readUnsignedInt();
            if (var8 < 0) {
               LOGGER.warning("invalid first way offset: " + var8);
               var4 = (PoiWayBundle)var3;
               if (this.mapFileHeader.getMapFileInfo().debugFile) {
                  LOGGER.warning("block signature: " + this.signatureBlock);
                  var4 = (PoiWayBundle)var3;
               }
            } else {
               var8 += this.readBuffer.getBufferPosition();
               if (var8 > this.readBuffer.getBufferSize()) {
                  LOGGER.warning("invalid first way offset: " + var8);
                  var4 = (PoiWayBundle)var3;
                  if (this.mapFileHeader.getMapFileInfo().debugFile) {
                     LOGGER.warning("block signature: " + this.signatureBlock);
                     var4 = (PoiWayBundle)var3;
                  }
               } else {
                  List var10 = this.processPOIs(var7);
                  var4 = (PoiWayBundle)var3;
                  if (var10 != null) {
                     if (this.readBuffer.getBufferPosition() > var8) {
                        LOGGER.warning("invalid buffer position: " + this.readBuffer.getBufferPosition());
                        var4 = (PoiWayBundle)var3;
                        if (this.mapFileHeader.getMapFileInfo().debugFile) {
                           LOGGER.warning("block signature: " + this.signatureBlock);
                           var4 = (PoiWayBundle)var3;
                        }
                     } else {
                        this.readBuffer.setBufferPosition(var8);
                        List var9 = this.processWays(var1, var6);
                        var4 = (PoiWayBundle)var3;
                        if (var9 != null) {
                           var4 = new PoiWayBundle(var10, var9);
                        }
                     }
                  }
               }
            }
         }
      }

      return var4;
   }

   private boolean processBlockSignature() {
      boolean var1;
      if (this.mapFileHeader.getMapFileInfo().debugFile) {
         this.signatureBlock = this.readBuffer.readUTF8EncodedString(32);
         if (!this.signatureBlock.startsWith("###TileStart")) {
            LOGGER.warning("invalid block signature: " + this.signatureBlock);
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   private MapReadResult processBlocks(QueryParameters var1, SubFileParameter var2) throws IOException {
      boolean var3 = true;
      boolean var4 = false;
      MapReadResultBuilder var5 = new MapReadResultBuilder();
      long var6 = var1.fromBlockY;

      MapReadResult var20;
      while(var6 <= var1.toBlockY) {
         long var8 = var1.fromBlockX;

         while(true) {
            if (var8 <= var1.toBlockX) {
               long var10 = var2.blocksWidth * var6 + var8;
               long var12 = this.databaseIndexCache.getIndexEntry(var2, var10);
               boolean var14 = var3;
               if (var3) {
                  if ((549755813888L & var12) != 0L) {
                     var14 = true;
                  } else {
                     var14 = false;
                  }

                  var14 &= var3;
                  var4 = true;
               }

               long var15 = var12 & 549755813887L;
               if (var15 >= 1L && var15 <= var2.subFileSize) {
                  if (1L + var10 == var2.numberOfBlocks) {
                     var10 = var2.subFileSize;
                  } else {
                     var12 = this.databaseIndexCache.getIndexEntry(var2, 1L + var10) & 549755813887L;
                     var10 = var12;
                     if (var12 > var2.subFileSize) {
                        LOGGER.warning("invalid next block pointer: " + var12);
                        LOGGER.warning("sub-file size: " + var2.subFileSize);
                        var20 = null;
                        return var20;
                     }
                  }

                  int var21 = (int)(var10 - var15);
                  if (var21 < 0) {
                     LOGGER.warning("current block size must not be negative: " + var21);
                     var20 = null;
                     return var20;
                  }

                  if (var21 != 0) {
                     if (var21 > 2500000) {
                        LOGGER.warning("current block size too large: " + var21);
                     } else {
                        label94: {
                           if ((long)var21 + var15 > this.fileSize) {
                              LOGGER.warning("current block largher than file size: " + var21);
                              var20 = null;
                              return var20;
                           }

                           this.inputFile.seek(var2.startAddress + var15);
                           if (!this.readBuffer.readFromFile(var21)) {
                              LOGGER.warning("reading current block has failed: " + var21);
                              var20 = null;
                              return var20;
                           }

                           this.tileLatitude = MercatorProjection.tileYToLatitude(var2.boundaryTileTop + var6, var2.baseZoomLevel);
                           this.tileLongitude = MercatorProjection.tileXToLongitude(var2.boundaryTileLeft + var8, var2.baseZoomLevel);

                           ArrayIndexOutOfBoundsException var10000;
                           label95: {
                              PoiWayBundle var17;
                              boolean var10001;
                              try {
                                 var17 = this.processBlock(var1, var2);
                              } catch (ArrayIndexOutOfBoundsException var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break label95;
                              }

                              if (var17 == null) {
                                 break label94;
                              }

                              try {
                                 var5.add(var17);
                                 break label94;
                              } catch (ArrayIndexOutOfBoundsException var18) {
                                 var10000 = var18;
                                 var10001 = false;
                              }
                           }

                           ArrayIndexOutOfBoundsException var22 = var10000;
                           LOGGER.log(Level.SEVERE, (String)null, var22);
                        }
                     }
                  }

                  ++var8;
                  var3 = var14;
                  continue;
               }

               LOGGER.warning("invalid current block pointer: " + var15);
               LOGGER.warning("subFileSize: " + var2.subFileSize);
               var20 = null;
               return var20;
            }

            ++var6;
            break;
         }
      }

      if (var3 && var4) {
         var5.isWater = true;
      }

      var20 = var5.build();
      return var20;
   }

   private List processPOIs(int var1) {
      ArrayList var2 = new ArrayList();
      Tag[] var3 = this.mapFileHeader.getMapFileInfo().poiTags;

      ArrayList var4;
      while(true) {
         var4 = var2;
         if (var1 == 0) {
            break;
         }

         if (this.mapFileHeader.getMapFileInfo().debugFile) {
            this.signaturePoi = this.readBuffer.readUTF8EncodedString(32);
            if (!this.signaturePoi.startsWith("***POIStart")) {
               LOGGER.warning("invalid POI signature: " + this.signaturePoi);
               LOGGER.warning("block signature: " + this.signatureBlock);
               var4 = null;
               break;
            }
         }

         double var5 = this.tileLatitude;
         double var7 = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         double var9 = this.tileLongitude;
         double var11 = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
         byte var13 = this.readBuffer.readByte();
         byte var14 = (byte)((var13 & 240) >>> 4);
         var13 = (byte)(var13 & 15);

         for(var4 = new ArrayList(); var13 != 0; --var13) {
            int var15 = this.readBuffer.readUnsignedInt();
            if (var15 < 0 || var15 >= var3.length) {
               LOGGER.warning("invalid POI tag ID: " + var15);
               if (this.mapFileHeader.getMapFileInfo().debugFile) {
                  LOGGER.warning("POI signature: " + this.signaturePoi);
                  LOGGER.warning("block signature: " + this.signatureBlock);
               }

               var4 = null;
               return var4;
            }

            var4.add(var3[var15]);
         }

         byte var16 = this.readBuffer.readByte();
         boolean var17;
         if ((var16 & 128) != 0) {
            var17 = true;
         } else {
            var17 = false;
         }

         boolean var18;
         if ((var16 & 64) != 0) {
            var18 = true;
         } else {
            var18 = false;
         }

         boolean var19;
         if ((var16 & 32) != 0) {
            var19 = true;
         } else {
            var19 = false;
         }

         if (var17) {
            var4.add(new Tag("name", this.readBuffer.readUTF8EncodedString()));
         }

         if (var18) {
            var4.add(new Tag("addr:housenumber", this.readBuffer.readUTF8EncodedString()));
         }

         if (var19) {
            var4.add(new Tag("ele", Integer.toString(this.readBuffer.readSignedInt())));
         }

         var2.add(new PointOfInterest(var14, var4, new GeoPoint(var5 + var7, var9 + var11)));
         --var1;
      }

      return var4;
   }

   private GeoPoint[][] processWayDataBlock(boolean var1) {
      int var2 = this.readBuffer.readUnsignedInt();
      GeoPoint[][] var3;
      if (var2 >= 1 && var2 <= 32767) {
         var3 = new GeoPoint[var2][];

         for(int var4 = 0; var4 < var2; ++var4) {
            int var5 = this.readBuffer.readUnsignedInt();
            if (var5 < 2 || var5 > 8192) {
               LOGGER.warning("invalid number of way nodes: " + var5);
               this.logDebugSignatures();
               var3 = (GeoPoint[][])null;
               break;
            }

            GeoPoint[] var6 = new GeoPoint[var5];
            if (var1) {
               this.decodeWayNodesDoubleDelta(var6);
            } else {
               this.decodeWayNodesSingleDelta(var6);
            }

            var3[var4] = var6;
         }
      } else {
         LOGGER.warning("invalid number of way coordinate blocks: " + var2);
         this.logDebugSignatures();
         var3 = (GeoPoint[][])null;
      }

      return var3;
   }

   private List processWays(QueryParameters var1, int var2) {
      ArrayList var3 = new ArrayList();
      Tag[] var4 = this.mapFileHeader.getMapFileInfo().wayTags;

      ArrayList var5;
      while(true) {
         var5 = var3;
         if (var2 == 0) {
            break;
         }

         if (this.mapFileHeader.getMapFileInfo().debugFile) {
            this.signatureWay = this.readBuffer.readUTF8EncodedString(32);
            if (!this.signatureWay.startsWith("---WayStart")) {
               LOGGER.warning("invalid way signature: " + this.signatureWay);
               LOGGER.warning("block signature: " + this.signatureBlock);
               var5 = null;
               break;
            }
         }

         int var6 = this.readBuffer.readUnsignedInt();
         if (var6 < 0) {
            LOGGER.warning("invalid way data size: " + var6);
            if (this.mapFileHeader.getMapFileInfo().debugFile) {
               LOGGER.warning("block signature: " + this.signatureBlock);
            }

            var5 = null;
            break;
         }

         label115: {
            int var7;
            if (var1.useTileBitmask) {
               var7 = this.readBuffer.readShort();
               if ((var1.queryTileBitmask & var7) == 0) {
                  this.readBuffer.skipBytes(var6 - 2);
                  break label115;
               }
            } else {
               this.readBuffer.skipBytes(2);
            }

            byte var17 = this.readBuffer.readByte();
            byte var8 = (byte)((var17 & 240) >>> 4);
            var17 = (byte)(var17 & 15);
            ArrayList var9 = new ArrayList();

            label102:
            while(true) {
               if (var17 == 0) {
                  byte var10 = this.readBuffer.readByte();
                  boolean var18;
                  if ((var10 & 128) != 0) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  boolean var19;
                  if ((var10 & 64) != 0) {
                     var19 = true;
                  } else {
                     var19 = false;
                  }

                  boolean var11;
                  if ((var10 & 32) != 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  boolean var12;
                  if ((var10 & 16) != 0) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  boolean var13;
                  if ((var10 & 8) != 0) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  boolean var14;
                  if ((var10 & 4) != 0) {
                     var14 = true;
                  } else {
                     var14 = false;
                  }

                  if (var18) {
                     var9.add(new Tag("name", this.readBuffer.readUTF8EncodedString()));
                  }

                  if (var19) {
                     var9.add(new Tag("addr:housenumber", this.readBuffer.readUTF8EncodedString()));
                  }

                  if (var11) {
                     var9.add(new Tag("ref", this.readBuffer.readUTF8EncodedString()));
                  }

                  GeoPoint var16 = this.readOptionalLabelPosition(var12);
                  var7 = this.readOptionalWayDataBlocksByte(var13);
                  if (var7 < 1) {
                     LOGGER.warning("invalid number of way data blocks: " + var7);
                     this.logDebugSignatures();
                     var5 = null;
                     return var5;
                  }

                  var6 = 0;

                  while(true) {
                     if (var6 >= var7) {
                        break label102;
                     }

                     GeoPoint[][] var15 = this.processWayDataBlock(var14);
                     if (var15 == null) {
                        var5 = null;
                        return var5;
                     }

                     var3.add(new Way(var8, var9, var15, var16));
                     ++var6;
                  }
               }

               var7 = this.readBuffer.readUnsignedInt();
               if (var7 < 0 || var7 >= var4.length) {
                  LOGGER.warning("invalid way tag ID: " + var7);
                  this.logDebugSignatures();
                  var5 = null;
                  return var5;
               }

               var9.add(var4[var7]);
               --var17;
            }
         }

         --var2;
      }

      return var5;
   }

   private GeoPoint readOptionalLabelPosition(boolean var1) {
      GeoPoint var2;
      if (var1) {
         var2 = new GeoPoint(this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()), this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()));
      } else {
         var2 = null;
      }

      return var2;
   }

   private int readOptionalWayDataBlocksByte(boolean var1) {
      int var2;
      if (var1) {
         var2 = this.readBuffer.readUnsignedInt();
      } else {
         var2 = 1;
      }

      return var2;
   }

   private int[][] readZoomTable(SubFileParameter var1) {
      int var2 = var1.zoomLevelMax - var1.zoomLevelMin + 1;
      int[][] var6 = new int[var2][2];
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;

      while(var5 < var2) {
         var3 += this.readBuffer.readUnsignedInt();
         var4 += this.readBuffer.readUnsignedInt();
         if (var3 >= 0 && var3 <= 65536) {
            if (var4 >= 0 && var4 <= 65536) {
               var6[var5][0] = var3;
               var6[var5][1] = var4;
               ++var5;
               continue;
            }

            LOGGER.warning("invalid cumulated number of ways in row " + var5 + ' ' + var4);
            if (this.mapFileHeader.getMapFileInfo().debugFile) {
               LOGGER.warning("block signature: " + this.signatureBlock);
            }

            var6 = (int[][])null;
            break;
         }

         LOGGER.warning("invalid cumulated number of POIs in row " + var5 + ' ' + var3);
         if (this.mapFileHeader.getMapFileInfo().debugFile) {
            LOGGER.warning("block signature: " + this.signatureBlock);
         }

         var6 = (int[][])null;
         break;
      }

      return var6;
   }

   public void closeFile() {
      try {
         this.mapFileHeader = null;
         if (this.databaseIndexCache != null) {
            this.databaseIndexCache.destroy();
            this.databaseIndexCache = null;
         }

         if (this.inputFile != null) {
            this.inputFile.close();
            this.inputFile = null;
         }

         this.readBuffer = null;
      } catch (IOException var2) {
         LOGGER.log(Level.SEVERE, (String)null, var2);
      }

   }

   public MapFileInfo getMapFileInfo() {
      if (this.mapFileHeader == null) {
         throw new IllegalStateException("no map file is currently opened");
      } else {
         return this.mapFileHeader.getMapFileInfo();
      }
   }

   public boolean hasOpenFile() {
      boolean var1;
      if (this.inputFile != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public FileOpenResult openFile(File var1) {
      FileOpenResult var11;
      IOException var10000;
      boolean var10001;
      if (var1 == null) {
         try {
            IllegalArgumentException var14 = new IllegalArgumentException("mapFile must not be null");
            throw var14;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      } else {
         label64: {
            StringBuilder var12;
            try {
               this.closeFile();
               if (!var1.exists()) {
                  var12 = new StringBuilder();
                  var11 = new FileOpenResult(var12.append("file does not exist: ").append(var1).toString());
                  return var11;
               }
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break label64;
            }

            try {
               if (!var1.isFile()) {
                  var12 = new StringBuilder();
                  var11 = new FileOpenResult(var12.append("not a file: ").append(var1).toString());
                  return var11;
               }
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label64;
            }

            try {
               if (!var1.canRead()) {
                  var12 = new StringBuilder();
                  var11 = new FileOpenResult(var12.append("cannot read file: ").append(var1).toString());
                  return var11;
               }
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label64;
            }

            try {
               RandomAccessFile var2 = new RandomAccessFile(var1, "r");
               this.inputFile = var2;
               this.fileSize = this.inputFile.length();
               ReadBuffer var9 = new ReadBuffer(this.inputFile);
               this.readBuffer = var9;
               MapFileHeader var10 = new MapFileHeader();
               this.mapFileHeader = var10;
               var11 = this.mapFileHeader.readHeader(this.readBuffer, this.fileSize);
               if (!var11.isSuccess()) {
                  this.closeFile();
                  return var11;
               }
            } catch (IOException var7) {
               var10000 = var7;
               var10001 = false;
               break label64;
            }

            try {
               var11 = FileOpenResult.SUCCESS;
               return var11;
            } catch (IOException var4) {
               var10000 = var4;
               var10001 = false;
            }
         }
      }

      IOException var13 = var10000;
      LOGGER.log(Level.SEVERE, (String)null, var13);
      this.closeFile();
      var11 = new FileOpenResult(var13.getMessage());
      return var11;
   }

   public MapReadResult readMapData(Tile var1) {
      Object var2 = null;

      MapReadResult var9;
      IOException var10000;
      label29: {
         QueryParameters var3;
         SubFileParameter var4;
         boolean var10001;
         try {
            this.prepareExecution();
            var3 = new QueryParameters();
            var3.queryZoomLevel = this.mapFileHeader.getQueryZoomLevel(var1.zoomLevel);
            var4 = this.mapFileHeader.getSubFileParameter(var3.queryZoomLevel);
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label29;
         }

         if (var4 == null) {
            label41: {
               try {
                  Logger var8 = LOGGER;
                  StringBuilder var11 = new StringBuilder();
                  var8.warning(var11.append("no sub-file for zoom level: ").append(var3.queryZoomLevel).toString());
               } catch (IOException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label41;
               }

               var9 = (MapReadResult)var2;
               return var9;
            }
         } else {
            try {
               QueryCalculations.calculateBaseTiles(var3, var1, var4);
               QueryCalculations.calculateBlocks(var3, var4);
               var9 = this.processBlocks(var3, var4);
               return var9;
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
            }
         }
      }

      IOException var10 = var10000;
      LOGGER.log(Level.SEVERE, (String)null, var10);
      var9 = (MapReadResult)var2;
      return var9;
   }
}
