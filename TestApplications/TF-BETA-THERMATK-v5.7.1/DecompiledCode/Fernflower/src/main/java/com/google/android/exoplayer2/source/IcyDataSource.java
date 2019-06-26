package com.google.android.exoplayer2.source;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.Map;

final class IcyDataSource implements DataSource {
   private int bytesUntilMetadata;
   private final IcyDataSource.Listener listener;
   private final int metadataIntervalBytes;
   private final byte[] metadataLengthByteHolder;
   private final DataSource upstream;

   public IcyDataSource(DataSource var1, int var2, IcyDataSource.Listener var3) {
      boolean var4;
      if (var2 > 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkArgument(var4);
      this.upstream = var1;
      this.metadataIntervalBytes = var2;
      this.listener = var3;
      this.metadataLengthByteHolder = new byte[1];
      this.bytesUntilMetadata = var2;
   }

   private boolean readMetadata() throws IOException {
      if (this.upstream.read(this.metadataLengthByteHolder, 0, 1) == -1) {
         return false;
      } else {
         int var1 = (this.metadataLengthByteHolder[0] & 255) << 4;
         if (var1 == 0) {
            return true;
         } else {
            byte[] var2 = new byte[var1];
            int var3 = var1;
            int var4 = 0;

            while(true) {
               int var5 = var1;
               if (var3 <= 0) {
                  while(var5 > 0 && var2[var5 - 1] == 0) {
                     --var5;
                  }

                  if (var5 > 0) {
                     this.listener.onIcyMetadata(new ParsableByteArray(var2, var5));
                  }

                  return true;
               }

               var5 = this.upstream.read(var2, var4, var3);
               if (var5 == -1) {
                  return false;
               }

               var4 += var5;
               var3 -= var5;
            }
         }
      }
   }

   public void addTransferListener(TransferListener var1) {
      this.upstream.addTransferListener(var1);
   }

   public void close() throws IOException {
      throw new UnsupportedOperationException();
   }

   public Map getResponseHeaders() {
      return this.upstream.getResponseHeaders();
   }

   public Uri getUri() {
      return this.upstream.getUri();
   }

   public long open(DataSpec var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.bytesUntilMetadata == 0) {
         if (!this.readMetadata()) {
            return -1;
         }

         this.bytesUntilMetadata = this.metadataIntervalBytes;
      }

      var2 = this.upstream.read(var1, var2, Math.min(this.bytesUntilMetadata, var3));
      if (var2 != -1) {
         this.bytesUntilMetadata -= var2;
      }

      return var2;
   }

   public interface Listener {
      void onIcyMetadata(ParsableByteArray var1);
   }
}
