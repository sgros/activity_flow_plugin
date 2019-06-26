package com.google.android.exoplayer2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class AtomicFile {
   private static final String TAG = "AtomicFile";
   private final File backupName;
   private final File baseName;

   public AtomicFile(File var1) {
      this.baseName = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(var1.getPath());
      var2.append(".bak");
      this.backupName = new File(var2.toString());
   }

   private void restoreBackup() {
      if (this.backupName.exists()) {
         this.baseName.delete();
         this.backupName.renameTo(this.baseName);
      }

   }

   public void delete() {
      this.baseName.delete();
      this.backupName.delete();
   }

   public void endWrite(OutputStream var1) throws IOException {
      var1.close();
      this.backupName.delete();
   }

   public InputStream openRead() throws FileNotFoundException {
      this.restoreBackup();
      return new FileInputStream(this.baseName);
   }

   public OutputStream startWrite() throws IOException {
      StringBuilder var1;
      if (this.baseName.exists()) {
         if (!this.backupName.exists()) {
            if (!this.baseName.renameTo(this.backupName)) {
               var1 = new StringBuilder();
               var1.append("Couldn't rename file ");
               var1.append(this.baseName);
               var1.append(" to backup file ");
               var1.append(this.backupName);
               Log.w("AtomicFile", var1.toString());
            }
         } else {
            this.baseName.delete();
         }
      }

      AtomicFile.AtomicFileOutputStream var5;
      try {
         var5 = new AtomicFile.AtomicFileOutputStream(this.baseName);
      } catch (FileNotFoundException var4) {
         File var2 = this.baseName.getParentFile();
         if (var2 == null || !var2.mkdirs()) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Couldn't create directory ");
            var6.append(this.baseName);
            throw new IOException(var6.toString(), var4);
         }

         try {
            var5 = new AtomicFile.AtomicFileOutputStream(this.baseName);
         } catch (FileNotFoundException var3) {
            var1 = new StringBuilder();
            var1.append("Couldn't create ");
            var1.append(this.baseName);
            throw new IOException(var1.toString(), var3);
         }
      }

      return var5;
   }

   private static final class AtomicFileOutputStream extends OutputStream {
      private boolean closed = false;
      private final FileOutputStream fileOutputStream;

      public AtomicFileOutputStream(File var1) throws FileNotFoundException {
         this.fileOutputStream = new FileOutputStream(var1);
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            this.flush();

            try {
               this.fileOutputStream.getFD().sync();
            } catch (IOException var2) {
               Log.w("AtomicFile", "Failed to sync file descriptor:", var2);
            }

            this.fileOutputStream.close();
         }
      }

      public void flush() throws IOException {
         this.fileOutputStream.flush();
      }

      public void write(int var1) throws IOException {
         this.fileOutputStream.write(var1);
      }

      public void write(byte[] var1) throws IOException {
         this.fileOutputStream.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.fileOutputStream.write(var1, var2, var3);
      }
   }
}
