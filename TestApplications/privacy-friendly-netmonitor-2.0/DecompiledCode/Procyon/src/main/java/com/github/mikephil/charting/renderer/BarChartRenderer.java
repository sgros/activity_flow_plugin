// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.formatter.IValueFormatter;
import java.util.List;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import android.graphics.RectF;
import com.github.mikephil.charting.buffer.BarBuffer;
import android.graphics.Paint;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer
{
    protected Paint mBarBorderPaint;
    protected BarBuffer[] mBarBuffers;
    protected RectF mBarRect;
    private RectF mBarShadowRectBuffer;
    protected BarDataProvider mChart;
    protected Paint mShadowPaint;
    
    public BarChartRenderer(final BarDataProvider mChart, final ChartAnimator chartAnimator, final ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mBarRect = new RectF();
        this.mBarShadowRectBuffer = new RectF();
        this.mChart = mChart;
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint$Style.FILL);
        this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        this.mHighlightPaint.setAlpha(120);
        (this.mShadowPaint = new Paint(1)).setStyle(Paint$Style.FILL);
        (this.mBarBorderPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
    }
    
    @Override
    public void drawData(final Canvas canvas) {
        final BarData barData = this.mChart.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); ++i) {
            final IBarDataSet set = barData.getDataSetByIndex(i);
            if (set.isVisible()) {
                this.drawDataSet(canvas, set, i);
            }
        }
    }
    
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
                this.mBarShadowRectBuffer.left = x - n3;
                this.mBarShadowRectBuffer.right = x + n3;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsLeft(this.mBarShadowRectBuffer.right)) {
                    if (!this.mViewPortHandler.isInBoundsRight(this.mBarShadowRectBuffer.left)) {
                        break;
                    }
                    this.mBarShadowRectBuffer.top = this.mViewPortHandler.contentTop();
                    this.mBarShadowRectBuffer.bottom = this.mViewPortHandler.contentBottom();
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
            final int n4 = j + 2;
            if (mViewPortHandler.isInBoundsLeft(buffer[n4])) {
                if (!this.mViewPortHandler.isInBoundsRight(barBuffer.buffer[j])) {
                    break;
                }
                if (dataSet == 0) {
                    this.mRenderPaint.setColor(set.getColor(j / 4));
                }
                final float n5 = barBuffer.buffer[j];
                final float[] buffer2 = barBuffer.buffer;
                final int n6 = j + 1;
                final float n7 = buffer2[n6];
                final float n8 = barBuffer.buffer[n4];
                final float[] buffer3 = barBuffer.buffer;
                final int n9 = j + 3;
                canvas.drawRect(n5, n7, n8, buffer3[n9], this.mRenderPaint);
                if (b) {
                    canvas.drawRect(barBuffer.buffer[j], barBuffer.buffer[n6], barBuffer.buffer[n4], barBuffer.buffer[n9], this.mBarBorderPaint);
                }
            }
            j += 4;
        }
    }
    
    @Override
    public void drawExtras(final Canvas canvas) {
    }
    
    @Override
    public void drawHighlighted(final Canvas canvas, final Highlight[] array) {
        final BarData barData = this.mChart.getBarData();
        for (final Highlight highlight : array) {
            final IBarDataSet set = barData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final BarEntry barEntry = set.getEntryForXValue(highlight.getX(), highlight.getY());
                    if (this.isInBoundsX(barEntry, set)) {
                        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                        this.mHighlightPaint.setColor(set.getHighLightColor());
                        this.mHighlightPaint.setAlpha(set.getHighLightAlpha());
                        float from = 0.0f;
                        float to = 0.0f;
                        Label_0256: {
                            float n;
                            float n2;
                            if (highlight.getStackIndex() >= 0 && barEntry.isStacked()) {
                                if (!this.mChart.isHighlightFullBarEnabled()) {
                                    final Range range = barEntry.getRanges()[highlight.getStackIndex()];
                                    from = range.from;
                                    to = range.to;
                                    break Label_0256;
                                }
                                n = barEntry.getPositiveSum();
                                n2 = -barEntry.getNegativeSum();
                            }
                            else {
                                n = barEntry.getY();
                                n2 = 0.0f;
                            }
                            final float n3 = n;
                            to = n2;
                            from = n3;
                        }
                        this.prepareBarHighlight(barEntry.getX(), from, to, barData.getBarWidth() / 2.0f, transformer);
                        this.setHighlightDrawPos(highlight, this.mBarRect);
                        canvas.drawRect(this.mBarRect, this.mHighlightPaint);
                    }
                }
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas canvas) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            float convertDpToPixel = Utils.convertDpToPixel(4.5f);
            int drawValueAboveBarEnabled = this.mChart.isDrawValueAboveBarEnabled() ? 1 : 0;
            float n;
            List<IBarDataSet> list;
            for (int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i, dataSets = list, convertDpToPixel = n) {
                final IBarDataSet set = dataSets.get(i);
                if (!this.shouldDrawValues(set)) {
                    n = convertDpToPixel;
                    list = dataSets;
                }
                else {
                    this.applyValueTextStyle(set);
                    final boolean inverted = this.mChart.isInverted(set.getAxisDependency());
                    final float n2 = (float)Utils.calcTextHeight(this.mValuePaint, "8");
                    float n3;
                    if (drawValueAboveBarEnabled != 0) {
                        n3 = -convertDpToPixel;
                    }
                    else {
                        n3 = n2 + convertDpToPixel;
                    }
                    float n4;
                    if (drawValueAboveBarEnabled != 0) {
                        n4 = n2 + convertDpToPixel;
                    }
                    else {
                        n4 = -convertDpToPixel;
                    }
                    float n5 = n3;
                    float n6 = n4;
                    if (inverted) {
                        n5 = -n3 - n2;
                        n6 = -n4 - n2;
                    }
                    final BarBuffer barBuffer = this.mBarBuffers[i];
                    final float phaseY = this.mAnimator.getPhaseY();
                    final MPPointF instance = MPPointF.getInstance(set.getIconsOffset());
                    instance.x = Utils.convertDpToPixel(instance.x);
                    instance.y = Utils.convertDpToPixel(instance.y);
                    float n13 = 0.0f;
                    MPPointF mpPointF3 = null;
                    int n14 = 0;
                    if (!set.isStacked()) {
                        int n7 = 0;
                        list = dataSets;
                        final BarBuffer barBuffer2 = barBuffer;
                        final MPPointF mpPointF = instance;
                        while (n7 < barBuffer2.buffer.length * this.mAnimator.getPhaseX()) {
                            final float n8 = (barBuffer2.buffer[n7] + barBuffer2.buffer[n7 + 2]) / 2.0f;
                            if (!this.mViewPortHandler.isInBoundsRight(n8)) {
                                break;
                            }
                            final ViewPortHandler mViewPortHandler = this.mViewPortHandler;
                            final float[] buffer = barBuffer2.buffer;
                            final int n9 = n7 + 1;
                            if (mViewPortHandler.isInBoundsY(buffer[n9])) {
                                if (this.mViewPortHandler.isInBoundsLeft(n8)) {
                                    final int n10 = n7 / 4;
                                    final BarEntry barEntry = set.getEntryForIndex(n10);
                                    final float y = barEntry.getY();
                                    if (set.isDrawValuesEnabled()) {
                                        final IValueFormatter valueFormatter = set.getValueFormatter();
                                        float n11;
                                        if (y >= 0.0f) {
                                            n11 = barBuffer2.buffer[n9] + n5;
                                        }
                                        else {
                                            n11 = barBuffer2.buffer[n7 + 3] + n6;
                                        }
                                        this.drawValue(canvas, valueFormatter, y, barEntry, i, n8, n11, set.getValueTextColor(n10));
                                    }
                                    final MPPointF mpPointF2 = mpPointF;
                                    final BarBuffer barBuffer3 = barBuffer2;
                                    if (barEntry.getIcon() != null && set.isDrawIconsEnabled()) {
                                        final Drawable icon = barEntry.getIcon();
                                        float n12;
                                        if (y >= 0.0f) {
                                            n12 = barBuffer3.buffer[n9] + n5;
                                        }
                                        else {
                                            n12 = barBuffer3.buffer[n7 + 3] + n6;
                                        }
                                        Utils.drawImage(canvas, icon, (int)(n8 + mpPointF2.x), (int)(n12 + mpPointF2.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                    }
                                }
                            }
                            n7 += 4;
                        }
                        n13 = convertDpToPixel;
                        mpPointF3 = mpPointF;
                        n14 = i;
                    }
                    else {
                        final MPPointF mpPointF4 = instance;
                        final List<IBarDataSet> list2 = dataSets;
                        final Transformer transformer = this.mChart.getTransformer(set.getAxisDependency());
                        int n15 = 0;
                        int n16 = 0;
                        final int n17 = drawValueAboveBarEnabled;
                    Label_1038_Outer:
                        while (true) {
                            n13 = convertDpToPixel;
                            drawValueAboveBarEnabled = n17;
                            mpPointF3 = mpPointF4;
                            n14 = i;
                            list = list2;
                            if (n15 < set.getEntryCount() * this.mAnimator.getPhaseX()) {
                                final BarEntry barEntry2 = set.getEntryForIndex(n15);
                                final float[] yVals = barEntry2.getYVals();
                                final float n18 = (barBuffer.buffer[n16] + barBuffer.buffer[n16 + 2]) / 2.0f;
                                final int valueTextColor = set.getValueTextColor(n15);
                                while (true) {
                                    Label_1456: {
                                        if (yVals != null) {
                                            final float[] array = yVals;
                                            final float[] array2 = new float[array.length * 2];
                                            float n19 = -barEntry2.getNegativeSum();
                                            float n20 = 0.0f;
                                            float n24 = 0.0f;
                                            float n25 = 0.0f;
                                            for (int j = 0, n21 = 0; j < array2.length; j += 2, ++n21, n20 = n24, n19 = n25) {
                                                final float n22 = array[n21];
                                                float n23 = 0.0f;
                                                Label_1178: {
                                                    if (n22 == 0.0f) {
                                                        n23 = n22;
                                                        n24 = n20;
                                                        n25 = n19;
                                                        if (n20 == 0.0f) {
                                                            break Label_1178;
                                                        }
                                                        if (n19 == 0.0f) {
                                                            n23 = n22;
                                                            n24 = n20;
                                                            n25 = n19;
                                                            break Label_1178;
                                                        }
                                                    }
                                                    if (n22 >= 0.0f) {
                                                        n23 = (n24 = n20 + n22);
                                                        n25 = n19;
                                                    }
                                                    else {
                                                        n25 = n19 - n22;
                                                        n24 = n20;
                                                        n23 = n19;
                                                    }
                                                }
                                                array2[j + 1] = n23 * phaseY;
                                            }
                                            transformer.pointValuesToPixel(array2);
                                            for (int k = 0; k < array2.length; k += 2) {
                                                final int n26 = k / 2;
                                                final float n27 = array[n26];
                                                final boolean b = (n27 == 0.0f && n19 == 0.0f && n20 > 0.0f) || n27 < 0.0f;
                                                final float n28 = array2[k + 1];
                                                float n29;
                                                if (b) {
                                                    n29 = n6;
                                                }
                                                else {
                                                    n29 = n5;
                                                }
                                                final float n30 = n28 + n29;
                                                if (!this.mViewPortHandler.isInBoundsRight(n18)) {
                                                    break;
                                                }
                                                if (this.mViewPortHandler.isInBoundsY(n30)) {
                                                    if (this.mViewPortHandler.isInBoundsLeft(n18)) {
                                                        if (set.isDrawValuesEnabled()) {
                                                            this.drawValue(canvas, set.getValueFormatter(), array[n26], barEntry2, i, n18, n30, valueTextColor);
                                                        }
                                                        if (barEntry2.getIcon() != null && set.isDrawIconsEnabled()) {
                                                            final Drawable icon2 = barEntry2.getIcon();
                                                            Utils.drawImage(canvas, icon2, (int)(n18 + mpPointF4.x), (int)(n30 + mpPointF4.y), icon2.getIntrinsicWidth(), icon2.getIntrinsicHeight());
                                                        }
                                                    }
                                                }
                                            }
                                            break Label_1456;
                                        }
                                        if (!this.mViewPortHandler.isInBoundsRight(n18)) {
                                            n13 = convertDpToPixel;
                                            drawValueAboveBarEnabled = n17;
                                            mpPointF3 = mpPointF4;
                                            n14 = i;
                                            list = list2;
                                            break;
                                        }
                                        final ViewPortHandler mViewPortHandler2 = this.mViewPortHandler;
                                        final float[] buffer2 = barBuffer.buffer;
                                        final int n31 = n16 + 1;
                                        int n36;
                                        if (mViewPortHandler2.isInBoundsY(buffer2[n31]) && this.mViewPortHandler.isInBoundsLeft(n18)) {
                                            if (set.isDrawValuesEnabled()) {
                                                final IValueFormatter valueFormatter2 = set.getValueFormatter();
                                                final float y2 = barEntry2.getY();
                                                final float n32 = barBuffer.buffer[n31];
                                                float n33;
                                                if (barEntry2.getY() >= 0.0f) {
                                                    n33 = n5;
                                                }
                                                else {
                                                    n33 = n6;
                                                }
                                                this.drawValue(canvas, valueFormatter2, y2, barEntry2, i, n18, n32 + n33, valueTextColor);
                                            }
                                            if (barEntry2.getIcon() != null && set.isDrawIconsEnabled()) {
                                                final Drawable icon3 = barEntry2.getIcon();
                                                final float n34 = barBuffer.buffer[n31];
                                                float n35;
                                                if (barEntry2.getY() >= 0.0f) {
                                                    n35 = n5;
                                                }
                                                else {
                                                    n35 = n6;
                                                }
                                                Utils.drawImage(canvas, icon3, (int)(n18 + mpPointF4.x), (int)(n34 + n35 + mpPointF4.y), icon3.getIntrinsicWidth(), icon3.getIntrinsicHeight());
                                            }
                                            break Label_1456;
                                        }
                                        else {
                                            n36 = n16;
                                        }
                                        n16 = n36;
                                        continue Label_1038_Outer;
                                    }
                                    if (yVals == null) {
                                        final int n36 = n16 + 4;
                                    }
                                    else {
                                        final int n36 = n16 + 4 * yVals.length;
                                    }
                                    ++n15;
                                    continue;
                                }
                            }
                            break;
                        }
                    }
                    n = n13;
                    MPPointF.recycleInstance(mpPointF3);
                    i = n14;
                }
            }
        }
    }
    
    @Override
    public void initBuffers() {
        final BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new BarBuffer[barData.getDataSetCount()];
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
            mBarBuffers[i] = new BarBuffer(entryCount * 4 * stackSize, barData.getDataSetCount(), set.isStacked());
        }
    }
    
    protected void prepareBarHighlight(final float n, final float n2, final float n3, final float n4, final Transformer transformer) {
        this.mBarRect.set(n - n4, n2, n + n4, n3);
        transformer.rectToPixelPhase(this.mBarRect, this.mAnimator.getPhaseY());
    }
    
    protected void setHighlightDrawPos(final Highlight highlight, final RectF rectF) {
        highlight.setDraw(rectF.centerX(), rectF.top);
    }
}
