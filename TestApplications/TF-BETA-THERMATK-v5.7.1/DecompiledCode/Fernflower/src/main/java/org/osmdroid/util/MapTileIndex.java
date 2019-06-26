package org.osmdroid.util;

public class MapTileIndex {
   public static int mMaxZoomLevel;
   private static int mModulo;

   static {
      mModulo = 1 << mMaxZoomLevel;
   }

   private static void checkValues(int var0, int var1, int var2) {
      if (var0 >= 0 && var0 <= mMaxZoomLevel) {
         long var3 = (long)(1 << var0);
         if (var1 >= 0 && (long)var1 < var3) {
            if (var2 < 0 || (long)var2 >= var3) {
               throwIllegalValue(var0, var2, "Y");
               throw null;
            }
         } else {
            throwIllegalValue(var0, var1, "X");
            throw null;
         }
      } else {
         throwIllegalValue(var0, var0, "Zoom");
         throw null;
      }
   }

   public static long getTileIndex(int var0, int var1, int var2) {
      checkValues(var0, var1, var2);
      long var3 = (long)var0;
      var0 = mMaxZoomLevel;
      return (var3 << var0 * 2) + ((long)var1 << var0) + (long)var2;
   }

   public static int getX(long var0) {
      return (int)((var0 >> mMaxZoomLevel) % (long)mModulo);
   }

   public static int getY(long var0) {
      return (int)(var0 % (long)mModulo);
   }

   public static int getZoom(long var0) {
      return (int)(var0 >> mMaxZoomLevel * 2);
   }

   private static void throwIllegalValue(int var0, int var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("MapTileIndex: ");
      var3.append(var2);
      var3.append(" (");
      var3.append(var1);
      var3.append(") is too big (zoom=");
      var3.append(var0);
      var3.append(")");
      throw new IllegalArgumentException(var3.toString());
   }

   public static String toString(int var0, int var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("/");
      var3.append(var0);
      var3.append("/");
      var3.append(var1);
      var3.append("/");
      var3.append(var2);
      return var3.toString();
   }

   public static String toString(long var0) {
      return toString(getZoom(var0), getX(var0), getY(var0));
   }
}
