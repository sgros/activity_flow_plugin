// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import java.util.Collection;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointD;
import java.util.Iterator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider> implements IHighlighter
{
    protected T mChart;
    protected List<Highlight> mHighlightBuffer;
    
    public ChartHighlighter(final T mChart) {
        this.mHighlightBuffer = new ArrayList<Highlight>();
        this.mChart = mChart;
    }
    
    protected List<Highlight> buildHighlights(final IDataSet set, final int n, final float n2, final DataSet.Rounding rounding) {
        final ArrayList<Highlight> list = new ArrayList<Highlight>();
        List<Entry> list3;
        final List<Entry> list2 = list3 = set.getEntriesForXValue(n2);
        if (list2.size() == 0) {
            final Entry entryForXValue = set.getEntryForXValue(n2, Float.NaN, rounding);
            list3 = list2;
            if (entryForXValue != null) {
                list3 = set.getEntriesForXValue(entryForXValue.getX());
            }
        }
        if (list3.size() == 0) {
            return list;
        }
        for (final Entry entry : list3) {
            final MPPointD pixelForValues = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entry.getX(), entry.getY());
            list.add(new Highlight(entry.getX(), entry.getY(), (float)pixelForValues.x, (float)pixelForValues.y, n, set.getAxisDependency()));
        }
        return list;
    }
    
    public Highlight getClosestHighlightByPixel(final List<Highlight> list, final float n, final float n2, final YAxis.AxisDependency axisDependency, float n3) {
        Highlight highlight = null;
        Highlight highlight3;
        float n4;
        for (int i = 0; i < list.size(); ++i, highlight = highlight3, n3 = n4) {
            final Highlight highlight2 = list.get(i);
            if (axisDependency != null) {
                highlight3 = highlight;
                n4 = n3;
                if (highlight2.getAxis() != axisDependency) {
                    continue;
                }
            }
            final float distance = this.getDistance(n, n2, highlight2.getXPx(), highlight2.getYPx());
            highlight3 = highlight;
            n4 = n3;
            if (distance < n3) {
                highlight3 = highlight2;
                n4 = distance;
            }
        }
        return highlight;
    }
    
    protected BarLineScatterCandleBubbleData getData() {
        return this.mChart.getData();
    }
    
    protected float getDistance(final float n, final float n2, final float n3, final float n4) {
        return (float)Math.hypot(n - n3, n2 - n4);
    }
    
    @Override
    public Highlight getHighlight(final float n, final float n2) {
        final MPPointD valsForTouch = this.getValsForTouch(n, n2);
        final float n3 = (float)valsForTouch.x;
        MPPointD.recycleInstance(valsForTouch);
        return this.getHighlightForX(n3, n, n2);
    }
    
    protected Highlight getHighlightForX(final float n, final float n2, final float n3) {
        final List<Highlight> highlightsAtXValue = this.getHighlightsAtXValue(n, n2, n3);
        if (highlightsAtXValue.isEmpty()) {
            return null;
        }
        YAxis.AxisDependency axisDependency;
        if (this.getMinimumDistance(highlightsAtXValue, n3, YAxis.AxisDependency.LEFT) < this.getMinimumDistance(highlightsAtXValue, n3, YAxis.AxisDependency.RIGHT)) {
            axisDependency = YAxis.AxisDependency.LEFT;
        }
        else {
            axisDependency = YAxis.AxisDependency.RIGHT;
        }
        return this.getClosestHighlightByPixel(highlightsAtXValue, n2, n3, axisDependency, this.mChart.getMaxHighlightDistance());
    }
    
    protected float getHighlightPos(final Highlight highlight) {
        return highlight.getYPx();
    }
    
    protected List<Highlight> getHighlightsAtXValue(final float n, final float n2, final float n3) {
        this.mHighlightBuffer.clear();
        final BarLineScatterCandleBubbleData data = this.getData();
        if (data == null) {
            return this.mHighlightBuffer;
        }
        for (int i = 0; i < data.getDataSetCount(); ++i) {
            final IDataSet dataSetByIndex = data.getDataSetByIndex(i);
            if (dataSetByIndex.isHighlightEnabled()) {
                this.mHighlightBuffer.addAll(this.buildHighlights(dataSetByIndex, i, n, DataSet.Rounding.CLOSEST));
            }
        }
        return this.mHighlightBuffer;
    }
    
    protected float getMinimumDistance(final List<Highlight> list, final float n, final YAxis.AxisDependency axisDependency) {
        float n2 = Float.MAX_VALUE;
        float n3;
        for (int i = 0; i < list.size(); ++i, n2 = n3) {
            final Highlight highlight = list.get(i);
            n3 = n2;
            if (highlight.getAxis() == axisDependency) {
                final float abs = Math.abs(this.getHighlightPos(highlight) - n);
                n3 = n2;
                if (abs < n2) {
                    n3 = abs;
                }
            }
        }
        return n2;
    }
    
    protected MPPointD getValsForTouch(final float n, final float n2) {
        return this.mChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(n, n2);
    }
}
