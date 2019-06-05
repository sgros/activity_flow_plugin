package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool implements BitmapPool {
   private static final Config DEFAULT_CONFIG;
   private final Set allowedConfigs;
   private int currentSize;
   private int evictions;
   private int hits;
   private final int initialMaxSize;
   private int maxSize;
   private int misses;
   private int puts;
   private final LruPoolStrategy strategy;
   private final LruBitmapPool.BitmapTracker tracker;

   static {
      DEFAULT_CONFIG = Config.ARGB_8888;
   }

   public LruBitmapPool(int var1) {
      this(var1, getDefaultStrategy(), getDefaultAllowedConfigs());
   }

   LruBitmapPool(int var1, LruPoolStrategy var2, Set var3) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
      this.strategy = var2;
      this.allowedConfigs = var3;
      this.tracker = new LruBitmapPool.NullBitmapTracker();
   }

   @TargetApi(26)
   private static void assertNotHardwareConfig(Config var0) {
      if (VERSION.SDK_INT >= 26) {
         if (var0 == Config.HARDWARE) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Cannot create a mutable Bitmap with config: ");
            var1.append(var0);
            var1.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
            throw new IllegalArgumentException(var1.toString());
         }
      }
   }

   private void dump() {
      if (Log.isLoggable("LruBitmapPool", 2)) {
         this.dumpUnchecked();
      }

   }

   private void dumpUnchecked() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Hits=");
      var1.append(this.hits);
      var1.append(", misses=");
      var1.append(this.misses);
      var1.append(", puts=");
      var1.append(this.puts);
      var1.append(", evictions=");
      var1.append(this.evictions);
      var1.append(", currentSize=");
      var1.append(this.currentSize);
      var1.append(", maxSize=");
      var1.append(this.maxSize);
      var1.append("\nStrategy=");
      var1.append(this.strategy);
      Log.v("LruBitmapPool", var1.toString());
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   @TargetApi(26)
   private static Set getDefaultAllowedConfigs() {
      HashSet var0 = new HashSet();
      var0.addAll(Arrays.asList(Config.values()));
      if (VERSION.SDK_INT >= 19) {
         var0.add((Object)null);
      }

      if (VERSION.SDK_INT >= 26) {
         var0.remove(Config.HARDWARE);
      }

      return Collections.unmodifiableSet(var0);
   }

   private static LruPoolStrategy getDefaultStrategy() {
      Object var0;
      if (VERSION.SDK_INT >= 19) {
         var0 = new SizeConfigStrategy();
      } else {
         var0 = new AttributeStrategy();
      }

      return (LruPoolStrategy)var0;
   }

   private Bitmap getDirtyOrNull(int var1, int var2, Config var3) {
      synchronized(this){}

      Throwable var10000;
      label584: {
         LruPoolStrategy var4;
         boolean var10001;
         try {
            assertNotHardwareConfig(var3);
            var4 = this.strategy;
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label584;
         }

         Config var5;
         if (var3 != null) {
            var5 = var3;
         } else {
            try {
               var5 = DEFAULT_CONFIG;
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break label584;
            }
         }

         Bitmap var80;
         try {
            var80 = var4.get(var1, var2, var5);
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label584;
         }

         StringBuilder var79;
         if (var80 == null) {
            try {
               if (Log.isLoggable("LruBitmapPool", 3)) {
                  var79 = new StringBuilder();
                  var79.append("Missing bitmap=");
                  var79.append(this.strategy.logBitmap(var1, var2, var3));
                  Log.d("LruBitmapPool", var79.toString());
               }
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label584;
            }

            try {
               ++this.misses;
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label584;
            }
         } else {
            try {
               ++this.hits;
               this.currentSize -= this.strategy.getSize(var80);
               this.tracker.remove(var80);
               normalize(var80);
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label584;
            }
         }

         try {
            if (Log.isLoggable("LruBitmapPool", 2)) {
               var79 = new StringBuilder();
               var79.append("Get bitmap=");
               var79.append(this.strategy.logBitmap(var1, var2, var3));
               Log.v("LruBitmapPool", var79.toString());
            }
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            break label584;
         }

         label555:
         try {
            this.dump();
            return var80;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label555;
         }
      }

      Throwable var78 = var10000;
      throw var78;
   }

   @TargetApi(19)
   private static void maybeSetPreMultiplied(Bitmap var0) {
      if (VERSION.SDK_INT >= 19) {
         var0.setPremultiplied(true);
      }

   }

   private static void normalize(Bitmap var0) {
      var0.setHasAlpha(true);
      maybeSetPreMultiplied(var0);
   }

   private void trimToSize(int var1) {
      synchronized(this){}

      Throwable var10000;
      while(true) {
         boolean var10001;
         Bitmap var2;
         label335: {
            try {
               if (this.currentSize > var1) {
                  var2 = this.strategy.removeLast();
                  break label335;
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break;
            }

            return;
         }

         if (var2 == null) {
            try {
               if (Log.isLoggable("LruBitmapPool", 5)) {
                  Log.w("LruBitmapPool", "Size mismatch, resetting");
                  this.dumpUnchecked();
               }
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break;
            }

            try {
               this.currentSize = 0;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break;
            }

            return;
         } else {
            try {
               this.tracker.remove(var2);
               this.currentSize -= this.strategy.getSize(var2);
               ++this.evictions;
               if (Log.isLoggable("LruBitmapPool", 3)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Evicting bitmap=");
                  var3.append(this.strategy.logBitmap(var2));
                  Log.d("LruBitmapPool", var3.toString());
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break;
            }

            try {
               this.dump();
               var2.recycle();
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var34 = var10000;
      throw var34;
   }

   public void clearMemory() {
      if (Log.isLoggable("LruBitmapPool", 3)) {
         Log.d("LruBitmapPool", "clearMemory");
      }

      this.trimToSize(0);
   }

   public Bitmap get(int var1, int var2, Config var3) {
      Bitmap var4 = this.getDirtyOrNull(var1, var2, var3);
      Bitmap var5;
      if (var4 != null) {
         var4.eraseColor(0);
         var5 = var4;
      } else {
         var5 = Bitmap.createBitmap(var1, var2, var3);
      }

      return var5;
   }

   public Bitmap getDirty(int var1, int var2, Config var3) {
      Bitmap var4 = this.getDirtyOrNull(var1, var2, var3);
      Bitmap var5 = var4;
      if (var4 == null) {
         var5 = Bitmap.createBitmap(var1, var2, var3);
      }

      return var5;
   }

   public void put(Bitmap var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != null) {
         label561: {
            label566: {
               StringBuilder var3;
               label569: {
                  try {
                     if (var1.isRecycled()) {
                        break label566;
                     }

                     if (var1.isMutable() && this.strategy.getSize(var1) <= this.maxSize && this.allowedConfigs.contains(var1.getConfig())) {
                        break label569;
                     }
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label561;
                  }

                  try {
                     if (Log.isLoggable("LruBitmapPool", 2)) {
                        var3 = new StringBuilder();
                        var3.append("Reject bitmap from pool, bitmap: ");
                        var3.append(this.strategy.logBitmap(var1));
                        var3.append(", is mutable: ");
                        var3.append(var1.isMutable());
                        var3.append(", is allowed config: ");
                        var3.append(this.allowedConfigs.contains(var1.getConfig()));
                        Log.v("LruBitmapPool", var3.toString());
                     }
                  } catch (Throwable var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label561;
                  }

                  try {
                     var1.recycle();
                  } catch (Throwable var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label561;
                  }

                  return;
               }

               try {
                  int var2 = this.strategy.getSize(var1);
                  this.strategy.put(var1);
                  this.tracker.add(var1);
                  ++this.puts;
                  this.currentSize += var2;
                  if (Log.isLoggable("LruBitmapPool", 2)) {
                     var3 = new StringBuilder();
                     var3.append("Put bitmap in pool=");
                     var3.append(this.strategy.logBitmap(var1));
                     Log.v("LruBitmapPool", var3.toString());
                  }
               } catch (Throwable var54) {
                  var10000 = var54;
                  var10001 = false;
                  break label561;
               }

               try {
                  this.dump();
                  this.evict();
               } catch (Throwable var53) {
                  var10000 = var53;
                  var10001 = false;
                  break label561;
               }

               return;
            }

            label545:
            try {
               IllegalStateException var60 = new IllegalStateException("Cannot pool recycled bitmap");
               throw var60;
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label545;
            }
         }
      } else {
         label563:
         try {
            NullPointerException var62 = new NullPointerException("Bitmap must not be null");
            throw var62;
         } catch (Throwable var59) {
            var10000 = var59;
            var10001 = false;
            break label563;
         }
      }

      Throwable var61 = var10000;
      throw var61;
   }

   @SuppressLint({"InlinedApi"})
   public void trimMemory(int var1) {
      if (Log.isLoggable("LruBitmapPool", 3)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("trimMemory, level=");
         var2.append(var1);
         Log.d("LruBitmapPool", var2.toString());
      }

      if (var1 >= 40) {
         this.clearMemory();
      } else if (var1 >= 20) {
         this.trimToSize(this.maxSize / 2);
      }

   }

   private interface BitmapTracker {
      void add(Bitmap var1);

      void remove(Bitmap var1);
   }

   private static class NullBitmapTracker implements LruBitmapPool.BitmapTracker {
      NullBitmapTracker() {
      }

      public void add(Bitmap var1) {
      }

      public void remove(Bitmap var1) {
      }
   }
}
