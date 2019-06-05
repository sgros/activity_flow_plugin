package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Http2Reader implements Closeable {
   static final Logger logger = Logger.getLogger(Http2.class.getName());
   private final boolean client;
   private final Http2Reader.ContinuationSource continuation;
   final Hpack.Reader hpackReader;
   private final BufferedSource source;

   public Http2Reader(BufferedSource var1, boolean var2) {
      this.source = var1;
      this.client = var2;
      this.continuation = new Http2Reader.ContinuationSource(this.source);
      this.hpackReader = new Hpack.Reader(4096, this.continuation);
   }

   static int lengthWithoutPadding(int var0, byte var1, short var2) throws IOException {
      int var3 = var0;
      if ((var1 & 8) != 0) {
         var3 = var0 - 1;
      }

      if (var2 > var3) {
         throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", var2, var3);
      } else {
         return (short)(var3 - var2);
      }
   }

   private void readData(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      boolean var5 = true;
      byte var6 = 0;
      boolean var7;
      if ((var3 & 1) != 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      if ((var3 & 32) == 0) {
         var5 = false;
      }

      if (var5) {
         throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA");
      } else {
         short var8 = var6;
         if ((var3 & 8) != 0) {
            short var9 = (short)(this.source.readByte() & 255);
            var8 = var9;
         }

         var2 = lengthWithoutPadding(var2, var3, var8);
         var1.data(var7, var4, this.source, var2);
         this.source.skip((long)var8);
      }
   }

   private void readGoAway(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 < 8) {
         throw Http2.ioException("TYPE_GOAWAY length < 8: %s", var2);
      } else if (var4 != 0) {
         throw Http2.ioException("TYPE_GOAWAY streamId != 0");
      } else {
         int var7 = this.source.readInt();
         var4 = this.source.readInt();
         var2 -= 8;
         ErrorCode var5 = ErrorCode.fromHttp2(var4);
         if (var5 == null) {
            throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", var4);
         } else {
            ByteString var6 = ByteString.EMPTY;
            if (var2 > 0) {
               var6 = this.source.readByteString((long)var2);
            }

            var1.goAway(var7, var5, var6);
         }
      }
   }

   private List readHeaderBlock(int var1, short var2, byte var3, int var4) throws IOException {
      Http2Reader.ContinuationSource var5 = this.continuation;
      this.continuation.left = var1;
      var5.length = var1;
      this.continuation.padding = (short)var2;
      this.continuation.flags = (byte)var3;
      this.continuation.streamId = var4;
      this.hpackReader.readHeaders();
      return this.hpackReader.getAndResetHeaderList();
   }

   private void readHeaders(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      byte var5 = 0;
      if (var4 == 0) {
         throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0");
      } else {
         boolean var6;
         if ((var3 & 1) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         short var7 = var5;
         if ((var3 & 8) != 0) {
            short var8 = (short)(this.source.readByte() & 255);
            var7 = var8;
         }

         int var9 = var2;
         if ((var3 & 32) != 0) {
            this.readPriority(var1, var4);
            var9 = var2 - 5;
         }

         var1.headers(var6, var4, -1, this.readHeaderBlock(lengthWithoutPadding(var9, var3, var7), var7, var3, var4));
      }
   }

   static int readMedium(BufferedSource var0) throws IOException {
      return (var0.readByte() & 255) << 16 | (var0.readByte() & 255) << 8 | var0.readByte() & 255;
   }

   private void readPing(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      boolean var5 = true;
      if (var2 != 8) {
         throw Http2.ioException("TYPE_PING length != 8: %s", var2);
      } else if (var4 != 0) {
         throw Http2.ioException("TYPE_PING streamId != 0");
      } else {
         var2 = this.source.readInt();
         var4 = this.source.readInt();
         if ((var3 & 1) == 0) {
            var5 = false;
         }

         var1.ping(var5, var2, var4);
      }
   }

   private void readPriority(Http2Reader.Handler var1, int var2) throws IOException {
      int var3 = this.source.readInt();
      boolean var4;
      if ((Integer.MIN_VALUE & var3) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var1.priority(var2, var3 & Integer.MAX_VALUE, (this.source.readByte() & 255) + 1, var4);
   }

   private void readPriority(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 != 5) {
         throw Http2.ioException("TYPE_PRIORITY length: %d != 5", var2);
      } else if (var4 == 0) {
         throw Http2.ioException("TYPE_PRIORITY streamId == 0");
      } else {
         this.readPriority(var1, var4);
      }
   }

   private void readPushPromise(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      byte var5 = 0;
      if (var4 == 0) {
         throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0");
      } else {
         short var6 = var5;
         if ((var3 & 8) != 0) {
            short var7 = (short)(this.source.readByte() & 255);
            var6 = var7;
         }

         var1.pushPromise(var4, this.source.readInt() & Integer.MAX_VALUE, this.readHeaderBlock(lengthWithoutPadding(var2 - 4, var3, var6), var6, var3, var4));
      }
   }

   private void readRstStream(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 != 4) {
         throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", var2);
      } else if (var4 == 0) {
         throw Http2.ioException("TYPE_RST_STREAM streamId == 0");
      } else {
         var2 = this.source.readInt();
         ErrorCode var5 = ErrorCode.fromHttp2(var2);
         if (var5 == null) {
            throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", var2);
         } else {
            var1.rstStream(var4, var5);
         }
      }
   }

   private void readSettings(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var4 != 0) {
         throw Http2.ioException("TYPE_SETTINGS streamId != 0");
      } else if ((var3 & 1) != 0) {
         if (var2 != 0) {
            throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!");
         } else {
            var1.ackSettings();
         }
      } else if (var2 % 6 != 0) {
         throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", var2);
      } else {
         Settings var5 = new Settings();

         for(var4 = 0; var4 < var2; var4 += 6) {
            short var6 = this.source.readShort();
            int var7 = this.source.readInt();
            short var8 = var6;
            switch(var6) {
            case 1:
            case 6:
               break;
            case 2:
               var8 = var6;
               if (var7 != 0) {
                  var8 = var6;
                  if (var7 != 1) {
                     throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1");
                  }
               }
               break;
            case 3:
               var8 = 4;
               break;
            case 4:
               var8 = 7;
               if (var7 < 0) {
                  throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1");
               }
               break;
            case 5:
               if (var7 < 16384) {
                  throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", var7);
               }

               var8 = var6;
               if (var7 > 16777215) {
                  throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", var7);
               }
               break;
            default:
               var8 = var6;
            }

            var5.set(var8, var7);
         }

         var1.settings(false, var5);
      }
   }

   private void readWindowUpdate(Http2Reader.Handler var1, int var2, byte var3, int var4) throws IOException {
      if (var2 != 4) {
         throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", var2);
      } else {
         long var5 = (long)this.source.readInt() & 2147483647L;
         if (var5 == 0L) {
            throw Http2.ioException("windowSizeIncrement was 0", var5);
         } else {
            var1.windowUpdate(var4, var5);
         }
      }
   }

   public void close() throws IOException {
      this.source.close();
   }

   public boolean nextFrame(boolean var1, Http2Reader.Handler var2) throws IOException {
      boolean var3 = true;

      try {
         this.source.require(9L);
      } catch (IOException var8) {
         var1 = false;
         return var1;
      }

      int var4 = readMedium(this.source);
      if (var4 < 0 || var4 > 16384) {
         throw Http2.ioException("FRAME_SIZE_ERROR: %s", var4);
      } else {
         byte var5 = (byte)(this.source.readByte() & 255);
         if (var1 && var5 != 4) {
            throw Http2.ioException("Expected a SETTINGS frame but was %s", var5);
         } else {
            byte var6 = (byte)(this.source.readByte() & 255);
            int var7 = this.source.readInt() & Integer.MAX_VALUE;
            if (logger.isLoggable(Level.FINE)) {
               logger.fine(Http2.frameLog(true, var7, var4, var5, var6));
            }

            switch(var5) {
            case 0:
               this.readData(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 1:
               this.readHeaders(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 2:
               this.readPriority(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 3:
               this.readRstStream(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 4:
               this.readSettings(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 5:
               this.readPushPromise(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 6:
               this.readPing(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 7:
               this.readGoAway(var2, var4, var6, var7);
               var1 = var3;
               break;
            case 8:
               this.readWindowUpdate(var2, var4, var6, var7);
               var1 = var3;
               break;
            default:
               this.source.skip((long)var4);
               var1 = var3;
            }

            return var1;
         }
      }
   }

   public void readConnectionPreface(Http2Reader.Handler var1) throws IOException {
      if (this.client) {
         if (!this.nextFrame(true, var1)) {
            throw Http2.ioException("Required SETTINGS preface not received");
         }
      } else {
         ByteString var2 = this.source.readByteString((long)Http2.CONNECTION_PREFACE.size());
         if (logger.isLoggable(Level.FINE)) {
            logger.fine(Util.format("<< CONNECTION %s", var2.hex()));
         }

         if (!Http2.CONNECTION_PREFACE.equals(var2)) {
            throw Http2.ioException("Expected a connection header but was %s", var2.utf8());
         }
      }

   }

   static final class ContinuationSource implements Source {
      byte flags;
      int left;
      int length;
      short padding;
      private final BufferedSource source;
      int streamId;

      public ContinuationSource(BufferedSource var1) {
         this.source = var1;
      }

      private void readContinuationHeader() throws IOException {
         int var1 = this.streamId;
         int var2 = Http2Reader.readMedium(this.source);
         this.left = var2;
         this.length = var2;
         byte var3 = (byte)(this.source.readByte() & 255);
         this.flags = (byte)((byte)(this.source.readByte() & 255));
         if (Http2Reader.logger.isLoggable(Level.FINE)) {
            Http2Reader.logger.fine(Http2.frameLog(true, this.streamId, this.length, var3, this.flags));
         }

         this.streamId = this.source.readInt() & Integer.MAX_VALUE;
         if (var3 != 9) {
            throw Http2.ioException("%s != TYPE_CONTINUATION", var3);
         } else if (this.streamId != var1) {
            throw Http2.ioException("TYPE_CONTINUATION streamId changed");
         }
      }

      public void close() throws IOException {
      }

      public long read(Buffer var1, long var2) throws IOException {
         while(true) {
            if (this.left == 0) {
               this.source.skip((long)this.padding);
               this.padding = (short)0;
               if ((this.flags & 4) == 0) {
                  this.readContinuationHeader();
                  continue;
               }

               var2 = -1L;
            } else {
               var2 = this.source.read(var1, Math.min(var2, (long)this.left));
               if (var2 == -1L) {
                  var2 = -1L;
               } else {
                  this.left = (int)((long)this.left - var2);
               }
            }

            return var2;
         }
      }

      public Timeout timeout() {
         return this.source.timeout();
      }
   }

   interface Handler {
      void ackSettings();

      void alternateService(int var1, String var2, ByteString var3, String var4, int var5, long var6);

      void data(boolean var1, int var2, BufferedSource var3, int var4) throws IOException;

      void goAway(int var1, ErrorCode var2, ByteString var3);

      void headers(boolean var1, int var2, int var3, List var4);

      void ping(boolean var1, int var2, int var3);

      void priority(int var1, int var2, int var3, boolean var4);

      void pushPromise(int var1, int var2, List var3) throws IOException;

      void rstStream(int var1, ErrorCode var2);

      void settings(boolean var1, Settings var2);

      void windowUpdate(int var1, long var2);
   }
}
