// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import android.graphics.Paint$Align;
import android.graphics.PathEffect;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.RectF;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Path;
import com.github.mikephil.charting.charts.BarChart;

public class XAxisRendererHorizontalBarChart extends XAxisRenderer
{
    protected BarChart mChart;
    protected Path mRenderLimitLinesPathBuffer;
    
    public XAxisRendererHorizontalBarChart(final ViewPortHandler viewPortHandler, final XAxis xAxis, final Transformer transformer, final BarChart mChart) {
        super(viewPortHandler, xAxis, transformer);
        this.mRenderLimitLinesPathBuffer = new Path();
        this.mChart = mChart;
    }
    
    @Override
    public void computeAxis(float n, float n2, final boolean b) {
        float n3 = n;
        float n4 = n2;
        if (this.mViewPortHandler.contentWidth() > 10.0f) {
            n3 = n;
            n4 = n2;
            if (!this.mViewPortHandler.isFullyZoomedOutY()) {
                final MPPointD valuesByTouchPoint = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
                final MPPointD valuesByTouchPoint2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
                if (b) {
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
        this.computeAxisValues(n3, n4);
    }
    
    @Override
    protected void computeSize() {
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        final FSize calcTextSize = Utils.calcTextSize(this.mAxisLabelPaint, this.mXAxis.getLongestLabel());
        final float a = (float)(int)(calcTextSize.width + this.mXAxis.getXOffset() * 3.5f);
        final float height = calcTextSize.height;
        final FSize sizeOfRotatedRectangleByDegrees = Utils.getSizeOfRotatedRectangleByDegrees(calcTextSize.width, height, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(a);
        this.mXAxis.mLabelHeight = Math.round(height);
        this.mXAxis.mLabelRotatedWidth = (int)(sizeOfRotatedRectangleByDegrees.width + this.mXAxis.getXOffset() * 3.5f);
        this.mXAxis.mLabelRotatedHeight = Math.round(sizeOfRotatedRectangleByDegrees.height);
        FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
    }
    
    @Override
    protected void drawGridLine(final Canvas canvas, final float n, final float n2, final Path path) {
        path.moveTo(this.mViewPortHandler.contentRight(), n2);
        path.lineTo(this.mViewPortHandler.contentLeft(), n2);
        canvas.drawPath(path, this.mGridPaint);
        path.reset();
    }
    
    @Override
    protected void drawLabels(final Canvas canvas, final float n, final MPPointF mpPointF) {
        final float labelRotationAngle = this.mXAxis.getLabelRotationAngle();
        final boolean centerAxisLabelsEnabled = this.mXAxis.isCenterAxisLabelsEnabled();
        final float[] array = new float[this.mXAxis.mEntryCount * 2];
        for (int i = 0; i < array.length; i += 2) {
            if (centerAxisLabelsEnabled) {
                array[i + 1] = this.mXAxis.mCenteredEntries[i / 2];
            }
            else {
                array[i + 1] = this.mXAxis.mEntries[i / 2];
            }
        }
        this.mTrans.pointValuesToPixel(array);
        for (int j = 0; j < array.length; j += 2) {
            final float n2 = array[j + 1];
            if (this.mViewPortHandler.isInBoundsY(n2)) {
                this.drawLabel(canvas, this.mXAxis.getValueFormatter().getFormattedValue(this.mXAxis.mEntries[j / 2], this.mXAxis), n, n2, mpPointF, labelRotationAngle);
            }
        }
    }
    
    @Override
    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(0.0f, -this.mAxis.getGridLineWidth());
        return this.mGridClippingRect;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            final float xOffset = this.mXAxis.getXOffset();
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                instance.x = 0.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentRight() + xOffset, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
                instance.x = 1.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentRight() - xOffset, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                instance.x = 1.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentLeft() - xOffset, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
                instance.x = 1.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentLeft() + xOffset, instance);
            }
            else {
                instance.x = 0.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentRight() + xOffset, instance);
                instance.x = 1.0f;
                instance.y = 0.5f;
                this.drawLabels(canvas, this.mViewPortHandler.contentLeft() - xOffset, instance);
            }
            MPPointF.recycleInstance(instance);
        }
    }
    
    @Override
    public void renderAxisLine(final Canvas canvas) {
        if (this.mXAxis.isDrawAxisLineEnabled() && this.mXAxis.isEnabled()) {
            this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                canvas.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
        final List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            final float[] mRenderLimitLinesBuffer = this.mRenderLimitLinesBuffer;
            int i = 0;
            mRenderLimitLinesBuffer[1] = (mRenderLimitLinesBuffer[0] = 0.0f);
            final Path mRenderLimitLinesPathBuffer = this.mRenderLimitLinesPathBuffer;
            mRenderLimitLinesPathBuffer.reset();
            while (i < limitLines.size()) {
                final LimitLine limitLine = limitLines.get(i);
                if (limitLine.isEnabled()) {
                    final int save = canvas.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(0.0f, -limitLine.getLineWidth());
                    canvas.clipRect(this.mLimitLineClippingRect);
                    this.mLimitLinePaint.setStyle(Paint$Style.STROKE);
                    this.mLimitLinePaint.setColor(limitLine.getLineColor());
                    this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
                    this.mLimitLinePaint.setPathEffect((PathEffect)limitLine.getDashPathEffect());
                    mRenderLimitLinesBuffer[1] = limitLine.getLimit();
                    this.mTrans.pointValuesToPixel(mRenderLimitLinesBuffer);
                    mRenderLimitLinesPathBuffer.moveTo(this.mViewPortHandler.contentLeft(), mRenderLimitLinesBuffer[1]);
                    mRenderLimitLinesPathBuffer.lineTo(this.mViewPortHandler.contentRight(), mRenderLimitLinesBuffer[1]);
                    canvas.drawPath(mRenderLimitLinesPathBuffer, this.mLimitLinePaint);
                    mRenderLimitLinesPathBuffer.reset();
                    final String label = limitLine.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
                        this.mLimitLinePaint.setPathEffect((PathEffect)null);
                        this.mLimitLinePaint.setColor(limitLine.getTextColor());
                        this.mLimitLinePaint.setStrokeWidth(0.5f);
                        this.mLimitLinePaint.setTextSize(limitLine.getTextSize());
                        final float n = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                        final float n2 = Utils.convertDpToPixel(4.0f) + limitLine.getXOffset();
                        final float n3 = limitLine.getLineWidth() + n + limitLine.getYOffset();
                        final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
                        if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                            canvas.drawText(label, this.mViewPortHandler.contentRight() - n2, mRenderLimitLinesBuffer[1] - n3 + n, this.mLimitLinePaint);
                        }
                        else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                            canvas.drawText(label, this.mViewPortHandler.contentRight() - n2, mRenderLimitLinesBuffer[1] + n3, this.mLimitLinePaint);
                        }
                        else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                            canvas.drawText(label, this.mViewPortHandler.contentLeft() + n2, mRenderLimitLinesBuffer[1] - n3 + n, this.mLimitLinePaint);
                        }
                        else {
                            this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                            canvas.drawText(label, this.mViewPortHandler.offsetLeft() + n2, mRenderLimitLinesBuffer[1] + n3, this.mLimitLinePaint);
                        }
                    }
                    canvas.restoreToCount(save);
                }
                ++i;
            }
        }
    }
}
