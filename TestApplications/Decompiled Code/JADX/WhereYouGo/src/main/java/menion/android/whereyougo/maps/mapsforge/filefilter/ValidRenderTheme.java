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

    public boolean accept(File file) {
        try {
            RenderThemeHandler.getRenderTheme(AndroidGraphics.INSTANCE, new ExternalRenderTheme(file));
            this.fileOpenResult = FileOpenResult.SUCCESS;
        } catch (ParserConfigurationException e) {
            this.fileOpenResult = new FileOpenResult(e.getMessage());
        } catch (SAXException e2) {
            this.fileOpenResult = new FileOpenResult(e2.getMessage());
        } catch (IOException e3) {
            this.fileOpenResult = new FileOpenResult(e3.getMessage());
        }
        return this.fileOpenResult.isSuccess();
    }

    public FileOpenResult getFileOpenResult() {
        return this.fileOpenResult;
    }
}
