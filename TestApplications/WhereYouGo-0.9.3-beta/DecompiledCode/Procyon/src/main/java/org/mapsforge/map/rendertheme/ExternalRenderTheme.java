// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.File;

public class ExternalRenderTheme implements XmlRenderTheme
{
    private static final long serialVersionUID = 1L;
    private final long lastModifiedTime;
    private final File renderThemeFile;
    
    public ExternalRenderTheme(final File renderThemeFile) throws FileNotFoundException {
        if (!renderThemeFile.exists()) {
            throw new FileNotFoundException("file does not exist: " + renderThemeFile.getAbsolutePath());
        }
        if (!renderThemeFile.isFile()) {
            throw new FileNotFoundException("not a file: " + renderThemeFile.getAbsolutePath());
        }
        if (!renderThemeFile.canRead()) {
            throw new FileNotFoundException("cannot read file: " + renderThemeFile.getAbsolutePath());
        }
        this.lastModifiedTime = renderThemeFile.lastModified();
        if (this.lastModifiedTime == 0L) {
            throw new FileNotFoundException("cannot read last modified time: " + renderThemeFile.getAbsolutePath());
        }
        this.renderThemeFile = renderThemeFile;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof ExternalRenderTheme)) {
                b = false;
            }
            else {
                final ExternalRenderTheme externalRenderTheme = (ExternalRenderTheme)o;
                if (this.lastModifiedTime != externalRenderTheme.lastModifiedTime) {
                    b = false;
                }
                else if (this.renderThemeFile == null) {
                    if (externalRenderTheme.renderThemeFile != null) {
                        b = false;
                    }
                }
                else if (!this.renderThemeFile.equals(externalRenderTheme.renderThemeFile)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public String getRelativePathPrefix() {
        return this.renderThemeFile.getParent();
    }
    
    @Override
    public InputStream getRenderThemeAsStream() throws FileNotFoundException {
        return new FileInputStream(this.renderThemeFile);
    }
    
    @Override
    public int hashCode() {
        final int n = (int)(this.lastModifiedTime ^ this.lastModifiedTime >>> 32);
        int hashCode;
        if (this.renderThemeFile == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.renderThemeFile.hashCode();
        }
        return (n + 31) * 31 + hashCode;
    }
}
