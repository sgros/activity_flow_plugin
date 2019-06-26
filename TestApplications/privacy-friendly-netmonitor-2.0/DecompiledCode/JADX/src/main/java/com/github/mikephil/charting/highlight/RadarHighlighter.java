package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

public class RadarHighlighter extends PieRadarHighlighter<RadarChart> {
    public RadarHighlighter(RadarChart radarChart) {
        super(radarChart);
    }

    /* Access modifiers changed, original: protected */
    public Highlight getClosestHighlight(int i, float f, float f2) {
        List highlightsAtIndex = getHighlightsAtIndex(i);
        f = ((RadarChart) this.mChart).distanceToCenter(f, f2) / ((RadarChart) this.mChart).getFactor();
        Highlight highlight = null;
        float f3 = Float.MAX_VALUE;
        for (int i2 = 0; i2 < highlightsAtIndex.size(); i2++) {
            Highlight highlight2 = (Highlight) highlightsAtIndex.get(i2);
            float abs = Math.abs(highlight2.getY() - f);
            if (abs < f3) {
                highlight = highlight2;
                f3 = abs;
            }
        }
        return highlight;
    }

    /* Access modifiers changed, original: protected */
    public List<Highlight> getHighlightsAtIndex(int i) {
        int i2 = i;
        this.mHighlightBuffer.clear();
        float phaseX = ((RadarChart) this.mChart).getAnimator().getPhaseX();
        float phaseY = ((RadarChart) this.mChart).getAnimator().getPhaseY();
        float sliceAngle = ((RadarChart) this.mChart).getSliceAngle();
        float factor = ((RadarChart) this.mChart).getFactor();
        MPPointF instance = MPPointF.getInstance(0.0f, 0.0f);
        int i3 = 0;
        while (i3 < ((RadarData) ((RadarChart) this.mChart).getData()).getDataSetCount()) {
            IDataSet dataSetByIndex = ((RadarData) ((RadarChart) this.mChart).getData()).getDataSetByIndex(i3);
            Entry entryForIndex = dataSetByIndex.getEntryForIndex(i2);
            float f = (float) i2;
            Utils.getPosition(((RadarChart) this.mChart).getCenterOffsets(), ((entryForIndex.getY() - ((RadarChart) this.mChart).getYChartMin()) * factor) * phaseY, ((sliceAngle * f) * phaseX) + ((RadarChart) this.mChart).getRotationAngle(), instance);
            List list = this.mHighlightBuffer;
            Highlight highlight = r8;
            Highlight highlight2 = new Highlight(f, entryForIndex.getY(), instance.f488x, instance.f489y, i3, dataSetByIndex.getAxisDependency());
            list.add(highlight);
            i3++;
            i2 = i;
        }
        return this.mHighlightBuffer;
    }
}
