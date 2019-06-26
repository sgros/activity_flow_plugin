// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.FSize;
import android.graphics.PathEffect;
import android.graphics.Canvas;
import android.graphics.Typeface;
import java.util.Collection;
import java.util.Collections;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.ChartData;
import android.graphics.Paint$Style;
import android.graphics.Paint$Align;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Path;
import android.graphics.Paint;
import com.github.mikephil.charting.components.Legend;
import android.graphics.Paint$FontMetrics;
import com.github.mikephil.charting.components.LegendEntry;
import java.util.List;

public class LegendRenderer extends Renderer
{
    protected List<LegendEntry> computedEntries;
    protected Paint$FontMetrics legendFontMetrics;
    protected Legend mLegend;
    protected Paint mLegendFormPaint;
    protected Paint mLegendLabelPaint;
    private Path mLineFormPath;
    
    public LegendRenderer(final ViewPortHandler viewPortHandler, final Legend mLegend) {
        super(viewPortHandler);
        this.computedEntries = new ArrayList<LegendEntry>(16);
        this.legendFontMetrics = new Paint$FontMetrics();
        this.mLineFormPath = new Path();
        this.mLegend = mLegend;
        (this.mLegendLabelPaint = new Paint(1)).setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Paint$Align.LEFT);
        (this.mLegendFormPaint = new Paint(1)).setStyle(Paint$Style.FILL);
    }
    
    public void computeLegend(final ChartData<?> chartData) {
        ChartData<?> chartData2 = chartData;
        if (!this.mLegend.isLegendCustom()) {
            this.computedEntries.clear();
            for (int i = 0; i < chartData.getDataSetCount(); ++i) {
                final Object dataSetByIndex = chartData2.getDataSetByIndex(i);
                final List colors = ((IDataSet)dataSetByIndex).getColors();
                final int entryCount = ((IDataSet)dataSetByIndex).getEntryCount();
                if (dataSetByIndex instanceof IBarDataSet) {
                    final IBarDataSet set = (IBarDataSet)dataSetByIndex;
                    if (set.isStacked()) {
                        final String[] stackLabels = set.getStackLabels();
                        for (int n = 0; n < colors.size() && n < set.getStackSize(); ++n) {
                            this.computedEntries.add(new LegendEntry(stackLabels[n % stackLabels.length], ((IDataSet)dataSetByIndex).getForm(), ((IDataSet)dataSetByIndex).getFormSize(), ((IDataSet)dataSetByIndex).getFormLineWidth(), ((IDataSet)dataSetByIndex).getFormLineDashEffect(), colors.get(n)));
                        }
                        if (set.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(((IDataSet)dataSetByIndex).getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, 1122867));
                        }
                        continue;
                    }
                }
                Label_0544: {
                    if (dataSetByIndex instanceof IPieDataSet) {
                        final IPieDataSet set2 = (IPieDataSet)dataSetByIndex;
                        for (int n2 = 0; n2 < colors.size() && n2 < entryCount; ++n2) {
                            this.computedEntries.add(new LegendEntry(set2.getEntryForIndex(n2).getLabel(), ((IDataSet)dataSetByIndex).getForm(), ((IDataSet)dataSetByIndex).getFormSize(), ((IDataSet)dataSetByIndex).getFormLineWidth(), ((IDataSet)dataSetByIndex).getFormLineDashEffect(), colors.get(n2)));
                        }
                        if (set2.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(((IDataSet)dataSetByIndex).getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, 1122867));
                        }
                    }
                    else {
                        if (dataSetByIndex instanceof ICandleDataSet) {
                            final ICandleDataSet set3 = (ICandleDataSet)dataSetByIndex;
                            if (set3.getDecreasingColor() != 1122867) {
                                final int decreasingColor = set3.getDecreasingColor();
                                final int increasingColor = set3.getIncreasingColor();
                                this.computedEntries.add(new LegendEntry(null, ((IDataSet)dataSetByIndex).getForm(), ((IDataSet)dataSetByIndex).getFormSize(), ((IDataSet)dataSetByIndex).getFormLineWidth(), ((IDataSet)dataSetByIndex).getFormLineDashEffect(), decreasingColor));
                                this.computedEntries.add(new LegendEntry(((IDataSet)dataSetByIndex).getLabel(), ((IDataSet)dataSetByIndex).getForm(), ((IDataSet)dataSetByIndex).getFormSize(), ((IDataSet)dataSetByIndex).getFormLineWidth(), ((IDataSet)dataSetByIndex).getFormLineDashEffect(), increasingColor));
                                break Label_0544;
                            }
                        }
                        for (int n3 = 0; n3 < colors.size() && n3 < entryCount; ++n3) {
                            String label;
                            if (n3 < colors.size() - 1 && n3 < entryCount - 1) {
                                label = null;
                            }
                            else {
                                label = ((IDataSet)chartData.getDataSetByIndex(i)).getLabel();
                            }
                            this.computedEntries.add(new LegendEntry(label, ((IDataSet)dataSetByIndex).getForm(), ((IDataSet)dataSetByIndex).getFormSize(), ((IDataSet)dataSetByIndex).getFormLineWidth(), ((IDataSet)dataSetByIndex).getFormLineDashEffect(), colors.get(n3)));
                        }
                    }
                }
                chartData2 = chartData;
            }
            if (this.mLegend.getExtraEntries() != null) {
                Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
            }
            this.mLegend.setEntries(this.computedEntries);
        }
        final Typeface typeface = this.mLegend.getTypeface();
        if (typeface != null) {
            this.mLegendLabelPaint.setTypeface(typeface);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }
    
    protected void drawForm(final Canvas canvas, final float n, final float n2, final LegendEntry legendEntry, final Legend legend) {
        if (legendEntry.formColor == 1122868 || legendEntry.formColor == 1122867 || legendEntry.formColor == 0) {
            return;
        }
        final int save = canvas.save();
        Enum<Legend.LegendForm> enum1;
        if ((enum1 = legendEntry.form) == Legend.LegendForm.DEFAULT) {
            enum1 = legend.getForm();
        }
        this.mLegendFormPaint.setColor(legendEntry.formColor);
        float n3;
        if (Float.isNaN(legendEntry.formSize)) {
            n3 = legend.getFormSize();
        }
        else {
            n3 = legendEntry.formSize;
        }
        final float convertDpToPixel = Utils.convertDpToPixel(n3);
        final float n4 = convertDpToPixel / 2.0f;
        while (true) {
            switch (LegendRenderer$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[enum1.ordinal()]) {
                default: {}
                case 1:
                case 2: {
                    canvas.restoreToCount(save);
                }
                case 6: {
                    float n5;
                    if (Float.isNaN(legendEntry.formLineWidth)) {
                        n5 = legend.getFormLineWidth();
                    }
                    else {
                        n5 = legendEntry.formLineWidth;
                    }
                    final float convertDpToPixel2 = Utils.convertDpToPixel(n5);
                    DashPathEffect pathEffect;
                    if (legendEntry.formLineDashEffect == null) {
                        pathEffect = legend.getFormLineDashEffect();
                    }
                    else {
                        pathEffect = legendEntry.formLineDashEffect;
                    }
                    this.mLegendFormPaint.setStyle(Paint$Style.STROKE);
                    this.mLegendFormPaint.setStrokeWidth(convertDpToPixel2);
                    this.mLegendFormPaint.setPathEffect((PathEffect)pathEffect);
                    this.mLineFormPath.reset();
                    this.mLineFormPath.moveTo(n, n2);
                    this.mLineFormPath.lineTo(n + convertDpToPixel, n2);
                    canvas.drawPath(this.mLineFormPath, this.mLegendFormPaint);
                    continue;
                }
                case 5: {
                    this.mLegendFormPaint.setStyle(Paint$Style.FILL);
                    canvas.drawRect(n, n2 - n4, n + convertDpToPixel, n2 + n4, this.mLegendFormPaint);
                    continue;
                }
                case 3:
                case 4: {
                    this.mLegendFormPaint.setStyle(Paint$Style.FILL);
                    canvas.drawCircle(n + n4, n2, n4, this.mLegendFormPaint);
                    continue;
                }
            }
            break;
        }
    }
    
    protected void drawLabel(final Canvas canvas, final float n, final float n2, final String s) {
        canvas.drawText(s, n, n2, this.mLegendLabelPaint);
    }
    
    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }
    
    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }
    
    public void renderLegend(final Canvas canvas) {
        if (!this.mLegend.isEnabled()) {
            return;
        }
        final Typeface typeface = this.mLegend.getTypeface();
        if (typeface != null) {
            this.mLegendLabelPaint.setTypeface(typeface);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        final float lineHeight = Utils.getLineHeight(this.mLegendLabelPaint, this.legendFontMetrics);
        final float n = Utils.getLineSpacing(this.mLegendLabelPaint, this.legendFontMetrics) + Utils.convertDpToPixel(this.mLegend.getYEntrySpace());
        final float n2 = lineHeight - Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0f;
        final LegendEntry[] entries = this.mLegend.getEntries();
        final float convertDpToPixel = Utils.convertDpToPixel(this.mLegend.getFormToTextSpace());
        final float convertDpToPixel2 = Utils.convertDpToPixel(this.mLegend.getXEntrySpace());
        final Legend.LegendOrientation orientation = this.mLegend.getOrientation();
        final Legend.LegendHorizontalAlignment horizontalAlignment = this.mLegend.getHorizontalAlignment();
        final Legend.LegendVerticalAlignment verticalAlignment = this.mLegend.getVerticalAlignment();
        final Legend.LegendDirection direction = this.mLegend.getDirection();
        final float convertDpToPixel3 = Utils.convertDpToPixel(this.mLegend.getFormSize());
        final float convertDpToPixel4 = Utils.convertDpToPixel(this.mLegend.getStackSpace());
        final float yOffset = this.mLegend.getYOffset();
        float xOffset = this.mLegend.getXOffset();
        float n3 = 0.0f;
        switch (LegendRenderer$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[horizontalAlignment.ordinal()]) {
            default: {
                n3 = 0.0f;
                break;
            }
            case 3: {
                float n4;
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    n4 = this.mViewPortHandler.getChartWidth() / 2.0f;
                }
                else {
                    n4 = this.mViewPortHandler.contentLeft() + this.mViewPortHandler.contentWidth() / 2.0f;
                }
                float n5;
                if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                    n5 = xOffset;
                }
                else {
                    n5 = -xOffset;
                }
                n3 = n5 + n4;
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    final double n6 = n3;
                    double n7;
                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        n7 = -this.mLegend.mNeededWidth / 2.0 + xOffset;
                    }
                    else {
                        n7 = this.mLegend.mNeededWidth / 2.0 - xOffset;
                    }
                    n3 = (float)(n6 + n7);
                    break;
                }
                break;
            }
            case 2: {
                float n8;
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    n8 = this.mViewPortHandler.getChartWidth() - xOffset;
                }
                else {
                    n8 = this.mViewPortHandler.contentRight() - xOffset;
                }
                n3 = n8;
                if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                    n3 = n8 - this.mLegend.mNeededWidth;
                    break;
                }
                break;
            }
            case 1: {
                if (orientation != Legend.LegendOrientation.VERTICAL) {
                    xOffset += this.mViewPortHandler.contentLeft();
                }
                n3 = xOffset;
                if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                    n3 = xOffset + this.mLegend.mNeededWidth;
                    break;
                }
                break;
            }
        }
        final float n9 = convertDpToPixel;
        Label_1697: {
            switch (LegendRenderer$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[orientation.ordinal()]) {
                case 2: {
                    float n10 = 0.0f;
                    switch (LegendRenderer$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[verticalAlignment.ordinal()]) {
                        default: {
                            n10 = 0.0f;
                            break;
                        }
                        case 3: {
                            n10 = this.mViewPortHandler.getChartHeight() / 2.0f - this.mLegend.mNeededHeight / 2.0f + this.mLegend.getYOffset();
                            break;
                        }
                        case 2: {
                            float n11;
                            if (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER) {
                                n11 = this.mViewPortHandler.getChartHeight();
                            }
                            else {
                                n11 = this.mViewPortHandler.contentBottom();
                            }
                            n10 = n11 - (this.mLegend.mNeededHeight + yOffset);
                            break;
                        }
                        case 1: {
                            float contentTop;
                            if (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER) {
                                contentTop = 0.0f;
                            }
                            else {
                                contentTop = this.mViewPortHandler.contentTop();
                            }
                            n10 = contentTop + yOffset;
                            break;
                        }
                    }
                    final float n12 = n10;
                    float n13 = 0.0f;
                    int i = 0;
                    int n14 = 0;
                    float n15 = convertDpToPixel4;
                    float n16 = n12;
                    final Legend.LegendDirection legendDirection = direction;
                    while (i < entries.length) {
                        final LegendEntry legendEntry = entries[i];
                        final boolean b = legendEntry.form != Legend.LegendForm.NONE;
                        float convertDpToPixel5;
                        if (Float.isNaN(legendEntry.formSize)) {
                            convertDpToPixel5 = convertDpToPixel3;
                        }
                        else {
                            convertDpToPixel5 = Utils.convertDpToPixel(legendEntry.formSize);
                        }
                        float n19;
                        if (b) {
                            float n17;
                            if (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                n17 = n3 + n13;
                            }
                            else {
                                n17 = n3 - (convertDpToPixel5 - n13);
                            }
                            final float n18 = n17;
                            this.drawForm(canvas, n18, n16 + n2, legendEntry, this.mLegend);
                            n19 = n18;
                            if (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                n19 = n18 + convertDpToPixel5;
                            }
                        }
                        else {
                            n19 = n3;
                        }
                        final float n20 = n15;
                        float n24;
                        if (legendEntry.label != null) {
                            float n22;
                            if (b && n14 == 0) {
                                float n21;
                                if (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                    n21 = n9;
                                }
                                else {
                                    n21 = -n9;
                                }
                                n22 = n19 + n21;
                            }
                            else if (n14 != 0) {
                                n22 = n3;
                            }
                            else {
                                n22 = n19;
                            }
                            float n23 = n22;
                            if (legendDirection == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                n23 = n22 - Utils.calcTextWidth(this.mLegendLabelPaint, legendEntry.label);
                            }
                            if (n14 == 0) {
                                this.drawLabel(canvas, n23, n16 + lineHeight, legendEntry.label);
                            }
                            else {
                                n16 += lineHeight + n;
                                this.drawLabel(canvas, n23, n16 + lineHeight, legendEntry.label);
                            }
                            n16 += lineHeight + n;
                            n24 = 0.0f;
                        }
                        else {
                            n24 = n13 + (convertDpToPixel5 + n20);
                            n14 = 1;
                        }
                        ++i;
                        n13 = n24;
                        n15 = n20;
                    }
                    break;
                }
                case 1: {
                    final float n25 = n9;
                    final List<FSize> calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
                    final List<FSize> calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
                    final List<Boolean> calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
                    float n26 = yOffset;
                    while (true) {
                        switch (LegendRenderer$1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[verticalAlignment.ordinal()]) {
                            default: {
                                n26 = 0.0f;
                            }
                            case 1: {
                                final int length = entries.length;
                                final float n27 = n26;
                                float n28 = n3;
                                int j = 0;
                                int n29 = 0;
                                final float n30 = convertDpToPixel2;
                                final float n31 = n2;
                                final float n32 = n3;
                                float n33 = n27;
                                while (j < length) {
                                    final LegendEntry legendEntry2 = entries[j];
                                    final boolean b2 = legendEntry2.form != Legend.LegendForm.NONE;
                                    float convertDpToPixel6;
                                    if (Float.isNaN(legendEntry2.formSize)) {
                                        convertDpToPixel6 = convertDpToPixel3;
                                    }
                                    else {
                                        convertDpToPixel6 = Utils.convertDpToPixel(legendEntry2.formSize);
                                    }
                                    float n34;
                                    float n35;
                                    if (j < calculatedLabelBreakPoints.size() && calculatedLabelBreakPoints.get(j)) {
                                        n34 = n33 + (lineHeight + n);
                                        n35 = n32;
                                    }
                                    else {
                                        n34 = n33;
                                        n35 = n28;
                                    }
                                    if (n35 == n32 && horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && n29 < calculatedLineSizes.size()) {
                                        float width;
                                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                            width = calculatedLineSizes.get(n29).width;
                                        }
                                        else {
                                            width = -calculatedLineSizes.get(n29).width;
                                        }
                                        n35 += width / 2.0f;
                                        ++n29;
                                    }
                                    final boolean b3 = legendEntry2.label == null;
                                    if (b2) {
                                        float n36 = n35;
                                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                            n36 = n35 - convertDpToPixel6;
                                        }
                                        this.drawForm(canvas, n36, n34 + n31, legendEntry2, this.mLegend);
                                        n35 = n36;
                                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                            n35 = n36 + convertDpToPixel6;
                                        }
                                    }
                                    if (!b3) {
                                        float n37 = n35;
                                        if (b2) {
                                            float n38;
                                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                                n38 = -n25;
                                            }
                                            else {
                                                n38 = n25;
                                            }
                                            n37 = n35 + n38;
                                        }
                                        float n39 = n37;
                                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                            n39 = n37 - calculatedLabelSizes.get(j).width;
                                        }
                                        final float n40 = n39;
                                        this.drawLabel(canvas, n40, n34 + lineHeight, legendEntry2.label);
                                        float n41 = n40;
                                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                            n41 = n40 + calculatedLabelSizes.get(j).width;
                                        }
                                        float n42;
                                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                            n42 = -n30;
                                        }
                                        else {
                                            n42 = n30;
                                        }
                                        n28 = n41 + n42;
                                    }
                                    else {
                                        float n43;
                                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                            n43 = -convertDpToPixel4;
                                        }
                                        else {
                                            n43 = convertDpToPixel4;
                                        }
                                        n28 = n35 + n43;
                                    }
                                    ++j;
                                    n33 = n34;
                                }
                                break Label_1697;
                            }
                            case 3: {
                                n26 = yOffset + (this.mViewPortHandler.getChartHeight() - this.mLegend.mNeededHeight) / 2.0f;
                                continue;
                            }
                            case 2: {
                                n26 = this.mViewPortHandler.getChartHeight() - yOffset - this.mLegend.mNeededHeight;
                                continue;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
}
