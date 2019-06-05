// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import android.database.CursorWindow;
import java.util.Iterator;
import android.database.CursorIndexOutOfBoundsException;
import java.util.ArrayList;
import android.database.AbstractCursor;

public class MatrixCursorWithBlob extends AbstractCursor
{
    private final int columnCount;
    private final String[] columnNames;
    private Object[] data;
    private int rowCount;
    
    public MatrixCursorWithBlob(final String[] array) {
        this(array, 16);
    }
    
    public MatrixCursorWithBlob(final String[] columnNames, final int n) {
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
            throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.size() = " + size);
        }
        ++this.rowCount;
        final Object[] data = this.data;
        for (int i = 0; i < size; ++i) {
            data[n + i] = list.get(i);
        }
    }
    
    private void ensureCapacity(final int n) {
        if (n > this.data.length) {
            final Object[] data = this.data;
            int n2;
            if ((n2 = this.data.length * 2) < n) {
                n2 = n;
            }
            System.arraycopy(data, 0, this.data = new Object[n2], 0, data.length);
        }
    }
    
    private Object get(final int i) {
        if (i < 0 || i >= this.columnCount) {
            throw new CursorIndexOutOfBoundsException("Requested column: " + i + ", # of columns: " + this.columnCount);
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
        final int n2 = n + this.columnCount;
        this.ensureCapacity(n2);
        if (iterable instanceof ArrayList) {
            this.addRow((ArrayList<?>)iterable, n);
        }
        else {
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
    }
    
    public void addRow(final Object[] array) {
        if (array.length != this.columnCount) {
            throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.length = " + array.length);
        }
        final int n = this.rowCount++ * this.columnCount;
        this.ensureCapacity(this.columnCount + n);
        System.arraycopy(array, 0, this.data, n, this.columnCount);
    }
    
    public void fillWindow(int startPosition, final CursorWindow cursorWindow) {
        if (startPosition >= 0 && startPosition < this.getCount()) {
            int mPos = 0;
        Label_0067_Outer:
            while (true) {
                cursorWindow.acquireReference();
                while (true) {
                Label_0143:
                    while (true) {
                        Label_0115: {
                            try {
                                mPos = this.mPos;
                                this.mPos = startPosition - 1;
                                cursorWindow.clear();
                                cursorWindow.setStartPosition(startPosition);
                                final int columnCount = this.getColumnCount();
                                cursorWindow.setNumColumns(columnCount);
                                while (this.moveToNext() && cursorWindow.allocRow()) {
                                    startPosition = 0;
                                    if (startPosition < columnCount) {
                                        final byte[] blob = this.getBlob(startPosition);
                                        if (blob == null) {
                                            break Label_0115;
                                        }
                                        if (cursorWindow.putBlob(blob, this.mPos, startPosition)) {
                                            break Label_0143;
                                        }
                                        cursorWindow.freeLastRow();
                                    }
                                }
                                break;
                            }
                            catch (IllegalStateException ex) {
                                return;
                                while (true) {
                                    cursorWindow.freeLastRow();
                                    continue Label_0067_Outer;
                                    continue;
                                }
                            }
                            // iftrue(Label_0143:, cursorWindow.putNull(this.mPos, startPosition))
                            finally {
                                cursorWindow.releaseReference();
                            }
                        }
                        break;
                    }
                    ++startPosition;
                    continue;
                }
            }
            this.mPos = mPos;
            cursorWindow.releaseReference();
        }
    }
    
    public byte[] getBlob(final int n) {
        final Object value = this.get(n);
        byte[] array;
        if (value == null) {
            array = new byte[0];
        }
        else if (value instanceof byte[]) {
            array = (byte[])value;
        }
        else {
            array = new byte[0];
        }
        return array;
    }
    
    public String[] getColumnNames() {
        return this.columnNames;
    }
    
    public int getCount() {
        return this.rowCount;
    }
    
    public double getDouble(final int n) {
        final Object value = this.get(n);
        double n2;
        if (value == null) {
            n2 = 0.0;
        }
        else if (value instanceof Number) {
            n2 = ((Number)value).doubleValue();
        }
        else {
            n2 = Double.parseDouble(value.toString());
        }
        return n2;
    }
    
    public float getFloat(final int n) {
        final Object value = this.get(n);
        float n2;
        if (value == null) {
            n2 = 0.0f;
        }
        else if (value instanceof Number) {
            n2 = ((Number)value).floatValue();
        }
        else {
            n2 = Float.parseFloat(value.toString());
        }
        return n2;
    }
    
    public int getInt(int n) {
        final Object value = this.get(n);
        if (value == null) {
            n = 0;
        }
        else if (value instanceof Number) {
            n = ((Number)value).intValue();
        }
        else {
            n = Integer.parseInt(value.toString());
        }
        return n;
    }
    
    public long getLong(final int n) {
        final Object value = this.get(n);
        long n2;
        if (value == null) {
            n2 = 0L;
        }
        else if (value instanceof Number) {
            n2 = ((Number)value).longValue();
        }
        else {
            n2 = Long.parseLong(value.toString());
        }
        return n2;
    }
    
    public short getShort(int n) {
        final Object value = this.get(n);
        short n2;
        if (value == null) {
            n = (n2 = 0);
        }
        else if (value instanceof Number) {
            n = (n2 = ((Number)value).shortValue());
        }
        else {
            n = (n2 = Short.parseShort(value.toString()));
        }
        return n2;
    }
    
    public String getString(final int n) {
        final Object value = this.get(n);
        String string;
        if (value == null) {
            string = null;
        }
        else {
            string = value.toString();
        }
        return string;
    }
    
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
            MatrixCursorWithBlob.this.data[this.index++] = o;
            return this;
        }
    }
}
