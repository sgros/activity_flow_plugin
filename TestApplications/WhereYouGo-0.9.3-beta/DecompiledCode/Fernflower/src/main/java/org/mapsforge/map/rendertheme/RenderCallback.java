package org.mapsforge.map.rendertheme;

import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

public interface RenderCallback {
   void renderArea(Paint var1, Paint var2, int var3);

   void renderAreaCaption(String var1, float var2, Paint var3, Paint var4);

   void renderAreaSymbol(Bitmap var1);

   void renderPointOfInterestCaption(String var1, float var2, Paint var3, Paint var4);

   void renderPointOfInterestCircle(float var1, Paint var2, Paint var3, int var4);

   void renderPointOfInterestSymbol(Bitmap var1);

   void renderWay(Paint var1, int var2);

   void renderWaySymbol(Bitmap var1, boolean var2, boolean var3);

   void renderWayText(String var1, Paint var2, Paint var3);
}
