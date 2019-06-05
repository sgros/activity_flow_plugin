package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;
import org.mapsforge.map.graphics.Bitmap;

class SymbolContainer {
    final boolean alignCenter;
    final Point point;
    final float rotation;
    final Bitmap symbol;

    SymbolContainer(Bitmap symbol, Point point) {
        this(symbol, point, false, 0.0f);
    }

    SymbolContainer(Bitmap symbol, Point point, boolean alignCenter, float rotation) {
        this.symbol = symbol;
        this.point = point;
        this.alignCenter = alignCenter;
        this.rotation = rotation;
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        this.symbol.destroy();
    }
}
