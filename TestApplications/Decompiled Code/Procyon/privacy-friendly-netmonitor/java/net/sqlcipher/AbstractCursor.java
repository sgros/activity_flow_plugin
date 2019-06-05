// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.os.Handler;
import java.lang.ref.WeakReference;
import android.database.DataSetObserver;
import android.util.Log;
import android.database.CursorWindow;
import android.database.CharArrayBuffer;
import java.util.Map;
import java.util.HashMap;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.database.DataSetObservable;
import android.content.ContentResolver;
import android.database.ContentObservable;
import android.database.CrossProcessCursor;

public abstract class AbstractCursor implements CrossProcessCursor, Cursor
{
    private static final String TAG = "Cursor";
    protected boolean mClosed;
    ContentObservable mContentObservable;
    protected ContentResolver mContentResolver;
    protected Long mCurrentRowID;
    DataSetObservable mDataSetObservable;
    private Bundle mExtras;
    private Uri mNotifyUri;
    protected int mPos;
    protected int mRowIdColumnIndex;
    private ContentObserver mSelfObserver;
    private final Object mSelfObserverLock;
    private boolean mSelfObserverRegistered;
    protected HashMap<Long, Map<String, Object>> mUpdatedRows;
    
    public AbstractCursor() {
        this.mDataSetObservable = new DataSetObservable();
        this.mContentObservable = new ContentObservable();
        this.mExtras = Bundle.EMPTY;
        this.mClosed = false;
        this.mSelfObserverLock = new Object();
        this.mPos = -1;
        this.mRowIdColumnIndex = -1;
        this.mCurrentRowID = null;
        this.mUpdatedRows = new HashMap<Long, Map<String, Object>>();
    }
    
    public void abortUpdates() {
        synchronized (this.mUpdatedRows) {
            this.mUpdatedRows.clear();
        }
    }
    
    protected void checkPosition() {
        if (-1 != this.mPos && this.getCount() != this.mPos) {
            return;
        }
        throw new CursorIndexOutOfBoundsException(this.mPos, this.getCount());
    }
    
    public void close() {
        this.mClosed = true;
        this.mContentObservable.unregisterAll();
        this.deactivateInternal();
    }
    
    public boolean commitUpdates() {
        return this.commitUpdates(null);
    }
    
    public boolean commitUpdates(final Map<? extends Long, ? extends Map<String, Object>> map) {
        return false;
    }
    
    public void copyStringToBuffer(final int n, final CharArrayBuffer charArrayBuffer) {
        final String string = this.getString(n);
        if (string != null) {
            final char[] data = charArrayBuffer.data;
            if (data != null && data.length >= string.length()) {
                string.getChars(0, string.length(), data, 0);
            }
            else {
                charArrayBuffer.data = string.toCharArray();
            }
            charArrayBuffer.sizeCopied = string.length();
        }
        else {
            charArrayBuffer.sizeCopied = 0;
        }
    }
    
    public void deactivate() {
        this.deactivateInternal();
    }
    
    public void deactivateInternal() {
        if (this.mSelfObserver != null) {
            this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
            this.mSelfObserverRegistered = false;
        }
        this.mDataSetObservable.notifyInvalidated();
    }
    
    public boolean deleteRow() {
        return false;
    }
    
    public void fillWindow(final int n, final CursorWindow cursorWindow) {
        DatabaseUtils.cursorFillWindow(this, n, cursorWindow);
    }
    
    @Override
    protected void finalize() {
        if (this.mSelfObserver != null && this.mSelfObserverRegistered) {
            this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
        }
    }
    
    public byte[] getBlob(final int n) {
        throw new UnsupportedOperationException("getBlob is not supported");
    }
    
    public int getColumnCount() {
        return this.getColumnNames().length;
    }
    
    public int getColumnIndex(final String str) {
        final int lastIndex = str.lastIndexOf(46);
        String substring = str;
        if (lastIndex != -1) {
            final Exception ex = new Exception();
            final StringBuilder sb = new StringBuilder();
            sb.append("requesting column name with table name -- ");
            sb.append(str);
            Log.e("Cursor", sb.toString(), (Throwable)ex);
            substring = str.substring(lastIndex + 1);
        }
        final String[] columnNames = this.getColumnNames();
        for (int length = columnNames.length, i = 0; i < length; ++i) {
            if (columnNames[i].equalsIgnoreCase(substring)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getColumnIndexOrThrow(final String str) {
        final int columnIndex = this.getColumnIndex(str);
        if (columnIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("column '");
            sb.append(str);
            sb.append("' does not exist");
            throw new IllegalArgumentException(sb.toString());
        }
        return columnIndex;
    }
    
    public String getColumnName(final int n) {
        return this.getColumnNames()[n];
    }
    
    public abstract String[] getColumnNames();
    
    public abstract int getCount();
    
    protected DataSetObservable getDataSetObservable() {
        return this.mDataSetObservable;
    }
    
    public abstract double getDouble(final int p0);
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public abstract float getFloat(final int p0);
    
    public abstract int getInt(final int p0);
    
    public abstract long getLong(final int p0);
    
    public Uri getNotificationUri() {
        return this.mNotifyUri;
    }
    
    public final int getPosition() {
        return this.mPos;
    }
    
    public abstract short getShort(final int p0);
    
    public abstract String getString(final int p0);
    
    public abstract int getType(final int p0);
    
    protected Object getUpdatedField(final int n) {
        return this.mUpdatedRows.get(this.mCurrentRowID).get(this.getColumnNames()[n]);
    }
    
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }
    
    public net.sqlcipher.CursorWindow getWindow() {
        return null;
    }
    
    public boolean hasUpdates() {
        synchronized (this.mUpdatedRows) {
            return this.mUpdatedRows.size() > 0;
        }
    }
    
    public final boolean isAfterLast() {
        final int count = this.getCount();
        boolean b = true;
        if (count == 0) {
            return true;
        }
        if (this.mPos != this.getCount()) {
            b = false;
        }
        return b;
    }
    
    public final boolean isBeforeFirst() {
        final int count = this.getCount();
        boolean b = true;
        if (count == 0) {
            return true;
        }
        if (this.mPos != -1) {
            b = false;
        }
        return b;
    }
    
    public boolean isClosed() {
        return this.mClosed;
    }
    
    protected boolean isFieldUpdated(final int n) {
        if (this.mRowIdColumnIndex != -1 && this.mUpdatedRows.size() > 0) {
            final Map<String, Object> map = this.mUpdatedRows.get(this.mCurrentRowID);
            if (map != null && map.containsKey(this.getColumnNames()[n])) {
                return true;
            }
        }
        return false;
    }
    
    public final boolean isFirst() {
        return this.mPos == 0 && this.getCount() != 0;
    }
    
    public final boolean isLast() {
        final int count = this.getCount();
        return this.mPos == count - 1 && count != 0;
    }
    
    public abstract boolean isNull(final int p0);
    
    public final boolean move(final int n) {
        return this.moveToPosition(this.mPos + n);
    }
    
    public final boolean moveToFirst() {
        return this.moveToPosition(0);
    }
    
    public final boolean moveToLast() {
        return this.moveToPosition(this.getCount() - 1);
    }
    
    public final boolean moveToNext() {
        return this.moveToPosition(this.mPos + 1);
    }
    
    public final boolean moveToPosition(final int mPos) {
        final int count = this.getCount();
        if (mPos >= count) {
            this.mPos = count;
            return false;
        }
        if (mPos < 0) {
            this.mPos = -1;
            return false;
        }
        if (mPos == this.mPos) {
            return true;
        }
        final boolean onMove = this.onMove(this.mPos, mPos);
        if (!onMove) {
            this.mPos = -1;
        }
        else {
            this.mPos = mPos;
            if (this.mRowIdColumnIndex != -1) {
                this.mCurrentRowID = this.getLong(this.mRowIdColumnIndex);
            }
        }
        return onMove;
    }
    
    public final boolean moveToPrevious() {
        return this.moveToPosition(this.mPos - 1);
    }
    
    protected void notifyDataSetChange() {
        this.mDataSetObservable.notifyChanged();
    }
    
    protected void onChange(final boolean b) {
        synchronized (this.mSelfObserverLock) {
            this.mContentObservable.dispatchChange(b);
            if (this.mNotifyUri != null && b) {
                this.mContentResolver.notifyChange(this.mNotifyUri, this.mSelfObserver);
            }
        }
    }
    
    public boolean onMove(final int n, final int n2) {
        return true;
    }
    
    public void registerContentObserver(final ContentObserver contentObserver) {
        this.mContentObservable.registerObserver(contentObserver);
    }
    
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mDataSetObservable.registerObserver((Object)dataSetObserver);
    }
    
    public boolean requery() {
        if (this.mSelfObserver != null && !this.mSelfObserverRegistered) {
            this.mContentResolver.registerContentObserver(this.mNotifyUri, true, this.mSelfObserver);
            this.mSelfObserverRegistered = true;
        }
        this.mDataSetObservable.notifyChanged();
        return true;
    }
    
    public Bundle respond(final Bundle bundle) {
        return Bundle.EMPTY;
    }
    
    public void setExtras(final Bundle bundle) {
        Bundle empty = bundle;
        if (bundle == null) {
            empty = Bundle.EMPTY;
        }
        this.mExtras = empty;
    }
    
    public void setNotificationUri(final ContentResolver mContentResolver, final Uri mNotifyUri) {
        synchronized (this.mSelfObserverLock) {
            this.mNotifyUri = mNotifyUri;
            this.mContentResolver = mContentResolver;
            if (this.mSelfObserver != null) {
                this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
            }
            this.mSelfObserver = new SelfContentObserver(this);
            this.mContentResolver.registerContentObserver(this.mNotifyUri, true, this.mSelfObserver);
            this.mSelfObserverRegistered = true;
        }
    }
    
    public boolean supportsUpdates() {
        return this.mRowIdColumnIndex != -1;
    }
    
    public void unregisterContentObserver(final ContentObserver contentObserver) {
        if (!this.mClosed) {
            this.mContentObservable.unregisterObserver((Object)contentObserver);
        }
    }
    
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mDataSetObservable.unregisterObserver((Object)dataSetObserver);
    }
    
    public boolean update(final int n, final Object o) {
        if (!this.supportsUpdates()) {
            return false;
        }
        final Long value = this.getLong(this.mRowIdColumnIndex);
        if (value == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("null rowid. mRowIdColumnIndex = ");
            sb.append(this.mRowIdColumnIndex);
            throw new IllegalStateException(sb.toString());
        }
        synchronized (this.mUpdatedRows) {
            Map<String, Object> value2;
            if ((value2 = this.mUpdatedRows.get(value)) == null) {
                value2 = new HashMap<String, Object>();
                this.mUpdatedRows.put(value, value2);
            }
            value2.put(this.getColumnNames()[n], o);
            return true;
        }
    }
    
    public boolean updateBlob(final int n, final byte[] array) {
        return this.update(n, array);
    }
    
    public boolean updateDouble(final int n, final double d) {
        return this.update(n, d);
    }
    
    public boolean updateFloat(final int n, final float f) {
        return this.update(n, f);
    }
    
    public boolean updateInt(final int n, final int i) {
        return this.update(n, i);
    }
    
    public boolean updateLong(final int n, final long l) {
        return this.update(n, l);
    }
    
    public boolean updateShort(final int n, final short s) {
        return this.update(n, s);
    }
    
    public boolean updateString(final int n, final String s) {
        return this.update(n, s);
    }
    
    public boolean updateToNull(final int n) {
        return this.update(n, null);
    }
    
    protected static class SelfContentObserver extends ContentObserver
    {
        WeakReference<AbstractCursor> mCursor;
        
        public SelfContentObserver(final AbstractCursor referent) {
            super((Handler)null);
            this.mCursor = new WeakReference<AbstractCursor>(referent);
        }
        
        public boolean deliverSelfNotifications() {
            return false;
        }
        
        public void onChange(final boolean b) {
            final AbstractCursor abstractCursor = this.mCursor.get();
            if (abstractCursor != null) {
                abstractCursor.onChange(false);
            }
        }
    }
}
