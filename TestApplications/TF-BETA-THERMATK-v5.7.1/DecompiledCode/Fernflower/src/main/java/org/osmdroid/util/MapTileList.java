package org.osmdroid.util;

public class MapTileList implements MapTileContainer {
   private int mSize;
   private long[] mTileIndices;

   public void clear() {
      this.mSize = 0;
   }

   public boolean contains(long var1) {
      if (this.mTileIndices == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < this.mSize; ++var3) {
            if (this.mTileIndices[var3] == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public void ensureCapacity(int var1) {
      if (var1 != 0) {
         long[] var2 = this.mTileIndices;
         if (var2 == null || var2.length < var1) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label159: {
               try {
                  var2 = new long[var1];
                  if (this.mTileIndices != null) {
                     System.arraycopy(this.mTileIndices, 0, var2, 0, this.mTileIndices.length);
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label159;
               }

               label156:
               try {
                  this.mTileIndices = var2;
                  return;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label156;
               }
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public long get(int var1) {
      return this.mTileIndices[var1];
   }

   public int getSize() {
      return this.mSize;
   }

   public void put(long var1) {
      this.ensureCapacity(this.mSize + 1);
      long[] var3 = this.mTileIndices;
      int var4 = this.mSize++;
      var3[var4] = var1;
   }
}
