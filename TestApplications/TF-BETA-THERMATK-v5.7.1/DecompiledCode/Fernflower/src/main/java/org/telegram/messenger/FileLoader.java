package org.telegram.messenger;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class FileLoader {
   private static volatile FileLoader[] Instance = new FileLoader[3];
   public static final int MEDIA_DIR_AUDIO = 1;
   public static final int MEDIA_DIR_CACHE = 4;
   public static final int MEDIA_DIR_DOCUMENT = 3;
   public static final int MEDIA_DIR_IMAGE = 0;
   public static final int MEDIA_DIR_VIDEO = 2;
   private static volatile DispatchQueue fileLoaderQueue = new DispatchQueue("fileUploadQueue");
   private static SparseArray mediaDirs = null;
   private ArrayList activeFileLoadOperation = new ArrayList();
   private SparseArray audioLoadOperationQueues = new SparseArray();
   private int currentAccount;
   private SparseIntArray currentAudioLoadOperationsCount = new SparseIntArray();
   private SparseIntArray currentLoadOperationsCount = new SparseIntArray();
   private SparseIntArray currentPhotoLoadOperationsCount = new SparseIntArray();
   private int currentUploadOperationsCount = 0;
   private int currentUploadSmallOperationsCount = 0;
   private FileLoader.FileLoaderDelegate delegate = null;
   private int lastReferenceId;
   private ConcurrentHashMap loadOperationPaths = new ConcurrentHashMap();
   private ConcurrentHashMap loadOperationPathsUI = new ConcurrentHashMap(10, 1.0F, 2);
   private SparseArray loadOperationQueues = new SparseArray();
   private HashMap loadingVideos = new HashMap();
   private ConcurrentHashMap parentObjectReferences = new ConcurrentHashMap();
   private SparseArray photoLoadOperationQueues = new SparseArray();
   private ConcurrentHashMap uploadOperationPaths = new ConcurrentHashMap();
   private ConcurrentHashMap uploadOperationPathsEnc = new ConcurrentHashMap();
   private LinkedList uploadOperationQueue = new LinkedList();
   private HashMap uploadSizes = new HashMap();
   private LinkedList uploadSmallOperationQueue = new LinkedList();

   public FileLoader(int var1) {
      this.currentAccount = var1;
   }

   // $FF: synthetic method
   static DispatchQueue access$300() {
      return fileLoaderQueue;
   }

   // $FF: synthetic method
   static ConcurrentHashMap access$400(FileLoader var0) {
      return var0.uploadOperationPathsEnc;
   }

   // $FF: synthetic method
   static ConcurrentHashMap access$500(FileLoader var0) {
      return var0.uploadOperationPaths;
   }

   // $FF: synthetic method
   static int access$600(FileLoader var0) {
      return var0.currentUploadSmallOperationsCount;
   }

   // $FF: synthetic method
   static int access$608(FileLoader var0) {
      int var1 = var0.currentUploadSmallOperationsCount++;
      return var1;
   }

   // $FF: synthetic method
   static int access$610(FileLoader var0) {
      int var1 = var0.currentUploadSmallOperationsCount--;
      return var1;
   }

   // $FF: synthetic method
   static LinkedList access$700(FileLoader var0) {
      return var0.uploadSmallOperationQueue;
   }

   // $FF: synthetic method
   static int access$800(FileLoader var0) {
      return var0.currentUploadOperationsCount;
   }

   // $FF: synthetic method
   static int access$808(FileLoader var0) {
      int var1 = var0.currentUploadOperationsCount++;
      return var1;
   }

   // $FF: synthetic method
   static int access$810(FileLoader var0) {
      int var1 = var0.currentUploadOperationsCount--;
      return var1;
   }

   // $FF: synthetic method
   static LinkedList access$900(FileLoader var0) {
      return var0.uploadOperationQueue;
   }

   private void addOperationToQueue(FileLoadOperation var1, LinkedList var2) {
      int var3 = var1.getPriority();
      if (var3 > 0) {
         int var4 = var2.size();
         int var5 = 0;
         int var6 = var2.size();

         int var7;
         while(true) {
            var7 = var4;
            if (var5 >= var6) {
               break;
            }

            if (((FileLoadOperation)var2.get(var5)).getPriority() < var3) {
               var7 = var5;
               break;
            }

            ++var5;
         }

         var2.add(var7, var1);
      } else {
         var2.add(var1);
      }

   }

   private void cancelLoadFile(TLRPC.Document var1, SecureDocument var2, WebFile var3, TLRPC.FileLocation var4, String var5) {
      if (var4 != null || var1 != null || var3 != null || var2 != null) {
         if (var4 != null) {
            var5 = getAttachFileName(var4, var5);
         } else if (var1 != null) {
            var5 = getAttachFileName(var1);
         } else if (var2 != null) {
            var5 = getAttachFileName(var2);
         } else if (var3 != null) {
            var5 = getAttachFileName(var3);
         } else {
            var5 = null;
         }

         if (var5 != null) {
            this.loadOperationPathsUI.remove(var5);
            fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$5pIYqCqDZLULELsU2WZGVaPNvhc(this, var5, var1, var3, var2, var4));
         }
      }
   }

   public static File checkDirectory(int var0) {
      return (File)mediaDirs.get(var0);
   }

   private void checkDownloadQueue(int var1, TLRPC.Document var2, WebFile var3, TLRPC.FileLocation var4, String var5) {
      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$zxmFDKJZWcEsmPLtOCvxbxeqvMw(this, var1, var5, var2, var3, var4));
   }

   public static boolean copyFile(InputStream var0, File var1) throws IOException {
      FileOutputStream var4 = new FileOutputStream(var1);
      byte[] var2 = new byte[4096];

      while(true) {
         int var3 = var0.read(var2);
         if (var3 <= 0) {
            var4.close();
            return true;
         }

         Thread.yield();
         var4.write(var2, 0, var3);
      }
   }

   public static String fixFileName(String var0) {
      String var1 = var0;
      if (var0 != null) {
         var1 = var0.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*\u007f]+", "").trim();
      }

      return var1;
   }

   public static String getAttachFileName(TLObject var0) {
      return getAttachFileName(var0, (String)null);
   }

   public static String getAttachFileName(TLObject var0, String var1) {
      boolean var2 = var0 instanceof TLRPC.Document;
      String var3 = "";
      StringBuilder var6;
      if (var2) {
         TLRPC.Document var4 = (TLRPC.Document)var0;
         var1 = getDocumentFileName(var4);
         String var10 = var3;
         if (var1 != null) {
            int var5 = var1.lastIndexOf(46);
            if (var5 == -1) {
               var10 = var3;
            } else {
               var10 = var1.substring(var5);
            }
         }

         var1 = var10;
         if (var10.length() <= 1) {
            var1 = getExtensionByMimeType(var4.mime_type);
         }

         if (var1.length() > 1) {
            var6 = new StringBuilder();
            var6.append(var4.dc_id);
            var6.append("_");
            var6.append(var4.id);
            var6.append(var1);
            return var6.toString();
         } else {
            var6 = new StringBuilder();
            var6.append(var4.dc_id);
            var6.append("_");
            var6.append(var4.id);
            return var6.toString();
         }
      } else {
         StringBuilder var11;
         if (var0 instanceof SecureDocument) {
            SecureDocument var9 = (SecureDocument)var0;
            var11 = new StringBuilder();
            var11.append(var9.secureFile.dc_id);
            var11.append("_");
            var11.append(var9.secureFile.id);
            var11.append(".jpg");
            return var11.toString();
         } else if (var0 instanceof TLRPC.TL_secureFile) {
            TLRPC.TL_secureFile var12 = (TLRPC.TL_secureFile)var0;
            var6 = new StringBuilder();
            var6.append(var12.dc_id);
            var6.append("_");
            var6.append(var12.id);
            var6.append(".jpg");
            return var6.toString();
         } else if (var0 instanceof WebFile) {
            WebFile var8 = (WebFile)var0;
            var11 = new StringBuilder();
            var11.append(Utilities.MD5(var8.url));
            var11.append(".");
            var11.append(ImageLoader.getHttpUrlExtension(var8.url, getMimeTypePart(var8.mime_type)));
            return var11.toString();
         } else if (var0 instanceof TLRPC.PhotoSize) {
            TLRPC.PhotoSize var14 = (TLRPC.PhotoSize)var0;
            TLRPC.FileLocation var7 = var14.location;
            if (var7 != null && !(var7 instanceof TLRPC.TL_fileLocationUnavailable)) {
               var6 = new StringBuilder();
               var6.append(var14.location.volume_id);
               var6.append("_");
               var6.append(var14.location.local_id);
               var6.append(".");
               if (var1 == null) {
                  var1 = "jpg";
               }

               var6.append(var1);
               return var6.toString();
            } else {
               return "";
            }
         } else if (var0 instanceof TLRPC.FileLocation) {
            if (var0 instanceof TLRPC.TL_fileLocationUnavailable) {
               return "";
            } else {
               TLRPC.FileLocation var13 = (TLRPC.FileLocation)var0;
               var6 = new StringBuilder();
               var6.append(var13.volume_id);
               var6.append("_");
               var6.append(var13.local_id);
               var6.append(".");
               if (var1 == null) {
                  var1 = "jpg";
               }

               var6.append(var1);
               return var6.toString();
            }
         } else {
            return "";
         }
      }
   }

   private LinkedList getAudioLoadOperationQueue(int var1) {
      LinkedList var2 = (LinkedList)this.audioLoadOperationQueues.get(var1);
      LinkedList var3 = var2;
      if (var2 == null) {
         var3 = new LinkedList();
         this.audioLoadOperationQueues.put(var1, var3);
      }

      return var3;
   }

   public static TLRPC.PhotoSize getClosestPhotoSizeWithSize(ArrayList var0, int var1) {
      return getClosestPhotoSizeWithSize(var0, var1, false);
   }

   public static TLRPC.PhotoSize getClosestPhotoSizeWithSize(ArrayList var0, int var1, boolean var2) {
      TLRPC.PhotoSize var3 = null;
      TLRPC.PhotoSize var4 = null;
      TLRPC.PhotoSize var5 = var3;
      if (var0 != null) {
         if (var0.isEmpty()) {
            var5 = var3;
         } else {
            int var6 = 0;
            int var7 = 0;

            while(true) {
               var5 = var4;
               if (var6 >= var0.size()) {
                  break;
               }

               var3 = (TLRPC.PhotoSize)var0.get(var6);
               var5 = var4;
               int var8 = var7;
               if (var3 != null) {
                  if (var3 instanceof TLRPC.TL_photoSizeEmpty) {
                     var5 = var4;
                     var8 = var7;
                  } else {
                     label83: {
                        int var10;
                        TLRPC.FileLocation var11;
                        if (var2) {
                           int var9 = var3.h;
                           var8 = var3.w;
                           var10 = var9;
                           if (var9 >= var8) {
                              var10 = var8;
                           }

                           var8 = var10;
                           if (var4 != null) {
                              label67: {
                                 if (var1 > 100) {
                                    var11 = var4.location;
                                    if (var11 != null) {
                                       var8 = var10;
                                       if (var11.dc_id == Integer.MIN_VALUE) {
                                          break label67;
                                       }
                                    }
                                 }

                                 var8 = var10;
                                 if (!(var3 instanceof TLRPC.TL_photoCachedSize)) {
                                    var5 = var4;
                                    var8 = var7;
                                    if (var1 <= var7) {
                                       break label83;
                                    }

                                    var5 = var4;
                                    var8 = var7;
                                    if (var7 >= var10) {
                                       break label83;
                                    }

                                    var8 = var10;
                                 }
                              }
                           }
                        } else {
                           var10 = var3.w;
                           var8 = var3.h;
                           if (var10 < var8) {
                              var10 = var8;
                           }

                           var8 = var10;
                           if (var4 != null) {
                              label60: {
                                 if (var1 > 100) {
                                    var11 = var4.location;
                                    if (var11 != null) {
                                       var8 = var10;
                                       if (var11.dc_id == Integer.MIN_VALUE) {
                                          break label60;
                                       }
                                    }
                                 }

                                 var8 = var10;
                                 if (!(var3 instanceof TLRPC.TL_photoCachedSize)) {
                                    var5 = var4;
                                    var8 = var7;
                                    if (var10 > var1) {
                                       break label83;
                                    }

                                    var5 = var4;
                                    var8 = var7;
                                    if (var7 >= var10) {
                                       break label83;
                                    }

                                    var8 = var10;
                                 }
                              }
                           }
                        }

                        var5 = var3;
                     }
                  }
               }

               ++var6;
               var4 = var5;
               var7 = var8;
            }
         }
      }

      return var5;
   }

   public static File getDirectory(int var0) {
      File var1 = (File)mediaDirs.get(var0);
      File var2 = var1;
      if (var1 == null) {
         var2 = var1;
         if (var0 != 4) {
            var2 = (File)mediaDirs.get(4);
         }
      }

      try {
         if (!var2.isDirectory()) {
            var2.mkdirs();
         }
      } catch (Exception var3) {
      }

      return var2;
   }

   public static String getDocumentExtension(TLRPC.Document var0) {
      String var1 = getDocumentFileName(var0);
      int var2 = var1.lastIndexOf(46);
      String var3;
      if (var2 != -1) {
         var3 = var1.substring(var2 + 1);
      } else {
         var3 = null;
      }

      label19: {
         if (var3 != null) {
            var1 = var3;
            if (var3.length() != 0) {
               break label19;
            }
         }

         var1 = var0.mime_type;
      }

      String var4 = var1;
      if (var1 == null) {
         var4 = "";
      }

      return var4.toUpperCase();
   }

   public static String getDocumentFileName(TLRPC.Document var0) {
      String var1 = null;
      String var2 = null;
      if (var0 != null) {
         var1 = var0.file_name;
         if (var1 == null) {
            int var3 = 0;

            while(true) {
               var1 = var2;
               if (var3 >= var0.attributes.size()) {
                  break;
               }

               TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var0.attributes.get(var3);
               if (var5 instanceof TLRPC.TL_documentAttributeFilename) {
                  var2 = var5.file_name;
               }

               ++var3;
            }
         }
      }

      String var4 = fixFileName(var1);
      if (var4 == null) {
         var4 = "";
      }

      return var4;
   }

   public static String getExtensionByMimeType(String var0) {
      if (var0 != null) {
         byte var1 = -1;
         int var2 = var0.hashCode();
         if (var2 != 187091926) {
            if (var2 != 1331848029) {
               if (var2 == 2039520277 && var0.equals("video/x-matroska")) {
                  var1 = 1;
               }
            } else if (var0.equals("video/mp4")) {
               var1 = 0;
            }
         } else if (var0.equals("audio/ogg")) {
            var1 = 2;
         }

         if (var1 == 0) {
            return ".mp4";
         }

         if (var1 == 1) {
            return ".mkv";
         }

         if (var1 == 2) {
            return ".ogg";
         }
      }

      return "";
   }

   public static String getFileExtension(File var0) {
      String var2 = var0.getName();

      try {
         var2 = var2.substring(var2.lastIndexOf(46) + 1);
         return var2;
      } catch (Exception var1) {
         return "";
      }
   }

   public static FileLoader getInstance(int var0) {
      FileLoader var1 = Instance[var0];
      FileLoader var2 = var1;
      if (var1 == null) {
         synchronized(FileLoader.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               FileLoader[] var23;
               try {
                  var23 = Instance;
                  var2 = new FileLoader(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   public static File getInternalCacheDir() {
      return ApplicationLoader.applicationContext.getCacheDir();
   }

   private LinkedList getLoadOperationQueue(int var1) {
      LinkedList var2 = (LinkedList)this.loadOperationQueues.get(var1);
      LinkedList var3 = var2;
      if (var2 == null) {
         var3 = new LinkedList();
         this.loadOperationQueues.put(var1, var3);
      }

      return var3;
   }

   public static String getMessageFileName(TLRPC.Message var0) {
      if (var0 == null) {
         return "";
      } else {
         ArrayList var4;
         TLRPC.PhotoSize var5;
         if (var0 instanceof TLRPC.TL_messageService) {
            TLRPC.Photo var3 = var0.action.photo;
            if (var3 != null) {
               var4 = var3.sizes;
               if (var4.size() > 0) {
                  var5 = getClosestPhotoSizeWithSize(var4, AndroidUtilities.getPhotoSize());
                  if (var5 != null) {
                     return getAttachFileName(var5);
                  }
               }
            }
         } else {
            TLRPC.MessageMedia var6 = var0.media;
            if (var6 instanceof TLRPC.TL_messageMediaDocument) {
               return getAttachFileName(var6.document);
            }

            if (var6 instanceof TLRPC.TL_messageMediaPhoto) {
               var4 = var6.photo.sizes;
               if (var4.size() > 0) {
                  var5 = getClosestPhotoSizeWithSize(var4, AndroidUtilities.getPhotoSize());
                  if (var5 != null) {
                     return getAttachFileName(var5);
                  }
               }
            } else if (var6 instanceof TLRPC.TL_messageMediaWebPage) {
               TLRPC.WebPage var1 = var6.webpage;
               TLRPC.Document var2 = var1.document;
               if (var2 != null) {
                  return getAttachFileName(var2);
               }

               TLRPC.Photo var7 = var1.photo;
               if (var7 != null) {
                  var4 = var7.sizes;
                  if (var4.size() > 0) {
                     var5 = getClosestPhotoSizeWithSize(var4, AndroidUtilities.getPhotoSize());
                     if (var5 != null) {
                        return getAttachFileName(var5);
                     }
                  }
               } else if (var6 instanceof TLRPC.TL_messageMediaInvoice) {
                  return getAttachFileName(((TLRPC.TL_messageMediaInvoice)var6).photo);
               }
            } else if (var6 instanceof TLRPC.TL_messageMediaInvoice) {
               TLRPC.WebDocument var8 = ((TLRPC.TL_messageMediaInvoice)var6).photo;
               if (var8 != null) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append(Utilities.MD5(var8.url));
                  var9.append(".");
                  var9.append(ImageLoader.getHttpUrlExtension(var8.url, getMimeTypePart(var8.mime_type)));
                  return var9.toString();
               }
            }
         }

         return "";
      }
   }

   public static String getMimeTypePart(String var0) {
      int var1 = var0.lastIndexOf(47);
      return var1 != -1 ? var0.substring(var1 + 1) : "";
   }

   public static File getPathToAttach(TLObject var0) {
      return getPathToAttach(var0, (String)null, false);
   }

   public static File getPathToAttach(TLObject var0, String var1, boolean var2) {
      File var3 = null;
      if (var2) {
         var3 = getDirectory(4);
      } else if (var0 instanceof TLRPC.Document) {
         TLRPC.Document var5 = (TLRPC.Document)var0;
         if (var5.key != null) {
            var3 = getDirectory(4);
         } else if (MessageObject.isVoiceDocument(var5)) {
            var3 = getDirectory(1);
         } else if (MessageObject.isVideoDocument(var5)) {
            var3 = getDirectory(2);
         } else {
            var3 = getDirectory(3);
         }
      } else {
         if (var0 instanceof TLRPC.Photo) {
            return getPathToAttach(getClosestPhotoSizeWithSize(((TLRPC.Photo)var0).sizes, AndroidUtilities.getPhotoSize()), var1, var2);
         }

         TLRPC.FileLocation var6;
         if (var0 instanceof TLRPC.PhotoSize) {
            TLRPC.PhotoSize var4 = (TLRPC.PhotoSize)var0;
            if (!(var4 instanceof TLRPC.TL_photoStrippedSize)) {
               var6 = var4.location;
               if (var6 != null && var6.key == null && (var6.volume_id != -2147483648L || var6.local_id >= 0) && var4.size >= 0) {
                  var3 = getDirectory(0);
               } else {
                  var3 = getDirectory(4);
               }
            }
         } else if (var0 instanceof TLRPC.FileLocation) {
            var6 = (TLRPC.FileLocation)var0;
            if (var6.key != null || var6.volume_id == -2147483648L && var6.local_id < 0) {
               var3 = getDirectory(4);
            } else {
               var3 = getDirectory(0);
            }
         } else if (var0 instanceof WebFile) {
            WebFile var7 = (WebFile)var0;
            if (var7.mime_type.startsWith("image/")) {
               var3 = getDirectory(0);
            } else if (var7.mime_type.startsWith("audio/")) {
               var3 = getDirectory(1);
            } else if (var7.mime_type.startsWith("video/")) {
               var3 = getDirectory(2);
            } else {
               var3 = getDirectory(3);
            }
         } else if (var0 instanceof TLRPC.TL_secureFile || var0 instanceof SecureDocument) {
            var3 = getDirectory(4);
         }
      }

      return var3 == null ? new File("") : new File(var3, getAttachFileName(var0, var1));
   }

   public static File getPathToAttach(TLObject var0, boolean var1) {
      return getPathToAttach(var0, (String)null, var1);
   }

   public static File getPathToMessage(TLRPC.Message var0) {
      if (var0 == null) {
         return new File("");
      } else {
         TLRPC.Photo var5;
         ArrayList var6;
         TLRPC.PhotoSize var7;
         if (var0 instanceof TLRPC.TL_messageService) {
            var5 = var0.action.photo;
            if (var5 != null) {
               var6 = var5.sizes;
               if (var6.size() > 0) {
                  var7 = getClosestPhotoSizeWithSize(var6, AndroidUtilities.getPhotoSize());
                  if (var7 != null) {
                     return getPathToAttach(var7);
                  }
               }
            }
         } else {
            TLRPC.MessageMedia var1 = var0.media;
            boolean var2 = var1 instanceof TLRPC.TL_messageMediaDocument;
            boolean var3 = false;
            boolean var4 = false;
            TLRPC.Document var9;
            if (var2) {
               var9 = var1.document;
               if (var1.ttl_seconds != 0) {
                  var4 = true;
               }

               return getPathToAttach(var9, var4);
            }

            if (var1 instanceof TLRPC.TL_messageMediaPhoto) {
               ArrayList var8 = var1.photo.sizes;
               if (var8.size() > 0) {
                  TLRPC.PhotoSize var10 = getClosestPhotoSizeWithSize(var8, AndroidUtilities.getPhotoSize());
                  if (var10 != null) {
                     var4 = var3;
                     if (var0.media.ttl_seconds != 0) {
                        var4 = true;
                     }

                     return getPathToAttach(var10, var4);
                  }
               }
            } else if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
               TLRPC.WebPage var11 = var1.webpage;
               var9 = var11.document;
               if (var9 != null) {
                  return getPathToAttach(var9);
               }

               var5 = var11.photo;
               if (var5 != null) {
                  var6 = var5.sizes;
                  if (var6.size() > 0) {
                     var7 = getClosestPhotoSizeWithSize(var6, AndroidUtilities.getPhotoSize());
                     if (var7 != null) {
                        return getPathToAttach(var7);
                     }
                  }
               }
            } else if (var1 instanceof TLRPC.TL_messageMediaInvoice) {
               return getPathToAttach(((TLRPC.TL_messageMediaInvoice)var1).photo, true);
            }
         }

         return new File("");
      }
   }

   private LinkedList getPhotoLoadOperationQueue(int var1) {
      LinkedList var2 = (LinkedList)this.photoLoadOperationQueues.get(var1);
      LinkedList var3 = var2;
      if (var2 == null) {
         var3 = new LinkedList();
         this.photoLoadOperationQueues.put(var1, var3);
      }

      return var3;
   }

   public static boolean isVideoMimeType(String var0) {
      boolean var1;
      if ("video/mp4".equals(var0) || SharedConfig.streamMkv && "video/x-matroska".equals(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   static void lambda$deleteFiles$10(ArrayList var0, int var1) {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         File var3 = (File)var0.get(var2);
         StringBuilder var4 = new StringBuilder();
         var4.append(var3.getAbsolutePath());
         var4.append(".enc");
         File var11 = new File(var4.toString());
         File var6;
         if (var11.exists()) {
            try {
               if (!var11.delete()) {
                  var11.deleteOnExit();
               }
            } catch (Exception var8) {
               FileLog.e((Throwable)var8);
            }

            try {
               var6 = getInternalCacheDir();
               var4 = new StringBuilder();
               var4.append(var3.getName());
               var4.append(".enc.key");
               File var5 = new File(var6, var4.toString());
               if (!var5.delete()) {
                  var5.deleteOnExit();
               }
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }
         } else if (var3.exists()) {
            try {
               if (!var3.delete()) {
                  var3.deleteOnExit();
               }
            } catch (Exception var10) {
               FileLog.e((Throwable)var10);
            }
         }

         try {
            var11 = var3.getParentFile();
            StringBuilder var12 = new StringBuilder();
            var12.append("q_");
            var12.append(var3.getName());
            var6 = new File(var11, var12.toString());
            if (var6.exists() && !var6.delete()) {
               var6.deleteOnExit();
            }
         } catch (Exception var9) {
            FileLog.e((Throwable)var9);
         }
      }

      if (var1 == 2) {
         ImageLoader.getInstance().clearMemory();
      }

   }

   private void loadFile(TLRPC.Document var1, SecureDocument var2, WebFile var3, TLRPC.TL_fileLocationToBeDeprecated var4, ImageLocation var5, Object var6, String var7, int var8, int var9, int var10) {
      String var11;
      if (var4 != null) {
         var11 = getAttachFileName(var4, var7);
      } else if (var1 != null) {
         var11 = getAttachFileName(var1);
      } else if (var3 != null) {
         var11 = getAttachFileName(var3);
      } else {
         var11 = null;
      }

      if (var10 != 10 && !TextUtils.isEmpty(var11) && !var11.contains("-2147483648")) {
         this.loadOperationPathsUI.put(var11, true);
      }

      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$QveLJ1Gqcvj_9l_dGSaDY1G3t6s(this, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10));
   }

   private FileLoadOperation loadFileInternal(final TLRPC.Document var1, SecureDocument var2, final WebFile var3, final TLRPC.TL_fileLocationToBeDeprecated var4, ImageLocation var5, Object var6, String var7, int var8, int var9, FileLoadOperationStream var10, int var11, int var12) {
      final String var13;
      if (var4 != null) {
         var13 = getAttachFileName(var4, var7);
      } else if (var2 != null) {
         var13 = getAttachFileName(var2);
      } else if (var1 != null) {
         var13 = getAttachFileName(var1);
      } else if (var3 != null) {
         var13 = getAttachFileName(var3);
      } else {
         var13 = null;
      }

      if (var13 != null && !var13.contains("-2147483648")) {
         if (var12 != 10 && !TextUtils.isEmpty(var13) && !var13.contains("-2147483648")) {
            this.loadOperationPathsUI.put(var13, true);
         }

         FileLoadOperation var14 = (FileLoadOperation)this.loadOperationPaths.get(var13);
         LinkedList var16;
         LinkedList var21;
         LinkedList var22;
         if (var14 != null) {
            if (var12 != 10 && var14.isPreloadVideoOperation()) {
               var14.setIsPreloadVideoOperation(false);
            }

            if (var10 != null || var9 > 0) {
               var9 = var14.getDatacenterId();
               var22 = this.getAudioLoadOperationQueue(var9);
               LinkedList var23 = this.getPhotoLoadOperationQueue(var9);
               var21 = this.getLoadOperationQueue(var9);
               var14.setForceRequest(true);
               if (!MessageObject.isVoiceDocument(var1) && !MessageObject.isVoiceWebDocument(var3)) {
                  if (var2 == null && var4 == null && !MessageObject.isImageWebDocument(var3)) {
                     var16 = var21;
                  } else {
                     var16 = var23;
                  }
               } else {
                  var16 = var22;
               }

               if (var16 != null) {
                  var8 = var16.indexOf(var14);
                  if (var8 >= 0) {
                     var16.remove(var8);
                     if (var10 != null) {
                        SparseIntArray var18;
                        if (var16 == var22) {
                           if (var14.start(var10, var11)) {
                              var18 = this.currentAudioLoadOperationsCount;
                              var18.put(var9, var18.get(var9) + 1);
                           }
                        } else if (var16 == var23) {
                           if (var14.start(var10, var11)) {
                              var18 = this.currentPhotoLoadOperationsCount;
                              var18.put(var9, var18.get(var9) + 1);
                           }
                        } else {
                           if (var14.start(var10, var11)) {
                              var18 = this.currentLoadOperationsCount;
                              var18.put(var9, var18.get(var9) + 1);
                           }

                           if (var14.wasStarted() && !this.activeFileLoadOperation.contains(var14)) {
                              if (var10 != null) {
                                 this.pauseCurrentFileLoadOperations(var14);
                              }

                              this.activeFileLoadOperation.add(var14);
                           }
                        }
                     } else {
                        var16.add(0, var14);
                     }
                  } else {
                     if (var10 != null) {
                        this.pauseCurrentFileLoadOperations(var14);
                     }

                     var14.start(var10, var11);
                     if (var16 == var21 && !this.activeFileLoadOperation.contains(var14)) {
                        this.activeFileLoadOperation.add(var14);
                     }
                  }
               }
            }

            return var14;
         } else {
            File var15;
            FileLoadOperation var17;
            final byte var24;
            label231: {
               label230: {
                  label248: {
                     var15 = getDirectory(4);
                     if (var2 != null) {
                        var17 = new FileLoadOperation(var2);
                     } else {
                        label247: {
                           if (var4 != null) {
                              var17 = new FileLoadOperation(var5, var6, var7, var8);
                           } else {
                              FileLoadOperation var19;
                              if (var1 != null) {
                                 var19 = new FileLoadOperation(var1, var6);
                                 if (MessageObject.isVoiceDocument(var1)) {
                                    var17 = var19;
                                    break label230;
                                 }

                                 var17 = var19;
                                 if (MessageObject.isVideoDocument(var1)) {
                                    var17 = var19;
                                    break label248;
                                 }
                                 break label247;
                              }

                              if (var3 == null) {
                                 var24 = 4;
                                 var17 = var14;
                                 break label231;
                              }

                              var19 = new FileLoadOperation(this.currentAccount, var3);
                              if (MessageObject.isVoiceWebDocument(var3)) {
                                 var17 = var19;
                                 break label230;
                              }

                              if (MessageObject.isVideoWebDocument(var3)) {
                                 var17 = var19;
                                 break label248;
                              }

                              var17 = var19;
                              if (!MessageObject.isImageWebDocument(var3)) {
                                 break label247;
                              }

                              var17 = var19;
                           }

                           var24 = 0;
                           break label231;
                        }
                     }

                     var24 = 3;
                     break label231;
                  }

                  var24 = 2;
                  break label231;
               }

               var24 = 1;
            }

            File var20;
            if (var12 != 0 && var12 != 10) {
               if (var12 == 2) {
                  var17.setEncryptFile(true);
               }

               var20 = var15;
            } else {
               var20 = getDirectory(var24);
            }

            var17.setPaths(this.currentAccount, var20, var15);
            if (var12 == 10) {
               var17.setIsPreloadVideoOperation(true);
            }

            var17.setDelegate(new FileLoadOperation.FileLoadOperationDelegate() {
               public void didChangedLoadProgress(FileLoadOperation var1x, float var2) {
                  if (FileLoader.this.delegate != null) {
                     FileLoader.this.delegate.fileLoadProgressChanged(var13, var2);
                  }

               }

               public void didFailedLoadingFile(FileLoadOperation var1x, int var2) {
                  FileLoader.this.loadOperationPathsUI.remove(var13);
                  FileLoader.this.checkDownloadQueue(var1x.getDatacenterId(), var1, var3, var4, var13);
                  if (FileLoader.this.delegate != null) {
                     FileLoader.this.delegate.fileDidFailedLoad(var13, var2);
                  }

               }

               public void didFinishLoadingFile(FileLoadOperation var1x, File var2) {
                  if (var1x.isPreloadVideoOperation() || !var1x.isPreloadFinished()) {
                     if (!var1x.isPreloadVideoOperation()) {
                        FileLoader.this.loadOperationPathsUI.remove(var13);
                        if (FileLoader.this.delegate != null) {
                           FileLoader.this.delegate.fileDidLoaded(var13, var2, var24);
                        }
                     }

                     FileLoader.this.checkDownloadQueue(var1x.getDatacenterId(), var1, var3, var4, var13);
                  }
               }
            });
            var12 = var17.getDatacenterId();
            var16 = this.getAudioLoadOperationQueue(var12);
            var21 = this.getPhotoLoadOperationQueue(var12);
            var22 = this.getLoadOperationQueue(var12);
            this.loadOperationPaths.put(var13, var17);
            var17.setPriority(var9);
            if (var24 == 1) {
               if (var9 > 0) {
                  var24 = 3;
               } else {
                  var24 = 1;
               }

               var9 = this.currentAudioLoadOperationsCount.get(var12);
               if (var10 == null && var9 >= var24) {
                  this.addOperationToQueue(var17, var16);
               } else if (var17.start(var10, var11)) {
                  this.currentAudioLoadOperationsCount.put(var12, var9 + 1);
               }
            } else if (var4 == null && !MessageObject.isImageWebDocument(var3)) {
               if (var9 > 0) {
                  var24 = 4;
               } else {
                  var24 = 1;
               }

               var9 = this.currentLoadOperationsCount.get(var12);
               if (var10 == null && var9 >= var24) {
                  this.addOperationToQueue(var17, var22);
               } else {
                  if (var17.start(var10, var11)) {
                     this.currentLoadOperationsCount.put(var12, var9 + 1);
                     this.activeFileLoadOperation.add(var17);
                  }

                  if (var17.wasStarted() && var10 != null) {
                     this.pauseCurrentFileLoadOperations(var17);
                  }
               }
            } else {
               if (var9 > 0) {
                  var24 = 6;
               } else {
                  var24 = 2;
               }

               var9 = this.currentPhotoLoadOperationsCount.get(var12);
               if (var10 == null && var9 >= var24) {
                  this.addOperationToQueue(var17, var21);
               } else if (var17.start(var10, var11)) {
                  this.currentPhotoLoadOperationsCount.put(var12, var9 + 1);
               }
            }

            return var17;
         }
      } else {
         return null;
      }
   }

   private void pauseCurrentFileLoadOperations(FileLoadOperation var1) {
      int var4;
      for(int var2 = 0; var2 < this.activeFileLoadOperation.size(); var2 = var4 + 1) {
         FileLoadOperation var3 = (FileLoadOperation)this.activeFileLoadOperation.get(var2);
         var4 = var2;
         if (var3 != var1) {
            if (var3.getDatacenterId() != var1.getDatacenterId()) {
               var4 = var2;
            } else {
               this.activeFileLoadOperation.remove(var3);
               var4 = var2 - 1;
               var2 = var3.getDatacenterId();
               this.getLoadOperationQueue(var2).add(0, var3);
               if (var3.wasStarted()) {
                  SparseIntArray var5 = this.currentLoadOperationsCount;
                  var5.put(var2, var5.get(var2) - 1);
               }

               var3.pause();
            }
         }
      }

   }

   private void removeLoadingVideoInternal(TLRPC.Document var1, boolean var2) {
      String var3 = getAttachFileName(var1);
      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      String var5;
      if (var2) {
         var5 = "p";
      } else {
         var5 = "";
      }

      var4.append(var5);
      var5 = var4.toString();
      if (this.loadingVideos.remove(var5) != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.videoLoadingStateChanged, var3);
      }

   }

   public static void setMediaDirs(SparseArray var0) {
      mediaDirs = var0;
   }

   public void cancelLoadFile(SecureDocument var1) {
      this.cancelLoadFile((TLRPC.Document)null, var1, (WebFile)null, (TLRPC.FileLocation)null, (String)null);
   }

   public void cancelLoadFile(WebFile var1) {
      this.cancelLoadFile((TLRPC.Document)null, (SecureDocument)null, var1, (TLRPC.FileLocation)null, (String)null);
   }

   public void cancelLoadFile(TLRPC.Document var1) {
      this.cancelLoadFile(var1, (SecureDocument)null, (WebFile)null, (TLRPC.FileLocation)null, (String)null);
   }

   public void cancelLoadFile(TLRPC.FileLocation var1, String var2) {
      this.cancelLoadFile((TLRPC.Document)null, (SecureDocument)null, (WebFile)null, var1, var2);
   }

   public void cancelLoadFile(TLRPC.PhotoSize var1) {
      this.cancelLoadFile((TLRPC.Document)null, (SecureDocument)null, (WebFile)null, var1.location, (String)null);
   }

   public void cancelUploadFile(String var1, boolean var2) {
      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$iP93DCpFk_1vNZP_nXjq8znzYAg(this, var2, var1));
   }

   public void checkUploadNewDataAvailable(String var1, boolean var2, long var3, long var5) {
      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$BtLqmhj036PHY9Oj2RFTl8bO4mc(this, var2, var1, var3, var5));
   }

   public void deleteFiles(ArrayList var1, int var2) {
      if (var1 != null && !var1.isEmpty()) {
         fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$4dX6FY75qVi0nYcXFAFSA0OGeOs(var1, var2));
      }

   }

   public float getBufferedProgressFromPosition(float var1, String var2) {
      if (TextUtils.isEmpty(var2)) {
         return 0.0F;
      } else {
         FileLoadOperation var3 = (FileLoadOperation)this.loadOperationPaths.get(var2);
         return var3 != null ? var3.getDownloadedLengthFromOffset(var1) : 0.0F;
      }
   }

   public int getFileReference(Object var1) {
      int var2 = this.lastReferenceId++;
      this.parentObjectReferences.put(var2, var1);
      return var2;
   }

   public Object getParentObject(int var1) {
      return this.parentObjectReferences.get(var1);
   }

   public boolean isLoadingFile(String var1) {
      return this.loadOperationPathsUI.containsKey(var1);
   }

   public boolean isLoadingVideo(TLRPC.Document var1, boolean var2) {
      if (var1 != null) {
         HashMap var3 = this.loadingVideos;
         StringBuilder var4 = new StringBuilder();
         var4.append(getAttachFileName(var1));
         String var5;
         if (var2) {
            var5 = "p";
         } else {
            var5 = "";
         }

         var4.append(var5);
         if (var3.containsKey(var4.toString())) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean isLoadingVideoAny(TLRPC.Document var1) {
      boolean var2 = false;
      if (this.isLoadingVideo(var1, false) || this.isLoadingVideo(var1, true)) {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$cancelLoadFile$6$FileLoader(String var1, TLRPC.Document var2, WebFile var3, SecureDocument var4, TLRPC.FileLocation var5) {
      FileLoadOperation var7 = (FileLoadOperation)this.loadOperationPaths.remove(var1);
      if (var7 != null) {
         int var6 = var7.getDatacenterId();
         SparseIntArray var8;
         if (!MessageObject.isVoiceDocument(var2) && !MessageObject.isVoiceWebDocument(var3)) {
            if (var4 == null && var5 == null && !MessageObject.isImageWebDocument(var3)) {
               if (!this.getLoadOperationQueue(var6).remove(var7)) {
                  var8 = this.currentLoadOperationsCount;
                  var8.put(var6, var8.get(var6) - 1);
               }

               this.activeFileLoadOperation.remove(var7);
            } else if (!this.getPhotoLoadOperationQueue(var6).remove(var7)) {
               var8 = this.currentPhotoLoadOperationsCount;
               var8.put(var6, var8.get(var6) - 1);
            }
         } else if (!this.getAudioLoadOperationQueue(var6).remove(var7)) {
            var8 = this.currentAudioLoadOperationsCount;
            var8.put(var6, var8.get(var6) - 1);
         }

         var7.cancel();
      }

   }

   // $FF: synthetic method
   public void lambda$cancelUploadFile$2$FileLoader(boolean var1, String var2) {
      FileUploadOperation var3;
      if (!var1) {
         var3 = (FileUploadOperation)this.uploadOperationPaths.get(var2);
      } else {
         var3 = (FileUploadOperation)this.uploadOperationPathsEnc.get(var2);
      }

      this.uploadSizes.remove(var2);
      if (var3 != null) {
         this.uploadOperationPathsEnc.remove(var2);
         this.uploadOperationQueue.remove(var3);
         this.uploadSmallOperationQueue.remove(var3);
         var3.cancel();
      }

   }

   // $FF: synthetic method
   public void lambda$checkDownloadQueue$9$FileLoader(int var1, String var2, TLRPC.Document var3, WebFile var4, TLRPC.FileLocation var5) {
      LinkedList var6 = this.getAudioLoadOperationQueue(var1);
      LinkedList var7 = this.getPhotoLoadOperationQueue(var1);
      LinkedList var8 = this.getLoadOperationQueue(var1);
      FileLoadOperation var11 = (FileLoadOperation)this.loadOperationPaths.remove(var2);
      int var9;
      int var10;
      byte var12;
      if (!MessageObject.isVoiceDocument(var3) && !MessageObject.isVoiceWebDocument(var4)) {
         if (var5 != null || MessageObject.isImageWebDocument(var4)) {
            var9 = this.currentPhotoLoadOperationsCount.get(var1);
            var10 = var9;
            if (var11 != null) {
               if (var11.wasStarted()) {
                  var10 = var9 - 1;
                  this.currentPhotoLoadOperationsCount.put(var1, var10);
               } else {
                  var7.remove(var11);
                  var10 = var9;
               }
            }

            while(!var7.isEmpty()) {
               if (((FileLoadOperation)var7.get(0)).getPriority() != 0) {
                  var12 = 6;
               } else {
                  var12 = 2;
               }

               if (var10 >= var12) {
                  break;
               }

               var11 = (FileLoadOperation)var7.poll();
               if (var11 != null && var11.start()) {
                  ++var10;
                  this.currentPhotoLoadOperationsCount.put(var1, var10);
               }
            }
         } else {
            var9 = this.currentLoadOperationsCount.get(var1);
            var10 = var9;
            if (var11 != null) {
               if (var11.wasStarted()) {
                  var10 = var9 - 1;
                  this.currentLoadOperationsCount.put(var1, var10);
               } else {
                  var8.remove(var11);
                  var10 = var9;
               }

               this.activeFileLoadOperation.remove(var11);
            }

            while(!var8.isEmpty()) {
               if (((FileLoadOperation)var8.get(0)).isForceRequest()) {
                  var12 = 3;
               } else {
                  var12 = 1;
               }

               if (var10 >= var12) {
                  break;
               }

               var11 = (FileLoadOperation)var8.poll();
               if (var11 != null && var11.start()) {
                  var9 = var10 + 1;
                  this.currentLoadOperationsCount.put(var1, var9);
                  var10 = var9;
                  if (!this.activeFileLoadOperation.contains(var11)) {
                     this.activeFileLoadOperation.add(var11);
                     var10 = var9;
                  }
               }
            }
         }
      } else {
         var9 = this.currentAudioLoadOperationsCount.get(var1);
         var10 = var9;
         if (var11 != null) {
            if (var11.wasStarted()) {
               var10 = var9 - 1;
               this.currentAudioLoadOperationsCount.put(var1, var10);
            } else {
               var6.remove(var11);
               var10 = var9;
            }
         }

         while(!var6.isEmpty()) {
            if (((FileLoadOperation)var6.get(0)).getPriority() != 0) {
               var12 = 3;
            } else {
               var12 = 1;
            }

            if (var10 >= var12) {
               break;
            }

            var11 = (FileLoadOperation)var6.poll();
            if (var11 != null && var11.start()) {
               ++var10;
               this.currentAudioLoadOperationsCount.put(var1, var10);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$checkUploadNewDataAvailable$3$FileLoader(boolean var1, String var2, long var3, long var5) {
      FileUploadOperation var7;
      if (var1) {
         var7 = (FileUploadOperation)this.uploadOperationPathsEnc.get(var2);
      } else {
         var7 = (FileUploadOperation)this.uploadOperationPaths.get(var2);
      }

      if (var7 != null) {
         var7.checkNewDataAvailable(var3, var5);
      } else if (var5 != 0L) {
         this.uploadSizes.put(var2, var5);
      }

   }

   // $FF: synthetic method
   public void lambda$loadFile$7$FileLoader(TLRPC.Document var1, SecureDocument var2, WebFile var3, TLRPC.TL_fileLocationToBeDeprecated var4, ImageLocation var5, Object var6, String var7, int var8, int var9, int var10) {
      this.loadFileInternal(var1, var2, var3, var4, var5, var6, var7, var8, var9, (FileLoadOperationStream)null, 0, var10);
   }

   // $FF: synthetic method
   public void lambda$loadStreamFile$8$FileLoader(FileLoadOperation[] var1, TLRPC.Document var2, Object var3, FileLoadOperationStream var4, int var5, CountDownLatch var6) {
      var1[0] = this.loadFileInternal(var2, (SecureDocument)null, (WebFile)null, (TLRPC.TL_fileLocationToBeDeprecated)null, (ImageLocation)null, var3, (String)null, 0, 1, var4, var5, 0);
      var6.countDown();
   }

   // $FF: synthetic method
   public void lambda$onNetworkChanged$4$FileLoader(boolean var1) {
      Iterator var2 = this.uploadOperationPaths.entrySet().iterator();

      while(var2.hasNext()) {
         ((FileUploadOperation)((Entry)var2.next()).getValue()).onNetworkChanged(var1);
      }

      var2 = this.uploadOperationPathsEnc.entrySet().iterator();

      while(var2.hasNext()) {
         ((FileUploadOperation)((Entry)var2.next()).getValue()).onNetworkChanged(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$removeLoadingVideo$1$FileLoader(TLRPC.Document var1, boolean var2) {
      this.removeLoadingVideoInternal(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$setLoadingVideo$0$FileLoader(TLRPC.Document var1, boolean var2) {
      this.setLoadingVideoInternal(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$uploadFile$5$FileLoader(boolean var1, String var2, int var3, int var4, boolean var5) {
      if (var1) {
         if (this.uploadOperationPathsEnc.containsKey(var2)) {
            return;
         }
      } else if (this.uploadOperationPaths.containsKey(var2)) {
         return;
      }

      if (var3 != 0 && (Long)this.uploadSizes.get(var2) != null) {
         this.uploadSizes.remove(var2);
         var3 = 0;
      }

      FileUploadOperation var6 = new FileUploadOperation(this.currentAccount, var2, var1, var3, var4);
      if (var1) {
         this.uploadOperationPathsEnc.put(var2, var6);
      } else {
         this.uploadOperationPaths.put(var2, var6);
      }

      var6.setDelegate(new FileLoader$1(this, var1, var2, var5));
      if (var5) {
         var3 = this.currentUploadSmallOperationsCount;
         if (var3 < 1) {
            this.currentUploadSmallOperationsCount = var3 + 1;
            var6.start();
         } else {
            this.uploadSmallOperationQueue.add(var6);
         }
      } else {
         var3 = this.currentUploadOperationsCount;
         if (var3 < 1) {
            this.currentUploadOperationsCount = var3 + 1;
            var6.start();
         } else {
            this.uploadOperationQueue.add(var6);
         }
      }

   }

   public void loadFile(ImageLocation var1, Object var2, String var3, int var4, int var5) {
      if (var1 != null) {
         if (var5 == 0 && (var1.isEncrypted() || var1.photoSize != null && var1.getSize() == 0)) {
            var5 = 1;
         }

         this.loadFile(var1.document, var1.secureDocument, var1.webFile, var1.location, var1, var2, var3, var1.getSize(), var4, var5);
      }
   }

   public void loadFile(SecureDocument var1, int var2) {
      if (var1 != null) {
         this.loadFile((TLRPC.Document)null, var1, (WebFile)null, (TLRPC.TL_fileLocationToBeDeprecated)null, (ImageLocation)null, (Object)null, (String)null, 0, var2, 1);
      }
   }

   public void loadFile(WebFile var1, int var2, int var3) {
      this.loadFile((TLRPC.Document)null, (SecureDocument)null, var1, (TLRPC.TL_fileLocationToBeDeprecated)null, (ImageLocation)null, (Object)null, (String)null, 0, var2, var3);
   }

   public void loadFile(TLRPC.Document var1, Object var2, int var3, int var4) {
      if (var1 != null) {
         if (var4 == 0 && var1.key != null) {
            var4 = 1;
         }

         if (var4 == 2) {
            FileLog.d("test");
         }

         this.loadFile(var1, (SecureDocument)null, (WebFile)null, (TLRPC.TL_fileLocationToBeDeprecated)null, (ImageLocation)null, var2, (String)null, 0, var3, var4);
      }
   }

   protected FileLoadOperation loadStreamFile(FileLoadOperationStream var1, TLRPC.Document var2, Object var3, int var4) {
      CountDownLatch var5 = new CountDownLatch(1);
      FileLoadOperation[] var6 = new FileLoadOperation[1];
      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$DMcEynaB8xpg04Y8QygQNaKjBb4(this, var6, var2, var3, var1, var4, var5));

      try {
         var5.await();
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      }

      return var6[0];
   }

   public void onNetworkChanged(boolean var1) {
      fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$YYo8hp0C6_EIkcQEvJqmJMnz1R4(this, var1));
   }

   public void removeLoadingVideo(TLRPC.Document var1, boolean var2, boolean var3) {
      if (var1 != null) {
         if (var3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$FileLoader$hhzyW9_Gs7B9dgoeAzy05Nfkj9g(this, var1, var2));
         } else {
            this.removeLoadingVideoInternal(var1, var2);
         }

      }
   }

   public void setDelegate(FileLoader.FileLoaderDelegate var1) {
      this.delegate = var1;
   }

   public void setLoadingVideo(TLRPC.Document var1, boolean var2, boolean var3) {
      if (var1 != null) {
         if (var3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$FileLoader$5K2sGZscq7bEKvLM0V2ywbk86iM(this, var1, var2));
         } else {
            this.setLoadingVideoInternal(var1, var2);
         }

      }
   }

   public void setLoadingVideoForPlayer(TLRPC.Document var1, boolean var2) {
      if (var1 != null) {
         String var3 = getAttachFileName(var1);
         HashMap var4 = this.loadingVideos;
         StringBuilder var5 = new StringBuilder();
         var5.append(var3);
         String var6 = "";
         String var7;
         if (var2) {
            var7 = "";
         } else {
            var7 = "p";
         }

         var5.append(var7);
         if (var4.containsKey(var5.toString())) {
            HashMap var9 = this.loadingVideos;
            StringBuilder var8 = new StringBuilder();
            var8.append(var3);
            var7 = var6;
            if (var2) {
               var7 = "p";
            }

            var8.append(var7);
            var9.put(var8.toString(), true);
         }

      }
   }

   public void setLoadingVideoInternal(TLRPC.Document var1, boolean var2) {
      String var3 = getAttachFileName(var1);
      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      String var5;
      if (var2) {
         var5 = "p";
      } else {
         var5 = "";
      }

      var4.append(var5);
      var5 = var4.toString();
      this.loadingVideos.put(var5, true);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.videoLoadingStateChanged, var3);
   }

   public void uploadFile(String var1, boolean var2, boolean var3, int var4) {
      this.uploadFile(var1, var2, var3, 0, var4);
   }

   public void uploadFile(String var1, boolean var2, boolean var3, int var4, int var5) {
      if (var1 != null) {
         fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$VC4JseGAlGdgB_OxKhWFev1MyY0(this, var2, var1, var4, var5, var3));
      }
   }

   public interface FileLoaderDelegate {
      void fileDidFailedLoad(String var1, int var2);

      void fileDidFailedUpload(String var1, boolean var2);

      void fileDidLoaded(String var1, File var2, int var3);

      void fileDidUploaded(String var1, TLRPC.InputFile var2, TLRPC.InputEncryptedFile var3, byte[] var4, byte[] var5, long var6);

      void fileLoadProgressChanged(String var1, float var2);

      void fileUploadProgressChanged(String var1, float var2, boolean var3);
   }
}
