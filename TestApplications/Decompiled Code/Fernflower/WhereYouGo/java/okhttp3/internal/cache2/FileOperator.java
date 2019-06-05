package okhttp3.internal.cache2;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import okio.Buffer;

final class FileOperator {
   private static final int BUFFER_SIZE = 8192;
   private final byte[] byteArray = new byte[8192];
   private final ByteBuffer byteBuffer;
   private final FileChannel fileChannel;

   public FileOperator(FileChannel var1) {
      this.byteBuffer = ByteBuffer.wrap(this.byteArray);
      this.fileChannel = var1;
   }

   public void read(long var1, Buffer var3, long var4) throws IOException {
      long var6 = var1;
      var1 = var4;
      if (var4 < 0L) {
         throw new IndexOutOfBoundsException();
      } else {
         while(var1 > 0L) {
            boolean var10 = false;

            int var8;
            try {
               var10 = true;
               this.byteBuffer.limit((int)Math.min(8192L, var1));
               if (this.fileChannel.read(this.byteBuffer, var6) == -1) {
                  EOFException var12 = new EOFException();
                  throw var12;
               }

               var8 = this.byteBuffer.position();
               var3.write(this.byteArray, 0, var8);
               var10 = false;
            } finally {
               if (var10) {
                  this.byteBuffer.clear();
               }
            }

            var6 += (long)var8;
            var1 -= (long)var8;
            this.byteBuffer.clear();
         }

      }
   }

   public void write(long var1, Buffer var3, long var4) throws IOException {
      if (var4 >= 0L) {
         long var6 = var4;
         if (var4 <= var3.size()) {
            label108:
            while(var6 > 0L) {
               Throwable var10000;
               label114: {
                  boolean var10001;
                  int var8;
                  try {
                     var8 = (int)Math.min(8192L, var6);
                     var3.read(this.byteArray, 0, var8);
                     this.byteBuffer.limit(var8);
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label114;
                  }

                  var4 = var1;

                  while(true) {
                     boolean var9;
                     try {
                        var1 = var4 + (long)this.fileChannel.write(this.byteBuffer, var4);
                        var9 = this.byteBuffer.hasRemaining();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }

                     var4 = var1;
                     if (!var9) {
                        var6 -= (long)var8;
                        this.byteBuffer.clear();
                        continue label108;
                     }
                  }
               }

               Throwable var16 = var10000;
               this.byteBuffer.clear();
               throw var16;
            }

            return;
         }
      }

      throw new IndexOutOfBoundsException();
   }
}
