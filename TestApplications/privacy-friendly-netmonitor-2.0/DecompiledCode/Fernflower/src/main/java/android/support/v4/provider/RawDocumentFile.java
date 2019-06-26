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
      File[] var7 = var0.listFiles();
      boolean var1 = true;
      if (var7 != null) {
         int var2 = var7.length;
         var1 = true;

         for(int var3 = 0; var3 < var2; ++var3) {
            File var4 = var7[var3];
            boolean var5 = var1;
            if (var4.isDirectory()) {
               var5 = var1 & deleteContents(var4);
            }

            var1 = var5;
            if (!var4.delete()) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Failed to delete ");
               var6.append(var4);
               Log.w("DocumentFile", var6.toString());
               var1 = false;
            }
         }
      }

      return var1;
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

      return "application/octet-stream";
   }

   public boolean canRead() {
      return this.mFile.canRead();
   }

   public boolean canWrite() {
      return this.mFile.canWrite();
   }

   public DocumentFile createDirectory(String var1) {
      File var2 = new File(this.mFile, var1);
      return !var2.isDirectory() && !var2.mkdir() ? null : new RawDocumentFile(this, var2);
   }

   public DocumentFile createFile(String var1, String var2) {
      String var3 = MimeTypeMap.getSingleton().getExtensionFromMimeType(var1);
      var1 = var2;
      StringBuilder var5;
      if (var3 != null) {
         var5 = new StringBuilder();
         var5.append(var2);
         var5.append(".");
         var5.append(var3);
         var1 = var5.toString();
      }

      File var6 = new File(this.mFile, var1);

      try {
         var6.createNewFile();
         RawDocumentFile var7 = new RawDocumentFile(this, var6);
         return var7;
      } catch (IOException var4) {
         var5 = new StringBuilder();
         var5.append("Failed to createFile: ");
         var5.append(var4);
         Log.w("DocumentFile", var5.toString());
         return null;
      }
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
      return this.mFile.isDirectory() ? null : getTypeForName(this.mFile.getName());
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
      File var2 = new File(this.mFile.getParentFile(), var1);
      if (this.mFile.renameTo(var2)) {
         this.mFile = var2;
         return true;
      } else {
         return false;
      }
   }
}
