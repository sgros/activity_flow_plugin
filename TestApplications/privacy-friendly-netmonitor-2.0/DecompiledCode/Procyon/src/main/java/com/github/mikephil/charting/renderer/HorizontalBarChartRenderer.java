// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.formatter.IValueFormatter;
import java.util.List;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.Canvas;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import android.graphics.RectF;

public class HorizontalBarChartRenderer extends BarChartRenderer
{
    private RectF mBarShadowRectBuffer;
    
    public HorizontalBarChartRenderer(final BarDataProvider barDataProvider, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(barDataProvider, chartAnimator, viewPortHandler);
        this.mBarShadowRectBuffer = new RectF();
        this.mValuePaint.setTextAlign(Paint$Align.LEFT);
    }
    
    @Override
    protected void drawDataSet(final Canvas canvas, final IBarDataSet set, int dataSet) {
        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
        this.mBarBorderPaint.setColor(set.getBarBorderColor());
        this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(set.getBarBorderWidth()));
        final float barBorderWidth = set.getBarBorderWidth();
        final int n = 0;
        final int n2 = 1;
        final boolean b = barBorderWidth > 0.0f;
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        if (this.mChart.isDrawBarShadowEnabled()) {
            this.mShadowPaint.setColor(set.getBarShadowColor());
            final float n3 = this.mChart.getBarData().getBarWidth() / 2.0f;
            for (int min = Math.min((int)Math.ceil(set.getEntryCount() * phaseX), set.getEntryCount()), i = 0; i < min; ++i) {
                final float x = set.getEntryForIndex(i).getX();
                this.mBarShadowRectBuffer.top = x - n3;
                this.mBarShadowRectBuffer.bottom = x + n3;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsTop(this.mBarShadowRectBuffer.bottom)) {
                    if (!this.mViewPortHandler.isInBoundsBottom(this.mBarShadowRectBuffer.top)) {
                        break;
                    }
                    this.mBarShadowRectBuffer.left = this.mViewPortHandler.contentLeft();
                    this.mBarShadowRectBuffer.right = this.mViewPortHandler.contentRight();
                    canvas.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
                }
            }
        }
        final BarBuffer barBuffer = this.mBarBuffers[dataSet];
        barBuffer.setPhases(phaseX, phaseY);
        barBuffer.setDataSet(dataSet);
        barBuffer.setInverted(this.mChart.isInverted(set.getAxisDependency()));
        barBuffer.setBarWidth(this.mChart.getBarData().getBarWidth());
        barBuffer.feed(set);
        transformer.pointValuesToPixel(barBuffer.buffer);
        if (set.getColors().size() == 1) {
            dataSet = n2;
        }
        else {
            dataSet = 0;
        }
        int j = n;
        if (dataSet != 0) {
            this.mRenderPaint.setColor(set.getColor());
            j = n;
        }
        while (j < barBuffer.size()) {
            final ViewPortHandler mViewPortHandler = this.mViewPortHandler;
            final float[] buffer = barBuffer.buffer;
            final int n4 = j + 3;
            if (!mViewPortHandler.isInBoundsTop(buffer[n4])) {
                break;
            }
            final ViewPortHandler mViewPortHandler2 = this.mViewPortHandler;
            final float[] buffer2 = barBuffer.buffer;
            final int n5 = j + 1;
            if (mViewPortHandler2.isInBoundsBottom(buffer2[n5])) {
                if (dataSet == 0) {
                    this.mRenderPaint.setColor(set.getColor(j / 4));
                }
                final float n6 = barBuffer.buffer[j];
                final float n7 = barBuffer.buffer[n5];
                final float[] buffer3 = barBuffer.buffer;
                final int n8 = j + 2;
                canvas.drawRect(n6, n7, buffer3[n8], barBuffer.buffer[n4], this.mRenderPaint);
                if (b) {
                    canvas.drawRect(barBuffer.buffer[j], barBuffer.buffer[n5], barBuffer.buffer[n8], barBuffer.buffer[n4], this.mBarBorderPaint);
                }
            }
            j += 4;
        }
    }
    
    protected void drawValue(final Canvas canvas, final String s, final float n, final float n2, final int color) {
        this.mValuePaint.setColor(color);
        canvas.drawText(s, n, n2, this.mValuePaint);
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            float convertDpToPixel = Utils.convertDpToPixel(5.0f);
            int drawValueAboveBarEnabled = this.mChart.isDrawValueAboveBarEnabled() ? 1 : 0;
            for (int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i) {
                final IBarDataSet set = dataSets.get(i);
                if (this.shouldDrawValues(set)) {
                    final boolean inverted = this.mChart.isInverted(set.getAxisDependency());
                    this.applyValueTextStyle(set);
                    final float n = Utils.calcTextHeight(this.mValuePaint, "10") / 2.0f;
                    final IValueFormatter valueFormatter = set.getValueFormatter();
                    final BarBuffer barBuffer = this.mBarBuffers[i];
                    final float phaseY = this.mAnimator.getPhaseY();
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    MPPointF mpPointF3;
                    float n15;
                    int n16;
                    if (!set.isStacked()) {
                        int n2 = 0;
                        final float n3 = n;
                        final boolean b = inverted;
                        final List<IBarDataSet> list = dataSets;
                        IValueFormatter valueFormatter2 = valueFormatter;
                        final BarBuffer barBuffer2 = barBuffer;
                        final MPPointF mpPointF = instance;
                        while (n2 < barBuffer2.buffer.length * this.mAnimator.getPhaseX()) {
                            final float[] buffer = barBuffer2.buffer;
                            final int n4 = n2 + 1;
                            final float n5 = (buffer[n4] + barBuffer2.buffer[n2 + 3]) / 2.0f;
                            if (!this.mViewPortHandler.isInBoundsTop(barBuffer2.buffer[n4])) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsX(barBuffer2.buffer[n2]) && this.mViewPortHandler.isInBoundsBottom(barBuffer2.buffer[n4])) {
                                final BarEntry barEntry = set.getEntryForIndex(n2 / 4);
                                final float y = barEntry.getY();
                                final String formattedValue = valueFormatter2.getFormattedValue(y, barEntry, i, this.mViewPortHandler);
                                final float n6 = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                float n7;
                                if (drawValueAboveBarEnabled != 0) {
                                    n7 = convertDpToPixel;
                                }
                                else {
                                    n7 = -(n6 + convertDpToPixel);
                                }
                                float n8;
                                if (drawValueAboveBarEnabled != 0) {
                                    n8 = -(n6 + convertDpToPixel);
                                }
                                else {
                                    n8 = convertDpToPixel;
                                }
                                float n9 = n7;
                                float n10 = n8;
                                if (b) {
                                    n9 = -n7 - n6;
                                    n10 = -n8 - n6;
                                }
                                float n11 = n9;
                                if (set.isDrawValuesEnabled()) {
                                    final float n12 = barBuffer2.buffer[n2 + 2];
                                    float n13;
                                    if (y >= 0.0f) {
                                        n13 = n11;
                                    }
                                    else {
                                        n13 = n10;
                                    }
                                    this.drawValue(canvas, formattedValue, n13 + n12, n5 + n3, set.getValueTextColor(n2 / 2));
                                }
                                final IValueFormatter valueFormatter3 = valueFormatter2;
                                final MPPointF mpPointF2 = mpPointF;
                                valueFormatter2 = valueFormatter3;
                                if (barEntry.getIcon() != null) {
                                    valueFormatter2 = valueFormatter3;
                                    if (set.isDrawIconsEnabled()) {
                                        final Drawable icon = barEntry.getIcon();
                                        final float n14 = barBuffer2.buffer[n2 + 2];
                                        if (y < 0.0f) {
                                            n11 = n10;
                                        }
                                        Utils.drawImage(canvas, icon, (int)(n14 + n11 + mpPointF2.x), (int)(n5 + mpPointF2.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                        valueFormatter2 = valueFormatter3;
                                    }
                                }
                            }
                            n2 += 4;
                        }
                        mpPointF3 = mpPointF;
                        n15 = convertDpToPixel;
                        n16 = drawValueAboveBarEnabled;
                        dataSets = list;
                    }
                    else {
                        final List<IBarDataSet> list2 = dataSets;
                        final MPPointF mpPointF4 = instance;
                        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                        int n17 = 0;
                        int n18 = 0;
                        while (true) {
                            mpPointF3 = mpPointF4;
                            n15 = convertDpToPixel;
                            n16 = drawValueAboveBarEnabled;
                            dataSets = list2;
                            if (n17 >= set.getEntryCount() * this.mAnimator.getPhaseX()) {
                                break;
                            }
                            final BarEntry barEntry2 = set.getEntryForIndex(n17);
                            final int valueTextColor = set.getValueTextColor(n17);
                            final float[] yVals = barEntry2.getYVals();
                            Label_1687: {
                                int n28;
                                if (yVals == null) {
                                    final ViewPortHandler mViewPortHandler = this.mViewPortHandler;
                                    final float[] buffer2 = barBuffer.buffer;
                                    final int n19 = n18 + 1;
                                    if (!mViewPortHandler.isInBoundsTop(buffer2[n19])) {
                                        mpPointF3 = mpPointF4;
                                        n15 = convertDpToPixel;
                                        n16 = drawValueAboveBarEnabled;
                                        dataSets = list2;
                                        break;
                                    }
                                    if (!this.mViewPortHandler.isInBoundsX(barBuffer.buffer[n18])) {
                                        continue;
                                    }
                                    if (!this.mViewPortHandler.isInBoundsBottom(barBuffer.buffer[n19])) {
                                        continue;
                                    }
                                    final String formattedValue2 = valueFormatter.getFormattedValue(barEntry2.getY(), barEntry2, i, this.mViewPortHandler);
                                    final float n20 = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue2);
                                    float n21;
                                    if (drawValueAboveBarEnabled != 0) {
                                        n21 = convertDpToPixel;
                                    }
                                    else {
                                        n21 = -(n20 + convertDpToPixel);
                                    }
                                    float n22;
                                    if (drawValueAboveBarEnabled != 0) {
                                        n22 = -(n20 + convertDpToPixel);
                                    }
                                    else {
                                        n22 = convertDpToPixel;
                                    }
                                    float n23 = n21;
                                    float n24 = n22;
                                    if (inverted) {
                                        n23 = -n21 - n20;
                                        n24 = -n22 - n20;
                                    }
                                    float n25 = n23;
                                    if (set.isDrawValuesEnabled()) {
                                        final float n26 = barBuffer.buffer[n18 + 2];
                                        float n27;
                                        if (barEntry2.getY() >= 0.0f) {
                                            n27 = n25;
                                        }
                                        else {
                                            n27 = n24;
                                        }
                                        this.drawValue(canvas, formattedValue2, n26 + n27, barBuffer.buffer[n19] + n, valueTextColor);
                                    }
                                    n28 = drawValueAboveBarEnabled;
                                    if (barEntry2.getIcon() != null) {
                                        n28 = drawValueAboveBarEnabled;
                                        if (set.isDrawIconsEnabled()) {
                                            final Drawable icon2 = barEntry2.getIcon();
                                            final float n29 = barBuffer.buffer[n18 + 2];
                                            if (barEntry2.getY() < 0.0f) {
                                                n25 = n24;
                                            }
                                            Utils.drawImage(canvas, icon2, (int)(n29 + n25 + mpPointF4.x), (int)(barBuffer.buffer[n19] + mpPointF4.y), icon2.getIntrinsicWidth(), icon2.getIntrinsicHeight());
                                            n28 = drawValueAboveBarEnabled;
                                        }
                                    }
                                }
                                else {
                                    final float n30 = convertDpToPixel;
                                    final float[] array = yVals;
                                    final float[] array2 = new float[array.length * 2];
                                    float n31 = -barEntry2.getNegativeSum();
                                    float n32 = 0.0f;
                                    float n36 = 0.0f;
                                    float n37 = 0.0f;
                                    for (int j = 0, n33 = 0; j < array2.length; j += 2, ++n33, n32 = n36, n31 = n37) {
                                        final float n34 = array[n33];
                                        float n35 = 0.0f;
                                        Label_1301: {
                                            if (n34 == 0.0f) {
                                                n35 = n34;
                                                n36 = n32;
                                                n37 = n31;
                                                if (n32 == 0.0f) {
                                                    break Label_1301;
                                                }
                                                if (n31 == 0.0f) {
                                                    n35 = n34;
                                                    n36 = n32;
                                                    n37 = n31;
                                                    break Label_1301;
                                                }
                                            }
                                            if (n34 >= 0.0f) {
                                                n35 = (n36 = n32 + n34);
                                                n37 = n31;
                                            }
                                            else {
                                                n37 = n31 - n34;
                                                n36 = n32;
                                                n35 = n31;
                                            }
                                        }
                                        array2[j] = n35 * phaseY;
                                    }
                                    transformer.pointValuesToPixel(array2);
                                    int n38 = 0;
                                    while (true) {
                                        n28 = drawValueAboveBarEnabled;
                                        if (n38 >= array2.length) {
                                            break;
                                        }
                                        final float n39 = array[n38 / 2];
                                        final String formattedValue3 = valueFormatter.getFormattedValue(n39, barEntry2, i, this.mViewPortHandler);
                                        final float n40 = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue3);
                                        float n41;
                                        if (drawValueAboveBarEnabled != 0) {
                                            n41 = n30;
                                        }
                                        else {
                                            n41 = -(n40 + n30);
                                        }
                                        float n42;
                                        if (drawValueAboveBarEnabled != 0) {
                                            n42 = -(n40 + n30);
                                        }
                                        else {
                                            n42 = n30;
                                        }
                                        float n43 = n41;
                                        float n44 = n42;
                                        if (inverted) {
                                            n43 = -n41 - n40;
                                            n44 = -n42 - n40;
                                        }
                                        final boolean b2 = (n39 == 0.0f && n31 == 0.0f && n32 > 0.0f) || n39 < 0.0f;
                                        final float n45 = array2[n38];
                                        if (b2) {
                                            n43 = n44;
                                        }
                                        final float n46 = n45 + n43;
                                        final float n47 = (barBuffer.buffer[n18 + 1] + barBuffer.buffer[n18 + 3]) / 2.0f;
                                        if (!this.mViewPortHandler.isInBoundsTop(n47)) {
                                            break Label_1687;
                                        }
                                        if (this.mViewPortHandler.isInBoundsX(n46) && this.mViewPortHandler.isInBoundsBottom(n47)) {
                                            if (set.isDrawValuesEnabled()) {
                                                this.drawValue(canvas, formattedValue3, n46, n47 + n, valueTextColor);
                                            }
                                            if (barEntry2.getIcon() != null && set.isDrawIconsEnabled()) {
                                                final Drawable icon3 = barEntry2.getIcon();
                                                Utils.drawImage(canvas, icon3, (int)(n46 + mpPointF4.x), (int)(n47 + mpPointF4.y), icon3.getIntrinsicWidth(), icon3.getIntrinsicHeight());
                                            }
                                        }
                                        n38 += 2;
                                    }
                                }
                                drawValueAboveBarEnabled = n28;
                            }
                            if (yVals == null) {
                                n18 += 4;
                            }
                            else {
                                n18 += 4 * yVals.length;
                            }
                            ++n17;
                        }
                    }
                    convertDpToPixel = n15;
                    drawValueAboveBarEnabled = n16;
                    MPPointF.recycleInstance(mpPointF3);
                }
            }
        }
    }
    
    @Override
    public void initBuffers() {
        final BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; ++i) {
            final IBarDataSet set = barData.getDataSetByIndex(i);
            final BarBuffer[] mBarBuffers = this.mBarBuffers;
            final int entryCount = set.getEntryCount();
            int stackSize;
            if (set.isStacked()) {
                stackSize = set.getStackSize();
            }
            else {
                stackSize = 1;
            }
            mBarBuffers[i] = new HorizontalBarBuffer(entryCount * 4 * stackSize, barData.getDataSetCount(), set.isStacked());
        }
    }
    
    @Override
    protected boolean isDrawingValuesAllowed(final ChartInterface chartInterface) {
        return chartInterface.getData().getEntryCount() < chartInterface.getMaxVisibleCount() * this.mViewPortHandler.getScaleY();
    }
    
    @Override
    protected void prepareBarHighlight(final float n, final float n2, final float n3, final float n4, final Transformer transformer) {
        this.mBarRect.set(n2, n - n4, n3, n + n4);
        transformer.rectToPixelPhaseHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }
    
    @Override
    protected void setHighlightDrawPos(final Highlight highlight, final RectF rectF) {
        highlight.setDraw(rectF.centerY(), rectF.right);
    }
}
