// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import android.graphics.Canvas;

public class SquareShapeRenderer implements IShapeRenderer
{
    @Override
    public void renderShape(final Canvas canvas, final IScatterDataSet set, final ViewPortHandler viewPortHandler, float n, float n2, final Paint paint) {
        final float scatterShapeSize = set.getScatterShapeSize();
        final float n3 = scatterShapeSize / 2.0f;
        final float convertDpToPixel = Utils.convertDpToPixel(set.getScatterShapeHoleRadius());
        final float strokeWidth = (scatterShapeSize - convertDpToPixel * 2.0f) / 2.0f;
        final float n4 = strokeWidth / 2.0f;
        final int scatterShapeHoleColor = set.getScatterShapeHoleColor();
        if (scatterShapeSize > 0.0) {
            paint.setStyle(Paint$Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            final float n5 = n - convertDpToPixel;
            final float n6 = n2 - convertDpToPixel;
            n += convertDpToPixel;
            n2 += convertDpToPixel;
            canvas.drawRect(n5 - n4, n6 - n4, n + n4, n2 + n4, paint);
            if (scatterShapeHoleColor != 1122867) {
                paint.setStyle(Paint$Style.FILL);
                paint.setColor(scatterShapeHoleColor);
                canvas.drawRect(n5, n6, n, n2, paint);
            }
        }
        else {
            paint.setStyle(Paint$Style.FILL);
            canvas.drawRect(n - n3, n2 - n3, n + n3, n2 + n3, paint);
        }
    }
}
