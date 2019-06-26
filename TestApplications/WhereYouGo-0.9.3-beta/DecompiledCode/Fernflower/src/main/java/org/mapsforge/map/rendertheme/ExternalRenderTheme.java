package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ExternalRenderTheme implements XmlRenderTheme {
   private static final long serialVersionUID = 1L;
   private final long lastModifiedTime;
   private final File renderThemeFile;

   public ExternalRenderTheme(File var1) throws FileNotFoundException {
      if (!var1.exists()) {
         throw new FileNotFoundException("file does not exist: " + var1.getAbsolutePath());
      } else if (!var1.isFile()) {
         throw new FileNotFoundException("not a file: " + var1.getAbsolutePath());
      } else if (!var1.canRead()) {
         throw new FileNotFoundException("cannot read file: " + var1.getAbsolutePath());
      } else {
         this.lastModifiedTime = var1.lastModified();
         if (this.lastModifiedTime == 0L) {
            throw new FileNotFoundException("cannot read last modified time: " + var1.getAbsolutePath());
         } else {
            this.renderThemeFile = var1;
         }
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof ExternalRenderTheme)) {
            var2 = false;
         } else {
            ExternalRenderTheme var3 = (ExternalRenderTheme)var1;
            if (this.lastModifiedTime != var3.lastModifiedTime) {
               var2 = false;
            } else if (this.renderThemeFile == null) {
               if (var3.renderThemeFile != null) {
                  var2 = false;
               }
            } else if (!this.renderThemeFile.equals(var3.renderThemeFile)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public String getRelativePathPrefix() {
      return this.renderThemeFile.getParent();
   }

   public InputStream getRenderThemeAsStream() throws FileNotFoundException {
      return new FileInputStream(this.renderThemeFile);
   }

   public int hashCode() {
      int var1 = (int)(this.lastModifiedTime ^ this.lastModifiedTime >>> 32);
      int var2;
      if (this.renderThemeFile == null) {
         var2 = 0;
      } else {
         var2 = this.renderThemeFile.hashCode();
      }

      return (var1 + 31) * 31 + var2;
   }
}
