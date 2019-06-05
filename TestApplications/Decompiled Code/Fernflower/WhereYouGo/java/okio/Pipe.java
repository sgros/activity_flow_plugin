package okio;

import java.io.IOException;

public final class Pipe {
   final Buffer buffer = new Buffer();
   final long maxBufferSize;
   private final Sink sink = new Pipe.PipeSink();
   boolean sinkClosed;
   private final Source source = new Pipe.PipeSource();
   boolean sourceClosed;

   public Pipe(long var1) {
      if (var1 < 1L) {
         throw new IllegalArgumentException("maxBufferSize < 1: " + var1);
      } else {
         this.maxBufferSize = var1;
      }
   }

   public Sink sink() {
      return this.sink;
   }

   public Source source() {
      return this.source;
   }

   final class PipeSink implements Sink {
      final Timeout timeout = new Timeout();

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void flush() throws IOException {
         Buffer var1 = Pipe.this.buffer;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label294: {
            try {
               if (Pipe.this.sinkClosed) {
                  IllegalStateException var34 = new IllegalStateException("closed");
                  throw var34;
               }
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label294;
            }

            while(true) {
               try {
                  if (Pipe.this.buffer.size() <= 0L) {
                     break;
                  }

                  if (Pipe.this.sourceClosed) {
                     IOException var2 = new IOException("source is closed");
                     throw var2;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label294;
               }

               try {
                  this.timeout.waitUntilNotified(Pipe.this.buffer);
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label294;
               }
            }

            label275:
            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label275;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         Buffer var4 = Pipe.this.buffer;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label564: {
            try {
               if (Pipe.this.sinkClosed) {
                  IllegalStateException var81 = new IllegalStateException("closed");
                  throw var81;
               }
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label564;
            }

            while(true) {
               if (var2 <= 0L) {
                  try {
                     return;
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  if (Pipe.this.sourceClosed) {
                     IOException var79 = new IOException("source is closed");
                     throw var79;
                  }
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break;
               }

               long var5;
               try {
                  var5 = Pipe.this.maxBufferSize - Pipe.this.buffer.size();
               } catch (Throwable var77) {
                  var10000 = var77;
                  var10001 = false;
                  break;
               }

               if (var5 == 0L) {
                  try {
                     this.timeout.waitUntilNotified(Pipe.this.buffer);
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     var5 = Math.min(var5, var2);
                     Pipe.this.buffer.write(var1, var5);
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break;
                  }

                  var2 -= var5;

                  try {
                     Pipe.this.buffer.notifyAll();
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     break;
                  }
               }
            }
         }

         while(true) {
            Throwable var80 = var10000;

            try {
               throw var80;
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               continue;
            }
         }
      }
   }

   final class PipeSource implements Source {
      final Timeout timeout = new Timeout();

      public void close() throws IOException {
         // $FF: Couldn't be decompiled
      }

      public long read(Buffer var1, long var2) throws IOException {
         Buffer var4 = Pipe.this.buffer;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label461: {
            try {
               if (Pipe.this.sourceClosed) {
                  IllegalStateException var62 = new IllegalStateException("closed");
                  throw var62;
               }
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label461;
            }

            while(true) {
               label453: {
                  label462: {
                     try {
                        if (Pipe.this.buffer.size() != 0L) {
                           break label462;
                        }

                        if (!Pipe.this.sinkClosed) {
                           break label453;
                        }
                     } catch (Throwable var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label461;
                     }

                     var2 = -1L;

                     try {
                        break;
                     } catch (Throwable var57) {
                        var10000 = var57;
                        var10001 = false;
                        break label461;
                     }
                  }

                  try {
                     var2 = Pipe.this.buffer.read(var1, var2);
                     Pipe.this.buffer.notifyAll();
                     break;
                  } catch (Throwable var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label461;
                  }
               }

               try {
                  this.timeout.waitUntilNotified(Pipe.this.buffer);
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label461;
               }
            }

            label437:
            try {
               return var2;
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label437;
            }
         }

         while(true) {
            Throwable var61 = var10000;

            try {
               throw var61;
            } catch (Throwable var54) {
               var10000 = var54;
               var10001 = false;
               continue;
            }
         }
      }

      public Timeout timeout() {
         return this.timeout;
      }
   }
}
