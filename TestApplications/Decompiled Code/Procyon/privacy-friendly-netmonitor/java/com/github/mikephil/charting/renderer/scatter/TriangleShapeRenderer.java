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
import android.graphics.Path;

public class TriangleShapeRenderer implements IShapeRenderer
{
    protected Path mTrianglePathBuffer;
    
    public TriangleShapeRenderer() {
        this.mTrianglePathBuffer = new Path();
    }
    
    @Override
    public void renderShape(final Canvas canvas, final IScatterDataSet set, final ViewPortHandler viewPortHandler, float n, float n2, final Paint paint) {
        final float scatterShapeSize = set.getScatterShapeSize();
        final float n3 = scatterShapeSize / 2.0f;
        final float n4 = (scatterShapeSize - Utils.convertDpToPixel(set.getScatterShapeHoleRadius()) * 2.0f) / 2.0f;
        final int scatterShapeHoleColor = set.getScatterShapeHoleColor();
        paint.setStyle(Paint$Style.FILL);
        final Path mTrianglePathBuffer = this.mTrianglePathBuffer;
        mTrianglePathBuffer.reset();
        final float n5 = n2 - n3;
        mTrianglePathBuffer.moveTo(n, n5);
        final float n6 = n + n3;
        n2 += n3;
        mTrianglePathBuffer.lineTo(n6, n2);
        final float n7 = n - n3;
        mTrianglePathBuffer.lineTo(n7, n2);
        final double n8 = scatterShapeSize;
        if (n8 > 0.0) {
            mTrianglePathBuffer.lineTo(n, n5);
            final float n9 = n7 + n4;
            final float n10 = n2 - n4;
            mTrianglePathBuffer.moveTo(n9, n10);
            mTrianglePathBuffer.lineTo(n6 - n4, n10);
            mTrianglePathBuffer.lineTo(n, n5 + n4);
            mTrianglePathBuffer.lineTo(n9, n10);
        }
        mTrianglePathBuffer.close();
        canvas.drawPath(mTrianglePathBuffer, paint);
        mTrianglePathBuffer.reset();
        if (n8 > 0.0 && scatterShapeHoleColor != 1122867) {
            paint.setColor(scatterShapeHoleColor);
            mTrianglePathBuffer.moveTo(n, n5 + n4);
            n = n2 - n4;
            mTrianglePathBuffer.lineTo(n6 - n4, n);
            mTrianglePathBuffer.lineTo(n7 + n4, n);
            mTrianglePathBuffer.close();
            canvas.drawPath(mTrianglePathBuffer, paint);
            mTrianglePathBuffer.reset();
        }
    }
}
