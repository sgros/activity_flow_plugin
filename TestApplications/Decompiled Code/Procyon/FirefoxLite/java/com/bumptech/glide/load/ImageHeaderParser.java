// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.io.IOException;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.InputStream;

public interface ImageHeaderParser
{
    int getOrientation(final InputStream p0, final ArrayPool p1) throws IOException;
    
    ImageType getType(final InputStream p0) throws IOException;
    
    public enum ImageType
    {
        GIF(true), 
        JPEG(false), 
        PNG(false), 
        PNG_A(true), 
        RAW(false), 
        UNKNOWN(false), 
        WEBP(false), 
        WEBP_A(true);
        
        private final boolean hasAlpha;
        
        private ImageType(final boolean hasAlpha) {
            this.hasAlpha = hasAlpha;
        }
        
        public boolean hasAlpha() {
            return this.hasAlpha;
        }
    }
}
