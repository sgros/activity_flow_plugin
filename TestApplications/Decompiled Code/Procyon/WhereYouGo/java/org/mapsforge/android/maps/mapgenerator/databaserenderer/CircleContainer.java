// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class CircleContainer implements ShapeContainer
{
    final Point point;
    final float radius;
    
    CircleContainer(final Point point, final float radius) {
        this.point = point;
        this.radius = radius;
    }
    
    @Override
    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }
}
