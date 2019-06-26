// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;
import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.graphics.Bitmap;

final class WayDecorator
{
    private static final int DISTANCE_BETWEEN_SYMBOLS = 200;
    private static final int DISTANCE_BETWEEN_WAY_NAMES = 500;
    private static final int SEGMENT_SAFETY_DISTANCE = 30;
    
    private WayDecorator() {
        throw new IllegalStateException();
    }
    
    static void renderSymbol(final Bitmap bitmap, final boolean b, final boolean b2, final Point[][] array, final List<SymbolContainer> list) {
        int n = 30;
        double x = array[0][0].x;
        double y = array[0][0].y;
    Label_0184:
        for (int i = 1; i < array[0].length; ++i) {
            final double x2 = array[0][i].x;
            final double y2 = array[0][i].y;
            double n2 = x2 - x;
            double n3 = y2 - y;
            float n4;
            for (n4 = (float)Math.sqrt(n2 * n2 + n3 * n3); n4 - n > 30.0f; n4 -= n, n = 200) {
                final float n5 = n / n4;
                x += n5 * n2;
                y += n5 * n3;
                list.add(new SymbolContainer(bitmap, new Point(x, y), b, (float)Math.toDegrees(Math.atan2(y2 - y, x2 - x))));
                if (!b2) {
                    break Label_0184;
                }
                n2 = x2 - x;
                n3 = y2 - y;
            }
            if ((n -= (int)n4) < 30) {
                n = 30;
            }
            x = x2;
            y = y2;
        }
    }
    
    static void renderText(final String s, final Paint paint, final Paint paint2, final Point[][] array, final List<WayTextContainer> list) {
        final int textWidth = paint.getTextWidth(s);
        int n = 0;
        double x = array[0][0].x;
        double y = array[0][0].y;
        for (int i = 1; i < array[0].length; ++i) {
            final double x2 = array[0][i].x;
            final double y2 = array[0][i].y;
            final double n2 = x2 - x;
            final double n3 = y2 - y;
            final double sqrt = Math.sqrt(n2 * n2 + n3 * n3);
            if (n > 0) {
                n -= (int)sqrt;
            }
            else if (sqrt > textWidth + 10) {
                final double[] array2 = new double[4];
                if (x <= x2) {
                    array2[0] = x;
                    array2[1] = y;
                    array2[2] = x2;
                    array2[3] = y2;
                }
                else {
                    array2[0] = x2;
                    array2[1] = y2;
                    array2[2] = x;
                    array2[3] = y;
                }
                list.add(new WayTextContainer(array2, s, paint));
                if (paint2 != null) {
                    list.add(new WayTextContainer(array2, s, paint2));
                }
                n = 500;
            }
            x = x2;
            y = y2;
        }
    }
}
