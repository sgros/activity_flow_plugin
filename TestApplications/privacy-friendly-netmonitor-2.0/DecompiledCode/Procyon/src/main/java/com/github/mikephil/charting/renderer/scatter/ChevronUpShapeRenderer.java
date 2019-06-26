// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer.scatter;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import android.graphics.Canvas;

public class ChevronUpShapeRenderer implements IShapeRenderer
{
    @Override
    public void renderShape(final Canvas canvas, final IScatterDataSet set, final ViewPortHandler viewPortHandler, final float n, final float n2, final Paint paint) {
        final float n3 = set.getScatterShapeSize() / 2.0f;
        paint.setStyle(Paint$Style.STROKE);
        paint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
        final float n4 = 2.0f * n3;
        final float n5 = n2 - n4;
        canvas.drawLine(n, n5, n + n4, n2, paint);
        canvas.drawLine(n, n5, n - n4, n2, paint);
    }
}
