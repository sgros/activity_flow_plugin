package org.mozilla.fileutils;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.WebStorage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.mozilla.threadutils.ThreadUtils;

public class FileUtils {
   public static boolean canReadExternalStorage(Context var0) {
      int var1 = VERSION.SDK_INT;
      boolean var2 = true;
      if (var1 >= 23) {
         if (var0.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            var2 = false;
         }

         return var2;
      } else {
         return true;
      }
   }

   public static long clearCache(Context var0) {
      WebStorage.getInstance().deleteAllData();
      return deleteWebViewCacheDirectory(var0);
   }

   public static boolean copy(File param0, File param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean copy(InputStream var0, OutputStream var1) {
      byte[] var2 = new byte[1024];

      while(true) {
         IOException var10000;
         label31: {
            boolean var10001;
            int var3;
            try {
               var3 = var0.read(var2);
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label31;
            }

            if (var3 == -1) {
               return true;
            }

            try {
               var1.write(var2, 0, var3);
               continue;
            } catch (IOException var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         IOException var6 = var10000;
         var6.printStackTrace();
         return false;
      }
   }

   private static boolean deleteContent(File var0) {
      String[] var1 = var0.list();
      int var2 = 0;
      if (var1 == null) {
         return false;
      } else {
         int var3 = var1.length;

         boolean var4;
         for(var4 = true; var2 < var3; ++var2) {
            File var5 = new File(var0, var1[var2]);
            if (var5.isDirectory()) {
               var4 &= deleteDirectory(var5);
            } else {
               var4 &= var5.delete();
            }
         }

         return var4;
      }
   }

   private static long deleteContentOnly(File var0) {
      String[] var1 = var0.list();
      long var2 = 0L;
      if (var1 == null) {
         return 0L;
      } else {
         int var4 = var1.length;

         long var7;
         for(int var5 = 0; var5 < var4; var2 = var7) {
            File var6 = new File(var0, var1[var5]);
            if (var6.isDirectory()) {
               var7 = var2 + deleteContentOnly(var6);
            } else {
               long var9 = var6.length();
               var7 = var2;
               if (var6.delete()) {
                  var7 = var2 + var9;
               }
            }

            ++var5;
         }

         return var2;
      }
   }

   public static boolean deleteDirectory(File var0) {
      boolean var1;
      if (deleteContent(var0) && var0.delete()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static long deleteWebViewCacheDirectory(Context var0) {
      File var1 = new File(var0.getApplicationInfo().dataDir, "cache");
      return !var1.exists() ? -1L : deleteContentOnly(var1);
   }

   public static boolean ensureDir(File var0) {
      boolean var1 = var0.mkdirs();
      boolean var2 = true;
      if (var1) {
         return true;
      } else {
         if (!var0.exists() || !var0.isDirectory() || !var0.canWrite()) {
            var2 = false;
         }

         return var2;
      }
   }

   public static HashMap fromJsonOnDisk(String param0) throws Exception {
      // $FF: Couldn't be decompiled
   }

   public static File getFaviconFolder(Context var0) {
      File var1 = new File(var0.getFilesDir(), "favicons");
      return !ensureDir(var1) ? var0.getCacheDir() : var1;
   }

   public static File getFileSlot(File var0, String var1) {
      File var2 = new File(var0, var1);
      if (!var2.exists()) {
         return var2;
      } else {
         StringBuilder var4;
         for(int var3 = 1; var3 < 1000; ++var3) {
            var4 = new StringBuilder();
            var4.append(var3);
            var4.append("-");
            var4.append(var1);
            var2 = new File(var0, var4.toString());
            if (!var2.exists()) {
               return var2;
            }
         }

         var4 = new StringBuilder();
         var4.append("Not-lucky-");
         var4.append(var1);
         return getFileSlot(var0, var4.toString());
      }
   }

   public static void notifyMediaScanner(Context var0, String var1) {
      MediaScannerConnection.scanFile(var0, new String[]{var1}, new String[]{null}, (OnScanCompletedListener)null);
   }

   public static Bundle readBundleFromStorage(File param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static String readStringFromFile(File param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static void writeBundleToStorage(File param0, String param1, Bundle param2) {
      // $FF: Couldn't be decompiled
   }

   public static void writeStringToFile(File param0, String param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   public static class DeleteFileRunnable extends FileUtils.FileIORunnable {
      public DeleteFileRunnable(File var1) {
         super(var1, null);
      }

      protected void doIO(File var1) {
         if (var1.exists()) {
            if (!var1.delete()) {
               Log.e("DeleteFileRunnable", "Failed to delete file");
            }

         }
      }
   }

   public static class DeleteFolderRunnable extends FileUtils.FileIORunnable {
      public DeleteFolderRunnable(File var1) {
         super(var1, null);
      }

      protected void doIO(File var1) {
         FileUtils.deleteContent(var1);
      }
   }

   private abstract static class FileIORunnable implements Runnable {
      private File file;

      private FileIORunnable(File var1) {
         this.file = var1;
      }

      // $FF: synthetic method
      FileIORunnable(File var1, Object var2) {
         this(var1);
      }

      protected abstract void doIO(File var1);

      public void run() {
         this.doIO(this.file);
      }
   }

   public static class GetCache extends FileUtils.GetFile {
      public GetCache(WeakReference var1) {
         super(var1, null);
      }

      protected File getFile(Context var1) {
         return var1.getCacheDir();
      }
   }

   public static class GetFaviconFolder extends FileUtils.GetFile {
      public GetFaviconFolder(WeakReference var1) {
         super(var1, null);
      }

      protected File getFile(Context var1) {
         return FileUtils.getFaviconFolder(var1);
      }
   }

   private abstract static class GetFile {
      private Future getFileFuture;

      private GetFile(WeakReference var1) {
         this.getFileFuture = ThreadUtils.postToBackgroundThread((Callable)(new _$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U(this, var1)));
      }

      // $FF: synthetic method
      GetFile(WeakReference var1, Object var2) {
         this(var1);
      }

      // $FF: synthetic method
      public static File lambda$new$0(FileUtils.GetFile var0, WeakReference var1) throws Exception {
         Context var2 = (Context)var1.get();
         return var2 == null ? null : var0.getFile(var2);
      }

      public File get() throws ExecutionException, InterruptedException {
         return (File)this.getFileFuture.get();
      }

      protected abstract File getFile(Context var1);
   }

   private static class LiveDataTask extends AsyncTask {
      private FileUtils.LiveDataTask.Function function;
      private MutableLiveData liveData;

      protected LiveDataTask(MutableLiveData var1, FileUtils.LiveDataTask.Function var2) {
         this.liveData = var1;
         this.function = var2;
      }

      protected Object doInBackground(Void... var1) {
         throw new IllegalStateException("LiveDataTask should not be instantiated");
      }

      protected void onPostExecute(Object var1) {
         this.liveData.setValue(this.function.apply(var1));
      }

      public interface Function {
         Object apply(Object var1);
      }
   }

   public static class ReadStringFromFileTask extends FileUtils.LiveDataTask {
      private File dir;
      private String fileName;

      public ReadStringFromFileTask(File var1, String var2, MutableLiveData var3, FileUtils.LiveDataTask.Function var4) {
         super(var3, var4);
         this.dir = var1;
         this.fileName = var2;
      }

      protected String doInBackground(Void... var1) {
         return FileUtils.readStringFromFile(this.dir, this.fileName);
      }
   }

   public static class WriteStringToFileRunnable extends FileUtils.FileIORunnable {
      private String string;

      public WriteStringToFileRunnable(File var1, String var2) {
         super(var1, null);
         this.string = var2;
      }

      protected void doIO(File var1) {
         FileUtils.writeStringToFile(var1.getParentFile(), var1.getName(), this.string);
      }
   }
}
