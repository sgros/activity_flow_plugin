package org.telegram.messenger;

import org.telegram.tgnet.SerializedData;

public class MessageKeyData {
   public byte[] aesIv;
   public byte[] aesKey;

   public static MessageKeyData generateMessageKeyData(byte[] var0, byte[] var1, boolean var2, int var3) {
      MessageKeyData var4 = new MessageKeyData();
      if (var0 != null && var0.length != 0) {
         byte var5;
         if (var2) {
            var5 = 8;
         } else {
            var5 = 0;
         }

         SerializedData var6;
         byte[] var7;
         SerializedData var10;
         if (var3 != 1) {
            if (var3 == 2) {
               var6 = new SerializedData();
               var6.writeBytes(var1, 0, 16);
               var6.writeBytes(var0, var5, 36);
               var7 = Utilities.computeSHA256(var6.toByteArray());
               var6.cleanup();
               var6 = new SerializedData();
               var6.writeBytes(var0, var5 + 40, 36);
               var6.writeBytes(var1, 0, 16);
               var0 = Utilities.computeSHA256(var6.toByteArray());
               var6.cleanup();
               var10 = new SerializedData();
               var10.writeBytes(var7, 0, 8);
               var10.writeBytes(var0, 8, 16);
               var10.writeBytes(var7, 24, 8);
               var4.aesKey = var10.toByteArray();
               var10.cleanup();
               var10 = new SerializedData();
               var10.writeBytes(var0, 0, 8);
               var10.writeBytes(var7, 8, 16);
               var10.writeBytes(var0, 24, 8);
               var4.aesIv = var10.toByteArray();
               var10.cleanup();
            }
         } else {
            var6 = new SerializedData();
            var6.writeBytes(var1);
            var6.writeBytes(var0, var5, 32);
            var7 = Utilities.computeSHA1(var6.toByteArray());
            var6.cleanup();
            SerializedData var8 = new SerializedData();
            var8.writeBytes(var0, var5 + 32, 16);
            var8.writeBytes(var1);
            var8.writeBytes(var0, var5 + 48, 16);
            byte[] var11 = Utilities.computeSHA1(var8.toByteArray());
            var8.cleanup();
            SerializedData var9 = new SerializedData();
            var9.writeBytes(var0, var5 + 64, 32);
            var9.writeBytes(var1);
            byte[] var12 = Utilities.computeSHA1(var9.toByteArray());
            var9.cleanup();
            var9 = new SerializedData();
            var9.writeBytes(var1);
            var9.writeBytes(var0, var5 + 96, 32);
            var0 = Utilities.computeSHA1(var9.toByteArray());
            var9.cleanup();
            var10 = new SerializedData();
            var10.writeBytes(var7, 0, 8);
            var10.writeBytes(var11, 8, 12);
            var10.writeBytes(var12, 4, 12);
            var4.aesKey = var10.toByteArray();
            var10.cleanup();
            var10 = new SerializedData();
            var10.writeBytes(var7, 8, 12);
            var10.writeBytes(var11, 0, 8);
            var10.writeBytes(var12, 16, 4);
            var10.writeBytes(var0, 0, 8);
            var4.aesIv = var10.toByteArray();
            var10.cleanup();
         }

         return var4;
      } else {
         var4.aesIv = null;
         var4.aesKey = null;
         return var4;
      }
   }
}
