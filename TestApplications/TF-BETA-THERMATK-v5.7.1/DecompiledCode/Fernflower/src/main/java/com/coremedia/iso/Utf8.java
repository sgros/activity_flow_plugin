package com.coremedia.iso;

import java.io.UnsupportedEncodingException;

public final class Utf8 {
   public static String convert(byte[] var0) {
      if (var0 != null) {
         try {
            String var2 = new String(var0, "UTF-8");
            return var2;
         } catch (UnsupportedEncodingException var1) {
            throw new Error(var1);
         }
      } else {
         return null;
      }
   }

   public static byte[] convert(String var0) {
      if (var0 != null) {
         try {
            byte[] var2 = var0.getBytes("UTF-8");
            return var2;
         } catch (UnsupportedEncodingException var1) {
            throw new Error(var1);
         }
      } else {
         return null;
      }
   }

   public static int utf8StringLengthInBytes(String var0) {
      if (var0 != null) {
         try {
            int var1 = var0.getBytes("UTF-8").length;
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException();
         }
      } else {
         return 0;
      }
   }
}
