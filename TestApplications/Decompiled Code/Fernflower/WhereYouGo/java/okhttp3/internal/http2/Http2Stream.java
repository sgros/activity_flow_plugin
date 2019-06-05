package okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http2Stream {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   long bytesLeftInWriteWindow;
   final Http2Connection connection;
   ErrorCode errorCode = null;
   private boolean hasResponseHeaders;
   final int id;
   final Http2Stream.StreamTimeout readTimeout = new Http2Stream.StreamTimeout();
   private final List requestHeaders;
   private List responseHeaders;
   final Http2Stream.FramingSink sink;
   private final Http2Stream.FramingSource source;
   long unacknowledgedBytesRead = 0L;
   final Http2Stream.StreamTimeout writeTimeout = new Http2Stream.StreamTimeout();

   static {
      boolean var0;
      if (!Http2Stream.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
   }

   Http2Stream(int var1, Http2Connection var2, boolean var3, boolean var4, List var5) {
      if (var2 == null) {
         throw new NullPointerException("connection == null");
      } else if (var5 == null) {
         throw new NullPointerException("requestHeaders == null");
      } else {
         this.id = var1;
         this.connection = var2;
         this.bytesLeftInWriteWindow = (long)var2.peerSettings.getInitialWindowSize();
         this.source = new Http2Stream.FramingSource((long)var2.okHttpSettings.getInitialWindowSize());
         this.sink = new Http2Stream.FramingSink();
         this.source.finished = var4;
         this.sink.finished = var3;
         this.requestHeaders = var5;
      }
   }

   private boolean closeInternal(ErrorCode var1) {
      boolean var2 = false;
      if (!$assertionsDisabled && Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         synchronized(this){}

         label265: {
            Throwable var10000;
            boolean var10001;
            label266: {
               try {
                  if (this.errorCode != null) {
                     return var2;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label266;
               }

               try {
                  if (this.source.finished && this.sink.finished) {
                     return var2;
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label266;
               }

               label251:
               try {
                  this.errorCode = var1;
                  this.notifyAll();
                  break label265;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label251;
               }
            }

            while(true) {
               Throwable var23 = var10000;

               try {
                  throw var23;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }

         this.connection.removeStream(this.id);
         var2 = true;
         return var2;
      }
   }

   void addBytesToWriteWindow(long var1) {
      this.bytesLeftInWriteWindow += var1;
      if (var1 > 0L) {
         this.notifyAll();
      }

   }

   void cancelStreamIfNecessary() throws IOException {
      if (!$assertionsDisabled && Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         synchronized(this){}

         boolean var1;
         boolean var2;
         label236: {
            Throwable var10000;
            boolean var10001;
            label229: {
               label228: {
                  label227: {
                     try {
                        if (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed)) {
                           break label227;
                        }
                     } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label229;
                     }

                     var1 = false;
                     break label228;
                  }

                  var1 = true;
               }

               label215:
               try {
                  var2 = this.isOpen();
                  break label236;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label215;
               }
            }

            while(true) {
               Throwable var3 = var10000;

               try {
                  throw var3;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var1) {
            this.close(ErrorCode.CANCEL);
         } else if (!var2) {
            this.connection.removeStream(this.id);
         }

      }
   }

   void checkOutNotClosed() throws IOException {
      if (this.sink.closed) {
         throw new IOException("stream closed");
      } else if (this.sink.finished) {
         throw new IOException("stream finished");
      } else if (this.errorCode != null) {
         throw new StreamResetException(this.errorCode);
      }
   }

   public void close(ErrorCode var1) throws IOException {
      if (this.closeInternal(var1)) {
         this.connection.writeSynReset(this.id, var1);
      }

   }

   public void closeLater(ErrorCode var1) {
      if (this.closeInternal(var1)) {
         this.connection.writeSynResetLater(this.id, var1);
      }

   }

   public Http2Connection getConnection() {
      return this.connection;
   }

   public ErrorCode getErrorCode() {
      synchronized(this){}

      ErrorCode var1;
      try {
         var1 = this.errorCode;
      } finally {
         ;
      }

      return var1;
   }

   public int getId() {
      return this.id;
   }

   public List getRequestHeaders() {
      return this.requestHeaders;
   }

   public Sink getSink() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label139: {
         try {
            if (!this.hasResponseHeaders && !this.isLocallyInitiated()) {
               IllegalStateException var14 = new IllegalStateException("reply before requesting the sink");
               throw var14;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label139;
         }

         label136:
         try {
            return this.sink;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label136;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public Source getSource() {
      return this.source;
   }

   public boolean isLocallyInitiated() {
      boolean var1 = true;
      boolean var2;
      if ((this.id & 1) == 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.connection.client == var2) {
         var2 = var1;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isOpen() {
      boolean var1 = false;
      synchronized(this){}

      label240: {
         Throwable var10000;
         label245: {
            boolean var10001;
            ErrorCode var2;
            try {
               var2 = this.errorCode;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label245;
            }

            if (var2 != null) {
               return var1;
            }

            try {
               if (!this.source.finished && !this.source.closed) {
                  break label240;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label245;
            }

            try {
               if (!this.sink.finished && !this.sink.closed) {
                  break label240;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label245;
            }

            boolean var3;
            try {
               var3 = this.hasResponseHeaders;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label245;
            }

            if (var3) {
               return var1;
            }
            break label240;
         }

         Throwable var24 = var10000;
         throw var24;
      }

      var1 = true;
      return var1;
   }

   public Timeout readTimeout() {
      return this.readTimeout;
   }

   void receiveData(BufferedSource var1, int var2) throws IOException {
      if (!$assertionsDisabled && Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         this.source.receive(var1, (long)var2);
      }
   }

   void receiveFin() {
      // $FF: Couldn't be decompiled
   }

   void receiveHeaders(List var1) {
      if (!$assertionsDisabled && Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         boolean var2 = true;
         synchronized(this){}

         label259: {
            Throwable var10000;
            boolean var10001;
            label252: {
               label260: {
                  try {
                     this.hasResponseHeaders = true;
                     if (this.responseHeaders == null) {
                        this.responseHeaders = var1;
                        var2 = this.isOpen();
                        this.notifyAll();
                        break label260;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label252;
                  }

                  try {
                     ArrayList var3 = new ArrayList();
                     var3.addAll(this.responseHeaders);
                     var3.add((Object)null);
                     var3.addAll(var1);
                     this.responseHeaders = var3;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label252;
                  }
               }

               label242:
               try {
                  break label259;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label242;
               }
            }

            while(true) {
               Throwable var24 = var10000;

               try {
                  throw var24;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (!var2) {
            this.connection.removeStream(this.id);
         }

      }
   }

   void receiveRstStream(ErrorCode var1) {
      synchronized(this){}

      try {
         if (this.errorCode == null) {
            this.errorCode = var1;
            this.notifyAll();
         }
      } finally {
         ;
      }

   }

   public void sendResponseHeaders(List var1, boolean var2) throws IOException {
      if (!$assertionsDisabled && Thread.holdsLock(this)) {
         throw new AssertionError();
      } else if (var1 == null) {
         throw new NullPointerException("responseHeaders == null");
      } else {
         boolean var3 = false;
         synchronized(this){}

         label269: {
            Throwable var10000;
            boolean var10001;
            label270: {
               try {
                  this.hasResponseHeaders = true;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label270;
               }

               if (!var2) {
                  try {
                     this.sink.finished = true;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label270;
                  }

                  var3 = true;
               }

               label253:
               try {
                  break label269;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label253;
               }
            }

            while(true) {
               Throwable var24 = var10000;

               try {
                  throw var24;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }

         this.connection.writeSynReply(this.id, var3, var1);
         if (var3) {
            this.connection.flush();
         }

      }
   }

   public List takeResponseHeaders() throws IOException {
      // $FF: Couldn't be decompiled
   }

   void waitForIo() throws InterruptedIOException {
      try {
         this.wait();
      } catch (InterruptedException var2) {
         throw new InterruptedIOException();
      }
   }

   public Timeout writeTimeout() {
      return this.writeTimeout;
   }

   final class FramingSink implements Sink {
      // $FF: synthetic field
      static final boolean $assertionsDisabled;
      private static final long EMIT_BUFFER_SIZE = 16384L;
      boolean closed;
      boolean finished;
      private final Buffer sendBuffer = new Buffer();

      static {
         boolean var0;
         if (!Http2Stream.class.desiredAssertionStatus()) {
            var0 = true;
         } else {
            var0 = false;
         }

         $assertionsDisabled = var0;
      }

      private void emitFrame(boolean var1) throws IOException {
         Http2Stream var2 = Http2Stream.this;
         synchronized(var2){}

         long var4;
         Throwable var10000;
         boolean var10001;
         label772: {
            label767: {
               try {
                  Http2Stream.this.writeTimeout.enter();
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break label767;
               }

               while(true) {
                  boolean var63 = false;

                  try {
                     var63 = true;
                     if (Http2Stream.this.bytesLeftInWriteWindow <= 0L) {
                        if (!this.finished) {
                           if (!this.closed) {
                              if (Http2Stream.this.errorCode == null) {
                                 Http2Stream.this.waitForIo();
                                 var63 = false;
                                 continue;
                              }

                              var63 = false;
                           } else {
                              var63 = false;
                           }
                        } else {
                           var63 = false;
                        }
                     } else {
                        var63 = false;
                     }
                  } finally {
                     if (var63) {
                        try {
                           Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                        } catch (Throwable var76) {
                           var10000 = var76;
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  try {
                     Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                     Http2Stream.this.checkOutNotClosed();
                     var4 = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
                     Http2Stream var82 = Http2Stream.this;
                     var82.bytesLeftInWriteWindow -= var4;
                     break label772;
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break;
                  }
               }
            }

            while(true) {
               Throwable var3 = var10000;

               try {
                  throw var3;
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  continue;
               }
            }
         }

         Http2Stream.this.writeTimeout.enter();

         label746: {
            label774: {
               int var6;
               Http2Connection var80;
               try {
                  var80 = Http2Stream.this.connection;
                  var6 = Http2Stream.this.id;
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label774;
               }

               label741: {
                  label740: {
                     if (var1) {
                        try {
                           if (var4 == this.sendBuffer.size()) {
                              break label740;
                           }
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label774;
                        }
                     }

                     var1 = false;
                     break label741;
                  }

                  var1 = true;
               }

               label733:
               try {
                  var80.writeData(var6, var1, this.sendBuffer, var4);
                  break label746;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label733;
               }
            }

            Throwable var81 = var10000;
            Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
            throw var81;
         }

         Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
      }

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void flush() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public Timeout timeout() {
         return Http2Stream.this.writeTimeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         if (!$assertionsDisabled && Thread.holdsLock(Http2Stream.this)) {
            throw new AssertionError();
         } else {
            this.sendBuffer.write(var1, var2);

            while(this.sendBuffer.size() >= 16384L) {
               this.emitFrame(false);
            }

         }
      }
   }

   private final class FramingSource implements Source {
      // $FF: synthetic field
      static final boolean $assertionsDisabled;
      boolean closed;
      boolean finished;
      private final long maxByteCount;
      private final Buffer readBuffer = new Buffer();
      private final Buffer receiveBuffer = new Buffer();

      static {
         boolean var0;
         if (!Http2Stream.class.desiredAssertionStatus()) {
            var0 = true;
         } else {
            var0 = false;
         }

         $assertionsDisabled = var0;
      }

      FramingSource(long var2) {
         this.maxByteCount = var2;
      }

      private void checkNotClosed() throws IOException {
         if (this.closed) {
            throw new IOException("stream closed");
         } else if (Http2Stream.this.errorCode != null) {
            throw new StreamResetException(Http2Stream.this.errorCode);
         }
      }

      private void waitUntilReadable() throws IOException {
         Http2Stream.this.readTimeout.enter();

         while(true) {
            boolean var3 = false;

            try {
               var3 = true;
               if (this.readBuffer.size() == 0L) {
                  if (!this.finished) {
                     if (!this.closed) {
                        if (Http2Stream.this.errorCode == null) {
                           Http2Stream.this.waitForIo();
                           var3 = false;
                           continue;
                        }

                        var3 = false;
                        break;
                     }

                     var3 = false;
                     break;
                  }

                  var3 = false;
                  break;
               }

               var3 = false;
               break;
            } finally {
               if (var3) {
                  Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
               }
            }
         }

         Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
      }

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + var2);
         } else {
            Http2Stream var4 = Http2Stream.this;
            synchronized(var4){}

            Throwable var10000;
            boolean var10001;
            label729: {
               label730: {
                  try {
                     this.waitUntilReadable();
                     this.checkNotClosed();
                     if (this.readBuffer.size() == 0L) {
                        break label730;
                     }
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label729;
                  }

                  try {
                     var2 = this.readBuffer.read(var1, Math.min(var2, this.readBuffer.size()));
                     Http2Stream var77 = Http2Stream.this;
                     var77.unacknowledgedBytesRead += var2;
                     if (Http2Stream.this.unacknowledgedBytesRead >= (long)(Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
                        Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.id, Http2Stream.this.unacknowledgedBytesRead);
                        Http2Stream.this.unacknowledgedBytesRead = 0L;
                     }
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label729;
                  }

                  try {
                     ;
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     break label729;
                  }

                  Http2Connection var78 = Http2Stream.this.connection;
                  synchronized(var78){}

                  label701: {
                     try {
                        Http2Connection var80 = Http2Stream.this.connection;
                        var80.unacknowledgedBytesRead += var2;
                        if (Http2Stream.this.connection.unacknowledgedBytesRead >= (long)(Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
                           Http2Stream.this.connection.writeWindowUpdateLater(0, Http2Stream.this.connection.unacknowledgedBytesRead);
                           Http2Stream.this.connection.unacknowledgedBytesRead = 0L;
                        }
                     } catch (Throwable var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label701;
                     }

                     label698:
                     try {
                        return var2;
                     } catch (Throwable var71) {
                        var10000 = var71;
                        var10001 = false;
                        break label698;
                     }
                  }

                  while(true) {
                     Throwable var81 = var10000;

                     try {
                        throw var81;
                     } catch (Throwable var69) {
                        var10000 = var69;
                        var10001 = false;
                        continue;
                     }
                  }
               }

               var2 = -1L;

               label708:
               try {
                  return var2;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label708;
               }
            }

            while(true) {
               Throwable var79 = var10000;

               try {
                  throw var79;
               } catch (Throwable var70) {
                  var10000 = var70;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      void receive(BufferedSource var1, long var2) throws IOException {
         long var4 = var2;
         if (!$assertionsDisabled) {
            var4 = var2;
            if (Thread.holdsLock(Http2Stream.this)) {
               throw new AssertionError();
            }
         }

         while(var4 > 0L) {
            Http2Stream var6 = Http2Stream.this;
            synchronized(var6){}

            boolean var7;
            boolean var8;
            Throwable var10000;
            boolean var10001;
            Throwable var81;
            label952: {
               label942: {
                  label941: {
                     label940: {
                        try {
                           var8 = this.finished;
                           if (this.readBuffer.size() + var4 <= this.maxByteCount) {
                              break label940;
                           }
                        } catch (Throwable var80) {
                           var10000 = var80;
                           var10001 = false;
                           break label942;
                        }

                        var7 = true;
                        break label941;
                     }

                     var7 = false;
                  }

                  label934:
                  try {
                     break label952;
                  } catch (Throwable var79) {
                     var10000 = var79;
                     var10001 = false;
                     break label934;
                  }
               }

               while(true) {
                  var81 = var10000;

                  try {
                     throw var81;
                  } catch (Throwable var73) {
                     var10000 = var73;
                     var10001 = false;
                     continue;
                  }
               }
            }

            if (var7) {
               var1.skip(var4);
               Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
            } else if (!var8) {
               var2 = var1.read(this.receiveBuffer, var4);
               if (var2 == -1L) {
                  throw new EOFException();
               }

               var4 -= var2;
               var6 = Http2Stream.this;
               synchronized(var6){}

               label954: {
                  label920: {
                     label919: {
                        try {
                           if (this.readBuffer.size() == 0L) {
                              break label919;
                           }
                        } catch (Throwable var78) {
                           var10000 = var78;
                           var10001 = false;
                           break label954;
                        }

                        var7 = false;
                        break label920;
                     }

                     var7 = true;
                  }

                  try {
                     this.readBuffer.writeAll(this.receiveBuffer);
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break label954;
                  }

                  if (var7) {
                     try {
                        Http2Stream.this.notifyAll();
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        break label954;
                     }
                  }

                  label906:
                  try {
                     continue;
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label906;
                  }
               }

               while(true) {
                  var81 = var10000;

                  try {
                     throw var81;
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     continue;
                  }
               }
            } else {
               var1.skip(var4);
            }
            break;
         }

      }

      public Timeout timeout() {
         return Http2Stream.this.readTimeout;
      }
   }

   class StreamTimeout extends AsyncTimeout {
      public void exitAndThrowIfTimedOut() throws IOException {
         if (this.exit()) {
            throw this.newTimeoutException((IOException)null);
         }
      }

      protected IOException newTimeoutException(IOException var1) {
         SocketTimeoutException var2 = new SocketTimeoutException("timeout");
         if (var1 != null) {
            var2.initCause(var1);
         }

         return var2;
      }

      protected void timedOut() {
         Http2Stream.this.closeLater(ErrorCode.CANCEL);
      }
   }
}
