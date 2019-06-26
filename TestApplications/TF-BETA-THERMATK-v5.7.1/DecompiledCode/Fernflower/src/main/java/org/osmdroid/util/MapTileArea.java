package org.osmdroid.util;

import android.graphics.Rect;
import java.util.Iterator;

public class MapTileArea implements MapTileContainer, IterableWithSize {
   private int mHeight;
   private int mLeft;
   private int mMapTileUpperBound;
   private int mTop;
   private int mWidth;
   private int mZoom;

   private int cleanValue(int var1) {
      while(true) {
         int var2 = var1;
         if (var1 >= 0) {
            while(true) {
               var1 = this.mMapTileUpperBound;
               if (var2 < var1) {
                  return var2;
               }

               var2 -= var1;
            }
         }

         var1 += this.mMapTileUpperBound;
      }
   }

   private int computeSize(int var1, int var2) {
      while(var1 > var2) {
         var2 += this.mMapTileUpperBound;
      }

      return Math.min(this.mMapTileUpperBound, var2 - var1 + 1);
   }

   private boolean contains(int var1, int var2, int var3) {
      while(var1 < var2) {
         var1 += this.mMapTileUpperBound;
      }

      boolean var4;
      if (var1 < var2 + var3) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean contains(long var1) {
      if (MapTileIndex.getZoom(var1) != this.mZoom) {
         return false;
      } else {
         return !this.contains(MapTileIndex.getX(var1), this.mLeft, this.mWidth) ? false : this.contains(MapTileIndex.getY(var1), this.mTop, this.mHeight);
      }
   }

   public int getBottom() {
      return (this.mTop + this.mHeight) % this.mMapTileUpperBound;
   }

   public int getHeight() {
      return this.mHeight;
   }

   public int getLeft() {
      return this.mLeft;
   }

   public int getRight() {
      return (this.mLeft + this.mWidth) % this.mMapTileUpperBound;
   }

   public int getTop() {
      return this.mTop;
   }

   public int getWidth() {
      return this.mWidth;
   }

   public int getZoom() {
      return this.mZoom;
   }

   public Iterator iterator() {
      return new Iterator() {
         private int mIndex;

         public boolean hasNext() {
            boolean var1;
            if (this.mIndex < MapTileArea.this.size()) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         }

         public Long next() {
            if (!this.hasNext()) {
               return null;
            } else {
               int var1 = MapTileArea.this.mLeft + this.mIndex % MapTileArea.this.mWidth;
               int var2 = MapTileArea.this.mTop + this.mIndex / MapTileArea.this.mWidth;
               ++this.mIndex;

               while(true) {
                  int var3 = var2;
                  if (var1 < MapTileArea.this.mMapTileUpperBound) {
                     while(var3 >= MapTileArea.this.mMapTileUpperBound) {
                        var3 -= MapTileArea.this.mMapTileUpperBound;
                     }

                     return MapTileIndex.getTileIndex(MapTileArea.this.mZoom, var1, var3);
                  }

                  var1 -= MapTileArea.this.mMapTileUpperBound;
               }
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public MapTileArea reset() {
      this.mWidth = 0;
      return this;
   }

   public MapTileArea set(int var1, int var2, int var3, int var4, int var5) {
      this.mZoom = var1;
      this.mMapTileUpperBound = 1 << this.mZoom;
      this.mWidth = this.computeSize(var2, var4);
      this.mHeight = this.computeSize(var3, var5);
      this.mLeft = this.cleanValue(var2);
      this.mTop = this.cleanValue(var3);
      return this;
   }

   public MapTileArea set(int var1, Rect var2) {
      this.set(var1, var2.left, var2.top, var2.right, var2.bottom);
      return this;
   }

   public MapTileArea set(MapTileArea var1) {
      if (var1.size() == 0) {
         this.reset();
         return this;
      } else {
         this.set(var1.mZoom, var1.mLeft, var1.mTop, var1.getRight(), var1.getBottom());
         return this;
      }
   }

   public int size() {
      return this.mWidth * this.mHeight;
   }

   public String toString() {
      if (this.mWidth == 0) {
         return "MapTileArea:empty";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("MapTileArea:zoom=");
         var1.append(this.mZoom);
         var1.append(",left=");
         var1.append(this.mLeft);
         var1.append(",top=");
         var1.append(this.mTop);
         var1.append(",width=");
         var1.append(this.mWidth);
         var1.append(",height=");
         var1.append(this.mHeight);
         return var1.toString();
      }
   }
}
