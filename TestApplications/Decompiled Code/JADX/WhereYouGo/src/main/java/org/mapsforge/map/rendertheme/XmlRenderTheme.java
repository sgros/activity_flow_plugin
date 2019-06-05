package org.mapsforge.map.rendertheme;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public interface XmlRenderTheme extends Serializable {
    String getRelativePathPrefix();

    InputStream getRenderThemeAsStream() throws FileNotFoundException;
}
