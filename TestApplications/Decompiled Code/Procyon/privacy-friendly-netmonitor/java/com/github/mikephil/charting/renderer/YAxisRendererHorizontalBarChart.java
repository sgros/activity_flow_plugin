// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import android.graphics.PathEffect;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.RectF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.MPPointD;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Path;

public class YAxisRendererHorizontalBarChart extends YAxisRenderer
{
    protected Path mDrawZeroLinePathBuffer;
    protected float[] mRenderLimitLinesBuffer;
    protected Path mRenderLimitLinesPathBuffer;
    
    public YAxisRendererHorizontalBarChart(final ViewPortHandler viewPortHandler, final YAxis yAxis, final Transformer transformer) {
        super(viewPortHandler, yAxis, transformer);
        this.mDrawZeroLinePathBuffer = new Path();
        this.mRenderLimitLinesPathBuffer = new Path();
        this.mRenderLimitLinesBuffer = new float[4];
        this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
    }
    
    @Override
    public void computeAxis(float n, float n2, final boolean b) {
        float n3 = n;
        float n4 = n2;
        if (this.mViewPortHandler.contentHeight() > 10.0f) {
            n3 = n;
            n4 = n2;
            if (!this.mViewPortHandler.isFullyZoomedOutX()) {
                final MPPointD valuesByTouchPoint = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
                final MPPointD valuesByTouchPoint2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
                if (!b) {
                    n2 = (float)valuesByTouchPoint.x;
                    n = (float)valuesByTouchPoint2.x;
                }
                else {
                    n2 = (float)valuesByTouchPoint2.x;
                    n = (float)valuesByTouchPoint.x;
                }
                MPPointD.recycleInstance(valuesByTouchPoint);
                MPPointD.recycleInstance(valuesByTouchPoint2);
                n4 = n;
                n3 = n2;
            }
        }
        this.computeAxisValues(n3, n4);
    }
    
    @Override
    protected void drawYLabels(final Canvas canvas, final float n, final float[] array, final float n2) {
        this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
        int i = (this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ true) ? 1 : 0;
        int mEntryCount;
        if (this.mYAxis.isDrawTopYLabelEntryEnabled()) {
            mEntryCount = this.mYAxis.mEntryCount;
        }
        else {
            mEntryCount = this.mYAxis.mEntryCount - 1;
        }
        while (i < mEntryCount) {
            canvas.drawText(this.mYAxis.getFormattedLabel(i), array[i * 2], n - n2, this.mAxisLabelPaint);
            ++i;
        }
    }
    
    @Override
    protected void drawZeroLine(final Canvas canvas) {
        final int save = canvas.save();
        this.mZeroLineClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mZeroLineClippingRect.inset(-this.mYAxis.getZeroLineWidth(), 0.0f);
        canvas.clipRect(this.mLimitLineClippingRect);
        final MPPointD pixelForValues = this.mTrans.getPixelForValues(0.0f, 0.0f);
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        final Path mDrawZeroLinePathBuffer = this.mDrawZeroLinePathBuffer;
        mDrawZeroLinePathBuffer.reset();
        mDrawZeroLinePathBuffer.moveTo((float)pixelForValues.x - 1.0f, this.mViewPortHandler.contentTop());
        mDrawZeroLinePathBuffer.lineTo((float)pixelForValues.x - 1.0f, this.mViewPortHandler.contentBottom());
        canvas.drawPath(mDrawZeroLinePathBuffer, this.mZeroLinePaint);
        canvas.restoreToCount(save);
    }
    
    @Override
    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(-this.mAxis.getGridLineWidth(), 0.0f);
        return this.mGridClippingRect;
    }
    
    @Override
    protected float[] getTransformedPositions() {
        if (this.mGetTransformedPositionsBuffer.length != this.mYAxis.mEntryCount * 2) {
            this.mGetTransformedPositionsBuffer = new float[this.mYAxis.mEntryCount * 2];
        }
        final float[] mGetTransformedPositionsBuffer = this.mGetTransformedPositionsBuffer;
        for (int i = 0; i < mGetTransformedPositionsBuffer.length; i += 2) {
            mGetTransformedPositionsBuffer[i] = this.mYAxis.mEntries[i / 2];
        }
        this.mTrans.pointValuesToPixel(mGetTransformedPositionsBuffer);
        return mGetTransformedPositionsBuffer;
    }
    
    @Override
    protected Path linePath(final Path path, final int n, final float[] array) {
        path.moveTo(array[n], this.mViewPortHandler.contentTop());
        path.lineTo(array[n], this.mViewPortHandler.contentBottom());
        return path;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            final float[] transformedPositions = this.getTransformedPositions();
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            this.mAxisLabelPaint.setTextAlign(Paint$Align.CENTER);
            final float convertDpToPixel = Utils.convertDpToPixel(2.5f);
            final float n = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
            final YAxis.AxisDependency axisDependency = this.mYAxis.getAxisDependency();
            final YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            float n2;
            if (axisDependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    n2 = this.mViewPortHandler.contentTop() - convertDpToPixel;
                }
                else {
                    n2 = this.mViewPortHandler.contentTop() - convertDpToPixel;
                }
            }
            else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                n2 = this.mViewPortHandler.contentBottom() + n + convertDpToPixel;
            }
            else {
                n2 = this.mViewPortHandler.contentBottom() + n + convertDpToPixel;
            }
            this.drawYLabels(canvas, n2, transformedPositions, this.mYAxis.getYOffset());
        }
    }
    
    @Override
    public void renderAxisLine(final Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
            }
            else {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
        final List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            final float[] mRenderLimitLinesBuffer = this.mRenderLimitLinesBuffer;
            mRenderLimitLinesBuffer[1] = (mRenderLimitLinesBuffer[0] = 0.0f);
            mRenderLimitLinesBuffer[3] = (mRenderLimitLinesBuffer[2] = 0.0f);
            final Path mRenderLimitLinesPathBuffer = this.mRenderLimitLinesPathBuffer;
            mRenderLimitLinesPathBuffer.reset();
            for (int i = 0; i < limitLines.size(); ++i) {
                final LimitLine limitLine = limitLines.get(i);
                if (limitLine.isEnabled()) {
                    final int save = canvas.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(-limitLine.getLineWidth(), 0.0f);
                    canvas.clipRect(this.mLimitLineClippingRect);
                    mRenderLimitLinesBuffer[0] = limitLine.getLimit();
                    mRenderLimitLinesBuffer[2] = limitLine.getLimit();
                    this.mTrans.pointValuesToPixel(mRenderLimitLinesBuffer);
                    mRenderLimitLinesBuffer[1] = this.mViewPortHandler.contentTop();
                    mRenderLimitLinesBuffer[3] = this.mViewPortHandler.contentBottom();
                    mRenderLimitLinesPathBuffer.moveTo(mRenderLimitLinesBuffer[0], mRenderLimitLinesBuffer[1]);
                    mRenderLimitLinesPathBuffer.lineTo(mRenderLimitLinesBuffer[2], mRenderLimitLinesBuffer[3]);
                    this.mLimitLinePaint.setStyle(Paint$Style.STROKE);
                    this.mLimitLinePaint.setColor(limitLine.getLineColor());
                    this.mLimitLinePaint.setPathEffect((PathEffect)limitLine.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
                    canvas.drawPath(mRenderLimitLinesPathBuffer, this.mLimitLinePaint);
                    mRenderLimitLinesPathBuffer.reset();
                    final String label = limitLine.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
                        this.mLimitLinePaint.setPathEffect((PathEffect)null);
                        this.mLimitLinePaint.setColor(limitLine.getTextColor());
                        this.mLimitLinePaint.setTypeface(limitLine.getTypeface());
                        this.mLimitLinePaint.setStrokeWidth(0.5f);
                        this.mLimitLinePaint.setTextSize(limitLine.getTextSize());
                        final float n = limitLine.getLineWidth() + limitLine.getXOffset();
                        final float n2 = Utils.convertDpToPixel(2.0f) + limitLine.getYOffset();
                        final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
                        if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            final float n3 = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                            this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                            canvas.drawText(label, mRenderLimitLinesBuffer[0] + n, this.mViewPortHandler.contentTop() + n2 + n3, this.mLimitLinePaint);
                        }
                        else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                            canvas.drawText(label, mRenderLimitLinesBuffer[0] + n, this.mViewPortHandler.contentBottom() - n2, this.mLimitLinePaint);
                        }
                        else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                            canvas.drawText(label, mRenderLimitLinesBuffer[0] - n, this.mViewPortHandler.contentTop() + n2 + Utils.calcTextHeight(this.mLimitLinePaint, label), this.mLimitLinePaint);
                        }
                        else {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                            canvas.drawText(label, mRenderLimitLinesBuffer[0] - n, this.mViewPortHandler.contentBottom() - n2, this.mLimitLinePaint);
                        }
                    }
                    canvas.restoreToCount(save);
                }
            }
        }
    }
}
