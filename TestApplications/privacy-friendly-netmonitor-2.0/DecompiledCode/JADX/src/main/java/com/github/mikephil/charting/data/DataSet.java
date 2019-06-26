package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {
    protected List<T> mValues = null;
    protected float mXMax = -3.4028235E38f;
    protected float mXMin = Float.MAX_VALUE;
    protected float mYMax = -3.4028235E38f;
    protected float mYMin = Float.MAX_VALUE;

    public enum Rounding {
        UP,
        DOWN,
        CLOSEST
    }

    public abstract DataSet<T> copy();

    public DataSet(List<T> list, String str) {
        super(str);
        this.mValues = list;
        if (this.mValues == null) {
            this.mValues = new ArrayList();
        }
        calcMinMax();
    }

    public void calcMinMax() {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38f;
            this.mYMin = Float.MAX_VALUE;
            this.mXMax = -3.4028235E38f;
            this.mXMin = Float.MAX_VALUE;
            for (Entry calcMinMax : this.mValues) {
                calcMinMax(calcMinMax);
            }
        }
    }

    public void calcMinMaxY(float f, float f2) {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38f;
            this.mYMin = Float.MAX_VALUE;
            int entryIndex = getEntryIndex(f2, Float.NaN, Rounding.UP);
            for (int entryIndex2 = getEntryIndex(f, Float.NaN, Rounding.DOWN); entryIndex2 <= entryIndex; entryIndex2++) {
                calcMinMaxY((Entry) this.mValues.get(entryIndex2));
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void calcMinMax(T t) {
        if (t != null) {
            calcMinMaxX(t);
            calcMinMaxY(t);
        }
    }

    /* Access modifiers changed, original: protected */
    public void calcMinMaxX(T t) {
        if (t.getX() < this.mXMin) {
            this.mXMin = t.getX();
        }
        if (t.getX() > this.mXMax) {
            this.mXMax = t.getX();
        }
    }

    /* Access modifiers changed, original: protected */
    public void calcMinMaxY(T t) {
        if (t.getY() < this.mYMin) {
            this.mYMin = t.getY();
        }
        if (t.getY() > this.mYMax) {
            this.mYMax = t.getY();
        }
    }

    public int getEntryCount() {
        return this.mValues.size();
    }

    public List<T> getValues() {
        return this.mValues;
    }

    public void setValues(List<T> list) {
        this.mValues = list;
        notifyDataSetChanged();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(toSimpleString());
        for (int i = 0; i < this.mValues.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(((Entry) this.mValues.get(i)).toString());
            stringBuilder.append(" ");
            stringBuffer.append(stringBuilder.toString());
        }
        return stringBuffer.toString();
    }

    public String toSimpleString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataSet, label: ");
        stringBuilder.append(getLabel() == null ? "" : getLabel());
        stringBuilder.append(", entries: ");
        stringBuilder.append(this.mValues.size());
        stringBuilder.append("\n");
        stringBuffer.append(stringBuilder.toString());
        return stringBuffer.toString();
    }

    public float getYMin() {
        return this.mYMin;
    }

    public float getYMax() {
        return this.mYMax;
    }

    public float getXMin() {
        return this.mXMin;
    }

    public float getXMax() {
        return this.mXMax;
    }

    public void addEntryOrdered(T t) {
        if (t != null) {
            if (this.mValues == null) {
                this.mValues = new ArrayList();
            }
            calcMinMax(t);
            if (this.mValues.size() <= 0 || ((Entry) this.mValues.get(this.mValues.size() - 1)).getX() <= t.getX()) {
                this.mValues.add(t);
            } else {
                this.mValues.add(getEntryIndex(t.getX(), t.getY(), Rounding.UP), t);
            }
        }
    }

    public void clear() {
        this.mValues.clear();
        notifyDataSetChanged();
    }

    public boolean addEntry(T t) {
        if (t == null) {
            return false;
        }
        List values = getValues();
        if (values == null) {
            values = new ArrayList();
        }
        calcMinMax(t);
        return values.add(t);
    }

    public boolean removeEntry(T t) {
        if (t == null || this.mValues == null) {
            return false;
        }
        boolean remove = this.mValues.remove(t);
        if (remove) {
            calcMinMax();
        }
        return remove;
    }

    public int getEntryIndex(Entry entry) {
        return this.mValues.indexOf(entry);
    }

    public T getEntryForXValue(float f, float f2, Rounding rounding) {
        int entryIndex = getEntryIndex(f, f2, rounding);
        return entryIndex > -1 ? (Entry) this.mValues.get(entryIndex) : null;
    }

    public T getEntryForXValue(float f, float f2) {
        return getEntryForXValue(f, f2, Rounding.CLOSEST);
    }

    public T getEntryForIndex(int i) {
        return (Entry) this.mValues.get(i);
    }

    public int getEntryIndex(float f, float f2, Rounding rounding) {
        if (this.mValues == null || this.mValues.isEmpty()) {
            return -1;
        }
        int i = 0;
        int size = this.mValues.size() - 1;
        while (i < size) {
            int i2 = (i + size) / 2;
            float x = ((Entry) this.mValues.get(i2)).getX() - f;
            int i3 = i2 + 1;
            float x2 = ((Entry) this.mValues.get(i3)).getX() - f;
            float abs = Math.abs(x);
            x2 = Math.abs(x2);
            if (x2 >= abs) {
                if (abs >= x2) {
                    double d = (double) x;
                    if (d < Utils.DOUBLE_EPSILON) {
                        if (d >= Utils.DOUBLE_EPSILON) {
                        }
                    }
                }
                size = i2;
            }
            i = i3;
        }
        if (size != -1) {
            float x3 = ((Entry) this.mValues.get(size)).getX();
            if (rounding == Rounding.UP) {
                if (x3 < f && size < this.mValues.size() - 1) {
                    size++;
                }
            } else if (rounding == Rounding.DOWN && x3 > f && size > 0) {
                size--;
            }
            if (!Float.isNaN(f2)) {
                int i4;
                while (size > 0 && ((Entry) this.mValues.get(size - 1)).getX() == x3) {
                    size--;
                }
                float y = ((Entry) this.mValues.get(size)).getY();
                loop2:
                while (true) {
                    i4 = size;
                    Entry entry;
                    do {
                        size++;
                        if (size >= this.mValues.size()) {
                            break loop2;
                        }
                        entry = (Entry) this.mValues.get(size);
                        if (entry.getX() != x3) {
                            break loop2;
                        }
                    } while (Math.abs(entry.getY() - f2) >= Math.abs(y - f2));
                    y = f2;
                }
                size = i4;
            }
        }
        return size;
    }

    public List<T> getEntriesForXValue(float f) {
        ArrayList arrayList = new ArrayList();
        int size = this.mValues.size() - 1;
        int i = 0;
        while (i <= size) {
            int i2 = (size + i) / 2;
            Entry entry = (Entry) this.mValues.get(i2);
            if (f == entry.getX()) {
                while (i2 > 0 && ((Entry) this.mValues.get(i2 - 1)).getX() == f) {
                    i2--;
                }
                size = this.mValues.size();
                while (i2 < size) {
                    Entry entry2 = (Entry) this.mValues.get(i2);
                    if (entry2.getX() != f) {
                        break;
                    }
                    arrayList.add(entry2);
                    i2++;
                }
                return arrayList;
            } else if (f > entry.getX()) {
                i = i2 + 1;
            } else {
                size = i2 - 1;
            }
        }
        return arrayList;
    }
}
