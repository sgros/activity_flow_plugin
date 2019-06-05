// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Canvas;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Align;
import android.graphics.Color;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.animation.ChartAnimator;

public abstract class DataRenderer extends Renderer
{
    protected ChartAnimator mAnimator;
    protected Paint mDrawPaint;
    protected Paint mHighlightPaint;
    protected Paint mRenderPaint;
    protected Paint mValuePaint;
    
    public DataRenderer(final ChartAnimator mAnimator, final ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = mAnimator;
        (this.mRenderPaint = new Paint(1)).setStyle(Paint$Style.FILL);
        this.mDrawPaint = new Paint(4);
        (this.mValuePaint = new Paint(1)).setColor(Color.rgb(63, 63, 63));
        this.mValuePaint.setTextAlign(Paint$Align.CENTER);
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0f));
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }
    
    protected void applyValueTextStyle(final IDataSet set) {
        this.mValuePaint.setTypeface(set.getValueTypeface());
        this.mValuePaint.setTextSize(set.getValueTextSize());
    }
    
    public abstract void drawData(final Canvas p0);
    
    public abstract void drawExtras(final Canvas p0);
    
    public abstract void drawHighlighted(final Canvas p0, final Highlight[] p1);
    
    public void drawValue(final Canvas canvas, final IValueFormatter valueFormatter, final float n, final Entry entry, final int n2, final float n3, final float n4, final int color) {
        this.mValuePaint.setColor(color);
        canvas.drawText(valueFormatter.getFormattedValue(n, entry, n2, this.mViewPortHandler), n3, n4, this.mValuePaint);
    }
    
    public abstract void drawValues(final Canvas p0);
    
    public Paint getPaintHighlight() {
        return this.mHighlightPaint;
    }
    
    public Paint getPaintRender() {
        return this.mRenderPaint;
    }
    
    public Paint getPaintValues() {
        return this.mValuePaint;
    }
    
    public abstract void initBuffers();
    
    protected boolean isDrawingValuesAllowed(final ChartInterface chartInterface) {
        return chartInterface.getData().getEntryCount() < chartInterface.getMaxVisibleCount() * this.mViewPortHandler.getScaleX();
    }
}
