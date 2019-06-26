// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ZoomEvent implements MapEvent
{
    protected MapView source;
    protected double zoomLevel;
    
    public ZoomEvent(final MapView source, final double zoomLevel) {
        this.source = source;
        this.zoomLevel = zoomLevel;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ZoomEvent [source=");
        sb.append(this.source);
        sb.append(", zoomLevel=");
        sb.append(this.zoomLevel);
        sb.append("]");
        return sb.toString();
    }
}
