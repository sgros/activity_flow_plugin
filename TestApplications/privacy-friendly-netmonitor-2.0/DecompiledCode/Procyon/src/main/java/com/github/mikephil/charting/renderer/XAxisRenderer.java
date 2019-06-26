// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.PathEffect;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.components.XAxis;
import android.graphics.Path;
import android.graphics.RectF;

public class XAxisRenderer extends AxisRenderer
{
    protected RectF mGridClippingRect;
    protected RectF mLimitLineClippingRect;
    private Path mLimitLinePath;
    float[] mLimitLineSegmentsBuffer;
    protected float[] mRenderGridLinesBuffer;
    protected Path mRenderGridLinesPath;
    protected float[] mRenderLimitLinesBuffer;
    protected XAxis mXAxis;
    
    public XAxisRenderer(final ViewPortHandler viewPortHandler, final XAxis mxAxis, final Transformer transformer) {
        super(viewPortHandler, transformer, mxAxis);
        this.mRenderGridLinesPath = new Path();
        this.mRenderGridLinesBuffer = new float[2];
        this.mGridClippingRect = new RectF();
        this.mRenderLimitLinesBuffer = new float[2];
        this.mLimitLineClippingRect = new RectF();
        this.mLimitLineSegmentsBuffer = new float[4];
        this.mLimitLinePath = new Path();
        this.mXAxis = mxAxis;
        this.mAxisLabelPaint.setColor(-16777216);
        this.mAxisLabelPaint.setTextAlign(Paint$Align.CENTER);
        this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0f));
    }
    
    @Override
    public void computeAxis(float n, float n2, final boolean b) {
        float n3 = n;
        float n4 = n2;
        if (this.mViewPortHandler.contentWidth() > 10.0f) {
            n3 = n;
            n4 = n2;
            if (!this.mViewPortHandler.isFullyZoomedOutX()) {
                final MPPointD valuesByTouchPoint = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
                final MPPointD valuesByTouchPoint2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
                if (b) {
                    n2 = (float)valuesByTouchPoint2.x;
                    n = (float)valuesByTouchPoint.x;
                }
                else {
                    n2 = (float)valuesByTouchPoint.x;
                    n = (float)valuesByTouchPoint2.x;
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
    protected void computeAxisValues(final float n, final float n2) {
        super.computeAxisValues(n, n2);
        this.computeSize();
    }
    
    protected void computeSize() {
        final String longestLabel = this.mXAxis.getLongestLabel();
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        final FSize calcTextSize = Utils.calcTextSize(this.mAxisLabelPaint, longestLabel);
        final float width = calcTextSize.width;
        final float a = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
        final FSize sizeOfRotatedRectangleByDegrees = Utils.getSizeOfRotatedRectangleByDegrees(width, a, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(width);
        this.mXAxis.mLabelHeight = Math.round(a);
        this.mXAxis.mLabelRotatedWidth = Math.round(sizeOfRotatedRectangleByDegrees.width);
        this.mXAxis.mLabelRotatedHeight = Math.round(sizeOfRotatedRectangleByDegrees.height);
        FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
        FSize.recycleInstance(calcTextSize);
    }
    
    protected void drawGridLine(final Canvas canvas, final float n, final float n2, final Path path) {
        path.moveTo(n, this.mViewPortHandler.contentBottom());
        path.lineTo(n, this.mViewPortHandler.contentTop());
        canvas.drawPath(path, this.mGridPaint);
        path.reset();
    }
    
    protected void drawLabel(final Canvas canvas, final String s, final float n, final float n2, final MPPointF mpPointF, final float n3) {
        Utils.drawXAxisValue(canvas, s, n, n2, this.mAxisLabelPaint, mpPointF, n3);
    }
    
    protected void drawLabels(final Canvas canvas, final float n, final MPPointF mpPointF) {
        final float labelRotationAngle = this.mXAxis.getLabelRotationAngle();
        final boolean centerAxisLabelsEnabled = this.mXAxis.isCenterAxisLabelsEnabled();
        final float[] array = new float[this.mXAxis.mEntryCount * 2];
        for (int i = 0; i < array.length; i += 2) {
            if (centerAxisLabelsEnabled) {
                array[i] = this.mXAxis.mCenteredEntries[i / 2];
            }
            else {
                array[i] = this.mXAxis.mEntries[i / 2];
            }
        }
        this.mTrans.pointValuesToPixel(array);
        for (int j = 0; j < array.length; j += 2) {
            final float n2 = array[j];
            if (this.mViewPortHandler.isInBoundsX(n2)) {
                final String formattedValue = this.mXAxis.getValueFormatter().getFormattedValue(this.mXAxis.mEntries[j / 2], this.mXAxis);
                float n3 = n2;
                if (this.mXAxis.isAvoidFirstLastClippingEnabled()) {
                    if (j == this.mXAxis.mEntryCount - 1 && this.mXAxis.mEntryCount > 1) {
                        final float n4 = (float)Utils.calcTextWidth(this.mAxisLabelPaint, formattedValue);
                        n3 = n2;
                        if (n4 > this.mViewPortHandler.offsetRight() * 2.0f) {
                            n3 = n2;
                            if (n2 + n4 > this.mViewPortHandler.getChartWidth()) {
                                n3 = n2 - n4 / 2.0f;
                            }
                        }
                    }
                    else {
                        n3 = n2;
                        if (j == 0) {
                            n3 = n2 + Utils.calcTextWidth(this.mAxisLabelPaint, formattedValue) / 2.0f;
                        }
                    }
                }
                this.drawLabel(canvas, formattedValue, n3, n, mpPointF, labelRotationAngle);
            }
        }
    }
    
    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(-this.mAxis.getGridLineWidth(), 0.0f);
        return this.mGridClippingRect;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            final float yOffset = this.mXAxis.getYOffset();
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                instance.x = 0.5f;
                instance.y = 1.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentTop() - yOffset, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
                instance.x = 0.5f;
                instance.y = 1.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentTop() + yOffset + this.mXAxis.mLabelRotatedHeight, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                instance.x = 0.5f;
                instance.y = 0.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentBottom() + yOffset, instance);
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
                instance.x = 0.5f;
                instance.y = 0.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentBottom() - yOffset - this.mXAxis.mLabelRotatedHeight, instance);
            }
            else {
                instance.x = 0.5f;
                instance.y = 1.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentTop() - yOffset, instance);
                instance.x = 0.5f;
                instance.y = 0.0f;
                this.drawLabels(canvas, this.mViewPortHandler.contentBottom() + yOffset, instance);
            }
            MPPointF.recycleInstance(instance);
        }
    }
    
    @Override
    public void renderAxisLine(final Canvas canvas) {
        if (this.mXAxis.isDrawAxisLineEnabled() && this.mXAxis.isEnabled()) {
            this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
            this.mAxisLinePaint.setPathEffect((PathEffect)this.mXAxis.getAxisLineDashPathEffect());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
            }
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
        }
    }
    
    @Override
    public void renderGridLines(final Canvas canvas) {
        if (this.mXAxis.isDrawGridLinesEnabled() && this.mXAxis.isEnabled()) {
            final int save = canvas.save();
            canvas.clipRect(this.getGridClippingRect());
            if (this.mRenderGridLinesBuffer.length != this.mAxis.mEntryCount * 2) {
                this.mRenderGridLinesBuffer = new float[this.mXAxis.mEntryCount * 2];
            }
            final float[] mRenderGridLinesBuffer = this.mRenderGridLinesBuffer;
            final int n = 0;
            for (int i = 0; i < mRenderGridLinesBuffer.length; i += 2) {
                final float[] mEntries = this.mXAxis.mEntries;
                final int n2 = i / 2;
                mRenderGridLinesBuffer[i] = mEntries[n2];
                mRenderGridLinesBuffer[i + 1] = this.mXAxis.mEntries[n2];
            }
            this.mTrans.pointValuesToPixel(mRenderGridLinesBuffer);
            this.setupGridPaint();
            final Path mRenderGridLinesPath = this.mRenderGridLinesPath;
            mRenderGridLinesPath.reset();
            for (int j = n; j < mRenderGridLinesBuffer.length; j += 2) {
                this.drawGridLine(canvas, mRenderGridLinesBuffer[j], mRenderGridLinesBuffer[j + 1], mRenderGridLinesPath);
            }
            canvas.restoreToCount(save);
        }
    }
    
    public void renderLimitLineLabel(final Canvas canvas, final LimitLine limitLine, final float[] array, final float n) {
        final String label = limitLine.getLabel();
        if (label != null && !label.equals("")) {
            this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
            this.mLimitLinePaint.setPathEffect((PathEffect)null);
            this.mLimitLinePaint.setColor(limitLine.getTextColor());
            this.mLimitLinePaint.setStrokeWidth(0.5f);
            this.mLimitLinePaint.setTextSize(limitLine.getTextSize());
            final float n2 = limitLine.getLineWidth() + limitLine.getXOffset();
            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                final float n3 = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                canvas.drawText(label, array[0] + n2, this.mViewPortHandler.contentTop() + n + n3, this.mLimitLinePaint);
            }
            else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                this.mLimitLinePaint.setTextAlign(Paint$Align.LEFT);
                canvas.drawText(label, array[0] + n2, this.mViewPortHandler.contentBottom() - n, this.mLimitLinePaint);
            }
            else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                canvas.drawText(label, array[0] - n2, this.mViewPortHandler.contentTop() + n + Utils.calcTextHeight(this.mLimitLinePaint, label), this.mLimitLinePaint);
            }
            else {
                this.mLimitLinePaint.setTextAlign(Paint$Align.RIGHT);
                canvas.drawText(label, array[0] - n2, this.mViewPortHandler.contentBottom() - n, this.mLimitLinePaint);
            }
        }
    }
    
    public void renderLimitLineLine(final Canvas canvas, final LimitLine limitLine, final float[] array) {
        this.mLimitLineSegmentsBuffer[0] = array[0];
        this.mLimitLineSegmentsBuffer[1] = this.mViewPortHandler.contentTop();
        this.mLimitLineSegmentsBuffer[2] = array[0];
        this.mLimitLineSegmentsBuffer[3] = this.mViewPortHandler.contentBottom();
        this.mLimitLinePath.reset();
        this.mLimitLinePath.moveTo(this.mLimitLineSegmentsBuffer[0], this.mLimitLineSegmentsBuffer[1]);
        this.mLimitLinePath.lineTo(this.mLimitLineSegmentsBuffer[2], this.mLimitLineSegmentsBuffer[3]);
        this.mLimitLinePaint.setStyle(Paint$Style.STROKE);
        this.mLimitLinePaint.setColor(limitLine.getLineColor());
        this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        this.mLimitLinePaint.setPathEffect((PathEffect)limitLine.getDashPathEffect());
        canvas.drawPath(this.mLimitLinePath, this.mLimitLinePaint);
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
        final List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            final float[] mRenderLimitLinesBuffer = this.mRenderLimitLinesBuffer;
            mRenderLimitLinesBuffer[1] = (mRenderLimitLinesBuffer[0] = 0.0f);
            for (int i = 0; i < limitLines.size(); ++i) {
                final LimitLine limitLine = limitLines.get(i);
                if (limitLine.isEnabled()) {
                    final int save = canvas.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(-limitLine.getLineWidth(), 0.0f);
                    canvas.clipRect(this.mLimitLineClippingRect);
                    mRenderLimitLinesBuffer[0] = limitLine.getLimit();
                    mRenderLimitLinesBuffer[1] = 0.0f;
                    this.mTrans.pointValuesToPixel(mRenderLimitLinesBuffer);
                    this.renderLimitLineLine(canvas, limitLine, mRenderLimitLinesBuffer);
                    this.renderLimitLineLabel(canvas, limitLine, mRenderLimitLinesBuffer, 2.0f + limitLine.getYOffset());
                    canvas.restoreToCount(save);
                }
            }
        }
    }
    
    protected void setupGridPaint() {
        this.mGridPaint.setColor(this.mXAxis.getGridColor());
        this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
        this.mGridPaint.setPathEffect((PathEffect)this.mXAxis.getGridDashPathEffect());
    }
}
