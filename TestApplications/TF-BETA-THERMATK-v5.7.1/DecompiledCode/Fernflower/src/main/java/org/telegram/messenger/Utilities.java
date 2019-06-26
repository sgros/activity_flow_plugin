package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
   public static volatile DispatchQueue globalQueue = new DispatchQueue("globalQueue");
   protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
   public static Pattern pattern = Pattern.compile("[\\-0-9]+");
   public static volatile DispatchQueue phoneBookQueue = new DispatchQueue("phoneBookQueue");
   public static SecureRandom random = new SecureRandom();
   public static volatile DispatchQueue searchQueue = new DispatchQueue("searchQueue");
   public static volatile DispatchQueue stageQueue = new DispatchQueue("stageQueue");

   static {
      try {
         File var0 = new File("/dev/urandom");
         FileInputStream var1 = new FileInputStream(var0);
         byte[] var3 = new byte[1024];
         var1.read(var3);
         var1.close();
         random.setSeed(var3);
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   public static String MD5(String var0) {
      if (var0 == null) {
         return null;
      } else {
         NoSuchAlgorithmException var10000;
         label40: {
            StringBuilder var1;
            boolean var10001;
            byte[] var6;
            try {
               var6 = MessageDigest.getInstance("MD5").digest(AndroidUtilities.getStringBytes(var0));
               var1 = new StringBuilder();
            } catch (NoSuchAlgorithmException var5) {
               var10000 = var5;
               var10001 = false;
               break label40;
            }

            int var2 = 0;

            while(true) {
               try {
                  if (var2 >= var6.length) {
                     break;
                  }

                  var1.append(Integer.toHexString(var6[var2] & 255 | 256).substring(1, 3));
               } catch (NoSuchAlgorithmException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break label40;
               }

               ++var2;
            }

            try {
               var0 = var1.toString();
               return var0;
            } catch (NoSuchAlgorithmException var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         NoSuchAlgorithmException var7 = var10000;
         FileLog.e((Throwable)var7);
         return null;
      }
   }

   public static native void aesCbcEncryption(ByteBuffer var0, byte[] var1, byte[] var2, int var3, int var4, int var5);

   private static native void aesCbcEncryptionByteArray(byte[] var0, byte[] var1, byte[] var2, int var3, int var4, int var5, int var6);

   public static void aesCbcEncryptionByteArraySafe(byte[] var0, byte[] var1, byte[] var2, int var3, int var4, int var5, int var6) {
      aesCbcEncryptionByteArray(var0, var1, (byte[])var2.clone(), var3, var4, var5, var6);
   }

   public static native void aesCtrDecryption(ByteBuffer var0, byte[] var1, byte[] var2, int var3, int var4);

   public static native void aesCtrDecryptionByteArray(byte[] var0, byte[] var1, byte[] var2, int var3, int var4, int var5);

   private static native void aesIgeEncryption(ByteBuffer var0, byte[] var1, byte[] var2, boolean var3, int var4, int var5);

   public static void aesIgeEncryption(ByteBuffer var0, byte[] var1, byte[] var2, boolean var3, boolean var4, int var5, int var6) {
      if (!var4) {
         var2 = (byte[])var2.clone();
      }

      aesIgeEncryption(var0, var1, var2, var3, var5, var6);
   }

   public static boolean arraysEquals(byte[] var0, int var1, byte[] var2, int var3) {
      if (var0 != null && var2 != null && var1 >= 0 && var3 >= 0 && var0.length - var1 <= var2.length - var3 && var0.length - var1 >= 0 && var2.length - var3 >= 0) {
         int var4 = var1;

         boolean var5;
         for(var5 = true; var4 < var0.length; ++var4) {
            if (var0[var4 + var1] != var2[var4 + var3]) {
               var5 = false;
            }
         }

         return var5;
      } else {
         return false;
      }
   }

   public static native void blurBitmap(Object var0, int var1, int var2, int var3, int var4, int var5);

   public static Bitmap blurWallpaper(Bitmap var0) {
      if (var0 == null) {
         return null;
      } else {
         Bitmap var1;
         if (var0.getHeight() > var0.getWidth()) {
            var1 = Bitmap.createBitmap(Math.round((float)var0.getWidth() * 450.0F / (float)var0.getHeight()), 450, Config.ARGB_8888);
         } else {
            var1 = Bitmap.createBitmap(450, Math.round((float)var0.getHeight() * 450.0F / (float)var0.getWidth()), Config.ARGB_8888);
         }

         Paint var2 = new Paint(2);
         Rect var3 = new Rect(0, 0, var1.getWidth(), var1.getHeight());
         (new Canvas(var1)).drawBitmap(var0, (Rect)null, var3, var2);
         stackBlurBitmap(var1, 12);
         return var1;
      }
   }

   public static String bytesToHex(byte[] var0) {
      if (var0 == null) {
         return "";
      } else {
         char[] var1 = new char[var0.length * 2];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            int var3 = var0[var2] & 255;
            int var4 = var2 * 2;
            char[] var5 = hexArray;
            var1[var4] = (char)var5[var3 >>> 4];
            var1[var4 + 1] = (char)var5[var3 & 15];
         }

         return new String(var1);
      }
   }

   public static int bytesToInt(byte[] var0) {
      return ((var0[3] & 255) << 24) + ((var0[2] & 255) << 16) + ((var0[1] & 255) << 8) + (var0[0] & 255);
   }

   public static long bytesToLong(byte[] var0) {
      return ((long)var0[7] << 56) + (((long)var0[6] & 255L) << 48) + (((long)var0[5] & 255L) << 40) + (((long)var0[4] & 255L) << 32) + (((long)var0[3] & 255L) << 24) + (((long)var0[2] & 255L) << 16) + (((long)var0[1] & 255L) << 8) + ((long)var0[0] & 255L);
   }

   public static native void calcCDT(ByteBuffer var0, int var1, int var2, ByteBuffer var3);

   public static native void clearDir(String var0, int var1, long var2);

   public static byte[] computePBKDF2(byte[] var0, byte[] var1) {
      byte[] var2 = new byte[64];
      pbkdf2(var0, var1, var2, 100000);
      return var2;
   }

   public static byte[] computeSHA1(ByteBuffer var0) {
      return computeSHA1((ByteBuffer)var0, 0, var0.limit());
   }

   public static byte[] computeSHA1(ByteBuffer var0, int var1, int var2) {
      int var3 = var0.position();
      int var4 = var0.limit();

      try {
         MessageDigest var5 = MessageDigest.getInstance("SHA-1");
         var0.position(var1);
         var0.limit(var2);
         var5.update(var0);
         byte[] var10 = var5.digest();
         return var10;
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
      } finally {
         var0.limit(var4);
         var0.position(var3);
      }

      return new byte[20];
   }

   public static byte[] computeSHA1(byte[] var0) {
      return computeSHA1((byte[])var0, 0, var0.length);
   }

   public static byte[] computeSHA1(byte[] var0, int var1, int var2) {
      try {
         MessageDigest var3 = MessageDigest.getInstance("SHA-1");
         var3.update(var0, var1, var2);
         var0 = var3.digest();
         return var0;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         return new byte[20];
      }
   }

   public static byte[] computeSHA256(byte[] var0) {
      return computeSHA256(var0, 0, var0.length);
   }

   public static byte[] computeSHA256(byte[] var0, int var1, int var2) {
      try {
         MessageDigest var3 = MessageDigest.getInstance("SHA-256");
         var3.update(var0, var1, var2);
         var0 = var3.digest();
         return var0;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         return new byte[32];
      }
   }

   public static byte[] computeSHA256(byte[] var0, int var1, int var2, ByteBuffer var3, int var4, int var5) {
      int var6 = var3.position();
      int var7 = var3.limit();

      try {
         MessageDigest var8 = MessageDigest.getInstance("SHA-256");
         var8.update(var0, var1, var2);
         var3.position(var4);
         var3.limit(var5);
         var8.update(var3);
         var0 = var8.digest();
         return var0;
      } catch (Exception var11) {
         FileLog.e((Throwable)var11);
      } finally {
         var3.limit(var7);
         var3.position(var6);
      }

      return new byte[32];
   }

   public static byte[] computeSHA256(byte[]... param0) {
      // $FF: Couldn't be decompiled
   }

   public static byte[] computeSHA512(byte[] var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-512");
         var1.update(var0, 0, var0.length);
         var0 = var1.digest();
         return var0;
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
         return new byte[64];
      }
   }

   public static byte[] computeSHA512(byte[] var0, byte[] var1) {
      try {
         MessageDigest var2 = MessageDigest.getInstance("SHA-512");
         var2.update(var0, 0, var0.length);
         var2.update(var1, 0, var1.length);
         var0 = var2.digest();
         return var0;
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
         return new byte[64];
      }
   }

   public static byte[] computeSHA512(byte[] var0, byte[] var1, byte[] var2) {
      try {
         MessageDigest var3 = MessageDigest.getInstance("SHA-512");
         var3.update(var0, 0, var0.length);
         var3.update(var1, 0, var1.length);
         var3.update(var2, 0, var2.length);
         var0 = var3.digest();
         return var0;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         return new byte[64];
      }
   }

   public static native int convertVideoFrame(ByteBuffer var0, ByteBuffer var1, int var2, int var3, int var4, int var5, int var6);

   public static native long getDirSize(String var0, int var1);

   public static byte[] hexToBytes(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         byte[] var2 = new byte[var1 / 2];

         for(int var3 = 0; var3 < var1; var3 += 2) {
            var2[var3 / 2] = (byte)((byte)((Character.digit(var0.charAt(var3), 16) << 4) + Character.digit(var0.charAt(var3 + 1), 16)));
         }

         return var2;
      }
   }

   public static boolean isGoodGaAndGb(BigInteger var0, BigInteger var1) {
      boolean var2;
      if (var0.compareTo(BigInteger.valueOf(1L)) > 0 && var0.compareTo(var1.subtract(BigInteger.valueOf(1L))) < 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static boolean isGoodPrime(byte[] var0, int var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 >= 2) {
         if (var1 > 7) {
            var3 = var2;
         } else {
            var3 = var2;
            if (var0.length == 256) {
               if (var0[0] >= 0) {
                  var3 = var2;
               } else {
                  BigInteger var4 = new BigInteger(1, var0);
                  if (var1 == 2) {
                     if (var4.mod(BigInteger.valueOf(8L)).intValue() != 7) {
                        return false;
                     }
                  } else if (var1 == 3) {
                     if (var4.mod(BigInteger.valueOf(3L)).intValue() != 2) {
                        return false;
                     }
                  } else if (var1 == 5) {
                     var1 = var4.mod(BigInteger.valueOf(5L)).intValue();
                     if (var1 != 1 && var1 != 4) {
                        return false;
                     }
                  } else if (var1 == 6) {
                     var1 = var4.mod(BigInteger.valueOf(24L)).intValue();
                     if (var1 != 19 && var1 != 23) {
                        return false;
                     }
                  } else if (var1 == 7) {
                     var1 = var4.mod(BigInteger.valueOf(7L)).intValue();
                     if (var1 != 3 && var1 != 5 && var1 != 6) {
                        return false;
                     }
                  }

                  if (bytesToHex(var0).equals("C71CAEB9C6B1C9048E6C522F70F13F73980D40238E3E21C14934D037563D930F48198A0AA7C14058229493D22530F4DBFA336F6E0AC925139543AED44CCE7C3720FD51F69458705AC68CD4FE6B6B13ABDC9746512969328454F18FAF8C595F642477FE96BB2A941D5BCD1D4AC8CC49880708FA9B378E3C4F3A9060BEE67CF9A4A4A695811051907E162753B56B0F6B410DBA74D8A84B2A14B3144E0EF1284754FD17ED950D5965B4B9DD46582DB1178D169C6BC465B0D6FF9CA3928FEF5B9AE4E418FC15E83EBEA0F87FA9FF5EED70050DED2849F47BF959D956850CE929851F0D8115F635B105EE2E4E15D04B2454BF6F4FADF034B10403119CD8E3B92FCC5B")) {
                     return true;
                  }

                  BigInteger var5 = var4.subtract(BigInteger.valueOf(1L)).divide(BigInteger.valueOf(2L));
                  var3 = var2;
                  if (var4.isProbablePrime(30)) {
                     var3 = var2;
                     if (var5.isProbablePrime(30)) {
                        var3 = true;
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public static native boolean loadWebpImage(Bitmap var0, ByteBuffer var1, int var2, Options var3, boolean var4);

   public static native int needInvert(Object var0, int var1, int var2, int var3, int var4);

   public static Integer parseInt(String var0) {
      Integer var1 = 0;
      if (var0 == null) {
         return var1;
      } else {
         int var3;
         Integer var6;
         label42: {
            Exception var10000;
            label34: {
               boolean var10001;
               Matcher var2;
               try {
                  var2 = pattern.matcher(var0);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label34;
               }

               var6 = var1;

               try {
                  if (!var2.find()) {
                     return var6;
                  }

                  var3 = Integer.parseInt(var2.group(0));
                  break label42;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            Exception var7 = var10000;
            FileLog.e((Throwable)var7);
            var6 = var1;
            return var6;
         }

         var6 = var3;
         return var6;
      }
   }

   public static String parseIntToString(String var0) {
      Matcher var1 = pattern.matcher(var0);
      return var1.find() ? var1.group(0) : null;
   }

   public static Long parseLong(String var0) {
      Long var1 = 0L;
      if (var0 == null) {
         return var1;
      } else {
         long var3;
         Long var7;
         label42: {
            Exception var10000;
            label34: {
               boolean var10001;
               Matcher var2;
               try {
                  var2 = pattern.matcher(var0);
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label34;
               }

               var7 = var1;

               try {
                  if (!var2.find()) {
                     return var7;
                  }

                  var3 = Long.parseLong(var2.group(0));
                  break label42;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var8 = var10000;
            FileLog.e((Throwable)var8);
            var7 = var1;
            return var7;
         }

         var7 = var3;
         return var7;
      }
   }

   private static native int pbkdf2(byte[] var0, byte[] var1, byte[] var2, int var3);

   public static native int pinBitmap(Bitmap var0);

   public static native String readlink(String var0);

   public static native void stackBlurBitmap(Bitmap var0, int var1);

   public static native void unpinBitmap(Bitmap var0);
}
