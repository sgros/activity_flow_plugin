package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LegendRenderer extends Renderer {
    protected List<LegendEntry> computedEntries = new ArrayList(16);
    protected FontMetrics legendFontMetrics = new FontMetrics();
    protected Legend mLegend;
    protected Paint mLegendFormPaint;
    protected Paint mLegendLabelPaint;
    private Path mLineFormPath = new Path();

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);
        this.mLegend = legend;
        this.mLegendLabelPaint = new Paint(1);
        this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Align.LEFT);
        this.mLegendFormPaint = new Paint(1);
        this.mLegendFormPaint.setStyle(Style.FILL);
    }

    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }

    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }

    public void computeLegend(ChartData<?> chartData) {
        ChartData chartData2 = chartData;
        if (!this.mLegend.isLegendCustom()) {
            this.computedEntries.clear();
            int i = 0;
            while (i < chartData.getDataSetCount()) {
                ChartData chartData3;
                IDataSet dataSetByIndex = chartData2.getDataSetByIndex(i);
                List colors = dataSetByIndex.getColors();
                int entryCount = dataSetByIndex.getEntryCount();
                if (dataSetByIndex instanceof IBarDataSet) {
                    IBarDataSet iBarDataSet = (IBarDataSet) dataSetByIndex;
                    if (iBarDataSet.isStacked()) {
                        String[] stackLabels = iBarDataSet.getStackLabels();
                        int i2 = 0;
                        while (i2 < colors.size() && i2 < iBarDataSet.getStackSize()) {
                            List list = this.computedEntries;
                            LegendEntry legendEntry = r10;
                            LegendEntry legendEntry2 = new LegendEntry(stackLabels[i2 % stackLabels.length], dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), ((Integer) colors.get(i2)).intValue());
                            list.add(legendEntry);
                            i2++;
                        }
                        if (iBarDataSet.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                        }
                        chartData3 = chartData2;
                        i++;
                        chartData2 = chartData3;
                    }
                }
                if (dataSetByIndex instanceof IPieDataSet) {
                    IPieDataSet iPieDataSet = (IPieDataSet) dataSetByIndex;
                    int i3 = 0;
                    while (i3 < colors.size() && i3 < entryCount) {
                        List list2 = this.computedEntries;
                        LegendEntry legendEntry3 = r9;
                        LegendEntry legendEntry4 = new LegendEntry(((PieEntry) iPieDataSet.getEntryForIndex(i3)).getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), ((Integer) colors.get(i3)).intValue());
                        list2.add(legendEntry3);
                        i3++;
                        ChartData<?> chartData4 = chartData;
                    }
                    if (iPieDataSet.getLabel() != null) {
                        this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                    }
                } else {
                    int increasingColor;
                    if (dataSetByIndex instanceof ICandleDataSet) {
                        ICandleDataSet iCandleDataSet = (ICandleDataSet) dataSetByIndex;
                        if (iCandleDataSet.getDecreasingColor() != ColorTemplate.COLOR_NONE) {
                            int decreasingColor = iCandleDataSet.getDecreasingColor();
                            increasingColor = iCandleDataSet.getIncreasingColor();
                            this.computedEntries.add(new LegendEntry(null, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), decreasingColor));
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), increasingColor));
                        }
                    }
                    increasingColor = 0;
                    while (increasingColor < colors.size() && increasingColor < entryCount) {
                        String label;
                        if (increasingColor >= colors.size() - 1 || increasingColor >= entryCount - 1) {
                            label = chartData.getDataSetByIndex(i).getLabel();
                        } else {
                            label = null;
                            ChartData<?> chartData5 = chartData;
                        }
                        this.computedEntries.add(new LegendEntry(label, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), ((Integer) colors.get(increasingColor)).intValue()));
                        increasingColor++;
                    }
                }
                chartData3 = chartData;
                i++;
                chartData2 = chartData3;
            }
            if (this.mLegend.getExtraEntries() != null) {
                Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
            }
            this.mLegend.setEntries(this.computedEntries);
        }
        Typeface typeface = this.mLegend.getTypeface();
        if (typeface != null) {
            this.mLegendLabelPaint.setTypeface(typeface);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }

    /* JADX WARNING: Missing block: B:32:0x014a, code skipped:
            r9 = r4;
     */
    /* JADX WARNING: Missing block: B:39:0x0169, code skipped:
            r9 = r3;
     */
    /* JADX WARNING: Missing block: B:41:0x0172, code skipped:
            switch(r0) {
                case com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL :com.github.mikephil.charting.components.Legend$LegendOrientation: goto L_0x027a;
                case com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL :com.github.mikephil.charting.components.Legend$LegendOrientation: goto L_0x0177;
                default: goto L_0x0175;
            };
     */
    /* JADX WARNING: Missing block: B:43:0x017f, code skipped:
            switch(r1) {
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x01b4;
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x019c;
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x0185;
                default: goto L_0x0182;
            };
     */
    /* JADX WARNING: Missing block: B:44:0x0182, code skipped:
            r0 = 0.0f;
     */
    /* JADX WARNING: Missing block: B:45:0x0185, code skipped:
            r0 = ((r6.mViewPortHandler.getChartHeight() / 2.0f) - (r6.mLegend.mNeededHeight / 2.0f)) + r6.mLegend.getYOffset();
     */
    /* JADX WARNING: Missing block: B:47:0x019e, code skipped:
            if (r15 != com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER) goto L_0x01a7;
     */
    /* JADX WARNING: Missing block: B:48:0x01a0, code skipped:
            r0 = r6.mViewPortHandler.getChartHeight();
     */
    /* JADX WARNING: Missing block: B:49:0x01a7, code skipped:
            r0 = r6.mViewPortHandler.contentBottom();
     */
    /* JADX WARNING: Missing block: B:50:0x01ad, code skipped:
            r0 = r0 - (r6.mLegend.mNeededHeight + r2);
     */
    /* JADX WARNING: Missing block: B:52:0x01b6, code skipped:
            if (r15 != com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER) goto L_0x01bb;
     */
    /* JADX WARNING: Missing block: B:53:0x01b8, code skipped:
            r0 = 0.0f;
     */
    /* JADX WARNING: Missing block: B:54:0x01bb, code skipped:
            r0 = r6.mViewPortHandler.contentTop();
     */
    /* JADX WARNING: Missing block: B:55:0x01c1, code skipped:
            r0 = r0 + r2;
     */
    /* JADX WARNING: Missing block: B:56:0x01c2, code skipped:
            r17 = r0;
            r15 = 0.0f;
            r14 = 0;
            r19 = null;
     */
    /* JADX WARNING: Missing block: B:58:0x01ca, code skipped:
            if (r14 >= r12.length) goto L_0x03e6;
     */
    /* JADX WARNING: Missing block: B:59:0x01cc, code skipped:
            r4 = r12[r14];
     */
    /* JADX WARNING: Missing block: B:60:0x01d2, code skipped:
            if (r4.form == com.github.mikephil.charting.components.Legend.LegendForm.NONE) goto L_0x01d7;
     */
    /* JADX WARNING: Missing block: B:61:0x01d4, code skipped:
            r22 = 1;
     */
    /* JADX WARNING: Missing block: B:62:0x01d7, code skipped:
            r22 = null;
     */
    /* JADX WARNING: Missing block: B:64:0x01df, code skipped:
            if (java.lang.Float.isNaN(r4.formSize) == false) goto L_0x01e4;
     */
    /* JADX WARNING: Missing block: B:65:0x01e1, code skipped:
            r23 = r16;
     */
    /* JADX WARNING: Missing block: B:66:0x01e4, code skipped:
            r23 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r4.formSize);
     */
    /* JADX WARNING: Missing block: B:67:0x01ec, code skipped:
            if (r22 == null) goto L_0x021b;
     */
    /* JADX WARNING: Missing block: B:69:0x01f0, code skipped:
            if (r5 != com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT) goto L_0x01f7;
     */
    /* JADX WARNING: Missing block: B:70:0x01f2, code skipped:
            r0 = r9 + r15;
     */
    /* JADX WARNING: Missing block: B:71:0x01f4, code skipped:
            r25 = r0;
     */
    /* JADX WARNING: Missing block: B:72:0x01f7, code skipped:
            r0 = r9 - (r23 - r15);
     */
    /* JADX WARNING: Missing block: B:73:0x01fc, code skipped:
            r27 = r4;
            r10 = r20;
            r13 = r5;
            drawForm(r7, r25, r17 + r11, r4, r6.mLegend);
     */
    /* JADX WARNING: Missing block: B:74:0x0212, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT) goto L_0x0218;
     */
    /* JADX WARNING: Missing block: B:75:0x0214, code skipped:
            r25 = r25 + r23;
     */
    /* JADX WARNING: Missing block: B:76:0x0218, code skipped:
            r0 = r27;
     */
    /* JADX WARNING: Missing block: B:77:0x021b, code skipped:
            r13 = r5;
            r10 = r20;
            r0 = r4;
            r25 = r9;
     */
    /* JADX WARNING: Missing block: B:79:0x0223, code skipped:
            if (r0.label == null) goto L_0x0269;
     */
    /* JADX WARNING: Missing block: B:80:0x0225, code skipped:
            if (r22 == null) goto L_0x0237;
     */
    /* JADX WARNING: Missing block: B:81:0x0227, code skipped:
            if (r19 != null) goto L_0x0237;
     */
    /* JADX WARNING: Missing block: B:83:0x022b, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT) goto L_0x0231;
     */
    /* JADX WARNING: Missing block: B:84:0x022d, code skipped:
            r1 = r24;
            r5 = r1;
     */
    /* JADX WARNING: Missing block: B:85:0x0231, code skipped:
            r5 = r24;
            r1 = -r5;
     */
    /* JADX WARNING: Missing block: B:86:0x0234, code skipped:
            r1 = r25 + r1;
     */
    /* JADX WARNING: Missing block: B:87:0x0237, code skipped:
            r5 = r24;
     */
    /* JADX WARNING: Missing block: B:88:0x0239, code skipped:
            if (r19 == null) goto L_0x023d;
     */
    /* JADX WARNING: Missing block: B:89:0x023b, code skipped:
            r1 = r9;
     */
    /* JADX WARNING: Missing block: B:90:0x023d, code skipped:
            r1 = r25;
     */
    /* JADX WARNING: Missing block: B:92:0x0241, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x024d;
     */
    /* JADX WARNING: Missing block: B:93:0x0243, code skipped:
            r1 = r1 - ((float) com.github.mikephil.charting.utils.Utils.calcTextWidth(r6.mLegendLabelPaint, r0.label));
     */
    /* JADX WARNING: Missing block: B:94:0x024d, code skipped:
            if (r19 != null) goto L_0x0257;
     */
    /* JADX WARNING: Missing block: B:95:0x024f, code skipped:
            drawLabel(r7, r1, r17 + r8, r0.label);
     */
    /* JADX WARNING: Missing block: B:96:0x0257, code skipped:
            r17 = r17 + (r8 + r21);
            drawLabel(r7, r1, r17 + r8, r0.label);
     */
    /* JADX WARNING: Missing block: B:97:0x0262, code skipped:
            r17 = r17 + (r8 + r21);
            r15 = 0.0f;
     */
    /* JADX WARNING: Missing block: B:98:0x0269, code skipped:
            r5 = r24;
            r15 = r15 + (r23 + r10);
            r19 = 1;
     */
    /* JADX WARNING: Missing block: B:99:0x0271, code skipped:
            r14 = r14 + 1;
            r24 = r5;
            r20 = r10;
            r5 = r13;
     */
    /* JADX WARNING: Missing block: B:100:0x027a, code skipped:
            r13 = r5;
            r10 = r20;
            r5 = r24;
            r14 = r6.mLegend.getCalculatedLineSizes();
            r4 = r6.mLegend.getCalculatedLabelSizes();
            r3 = r6.mLegend.getCalculatedLabelBreakPoints();
     */
    /* JADX WARNING: Missing block: B:101:0x0299, code skipped:
            switch(r1) {
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x02bc;
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x02af;
                case com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER :com.github.mikephil.charting.components.Legend$LegendVerticalAlignment: goto L_0x029f;
                default: goto L_0x029c;
            };
     */
    /* JADX WARNING: Missing block: B:102:0x029c, code skipped:
            r2 = 0.0f;
     */
    /* JADX WARNING: Missing block: B:103:0x029f, code skipped:
            r2 = r2 + ((r6.mViewPortHandler.getChartHeight() - r6.mLegend.mNeededHeight) / 2.0f);
     */
    /* JADX WARNING: Missing block: B:104:0x02af, code skipped:
            r2 = (r6.mViewPortHandler.getChartHeight() - r2) - r6.mLegend.mNeededHeight;
     */
    /* JADX WARNING: Missing block: B:105:0x02bc, code skipped:
            r1 = r12.length;
            r0 = r2;
            r28 = r4;
            r17 = r9;
            r2 = 0;
            r4 = 0;
     */
    /* JADX WARNING: Missing block: B:106:0x02c4, code skipped:
            if (r2 >= r1) goto L_0x03e6;
     */
    /* JADX WARNING: Missing block: B:107:0x02c6, code skipped:
            r29 = r10;
            r10 = r12[r2];
            r30 = r1;
            r31 = r5;
     */
    /* JADX WARNING: Missing block: B:108:0x02d2, code skipped:
            if (r10.form == com.github.mikephil.charting.components.Legend.LegendForm.NONE) goto L_0x02d7;
     */
    /* JADX WARNING: Missing block: B:109:0x02d4, code skipped:
            r18 = 1;
     */
    /* JADX WARNING: Missing block: B:110:0x02d7, code skipped:
            r18 = null;
     */
    /* JADX WARNING: Missing block: B:112:0x02df, code skipped:
            if (java.lang.Float.isNaN(r10.formSize) == false) goto L_0x02e4;
     */
    /* JADX WARNING: Missing block: B:113:0x02e1, code skipped:
            r20 = r16;
     */
    /* JADX WARNING: Missing block: B:114:0x02e4, code skipped:
            r20 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r10.formSize);
     */
    /* JADX WARNING: Missing block: B:116:0x02f0, code skipped:
            if (r2 >= r3.size()) goto L_0x0306;
     */
    /* JADX WARNING: Missing block: B:118:0x02fc, code skipped:
            if (((java.lang.Boolean) r3.get(r2)).booleanValue() == false) goto L_0x0306;
     */
    /* JADX WARNING: Missing block: B:119:0x02fe, code skipped:
            r22 = r0 + (r8 + r21);
            r17 = r9;
     */
    /* JADX WARNING: Missing block: B:120:0x0306, code skipped:
            r22 = r0;
     */
    /* JADX WARNING: Missing block: B:122:0x030a, code skipped:
            if (r17 != r9) goto L_0x0336;
     */
    /* JADX WARNING: Missing block: B:124:0x030e, code skipped:
            if (r15 != com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER) goto L_0x0336;
     */
    /* JADX WARNING: Missing block: B:126:0x0314, code skipped:
            if (r4 >= r14.size()) goto L_0x0336;
     */
    /* JADX WARNING: Missing block: B:128:0x0318, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x0325;
     */
    /* JADX WARNING: Missing block: B:129:0x031a, code skipped:
            r0 = ((com.github.mikephil.charting.utils.FSize) r14.get(r4)).width;
     */
    /* JADX WARNING: Missing block: B:131:0x0325, code skipped:
            r0 = -((com.github.mikephil.charting.utils.FSize) r14.get(r4)).width;
     */
    /* JADX WARNING: Missing block: B:132:0x032f, code skipped:
            r17 = r17 + (r0 / 2.0f);
            r4 = r4 + 1;
     */
    /* JADX WARNING: Missing block: B:134:0x0338, code skipped:
            r23 = r4;
     */
    /* JADX WARNING: Missing block: B:135:0x033c, code skipped:
            if (r10.label != null) goto L_0x0341;
     */
    /* JADX WARNING: Missing block: B:136:0x033e, code skipped:
            r24 = 1;
     */
    /* JADX WARNING: Missing block: B:137:0x0341, code skipped:
            r24 = null;
     */
    /* JADX WARNING: Missing block: B:138:0x0343, code skipped:
            if (r18 == null) goto L_0x036e;
     */
    /* JADX WARNING: Missing block: B:140:0x0347, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x034b;
     */
    /* JADX WARNING: Missing block: B:141:0x0349, code skipped:
            r17 = r17 - r20;
     */
    /* JADX WARNING: Missing block: B:142:0x034b, code skipped:
            r26 = r30;
            r32 = r9;
            r9 = r2;
            r27 = r3;
            r33 = r11;
            r11 = r28;
            r34 = r12;
            r12 = r31;
            drawForm(r7, r17, r22 + r11, r10, r6.mLegend);
     */
    /* JADX WARNING: Missing block: B:143:0x0369, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT) goto L_0x037d;
     */
    /* JADX WARNING: Missing block: B:144:0x036b, code skipped:
            r17 = r17 + r20;
     */
    /* JADX WARNING: Missing block: B:145:0x036e, code skipped:
            r27 = r3;
            r32 = r9;
            r33 = r11;
            r34 = r12;
            r11 = r28;
            r26 = r30;
            r12 = r31;
            r9 = r2;
     */
    /* JADX WARNING: Missing block: B:146:0x037d, code skipped:
            if (r24 != null) goto L_0x03bf;
     */
    /* JADX WARNING: Missing block: B:147:0x037f, code skipped:
            if (r18 == null) goto L_0x038a;
     */
    /* JADX WARNING: Missing block: B:149:0x0383, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x0387;
     */
    /* JADX WARNING: Missing block: B:150:0x0385, code skipped:
            r0 = -r12;
     */
    /* JADX WARNING: Missing block: B:151:0x0387, code skipped:
            r0 = r12;
     */
    /* JADX WARNING: Missing block: B:152:0x0388, code skipped:
            r17 = r17 + r0;
     */
    /* JADX WARNING: Missing block: B:154:0x038c, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x0398;
     */
    /* JADX WARNING: Missing block: B:155:0x038e, code skipped:
            r17 = r17 - ((com.github.mikephil.charting.utils.FSize) r11.get(r9)).width;
     */
    /* JADX WARNING: Missing block: B:156:0x0398, code skipped:
            r0 = r17;
            drawLabel(r7, r0, r22 + r8, r10.label);
     */
    /* JADX WARNING: Missing block: B:157:0x03a3, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT) goto L_0x03ae;
     */
    /* JADX WARNING: Missing block: B:158:0x03a5, code skipped:
            r0 = r0 + ((com.github.mikephil.charting.utils.FSize) r11.get(r9)).width;
     */
    /* JADX WARNING: Missing block: B:160:0x03b0, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x03b6;
     */
    /* JADX WARNING: Missing block: B:161:0x03b2, code skipped:
            r1 = r25;
            r2 = -r1;
     */
    /* JADX WARNING: Missing block: B:162:0x03b6, code skipped:
            r1 = r25;
            r2 = r1;
     */
    /* JADX WARNING: Missing block: B:163:0x03b9, code skipped:
            r17 = r0 + r2;
            r0 = r29;
     */
    /* JADX WARNING: Missing block: B:164:0x03bf, code skipped:
            r1 = r25;
     */
    /* JADX WARNING: Missing block: B:165:0x03c3, code skipped:
            if (r13 != com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT) goto L_0x03c9;
     */
    /* JADX WARNING: Missing block: B:166:0x03c5, code skipped:
            r0 = r29;
            r4 = -r0;
     */
    /* JADX WARNING: Missing block: B:167:0x03c9, code skipped:
            r0 = r29;
            r4 = r0;
     */
    /* JADX WARNING: Missing block: B:168:0x03cc, code skipped:
            r17 = r17 + r4;
     */
    /* JADX WARNING: Missing block: B:169:0x03ce, code skipped:
            r2 = r9 + 1;
            r10 = r0;
            r25 = r1;
            r28 = r11;
            r5 = r12;
            r0 = r22;
            r4 = r23;
            r1 = r26;
            r3 = r27;
            r9 = r32;
            r11 = r33;
            r12 = r34;
     */
    /* JADX WARNING: Missing block: B:170:0x03e6, code skipped:
            return;
     */
    public void renderLegend(android.graphics.Canvas r36) {
        /*
        r35 = this;
        r6 = r35;
        r7 = r36;
        r0 = r6.mLegend;
        r0 = r0.isEnabled();
        if (r0 != 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r0 = r6.mLegend;
        r0 = r0.getTypeface();
        if (r0 == 0) goto L_0x001a;
    L_0x0015:
        r1 = r6.mLegendLabelPaint;
        r1.setTypeface(r0);
    L_0x001a:
        r0 = r6.mLegendLabelPaint;
        r1 = r6.mLegend;
        r1 = r1.getTextSize();
        r0.setTextSize(r1);
        r0 = r6.mLegendLabelPaint;
        r1 = r6.mLegend;
        r1 = r1.getTextColor();
        r0.setColor(r1);
        r0 = r6.mLegendLabelPaint;
        r1 = r6.legendFontMetrics;
        r8 = com.github.mikephil.charting.utils.Utils.getLineHeight(r0, r1);
        r0 = r6.mLegendLabelPaint;
        r1 = r6.legendFontMetrics;
        r0 = com.github.mikephil.charting.utils.Utils.getLineSpacing(r0, r1);
        r1 = r6.mLegend;
        r1 = r1.getYEntrySpace();
        r1 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r1);
        r9 = r0 + r1;
        r0 = r6.mLegendLabelPaint;
        r1 = "ABC";
        r0 = com.github.mikephil.charting.utils.Utils.calcTextHeight(r0, r1);
        r0 = (float) r0;
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r0 / r10;
        r11 = r8 - r0;
        r0 = r6.mLegend;
        r12 = r0.getEntries();
        r0 = r6.mLegend;
        r0 = r0.getFormToTextSpace();
        r13 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r0 = r6.mLegend;
        r0 = r0.getXEntrySpace();
        r14 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r0 = r6.mLegend;
        r0 = r0.getOrientation();
        r1 = r6.mLegend;
        r15 = r1.getHorizontalAlignment();
        r1 = r6.mLegend;
        r1 = r1.getVerticalAlignment();
        r2 = r6.mLegend;
        r5 = r2.getDirection();
        r2 = r6.mLegend;
        r2 = r2.getFormSize();
        r16 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r2 = r6.mLegend;
        r2 = r2.getStackSpace();
        r4 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r2);
        r2 = r6.mLegend;
        r2 = r2.getYOffset();
        r3 = r6.mLegend;
        r3 = r3.getXOffset();
        r17 = com.github.mikephil.charting.renderer.LegendRenderer.C04331.f65x2787f53e;
        r18 = r15.ordinal();
        r17 = r17[r18];
        r18 = 0;
        switch(r17) {
            case 1: goto L_0x014c;
            case 2: goto L_0x0124;
            case 3: goto L_0x00c5;
            default: goto L_0x00b9;
        };
    L_0x00b9:
        r20 = r4;
        r21 = r9;
        r24 = r13;
        r25 = r14;
        r9 = r18;
        goto L_0x016a;
    L_0x00c5:
        r10 = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL;
        if (r0 != r10) goto L_0x00d6;
    L_0x00c9:
        r10 = r6.mViewPortHandler;
        r10 = r10.getChartWidth();
        r17 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = r10 / r17;
        r20 = r4;
        goto L_0x00e9;
    L_0x00d6:
        r17 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = r6.mViewPortHandler;
        r10 = r10.contentLeft();
        r20 = r4;
        r4 = r6.mViewPortHandler;
        r4 = r4.contentWidth();
        r4 = r4 / r17;
        r10 = r10 + r4;
    L_0x00e9:
        r4 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r5 != r4) goto L_0x00ef;
    L_0x00ed:
        r4 = r3;
        goto L_0x00f0;
    L_0x00ef:
        r4 = -r3;
    L_0x00f0:
        r4 = r4 + r10;
        r10 = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL;
        if (r0 != r10) goto L_0x011d;
    L_0x00f5:
        r21 = r9;
        r9 = (double) r4;
        r4 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        r22 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        if (r5 != r4) goto L_0x010d;
    L_0x00fe:
        r4 = r6.mLegend;
        r4 = r4.mNeededWidth;
        r4 = -r4;
        r24 = r13;
        r25 = r14;
        r13 = (double) r4;
        r13 = r13 / r22;
        r3 = (double) r3;
        r13 = r13 + r3;
        goto L_0x011a;
    L_0x010d:
        r24 = r13;
        r25 = r14;
        r4 = r6.mLegend;
        r4 = r4.mNeededWidth;
        r13 = (double) r4;
        r13 = r13 / r22;
        r3 = (double) r3;
        r13 = r13 - r3;
    L_0x011a:
        r9 = r9 + r13;
        r3 = (float) r9;
        goto L_0x0169;
    L_0x011d:
        r21 = r9;
        r24 = r13;
        r25 = r14;
        goto L_0x014a;
    L_0x0124:
        r20 = r4;
        r21 = r9;
        r24 = r13;
        r25 = r14;
        r4 = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL;
        if (r0 != r4) goto L_0x0138;
    L_0x0130:
        r4 = r6.mViewPortHandler;
        r4 = r4.getChartWidth();
        r4 = r4 - r3;
        goto L_0x013f;
    L_0x0138:
        r4 = r6.mViewPortHandler;
        r4 = r4.contentRight();
        r4 = r4 - r3;
    L_0x013f:
        r3 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r5 != r3) goto L_0x014a;
    L_0x0143:
        r3 = r6.mLegend;
        r3 = r3.mNeededWidth;
        r3 = r4 - r3;
        goto L_0x0169;
    L_0x014a:
        r9 = r4;
        goto L_0x016a;
    L_0x014c:
        r20 = r4;
        r21 = r9;
        r24 = r13;
        r25 = r14;
        r4 = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL;
        if (r0 != r4) goto L_0x0159;
    L_0x0158:
        goto L_0x0160;
    L_0x0159:
        r4 = r6.mViewPortHandler;
        r4 = r4.contentLeft();
        r3 = r3 + r4;
    L_0x0160:
        r4 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r5 != r4) goto L_0x0169;
    L_0x0164:
        r4 = r6.mLegend;
        r4 = r4.mNeededWidth;
        r3 = r3 + r4;
    L_0x0169:
        r9 = r3;
    L_0x016a:
        r3 = com.github.mikephil.charting.renderer.LegendRenderer.C04331.f66x9c9dbef;
        r0 = r0.ordinal();
        r0 = r3[r0];
        switch(r0) {
            case 1: goto L_0x027a;
            case 2: goto L_0x0177;
            default: goto L_0x0175;
        };
    L_0x0175:
        goto L_0x03e6;
    L_0x0177:
        r0 = com.github.mikephil.charting.renderer.LegendRenderer.C04331.f67xc926f1ec;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x01b4;
            case 2: goto L_0x019c;
            case 3: goto L_0x0185;
            default: goto L_0x0182;
        };
    L_0x0182:
        r0 = r18;
        goto L_0x01c2;
    L_0x0185:
        r0 = r6.mViewPortHandler;
        r0 = r0.getChartHeight();
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r0 / r1;
        r2 = r6.mLegend;
        r2 = r2.mNeededHeight;
        r2 = r2 / r1;
        r0 = r0 - r2;
        r1 = r6.mLegend;
        r1 = r1.getYOffset();
        r0 = r0 + r1;
        goto L_0x01c2;
    L_0x019c:
        r0 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER;
        if (r15 != r0) goto L_0x01a7;
    L_0x01a0:
        r0 = r6.mViewPortHandler;
        r0 = r0.getChartHeight();
        goto L_0x01ad;
    L_0x01a7:
        r0 = r6.mViewPortHandler;
        r0 = r0.contentBottom();
    L_0x01ad:
        r1 = r6.mLegend;
        r1 = r1.mNeededHeight;
        r1 = r1 + r2;
        r0 = r0 - r1;
        goto L_0x01c2;
    L_0x01b4:
        r0 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER;
        if (r15 != r0) goto L_0x01bb;
    L_0x01b8:
        r0 = r18;
        goto L_0x01c1;
    L_0x01bb:
        r0 = r6.mViewPortHandler;
        r0 = r0.contentTop();
    L_0x01c1:
        r0 = r0 + r2;
    L_0x01c2:
        r17 = r0;
        r15 = r18;
        r14 = 0;
        r19 = 0;
    L_0x01c9:
        r0 = r12.length;
        if (r14 >= r0) goto L_0x03e6;
    L_0x01cc:
        r4 = r12[r14];
        r0 = r4.form;
        r1 = com.github.mikephil.charting.components.Legend.LegendForm.NONE;
        if (r0 == r1) goto L_0x01d7;
    L_0x01d4:
        r22 = 1;
        goto L_0x01d9;
    L_0x01d7:
        r22 = 0;
    L_0x01d9:
        r0 = r4.formSize;
        r0 = java.lang.Float.isNaN(r0);
        if (r0 == 0) goto L_0x01e4;
    L_0x01e1:
        r23 = r16;
        goto L_0x01ec;
    L_0x01e4:
        r0 = r4.formSize;
        r0 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r0);
        r23 = r0;
    L_0x01ec:
        if (r22 == 0) goto L_0x021b;
    L_0x01ee:
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r5 != r0) goto L_0x01f7;
    L_0x01f2:
        r0 = r9 + r15;
    L_0x01f4:
        r25 = r0;
        goto L_0x01fc;
    L_0x01f7:
        r0 = r23 - r15;
        r0 = r9 - r0;
        goto L_0x01f4;
    L_0x01fc:
        r3 = r17 + r11;
        r2 = r6.mLegend;
        r0 = r6;
        r1 = r7;
        r26 = r2;
        r2 = r25;
        r27 = r4;
        r10 = r20;
        r13 = r5;
        r5 = r26;
        r0.drawForm(r1, r2, r3, r4, r5);
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r13 != r0) goto L_0x0218;
    L_0x0214:
        r0 = r25 + r23;
        r25 = r0;
    L_0x0218:
        r0 = r27;
        goto L_0x0221;
    L_0x021b:
        r13 = r5;
        r10 = r20;
        r0 = r4;
        r25 = r9;
    L_0x0221:
        r1 = r0.label;
        if (r1 == 0) goto L_0x0269;
    L_0x0225:
        if (r22 == 0) goto L_0x0237;
    L_0x0227:
        if (r19 != 0) goto L_0x0237;
    L_0x0229:
        r1 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r13 != r1) goto L_0x0231;
    L_0x022d:
        r1 = r24;
        r5 = r1;
        goto L_0x0234;
    L_0x0231:
        r5 = r24;
        r1 = -r5;
    L_0x0234:
        r1 = r25 + r1;
        goto L_0x023f;
    L_0x0237:
        r5 = r24;
        if (r19 == 0) goto L_0x023d;
    L_0x023b:
        r1 = r9;
        goto L_0x023f;
    L_0x023d:
        r1 = r25;
    L_0x023f:
        r2 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r2) goto L_0x024d;
    L_0x0243:
        r2 = r6.mLegendLabelPaint;
        r3 = r0.label;
        r2 = com.github.mikephil.charting.utils.Utils.calcTextWidth(r2, r3);
        r2 = (float) r2;
        r1 = r1 - r2;
    L_0x024d:
        if (r19 != 0) goto L_0x0257;
    L_0x024f:
        r2 = r17 + r8;
        r0 = r0.label;
        r6.drawLabel(r7, r1, r2, r0);
        goto L_0x0262;
    L_0x0257:
        r2 = r8 + r21;
        r17 = r17 + r2;
        r2 = r17 + r8;
        r0 = r0.label;
        r6.drawLabel(r7, r1, r2, r0);
    L_0x0262:
        r0 = r8 + r21;
        r17 = r17 + r0;
        r15 = r18;
        goto L_0x0271;
    L_0x0269:
        r5 = r24;
        r23 = r23 + r10;
        r15 = r15 + r23;
        r19 = 1;
    L_0x0271:
        r14 = r14 + 1;
        r24 = r5;
        r20 = r10;
        r5 = r13;
        goto L_0x01c9;
    L_0x027a:
        r13 = r5;
        r10 = r20;
        r5 = r24;
        r0 = r6.mLegend;
        r14 = r0.getCalculatedLineSizes();
        r0 = r6.mLegend;
        r4 = r0.getCalculatedLabelSizes();
        r0 = r6.mLegend;
        r3 = r0.getCalculatedLabelBreakPoints();
        r0 = com.github.mikephil.charting.renderer.LegendRenderer.C04331.f67xc926f1ec;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x02bc;
            case 2: goto L_0x02af;
            case 3: goto L_0x029f;
            default: goto L_0x029c;
        };
    L_0x029c:
        r2 = r18;
        goto L_0x02bc;
    L_0x029f:
        r0 = r6.mViewPortHandler;
        r0 = r0.getChartHeight();
        r1 = r6.mLegend;
        r1 = r1.mNeededHeight;
        r0 = r0 - r1;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r0 / r1;
        r2 = r2 + r0;
        goto L_0x02bc;
    L_0x02af:
        r0 = r6.mViewPortHandler;
        r0 = r0.getChartHeight();
        r0 = r0 - r2;
        r1 = r6.mLegend;
        r1 = r1.mNeededHeight;
        r2 = r0 - r1;
    L_0x02bc:
        r1 = r12.length;
        r0 = r2;
        r28 = r4;
        r17 = r9;
        r2 = 0;
        r4 = 0;
    L_0x02c4:
        if (r2 >= r1) goto L_0x03e6;
    L_0x02c6:
        r29 = r10;
        r10 = r12[r2];
        r30 = r1;
        r1 = r10.form;
        r31 = r5;
        r5 = com.github.mikephil.charting.components.Legend.LegendForm.NONE;
        if (r1 == r5) goto L_0x02d7;
    L_0x02d4:
        r18 = 1;
        goto L_0x02d9;
    L_0x02d7:
        r18 = 0;
    L_0x02d9:
        r1 = r10.formSize;
        r1 = java.lang.Float.isNaN(r1);
        if (r1 == 0) goto L_0x02e4;
    L_0x02e1:
        r20 = r16;
        goto L_0x02ec;
    L_0x02e4:
        r1 = r10.formSize;
        r1 = com.github.mikephil.charting.utils.Utils.convertDpToPixel(r1);
        r20 = r1;
    L_0x02ec:
        r1 = r3.size();
        if (r2 >= r1) goto L_0x0306;
    L_0x02f2:
        r1 = r3.get(r2);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0306;
    L_0x02fe:
        r1 = r8 + r21;
        r0 = r0 + r1;
        r22 = r0;
        r17 = r9;
        goto L_0x0308;
    L_0x0306:
        r22 = r0;
    L_0x0308:
        r0 = (r17 > r9 ? 1 : (r17 == r9 ? 0 : -1));
        if (r0 != 0) goto L_0x0336;
    L_0x030c:
        r0 = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER;
        if (r15 != r0) goto L_0x0336;
    L_0x0310:
        r0 = r14.size();
        if (r4 >= r0) goto L_0x0336;
    L_0x0316:
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r0) goto L_0x0325;
    L_0x031a:
        r0 = r14.get(r4);
        r0 = (com.github.mikephil.charting.utils.FSize) r0;
        r0 = r0.width;
    L_0x0322:
        r19 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x032f;
    L_0x0325:
        r0 = r14.get(r4);
        r0 = (com.github.mikephil.charting.utils.FSize) r0;
        r0 = r0.width;
        r0 = -r0;
        goto L_0x0322;
    L_0x032f:
        r0 = r0 / r19;
        r17 = r17 + r0;
        r4 = r4 + 1;
        goto L_0x0338;
    L_0x0336:
        r19 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
    L_0x0338:
        r23 = r4;
        r0 = r10.label;
        if (r0 != 0) goto L_0x0341;
    L_0x033e:
        r24 = 1;
        goto L_0x0343;
    L_0x0341:
        r24 = 0;
    L_0x0343:
        if (r18 == 0) goto L_0x036e;
    L_0x0345:
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r0) goto L_0x034b;
    L_0x0349:
        r17 = r17 - r20;
    L_0x034b:
        r4 = r22 + r11;
        r5 = r6.mLegend;
        r0 = r6;
        r26 = r30;
        r1 = r7;
        r32 = r9;
        r9 = r2;
        r2 = r17;
        r27 = r3;
        r3 = r4;
        r33 = r11;
        r11 = r28;
        r4 = r10;
        r34 = r12;
        r12 = r31;
        r0.drawForm(r1, r2, r3, r4, r5);
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r13 != r0) goto L_0x037d;
    L_0x036b:
        r17 = r17 + r20;
        goto L_0x037d;
    L_0x036e:
        r27 = r3;
        r32 = r9;
        r33 = r11;
        r34 = r12;
        r11 = r28;
        r26 = r30;
        r12 = r31;
        r9 = r2;
    L_0x037d:
        if (r24 != 0) goto L_0x03bf;
    L_0x037f:
        if (r18 == 0) goto L_0x038a;
    L_0x0381:
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r0) goto L_0x0387;
    L_0x0385:
        r0 = -r12;
        goto L_0x0388;
    L_0x0387:
        r0 = r12;
    L_0x0388:
        r17 = r17 + r0;
    L_0x038a:
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r0) goto L_0x0398;
    L_0x038e:
        r0 = r11.get(r9);
        r0 = (com.github.mikephil.charting.utils.FSize) r0;
        r0 = r0.width;
        r17 = r17 - r0;
    L_0x0398:
        r0 = r17;
        r1 = r22 + r8;
        r2 = r10.label;
        r6.drawLabel(r7, r0, r1, r2);
        r1 = com.github.mikephil.charting.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        if (r13 != r1) goto L_0x03ae;
    L_0x03a5:
        r1 = r11.get(r9);
        r1 = (com.github.mikephil.charting.utils.FSize) r1;
        r1 = r1.width;
        r0 = r0 + r1;
    L_0x03ae:
        r1 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r1) goto L_0x03b6;
    L_0x03b2:
        r1 = r25;
        r2 = -r1;
        goto L_0x03b9;
    L_0x03b6:
        r1 = r25;
        r2 = r1;
    L_0x03b9:
        r0 = r0 + r2;
        r17 = r0;
        r0 = r29;
        goto L_0x03ce;
    L_0x03bf:
        r1 = r25;
        r0 = com.github.mikephil.charting.components.Legend.LegendDirection.RIGHT_TO_LEFT;
        if (r13 != r0) goto L_0x03c9;
    L_0x03c5:
        r0 = r29;
        r4 = -r0;
        goto L_0x03cc;
    L_0x03c9:
        r0 = r29;
        r4 = r0;
    L_0x03cc:
        r17 = r17 + r4;
    L_0x03ce:
        r2 = r9 + 1;
        r10 = r0;
        r25 = r1;
        r28 = r11;
        r5 = r12;
        r0 = r22;
        r4 = r23;
        r1 = r26;
        r3 = r27;
        r9 = r32;
        r11 = r33;
        r12 = r34;
        goto L_0x02c4;
    L_0x03e6:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.LegendRenderer.renderLegend(android.graphics.Canvas):void");
    }

    /* Access modifiers changed, original: protected */
    public void drawForm(Canvas canvas, float f, float f2, LegendEntry legendEntry, Legend legend) {
        if (legendEntry.formColor != ColorTemplate.COLOR_SKIP && legendEntry.formColor != ColorTemplate.COLOR_NONE && legendEntry.formColor != 0) {
            int save = canvas.save();
            LegendForm legendForm = legendEntry.form;
            if (legendForm == LegendForm.DEFAULT) {
                legendForm = legend.getForm();
            }
            this.mLegendFormPaint.setColor(legendEntry.formColor);
            float convertDpToPixel = Utils.convertDpToPixel(Float.isNaN(legendEntry.formSize) ? legend.getFormSize() : legendEntry.formSize);
            float f3 = convertDpToPixel / 2.0f;
            switch (legendForm) {
                case DEFAULT:
                case CIRCLE:
                    this.mLegendFormPaint.setStyle(Style.FILL);
                    canvas.drawCircle(f + f3, f2, f3, this.mLegendFormPaint);
                    break;
                case SQUARE:
                    this.mLegendFormPaint.setStyle(Style.FILL);
                    canvas.drawRect(f, f2 - f3, f + convertDpToPixel, f2 + f3, this.mLegendFormPaint);
                    break;
                case LINE:
                    float convertDpToPixel2 = Utils.convertDpToPixel(Float.isNaN(legendEntry.formLineWidth) ? legend.getFormLineWidth() : legendEntry.formLineWidth);
                    PathEffect formLineDashEffect = legendEntry.formLineDashEffect == null ? legend.getFormLineDashEffect() : legendEntry.formLineDashEffect;
                    this.mLegendFormPaint.setStyle(Style.STROKE);
                    this.mLegendFormPaint.setStrokeWidth(convertDpToPixel2);
                    this.mLegendFormPaint.setPathEffect(formLineDashEffect);
                    this.mLineFormPath.reset();
                    this.mLineFormPath.moveTo(f, f2);
                    this.mLineFormPath.lineTo(f + convertDpToPixel, f2);
                    canvas.drawPath(this.mLineFormPath, this.mLegendFormPaint);
                    break;
            }
            canvas.restoreToCount(save);
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawLabel(Canvas canvas, float f, float f2, String str) {
        canvas.drawText(str, f, f2, this.mLegendLabelPaint);
    }
}
