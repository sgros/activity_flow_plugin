package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.map.reader.ReadBuffer;

public class MapFileHeader {
   private static final int BASE_ZOOM_LEVEL_MAX = 20;
   private static final int HEADER_SIZE_MIN = 70;
   private static final byte SIGNATURE_LENGTH_INDEX = 16;
   private static final char SPACE = ' ';
   private MapFileInfo mapFileInfo;
   private SubFileParameter[] subFileParameters;
   private byte zoomLevelMaximum;
   private byte zoomLevelMinimum;

   private FileOpenResult readSubFileParameters(ReadBuffer var1, long var2, MapFileInfoBuilder var4) {
      byte var5 = var1.readByte();
      FileOpenResult var15;
      if (var5 < 1) {
         var15 = new FileOpenResult("invalid number of sub-files: " + var5);
      } else {
         var4.numberOfSubFiles = (byte)var5;
         SubFileParameter[] var6 = new SubFileParameter[var5];
         this.zoomLevelMinimum = (byte)127;
         this.zoomLevelMaximum = (byte)-128;
         byte var7 = 0;

         while(true) {
            byte var9;
            if (var7 >= var5) {
               this.subFileParameters = new SubFileParameter[this.zoomLevelMaximum + 1];

               for(int var17 = 0; var17 < var5; ++var17) {
                  SubFileParameter var16 = var6[var17];

                  for(var9 = var16.zoomLevelMin; var9 <= var16.zoomLevelMax; ++var9) {
                     this.subFileParameters[var9] = var16;
                  }
               }

               var15 = FileOpenResult.SUCCESS;
               break;
            }

            SubFileParameterBuilder var8 = new SubFileParameterBuilder();
            var9 = var1.readByte();
            if (var9 >= 0 && var9 <= 20) {
               var8.baseZoomLevel = (byte)var9;
               var9 = var1.readByte();
               if (var9 >= 0 && var9 <= 22) {
                  var8.zoomLevelMin = (byte)var9;
                  byte var10 = var1.readByte();
                  if (var10 >= 0 && var10 <= 22) {
                     var8.zoomLevelMax = (byte)var10;
                     if (var9 > var10) {
                        var15 = new FileOpenResult("invalid zoom level range: " + var9 + ' ' + var10);
                        break;
                     }

                     long var11 = var1.readLong();
                     if (var11 >= 70L && var11 < var2) {
                        var8.startAddress = var11;
                        long var13 = var11;
                        if (var4.optionalFields.isDebugFile) {
                           var13 = var11 + 16L;
                        }

                        var8.indexStartAddress = var13;
                        var11 = var1.readLong();
                        if (var11 < 1L) {
                           var15 = new FileOpenResult("invalid sub-file size: " + var11);
                           break;
                        }

                        var8.subFileSize = var11;
                        var8.boundingBox = var4.boundingBox;
                        var6[var7] = var8.build();
                        this.updateZoomLevelInformation(var6[var7]);
                        ++var7;
                        continue;
                     }

                     var15 = new FileOpenResult("invalid start address: " + var11);
                     break;
                  }

                  var15 = new FileOpenResult("invalid maximum zoom level: " + var10);
                  break;
               }

               var15 = new FileOpenResult("invalid minimum zoom level: " + var9);
               break;
            }

            var15 = new FileOpenResult("invalid base zooom level: " + var9);
            break;
         }
      }

      return var15;
   }

   private void updateZoomLevelInformation(SubFileParameter var1) {
      if (this.zoomLevelMinimum > var1.zoomLevelMin) {
         this.zoomLevelMinimum = (byte)var1.zoomLevelMin;
      }

      if (this.zoomLevelMaximum < var1.zoomLevelMax) {
         this.zoomLevelMaximum = (byte)var1.zoomLevelMax;
      }

   }

   public MapFileInfo getMapFileInfo() {
      return this.mapFileInfo;
   }

   public byte getQueryZoomLevel(byte var1) {
      byte var2;
      if (var1 > this.zoomLevelMaximum) {
         var1 = this.zoomLevelMaximum;
         var2 = var1;
      } else {
         var2 = var1;
         if (var1 < this.zoomLevelMinimum) {
            var1 = this.zoomLevelMinimum;
            var2 = var1;
         }
      }

      return var2;
   }

   public SubFileParameter getSubFileParameter(int var1) {
      return this.subFileParameters[var1];
   }

   public FileOpenResult readHeader(ReadBuffer var1, long var2) throws IOException {
      FileOpenResult var4 = RequiredFields.readMagicByte(var1);
      FileOpenResult var6;
      if (!var4.isSuccess()) {
         var6 = var4;
      } else {
         var4 = RequiredFields.readRemainingHeader(var1);
         if (!var4.isSuccess()) {
            var6 = var4;
         } else {
            MapFileInfoBuilder var5 = new MapFileInfoBuilder();
            var4 = RequiredFields.readFileVersion(var1, var5);
            if (!var4.isSuccess()) {
               var6 = var4;
            } else {
               var4 = RequiredFields.readFileSize(var1, var2, var5);
               if (!var4.isSuccess()) {
                  var6 = var4;
               } else {
                  var4 = RequiredFields.readMapDate(var1, var5);
                  if (!var4.isSuccess()) {
                     var6 = var4;
                  } else {
                     var4 = RequiredFields.readBoundingBox(var1, var5);
                     if (!var4.isSuccess()) {
                        var6 = var4;
                     } else {
                        var4 = RequiredFields.readTilePixelSize(var1, var5);
                        if (!var4.isSuccess()) {
                           var6 = var4;
                        } else {
                           var4 = RequiredFields.readProjectionName(var1, var5);
                           if (!var4.isSuccess()) {
                              var6 = var4;
                           } else {
                              var4 = OptionalFields.readOptionalFields(var1, var5);
                              if (!var4.isSuccess()) {
                                 var6 = var4;
                              } else {
                                 var4 = RequiredFields.readPoiTags(var1, var5);
                                 if (!var4.isSuccess()) {
                                    var6 = var4;
                                 } else {
                                    var4 = RequiredFields.readWayTags(var1, var5);
                                    if (!var4.isSuccess()) {
                                       var6 = var4;
                                    } else {
                                       var6 = this.readSubFileParameters(var1, var2, var5);
                                       if (var6.isSuccess()) {
                                          this.mapFileInfo = var5.build();
                                          var6 = FileOpenResult.SUCCESS;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var6;
   }
}
