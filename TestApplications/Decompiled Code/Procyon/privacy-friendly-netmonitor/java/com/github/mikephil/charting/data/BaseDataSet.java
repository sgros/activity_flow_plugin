// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.content.Context;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import java.util.ArrayList;
import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.components.Legend;
import java.util.List;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T>
{
    protected YAxis.AxisDependency mAxisDependency;
    protected List<Integer> mColors;
    protected boolean mDrawIcons;
    protected boolean mDrawValues;
    private Legend.LegendForm mForm;
    private DashPathEffect mFormLineDashEffect;
    private float mFormLineWidth;
    private float mFormSize;
    protected boolean mHighlightEnabled;
    protected MPPointF mIconsOffset;
    private String mLabel;
    protected List<Integer> mValueColors;
    protected transient IValueFormatter mValueFormatter;
    protected float mValueTextSize;
    protected Typeface mValueTypeface;
    protected boolean mVisible;
    
    public BaseDataSet() {
        this.mColors = null;
        this.mValueColors = null;
        this.mLabel = "DataSet";
        this.mAxisDependency = YAxis.AxisDependency.LEFT;
        this.mHighlightEnabled = true;
        this.mForm = Legend.LegendForm.DEFAULT;
        this.mFormSize = Float.NaN;
        this.mFormLineWidth = Float.NaN;
        this.mFormLineDashEffect = null;
        this.mDrawValues = true;
        this.mDrawIcons = true;
        this.mIconsOffset = new MPPointF();
        this.mValueTextSize = 17.0f;
        this.mVisible = true;
        this.mColors = new ArrayList<Integer>();
        this.mValueColors = new ArrayList<Integer>();
        this.mColors.add(Color.rgb(140, 234, 255));
        this.mValueColors.add(-16777216);
    }
    
    public BaseDataSet(final String mLabel) {
        this();
        this.mLabel = mLabel;
    }
    
    public void addColor(final int i) {
        if (this.mColors == null) {
            this.mColors = new ArrayList<Integer>();
        }
        this.mColors.add(i);
    }
    
    @Override
    public boolean contains(final T obj) {
        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (this.getEntryForIndex(i).equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public YAxis.AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }
    
    @Override
    public int getColor() {
        return this.mColors.get(0);
    }
    
    @Override
    public int getColor(final int n) {
        return this.mColors.get(n % this.mColors.size());
    }
    
    @Override
    public List<Integer> getColors() {
        return this.mColors;
    }
    
    @Override
    public Legend.LegendForm getForm() {
        return this.mForm;
    }
    
    @Override
    public DashPathEffect getFormLineDashEffect() {
        return this.mFormLineDashEffect;
    }
    
    @Override
    public float getFormLineWidth() {
        return this.mFormLineWidth;
    }
    
    @Override
    public float getFormSize() {
        return this.mFormSize;
    }
    
    @Override
    public MPPointF getIconsOffset() {
        return this.mIconsOffset;
    }
    
    @Override
    public int getIndexInEntries(final int n) {
        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (n == this.getEntryForIndex(i).getX()) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getLabel() {
        return this.mLabel;
    }
    
    public List<Integer> getValueColors() {
        return this.mValueColors;
    }
    
    @Override
    public IValueFormatter getValueFormatter() {
        if (this.needsFormatter()) {
            return Utils.getDefaultValueFormatter();
        }
        return this.mValueFormatter;
    }
    
    @Override
    public int getValueTextColor() {
        return this.mValueColors.get(0);
    }
    
    @Override
    public int getValueTextColor(final int n) {
        return this.mValueColors.get(n % this.mValueColors.size());
    }
    
    @Override
    public float getValueTextSize() {
        return this.mValueTextSize;
    }
    
    @Override
    public Typeface getValueTypeface() {
        return this.mValueTypeface;
    }
    
    @Override
    public boolean isDrawIconsEnabled() {
        return this.mDrawIcons;
    }
    
    @Override
    public boolean isDrawValuesEnabled() {
        return this.mDrawValues;
    }
    
    @Override
    public boolean isHighlightEnabled() {
        return this.mHighlightEnabled;
    }
    
    @Override
    public boolean isVisible() {
        return this.mVisible;
    }
    
    @Override
    public boolean needsFormatter() {
        return this.mValueFormatter == null;
    }
    
    public void notifyDataSetChanged() {
        this.calcMinMax();
    }
    
    @Override
    public boolean removeEntry(final int n) {
        return this.removeEntry(this.getEntryForIndex(n));
    }
    
    @Override
    public boolean removeEntryByXValue(final float n) {
        return this.removeEntry(this.getEntryForXValue(n, Float.NaN));
    }
    
    @Override
    public boolean removeFirst() {
        return this.getEntryCount() > 0 && this.removeEntry(this.getEntryForIndex(0));
    }
    
    @Override
    public boolean removeLast() {
        return this.getEntryCount() > 0 && this.removeEntry(this.getEntryForIndex(this.getEntryCount() - 1));
    }
    
    public void resetColors() {
        if (this.mColors == null) {
            this.mColors = new ArrayList<Integer>();
        }
        this.mColors.clear();
    }
    
    @Override
    public void setAxisDependency(final YAxis.AxisDependency mAxisDependency) {
        this.mAxisDependency = mAxisDependency;
    }
    
    public void setColor(final int i) {
        this.resetColors();
        this.mColors.add(i);
    }
    
    public void setColor(final int n, final int n2) {
        this.setColor(Color.argb(n2, Color.red(n), Color.green(n), Color.blue(n)));
    }
    
    public void setColors(final List<Integer> mColors) {
        this.mColors = mColors;
    }
    
    public void setColors(final int... array) {
        this.mColors = ColorTemplate.createColors(array);
    }
    
    public void setColors(final int[] array, final int n) {
        this.resetColors();
        for (int i = 0; i < array.length; ++i) {
            final int n2 = array[i];
            this.addColor(Color.argb(n, Color.red(n2), Color.green(n2), Color.blue(n2)));
        }
    }
    
    public void setColors(final int[] array, final Context context) {
        if (this.mColors == null) {
            this.mColors = new ArrayList<Integer>();
        }
        this.mColors.clear();
        for (int length = array.length, i = 0; i < length; ++i) {
            this.mColors.add(context.getResources().getColor(array[i]));
        }
    }
    
    @Override
    public void setDrawIcons(final boolean mDrawIcons) {
        this.mDrawIcons = mDrawIcons;
    }
    
    @Override
    public void setDrawValues(final boolean mDrawValues) {
        this.mDrawValues = mDrawValues;
    }
    
    public void setForm(final Legend.LegendForm mForm) {
        this.mForm = mForm;
    }
    
    public void setFormLineDashEffect(final DashPathEffect mFormLineDashEffect) {
        this.mFormLineDashEffect = mFormLineDashEffect;
    }
    
    public void setFormLineWidth(final float mFormLineWidth) {
        this.mFormLineWidth = mFormLineWidth;
    }
    
    public void setFormSize(final float mFormSize) {
        this.mFormSize = mFormSize;
    }
    
    @Override
    public void setHighlightEnabled(final boolean mHighlightEnabled) {
        this.mHighlightEnabled = mHighlightEnabled;
    }
    
    @Override
    public void setIconsOffset(final MPPointF mpPointF) {
        this.mIconsOffset.x = mpPointF.x;
        this.mIconsOffset.y = mpPointF.y;
    }
    
    @Override
    public void setLabel(final String mLabel) {
        this.mLabel = mLabel;
    }
    
    @Override
    public void setValueFormatter(final IValueFormatter mValueFormatter) {
        if (mValueFormatter == null) {
            return;
        }
        this.mValueFormatter = mValueFormatter;
    }
    
    @Override
    public void setValueTextColor(final int i) {
        this.mValueColors.clear();
        this.mValueColors.add(i);
    }
    
    @Override
    public void setValueTextColors(final List<Integer> mValueColors) {
        this.mValueColors = mValueColors;
    }
    
    @Override
    public void setValueTextSize(final float n) {
        this.mValueTextSize = Utils.convertDpToPixel(n);
    }
    
    @Override
    public void setValueTypeface(final Typeface mValueTypeface) {
        this.mValueTypeface = mValueTypeface;
    }
    
    @Override
    public void setVisible(final boolean mVisible) {
        this.mVisible = mVisible;
    }
}
