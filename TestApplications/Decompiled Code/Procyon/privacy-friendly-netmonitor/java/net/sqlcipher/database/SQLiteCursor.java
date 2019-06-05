// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.os.Process;
import android.os.Message;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.text.TextUtils;
import android.database.Cursor;
import android.database.DataSetObserver;
import net.sqlcipher.SQLException;
import java.util.Iterator;
import java.util.HashMap;
import android.util.Log;
import net.sqlcipher.CursorWindow;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import net.sqlcipher.AbstractWindowedCursor;

public class SQLiteCursor extends AbstractWindowedCursor
{
    static final int NO_COUNT = -1;
    static final String TAG = "Cursor";
    private Map<String, Integer> mColumnNameMap;
    private String[] mColumns;
    private int mCount;
    private int mCursorState;
    private SQLiteDatabase mDatabase;
    private SQLiteCursorDriver mDriver;
    private String mEditTable;
    private int mInitialRead;
    private ReentrantLock mLock;
    private int mMaxRead;
    protected MainThreadNotificationHandler mNotificationHandler;
    private boolean mPendingData;
    private SQLiteQuery mQuery;
    private Throwable mStackTrace;
    
    public SQLiteCursor(final SQLiteDatabase mDatabase, final SQLiteCursorDriver mDriver, final String mEditTable, final SQLiteQuery mQuery) {
        this.mCount = -1;
        this.mMaxRead = Integer.MAX_VALUE;
        this.mInitialRead = Integer.MAX_VALUE;
        int i = 0;
        this.mCursorState = 0;
        this.mLock = null;
        this.mPendingData = false;
        this.mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
        this.mDatabase = mDatabase;
        this.mDriver = mDriver;
        this.mEditTable = mEditTable;
        this.mColumnNameMap = null;
        this.mQuery = mQuery;
        try {
            mDatabase.lock();
            final int columnCountLocked = this.mQuery.columnCountLocked();
            this.mColumns = new String[columnCountLocked];
            while (i < columnCountLocked) {
                final String columnNameLocked = this.mQuery.columnNameLocked(i);
                this.mColumns[i] = columnNameLocked;
                if ("_id".equals(columnNameLocked)) {
                    this.mRowIdColumnIndex = i;
                }
                ++i;
            }
        }
        finally {
            mDatabase.unlock();
        }
    }
    
    private void deactivateCommon() {
        this.mCursorState = 0;
        if (this.mWindow != null) {
            this.mWindow.close();
            this.mWindow = null;
        }
    }
    
    private void fillWindow(final int startPosition) {
        Label_0047: {
            if (this.mWindow == null) {
                this.mWindow = new CursorWindow(true);
                break Label_0047;
            }
            ++this.mCursorState;
            this.queryThreadLock();
            try {
                this.mWindow.clear();
                this.queryThreadUnlock();
                this.mWindow.setStartPosition(startPosition);
                this.mCount = this.mQuery.fillWindow(this.mWindow, this.mInitialRead, 0);
                if (this.mCount == -1) {
                    this.mCount = startPosition + this.mInitialRead;
                    new Thread(new QueryThread(this.mCursorState), "query thread").start();
                }
            }
            finally {
                this.queryThreadUnlock();
            }
        }
    }
    
    private void queryThreadLock() {
        if (this.mLock != null) {
            this.mLock.lock();
        }
    }
    
    private void queryThreadUnlock() {
        if (this.mLock != null) {
            this.mLock.unlock();
        }
    }
    
    @Override
    public void close() {
        super.close();
        this.deactivateCommon();
        this.mQuery.close();
        this.mDriver.cursorClosed();
    }
    
    @Override
    public boolean commitUpdates(final Map<? extends Long, ? extends Map<String, Object>> m) {
        if (!this.supportsUpdates()) {
            Log.e("Cursor", "commitUpdates not supported on this cursor, did you include the _id column?");
            return false;
        }
        final HashMap<Long, Map<String, Object>> mUpdatedRows = this.mUpdatedRows;
        // monitorenter(mUpdatedRows)
        Label_0043: {
            if (m == null) {
                break Label_0043;
            }
            try {
                this.mUpdatedRows.putAll(m);
                break Label_0043;
            }
            finally {
                // monitorexit(mUpdatedRows)
                Label_0057: {
                    this.mDatabase.beginTransaction();
                }
                try {
                    final StringBuilder sb = new StringBuilder(128);
                    for (final Map.Entry<Long, Map<String, Object>> entry : this.mUpdatedRows.entrySet()) {
                        final Map<String, Object> obj = entry.getValue();
                        final Long obj2 = entry.getKey();
                        if (obj2 == null || obj == null) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("null rowId or values found! rowId = ");
                            sb2.append(obj2);
                            sb2.append(", values = ");
                            sb2.append(obj);
                            throw new IllegalStateException(sb2.toString());
                        }
                        if (obj.size() == 0) {
                            continue;
                        }
                        final long longValue = obj2;
                        final Iterator<Map.Entry<String, V>> iterator2 = obj.entrySet().iterator();
                        sb.setLength(0);
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("UPDATE ");
                        sb3.append(this.mEditTable);
                        sb3.append(" SET ");
                        sb.append(sb3.toString());
                        final Object[] array = new Object[obj.size()];
                        int n = 0;
                        while (iterator2.hasNext()) {
                            final Map.Entry<String, V> entry2 = iterator2.next();
                            sb.append(entry2.getKey());
                            sb.append("=?");
                            array[n] = entry2.getValue();
                            if (iterator2.hasNext()) {
                                sb.append(", ");
                            }
                            ++n;
                        }
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(" WHERE ");
                        sb4.append(this.mColumns[this.mRowIdColumnIndex]);
                        sb4.append('=');
                        sb4.append(longValue);
                        sb.append(sb4.toString());
                        sb.append(';');
                        this.mDatabase.execSQL(sb.toString(), array);
                        this.mDatabase.rowUpdated(this.mEditTable, longValue);
                    }
                    this.mDatabase.setTransactionSuccessful();
                    this.mDatabase.endTransaction();
                    this.mUpdatedRows.clear();
                    // monitorexit(mUpdatedRows)
                    this.onChange(true);
                    return true;
                }
                finally {
                    this.mDatabase.endTransaction();
                }
                // iftrue(Label_0057:, this.mUpdatedRows.size() != 0)
                // monitorexit(mUpdatedRows)
                return true;
            }
        }
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        this.deactivateCommon();
        this.mDriver.cursorDeactivated();
    }
    
    @Override
    public boolean deleteRow() {
        this.checkPosition();
        if (this.mRowIdColumnIndex == -1 || this.mCurrentRowID == null) {
            Log.e("Cursor", "Could not delete row because either the row ID column is not available or ithas not been read.");
            return false;
        }
        this.mDatabase.lock();
        boolean b;
        try {
            try {
                final SQLiteDatabase mDatabase = this.mDatabase;
                final String mEditTable = this.mEditTable;
                final StringBuilder sb = new StringBuilder();
                sb.append(this.mColumns[this.mRowIdColumnIndex]);
                sb.append("=?");
                mDatabase.delete(mEditTable, sb.toString(), new String[] { this.mCurrentRowID.toString() });
                b = true;
            }
            finally {}
        }
        catch (SQLException ex) {
            b = false;
        }
        final int mPos = this.mPos;
        this.requery();
        this.moveToPosition(mPos);
        this.mDatabase.unlock();
        if (b) {
            this.onChange(true);
            return true;
        }
        return false;
        this.mDatabase.unlock();
    }
    
    @Override
    public void fillWindow(final int startPosition, final android.database.CursorWindow cursorWindow) {
        Label_0047: {
            if (this.mWindow == null) {
                this.mWindow = new CursorWindow(true);
                break Label_0047;
            }
            ++this.mCursorState;
            this.queryThreadLock();
            try {
                this.mWindow.clear();
                this.queryThreadUnlock();
                this.mWindow.setStartPosition(startPosition);
                this.mCount = this.mQuery.fillWindow(this.mWindow, this.mInitialRead, 0);
                if (this.mCount == -1) {
                    this.mCount = startPosition + this.mInitialRead;
                    new Thread(new QueryThread(this.mCursorState), "query thread").start();
                }
            }
            finally {
                this.queryThreadUnlock();
            }
        }
    }
    
    @Override
    protected void finalize() {
        try {
            if (this.mWindow != null) {
                final int length = this.mQuery.mSql.length();
                final StringBuilder sb = new StringBuilder();
                sb.append("Finalizing a Cursor that has not been deactivated or closed. database = ");
                sb.append(this.mDatabase.getPath());
                sb.append(", table = ");
                sb.append(this.mEditTable);
                sb.append(", query = ");
                final String mSql = this.mQuery.mSql;
                int endIndex;
                if ((endIndex = length) > 100) {
                    endIndex = 100;
                }
                sb.append(mSql.substring(0, endIndex));
                Log.e("Cursor", sb.toString(), this.mStackTrace);
                this.close();
                SQLiteDebug.notifyActiveCursorFinalized();
            }
        }
        finally {
            super.finalize();
        }
    }
    
    @Override
    public int getColumnIndex(final String str) {
        if (this.mColumnNameMap == null) {
            final String[] mColumns = this.mColumns;
            final int length = mColumns.length;
            final HashMap mColumnNameMap = new HashMap<String, Integer>(length, 1.0f);
            for (int i = 0; i < length; ++i) {
                mColumnNameMap.put(mColumns[i], i);
            }
            this.mColumnNameMap = (Map<String, Integer>)mColumnNameMap;
        }
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
        final Integer n = this.mColumnNameMap.get(substring);
        if (n != null) {
            return n;
        }
        return -1;
    }
    
    @Override
    public String[] getColumnNames() {
        return this.mColumns;
    }
    
    @Override
    public int getCount() {
        if (this.mCount == -1) {
            this.fillWindow(0);
        }
        return this.mCount;
    }
    
    public SQLiteDatabase getDatabase() {
        return this.mDatabase;
    }
    
    @Override
    public boolean onMove(final int n, final int n2) {
        if (this.mWindow == null || n2 < this.mWindow.getStartPosition() || n2 >= this.mWindow.getStartPosition() + this.mWindow.getNumRows()) {
            this.fillWindow(n2);
        }
        return true;
    }
    
    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        super.registerDataSetObserver(dataSetObserver);
        if ((Integer.MAX_VALUE != this.mMaxRead || Integer.MAX_VALUE != this.mInitialRead) && this.mNotificationHandler == null) {
            this.queryThreadLock();
            try {
                this.mNotificationHandler = new MainThreadNotificationHandler(this);
                if (this.mPendingData) {
                    this.notifyDataSetChange();
                    this.mPendingData = false;
                }
            }
            finally {
                this.queryThreadUnlock();
            }
        }
    }
    
    @Override
    public boolean requery() {
        if (this.isClosed()) {
            return false;
        }
        this.mDatabase.lock();
        try {
            if (this.mWindow != null) {
                this.mWindow.clear();
            }
            this.mPos = -1;
            this.mDriver.cursorRequeried((android.database.Cursor)this);
            this.mCount = -1;
            ++this.mCursorState;
            this.queryThreadLock();
            try {
                this.mQuery.requery();
                this.queryThreadUnlock();
                this.mDatabase.unlock();
                return super.requery();
            }
            finally {
                this.queryThreadUnlock();
            }
        }
        finally {
            this.mDatabase.unlock();
        }
    }
    
    public void setLoadStyle(final int mInitialRead, final int mMaxRead) {
        this.mMaxRead = mMaxRead;
        this.mInitialRead = mInitialRead;
        this.mLock = new ReentrantLock(true);
    }
    
    public void setSelectionArguments(final String[] bindArguments) {
        this.mDriver.setBindArguments(bindArguments);
    }
    
    @Override
    public void setWindow(final CursorWindow mWindow) {
        if (this.mWindow != null) {
            ++this.mCursorState;
            this.queryThreadLock();
            try {
                this.mWindow.close();
                this.queryThreadUnlock();
                this.mCount = -1;
            }
            finally {
                this.queryThreadUnlock();
            }
        }
        this.mWindow = mWindow;
    }
    
    @Override
    public boolean supportsUpdates() {
        return TextUtils.isEmpty((CharSequence)this.mEditTable) ^ true;
    }
    
    protected static class MainThreadNotificationHandler extends Handler
    {
        private final WeakReference<SQLiteCursor> wrappedCursor;
        
        MainThreadNotificationHandler(final SQLiteCursor referent) {
            this.wrappedCursor = new WeakReference<SQLiteCursor>(referent);
        }
        
        public void handleMessage(final Message message) {
            final SQLiteCursor sqLiteCursor = this.wrappedCursor.get();
            if (sqLiteCursor != null) {
                sqLiteCursor.notifyDataSetChange();
            }
        }
    }
    
    private final class QueryThread implements Runnable
    {
        private final int mThreadState;
        
        QueryThread(final int mThreadState) {
            this.mThreadState = mThreadState;
        }
        
        private void sendMessage() {
            if (SQLiteCursor.this.mNotificationHandler != null) {
                SQLiteCursor.this.mNotificationHandler.sendEmptyMessage(1);
                SQLiteCursor.this.mPendingData = false;
            }
            else {
                SQLiteCursor.this.mPendingData = true;
            }
        }
        
        @Override
        public void run() {
            final CursorWindow access$100 = SQLiteCursor.this.mWindow;
            Process.setThreadPriority(Process.myTid(), 10);
            while (true) {
                SQLiteCursor.this.mLock.lock();
                if (SQLiteCursor.this.mCursorState != this.mThreadState) {
                    break;
                }
                try {
                    final int fillWindow = SQLiteCursor.this.mQuery.fillWindow(access$100, SQLiteCursor.this.mMaxRead, SQLiteCursor.this.mCount);
                    if (fillWindow == 0) {
                        goto Label_0157;
                    }
                    if (fillWindow != -1) {
                        SQLiteCursor.this.mCount = fillWindow;
                        this.sendMessage();
                        goto Label_0157;
                    }
                    SQLiteCursor.this.mCount += SQLiteCursor.this.mMaxRead;
                    this.sendMessage();
                }
                catch (Exception ex) {
                    goto Label_0157;
                }
                finally {
                    SQLiteCursor.this.mLock.unlock();
                }
            }
            SQLiteCursor.this.mLock.unlock();
            goto Label_0167;
        }
    }
}
