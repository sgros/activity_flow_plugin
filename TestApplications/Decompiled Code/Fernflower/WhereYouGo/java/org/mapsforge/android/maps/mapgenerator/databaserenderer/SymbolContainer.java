package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;
import org.mapsforge.map.graphics.Bitmap;

class SymbolContainer {
   final boolean alignCenter;
   final Point point;
   final float rotation;
   final Bitmap symbol;

   SymbolContainer(Bitmap var1, Point var2) {
      this(var1, var2, false, 0.0F);
   }

   SymbolContainer(Bitmap var1, Point var2, boolean var3, float var4) {
      this.symbol = var1;
      this.point = var2;
      this.alignCenter = var3;
      this.rotation = var4;
   }

   void destroy() {
      this.symbol.destroy();
   }
}
