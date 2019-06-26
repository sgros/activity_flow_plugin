package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ByteString implements Serializable, Comparable {
   public static final ByteString EMPTY = of();
   static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final long serialVersionUID = 1L;
   final byte[] data;
   transient int hashCode;
   transient String utf8;

   ByteString(byte[] var1) {
      this.data = var1;
   }

   static int codePointIndexToCharIndex(String var0, int var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = var0.length();

      while(true) {
         if (var2 < var4) {
            if (var3 == var1) {
               break;
            }

            int var5 = var0.codePointAt(var2);
            if ((!Character.isISOControl(var5) || var5 == 10 || var5 == 13) && var5 != 65533) {
               ++var3;
               var2 += Character.charCount(var5);
               continue;
            }

            var2 = -1;
            break;
         }

         var2 = var0.length();
         break;
      }

      return var2;
   }

   public static ByteString decodeBase64(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("base64 == null");
      } else {
         byte[] var1 = Base64.decode(var0);
         ByteString var2;
         if (var1 != null) {
            var2 = new ByteString(var1);
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   public static ByteString decodeHex(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("hex == null");
      } else if (var0.length() % 2 != 0) {
         throw new IllegalArgumentException("Unexpected hex string: " + var0);
      } else {
         byte[] var1 = new byte[var0.length() / 2];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (byte)((byte)((decodeHexDigit(var0.charAt(var2 * 2)) << 4) + decodeHexDigit(var0.charAt(var2 * 2 + 1))));
         }

         return of(var1);
      }
   }

   private static int decodeHexDigit(char var0) {
      int var1;
      if (var0 >= '0' && var0 <= '9') {
         var1 = var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         var1 = var0 - 97 + 10;
      } else {
         if (var0 < 'A' || var0 > 'F') {
            throw new IllegalArgumentException("Unexpected hex digit: " + var0);
         }

         var1 = var0 - 65 + 10;
      }

      return var1;
   }

   private ByteString digest(String var1) {
      try {
         ByteString var3 = of(MessageDigest.getInstance(var1).digest(this.data));
         return var3;
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public static ByteString encodeString(String var0, Charset var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("s == null");
      } else if (var1 == null) {
         throw new IllegalArgumentException("charset == null");
      } else {
         return new ByteString(var0.getBytes(var1));
      }
   }

   public static ByteString encodeUtf8(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("s == null");
      } else {
         ByteString var1 = new ByteString(var0.getBytes(Util.UTF_8));
         var1.utf8 = var0;
         return var1;
      }
   }

   private ByteString hmac(String var1, ByteString var2) {
      try {
         Mac var3 = Mac.getInstance(var1);
         SecretKeySpec var4 = new SecretKeySpec(var2.toByteArray(), var1);
         var3.init(var4);
         ByteString var7 = of(var3.doFinal(this.data));
         return var7;
      } catch (NoSuchAlgorithmException var5) {
         throw new AssertionError(var5);
      } catch (InvalidKeyException var6) {
         throw new IllegalArgumentException(var6);
      }
   }

   public static ByteString of(ByteBuffer var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("data == null");
      } else {
         byte[] var1 = new byte[var0.remaining()];
         var0.get(var1);
         return new ByteString(var1);
      }
   }

   public static ByteString of(byte... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("data == null");
      } else {
         return new ByteString((byte[])var0.clone());
      }
   }

   public static ByteString of(byte[] var0, int var1, int var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("data == null");
      } else {
         Util.checkOffsetAndCount((long)var0.length, (long)var1, (long)var2);
         byte[] var3 = new byte[var2];
         System.arraycopy(var0, var1, var3, 0, var2);
         return new ByteString(var3);
      }
   }

   public static ByteString read(InputStream var0, int var1) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("in == null");
      } else if (var1 < 0) {
         throw new IllegalArgumentException("byteCount < 0: " + var1);
      } else {
         byte[] var2 = new byte[var1];

         int var4;
         for(int var3 = 0; var3 < var1; var3 += var4) {
            var4 = var0.read(var2, var3, var1 - var3);
            if (var4 == -1) {
               throw new EOFException();
            }
         }

         return new ByteString(var2);
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      ByteString var5 = read(var1, var1.readInt());

      try {
         Field var2 = ByteString.class.getDeclaredField("data");
         var2.setAccessible(true);
         var2.set(this, var5.data);
      } catch (NoSuchFieldException var3) {
         throw new AssertionError();
      } catch (IllegalAccessException var4) {
         throw new AssertionError();
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeInt(this.data.length);
      var1.write(this.data);
   }

   public ByteBuffer asByteBuffer() {
      return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
   }

   public String base64() {
      return Base64.encode(this.data);
   }

   public String base64Url() {
      return Base64.encodeUrl(this.data);
   }

   public int compareTo(ByteString var1) {
      byte var2 = -1;
      int var3 = this.size();
      int var4 = var1.size();
      int var5 = 0;
      int var6 = Math.min(var3, var4);

      byte var9;
      while(true) {
         if (var5 < var6) {
            int var7 = this.getByte(var5) & 255;
            int var8 = var1.getByte(var5) & 255;
            if (var7 == var8) {
               ++var5;
               continue;
            }

            if (var7 < var8) {
               var9 = var2;
            } else {
               var9 = 1;
            }
            break;
         }

         if (var3 == var4) {
            var9 = 0;
         } else {
            var9 = var2;
            if (var3 >= var4) {
               var9 = 1;
            }
         }
         break;
      }

      return var9;
   }

   public final boolean endsWith(ByteString var1) {
      return this.rangeEquals(this.size() - var1.size(), (ByteString)var1, 0, var1.size());
   }

   public final boolean endsWith(byte[] var1) {
      return this.rangeEquals(this.size() - var1.length, (byte[])var1, 0, var1.length);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 != this) {
         if (var1 instanceof ByteString && ((ByteString)var1).size() == this.data.length && ((ByteString)var1).rangeEquals(0, (byte[])this.data, 0, this.data.length)) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public byte getByte(int var1) {
      return this.data[var1];
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (var1 == 0) {
         var1 = Arrays.hashCode(this.data);
         this.hashCode = var1;
      }

      return var1;
   }

   public String hex() {
      char[] var1 = new char[this.data.length * 2];
      byte[] var2 = this.data;
      int var3 = var2.length;
      int var4 = 0;

      for(int var5 = 0; var4 < var3; ++var4) {
         byte var6 = var2[var4];
         int var7 = var5 + 1;
         var1[var5] = (char)HEX_DIGITS[var6 >> 4 & 15];
         var5 = var7 + 1;
         var1[var7] = (char)HEX_DIGITS[var6 & 15];
      }

      return new String(var1);
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.hmac("HmacSHA1", var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.hmac("HmacSHA256", var1);
   }

   public final int indexOf(ByteString var1) {
      return this.indexOf((byte[])var1.internalArray(), 0);
   }

   public final int indexOf(ByteString var1, int var2) {
      return this.indexOf(var1.internalArray(), var2);
   }

   public final int indexOf(byte[] var1) {
      return this.indexOf((byte[])var1, 0);
   }

   public int indexOf(byte[] var1, int var2) {
      var2 = Math.max(var2, 0);
      int var3 = this.data.length;
      int var4 = var1.length;

      while(true) {
         if (var2 > var3 - var4) {
            var2 = -1;
            break;
         }

         if (Util.arrayRangeEquals(this.data, var2, var1, 0, var1.length)) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   byte[] internalArray() {
      return this.data;
   }

   public final int lastIndexOf(ByteString var1) {
      return this.lastIndexOf(var1.internalArray(), this.size());
   }

   public final int lastIndexOf(ByteString var1, int var2) {
      return this.lastIndexOf(var1.internalArray(), var2);
   }

   public final int lastIndexOf(byte[] var1) {
      return this.lastIndexOf(var1, this.size());
   }

   public int lastIndexOf(byte[] var1, int var2) {
      var2 = Math.min(var2, this.data.length - var1.length);

      while(true) {
         if (var2 < 0) {
            var2 = -1;
            break;
         }

         if (Util.arrayRangeEquals(this.data, var2, var1, 0, var1.length)) {
            break;
         }

         --var2;
      }

      return var2;
   }

   public ByteString md5() {
      return this.digest("MD5");
   }

   public boolean rangeEquals(int var1, ByteString var2, int var3, int var4) {
      return var2.rangeEquals(var3, this.data, var1, var4);
   }

   public boolean rangeEquals(int var1, byte[] var2, int var3, int var4) {
      boolean var5;
      if (var1 >= 0 && var1 <= this.data.length - var4 && var3 >= 0 && var3 <= var2.length - var4 && Util.arrayRangeEquals(this.data, var1, var2, var3, var4)) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public ByteString sha1() {
      return this.digest("SHA-1");
   }

   public ByteString sha256() {
      return this.digest("SHA-256");
   }

   public int size() {
      return this.data.length;
   }

   public final boolean startsWith(ByteString var1) {
      return this.rangeEquals(0, (ByteString)var1, 0, var1.size());
   }

   public final boolean startsWith(byte[] var1) {
      return this.rangeEquals(0, (byte[])var1, 0, var1.length);
   }

   public String string(Charset var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("charset == null");
      } else {
         return new String(this.data, var1);
      }
   }

   public ByteString substring(int var1) {
      return this.substring(var1, this.data.length);
   }

   public ByteString substring(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("beginIndex < 0");
      } else if (var2 > this.data.length) {
         throw new IllegalArgumentException("endIndex > length(" + this.data.length + ")");
      } else {
         int var3 = var2 - var1;
         if (var3 < 0) {
            throw new IllegalArgumentException("endIndex < beginIndex");
         } else {
            ByteString var5;
            if (var1 == 0 && var2 == this.data.length) {
               var5 = this;
            } else {
               byte[] var4 = new byte[var3];
               System.arraycopy(this.data, var1, var4, 0, var3);
               var5 = new ByteString(var4);
            }

            return var5;
         }
      }
   }

   public ByteString toAsciiLowercase() {
      int var1 = 0;

      ByteString var2;
      while(true) {
         var2 = this;
         if (var1 >= this.data.length) {
            break;
         }

         byte var3 = this.data[var1];
         if (var3 >= 65 && var3 <= 90) {
            byte[] var4 = (byte[])this.data.clone();
            var4[var1] = (byte)((byte)(var3 + 32));
            ++var1;

            for(; var1 < var4.length; ++var1) {
               var3 = var4[var1];
               if (var3 >= 65 && var3 <= 90) {
                  var4[var1] = (byte)((byte)(var3 + 32));
               }
            }

            var2 = new ByteString(var4);
            break;
         }

         ++var1;
      }

      return var2;
   }

   public ByteString toAsciiUppercase() {
      int var1 = 0;

      ByteString var2;
      while(true) {
         var2 = this;
         if (var1 >= this.data.length) {
            break;
         }

         byte var3 = this.data[var1];
         if (var3 >= 97 && var3 <= 122) {
            byte[] var4 = (byte[])this.data.clone();
            var4[var1] = (byte)((byte)(var3 - 32));
            ++var1;

            for(; var1 < var4.length; ++var1) {
               var3 = var4[var1];
               if (var3 >= 97 && var3 <= 122) {
                  var4[var1] = (byte)((byte)(var3 - 32));
               }
            }

            var2 = new ByteString(var4);
            break;
         }

         ++var1;
      }

      return var2;
   }

   public byte[] toByteArray() {
      return (byte[])this.data.clone();
   }

   public String toString() {
      String var1;
      if (this.data.length == 0) {
         var1 = "[size=0]";
      } else {
         var1 = this.utf8();
         int var2 = codePointIndexToCharIndex(var1, 64);
         if (var2 == -1) {
            if (this.data.length <= 64) {
               var1 = "[hex=" + this.hex() + "]";
            } else {
               var1 = "[size=" + this.data.length + " hex=" + this.substring(0, 64).hex() + "…]";
            }
         } else {
            String var3 = var1.substring(0, var2).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
            if (var2 < var1.length()) {
               var1 = "[size=" + this.data.length + " text=" + var3 + "…]";
            } else {
               var1 = "[text=" + var3 + "]";
            }
         }
      }

      return var1;
   }

   public String utf8() {
      String var1 = this.utf8;
      if (var1 == null) {
         var1 = new String(this.data, Util.UTF_8);
         this.utf8 = var1;
      }

      return var1;
   }

   public void write(OutputStream var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         var1.write(this.data);
      }
   }

   void write(Buffer var1) {
      var1.write(this.data, 0, this.data.length);
   }
}
