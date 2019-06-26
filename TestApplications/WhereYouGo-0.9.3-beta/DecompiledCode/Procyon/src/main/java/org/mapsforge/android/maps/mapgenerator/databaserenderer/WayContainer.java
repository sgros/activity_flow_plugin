// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class WayContainer implements ShapeContainer
{
    final Point[][] coordinates;
    
    WayContainer(final Point[][] coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
    public ShapeType getShapeType() {
        return ShapeType.WAY;
    }
}
