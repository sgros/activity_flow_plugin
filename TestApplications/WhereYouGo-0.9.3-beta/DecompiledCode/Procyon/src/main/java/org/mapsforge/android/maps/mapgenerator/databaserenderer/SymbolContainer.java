// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.core.model.Point;

class SymbolContainer
{
    final boolean alignCenter;
    final Point point;
    final float rotation;
    final Bitmap symbol;
    
    SymbolContainer(final Bitmap bitmap, final Point point) {
        this(bitmap, point, false, 0.0f);
    }
    
    SymbolContainer(final Bitmap symbol, final Point point, final boolean alignCenter, final float rotation) {
        this.symbol = symbol;
        this.point = point;
        this.alignCenter = alignCenter;
        this.rotation = rotation;
    }
    
    void destroy() {
        this.symbol.destroy();
    }
}
