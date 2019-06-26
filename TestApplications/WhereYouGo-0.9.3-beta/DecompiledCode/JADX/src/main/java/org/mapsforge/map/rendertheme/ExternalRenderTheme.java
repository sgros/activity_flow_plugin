package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ExternalRenderTheme implements XmlRenderTheme {
    private static final long serialVersionUID = 1;
    private final long lastModifiedTime;
    private final File renderThemeFile;

    public ExternalRenderTheme(File renderThemeFile) throws FileNotFoundException {
        if (!renderThemeFile.exists()) {
            throw new FileNotFoundException("file does not exist: " + renderThemeFile.getAbsolutePath());
        } else if (!renderThemeFile.isFile()) {
            throw new FileNotFoundException("not a file: " + renderThemeFile.getAbsolutePath());
        } else if (renderThemeFile.canRead()) {
            this.lastModifiedTime = renderThemeFile.lastModified();
            if (this.lastModifiedTime == 0) {
                throw new FileNotFoundException("cannot read last modified time: " + renderThemeFile.getAbsolutePath());
            }
            this.renderThemeFile = renderThemeFile;
        } else {
            throw new FileNotFoundException("cannot read file: " + renderThemeFile.getAbsolutePath());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ExternalRenderTheme)) {
            return false;
        }
        ExternalRenderTheme other = (ExternalRenderTheme) obj;
        if (this.lastModifiedTime != other.lastModifiedTime) {
            return false;
        }
        if (this.renderThemeFile == null) {
            if (other.renderThemeFile != null) {
                return false;
            }
            return true;
        } else if (this.renderThemeFile.equals(other.renderThemeFile)) {
            return true;
        } else {
            return false;
        }
    }

    public String getRelativePathPrefix() {
        return this.renderThemeFile.getParent();
    }

    public InputStream getRenderThemeAsStream() throws FileNotFoundException {
        return new FileInputStream(this.renderThemeFile);
    }

    public int hashCode() {
        return ((((int) (this.lastModifiedTime ^ (this.lastModifiedTime >>> 32))) + 31) * 31) + (this.renderThemeFile == null ? 0 : this.renderThemeFile.hashCode());
    }
}
