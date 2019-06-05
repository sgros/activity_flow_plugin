package com.davemorrissey.labs.subscaleview.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

public class SkiaImageRegionDecoder implements ImageRegionDecoder {
   private BitmapRegionDecoder decoder;
   private final Object decoderLock = new Object();

   public Bitmap decodeRegion(Rect var1, int var2) {
      Object var3 = this.decoderLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label163: {
         Bitmap var25;
         try {
            Options var4 = new Options();
            var4.inSampleSize = var2;
            var4.inPreferredConfig = Config.RGB_565;
            var25 = this.decoder.decodeRegion(var1, var4);
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label163;
         }

         if (var25 != null) {
            label157:
            try {
               return var25;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label157;
            }
         } else {
            label159:
            try {
               RuntimeException var27 = new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
               throw var27;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label159;
            }
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public Point init(Context param1, Uri param2) throws Exception {
      // $FF: Couldn't be decompiled
   }

   public boolean isReady() {
      boolean var1;
      if (this.decoder != null && !this.decoder.isRecycled()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void recycle() {
      this.decoder.recycle();
   }
}
