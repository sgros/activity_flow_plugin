package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;

public abstract class DataChunk extends Chunk {
   private byte[] data;
   private volatile boolean loadCanceled;

   public DataChunk(DataSource var1, DataSpec var2, int var3, Format var4, int var5, Object var6, byte[] var7) {
      super(var1, var2, var3, var4, var5, var6, -9223372036854775807L, -9223372036854775807L);
      this.data = var7;
   }

   private void maybeExpandData(int var1) {
      byte[] var2 = this.data;
      if (var2 == null) {
         this.data = new byte[16384];
      } else if (var2.length < var1 + 16384) {
         this.data = Arrays.copyOf(var2, var2.length + 16384);
      }

   }

   public final void cancelLoad() {
      this.loadCanceled = true;
   }

   protected abstract void consume(byte[] var1, int var2) throws IOException;

   public byte[] getDataHolder() {
      return this.data;
   }

   public final void load() throws IOException, InterruptedException {
      label169: {
         Throwable var10000;
         label171: {
            boolean var10001;
            try {
               super.dataSource.open(super.dataSpec);
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label171;
            }

            int var1 = 0;
            int var2 = 0;

            while(var1 != -1) {
               int var3;
               try {
                  if (this.loadCanceled) {
                     break;
                  }

                  this.maybeExpandData(var2);
                  var3 = super.dataSource.read(this.data, var2, 16384);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label171;
               }

               var1 = var3;
               if (var3 != -1) {
                  var2 += var3;
                  var1 = var3;
               }
            }

            label154:
            try {
               if (!this.loadCanceled) {
                  this.consume(this.data, var2);
               }
               break label169;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label154;
            }
         }

         Throwable var4 = var10000;
         Util.closeQuietly((DataSource)super.dataSource);
         throw var4;
      }

      Util.closeQuietly((DataSource)super.dataSource);
   }
}
