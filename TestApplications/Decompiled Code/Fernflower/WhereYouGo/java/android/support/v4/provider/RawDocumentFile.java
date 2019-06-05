package android.support.v4.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RawDocumentFile extends DocumentFile {
   private File mFile;

   RawDocumentFile(DocumentFile var1, File var2) {
      super(var1);
      this.mFile = var2;
   }

   private static boolean deleteContents(File var0) {
      File[] var1 = var0.listFiles();
      boolean var2 = true;
      boolean var3 = true;
      if (var1 != null) {
         int var4 = var1.length;
         int var5 = 0;

         while(true) {
            var2 = var3;
            if (var5 >= var4) {
               break;
            }

            var0 = var1[var5];
            var2 = var3;
            if (var0.isDirectory()) {
               var2 = var3 & deleteContents(var0);
            }

            var3 = var2;
            if (!var0.delete()) {
               Log.w("DocumentFile", "Failed to delete " + var0);
               var3 = false;
            }

            ++var5;
         }
      }

      return var2;
   }

   private static String getTypeForName(String var0) {
      int var1 = var0.lastIndexOf(46);
      if (var1 >= 0) {
         var0 = var0.substring(var1 + 1).toLowerCase();
         var0 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var0);
         if (var0 != null) {
            return var0;
         }
      }

      var0 = "application/octet-stream";
      return var0;
   }

   public boolean canRead() {
      return this.mFile.canRead();
   }

   public boolean canWrite() {
      return this.mFile.canWrite();
   }

   public DocumentFile createDirectory(String var1) {
      File var2 = new File(this.mFile, var1);
      RawDocumentFile var3;
      if (!var2.isDirectory() && !var2.mkdir()) {
         var3 = null;
      } else {
         var3 = new RawDocumentFile(this, var2);
      }

      return var3;
   }

   public DocumentFile createFile(String var1, String var2) {
      String var3 = MimeTypeMap.getSingleton().getExtensionFromMimeType(var1);
      var1 = var2;
      if (var3 != null) {
         var1 = var2 + "." + var3;
      }

      File var5 = new File(this.mFile, var1);

      RawDocumentFile var6;
      try {
         var5.createNewFile();
         var6 = new RawDocumentFile(this, var5);
      } catch (IOException var4) {
         Log.w("DocumentFile", "Failed to createFile: " + var4);
         var6 = null;
      }

      return var6;
   }

   public boolean delete() {
      deleteContents(this.mFile);
      return this.mFile.delete();
   }

   public boolean exists() {
      return this.mFile.exists();
   }

   public String getName() {
      return this.mFile.getName();
   }

   public String getType() {
      String var1;
      if (this.mFile.isDirectory()) {
         var1 = null;
      } else {
         var1 = getTypeForName(this.mFile.getName());
      }

      return var1;
   }

   public Uri getUri() {
      return Uri.fromFile(this.mFile);
   }

   public boolean isDirectory() {
      return this.mFile.isDirectory();
   }

   public boolean isFile() {
      return this.mFile.isFile();
   }

   public boolean isVirtual() {
      return false;
   }

   public long lastModified() {
      return this.mFile.lastModified();
   }

   public long length() {
      return this.mFile.length();
   }

   public DocumentFile[] listFiles() {
      ArrayList var1 = new ArrayList();
      File[] var2 = this.mFile.listFiles();
      if (var2 != null) {
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.add(new RawDocumentFile(this, var2[var4]));
         }
      }

      return (DocumentFile[])var1.toArray(new DocumentFile[var1.size()]);
   }

   public boolean renameTo(String var1) {
      File var3 = new File(this.mFile.getParentFile(), var1);
      boolean var2;
      if (this.mFile.renameTo(var3)) {
         this.mFile = var3;
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
