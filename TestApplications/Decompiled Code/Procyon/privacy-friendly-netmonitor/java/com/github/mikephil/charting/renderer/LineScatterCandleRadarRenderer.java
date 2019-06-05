// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.PathEffect;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Path;

public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer
{
    private Path mHighlightLinePath;
    
    public LineScatterCandleRadarRenderer(final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mHighlightLinePath = new Path();
    }
    
    protected void drawHighlightLines(final Canvas canvas, final float n, final float n2, final ILineScatterCandleRadarDataSet set) {
        this.mHighlightPaint.setColor(set.getHighLightColor());
        this.mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
        this.mHighlightPaint.setPathEffect((PathEffect)set.getDashPathEffectHighlight());
        if (set.isVerticalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(n, this.mViewPortHandler.contentTop());
            this.mHighlightLinePath.lineTo(n, this.mViewPortHandler.contentBottom());
            canvas.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(this.mViewPortHandler.contentLeft(), n2);
            this.mHighlightLinePath.lineTo(this.mViewPortHandler.contentRight(), n2);
            canvas.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
    }
}
