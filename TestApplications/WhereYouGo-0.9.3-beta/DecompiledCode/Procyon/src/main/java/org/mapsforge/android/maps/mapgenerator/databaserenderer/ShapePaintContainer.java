// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;

class ShapePaintContainer
{
    final Paint paint;
    final ShapeContainer shapeContainer;
    
    ShapePaintContainer(final ShapeContainer shapeContainer, final Paint paint) {
        this.shapeContainer = shapeContainer;
        this.paint = paint;
    }
}
