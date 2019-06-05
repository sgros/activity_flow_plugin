package okhttp3.internal.cache2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Relay {
   private static final long FILE_HEADER_SIZE = 32L;
   static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
   static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
   private static final int SOURCE_FILE = 2;
   private static final int SOURCE_UPSTREAM = 1;
   final Buffer buffer = new Buffer();
   final long bufferMaxSize;
   boolean complete;
   RandomAccessFile file;
   private final ByteString metadata;
   int sourceCount;
   Source upstream;
   final Buffer upstreamBuffer = new Buffer();
   long upstreamPos;
   Thread upstreamReader;

   private Relay(RandomAccessFile var1, Source var2, long var3, ByteString var5, long var6) {
      this.file = var1;
      this.upstream = var2;
      boolean var8;
      if (var2 == null) {
         var8 = true;
      } else {
         var8 = false;
      }

      this.complete = var8;
      this.upstreamPos = var3;
      this.metadata = var5;
      this.bufferMaxSize = var6;
   }

   public static Relay edit(File var0, Source var1, ByteString var2, long var3) throws IOException {
      RandomAccessFile var5 = new RandomAccessFile(var0, "rw");
      Relay var6 = new Relay(var5, var1, 0L, var2, var3);
      var5.setLength(0L);
      var6.writeHeader(PREFIX_DIRTY, -1L, -1L);
      return var6;
   }

   public static Relay read(File var0) throws IOException {
      RandomAccessFile var7 = new RandomAccessFile(var0, "rw");
      FileOperator var1 = new FileOperator(var7.getChannel());
      Buffer var2 = new Buffer();
      var1.read(0L, var2, 32L);
      if (!var2.readByteString((long)PREFIX_CLEAN.size()).equals(PREFIX_CLEAN)) {
         throw new IOException("unreadable cache file");
      } else {
         long var3 = var2.readLong();
         long var5 = var2.readLong();
         var2 = new Buffer();
         var1.read(32L + var3, var2, var5);
         return new Relay(var7, (Source)null, var3, var2.readByteString(), 0L);
      }
   }

   private void writeHeader(ByteString var1, long var2, long var4) throws IOException {
      Buffer var6 = new Buffer();
      var6.write(var1);
      var6.writeLong(var2);
      var6.writeLong(var4);
      if (var6.size() != 32L) {
         throw new IllegalArgumentException();
      } else {
         (new FileOperator(this.file.getChannel())).write(0L, var6, 32L);
      }
   }

   private void writeMetadata(long var1) throws IOException {
      Buffer var3 = new Buffer();
      var3.write(this.metadata);
      (new FileOperator(this.file.getChannel())).write(32L + var1, var3, (long)this.metadata.size());
   }

   void commit(long param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   boolean isClosed() {
      boolean var1;
      if (this.file == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public ByteString metadata() {
      return this.metadata;
   }

   public Source newSource() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label214: {
         Relay.RelaySource var1;
         label225: {
            try {
               if (this.file == null) {
                  break label225;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label214;
            }

            try {
               ++this.sourceCount;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label214;
            }

            var1 = new Relay.RelaySource();
            return var1;
         }

         var1 = null;

         label198:
         try {
            return var1;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label198;
         }
      }

      while(true) {
         Throwable var22 = var10000;

         try {
            throw var22;
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            continue;
         }
      }
   }

   class RelaySource implements Source {
      private FileOperator fileOperator;
      private long sourcePos;
      private final Timeout timeout = new Timeout();

      RelaySource() {
         this.fileOperator = new FileOperator(Relay.this.file.getChannel());
      }

      public void close() throws IOException {
         if (this.fileOperator != null) {
            this.fileOperator = null;
            RandomAccessFile var1 = null;
            Relay var2 = Relay.this;
            synchronized(var2){}

            label244: {
               Throwable var10000;
               boolean var10001;
               label245: {
                  try {
                     Relay var3 = Relay.this;
                     --var3.sourceCount;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label245;
                  }

                  try {
                     if (Relay.this.sourceCount == 0) {
                        var1 = Relay.this.file;
                        Relay.this.file = null;
                     }
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label245;
                  }

                  label231:
                  try {
                     break label244;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label231;
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

            if (var1 != null) {
               Util.closeQuietly((Closeable)var1);
            }
         }

      }

      public long read(Buffer param1, long param2) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public Timeout timeout() {
         return this.timeout;
      }
   }
}
