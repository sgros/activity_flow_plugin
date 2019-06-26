// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.PathEffect;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.MPPointD;
import android.graphics.Canvas;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.components.YAxis;
import android.graphics.RectF;
import android.graphics.Path;

public class YAxisRenderer extends AxisRenderer
{
    protected Path mDrawZeroLinePath;
    protected float[] mGetTransformedPositionsBuffer;
    protected RectF mGridClippingRect;
    protected RectF mLimitLineClippingRect;
    protected Path mRenderGridLinesPath;
    protected Path mRenderLimitLines;
    protected float[] mRenderLimitLinesBuffer;
    protected YAxis mYAxis;
    protected RectF mZeroLineClippingRect;
    protected Paint mZeroLinePaint;
    
    public YAxisRenderer(final ViewPortHandler viewPortHandler, final YAxis myAxis, final Transformer transformer) {
        super(viewPortHandler, transformer, myAxis);
        this.mRenderGridLinesPath = new Path();
        this.mGridClippingRect = new RectF();
        this.mGetTransformedPositionsBuffer = new float[2];
        this.mDrawZeroLinePath = new Path();
        this.mZeroLineClippingRect = new RectF();
        this.mRenderLimitLines = new Path();
        this.mRenderLimitLinesBuffer = new float[2];
        this.mLimitLineClippingRect = new RectF();
        this.mYAxis = myAxis;
        if (this.mViewPortHandler != null) {
            this.mAxisLabelPaint.setColor(-16777216);
            this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0f));
            (this.mZeroLinePaint = new Paint(1)).setColor(-7829368);
            this.mZeroLinePaint.setStrokeWidth(1.0f);
            this.mZeroLinePaint.setStyle(Paint$Style.STROKE);
        }
    }
    
    protected void drawYLabels(final Canvas canvas, final float n, final float[] array, final float n2) {
        int i = (this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ true) ? 1 : 0;
        int mEntryCount;
        if (this.mYAxis.isDrawTopYLabelEntryEnabled()) {
            mEntryCount = this.mYAxis.mEntryCount;
        }
        else {
            mEntryCount = this.mYAxis.mEntryCount - 1;
        }
        while (i < mEntryCount) {
            canvas.drawText(this.mYAxis.getFormattedLabel(i), n, array[i * 2 + 1] + n2, this.mAxisLabelPaint);
            ++i;
        }
    }
    
    protected void drawZeroLine(final Canvas canvas) {
        final int save = canvas.save();
        this.mZeroLineClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mZeroLineClippingRect.inset(0.0f, -this.mYAxis.getZeroLineWidth());
        canvas.clipRect(this.mZeroLineClippingRect);
        final MPPointD pixelForValues = this.mTrans.getPixelForValues(0.0f, 0.0f);
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        final Path mDrawZeroLinePath = this.mDrawZeroLinePath;
        mDrawZeroLinePath.reset();
        mDrawZeroLinePath.moveTo(this.mViewPortHandler.contentLeft(), (float)pixelForValues.y);
        mDrawZeroLinePath.lineTo(this.mViewPortHandler.contentRight(), (float)pixelForValues.y);
        canvas.drawPath(mDrawZeroLinePath, this.mZeroLinePaint);
        canvas.restoreToCount(save);
    }
    
    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(0.0f, -this.mAxis.getGridLineWidth());
        return this.mGridClippingRect;
    }
    
    protected float[] getTransformedPositions() {
        if (this.mGetTransformedPositionsBuffer.length != this.mYAxis.mEntryCount * 2) {
            this.mGetTransformedPositionsBuffer = new float[this.mYAxis.mEntryCount * 2];
        }
        final float[] mGetTransformedPositionsBuffer = this.mGetTransformedPositionsBuffer;
        for (int i = 0; i < mGetTransformedPositionsBuffer.length; i += 2) {
            mGetTransformedPositionsBuffer[i + 1] = this.mYAxis.mEntries[i / 2];
        }
        this.mTrans.pointValuesToPixel(mGetTransformedPositionsBuffer);
        return mGetTransformedPositionsBuffer;
    }
    
    protected Path linePath(final Path path, int n, final float[] array) {
        final float offsetLeft = this.mViewPortHandler.offsetLeft();
        ++n;
        path.moveTo(offsetLeft, array[n]);
        path.lineTo(this.mViewPortHandler.contentRight(), array[n]);
        return path;
    }
    
    @Override
    public void renderAxisLabels(final Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            final float[] transformedPositions = this.getTransformedPositions();
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            final float xOffset = this.mYAxis.getXOffset();
            final float n = Utils.calcTextHeight(this.mAxisLabelPaint, "A") / 2.5f;
            final float yOffset = this.mYAxis.getYOffset();
            final YAxis.AxisDependency axisDependency = this.mYAxis.getAxisDependency();
            final YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            float n2;
            if (axisDependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    this.mAxisLabelPaint.setTextAlign(Paint$Align.RIGHT);
                    n2 = this.mViewPortHandler.offsetLeft() - xOffset;
                }
                else {
                    this.mAxisLabelPaint.setTextAlign(Paint$Align.LEFT);
                    n2 = this.mViewPortHandler.offsetLeft() + xOffset;
                }
            }
            else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                this.mAxisLabelPaint.setTextAlign(Paint$Align.LEFT);
                n2 = this.mViewPortHandler.contentRight() + xOffset;
            }
            else {
                this.mAxisLabelPaint.setTextAlign(Paint$Align.RIGHT);
                n2 = this.mViewPortHandler.contentRight() - xOffset;
            }
            this.drawYLabels(canvas, n2, transformedPositions, n + yOffset);
        }
    }
    
    @Override
    public void renderAxisLine(final Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                canvas.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
            else {
                canvas.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
        }
    }
    
    @Override
    public void renderGridLines(final Canvas canvas) {
        if (!this.mYAxis.isEnabled()) {
            return;
        }
        if (this.mYAxis.isDrawGridLinesEnabled()) {
            final int save = canvas.save();
            canvas.clipRect(this.getGridClippingRect());
            final float[] transformedPositions = this.getTransformedPositions();
            this.mGridPaint.setColor(this.mYAxis.getGridColor());
            this.mGridPaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
            this.mGridPaint.setPathEffect((PathEffect)this.mYAxis.getGridDashPathEffect());
            final Path mRenderGridLinesPath = this.mRenderGridLinesPath;
            mRenderGridLinesPath.reset();
            for (int i = 0; i < transformedPositions.length; i += 2) {
                canvas.drawPath(this.linePath(mRenderGridLinesPath, i, transformedPositions), this.mGridPaint);
                mRenderGridLinesPath.reset();
            }
            canvas.restoreToCount(save);
        }
        if (this.mYAxis.isDrawZeroLineEnabled()) {
            this.drawZeroLine(canvas);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas canvas) {
        final List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            final float[] mRenderLimitLinesBuffer = this.mRenderLimitLinesBuffer;
            int i = 0;
            mRenderLimitLinesBuffer[1] = (mRenderLimitLinesBuffer[0] = 0.0f);
            final Path mRenderLimitLines = this.mRenderLimitLines;
            mRenderLimitLines.reset();
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
                    mRenderLimitLines.moveTo(this.mViewPortHandler.contentLeft(), mRenderLimitLinesBuffer[1]);
                    mRenderLimitLines.lineTo(this.mViewPortHandler.contentRight(), mRenderLimitLinesBuffer[1]);
                    canvas.drawPath(mRenderLimitLines, this.mLimitLinePaint);
                    mRenderLimitLines.reset();
                    final String label = limitLine.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
                        this.mLimitLinePaint.setPathEffect((PathEffect)null);
                        this.mLimitLinePaint.setColor(limitLine.getTextColor());
                        this.mLimitLinePaint.setTypeface(limitLine.getTypeface());
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
