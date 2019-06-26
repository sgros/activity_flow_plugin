package org.telegram.messenger;

import android.app.IntentService;
import android.content.Intent;
import android.util.SparseArray;
import java.io.File;

public class ClearCacheService extends IntentService {
   public ClearCacheService() {
      super("ClearCacheService");
   }

   // $FF: synthetic method
   static void lambda$onHandleIntent$0(int var0) {
      byte var8;
      if (var0 == 0) {
         var8 = 7;
      } else if (var0 == 1) {
         var8 = 30;
      } else {
         var8 = 3;
      }

      long var1 = System.currentTimeMillis() / 1000L;
      long var3 = (long)(var8 * 86400);
      SparseArray var5 = ImageLoader.getInstance().createMediaPaths();

      for(var0 = 0; var0 < var5.size(); ++var0) {
         if (var5.keyAt(var0) != 4) {
            try {
               Utilities.clearDir(((File)var5.valueAt(var0)).getAbsolutePath(), 0, var1 - var3);
            } catch (Throwable var7) {
               FileLog.e(var7);
            }
         }
      }

   }

   protected void onHandleIntent(Intent var1) {
      ApplicationLoader.postInitApplication();
      int var2 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
      if (var2 != 2) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$ClearCacheService$eaqMxt0ELhhnRq_8qc8rSbdFYt0(var2));
      }
   }
}
