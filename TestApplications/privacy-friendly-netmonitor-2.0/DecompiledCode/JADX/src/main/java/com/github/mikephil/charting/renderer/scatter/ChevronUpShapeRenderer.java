package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChevronUpShapeRenderer implements IShapeRenderer {
    public void renderShape(Canvas canvas, IScatterDataSet iScatterDataSet, ViewPortHandler viewPortHandler, float f, float f2, Paint paint) {
        float scatterShapeSize = iScatterDataSet.getScatterShapeSize() / 2.0f;
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
        float f3 = 2.0f * scatterShapeSize;
        Canvas canvas2 = canvas;
        float f4 = f;
        float f5 = f2 - f3;
        float f6 = f2;
        Paint paint2 = paint;
        canvas2.drawLine(f4, f5, f + f3, f6, paint2);
        canvas2.drawLine(f4, f5, f - f3, f6, paint2);
    }
}
