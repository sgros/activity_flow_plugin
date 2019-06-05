package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class InMemoryTileCache implements TileCache {
   private static final float LOAD_FACTOR = 0.6F;
   private static final int TILE_SIZE_IN_BYTES = 131072;
   private final List bitmapPool;
   private final ByteBuffer byteBuffer;
   private final int capacity;
   private final Map map;

   public InMemoryTileCache(int var1) {
      this.capacity = getCapacity(var1);
      this.bitmapPool = createBitmapPool(this.capacity + 1);
      this.map = createMap(this.capacity, this.bitmapPool);
      this.byteBuffer = ByteBuffer.allocate(131072);
   }

   private static List createBitmapPool(int var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0; ++var2) {
         var1.add(Bitmap.createBitmap(256, 256, Config.RGB_565));
      }

      return var1;
   }

   private static Map createMap(final int var0, final List var1) {
      return new LinkedHashMap((int)((float)var0 / 0.6F) + 2, 0.6F, true) {
         private static final long serialVersionUID = 1L;

         protected boolean removeEldestEntry(Entry var1x) {
            if (this.size() > var0) {
               this.remove(var1x.getKey());
               var1.add(var1x.getValue());
            }

            return false;
         }
      };
   }

   private static int getCapacity(int var0) {
      if (var0 < 0) {
         throw new IllegalArgumentException("capacity must not be negative: " + var0);
      } else {
         return var0;
      }
   }

   public boolean containsKey(MapGeneratorJob param1) {
      // $FF: Couldn't be decompiled
   }

   public void destroy() {
      Map var1 = this.map;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label395: {
         Iterator var2;
         try {
            var2 = this.map.values().iterator();
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label395;
         }

         label394:
         while(true) {
            try {
               if (var2.hasNext()) {
                  ((Bitmap)var2.next()).recycle();
                  continue;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break;
            }

            try {
               this.map.clear();
               var2 = this.bitmapPool.iterator();
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break;
            }

            while(true) {
               try {
                  if (!var2.hasNext()) {
                     break;
                  }

                  ((Bitmap)var2.next()).recycle();
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label394;
               }
            }

            try {
               this.bitmapPool.clear();
               return;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var45 = var10000;

         try {
            throw var45;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public Bitmap get(MapGeneratorJob param1) {
      // $FF: Couldn't be decompiled
   }

   public int getCapacity() {
      return this.capacity;
   }

   public boolean isPersistent() {
      return false;
   }

   public void put(MapGeneratorJob var1, Bitmap var2) {
      if (this.capacity != 0) {
         Map var3 = this.map;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label145: {
            try {
               if (this.bitmapPool.isEmpty()) {
                  return;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label145;
            }

            label139:
            try {
               Bitmap var4 = (Bitmap)this.bitmapPool.remove(this.bitmapPool.size() - 1);
               this.byteBuffer.rewind();
               var2.copyPixelsToBuffer(this.byteBuffer);
               this.byteBuffer.rewind();
               var4.copyPixelsFromBuffer(this.byteBuffer);
               this.map.put(var1, var4);
               return;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label139;
            }
         }

         while(true) {
            Throwable var17 = var10000;

            try {
               throw var17;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void setCapacity(int var1) {
      throw new UnsupportedOperationException();
   }

   public void setPersistent(boolean var1) {
      throw new UnsupportedOperationException();
   }
}
