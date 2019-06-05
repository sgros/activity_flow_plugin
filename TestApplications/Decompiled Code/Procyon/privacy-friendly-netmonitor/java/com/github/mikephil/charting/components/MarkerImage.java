// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import android.graphics.Canvas;
import android.content.res.Resources$Theme;
import android.os.Build$VERSION;
import com.github.mikephil.charting.charts.Chart;
import java.lang.ref.WeakReference;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.content.Context;

public class MarkerImage implements IMarker
{
    private Context mContext;
    private Drawable mDrawable;
    private Rect mDrawableBoundsCache;
    private MPPointF mOffset;
    private MPPointF mOffset2;
    private FSize mSize;
    private WeakReference<Chart> mWeakChart;
    
    public MarkerImage(final Context mContext, final int n) {
        this.mOffset = new MPPointF();
        this.mOffset2 = new MPPointF();
        this.mSize = new FSize();
        this.mDrawableBoundsCache = new Rect();
        this.mContext = mContext;
        if (Build$VERSION.SDK_INT >= 21) {
            this.mDrawable = this.mContext.getResources().getDrawable(n, (Resources$Theme)null);
        }
        else {
            this.mDrawable = this.mContext.getResources().getDrawable(n);
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final float n, final float n2) {
        if (this.mDrawable == null) {
            return;
        }
        final MPPointF offsetForDrawingAtPoint = this.getOffsetForDrawingAtPoint(n, n2);
        final float width = this.mSize.width;
        final float height = this.mSize.height;
        float n3 = width;
        if (width == 0.0f) {
            n3 = width;
            if (this.mDrawable != null) {
                n3 = (float)this.mDrawable.getIntrinsicWidth();
            }
        }
        float n4 = height;
        if (height == 0.0f) {
            n4 = height;
            if (this.mDrawable != null) {
                n4 = (float)this.mDrawable.getIntrinsicHeight();
            }
        }
        this.mDrawable.copyBounds(this.mDrawableBoundsCache);
        this.mDrawable.setBounds(this.mDrawableBoundsCache.left, this.mDrawableBoundsCache.top, this.mDrawableBoundsCache.left + (int)n3, this.mDrawableBoundsCache.top + (int)n4);
        final int save = canvas.save();
        canvas.translate(n + offsetForDrawingAtPoint.x, n2 + offsetForDrawingAtPoint.y);
        this.mDrawable.draw(canvas);
        canvas.restoreToCount(save);
        this.mDrawable.setBounds(this.mDrawableBoundsCache);
    }
    
    public Chart getChartView() {
        Chart chart;
        if (this.mWeakChart == null) {
            chart = null;
        }
        else {
            chart = this.mWeakChart.get();
        }
        return chart;
    }
    
    @Override
    public MPPointF getOffset() {
        return this.mOffset;
    }
    
    @Override
    public MPPointF getOffsetForDrawingAtPoint(final float n, final float n2) {
        final MPPointF offset = this.getOffset();
        this.mOffset2.x = offset.x;
        this.mOffset2.y = offset.y;
        final Chart chartView = this.getChartView();
        final float width = this.mSize.width;
        final float height = this.mSize.height;
        float n3 = width;
        if (width == 0.0f) {
            n3 = width;
            if (this.mDrawable != null) {
                n3 = (float)this.mDrawable.getIntrinsicWidth();
            }
        }
        float n4 = height;
        if (height == 0.0f) {
            n4 = height;
            if (this.mDrawable != null) {
                n4 = (float)this.mDrawable.getIntrinsicHeight();
            }
        }
        if (this.mOffset2.x + n < 0.0f) {
            this.mOffset2.x = -n;
        }
        else if (chartView != null && n + n3 + this.mOffset2.x > chartView.getWidth()) {
            this.mOffset2.x = chartView.getWidth() - n - n3;
        }
        if (this.mOffset2.y + n2 < 0.0f) {
            this.mOffset2.y = -n2;
        }
        else if (chartView != null && n2 + n4 + this.mOffset2.y > chartView.getHeight()) {
            this.mOffset2.y = chartView.getHeight() - n2 - n4;
        }
        return this.mOffset2;
    }
    
    public FSize getSize() {
        return this.mSize;
    }
    
    @Override
    public void refreshContent(final Entry entry, final Highlight highlight) {
    }
    
    public void setChartView(final Chart referent) {
        this.mWeakChart = new WeakReference<Chart>(referent);
    }
    
    public void setOffset(final float x, final float y) {
        this.mOffset.x = x;
        this.mOffset.y = y;
    }
    
    public void setOffset(final MPPointF mOffset) {
        this.mOffset = mOffset;
        if (this.mOffset == null) {
            this.mOffset = new MPPointF();
        }
    }
    
    public void setSize(final FSize mSize) {
        this.mSize = mSize;
        if (this.mSize == null) {
            this.mSize = new FSize();
        }
    }
}
