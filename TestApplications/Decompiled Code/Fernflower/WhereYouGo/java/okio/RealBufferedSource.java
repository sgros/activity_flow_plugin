package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

final class RealBufferedSource implements BufferedSource {
   public final Buffer buffer = new Buffer();
   boolean closed;
   public final Source source;

   RealBufferedSource(Source var1) {
      if (var1 == null) {
         throw new NullPointerException("source == null");
      } else {
         this.source = var1;
      }
   }

   public Buffer buffer() {
      return this.buffer;
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.source.close();
         this.buffer.clear();
      }

   }

   public boolean exhausted() throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         boolean var1;
         if (this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public long indexOf(byte var1) throws IOException {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(byte var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(true) {
            long var4 = this.buffer.indexOf(var1, var2);
            if (var4 != -1L) {
               var2 = var4;
               break;
            }

            var4 = this.buffer.size;
            if (this.source.read(this.buffer, 8192L) == -1L) {
               var2 = -1L;
               break;
            }

            var2 = Math.max(var2, var4);
         }

         return var2;
      }
   }

   public long indexOf(ByteString var1) throws IOException {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(ByteString var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(true) {
            long var4 = this.buffer.indexOf(var1, var2);
            if (var4 != -1L) {
               var2 = var4;
               break;
            }

            var4 = this.buffer.size;
            if (this.source.read(this.buffer, 8192L) == -1L) {
               var2 = -1L;
               break;
            }

            var2 = Math.max(var2, var4 - (long)var1.size() + 1L);
         }

         return var2;
      }
   }

   public long indexOfElement(ByteString var1) throws IOException {
      return this.indexOfElement(var1, 0L);
   }

   public long indexOfElement(ByteString var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(true) {
            long var4 = this.buffer.indexOfElement(var1, var2);
            if (var4 != -1L) {
               var2 = var4;
               break;
            }

            var4 = this.buffer.size;
            if (this.source.read(this.buffer, 8192L) == -1L) {
               var2 = -1L;
               break;
            }

            var2 = Math.max(var2, var4);
         }

         return var2;
      }
   }

   public InputStream inputStream() {
      return new InputStream() {
         public int available() throws IOException {
            if (RealBufferedSource.this.closed) {
               throw new IOException("closed");
            } else {
               return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            }
         }

         public void close() throws IOException {
            RealBufferedSource.this.close();
         }

         public int read() throws IOException {
            if (RealBufferedSource.this.closed) {
               throw new IOException("closed");
            } else {
               int var1;
               if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) {
                  var1 = -1;
               } else {
                  var1 = RealBufferedSource.this.buffer.readByte() & 255;
               }

               return var1;
            }
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            if (RealBufferedSource.this.closed) {
               throw new IOException("closed");
            } else {
               Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
               if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) {
                  var2 = -1;
               } else {
                  var2 = RealBufferedSource.this.buffer.read(var1, var2, var3);
               }

               return var2;
            }
         }

         public String toString() {
            return RealBufferedSource.this + ".inputStream()";
         }
      };
   }

   public boolean rangeEquals(long var1, ByteString var3) throws IOException {
      return this.rangeEquals(var1, var3, 0, var3.size());
   }

   public boolean rangeEquals(long var1, ByteString var3, int var4, int var5) throws IOException {
      boolean var6 = false;
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         boolean var7 = var6;
         if (var1 >= 0L) {
            var7 = var6;
            if (var4 >= 0) {
               var7 = var6;
               if (var5 >= 0) {
                  if (var3.size() - var4 < var5) {
                     var7 = var6;
                  } else {
                     int var8 = 0;

                     while(true) {
                        if (var8 >= var5) {
                           var7 = true;
                           break;
                        }

                        long var9 = var1 + (long)var8;
                        var7 = var6;
                        if (!this.request(1L + var9)) {
                           break;
                        }

                        var7 = var6;
                        if (this.buffer.getByte(var9) != var3.getByte(var4 + var8)) {
                           break;
                        }

                        ++var8;
                     }
                  }
               }
            }
         }

         return var7;
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
      if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
         var2 = -1;
      } else {
         var3 = (int)Math.min((long)var3, this.buffer.size);
         var2 = this.buffer.read(var1, var2, var3);
      }

      return var2;
   }

   public long read(Buffer var1, long var2) throws IOException {
      long var4 = -1L;
      if (var1 == null) {
         throw new IllegalArgumentException("sink == null");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
            var2 = var4;
         } else {
            var2 = Math.min(var2, this.buffer.size);
            var2 = this.buffer.read(var1, var2);
         }

         return var2;
      }
   }

   public long readAll(Sink var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("sink == null");
      } else {
         long var2 = 0L;

         long var4;
         while(this.source.read(this.buffer, 8192L) != -1L) {
            var4 = this.buffer.completeSegmentByteCount();
            if (var4 > 0L) {
               var2 += var4;
               var1.write(this.buffer, var4);
            }
         }

         var4 = var2;
         if (this.buffer.size() > 0L) {
            var4 = var2 + this.buffer.size();
            var1.write(this.buffer, this.buffer.size());
         }

         return var4;
      }
   }

   public byte readByte() throws IOException {
      this.require(1L);
      return this.buffer.readByte();
   }

   public byte[] readByteArray() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readByteArray();
   }

   public byte[] readByteArray(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readByteArray(var1);
   }

   public ByteString readByteString() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readByteString();
   }

   public ByteString readByteString(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readByteString(var1);
   }

   public long readDecimalLong() throws IOException {
      this.require(1L);

      for(int var1 = 0; this.request((long)(var1 + 1)); ++var1) {
         byte var2 = this.buffer.getByte((long)var1);
         if ((var2 < 48 || var2 > 57) && (var1 != 0 || var2 != 45)) {
            if (var1 == 0) {
               throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", var2));
            }
            break;
         }
      }

      return this.buffer.readDecimalLong();
   }

   public void readFully(Buffer var1, long var2) throws IOException {
      try {
         this.require(var2);
      } catch (EOFException var5) {
         var1.writeAll(this.buffer);
         throw var5;
      }

      this.buffer.readFully(var1, var2);
   }

   public void readFully(byte[] var1) throws IOException {
      try {
         this.require((long)var1.length);
      } catch (EOFException var5) {
         int var4;
         for(int var3 = 0; this.buffer.size > 0L; var3 += var4) {
            var4 = this.buffer.read(var1, var3, (int)this.buffer.size);
            if (var4 == -1) {
               throw new AssertionError();
            }
         }

         throw var5;
      }

      this.buffer.readFully(var1);
   }

   public long readHexadecimalUnsignedLong() throws IOException {
      this.require(1L);

      for(int var1 = 0; this.request((long)(var1 + 1)); ++var1) {
         byte var2 = this.buffer.getByte((long)var1);
         if ((var2 < 48 || var2 > 57) && (var2 < 97 || var2 > 102) && (var2 < 65 || var2 > 70)) {
            if (var1 == 0) {
               throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", var2));
            }
            break;
         }
      }

      return this.buffer.readHexadecimalUnsignedLong();
   }

   public int readInt() throws IOException {
      this.require(4L);
      return this.buffer.readInt();
   }

   public int readIntLe() throws IOException {
      this.require(4L);
      return this.buffer.readIntLe();
   }

   public long readLong() throws IOException {
      this.require(8L);
      return this.buffer.readLong();
   }

   public long readLongLe() throws IOException {
      this.require(8L);
      return this.buffer.readLongLe();
   }

   public short readShort() throws IOException {
      this.require(2L);
      return this.buffer.readShort();
   }

   public short readShortLe() throws IOException {
      this.require(2L);
      return this.buffer.readShortLe();
   }

   public String readString(long var1, Charset var3) throws IOException {
      this.require(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("charset == null");
      } else {
         return this.buffer.readString(var1, var3);
      }
   }

   public String readString(Charset var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("charset == null");
      } else {
         this.buffer.writeAll(this.source);
         return this.buffer.readString(var1);
      }
   }

   public String readUtf8() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readUtf8();
   }

   public String readUtf8(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readUtf8(var1);
   }

   public int readUtf8CodePoint() throws IOException {
      this.require(1L);
      byte var1 = this.buffer.getByte(0L);
      if ((var1 & 224) == 192) {
         this.require(2L);
      } else if ((var1 & 240) == 224) {
         this.require(3L);
      } else if ((var1 & 248) == 240) {
         this.require(4L);
      }

      return this.buffer.readUtf8CodePoint();
   }

   public String readUtf8Line() throws IOException {
      long var1 = this.indexOf((byte)10);
      String var3;
      if (var1 == -1L) {
         if (this.buffer.size != 0L) {
            var3 = this.readUtf8(this.buffer.size);
         } else {
            var3 = null;
         }
      } else {
         var3 = this.buffer.readUtf8Line(var1);
      }

      return var3;
   }

   public String readUtf8LineStrict() throws IOException {
      long var1 = this.indexOf((byte)10);
      if (var1 == -1L) {
         Buffer var3 = new Buffer();
         this.buffer.copyTo(var3, 0L, Math.min(32L, this.buffer.size()));
         throw new EOFException("\\n not found: size=" + this.buffer.size() + " content=" + var3.readByteString().hex() + "…");
      } else {
         return this.buffer.readUtf8Line(var1);
      }
   }

   public boolean request(long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var1);
      } else if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         boolean var3;
         while(true) {
            if (this.buffer.size < var1) {
               if (this.source.read(this.buffer, 8192L) != -1L) {
                  continue;
               }

               var3 = false;
               break;
            }

            var3 = true;
            break;
         }

         return var3;
      }
   }

   public void require(long var1) throws IOException {
      if (!this.request(var1)) {
         throw new EOFException();
      }
   }

   public int select(Options var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         int var2;
         while(true) {
            var2 = this.buffer.selectPrefix(var1);
            if (var2 == -1) {
               var2 = -1;
               break;
            }

            int var3 = var1.byteStrings[var2].size();
            if ((long)var3 <= this.buffer.size) {
               this.buffer.skip((long)var3);
               break;
            }

            if (this.source.read(this.buffer, 8192L) == -1L) {
               var2 = -1;
               break;
            }
         }

         return var2;
      }
   }

   public void skip(long var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(var1 > 0L) {
            if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
               throw new EOFException();
            }

            long var3 = Math.min(var1, this.buffer.size());
            this.buffer.skip(var3);
            var1 -= var3;
         }

      }
   }

   public Timeout timeout() {
      return this.source.timeout();
   }

   public String toString() {
      return "buffer(" + this.source + ")";
   }
}
