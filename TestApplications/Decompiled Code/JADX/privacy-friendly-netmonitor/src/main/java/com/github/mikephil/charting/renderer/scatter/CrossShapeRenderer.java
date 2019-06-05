package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CrossShapeRenderer implements IShapeRenderer {
    public void renderShape(Canvas canvas, IScatterDataSet iScatterDataSet, ViewPortHandler viewPortHandler, float f, float f2, Paint paint) {
        Paint paint2 = paint;
        float scatterShapeSize = iScatterDataSet.getScatterShapeSize() / 2.0f;
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(Utils.convertDpToPixel(1.0f));
        canvas.drawLine(f - scatterShapeSize, f2, f + scatterShapeSize, f2, paint2);
        canvas.drawLine(f, f2 - scatterShapeSize, f, f2 + scatterShapeSize, paint2);
    }
}
