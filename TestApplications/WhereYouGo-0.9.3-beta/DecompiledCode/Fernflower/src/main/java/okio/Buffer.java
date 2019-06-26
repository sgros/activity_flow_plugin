package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable {
   private static final byte[] DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
   static final int REPLACEMENT_CHARACTER = 65533;
   Segment head;
   long size;

   private ByteString digest(String param1) {
      // $FF: Couldn't be decompiled
   }

   private ByteString hmac(String param1, ByteString param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean rangeEquals(Segment var1, int var2, ByteString var3, int var4, int var5) {
      int var6 = var1.limit;
      byte[] var7 = var1.data;

      boolean var11;
      while(true) {
         if (var4 >= var5) {
            var11 = true;
            break;
         }

         int var8 = var6;
         Segment var9 = var1;
         int var10 = var2;
         if (var2 == var6) {
            var9 = var1.next;
            var7 = var9.data;
            var10 = var9.pos;
            var8 = var9.limit;
         }

         if (var7[var10] != var3.getByte(var4)) {
            var11 = false;
            break;
         }

         var2 = var10 + 1;
         ++var4;
         var6 = var8;
         var1 = var9;
      }

      return var11;
   }

   private void readFrom(InputStream var1, long var2, boolean var4) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("in == null");
      } else {
         while(var2 > 0L || var4) {
            Segment var5 = this.writableSegment(1);
            int var6 = (int)Math.min(var2, (long)(8192 - var5.limit));
            var6 = var1.read(var5.data, var5.limit, var6);
            if (var6 == -1) {
               if (!var4) {
                  throw new EOFException();
               }
               break;
            }

            var5.limit += var6;
            this.size += (long)var6;
            var2 -= (long)var6;
         }

      }
   }

   public Buffer buffer() {
      return this;
   }

   public void clear() {
      try {
         this.skip(this.size);
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public Buffer clone() {
      Buffer var1 = new Buffer();
      if (this.size != 0L) {
         var1.head = new Segment(this.head);
         Segment var2 = var1.head;
         Segment var3 = var1.head;
         Segment var4 = var1.head;
         var3.prev = var4;
         var2.next = var4;

         for(var4 = this.head.next; var4 != this.head; var4 = var4.next) {
            var1.head.prev.push(new Segment(var4));
         }

         var1.size = this.size;
      }

      return var1;
   }

   public void close() {
   }

   public long completeSegmentByteCount() {
      long var1 = 0L;
      long var3 = this.size;
      if (var3 != 0L) {
         Segment var5 = this.head.prev;
         var1 = var3;
         if (var5.limit < 8192) {
            var1 = var3;
            if (var5.owner) {
               var1 = var3 - (long)(var5.limit - var5.pos);
            }
         }
      }

      return var1;
   }

   public Buffer copyTo(OutputStream var1) throws IOException {
      return this.copyTo(var1, 0L, this.size);
   }

   public Buffer copyTo(OutputStream var1, long var2, long var4) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         Util.checkOffsetAndCount(this.size, var2, var4);
         if (var4 != 0L) {
            Segment var6 = this.head;

            while(true) {
               Segment var7 = var6;
               long var8 = var2;
               long var10 = var4;
               if (var2 < (long)(var6.limit - var6.pos)) {
                  while(var10 > 0L) {
                     int var12 = (int)((long)var7.pos + var8);
                     int var13 = (int)Math.min((long)(var7.limit - var12), var10);
                     var1.write(var7.data, var12, var13);
                     var10 -= (long)var13;
                     var8 = 0L;
                     var7 = var7.next;
                  }
                  break;
               }

               var2 -= (long)(var6.limit - var6.pos);
               var6 = var6.next;
            }
         }

         return this;
      }
   }

   public Buffer copyTo(Buffer var1, long var2, long var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         Util.checkOffsetAndCount(this.size, var2, var4);
         if (var4 != 0L) {
            var1.size += var4;
            Segment var6 = this.head;

            while(true) {
               Segment var7 = var6;
               long var8 = var2;
               long var10 = var4;
               if (var2 < (long)(var6.limit - var6.pos)) {
                  while(var10 > 0L) {
                     var6 = new Segment(var7);
                     var6.pos = (int)((long)var6.pos + var8);
                     var6.limit = Math.min(var6.pos + (int)var10, var6.limit);
                     if (var1.head == null) {
                        var6.prev = var6;
                        var6.next = var6;
                        var1.head = var6;
                     } else {
                        var1.head.prev.push(var6);
                     }

                     var10 -= (long)(var6.limit - var6.pos);
                     var8 = 0L;
                     var7 = var7.next;
                  }
                  break;
               }

               var2 -= (long)(var6.limit - var6.pos);
               var6 = var6.next;
            }
         }

         return this;
      }
   }

   public BufferedSink emit() {
      return this;
   }

   public Buffer emitCompleteSegments() {
      return this;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (this == var1) {
         var2 = true;
      } else if (!(var1 instanceof Buffer)) {
         var2 = false;
      } else {
         Buffer var12 = (Buffer)var1;
         if (this.size != var12.size) {
            var2 = false;
         } else if (this.size == 0L) {
            var2 = true;
         } else {
            Segment var3 = this.head;
            Segment var13 = var12.head;
            int var4 = var3.pos;
            int var5 = var13.pos;
            long var6 = 0L;

            while(true) {
               if (var6 >= this.size) {
                  var2 = true;
                  break;
               }

               long var8 = (long)Math.min(var3.limit - var4, var13.limit - var5);
               byte var10 = 0;
               int var11 = var4;
               var4 = var5;
               var5 = var11;

               for(var11 = var10; (long)var11 < var8; ++var5) {
                  if (var3.data[var5] != var13.data[var4]) {
                     var2 = false;
                     return var2;
                  }

                  ++var11;
                  ++var4;
               }

               if (var5 == var3.limit) {
                  var3 = var3.next;
                  var5 = var3.pos;
               }

               if (var4 == var13.limit) {
                  var13 = var13.next;
                  var11 = var13.pos;
               } else {
                  var11 = var4;
               }

               var6 += var8;
               var4 = var5;
               var5 = var11;
            }
         }
      }

      return var2;
   }

   public boolean exhausted() {
      boolean var1;
      if (this.size == 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void flush() {
   }

   public byte getByte(long var1) {
      Util.checkOffsetAndCount(this.size, var1, 1L);
      Segment var3 = this.head;

      while(true) {
         int var4 = var3.limit - var3.pos;
         if (var1 < (long)var4) {
            return var3.data[var3.pos + (int)var1];
         }

         var1 -= (long)var4;
         var3 = var3.next;
      }
   }

   public int hashCode() {
      Segment var1 = this.head;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         int var3 = 1;

         Segment var6;
         do {
            int var4 = var1.pos;
            int var5 = var1.limit;
            var2 = var3;

            for(var3 = var4; var3 < var5; ++var3) {
               var2 = var2 * 31 + var1.data[var3];
            }

            var6 = var1.next;
            var3 = var2;
            var1 = var6;
         } while(var6 != this.head);
      }

      return var2;
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.hmac("HmacSHA1", var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.hmac("HmacSHA256", var1);
   }

   public long indexOf(byte var1) {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(byte var1, long var2) {
      long var4 = -1L;
      if (var2 < 0L) {
         throw new IllegalArgumentException("fromIndex < 0");
      } else {
         Segment var6 = this.head;
         if (var6 == null) {
            var2 = var4;
         } else {
            long var7;
            long var9;
            Segment var11;
            long var12;
            if (this.size - var2 < var2) {
               var7 = this.size;

               while(true) {
                  var9 = var7;
                  var11 = var6;
                  var12 = var2;
                  if (var7 <= var2) {
                     break;
                  }

                  var6 = var6.prev;
                  var7 -= (long)(var6.limit - var6.pos);
               }
            } else {
               var9 = 0L;

               while(true) {
                  var7 = var9 + (long)(var6.limit - var6.pos);
                  var11 = var6;
                  var12 = var2;
                  if (var7 >= var2) {
                     break;
                  }

                  var6 = var6.next;
                  var9 = var7;
               }
            }

            while(true) {
               var2 = var4;
               if (var9 >= this.size) {
                  break;
               }

               byte[] var16 = var11.data;
               int var14 = (int)((long)var11.pos + var12 - var9);

               for(int var15 = var11.limit; var14 < var15; ++var14) {
                  if (var16[var14] == var1) {
                     var2 = (long)(var14 - var11.pos) + var9;
                     return var2;
                  }
               }

               var9 += (long)(var11.limit - var11.pos);
               var12 = var9;
               var11 = var11.next;
            }
         }

         return var2;
      }
   }

   public long indexOf(ByteString var1) throws IOException {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(ByteString var1, long var2) throws IOException {
      if (var1.size() == 0) {
         throw new IllegalArgumentException("bytes is empty");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("fromIndex < 0");
      } else {
         Segment var4 = this.head;
         if (var4 == null) {
            var2 = -1L;
         } else {
            long var5;
            Segment var7;
            long var8;
            if (this.size - var2 < var2) {
               var5 = this.size;

               while(true) {
                  var7 = var4;
                  var8 = var5;
                  if (var5 <= var2) {
                     break;
                  }

                  var4 = var4.prev;
                  var5 -= (long)(var4.limit - var4.pos);
               }
            } else {
               var8 = 0L;

               while(true) {
                  var5 = var8 + (long)(var4.limit - var4.pos);
                  var7 = var4;
                  if (var5 >= var2) {
                     break;
                  }

                  var4 = var4.next;
                  var8 = var5;
               }
            }

            byte var10 = var1.getByte(0);
            int var11 = var1.size();
            var5 = this.size - (long)var11 + 1L;

            while(true) {
               if (var8 >= var5) {
                  var2 = -1L;
                  break;
               }

               byte[] var14 = var7.data;
               int var12 = (int)Math.min((long)var7.limit, (long)var7.pos + var5 - var8);

               for(int var13 = (int)((long)var7.pos + var2 - var8); var13 < var12; ++var13) {
                  if (var14[var13] == var10 && this.rangeEquals(var7, var13 + 1, var1, 1, var11)) {
                     var2 = (long)(var13 - var7.pos) + var8;
                     return var2;
                  }
               }

               var8 += (long)(var7.limit - var7.pos);
               var2 = var8;
               var7 = var7.next;
            }
         }

         return var2;
      }
   }

   public long indexOfElement(ByteString var1) {
      return this.indexOfElement(var1, 0L);
   }

   public long indexOfElement(ByteString var1, long var2) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("fromIndex < 0");
      } else {
         Segment var4 = this.head;
         if (var4 == null) {
            var2 = -1L;
         } else {
            long var5;
            long var7;
            Segment var9;
            if (this.size - var2 < var2) {
               var5 = this.size;

               while(true) {
                  var7 = var5;
                  var9 = var4;
                  if (var5 <= var2) {
                     break;
                  }

                  var4 = var4.prev;
                  var5 -= (long)(var4.limit - var4.pos);
               }
            } else {
               var7 = 0L;

               while(true) {
                  var5 = var7 + (long)(var4.limit - var4.pos);
                  var9 = var4;
                  if (var5 >= var2) {
                     break;
                  }

                  var4 = var4.next;
                  var7 = var5;
               }
            }

            int var12;
            int var13;
            byte var14;
            byte[] var15;
            if (var1.size() != 2) {
               for(var15 = var1.internalArray(); var7 < this.size; var9 = var9.next) {
                  byte[] var16 = var9.data;
                  var12 = (int)((long)var9.pos + var2 - var7);

                  for(int var17 = var9.limit; var12 < var17; ++var12) {
                     var14 = var16[var12];
                     var13 = var15.length;

                     for(int var18 = 0; var18 < var13; ++var18) {
                        if (var14 == var15[var18]) {
                           var2 = (long)(var12 - var9.pos) + var7;
                           return var2;
                        }
                     }
                  }

                  var7 += (long)(var9.limit - var9.pos);
                  var2 = var7;
               }
            } else {
               byte var10 = var1.getByte(0);

               for(byte var11 = var1.getByte(1); var7 < this.size; var9 = var9.next) {
                  var15 = var9.data;
                  var12 = (int)((long)var9.pos + var2 - var7);

                  for(var13 = var9.limit; var12 < var13; ++var12) {
                     var14 = var15[var12];
                     if (var14 == var10 || var14 == var11) {
                        var2 = (long)(var12 - var9.pos) + var7;
                        return var2;
                     }
                  }

                  var7 += (long)(var9.limit - var9.pos);
                  var2 = var7;
               }
            }

            var2 = -1L;
         }

         return var2;
      }
   }

   public InputStream inputStream() {
      return new InputStream() {
         public int available() {
            return (int)Math.min(Buffer.this.size, 2147483647L);
         }

         public void close() {
         }

         public int read() {
            int var1;
            if (Buffer.this.size > 0L) {
               var1 = Buffer.this.readByte() & 255;
            } else {
               var1 = -1;
            }

            return var1;
         }

         public int read(byte[] var1, int var2, int var3) {
            return Buffer.this.read(var1, var2, var3);
         }

         public String toString() {
            return Buffer.this + ".inputStream()";
         }
      };
   }

   public ByteString md5() {
      return this.digest("MD5");
   }

   public OutputStream outputStream() {
      return new OutputStream() {
         public void close() {
         }

         public void flush() {
         }

         public String toString() {
            return Buffer.this + ".outputStream()";
         }

         public void write(int var1) {
            Buffer.this.writeByte((byte)var1);
         }

         public void write(byte[] var1, int var2, int var3) {
            Buffer.this.write(var1, var2, var3);
         }
      };
   }

   public boolean rangeEquals(long var1, ByteString var3) {
      return this.rangeEquals(var1, var3, 0, var3.size());
   }

   public boolean rangeEquals(long var1, ByteString var3, int var4, int var5) {
      boolean var6 = false;
      boolean var7 = var6;
      if (var1 >= 0L) {
         var7 = var6;
         if (var4 >= 0) {
            var7 = var6;
            if (var5 >= 0) {
               var7 = var6;
               if (this.size - var1 >= (long)var5) {
                  if (var3.size() - var4 < var5) {
                     var7 = var6;
                  } else {
                     int var8 = 0;

                     while(true) {
                        if (var8 >= var5) {
                           var7 = true;
                           break;
                        }

                        var7 = var6;
                        if (this.getByte((long)var8 + var1) != var3.getByte(var4 + var8)) {
                           break;
                        }

                        ++var8;
                     }
                  }
               }
            }
         }
      }

      return var7;
   }

   public int read(byte[] var1) {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) {
      Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
      Segment var4 = this.head;
      if (var4 == null) {
         var2 = -1;
      } else {
         var3 = Math.min(var3, var4.limit - var4.pos);
         System.arraycopy(var4.data, var4.pos, var1, var2, var3);
         var4.pos += var3;
         this.size -= (long)var3;
         var2 = var3;
         if (var4.pos == var4.limit) {
            this.head = var4.pop();
            SegmentPool.recycle(var4);
            var2 = var3;
         }
      }

      return var2;
   }

   public long read(Buffer var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("sink == null");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else {
         if (this.size == 0L) {
            var2 = -1L;
         } else {
            long var4 = var2;
            if (var2 > this.size) {
               var4 = this.size;
            }

            var1.write(this, var4);
            var2 = var4;
         }

         return var2;
      }
   }

   public long readAll(Sink var1) throws IOException {
      long var2 = this.size;
      if (var2 > 0L) {
         var1.write(this, var2);
      }

      return var2;
   }

   public byte readByte() {
      if (this.size == 0L) {
         throw new IllegalStateException("size == 0");
      } else {
         Segment var1 = this.head;
         int var2 = var1.pos;
         int var3 = var1.limit;
         byte[] var4 = var1.data;
         int var5 = var2 + 1;
         byte var6 = var4[var2];
         --this.size;
         if (var5 == var3) {
            this.head = var1.pop();
            SegmentPool.recycle(var1);
         } else {
            var1.pos = var5;
         }

         return var6;
      }
   }

   public byte[] readByteArray() {
      try {
         byte[] var1 = this.readByteArray(this.size);
         return var1;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public byte[] readByteArray(long var1) throws EOFException {
      Util.checkOffsetAndCount(this.size, 0L, var1);
      if (var1 > 2147483647L) {
         throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + var1);
      } else {
         byte[] var3 = new byte[(int)var1];
         this.readFully(var3);
         return var3;
      }
   }

   public ByteString readByteString() {
      return new ByteString(this.readByteArray());
   }

   public ByteString readByteString(long var1) throws EOFException {
      return new ByteString(this.readByteArray(var1));
   }

   public long readDecimalLong() {
      if (this.size == 0L) {
         throw new IllegalStateException("size == 0");
      } else {
         long var1 = 0L;
         int var3 = 0;
         boolean var4 = false;
         boolean var5 = false;
         long var6 = -7L;

         while(true) {
            Segment var8 = this.head;
            byte[] var9 = var8.data;
            int var10 = var8.pos;
            int var11 = var8.limit;
            long var12 = var1;
            int var14 = var3;
            var1 = var6;
            var3 = var10;
            boolean var18 = var4;

            while(true) {
               var4 = var5;
               if (var3 >= var11) {
                  break;
               }

               byte var15 = var9[var3];
               if (var15 >= 48 && var15 <= 57) {
                  int var16 = 48 - var15;
                  if (var12 < -922337203685477580L || var12 == -922337203685477580L && (long)var16 < var1) {
                     Buffer var17 = (new Buffer()).writeDecimalLong(var12).writeByte(var15);
                     if (!var18) {
                        var17.readByte();
                     }

                     throw new NumberFormatException("Number too large: " + var17.readUtf8());
                  }

                  var12 = var12 * 10L + (long)var16;
               } else {
                  if (var15 != 45 || var14 != 0) {
                     if (var14 == 0) {
                        throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + Integer.toHexString(var15));
                     }

                     var4 = true;
                     break;
                  }

                  var18 = true;
                  --var1;
               }

               ++var3;
               ++var14;
            }

            if (var3 == var11) {
               this.head = var8.pop();
               SegmentPool.recycle(var8);
            } else {
               var8.pos = var3;
            }

            if (!var4) {
               var5 = var4;
               var4 = var18;
               var6 = var1;
               var3 = var14;
               var1 = var12;
               if (this.head != null) {
                  continue;
               }
            }

            this.size -= (long)var14;
            if (!var18) {
               var12 = -var12;
            }

            return var12;
         }
      }
   }

   public Buffer readFrom(InputStream var1) throws IOException {
      this.readFrom(var1, Long.MAX_VALUE, true);
      return this;
   }

   public Buffer readFrom(InputStream var1, long var2) throws IOException {
      if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else {
         this.readFrom(var1, var2, false);
         return this;
      }
   }

   public void readFully(Buffer var1, long var2) throws EOFException {
      if (this.size < var2) {
         var1.write(this, this.size);
         throw new EOFException();
      } else {
         var1.write(this, var2);
      }
   }

   public void readFully(byte[] var1) throws EOFException {
      int var3;
      for(int var2 = 0; var2 < var1.length; var2 += var3) {
         var3 = this.read(var1, var2, var1.length - var2);
         if (var3 == -1) {
            throw new EOFException();
         }
      }

   }

   public long readHexadecimalUnsignedLong() {
      if (this.size == 0L) {
         throw new IllegalStateException("size == 0");
      } else {
         long var1 = 0L;
         int var3 = 0;
         boolean var4 = false;

         long var9;
         int var11;
         do {
            Segment var5 = this.head;
            byte[] var6 = var5.data;
            int var7 = var5.pos;
            int var8 = var5.limit;
            var9 = var1;
            var11 = var3;

            boolean var13;
            while(true) {
               var13 = var4;
               if (var7 >= var8) {
                  break;
               }

               byte var12 = var6[var7];
               if (var12 >= 48 && var12 <= 57) {
                  var3 = var12 - 48;
               } else if (var12 >= 97 && var12 <= 102) {
                  var3 = var12 - 97 + 10;
               } else {
                  if (var12 < 65 || var12 > 70) {
                     if (var11 == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + Integer.toHexString(var12));
                     }

                     var13 = true;
                     break;
                  }

                  var3 = var12 - 65 + 10;
               }

               if ((-1152921504606846976L & var9) != 0L) {
                  Buffer var14 = (new Buffer()).writeHexadecimalUnsignedLong(var9).writeByte(var12);
                  throw new NumberFormatException("Number too large: " + var14.readUtf8());
               }

               var9 = var9 << 4 | (long)var3;
               ++var7;
               ++var11;
            }

            if (var7 == var8) {
               this.head = var5.pop();
               SegmentPool.recycle(var5);
            } else {
               var5.pos = var7;
            }

            if (var13) {
               break;
            }

            var4 = var13;
            var3 = var11;
            var1 = var9;
         } while(this.head != null);

         this.size -= (long)var11;
         return var9;
      }
   }

   public int readInt() {
      if (this.size < 4L) {
         throw new IllegalStateException("size < 4: " + this.size);
      } else {
         Segment var1 = this.head;
         int var2 = var1.pos;
         int var3 = var1.limit;
         if (var3 - var2 < 4) {
            var2 = (this.readByte() & 255) << 24 | (this.readByte() & 255) << 16 | (this.readByte() & 255) << 8 | this.readByte() & 255;
         } else {
            byte[] var4 = var1.data;
            int var5 = var2 + 1;
            byte var9 = var4[var2];
            int var6 = var5 + 1;
            byte var10 = var4[var5];
            int var7 = var6 + 1;
            byte var8 = var4[var6];
            var6 = var7 + 1;
            var2 = (var9 & 255) << 24 | (var10 & 255) << 16 | (var8 & 255) << 8 | var4[var7] & 255;
            this.size -= 4L;
            if (var6 == var3) {
               this.head = var1.pop();
               SegmentPool.recycle(var1);
            } else {
               var1.pos = var6;
            }
         }

         return var2;
      }
   }

   public int readIntLe() {
      return Util.reverseBytesInt(this.readInt());
   }

   public long readLong() {
      if (this.size < 8L) {
         throw new IllegalStateException("size < 8: " + this.size);
      } else {
         Segment var1 = this.head;
         int var2 = var1.pos;
         int var3 = var1.limit;
         long var4;
         if (var3 - var2 < 8) {
            var4 = ((long)this.readInt() & 4294967295L) << 32 | (long)this.readInt() & 4294967295L;
         } else {
            byte[] var6 = var1.data;
            int var7 = var2 + 1;
            long var8 = (long)var6[var2];
            int var10 = var7 + 1;
            long var11 = (long)var6[var7];
            var2 = var10 + 1;
            long var13 = (long)var6[var10];
            var7 = var2 + 1;
            var4 = (long)var6[var2];
            var2 = var7 + 1;
            long var15 = (long)var6[var7];
            var7 = var2 + 1;
            long var17 = (long)var6[var2];
            var2 = var7 + 1;
            long var19 = (long)var6[var7];
            var7 = var2 + 1;
            var4 = (var8 & 255L) << 56 | (var11 & 255L) << 48 | (var13 & 255L) << 40 | (var4 & 255L) << 32 | (var15 & 255L) << 24 | (var17 & 255L) << 16 | (var19 & 255L) << 8 | (long)var6[var2] & 255L;
            this.size -= 8L;
            if (var7 == var3) {
               this.head = var1.pop();
               SegmentPool.recycle(var1);
            } else {
               var1.pos = var7;
            }
         }

         return var4;
      }
   }

   public long readLongLe() {
      return Util.reverseBytesLong(this.readLong());
   }

   public short readShort() {
      if (this.size < 2L) {
         throw new IllegalStateException("size < 2: " + this.size);
      } else {
         Segment var1 = this.head;
         int var2 = var1.pos;
         int var3 = var1.limit;
         short var4;
         short var8;
         if (var3 - var2 < 2) {
            var8 = (short)((this.readByte() & 255) << 8 | this.readByte() & 255);
            var4 = var8;
         } else {
            byte[] var5 = var1.data;
            int var6 = var2 + 1;
            byte var7 = var5[var2];
            var2 = var6 + 1;
            byte var9 = var5[var6];
            this.size -= 2L;
            if (var2 == var3) {
               this.head = var1.pop();
               SegmentPool.recycle(var1);
            } else {
               var1.pos = var2;
            }

            var8 = (short)((var7 & 255) << 8 | var9 & 255);
            var4 = var8;
         }

         return var4;
      }
   }

   public short readShortLe() {
      return Util.reverseBytesShort(this.readShort());
   }

   public String readString(long var1, Charset var3) throws EOFException {
      Util.checkOffsetAndCount(this.size, 0L, var1);
      if (var3 == null) {
         throw new IllegalArgumentException("charset == null");
      } else if (var1 > 2147483647L) {
         throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + var1);
      } else {
         String var6;
         if (var1 == 0L) {
            var6 = "";
         } else {
            Segment var4 = this.head;
            if ((long)var4.pos + var1 > (long)var4.limit) {
               var6 = new String(this.readByteArray(var1), var3);
            } else {
               String var5 = new String(var4.data, var4.pos, (int)var1, var3);
               var4.pos = (int)((long)var4.pos + var1);
               this.size -= var1;
               var6 = var5;
               if (var4.pos == var4.limit) {
                  this.head = var4.pop();
                  SegmentPool.recycle(var4);
                  var6 = var5;
               }
            }
         }

         return var6;
      }
   }

   public String readString(Charset var1) {
      try {
         String var3 = this.readString(this.size, var1);
         return var3;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public String readUtf8() {
      try {
         String var1 = this.readString(this.size, Util.UTF_8);
         return var1;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public String readUtf8(long var1) throws EOFException {
      return this.readString(var1, Util.UTF_8);
   }

   public int readUtf8CodePoint() throws EOFException {
      if (this.size == 0L) {
         throw new EOFException();
      } else {
         byte var1 = this.getByte(0L);
         int var2;
         byte var3;
         int var4;
         if ((var1 & 128) == 0) {
            var2 = var1 & 127;
            var3 = 1;
            var4 = 0;
         } else if ((var1 & 224) == 192) {
            var2 = var1 & 31;
            var3 = 2;
            var4 = 128;
         } else if ((var1 & 240) == 224) {
            var2 = var1 & 15;
            var3 = 3;
            var4 = 2048;
         } else {
            if ((var1 & 248) != 240) {
               this.skip(1L);
               var2 = 65533;
               return var2;
            }

            var2 = var1 & 7;
            var3 = 4;
            var4 = 65536;
         }

         if (this.size < (long)var3) {
            throw new EOFException("size < " + var3 + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(var1) + ")");
         } else {
            int var5 = 1;
            int var6 = var2;

            while(true) {
               if (var5 >= var3) {
                  this.skip((long)var3);
                  if (var6 > 1114111) {
                     var2 = 65533;
                  } else if (var6 >= 55296 && var6 <= 57343) {
                     var2 = 65533;
                  } else {
                     var2 = var6;
                     if (var6 < var4) {
                        var2 = 65533;
                     }
                  }
                  break;
               }

               byte var7 = this.getByte((long)var5);
               if ((var7 & 192) != 128) {
                  this.skip((long)var5);
                  var2 = 65533;
                  break;
               }

               var6 = var6 << 6 | var7 & 63;
               ++var5;
            }

            return var2;
         }
      }
   }

   public String readUtf8Line() throws EOFException {
      long var1 = this.indexOf((byte)10);
      String var3;
      if (var1 == -1L) {
         if (this.size != 0L) {
            var3 = this.readUtf8(this.size);
         } else {
            var3 = null;
         }
      } else {
         var3 = this.readUtf8Line(var1);
      }

      return var3;
   }

   String readUtf8Line(long var1) throws EOFException {
      String var3;
      if (var1 > 0L && this.getByte(var1 - 1L) == 13) {
         var3 = this.readUtf8(var1 - 1L);
         this.skip(2L);
      } else {
         var3 = this.readUtf8(var1);
         this.skip(1L);
      }

      return var3;
   }

   public String readUtf8LineStrict() throws EOFException {
      long var1 = this.indexOf((byte)10);
      if (var1 == -1L) {
         Buffer var3 = new Buffer();
         this.copyTo(var3, 0L, Math.min(32L, this.size));
         throw new EOFException("\\n not found: size=" + this.size() + " content=" + var3.readByteString().hex() + "…");
      } else {
         return this.readUtf8Line(var1);
      }
   }

   public boolean request(long var1) {
      boolean var3;
      if (this.size >= var1) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void require(long var1) throws EOFException {
      if (this.size < var1) {
         throw new EOFException();
      }
   }

   List segmentSizes() {
      Object var1;
      if (this.head == null) {
         var1 = Collections.emptyList();
      } else {
         ArrayList var2 = new ArrayList();
         var2.add(this.head.limit - this.head.pos);
         Segment var3 = this.head.next;

         while(true) {
            var1 = var2;
            if (var3 == this.head) {
               break;
            }

            var2.add(var3.limit - var3.pos);
            var3 = var3.next;
         }
      }

      return (List)var1;
   }

   public int select(Options var1) {
      Segment var2 = this.head;
      int var3;
      if (var2 == null) {
         var3 = var1.indexOf(ByteString.EMPTY);
      } else {
         ByteString[] var4 = var1.byteStrings;
         var3 = 0;
         int var5 = var4.length;

         while(true) {
            if (var3 >= var5) {
               var3 = -1;
               break;
            }

            ByteString var7 = var4[var3];
            if (this.size >= (long)var7.size() && this.rangeEquals(var2, var2.pos, var7, 0, var7.size())) {
               try {
                  this.skip((long)var7.size());
                  break;
               } catch (EOFException var6) {
                  throw new AssertionError(var6);
               }
            }

            ++var3;
         }
      }

      return var3;
   }

   int selectPrefix(Options var1) {
      Segment var2 = this.head;
      ByteString[] var8 = var1.byteStrings;
      int var3 = 0;
      int var4 = var8.length;

      int var7;
      while(true) {
         if (var3 >= var4) {
            var7 = -1;
            break;
         }

         ByteString var5 = var8[var3];
         int var6 = (int)Math.min(this.size, (long)var5.size());
         var7 = var3;
         if (var6 == 0) {
            break;
         }

         if (this.rangeEquals(var2, var2.pos, var5, 0, var6)) {
            var7 = var3;
            break;
         }

         ++var3;
      }

      return var7;
   }

   public ByteString sha1() {
      return this.digest("SHA-1");
   }

   public ByteString sha256() {
      return this.digest("SHA-256");
   }

   public long size() {
      return this.size;
   }

   public void skip(long var1) throws EOFException {
      while(var1 > 0L) {
         if (this.head == null) {
            throw new EOFException();
         }

         int var3 = (int)Math.min(var1, (long)(this.head.limit - this.head.pos));
         this.size -= (long)var3;
         long var4 = var1 - (long)var3;
         Segment var6 = this.head;
         var6.pos += var3;
         var1 = var4;
         if (this.head.pos == this.head.limit) {
            var6 = this.head;
            this.head = var6.pop();
            SegmentPool.recycle(var6);
            var1 = var4;
         }
      }

   }

   public ByteString snapshot() {
      if (this.size > 2147483647L) {
         throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
      } else {
         return this.snapshot((int)this.size);
      }
   }

   public ByteString snapshot(int var1) {
      Object var2;
      if (var1 == 0) {
         var2 = ByteString.EMPTY;
      } else {
         var2 = new SegmentedByteString(this, var1);
      }

      return (ByteString)var2;
   }

   public Timeout timeout() {
      return Timeout.NONE;
   }

   public String toString() {
      return this.snapshot().toString();
   }

   Segment writableSegment(int var1) {
      if (var1 >= 1 && var1 <= 8192) {
         Segment var2;
         Segment var4;
         if (this.head == null) {
            this.head = SegmentPool.take();
            var2 = this.head;
            Segment var3 = this.head;
            var4 = this.head;
            var3.prev = var4;
            var2.next = var4;
         } else {
            var2 = this.head.prev;
            if (var2.limit + var1 <= 8192) {
               var4 = var2;
               if (var2.owner) {
                  return var4;
               }
            }

            var4 = var2.push(SegmentPool.take());
         }

         return var4;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Buffer write(ByteString var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("byteString == null");
      } else {
         var1.write(this);
         return this;
      }
   }

   public Buffer write(byte[] var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         return this.write(var1, 0, var1.length);
      }
   }

   public Buffer write(byte[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);

         Segment var5;
         int var6;
         for(int var4 = var2 + var3; var2 < var4; var5.limit += var6) {
            var5 = this.writableSegment(1);
            var6 = Math.min(var4 - var2, 8192 - var5.limit);
            System.arraycopy(var1, var2, var5.data, var5.limit, var6);
            var2 += var6;
         }

         this.size += (long)var3;
         return this;
      }
   }

   public BufferedSink write(Source var1, long var2) throws IOException {
      while(var2 > 0L) {
         long var4 = var1.read(this, var2);
         if (var4 == -1L) {
            throw new EOFException();
         }

         var2 -= var4;
      }

      return this;
   }

   public void write(Buffer var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else if (var1 == this) {
         throw new IllegalArgumentException("source == this");
      } else {
         Util.checkOffsetAndCount(var1.size, 0L, var2);

         while(var2 > 0L) {
            Segment var4;
            long var5;
            if (var2 < (long)(var1.head.limit - var1.head.pos)) {
               if (this.head != null) {
                  var4 = this.head.prev;
               } else {
                  var4 = null;
               }

               if (var4 != null && var4.owner) {
                  var5 = (long)var4.limit;
                  int var7;
                  if (var4.shared) {
                     var7 = 0;
                  } else {
                     var7 = var4.pos;
                  }

                  if (var2 + var5 - (long)var7 <= 8192L) {
                     var1.head.writeTo(var4, (int)var2);
                     var1.size -= var2;
                     this.size += var2;
                     break;
                  }
               }

               var1.head = var1.head.split((int)var2);
            }

            var4 = var1.head;
            var5 = (long)(var4.limit - var4.pos);
            var1.head = var4.pop();
            if (this.head == null) {
               this.head = var4;
               var4 = this.head;
               Segment var8 = this.head;
               Segment var9 = this.head;
               var8.prev = var9;
               var4.next = var9;
            } else {
               this.head.prev.push(var4).compact();
            }

            var1.size -= var5;
            this.size += var5;
            var2 -= var5;
         }

      }
   }

   public long writeAll(Source var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         long var2 = 0L;

         while(true) {
            long var4 = var1.read(this, 8192L);
            if (var4 == -1L) {
               return var2;
            }

            var2 += var4;
         }
      }
   }

   public Buffer writeByte(int var1) {
      Segment var2 = this.writableSegment(1);
      byte[] var3 = var2.data;
      int var4 = var2.limit++;
      var3[var4] = (byte)((byte)var1);
      ++this.size;
      return this;
   }

   public Buffer writeDecimalLong(long var1) {
      Buffer var3;
      if (var1 == 0L) {
         var3 = this.writeByte(48);
      } else {
         boolean var4 = false;
         long var5 = var1;
         if (var1 < 0L) {
            var5 = -var1;
            if (var5 < 0L) {
               var3 = this.writeUtf8("-9223372036854775808");
               return var3;
            }

            var4 = true;
         }

         byte var7;
         if (var5 < 100000000L) {
            if (var5 < 10000L) {
               if (var5 < 100L) {
                  if (var5 < 10L) {
                     var7 = 1;
                  } else {
                     var7 = 2;
                  }
               } else if (var5 < 1000L) {
                  var7 = 3;
               } else {
                  var7 = 4;
               }
            } else if (var5 < 1000000L) {
               if (var5 < 100000L) {
                  var7 = 5;
               } else {
                  var7 = 6;
               }
            } else if (var5 < 10000000L) {
               var7 = 7;
            } else {
               var7 = 8;
            }
         } else if (var5 < 1000000000000L) {
            if (var5 < 10000000000L) {
               if (var5 < 1000000000L) {
                  var7 = 9;
               } else {
                  var7 = 10;
               }
            } else if (var5 < 100000000000L) {
               var7 = 11;
            } else {
               var7 = 12;
            }
         } else if (var5 < 1000000000000000L) {
            if (var5 < 10000000000000L) {
               var7 = 13;
            } else if (var5 < 100000000000000L) {
               var7 = 14;
            } else {
               var7 = 15;
            }
         } else if (var5 < 100000000000000000L) {
            if (var5 < 10000000000000000L) {
               var7 = 16;
            } else {
               var7 = 17;
            }
         } else if (var5 < 1000000000000000000L) {
            var7 = 18;
         } else {
            var7 = 19;
         }

         int var8 = var7;
         if (var4) {
            var8 = var7 + 1;
         }

         Segment var11 = this.writableSegment(var8);
         byte[] var9 = var11.data;

         int var12;
         for(var12 = var11.limit + var8; var5 != 0L; var5 /= 10L) {
            int var10 = (int)(var5 % 10L);
            --var12;
            var9[var12] = (byte)DIGITS[var10];
         }

         if (var4) {
            var9[var12 - 1] = (byte)45;
         }

         var11.limit += var8;
         this.size += (long)var8;
         var3 = this;
      }

      return var3;
   }

   public Buffer writeHexadecimalUnsignedLong(long var1) {
      Buffer var3;
      if (var1 == 0L) {
         var3 = this.writeByte(48);
      } else {
         int var4 = Long.numberOfTrailingZeros(Long.highestOneBit(var1)) / 4 + 1;
         Segment var8 = this.writableSegment(var4);
         byte[] var5 = var8.data;
         int var6 = var8.limit + var4 - 1;

         for(int var7 = var8.limit; var6 >= var7; --var6) {
            var5[var6] = (byte)DIGITS[(int)(15L & var1)];
            var1 >>>= 4;
         }

         var8.limit += var4;
         this.size += (long)var4;
         var3 = this;
      }

      return var3;
   }

   public Buffer writeInt(int var1) {
      Segment var2 = this.writableSegment(4);
      byte[] var3 = var2.data;
      int var4 = var2.limit;
      int var5 = var4 + 1;
      var3[var4] = (byte)((byte)(var1 >>> 24 & 255));
      var4 = var5 + 1;
      var3[var5] = (byte)((byte)(var1 >>> 16 & 255));
      var5 = var4 + 1;
      var3[var4] = (byte)((byte)(var1 >>> 8 & 255));
      var3[var5] = (byte)((byte)(var1 & 255));
      var2.limit = var5 + 1;
      this.size += 4L;
      return this;
   }

   public Buffer writeIntLe(int var1) {
      return this.writeInt(Util.reverseBytesInt(var1));
   }

   public Buffer writeLong(long var1) {
      Segment var3 = this.writableSegment(8);
      byte[] var4 = var3.data;
      int var5 = var3.limit;
      int var6 = var5 + 1;
      var4[var5] = (byte)((byte)((int)(var1 >>> 56 & 255L)));
      var5 = var6 + 1;
      var4[var6] = (byte)((byte)((int)(var1 >>> 48 & 255L)));
      var6 = var5 + 1;
      var4[var5] = (byte)((byte)((int)(var1 >>> 40 & 255L)));
      var5 = var6 + 1;
      var4[var6] = (byte)((byte)((int)(var1 >>> 32 & 255L)));
      var6 = var5 + 1;
      var4[var5] = (byte)((byte)((int)(var1 >>> 24 & 255L)));
      var5 = var6 + 1;
      var4[var6] = (byte)((byte)((int)(var1 >>> 16 & 255L)));
      var6 = var5 + 1;
      var4[var5] = (byte)((byte)((int)(var1 >>> 8 & 255L)));
      var4[var6] = (byte)((byte)((int)(var1 & 255L)));
      var3.limit = var6 + 1;
      this.size += 8L;
      return this;
   }

   public Buffer writeLongLe(long var1) {
      return this.writeLong(Util.reverseBytesLong(var1));
   }

   public Buffer writeShort(int var1) {
      Segment var2 = this.writableSegment(2);
      byte[] var3 = var2.data;
      int var4 = var2.limit;
      int var5 = var4 + 1;
      var3[var4] = (byte)((byte)(var1 >>> 8 & 255));
      var3[var5] = (byte)((byte)(var1 & 255));
      var2.limit = var5 + 1;
      this.size += 2L;
      return this;
   }

   public Buffer writeShortLe(int var1) {
      return this.writeShort(Util.reverseBytesShort((short)var1));
   }

   public Buffer writeString(String var1, int var2, int var3, Charset var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("string == null");
      } else if (var2 < 0) {
         throw new IllegalAccessError("beginIndex < 0: " + var2);
      } else if (var3 < var2) {
         throw new IllegalArgumentException("endIndex < beginIndex: " + var3 + " < " + var2);
      } else if (var3 > var1.length()) {
         throw new IllegalArgumentException("endIndex > string.length: " + var3 + " > " + var1.length());
      } else if (var4 == null) {
         throw new IllegalArgumentException("charset == null");
      } else {
         Buffer var5;
         if (var4.equals(Util.UTF_8)) {
            var5 = this.writeUtf8(var1, var2, var3);
         } else {
            byte[] var6 = var1.substring(var2, var3).getBytes(var4);
            var5 = this.write(var6, 0, var6.length);
         }

         return var5;
      }
   }

   public Buffer writeString(String var1, Charset var2) {
      return this.writeString(var1, 0, var1.length(), var2);
   }

   public Buffer writeTo(OutputStream var1) throws IOException {
      return this.writeTo(var1, this.size);
   }

   public Buffer writeTo(OutputStream var1, long var2) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         Util.checkOffsetAndCount(this.size, 0L, var2);
         Segment var4 = this.head;

         while(true) {
            Segment var5 = var4;
            if (var2 <= 0L) {
               return this;
            }

            int var6 = (int)Math.min(var2, (long)(var4.limit - var4.pos));
            var1.write(var4.data, var4.pos, var6);
            var4.pos += var6;
            this.size -= (long)var6;
            long var7 = var2 - (long)var6;
            var4 = var4;
            var2 = var7;
            if (var5.pos == var5.limit) {
               var4 = var5.pop();
               this.head = var4;
               SegmentPool.recycle(var5);
               var2 = var7;
            }
         }
      }
   }

   public Buffer writeUtf8(String var1) {
      return this.writeUtf8(var1, 0, var1.length());
   }

   public Buffer writeUtf8(String var1, int var2, int var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("string == null");
      } else if (var2 < 0) {
         throw new IllegalAccessError("beginIndex < 0: " + var2);
      } else if (var3 < var2) {
         throw new IllegalArgumentException("endIndex < beginIndex: " + var3 + " < " + var2);
      } else if (var3 > var1.length()) {
         throw new IllegalArgumentException("endIndex > string.length: " + var3 + " > " + var1.length());
      } else {
         while(true) {
            while(var2 < var3) {
               char var4 = var1.charAt(var2);
               int var9;
               if (var4 < 128) {
                  Segment var5 = this.writableSegment(1);
                  byte[] var6 = var5.data;
                  int var7 = var5.limit - var2;
                  var9 = Math.min(var3, 8192 - var7);
                  var6[var7 + var2] = (byte)((byte)var4);
                  ++var2;

                  while(var2 < var9) {
                     var4 = var1.charAt(var2);
                     if (var4 >= 128) {
                        break;
                     }

                     var6[var7 + var2] = (byte)((byte)var4);
                     ++var2;
                  }

                  var9 = var2 + var7 - var5.limit;
                  var5.limit += var9;
                  this.size += (long)var9;
               } else if (var4 < 2048) {
                  this.writeByte(var4 >> 6 | 192);
                  this.writeByte(var4 & 63 | 128);
                  ++var2;
               } else if (var4 >= '\ud800' && var4 <= '\udfff') {
                  char var8;
                  if (var2 + 1 < var3) {
                     var8 = var1.charAt(var2 + 1);
                  } else {
                     var8 = 0;
                  }

                  if (var4 <= '\udbff' && var8 >= '\udc00' && var8 <= '\udfff') {
                     var9 = 65536 + ((-55297 & var4) << 10 | -56321 & var8);
                     this.writeByte(var9 >> 18 | 240);
                     this.writeByte(var9 >> 12 & 63 | 128);
                     this.writeByte(var9 >> 6 & 63 | 128);
                     this.writeByte(var9 & 63 | 128);
                     var2 += 2;
                  } else {
                     this.writeByte(63);
                     ++var2;
                  }
               } else {
                  this.writeByte(var4 >> 12 | 224);
                  this.writeByte(var4 >> 6 & 63 | 128);
                  this.writeByte(var4 & 63 | 128);
                  ++var2;
               }
            }

            return this;
         }
      }
   }

   public Buffer writeUtf8CodePoint(int var1) {
      if (var1 < 128) {
         this.writeByte(var1);
      } else if (var1 < 2048) {
         this.writeByte(var1 >> 6 | 192);
         this.writeByte(var1 & 63 | 128);
      } else if (var1 < 65536) {
         if (var1 >= 55296 && var1 <= 57343) {
            throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(var1));
         }

         this.writeByte(var1 >> 12 | 224);
         this.writeByte(var1 >> 6 & 63 | 128);
         this.writeByte(var1 & 63 | 128);
      } else {
         if (var1 > 1114111) {
            throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(var1));
         }

         this.writeByte(var1 >> 18 | 240);
         this.writeByte(var1 >> 12 & 63 | 128);
         this.writeByte(var1 >> 6 & 63 | 128);
         this.writeByte(var1 & 63 | 128);
      }

      return this;
   }
}
