// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

public final class UtcTimingElement
{
    public final String schemeIdUri;
    public final String value;
    
    public UtcTimingElement(final String schemeIdUri, final String value) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.schemeIdUri);
        sb.append(", ");
        sb.append(this.value);
        return sb.toString();
    }
}
