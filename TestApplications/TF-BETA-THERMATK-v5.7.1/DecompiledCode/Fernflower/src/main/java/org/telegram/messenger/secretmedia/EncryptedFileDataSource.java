package org.telegram.messenger.secretmedia;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.Utilities;

public final class EncryptedFileDataSource extends BaseDataSource {
   private long bytesRemaining;
   private RandomAccessFile file;
   private int fileOffset;
   private byte[] iv;
   private byte[] key;
   private boolean opened;
   private Uri uri;

   public EncryptedFileDataSource() {
      super(false);
      this.key = new byte[32];
      this.iv = new byte[16];
   }

   @Deprecated
   public EncryptedFileDataSource(TransferListener var1) {
      this();
      if (var1 != null) {
         this.addTransferListener(var1);
      }

   }

   public void close() throws EncryptedFileDataSource.EncryptedFileDataSourceException {
      this.uri = null;
      this.fileOffset = 0;

      try {
         if (this.file != null) {
            this.file.close();
         }
      } catch (IOException var5) {
         EncryptedFileDataSource.EncryptedFileDataSourceException var1 = new EncryptedFileDataSource.EncryptedFileDataSourceException(var5);
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

   public long open(DataSpec var1) throws EncryptedFileDataSource.EncryptedFileDataSourceException {
      IOException var10000;
      label42: {
         long var7;
         boolean var10001;
         label37: {
            try {
               this.uri = var1.uri;
               File var2 = new File(var1.uri.getPath());
               String var3 = var2.getName();
               File var5 = FileLoader.getInternalCacheDir();
               StringBuilder var6 = new StringBuilder();
               var6.append(var3);
               var6.append(".key");
               File var4 = new File(var5, var6.toString());
               RandomAccessFile var16 = new RandomAccessFile(var4, "r");
               var16.read(this.key);
               var16.read(this.iv);
               var16.close();
               RandomAccessFile var15 = new RandomAccessFile(var2, "r");
               this.file = var15;
               this.file.seek(var1.position);
               this.fileOffset = (int)var1.position;
               if (var1.length == -1L) {
                  var7 = this.file.length() - var1.position;
                  break label37;
               }
            } catch (IOException var12) {
               var10000 = var12;
               var10001 = false;
               break label42;
            }

            try {
               var7 = var1.length;
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break label42;
            }
         }

         try {
            this.bytesRemaining = var7;
            var7 = this.bytesRemaining;
         } catch (IOException var10) {
            var10000 = var10;
            var10001 = false;
            break label42;
         }

         if (var7 >= 0L) {
            this.opened = true;
            this.transferStarted(var1);
            return this.bytesRemaining;
         }

         try {
            EOFException var14 = new EOFException();
            throw var14;
         } catch (IOException var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      IOException var13 = var10000;
      throw new EncryptedFileDataSource.EncryptedFileDataSourceException(var13);
   }

   public int read(byte[] var1, int var2, int var3) throws EncryptedFileDataSource.EncryptedFileDataSourceException {
      if (var3 == 0) {
         return 0;
      } else {
         long var4 = this.bytesRemaining;
         if (var4 == 0L) {
            return -1;
         } else {
            try {
               var3 = this.file.read(var1, var2, (int)Math.min(var4, (long)var3));
               Utilities.aesCtrDecryptionByteArray(var1, this.key, this.iv, var2, var3, this.fileOffset);
               this.fileOffset += var3;
            } catch (IOException var6) {
               throw new EncryptedFileDataSource.EncryptedFileDataSourceException(var6);
            }

            if (var3 > 0) {
               this.bytesRemaining -= (long)var3;
               this.bytesTransferred(var3);
            }

            return var3;
         }
      }
   }

   public static class EncryptedFileDataSourceException extends IOException {
      public EncryptedFileDataSourceException(IOException var1) {
         super(var1);
      }
   }
}
