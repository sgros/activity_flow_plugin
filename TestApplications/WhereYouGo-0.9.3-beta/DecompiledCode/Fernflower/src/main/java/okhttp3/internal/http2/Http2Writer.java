package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

final class Http2Writer implements Closeable {
   private static final Logger logger = Logger.getLogger(Http2.class.getName());
   private final boolean client;
   private boolean closed;
   private final Buffer hpackBuffer;
   final Hpack.Writer hpackWriter;
   private int maxFrameSize;
   private final BufferedSink sink;

   public Http2Writer(BufferedSink var1, boolean var2) {
      this.sink = var1;
      this.client = var2;
      this.hpackBuffer = new Buffer();
      this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
      this.maxFrameSize = 16384;
   }

   private void writeContinuationFrames(int var1, long var2) throws IOException {
      while(var2 > 0L) {
         int var4 = (int)Math.min((long)this.maxFrameSize, var2);
         var2 -= (long)var4;
         byte var5;
         byte var6;
         if (var2 == 0L) {
            var5 = 4;
            var6 = var5;
         } else {
            var5 = 0;
            var6 = var5;
         }

         this.frameHeader(var1, var4, (byte)9, var6);
         this.sink.write(this.hpackBuffer, (long)var4);
      }

   }

   private static void writeMedium(BufferedSink var0, int var1) throws IOException {
      var0.writeByte(var1 >>> 16 & 255);
      var0.writeByte(var1 >>> 8 & 255);
      var0.writeByte(var1 & 255);
   }

   public void applyAndAckSettings(Settings var1) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var4 = new IOException("closed");
            throw var4;
         }

         this.maxFrameSize = var1.getMaxFrameSize(this.maxFrameSize);
         if (var1.getHeaderTableSize() != -1) {
            this.hpackWriter.setHeaderTableSizeSetting(var1.getHeaderTableSize());
         }

         this.frameHeader(0, 0, (byte)4, (byte)1);
         this.sink.flush();
      } finally {
         ;
      }

   }

   public void close() throws IOException {
      synchronized(this){}

      try {
         this.closed = true;
         this.sink.close();
      } finally {
         ;
      }

   }

   public void connectionPreface() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label206: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var23 = new IOException("closed");
               throw var23;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label206;
         }

         boolean var2;
         try {
            var2 = this.client;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label206;
         }

         if (var2) {
            try {
               if (logger.isLoggable(Level.FINE)) {
                  logger.fine(Util.format(">> CONNECTION %s", Http2.CONNECTION_PREFACE.hex()));
               }
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label206;
            }

            try {
               this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
               this.sink.flush();
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label206;
            }
         }

         return;
      }

      Throwable var1 = var10000;
      throw var1;
   }

   public void data(boolean var1, int var2, Buffer var3, int var4) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label91: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var14 = new IOException("closed");
               throw var14;
            }
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label91;
         }

         byte var5 = 0;
         byte var6 = var5;
         if (var1) {
            byte var15 = (byte)1;
            var6 = var15;
         }

         label81:
         try {
            this.dataFrame(var2, var6, var3, var4);
            return;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label81;
         }
      }

      Throwable var13 = var10000;
      throw var13;
   }

   void dataFrame(int var1, byte var2, Buffer var3, int var4) throws IOException {
      this.frameHeader(var1, var4, (byte)0, var2);
      if (var4 > 0) {
         this.sink.write(var3, (long)var4);
      }

   }

   public void flush() throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var1 = new IOException("closed");
            throw var1;
         }

         this.sink.flush();
      } finally {
         ;
      }

   }

   public void frameHeader(int var1, int var2, byte var3, byte var4) throws IOException {
      if (logger.isLoggable(Level.FINE)) {
         logger.fine(Http2.frameLog(false, var1, var2, var3, var4));
      }

      if (var2 > this.maxFrameSize) {
         throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", this.maxFrameSize, var2);
      } else if ((Integer.MIN_VALUE & var1) != 0) {
         throw Http2.illegalArgument("reserved bit set: %s", var1);
      } else {
         writeMedium(this.sink, var2);
         this.sink.writeByte(var3 & 255);
         this.sink.writeByte(var4 & 255);
         this.sink.writeInt(Integer.MAX_VALUE & var1);
      }
   }

   public void goAway(int var1, ErrorCode var2, byte[] var3) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var6 = new IOException("closed");
            throw var6;
         }

         if (var2.httpCode == -1) {
            throw Http2.illegalArgument("errorCode.httpCode == -1");
         }

         this.frameHeader(0, var3.length + 8, (byte)7, (byte)0);
         this.sink.writeInt(var1);
         this.sink.writeInt(var2.httpCode);
         if (var3.length > 0) {
            this.sink.write(var3);
         }

         this.sink.flush();
      } finally {
         ;
      }

   }

   public void headers(int var1, List var2) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var5 = new IOException("closed");
            throw var5;
         }

         this.headers(false, var1, var2);
      } finally {
         ;
      }

   }

   void headers(boolean var1, int var2, List var3) throws IOException {
      if (this.closed) {
         throw new IOException("closed");
      } else {
         this.hpackWriter.writeHeaders(var3);
         long var4 = this.hpackBuffer.size();
         int var6 = (int)Math.min((long)this.maxFrameSize, var4);
         byte var7;
         if (var4 == (long)var6) {
            var7 = 4;
         } else {
            var7 = 0;
         }

         byte var8 = var7;
         if (var1) {
            byte var9 = (byte)(var7 | 1);
            var8 = var9;
         }

         this.frameHeader(var2, var6, (byte)1, var8);
         this.sink.write(this.hpackBuffer, (long)var6);
         if (var4 > (long)var6) {
            this.writeContinuationFrames(var2, var4 - (long)var6);
         }

      }
   }

   public int maxDataLength() {
      return this.maxFrameSize;
   }

   public void ping(boolean var1, int var2, int var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label93: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var13 = new IOException("closed");
               throw var13;
            }
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label93;
         }

         byte var5;
         byte var6;
         if (var1) {
            var5 = 1;
            var6 = var5;
         } else {
            var5 = 0;
            var6 = var5;
         }

         try {
            this.frameHeader(0, 8, (byte)6, var6);
            this.sink.writeInt(var2);
            this.sink.writeInt(var3);
            this.sink.flush();
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label93;
         }

         return;
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void pushPromise(int var1, int var2, List var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label215: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var30 = new IOException("closed");
               throw var30;
            }
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            break label215;
         }

         long var4;
         int var6;
         try {
            this.hpackWriter.writeHeaders(var3);
            var4 = this.hpackBuffer.size();
            var6 = (int)Math.min((long)(this.maxFrameSize - 4), var4);
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label215;
         }

         byte var7;
         byte var8;
         if (var4 == (long)var6) {
            var7 = 4;
            var8 = var7;
         } else {
            var7 = 0;
            var8 = var7;
         }

         try {
            this.frameHeader(var1, var6 + 4, (byte)5, var8);
            this.sink.writeInt(Integer.MAX_VALUE & var2);
            this.sink.write(this.hpackBuffer, (long)var6);
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label215;
         }

         if (var4 > (long)var6) {
            try {
               this.writeContinuationFrames(var1, var4 - (long)var6);
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label215;
            }
         }

         return;
      }

      Throwable var29 = var10000;
      throw var29;
   }

   public void rstStream(int var1, ErrorCode var2) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var6 = new IOException("closed");
            throw var6;
         }

         if (var2.httpCode == -1) {
            IllegalArgumentException var5 = new IllegalArgumentException();
            throw var5;
         }

         this.frameHeader(var1, 4, (byte)3, (byte)0);
         this.sink.writeInt(var2.httpCode);
         this.sink.flush();
      } finally {
         ;
      }

   }

   public void settings(Settings var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label347: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var36 = new IOException("closed");
               throw var36;
            }
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label347;
         }

         try {
            this.frameHeader(0, var1.size() * 6, (byte)4, (byte)0);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label347;
         }

         int var2 = 0;

         while(true) {
            if (var2 >= 10) {
               try {
                  this.sink.flush();
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break;
               }

               return;
            }

            label349: {
               try {
                  if (!var1.isSet(var2)) {
                     break label349;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break;
               }

               int var4;
               if (var2 == 4) {
                  var4 = 3;
               } else {
                  var4 = var2;
                  if (var2 == 7) {
                     var4 = 4;
                  }
               }

               try {
                  this.sink.writeShort(var4);
                  this.sink.writeInt(var1.get(var2));
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break;
               }
            }

            ++var2;
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }

   public void synReply(boolean var1, int var2, List var3) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var6 = new IOException("closed");
            throw var6;
         }

         this.headers(var1, var2, var3);
      } finally {
         ;
      }

   }

   public void synStream(boolean var1, int var2, int var3, List var4) throws IOException {
      synchronized(this){}

      try {
         if (this.closed) {
            IOException var7 = new IOException("closed");
            throw var7;
         }

         this.headers(var1, var2, var4);
      } finally {
         ;
      }

   }

   public void windowUpdate(int var1, long var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         try {
            if (this.closed) {
               IOException var17 = new IOException("closed");
               throw var17;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label136;
         }

         if (var2 != 0L && var2 <= 2147483647L) {
            label123: {
               try {
                  this.frameHeader(var1, 4, (byte)8, (byte)0);
                  this.sink.writeInt((int)var2);
                  this.sink.flush();
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label123;
               }

               return;
            }
         } else {
            label125:
            try {
               throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", var2);
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label125;
            }
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }
}
