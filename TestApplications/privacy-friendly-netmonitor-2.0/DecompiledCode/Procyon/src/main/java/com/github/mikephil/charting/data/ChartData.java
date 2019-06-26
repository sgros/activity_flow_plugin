// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.Iterator;
import com.github.mikephil.charting.components.YAxis;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public abstract class ChartData<T extends IDataSet<? extends Entry>>
{
    protected List<T> mDataSets;
    protected float mLeftAxisMax;
    protected float mLeftAxisMin;
    protected float mRightAxisMax;
    protected float mRightAxisMin;
    protected float mXMax;
    protected float mXMin;
    protected float mYMax;
    protected float mYMin;
    
    public ChartData() {
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        this.mLeftAxisMax = -3.4028235E38f;
        this.mLeftAxisMin = Float.MAX_VALUE;
        this.mRightAxisMax = -3.4028235E38f;
        this.mRightAxisMin = Float.MAX_VALUE;
        this.mDataSets = new ArrayList<T>();
    }
    
    public ChartData(final List<T> mDataSets) {
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        this.mLeftAxisMax = -3.4028235E38f;
        this.mLeftAxisMin = Float.MAX_VALUE;
        this.mRightAxisMax = -3.4028235E38f;
        this.mRightAxisMin = Float.MAX_VALUE;
        this.mDataSets = mDataSets;
        this.notifyDataChanged();
    }
    
    public ChartData(final T... array) {
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        this.mLeftAxisMax = -3.4028235E38f;
        this.mLeftAxisMin = Float.MAX_VALUE;
        this.mRightAxisMax = -3.4028235E38f;
        this.mRightAxisMin = Float.MAX_VALUE;
        this.mDataSets = this.arrayToList(array);
        this.notifyDataChanged();
    }
    
    private List<T> arrayToList(final T[] array) {
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i]);
        }
        return list;
    }
    
    public void addDataSet(final T t) {
        if (t == null) {
            return;
        }
        this.calcMinMax(t);
        this.mDataSets.add(t);
    }
    
    public void addEntry(final Entry entry, final int n) {
        if (this.mDataSets.size() > n && n >= 0) {
            final IDataSet<? extends Entry> set = this.mDataSets.get(n);
            if (!set.addEntry(entry)) {
                return;
            }
            this.calcMinMax(entry, set.getAxisDependency());
        }
        else {
            Log.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.");
        }
    }
    
    protected void calcMinMax() {
        if (this.mDataSets == null) {
            return;
        }
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            this.calcMinMax(iterator.next());
        }
        this.mLeftAxisMax = -3.4028235E38f;
        this.mLeftAxisMin = Float.MAX_VALUE;
        this.mRightAxisMax = -3.4028235E38f;
        this.mRightAxisMin = Float.MAX_VALUE;
        final IDataSet<? extends Entry> firstLeft = this.getFirstLeft((List<IDataSet<? extends Entry>>)this.mDataSets);
        if (firstLeft != null) {
            this.mLeftAxisMax = firstLeft.getYMax();
            this.mLeftAxisMin = firstLeft.getYMin();
            for (final IDataSet<? extends Entry> set : this.mDataSets) {
                if (set.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                    if (set.getYMin() < this.mLeftAxisMin) {
                        this.mLeftAxisMin = set.getYMin();
                    }
                    if (set.getYMax() <= this.mLeftAxisMax) {
                        continue;
                    }
                    this.mLeftAxisMax = set.getYMax();
                }
            }
        }
        final IDataSet<? extends Entry> firstRight = this.getFirstRight((List<IDataSet<? extends Entry>>)this.mDataSets);
        if (firstRight != null) {
            this.mRightAxisMax = firstRight.getYMax();
            this.mRightAxisMin = firstRight.getYMin();
            for (final IDataSet<? extends Entry> set2 : this.mDataSets) {
                if (set2.getAxisDependency() == YAxis.AxisDependency.RIGHT) {
                    if (set2.getYMin() < this.mRightAxisMin) {
                        this.mRightAxisMin = set2.getYMin();
                    }
                    if (set2.getYMax() <= this.mRightAxisMax) {
                        continue;
                    }
                    this.mRightAxisMax = set2.getYMax();
                }
            }
        }
    }
    
    protected void calcMinMax(final Entry entry, final YAxis.AxisDependency axisDependency) {
        if (this.mYMax < entry.getY()) {
            this.mYMax = entry.getY();
        }
        if (this.mYMin > entry.getY()) {
            this.mYMin = entry.getY();
        }
        if (this.mXMax < entry.getX()) {
            this.mXMax = entry.getX();
        }
        if (this.mXMin > entry.getX()) {
            this.mXMin = entry.getX();
        }
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMax < entry.getY()) {
                this.mLeftAxisMax = entry.getY();
            }
            if (this.mLeftAxisMin > entry.getY()) {
                this.mLeftAxisMin = entry.getY();
            }
        }
        else {
            if (this.mRightAxisMax < entry.getY()) {
                this.mRightAxisMax = entry.getY();
            }
            if (this.mRightAxisMin > entry.getY()) {
                this.mRightAxisMin = entry.getY();
            }
        }
    }
    
    protected void calcMinMax(final T t) {
        if (this.mYMax < t.getYMax()) {
            this.mYMax = t.getYMax();
        }
        if (this.mYMin > t.getYMin()) {
            this.mYMin = t.getYMin();
        }
        if (this.mXMax < t.getXMax()) {
            this.mXMax = t.getXMax();
        }
        if (this.mXMin > t.getXMin()) {
            this.mXMin = t.getXMin();
        }
        if (t.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMax < t.getYMax()) {
                this.mLeftAxisMax = t.getYMax();
            }
            if (this.mLeftAxisMin > t.getYMin()) {
                this.mLeftAxisMin = t.getYMin();
            }
        }
        else {
            if (this.mRightAxisMax < t.getYMax()) {
                this.mRightAxisMax = t.getYMax();
            }
            if (this.mRightAxisMin > t.getYMin()) {
                this.mRightAxisMin = t.getYMin();
            }
        }
    }
    
    public void calcMinMaxY(final float n, final float n2) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().calcMinMaxY(n, n2);
        }
        this.calcMinMax();
    }
    
    public void clearValues() {
        if (this.mDataSets != null) {
            this.mDataSets.clear();
        }
        this.notifyDataChanged();
    }
    
    public boolean contains(final T obj) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    public int[] getColors() {
        if (this.mDataSets == null) {
            return null;
        }
        final int n = 0;
        int n2;
        for (int i = n2 = 0; i < this.mDataSets.size(); ++i) {
            n2 += this.mDataSets.get(i).getColors().size();
        }
        final int[] array = new int[n2];
        int n3 = 0;
        for (int j = n; j < this.mDataSets.size(); ++j) {
            final Iterator<Integer> iterator = this.mDataSets.get(j).getColors().iterator();
            while (iterator.hasNext()) {
                array[n3] = iterator.next();
                ++n3;
            }
        }
        return array;
    }
    
    public T getDataSetByIndex(final int n) {
        if (this.mDataSets != null && n >= 0 && n < this.mDataSets.size()) {
            return this.mDataSets.get(n);
        }
        return null;
    }
    
    public T getDataSetByLabel(final String s, final boolean b) {
        final int dataSetIndexByLabel = this.getDataSetIndexByLabel(this.mDataSets, s, b);
        if (dataSetIndexByLabel >= 0 && dataSetIndexByLabel < this.mDataSets.size()) {
            return this.mDataSets.get(dataSetIndexByLabel);
        }
        return null;
    }
    
    public int getDataSetCount() {
        if (this.mDataSets == null) {
            return 0;
        }
        return this.mDataSets.size();
    }
    
    public T getDataSetForEntry(final Entry entry) {
        if (entry == null) {
            return null;
        }
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            final IDataSet<? extends Entry> set = this.mDataSets.get(i);
            for (int j = 0; j < set.getEntryCount(); ++j) {
                if (entry.equalTo((Entry)set.getEntryForXValue(entry.getX(), entry.getY()))) {
                    return (T)set;
                }
            }
        }
        return null;
    }
    
    protected int getDataSetIndexByLabel(final List<T> list, final String s, final boolean b) {
        int i = 0;
        final int n = 0;
        if (b) {
            for (int j = n; j < list.size(); ++j) {
                if (s.equalsIgnoreCase(list.get(j).getLabel())) {
                    return j;
                }
            }
        }
        else {
            while (i < list.size()) {
                if (s.equals(list.get(i).getLabel())) {
                    return i;
                }
                ++i;
            }
        }
        return -1;
    }
    
    public String[] getDataSetLabels() {
        final String[] array = new String[this.mDataSets.size()];
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            array[i] = this.mDataSets.get(i).getLabel();
        }
        return array;
    }
    
    public List<T> getDataSets() {
        return this.mDataSets;
    }
    
    public int getEntryCount() {
        final Iterator<T> iterator = this.mDataSets.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            n += iterator.next().getEntryCount();
        }
        return n;
    }
    
    public Entry getEntryForHighlight(final Highlight highlight) {
        if (highlight.getDataSetIndex() >= this.mDataSets.size()) {
            return null;
        }
        return ((IDataSet<Entry>)this.mDataSets.get(highlight.getDataSetIndex())).getEntryForXValue(highlight.getX(), highlight.getY());
    }
    
    protected T getFirstLeft(final List<T> list) {
        for (final IDataSet<? extends Entry> set : list) {
            if (set.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                return (T)set;
            }
        }
        return null;
    }
    
    public T getFirstRight(final List<T> list) {
        for (final IDataSet<? extends Entry> set : list) {
            if (set.getAxisDependency() == YAxis.AxisDependency.RIGHT) {
                return (T)set;
            }
        }
        return null;
    }
    
    public int getIndexOfDataSet(final T t) {
        return this.mDataSets.indexOf(t);
    }
    
    public T getMaxEntryCountSet() {
        if (this.mDataSets != null && !this.mDataSets.isEmpty()) {
            IDataSet<? extends Entry> set = this.mDataSets.get(0);
            for (final IDataSet<? extends Entry> set2 : this.mDataSets) {
                if (set2.getEntryCount() > set.getEntryCount()) {
                    set = set2;
                }
            }
            return (T)set;
        }
        return null;
    }
    
    public float getXMax() {
        return this.mXMax;
    }
    
    public float getXMin() {
        return this.mXMin;
    }
    
    public float getYMax() {
        return this.mYMax;
    }
    
    public float getYMax(final YAxis.AxisDependency axisDependency) {
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMax == -3.4028235E38f) {
                return this.mRightAxisMax;
            }
            return this.mLeftAxisMax;
        }
        else {
            if (this.mRightAxisMax == -3.4028235E38f) {
                return this.mLeftAxisMax;
            }
            return this.mRightAxisMax;
        }
    }
    
    public float getYMin() {
        return this.mYMin;
    }
    
    public float getYMin(final YAxis.AxisDependency axisDependency) {
        if (axisDependency == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMin == Float.MAX_VALUE) {
                return this.mRightAxisMin;
            }
            return this.mLeftAxisMin;
        }
        else {
            if (this.mRightAxisMin == Float.MAX_VALUE) {
                return this.mLeftAxisMin;
            }
            return this.mRightAxisMin;
        }
    }
    
    public boolean isHighlightEnabled() {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isHighlightEnabled()) {
                return false;
            }
        }
        return true;
    }
    
    public void notifyDataChanged() {
        this.calcMinMax();
    }
    
    public boolean removeDataSet(final int n) {
        return n < this.mDataSets.size() && n >= 0 && this.removeDataSet(this.mDataSets.get(n));
    }
    
    public boolean removeDataSet(final T t) {
        if (t == null) {
            return false;
        }
        final boolean remove = this.mDataSets.remove(t);
        if (remove) {
            this.calcMinMax();
        }
        return remove;
    }
    
    public boolean removeEntry(final float n, final int n2) {
        if (n2 >= this.mDataSets.size()) {
            return false;
        }
        final Entry entryForXValue = ((IDataSet<Entry>)this.mDataSets.get(n2)).getEntryForXValue(n, Float.NaN);
        return entryForXValue != null && this.removeEntry(entryForXValue, n2);
    }
    
    public boolean removeEntry(final Entry entry, final int n) {
        if (entry == null || n >= this.mDataSets.size()) {
            return false;
        }
        final IDataSet<? extends Entry> set = this.mDataSets.get(n);
        if (set != null) {
            final boolean removeEntry = set.removeEntry(entry);
            if (removeEntry) {
                this.calcMinMax();
            }
            return removeEntry;
        }
        return false;
    }
    
    public void setDrawValues(final boolean drawValues) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setDrawValues(drawValues);
        }
    }
    
    public void setHighlightEnabled(final boolean highlightEnabled) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setHighlightEnabled(highlightEnabled);
        }
    }
    
    public void setValueFormatter(final IValueFormatter valueFormatter) {
        if (valueFormatter == null) {
            return;
        }
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setValueFormatter(valueFormatter);
        }
    }
    
    public void setValueTextColor(final int valueTextColor) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setValueTextColor(valueTextColor);
        }
    }
    
    public void setValueTextColors(final List<Integer> valueTextColors) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setValueTextColors(valueTextColors);
        }
    }
    
    public void setValueTextSize(final float valueTextSize) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setValueTextSize(valueTextSize);
        }
    }
    
    public void setValueTypeface(final Typeface valueTypeface) {
        final Iterator<T> iterator = this.mDataSets.iterator();
        while (iterator.hasNext()) {
            iterator.next().setValueTypeface(valueTypeface);
        }
    }
}
