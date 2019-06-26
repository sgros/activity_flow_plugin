// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme;

import java.io.InputStream;

public enum InternalRenderTheme implements XmlRenderTheme
{
    OSMARENDER("/osmarender/", "osmarender.xml");
    
    private final String absolutePath;
    private final String file;
    
    private InternalRenderTheme(final String absolutePath, final String file) {
        this.absolutePath = absolutePath;
        this.file = file;
    }
    
    @Override
    public String getRelativePathPrefix() {
        return this.absolutePath;
    }
    
    @Override
    public InputStream getRenderThemeAsStream() {
        return Thread.currentThread().getClass().getResourceAsStream(this.absolutePath + this.file);
    }
}
