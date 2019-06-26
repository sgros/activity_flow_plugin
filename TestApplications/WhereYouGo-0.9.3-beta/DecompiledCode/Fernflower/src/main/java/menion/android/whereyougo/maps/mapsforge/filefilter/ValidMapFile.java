package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;

public final class ValidMapFile implements ValidFileFilter {
   private FileOpenResult fileOpenResult;

   public boolean accept(File var1) {
      MapDatabase var2 = new MapDatabase();
      this.fileOpenResult = var2.openFile(var1);
      var2.closeFile();
      return this.fileOpenResult.isSuccess();
   }

   public FileOpenResult getFileOpenResult() {
      return this.fileOpenResult;
   }
}
