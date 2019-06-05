// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import java.util.Iterator;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import java.util.List;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;

public class CombinedHighlighter extends ChartHighlighter<CombinedDataProvider> implements IHighlighter
{
    protected BarHighlighter barHighlighter;
    
    public CombinedHighlighter(final CombinedDataProvider combinedDataProvider, final BarDataProvider barDataProvider) {
        super(combinedDataProvider);
        BarHighlighter barHighlighter;
        if (barDataProvider.getBarData() == null) {
            barHighlighter = null;
        }
        else {
            barHighlighter = new BarHighlighter(barDataProvider);
        }
        this.barHighlighter = barHighlighter;
    }
    
    @Override
    protected List<Highlight> getHighlightsAtXValue(final float n, final float n2, final float n3) {
        this.mHighlightBuffer.clear();
        final List<BarLineScatterCandleBubbleData> allData = ((CombinedDataProvider)this.mChart).getCombinedData().getAllData();
        for (int i = 0; i < allData.size(); ++i) {
            final BarLineScatterCandleBubbleData<IDataSet> barLineScatterCandleBubbleData = allData.get(i);
            if (this.barHighlighter != null && barLineScatterCandleBubbleData instanceof BarData) {
                final Highlight highlight = this.barHighlighter.getHighlight(n2, n3);
                if (highlight != null) {
                    highlight.setDataIndex(i);
                    this.mHighlightBuffer.add(highlight);
                }
            }
            else {
                for (int dataSetCount = barLineScatterCandleBubbleData.getDataSetCount(), j = 0; j < dataSetCount; ++j) {
                    final IDataSet dataSetByIndex = allData.get(i).getDataSetByIndex(j);
                    if (dataSetByIndex.isHighlightEnabled()) {
                        for (final Highlight highlight2 : this.buildHighlights(dataSetByIndex, j, n, DataSet.Rounding.CLOSEST)) {
                            highlight2.setDataIndex(i);
                            this.mHighlightBuffer.add(highlight2);
                        }
                    }
                }
            }
        }
        return this.mHighlightBuffer;
    }
}
