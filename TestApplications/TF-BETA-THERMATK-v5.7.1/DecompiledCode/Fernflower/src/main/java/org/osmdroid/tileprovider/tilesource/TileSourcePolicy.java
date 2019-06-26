package org.osmdroid.tileprovider.tilesource;

public class TileSourcePolicy {
   private final int mFlags;
   private final int mMaxConcurrent;

   public TileSourcePolicy() {
      this(0, 0);
   }

   public TileSourcePolicy(int var1, int var2) {
      this.mMaxConcurrent = var1;
      this.mFlags = var2;
   }

   private boolean acceptsMeaninglessUserAgent() {
      boolean var1;
      if ((this.mFlags & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean acceptsPreventive() {
      boolean var1;
      if ((this.mFlags & 2) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean acceptsUserAgent(String var1) {
      boolean var2 = this.acceptsMeaninglessUserAgent();
      boolean var3 = true;
      if (var2) {
         return true;
      } else {
         if (var1 == null || var1.trim().length() <= 0 || var1.equals("osmdroid")) {
            var3 = false;
         }

         return var3;
      }
   }

   public int getMaxConcurrent() {
      return this.mMaxConcurrent;
   }

   public boolean normalizesUserAgent() {
      boolean var1;
      if ((this.mFlags & 8) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
