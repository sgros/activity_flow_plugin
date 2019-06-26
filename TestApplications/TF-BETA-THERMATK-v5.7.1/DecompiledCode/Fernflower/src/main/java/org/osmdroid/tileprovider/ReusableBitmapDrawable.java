package org.osmdroid.tileprovider;

import android.graphics.Bitmap;

public class ReusableBitmapDrawable extends ExpirableBitmapDrawable {
   private boolean mBitmapRecycled = false;
   private int mUsageRefCount = 0;

   public ReusableBitmapDrawable(Bitmap var1) {
      super(var1);
   }

   public void beginUsingDrawable() {
      // $FF: Couldn't be decompiled
   }

   public void finishUsingDrawable() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            --this.mUsageRefCount;
            if (this.mUsageRefCount >= 0) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            IllegalStateException var14 = new IllegalStateException("Unbalanced endUsingDrawable() called.");
            throw var14;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isBitmapValid() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var1;
         label133: {
            label132: {
               try {
                  if (!this.mBitmapRecycled) {
                     break label132;
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label134;
               }

               var1 = false;
               break label133;
            }

            var1 = true;
         }

         label126:
         try {
            return var1;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public Bitmap tryRecycle() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.mUsageRefCount == 0) {
               this.mBitmapRecycled = true;
               Bitmap var14 = this.getBitmap();
               return var14;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            return null;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }
}
