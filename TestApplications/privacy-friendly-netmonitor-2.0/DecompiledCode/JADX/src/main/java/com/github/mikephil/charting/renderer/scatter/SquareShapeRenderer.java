package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class SquareShapeRenderer implements IShapeRenderer {
    public void renderShape(Canvas canvas, IScatterDataSet iScatterDataSet, ViewPortHandler viewPortHandler, float f, float f2, Paint paint) {
        Paint paint2 = paint;
        float scatterShapeSize = iScatterDataSet.getScatterShapeSize();
        float f3 = scatterShapeSize / 2.0f;
        float convertDpToPixel = Utils.convertDpToPixel(iScatterDataSet.getScatterShapeHoleRadius());
        float f4 = (scatterShapeSize - (convertDpToPixel * 2.0f)) / 2.0f;
        float f5 = f4 / 2.0f;
        int scatterShapeHoleColor = iScatterDataSet.getScatterShapeHoleColor();
        if (((double) scatterShapeSize) > Utils.DOUBLE_EPSILON) {
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(f4);
            f4 = f - convertDpToPixel;
            float f6 = f2 - convertDpToPixel;
            float f7 = f + convertDpToPixel;
            float f8 = f2 + convertDpToPixel;
            canvas.drawRect(f4 - f5, f6 - f5, f7 + f5, f8 + f5, paint2);
            if (scatterShapeHoleColor != ColorTemplate.COLOR_NONE) {
                paint2.setStyle(Style.FILL);
                paint2.setColor(scatterShapeHoleColor);
                canvas.drawRect(f4, f6, f7, f8, paint2);
                return;
            }
            return;
        }
        paint2.setStyle(Style.FILL);
        canvas.drawRect(f - f3, f2 - f3, f + f3, f2 + f3, paint2);
    }
}
