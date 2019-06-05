package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class TriangleShapeRenderer implements IShapeRenderer {
    protected Path mTrianglePathBuffer = new Path();

    public void renderShape(Canvas canvas, IScatterDataSet iScatterDataSet, ViewPortHandler viewPortHandler, float f, float f2, Paint paint) {
        Canvas canvas2 = canvas;
        float f3 = f;
        Paint paint2 = paint;
        float scatterShapeSize = iScatterDataSet.getScatterShapeSize();
        float f4 = scatterShapeSize / 2.0f;
        float convertDpToPixel = (scatterShapeSize - (Utils.convertDpToPixel(iScatterDataSet.getScatterShapeHoleRadius()) * 2.0f)) / 2.0f;
        int scatterShapeHoleColor = iScatterDataSet.getScatterShapeHoleColor();
        paint2.setStyle(Style.FILL);
        Path path = this.mTrianglePathBuffer;
        path.reset();
        float f5 = f2 - f4;
        path.moveTo(f3, f5);
        float f6 = f3 + f4;
        float f7 = f2 + f4;
        path.lineTo(f6, f7);
        f4 = f3 - f4;
        path.lineTo(f4, f7);
        double d = (double) scatterShapeSize;
        if (d > Utils.DOUBLE_EPSILON) {
            path.lineTo(f3, f5);
            scatterShapeSize = f4 + convertDpToPixel;
            float f8 = f7 - convertDpToPixel;
            path.moveTo(scatterShapeSize, f8);
            path.lineTo(f6 - convertDpToPixel, f8);
            path.lineTo(f3, f5 + convertDpToPixel);
            path.lineTo(scatterShapeSize, f8);
        }
        path.close();
        canvas2.drawPath(path, paint2);
        path.reset();
        if (d > Utils.DOUBLE_EPSILON && scatterShapeHoleColor != ColorTemplate.COLOR_NONE) {
            paint2.setColor(scatterShapeHoleColor);
            path.moveTo(f3, f5 + convertDpToPixel);
            f7 -= convertDpToPixel;
            path.lineTo(f6 - convertDpToPixel, f7);
            path.lineTo(f4 + convertDpToPixel, f7);
            path.close();
            canvas2.drawPath(path, paint2);
            path.reset();
        }
    }
}
