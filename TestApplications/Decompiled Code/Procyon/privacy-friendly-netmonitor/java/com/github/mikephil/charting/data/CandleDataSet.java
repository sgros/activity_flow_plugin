// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Paint$Style;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

public class CandleDataSet extends LineScatterCandleRadarDataSet<CandleEntry> implements ICandleDataSet
{
    private float mBarSpace;
    protected int mDecreasingColor;
    protected Paint$Style mDecreasingPaintStyle;
    protected int mIncreasingColor;
    protected Paint$Style mIncreasingPaintStyle;
    protected int mNeutralColor;
    protected int mShadowColor;
    private boolean mShadowColorSameAsCandle;
    private float mShadowWidth;
    private boolean mShowCandleBar;
    
    public CandleDataSet(final List<CandleEntry> list, final String s) {
        super(list, s);
        this.mShadowWidth = 3.0f;
        this.mShowCandleBar = true;
        this.mBarSpace = 0.1f;
        this.mShadowColorSameAsCandle = false;
        this.mIncreasingPaintStyle = Paint$Style.STROKE;
        this.mDecreasingPaintStyle = Paint$Style.FILL;
        this.mNeutralColor = 1122868;
        this.mIncreasingColor = 1122868;
        this.mDecreasingColor = 1122868;
        this.mShadowColor = 1122868;
    }
    
    @Override
    protected void calcMinMax(final CandleEntry candleEntry) {
        if (candleEntry.getLow() < this.mYMin) {
            this.mYMin = candleEntry.getLow();
        }
        if (candleEntry.getHigh() > this.mYMax) {
            this.mYMax = candleEntry.getHigh();
        }
        this.calcMinMaxX(candleEntry);
    }
    
    @Override
    protected void calcMinMaxY(final CandleEntry candleEntry) {
        if (candleEntry.getHigh() < this.mYMin) {
            this.mYMin = candleEntry.getHigh();
        }
        if (candleEntry.getHigh() > this.mYMax) {
            this.mYMax = candleEntry.getHigh();
        }
        if (candleEntry.getLow() < this.mYMin) {
            this.mYMin = candleEntry.getLow();
        }
        if (candleEntry.getLow() > this.mYMax) {
            this.mYMax = candleEntry.getLow();
        }
    }
    
    @Override
    public DataSet<CandleEntry> copy() {
        final ArrayList<CandleEntry> list = new ArrayList<CandleEntry>();
        list.clear();
        for (int i = 0; i < this.mValues.size(); ++i) {
            list.add(((CandleEntry)this.mValues.get(i)).copy());
        }
        final CandleDataSet set = new CandleDataSet(list, this.getLabel());
        set.mColors = this.mColors;
        set.mShadowWidth = this.mShadowWidth;
        set.mShowCandleBar = this.mShowCandleBar;
        set.mBarSpace = this.mBarSpace;
        set.mHighLightColor = this.mHighLightColor;
        set.mIncreasingPaintStyle = this.mIncreasingPaintStyle;
        set.mDecreasingPaintStyle = this.mDecreasingPaintStyle;
        set.mShadowColor = this.mShadowColor;
        return set;
    }
    
    @Override
    public float getBarSpace() {
        return this.mBarSpace;
    }
    
    @Override
    public int getDecreasingColor() {
        return this.mDecreasingColor;
    }
    
    @Override
    public Paint$Style getDecreasingPaintStyle() {
        return this.mDecreasingPaintStyle;
    }
    
    @Override
    public int getIncreasingColor() {
        return this.mIncreasingColor;
    }
    
    @Override
    public Paint$Style getIncreasingPaintStyle() {
        return this.mIncreasingPaintStyle;
    }
    
    @Override
    public int getNeutralColor() {
        return this.mNeutralColor;
    }
    
    @Override
    public int getShadowColor() {
        return this.mShadowColor;
    }
    
    @Override
    public boolean getShadowColorSameAsCandle() {
        return this.mShadowColorSameAsCandle;
    }
    
    @Override
    public float getShadowWidth() {
        return this.mShadowWidth;
    }
    
    @Override
    public boolean getShowCandleBar() {
        return this.mShowCandleBar;
    }
    
    public void setBarSpace(float mBarSpace) {
        float n = mBarSpace;
        if (mBarSpace < 0.0f) {
            n = 0.0f;
        }
        mBarSpace = n;
        if (n > 0.45f) {
            mBarSpace = 0.45f;
        }
        this.mBarSpace = mBarSpace;
    }
    
    public void setDecreasingColor(final int mDecreasingColor) {
        this.mDecreasingColor = mDecreasingColor;
    }
    
    public void setDecreasingPaintStyle(final Paint$Style mDecreasingPaintStyle) {
        this.mDecreasingPaintStyle = mDecreasingPaintStyle;
    }
    
    public void setIncreasingColor(final int mIncreasingColor) {
        this.mIncreasingColor = mIncreasingColor;
    }
    
    public void setIncreasingPaintStyle(final Paint$Style mIncreasingPaintStyle) {
        this.mIncreasingPaintStyle = mIncreasingPaintStyle;
    }
    
    public void setNeutralColor(final int mNeutralColor) {
        this.mNeutralColor = mNeutralColor;
    }
    
    public void setShadowColor(final int mShadowColor) {
        this.mShadowColor = mShadowColor;
    }
    
    public void setShadowColorSameAsCandle(final boolean mShadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = mShadowColorSameAsCandle;
    }
    
    public void setShadowWidth(final float n) {
        this.mShadowWidth = Utils.convertDpToPixel(n);
    }
    
    public void setShowCandleBar(final boolean mShowCandleBar) {
        this.mShowCandleBar = mShowCandleBar;
    }
}
