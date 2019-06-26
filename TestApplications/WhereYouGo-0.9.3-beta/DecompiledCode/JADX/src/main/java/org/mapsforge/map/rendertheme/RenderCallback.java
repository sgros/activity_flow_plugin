package org.mapsforge.map.rendertheme;

import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

public interface RenderCallback {
    void renderArea(Paint paint, Paint paint2, int i);

    void renderAreaCaption(String str, float f, Paint paint, Paint paint2);

    void renderAreaSymbol(Bitmap bitmap);

    void renderPointOfInterestCaption(String str, float f, Paint paint, Paint paint2);

    void renderPointOfInterestCircle(float f, Paint paint, Paint paint2, int i);

    void renderPointOfInterestSymbol(Bitmap bitmap);

    void renderWay(Paint paint, int i);

    void renderWaySymbol(Bitmap bitmap, boolean z, boolean z2);

    void renderWayText(String str, Paint paint, Paint paint2);
}
