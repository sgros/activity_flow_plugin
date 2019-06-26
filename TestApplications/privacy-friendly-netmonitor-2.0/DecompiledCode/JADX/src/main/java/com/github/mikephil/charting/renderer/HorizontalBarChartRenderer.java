package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class HorizontalBarChartRenderer extends BarChartRenderer {
    private RectF mBarShadowRectBuffer = new RectF();

    public HorizontalBarChartRenderer(BarDataProvider barDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(barDataProvider, chartAnimator, viewPortHandler);
        this.mValuePaint.setTextAlign(Align.LEFT);
    }

    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new HorizontalBarBuffer((iBarDataSet.getEntryCount() * 4) * (iBarDataSet.isStacked() ? iBarDataSet.getStackSize() : 1), barData.getDataSetCount(), iBarDataSet.isStacked());
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawDataSet(Canvas canvas, IBarDataSet iBarDataSet, int i) {
        int min;
        Canvas canvas2;
        IBarDataSet iBarDataSet2 = iBarDataSet;
        int i2 = i;
        Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
        this.mBarBorderPaint.setColor(iBarDataSet.getBarBorderColor());
        this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(iBarDataSet.getBarBorderWidth()));
        int i3 = 0;
        int i4 = 1;
        int i5 = iBarDataSet.getBarBorderWidth() > 0.0f ? 1 : 0;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        if (this.mChart.isDrawBarShadowEnabled()) {
            this.mShadowPaint.setColor(iBarDataSet.getBarShadowColor());
            float barWidth = this.mChart.getBarData().getBarWidth() / 2.0f;
            min = Math.min((int) Math.ceil((double) (((float) iBarDataSet.getEntryCount()) * phaseX)), iBarDataSet.getEntryCount());
            for (int i6 = 0; i6 < min; i6++) {
                float x = ((BarEntry) iBarDataSet2.getEntryForIndex(i6)).getX();
                this.mBarShadowRectBuffer.top = x - barWidth;
                this.mBarShadowRectBuffer.bottom = x + barWidth;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (!this.mViewPortHandler.isInBoundsTop(this.mBarShadowRectBuffer.bottom)) {
                    canvas2 = canvas;
                } else if (!this.mViewPortHandler.isInBoundsBottom(this.mBarShadowRectBuffer.top)) {
                    break;
                } else {
                    this.mBarShadowRectBuffer.left = this.mViewPortHandler.contentLeft();
                    this.mBarShadowRectBuffer.right = this.mViewPortHandler.contentRight();
                    canvas.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
                }
            }
        }
        canvas2 = canvas;
        BarBuffer barBuffer = this.mBarBuffers[i2];
        barBuffer.setPhases(phaseX, phaseY);
        barBuffer.setDataSet(i2);
        barBuffer.setInverted(this.mChart.isInverted(iBarDataSet.getAxisDependency()));
        barBuffer.setBarWidth(this.mChart.getBarData().getBarWidth());
        barBuffer.feed(iBarDataSet2);
        transformer.pointValuesToPixel(barBuffer.buffer);
        if (iBarDataSet.getColors().size() != 1) {
            i4 = 0;
        }
        if (i4 != 0) {
            this.mRenderPaint.setColor(iBarDataSet.getColor());
        }
        while (i3 < barBuffer.size()) {
            int i7 = i3 + 3;
            if (this.mViewPortHandler.isInBoundsTop(barBuffer.buffer[i7])) {
                int i8 = i3 + 1;
                if (this.mViewPortHandler.isInBoundsBottom(barBuffer.buffer[i8])) {
                    if (i4 == 0) {
                        this.mRenderPaint.setColor(iBarDataSet2.getColor(i3 / 4));
                    }
                    min = i3 + 2;
                    canvas2.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i8], barBuffer.buffer[min], barBuffer.buffer[i7], this.mRenderPaint);
                    if (i5 != 0) {
                        canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i8], barBuffer.buffer[min], barBuffer.buffer[i7], this.mBarBorderPaint);
                    }
                }
                i3 += 4;
                canvas2 = canvas;
            } else {
                return;
            }
        }
    }

    public void drawValues(Canvas canvas) {
        if (isDrawingValuesAllowed(this.mChart)) {
            List dataSets = this.mChart.getBarData().getDataSets();
            float convertDpToPixel = Utils.convertDpToPixel(5.0f);
            boolean isDrawValueAboveBarEnabled = this.mChart.isDrawValueAboveBarEnabled();
            int i = 0;
            while (i < this.mChart.getBarData().getDataSetCount()) {
                List list;
                float f;
                boolean z;
                IBarDataSet iBarDataSet = (IBarDataSet) dataSets.get(i);
                if (shouldDrawValues(iBarDataSet)) {
                    MPPointF mPPointF;
                    boolean isInverted = this.mChart.isInverted(iBarDataSet.getAxisDependency());
                    applyValueTextStyle(iBarDataSet);
                    float f2 = 2.0f;
                    float calcTextHeight = ((float) Utils.calcTextHeight(this.mValuePaint, "10")) / 2.0f;
                    IValueFormatter valueFormatter = iBarDataSet.getValueFormatter();
                    BarBuffer barBuffer = this.mBarBuffers[i];
                    float phaseY = this.mAnimator.getPhaseY();
                    MPPointF instance = MPPointF.getInstance(iBarDataSet.getIconsOffset());
                    instance.f488x = Utils.convertDpToPixel(instance.f488x);
                    instance.f489y = Utils.convertDpToPixel(instance.f489y);
                    boolean z2;
                    float f3;
                    BarBuffer barBuffer2;
                    IValueFormatter iValueFormatter;
                    float calcTextWidth;
                    float f4;
                    float f5;
                    float f6;
                    Drawable icon;
                    int i2;
                    float f7;
                    int i3;
                    if (iBarDataSet.isStacked()) {
                        list = dataSets;
                        z2 = isInverted;
                        f3 = calcTextHeight;
                        mPPointF = instance;
                        barBuffer2 = barBuffer;
                        iValueFormatter = valueFormatter;
                        Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
                        int i4 = 0;
                        int i5 = 0;
                        while (((float) i4) < ((float) iBarDataSet.getEntryCount()) * this.mAnimator.getPhaseX()) {
                            int i6;
                            float[] fArr;
                            BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForIndex(i4);
                            int valueTextColor = iBarDataSet.getValueTextColor(i4);
                            float[] yVals = barEntry.getYVals();
                            String formattedValue;
                            BarEntry barEntry2;
                            if (yVals == null) {
                                int i7 = i5 + 1;
                                if (!this.mViewPortHandler.isInBoundsTop(barBuffer2.buffer[i7])) {
                                    break;
                                } else if (this.mViewPortHandler.isInBoundsX(barBuffer2.buffer[i5])) {
                                    if (this.mViewPortHandler.isInBoundsBottom(barBuffer2.buffer[i7])) {
                                        formattedValue = iValueFormatter.getFormattedValue(barEntry.getY(), barEntry, i, this.mViewPortHandler);
                                        calcTextWidth = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                        f4 = isDrawValueAboveBarEnabled ? convertDpToPixel : -(calcTextWidth + convertDpToPixel);
                                        f5 = isDrawValueAboveBarEnabled ? -(calcTextWidth + convertDpToPixel) : convertDpToPixel;
                                        if (z2) {
                                            f4 = (-f4) - calcTextWidth;
                                            f5 = (-f5) - calcTextWidth;
                                        }
                                        f6 = f4;
                                        float f8 = f5;
                                        if (iBarDataSet.isDrawValuesEnabled()) {
                                            i6 = i4;
                                            fArr = yVals;
                                            f = convertDpToPixel;
                                            barEntry2 = barEntry;
                                            drawValue(canvas, formattedValue, barBuffer2.buffer[i5 + 2] + (barEntry.getY() >= 0.0f ? f6 : f8), barBuffer2.buffer[i7] + f3, valueTextColor);
                                        } else {
                                            f = convertDpToPixel;
                                            i6 = i4;
                                            fArr = yVals;
                                            barEntry2 = barEntry;
                                        }
                                        if (barEntry2.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                            icon = barEntry2.getIcon();
                                            f4 = barBuffer2.buffer[i5 + 2];
                                            if (barEntry2.getY() < 0.0f) {
                                                f6 = f8;
                                            }
                                            Utils.drawImage(canvas, icon, (int) ((f4 + f6) + mPPointF.f488x), (int) (barBuffer2.buffer[i7] + mPPointF.f489y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                        }
                                    }
                                }
                            } else {
                                f = convertDpToPixel;
                                i6 = i4;
                                fArr = yVals;
                                barEntry2 = barEntry;
                                float[] fArr2 = new float[(fArr.length * 2)];
                                f6 = -barEntry2.getNegativeSum();
                                float f9 = 0.0f;
                                int i8 = 0;
                                i2 = 0;
                                while (i8 < fArr2.length) {
                                    float f10 = fArr[i2];
                                    if (!(f10 == 0.0f && (f9 == 0.0f || f6 == 0.0f))) {
                                        if (f10 >= 0.0f) {
                                            f10 = f9 + f10;
                                            f9 = f10;
                                        } else {
                                            float f11 = f6;
                                            f6 -= f10;
                                            f10 = f11;
                                        }
                                    }
                                    fArr2[i8] = f10 * phaseY;
                                    i8 += 2;
                                    i2++;
                                }
                                transformer.pointValuesToPixel(fArr2);
                                int i9 = 0;
                                while (i9 < fArr2.length) {
                                    float f12;
                                    calcTextWidth = fArr[i9 / 2];
                                    formattedValue = iValueFormatter.getFormattedValue(calcTextWidth, barEntry2, i, this.mViewPortHandler);
                                    f4 = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                    f5 = isDrawValueAboveBarEnabled ? f : -(f4 + f);
                                    if (isDrawValueAboveBarEnabled) {
                                        z = isDrawValueAboveBarEnabled;
                                        f12 = -(f4 + f);
                                    } else {
                                        z = isDrawValueAboveBarEnabled;
                                        f12 = f;
                                    }
                                    if (z2) {
                                        f5 = (-f5) - f4;
                                        f12 = (-f12) - f4;
                                    }
                                    Object obj = (!(calcTextWidth == 0.0f && f6 == 0.0f && f9 > 0.0f) && calcTextWidth >= 0.0f) ? null : 1;
                                    f4 = fArr2[i9];
                                    if (obj != null) {
                                        f5 = f12;
                                    }
                                    f12 = f4 + f5;
                                    f5 = (barBuffer2.buffer[i5 + 1] + barBuffer2.buffer[i5 + 3]) / 2.0f;
                                    if (!this.mViewPortHandler.isInBoundsTop(f5)) {
                                        break;
                                    }
                                    float[] fArr3;
                                    if (this.mViewPortHandler.isInBoundsX(f12) && this.mViewPortHandler.isInBoundsBottom(f5)) {
                                        if (iBarDataSet.isDrawValuesEnabled()) {
                                            f7 = f5;
                                            i3 = i9;
                                            fArr3 = fArr2;
                                            drawValue(canvas, formattedValue, f12, f5 + f3, valueTextColor);
                                        } else {
                                            f7 = f5;
                                            i3 = i9;
                                            fArr3 = fArr2;
                                        }
                                        if (barEntry2.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                            icon = barEntry2.getIcon();
                                            Utils.drawImage(canvas, icon, (int) (f12 + mPPointF.f488x), (int) (f7 + mPPointF.f489y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                        }
                                    } else {
                                        i3 = i9;
                                        fArr3 = fArr2;
                                    }
                                    i9 = i3 + 2;
                                    fArr2 = fArr3;
                                    isDrawValueAboveBarEnabled = z;
                                }
                            }
                            z = isDrawValueAboveBarEnabled;
                            i5 = fArr == null ? i5 + 4 : i5 + (4 * fArr.length);
                            i4 = i6 + 1;
                            convertDpToPixel = f;
                            isDrawValueAboveBarEnabled = z;
                        }
                    } else {
                        int i10 = 0;
                        while (((float) i10) < ((float) barBuffer.buffer.length) * this.mAnimator.getPhaseX()) {
                            i2 = i10 + 1;
                            float f13 = (barBuffer.buffer[i2] + barBuffer.buffer[i10 + 3]) / f2;
                            if (!this.mViewPortHandler.isInBoundsTop(barBuffer.buffer[i2])) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsX(barBuffer.buffer[i10]) && this.mViewPortHandler.isInBoundsBottom(barBuffer.buffer[i2])) {
                                String str;
                                IValueFormatter iValueFormatter2;
                                float f14;
                                BarEntry barEntry3 = (BarEntry) iBarDataSet.getEntryForIndex(i10 / 4);
                                f4 = barEntry3.getY();
                                String formattedValue2 = valueFormatter.getFormattedValue(f4, barEntry3, i, this.mViewPortHandler);
                                MPPointF mPPointF2 = instance;
                                f5 = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue2);
                                if (isDrawValueAboveBarEnabled) {
                                    str = formattedValue2;
                                    calcTextWidth = convertDpToPixel;
                                } else {
                                    str = formattedValue2;
                                    calcTextWidth = -(f5 + convertDpToPixel);
                                }
                                if (isDrawValueAboveBarEnabled) {
                                    iValueFormatter2 = valueFormatter;
                                    f14 = -(f5 + convertDpToPixel);
                                } else {
                                    iValueFormatter2 = valueFormatter;
                                    f14 = convertDpToPixel;
                                }
                                if (isInverted) {
                                    calcTextWidth = (-calcTextWidth) - f5;
                                    f14 = (-f14) - f5;
                                }
                                phaseY = calcTextWidth;
                                f6 = f14;
                                if (iBarDataSet.isDrawValuesEnabled()) {
                                    f7 = f4;
                                    i3 = i10;
                                    list = dataSets;
                                    mPPointF = mPPointF2;
                                    f3 = calcTextHeight;
                                    barBuffer2 = barBuffer;
                                    z2 = isInverted;
                                    iValueFormatter = iValueFormatter2;
                                    drawValue(canvas, str, (f4 >= 0.0f ? phaseY : f6) + barBuffer.buffer[i10 + 2], f13 + calcTextHeight, iBarDataSet.getValueTextColor(i10 / 2));
                                } else {
                                    f7 = f4;
                                    i3 = i10;
                                    list = dataSets;
                                    z2 = isInverted;
                                    f3 = calcTextHeight;
                                    mPPointF = mPPointF2;
                                    iValueFormatter = iValueFormatter2;
                                    barBuffer2 = barBuffer;
                                }
                                if (barEntry3.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                    icon = barEntry3.getIcon();
                                    f4 = barBuffer2.buffer[i3 + 2];
                                    if (f7 < 0.0f) {
                                        phaseY = f6;
                                    }
                                    Utils.drawImage(canvas, icon, (int) ((f4 + phaseY) + mPPointF.f488x), (int) (f13 + mPPointF.f489y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            } else {
                                i3 = i10;
                                list = dataSets;
                                z2 = isInverted;
                                f3 = calcTextHeight;
                                mPPointF = instance;
                                barBuffer2 = barBuffer;
                                iValueFormatter = valueFormatter;
                            }
                            i10 = i3 + 4;
                            instance = mPPointF;
                            valueFormatter = iValueFormatter;
                            barBuffer = barBuffer2;
                            dataSets = list;
                            calcTextHeight = f3;
                            isInverted = z2;
                            f2 = 2.0f;
                        }
                        list = dataSets;
                        mPPointF = instance;
                    }
                    f = convertDpToPixel;
                    z = isDrawValueAboveBarEnabled;
                    MPPointF.recycleInstance(mPPointF);
                } else {
                    list = dataSets;
                    f = convertDpToPixel;
                    z = isDrawValueAboveBarEnabled;
                }
                i++;
                dataSets = list;
                convertDpToPixel = f;
                isDrawValueAboveBarEnabled = z;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawValue(Canvas canvas, String str, float f, float f2, int i) {
        this.mValuePaint.setColor(i);
        canvas.drawText(str, f, f2, this.mValuePaint);
    }

    /* Access modifiers changed, original: protected */
    public void prepareBarHighlight(float f, float f2, float f3, float f4, Transformer transformer) {
        this.mBarRect.set(f2, f - f4, f3, f + f4);
        transformer.rectToPixelPhaseHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }

    /* Access modifiers changed, original: protected */
    public void setHighlightDrawPos(Highlight highlight, RectF rectF) {
        highlight.setDraw(rectF.centerY(), rectF.right);
    }

    /* Access modifiers changed, original: protected */
    public boolean isDrawingValuesAllowed(ChartInterface chartInterface) {
        return ((float) chartInterface.getData().getEntryCount()) < ((float) chartInterface.getMaxVisibleCount()) * this.mViewPortHandler.getScaleY();
    }
}
