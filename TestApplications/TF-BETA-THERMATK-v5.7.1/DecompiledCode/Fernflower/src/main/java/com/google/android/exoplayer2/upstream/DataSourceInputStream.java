package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

public final class DataSourceInputStream extends InputStream {
   private boolean closed = false;
   private final DataSource dataSource;
   private final DataSpec dataSpec;
   private boolean opened = false;
   private final byte[] singleByteArray;
   private long totalBytesRead;

   public DataSourceInputStream(DataSource var1, DataSpec var2) {
      this.dataSource = var1;
      this.dataSpec = var2;
      this.singleByteArray = new byte[1];
   }

   private void checkOpened() throws IOException {
      if (!this.opened) {
         this.dataSource.open(this.dataSpec);
         this.opened = true;
      }

   }

   public void close() throws IOException {
      if (!this.closed) {
         this.dataSource.close();
         this.closed = true;
      }

   }

   public void open() throws IOException {
      this.checkOpened();
   }

   public int read() throws IOException {
      int var1 = this.read(this.singleByteArray);
      int var2 = -1;
      if (var1 != -1) {
         var2 = this.singleByteArray[0] & 255;
      }

      return var2;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      Assertions.checkState(this.closed ^ true);
      this.checkOpened();
      var2 = this.dataSource.read(var1, var2, var3);
      if (var2 == -1) {
         return -1;
      } else {
         this.totalBytesRead += (long)var2;
         return var2;
      }
   }
}
