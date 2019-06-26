// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.MPPointD;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Paint;
import com.github.mikephil.charting.components.AxisBase;

public abstract class AxisRenderer extends Renderer
{
    protected AxisBase mAxis;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;
    protected Paint mGridPaint;
    protected Paint mLimitLinePaint;
    protected Transformer mTrans;
    
    public AxisRenderer(final ViewPortHandler viewPortHandler, final Transformer mTrans, final AxisBase mAxis) {
        super(viewPortHandler);
        this.mTrans = mTrans;
        this.mAxis = mAxis;
        if (this.mViewPortHandler != null) {
            this.mAxisLabelPaint = new Paint(1);
            (this.mGridPaint = new Paint()).setColor(-7829368);
            this.mGridPaint.setStrokeWidth(1.0f);
            this.mGridPaint.setStyle(Paint$Style.STROKE);
            this.mGridPaint.setAlpha(90);
            (this.mAxisLinePaint = new Paint()).setColor(-16777216);
            this.mAxisLinePaint.setStrokeWidth(1.0f);
            this.mAxisLinePaint.setStyle(Paint$Style.STROKE);
            (this.mLimitLinePaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        }
    }
    
    public void computeAxis(float n, float n2, final boolean b) {
        float n3 = n;
        float n4 = n2;
        if (this.mViewPortHandler != null) {
            n3 = n;
            n4 = n2;
            if (this.mViewPortHandler.contentWidth() > 10.0f) {
                n3 = n;
                n4 = n2;
                if (!this.mViewPortHandler.isFullyZoomedOutY()) {
                    final MPPointD valuesByTouchPoint = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
                    final MPPointD valuesByTouchPoint2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
                    if (!b) {
                        n2 = (float)valuesByTouchPoint2.y;
                        n = (float)valuesByTouchPoint.y;
                    }
                    else {
                        n2 = (float)valuesByTouchPoint.y;
                        n = (float)valuesByTouchPoint2.y;
                    }
                    MPPointD.recycleInstance(valuesByTouchPoint);
                    MPPointD.recycleInstance(valuesByTouchPoint2);
                    n4 = n;
                    n3 = n2;
                }
            }
        }
        this.computeAxisValues(n3, n4);
    }
    
    protected void computeAxisValues(float n, final float n2) {
        final int labelCount = this.mAxis.getLabelCount();
        final double v = Math.abs(n2 - n);
        if (labelCount != 0 && v > 0.0 && !Double.isInfinite(v)) {
            double a;
            final double n3 = a = Utils.roundToNextSignificant(v / labelCount);
            if (this.mAxis.isGranularityEnabled()) {
                a = n3;
                if (n3 < this.mAxis.getGranularity()) {
                    a = this.mAxis.getGranularity();
                }
            }
            final double n4 = Utils.roundToNextSignificant(Math.pow(10.0, (int)Math.log10(a)));
            double floor = a;
            if ((int)(a / n4) > 5) {
                floor = Math.floor(10.0 * n4);
            }
            int centerAxisLabelsEnabled = this.mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
            int n7;
            double a2;
            if (this.mAxis.isForceLabelsEnabled()) {
                final double n5 = (float)v / (labelCount - 1);
                if (this.mAxis.mEntries.length < (this.mAxis.mEntryCount = labelCount)) {
                    this.mAxis.mEntries = new float[labelCount];
                }
                int n6 = 0;
                while (true) {
                    n7 = labelCount;
                    a2 = n5;
                    if (n6 >= labelCount) {
                        break;
                    }
                    this.mAxis.mEntries[n6] = n;
                    n += (float)n5;
                    ++n6;
                }
            }
            else {
                double n8;
                if (floor == 0.0) {
                    n8 = 0.0;
                }
                else {
                    n8 = Math.ceil(n / floor) * floor;
                }
                double n9 = n8;
                if (this.mAxis.isCenterAxisLabelsEnabled()) {
                    n9 = n8 - floor;
                }
                double nextUp;
                if (floor == 0.0) {
                    nextUp = 0.0;
                }
                else {
                    nextUp = Utils.nextUp(Math.floor(n2 / floor) * floor);
                }
                int n10 = centerAxisLabelsEnabled;
                if (floor != 0.0) {
                    double n11 = n9;
                    while (true) {
                        n10 = centerAxisLabelsEnabled;
                        if (n11 > nextUp) {
                            break;
                        }
                        ++centerAxisLabelsEnabled;
                        n11 += floor;
                    }
                }
                final int mEntryCount = n10;
                if (this.mAxis.mEntries.length < (this.mAxis.mEntryCount = mEntryCount)) {
                    this.mAxis.mEntries = new float[mEntryCount];
                }
                int n12 = 0;
                while (true) {
                    n7 = mEntryCount;
                    a2 = floor;
                    if (n12 >= mEntryCount) {
                        break;
                    }
                    double n13 = n9;
                    if (n9 == 0.0) {
                        n13 = 0.0;
                    }
                    this.mAxis.mEntries[n12] = (float)n13;
                    n9 = n13 + floor;
                    ++n12;
                }
            }
            if (a2 < 1.0) {
                this.mAxis.mDecimals = (int)Math.ceil(-Math.log10(a2));
            }
            else {
                this.mAxis.mDecimals = 0;
            }
            if (this.mAxis.isCenterAxisLabelsEnabled()) {
                if (this.mAxis.mCenteredEntries.length < n7) {
                    this.mAxis.mCenteredEntries = new float[n7];
                }
                n = (float)a2 / 2.0f;
                for (int i = 0; i < n7; ++i) {
                    this.mAxis.mCenteredEntries[i] = this.mAxis.mEntries[i] + n;
                }
            }
            return;
        }
        this.mAxis.mEntries = new float[0];
        this.mAxis.mCenteredEntries = new float[0];
        this.mAxis.mEntryCount = 0;
    }
    
    public Paint getPaintAxisLabels() {
        return this.mAxisLabelPaint;
    }
    
    public Paint getPaintAxisLine() {
        return this.mAxisLinePaint;
    }
    
    public Paint getPaintGrid() {
        return this.mGridPaint;
    }
    
    public Transformer getTransformer() {
        return this.mTrans;
    }
    
    public abstract void renderAxisLabels(final Canvas p0);
    
    public abstract void renderAxisLine(final Canvas p0);
    
    public abstract void renderGridLines(final Canvas p0);
    
    public abstract void renderLimitLines(final Canvas p0);
}
