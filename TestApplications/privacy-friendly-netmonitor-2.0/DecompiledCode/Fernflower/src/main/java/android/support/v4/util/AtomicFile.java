package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
   private final File mBackupName;
   private final File mBaseName;

   public AtomicFile(File var1) {
      this.mBaseName = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(var1.getPath());
      var2.append(".bak");
      this.mBackupName = new File(var2.toString());
   }

   static boolean sync(FileOutputStream var0) {
      if (var0 != null) {
         try {
            var0.getFD().sync();
         } catch (IOException var1) {
            return false;
         }
      }

      return true;
   }

   public void delete() {
      this.mBaseName.delete();
      this.mBackupName.delete();
   }

   public void failWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
         } catch (IOException var2) {
            Log.w("AtomicFile", "failWrite: Got exception:", var2);
         }
      }

   }

   public void finishWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBackupName.delete();
         } catch (IOException var2) {
            Log.w("AtomicFile", "finishWrite: Got exception:", var2);
         }
      }

   }

   public File getBaseFile() {
      return this.mBaseName;
   }

   public FileInputStream openRead() throws FileNotFoundException {
      if (this.mBackupName.exists()) {
         this.mBaseName.delete();
         this.mBackupName.renameTo(this.mBaseName);
      }

      return new FileInputStream(this.mBaseName);
   }

   public byte[] readFully() throws IOException {
      FileInputStream var1 = this.openRead();

      Throwable var10000;
      label229: {
         byte[] var2;
         boolean var10001;
         try {
            var2 = new byte[var1.available()];
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label229;
         }

         int var3 = 0;

         while(true) {
            int var4;
            try {
               var4 = var1.read(var2, var3, var2.length - var3);
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            if (var4 <= 0) {
               var1.close();
               return var2;
            }

            var4 += var3;

            int var5;
            try {
               var5 = var1.available();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            var3 = var4;

            byte[] var6;
            try {
               if (var5 <= var2.length - var4) {
                  continue;
               }

               var6 = new byte[var5 + var4];
               System.arraycopy(var2, 0, var6, 0, var4);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            var2 = var6;
            var3 = var4;
         }
      }

      Throwable var27 = var10000;
      var1.close();
      throw var27;
   }

   public FileOutputStream startWrite() throws IOException {
      StringBuilder var1;
      if (this.mBaseName.exists()) {
         if (!this.mBackupName.exists()) {
            if (!this.mBaseName.renameTo(this.mBackupName)) {
               var1 = new StringBuilder();
               var1.append("Couldn't rename file ");
               var1.append(this.mBaseName);
               var1.append(" to backup file ");
               var1.append(this.mBackupName);
               Log.w("AtomicFile", var1.toString());
            }
         } else {
            this.mBaseName.delete();
         }
      }

      FileOutputStream var4;
      try {
         var4 = new FileOutputStream(this.mBaseName);
      } catch (FileNotFoundException var3) {
         if (!this.mBaseName.getParentFile().mkdirs()) {
            var1 = new StringBuilder();
            var1.append("Couldn't create directory ");
            var1.append(this.mBaseName);
            throw new IOException(var1.toString());
         }

         try {
            var4 = new FileOutputStream(this.mBaseName);
         } catch (FileNotFoundException var2) {
            var1 = new StringBuilder();
            var1.append("Couldn't create ");
            var1.append(this.mBaseName);
            throw new IOException(var1.toString());
         }
      }

      return var4;
   }
}
