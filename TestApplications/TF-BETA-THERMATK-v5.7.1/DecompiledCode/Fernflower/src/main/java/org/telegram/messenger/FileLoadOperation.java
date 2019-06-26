package org.telegram.messenger;

import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.WriteToSocketDelegate;

public class FileLoadOperation {
   private static final int bigFileSizeFrom = 1048576;
   private static final int cdnChunkCheckSize = 131072;
   private static final int downloadChunkSize = 32768;
   private static final int downloadChunkSizeBig = 131072;
   private static final int maxCdnParts = 12288;
   private static final int maxDownloadRequests = 4;
   private static final int maxDownloadRequestsBig = 4;
   private static final int preloadMaxBytes = 2097152;
   private static final int stateDownloading = 1;
   private static final int stateFailed = 2;
   private static final int stateFinished = 3;
   private static final int stateIdle = 0;
   private boolean allowDisordererFileSave;
   private int bytesCountPadding;
   private File cacheFileFinal;
   private File cacheFileParts;
   private File cacheFilePreload;
   private File cacheFileTemp;
   private File cacheIvTemp;
   private byte[] cdnCheckBytes;
   private int cdnDatacenterId;
   private SparseArray cdnHashes;
   private byte[] cdnIv;
   private byte[] cdnKey;
   private byte[] cdnToken;
   private int currentAccount;
   private int currentDownloadChunkSize;
   private int currentMaxDownloadRequests;
   private int currentType;
   private int datacenterId;
   private ArrayList delayedRequestInfos;
   private FileLoadOperation.FileLoadOperationDelegate delegate;
   private int downloadedBytes;
   private boolean encryptFile;
   private byte[] encryptIv;
   private byte[] encryptKey;
   private String ext;
   private RandomAccessFile fileOutputStream;
   private RandomAccessFile filePartsStream;
   private RandomAccessFile fileReadStream;
   private RandomAccessFile fiv;
   private int foundMoovSize;
   private int initialDatacenterId;
   private boolean isCdn;
   private boolean isForceRequest;
   private boolean isPreloadVideoOperation;
   private byte[] iv;
   private byte[] key;
   protected TLRPC.InputFileLocation location;
   private int moovFound;
   private int nextAtomOffset;
   private boolean nextPartWasPreloaded;
   private int nextPreloadDownloadOffset;
   private ArrayList notCheckedCdnRanges;
   private ArrayList notLoadedBytesRanges;
   private volatile ArrayList notLoadedBytesRangesCopy;
   private ArrayList notRequestedBytesRanges;
   private Object parentObject;
   private volatile boolean paused;
   private boolean preloadFinished;
   private int preloadNotRequestedBytesCount;
   private RandomAccessFile preloadStream;
   private int preloadStreamFileOffset;
   private byte[] preloadTempBuffer = new byte[16];
   private int preloadTempBufferCount;
   private SparseArray preloadedBytesRanges;
   private int priority;
   private int renameRetryCount;
   private ArrayList requestInfos;
   private int requestedBytesCount;
   private SparseIntArray requestedPreloadedBytesRanges;
   private boolean requestingCdnOffsets;
   protected boolean requestingReference;
   private int requestsCount;
   private boolean reuploadingCdn;
   private boolean started;
   private volatile int state = 0;
   private File storePath;
   private ArrayList streamListeners;
   private int streamStartOffset;
   private boolean supportsPreloading;
   private File tempPath;
   private int totalBytesCount;
   private int totalPreloadedBytes;
   private boolean ungzip;
   private WebFile webFile;
   private TLRPC.InputWebFileLocation webLocation;

   public FileLoadOperation(int var1, WebFile var2) {
      this.currentAccount = var1;
      this.webFile = var2;
      this.webLocation = var2.location;
      this.totalBytesCount = var2.size;
      var1 = MessagesController.getInstance(this.currentAccount).webFileDatacenterId;
      this.datacenterId = var1;
      this.initialDatacenterId = var1;
      String var3 = FileLoader.getMimeTypePart(var2.mime_type);
      if (var2.mime_type.startsWith("image/")) {
         this.currentType = 16777216;
      } else if (var2.mime_type.equals("audio/ogg")) {
         this.currentType = 50331648;
      } else if (var2.mime_type.startsWith("video/")) {
         this.currentType = 33554432;
      } else {
         this.currentType = 67108864;
      }

      this.allowDisordererFileSave = true;
      this.ext = ImageLoader.getHttpUrlExtension(var2.url, var3);
   }

   public FileLoadOperation(ImageLocation var1, Object var2, String var3, int var4) {
      this.parentObject = var2;
      TLRPC.InputFileLocation var5;
      long var6;
      TLRPC.TL_fileLocationToBeDeprecated var9;
      if (var1.isEncrypted()) {
         this.location = new TLRPC.TL_inputEncryptedFileLocation();
         var5 = this.location;
         var9 = var1.location;
         var6 = var9.volume_id;
         var5.id = var6;
         var5.volume_id = var6;
         var5.local_id = var9.local_id;
         var5.access_hash = var1.access_hash;
         this.iv = new byte[32];
         byte[] var10 = var1.iv;
         byte[] var12 = this.iv;
         System.arraycopy(var10, 0, var12, 0, var12.length);
         this.key = var1.key;
      } else {
         TLRPC.InputFileLocation var11;
         TLRPC.TL_fileLocationToBeDeprecated var13;
         if (var1.photoPeer != null) {
            this.location = new TLRPC.TL_inputPeerPhotoFileLocation();
            var11 = this.location;
            var13 = var1.location;
            var6 = var13.volume_id;
            var11.id = var6;
            var11.volume_id = var6;
            var11.local_id = var13.local_id;
            var11.big = var1.photoPeerBig;
            var11.peer = var1.photoPeer;
         } else if (var1.stickerSet != null) {
            this.location = new TLRPC.TL_inputStickerSetThumb();
            var11 = this.location;
            var13 = var1.location;
            var6 = var13.volume_id;
            var11.id = var6;
            var11.volume_id = var6;
            var11.local_id = var13.local_id;
            var11.stickerset = var1.stickerSet;
         } else if (var1.thumbSize != null) {
            if (var1.photoId != 0L) {
               this.location = new TLRPC.TL_inputPhotoFileLocation();
               var11 = this.location;
               var11.id = var1.photoId;
               var13 = var1.location;
               var11.volume_id = var13.volume_id;
               var11.local_id = var13.local_id;
               var11.access_hash = var1.access_hash;
               var11.file_reference = var1.file_reference;
               var11.thumb_size = var1.thumbSize;
            } else {
               this.location = new TLRPC.TL_inputDocumentFileLocation();
               var5 = this.location;
               var5.id = var1.documentId;
               var9 = var1.location;
               var5.volume_id = var9.volume_id;
               var5.local_id = var9.local_id;
               var5.access_hash = var1.access_hash;
               var5.file_reference = var1.file_reference;
               var5.thumb_size = var1.thumbSize;
            }

            var11 = this.location;
            if (var11.file_reference == null) {
               var11.file_reference = new byte[0];
            }
         } else {
            this.location = new TLRPC.TL_inputFileLocation();
            var5 = this.location;
            var9 = var1.location;
            var5.volume_id = var9.volume_id;
            var5.local_id = var9.local_id;
            var5.secret = var1.access_hash;
            var5.file_reference = var1.file_reference;
            if (var5.file_reference == null) {
               var5.file_reference = new byte[0];
            }

            this.allowDisordererFileSave = true;
         }
      }

      int var8 = var1.dc_id;
      this.datacenterId = var8;
      this.initialDatacenterId = var8;
      this.currentType = 16777216;
      this.totalBytesCount = var4;
      if (var3 == null) {
         var3 = "jpg";
      }

      this.ext = var3;
   }

   public FileLoadOperation(SecureDocument var1) {
      this.location = new TLRPC.TL_inputSecureFileLocation();
      TLRPC.InputFileLocation var2 = this.location;
      TLRPC.TL_secureFile var3 = var1.secureFile;
      var2.id = var3.id;
      var2.access_hash = var3.access_hash;
      this.datacenterId = var3.dc_id;
      this.totalBytesCount = var3.size;
      this.allowDisordererFileSave = true;
      this.currentType = 67108864;
      this.ext = ".jpg";
   }

   public FileLoadOperation(TLRPC.Document var1, Object var2) {
      Exception var10000;
      label142: {
         boolean var3;
         boolean var10001;
         try {
            this.parentObject = var2;
            var3 = var1 instanceof TLRPC.TL_documentEncrypted;
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label142;
         }

         int var4;
         if (var3) {
            try {
               TLRPC.TL_inputEncryptedFileLocation var20 = new TLRPC.TL_inputEncryptedFileLocation();
               this.location = var20;
               this.location.id = var1.id;
               this.location.access_hash = var1.access_hash;
               var4 = var1.dc_id;
               this.datacenterId = var4;
               this.initialDatacenterId = var4;
               this.iv = new byte[32];
               System.arraycopy(var1.iv, 0, this.iv, 0, this.iv.length);
               this.key = var1.key;
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label142;
            }
         } else {
            label143: {
               try {
                  if (!(var1 instanceof TLRPC.TL_document)) {
                     break label143;
                  }

                  TLRPC.TL_inputDocumentFileLocation var21 = new TLRPC.TL_inputDocumentFileLocation();
                  this.location = var21;
                  this.location.id = var1.id;
                  this.location.access_hash = var1.access_hash;
                  this.location.file_reference = var1.file_reference;
                  this.location.thumb_size = "";
                  if (this.location.file_reference == null) {
                     this.location.file_reference = new byte[0];
                  }
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label142;
               }

               int var5;
               try {
                  var4 = var1.dc_id;
                  this.datacenterId = var4;
                  this.initialDatacenterId = var4;
                  this.allowDisordererFileSave = true;
                  var5 = var1.attributes.size();
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label142;
               }

               for(var4 = 0; var4 < var5; ++var4) {
                  try {
                     if (var1.attributes.get(var4) instanceof TLRPC.TL_documentAttributeVideo) {
                        this.supportsPreloading = true;
                        break;
                     }
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label142;
                  }
               }
            }
         }

         try {
            this.ungzip = "application/x-tgsticker".equals(var1.mime_type);
            this.totalBytesCount = var1.size;
            if (this.key != null && this.totalBytesCount % 16 != 0) {
               this.bytesCountPadding = 16 - this.totalBytesCount % 16;
               this.totalBytesCount += this.bytesCountPadding;
            }
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label142;
         }

         label105: {
            label104: {
               try {
                  this.ext = FileLoader.getDocumentFileName(var1);
                  if (this.ext == null) {
                     break label104;
                  }

                  var4 = this.ext.lastIndexOf(46);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label142;
               }

               if (var4 != -1) {
                  try {
                     this.ext = this.ext.substring(var4);
                     break label105;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label142;
                  }
               }
            }

            try {
               this.ext = "";
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label142;
            }
         }

         label147: {
            try {
               if ("audio/ogg".equals(var1.mime_type)) {
                  this.currentType = 50331648;
                  break label147;
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label142;
            }

            try {
               if (FileLoader.isVideoMimeType(var1.mime_type)) {
                  this.currentType = 33554432;
                  break label147;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label142;
            }

            try {
               this.currentType = 67108864;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label142;
            }
         }

         try {
            if (this.ext.length() <= 1) {
               this.ext = FileLoader.getExtensionByMimeType(var1.mime_type);
            }

            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var19 = var10000;
      FileLog.e((Throwable)var19);
      this.onFail(true, 0);
   }

   private void addPart(ArrayList var1, int var2, int var3, boolean var4) {
      if (var1 != null && var3 >= var2) {
         int var5 = var1.size();
         byte var6 = 0;
         int var7 = 0;

         FileLoadOperation.Range var9;
         boolean var14;
         while(true) {
            boolean var8 = true;
            if (var7 >= var5) {
               var14 = false;
               break;
            }

            var9 = (FileLoadOperation.Range)var1.get(var7);
            if (var2 <= var9.start) {
               if (var3 >= var9.end) {
                  var1.remove(var7);
                  var14 = var8;
                  break;
               }

               if (var3 > var9.start) {
                  var9.start = var3;
                  var14 = var8;
                  break;
               }
            } else {
               if (var3 < var9.end) {
                  var1.add(0, new FileLoadOperation.Range(var9.start, var2));
                  var9.start = var3;
                  var14 = var8;
                  break;
               }

               if (var2 < var9.end) {
                  var9.end = var2;
                  var14 = var8;
                  break;
               }
            }

            ++var7;
         }

         if (var4) {
            if (var14) {
               label58: {
                  Exception var10000;
                  label81: {
                     boolean var10001;
                     try {
                        this.filePartsStream.seek(0L);
                        var3 = var1.size();
                        this.filePartsStream.writeInt(var3);
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label81;
                     }

                     var2 = 0;

                     while(true) {
                        if (var2 >= var3) {
                           break label58;
                        }

                        try {
                           var9 = (FileLoadOperation.Range)var1.get(var2);
                           this.filePartsStream.writeInt(var9.start);
                           this.filePartsStream.writeInt(var9.end);
                        } catch (Exception var10) {
                           var10000 = var10;
                           var10001 = false;
                           break;
                        }

                        ++var2;
                     }
                  }

                  Exception var12 = var10000;
                  FileLog.e((Throwable)var12);
               }

               var1 = this.streamListeners;
               if (var1 != null) {
                  var3 = var1.size();

                  for(var2 = var6; var2 < var3; ++var2) {
                     ((FileLoadOperationStream)this.streamListeners.get(var2)).newDataAvailable();
                  }
               }
            } else if (BuildVars.LOGS_ENABLED) {
               StringBuilder var13 = new StringBuilder();
               var13.append(this.cacheFileFinal);
               var13.append(" downloaded duplicate file part ");
               var13.append(var2);
               var13.append(" - ");
               var13.append(var3);
               FileLog.e(var13.toString());
            }
         }
      }

   }

   private void cleanup() {
      RandomAccessFile var1;
      Exception var10000;
      boolean var10001;
      Exception var20;
      label159: {
         label162: {
            try {
               var1 = this.fileOutputStream;
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label162;
            }

            if (var1 == null) {
               break label159;
            }

            try {
               this.fileOutputStream.getChannel().close();
            } catch (Exception var18) {
               var20 = var18;

               try {
                  FileLog.e((Throwable)var20);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label162;
               }
            }

            try {
               this.fileOutputStream.close();
               this.fileOutputStream = null;
               break label159;
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
            }
         }

         var20 = var10000;
         FileLog.e((Throwable)var20);
      }

      label144: {
         label163: {
            try {
               var1 = this.preloadStream;
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label163;
            }

            if (var1 == null) {
               break label144;
            }

            try {
               this.preloadStream.getChannel().close();
            } catch (Exception var14) {
               var20 = var14;

               try {
                  FileLog.e((Throwable)var20);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label163;
               }
            }

            try {
               this.preloadStream.close();
               this.preloadStream = null;
               break label144;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
            }
         }

         var20 = var10000;
         FileLog.e((Throwable)var20);
      }

      label129: {
         label164: {
            try {
               var1 = this.fileReadStream;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label164;
            }

            if (var1 == null) {
               break label129;
            }

            try {
               this.fileReadStream.getChannel().close();
            } catch (Exception var10) {
               var20 = var10;

               try {
                  FileLog.e((Throwable)var20);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label164;
               }
            }

            try {
               this.fileReadStream.close();
               this.fileReadStream = null;
               break label129;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         var20 = var10000;
         FileLog.e((Throwable)var20);
      }

      label114: {
         label165: {
            try {
               var1 = this.filePartsStream;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label165;
            }

            if (var1 == null) {
               break label114;
            }

            try {
               this.filePartsStream.getChannel().close();
            } catch (Exception var6) {
               var20 = var6;

               try {
                  FileLog.e((Throwable)var20);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label165;
               }
            }

            try {
               this.filePartsStream.close();
               this.filePartsStream = null;
               break label114;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         var20 = var10000;
         FileLog.e((Throwable)var20);
      }

      try {
         if (this.fiv != null) {
            this.fiv.close();
            this.fiv = null;
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      if (this.delayedRequestInfos != null) {
         for(int var2 = 0; var2 < this.delayedRequestInfos.size(); ++var2) {
            FileLoadOperation.RequestInfo var21 = (FileLoadOperation.RequestInfo)this.delayedRequestInfos.get(var2);
            if (var21.response != null) {
               var21.response.disableFree = false;
               var21.response.freeResources();
            } else if (var21.responseWeb != null) {
               var21.responseWeb.disableFree = false;
               var21.responseWeb.freeResources();
            } else if (var21.responseCdn != null) {
               var21.responseCdn.disableFree = false;
               var21.responseCdn.freeResources();
            }
         }

         this.delayedRequestInfos.clear();
      }

   }

   private void clearOperaion(FileLoadOperation.RequestInfo var1, boolean var2) {
      int var3 = 0;

      int var4;
      for(var4 = Integer.MAX_VALUE; var3 < this.requestInfos.size(); ++var3) {
         FileLoadOperation.RequestInfo var5 = (FileLoadOperation.RequestInfo)this.requestInfos.get(var3);
         var4 = Math.min(var5.offset, var4);
         if (this.isPreloadVideoOperation) {
            this.requestedPreloadedBytesRanges.delete(var5.offset);
         } else {
            this.removePart(this.notRequestedBytesRanges, var5.offset, var5.offset + this.currentDownloadChunkSize);
         }

         if (var1 != var5 && var5.requestToken != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var5.requestToken, true);
         }
      }

      this.requestInfos.clear();

      for(var3 = 0; var3 < this.delayedRequestInfos.size(); ++var3) {
         var1 = (FileLoadOperation.RequestInfo)this.delayedRequestInfos.get(var3);
         if (this.isPreloadVideoOperation) {
            this.requestedPreloadedBytesRanges.delete(var1.offset);
         } else {
            this.removePart(this.notRequestedBytesRanges, var1.offset, var1.offset + this.currentDownloadChunkSize);
         }

         if (var1.response != null) {
            var1.response.disableFree = false;
            var1.response.freeResources();
         } else if (var1.responseWeb != null) {
            var1.responseWeb.disableFree = false;
            var1.responseWeb.freeResources();
         } else if (var1.responseCdn != null) {
            var1.responseCdn.disableFree = false;
            var1.responseCdn.freeResources();
         }

         var4 = Math.min(var1.offset, var4);
      }

      this.delayedRequestInfos.clear();
      this.requestsCount = 0;
      if (!var2 && this.isPreloadVideoOperation) {
         this.requestedBytesCount = this.totalPreloadedBytes;
      } else if (this.notLoadedBytesRanges == null) {
         this.downloadedBytes = var4;
         this.requestedBytesCount = var4;
      }

   }

   private void copyNotLoadedRanges() {
      ArrayList var1 = this.notLoadedBytesRanges;
      if (var1 != null) {
         this.notLoadedBytesRangesCopy = new ArrayList(var1);
      }
   }

   private void delayRequestInfo(FileLoadOperation.RequestInfo var1) {
      this.delayedRequestInfos.add(var1);
      if (var1.response != null) {
         var1.response.disableFree = true;
      } else if (var1.responseWeb != null) {
         var1.responseWeb.disableFree = true;
      } else if (var1.responseCdn != null) {
         var1.responseCdn.disableFree = true;
      }

   }

   private int findNextPreloadDownloadOffset(int var1, int var2, NativeByteBuffer var3) {
      int var4 = var3.limit();

      while(true) {
         byte var5;
         if (this.preloadTempBuffer != null) {
            var5 = 16;
         } else {
            var5 = 0;
         }

         if (var1 < var2 - var5) {
            break;
         }

         int var6 = var2 + var4;
         if (var1 >= var6) {
            break;
         }

         if (var1 >= var6 - 16) {
            this.preloadTempBufferCount = var6 - var1;
            var3.position(var3.limit() - this.preloadTempBufferCount);
            var3.readBytes(this.preloadTempBuffer, 0, this.preloadTempBufferCount, false);
            return var6;
         }

         byte[] var7;
         int var9;
         if (this.preloadTempBufferCount != 0) {
            var3.position(0);
            var7 = this.preloadTempBuffer;
            var9 = this.preloadTempBufferCount;
            var3.readBytes(var7, var9, 16 - var9, false);
            this.preloadTempBufferCount = 0;
         } else {
            var3.position(var1 - var2);
            var3.readBytes(this.preloadTempBuffer, 0, 16, false);
         }

         var7 = this.preloadTempBuffer;
         int var8 = ((var7[0] & 255) << 24) + ((var7[1] & 255) << 16) + ((var7[2] & 255) << 8) + (var7[3] & 255);
         if (var8 == 0) {
            return 0;
         }

         var9 = var8;
         if (var8 == 1) {
            var9 = ((var7[12] & 255) << 24) + ((var7[13] & 255) << 16) + ((var7[14] & 255) << 8) + (var7[15] & 255);
         }

         var7 = this.preloadTempBuffer;
         if (var7[4] == 109 && var7[5] == 111 && var7[6] == 111 && var7[7] == 118) {
            return -var9;
         }

         var9 += var1;
         var1 = var9;
         if (var9 >= var6) {
            return var9;
         }
      }

      return 0;
   }

   private int getDownloadedLengthFromOffsetInternal(ArrayList var1, int var2, int var3) {
      int var7;
      if (var1 != null && this.state != 3 && !var1.isEmpty()) {
         int var4 = var1.size();
         int var5 = var3;
         FileLoadOperation.Range var6 = null;

         FileLoadOperation.Range var9;
         for(var7 = 0; var7 < var4; var6 = var9) {
            FileLoadOperation.Range var8 = (FileLoadOperation.Range)var1.get(var7);
            var9 = var6;
            if (var2 <= var8.start) {
               label54: {
                  if (var6 != null) {
                     var9 = var6;
                     if (var8.start >= var6.start) {
                        break label54;
                     }
                  }

                  var9 = var8;
               }
            }

            int var10 = var5;
            if (var8.start <= var2) {
               var10 = var5;
               if (var8.end > var2) {
                  var10 = 0;
               }
            }

            ++var7;
            var5 = var10;
         }

         if (var5 == 0) {
            return 0;
         } else if (var6 != null) {
            return Math.min(var3, var6.start - var2);
         } else {
            return Math.min(var3, Math.max(this.totalBytesCount - var2, 0));
         }
      } else {
         var7 = this.downloadedBytes;
         return var7 == 0 ? var3 : Math.min(var3, Math.max(var7 - var2, 0));
      }
   }

   private void onFinishLoadingFile(boolean var1) {
      if (this.state == 1) {
         this.state = 3;
         this.cleanup();
         StringBuilder var2;
         if (this.isPreloadVideoOperation) {
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
               var2 = new StringBuilder();
               var2.append("finished preloading file to ");
               var2.append(this.cacheFileTemp);
               var2.append(" loaded ");
               var2.append(this.totalPreloadedBytes);
               var2.append(" of ");
               var2.append(this.totalBytesCount);
               FileLog.d(var2.toString());
            }
         } else {
            File var8 = this.cacheIvTemp;
            if (var8 != null) {
               var8.delete();
               this.cacheIvTemp = null;
            }

            var8 = this.cacheFileParts;
            if (var8 != null) {
               var8.delete();
               this.cacheFileParts = null;
            }

            var8 = this.cacheFilePreload;
            if (var8 != null) {
               var8.delete();
               this.cacheFilePreload = null;
            }

            var8 = this.cacheFileTemp;
            if (var8 != null) {
               if (this.ungzip) {
                  try {
                     FileInputStream var4 = new FileInputStream(var8);
                     GZIPInputStream var3 = new GZIPInputStream(var4);
                     FileLoader.copyFile(var3, this.cacheFileFinal);
                     var3.close();
                     this.cacheFileTemp.delete();
                  } catch (ZipException var6) {
                     this.ungzip = false;
                  } catch (Throwable var7) {
                     FileLog.e(var7);
                     var2 = new StringBuilder();
                     var2.append("unable to ungzip temp = ");
                     var2.append(this.cacheFileTemp);
                     var2.append(" to final = ");
                     var2.append(this.cacheFileFinal);
                     FileLog.e(var2.toString());
                  }
               }

               if (!this.ungzip && !this.cacheFileTemp.renameTo(this.cacheFileFinal)) {
                  if (BuildVars.LOGS_ENABLED) {
                     var2 = new StringBuilder();
                     var2.append("unable to rename temp = ");
                     var2.append(this.cacheFileTemp);
                     var2.append(" to final = ");
                     var2.append(this.cacheFileFinal);
                     var2.append(" retry = ");
                     var2.append(this.renameRetryCount);
                     FileLog.e(var2.toString());
                  }

                  ++this.renameRetryCount;
                  if (this.renameRetryCount < 3) {
                     this.state = 1;
                     Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$Pg1188DV6hLAQ13wzfrMjXc2Ie0(this, var1), 200L);
                     return;
                  }

                  this.cacheFileFinal = this.cacheFileTemp;
               }
            }

            if (BuildVars.LOGS_ENABLED) {
               var2 = new StringBuilder();
               var2.append("finished downloading file to ");
               var2.append(this.cacheFileFinal);
               FileLog.d(var2.toString());
            }

            if (var1) {
               int var5 = this.currentType;
               if (var5 == 50331648) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
               } else if (var5 == 33554432) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
               } else if (var5 == 16777216) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
               } else if (var5 == 67108864) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
               }
            }
         }

         this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
      }
   }

   private void removePart(ArrayList var1, int var2, int var3) {
      if (var1 != null && var3 >= var2) {
         int var4 = var1.size();
         int var5 = 0;

         boolean var8;
         while(true) {
            boolean var6 = true;
            if (var5 >= var4) {
               var8 = false;
               break;
            }

            FileLoadOperation.Range var7 = (FileLoadOperation.Range)var1.get(var5);
            if (var2 == var7.end) {
               var7.end = var3;
               var8 = var6;
               break;
            }

            if (var3 == var7.start) {
               var7.start = var2;
               var8 = var6;
               break;
            }

            ++var5;
         }

         if (!var8) {
            var1.add(new FileLoadOperation.Range(var2, var3));
         }
      }

   }

   private void requestFileOffsets(int var1) {
      if (!this.requestingCdnOffsets) {
         this.requestingCdnOffsets = true;
         TLRPC.TL_upload_getCdnFileHashes var2 = new TLRPC.TL_upload_getCdnFileHashes();
         var2.file_token = this.cdnToken;
         var2.offset = var1;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$FileLoadOperation$AloCOvGHlndklEjA6lccwgvlez8(this), (QuickAckDelegate)null, (WriteToSocketDelegate)null, 0, this.datacenterId, 1, true);
      }
   }

   private void requestReference(FileLoadOperation.RequestInfo var1) {
      if (!this.requestingReference) {
         this.clearOperaion(var1, false);
         this.requestingReference = true;
         Object var2 = this.parentObject;
         if (var2 instanceof MessageObject) {
            MessageObject var3 = (MessageObject)var2;
            if (var3.getId() < 0) {
               TLRPC.WebPage var4 = var3.messageOwner.media.webpage;
               if (var4 != null) {
                  this.parentObject = var4;
               }
            }
         }

         FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, var1);
      }
   }

   public void cancel() {
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$J6yoj3V6lUrD2GOh_IqPARM2LhA(this));
   }

   protected File getCurrentFile() {
      CountDownLatch var1 = new CountDownLatch(1);
      File[] var2 = new File[1];
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$sszlpx_7B35gY8yLDFGHhHn8Nio(this, var2, var1));

      try {
         var1.await();
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      return var2[0];
   }

   public int getCurrentType() {
      return this.currentType;
   }

   public int getDatacenterId() {
      return this.initialDatacenterId;
   }

   protected float getDownloadedLengthFromOffset(float var1) {
      ArrayList var2 = this.notLoadedBytesRangesCopy;
      int var3 = this.totalBytesCount;
      return var3 != 0 && var2 != null ? var1 + (float)this.getDownloadedLengthFromOffsetInternal(var2, (int)((float)var3 * var1), var3) / (float)this.totalBytesCount : 0.0F;
   }

   protected int getDownloadedLengthFromOffset(int var1, int var2) {
      CountDownLatch var3 = new CountDownLatch(1);
      int[] var4 = new int[1];
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$7ur7HYEmrvSDh4lwc7_mdrw1G5A(this, var4, var1, var2, var3));

      try {
         var3.await();
      } catch (Exception var5) {
      }

      return var4[0];
   }

   public String getFileName() {
      StringBuilder var1;
      if (this.location != null) {
         var1 = new StringBuilder();
         var1.append(this.location.volume_id);
         var1.append("_");
         var1.append(this.location.local_id);
         var1.append(".");
         var1.append(this.ext);
         return var1.toString();
      } else {
         var1 = new StringBuilder();
         var1.append(Utilities.MD5(this.webFile.url));
         var1.append(".");
         var1.append(this.ext);
         return var1.toString();
      }
   }

   public int getPriority() {
      return this.priority;
   }

   public boolean isForceRequest() {
      return this.isForceRequest;
   }

   public boolean isPaused() {
      return this.paused;
   }

   public boolean isPreloadFinished() {
      return this.preloadFinished;
   }

   public boolean isPreloadVideoOperation() {
      return this.isPreloadVideoOperation;
   }

   // $FF: synthetic method
   public void lambda$cancel$6$FileLoadOperation() {
      if (this.state != 3 && this.state != 2) {
         if (this.requestInfos != null) {
            for(int var1 = 0; var1 < this.requestInfos.size(); ++var1) {
               FileLoadOperation.RequestInfo var2 = (FileLoadOperation.RequestInfo)this.requestInfos.get(var1);
               if (var2.requestToken != 0) {
                  ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var2.requestToken, true);
               }
            }
         }

         this.onFail(false, 1);
      }

   }

   // $FF: synthetic method
   public void lambda$getCurrentFile$0$FileLoadOperation(File[] var1, CountDownLatch var2) {
      if (this.state == 3) {
         var1[0] = this.cacheFileFinal;
      } else {
         var1[0] = this.cacheFileTemp;
      }

      var2.countDown();
   }

   // $FF: synthetic method
   public void lambda$getDownloadedLengthFromOffset$1$FileLoadOperation(int[] var1, int var2, int var3, CountDownLatch var4) {
      var1[0] = this.getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, var2, var3);
      var4.countDown();
   }

   // $FF: synthetic method
   public void lambda$null$11$FileLoadOperation(FileLoadOperation.RequestInfo var1, TLObject var2, TLRPC.TL_error var3) {
      byte var4 = 0;
      this.reuploadingCdn = false;
      if (var3 == null) {
         TLRPC.Vector var7 = (TLRPC.Vector)var2;
         if (!var7.objects.isEmpty()) {
            int var5 = var4;
            if (this.cdnHashes == null) {
               this.cdnHashes = new SparseArray();
               var5 = var4;
            }

            while(var5 < var7.objects.size()) {
               TLRPC.TL_fileHash var6 = (TLRPC.TL_fileHash)var7.objects.get(var5);
               this.cdnHashes.put(var6.offset, var6);
               ++var5;
            }
         }

         this.startDownloadRequest();
      } else if (!var3.text.equals("FILE_TOKEN_INVALID") && !var3.text.equals("REQUEST_TOKEN_INVALID")) {
         this.onFail(false, 0);
      } else {
         this.isCdn = false;
         this.clearOperaion(var1, false);
         this.startDownloadRequest();
      }

   }

   // $FF: synthetic method
   public void lambda$onFail$9$FileLoadOperation(int var1) {
      this.delegate.didFailedLoadingFile(this, var1);
   }

   // $FF: synthetic method
   public void lambda$onFinishLoadingFile$7$FileLoadOperation(boolean var1) {
      try {
         this.onFinishLoadingFile(var1);
      } catch (Exception var3) {
         this.onFail(false, 0);
      }

   }

   // $FF: synthetic method
   public void lambda$removeStreamListener$2$FileLoadOperation(FileStreamLoadOperation var1) {
      ArrayList var2 = this.streamListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   // $FF: synthetic method
   public void lambda$requestFileOffsets$8$FileLoadOperation(TLObject var1, TLRPC.TL_error var2) {
      if (var2 != null) {
         this.onFail(false, 0);
      } else {
         this.requestingCdnOffsets = false;
         TLRPC.Vector var4 = (TLRPC.Vector)var1;
         int var3;
         if (!var4.objects.isEmpty()) {
            if (this.cdnHashes == null) {
               this.cdnHashes = new SparseArray();
            }

            for(var3 = 0; var3 < var4.objects.size(); ++var3) {
               TLRPC.TL_fileHash var6 = (TLRPC.TL_fileHash)var4.objects.get(var3);
               this.cdnHashes.put(var6.offset, var6);
            }
         }

         for(var3 = 0; var3 < this.delayedRequestInfos.size(); ++var3) {
            FileLoadOperation.RequestInfo var5 = (FileLoadOperation.RequestInfo)this.delayedRequestInfos.get(var3);
            if (this.notLoadedBytesRanges != null || this.downloadedBytes == var5.offset) {
               this.delayedRequestInfos.remove(var3);
               if (!this.processRequestResult(var5, (TLRPC.TL_error)null)) {
                  if (var5.response != null) {
                     var5.response.disableFree = false;
                     var5.response.freeResources();
                  } else if (var5.responseWeb != null) {
                     var5.responseWeb.disableFree = false;
                     var5.responseWeb.freeResources();
                  } else if (var5.responseCdn != null) {
                     var5.responseCdn.disableFree = false;
                     var5.responseCdn.freeResources();
                  }
               }
               break;
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$setIsPreloadVideoOperation$5$FileLoadOperation(boolean var1) {
      this.requestedBytesCount = 0;
      this.clearOperaion((FileLoadOperation.RequestInfo)null, true);
      this.isPreloadVideoOperation = var1;
      this.startDownloadRequest();
   }

   // $FF: synthetic method
   public void lambda$start$3$FileLoadOperation(int var1, FileLoadOperationStream var2, boolean var3) {
      if (this.streamListeners == null) {
         this.streamListeners = new ArrayList();
      }

      int var4 = this.currentDownloadChunkSize;
      this.streamStartOffset = var1 / var4 * var4;
      this.streamListeners.add(var2);
      if (var3) {
         if (this.preloadedBytesRanges != null && this.getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, this.streamStartOffset, 1) == 0 && this.preloadedBytesRanges.get(this.streamStartOffset) != null) {
            this.nextPartWasPreloaded = true;
         }

         this.startDownloadRequest();
         this.nextPartWasPreloaded = false;
      }

   }

   // $FF: synthetic method
   public void lambda$start$4$FileLoadOperation(boolean[] var1) {
      if (this.totalBytesCount != 0 && (this.isPreloadVideoOperation && var1[0] || this.downloadedBytes == this.totalBytesCount)) {
         try {
            this.onFinishLoadingFile(false);
         } catch (Exception var2) {
            this.onFail(true, 0);
         }
      } else {
         this.startDownloadRequest();
      }

   }

   // $FF: synthetic method
   public void lambda$startDownloadRequest$10$FileLoadOperation(FileLoadOperation.RequestInfo var1) {
      this.processRequestResult(var1, (TLRPC.TL_error)null);
      var1.response.freeResources();
   }

   // $FF: synthetic method
   public void lambda$startDownloadRequest$12$FileLoadOperation(FileLoadOperation.RequestInfo var1, TLObject var2, TLObject var3, TLRPC.TL_error var4) {
      if (this.requestInfos.contains(var1)) {
         if (var4 != null) {
            if (FileRefController.isFileRefError(var4.text)) {
               this.requestReference(var1);
               return;
            }

            if (var2 instanceof TLRPC.TL_upload_getCdnFile && var4.text.equals("FILE_TOKEN_INVALID")) {
               this.isCdn = false;
               this.clearOperaion(var1, false);
               this.startDownloadRequest();
               return;
            }
         }

         int var5;
         if (var3 instanceof TLRPC.TL_upload_fileCdnRedirect) {
            TLRPC.TL_upload_fileCdnRedirect var6 = (TLRPC.TL_upload_fileCdnRedirect)var3;
            if (!var6.file_hashes.isEmpty()) {
               if (this.cdnHashes == null) {
                  this.cdnHashes = new SparseArray();
               }

               for(var5 = 0; var5 < var6.file_hashes.size(); ++var5) {
                  TLRPC.TL_fileHash var9 = (TLRPC.TL_fileHash)var6.file_hashes.get(var5);
                  this.cdnHashes.put(var9.offset, var9);
               }
            }

            byte[] var10 = var6.encryption_iv;
            if (var10 != null) {
               byte[] var12 = var6.encryption_key;
               if (var12 != null && var10.length == 16 && var12.length == 32) {
                  this.isCdn = true;
                  if (this.notCheckedCdnRanges == null) {
                     this.notCheckedCdnRanges = new ArrayList();
                     this.notCheckedCdnRanges.add(new FileLoadOperation.Range(0, 12288));
                  }

                  this.cdnDatacenterId = var6.dc_id;
                  this.cdnIv = var6.encryption_iv;
                  this.cdnKey = var6.encryption_key;
                  this.cdnToken = var6.file_token;
                  this.clearOperaion(var1, false);
                  this.startDownloadRequest();
                  return;
               }
            }

            TLRPC.TL_error var7 = new TLRPC.TL_error();
            var7.text = "bad redirect response";
            var7.code = 400;
            this.processRequestResult(var1, var7);
         } else if (var3 instanceof TLRPC.TL_upload_cdnFileReuploadNeeded) {
            if (!this.reuploadingCdn) {
               this.clearOperaion(var1, false);
               this.reuploadingCdn = true;
               TLRPC.TL_upload_cdnFileReuploadNeeded var8 = (TLRPC.TL_upload_cdnFileReuploadNeeded)var3;
               TLRPC.TL_upload_reuploadCdnFile var11 = new TLRPC.TL_upload_reuploadCdnFile();
               var11.file_token = this.cdnToken;
               var11.request_token = var8.request_token;
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$FileLoadOperation$_RANhbuGWnPnpbUnhksdq4cjW2c(this, var1), (QuickAckDelegate)null, (WriteToSocketDelegate)null, 0, this.datacenterId, 1, true);
            }
         } else {
            if (var3 instanceof TLRPC.TL_upload_file) {
               var1.response = (TLRPC.TL_upload_file)var3;
            } else if (var3 instanceof TLRPC.TL_upload_webFile) {
               var1.responseWeb = (TLRPC.TL_upload_webFile)var3;
               if (this.totalBytesCount == 0 && var1.responseWeb.size != 0) {
                  this.totalBytesCount = var1.responseWeb.size;
               }
            } else {
               var1.responseCdn = (TLRPC.TL_upload_cdnFile)var3;
            }

            if (var3 != null) {
               var5 = this.currentType;
               if (var5 == 50331648) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(var3.networkType, 3, (long)(var3.getObjectSize() + 4));
               } else if (var5 == 33554432) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(var3.networkType, 2, (long)(var3.getObjectSize() + 4));
               } else if (var5 == 16777216) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(var3.networkType, 4, (long)(var3.getObjectSize() + 4));
               } else if (var5 == 67108864) {
                  StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(var3.networkType, 5, (long)(var3.getObjectSize() + 4));
               }
            }

            this.processRequestResult(var1, var4);
         }

      }
   }

   protected void onFail(boolean var1, int var2) {
      this.cleanup();
      this.state = 2;
      if (var1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$9spswKeKfAcOLWXDEy_zgCWBTtA(this, var2));
      } else {
         this.delegate.didFailedLoadingFile(this, var2);
      }

   }

   public void pause() {
      if (this.state == 1) {
         this.paused = true;
      }
   }

   protected boolean processRequestResult(FileLoadOperation.RequestInfo var1, TLRPC.TL_error var2) {
      if (this.state != 1) {
         if (BuildVars.DEBUG_VERSION) {
            StringBuilder var66 = new StringBuilder();
            var66.append("trying to write to finished file ");
            var66.append(this.cacheFileFinal);
            var66.append(" offset ");
            var66.append(var1.offset);
            FileLog.d(var66.toString());
         }

         return false;
      } else {
         this.requestInfos.remove(var1);
         TLRPC.TL_fileHash var3 = null;
         StringBuilder var60;
         if (var2 == null) {
            Exception var10000;
            label598: {
               boolean var10001;
               try {
                  if (this.notLoadedBytesRanges == null && this.downloadedBytes != var1.offset) {
                     this.delayRequestInfo(var1);
                     return false;
                  }
               } catch (Exception var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label598;
               }

               NativeByteBuffer var61;
               label568: {
                  try {
                     if (var1.response != null) {
                        var61 = var1.response.bytes;
                        break label568;
                     }
                  } catch (Exception var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label598;
                  }

                  try {
                     if (var1.responseWeb != null) {
                        var61 = var1.responseWeb.bytes;
                        break label568;
                     }
                  } catch (Exception var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label598;
                  }

                  try {
                     if (var1.responseCdn != null) {
                        var61 = var1.responseCdn.bytes;
                        break label568;
                     }
                  } catch (Exception var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label598;
                  }

                  var61 = null;
               }

               if (var61 != null) {
                  label593: {
                     try {
                        if (var61.limit() == 0) {
                           break label593;
                        }
                     } catch (Exception var53) {
                        var10000 = var53;
                        var10001 = false;
                        break label598;
                     }

                     int var4;
                     int var5;
                     label526: {
                        label525: {
                           try {
                              var4 = var61.limit();
                              if (!this.isCdn) {
                                 break label526;
                              }

                              var5 = var1.offset / 131072 * 131072;
                              if (this.cdnHashes != null) {
                                 var3 = (TLRPC.TL_fileHash)this.cdnHashes.get(var5);
                                 break label525;
                              }
                           } catch (Exception var52) {
                              var10000 = var52;
                              var10001 = false;
                              break label598;
                           }

                           var3 = null;
                        }

                        if (var3 == null) {
                           try {
                              this.delayRequestInfo(var1);
                              this.requestFileOffsets(var5);
                              return true;
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label598;
                           }
                        }
                     }

                     try {
                        if (var1.responseCdn != null) {
                           var5 = var1.offset / 16;
                           this.cdnIv[15] = (byte)((byte)(var5 & 255));
                           this.cdnIv[14] = (byte)((byte)(var5 >> 8 & 255));
                           this.cdnIv[13] = (byte)((byte)(var5 >> 16 & 255));
                           this.cdnIv[12] = (byte)((byte)(var5 >> 24 & 255));
                           Utilities.aesCtrDecryption(var61.buffer, this.cdnKey, this.cdnIv, 0, var61.limit());
                        }
                     } catch (Exception var51) {
                        var10000 = var51;
                        var10001 = false;
                        break label598;
                     }

                     boolean var69;
                     label572: {
                        boolean var70;
                        label573: {
                           StringBuilder var67;
                           try {
                              if (this.isPreloadVideoOperation) {
                                 this.preloadStream.writeInt(var1.offset);
                                 this.preloadStream.writeInt(var4);
                                 this.preloadStreamFileOffset += 8;
                                 this.preloadStream.getChannel().write(var61.buffer);
                                 if (BuildVars.DEBUG_VERSION) {
                                    var67 = new StringBuilder();
                                    var67.append("save preload file part ");
                                    var67.append(this.cacheFilePreload);
                                    var67.append(" offset ");
                                    var67.append(var1.offset);
                                    var67.append(" size ");
                                    var67.append(var4);
                                    FileLog.d(var67.toString());
                                 }
                                 break label573;
                              }
                           } catch (Exception var50) {
                              var10000 = var50;
                              var10001 = false;
                              break label598;
                           }

                           label502: {
                              label501: {
                                 label575: {
                                    try {
                                       this.downloadedBytes += var4;
                                       if (this.totalBytesCount > 0) {
                                          if (this.downloadedBytes >= this.totalBytesCount) {
                                             break label501;
                                          }
                                          break label575;
                                       }
                                    } catch (Exception var49) {
                                       var10000 = var49;
                                       var10001 = false;
                                       break label598;
                                    }

                                    try {
                                       if (var4 != this.currentDownloadChunkSize) {
                                          break label501;
                                       }

                                       if (this.totalBytesCount != this.downloadedBytes && this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                                          break label575;
                                       }
                                    } catch (Exception var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label598;
                                    }

                                    try {
                                       if (this.totalBytesCount <= 0 || this.totalBytesCount <= this.downloadedBytes) {
                                          break label501;
                                       }
                                    } catch (Exception var47) {
                                       var10000 = var47;
                                       var10001 = false;
                                       break label598;
                                    }
                                 }

                                 var70 = false;
                                 break label502;
                              }

                              var70 = true;
                           }

                           label477: {
                              try {
                                 if (this.key == null) {
                                    break label477;
                                 }

                                 Utilities.aesIgeEncryption(var61.buffer, this.key, this.iv, false, true, 0, var61.limit());
                              } catch (Exception var46) {
                                 var10000 = var46;
                                 var10001 = false;
                                 break label598;
                              }

                              if (var70) {
                                 try {
                                    if (this.bytesCountPadding != 0) {
                                       var61.limit(var61.limit() - this.bytesCountPadding);
                                    }
                                 } catch (Exception var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break label598;
                                 }
                              }
                           }

                           int var7;
                           try {
                              if (this.encryptFile) {
                                 var7 = var1.offset / 16;
                                 this.encryptIv[15] = (byte)((byte)(var7 & 255));
                                 this.encryptIv[14] = (byte)((byte)(var7 >> 8 & 255));
                                 this.encryptIv[13] = (byte)((byte)(var7 >> 16 & 255));
                                 this.encryptIv[12] = (byte)((byte)(var7 >> 24 & 255));
                                 Utilities.aesCtrDecryption(var61.buffer, this.encryptKey, this.encryptIv, 0, var61.limit());
                              }
                           } catch (Exception var45) {
                              var10000 = var45;
                              var10001 = false;
                              break label598;
                           }

                           try {
                              if (this.notLoadedBytesRanges != null) {
                                 this.fileOutputStream.seek((long)var1.offset);
                                 if (BuildVars.DEBUG_VERSION) {
                                    var67 = new StringBuilder();
                                    var67.append("save file part ");
                                    var67.append(this.cacheFileFinal);
                                    var67.append(" offset ");
                                    var67.append(var1.offset);
                                    FileLog.d(var67.toString());
                                 }
                              }
                           } catch (Exception var25) {
                              var10000 = var25;
                              var10001 = false;
                              break label598;
                           }

                           label577: {
                              label578: {
                                 int var8;
                                 try {
                                    this.fileOutputStream.getChannel().write(var61.buffer);
                                    this.addPart(this.notLoadedBytesRanges, var1.offset, var1.offset + var4, true);
                                    if (!this.isCdn) {
                                       break label578;
                                    }

                                    var7 = var1.offset / 131072;
                                    var8 = this.notCheckedCdnRanges.size();
                                 } catch (Exception var41) {
                                    var10000 = var41;
                                    var10001 = false;
                                    break label598;
                                 }

                                 var4 = 0;

                                 while(true) {
                                    if (var4 >= var8) {
                                       var69 = true;
                                       break;
                                    }

                                    label459: {
                                       try {
                                          FileLoadOperation.Range var58 = (FileLoadOperation.Range)this.notCheckedCdnRanges.get(var4);
                                          if (var58.start <= var7 && var7 <= var58.end) {
                                             break label459;
                                          }
                                       } catch (Exception var44) {
                                          var10000 = var44;
                                          var10001 = false;
                                          break label598;
                                       }

                                       ++var4;
                                       continue;
                                    }

                                    var69 = false;
                                    break;
                                 }

                                 if (!var69) {
                                    var4 = var7 * 131072;

                                    try {
                                       var8 = this.getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, var4, 131072);
                                    } catch (Exception var24) {
                                       var10000 = var24;
                                       var10001 = false;
                                       break label598;
                                    }

                                    if (var8 != 0) {
                                       label596: {
                                          if (var8 != 131072) {
                                             label580: {
                                                try {
                                                   if (this.totalBytesCount > 0 && var8 == this.totalBytesCount - var4) {
                                                      break label580;
                                                   }
                                                } catch (Exception var43) {
                                                   var10000 = var43;
                                                   var10001 = false;
                                                   break label598;
                                                }

                                                try {
                                                   if (this.totalBytesCount > 0) {
                                                      break label596;
                                                   }
                                                } catch (Exception var40) {
                                                   var10000 = var40;
                                                   var10001 = false;
                                                   break label598;
                                                }

                                                if (!var70) {
                                                   break label596;
                                                }
                                             }
                                          }

                                          TLRPC.TL_fileHash var65;
                                          try {
                                             var65 = (TLRPC.TL_fileHash)this.cdnHashes.get(var4);
                                             if (this.fileReadStream == null) {
                                                this.cdnCheckBytes = new byte[131072];
                                                RandomAccessFile var59 = new RandomAccessFile(this.cacheFileTemp, "r");
                                                this.fileReadStream = var59;
                                             }
                                          } catch (Exception var23) {
                                             var10000 = var23;
                                             var10001 = false;
                                             break label598;
                                          }

                                          label599: {
                                             try {
                                                this.fileReadStream.seek((long)var4);
                                                this.fileReadStream.readFully(this.cdnCheckBytes, 0, var8);
                                                if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, var8), var65.hash)) {
                                                   if (BuildVars.LOGS_ENABLED) {
                                                      if (this.location == null) {
                                                         break label599;
                                                      }

                                                      var60 = new StringBuilder();
                                                      var60.append("invalid cdn hash ");
                                                      var60.append(this.location);
                                                      var60.append(" id = ");
                                                      var60.append(this.location.id);
                                                      var60.append(" local_id = ");
                                                      var60.append(this.location.local_id);
                                                      var60.append(" access_hash = ");
                                                      var60.append(this.location.access_hash);
                                                      var60.append(" volume_id = ");
                                                      var60.append(this.location.volume_id);
                                                      var60.append(" secret = ");
                                                      var60.append(this.location.secret);
                                                      FileLog.e(var60.toString());
                                                   }
                                                   break label577;
                                                }
                                             } catch (Exception var42) {
                                                var10000 = var42;
                                                var10001 = false;
                                                break label598;
                                             }

                                             try {
                                                this.cdnHashes.remove(var4);
                                                this.addPart(this.notCheckedCdnRanges, var7, var7 + 1, false);
                                                break label596;
                                             } catch (Exception var22) {
                                                var10000 = var22;
                                                var10001 = false;
                                                break label598;
                                             }
                                          }

                                          try {
                                             if (this.webLocation != null) {
                                                var60 = new StringBuilder();
                                                var60.append("invalid cdn hash  ");
                                                var60.append(this.webLocation);
                                                var60.append(" id = ");
                                                var60.append(this.getFileName());
                                                FileLog.e(var60.toString());
                                             }
                                             break label577;
                                          } catch (Exception var20) {
                                             var10000 = var20;
                                             var10001 = false;
                                             break label598;
                                          }
                                       }
                                    }
                                 }
                              }

                              try {
                                 if (this.fiv != null) {
                                    this.fiv.seek(0L);
                                    this.fiv.write(this.iv);
                                 }
                              } catch (Exception var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break label598;
                              }

                              try {
                                 if (this.totalBytesCount > 0 && this.state == 1) {
                                    this.copyNotLoadedRanges();
                                    this.delegate.didChangedLoadProgress(this, Math.min(1.0F, (float)this.downloadedBytes / (float)this.totalBytesCount));
                                 }
                              } catch (Exception var34) {
                                 var10000 = var34;
                                 var10001 = false;
                                 break label598;
                              }

                              var69 = var70;
                              break label572;
                           }

                           try {
                              this.onFail(false, 0);
                              this.cacheFileTemp.delete();
                              return false;
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label598;
                           }
                        }

                        SparseArray var68;
                        try {
                           if (this.preloadedBytesRanges == null) {
                              var68 = new SparseArray();
                              this.preloadedBytesRanges = var68;
                           }
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label598;
                        }

                        label582: {
                           try {
                              var68 = this.preloadedBytesRanges;
                              var5 = var1.offset;
                              FileLoadOperation.PreloadRange var6 = new FileLoadOperation.PreloadRange(this.preloadStreamFileOffset, var1.offset, var4);
                              var68.put(var5, var6);
                              this.totalPreloadedBytes += var4;
                              this.preloadStreamFileOffset += var4;
                              if (this.moovFound != 0) {
                                 break label582;
                              }

                              var5 = this.findNextPreloadDownloadOffset(this.nextAtomOffset, var1.offset, var61);
                           } catch (Exception var38) {
                              var10000 = var38;
                              var10001 = false;
                              break label598;
                           }

                           if (var5 < 0) {
                              var5 *= -1;

                              label399: {
                                 label584: {
                                    try {
                                       this.nextPreloadDownloadOffset += this.currentDownloadChunkSize;
                                       if (this.nextPreloadDownloadOffset >= this.totalBytesCount / 2) {
                                          break label584;
                                       }
                                    } catch (Exception var39) {
                                       var10000 = var39;
                                       var10001 = false;
                                       break label598;
                                    }

                                    var4 = 1048576 + var5;

                                    try {
                                       this.foundMoovSize = var4;
                                       this.preloadNotRequestedBytesCount = var4;
                                       this.moovFound = 1;
                                       break label399;
                                    } catch (Exception var32) {
                                       var10000 = var32;
                                       var10001 = false;
                                       break label598;
                                    }
                                 }

                                 try {
                                    this.foundMoovSize = 2097152;
                                    this.preloadNotRequestedBytesCount = 2097152;
                                    this.moovFound = 2;
                                 } catch (Exception var31) {
                                    var10000 = var31;
                                    var10001 = false;
                                    break label598;
                                 }
                              }

                              try {
                                 this.nextPreloadDownloadOffset = -1;
                              } catch (Exception var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break label598;
                              }
                           } else {
                              try {
                                 this.nextPreloadDownloadOffset = var5 / this.currentDownloadChunkSize * this.currentDownloadChunkSize;
                              } catch (Exception var29) {
                                 var10000 = var29;
                                 var10001 = false;
                                 break label598;
                              }
                           }

                           try {
                              this.nextAtomOffset = var5;
                           } catch (Exception var28) {
                              var10000 = var28;
                              var10001 = false;
                              break label598;
                           }
                        }

                        label388: {
                           label387: {
                              label386: {
                                 try {
                                    this.preloadStream.writeInt(this.foundMoovSize);
                                    this.preloadStream.writeInt(this.nextPreloadDownloadOffset);
                                    this.preloadStream.writeInt(this.nextAtomOffset);
                                    this.preloadStreamFileOffset += 12;
                                    if (this.nextPreloadDownloadOffset == 0 || this.moovFound != 0 && this.foundMoovSize < 0) {
                                       break label386;
                                    }
                                 } catch (Exception var37) {
                                    var10000 = var37;
                                    var10001 = false;
                                    break label598;
                                 }

                                 try {
                                    if (this.totalPreloadedBytes <= 2097152 && this.nextPreloadDownloadOffset < this.totalBytesCount) {
                                       break label387;
                                    }
                                 } catch (Exception var36) {
                                    var10000 = var36;
                                    var10001 = false;
                                    break label598;
                                 }
                              }

                              var70 = true;
                              break label388;
                           }

                           var70 = false;
                        }

                        if (var70) {
                           try {
                              this.preloadStream.seek(0L);
                              this.preloadStream.write(1);
                           } catch (Exception var27) {
                              var10000 = var27;
                              var10001 = false;
                              break label598;
                           }

                           var69 = var70;
                        } else {
                           label586: {
                              var69 = var70;

                              try {
                                 if (this.moovFound == 0) {
                                    break label586;
                                 }

                                 this.foundMoovSize -= this.currentDownloadChunkSize;
                              } catch (Exception var35) {
                                 var10000 = var35;
                                 var10001 = false;
                                 break label598;
                              }

                              var69 = var70;
                           }
                        }
                     }

                     var5 = 0;

                     label587: {
                        while(true) {
                           try {
                              if (var5 >= this.delayedRequestInfos.size()) {
                                 break label587;
                              }

                              var1 = (FileLoadOperation.RequestInfo)this.delayedRequestInfos.get(var5);
                              if (this.notLoadedBytesRanges != null || this.downloadedBytes == var1.offset) {
                                 break;
                              }
                           } catch (Exception var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label598;
                           }

                           ++var5;
                        }

                        try {
                           this.delayedRequestInfos.remove(var5);
                           if (this.processRequestResult(var1, (TLRPC.TL_error)null)) {
                              break label587;
                           }

                           if (var1.response != null) {
                              var1.response.disableFree = false;
                              var1.response.freeResources();
                              break label587;
                           }
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label598;
                        }

                        try {
                           if (var1.responseWeb != null) {
                              var1.responseWeb.disableFree = false;
                              var1.responseWeb.freeResources();
                              break label587;
                           }
                        } catch (Exception var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label598;
                        }

                        try {
                           if (var1.responseCdn != null) {
                              var1.responseCdn.disableFree = false;
                              var1.responseCdn.freeResources();
                           }
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label598;
                        }
                     }

                     if (var69) {
                        try {
                           this.onFinishLoadingFile(true);
                           return false;
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label598;
                        }
                     } else {
                        try {
                           this.startDownloadRequest();
                           return false;
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label598;
                        }
                     }
                  }
               }

               try {
                  this.onFinishLoadingFile(true);
                  return false;
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
               }
            }

            Exception var62 = var10000;
            this.onFail(false, 0);
            FileLog.e((Throwable)var62);
         } else if (var2.text.contains("FILE_MIGRATE_")) {
            Scanner var63 = new Scanner(var2.text.replace("FILE_MIGRATE_", ""));
            var63.useDelimiter("");

            Integer var64;
            try {
               var64 = var63.nextInt();
            } catch (Exception var10) {
               var64 = var3;
            }

            if (var64 == null) {
               this.onFail(false, 0);
            } else {
               this.datacenterId = var64;
               this.downloadedBytes = 0;
               this.requestedBytesCount = 0;
               this.startDownloadRequest();
            }
         } else if (var2.text.contains("OFFSET_INVALID")) {
            if (this.downloadedBytes % this.currentDownloadChunkSize == 0) {
               try {
                  this.onFinishLoadingFile(true);
               } catch (Exception var9) {
                  FileLog.e((Throwable)var9);
                  this.onFail(false, 0);
               }
            } else {
               this.onFail(false, 0);
            }
         } else if (var2.text.contains("RETRY_LIMIT")) {
            this.onFail(false, 2);
         } else {
            if (BuildVars.LOGS_ENABLED) {
               if (this.location != null) {
                  var60 = new StringBuilder();
                  var60.append(var2.text);
                  var60.append(" ");
                  var60.append(this.location);
                  var60.append(" id = ");
                  var60.append(this.location.id);
                  var60.append(" local_id = ");
                  var60.append(this.location.local_id);
                  var60.append(" access_hash = ");
                  var60.append(this.location.access_hash);
                  var60.append(" volume_id = ");
                  var60.append(this.location.volume_id);
                  var60.append(" secret = ");
                  var60.append(this.location.secret);
                  FileLog.e(var60.toString());
               } else if (this.webLocation != null) {
                  var60 = new StringBuilder();
                  var60.append(var2.text);
                  var60.append(" ");
                  var60.append(this.webLocation);
                  var60.append(" id = ");
                  var60.append(this.getFileName());
                  FileLog.e(var60.toString());
               }
            }

            this.onFail(false, 0);
         }

         return false;
      }
   }

   protected void removeStreamListener(FileStreamLoadOperation var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$pRXavC_xR0CSUF8u1ykitHqvWcY(this, var1));
   }

   public void setDelegate(FileLoadOperation.FileLoadOperationDelegate var1) {
      this.delegate = var1;
   }

   public void setEncryptFile(boolean var1) {
      this.encryptFile = var1;
      if (this.encryptFile) {
         this.allowDisordererFileSave = false;
      }

   }

   public void setForceRequest(boolean var1) {
      this.isForceRequest = var1;
   }

   public void setIsPreloadVideoOperation(boolean var1) {
      if (this.isPreloadVideoOperation != var1 && (!var1 || this.totalBytesCount > 2097152)) {
         if (!var1 && this.isPreloadVideoOperation) {
            if (this.state == 3) {
               this.isPreloadVideoOperation = var1;
               this.state = 0;
               this.preloadFinished = false;
               this.start();
            } else if (this.state == 1) {
               Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$dCUC8Z2YsvYYD1fE6icyc8raIws(this, var1));
            } else {
               this.isPreloadVideoOperation = var1;
            }
         } else {
            this.isPreloadVideoOperation = var1;
         }
      }

   }

   public void setPaths(int var1, File var2, File var3) {
      this.storePath = var2;
      this.tempPath = var3;
      this.currentAccount = var1;
   }

   public void setPriority(int var1) {
      this.priority = var1;
   }

   public boolean start() {
      return this.start((FileLoadOperationStream)null, 0);
   }

   public boolean start(FileLoadOperationStream var1, int var2) {
      int var3;
      if (this.currentDownloadChunkSize == 0) {
         if (this.totalBytesCount >= 1048576) {
            var3 = 131072;
         } else {
            var3 = 32768;
         }

         this.currentDownloadChunkSize = var3;
         var3 = this.totalBytesCount;
         this.currentMaxDownloadRequests = 4;
      }

      boolean var4;
      if (this.state != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var5 = this.paused;
      this.paused = false;
      if (var1 != null) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$LZcjmIEbWfyJMJubpJYFWtYZ3uM(this, var2, var1, var4));
      } else if (var5 && var4) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$HfxdqGkDOxPIykHiS0VM1dK9UjM(this));
      }

      if (var4) {
         return var5;
      } else if (this.location == null && this.webLocation == null) {
         this.onFail(true, 0);
         return false;
      } else {
         var3 = this.currentDownloadChunkSize;
         this.streamStartOffset = var2 / var3 * var3;
         if (this.allowDisordererFileSave) {
            var2 = this.totalBytesCount;
            if (var2 > 0 && var2 > var3) {
               this.notLoadedBytesRanges = new ArrayList();
               this.notRequestedBytesRanges = new ArrayList();
            }
         }

         String var6;
         String var7;
         String var8;
         String var9;
         String var10;
         long var11;
         StringBuilder var72;
         String var73;
         StringBuilder var76;
         if (this.webLocation != null) {
            label693: {
               var6 = Utilities.MD5(this.webFile.url);
               if (this.encryptFile) {
                  var72 = new StringBuilder();
                  var72.append(var6);
                  var72.append(".temp.enc");
                  var7 = var72.toString();
                  var72 = new StringBuilder();
                  var72.append(var6);
                  var72.append(".");
                  var72.append(this.ext);
                  var72.append(".enc");
                  var73 = var72.toString();
                  if (this.key != null) {
                     StringBuilder var77 = new StringBuilder();
                     var77.append(var6);
                     var77.append(".iv.enc");
                     var6 = var77.toString();
                     break label693;
                  }
               } else {
                  var72 = new StringBuilder();
                  var72.append(var6);
                  var72.append(".temp");
                  var9 = var72.toString();
                  var72 = new StringBuilder();
                  var72.append(var6);
                  var72.append(".");
                  var72.append(this.ext);
                  var8 = var72.toString();
                  var7 = var9;
                  var73 = var8;
                  if (this.key != null) {
                     var72 = new StringBuilder();
                     var72.append(var6);
                     var72.append(".iv");
                     var6 = var72.toString();
                     var7 = var9;
                     var73 = var8;
                     break label693;
                  }
               }

               var6 = null;
            }

            var10 = null;
            var8 = null;
            var9 = var7;
            var7 = var10;
         } else {
            label764: {
               label783: {
                  label777: {
                     TLRPC.InputFileLocation var71 = this.location;
                     var11 = var71.volume_id;
                     if (var11 != 0L && var71.local_id != 0) {
                        var2 = this.datacenterId;
                        if (var2 == Integer.MIN_VALUE || var11 == -2147483648L || var2 == 0) {
                           this.onFail(true, 0);
                           return false;
                        }

                        if (!this.encryptFile) {
                           var72 = new StringBuilder();
                           var72.append(this.location.volume_id);
                           var72.append("_");
                           var72.append(this.location.local_id);
                           var72.append(".temp");
                           var9 = var72.toString();
                           var72 = new StringBuilder();
                           var72.append(this.location.volume_id);
                           var72.append("_");
                           var72.append(this.location.local_id);
                           var72.append(".");
                           var72.append(this.ext);
                           var6 = var72.toString();
                           if (this.key != null) {
                              var72 = new StringBuilder();
                              var72.append(this.location.volume_id);
                              var72.append("_");
                              var72.append(this.location.local_id);
                              var72.append(".iv");
                              var73 = var72.toString();
                           } else {
                              var73 = null;
                           }

                           if (this.notLoadedBytesRanges != null) {
                              var76 = new StringBuilder();
                              var76.append(this.location.volume_id);
                              var76.append("_");
                              var76.append(this.location.local_id);
                              var76.append(".pt");
                              var8 = var76.toString();
                           } else {
                              var8 = null;
                           }

                           var76 = new StringBuilder();
                           var76.append(this.location.volume_id);
                           var76.append("_");
                           var76.append(this.location.local_id);
                           var76.append(".preload");
                           var7 = var76.toString();
                           break label783;
                        }

                        var72 = new StringBuilder();
                        var72.append(this.location.volume_id);
                        var72.append("_");
                        var72.append(this.location.local_id);
                        var72.append(".temp.enc");
                        var8 = var72.toString();
                        var72 = new StringBuilder();
                        var72.append(this.location.volume_id);
                        var72.append("_");
                        var72.append(this.location.local_id);
                        var72.append(".");
                        var72.append(this.ext);
                        var72.append(".enc");
                        var6 = var72.toString();
                        var7 = var8;
                        var73 = var6;
                        if (this.key != null) {
                           var72 = new StringBuilder();
                           var72.append(this.location.volume_id);
                           var72.append("_");
                           var72.append(this.location.local_id);
                           var72.append(".iv.enc");
                           var9 = var72.toString();
                           var7 = var8;
                           var73 = var6;
                           var6 = var9;
                           break label777;
                        }
                     } else {
                        if (this.datacenterId == 0 || this.location.id == 0L) {
                           this.onFail(true, 0);
                           return false;
                        }

                        if (!this.encryptFile) {
                           var72 = new StringBuilder();
                           var72.append(this.datacenterId);
                           var72.append("_");
                           var72.append(this.location.id);
                           var72.append(".temp");
                           var9 = var72.toString();
                           var72 = new StringBuilder();
                           var72.append(this.datacenterId);
                           var72.append("_");
                           var72.append(this.location.id);
                           var72.append(this.ext);
                           var6 = var72.toString();
                           if (this.key != null) {
                              var72 = new StringBuilder();
                              var72.append(this.datacenterId);
                              var72.append("_");
                              var72.append(this.location.id);
                              var72.append(".iv");
                              var73 = var72.toString();
                           } else {
                              var73 = null;
                           }

                           if (this.notLoadedBytesRanges != null) {
                              var76 = new StringBuilder();
                              var76.append(this.datacenterId);
                              var76.append("_");
                              var76.append(this.location.id);
                              var76.append(".pt");
                              var8 = var76.toString();
                           } else {
                              var8 = null;
                           }

                           var76 = new StringBuilder();
                           var76.append(this.datacenterId);
                           var76.append("_");
                           var76.append(this.location.id);
                           var76.append(".preload");
                           var7 = var76.toString();
                           break label783;
                        }

                        var72 = new StringBuilder();
                        var72.append(this.datacenterId);
                        var72.append("_");
                        var72.append(this.location.id);
                        var72.append(".temp.enc");
                        var8 = var72.toString();
                        var72 = new StringBuilder();
                        var72.append(this.datacenterId);
                        var72.append("_");
                        var72.append(this.location.id);
                        var72.append(this.ext);
                        var72.append(".enc");
                        var9 = var72.toString();
                        var7 = var8;
                        var73 = var9;
                        if (this.key != null) {
                           var72 = new StringBuilder();
                           var72.append(this.datacenterId);
                           var72.append("_");
                           var72.append(this.location.id);
                           var72.append(".iv.enc");
                           var6 = var72.toString();
                           var73 = var9;
                           var7 = var8;
                           break label777;
                        }
                     }

                     var6 = null;
                  }

                  var10 = null;
                  var8 = null;
                  var9 = var7;
                  var7 = var10;
                  break label764;
               }

               var10 = var73;
               var73 = var6;
               var6 = var10;
            }
         }

         this.requestInfos = new ArrayList(this.currentMaxDownloadRequests);
         this.delayedRequestInfos = new ArrayList(this.currentMaxDownloadRequests - 1);
         this.state = 1;
         this.cacheFileFinal = new File(this.storePath, var73);
         var5 = this.cacheFileFinal.exists();
         var4 = var5;
         if (var5) {
            var2 = this.totalBytesCount;
            var4 = var5;
            if (var2 != 0) {
               var4 = var5;
               if ((long)var2 != this.cacheFileFinal.length()) {
                  this.cacheFileFinal.delete();
                  var4 = false;
               }
            }
         }

         if (!var4) {
            this.cacheFileTemp = new File(this.tempPath, var9);
            Exception var10000;
            boolean var10001;
            boolean var74;
            Exception var87;
            if (this.encryptFile) {
               label744: {
                  File var78 = FileLoader.getInternalCacheDir();
                  StringBuilder var80 = new StringBuilder();
                  var80.append(var73);
                  var80.append(".key");
                  File var83 = new File(var78, var80.toString());

                  label745: {
                     RandomAccessFile var84;
                     label679: {
                        label746: {
                           try {
                              var84 = new RandomAccessFile(var83, "rws");
                              var11 = var83.length();
                              this.encryptKey = new byte[32];
                              this.encryptIv = new byte[16];
                           } catch (Exception var70) {
                              var10000 = var70;
                              var10001 = false;
                              break label746;
                           }

                           if (var11 > 0L && var11 % 48L == 0L) {
                              label668: {
                                 try {
                                    var84.read(this.encryptKey, 0, 32);
                                    var84.read(this.encryptIv, 0, 16);
                                 } catch (Exception var68) {
                                    var10000 = var68;
                                    var10001 = false;
                                    break label668;
                                 }

                                 var74 = false;
                              }
                           } else {
                              label778: {
                                 try {
                                    Utilities.random.nextBytes(this.encryptKey);
                                    Utilities.random.nextBytes(this.encryptIv);
                                    var84.write(this.encryptKey);
                                    var84.write(this.encryptIv);
                                 } catch (Exception var69) {
                                    var10000 = var69;
                                    var10001 = false;
                                    break label778;
                                 }

                                 var74 = true;
                              }
                           }
                           break label679;
                        }

                        var87 = var10000;
                        var74 = false;
                        break label745;
                     }

                     label662: {
                        try {
                           var84.getChannel().close();
                        } catch (Exception var67) {
                           Exception var88 = var67;

                           try {
                              FileLog.e((Throwable)var88);
                           } catch (Exception var66) {
                              var10000 = var66;
                              var10001 = false;
                              break label662;
                           }
                        }

                        try {
                           var84.close();
                           break label744;
                        } catch (Exception var65) {
                           var10000 = var65;
                           var10001 = false;
                        }
                     }

                     var87 = var10000;
                  }

                  FileLog.e((Throwable)var87);
               }
            } else {
               var74 = false;
            }

            boolean[] var92 = new boolean[]{false};
            int var15;
            int var16;
            int var17;
            if (this.supportsPreloading && var7 != null) {
               this.cacheFilePreload = new File(this.tempPath, var7);
               boolean[] var90 = var92;

               RandomAccessFile var89;
               label650: {
                  label649: {
                     boolean[] var91;
                     label748: {
                        label647: {
                           label749: {
                              try {
                                 var89 = new RandomAccessFile;
                              } catch (Exception var64) {
                                 var10000 = var64;
                                 var10001 = false;
                                 break label749;
                              }

                              var90 = var92;

                              try {
                                 var89.<init>(this.cacheFilePreload, "rws");
                              } catch (Exception var63) {
                                 var10000 = var63;
                                 var10001 = false;
                                 break label749;
                              }

                              var90 = var92;

                              try {
                                 this.preloadStream = var89;
                              } catch (Exception var62) {
                                 var10000 = var62;
                                 var10001 = false;
                                 break label749;
                              }

                              var90 = var92;

                              try {
                                 var11 = this.preloadStream.length();
                              } catch (Exception var61) {
                                 var10000 = var61;
                                 var10001 = false;
                                 break label749;
                              }

                              var90 = var92;

                              try {
                                 this.preloadStreamFileOffset = 1;
                              } catch (Exception var60) {
                                 var10000 = var60;
                                 var10001 = false;
                                 break label749;
                              }

                              var90 = var92;
                              if (var11 - (long)0 <= 1L) {
                                 break label647;
                              }

                              var90 = var92;

                              label625: {
                                 label624: {
                                    try {
                                       if (this.preloadStream.readByte() != 0) {
                                          break label624;
                                       }
                                    } catch (Exception var59) {
                                       var10000 = var59;
                                       var10001 = false;
                                       break label749;
                                    }

                                    var4 = false;
                                    break label625;
                                 }

                                 var4 = true;
                              }

                              var92[0] = var4;
                              var3 = 1;

                              while(true) {
                                 long var13 = (long)var3;
                                 var90 = var92;
                                 if (var13 >= var11) {
                                    break label647;
                                 }

                                 if (var11 - var13 < 4L) {
                                    var90 = var92;
                                    break label647;
                                 }

                                 var90 = var92;

                                 try {
                                    var15 = this.preloadStream.readInt();
                                 } catch (Exception var53) {
                                    var10000 = var53;
                                    var10001 = false;
                                    break;
                                 }

                                 var3 += 4;
                                 var90 = var92;
                                 if (var11 - (long)var3 < 4L) {
                                    break label647;
                                 }

                                 var90 = var92;
                                 if (var15 < 0) {
                                    break label647;
                                 }

                                 var90 = var92;

                                 label751: {
                                    try {
                                       if (var15 > this.totalBytesCount) {
                                          break label751;
                                       }
                                    } catch (Exception var58) {
                                       var10000 = var58;
                                       var10001 = false;
                                       break;
                                    }

                                    var90 = var92;

                                    try {
                                       var16 = this.preloadStream.readInt();
                                    } catch (Exception var52) {
                                       var10000 = var52;
                                       var10001 = false;
                                       break;
                                    }

                                    var3 += 4;
                                    var90 = var92;
                                    if (var11 - (long)var3 < (long)var16) {
                                       break label647;
                                    }

                                    var91 = var92;

                                    label604: {
                                       try {
                                          if (var16 <= this.currentDownloadChunkSize) {
                                             break label604;
                                          }
                                       } catch (Exception var57) {
                                          var10000 = var57;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var90 = var92;
                                       break label647;
                                    }

                                    var91 = var92;

                                    FileLoadOperation.PreloadRange var93;
                                    try {
                                       var93 = new FileLoadOperation.PreloadRange;
                                    } catch (Exception var50) {
                                       var10000 = var50;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    try {
                                       var93.<init>(var3, var15, var16, null);
                                    } catch (Exception var49) {
                                       var10000 = var49;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var17 = var3 + var16;
                                    var91 = var92;

                                    RandomAccessFile var79;
                                    try {
                                       var79 = this.preloadStream;
                                    } catch (Exception var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var13 = (long)var17;
                                    var91 = var92;

                                    try {
                                       var79.seek(var13);
                                    } catch (Exception var47) {
                                       var10000 = var47;
                                       var10001 = false;
                                       break label748;
                                    }

                                    if (var11 - var13 < 12L) {
                                       var90 = var92;
                                       break label647;
                                    }

                                    var91 = var92;

                                    try {
                                       this.foundMoovSize = this.preloadStream.readInt();
                                    } catch (Exception var46) {
                                       var10000 = var46;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    label752: {
                                       try {
                                          if (this.foundMoovSize == 0) {
                                             break label752;
                                          }
                                       } catch (Exception var56) {
                                          var10000 = var56;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       byte var75;
                                       label559: {
                                          label558: {
                                             try {
                                                if (this.nextPreloadDownloadOffset > this.totalBytesCount / 2) {
                                                   break label558;
                                                }
                                             } catch (Exception var51) {
                                                var10000 = var51;
                                                var10001 = false;
                                                break label748;
                                             }

                                             var75 = 1;
                                             break label559;
                                          }

                                          var75 = 2;
                                       }

                                       var91 = var92;

                                       try {
                                          this.moovFound = var75;
                                       } catch (Exception var45) {
                                          var10000 = var45;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       try {
                                          this.preloadNotRequestedBytesCount = this.foundMoovSize;
                                       } catch (Exception var44) {
                                          var10000 = var44;
                                          var10001 = false;
                                          break label748;
                                       }
                                    }

                                    var91 = var92;

                                    try {
                                       this.nextPreloadDownloadOffset = this.preloadStream.readInt();
                                    } catch (Exception var43) {
                                       var10000 = var43;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    try {
                                       this.nextAtomOffset = this.preloadStream.readInt();
                                    } catch (Exception var42) {
                                       var10000 = var42;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var3 = var17 + 12;
                                    var91 = var92;

                                    label753: {
                                       try {
                                          if (this.preloadedBytesRanges != null) {
                                             break label753;
                                          }
                                       } catch (Exception var55) {
                                          var10000 = var55;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       SparseArray var82;
                                       try {
                                          var82 = new SparseArray;
                                       } catch (Exception var41) {
                                          var10000 = var41;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       try {
                                          var82.<init>();
                                       } catch (Exception var40) {
                                          var10000 = var40;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       try {
                                          this.preloadedBytesRanges = var82;
                                       } catch (Exception var39) {
                                          var10000 = var39;
                                          var10001 = false;
                                          break label748;
                                       }
                                    }

                                    var91 = var92;

                                    label754: {
                                       try {
                                          if (this.requestedPreloadedBytesRanges != null) {
                                             break label754;
                                          }
                                       } catch (Exception var54) {
                                          var10000 = var54;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       SparseIntArray var86;
                                       try {
                                          var86 = new SparseIntArray;
                                       } catch (Exception var38) {
                                          var10000 = var38;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       try {
                                          var86.<init>();
                                       } catch (Exception var37) {
                                          var10000 = var37;
                                          var10001 = false;
                                          break label748;
                                       }

                                       var91 = var92;

                                       try {
                                          this.requestedPreloadedBytesRanges = var86;
                                       } catch (Exception var36) {
                                          var10000 = var36;
                                          var10001 = false;
                                          break label748;
                                       }
                                    }

                                    var91 = var92;

                                    try {
                                       this.preloadedBytesRanges.put(var15, var93);
                                    } catch (Exception var35) {
                                       var10000 = var35;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    try {
                                       this.requestedPreloadedBytesRanges.put(var15, 1);
                                    } catch (Exception var34) {
                                       var10000 = var34;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    try {
                                       this.totalPreloadedBytes += var16;
                                    } catch (Exception var33) {
                                       var10000 = var33;
                                       var10001 = false;
                                       break label748;
                                    }

                                    var91 = var92;

                                    try {
                                       this.preloadStreamFileOffset += var16 + 20;
                                       continue;
                                    } catch (Exception var32) {
                                       var10000 = var32;
                                       var10001 = false;
                                       break label748;
                                    }
                                 }

                                 var90 = var92;
                                 break label647;
                              }
                           }

                           var87 = var10000;
                           break label649;
                        }

                        var91 = var90;

                        try {
                           this.preloadStream.seek((long)this.preloadStreamFileOffset);
                           break label650;
                        } catch (Exception var31) {
                           var10000 = var31;
                           var10001 = false;
                        }
                     }

                     var87 = var10000;
                     var90 = var91;
                  }

                  FileLog.e((Throwable)var87);
               }

               var92 = var90;
               if (!this.isPreloadVideoOperation) {
                  var92 = var90;
                  if (this.preloadedBytesRanges == null) {
                     label768: {
                        this.cacheFilePreload = null;

                        label492: {
                           label755: {
                              try {
                                 var89 = this.preloadStream;
                              } catch (Exception var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break label755;
                              }

                              var92 = var90;
                              if (var89 == null) {
                                 break label768;
                              }

                              try {
                                 this.preloadStream.getChannel().close();
                              } catch (Exception var29) {
                                 var87 = var29;

                                 try {
                                    FileLog.e((Throwable)var87);
                                 } catch (Exception var28) {
                                    var10000 = var28;
                                    var10001 = false;
                                    break label755;
                                 }
                              }

                              try {
                                 this.preloadStream.close();
                                 this.preloadStream = null;
                                 break label492;
                              } catch (Exception var27) {
                                 var10000 = var27;
                                 var10001 = false;
                              }
                           }

                           var87 = var10000;
                           FileLog.e((Throwable)var87);
                           var92 = var90;
                           break label768;
                        }

                        var92 = var90;
                     }
                  }
               }
            }

            RandomAccessFile var94;
            Exception var95;
            ArrayList var96;
            FileLoadOperation.Range var97;
            if (var8 != null) {
               label769: {
                  this.cacheFileParts = new File(this.tempPath, var8);

                  label757: {
                     try {
                        var94 = new RandomAccessFile(this.cacheFileParts, "rws");
                        this.filePartsStream = var94;
                        var11 = this.filePartsStream.length();
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label757;
                     }

                     if (var11 % 8L != 4L) {
                        break label769;
                     }

                     try {
                        var15 = this.filePartsStream.readInt();
                        if ((long)var15 > (var11 - 4L) / 2L) {
                           break label769;
                        }
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label757;
                     }

                     var3 = 0;

                     while(true) {
                        if (var3 >= var15) {
                           break label769;
                        }

                        try {
                           var16 = this.filePartsStream.readInt();
                           var17 = this.filePartsStream.readInt();
                           var96 = this.notLoadedBytesRanges;
                           FileLoadOperation.Range var81 = new FileLoadOperation.Range(var16, var17);
                           var96.add(var81);
                           ArrayList var85 = this.notRequestedBytesRanges;
                           var97 = new FileLoadOperation.Range(var16, var17);
                           var85.add(var97);
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break;
                        }

                        ++var3;
                     }
                  }

                  var95 = var10000;
                  FileLog.e((Throwable)var95);
               }
            }

            if (this.cacheFileTemp.exists()) {
               if (var74) {
                  this.cacheFileTemp.delete();
               } else {
                  var11 = this.cacheFileTemp.length();
                  if (var6 != null && var11 % (long)this.currentDownloadChunkSize != 0L) {
                     this.downloadedBytes = 0;
                     this.requestedBytesCount = 0;
                  } else {
                     var15 = (int)this.cacheFileTemp.length();
                     var3 = this.currentDownloadChunkSize;
                     var3 = var15 / var3 * var3;
                     this.downloadedBytes = var3;
                     this.requestedBytesCount = var3;
                  }

                  var96 = this.notLoadedBytesRanges;
                  if (var96 != null && var96.isEmpty()) {
                     this.notLoadedBytesRanges.add(new FileLoadOperation.Range(this.downloadedBytes, this.totalBytesCount));
                     this.notRequestedBytesRanges.add(new FileLoadOperation.Range(this.downloadedBytes, this.totalBytesCount));
                  }
               }
            } else {
               var96 = this.notLoadedBytesRanges;
               if (var96 != null && var96.isEmpty()) {
                  this.notLoadedBytesRanges.add(new FileLoadOperation.Range(0, this.totalBytesCount));
                  this.notRequestedBytesRanges.add(new FileLoadOperation.Range(0, this.totalBytesCount));
               }
            }

            var96 = this.notLoadedBytesRanges;
            if (var96 != null) {
               this.downloadedBytes = this.totalBytesCount;
               var15 = var96.size();

               for(var3 = 0; var3 < var15; ++var3) {
                  var97 = (FileLoadOperation.Range)this.notLoadedBytesRanges.get(var3);
                  this.downloadedBytes -= var97.end - var97.start;
               }

               this.requestedBytesCount = this.downloadedBytes;
            }

            if (BuildVars.LOGS_ENABLED) {
               if (this.isPreloadVideoOperation) {
                  var76 = new StringBuilder();
                  var76.append("start preloading file to temp = ");
                  var76.append(this.cacheFileTemp);
                  FileLog.d(var76.toString());
               } else {
                  var76 = new StringBuilder();
                  var76.append("start loading file to temp = ");
                  var76.append(this.cacheFileTemp);
                  var76.append(" final = ");
                  var76.append(this.cacheFileFinal);
                  FileLog.d(var76.toString());
               }
            }

            if (var6 != null) {
               label772: {
                  this.cacheIvTemp = new File(this.tempPath, var6);

                  label762: {
                     try {
                        var94 = new RandomAccessFile(this.cacheIvTemp, "rws");
                        this.fiv = var94;
                        if (this.downloadedBytes == 0) {
                           break label772;
                        }
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label762;
                     }

                     if (var74) {
                        break label772;
                     }

                     try {
                        var11 = this.cacheIvTemp.length();
                     } catch (Exception var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label762;
                     }

                     if (var11 > 0L && var11 % 32L == 0L) {
                        try {
                           this.fiv.read(this.iv, 0, 32);
                           break label772;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                        }
                     } else {
                        try {
                           this.downloadedBytes = 0;
                           this.requestedBytesCount = 0;
                           break label772;
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                        }
                     }
                  }

                  var95 = var10000;
                  FileLog.e((Throwable)var95);
                  this.downloadedBytes = 0;
                  this.requestedBytesCount = 0;
               }
            }

            if (!this.isPreloadVideoOperation && this.downloadedBytes != 0 && this.totalBytesCount > 0) {
               this.copyNotLoadedRanges();
               this.delegate.didChangedLoadProgress(this, Math.min(1.0F, (float)this.downloadedBytes / (float)this.totalBytesCount));
            }

            try {
               var94 = new RandomAccessFile(this.cacheFileTemp, "rws");
               this.fileOutputStream = var94;
               if (this.downloadedBytes != 0) {
                  this.fileOutputStream.seek((long)this.downloadedBytes);
               }
            } catch (Exception var19) {
               FileLog.e((Throwable)var19);
            }

            if (this.fileOutputStream == null) {
               this.onFail(true, 0);
               return false;
            }

            this.started = true;
            Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$zWcnXiI1arbfnF__ZxDNUNllncs(this, var92));
         } else {
            this.started = true;

            try {
               this.onFinishLoadingFile(false);
            } catch (Exception var18) {
               this.onFail(true, 0);
            }
         }

         return true;
      }
   }

   protected void startDownloadRequest() {
      if (!this.paused && this.state == 1 && (this.nextPartWasPreloaded || this.requestInfos.size() + this.delayedRequestInfos.size() < this.currentMaxDownloadRequests) && (!this.isPreloadVideoOperation || this.requestedBytesCount <= 2097152 && (this.moovFound == 0 || this.requestInfos.size() <= 0))) {
         int var1;
         if (!this.nextPartWasPreloaded && (!this.isPreloadVideoOperation || this.moovFound != 0) && this.totalBytesCount > 0) {
            var1 = Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
         } else {
            var1 = 1;
         }

         for(int var2 = 0; var2 < var1; ++var2) {
            int var3;
            int var4;
            int var5;
            int var6;
            int var7;
            ArrayList var8;
            if (this.isPreloadVideoOperation) {
               if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= 0) {
                  return;
               }

               var3 = this.nextPreloadDownloadOffset;
               var4 = var3;
               if (var3 == -1) {
                  var5 = 2097152 / this.currentDownloadChunkSize + 2;
                  var4 = 0;

                  boolean var15;
                  label147: {
                     while(true) {
                        var3 = var4;
                        if (var5 == 0) {
                           break;
                        }

                        if (this.requestedPreloadedBytesRanges.get(var4, 0) == 0) {
                           var15 = true;
                           break label147;
                        }

                        var6 = this.currentDownloadChunkSize;
                        var3 = var4 + var6;
                        var7 = this.totalBytesCount;
                        if (var3 > var7) {
                           break;
                        }

                        var4 = var3;
                        if (this.moovFound == 2) {
                           var4 = var3;
                           if (var3 == var6 * 8) {
                              var4 = (var7 - 1048576) / var6 * var6;
                           }
                        }

                        --var5;
                     }

                     boolean var17 = false;
                     var4 = var3;
                     var15 = var17;
                  }

                  if (!var15 && this.requestInfos.isEmpty()) {
                     this.onFinishLoadingFile(false);
                  }
               }

               if (this.requestedPreloadedBytesRanges == null) {
                  this.requestedPreloadedBytesRanges = new SparseIntArray();
               }

               this.requestedPreloadedBytesRanges.put(var4, 1);
               if (BuildVars.DEBUG_VERSION) {
                  StringBuilder var18 = new StringBuilder();
                  var18.append("start next preload from ");
                  var18.append(var4);
                  var18.append(" size ");
                  var18.append(this.totalBytesCount);
                  var18.append(" for ");
                  var18.append(this.cacheFilePreload);
                  FileLog.d(var18.toString());
               }

               this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
            } else {
               var8 = this.notRequestedBytesRanges;
               if (var8 == null) {
                  var4 = this.requestedBytesCount;
               } else {
                  int var9 = var8.size();
                  var7 = 0;
                  var3 = Integer.MAX_VALUE;
                  var4 = Integer.MAX_VALUE;

                  while(true) {
                     var5 = var3;
                     var6 = var4;
                     if (var7 >= var9) {
                        break;
                     }

                     FileLoadOperation.Range var16 = (FileLoadOperation.Range)this.notRequestedBytesRanges.get(var7);
                     var5 = var3;
                     if (this.streamStartOffset != 0) {
                        if (var16.start <= this.streamStartOffset) {
                           var6 = var16.end;
                           var5 = this.streamStartOffset;
                           if (var6 > var5) {
                              var6 = Integer.MAX_VALUE;
                              break;
                           }
                        }

                        var5 = var3;
                        if (this.streamStartOffset < var16.start) {
                           var5 = var3;
                           if (var16.start < var3) {
                              var5 = var16.start;
                           }
                        }
                     }

                     var4 = Math.min(var4, var16.start);
                     ++var7;
                     var3 = var5;
                  }

                  if (var5 != Integer.MAX_VALUE) {
                     var4 = var5;
                  } else {
                     if (var6 == Integer.MAX_VALUE) {
                        break;
                     }

                     var4 = var6;
                  }
               }
            }

            if (!this.isPreloadVideoOperation) {
               var8 = this.notRequestedBytesRanges;
               if (var8 != null) {
                  this.addPart(var8, var4, this.currentDownloadChunkSize + var4, false);
               }
            }

            var3 = this.totalBytesCount;
            if (var3 > 0 && var4 >= var3) {
               break;
            }

            var3 = this.totalBytesCount;
            boolean var10;
            if (var3 <= 0 || var2 == var1 - 1 || var3 > 0 && this.currentDownloadChunkSize + var4 >= var3) {
               var10 = true;
            } else {
               var10 = false;
            }

            if (this.requestsCount % 2 == 0) {
               var6 = 2;
            } else {
               var6 = 65538;
            }

            byte var19;
            if (this.isForceRequest) {
               var19 = 32;
            } else {
               var19 = 0;
            }

            var3 = var19;
            if (!(this.webLocation instanceof TLRPC.TL_inputWebFileGeoPointLocation)) {
               var3 = var19 | 2;
            }

            Object var20;
            if (this.isCdn) {
               var20 = new TLRPC.TL_upload_getCdnFile();
               ((TLRPC.TL_upload_getCdnFile)var20).file_token = this.cdnToken;
               ((TLRPC.TL_upload_getCdnFile)var20).offset = var4;
               ((TLRPC.TL_upload_getCdnFile)var20).limit = this.currentDownloadChunkSize;
               var3 |= 1;
            } else if (this.webLocation != null) {
               var20 = new TLRPC.TL_upload_getWebFile();
               ((TLRPC.TL_upload_getWebFile)var20).location = this.webLocation;
               ((TLRPC.TL_upload_getWebFile)var20).offset = var4;
               ((TLRPC.TL_upload_getWebFile)var20).limit = this.currentDownloadChunkSize;
            } else {
               var20 = new TLRPC.TL_upload_getFile();
               ((TLRPC.TL_upload_getFile)var20).location = this.location;
               ((TLRPC.TL_upload_getFile)var20).offset = var4;
               ((TLRPC.TL_upload_getFile)var20).limit = this.currentDownloadChunkSize;
            }

            this.requestedBytesCount += this.currentDownloadChunkSize;
            FileLoadOperation.RequestInfo var11 = new FileLoadOperation.RequestInfo();
            this.requestInfos.add(var11);
            var11.offset = var4;
            if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null) {
               SparseArray var12 = this.preloadedBytesRanges;
               if (var12 != null) {
                  FileLoadOperation.PreloadRange var13 = (FileLoadOperation.PreloadRange)var12.get(var11.offset);
                  if (var13 != null) {
                     var11.response = new TLRPC.TL_upload_file();

                     try {
                        NativeByteBuffer var22 = new NativeByteBuffer(var13.length);
                        this.preloadStream.seek((long)var13.fileOffset);
                        this.preloadStream.getChannel().read(var22.buffer);
                        var22.buffer.position(0);
                        var11.response.bytes = var22;
                        DispatchQueue var23 = Utilities.stageQueue;
                        _$$Lambda$FileLoadOperation$CkXyQ6ScRYNY6TMd6qRPWNLnxAA var25 = new _$$Lambda$FileLoadOperation$CkXyQ6ScRYNY6TMd6qRPWNLnxAA(this, var11);
                        var23.postRunnable(var25);
                        continue;
                     } catch (Exception var14) {
                     }
                  }
               }
            }

            ConnectionsManager var21 = ConnectionsManager.getInstance(this.currentAccount);
            _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs var24 = new _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs(this, var11, (TLObject)var20);
            if (this.isCdn) {
               var4 = this.cdnDatacenterId;
            } else {
               var4 = this.datacenterId;
            }

            var11.requestToken = var21.sendRequest((TLObject)var20, var24, (QuickAckDelegate)null, (WriteToSocketDelegate)null, var3, var4, var6, var10);
            ++this.requestsCount;
         }
      }

   }

   public boolean wasStarted() {
      boolean var1;
      if (this.started && !this.paused) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public interface FileLoadOperationDelegate {
      void didChangedLoadProgress(FileLoadOperation var1, float var2);

      void didFailedLoadingFile(FileLoadOperation var1, int var2);

      void didFinishLoadingFile(FileLoadOperation var1, File var2);
   }

   private static class PreloadRange {
      private int fileOffset;
      private int length;
      private int start;

      private PreloadRange(int var1, int var2, int var3) {
         this.fileOffset = var1;
         this.start = var2;
         this.length = var3;
      }

      // $FF: synthetic method
      PreloadRange(int var1, int var2, int var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   public static class Range {
      private int end;
      private int start;

      private Range(int var1, int var2) {
         this.start = var1;
         this.end = var2;
      }

      // $FF: synthetic method
      Range(int var1, int var2, Object var3) {
         this(var1, var2);
      }
   }

   protected static class RequestInfo {
      private int offset;
      private int requestToken;
      private TLRPC.TL_upload_file response;
      private TLRPC.TL_upload_cdnFile responseCdn;
      private TLRPC.TL_upload_webFile responseWeb;
   }
}
