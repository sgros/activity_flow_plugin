package org.osmdroid.tileprovider.tilesource;

import java.util.concurrent.Semaphore;

public abstract class OnlineTileSourceBase extends BitmapTileSourceBase {
   private final String[] mBaseUrls;
   private final Semaphore mSemaphore;
   private final TileSourcePolicy mTileSourcePolicy;

   public OnlineTileSourceBase(String var1, int var2, int var3, int var4, String var5, String[] var6) {
      this(var1, var2, var3, var4, var5, var6, (String)null);
   }

   public OnlineTileSourceBase(String var1, int var2, int var3, int var4, String var5, String[] var6, String var7) {
      this(var1, var2, var3, var4, var5, var6, var7, new TileSourcePolicy());
   }

   public OnlineTileSourceBase(String var1, int var2, int var3, int var4, String var5, String[] var6, String var7, TileSourcePolicy var8) {
      super(var1, var2, var3, var4, var5, var7);
      this.mBaseUrls = var6;
      this.mTileSourcePolicy = var8;
      if (this.mTileSourcePolicy.getMaxConcurrent() > 0) {
         this.mSemaphore = new Semaphore(this.mTileSourcePolicy.getMaxConcurrent(), true);
      } else {
         this.mSemaphore = null;
      }

   }

   public void acquire() throws InterruptedException {
      Semaphore var1 = this.mSemaphore;
      if (var1 != null) {
         var1.acquire();
      }
   }

   public String getBaseUrl() {
      String[] var1 = this.mBaseUrls;
      return var1[super.random.nextInt(var1.length)];
   }

   public TileSourcePolicy getTileSourcePolicy() {
      return this.mTileSourcePolicy;
   }

   public abstract String getTileURLString(long var1);

   public void release() {
      Semaphore var1 = this.mSemaphore;
      if (var1 != null) {
         var1.release();
      }
   }
}
