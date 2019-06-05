// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import java.util.ArrayList;
import com.bumptech.glide.load.ImageHeaderParser;
import java.util.List;

public final class ImageHeaderParserRegistry
{
    private final List<ImageHeaderParser> parsers;
    
    public ImageHeaderParserRegistry() {
        this.parsers = new ArrayList<ImageHeaderParser>();
    }
    
    public void add(final ImageHeaderParser imageHeaderParser) {
        synchronized (this) {
            this.parsers.add(imageHeaderParser);
        }
    }
    
    public List<ImageHeaderParser> getParsers() {
        synchronized (this) {
            return this.parsers;
        }
    }
}
