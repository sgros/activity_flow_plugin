// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import java.util.Iterator;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class HorizontalBarHighlighter extends BarHighlighter
{
    public HorizontalBarHighlighter(final BarDataProvider barDataProvider) {
        super(barDataProvider);
    }
    
    @Override
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
            final MPPointD pixelForValues = ((BarDataProvider)this.mChart).getTransformer(set.getAxisDependency()).getPixelForValues(entry.getY(), entry.getX());
            list.add(new Highlight(entry.getX(), entry.getY(), (float)pixelForValues.x, (float)pixelForValues.y, n, set.getAxisDependency()));
        }
        return list;
    }
    
    @Override
    protected float getDistance(final float n, final float n2, final float n3, final float n4) {
        return Math.abs(n2 - n4);
    }
    
    @Override
    public Highlight getHighlight(final float n, final float n2) {
        final BarData barData = ((BarDataProvider)this.mChart).getBarData();
        final MPPointD valsForTouch = this.getValsForTouch(n2, n);
        final Highlight highlightForX = this.getHighlightForX((float)valsForTouch.y, n2, n);
        if (highlightForX == null) {
            return null;
        }
        final IBarDataSet set = barData.getDataSetByIndex(highlightForX.getDataSetIndex());
        if (set.isStacked()) {
            return this.getStackedHighlight(highlightForX, set, (float)valsForTouch.y, (float)valsForTouch.x);
        }
        MPPointD.recycleInstance(valsForTouch);
        return highlightForX;
    }
}
