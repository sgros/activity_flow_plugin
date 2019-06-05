package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

final class SegmentedByteString extends ByteString {
   final transient int[] directory;
   final transient byte[][] segments;

   SegmentedByteString(Buffer var1, int var2) {
      super((byte[])null);
      Util.checkOffsetAndCount(var1.size, 0L, (long)var2);
      int var3 = 0;
      int var4 = 0;

      for(Segment var5 = var1.head; var3 < var2; var5 = var5.next) {
         if (var5.limit == var5.pos) {
            throw new AssertionError("s.limit == s.pos");
         }

         var3 += var5.limit - var5.pos;
         ++var4;
      }

      this.segments = new byte[var4][];
      this.directory = new int[var4 * 2];
      var4 = 0;
      var3 = 0;

      for(Segment var7 = var1.head; var4 < var2; var7 = var7.next) {
         this.segments[var3] = var7.data;
         int var6 = var4 + (var7.limit - var7.pos);
         var4 = var6;
         if (var6 > var2) {
            var4 = var2;
         }

         this.directory[var3] = var4;
         this.directory[this.segments.length + var3] = var7.pos;
         var7.shared = true;
         ++var3;
      }

   }

   private int segment(int var1) {
      var1 = Arrays.binarySearch(this.directory, 0, this.segments.length, var1 + 1);
      if (var1 < 0) {
         var1 = ~var1;
      }

      return var1;
   }

   private ByteString toByteString() {
      return new ByteString(this.toByteArray());
   }

   private Object writeReplace() {
      return this.toByteString();
   }

   public ByteBuffer asByteBuffer() {
      return ByteBuffer.wrap(this.toByteArray()).asReadOnlyBuffer();
   }

   public String base64() {
      return this.toByteString().base64();
   }

   public String base64Url() {
      return this.toByteString().base64Url();
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 != this) {
         if (var1 instanceof ByteString && ((ByteString)var1).size() == this.size() && this.rangeEquals(0, (ByteString)((ByteString)var1), 0, this.size())) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public byte getByte(int var1) {
      Util.checkOffsetAndCount((long)this.directory[this.segments.length - 1], (long)var1, 1L);
      int var2 = this.segment(var1);
      int var3;
      if (var2 == 0) {
         var3 = 0;
      } else {
         var3 = this.directory[var2 - 1];
      }

      int var4 = this.directory[this.segments.length + var2];
      return this.segments[var2][var1 - var3 + var4];
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (var1 == 0) {
         var1 = 1;
         int var2 = 0;
         int var3 = 0;

         for(int var4 = this.segments.length; var3 < var4; ++var3) {
            byte[] var5 = this.segments[var3];
            int var6 = this.directory[var4 + var3];
            int var7 = this.directory[var3];

            for(int var8 = var6; var8 < var6 + (var7 - var2); ++var8) {
               var1 = var1 * 31 + var5[var8];
            }

            var2 = var7;
         }

         this.hashCode = var1;
      }

      return var1;
   }

   public String hex() {
      return this.toByteString().hex();
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.toByteString().hmacSha1(var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.toByteString().hmacSha256(var1);
   }

   public int indexOf(byte[] var1, int var2) {
      return this.toByteString().indexOf(var1, var2);
   }

   byte[] internalArray() {
      return this.toByteArray();
   }

   public int lastIndexOf(byte[] var1, int var2) {
      return this.toByteString().lastIndexOf(var1, var2);
   }

   public ByteString md5() {
      return this.toByteString().md5();
   }

   public boolean rangeEquals(int var1, ByteString var2, int var3, int var4) {
      boolean var5 = false;
      boolean var6 = var5;
      if (var1 >= 0) {
         if (var1 > this.size() - var4) {
            var6 = var5;
         } else {
            int var7 = this.segment(var1);
            int var8 = var1;

            for(var1 = var7; var4 > 0; ++var1) {
               if (var1 == 0) {
                  var7 = 0;
               } else {
                  var7 = this.directory[var1 - 1];
               }

               int var9 = Math.min(var4, var7 + (this.directory[var1] - var7) - var8);
               int var10 = this.directory[this.segments.length + var1];
               var6 = var5;
               if (!var2.rangeEquals(var3, this.segments[var1], var8 - var7 + var10, var9)) {
                  return var6;
               }

               var8 += var9;
               var3 += var9;
               var4 -= var9;
            }

            var6 = true;
         }
      }

      return var6;
   }

   public boolean rangeEquals(int var1, byte[] var2, int var3, int var4) {
      boolean var5 = false;
      boolean var6 = var5;
      if (var1 >= 0) {
         var6 = var5;
         if (var1 <= this.size() - var4) {
            var6 = var5;
            if (var3 >= 0) {
               if (var3 > var2.length - var4) {
                  var6 = var5;
               } else {
                  int var7 = this.segment(var1);
                  int var8 = var1;

                  for(var1 = var7; var4 > 0; ++var1) {
                     if (var1 == 0) {
                        var7 = 0;
                     } else {
                        var7 = this.directory[var1 - 1];
                     }

                     int var9 = Math.min(var4, var7 + (this.directory[var1] - var7) - var8);
                     int var10 = this.directory[this.segments.length + var1];
                     var6 = var5;
                     if (!Util.arrayRangeEquals(this.segments[var1], var8 - var7 + var10, var2, var3, var9)) {
                        return var6;
                     }

                     var8 += var9;
                     var3 += var9;
                     var4 -= var9;
                  }

                  var6 = true;
               }
            }
         }
      }

      return var6;
   }

   public ByteString sha1() {
      return this.toByteString().sha1();
   }

   public ByteString sha256() {
      return this.toByteString().sha256();
   }

   public int size() {
      return this.directory[this.segments.length - 1];
   }

   public String string(Charset var1) {
      return this.toByteString().string(var1);
   }

   public ByteString substring(int var1) {
      return this.toByteString().substring(var1);
   }

   public ByteString substring(int var1, int var2) {
      return this.toByteString().substring(var1, var2);
   }

   public ByteString toAsciiLowercase() {
      return this.toByteString().toAsciiLowercase();
   }

   public ByteString toAsciiUppercase() {
      return this.toByteString().toAsciiUppercase();
   }

   public byte[] toByteArray() {
      byte[] var1 = new byte[this.directory[this.segments.length - 1]];
      int var2 = 0;
      int var3 = 0;

      for(int var4 = this.segments.length; var3 < var4; ++var3) {
         int var5 = this.directory[var4 + var3];
         int var6 = this.directory[var3];
         System.arraycopy(this.segments[var3], var5, var1, var2, var6 - var2);
         var2 = var6;
      }

      return var1;
   }

   public String toString() {
      return this.toByteString().toString();
   }

   public String utf8() {
      return this.toByteString().utf8();
   }

   public void write(OutputStream var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         int var2 = 0;
         int var3 = 0;

         for(int var4 = this.segments.length; var3 < var4; ++var3) {
            int var5 = this.directory[var4 + var3];
            int var6 = this.directory[var3];
            var1.write(this.segments[var3], var5, var6 - var2);
            var2 = var6;
         }

      }
   }

   void write(Buffer var1) {
      int var2 = 0;
      int var3 = 0;

      for(int var4 = this.segments.length; var3 < var4; ++var3) {
         int var5 = this.directory[var4 + var3];
         int var6 = this.directory[var3];
         Segment var7 = new Segment(this.segments[var3], var5, var5 + var6 - var2);
         if (var1.head == null) {
            var7.prev = var7;
            var7.next = var7;
            var1.head = var7;
         } else {
            var1.head.prev.push(var7);
         }

         var2 = var6;
      }

      var1.size += (long)var2;
   }
}
