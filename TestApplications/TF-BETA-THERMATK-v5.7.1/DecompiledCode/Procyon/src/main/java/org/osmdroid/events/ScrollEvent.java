// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ScrollEvent implements MapEvent
{
    protected MapView source;
    protected int x;
    protected int y;
    
    public ScrollEvent(final MapView source, final int x, final int y) {
        this.source = source;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ScrollEvent [source=");
        sb.append(this.source);
        sb.append(", x=");
        sb.append(this.x);
        sb.append(", y=");
        sb.append(this.y);
        sb.append("]");
        return sb.toString();
    }
}
