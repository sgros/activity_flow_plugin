// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;

class WayTextContainer
{
    final double[] coordinates;
    final Paint paint;
    final String text;
    
    WayTextContainer(final double[] coordinates, final String text, final Paint paint) {
        this.coordinates = coordinates;
        this.text = text;
        this.paint = paint;
    }
}
