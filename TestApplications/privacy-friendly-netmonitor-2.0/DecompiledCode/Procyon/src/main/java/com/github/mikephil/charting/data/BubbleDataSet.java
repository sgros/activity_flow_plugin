// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet
{
    private float mHighlightCircleWidth;
    protected float mMaxSize;
    protected boolean mNormalizeSize;
    
    public BubbleDataSet(final List<BubbleEntry> list, final String s) {
        super(list, s);
        this.mNormalizeSize = true;
        this.mHighlightCircleWidth = 2.5f;
    }
    
    @Override
    protected void calcMinMax(final BubbleEntry bubbleEntry) {
        super.calcMinMax(bubbleEntry);
        final float size = bubbleEntry.getSize();
        if (size > this.mMaxSize) {
            this.mMaxSize = size;
        }
    }
    
    @Override
    public DataSet<BubbleEntry> copy() {
        final ArrayList<BubbleEntry> list = new ArrayList<BubbleEntry>();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(((BubbleEntry)this.mValues.get(i)).copy());
        }
        final BubbleDataSet set = new BubbleDataSet(list, this.getLabel());
        set.mColors = this.mColors;
        set.mHighLightColor = this.mHighLightColor;
        return set;
    }
    
    @Override
    public float getHighlightCircleWidth() {
        return this.mHighlightCircleWidth;
    }
    
    @Override
    public float getMaxSize() {
        return this.mMaxSize;
    }
    
    @Override
    public boolean isNormalizeSizeEnabled() {
        return this.mNormalizeSize;
    }
    
    @Override
    public void setHighlightCircleWidth(final float n) {
        this.mHighlightCircleWidth = Utils.convertDpToPixel(n);
    }
    
    public void setNormalizeSizeEnabled(final boolean mNormalizeSize) {
        this.mNormalizeSize = mNormalizeSize;
    }
}
