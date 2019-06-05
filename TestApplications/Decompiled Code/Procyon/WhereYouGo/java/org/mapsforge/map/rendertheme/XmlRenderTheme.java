// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public interface XmlRenderTheme extends Serializable
{
    String getRelativePathPrefix();
    
    InputStream getRenderThemeAsStream() throws FileNotFoundException;
}
