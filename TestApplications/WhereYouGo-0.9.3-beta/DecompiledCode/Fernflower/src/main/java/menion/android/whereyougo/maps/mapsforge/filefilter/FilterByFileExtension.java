package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import java.io.FileFilter;

public class FilterByFileExtension implements FileFilter {
   private final String extension;

   public FilterByFileExtension(String var1) {
      this.extension = var1;
   }

   public boolean accept(File var1) {
      boolean var2 = true;
      if (!var1.canRead() || !var1.isDirectory() && (!var1.isFile() || !var1.getName().endsWith(this.extension))) {
         var2 = false;
      }

      return var2;
   }
}
