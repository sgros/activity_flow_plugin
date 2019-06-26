package org.mapsforge.map.reader.header;

public class FileOpenResult {
   public static final FileOpenResult SUCCESS = new FileOpenResult();
   private final String errorMessage;
   private final boolean success;

   private FileOpenResult() {
      this.success = true;
      this.errorMessage = null;
   }

   public FileOpenResult(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("error message must not be null");
      } else {
         this.success = false;
         this.errorMessage = var1;
      }
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public boolean isSuccess() {
      return this.success;
   }
}
