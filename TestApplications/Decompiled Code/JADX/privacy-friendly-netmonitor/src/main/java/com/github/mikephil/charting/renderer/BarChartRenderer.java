package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {
    protected Paint mBarBorderPaint;
    protected BarBuffer[] mBarBuffers;
    protected RectF mBarRect = new RectF();
    private RectF mBarShadowRectBuffer = new RectF();
    protected BarDataProvider mChart;
    protected Paint mShadowPaint;

    public void drawExtras(Canvas canvas) {
    }

    public BarChartRenderer(BarDataProvider barDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mChart = barDataProvider;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Style.FILL);
        this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        this.mHighlightPaint.setAlpha(120);
        this.mShadowPaint = new Paint(1);
        this.mShadowPaint.setStyle(Style.FILL);
        this.mBarBorderPaint = new Paint(1);
        this.mBarBorderPaint.setStyle(Style.STROKE);
    }

    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new BarBuffer((iBarDataSet.getEntryCount() * 4) * (iBarDataSet.isStacked() ? iBarDataSet.getStackSize() : 1), barData.getDataSetCount(), iBarDataSet.isStacked());
        }
    }

    public void drawData(Canvas canvas) {
        BarData barData = this.mChart.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            if (iBarDataSet.isVisible()) {
                drawDataSet(canvas, iBarDataSet, i);
            }
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
                this.mBarShadowRectBuffer.left = x - barWidth;
                this.mBarShadowRectBuffer.right = x + barWidth;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (!this.mViewPortHandler.isInBoundsLeft(this.mBarShadowRectBuffer.right)) {
                    canvas2 = canvas;
                } else if (!this.mViewPortHandler.isInBoundsRight(this.mBarShadowRectBuffer.left)) {
                    break;
                } else {
                    this.mBarShadowRectBuffer.top = this.mViewPortHandler.contentTop();
                    this.mBarShadowRectBuffer.bottom = this.mViewPortHandler.contentBottom();
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
            int i7 = i3 + 2;
            if (this.mViewPortHandler.isInBoundsLeft(barBuffer.buffer[i7])) {
                if (this.mViewPortHandler.isInBoundsRight(barBuffer.buffer[i3])) {
                    if (i4 == 0) {
                        this.mRenderPaint.setColor(iBarDataSet2.getColor(i3 / 4));
                    }
                    int i8 = i3 + 1;
                    min = i3 + 3;
                    canvas2.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i8], barBuffer.buffer[i7], barBuffer.buffer[min], this.mRenderPaint);
                    if (i5 != 0) {
                        canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i8], barBuffer.buffer[i7], barBuffer.buffer[min], this.mBarBorderPaint);
                    }
                } else {
                    return;
                }
            }
            i3 += 4;
            canvas2 = canvas;
        }
    }

    /* Access modifiers changed, original: protected */
    public void prepareBarHighlight(float f, float f2, float f3, float f4, Transformer transformer) {
        this.mBarRect.set(f - f4, f2, f + f4, f3);
        transformer.rectToPixelPhase(this.mBarRect, this.mAnimator.getPhaseY());
    }

    /* JADX WARNING: Removed duplicated region for block: B:129:0x0373  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0370  */
    public void drawValues(android.graphics.Canvas r36) {
        /*
        r35 = this;
        r9 = r35;
        r0 = r9.mChart;
        r0 = r9.isDrawingValuesAllowed(r0);
        if (r0 == 0) goto L_0x0390;
    L_0x000a:
        r0 = r9.mChart;
        r0 = r0.getBarData();
        r10 = r0.getDataSets();
        r0 = 1083179008; // 0x40900000 float:4.5 double:5.35161536E-315;
        r11 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r0 = r9.mChart;
        r12 = r0.isDrawValueAboveBarEnabled();
        r14 = 0;
    L_0x0021:
        r0 = r9.mChart;
        r0 = r0.getBarData();
        r0 = r0.getDataSetCount();
        if (r14 >= r0) goto L_0x0390;
    L_0x002d:
        r0 = r10.get(r14);
        r15 = r0;
        r15 = (com.github.mikephil.charting.interfaces.datasets.IBarDataSet) r15;
        r0 = r9.shouldDrawValues(r15);
        if (r0 != 0) goto L_0x0044;
    L_0x003a:
        r27 = r10;
        r29 = r11;
        r30 = r12;
        r31 = r14;
        goto L_0x0386;
    L_0x0044:
        r9.applyValueTextStyle(r15);
        r0 = r9.mChart;
        r1 = r15.getAxisDependency();
        r0 = r0.isInverted(r1);
        r1 = r9.mValuePaint;
        r2 = "8";
        r1 = com.github.mikephil.charting.utils.Utils.calcTextHeight(r1, r2);
        r1 = (float) r1;
        if (r12 == 0) goto L_0x005e;
    L_0x005c:
        r2 = -r11;
        goto L_0x0060;
    L_0x005e:
        r2 = r1 + r11;
    L_0x0060:
        if (r12 == 0) goto L_0x0065;
    L_0x0062:
        r3 = r1 + r11;
        goto L_0x0066;
    L_0x0065:
        r3 = -r11;
    L_0x0066:
        if (r0 == 0) goto L_0x006e;
    L_0x0068:
        r0 = -r2;
        r2 = r0 - r1;
        r0 = -r3;
        r3 = r0 - r1;
    L_0x006e:
        r16 = r2;
        r17 = r3;
        r0 = r9.mBarBuffers;
        r8 = r0[r14];
        r0 = r9.mAnimator;
        r18 = r0.getPhaseY();
        r0 = r15.getIconsOffset();
        r7 = com.github.mikephil.charting.utils.MPPointF.getInstance(r0);
        r0 = r7.f488x;
        r0 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r7.f488x = r0;
        r0 = r7.f489y;
        r0 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r7.f489y = r0;
        r0 = r15.isStacked();
        r19 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r20 = 0;
        if (r0 != 0) goto L_0x0181;
    L_0x009e:
        r6 = 0;
    L_0x009f:
        r0 = (float) r6;
        r1 = r8.buffer;
        r1 = r1.length;
        r1 = (float) r1;
        r2 = r9.mAnimator;
        r2 = r2.getPhaseX();
        r1 = r1 * r2;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 >= 0) goto L_0x017c;
    L_0x00af:
        r0 = r8.buffer;
        r0 = r0[r6];
        r1 = r8.buffer;
        r2 = r6 + 2;
        r1 = r1[r2];
        r0 = r0 + r1;
        r5 = r0 / r19;
        r0 = r9.mViewPortHandler;
        r0 = r0.isInBoundsRight(r5);
        if (r0 != 0) goto L_0x00c6;
    L_0x00c4:
        goto L_0x017c;
    L_0x00c6:
        r0 = r9.mViewPortHandler;
        r1 = r8.buffer;
        r18 = r6 + 1;
        r1 = r1[r18];
        r0 = r0.isInBoundsY(r1);
        if (r0 == 0) goto L_0x016e;
    L_0x00d4:
        r0 = r9.mViewPortHandler;
        r0 = r0.isInBoundsLeft(r5);
        if (r0 != 0) goto L_0x00de;
    L_0x00dc:
        goto L_0x016e;
    L_0x00de:
        r0 = r6 / 4;
        r1 = r15.getEntryForIndex(r0);
        r4 = r1;
        r4 = (com.github.mikephil.charting.data.BarEntry) r4;
        r21 = r4.getY();
        r1 = r15.isDrawValuesEnabled();
        if (r1 == 0) goto L_0x012b;
    L_0x00f1:
        r2 = r15.getValueFormatter();
        r1 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1));
        if (r1 < 0) goto L_0x0102;
    L_0x00f9:
        r1 = r8.buffer;
        r1 = r1[r18];
        r1 = r1 + r16;
    L_0x00ff:
        r22 = r1;
        goto L_0x010b;
    L_0x0102:
        r1 = r8.buffer;
        r3 = r6 + 3;
        r1 = r1[r3];
        r1 = r1 + r17;
        goto L_0x00ff;
    L_0x010b:
        r23 = r15.getValueTextColor(r0);
        r0 = r9;
        r1 = r36;
        r3 = r21;
        r24 = r4;
        r25 = r5;
        r5 = r14;
        r26 = r6;
        r6 = r25;
        r13 = r7;
        r7 = r22;
        r27 = r10;
        r10 = r8;
        r8 = r23;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
        r1 = r24;
        goto L_0x0134;
    L_0x012b:
        r25 = r5;
        r26 = r6;
        r13 = r7;
        r27 = r10;
        r10 = r8;
        r1 = r4;
    L_0x0134:
        r0 = r1.getIcon();
        if (r0 == 0) goto L_0x0174;
    L_0x013a:
        r0 = r15.isDrawIconsEnabled();
        if (r0 == 0) goto L_0x0174;
    L_0x0140:
        r3 = r1.getIcon();
        r0 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1));
        if (r0 < 0) goto L_0x014f;
    L_0x0148:
        r0 = r10.buffer;
        r0 = r0[r18];
        r0 = r0 + r16;
        goto L_0x0157;
    L_0x014f:
        r0 = r10.buffer;
        r6 = r26 + 3;
        r0 = r0[r6];
        r0 = r0 + r17;
    L_0x0157:
        r1 = r13.f488x;
        r5 = r25 + r1;
        r1 = r13.f489y;
        r0 = r0 + r1;
        r4 = (int) r5;
        r5 = (int) r0;
        r6 = r3.getIntrinsicWidth();
        r7 = r3.getIntrinsicHeight();
        r2 = r36;
        com.github.mikephil.charting.utils.Utils.drawImage(r2, r3, r4, r5, r6, r7);
        goto L_0x0174;
    L_0x016e:
        r26 = r6;
        r13 = r7;
        r27 = r10;
        r10 = r8;
    L_0x0174:
        r6 = r26 + 4;
        r8 = r10;
        r7 = r13;
        r10 = r27;
        goto L_0x009f;
    L_0x017c:
        r13 = r7;
        r27 = r10;
        goto L_0x037d;
    L_0x0181:
        r13 = r7;
        r27 = r10;
        r10 = r8;
        r0 = r9.mChart;
        r1 = r15.getAxisDependency();
        r8 = r0.getTransformer(r1);
        r7 = 0;
        r21 = 0;
    L_0x0192:
        r0 = (float) r7;
        r1 = r15.getEntryCount();
        r1 = (float) r1;
        r2 = r9.mAnimator;
        r2 = r2.getPhaseX();
        r1 = r1 * r2;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 >= 0) goto L_0x037d;
    L_0x01a3:
        r0 = r15.getEntryForIndex(r7);
        r6 = r0;
        r6 = (com.github.mikephil.charting.data.BarEntry) r6;
        r5 = r6.getYVals();
        r0 = r10.buffer;
        r0 = r0[r21];
        r1 = r10.buffer;
        r2 = r21 + 2;
        r1 = r1[r2];
        r0 = r0 + r1;
        r4 = r0 / r19;
        r22 = r15.getValueTextColor(r7);
        if (r5 != 0) goto L_0x027d;
    L_0x01c1:
        r0 = r9.mViewPortHandler;
        r0 = r0.isInBoundsRight(r4);
        if (r0 != 0) goto L_0x01cb;
    L_0x01c9:
        goto L_0x037d;
    L_0x01cb:
        r0 = r9.mViewPortHandler;
        r1 = r10.buffer;
        r23 = r21 + 1;
        r1 = r1[r23];
        r0 = r0.isInBoundsY(r1);
        if (r0 == 0) goto L_0x0269;
    L_0x01d9:
        r0 = r9.mViewPortHandler;
        r0 = r0.isInBoundsLeft(r4);
        if (r0 != 0) goto L_0x01e3;
    L_0x01e1:
        goto L_0x0269;
    L_0x01e3:
        r0 = r15.isDrawValuesEnabled();
        if (r0 == 0) goto L_0x0220;
    L_0x01e9:
        r2 = r15.getValueFormatter();
        r3 = r6.getY();
        r0 = r10.buffer;
        r0 = r0[r23];
        r1 = r6.getY();
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 < 0) goto L_0x0200;
    L_0x01fd:
        r1 = r16;
        goto L_0x0202;
    L_0x0200:
        r1 = r17;
    L_0x0202:
        r24 = r0 + r1;
        r0 = r9;
        r1 = r36;
        r28 = r4;
        r4 = r6;
        r29 = r11;
        r11 = r5;
        r5 = r14;
        r30 = r12;
        r12 = r6;
        r6 = r28;
        r25 = r7;
        r7 = r24;
        r31 = r14;
        r14 = r8;
        r8 = r22;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x022d;
    L_0x0220:
        r28 = r4;
        r25 = r7;
        r29 = r11;
        r30 = r12;
        r31 = r14;
        r11 = r5;
        r12 = r6;
        r14 = r8;
    L_0x022d:
        r0 = r12.getIcon();
        if (r0 == 0) goto L_0x036e;
    L_0x0233:
        r0 = r15.isDrawIconsEnabled();
        if (r0 == 0) goto L_0x036e;
    L_0x0239:
        r2 = r12.getIcon();
        r0 = r10.buffer;
        r0 = r0[r23];
        r1 = r12.getY();
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 < 0) goto L_0x024c;
    L_0x0249:
        r1 = r16;
        goto L_0x024e;
    L_0x024c:
        r1 = r17;
    L_0x024e:
        r0 = r0 + r1;
        r1 = r13.f488x;
        r8 = r28;
        r4 = r8 + r1;
        r1 = r13.f489y;
        r0 = r0 + r1;
        r3 = (int) r4;
        r4 = (int) r0;
        r5 = r2.getIntrinsicWidth();
        r6 = r2.getIntrinsicHeight();
        r1 = r36;
        com.github.mikephil.charting.utils.Utils.drawImage(r1, r2, r3, r4, r5, r6);
        goto L_0x036e;
    L_0x0269:
        r25 = r7;
        r29 = r11;
        r30 = r12;
        r31 = r14;
        r14 = r8;
        r8 = r14;
        r7 = r25;
    L_0x0275:
        r11 = r29;
        r12 = r30;
        r14 = r31;
        goto L_0x0192;
    L_0x027d:
        r25 = r7;
        r29 = r11;
        r30 = r12;
        r31 = r14;
        r11 = r5;
        r12 = r6;
        r14 = r8;
        r8 = r4;
        r0 = r11.length;
        r0 = r0 * 2;
        r7 = new float[r0];
        r0 = r12.getNegativeSum();
        r0 = -r0;
        r24 = r0;
        r23 = r20;
        r0 = 0;
        r1 = 0;
    L_0x0299:
        r2 = r7.length;
        if (r0 >= r2) goto L_0x02c7;
    L_0x029c:
        r2 = r11[r1];
        r3 = (r2 > r20 ? 1 : (r2 == r20 ? 0 : -1));
        if (r3 != 0) goto L_0x02ab;
    L_0x02a2:
        r3 = (r23 > r20 ? 1 : (r23 == r20 ? 0 : -1));
        if (r3 == 0) goto L_0x02bc;
    L_0x02a6:
        r3 = (r24 > r20 ? 1 : (r24 == r20 ? 0 : -1));
        if (r3 != 0) goto L_0x02ab;
    L_0x02aa:
        goto L_0x02bc;
    L_0x02ab:
        r3 = (r2 > r20 ? 1 : (r2 == r20 ? 0 : -1));
        if (r3 < 0) goto L_0x02b4;
    L_0x02af:
        r2 = r23 + r2;
        r23 = r2;
        goto L_0x02bc;
    L_0x02b4:
        r2 = r24 - r2;
        r34 = r24;
        r24 = r2;
        r2 = r34;
    L_0x02bc:
        r3 = r0 + 1;
        r2 = r2 * r18;
        r7[r3] = r2;
        r0 = r0 + 2;
        r1 = r1 + 1;
        goto L_0x0299;
    L_0x02c7:
        r14.pointValuesToPixel(r7);
        r6 = 0;
    L_0x02cb:
        r0 = r7.length;
        if (r6 >= r0) goto L_0x036e;
    L_0x02ce:
        r0 = r6 / 2;
        r1 = r11[r0];
        r2 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r2 != 0) goto L_0x02de;
    L_0x02d6:
        r2 = (r24 > r20 ? 1 : (r24 == r20 ? 0 : -1));
        if (r2 != 0) goto L_0x02de;
    L_0x02da:
        r2 = (r23 > r20 ? 1 : (r23 == r20 ? 0 : -1));
        if (r2 > 0) goto L_0x02e2;
    L_0x02de:
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 >= 0) goto L_0x02e4;
    L_0x02e2:
        r1 = 1;
        goto L_0x02e5;
    L_0x02e4:
        r1 = 0;
    L_0x02e5:
        r2 = r6 + 1;
        r2 = r7[r2];
        if (r1 == 0) goto L_0x02ee;
    L_0x02eb:
        r1 = r17;
        goto L_0x02f0;
    L_0x02ee:
        r1 = r16;
    L_0x02f0:
        r5 = r2 + r1;
        r1 = r9.mViewPortHandler;
        r1 = r1.isInBoundsRight(r8);
        if (r1 != 0) goto L_0x02fc;
    L_0x02fa:
        goto L_0x036e;
    L_0x02fc:
        r1 = r9.mViewPortHandler;
        r1 = r1.isInBoundsY(r5);
        if (r1 == 0) goto L_0x0360;
    L_0x0304:
        r1 = r9.mViewPortHandler;
        r1 = r1.isInBoundsLeft(r8);
        if (r1 != 0) goto L_0x030d;
    L_0x030c:
        goto L_0x0360;
    L_0x030d:
        r1 = r15.isDrawValuesEnabled();
        if (r1 == 0) goto L_0x0330;
    L_0x0313:
        r2 = r15.getValueFormatter();
        r3 = r11[r0];
        r0 = r9;
        r1 = r36;
        r4 = r12;
        r26 = r5;
        r5 = r31;
        r28 = r6;
        r6 = r8;
        r32 = r7;
        r7 = r26;
        r33 = r8;
        r8 = r22;
        r0.drawValue(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x0338;
    L_0x0330:
        r26 = r5;
        r28 = r6;
        r32 = r7;
        r33 = r8;
    L_0x0338:
        r0 = r12.getIcon();
        if (r0 == 0) goto L_0x0366;
    L_0x033e:
        r0 = r15.isDrawIconsEnabled();
        if (r0 == 0) goto L_0x0366;
    L_0x0344:
        r2 = r12.getIcon();
        r0 = r13.f488x;
        r4 = r33 + r0;
        r3 = (int) r4;
        r0 = r13.f489y;
        r5 = r26 + r0;
        r4 = (int) r5;
        r5 = r2.getIntrinsicWidth();
        r6 = r2.getIntrinsicHeight();
        r1 = r36;
        com.github.mikephil.charting.utils.Utils.drawImage(r1, r2, r3, r4, r5, r6);
        goto L_0x0366;
    L_0x0360:
        r28 = r6;
        r32 = r7;
        r33 = r8;
    L_0x0366:
        r6 = r28 + 2;
        r7 = r32;
        r8 = r33;
        goto L_0x02cb;
    L_0x036e:
        if (r11 != 0) goto L_0x0373;
    L_0x0370:
        r21 = r21 + 4;
        goto L_0x0378;
    L_0x0373:
        r0 = 4;
        r1 = r11.length;
        r0 = r0 * r1;
        r21 = r21 + r0;
    L_0x0378:
        r7 = r25 + 1;
        r8 = r14;
        goto L_0x0275;
    L_0x037d:
        r29 = r11;
        r30 = r12;
        r31 = r14;
        com.github.mikephil.charting.utils.MPPointF.recycleInstance(r13);
    L_0x0386:
        r14 = r31 + 1;
        r10 = r27;
        r11 = r29;
        r12 = r30;
        goto L_0x0021;
    L_0x0390:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.BarChartRenderer.drawValues(android.graphics.Canvas):void");
    }

    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        BarData barData = this.mChart.getBarData();
        for (Highlight highlight : highlightArr) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(highlight.getDataSetIndex());
            if (iBarDataSet != null && iBarDataSet.isHighlightEnabled()) {
                BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForXValue(highlight.getX(), highlight.getY());
                if (isInBoundsX(barEntry, iBarDataSet)) {
                    float y;
                    float f;
                    float f2;
                    Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
                    this.mHighlightPaint.setColor(iBarDataSet.getHighLightColor());
                    this.mHighlightPaint.setAlpha(iBarDataSet.getHighLightAlpha());
                    Object obj = (highlight.getStackIndex() < 0 || !barEntry.isStacked()) ? null : 1;
                    if (obj == null) {
                        y = barEntry.getY();
                        f = 0.0f;
                    } else if (this.mChart.isHighlightFullBarEnabled()) {
                        y = barEntry.getPositiveSum();
                        f = -barEntry.getNegativeSum();
                    } else {
                        Range range = barEntry.getRanges()[highlight.getStackIndex()];
                        f = range.from;
                        f2 = range.f59to;
                        prepareBarHighlight(barEntry.getX(), f, f2, barData.getBarWidth() / 2.0f, transformer);
                        setHighlightDrawPos(highlight, this.mBarRect);
                        canvas.drawRect(this.mBarRect, this.mHighlightPaint);
                    }
                    f2 = f;
                    f = y;
                    prepareBarHighlight(barEntry.getX(), f, f2, barData.getBarWidth() / 2.0f, transformer);
                    setHighlightDrawPos(highlight, this.mBarRect);
                    canvas.drawRect(this.mBarRect, this.mHighlightPaint);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void setHighlightDrawPos(Highlight highlight, RectF rectF) {
        highlight.setDraw(rectF.centerX(), rectF.top);
    }
}
