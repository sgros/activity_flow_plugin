package org.telegram.messenger;

import java.math.BigInteger;
import org.telegram.tgnet.TLRPC;

public class SRPHelper {
   public static byte[] getBigIntegerBytes(BigInteger var0) {
      byte[] var3 = var0.toByteArray();
      byte[] var1;
      if (var3.length > 256) {
         var1 = new byte[256];
         System.arraycopy(var3, 1, var1, 0, 256);
         return var1;
      } else if (var3.length >= 256) {
         return var3;
      } else {
         var1 = new byte[256];
         System.arraycopy(var3, 0, var1, 256 - var3.length, var3.length);

         for(int var2 = 0; var2 < 256 - var3.length; ++var2) {
            var1[var2] = (byte)0;
         }

         return var1;
      }
   }

   public static BigInteger getV(byte[] var0, TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var1) {
      BigInteger var2 = BigInteger.valueOf((long)var1.g);
      getBigIntegerBytes(var2);
      BigInteger var3 = new BigInteger(1, var1.p);
      return var2.modPow(new BigInteger(1, getX(var0, var1)), var3);
   }

   public static byte[] getVBytes(byte[] var0, TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var1) {
      return !Utilities.isGoodPrime(var1.p, var1.g) ? null : getBigIntegerBytes(getV(var0, var1));
   }

   public static byte[] getX(byte[] var0, TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var1) {
      byte[] var2 = var1.salt1;
      var2 = Utilities.computeSHA256(var2, var0, var2);
      var0 = var1.salt2;
      var0 = Utilities.computePBKDF2(Utilities.computeSHA256(var0, var2, var0), var1.salt1);
      byte[] var3 = var1.salt2;
      return Utilities.computeSHA256(var3, var0, var3);
   }

   public static TLRPC.TL_inputCheckPasswordSRP startCheck(byte[] var0, long var1, byte[] var3, TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var4) {
      if (var0 != null && var3 != null && var3.length != 0 && Utilities.isGoodPrime(var4.p, var4.g)) {
         BigInteger var5 = BigInteger.valueOf((long)var4.g);
         byte[] var6 = getBigIntegerBytes(var5);
         BigInteger var7 = new BigInteger(1, var4.p);
         BigInteger var8 = new BigInteger(1, Utilities.computeSHA256(var4.p, var6));
         BigInteger var9 = new BigInteger(1, var0);
         var0 = new byte[256];
         Utilities.random.nextBytes(var0);
         BigInteger var10 = new BigInteger(1, var0);
         byte[] var11 = getBigIntegerBytes(var5.modPow(var10, var7));
         BigInteger var15 = new BigInteger(1, var3);
         if (var15.compareTo(BigInteger.ZERO) > 0 && var15.compareTo(var7) < 0) {
            byte[] var12 = getBigIntegerBytes(var15);
            BigInteger var13 = new BigInteger(1, Utilities.computeSHA256(var11, var12));
            if (var13.compareTo(BigInteger.ZERO) == 0) {
               return null;
            }

            BigInteger var16 = var15.subtract(var8.multiply(var5.modPow(var9, var7)).mod(var7));
            var15 = var16;
            if (var16.compareTo(BigInteger.ZERO) < 0) {
               var15 = var16.add(var7);
            }

            if (!Utilities.isGoodGaAndGb(var15, var7)) {
               return null;
            }

            var3 = Utilities.computeSHA256(getBigIntegerBytes(var15.modPow(var10.add(var13.multiply(var9)), var7)));
            var0 = Utilities.computeSHA256(var4.p);
            var6 = Utilities.computeSHA256(var6);

            for(int var14 = 0; var14 < var0.length; ++var14) {
               var0[var14] = (byte)((byte)(var6[var14] ^ var0[var14]));
            }

            TLRPC.TL_inputCheckPasswordSRP var17 = new TLRPC.TL_inputCheckPasswordSRP();
            var17.M1 = Utilities.computeSHA256(var0, Utilities.computeSHA256(var4.salt1), Utilities.computeSHA256(var4.salt2), var11, var12, var3);
            var17.A = var11;
            var17.srp_id = var1;
            return var17;
         }
      }

      return null;
   }
}
