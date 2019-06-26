package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class Okio {
   static final Logger logger = Logger.getLogger(Okio.class.getName());

   private Okio() {
   }

   public static Sink appendingSink(File var0) throws FileNotFoundException {
      if (var0 == null) {
         throw new IllegalArgumentException("file == null");
      } else {
         return sink((OutputStream)(new FileOutputStream(var0, true)));
      }
   }

   public static Sink blackhole() {
      return new Sink() {
         public void close() throws IOException {
         }

         public void flush() throws IOException {
         }

         public Timeout timeout() {
            return Timeout.NONE;
         }

         public void write(Buffer var1, long var2) throws IOException {
            var1.skip(var2);
         }
      };
   }

   public static BufferedSink buffer(Sink var0) {
      return new RealBufferedSink(var0);
   }

   public static BufferedSource buffer(Source var0) {
      return new RealBufferedSource(var0);
   }

   static boolean isAndroidGetsocknameError(AssertionError var0) {
      boolean var1;
      if (var0.getCause() != null && var0.getMessage() != null && var0.getMessage().contains("getsockname failed")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static Sink sink(File var0) throws FileNotFoundException {
      if (var0 == null) {
         throw new IllegalArgumentException("file == null");
      } else {
         return sink((OutputStream)(new FileOutputStream(var0)));
      }
   }

   public static Sink sink(OutputStream var0) {
      return sink(var0, new Timeout());
   }

   private static Sink sink(final OutputStream var0, final Timeout var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("out == null");
      } else if (var1 == null) {
         throw new IllegalArgumentException("timeout == null");
      } else {
         return new Sink() {
            public void close() throws IOException {
               var0.close();
            }

            public void flush() throws IOException {
               var0.flush();
            }

            public Timeout timeout() {
               return var1;
            }

            public String toString() {
               return "sink(" + var0 + ")";
            }

            public void write(Buffer var1x, long var2) throws IOException {
               Util.checkOffsetAndCount(var1x.size, 0L, var2);

               while(var2 > 0L) {
                  var1.throwIfReached();
                  Segment var4 = var1x.head;
                  int var5 = (int)Math.min(var2, (long)(var4.limit - var4.pos));
                  var0.write(var4.data, var4.pos, var5);
                  var4.pos += var5;
                  long var6 = var2 - (long)var5;
                  var1x.size -= (long)var5;
                  var2 = var6;
                  if (var4.pos == var4.limit) {
                     var1x.head = var4.pop();
                     SegmentPool.recycle(var4);
                     var2 = var6;
                  }
               }

            }
         };
      }
   }

   public static Sink sink(Socket var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("socket == null");
      } else {
         AsyncTimeout var1 = timeout(var0);
         return var1.sink(sink((OutputStream)var0.getOutputStream(), (Timeout)var1));
      }
   }

   @IgnoreJRERequirement
   public static Sink sink(Path var0, OpenOption... var1) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("path == null");
      } else {
         return sink(Files.newOutputStream(var0, var1));
      }
   }

   public static Source source(File var0) throws FileNotFoundException {
      if (var0 == null) {
         throw new IllegalArgumentException("file == null");
      } else {
         return source((InputStream)(new FileInputStream(var0)));
      }
   }

   public static Source source(InputStream var0) {
      return source(var0, new Timeout());
   }

   private static Source source(final InputStream var0, final Timeout var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("in == null");
      } else if (var1 == null) {
         throw new IllegalArgumentException("timeout == null");
      } else {
         return new Source() {
            public void close() throws IOException {
               var0.close();
            }

            public long read(Buffer var1x, long var2) throws IOException {
               long var4 = 0L;
               if (var2 < 0L) {
                  throw new IllegalArgumentException("byteCount < 0: " + var2);
               } else if (var2 == 0L) {
                  var2 = var4;
                  return var2;
               } else {
                  AssertionError var10000;
                  label52: {
                     boolean var10001;
                     Segment var6;
                     int var7;
                     try {
                        var1.throwIfReached();
                        var6 = var1x.writableSegment(1);
                        var7 = (int)Math.min(var2, (long)(8192 - var6.limit));
                        var7 = var0.read(var6.data, var6.limit, var7);
                     } catch (AssertionError var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label52;
                     }

                     if (var7 == -1) {
                        var2 = -1L;
                        return var2;
                     }

                     try {
                        var6.limit += var7;
                        var1x.size += (long)var7;
                     } catch (AssertionError var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label52;
                     }

                     var2 = (long)var7;
                     return var2;
                  }

                  AssertionError var10 = var10000;
                  if (Okio.isAndroidGetsocknameError(var10)) {
                     throw new IOException(var10);
                  } else {
                     throw var10;
                  }
               }
            }

            public Timeout timeout() {
               return var1;
            }

            public String toString() {
               return "source(" + var0 + ")";
            }
         };
      }
   }

   public static Source source(Socket var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("socket == null");
      } else {
         AsyncTimeout var1 = timeout(var0);
         return var1.source(source((InputStream)var0.getInputStream(), (Timeout)var1));
      }
   }

   @IgnoreJRERequirement
   public static Source source(Path var0, OpenOption... var1) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("path == null");
      } else {
         return source(Files.newInputStream(var0, var1));
      }
   }

   private static AsyncTimeout timeout(final Socket var0) {
      return new AsyncTimeout() {
         protected IOException newTimeoutException(IOException var1) {
            SocketTimeoutException var2 = new SocketTimeoutException("timeout");
            if (var1 != null) {
               var2.initCause(var1);
            }

            return var2;
         }

         protected void timedOut() {
            try {
               var0.close();
            } catch (Exception var2) {
               Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + var0, var2);
            } catch (AssertionError var3) {
               if (!Okio.isAndroidGetsocknameError(var3)) {
                  throw var3;
               }

               Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + var0, var3);
            }

         }
      };
   }
}
