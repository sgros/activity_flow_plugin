// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import android.graphics.Color;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarDataSet extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet
{
    private int mBarBorderColor;
    private float mBarBorderWidth;
    private int mBarShadowColor;
    private int mEntryCountStacks;
    private int mHighLightAlpha;
    private String[] mStackLabels;
    private int mStackSize;
    
    public BarDataSet(final List<BarEntry> list, final String s) {
        super(list, s);
        this.mStackSize = 1;
        this.mBarShadowColor = Color.rgb(215, 215, 215);
        this.mBarBorderWidth = 0.0f;
        this.mBarBorderColor = -16777216;
        this.mHighLightAlpha = 120;
        this.mEntryCountStacks = 0;
        this.mStackLabels = new String[] { "Stack" };
        this.mHighLightColor = Color.rgb(0, 0, 0);
        this.calcStackSize(list);
        this.calcEntryCountIncludingStacks(list);
    }
    
    private void calcEntryCountIncludingStacks(final List<BarEntry> list) {
        int i = 0;
        this.mEntryCountStacks = 0;
        while (i < list.size()) {
            final float[] yVals = list.get(i).getYVals();
            if (yVals == null) {
                ++this.mEntryCountStacks;
            }
            else {
                this.mEntryCountStacks += yVals.length;
            }
            ++i;
        }
    }
    
    private void calcStackSize(final List<BarEntry> list) {
        for (int i = 0; i < list.size(); ++i) {
            final float[] yVals = list.get(i).getYVals();
            if (yVals != null && yVals.length > this.mStackSize) {
                this.mStackSize = yVals.length;
            }
        }
    }
    
    @Override
    protected void calcMinMax(final BarEntry barEntry) {
        if (barEntry != null && !Float.isNaN(barEntry.getY())) {
            if (barEntry.getYVals() == null) {
                if (barEntry.getY() < this.mYMin) {
                    this.mYMin = barEntry.getY();
                }
                if (barEntry.getY() > this.mYMax) {
                    this.mYMax = barEntry.getY();
                }
            }
            else {
                if (-barEntry.getNegativeSum() < this.mYMin) {
                    this.mYMin = -barEntry.getNegativeSum();
                }
                if (barEntry.getPositiveSum() > this.mYMax) {
                    this.mYMax = barEntry.getPositiveSum();
                }
            }
            this.calcMinMaxX(barEntry);
        }
    }
    
    @Override
    public DataSet<BarEntry> copy() {
        final ArrayList<BarEntry> list = new ArrayList<BarEntry>();
        list.clear();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(((BarEntry)this.mValues.get(i)).copy());
        }
        final BarDataSet set = new BarDataSet(list, this.getLabel());
        set.mColors = this.mColors;
        set.mStackSize = this.mStackSize;
        set.mBarShadowColor = this.mBarShadowColor;
        set.mStackLabels = this.mStackLabels;
        set.mHighLightColor = this.mHighLightColor;
        set.mHighLightAlpha = this.mHighLightAlpha;
        return set;
    }
    
    @Override
    public int getBarBorderColor() {
        return this.mBarBorderColor;
    }
    
    @Override
    public float getBarBorderWidth() {
        return this.mBarBorderWidth;
    }
    
    @Override
    public int getBarShadowColor() {
        return this.mBarShadowColor;
    }
    
    public int getEntryCountStacks() {
        return this.mEntryCountStacks;
    }
    
    @Override
    public int getHighLightAlpha() {
        return this.mHighLightAlpha;
    }
    
    @Override
    public String[] getStackLabels() {
        return this.mStackLabels;
    }
    
    @Override
    public int getStackSize() {
        return this.mStackSize;
    }
    
    @Override
    public boolean isStacked() {
        final int mStackSize = this.mStackSize;
        boolean b = true;
        if (mStackSize <= 1) {
            b = false;
        }
        return b;
    }
    
    public void setBarBorderColor(final int mBarBorderColor) {
        this.mBarBorderColor = mBarBorderColor;
    }
    
    public void setBarBorderWidth(final float mBarBorderWidth) {
        this.mBarBorderWidth = mBarBorderWidth;
    }
    
    public void setBarShadowColor(final int mBarShadowColor) {
        this.mBarShadowColor = mBarShadowColor;
    }
    
    public void setHighLightAlpha(final int mHighLightAlpha) {
        this.mHighLightAlpha = mHighLightAlpha;
    }
    
    public void setStackLabels(final String[] mStackLabels) {
        this.mStackLabels = mStackLabels;
    }
}
