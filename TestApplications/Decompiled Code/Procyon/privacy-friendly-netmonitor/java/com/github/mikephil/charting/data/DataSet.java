// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSet<T extends Entry> extends BaseDataSet<T>
{
    protected List<T> mValues;
    protected float mXMax;
    protected float mXMin;
    protected float mYMax;
    protected float mYMin;
    
    public DataSet(final List<T> mValues, final String s) {
        super(s);
        this.mValues = null;
        this.mYMax = -3.4028235E38f;
        this.mYMin = Float.MAX_VALUE;
        this.mXMax = -3.4028235E38f;
        this.mXMin = Float.MAX_VALUE;
        this.mValues = mValues;
        if (this.mValues == null) {
            this.mValues = new ArrayList<T>();
        }
        this.calcMinMax();
    }
    
    @Override
    public boolean addEntry(final T t) {
        if (t == null) {
            return false;
        }
        List<T> values;
        if ((values = this.getValues()) == null) {
            values = new ArrayList<T>();
        }
        this.calcMinMax(t);
        return values.add(t);
    }
    
    @Override
    public void addEntryOrdered(final T t) {
        if (t == null) {
            return;
        }
        if (this.mValues == null) {
            this.mValues = new ArrayList<T>();
        }
        this.calcMinMax(t);
        if (this.mValues.size() > 0 && this.mValues.get(this.mValues.size() - 1).getX() > t.getX()) {
            this.mValues.add(this.getEntryIndex(t.getX(), t.getY(), Rounding.UP), t);
        }
        else {
            this.mValues.add(t);
        }
    }
    
    @Override
    public void calcMinMax() {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38f;
            this.mYMin = Float.MAX_VALUE;
            this.mXMax = -3.4028235E38f;
            this.mXMin = Float.MAX_VALUE;
            final Iterator<T> iterator = this.mValues.iterator();
            while (iterator.hasNext()) {
                this.calcMinMax(iterator.next());
            }
        }
    }
    
    protected void calcMinMax(final T t) {
        if (t == null) {
            return;
        }
        this.calcMinMaxX(t);
        this.calcMinMaxY(t);
    }
    
    protected void calcMinMaxX(final T t) {
        if (t.getX() < this.mXMin) {
            this.mXMin = t.getX();
        }
        if (t.getX() > this.mXMax) {
            this.mXMax = t.getX();
        }
    }
    
    @Override
    public void calcMinMaxY(final float n, final float n2) {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38f;
            this.mYMin = Float.MAX_VALUE;
            for (int i = this.getEntryIndex(n, Float.NaN, Rounding.DOWN); i <= this.getEntryIndex(n2, Float.NaN, Rounding.UP); ++i) {
                this.calcMinMaxY(this.mValues.get(i));
            }
        }
    }
    
    protected void calcMinMaxY(final T t) {
        if (t.getY() < this.mYMin) {
            this.mYMin = t.getY();
        }
        if (t.getY() > this.mYMax) {
            this.mYMax = t.getY();
        }
    }
    
    @Override
    public void clear() {
        this.mValues.clear();
        this.notifyDataSetChanged();
    }
    
    public abstract DataSet<T> copy();
    
    @Override
    public List<T> getEntriesForXValue(final float n) {
        final ArrayList<Entry> list = (ArrayList<Entry>)new ArrayList<T>();
        int n2 = this.mValues.size() - 1;
        int i = 0;
        while (i <= n2) {
            final int n3 = (n2 + i) / 2;
            final Entry entry = this.mValues.get(n3);
            if (n == entry.getX()) {
                int j;
                for (j = n3; j > 0 && this.mValues.get(j - 1).getX() == n; --j) {}
                while (j < this.mValues.size()) {
                    final Entry entry2 = this.mValues.get(j);
                    if (entry2.getX() != n) {
                        break;
                    }
                    list.add(entry2);
                    ++j;
                }
                break;
            }
            if (n > entry.getX()) {
                i = n3 + 1;
            }
            else {
                n2 = n3 - 1;
            }
        }
        return (List<T>)list;
    }
    
    @Override
    public int getEntryCount() {
        return this.mValues.size();
    }
    
    @Override
    public T getEntryForIndex(final int n) {
        return this.mValues.get(n);
    }
    
    @Override
    public T getEntryForXValue(final float n, final float n2) {
        return this.getEntryForXValue(n, n2, Rounding.CLOSEST);
    }
    
    @Override
    public T getEntryForXValue(final float n, final float n2, final Rounding rounding) {
        final int entryIndex = this.getEntryIndex(n, n2, rounding);
        if (entryIndex > -1) {
            return this.mValues.get(entryIndex);
        }
        return null;
    }
    
    @Override
    public int getEntryIndex(final float n, final float n2, final Rounding rounding) {
        throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: fail exe a34 = a4\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.exec(BaseAnalyze.java:92)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.exec(BaseAnalyze.java:31)\n\tat com.googlecode.dex2jar.ir.ts.Cfg.dfs(Cfg.java:255)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.analyze0(BaseAnalyze.java:75)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.analyze(BaseAnalyze.java:69)\n\tat com.googlecode.dex2jar.ir.ts.Ir2JRegAssignTransformer.transform(Ir2JRegAssignTransformer.java:182)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:167)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:442)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:40)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:132)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:575)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:434)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:450)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:175)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:275)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:112)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:290)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:33)\nCaused by: java.lang.NullPointerException\n\tat com.googlecode.dex2jar.ir.ts.an.SimpleLiveAnalyze.onUseLocal(SimpleLiveAnalyze.java:89)\n\tat com.googlecode.dex2jar.ir.ts.an.SimpleLiveAnalyze.onUseLocal(SimpleLiveAnalyze.java:27)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.onUse(BaseAnalyze.java:166)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.onUse(BaseAnalyze.java:31)\n\tat com.googlecode.dex2jar.ir.ts.Cfg.travel(Cfg.java:331)\n\tat com.googlecode.dex2jar.ir.ts.Cfg.travel(Cfg.java:387)\n\tat com.googlecode.dex2jar.ir.ts.an.BaseAnalyze.exec(BaseAnalyze.java:90)\n\t... 17 more\n");
    }
    
    @Override
    public int getEntryIndex(final Entry entry) {
        return this.mValues.indexOf(entry);
    }
    
    public List<T> getValues() {
        return this.mValues;
    }
    
    @Override
    public float getXMax() {
        return this.mXMax;
    }
    
    @Override
    public float getXMin() {
        return this.mXMin;
    }
    
    @Override
    public float getYMax() {
        return this.mYMax;
    }
    
    @Override
    public float getYMin() {
        return this.mYMin;
    }
    
    @Override
    public boolean removeEntry(final T t) {
        if (t == null) {
            return false;
        }
        if (this.mValues == null) {
            return false;
        }
        final boolean remove = this.mValues.remove(t);
        if (remove) {
            this.calcMinMax();
        }
        return remove;
    }
    
    public void setValues(final List<T> mValues) {
        this.mValues = mValues;
        this.notifyDataSetChanged();
    }
    
    public String toSimpleString() {
        final StringBuffer sb = new StringBuffer();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("DataSet, label: ");
        String label;
        if (this.getLabel() == null) {
            label = "";
        }
        else {
            label = this.getLabel();
        }
        sb2.append(label);
        sb2.append(", entries: ");
        sb2.append(this.mValues.size());
        sb2.append("\n");
        sb.append(sb2.toString());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.toSimpleString());
        for (int i = 0; i < this.mValues.size(); ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mValues.get(i).toString());
            sb2.append(" ");
            sb.append(sb2.toString());
        }
        return sb.toString();
    }
    
    public enum Rounding
    {
        CLOSEST, 
        DOWN, 
        UP;
    }
}
