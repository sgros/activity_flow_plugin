package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;

public class Bitmaps {
   protected static byte[] footer = new byte[]{-1, -39};
   protected static byte[] header = new byte[]{-1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, -1, -37, 0, 67, 0, 40, 28, 30, 35, 30, 25, 40, 35, 33, 35, 45, 43, 40, 48, 60, 100, 65, 60, 55, 55, 60, 123, 88, 93, 73, 100, -111, -128, -103, -106, -113, -128, -116, -118, -96, -76, -26, -61, -96, -86, -38, -83, -118, -116, -56, -1, -53, -38, -18, -11, -1, -1, -1, -101, -63, -1, -1, -1, -6, -1, -26, -3, -1, -8, -1, -37, 0, 67, 1, 43, 45, 45, 60, 53, 60, 118, 65, 65, 118, -8, -91, -116, -91, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -1, -64, 0, 17, 8, 0, 30, 0, 40, 3, 1, 34, 0, 2, 17, 1, 3, 17, 1, -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0};
   private static final ThreadLocal jpegData = new ThreadLocal() {
      protected byte[] initialValue() {
         return new byte[]{-1, -40, -1, -37, 0, 67, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -64, 0, 17, 8, 0, 0, 0, 0, 3, 1, 34, 0, 2, 17, 0, 3, 17, 0, -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0, -114, -118, 40, -96, 15, -1, -39};
      }
   };
   private static volatile Matrix sScaleMatrix;

   private static void checkWidthHeight(int var0, int var1) {
      if (var0 > 0) {
         if (var1 <= 0) {
            throw new IllegalArgumentException("height must be > 0");
         }
      } else {
         throw new IllegalArgumentException("width must be > 0");
      }
   }

   private static void checkXYSign(int var0, int var1) {
      if (var0 >= 0) {
         if (var1 < 0) {
            throw new IllegalArgumentException("y must be >= 0");
         }
      } else {
         throw new IllegalArgumentException("x must be >= 0");
      }
   }

   public static Bitmap createBitmap(int var0, int var1, Config var2) {
      Bitmap var5;
      if (VERSION.SDK_INT < 21) {
         Options var3 = new Options();
         var3.inDither = true;
         var3.inPreferredConfig = var2;
         var3.inPurgeable = true;
         var3.inSampleSize = 1;
         var3.inMutable = true;
         byte[] var4 = (byte[])jpegData.get();
         var4[76] = (byte)((byte)(var1 >> 8));
         var4[77] = (byte)((byte)(var1 & 255));
         var4[78] = (byte)((byte)(var0 >> 8));
         var4[79] = (byte)((byte)(var0 & 255));
         var5 = BitmapFactory.decodeByteArray(var4, 0, var4.length, var3);
         Utilities.pinBitmap(var5);
         var5.setHasAlpha(true);
         var5.eraseColor(0);
      } else {
         var5 = Bitmap.createBitmap(var0, var1, var2);
      }

      if (var2 == Config.ARGB_8888 || var2 == Config.ARGB_4444) {
         var5.eraseColor(0);
      }

      return var5;
   }

   public static Bitmap createBitmap(Bitmap var0, int var1, int var2, int var3, int var4) {
      return createBitmap(var0, var1, var2, var3, var4, (Matrix)null, false);
   }

   public static Bitmap createBitmap(Bitmap var0, int var1, int var2, int var3, int var4, Matrix var5, boolean var6) {
      if (VERSION.SDK_INT >= 21) {
         return Bitmap.createBitmap(var0, var1, var2, var3, var4, var5, var6);
      } else {
         checkXYSign(var1, var2);
         checkWidthHeight(var3, var4);
         int var7 = var1 + var3;
         if (var7 <= var0.getWidth()) {
            int var8 = var2 + var4;
            if (var8 > var0.getHeight()) {
               throw new IllegalArgumentException("y + height must be <= bitmap.height()");
            } else if (!var0.isMutable() && var1 == 0 && var2 == 0 && var3 == var0.getWidth() && var4 == var0.getHeight() && (var5 == null || var5.isIdentity())) {
               return var0;
            } else {
               Canvas var9 = new Canvas();
               Rect var10 = new Rect(var1, var2, var7, var8);
               RectF var11 = new RectF(0.0F, 0.0F, (float)var3, (float)var4);
               Config var12 = Config.ARGB_8888;
               Config var13 = var0.getConfig();
               if (var13 != null) {
                  var1 = null.$SwitchMap$android$graphics$Bitmap$Config[var13.ordinal()];
                  if (var1 != 1) {
                     if (var1 != 2) {
                        var12 = Config.ARGB_8888;
                     } else {
                        var12 = Config.ALPHA_8;
                     }
                  } else {
                     var12 = Config.ARGB_8888;
                  }
               }

               Paint var17;
               Bitmap var19;
               if (var5 != null && !var5.isIdentity()) {
                  boolean var16 = var5.rectStaysRect() ^ true;
                  RectF var14 = new RectF();
                  var5.mapRect(var14, var11);
                  var2 = Math.round(var14.width());
                  var3 = Math.round(var14.height());
                  if (var16) {
                     var12 = Config.ARGB_8888;
                  }

                  Bitmap var18 = createBitmap(var2, var3, var12);
                  var9.translate(-var14.left, -var14.top);
                  var9.concat(var5);
                  Paint var20 = new Paint();
                  var20.setFilterBitmap(var6);
                  var19 = var18;
                  var17 = var20;
                  if (var16) {
                     var20.setAntiAlias(true);
                     var19 = var18;
                     var17 = var20;
                  }
               } else {
                  var19 = createBitmap(var3, var4, var12);
                  var17 = null;
               }

               var19.setDensity(var0.getDensity());
               var19.setHasAlpha(var0.hasAlpha());
               if (VERSION.SDK_INT >= 19) {
                  var19.setPremultiplied(var0.isPremultiplied());
               }

               var9.setBitmap(var19);
               var9.drawBitmap(var0, var10, var11, var17);

               try {
                  var9.setBitmap((Bitmap)null);
               } catch (Exception var15) {
               }

               return var19;
            }
         } else {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
         }
      }
   }

   public static Bitmap createScaledBitmap(Bitmap param0, int param1, int param2, boolean param3) {
      // $FF: Couldn't be decompiled
   }
}
