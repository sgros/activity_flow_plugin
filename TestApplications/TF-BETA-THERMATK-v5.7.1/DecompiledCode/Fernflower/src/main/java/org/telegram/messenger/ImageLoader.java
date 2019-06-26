package org.telegram.messenger;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import com.airbnb.lottie.LottieDrawable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AnimatedFileDrawable;

public class ImageLoader {
   public static final String AUTOPLAY_FILTER = "g";
   private static volatile ImageLoader Instance = null;
   private static ThreadLocal bytesLocal = new ThreadLocal();
   private static ThreadLocal bytesThumbLocal = new ThreadLocal();
   private static byte[] header = new byte[12];
   private static byte[] headerThumb = new byte[12];
   private LinkedList artworkTasks = new LinkedList();
   private HashMap bitmapUseCounts = new HashMap();
   private DispatchQueue cacheOutQueue = new DispatchQueue("cacheOutQueue");
   private DispatchQueue cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
   private boolean canForce8888;
   private int currentArtworkTasksCount;
   private int currentHttpFileLoadTasksCount;
   private int currentHttpTasksCount;
   private ConcurrentHashMap fileProgresses = new ConcurrentHashMap();
   private HashMap forceLoadingImages = new HashMap();
   private LinkedList httpFileLoadTasks;
   private HashMap httpFileLoadTasksByKeys;
   private LinkedList httpTasks = new LinkedList();
   private String ignoreRemoval;
   private DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
   private HashMap imageLoadingByKeys = new HashMap();
   private SparseArray imageLoadingByTag = new SparseArray();
   private HashMap imageLoadingByUrl = new HashMap();
   private volatile long lastCacheOutTime;
   private int lastImageNum;
   private long lastProgressUpdateTime;
   private LruCache lottieMemCache;
   private LruCache memCache;
   private HashMap replacedBitmaps = new HashMap();
   private HashMap retryHttpsTasks;
   private File telegramPath;
   private ConcurrentHashMap testWebFile;
   private HashMap thumbGenerateTasks = new HashMap();
   private DispatchQueue thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
   private HashMap waitingForQualityThumb = new HashMap();
   private SparseArray waitingForQualityThumbByTag = new SparseArray();

   public ImageLoader() {
      byte var1 = 0;
      this.currentHttpTasksCount = 0;
      this.currentArtworkTasksCount = 0;
      this.testWebFile = new ConcurrentHashMap();
      this.httpFileLoadTasks = new LinkedList();
      this.httpFileLoadTasksByKeys = new HashMap();
      this.retryHttpsTasks = new HashMap();
      this.currentHttpFileLoadTasksCount = 0;
      this.ignoreRemoval = null;
      this.lastCacheOutTime = 0L;
      this.lastImageNum = 0;
      this.lastProgressUpdateTime = 0L;
      this.telegramPath = null;
      DispatchQueue var2 = this.thumbGeneratingQueue;
      boolean var3 = true;
      var2.setPriority(1);
      int var4 = ((ActivityManager)ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
      if (var4 < 192) {
         var3 = false;
      }

      this.canForce8888 = var3;
      byte var5;
      if (var3) {
         var5 = 30;
      } else {
         var5 = 15;
      }

      this.memCache = new LruCache(Math.min(var5, var4 / 7) * 1024 * 1024) {
         protected void entryRemoved(boolean var1, String var2, BitmapDrawable var3, BitmapDrawable var4) {
            if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(var2)) {
               Integer var5 = (Integer)ImageLoader.this.bitmapUseCounts.get(var2);
               if (var5 == null || var5 == 0) {
                  Bitmap var6 = var3.getBitmap();
                  if (!var6.isRecycled()) {
                     var6.recycle();
                  }
               }

            }
         }

         protected int sizeOf(String var1, BitmapDrawable var2) {
            return var2.getBitmap().getByteCount();
         }
      };
      this.lottieMemCache = new LruCache(5) {
         protected int sizeOf(String var1, LottieDrawable var2) {
            return 1;
         }
      };
      SparseArray var6 = new SparseArray();
      File var11 = AndroidUtilities.getCacheDir();
      if (!var11.isDirectory()) {
         try {
            var11.mkdirs();
         } catch (Exception var10) {
            FileLog.e((Throwable)var10);
         }
      }

      try {
         File var7 = new File(var11, ".nomedia");
         var7.createNewFile();
      } catch (Exception var9) {
         FileLog.e((Throwable)var9);
      }

      var6.put(4, var11);

      for(final int var13 = var1; var13 < 3; ++var13) {
         FileLoader.getInstance(var13).setDelegate(new FileLoader.FileLoaderDelegate() {
            // $FF: synthetic method
            static void lambda$fileLoadProgressChanged$7(int var0, String var1, float var2) {
               NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.FileLoadProgressChanged, var1, var2);
            }

            // $FF: synthetic method
            static void lambda$fileUploadProgressChanged$0(int var0, String var1, float var2, boolean var3) {
               NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.FileUploadProgressChanged, var1, var2, var3);
            }

            // $FF: synthetic method
            static void lambda$null$1(int var0, String var1, TLRPC.InputFile var2, TLRPC.InputEncryptedFile var3, byte[] var4, byte[] var5, long var6) {
               NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.FileDidUpload, var1, var2, var3, var4, var5, var6);
            }

            // $FF: synthetic method
            static void lambda$null$3(int var0, String var1, boolean var2) {
               NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.FileDidFailUpload, var1, var2);
            }

            public void fileDidFailedLoad(String var1, int var2) {
               ImageLoader.this.fileProgresses.remove(var1);
               AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$9ENgl3dEFjSW9fmN2KOTidfJzZ0(this, var1, var2, var13));
            }

            public void fileDidFailedUpload(String var1, boolean var2) {
               Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$3$3M_VSd8r5buZPqJNpilxe2zYwos(this, var13, var1, var2));
            }

            public void fileDidLoaded(String var1, File var2, int var3) {
               ImageLoader.this.fileProgresses.remove(var1);
               AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r_c(this, var2, var1, var13, var3));
            }

            public void fileDidUploaded(String var1, TLRPC.InputFile var2, TLRPC.InputEncryptedFile var3, byte[] var4, byte[] var5, long var6) {
               Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$3$_9uOaZVxDmXjnClpJdro4iTil8Q(this, var13, var1, var2, var3, var4, var5, var6));
            }

            public void fileLoadProgressChanged(String var1, float var2) {
               ImageLoader.this.fileProgresses.put(var1, var2);
               long var3 = System.currentTimeMillis();
               if (ImageLoader.this.lastProgressUpdateTime == 0L || ImageLoader.this.lastProgressUpdateTime < var3 - 500L) {
                  ImageLoader.this.lastProgressUpdateTime = var3;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$iY0R0L0rfhonnXAaHAq36LTxLHA(var13, var1, var2));
               }

            }

            public void fileUploadProgressChanged(String var1, float var2, boolean var3) {
               ImageLoader.this.fileProgresses.put(var1, var2);
               long var4 = System.currentTimeMillis();
               if (ImageLoader.this.lastProgressUpdateTime == 0L || ImageLoader.this.lastProgressUpdateTime < var4 - 500L) {
                  ImageLoader.this.lastProgressUpdateTime = var4;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$FqjDIba7eslh2iQtqmCAn4WCKjU(var13, var1, var2, var3));
               }

            }

            // $FF: synthetic method
            public void lambda$fileDidFailedLoad$6$ImageLoader$3(String var1, int var2, int var3) {
               ImageLoader.this.fileDidFailedLoad(var1, var2);
               NotificationCenter.getInstance(var3).postNotificationName(NotificationCenter.fileDidFailedLoad, var1, var2);
            }

            // $FF: synthetic method
            public void lambda$fileDidFailedUpload$4$ImageLoader$3(int var1, String var2, boolean var3) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$lo1j3M5K6zygAvdIFH_K82igfdE(var1, var2, var3));
               ImageLoader.this.fileProgresses.remove(var2);
            }

            // $FF: synthetic method
            public void lambda$fileDidLoaded$5$ImageLoader$3(File var1, String var2, int var3, int var4) {
               if (SharedConfig.saveToGallery && ImageLoader.this.telegramPath != null && var1 != null && (var2.endsWith(".mp4") || var2.endsWith(".jpg")) && var1.toString().startsWith(ImageLoader.this.telegramPath.toString())) {
                  AndroidUtilities.addMediaToGallery(var1.toString());
               }

               NotificationCenter.getInstance(var3).postNotificationName(NotificationCenter.fileDidLoad, var2);
               ImageLoader.this.fileDidLoaded(var2, var1, var4);
            }

            // $FF: synthetic method
            public void lambda$fileDidUploaded$2$ImageLoader$3(int var1, String var2, TLRPC.InputFile var3, TLRPC.InputEncryptedFile var4, byte[] var5, byte[] var6, long var7) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$O9MuPZ9kD1RfxaVvbxm2MMOXv_E(var1, var2, var3, var4, var5, var6, var7));
               ImageLoader.this.fileProgresses.remove(var2);
            }
         });
      }

      FileLoader.setMediaDirs(var6);
      BroadcastReceiver var12 = new BroadcastReceiver() {
         // $FF: synthetic method
         public void lambda$onReceive$0$ImageLoader$4() {
            ImageLoader.this.checkMediaPaths();
         }

         public void onReceive(Context var1, Intent var2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("file system changed");
            }

            _$$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw var3 = new _$$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw(this);
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(var2.getAction())) {
               AndroidUtilities.runOnUIThread(var3, 1000L);
            } else {
               var3.run();
            }

         }
      };
      IntentFilter var14 = new IntentFilter();
      var14.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
      var14.addAction("android.intent.action.MEDIA_CHECKING");
      var14.addAction("android.intent.action.MEDIA_EJECT");
      var14.addAction("android.intent.action.MEDIA_MOUNTED");
      var14.addAction("android.intent.action.MEDIA_NOFS");
      var14.addAction("android.intent.action.MEDIA_REMOVED");
      var14.addAction("android.intent.action.MEDIA_SHARED");
      var14.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
      var14.addAction("android.intent.action.MEDIA_UNMOUNTED");
      var14.addDataScheme("file");

      try {
         ApplicationLoader.applicationContext.registerReceiver(var12, var14);
      } catch (Throwable var8) {
      }

      this.checkMediaPaths();
   }

   // $FF: synthetic method
   static ThreadLocal access$1600() {
      return bytesLocal;
   }

   // $FF: synthetic method
   static byte[] access$1700() {
      return headerThumb;
   }

   // $FF: synthetic method
   static byte[] access$1800() {
      return header;
   }

   // $FF: synthetic method
   static boolean access$1900(ImageLoader var0) {
      return var0.canForce8888;
   }

   // $FF: synthetic method
   static long access$2000(ImageLoader var0) {
      return var0.lastCacheOutTime;
   }

   // $FF: synthetic method
   static long access$2002(ImageLoader var0, long var1) {
      var0.lastCacheOutTime = var1;
      return var1;
   }

   // $FF: synthetic method
   static ThreadLocal access$2100() {
      return bytesThumbLocal;
   }

   // $FF: synthetic method
   static ConcurrentHashMap access$700(ImageLoader var0) {
      return var0.testWebFile;
   }

   private void artworkLoadError(String var1) {
      this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$aEOKyJRqeuDWeruDQhba5rQv8xo(this, var1));
   }

   private boolean canMoveFiles(File param1, File param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private void createLoadOperationForImageReceiver(ImageReceiver var1, String var2, String var3, String var4, ImageLocation var5, String var6, int var7, int var8, int var9, int var10) {
      if (var1 != null && var3 != null && var2 != null && var5 != null) {
         int var11 = var1.getTag(var9);
         int var12 = var11;
         if (var11 == 0) {
            var11 = this.lastImageNum;
            var1.setTag(var11, var9);
            ++this.lastImageNum;
            var12 = var11;
            if (this.lastImageNum == Integer.MAX_VALUE) {
               this.lastImageNum = 0;
               var12 = var11;
            }
         }

         boolean var13 = var1.isNeedsQualityThumb();
         Object var14 = var1.getParentObject();
         TLRPC.Document var15 = var1.getQulityThumbDocument();
         boolean var16 = var1.isShouldGenerateQualityThumb();
         var11 = var1.getCurrentAccount();
         boolean var17;
         if (var9 == 0 && var1.isCurrentKeyQuality()) {
            var17 = true;
         } else {
            var17 = false;
         }

         this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU(this, var10, var3, var2, var12, var1, var6, var9, var5, var17, var14, var15, var13, var16, var8, var7, var4, var11));
      }

   }

   private void fileDidFailedLoad(String var1, int var2) {
      if (var2 != 1) {
         this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$oYsbNqws1vmTTbWlpv4MDvOVi0o(this, var1));
      }
   }

   private void fileDidLoaded(String var1, File var2, int var3) {
      this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$k74dlCvVooO9jw6g30Qy8tUSaQg(this, var1, var3, var2));
   }

   public static void fillPhotoSizeWithBytes(TLRPC.PhotoSize var0) {
      if (var0 != null) {
         byte[] var1 = var0.bytes;
         if (var1 == null || var1.length == 0) {
            File var2 = FileLoader.getPathToAttach(var0, true);

            try {
               RandomAccessFile var4 = new RandomAccessFile(var2, "r");
               if ((int)var4.length() < 20000) {
                  var0.bytes = new byte[(int)var4.length()];
                  var4.readFully(var0.bytes, 0, var0.bytes.length);
               }
            } catch (Throwable var3) {
               FileLog.e(var3);
            }
         }
      }

   }

   private void generateThumb(int var1, File var2, ImageLoader.ThumbGenerateInfo var3) {
      if ((var1 == 0 || var1 == 2 || var1 == 3) && var2 != null && var3 != null) {
         String var4 = FileLoader.getAttachFileName(var3.parentDocument);
         if ((ImageLoader.ThumbGenerateTask)this.thumbGenerateTasks.get(var4) == null) {
            ImageLoader.ThumbGenerateTask var5 = new ImageLoader.ThumbGenerateTask(var1, var2, var3);
            this.thumbGeneratingQueue.postRunnable(var5);
         }
      }

   }

   public static String getHttpFileName(String var0) {
      return Utilities.MD5(var0);
   }

   public static File getHttpFilePath(String var0, String var1) {
      var1 = getHttpUrlExtension(var0, var1);
      File var2 = FileLoader.getDirectory(4);
      StringBuilder var3 = new StringBuilder();
      var3.append(Utilities.MD5(var0));
      var3.append(".");
      var3.append(var1);
      return new File(var2, var3.toString());
   }

   public static String getHttpUrlExtension(String var0, String var1) {
      String var2 = Uri.parse(var0).getLastPathSegment();
      String var3 = var0;
      if (!TextUtils.isEmpty(var2)) {
         var3 = var0;
         if (var2.length() > 1) {
            var3 = var2;
         }
      }

      int var4 = var3.lastIndexOf(46);
      if (var4 != -1) {
         var0 = var3.substring(var4 + 1);
      } else {
         var0 = null;
      }

      if (var0 != null && var0.length() != 0) {
         var3 = var0;
         if (var0.length() <= 4) {
            return var3;
         }
      }

      var3 = var1;
      return var3;
   }

   public static ImageLoader getInstance() {
      ImageLoader var0 = Instance;
      ImageLoader var1 = var0;
      if (var0 == null) {
         synchronized(ImageLoader.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new ImageLoader();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   private void httpFileLoadError(String var1) {
      this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0(this, var1));
   }

   // $FF: synthetic method
   static void lambda$null$0(SparseArray var0) {
      FileLoader.setMediaDirs(var0);
   }

   public static Bitmap loadBitmap(String param0, Uri param1, float param2, float param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   private void performReplace(String var1, String var2) {
      BitmapDrawable var3 = (BitmapDrawable)this.memCache.get(var1);
      this.replacedBitmaps.put(var1, var2);
      if (var3 != null) {
         BitmapDrawable var4 = (BitmapDrawable)this.memCache.get(var2);
         boolean var5 = false;
         boolean var6 = var5;
         if (var4 != null) {
            var6 = var5;
            if (var4.getBitmap() != null) {
               var6 = var5;
               if (var3.getBitmap() != null) {
                  label37: {
                     Bitmap var7 = var4.getBitmap();
                     Bitmap var9 = var3.getBitmap();
                     if (var7.getWidth() <= var9.getWidth()) {
                        var6 = var5;
                        if (var7.getHeight() <= var9.getHeight()) {
                           break label37;
                        }
                     }

                     var6 = true;
                  }
               }
            }
         }

         if (!var6) {
            this.ignoreRemoval = var1;
            this.memCache.remove(var1);
            this.memCache.put(var2, var3);
            this.ignoreRemoval = null;
         } else {
            this.memCache.remove(var1);
         }
      }

      Integer var8 = (Integer)this.bitmapUseCounts.get(var1);
      if (var8 != null) {
         this.bitmapUseCounts.put(var2, var8);
         this.bitmapUseCounts.remove(var1);
      }

   }

   private void removeFromWaitingForThumb(int var1, ImageReceiver var2) {
      String var3 = (String)this.waitingForQualityThumbByTag.get(var1);
      if (var3 != null) {
         ImageLoader.ThumbGenerateInfo var4 = (ImageLoader.ThumbGenerateInfo)this.waitingForQualityThumb.get(var3);
         if (var4 != null) {
            var4.imageReceiverArray.remove(var2);
            if (var4.imageReceiverArray.isEmpty()) {
               this.waitingForQualityThumb.remove(var3);
            }
         }

         this.waitingForQualityThumbByTag.remove(var1);
      }

   }

   private void replaceImageInCacheInternal(String var1, String var2, ImageLocation var3) {
      ArrayList var4 = this.memCache.getFilterKeys(var1);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.size(); ++var5) {
            String var6 = (String)var4.get(var5);
            StringBuilder var7 = new StringBuilder();
            var7.append(var1);
            var7.append("@");
            var7.append(var6);
            String var9 = var7.toString();
            StringBuilder var8 = new StringBuilder();
            var8.append(var2);
            var8.append("@");
            var8.append(var6);
            var6 = var8.toString();
            this.performReplace(var9, var6);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, var9, var6, var3);
         }
      } else {
         this.performReplace(var1, var2);
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, var1, var2, var3);
      }

   }

   private void runArtworkTasks(boolean var1) {
      if (var1) {
         --this.currentArtworkTasksCount;
      }

      while(this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
         try {
            ((ImageLoader.ArtworkLoadTask)this.artworkTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            ++this.currentArtworkTasksCount;
         } catch (Throwable var3) {
            this.runArtworkTasks(false);
         }
      }

   }

   private void runHttpFileLoadTasks(ImageLoader.HttpFileTask var1, int var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g(this, var1, var2));
   }

   private void runHttpTasks(boolean var1) {
      if (var1) {
         --this.currentHttpTasksCount;
      }

      while(this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
         ImageLoader.HttpImageTask var2 = (ImageLoader.HttpImageTask)this.httpTasks.poll();
         if (var2 != null) {
            var2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            ++this.currentHttpTasksCount;
         }
      }

   }

   public static void saveMessageThumbs(TLRPC.Message var0) {
      TLRPC.MessageMedia var1 = var0.media;
      boolean var2 = var1 instanceof TLRPC.TL_messageMediaPhoto;
      byte var3 = 0;
      byte var4 = 0;
      byte var5 = 0;
      TLRPC.FileLocation var6 = null;
      int var7;
      int var8;
      TLRPC.PhotoSize var9;
      if (var2) {
         var7 = var1.photo.sizes.size();
         var8 = 0;

         while(true) {
            var9 = var6;
            if (var8 >= var7) {
               break;
            }

            var9 = (TLRPC.PhotoSize)var0.media.photo.sizes.get(var8);
            if (var9 instanceof TLRPC.TL_photoCachedSize) {
               break;
            }

            ++var8;
         }
      } else if (var1 instanceof TLRPC.TL_messageMediaDocument) {
         var7 = var1.document.thumbs.size();
         var8 = 0;

         while(true) {
            var9 = var6;
            if (var8 >= var7) {
               break;
            }

            var9 = (TLRPC.PhotoSize)var0.media.document.thumbs.get(var8);
            if (var9 instanceof TLRPC.TL_photoCachedSize) {
               break;
            }

            ++var8;
         }
      } else {
         var9 = var6;
         if (var1 instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.Photo var19 = var1.webpage.photo;
            var9 = var6;
            if (var19 != null) {
               var7 = var19.sizes.size();
               var8 = 0;

               while(true) {
                  var9 = var6;
                  if (var8 >= var7) {
                     break;
                  }

                  var9 = (TLRPC.PhotoSize)var0.media.webpage.photo.sizes.get(var8);
                  if (var9 instanceof TLRPC.TL_photoCachedSize) {
                     break;
                  }

                  ++var8;
               }
            }
         }
      }

      if (var9 != null) {
         byte[] var26 = var9.bytes;
         if (var26 != null && var26.length != 0) {
            var6 = var9.location;
            if (var6 == null || var6 instanceof TLRPC.TL_fileLocationUnavailable) {
               var9.location = new TLRPC.TL_fileLocationToBeDeprecated();
               var6 = var9.location;
               var6.volume_id = -2147483648L;
               var6.local_id = SharedConfig.getLastLocalId();
            }

            boolean var30 = true;
            File var27 = FileLoader.getPathToAttach(var9, true);
            if (MessageObject.shouldEncryptPhotoOrVideo(var0)) {
               StringBuilder var20 = new StringBuilder();
               var20.append(var27.getAbsolutePath());
               var20.append(".enc");
               var27 = new File(var20.toString());
            } else {
               var30 = false;
            }

            if (!var27.exists()) {
               label170: {
                  Exception var10000;
                  label173: {
                     boolean var10001;
                     if (var30) {
                        long var12;
                        byte[] var22;
                        byte[] var31;
                        RandomAccessFile var32;
                        try {
                           File var10 = FileLoader.getInternalCacheDir();
                           StringBuilder var11 = new StringBuilder();
                           var11.append(var27.getName());
                           var11.append(".key");
                           File var21 = new File(var10, var11.toString());
                           var32 = new RandomAccessFile(var21, "rws");
                           var12 = var32.length();
                           var22 = new byte[32];
                           var31 = new byte[16];
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label173;
                        }

                        if (var12 > 0L && var12 % 48L == 0L) {
                           try {
                              var32.read(var22, 0, 32);
                              var32.read(var31, 0, 16);
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label173;
                           }
                        } else {
                           try {
                              Utilities.random.nextBytes(var22);
                              Utilities.random.nextBytes(var31);
                              var32.write(var22);
                              var32.write(var31);
                           } catch (Exception var16) {
                              var10000 = var16;
                              var10001 = false;
                              break label173;
                           }
                        }

                        try {
                           var32.close();
                           Utilities.aesCtrDecryptionByteArray(var9.bytes, var22, var31, 0, var9.bytes.length, 0);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label173;
                        }
                     }

                     try {
                        RandomAccessFile var23 = new RandomAccessFile(var27, "rws");
                        var23.write(var9.bytes);
                        var23.close();
                        break label170;
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                     }
                  }

                  Exception var28 = var10000;
                  FileLog.e((Throwable)var28);
               }
            }

            TLRPC.TL_photoSize var29 = new TLRPC.TL_photoSize();
            var29.w = var9.w;
            var29.h = var9.h;
            var29.location = var9.location;
            var29.size = var9.size;
            var29.type = var9.type;
            TLRPC.MessageMedia var33 = var0.media;
            if (var33 instanceof TLRPC.TL_messageMediaPhoto) {
               int var24 = var33.photo.sizes.size();

               for(var8 = var5; var8 < var24; ++var8) {
                  if ((TLRPC.PhotoSize)var0.media.photo.sizes.get(var8) instanceof TLRPC.TL_photoCachedSize) {
                     var0.media.photo.sizes.set(var8, var29);
                     break;
                  }
               }
            } else {
               int var25;
               if (var33 instanceof TLRPC.TL_messageMediaDocument) {
                  var25 = var33.document.thumbs.size();

                  for(var8 = var3; var8 < var25; ++var8) {
                     if ((TLRPC.PhotoSize)var0.media.document.thumbs.get(var8) instanceof TLRPC.TL_photoCachedSize) {
                        var0.media.document.thumbs.set(var8, var29);
                        break;
                     }
                  }
               } else if (var33 instanceof TLRPC.TL_messageMediaWebPage) {
                  var25 = var33.webpage.photo.sizes.size();

                  for(var8 = var4; var8 < var25; ++var8) {
                     if ((TLRPC.PhotoSize)var0.media.webpage.photo.sizes.get(var8) instanceof TLRPC.TL_photoCachedSize) {
                        var0.media.webpage.photo.sizes.set(var8, var29);
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   public static void saveMessagesThumbs(ArrayList var0) {
      if (var0 != null && !var0.isEmpty()) {
         for(int var1 = 0; var1 < var0.size(); ++var1) {
            saveMessageThumbs((TLRPC.Message)var0.get(var1));
         }
      }

   }

   public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap var0, float var1, float var2, int var3, boolean var4) {
      return scaleAndSaveImage((TLRPC.PhotoSize)null, var0, var1, var2, var3, var4, 0, 0);
   }

   public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap var0, float var1, float var2, int var3, boolean var4, int var5, int var6) {
      return scaleAndSaveImage((TLRPC.PhotoSize)null, var0, var1, var2, var3, var4, var5, var6);
   }

   public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize var0, Bitmap var1, float var2, float var3, int var4, boolean var5) {
      return scaleAndSaveImage(var0, var1, var2, var3, var4, var5, 0, 0);
   }

   public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize var0, Bitmap var1, float var2, float var3, int var4, boolean var5, int var6, int var7) {
      if (var1 == null) {
         return null;
      } else {
         float var8 = (float)var1.getWidth();
         float var9 = (float)var1.getHeight();
         if (var8 != 0.0F && var9 != 0.0F) {
            boolean var10;
            label74: {
               var2 = Math.max(var8 / var2, var9 / var3);
               if (var6 != 0 && var7 != 0) {
                  var3 = (float)var6;
                  if (var8 < var3 || var9 < (float)var7) {
                     if (var8 < var3 && var9 > (float)var7) {
                        var2 = var8 / var3;
                     } else {
                        label76: {
                           if (var8 > var3) {
                              var2 = (float)var7;
                              if (var9 < var2) {
                                 var2 = var9 / var2;
                                 break label76;
                              }
                           }

                           var2 = Math.max(var8 / var3, var9 / (float)var7);
                        }
                     }

                     var10 = true;
                     break label74;
                  }
               }

               var10 = false;
            }

            var7 = (int)(var8 / var2);
            var6 = (int)(var9 / var2);
            if (var6 != 0 && var7 != 0) {
               try {
                  TLRPC.PhotoSize var11 = scaleAndSaveImageInternal(var0, var1, var7, var6, var8, var9, var2, var4, var5, var10);
                  return var11;
               } catch (Throwable var13) {
                  FileLog.e(var13);
                  getInstance().clearMemory();
                  System.gc();

                  try {
                     var0 = scaleAndSaveImageInternal(var0, var1, var7, var6, var8, var9, var2, var4, var5, var10);
                     return var0;
                  } catch (Throwable var12) {
                     FileLog.e(var12);
                  }
               }
            }
         }

         return null;
      }
   }

   private static TLRPC.PhotoSize scaleAndSaveImageInternal(TLRPC.PhotoSize var0, Bitmap var1, int var2, int var3, float var4, float var5, float var6, int var7, boolean var8, boolean var9) throws Exception {
      Bitmap var10;
      if (var6 <= 1.0F && !var9) {
         var10 = var1;
      } else {
         var10 = Bitmaps.createScaledBitmap(var1, var2, var3, true);
      }

      byte var13;
      TLRPC.TL_fileLocationToBeDeprecated var14;
      label68: {
         var13 = 0;
         if (var0 != null) {
            TLRPC.FileLocation var11 = ((TLRPC.PhotoSize)var0).location;
            if (var11 instanceof TLRPC.TL_fileLocationToBeDeprecated) {
               var14 = (TLRPC.TL_fileLocationToBeDeprecated)var11;
               break label68;
            }
         }

         var14 = new TLRPC.TL_fileLocationToBeDeprecated();
         var14.volume_id = -2147483648L;
         var14.dc_id = Integer.MIN_VALUE;
         var14.local_id = SharedConfig.getLastLocalId();
         var14.file_reference = new byte[0];
         var0 = new TLRPC.TL_photoSize();
         ((TLRPC.PhotoSize)var0).location = var14;
         ((TLRPC.PhotoSize)var0).w = var10.getWidth();
         ((TLRPC.PhotoSize)var0).h = var10.getHeight();
         if (((TLRPC.PhotoSize)var0).w <= 100 && ((TLRPC.PhotoSize)var0).h <= 100) {
            ((TLRPC.PhotoSize)var0).type = "s";
         } else if (((TLRPC.PhotoSize)var0).w <= 320 && ((TLRPC.PhotoSize)var0).h <= 320) {
            ((TLRPC.PhotoSize)var0).type = "m";
         } else if (((TLRPC.PhotoSize)var0).w <= 800 && ((TLRPC.PhotoSize)var0).h <= 800) {
            ((TLRPC.PhotoSize)var0).type = "x";
         } else if (((TLRPC.PhotoSize)var0).w <= 1280 && ((TLRPC.PhotoSize)var0).h <= 1280) {
            ((TLRPC.PhotoSize)var0).type = "y";
         } else {
            ((TLRPC.PhotoSize)var0).type = "w";
         }
      }

      StringBuilder var12 = new StringBuilder();
      var12.append(var14.volume_id);
      var12.append("_");
      var12.append(var14.local_id);
      var12.append(".jpg");
      String var15 = var12.toString();
      if (var14.volume_id == -2147483648L) {
         var13 = 4;
      }

      FileOutputStream var17 = new FileOutputStream(new File(FileLoader.getDirectory(var13), var15));
      var10.compress(CompressFormat.JPEG, var7, var17);
      if (var8) {
         ByteArrayOutputStream var16 = new ByteArrayOutputStream();
         var10.compress(CompressFormat.JPEG, var7, var16);
         ((TLRPC.PhotoSize)var0).bytes = var16.toByteArray();
         ((TLRPC.PhotoSize)var0).size = ((TLRPC.PhotoSize)var0).bytes.length;
         var16.close();
      } else {
         ((TLRPC.PhotoSize)var0).size = (int)var17.getChannel().size();
      }

      var17.close();
      if (var10 != var1) {
         var10.recycle();
      }

      return (TLRPC.PhotoSize)var0;
   }

   public static boolean shouldSendImageAsDocument(String var0, Uri var1) {
      Options var2 = new Options();
      boolean var3 = true;
      var2.inJustDecodeBounds = true;
      String var4 = var0;
      if (var0 == null) {
         var4 = var0;
         if (var1 != null) {
            var4 = var0;
            if (var1.getScheme() != null) {
               if (var1.getScheme().contains("file")) {
                  var4 = var1.getPath();
               } else {
                  try {
                     var4 = AndroidUtilities.getPath(var1);
                  } catch (Throwable var9) {
                     FileLog.e(var9);
                     var4 = var0;
                  }
               }
            }
         }
      }

      if (var4 != null) {
         BitmapFactory.decodeFile(var4, var2);
      } else if (var1 != null) {
         try {
            InputStream var10 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(var1);
            BitmapFactory.decodeStream(var10, (Rect)null, var2);
            var10.close();
         } catch (Throwable var8) {
            FileLog.e(var8);
            return false;
         }
      }

      float var5 = (float)var2.outWidth;
      float var6 = (float)var2.outHeight;
      boolean var7 = var3;
      if (var5 / var6 <= 10.0F) {
         if (var6 / var5 > 10.0F) {
            var7 = var3;
         } else {
            var7 = false;
         }
      }

      return var7;
   }

   public void addTestWebFile(String var1, WebFile var2) {
      if (var1 != null && var2 != null) {
         this.testWebFile.put(var1, var2);
      }

   }

   public void cancelForceLoadingForImageReceiver(ImageReceiver var1) {
      if (var1 != null) {
         String var2 = var1.getImageKey();
         if (var2 != null) {
            this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$pOb77_ep1O4qdDkpbIPheznVQgg(this, var2));
         }
      }
   }

   public void cancelLoadHttpFile(String var1) {
      ImageLoader.HttpFileTask var2 = (ImageLoader.HttpFileTask)this.httpFileLoadTasksByKeys.get(var1);
      if (var2 != null) {
         var2.cancel(true);
         this.httpFileLoadTasksByKeys.remove(var1);
         this.httpFileLoadTasks.remove(var2);
      }

      Runnable var3 = (Runnable)this.retryHttpsTasks.get(var1);
      if (var3 != null) {
         AndroidUtilities.cancelRunOnUIThread(var3);
      }

      this.runHttpFileLoadTasks((ImageLoader.HttpFileTask)null, 0);
   }

   public void cancelLoadingForImageReceiver(ImageReceiver var1, boolean var2) {
      if (var1 != null) {
         this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$QQrxxTOTOPgi4Ibzj2dcFh6tMmY(this, var2, var1));
      }
   }

   public void checkMediaPaths() {
      this.cacheOutQueue.postRunnable(new _$$Lambda$ImageLoader$TEcsmbVkFIlJCFa_8B6JxwYMU3A(this));
   }

   public void clearMemory() {
      this.memCache.evictAll();
      this.lottieMemCache.evictAll();
   }

   public SparseArray createMediaPaths() {
      SparseArray var1 = new SparseArray();
      File var2 = AndroidUtilities.getCacheDir();
      if (!var2.isDirectory()) {
         try {
            var2.mkdirs();
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }
      }

      File var3;
      try {
         var3 = new File(var2, ".nomedia");
         var3.createNewFile();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      var1.put(4, var2);
      StringBuilder var21;
      if (BuildVars.LOGS_ENABLED) {
         var21 = new StringBuilder();
         var21.append("cache path = ");
         var21.append(var2);
         FileLog.d(var21.toString());
      }

      Exception var10000;
      Exception var19;
      label118: {
         boolean var10001;
         label117: {
            label116: {
               boolean var4;
               try {
                  if (!"mounted".equals(Environment.getExternalStorageState())) {
                     break label116;
                  }

                  var3 = new File(Environment.getExternalStorageDirectory(), "Telegram");
                  this.telegramPath = var3;
                  this.telegramPath.mkdirs();
                  var4 = this.telegramPath.isDirectory();
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label118;
               }

               if (var4) {
                  File var5;
                  Exception var22;
                  try {
                     var5 = new File(this.telegramPath, "Telegram Images");
                     var5.mkdir();
                     if (var5.isDirectory() && this.canMoveFiles(var2, var5, 0)) {
                        var1.put(0, var5);
                        if (BuildVars.LOGS_ENABLED) {
                           var21 = new StringBuilder();
                           var21.append("image path = ");
                           var21.append(var5);
                           FileLog.d(var21.toString());
                        }
                     }
                  } catch (Exception var17) {
                     var22 = var17;

                     try {
                        FileLog.e((Throwable)var22);
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label118;
                     }
                  }

                  try {
                     var5 = new File(this.telegramPath, "Telegram Video");
                     var5.mkdir();
                     if (var5.isDirectory() && this.canMoveFiles(var2, var5, 2)) {
                        var1.put(2, var5);
                        if (BuildVars.LOGS_ENABLED) {
                           var21 = new StringBuilder();
                           var21.append("video path = ");
                           var21.append(var5);
                           FileLog.d(var21.toString());
                        }
                     }
                  } catch (Exception var15) {
                     var22 = var15;

                     try {
                        FileLog.e((Throwable)var22);
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label118;
                     }
                  }

                  try {
                     var3 = new File(this.telegramPath, "Telegram Audio");
                     var3.mkdir();
                     if (var3.isDirectory() && this.canMoveFiles(var2, var3, 1)) {
                        var5 = new File(var3, ".nomedia");
                        var5.createNewFile();
                        var1.put(1, var3);
                        if (BuildVars.LOGS_ENABLED) {
                           StringBuilder var23 = new StringBuilder();
                           var23.append("audio path = ");
                           var23.append(var3);
                           FileLog.d(var23.toString());
                        }
                     }
                  } catch (Exception var13) {
                     var22 = var13;

                     try {
                        FileLog.e((Throwable)var22);
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label118;
                     }
                  }

                  try {
                     var3 = new File(this.telegramPath, "Telegram Documents");
                     var3.mkdir();
                     if (var3.isDirectory() && this.canMoveFiles(var2, var3, 3)) {
                        var2 = new File(var3, ".nomedia");
                        var2.createNewFile();
                        var1.put(3, var3);
                        if (BuildVars.LOGS_ENABLED) {
                           StringBuilder var20 = new StringBuilder();
                           var20.append("documents path = ");
                           var20.append(var3);
                           FileLog.d(var20.toString());
                        }
                     }
                  } catch (Exception var11) {
                     var19 = var11;

                     try {
                        FileLog.e((Throwable)var19);
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label118;
                     }
                  }
               }
               break label117;
            }

            try {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("this Android can't rename files");
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label118;
            }
         }

         try {
            SharedConfig.checkSaveToGalleryFiles();
            return var1;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      var19 = var10000;
      FileLog.e((Throwable)var19);
      return var1;
   }

   public boolean decrementUseCount(String var1) {
      Integer var2 = (Integer)this.bitmapUseCounts.get(var1);
      if (var2 == null) {
         return true;
      } else if (var2 == 1) {
         this.bitmapUseCounts.remove(var1);
         return true;
      } else {
         this.bitmapUseCounts.put(var1, var2 - 1);
         return false;
      }
   }

   public BitmapDrawable getAnyImageFromMemory(String var1) {
      BitmapDrawable var2 = (BitmapDrawable)this.memCache.get(var1);
      if (var2 == null) {
         ArrayList var3 = this.memCache.getFilterKeys(var1);
         if (var3 != null && !var3.isEmpty()) {
            LruCache var4 = this.memCache;
            StringBuilder var5 = new StringBuilder();
            var5.append(var1);
            var5.append("@");
            var5.append((String)var3.get(0));
            return (BitmapDrawable)var4.get(var5.toString());
         }
      }

      return var2;
   }

   public Float getFileProgress(String var1) {
      return var1 == null ? null : (Float)this.fileProgresses.get(var1);
   }

   public BitmapDrawable getImageFromMemory(TLObject var1, String var2, String var3) {
      Object var4 = null;
      if (var1 == null && var2 == null) {
         return null;
      } else {
         StringBuilder var5;
         if (var2 != null) {
            var2 = Utilities.MD5(var2);
         } else if (var1 instanceof TLRPC.FileLocation) {
            TLRPC.FileLocation var7 = (TLRPC.FileLocation)var1;
            var5 = new StringBuilder();
            var5.append(var7.volume_id);
            var5.append("_");
            var5.append(var7.local_id);
            var2 = var5.toString();
         } else if (var1 instanceof TLRPC.Document) {
            TLRPC.Document var8 = (TLRPC.Document)var1;
            var5 = new StringBuilder();
            var5.append(var8.dc_id);
            var5.append("_");
            var5.append(var8.id);
            var2 = var5.toString();
         } else if (var1 instanceof SecureDocument) {
            SecureDocument var9 = (SecureDocument)var1;
            var5 = new StringBuilder();
            var5.append(var9.secureFile.dc_id);
            var5.append("_");
            var5.append(var9.secureFile.id);
            var2 = var5.toString();
         } else {
            var2 = (String)var4;
            if (var1 instanceof WebFile) {
               var2 = Utilities.MD5(((WebFile)var1).url);
            }
         }

         String var6 = var2;
         if (var3 != null) {
            var5 = new StringBuilder();
            var5.append(var2);
            var5.append("@");
            var5.append(var3);
            var6 = var5.toString();
         }

         return (BitmapDrawable)this.memCache.get(var6);
      }
   }

   public String getReplacedKey(String var1) {
      return var1 == null ? null : (String)this.replacedBitmaps.get(var1);
   }

   public void incrementUseCount(String var1) {
      Integer var2 = (Integer)this.bitmapUseCounts.get(var1);
      if (var2 == null) {
         this.bitmapUseCounts.put(var1, 1);
      } else {
         this.bitmapUseCounts.put(var1, var2 + 1);
      }

   }

   public boolean isInCache(String var1) {
      boolean var2;
      if (this.memCache.get(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isLoadingHttpFile(String var1) {
      return this.httpFileLoadTasksByKeys.containsKey(var1);
   }

   // $FF: synthetic method
   public void lambda$artworkLoadError$7$ImageLoader(String var1) {
      ImageLoader.CacheImage var2 = (ImageLoader.CacheImage)this.imageLoadingByUrl.get(var1);
      if (var2 != null) {
         var2.artworkTask = new ImageLoader.ArtworkLoadTask(var2.artworkTask.cacheImage);
         this.artworkTasks.add(var2.artworkTask);
         this.runArtworkTasks(false);
      }
   }

   // $FF: synthetic method
   public void lambda$cancelForceLoadingForImageReceiver$4$ImageLoader(String var1) {
      Integer var2 = (Integer)this.forceLoadingImages.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$cancelLoadingForImageReceiver$2$ImageLoader(boolean var1, ImageReceiver var2) {
      int var3 = 0;

      while(true) {
         byte var4 = 3;
         if (var3 >= 3) {
            return;
         }

         if (var3 > 0 && !var1) {
            return;
         }

         if (var3 == 0) {
            var4 = 1;
         } else if (var3 == 1) {
            var4 = 0;
         }

         int var6 = var2.getTag(var4);
         if (var6 != 0) {
            if (var3 == 0) {
               this.removeFromWaitingForThumb(var6, var2);
            }

            ImageLoader.CacheImage var5 = (ImageLoader.CacheImage)this.imageLoadingByTag.get(var6);
            if (var5 != null) {
               var5.removeImageReceiver(var2);
            }
         }

         ++var3;
      }
   }

   // $FF: synthetic method
   public void lambda$checkMediaPaths$1$ImageLoader() {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$54eJN0C_gHRDy8W6_JUJzMnPHt0(this.createMediaPaths()));
   }

   // $FF: synthetic method
   public void lambda$createLoadOperationForImageReceiver$5$ImageLoader(int var1, String var2, String var3, int var4, ImageReceiver var5, String var6, int var7, ImageLocation var8, boolean var9, Object var10, TLRPC.Document var11, boolean var12, boolean var13, int var14, int var15, String var16, int var17) {
      TLRPC.Document var19 = var11;
      ImageLoader.CacheImage var20;
      boolean var23;
      boolean var24;
      if (var1 != 2) {
         ImageLoader.CacheImage var21;
         label322: {
            label321: {
               var20 = (ImageLoader.CacheImage)this.imageLoadingByUrl.get(var2);
               var21 = (ImageLoader.CacheImage)this.imageLoadingByKeys.get(var3);
               ImageLoader.CacheImage var22 = (ImageLoader.CacheImage)this.imageLoadingByTag.get(var4);
               if (var22 != null) {
                  if (var22 == var21) {
                     break label321;
                  }

                  if (var22 == var20) {
                     if (var21 == null) {
                        var22.replaceImageReceiver(var5, var3, var6, var7);
                     }
                     break label321;
                  }

                  var22.removeImageReceiver(var5);
               }

               var23 = false;
               break label322;
            }

            var23 = true;
         }

         var24 = var23;
         if (!var23) {
            var24 = var23;
            if (var21 != null) {
               var21.addImageReceiver(var5, var3, var6, var7);
               var24 = true;
            }
         }

         var23 = var24;
         if (!var24) {
            var23 = var24;
            if (var20 != null) {
               var20.addImageReceiver(var5, var3, var6, var7);
               var23 = true;
            }
         }
      } else {
         var23 = false;
      }

      if (!var23) {
         String var41 = var8.path;
         boolean var32;
         File var37;
         File var49;
         if (var41 != null) {
            if (!var41.startsWith("http") && !var41.startsWith("athumb")) {
               label288: {
                  if (var41.startsWith("thumb://")) {
                     var4 = var41.indexOf(":", 8);
                     if (var4 >= 0) {
                        var37 = new File(var41.substring(var4 + 1));
                        break label288;
                     }
                  } else {
                     if (!var41.startsWith("vthumb://")) {
                        var37 = new File(var41);
                        break label288;
                     }

                     var4 = var41.indexOf(":", 9);
                     if (var4 >= 0) {
                        var37 = new File(var41.substring(var4 + 1));
                        break label288;
                     }
                  }

                  var37 = null;
               }

               var32 = true;
            } else {
               var32 = false;
               var37 = null;
            }

            var23 = false;
            var24 = var32;
            var32 = var23;
         } else if (var1 == 0 && var9) {
            boolean var25;
            File var43;
            int var52;
            if (var10 instanceof MessageObject) {
               MessageObject var48 = (MessageObject)var10;
               var19 = var48.getDocument();
               TLRPC.Message var38 = var48.messageOwner;
               var41 = var38.attachPath;
               var37 = FileLoader.getPathToMessage(var38);
               var52 = var48.getFileType();
               var25 = false;
            } else if (var11 != null) {
               var43 = FileLoader.getPathToAttach(var11, true);
               if (MessageObject.isVideoDocument(var11)) {
                  var52 = 2;
               } else {
                  var52 = 3;
               }

               var37 = var43;
               var41 = null;
               var25 = true;
            } else {
               var37 = null;
               var25 = false;
               var19 = null;
               var41 = null;
               var52 = 0;
            }

            if (var19 == null) {
               var32 = false;
               var37 = null;
               var24 = true;
            } else {
               label304: {
                  if (var12) {
                     var49 = FileLoader.getDirectory(4);
                     StringBuilder var45 = new StringBuilder();
                     var45.append("q_");
                     var45.append(var19.dc_id);
                     var45.append("_");
                     var45.append(var19.id);
                     var45.append(".jpg");
                     var49 = new File(var49, var45.toString());
                     if (var49.exists()) {
                        var23 = true;
                        break label304;
                     }
                  }

                  var23 = false;
                  var49 = null;
               }

               File var47;
               label299: {
                  if (!TextUtils.isEmpty(var41)) {
                     var47 = new File(var41);
                     var43 = var47;
                     if (var47.exists()) {
                        break label299;
                     }
                  }

                  var43 = null;
               }

               var47 = var43;
               if (var43 == null) {
                  var47 = var37;
               }

               if (var49 == null) {
                  String var36 = FileLoader.getAttachFileName(var19);
                  ImageLoader.ThumbGenerateInfo var28 = (ImageLoader.ThumbGenerateInfo)this.waitingForQualityThumb.get(var36);
                  ImageLoader.ThumbGenerateInfo var27 = var28;
                  if (var28 == null) {
                     var27 = new ImageLoader.ThumbGenerateInfo();
                     var27.parentDocument = var19;
                     var27.filter = var6;
                     var27.big = var25;
                     this.waitingForQualityThumb.put(var36, var27);
                  }

                  if (!var27.imageReceiverArray.contains(var5)) {
                     var27.imageReceiverArray.add(var5);
                  }

                  this.waitingForQualityThumbByTag.put(var4, var36);
                  if (var47.exists() && var13) {
                     this.generateThumb(var52, var47, var27);
                  }

                  return;
               }

               var32 = var23;
               var24 = true;
               var37 = var49;
            }
         } else {
            var32 = false;
            var37 = null;
            var24 = false;
         }

         if (var1 != 2) {
            var12 = var8.isEncrypted();
            var20 = new ImageLoader.CacheImage();
            if (!var9) {
               if (!MessageObject.isGifDocument(var8.webFile) && !MessageObject.isGifDocument(var8.document) && !MessageObject.isRoundVideoDocument(var8.document)) {
                  String var42 = var8.path;
                  if (var42 != null && !var42.startsWith("vthumb") && !var42.startsWith("thumb")) {
                     var42 = getHttpUrlExtension(var42, "jpg");
                     if (var42.equals("mp4") || var42.equals("gif")) {
                        var20.animatedFile = true;
                     }
                  }
               } else {
                  var20.animatedFile = true;
               }
            }

            File var44;
            label259: {
               boolean var18;
               if (var37 == null) {
                  int var53;
                  if (var8.photoSize instanceof TLRPC.TL_photoStrippedSize) {
                     var53 = 0;
                     var24 = true;
                  } else {
                     label343: {
                        label349: {
                           SecureDocument var39 = var8.secureDocument;
                           if (var39 != null) {
                              var20.secureDocument = var39;
                              if (var20.secureDocument.secureFile.dc_id == Integer.MIN_VALUE) {
                                 var23 = true;
                              } else {
                                 var23 = false;
                              }

                              var44 = new File(FileLoader.getDirectory(4), var2);
                              var18 = var32;
                           } else {
                              TLRPC.Document var50;
                              if ("g".equals(var6) || var14 == 0 && var15 > 0 && var8.path == null && !var12) {
                                 var50 = var8.document;
                                 if (var50 != null) {
                                    if (var50 instanceof TLRPC.TL_documentEncrypted) {
                                       var37 = new File(FileLoader.getDirectory(4), var2);
                                    } else if (MessageObject.isVideoDocument(var50)) {
                                       var37 = new File(FileLoader.getDirectory(2), var2);
                                    } else {
                                       var37 = new File(FileLoader.getDirectory(3), var2);
                                    }

                                    var44 = var37;
                                    if ("g".equals(var6)) {
                                       var44 = var37;
                                       if (!var37.exists()) {
                                          var44 = FileLoader.getDirectory(4);
                                          StringBuilder var40 = new StringBuilder();
                                          var40.append(var50.dc_id);
                                          var40.append("_");
                                          var40.append(var50.id);
                                          var40.append(".temp");
                                          var44 = new File(var44, var40.toString());
                                       }
                                    }

                                    var20.lottieFile = "application/x-tgsticker".equals(var50.mime_type);
                                    var53 = var50.size;
                                    var37 = var44;
                                    break label343;
                                 }

                                 if (var8.webFile != null) {
                                    var37 = new File(FileLoader.getDirectory(3), var2);
                                 } else {
                                    var37 = new File(FileLoader.getDirectory(0), var2);
                                 }
                                 break label349;
                              }

                              var44 = new File(FileLoader.getDirectory(4), var2);
                              if (var44.exists()) {
                                 var32 = true;
                                 var37 = var44;
                              } else if (var14 == 2) {
                                 var49 = FileLoader.getDirectory(4);
                                 StringBuilder var46 = new StringBuilder();
                                 var46.append(var2);
                                 var46.append(".enc");
                                 var37 = new File(var49, var46.toString());
                              } else {
                                 var37 = var44;
                              }

                              var50 = var8.document;
                              var18 = var32;
                              var44 = var37;
                              var23 = var24;
                              if (var50 != null) {
                                 var20.lottieFile = "application/x-tgsticker".equals(var50.mime_type);
                                 var18 = var32;
                                 var44 = var37;
                                 var23 = var24;
                              }
                           }

                           var37 = var44;
                           var24 = var23;
                           var32 = var18;
                        }

                        var53 = 0;
                     }
                  }

                  var18 = var32;
                  var44 = var37;
                  if ("g".equals(var6)) {
                     var20.animatedFile = true;
                     var20.size = var53;
                     var24 = true;
                     break label259;
                  }
               } else {
                  var44 = var37;
                  var18 = var32;
               }

               var37 = var44;
               var32 = var18;
            }

            var20.imageType = var7;
            var20.key = var3;
            var20.filter = var6;
            var20.imageLocation = var8;
            var20.ext = var16;
            var20.currentAccount = var17;
            var20.parentObject = var10;
            if (var14 == 2) {
               var44 = FileLoader.getInternalCacheDir();
               StringBuilder var51 = new StringBuilder();
               var51.append(var2);
               var51.append(".enc.key");
               var20.encryptionKeyPath = new File(var44, var51.toString());
            }

            var20.addImageReceiver(var5, var3, var6, var7);
            if (!var24 && !var32 && !var37.exists()) {
               var20.url = var2;
               this.imageLoadingByUrl.put(var2, var20);
               var2 = var8.path;
               if (var2 != null) {
                  var2 = Utilities.MD5(var2);
                  File var29 = FileLoader.getDirectory(4);
                  StringBuilder var35 = new StringBuilder();
                  var35.append(var2);
                  var35.append("_temp.jpg");
                  var20.tempFilePath = new File(var29, var35.toString());
                  var20.finalFilePath = var37;
                  if (var8.path.startsWith("athumb")) {
                     var20.artworkTask = new ImageLoader.ArtworkLoadTask(var20);
                     this.artworkTasks.add(var20.artworkTask);
                     this.runArtworkTasks(false);
                  } else {
                     var20.httpTask = new ImageLoader.HttpImageTask(var20, var15);
                     this.httpTasks.add(var20.httpTask);
                     this.runHttpTasks(false);
                  }
               } else {
                  byte var26;
                  FileLoader var31;
                  if (var8.location == null) {
                     if (var8.document != null) {
                        var31 = FileLoader.getInstance(var17);
                        TLRPC.Document var30 = var8.document;
                        if (var1 != 0) {
                           var26 = 2;
                        } else {
                           var26 = 1;
                        }

                        var31.loadFile(var30, var10, var26, var14);
                     } else if (var8.secureDocument != null) {
                        var31 = FileLoader.getInstance(var17);
                        SecureDocument var33 = var8.secureDocument;
                        if (var1 != 0) {
                           var26 = 2;
                        } else {
                           var26 = 1;
                        }

                        var31.loadFile(var33, var26);
                     } else if (var8.webFile != null) {
                        var31 = FileLoader.getInstance(var17);
                        WebFile var34 = var8.webFile;
                        if (var1 != 0) {
                           var26 = 2;
                        } else {
                           var26 = 1;
                        }

                        var31.loadFile(var34, var26, var14);
                     }
                  } else {
                     if (var14 != 0 || var15 > 0 && var8.key == null) {
                        var4 = var14;
                     } else {
                        var4 = 1;
                     }

                     var31 = FileLoader.getInstance(var17);
                     if (var1 != 0) {
                        var26 = 2;
                     } else {
                        var26 = 1;
                     }

                     var31.loadFile(var8, var10, var16, var26, var4);
                  }

                  if (var5.isForceLoding()) {
                     this.forceLoadingImages.put(var20.key, 0);
                  }
               }
            } else {
               var20.finalFilePath = var37;
               var20.imageLocation = var8;
               var20.cacheTask = new ImageLoader.CacheOutTask(var20);
               this.imageLoadingByKeys.put(var3, var20);
               if (var1 != 0) {
                  this.cacheThumbOutQueue.postRunnable(var20.cacheTask);
               } else {
                  this.cacheOutQueue.postRunnable(var20.cacheTask);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$fileDidFailedLoad$9$ImageLoader(String var1) {
      ImageLoader.CacheImage var2 = (ImageLoader.CacheImage)this.imageLoadingByUrl.get(var1);
      if (var2 != null) {
         var2.setImageAndClear((Drawable)null, (String)null);
      }

   }

   // $FF: synthetic method
   public void lambda$fileDidLoaded$8$ImageLoader(String var1, int var2, File var3) {
      ImageLoader.ThumbGenerateInfo var4 = (ImageLoader.ThumbGenerateInfo)this.waitingForQualityThumb.get(var1);
      if (var4 != null && var4.parentDocument != null) {
         this.generateThumb(var2, var3, var4);
         this.waitingForQualityThumb.remove(var1);
      }

      ImageLoader.CacheImage var5 = (ImageLoader.CacheImage)this.imageLoadingByUrl.get(var1);
      if (var5 != null) {
         this.imageLoadingByUrl.remove(var1);
         ArrayList var6 = new ArrayList();
         byte var7 = 0;
         var2 = 0;

         while(true) {
            int var8 = var7;
            if (var2 >= var5.imageReceiverArray.size()) {
               for(; var8 < var6.size(); ++var8) {
                  ImageLoader.CacheOutTask var13 = (ImageLoader.CacheOutTask)var6.get(var8);
                  if (var13.cacheImage.imageType == 1) {
                     this.cacheThumbOutQueue.postRunnable(var13);
                  } else {
                     this.cacheOutQueue.postRunnable(var13);
                  }
               }

               return;
            }

            String var9 = (String)var5.keys.get(var2);
            String var10 = (String)var5.filters.get(var2);
            var8 = (Integer)var5.imageTypes.get(var2);
            ImageReceiver var11 = (ImageReceiver)var5.imageReceiverArray.get(var2);
            ImageLoader.CacheImage var14 = (ImageLoader.CacheImage)this.imageLoadingByKeys.get(var9);
            ImageLoader.CacheImage var12 = var14;
            if (var14 == null) {
               var12 = new ImageLoader.CacheImage();
               var12.secureDocument = var5.secureDocument;
               var12.currentAccount = var5.currentAccount;
               var12.finalFilePath = var3;
               var12.key = var9;
               var12.imageLocation = var5.imageLocation;
               var12.imageType = var8;
               var12.ext = var5.ext;
               var12.encryptionKeyPath = var5.encryptionKeyPath;
               var12.cacheTask = new ImageLoader.CacheOutTask(var12);
               var12.filter = var10;
               var12.animatedFile = var5.animatedFile;
               var12.lottieFile = var5.lottieFile;
               this.imageLoadingByKeys.put(var9, var12);
               var6.add(var12.cacheTask);
            }

            var12.addImageReceiver(var11, var9, var10, var8);
            ++var2;
         }
      }
   }

   // $FF: synthetic method
   public void lambda$httpFileLoadError$6$ImageLoader(String var1) {
      ImageLoader.CacheImage var3 = (ImageLoader.CacheImage)this.imageLoadingByUrl.get(var1);
      if (var3 != null) {
         ImageLoader.HttpImageTask var2 = var3.httpTask;
         var3.httpTask = new ImageLoader.HttpImageTask(var2.cacheImage, var2.imageSize);
         this.httpTasks.add(var3.httpTask);
         this.runHttpTasks(false);
      }
   }

   // $FF: synthetic method
   public void lambda$null$10$ImageLoader(ImageLoader.HttpFileTask var1) {
      this.httpFileLoadTasks.add(var1);
      this.runHttpFileLoadTasks((ImageLoader.HttpFileTask)null, 0);
   }

   // $FF: synthetic method
   public void lambda$replaceImageInCache$3$ImageLoader(String var1, String var2, ImageLocation var3) {
      this.replaceImageInCacheInternal(var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$runHttpFileLoadTasks$11$ImageLoader(ImageLoader.HttpFileTask var1, int var2) {
      if (var1 != null) {
         --this.currentHttpFileLoadTasksCount;
      }

      if (var1 != null) {
         if (var2 == 1) {
            if (var1.canRetry) {
               _$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig var3 = new _$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig(this, new ImageLoader.HttpFileTask(var1.url, var1.tempFile, var1.ext, var1.currentAccount));
               this.retryHttpsTasks.put(var1.url, var3);
               AndroidUtilities.runOnUIThread(var3, 1000L);
            } else {
               this.httpFileLoadTasksByKeys.remove(var1.url);
               NotificationCenter.getInstance(var1.currentAccount).postNotificationName(NotificationCenter.httpFileDidFailedLoad, var1.url, 0);
            }
         } else if (var2 == 2) {
            this.httpFileLoadTasksByKeys.remove(var1.url);
            File var4 = FileLoader.getDirectory(4);
            StringBuilder var5 = new StringBuilder();
            var5.append(Utilities.MD5(var1.url));
            var5.append(".");
            var5.append(var1.ext);
            File var6 = new File(var4, var5.toString());
            if (!var1.tempFile.renameTo(var6)) {
               var6 = var1.tempFile;
            }

            String var7 = var6.toString();
            NotificationCenter.getInstance(var1.currentAccount).postNotificationName(NotificationCenter.httpFileDidLoad, var1.url, var7);
         }
      }

      while(this.currentHttpFileLoadTasksCount < 2 && !this.httpFileLoadTasks.isEmpty()) {
         ((ImageLoader.HttpFileTask)this.httpFileLoadTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
         ++this.currentHttpFileLoadTasksCount;
      }

   }

   public void loadHttpFile(String var1, String var2, int var3) {
      if (var1 != null && var1.length() != 0 && !this.httpFileLoadTasksByKeys.containsKey(var1)) {
         var2 = getHttpUrlExtension(var1, var2);
         File var4 = FileLoader.getDirectory(4);
         StringBuilder var5 = new StringBuilder();
         var5.append(Utilities.MD5(var1));
         var5.append("_temp.");
         var5.append(var2);
         File var7 = new File(var4, var5.toString());
         var7.delete();
         ImageLoader.HttpFileTask var6 = new ImageLoader.HttpFileTask(var1, var7, var2, var3);
         this.httpFileLoadTasks.add(var6);
         this.httpFileLoadTasksByKeys.put(var1, var6);
         this.runHttpFileLoadTasks((ImageLoader.HttpFileTask)null, 0);
      }

   }

   public void loadImageForImageReceiver(ImageReceiver var1) {
      if (var1 != null) {
         String var2;
         Drawable var3;
         boolean var4;
         label295: {
            var2 = var1.getMediaKey();
            if (var2 != null) {
               if (MessageObject.isAnimatedStickerDocument(var1.getMediaLocation().document)) {
                  var3 = (Drawable)this.lottieMemCache.get(var2);
               } else {
                  var3 = (Drawable)this.memCache.get(var2);
               }

               if (var3 != null) {
                  this.cancelLoadingForImageReceiver(var1, true);
                  var1.setImageBitmapByKey(var3, var2, 3, true);
                  if (!var1.isForcePreview()) {
                     return;
                  }

                  var4 = true;
                  break label295;
               }
            }

            var4 = false;
         }

         String var5 = var1.getImageKey();
         if (!var4 && var5 != null) {
            ImageLocation var29 = var1.getImageLocation();
            if (var29 != null && MessageObject.isAnimatedStickerDocument(var29.document)) {
               var3 = (Drawable)this.lottieMemCache.get(var5);
            } else {
               var3 = (Drawable)this.memCache.get(var5);
            }

            if (var3 != null) {
               this.cancelLoadingForImageReceiver(var1, true);
               var1.setImageBitmapByKey(var3, var5, 0, true);
               if (!var1.isForcePreview() && var2 == null) {
                  return;
               }

               var4 = true;
            }
         }

         boolean var6;
         String var30;
         label281: {
            var30 = var1.getThumbKey();
            if (var30 != null) {
               BitmapDrawable var27 = (BitmapDrawable)this.memCache.get(var30);
               if (var27 != null) {
                  var1.setImageBitmapByKey(var27, var30, 1, true);
                  this.cancelLoadingForImageReceiver(var1, false);
                  if (var4 && var1.isForcePreview()) {
                     return;
                  }

                  var6 = true;
                  break label281;
               }
            }

            var6 = false;
         }

         Object var7;
         ImageLocation var8;
         String var9;
         String var10;
         String var11;
         boolean var12;
         Object var28;
         ImageLocation var31;
         label276: {
            label275: {
               var7 = var1.getParentObject();
               TLRPC.Document var32 = var1.getQulityThumbDocument();
               var8 = var1.getThumbLocation();
               var9 = var1.getThumbFilter();
               var28 = var1.getMediaLocation();
               var10 = var1.getMediaFilter();
               var31 = var1.getImageLocation();
               var11 = var1.getImageFilter();
               if (var31 == null && var1.isNeedsQualityThumb() && var1.isCurrentKeyQuality()) {
                  if (var7 instanceof MessageObject) {
                     var31 = ImageLocation.getForDocument(((MessageObject)var7).getDocument());
                     break label275;
                  }

                  if (var32 != null) {
                     var31 = ImageLocation.getForDocument(var32);
                     break label275;
                  }
               }

               var12 = false;
               break label276;
            }

            var12 = true;
         }

         var30 = var1.getExt();
         String var13 = var30;
         if (var30 == null) {
            var13 = "jpg";
         }

         String var14 = null;
         Object var15 = var14;
         Object var16 = var14;
         String var17 = var14;
         int var18 = 0;

         boolean var19;
         String var22;
         Object var24;
         Object var33;
         int var35;
         StringBuilder var36;
         StringBuilder var41;
         String var43;
         for(var19 = false; var18 < 2; var28 = var24) {
            Object var21;
            if (var18 == 0) {
               var21 = var31;
            } else {
               var21 = var28;
            }

            ImageLocation var23;
            label261: {
               if (var21 != null) {
                  if (var28 != null) {
                     var33 = var28;
                  } else {
                     var33 = var31;
                  }

                  var22 = ((ImageLocation)var21).getKey(var7, var33);
                  if (var22 != null) {
                     label255: {
                        label254: {
                           if (((ImageLocation)var21).path != null) {
                              var36 = new StringBuilder();
                              var36.append(var22);
                              var36.append(".");
                              var36.append(getHttpUrlExtension(((ImageLocation)var21).path, "jpg"));
                              var30 = var36.toString();
                           } else if (((ImageLocation)var21).photoSize instanceof TLRPC.TL_photoStrippedSize) {
                              var36 = new StringBuilder();
                              var36.append(var22);
                              var36.append(".");
                              var36.append(var13);
                              var30 = var36.toString();
                           } else if (((ImageLocation)var21).location != null) {
                              var36 = new StringBuilder();
                              var36.append(var22);
                              var36.append(".");
                              var36.append(var13);
                              var30 = var36.toString();
                              if (var1.getExt() != null) {
                                 break label254;
                              }

                              TLRPC.TL_fileLocationToBeDeprecated var25 = ((ImageLocation)var21).location;
                              if (var25.key != null || var25.volume_id == -2147483648L && var25.local_id < 0) {
                                 break label254;
                              }
                           } else {
                              WebFile var38 = ((ImageLocation)var21).webFile;
                              if (var38 != null) {
                                 var43 = FileLoader.getMimeTypePart(var38.mime_type);
                                 var36 = new StringBuilder();
                                 var36.append(var22);
                                 var36.append(".");
                                 var36.append(getHttpUrlExtension(((ImageLocation)var21).webFile.url, var43));
                                 var30 = var36.toString();
                              } else {
                                 if (((ImageLocation)var21).secureDocument == null) {
                                    if (((ImageLocation)var21).document != null) {
                                       var43 = var22;
                                       if (var18 == 0) {
                                          var43 = var22;
                                          if (var12) {
                                             var36 = new StringBuilder();
                                             var36.append("q_");
                                             var36.append(var22);
                                             var43 = var36.toString();
                                          }
                                       }

                                       label236: {
                                          var30 = FileLoader.getDocumentFileName(((ImageLocation)var21).document);
                                          var22 = "";
                                          if (var30 != null) {
                                             var35 = var30.lastIndexOf(46);
                                             if (var35 != -1) {
                                                var30 = var30.substring(var35);
                                                break label236;
                                             }
                                          }

                                          var30 = "";
                                       }

                                       if (var30.length() <= 1) {
                                          if ("video/mp4".equals(((ImageLocation)var21).document.mime_type)) {
                                             var30 = ".mp4";
                                          } else {
                                             var30 = var22;
                                             if ("video/x-matroska".equals(((ImageLocation)var21).document.mime_type)) {
                                                var30 = ".mkv";
                                             }
                                          }
                                       }

                                       var41 = new StringBuilder();
                                       var41.append(var43);
                                       var41.append(var30);
                                       var30 = var41.toString();
                                       if (!MessageObject.isVideoDocument(((ImageLocation)var21).document) && !MessageObject.isGifDocument(((ImageLocation)var21).document) && !MessageObject.isRoundVideoDocument(((ImageLocation)var21).document) && !MessageObject.canPreviewDocument(((ImageLocation)var21).document)) {
                                          var4 = true;
                                          break label255;
                                       }

                                       var4 = false;
                                       break label255;
                                    }

                                    var30 = null;
                                    var4 = var19;
                                    var43 = var22;
                                    break label255;
                                 }

                                 var36 = new StringBuilder();
                                 var36.append(var22);
                                 var36.append(".");
                                 var36.append(var13);
                                 var30 = var36.toString();
                              }
                           }

                           var43 = var22;
                           var4 = var19;
                           break label255;
                        }

                        var4 = true;
                        var43 = var22;
                     }

                     Object var26;
                     Object var42;
                     if (var18 == 0) {
                        var26 = var30;
                        var30 = var14;
                        var42 = var43;
                        var43 = var17;
                     } else {
                        var14 = var30;
                        var30 = var43;
                        var43 = var14;
                        var26 = var16;
                        var42 = var15;
                     }

                     var14 = var30;
                     var23 = var31;
                     var24 = var28;
                     var15 = var42;
                     var19 = var4;
                     var16 = var26;
                     var17 = var43;
                     if (var21 == var8) {
                        if (var18 == 0) {
                           var23 = null;
                           var15 = var23;
                           var16 = var23;
                           var14 = var30;
                           var24 = var28;
                           var19 = var4;
                           var17 = var43;
                        } else {
                           var14 = null;
                           var17 = var14;
                           var16 = var26;
                           var19 = var4;
                           var15 = var42;
                           var24 = var14;
                           var23 = var31;
                        }
                     }
                     break label261;
                  }
               }

               var23 = var31;
               var24 = var28;
            }

            ++var18;
            var31 = var23;
         }

         byte var37 = 1;
         if (var8 != null) {
            ImageLocation var44 = var1.getStrippedLocation();
            var33 = var44;
            if (var44 == null) {
               if (var28 != null) {
                  var33 = var28;
               } else {
                  var33 = var31;
               }
            }

            var43 = var8.getKey(var7, var33);
            if (var8.path != null) {
               var36 = new StringBuilder();
               var36.append(var43);
               var36.append(".");
               var36.append(getHttpUrlExtension(var8.path, "jpg"));
               var30 = var36.toString();
            } else if (var8.photoSize instanceof TLRPC.TL_photoStrippedSize) {
               var36 = new StringBuilder();
               var36.append(var43);
               var36.append(".");
               var36.append(var13);
               var30 = var36.toString();
            } else if (var8.location != null) {
               var36 = new StringBuilder();
               var36.append(var43);
               var36.append(".");
               var36.append(var13);
               var30 = var36.toString();
            } else {
               var30 = null;
            }
         } else {
            var43 = null;
            var30 = var43;
         }

         if (var14 != null && var10 != null) {
            var41 = new StringBuilder();
            var41.append(var14);
            var41.append("@");
            var41.append(var10);
            var14 = var41.toString();
         }

         if (var15 != null && var11 != null) {
            var41 = new StringBuilder();
            var41.append((String)var15);
            var41.append("@");
            var41.append(var11);
            var15 = var41.toString();
         }

         var22 = var11;
         var11 = var43;
         if (var43 != null) {
            var11 = var43;
            if (var9 != null) {
               StringBuilder var39 = new StringBuilder();
               var39.append(var43);
               var39.append("@");
               var39.append(var9);
               var11 = var39.toString();
            }
         }

         if (var31 != null && var31.path != null) {
            if (var6) {
               var37 = 2;
            }

            this.createLoadOperationForImageReceiver(var1, var11, var30, var13, var8, var9, 0, 1, 1, var37);
            this.createLoadOperationForImageReceiver(var1, (String)var15, (String)var16, var13, var31, var22, var1.getSize(), 1, 0, 0);
         } else {
            byte var34;
            int var40;
            if (var28 != null) {
               var35 = var1.getCacheType();
               if (var35 == 0 && var19) {
                  var35 = 1;
               }

               if (var35 == 0) {
                  var40 = 1;
               } else {
                  var40 = var35;
               }

               if (!var6) {
                  if (var6) {
                     var34 = 2;
                  } else {
                     var34 = 1;
                  }

                  this.createLoadOperationForImageReceiver(var1, var11, var30, var13, var8, var9, 0, var40, 1, var34);
               }

               if (!var4) {
                  this.createLoadOperationForImageReceiver(var1, (String)var15, (String)var16, var13, var31, var22, 0, 1, 0, 0);
               }

               this.createLoadOperationForImageReceiver(var1, var14, var17, var13, (ImageLocation)var28, var10, var1.getSize(), var35, 3, 0);
            } else {
               var35 = var1.getCacheType();
               if (var35 == 0 && var19) {
                  var35 = 1;
               }

               if (var35 == 0) {
                  var40 = 1;
               } else {
                  var40 = var35;
               }

               if (var6) {
                  var34 = 2;
               } else {
                  var34 = 1;
               }

               this.createLoadOperationForImageReceiver(var1, var11, var30, var13, var8, var9, 0, var40, 1, var34);
               this.createLoadOperationForImageReceiver(var1, (String)var15, (String)var16, var13, var31, var22, var1.getSize(), var35, 0, 0);
            }
         }

      }
   }

   public void putImageToCache(BitmapDrawable var1, String var2) {
      this.memCache.put(var2, var1);
   }

   public void removeImage(String var1) {
      this.bitmapUseCounts.remove(var1);
      this.memCache.remove(var1);
   }

   public void removeTestWebFile(String var1) {
      if (var1 != null) {
         this.testWebFile.remove(var1);
      }
   }

   public void replaceImageInCache(String var1, String var2, ImageLocation var3, boolean var4) {
      if (var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$goqDHHQdnb5snOP60neGaS99rrI(this, var1, var2, var3));
      } else {
         this.replaceImageInCacheInternal(var1, var2, var3);
      }

   }

   private class ArtworkLoadTask extends AsyncTask {
      private ImageLoader.CacheImage cacheImage;
      private boolean canRetry;
      private HttpURLConnection httpConnection;
      private boolean small;

      public ArtworkLoadTask(ImageLoader.CacheImage var2) {
         boolean var3 = true;
         this.canRetry = true;
         this.cacheImage = var2;
         if (Uri.parse(var2.imageLocation.path).getQueryParameter("s") == null) {
            var3 = false;
         }

         this.small = var3;
      }

      protected String doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      public void lambda$onCancelled$1$ImageLoader$ArtworkLoadTask() {
         ImageLoader.this.runArtworkTasks(true);
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$0$ImageLoader$ArtworkLoadTask() {
         ImageLoader.this.runArtworkTasks(true);
      }

      protected void onCancelled() {
         ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ArtworkLoadTask$MXyHRkFD1Tybf8nHKTkuKr4tAsA(this));
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            ImageLoader.CacheImage var2 = this.cacheImage;
            var2.httpTask = ImageLoader.this.new HttpImageTask(var2, 0, var1);
            ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
            ImageLoader.this.runHttpTasks(false);
         } else if (this.canRetry) {
            ImageLoader.this.artworkLoadError(this.cacheImage.url);
         }

         ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ArtworkLoadTask$AYFfY8_xR4BmCIfdHvNLIsGZyV0(this));
      }
   }

   private class CacheImage {
      protected boolean animatedFile;
      protected ImageLoader.ArtworkLoadTask artworkTask;
      protected ImageLoader.CacheOutTask cacheTask;
      protected int currentAccount;
      protected File encryptionKeyPath;
      protected String ext;
      protected String filter;
      protected ArrayList filters;
      protected File finalFilePath;
      protected ImageLoader.HttpImageTask httpTask;
      protected ImageLocation imageLocation;
      protected ArrayList imageReceiverArray;
      protected int imageType;
      protected ArrayList imageTypes;
      protected String key;
      protected ArrayList keys;
      protected boolean lottieFile;
      protected Object parentObject;
      protected SecureDocument secureDocument;
      protected int size;
      protected File tempFilePath;
      protected String url;

      private CacheImage() {
         this.imageReceiverArray = new ArrayList();
         this.keys = new ArrayList();
         this.filters = new ArrayList();
         this.imageTypes = new ArrayList();
      }

      // $FF: synthetic method
      CacheImage(Object var2) {
         this();
      }

      public void addImageReceiver(ImageReceiver var1, String var2, String var3, int var4) {
         if (!this.imageReceiverArray.contains(var1)) {
            this.imageReceiverArray.add(var1);
            this.keys.add(var2);
            this.filters.add(var3);
            this.imageTypes.add(var4);
            ImageLoader.this.imageLoadingByTag.put(var1.getTag(var4), this);
         }
      }

      // $FF: synthetic method
      public void lambda$setImageAndClear$0$ImageLoader$CacheImage(Drawable var1, ArrayList var2, String var3) {
         if (var1 instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable var4 = (AnimatedFileDrawable)var1;
            int var5 = 0;

            boolean var6;
            boolean var8;
            for(var6 = false; var5 < var2.size(); var6 = var8) {
               ImageReceiver var7 = (ImageReceiver)var2.get(var5);
               AnimatedFileDrawable var9;
               if (var5 == 0) {
                  var9 = var4;
               } else {
                  var9 = var4.makeCopy();
               }

               if (var7.setImageBitmapByKey(var9, this.key, this.imageType, false)) {
                  var8 = var6;
                  if (var9 == var4) {
                     var8 = true;
                  }
               } else {
                  var8 = var6;
                  if (var9 != var4) {
                     var9.recycle();
                     var8 = var6;
                  }
               }

               ++var5;
            }

            if (!var6) {
               var4.recycle();
            }
         } else {
            for(int var10 = 0; var10 < var2.size(); ++var10) {
               ((ImageReceiver)var2.get(var10)).setImageBitmapByKey(var1, this.key, (Integer)this.imageTypes.get(var10), false);
            }
         }

         if (var3 != null) {
            ImageLoader.this.decrementUseCount(var3);
         }

      }

      public void removeImageReceiver(ImageReceiver var1) {
         int var2 = this.imageType;
         byte var3 = 0;

         int var4;
         int var6;
         for(var4 = 0; var4 < this.imageReceiverArray.size(); var4 = var6 + 1) {
            ImageReceiver var5 = (ImageReceiver)this.imageReceiverArray.get(var4);
            if (var5 != null) {
               var6 = var4;
               if (var5 != var1) {
                  continue;
               }
            }

            this.imageReceiverArray.remove(var4);
            this.keys.remove(var4);
            this.filters.remove(var4);
            var2 = (Integer)this.imageTypes.remove(var4);
            if (var5 != null) {
               ImageLoader.this.imageLoadingByTag.remove(var5.getTag(var2));
            }

            var6 = var4 - 1;
         }

         if (this.imageReceiverArray.size() == 0) {
            for(var4 = var3; var4 < this.imageReceiverArray.size(); ++var4) {
               ImageLoader.this.imageLoadingByTag.remove(((ImageReceiver)this.imageReceiverArray.get(var4)).getTag(var2));
            }

            this.imageReceiverArray.clear();
            if (this.imageLocation != null && !ImageLoader.this.forceLoadingImages.containsKey(this.key)) {
               ImageLocation var7 = this.imageLocation;
               if (var7.location != null) {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
               } else if (var7.document != null) {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
               } else if (var7.secureDocument != null) {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
               } else if (var7.webFile != null) {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
               }
            }

            if (this.cacheTask != null) {
               if (var2 == 1) {
                  ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
               } else {
                  ImageLoader.this.cacheOutQueue.cancelRunnable(this.cacheTask);
               }

               this.cacheTask.cancel();
               this.cacheTask = null;
            }

            if (this.httpTask != null) {
               ImageLoader.this.httpTasks.remove(this.httpTask);
               this.httpTask.cancel(true);
               this.httpTask = null;
            }

            if (this.artworkTask != null) {
               ImageLoader.this.artworkTasks.remove(this.artworkTask);
               this.artworkTask.cancel(true);
               this.artworkTask = null;
            }

            if (this.url != null) {
               ImageLoader.this.imageLoadingByUrl.remove(this.url);
            }

            if (this.key != null) {
               ImageLoader.this.imageLoadingByKeys.remove(this.key);
            }
         }

      }

      public void replaceImageReceiver(ImageReceiver var1, String var2, String var3, int var4) {
         int var5 = this.imageReceiverArray.indexOf(var1);
         if (var5 != -1) {
            int var6 = var5;
            if ((Integer)this.imageTypes.get(var5) != var4) {
               ArrayList var7 = this.imageReceiverArray;
               var4 = var7.subList(var5 + 1, var7.size()).indexOf(var1);
               var6 = var4;
               if (var4 == -1) {
                  return;
               }
            }

            this.keys.set(var6, var2);
            this.filters.set(var6, var3);
         }
      }

      public void setImageAndClear(Drawable var1, String var2) {
         if (var1 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE(this, var1, new ArrayList(this.imageReceiverArray), var2));
         }

         for(int var3 = 0; var3 < this.imageReceiverArray.size(); ++var3) {
            ImageReceiver var4 = (ImageReceiver)this.imageReceiverArray.get(var3);
            ImageLoader.this.imageLoadingByTag.remove(var4.getTag(this.imageType));
         }

         this.imageReceiverArray.clear();
         if (this.url != null) {
            ImageLoader.this.imageLoadingByUrl.remove(this.url);
         }

         if (this.key != null) {
            ImageLoader.this.imageLoadingByKeys.remove(this.key);
         }

      }
   }

   private class CacheOutTask implements Runnable {
      private ImageLoader.CacheImage cacheImage;
      private boolean isCancelled;
      private Thread runningThread;
      private final Object sync = new Object();

      public CacheOutTask(ImageLoader.CacheImage var2) {
         this.cacheImage = var2;
      }

      private void onPostExecute(Drawable var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$CacheOutTask$P_Q_SglLFg8CKw46QDkF5nN_7Ko(this, var1));
      }

      public void cancel() {
         Object var1 = this.sync;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label143: {
            try {
               try {
                  this.isCancelled = true;
                  if (this.runningThread != null) {
                     this.runningThread.interrupt();
                  }
               } catch (Exception var17) {
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label143;
            }

            label131:
            try {
               return;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label131;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               continue;
            }
         }
      }

      // $FF: synthetic method
      public void lambda$null$0$ImageLoader$CacheOutTask(Drawable var1, String var2) {
         this.cacheImage.setImageAndClear(var1, var2);
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$1$ImageLoader$CacheOutTask(Drawable var1) {
         boolean var2 = var1 instanceof LottieDrawable;
         String var3 = null;
         if (var2) {
            var1 = (LottieDrawable)var1;
            Drawable var4 = (Drawable)ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
            if (var4 == null) {
               ImageLoader.this.lottieMemCache.put(this.cacheImage.key, var1);
            } else {
               var1 = var4;
            }
         } else if (!(var1 instanceof AnimatedFileDrawable)) {
            if (var1 instanceof BitmapDrawable) {
               Object var6 = (BitmapDrawable)var1;
               Drawable var5 = (Drawable)ImageLoader.this.memCache.get(this.cacheImage.key);
               if (var5 == null) {
                  ImageLoader.this.memCache.put(this.cacheImage.key, var6);
               } else {
                  ((BitmapDrawable)var6).getBitmap().recycle();
                  var6 = var5;
               }

               var1 = var6;
               if (var6 != null) {
                  ImageLoader.this.incrementUseCount(this.cacheImage.key);
                  var3 = this.cacheImage.key;
                  var1 = var6;
               }
            } else {
               var1 = null;
            }
         }

         ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$CacheOutTask$L2FS7HdPO2NRh4SX6OgH248fhO4(this, (Drawable)var1, var3));
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private class HttpFileTask extends AsyncTask {
      private boolean canRetry = true;
      private int currentAccount;
      private String ext;
      private RandomAccessFile fileOutputStream = null;
      private int fileSize;
      private long lastProgressTime;
      private File tempFile;
      private String url;

      public HttpFileTask(String var2, File var3, String var4, int var5) {
         this.url = var2;
         this.tempFile = var3;
         this.ext = var4;
         this.currentAccount = var5;
      }

      private void reportProgress(float var1) {
         long var2 = System.currentTimeMillis();
         if (var1 != 1.0F) {
            long var4 = this.lastProgressTime;
            if (var4 != 0L && var4 >= var2 - 500L) {
               return;
            }
         }

         this.lastProgressTime = var2;
         Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpFileTask$CbdoQhu0HscXntXREbdZu5bUbuA(this, var1));
      }

      protected Boolean doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      public void lambda$null$0$ImageLoader$HttpFileTask(float var1) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, this.url, var1);
      }

      // $FF: synthetic method
      public void lambda$reportProgress$1$ImageLoader$HttpFileTask(float var1) {
         ImageLoader.this.fileProgresses.put(this.url, var1);
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$HttpFileTask$jxNOungyWTOpPFlVvFvzXDtQEx0(this, var1));
      }

      protected void onCancelled() {
         ImageLoader.this.runHttpFileLoadTasks(this, 2);
      }

      protected void onPostExecute(Boolean var1) {
         ImageLoader var2 = ImageLoader.this;
         byte var3;
         if (var1) {
            var3 = 2;
         } else {
            var3 = 1;
         }

         var2.runHttpFileLoadTasks(this, var3);
      }
   }

   private class HttpImageTask extends AsyncTask {
      private ImageLoader.CacheImage cacheImage;
      private boolean canRetry = true;
      private RandomAccessFile fileOutputStream;
      private HttpURLConnection httpConnection;
      private int imageSize;
      private long lastProgressTime;
      private String overrideUrl;

      public HttpImageTask(ImageLoader.CacheImage var2, int var3) {
         this.cacheImage = var2;
         this.imageSize = var3;
      }

      public HttpImageTask(ImageLoader.CacheImage var2, int var3, String var4) {
         this.cacheImage = var2;
         this.imageSize = var3;
         this.overrideUrl = var4;
      }

      // $FF: synthetic method
      static void lambda$doInBackground$2(TLObject var0, TLRPC.TL_error var1) {
      }

      private void reportProgress(float var1) {
         long var2 = System.currentTimeMillis();
         if (var1 != 1.0F) {
            long var4 = this.lastProgressTime;
            if (var4 != 0L && var4 >= var2 - 500L) {
               return;
            }
         }

         this.lastProgressTime = var2;
         Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7_WIiuq5bKLqtQ8BNBI(this, var1));
      }

      protected Boolean doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      public void lambda$null$0$ImageLoader$HttpImageTask(float var1) {
         NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, this.cacheImage.url, var1);
      }

      // $FF: synthetic method
      public void lambda$null$3$ImageLoader$HttpImageTask(Boolean var1) {
         if (var1) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidLoad, this.cacheImage.url);
         } else {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidFailedLoad, this.cacheImage.url, 2);
         }

      }

      // $FF: synthetic method
      public void lambda$null$7$ImageLoader$HttpImageTask() {
         NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidFailedLoad, this.cacheImage.url, 1);
      }

      // $FF: synthetic method
      public void lambda$onCancelled$6$ImageLoader$HttpImageTask() {
         ImageLoader.this.runHttpTasks(true);
      }

      // $FF: synthetic method
      public void lambda$onCancelled$8$ImageLoader$HttpImageTask() {
         ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$HttpImageTask$gXa55exgYzWejvB_mF_uS_Y2fOo(this));
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$4$ImageLoader$HttpImageTask(Boolean var1) {
         ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$HttpImageTask$pG_0hY9R_vFFkVPjhMdZBfbtL24(this, var1));
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$5$ImageLoader$HttpImageTask() {
         ImageLoader.this.runHttpTasks(true);
      }

      // $FF: synthetic method
      public void lambda$reportProgress$1$ImageLoader$HttpImageTask(float var1) {
         ImageLoader.this.fileProgresses.put(this.cacheImage.url, var1);
         AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$HttpImageTask$5HGicoisjrTisboRYDJXYCptNjk(this, var1));
      }

      protected void onCancelled() {
         ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$hp6Qb_emVVm8eUAVBNZgyYyMaoI(this));
         Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$a_vnJl4U3DAZgDJvr8vdGGH5i2s(this));
      }

      protected void onPostExecute(Boolean var1) {
         if (!var1 && this.canRetry) {
            ImageLoader.this.httpFileLoadError(this.cacheImage.url);
         } else {
            ImageLoader var2 = ImageLoader.this;
            ImageLoader.CacheImage var3 = this.cacheImage;
            var2.fileDidLoaded(var3.url, var3.finalFilePath, 0);
         }

         Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn_IXbR_DnQ0(this, var1));
         ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$Z2AJLo51Bz13ruMq5jl4ihFrKyc(this));
      }
   }

   private class ThumbGenerateInfo {
      private boolean big;
      private String filter;
      private ArrayList imageReceiverArray;
      private TLRPC.Document parentDocument;

      private ThumbGenerateInfo() {
         this.imageReceiverArray = new ArrayList();
      }

      // $FF: synthetic method
      ThumbGenerateInfo(Object var2) {
         this();
      }

      // $FF: synthetic method
      static boolean access$1100(ImageLoader.ThumbGenerateInfo var0) {
         return var0.big;
      }
   }

   private class ThumbGenerateTask implements Runnable {
      private ImageLoader.ThumbGenerateInfo info;
      private int mediaType;
      private File originalPath;

      public ThumbGenerateTask(int var2, File var3, ImageLoader.ThumbGenerateInfo var4) {
         this.mediaType = var2;
         this.originalPath = var3;
         this.info = var4;
      }

      private void removeTask() {
         ImageLoader.ThumbGenerateInfo var1 = this.info;
         if (var1 != null) {
            String var2 = FileLoader.getAttachFileName(var1.parentDocument);
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ThumbGenerateTask$wNtiv0w_w5JLAy5lgehk_MfB6UY(this, var2));
         }
      }

      // $FF: synthetic method
      public void lambda$removeTask$0$ImageLoader$ThumbGenerateTask(String var1) {
         ImageLoader.ThumbGenerateTask var10000 = (ImageLoader.ThumbGenerateTask)ImageLoader.this.thumbGenerateTasks.remove(var1);
      }

      // $FF: synthetic method
      public void lambda$run$1$ImageLoader$ThumbGenerateTask(String var1, ArrayList var2, BitmapDrawable var3) {
         this.removeTask();
         String var4 = var1;
         if (this.info.filter != null) {
            StringBuilder var6 = new StringBuilder();
            var6.append(var1);
            var6.append("@");
            var6.append(this.info.filter);
            var4 = var6.toString();
         }

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            ((ImageReceiver)var2.get(var5)).setImageBitmapByKey(var3, var4, 0, false);
         }

         ImageLoader.this.memCache.put(var4, var3);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
