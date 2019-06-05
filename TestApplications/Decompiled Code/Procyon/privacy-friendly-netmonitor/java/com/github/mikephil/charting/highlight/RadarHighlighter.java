// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.List;
import com.github.mikephil.charting.charts.RadarChart;

public class RadarHighlighter extends PieRadarHighlighter<RadarChart>
{
    public RadarHighlighter(final RadarChart radarChart) {
        super(radarChart);
    }
    
    @Override
    protected Highlight getClosestHighlight(int i, float n, float n2) {
        final List<Highlight> highlightsAtIndex = this.getHighlightsAtIndex(i);
        final float n3 = ((RadarChart)this.mChart).distanceToCenter(n, n2) / ((RadarChart)this.mChart).getFactor();
        Highlight highlight = null;
        n2 = Float.MAX_VALUE;
        Highlight highlight2;
        float abs;
        for (i = 0; i < highlightsAtIndex.size(); ++i, n2 = n) {
            highlight2 = highlightsAtIndex.get(i);
            abs = Math.abs(highlight2.getY() - n3);
            n = n2;
            if (abs < n2) {
                highlight = highlight2;
                n = abs;
            }
        }
        return highlight;
    }
    
    protected List<Highlight> getHighlightsAtIndex(final int n) {
        this.mHighlightBuffer.clear();
        final float phaseX = ((RadarChart)this.mChart).getAnimator().getPhaseX();
        final float phaseY = ((RadarChart)this.mChart).getAnimator().getPhaseY();
        final float sliceAngle = ((RadarChart)this.mChart).getSliceAngle();
        final float factor = ((RadarChart)this.mChart).getFactor();
        final MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        for (int i = 0; i < ((RadarChart)this.mChart).getData().getDataSetCount(); ++i) {
            final IRadarDataSet dataSetByIndex = ((RadarChart)this.mChart).getData().getDataSetByIndex(i);
            final Entry entryForIndex = ((IDataSet<Entry>)dataSetByIndex).getEntryForIndex(n);
            final float y = entryForIndex.getY();
            final float yChartMin = ((RadarChart)this.mChart).getYChartMin();
            final MPPointF centerOffsets = ((RadarChart)this.mChart).getCenterOffsets();
            final float n2 = (float)n;
            Utils.getPosition(centerOffsets, (y - yChartMin) * factor * phaseY, sliceAngle * n2 * phaseX + ((RadarChart)this.mChart).getRotationAngle(), instance);
            this.mHighlightBuffer.add(new Highlight(n2, entryForIndex.getY(), instance.x, instance.y, i, dataSetByIndex.getAxisDependency()));
        }
        return this.mHighlightBuffer;
    }
}
