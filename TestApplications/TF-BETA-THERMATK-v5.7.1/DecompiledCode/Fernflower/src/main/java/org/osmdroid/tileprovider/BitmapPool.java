package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.osmdroid.tileprovider.modules.ConfigurablePriorityThreadFactory;

public class BitmapPool {
   private static final BitmapPool sInstance = new BitmapPool();
   private final ExecutorService mExecutor = Executors.newFixedThreadPool(1, new ConfigurablePriorityThreadFactory(1, BitmapPool.class.getName()));
   private final LinkedList mPool = new LinkedList();

   private BitmapPool() {
   }

   public static BitmapPool getInstance() {
      return sInstance;
   }

   private void syncRecycle(Drawable var1) {
      if (var1 != null) {
         if (VERSION.SDK_INT <= 10 && var1 instanceof BitmapDrawable) {
            Bitmap var2 = ((BitmapDrawable)var1).getBitmap();
            if (var2 != null) {
               var2.recycle();
            }
         }

         if (var1 instanceof ReusableBitmapDrawable) {
            this.returnDrawableToPool((ReusableBitmapDrawable)var1);
         }

      }
   }

   public void applyReusableOptions(Options var1, int var2, int var3) {
      if (VERSION.SDK_INT >= 11) {
         var1.inBitmap = this.obtainSizedBitmapFromPool(var2, var3);
         var1.inSampleSize = 1;
         var1.inMutable = true;
      }

   }

   public void asyncRecycle(final Drawable var1) {
      if (var1 != null) {
         this.mExecutor.execute(new Runnable() {
            public void run() {
               BitmapPool.this.syncRecycle(var1);
            }
         });
      }
   }

   public Bitmap obtainSizedBitmapFromPool(int var1, int var2) {
      LinkedList var3 = this.mPool;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label416: {
         try {
            if (this.mPool.isEmpty()) {
               return null;
            }
         } catch (Throwable var47) {
            var10000 = var47;
            var10001 = false;
            break label416;
         }

         Iterator var4;
         try {
            var4 = this.mPool.iterator();
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label416;
         }

         while(true) {
            Bitmap var5;
            try {
               if (!var4.hasNext()) {
                  break;
               }

               var5 = (Bitmap)var4.next();
               if (var5.isRecycled()) {
                  this.mPool.remove(var5);
                  var5 = this.obtainSizedBitmapFromPool(var1, var2);
                  return var5;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label416;
            }

            try {
               if (var5.getWidth() == var1 && var5.getHeight() == var2) {
                  this.mPool.remove(var5);
                  return var5;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label416;
            }
         }

         label392:
         try {
            return null;
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label392;
         }
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            continue;
         }
      }
   }

   public void returnDrawableToPool(ReusableBitmapDrawable param1) {
      // $FF: Couldn't be decompiled
   }
}
