// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.ArrayList;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>>
{
    private BarData mBarData;
    private BubbleData mBubbleData;
    private CandleData mCandleData;
    private LineData mLineData;
    private ScatterData mScatterData;
    
    public void calcMinMax() {
        if (this.mDataSets == null) {
            this.mDataSets = (List<T>)new ArrayList<IDataSet<? extends Entry>>();
        }
        this.mDataSets.clear();
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        this.mLeftAxisMax = -3.4028235E38f;
        this.mLeftAxisMin = Float.MAX_VALUE;
        this.mRightAxisMax = -3.4028235E38f;
        this.mRightAxisMin = Float.MAX_VALUE;
        for (final BarLineScatterCandleBubbleData<? extends T> barLineScatterCandleBubbleData : this.getAllData()) {
            barLineScatterCandleBubbleData.calcMinMax();
            this.mDataSets.addAll((Collection<? extends T>)barLineScatterCandleBubbleData.getDataSets());
            if (barLineScatterCandleBubbleData.getYMax() > this.mYMax) {
                this.mYMax = barLineScatterCandleBubbleData.getYMax();
            }
            if (barLineScatterCandleBubbleData.getYMin() < this.mYMin) {
                this.mYMin = barLineScatterCandleBubbleData.getYMin();
            }
            if (barLineScatterCandleBubbleData.getXMax() > this.mXMax) {
                this.mXMax = barLineScatterCandleBubbleData.getXMax();
            }
            if (barLineScatterCandleBubbleData.getXMin() < this.mXMin) {
                this.mXMin = barLineScatterCandleBubbleData.getXMin();
            }
            if (barLineScatterCandleBubbleData.mLeftAxisMax > this.mLeftAxisMax) {
                this.mLeftAxisMax = barLineScatterCandleBubbleData.mLeftAxisMax;
            }
            if (barLineScatterCandleBubbleData.mLeftAxisMin < this.mLeftAxisMin) {
                this.mLeftAxisMin = barLineScatterCandleBubbleData.mLeftAxisMin;
            }
            if (barLineScatterCandleBubbleData.mRightAxisMax > this.mRightAxisMax) {
                this.mRightAxisMax = barLineScatterCandleBubbleData.mRightAxisMax;
            }
            if (barLineScatterCandleBubbleData.mRightAxisMin < this.mRightAxisMin) {
                this.mRightAxisMin = barLineScatterCandleBubbleData.mRightAxisMin;
            }
        }
    }
    
    public List<BarLineScatterCandleBubbleData> getAllData() {
        final ArrayList<LineData> list = (ArrayList<LineData>)new ArrayList<BubbleData>();
        if (this.mLineData != null) {
            list.add((BubbleData)this.mLineData);
        }
        if (this.mBarData != null) {
            list.add((BubbleData)this.mBarData);
        }
        if (this.mScatterData != null) {
            list.add((BubbleData)this.mScatterData);
        }
        if (this.mCandleData != null) {
            list.add((BubbleData)this.mCandleData);
        }
        if (this.mBubbleData != null) {
            list.add(this.mBubbleData);
        }
        return (List<BarLineScatterCandleBubbleData>)list;
    }
    
    public BarData getBarData() {
        return this.mBarData;
    }
    
    public BubbleData getBubbleData() {
        return this.mBubbleData;
    }
    
    public CandleData getCandleData() {
        return this.mCandleData;
    }
    
    public BarLineScatterCandleBubbleData getDataByIndex(final int n) {
        return this.getAllData().get(n);
    }
    
    public int getDataIndex(final ChartData chartData) {
        return this.getAllData().indexOf(chartData);
    }
    
    public IBarLineScatterCandleBubbleDataSet<? extends Entry> getDataSetByHighlight(final Highlight highlight) {
        if (highlight.getDataIndex() >= this.getAllData().size()) {
            return null;
        }
        final BarLineScatterCandleBubbleData dataByIndex = this.getDataByIndex(highlight.getDataIndex());
        if (highlight.getDataSetIndex() >= dataByIndex.getDataSetCount()) {
            return null;
        }
        return dataByIndex.getDataSets().get(highlight.getDataSetIndex());
    }
    
    @Override
    public Entry getEntryForHighlight(final Highlight highlight) {
        if (highlight.getDataIndex() >= this.getAllData().size()) {
            return null;
        }
        final BarLineScatterCandleBubbleData dataByIndex = this.getDataByIndex(highlight.getDataIndex());
        if (highlight.getDataSetIndex() >= dataByIndex.getDataSetCount()) {
            return null;
        }
        for (final Entry entry : dataByIndex.getDataSetByIndex(highlight.getDataSetIndex()).getEntriesForXValue(highlight.getX())) {
            if (entry.getY() == highlight.getY() || Float.isNaN(highlight.getY())) {
                return entry;
            }
        }
        return null;
    }
    
    public LineData getLineData() {
        return this.mLineData;
    }
    
    public ScatterData getScatterData() {
        return this.mScatterData;
    }
    
    @Override
    public void notifyDataChanged() {
        if (this.mLineData != null) {
            this.mLineData.notifyDataChanged();
        }
        if (this.mBarData != null) {
            this.mBarData.notifyDataChanged();
        }
        if (this.mCandleData != null) {
            this.mCandleData.notifyDataChanged();
        }
        if (this.mScatterData != null) {
            this.mScatterData.notifyDataChanged();
        }
        if (this.mBubbleData != null) {
            this.mBubbleData.notifyDataChanged();
        }
        this.calcMinMax();
    }
    
    @Deprecated
    @Override
    public boolean removeDataSet(final int n) {
        Log.e("MPAndroidChart", "removeDataSet(int index) not supported for CombinedData");
        return false;
    }
    
    @Override
    public boolean removeDataSet(final IBarLineScatterCandleBubbleDataSet<? extends Entry> set) {
        final Iterator<BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>>> iterator = (Iterator<BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>>>)this.getAllData().iterator();
        boolean removeDataSet = false;
        while (iterator.hasNext()) {
            final boolean b = removeDataSet = iterator.next().removeDataSet(set);
            if (b) {
                removeDataSet = b;
                break;
            }
        }
        return removeDataSet;
    }
    
    @Deprecated
    @Override
    public boolean removeEntry(final float n, final int n2) {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }
    
    @Deprecated
    @Override
    public boolean removeEntry(final Entry entry, final int n) {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }
    
    public void setData(final BarData mBarData) {
        this.mBarData = mBarData;
        this.notifyDataChanged();
    }
    
    public void setData(final BubbleData mBubbleData) {
        this.mBubbleData = mBubbleData;
        this.notifyDataChanged();
    }
    
    public void setData(final CandleData mCandleData) {
        this.mCandleData = mCandleData;
        this.notifyDataChanged();
    }
    
    public void setData(final LineData mLineData) {
        this.mLineData = mLineData;
        this.notifyDataChanged();
    }
    
    public void setData(final ScatterData mScatterData) {
        this.mScatterData = mScatterData;
        this.notifyDataChanged();
    }
}
