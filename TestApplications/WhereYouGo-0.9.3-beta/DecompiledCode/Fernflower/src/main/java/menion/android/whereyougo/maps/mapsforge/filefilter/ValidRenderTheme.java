package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.mapsforge.graphics.android.AndroidGraphics;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeHandler;
import org.xml.sax.SAXException;

public final class ValidRenderTheme implements ValidFileFilter {
   private FileOpenResult fileOpenResult;

   public boolean accept(File var1) {
      try {
         ExternalRenderTheme var2 = new ExternalRenderTheme(var1);
         RenderThemeHandler.getRenderTheme(AndroidGraphics.INSTANCE, var2);
         this.fileOpenResult = FileOpenResult.SUCCESS;
      } catch (ParserConfigurationException var3) {
         this.fileOpenResult = new FileOpenResult(var3.getMessage());
      } catch (SAXException var4) {
         this.fileOpenResult = new FileOpenResult(var4.getMessage());
      } catch (IOException var5) {
         this.fileOpenResult = new FileOpenResult(var5.getMessage());
      }

      return this.fileOpenResult.isSuccess();
   }

   public FileOpenResult getFileOpenResult() {
      return this.fileOpenResult;
   }
}
