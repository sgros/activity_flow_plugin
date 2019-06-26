package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashingSink extends ForwardingSink {
   private final Mac mac;
   private final MessageDigest messageDigest;

   private HashingSink(Sink var1, String var2) {
      super(var1);

      try {
         this.messageDigest = MessageDigest.getInstance(var2);
         this.mac = null;
      } catch (NoSuchAlgorithmException var3) {
         throw new AssertionError();
      }
   }

   private HashingSink(Sink var1, ByteString var2, String var3) {
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

   public static HashingSink hmacSha1(Sink var0, ByteString var1) {
      return new HashingSink(var0, var1, "HmacSHA1");
   }

   public static HashingSink hmacSha256(Sink var0, ByteString var1) {
      return new HashingSink(var0, var1, "HmacSHA256");
   }

   public static HashingSink md5(Sink var0) {
      return new HashingSink(var0, "MD5");
   }

   public static HashingSink sha1(Sink var0) {
      return new HashingSink(var0, "SHA-1");
   }

   public static HashingSink sha256(Sink var0) {
      return new HashingSink(var0, "SHA-256");
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

   public void write(Buffer var1, long var2) throws IOException {
      Util.checkOffsetAndCount(var1.size, 0L, var2);
      long var4 = 0L;

      for(Segment var6 = var1.head; var4 < var2; var6 = var6.next) {
         int var7 = (int)Math.min(var2 - var4, (long)(var6.limit - var6.pos));
         if (this.messageDigest != null) {
            this.messageDigest.update(var6.data, var6.pos, var7);
         } else {
            this.mac.update(var6.data, var6.pos, var7);
         }

         var4 += (long)var7;
      }

      super.write(var1, var2);
   }
}
