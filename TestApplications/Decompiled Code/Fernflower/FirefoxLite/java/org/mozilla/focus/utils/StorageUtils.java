package org.mozilla.focus.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import org.mozilla.fileutils.FileUtils;

public class StorageUtils {
   public static File getAppMediaDirOnRemovableStorage(Context var0) throws NoRemovableStorageException {
      File var1 = getFirstRemovableMedia(var0);
      if (var1 != null) {
         if ("mounted".equals(Environment.getExternalStorageState(var1))) {
            return var1;
         } else {
            throw new NoRemovableStorageException("No mounted-removable media to use");
         }
      } else {
         throw new NoRemovableStorageException("No removable media to use");
      }
   }

   static File getFirstRemovableMedia(Context var0) {
      File[] var6 = var0.getExternalMediaDirs();
      int var1 = var6.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         File var3 = var6[var2];
         if (var3 != null) {
            boolean var4;
            try {
               var4 = Environment.isExternalStorageRemovable(var3);
            } catch (IllegalArgumentException var5) {
               var5.printStackTrace();
               continue;
            }

            if (var4) {
               return var3;
            }
         }
      }

      return null;
   }

   public static File getTargetDirForSaveScreenshot(Context var0) {
      String var1 = var0.getString(2131755062).replaceAll(" ", "");
      File var2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), var1);
      FileUtils.ensureDir(var2);
      if (!Settings.getInstance(var0).shouldSaveToRemovableStorage()) {
         return var2;
      } else {
         try {
            File var4 = new File(getAppMediaDirOnRemovableStorage(var0), var1);
            return var4;
         } catch (NoRemovableStorageException var3) {
            return var2;
         }
      }
   }

   public static File getTargetDirOnRemovableStorageForDownloads(Context var0, String var1) throws NoRemovableStorageException {
      if (!Settings.getInstance(var0).shouldSaveToRemovableStorage()) {
         return null;
      } else {
         File var2 = new File(getAppMediaDirOnRemovableStorage(var0), "downloads");
         return MimeUtils.isImage(var1) ? new File(var2, "pictures") : new File(var2, "others");
      }
   }
}
