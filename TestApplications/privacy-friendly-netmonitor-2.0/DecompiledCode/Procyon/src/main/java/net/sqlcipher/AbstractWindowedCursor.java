// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.database.CharArrayBuffer;

public abstract class AbstractWindowedCursor extends AbstractCursor
{
    protected CursorWindow mWindow;
    
    @Override
    protected void checkPosition() {
        super.checkPosition();
        if (this.mWindow == null) {
            throw new StaleDataException("Access closed cursor");
        }
    }
    
    @Override
    public void copyStringToBuffer(final int n, final CharArrayBuffer charArrayBuffer) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                super.copyStringToBuffer(n, charArrayBuffer);
            }
            // monitorexit(this.mUpdatedRows)
            this.mWindow.copyStringToBuffer(this.mPos, n, charArrayBuffer);
        }
    }
    
    @Override
    public byte[] getBlob(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return (byte[])this.getUpdatedField(n);
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getBlob(this.mPos, n);
        }
    }
    
    @Override
    public double getDouble(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return ((Number)this.getUpdatedField(n)).doubleValue();
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getDouble(this.mPos, n);
        }
    }
    
    @Override
    public float getFloat(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return ((Number)this.getUpdatedField(n)).floatValue();
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getFloat(this.mPos, n);
        }
    }
    
    @Override
    public int getInt(int intValue) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(intValue)) {
                intValue = ((Number)this.getUpdatedField(intValue)).intValue();
                return intValue;
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getInt(this.mPos, intValue);
        }
    }
    
    @Override
    public long getLong(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return ((Number)this.getUpdatedField(n)).longValue();
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getLong(this.mPos, n);
        }
    }
    
    @Override
    public short getShort(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return ((Number)this.getUpdatedField(n)).shortValue();
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getShort(this.mPos, n);
        }
    }
    
    @Override
    public String getString(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return (String)this.getUpdatedField(n);
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.getString(this.mPos, n);
        }
    }
    
    @Override
    public int getType(final int n) {
        this.checkPosition();
        return this.mWindow.getType(this.mPos, n);
    }
    
    @Override
    public CursorWindow getWindow() {
        return this.mWindow;
    }
    
    public boolean hasWindow() {
        return this.mWindow != null;
    }
    
    public boolean isBlob(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                final Object updatedField = this.getUpdatedField(n);
                return updatedField == null || updatedField instanceof byte[];
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.isBlob(this.mPos, n);
        }
    }
    
    public boolean isFloat(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                final Object updatedField = this.getUpdatedField(n);
                return updatedField != null && (updatedField instanceof Float || updatedField instanceof Double);
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.isFloat(this.mPos, n);
        }
    }
    
    public boolean isLong(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                final Object updatedField = this.getUpdatedField(n);
                return updatedField != null && (updatedField instanceof Integer || updatedField instanceof Long);
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.isLong(this.mPos, n);
        }
    }
    
    @Override
    public boolean isNull(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                return this.getUpdatedField(n) == null;
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.isNull(this.mPos, n);
        }
    }
    
    public boolean isString(final int n) {
        this.checkPosition();
        synchronized (this.mUpdatedRows) {
            if (this.isFieldUpdated(n)) {
                final Object updatedField = this.getUpdatedField(n);
                return updatedField == null || updatedField instanceof String;
            }
            // monitorexit(this.mUpdatedRows)
            return this.mWindow.isString(this.mPos, n);
        }
    }
    
    public void setWindow(final CursorWindow mWindow) {
        if (this.mWindow != null) {
            this.mWindow.close();
        }
        this.mWindow = mWindow;
    }
}
