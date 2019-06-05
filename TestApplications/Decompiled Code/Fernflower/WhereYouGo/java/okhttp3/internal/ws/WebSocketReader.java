package okhttp3.internal.ws;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

final class WebSocketReader {
   boolean closed;
   long frameBytesRead;
   final WebSocketReader.FrameCallback frameCallback;
   long frameLength;
   final boolean isClient;
   boolean isControlFrame;
   boolean isFinalFrame;
   boolean isMasked;
   final byte[] maskBuffer = new byte[8192];
   final byte[] maskKey = new byte[4];
   int opcode;
   final BufferedSource source;

   WebSocketReader(boolean var1, BufferedSource var2, WebSocketReader.FrameCallback var3) {
      if (var2 == null) {
         throw new NullPointerException("source == null");
      } else if (var3 == null) {
         throw new NullPointerException("frameCallback == null");
      } else {
         this.isClient = var1;
         this.source = var2;
         this.frameCallback = var3;
      }
   }

   private void readControlFrame() throws IOException {
      Buffer var1 = new Buffer();
      if (this.frameBytesRead < this.frameLength) {
         if (this.isClient) {
            this.source.readFully(var1, this.frameLength);
         } else {
            while(this.frameBytesRead < this.frameLength) {
               int var2 = (int)Math.min(this.frameLength - this.frameBytesRead, (long)this.maskBuffer.length);
               var2 = this.source.read(this.maskBuffer, 0, var2);
               if (var2 == -1) {
                  throw new EOFException();
               }

               WebSocketProtocol.toggleMask(this.maskBuffer, (long)var2, this.maskKey, this.frameBytesRead);
               var1.write(this.maskBuffer, 0, var2);
               this.frameBytesRead += (long)var2;
            }
         }
      }

      switch(this.opcode) {
      case 8:
         short var7 = 1005;
         String var3 = "";
         long var4 = var1.size();
         if (var4 == 1L) {
            throw new ProtocolException("Malformed close payload length of 1.");
         }

         if (var4 != 0L) {
            var7 = var1.readShort();
            var3 = var1.readUtf8();
            String var6 = WebSocketProtocol.closeCodeExceptionMessage(var7);
            if (var6 != null) {
               throw new ProtocolException(var6);
            }
         }

         this.frameCallback.onReadClose(var7, var3);
         this.closed = true;
         break;
      case 9:
         this.frameCallback.onReadPing(var1.readByteString());
         break;
      case 10:
         this.frameCallback.onReadPong(var1.readByteString());
         break;
      default:
         throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.opcode));
      }

   }

   private void readHeader() throws IOException {
      boolean var1 = true;
      if (this.closed) {
         throw new IOException("closed");
      } else {
         long var2 = this.source.timeout().timeoutNanos();
         this.source.timeout().clearTimeout();
         boolean var10 = false;

         byte var4;
         try {
            var10 = true;
            var4 = this.source.readByte();
            var10 = false;
         } finally {
            if (var10) {
               this.source.timeout().timeout(var2, TimeUnit.NANOSECONDS);
            }
         }

         int var5 = var4 & 255;
         this.source.timeout().timeout(var2, TimeUnit.NANOSECONDS);
         this.opcode = var5 & 15;
         boolean var6;
         if ((var5 & 128) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.isFinalFrame = var6;
         if ((var5 & 8) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.isControlFrame = var6;
         if (this.isControlFrame && !this.isFinalFrame) {
            throw new ProtocolException("Control frames must be final.");
         } else {
            boolean var12;
            if ((var5 & 64) != 0) {
               var12 = true;
            } else {
               var12 = false;
            }

            boolean var8;
            if ((var5 & 32) != 0) {
               var8 = true;
            } else {
               var8 = false;
            }

            boolean var13;
            if ((var5 & 16) != 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            if (!var12 && !var8 && !var13) {
               int var14 = this.source.readByte() & 255;
               if ((var14 & 128) != 0) {
                  var6 = var1;
               } else {
                  var6 = false;
               }

               this.isMasked = var6;
               if (this.isMasked == this.isClient) {
                  String var7;
                  if (this.isClient) {
                     var7 = "Server-sent frames must not be masked.";
                  } else {
                     var7 = "Client-sent frames must be masked.";
                  }

                  throw new ProtocolException(var7);
               } else {
                  this.frameLength = (long)(var14 & 127);
                  if (this.frameLength == 126L) {
                     this.frameLength = (long)this.source.readShort() & 65535L;
                  } else if (this.frameLength == 127L) {
                     this.frameLength = this.source.readLong();
                     if (this.frameLength < 0L) {
                        throw new ProtocolException("Frame length 0x" + Long.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
                     }
                  }

                  this.frameBytesRead = 0L;
                  if (this.isControlFrame && this.frameLength > 125L) {
                     throw new ProtocolException("Control frame must be less than 125B.");
                  } else {
                     if (this.isMasked) {
                        this.source.readFully(this.maskKey);
                     }

                  }
               }
            } else {
               throw new ProtocolException("Reserved flags are unsupported.");
            }
         }
      }
   }

   private void readMessage(Buffer var1) throws IOException {
      long var2;
      for(; !this.closed; this.frameBytesRead += var2) {
         if (this.frameBytesRead == this.frameLength) {
            if (this.isFinalFrame) {
               return;
            }

            this.readUntilNonControlFrame();
            if (this.opcode != 0) {
               throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(this.opcode));
            }

            if (this.isFinalFrame && this.frameLength == 0L) {
               return;
            }
         }

         var2 = this.frameLength - this.frameBytesRead;
         if (this.isMasked) {
            var2 = Math.min(var2, (long)this.maskBuffer.length);
            var2 = (long)this.source.read(this.maskBuffer, 0, (int)var2);
            if (var2 == -1L) {
               throw new EOFException();
            }

            WebSocketProtocol.toggleMask(this.maskBuffer, var2, this.maskKey, this.frameBytesRead);
            var1.write(this.maskBuffer, 0, (int)var2);
         } else {
            long var4 = this.source.read(var1, var2);
            var2 = var4;
            if (var4 == -1L) {
               throw new EOFException();
            }
         }
      }

      throw new IOException("closed");
   }

   private void readMessageFrame() throws IOException {
      int var1 = this.opcode;
      if (var1 != 1 && var1 != 2) {
         throw new ProtocolException("Unknown opcode: " + Integer.toHexString(var1));
      } else {
         Buffer var2 = new Buffer();
         this.readMessage(var2);
         if (var1 == 1) {
            this.frameCallback.onReadMessage(var2.readUtf8());
         } else {
            this.frameCallback.onReadMessage(var2.readByteString());
         }

      }
   }

   void processNextFrame() throws IOException {
      this.readHeader();
      if (this.isControlFrame) {
         this.readControlFrame();
      } else {
         this.readMessageFrame();
      }

   }

   void readUntilNonControlFrame() throws IOException {
      while(true) {
         if (!this.closed) {
            this.readHeader();
            if (this.isControlFrame) {
               this.readControlFrame();
               continue;
            }
         }

         return;
      }
   }

   public interface FrameCallback {
      void onReadClose(int var1, String var2);

      void onReadMessage(String var1) throws IOException;

      void onReadMessage(ByteString var1) throws IOException;

      void onReadPing(ByteString var1);

      void onReadPong(ByteString var1);
   }
}
