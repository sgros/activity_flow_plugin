// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import android.util.Log;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import android.graphics.DashPathEffect;

public abstract class AxisBase extends ComponentBase
{
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
        this.mLimitLines = new ArrayList<LimitLine>();
    }
    
    public void addLimitLine(final LimitLine limitLine) {
        this.mLimitLines.add(limitLine);
        if (this.mLimitLines.size() > 6) {
            Log.e("MPAndroiChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
        }
    }
    
    public void calculate(float mAxisMinimum, float mAxisMaximum) {
        if (this.mCustomAxisMin) {
            mAxisMinimum = this.mAxisMinimum;
        }
        else {
            mAxisMinimum -= this.mSpaceMin;
        }
        if (this.mCustomAxisMax) {
            mAxisMaximum = this.mAxisMaximum;
        }
        else {
            mAxisMaximum += this.mSpaceMax;
        }
        float mAxisMinimum2 = mAxisMinimum;
        float mAxisMaximum2 = mAxisMaximum;
        if (Math.abs(mAxisMaximum - mAxisMinimum) == 0.0f) {
            mAxisMaximum2 = mAxisMaximum + 1.0f;
            mAxisMinimum2 = mAxisMinimum - 1.0f;
        }
        this.mAxisMinimum = mAxisMinimum2;
        this.mAxisMaximum = mAxisMaximum2;
        this.mAxisRange = Math.abs(mAxisMaximum2 - mAxisMinimum2);
    }
    
    public void disableAxisLineDashedLine() {
        this.mAxisLineDashPathEffect = null;
    }
    
    public void disableGridDashedLine() {
        this.mGridDashPathEffect = null;
    }
    
    public void enableAxisLineDashedLine(final float n, final float n2, final float n3) {
        this.mAxisLineDashPathEffect = new DashPathEffect(new float[] { n, n2 }, n3);
    }
    
    public void enableGridDashedLine(final float n, final float n2, final float n3) {
        this.mGridDashPathEffect = new DashPathEffect(new float[] { n, n2 }, n3);
    }
    
    public int getAxisLineColor() {
        return this.mAxisLineColor;
    }
    
    public DashPathEffect getAxisLineDashPathEffect() {
        return this.mAxisLineDashPathEffect;
    }
    
    public float getAxisLineWidth() {
        return this.mAxisLineWidth;
    }
    
    public float getAxisMaximum() {
        return this.mAxisMaximum;
    }
    
    public float getAxisMinimum() {
        return this.mAxisMinimum;
    }
    
    public String getFormattedLabel(final int n) {
        if (n >= 0 && n < this.mEntries.length) {
            return this.getValueFormatter().getFormattedValue(this.mEntries[n], this);
        }
        return "";
    }
    
    public float getGranularity() {
        return this.mGranularity;
    }
    
    public int getGridColor() {
        return this.mGridColor;
    }
    
    public DashPathEffect getGridDashPathEffect() {
        return this.mGridDashPathEffect;
    }
    
    public float getGridLineWidth() {
        return this.mGridLineWidth;
    }
    
    public int getLabelCount() {
        return this.mLabelCount;
    }
    
    public List<LimitLine> getLimitLines() {
        return this.mLimitLines;
    }
    
    public String getLongestLabel() {
        String s = "";
        String s2;
        for (int i = 0; i < this.mEntries.length; ++i, s = s2) {
            final String formattedLabel = this.getFormattedLabel(i);
            s2 = s;
            if (formattedLabel != null) {
                s2 = s;
                if (s.length() < formattedLabel.length()) {
                    s2 = formattedLabel;
                }
            }
        }
        return s;
    }
    
    public float getSpaceMax() {
        return this.mSpaceMax;
    }
    
    public float getSpaceMin() {
        return this.mSpaceMin;
    }
    
    public IAxisValueFormatter getValueFormatter() {
        if (this.mAxisValueFormatter == null || (this.mAxisValueFormatter instanceof DefaultAxisValueFormatter && ((DefaultAxisValueFormatter)this.mAxisValueFormatter).getDecimalDigits() != this.mDecimals)) {
            this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
        }
        return this.mAxisValueFormatter;
    }
    
    public boolean isAxisLineDashedLineEnabled() {
        return this.mAxisLineDashPathEffect != null;
    }
    
    public boolean isAxisMaxCustom() {
        return this.mCustomAxisMax;
    }
    
    public boolean isAxisMinCustom() {
        return this.mCustomAxisMin;
    }
    
    public boolean isCenterAxisLabelsEnabled() {
        return this.mCenterAxisLabels && this.mEntryCount > 0;
    }
    
    public boolean isDrawAxisLineEnabled() {
        return this.mDrawAxisLine;
    }
    
    public boolean isDrawGridLinesEnabled() {
        return this.mDrawGridLines;
    }
    
    public boolean isDrawLabelsEnabled() {
        return this.mDrawLabels;
    }
    
    public boolean isDrawLimitLinesBehindDataEnabled() {
        return this.mDrawLimitLineBehindData;
    }
    
    public boolean isForceLabelsEnabled() {
        return this.mForceLabels;
    }
    
    public boolean isGranularityEnabled() {
        return this.mGranularityEnabled;
    }
    
    public boolean isGridDashedLineEnabled() {
        return this.mGridDashPathEffect != null;
    }
    
    public void removeAllLimitLines() {
        this.mLimitLines.clear();
    }
    
    public void removeLimitLine(final LimitLine limitLine) {
        this.mLimitLines.remove(limitLine);
    }
    
    public void resetAxisMaximum() {
        this.mCustomAxisMax = false;
    }
    
    public void resetAxisMinimum() {
        this.mCustomAxisMin = false;
    }
    
    public void setAxisLineColor(final int mAxisLineColor) {
        this.mAxisLineColor = mAxisLineColor;
    }
    
    public void setAxisLineDashedLine(final DashPathEffect mAxisLineDashPathEffect) {
        this.mAxisLineDashPathEffect = mAxisLineDashPathEffect;
    }
    
    public void setAxisLineWidth(final float n) {
        this.mAxisLineWidth = Utils.convertDpToPixel(n);
    }
    
    @Deprecated
    public void setAxisMaxValue(final float axisMaximum) {
        this.setAxisMaximum(axisMaximum);
    }
    
    public void setAxisMaximum(final float mAxisMaximum) {
        this.mCustomAxisMax = true;
        this.mAxisMaximum = mAxisMaximum;
        this.mAxisRange = Math.abs(mAxisMaximum - this.mAxisMinimum);
    }
    
    @Deprecated
    public void setAxisMinValue(final float axisMinimum) {
        this.setAxisMinimum(axisMinimum);
    }
    
    public void setAxisMinimum(final float mAxisMinimum) {
        this.mCustomAxisMin = true;
        this.mAxisMinimum = mAxisMinimum;
        this.mAxisRange = Math.abs(this.mAxisMaximum - mAxisMinimum);
    }
    
    public void setCenterAxisLabels(final boolean mCenterAxisLabels) {
        this.mCenterAxisLabels = mCenterAxisLabels;
    }
    
    public void setDrawAxisLine(final boolean mDrawAxisLine) {
        this.mDrawAxisLine = mDrawAxisLine;
    }
    
    public void setDrawGridLines(final boolean mDrawGridLines) {
        this.mDrawGridLines = mDrawGridLines;
    }
    
    public void setDrawLabels(final boolean mDrawLabels) {
        this.mDrawLabels = mDrawLabels;
    }
    
    public void setDrawLimitLinesBehindData(final boolean mDrawLimitLineBehindData) {
        this.mDrawLimitLineBehindData = mDrawLimitLineBehindData;
    }
    
    public void setGranularity(final float mGranularity) {
        this.mGranularity = mGranularity;
        this.mGranularityEnabled = true;
    }
    
    public void setGranularityEnabled(final boolean mGranularityEnabled) {
        this.mGranularityEnabled = mGranularityEnabled;
    }
    
    public void setGridColor(final int mGridColor) {
        this.mGridColor = mGridColor;
    }
    
    public void setGridDashedLine(final DashPathEffect mGridDashPathEffect) {
        this.mGridDashPathEffect = mGridDashPathEffect;
    }
    
    public void setGridLineWidth(final float n) {
        this.mGridLineWidth = Utils.convertDpToPixel(n);
    }
    
    public void setLabelCount(int mLabelCount) {
        int n = mLabelCount;
        if (mLabelCount > 25) {
            n = 25;
        }
        if ((mLabelCount = n) < 2) {
            mLabelCount = 2;
        }
        this.mLabelCount = mLabelCount;
        this.mForceLabels = false;
    }
    
    public void setLabelCount(final int labelCount, final boolean mForceLabels) {
        this.setLabelCount(labelCount);
        this.mForceLabels = mForceLabels;
    }
    
    public void setSpaceMax(final float mSpaceMax) {
        this.mSpaceMax = mSpaceMax;
    }
    
    public void setSpaceMin(final float mSpaceMin) {
        this.mSpaceMin = mSpaceMin;
    }
    
    public void setValueFormatter(final IAxisValueFormatter mAxisValueFormatter) {
        if (mAxisValueFormatter == null) {
            this.mAxisValueFormatter = new DefaultAxisValueFormatter(this.mDecimals);
        }
        else {
            this.mAxisValueFormatter = mAxisValueFormatter;
        }
    }
}
