package okio;

import java.io.UnsupportedEncodingException;

final class Base64 {
   private static final byte[] MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] URL_MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

   private Base64() {
   }

   public static byte[] decode(String var0) {
      int var1;
      for(var1 = var0.length(); var1 > 0; --var1) {
         char var2 = var0.charAt(var1 - 1);
         if (var2 != '=' && var2 != '\n' && var2 != '\r' && var2 != ' ' && var2 != '\t') {
            break;
         }
      }

      byte[] var3 = new byte[(int)((long)var1 * 6L / 8L)];
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var11 = 0;

      byte[] var10;
      while(true) {
         int var8;
         if (var6 >= var1) {
            var4 %= 4;
            if (var4 == 1) {
               var10 = null;
            } else {
               if (var4 == 2) {
                  var8 = var11 + 1;
                  var3[var11] = (byte)((byte)(var5 << 12 >> 16));
                  var11 = var8;
               } else {
                  var8 = var11;
                  if (var4 == 3) {
                     var4 = var5 << 6;
                     var5 = var11 + 1;
                     var3[var11] = (byte)((byte)(var4 >> 16));
                     var8 = var5 + 1;
                     var3[var5] = (byte)((byte)(var4 >> 8));
                  }

                  var11 = var8;
               }

               var10 = var3;
               if (var11 != var3.length) {
                  var10 = new byte[var11];
                  System.arraycopy(var3, 0, var10, 0, var11);
               }
            }
            break;
         }

         label85: {
            int var9;
            label110: {
               char var7 = var0.charAt(var6);
               if (var7 >= 'A' && var7 <= 'Z') {
                  var8 = var7 - 65;
               } else if (var7 >= 'a' && var7 <= 'z') {
                  var8 = var7 - 71;
               } else if (var7 >= '0' && var7 <= '9') {
                  var8 = var7 + 4;
               } else if (var7 != '+' && var7 != '-') {
                  if (var7 != '/' && var7 != '_') {
                     var9 = var4;
                     var8 = var5;
                     if (var7 != '\n') {
                        var9 = var4;
                        var8 = var5;
                        if (var7 != '\r') {
                           var9 = var4;
                           var8 = var5;
                           if (var7 != ' ') {
                              if (var7 != '\t') {
                                 var10 = null;
                                 break;
                              }

                              var8 = var5;
                              break label85;
                           }
                        }
                     }
                     break label110;
                  }

                  var8 = 63;
               } else {
                  var8 = 62;
               }

               var5 = var5 << 6 | (byte)var8;
               ++var4;
               var9 = var4;
               var8 = var5;
               if (var4 % 4 == 0) {
                  var9 = var11 + 1;
                  var3[var11] = (byte)((byte)(var5 >> 16));
                  var8 = var9 + 1;
                  var3[var9] = (byte)((byte)(var5 >> 8));
                  var11 = var8 + 1;
                  var3[var8] = (byte)((byte)var5);
                  var8 = var5;
                  break label85;
               }
            }

            var4 = var9;
         }

         ++var6;
         var5 = var8;
      }

      return var10;
   }

   public static String encode(byte[] var0) {
      return encode(var0, MAP);
   }

   private static String encode(byte[] var0, byte[] var1) {
      byte[] var2 = new byte[(var0.length + 2) / 3 * 4];
      int var3 = var0.length - var0.length % 3;
      int var4 = 0;

      int var5;
      for(var5 = 0; var4 < var3; var4 += 3) {
         int var6 = var5 + 1;
         var2[var5] = (byte)var1[(var0[var4] & 255) >> 2];
         var5 = var6 + 1;
         var2[var6] = (byte)var1[(var0[var4] & 3) << 4 | (var0[var4 + 1] & 255) >> 4];
         var6 = var5 + 1;
         var2[var5] = (byte)var1[(var0[var4 + 1] & 15) << 2 | (var0[var4 + 2] & 255) >> 6];
         var5 = var6 + 1;
         var2[var6] = (byte)var1[var0[var4 + 2] & 63];
      }

      switch(var0.length % 3) {
      case 1:
         var4 = var5 + 1;
         var2[var5] = (byte)var1[(var0[var3] & 255) >> 2];
         var5 = var4 + 1;
         var2[var4] = (byte)var1[(var0[var3] & 3) << 4];
         var4 = var5 + 1;
         var2[var5] = (byte)61;
         var2[var4] = (byte)61;
         break;
      case 2:
         var4 = var5 + 1;
         var2[var5] = (byte)var1[(var0[var3] & 255) >> 2];
         var5 = var4 + 1;
         var2[var4] = (byte)var1[(var0[var3] & 3) << 4 | (var0[var3 + 1] & 255) >> 4];
         var4 = var5 + 1;
         var2[var5] = (byte)var1[(var0[var3 + 1] & 15) << 2];
         var5 = var4 + 1;
         var2[var4] = (byte)61;
      }

      try {
         String var8 = new String(var2, "US-ASCII");
         return var8;
      } catch (UnsupportedEncodingException var7) {
         throw new AssertionError(var7);
      }
   }

   public static String encodeUrl(byte[] var0) {
      return encode(var0, URL_MAP);
   }
}
