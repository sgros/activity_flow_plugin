package org.telegram.messenger.secretmedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.telegram.messenger.SecureDocumentKey;
import org.telegram.messenger.Utilities;

public class EncryptedFileInputStream extends FileInputStream {
   private static final int MODE_CBC = 1;
   private static final int MODE_CTR = 0;
   private int currentMode;
   private int fileOffset;
   private byte[] iv = new byte[16];
   private byte[] key = new byte[32];

   public EncryptedFileInputStream(File var1, File var2) throws Exception {
      super(var1);
      this.currentMode = 0;
      RandomAccessFile var3 = new RandomAccessFile(var2, "r");
      var3.read(this.key, 0, 32);
      var3.read(this.iv, 0, 16);
      var3.close();
   }

   public EncryptedFileInputStream(File var1, SecureDocumentKey var2) throws Exception {
      super(var1);
      this.currentMode = 1;
      byte[] var4 = var2.file_key;
      byte[] var3 = this.key;
      System.arraycopy(var4, 0, var3, 0, var3.length);
      byte[] var5 = var2.file_iv;
      var4 = this.iv;
      System.arraycopy(var5, 0, var4, 0, var4.length);
   }

   public static void decryptBytesWithKeyFile(byte[] var0, int var1, int var2, File var3) throws Exception {
      byte[] var4 = new byte[32];
      byte[] var5 = new byte[16];
      RandomAccessFile var6 = new RandomAccessFile(var3, "r");
      var6.read(var4, 0, 32);
      var6.read(var5, 0, 16);
      var6.close();
      Utilities.aesCtrDecryptionByteArray(var0, var4, var5, var1, var2, 0);
   }

   public static void decryptBytesWithKeyFile(byte[] var0, int var1, int var2, SecureDocumentKey var3) {
      Utilities.aesCbcEncryptionByteArraySafe(var0, var3.file_key, var3.file_iv, var1, var2, 0, 0);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.currentMode == 1 && this.fileOffset == 0) {
         byte[] var4 = new byte[32];
         super.read(var4, 0, 32);
         Utilities.aesCbcEncryptionByteArraySafe(var1, this.key, this.iv, var2, var3, this.fileOffset, 0);
         this.fileOffset += 32;
         this.skip((long)((var4[0] & 255) - 32));
      }

      int var5 = super.read(var1, var2, var3);
      int var6 = this.currentMode;
      if (var6 == 1) {
         Utilities.aesCbcEncryptionByteArraySafe(var1, this.key, this.iv, var2, var3, this.fileOffset, 0);
      } else if (var6 == 0) {
         Utilities.aesCtrDecryptionByteArray(var1, this.key, this.iv, var2, var3, this.fileOffset);
      }

      this.fileOffset += var3;
      return var5;
   }

   public long skip(long var1) throws IOException {
      this.fileOffset = (int)((long)this.fileOffset + var1);
      return super.skip(var1);
   }
}
