package okhttp3.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import okio.Okio;
import okio.Sink;
import okio.Source;

public interface FileSystem {
   FileSystem SYSTEM = new FileSystem() {
      public Sink appendingSink(File var1) throws FileNotFoundException {
         Sink var2;
         Sink var4;
         try {
            var2 = Okio.appendingSink(var1);
         } catch (FileNotFoundException var3) {
            var1.getParentFile().mkdirs();
            var4 = Okio.appendingSink(var1);
            return var4;
         }

         var4 = var2;
         return var4;
      }

      public void delete(File var1) throws IOException {
         if (!var1.delete() && var1.exists()) {
            throw new IOException("failed to delete " + var1);
         }
      }

      public void deleteContents(File var1) throws IOException {
         File[] var2 = var1.listFiles();
         if (var2 == null) {
            throw new IOException("not a readable directory: " + var1);
         } else {
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               var1 = var2[var4];
               if (var1.isDirectory()) {
                  this.deleteContents(var1);
               }

               if (!var1.delete()) {
                  throw new IOException("failed to delete " + var1);
               }
            }

         }
      }

      public boolean exists(File var1) {
         return var1.exists();
      }

      public void rename(File var1, File var2) throws IOException {
         this.delete(var2);
         if (!var1.renameTo(var2)) {
            throw new IOException("failed to rename " + var1 + " to " + var2);
         }
      }

      public Sink sink(File var1) throws FileNotFoundException {
         Sink var2;
         Sink var4;
         try {
            var2 = Okio.sink(var1);
         } catch (FileNotFoundException var3) {
            var1.getParentFile().mkdirs();
            var4 = Okio.sink(var1);
            return var4;
         }

         var4 = var2;
         return var4;
      }

      public long size(File var1) {
         return var1.length();
      }

      public Source source(File var1) throws FileNotFoundException {
         return Okio.source(var1);
      }
   };

   Sink appendingSink(File var1) throws FileNotFoundException;

   void delete(File var1) throws IOException;

   void deleteContents(File var1) throws IOException;

   boolean exists(File var1);

   void rename(File var1, File var2) throws IOException;

   Sink sink(File var1) throws FileNotFoundException;

   long size(File var1);

   Source source(File var1) throws FileNotFoundException;
}
