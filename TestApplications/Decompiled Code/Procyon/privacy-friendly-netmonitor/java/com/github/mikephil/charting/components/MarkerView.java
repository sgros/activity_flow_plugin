// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import android.graphics.Canvas;
import android.view.View;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import com.github.mikephil.charting.charts.Chart;
import java.lang.ref.WeakReference;
import com.github.mikephil.charting.utils.MPPointF;
import android.widget.RelativeLayout;

public class MarkerView extends RelativeLayout implements IMarker
{
    private MPPointF mOffset;
    private MPPointF mOffset2;
    private WeakReference<Chart> mWeakChart;
    
    public MarkerView(final Context context, final int n) {
        super(context);
        this.mOffset = new MPPointF();
        this.mOffset2 = new MPPointF();
        this.setupLayoutResource(n);
    }
    
    private void setupLayoutResource(final int n) {
        final View inflate = LayoutInflater.from(this.getContext()).inflate(n, (ViewGroup)this);
        inflate.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-2, -2));
        inflate.measure(View$MeasureSpec.makeMeasureSpec(0, 0), View$MeasureSpec.makeMeasureSpec(0, 0));
        inflate.layout(0, 0, inflate.getMeasuredWidth(), inflate.getMeasuredHeight());
    }
    
    public void draw(final Canvas canvas, final float n, final float n2) {
        final MPPointF offsetForDrawingAtPoint = this.getOffsetForDrawingAtPoint(n, n2);
        final int save = canvas.save();
        canvas.translate(n + offsetForDrawingAtPoint.x, n2 + offsetForDrawingAtPoint.y);
        this.draw(canvas);
        canvas.restoreToCount(save);
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
    
    public MPPointF getOffset() {
        return this.mOffset;
    }
    
    public MPPointF getOffsetForDrawingAtPoint(final float n, final float n2) {
        final MPPointF offset = this.getOffset();
        this.mOffset2.x = offset.x;
        this.mOffset2.y = offset.y;
        final Chart chartView = this.getChartView();
        final float n3 = (float)this.getWidth();
        final float n4 = (float)this.getHeight();
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
    
    public void refreshContent(final Entry entry, final Highlight highlight) {
        this.measure(View$MeasureSpec.makeMeasureSpec(0, 0), View$MeasureSpec.makeMeasureSpec(0, 0));
        this.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
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
}
