package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.SparseArray;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
   private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
   private static final SparseArray mActiveWakeLocks = new SparseArray();
   private static int mNextId = 1;

   public static boolean completeWakefulIntent(Intent var0) {
      boolean var1 = false;
      int var2 = var0.getIntExtra("android.support.content.wakelockid", 0);
      if (var2 != 0) {
         SparseArray var24 = mActiveWakeLocks;
         synchronized(var24){}

         Throwable var10000;
         boolean var10001;
         label224: {
            WakeLock var3;
            try {
               var3 = (WakeLock)mActiveWakeLocks.get(var2);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label224;
            }

            if (var3 != null) {
               try {
                  var3.release();
                  mActiveWakeLocks.remove(var2);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label224;
               }

               var1 = true;
            } else {
               try {
                  StringBuilder var26 = new StringBuilder();
                  Log.w("WakefulBroadcastReceiver", var26.append("No active wake lock id #").append(var2).toString());
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label224;
               }

               var1 = true;
            }

            return var1;
         }

         while(true) {
            Throwable var25 = var10000;

            try {
               throw var25;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static ComponentName startWakefulService(Context var0, Intent var1) {
      SparseArray var2 = mActiveWakeLocks;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label307: {
         int var3;
         try {
            var3 = mNextId++;
            if (mNextId <= 0) {
               mNextId = 1;
            }
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label307;
         }

         ComponentName var38;
         try {
            var1.putExtra("android.support.content.wakelockid", var3);
            var38 = var0.startService(var1);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label307;
         }

         ComponentName var35;
         if (var38 == null) {
            var35 = null;

            try {
               ;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label307;
            }
         } else {
            try {
               PowerManager var4 = (PowerManager)var0.getSystemService("power");
               StringBuilder var37 = new StringBuilder();
               WakeLock var39 = var4.newWakeLock(1, var37.append("wake:").append(var38.flattenToShortString()).toString());
               var39.setReferenceCounted(false);
               var39.acquire(60000L);
               mActiveWakeLocks.put(var3, var39);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label307;
            }

            var35 = var38;
         }

         return var35;
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }
}
