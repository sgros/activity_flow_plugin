// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import java.util.Iterator;
import java.util.ArrayList;

public class MatrixCursor extends AbstractCursor
{
    private final int columnCount;
    private final String[] columnNames;
    private Object[] data;
    private int rowCount;
    
    public MatrixCursor(final String[] array) {
        this(array, 16);
    }
    
    public MatrixCursor(final String[] columnNames, final int n) {
        this.rowCount = 0;
        this.columnNames = columnNames;
        this.columnCount = columnNames.length;
        int n2 = n;
        if (n < 1) {
            n2 = 1;
        }
        this.data = new Object[this.columnCount * n2];
    }
    
    private void addRow(final ArrayList<?> list, final int n) {
        final int size = list.size();
        if (size != this.columnCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("columnNames.length = ");
            sb.append(this.columnCount);
            sb.append(", columnValues.size() = ");
            sb.append(size);
            throw new IllegalArgumentException(sb.toString());
        }
        ++this.rowCount;
        final Object[] data = this.data;
        for (int i = 0; i < size; ++i) {
            data[n + i] = list.get(i);
        }
    }
    
    private void ensureCapacity(int n) {
        if (n > this.data.length) {
            final Object[] data = this.data;
            final int n2 = this.data.length * 2;
            if (n2 >= n) {
                n = n2;
            }
            System.arraycopy(data, 0, this.data = new Object[n], 0, data.length);
        }
    }
    
    private Object get(final int i) {
        if (i < 0 || i >= this.columnCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested column: ");
            sb.append(i);
            sb.append(", # of columns: ");
            sb.append(this.columnCount);
            throw new CursorIndexOutOfBoundsException(sb.toString());
        }
        if (this.mPos < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
        }
        if (this.mPos >= this.rowCount) {
            throw new CursorIndexOutOfBoundsException("After last row.");
        }
        return this.data[this.mPos * this.columnCount + i];
    }
    
    public void addRow(final Iterable<?> iterable) {
        int n = this.rowCount * this.columnCount;
        final int n2 = this.columnCount + n;
        this.ensureCapacity(n2);
        if (iterable instanceof ArrayList) {
            this.addRow((ArrayList<?>)iterable, n);
            return;
        }
        final Object[] data = this.data;
        for (final Object next : iterable) {
            if (n == n2) {
                throw new IllegalArgumentException("columnValues.size() > columnNames.length");
            }
            data[n] = next;
            ++n;
        }
        if (n != n2) {
            throw new IllegalArgumentException("columnValues.size() < columnNames.length");
        }
        ++this.rowCount;
    }
    
    public void addRow(final Object[] array) {
        if (array.length != this.columnCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append("columnNames.length = ");
            sb.append(this.columnCount);
            sb.append(", columnValues.length = ");
            sb.append(array.length);
            throw new IllegalArgumentException(sb.toString());
        }
        final int n = this.rowCount++ * this.columnCount;
        this.ensureCapacity(this.columnCount + n);
        System.arraycopy(array, 0, this.data, n, this.columnCount);
    }
    
    @Override
    public String[] getColumnNames() {
        return this.columnNames;
    }
    
    @Override
    public int getCount() {
        return this.rowCount;
    }
    
    @Override
    public double getDouble(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }
    
    @Override
    public float getFloat(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return 0.0f;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        return Float.parseFloat(value.toString());
    }
    
    @Override
    public int getInt(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        return Integer.parseInt(value.toString());
    }
    
    @Override
    public long getLong(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        return Long.parseLong(value.toString());
    }
    
    @Override
    public short getShort(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        return Short.parseShort(value.toString());
    }
    
    @Override
    public String getString(final int n) {
        final Object value = this.get(n);
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    @Override
    public int getType(final int n) {
        return DatabaseUtils.getTypeOfObject(this.get(n));
    }
    
    @Override
    public boolean isNull(final int n) {
        return this.get(n) == null;
    }
    
    public RowBuilder newRow() {
        ++this.rowCount;
        final int n = this.rowCount * this.columnCount;
        this.ensureCapacity(n);
        return new RowBuilder(n - this.columnCount, n);
    }
    
    public class RowBuilder
    {
        private final int endIndex;
        private int index;
        
        RowBuilder(final int index, final int endIndex) {
            this.index = index;
            this.endIndex = endIndex;
        }
        
        public RowBuilder add(final Object o) {
            if (this.index == this.endIndex) {
                throw new CursorIndexOutOfBoundsException("No more columns left.");
            }
            MatrixCursor.this.data[this.index++] = o;
            return this;
        }
    }
}
