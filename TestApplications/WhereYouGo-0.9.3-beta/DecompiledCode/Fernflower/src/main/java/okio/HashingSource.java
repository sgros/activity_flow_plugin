package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashingSource extends ForwardingSource {
   private final Mac mac;
   private final MessageDigest messageDigest;

   private HashingSource(Source var1, String var2) {
      super(var1);

      try {
         this.messageDigest = MessageDigest.getInstance(var2);
         this.mac = null;
      } catch (NoSuchAlgorithmException var3) {
         throw new AssertionError();
      }
   }

   private HashingSource(Source var1, ByteString var2, String var3) {
      super(var1);

      try {
         this.mac = Mac.getInstance(var3);
         Mac var7 = this.mac;
         SecretKeySpec var4 = new SecretKeySpec(var2.toByteArray(), var3);
         var7.init(var4);
         this.messageDigest = null;
      } catch (NoSuchAlgorithmException var5) {
         throw new AssertionError();
      } catch (InvalidKeyException var6) {
         throw new IllegalArgumentException(var6);
      }
   }

   public static HashingSource hmacSha1(Source var0, ByteString var1) {
      return new HashingSource(var0, var1, "HmacSHA1");
   }

   public static HashingSource hmacSha256(Source var0, ByteString var1) {
      return new HashingSource(var0, var1, "HmacSHA256");
   }

   public static HashingSource md5(Source var0) {
      return new HashingSource(var0, "MD5");
   }

   public static HashingSource sha1(Source var0) {
      return new HashingSource(var0, "SHA-1");
   }

   public static HashingSource sha256(Source var0) {
      return new HashingSource(var0, "SHA-256");
   }

   public ByteString hash() {
      byte[] var1;
      if (this.messageDigest != null) {
         var1 = this.messageDigest.digest();
      } else {
         var1 = this.mac.doFinal();
      }

      return ByteString.of(var1);
   }

   public long read(Buffer var1, long var2) throws IOException {
      long var4 = super.read(var1, var2);
      if (var4 != -1L) {
         long var6 = var1.size - var4;
         var2 = var1.size;
         Segment var8 = var1.head;

         while(true) {
            long var9 = var2;
            Segment var11 = var8;
            long var12 = var6;
            if (var2 <= var6) {
               while(var9 < var1.size) {
                  int var14 = (int)((long)var11.pos + var12 - var9);
                  if (this.messageDigest != null) {
                     this.messageDigest.update(var11.data, var14, var11.limit - var14);
                  } else {
                     this.mac.update(var11.data, var14, var11.limit - var14);
                  }

                  var9 += (long)(var11.limit - var11.pos);
                  var12 = var9;
                  var11 = var11.next;
               }
               break;
            }

            var8 = var8.prev;
            var2 -= (long)(var8.limit - var8.pos);
         }
      }

      return var4;
   }
}
