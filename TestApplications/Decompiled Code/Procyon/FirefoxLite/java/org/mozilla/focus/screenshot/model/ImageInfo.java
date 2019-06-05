// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot.model;

public class ImageInfo
{
    public String title;
    
    public ImageInfo(final String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ImageInfo{title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
