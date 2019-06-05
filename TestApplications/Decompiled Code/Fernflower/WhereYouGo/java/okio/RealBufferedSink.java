package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
   public final Buffer buffer = new Buffer();
   boolean closed;
   public final Sink sink;

   RealBufferedSink(Sink var1) {
      if (var1 == null) {
         throw new NullPointerException("sink == null");
      } else {
         this.sink = var1;
      }
   }

   public Buffer buffer() {
      return this.buffer;
   }

   public void close() throws IOException {
      if (!this.closed) {
         Throwable var1 = null;
         Throwable var2 = var1;

         label31: {
            try {
               if (this.buffer.size <= 0L) {
                  break label31;
               }

               this.sink.write(this.buffer, this.buffer.size);
            } catch (Throwable var5) {
               var2 = var5;
               break label31;
            }

            var2 = var1;
         }

         label25: {
            try {
               this.sink.close();
            } catch (Throwable var4) {
               var1 = var2;
               if (var2 == null) {
                  var1 = var4;
               }
               break label25;
            }

            var1 = var2;
         }

         this.closed = true;
         if (var1 != null) {
            Util.sneakyRethrow(var1);
         }
      }

   }

   public BufferedSink emit() throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         long var1 = this.buffer.size();
         if (var1 > 0L) {
            this.sink.write(this.buffer, var1);
         }

         return this;
      }
   }

   public BufferedSink emitCompleteSegments() throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         long var1 = this.buffer.completeSegmentByteCount();
         if (var1 > 0L) {
            this.sink.write(this.buffer, var1);
         }

         return this;
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         if (this.buffer.size > 0L) {
            this.sink.write(this.buffer, this.buffer.size);
         }

         this.sink.flush();
      }
   }

   public OutputStream outputStream() {
      return new OutputStream() {
         public void close() throws IOException {
            RealBufferedSink.this.close();
         }

         public void flush() throws IOException {
            if (!RealBufferedSink.this.closed) {
               RealBufferedSink.this.flush();
            }

         }

         public String toString() {
            return RealBufferedSink.this + ".outputStream()";
         }

         public void write(int var1) throws IOException {
            if (RealBufferedSink.this.closed) {
               throw new IOException("closed");
            } else {
               RealBufferedSink.this.buffer.writeByte((byte)var1);
               RealBufferedSink.this.emitCompleteSegments();
            }
         }

         public void write(byte[] var1, int var2, int var3) throws IOException {
            if (RealBufferedSink.this.closed) {
               throw new IOException("closed");
            } else {
               RealBufferedSink.this.buffer.write(var1, var2, var3);
               RealBufferedSink.this.emitCompleteSegments();
            }
         }
      };
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public String toString() {
      return "buffer(" + this.sink + ")";
   }

   public BufferedSink write(ByteString var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.write(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink write(Source var1, long var2) throws IOException {
      while(var2 > 0L) {
         long var4 = var1.read(this.buffer, var2);
         if (var4 == -1L) {
            throw new EOFException();
         }

         var2 -= var4;
         this.emitCompleteSegments();
      }

      return this;
   }

   public BufferedSink write(byte[] var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.write(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.write(var1, var2, var3);
         return this.emitCompleteSegments();
      }
   }

   public void write(Buffer var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.write(var1, var2);
         this.emitCompleteSegments();
      }
   }

   public long writeAll(Source var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         long var2 = 0L;

         while(true) {
            long var4 = var1.read(this.buffer, 8192L);
            if (var4 == -1L) {
               return var2;
            }

            var2 += var4;
            this.emitCompleteSegments();
         }
      }
   }

   public BufferedSink writeByte(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeByte(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeDecimalLong(long var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeDecimalLong(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeHexadecimalUnsignedLong(long var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeHexadecimalUnsignedLong(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeInt(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeInt(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeIntLe(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeIntLe(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeLong(long var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeLong(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeLongLe(long var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeLongLe(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeShort(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeShort(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeShortLe(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeShortLe(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeString(String var1, int var2, int var3, Charset var4) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeString(var1, var2, var3, var4);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeString(String var1, Charset var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeString(var1, var2);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeUtf8(String var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeUtf8(var1);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeUtf8(String var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeUtf8(var1, var2, var3);
         return this.emitCompleteSegments();
      }
   }

   public BufferedSink writeUtf8CodePoint(int var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         this.buffer.writeUtf8CodePoint(var1);
         return this.emitCompleteSegments();
      }
   }
}
