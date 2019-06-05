package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import java.io.File;

final class HardwareConfigState {
   private static final File FD_SIZE_LIST = new File("/proc/self/fd");
   private static volatile HardwareConfigState instance;
   private volatile int decodesSinceLastFdCheck;
   private volatile boolean isHardwareConfigAllowed = true;

   private HardwareConfigState() {
   }

   static HardwareConfigState getInstance() {
      if (instance == null) {
         synchronized(HardwareConfigState.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  HardwareConfigState var0 = new HardwareConfigState();
                  instance = var0;
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }

   private boolean isFdSizeBelowHardwareLimit() {
      synchronized(this){}

      Throwable var10000;
      label219: {
         int var1;
         boolean var10001;
         try {
            var1 = this.decodesSinceLastFdCheck + 1;
            this.decodesSinceLastFdCheck = var1;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label219;
         }

         boolean var2;
         if (var1 >= 50) {
            var2 = false;

            try {
               this.decodesSinceLastFdCheck = 0;
               var1 = FD_SIZE_LIST.list().length;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label219;
            }

            if (var1 < 700) {
               var2 = true;
            }

            try {
               this.isHardwareConfigAllowed = var2;
               if (!this.isHardwareConfigAllowed && Log.isLoggable("Downsampler", 5)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
                  var3.append(var1);
                  var3.append(", limit ");
                  var3.append(700);
                  Log.w("Downsampler", var3.toString());
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label219;
            }
         }

         label202:
         try {
            var2 = this.isHardwareConfigAllowed;
            return var2;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label202;
         }
      }

      Throwable var24 = var10000;
      throw var24;
   }

   @TargetApi(26)
   boolean setHardwareConfigIfAllowed(int var1, int var2, Options var3, DecodeFormat var4, boolean var5, boolean var6) {
      if (var5 && VERSION.SDK_INT >= 26 && var4 != DecodeFormat.PREFER_ARGB_8888_DISALLOW_HARDWARE && !var6) {
         if (var1 >= 128 && var2 >= 128 && this.isFdSizeBelowHardwareLimit()) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5) {
            var3.inPreferredConfig = Config.HARDWARE;
            var3.inMutable = false;
         }

         return var5;
      } else {
         return false;
      }
   }
}
