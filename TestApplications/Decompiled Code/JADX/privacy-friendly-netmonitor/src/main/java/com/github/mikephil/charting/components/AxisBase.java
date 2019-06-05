package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.util.Log;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class AxisBase extends ComponentBase {
    private int mAxisLineColor;
    private DashPathEffect mAxisLineDashPathEffect;
    private float mAxisLineWidth;
    public float mAxisMaximum;
    public float mAxisMinimum;
    public float mAxisRange;
    protected IAxisValueFormatter mAxisValueFormatter;
    protected boolean mCenterAxisLabels;
    public float[] mCenteredEntries;
    protected boolean mCustomAxisMax;
    protected boolean mCustomAxisMin;
    public int mDecimals;
    protected boolean mDrawAxisLine;
    protected boolean mDrawGridLines;
    protected boolean mDrawLabels;
    protected boolean mDrawLimitLineBehindData;
    public float[] mEntries;
    public int mEntryCount;
    protected boolean mForceLabels;
    protected float mGranularity;
    protected boolean mGranularityEnabled;
    private int mGridColor;
    private DashPathEffect mGridDashPathEffect;
    private float mGridLineWidth;
    private int mLabelCount;
    protected List<LimitLine> mLimitLines;
    protected float mSpaceMax;
    protected float mSpaceMin;

    public AxisBase() {
        this.mGridColor = -7829368;
        this.mGridLineWidth = 1.0f;
        this.mAxisLineColor = -7829368;
        this.mAxisLineWidth = 1.0f;
        this.mEntries = new float[0];
        this.mCenteredEntries = new float[0];
        this.mLabelCount = 6;
        this.mGranularity = 1.0f;
        this.mGranularityEnabled = false;
        this.mForceLabels = false;
        this.mDrawGridLines = true;
        this.mDrawAxisLine = true;
        this.mDrawLabels = true;
        this.mCenterAxisLabels = false;
        this.mAxisLineDashPathEffect = null;
        this.mGridDashPathEffect = null;
        this.mDrawLimitLineBehindData = false;
        this.mSpaceMin = 0.0f;
        this.mSpaceMax = 0.0f;
        this.mCustomAxisMin = false;
        this.mCustomAxisMax = false;
        this.mAxisMaximum = 0.0f;
        this.mAxisMinimum = 0.0f;
        this.mAxisRange = 0.0f;
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(5.0f);
        this.mLimitLines = new ArrayList();
    }

    public void setDrawGridLines(boolean z) {
        this.mDrawGridLines = z;
    }

    public boolean isDrawGridLinesEnabled() {
        return this.mDrawGridLines;
    }

    public void setDrawAxisLine(boolean z) {
        this.mDrawAxisLine = z;
    }

    public boolean isDrawAxisLineEnabled() {
        return this.mDrawAxisLine;
    }

    public void setCenterAxisLabels(boolean z) {
        this.mCenterAxisLabels = z;
    }

    public boolean isCenterAxisLabelsEnabled() {
        return this.mCenterAxisLabels && this.mEntryCount > 0;
    }

    public void setGridColor(int i) {
        this.mGridColor = i;
    }

    public int getGridColor() {
        return this.mGridColor;
    }

    public void setAxisLineWidth(float f) {
        this.mAxisLineWidth = Utils.convertDpToPixel(f);
    }

    public float getAxisLineWidth() {
        return this.mAxisLineWidth;
    }

    public void setGridLineWidth(float f) {
        this.mGridLineWidth = Utils.convertDpToPixel(f);
    }

    public float getGridLineWidth() {
        return this.mGridLineWidth;
    }

    public void setAxisLineColor(int i) {
        this.mAxisLineColor = i;
    }

    public int getAxisLineColor() {
        return this.mAxisLineColor;
    }

    public void setDrawLabels(boolean z) {
        this.mDrawLabels = z;
    }

    public boolean isDrawLabelsEnabled() {
        return this.mDrawLabels;
    }

    public void setLabelCount(int i) {
        if (i > 25) {
            i = 25;
        }
        if (i < 2) {
            i = 2;
        }
        this.mLabelCount = i;
        this.mForceLabels = false;
    }

    public void setLabelCount(int i, boolean z) {
        setLabelCount(i);
        this.mForceLabels = z;
    }

    public boolean isForceLabelsEnabled() {
        return this.mForceLabels;
    }

    public int getLabelCount() {
        return this.mLabelCount;
    }

    public boolean isGranularityEnabled() {
        return this.mGranularityEnabled;
    }

    public void setGranularityEnabled(boolean z) {
        this.mGranularityEnabled = z;
    }

    public float getGranularity() {
        return this.mGranularity;
    }

    public void setGranularity(float f) {
        this.mGranularity = f;
        this.mGranularityEnabled = true;
    }

    public void addLimitLine(LimitLine limitLine) {
        this.mLimitLines.add(limitLine);
        if (this.mLimitLines.size() > 6) {
            Log.e("MPAndroiChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
        }
    }

    public void removeLimitLine(LimitLine limitLine) {
        this.mLimitLines.remove(limitLine);
    }

    public void removeAllLimitLines() {
        this.mLimitLines.clear();
    }

    public List<LimitLine> getLimitLines() {
        return this.mLimitLines;
    }

    public void setDrawLimitLinesBehindData(boolean z) {
        this.mDrawLimitLineBehindData = z;
    }

    public boolean isDrawLimitLinesBehindDataEnabled() {
        return this.mDrawLimitLineBehindData;
    }

    public String getLongestLabel() {
        String str = "";
        for (int i = 0; i < this.mEntries.length; i++) {
            String formattedLabel = getFormattedLabel(i);
            if (formattedLabel != null && str.length() < formattedLabel.length()) {
                str = formattedLabel;
            }
        }
        return str;
    }

    public String getFormattedLabel(int i) {
        return (i < 0 || i >= this.mEntries.length) ? "" : getValueFormatter().getFormattedValue(this.mEntries[i], this);
    }

    public void setValueFormatter(IAxisValueFormatter iAxisValueFormatter) {
        if (iAxisValueFormatter == null) {
            this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
        } else {
            this.mAxisValueFormatter = iAxisValueFormatter;
        }
    }

    public IAxisValueFormatter getValueFormatter() {
        if (this.mAxisValueFormatter == null || ((this.mAxisValueFormatter instanceof DefaultAxisValueFormatter) && ((DefaultAxisValueFormatter) this.mAxisValueFormatter).getDecimalDigits() != this.mDecimals)) {
            this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
        }
        return this.mAxisValueFormatter;
    }

    public void enableGridDashedLine(float f, float f2, float f3) {
        this.mGridDashPathEffect = new DashPathEffect(new float[]{f, f2}, f3);
    }

    public void setGridDashedLine(DashPathEffect dashPathEffect) {
        this.mGridDashPathEffect = dashPathEffect;
    }

    public void disableGridDashedLine() {
        this.mGridDashPathEffect = null;
    }

    public boolean isGridDashedLineEnabled() {
        return this.mGridDashPathEffect != null;
    }

    public DashPathEffect getGridDashPathEffect() {
        return this.mGridDashPathEffect;
    }

    public void enableAxisLineDashedLine(float f, float f2, float f3) {
        this.mAxisLineDashPathEffect = new DashPathEffect(new float[]{f, f2}, f3);
    }

    public void setAxisLineDashedLine(DashPathEffect dashPathEffect) {
        this.mAxisLineDashPathEffect = dashPathEffect;
    }

    public void disableAxisLineDashedLine() {
        this.mAxisLineDashPathEffect = null;
    }

    public boolean isAxisLineDashedLineEnabled() {
        return this.mAxisLineDashPathEffect != null;
    }

    public DashPathEffect getAxisLineDashPathEffect() {
        return this.mAxisLineDashPathEffect;
    }

    public float getAxisMaximum() {
        return this.mAxisMaximum;
    }

    public float getAxisMinimum() {
        return this.mAxisMinimum;
    }

    public void resetAxisMaximum() {
        this.mCustomAxisMax = false;
    }

    public boolean isAxisMaxCustom() {
        return this.mCustomAxisMax;
    }

    public void resetAxisMinimum() {
        this.mCustomAxisMin = false;
    }

    public boolean isAxisMinCustom() {
        return this.mCustomAxisMin;
    }

    public void setAxisMinimum(float f) {
        this.mCustomAxisMin = true;
        this.mAxisMinimum = f;
        this.mAxisRange = Math.abs(this.mAxisMaximum - f);
    }

    @Deprecated
    public void setAxisMinValue(float f) {
        setAxisMinimum(f);
    }

    public void setAxisMaximum(float f) {
        this.mCustomAxisMax = true;
        this.mAxisMaximum = f;
        this.mAxisRange = Math.abs(f - this.mAxisMinimum);
    }

    @Deprecated
    public void setAxisMaxValue(float f) {
        setAxisMaximum(f);
    }

    public void calculate(float f, float f2) {
        f = this.mCustomAxisMin ? this.mAxisMinimum : f - this.mSpaceMin;
        f2 = this.mCustomAxisMax ? this.mAxisMaximum : f2 + this.mSpaceMax;
        if (Math.abs(f2 - f) == 0.0f) {
            f2 += 1.0f;
            f -= 1.0f;
        }
        this.mAxisMinimum = f;
        this.mAxisMaximum = f2;
        this.mAxisRange = Math.abs(f2 - f);
    }

    public float getSpaceMin() {
        return this.mSpaceMin;
    }

    public void setSpaceMin(float f) {
        this.mSpaceMin = f;
    }

    public float getSpaceMax() {
        return this.mSpaceMax;
    }

    public void setSpaceMax(float f) {
        this.mSpaceMax = f;
    }
}
