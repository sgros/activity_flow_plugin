package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class FileDataSource extends BaseDataSource {
   private long bytesRemaining;
   private RandomAccessFile file;
   private boolean opened;
   private Uri uri;

   public FileDataSource() {
      super(false);
   }

   public void close() throws FileDataSource.FileDataSourceException {
      this.uri = null;

      try {
         if (this.file != null) {
            this.file.close();
         }
      } catch (IOException var5) {
         FileDataSource.FileDataSourceException var1 = new FileDataSource.FileDataSourceException(var5);
         throw var1;
      } finally {
         this.file = null;
         if (this.opened) {
            this.opened = false;
            this.transferEnded();
         }

      }

   }

   public Uri getUri() {
      return this.uri;
   }

   public long open(DataSpec var1) throws FileDataSource.FileDataSourceException {
      IOException var10000;
      label42: {
         boolean var10001;
         long var3;
         label37: {
            try {
               this.uri = var1.uri;
               this.transferInitializing(var1);
               RandomAccessFile var2 = new RandomAccessFile(var1.uri.getPath(), "r");
               this.file = var2;
               this.file.seek(var1.position);
               if (var1.length == -1L) {
                  var3 = this.file.length() - var1.position;
                  break label37;
               }
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label42;
            }

            try {
               var3 = var1.length;
            } catch (IOException var7) {
               var10000 = var7;
               var10001 = false;
               break label42;
            }
         }

         try {
            this.bytesRemaining = var3;
            var3 = this.bytesRemaining;
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label42;
         }

         if (var3 >= 0L) {
            this.opened = true;
            this.transferStarted(var1);
            return this.bytesRemaining;
         }

         try {
            EOFException var10 = new EOFException();
            throw var10;
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IOException var9 = var10000;
      throw new FileDataSource.FileDataSourceException(var9);
   }

   public int read(byte[] var1, int var2, int var3) throws FileDataSource.FileDataSourceException {
      if (var3 == 0) {
         return 0;
      } else {
         long var4 = this.bytesRemaining;
         if (var4 == 0L) {
            return -1;
         } else {
            try {
               var2 = this.file.read(var1, var2, (int)Math.min(var4, (long)var3));
            } catch (IOException var6) {
               throw new FileDataSource.FileDataSourceException(var6);
            }

            if (var2 > 0) {
               this.bytesRemaining -= (long)var2;
               this.bytesTransferred(var2);
            }

            return var2;
         }
      }
   }

   public static class FileDataSourceException extends IOException {
      public FileDataSourceException(IOException var1) {
         super(var1);
      }
   }
}
