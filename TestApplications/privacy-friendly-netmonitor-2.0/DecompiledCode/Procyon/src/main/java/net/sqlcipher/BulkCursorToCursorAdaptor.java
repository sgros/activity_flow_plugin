// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.database.DataSetObserver;
import android.database.ContentObserver;
import android.os.Bundle;
import android.database.CharArrayBuffer;
import java.util.HashMap;
import java.util.Map;
import android.os.RemoteException;
import android.util.Log;

public final class BulkCursorToCursorAdaptor extends AbstractWindowedCursor
{
    private static final String TAG = "BulkCursor";
    private IBulkCursor mBulkCursor;
    private String[] mColumns;
    private int mCount;
    private SelfContentObserver mObserverBridge;
    private boolean mWantsAllOnMoveCalls;
    
    public static int findRowIdColumnIndex(final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals("_id")) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void close() {
        super.close();
        try {
            this.mBulkCursor.close();
        }
        catch (RemoteException ex) {
            Log.w("BulkCursor", "Remote process exception when closing");
        }
        this.mWindow = null;
    }
    
    @Override
    public boolean commitUpdates(final Map<? extends Long, ? extends Map<String, Object>> m) {
        if (!this.supportsUpdates()) {
            Log.e("BulkCursor", "commitUpdates not supported on this cursor, did you include the _id column?");
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
                // iftrue(Label_0057:, this.mUpdatedRows.size() > 0)
                // monitorexit(mUpdatedRows)
                return false;
                try {
                    final boolean updateRows;
                    Label_0057: {
                        updateRows = this.mBulkCursor.updateRows(this.mUpdatedRows);
                    }
                    if (updateRows) {
                        this.mUpdatedRows.clear();
                        this.onChange(true);
                    }
                    // monitorexit(mUpdatedRows)
                    return updateRows;
                }
                catch (RemoteException ex) {
                    Log.e("BulkCursor", "Unable to commit updates because the remote process is dead");
                    // monitorexit(mUpdatedRows)
                    return false;
                }
            }
        }
    }
    
    @Override
    public void copyStringToBuffer(final int n, final CharArrayBuffer charArrayBuffer) {
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        try {
            this.mBulkCursor.deactivate();
        }
        catch (RemoteException ex) {
            Log.w("BulkCursor", "Remote process exception when deactivating");
        }
        this.mWindow = null;
    }
    
    @Override
    public boolean deleteRow() {
        try {
            final boolean deleteRow = this.mBulkCursor.deleteRow(this.mPos);
            if (deleteRow) {
                this.mWindow = null;
                this.mCount = this.mBulkCursor.count();
                if (this.mPos < this.mCount) {
                    final int mPos = this.mPos;
                    this.mPos = -1;
                    this.moveToPosition(mPos);
                }
                else {
                    this.mPos = this.mCount;
                }
                this.onChange(true);
            }
            return deleteRow;
        }
        catch (RemoteException ex) {
            Log.e("BulkCursor", "Unable to delete row because the remote process is dead");
            return false;
        }
    }
    
    @Override
    public String[] getColumnNames() {
        if (this.mColumns == null) {
            try {
                this.mColumns = this.mBulkCursor.getColumnNames();
            }
            catch (RemoteException ex) {
                Log.e("BulkCursor", "Unable to fetch column names because the remote process is dead");
                return null;
            }
        }
        return this.mColumns;
    }
    
    @Override
    public int getCount() {
        return this.mCount;
    }
    
    @Override
    public Bundle getExtras() {
        try {
            return this.mBulkCursor.getExtras();
        }
        catch (RemoteException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    public IContentObserver getObserver() {
        synchronized (this) {
            if (this.mObserverBridge == null) {
                this.mObserverBridge = new SelfContentObserver(this);
            }
            return null;
        }
    }
    
    @Override
    public boolean onMove(final int n, final int n2) {
        try {
            if (this.mWindow != null) {
                if (n2 >= this.mWindow.getStartPosition() && n2 < this.mWindow.getStartPosition() + this.mWindow.getNumRows()) {
                    if (this.mWantsAllOnMoveCalls) {
                        this.mBulkCursor.onMove(n2);
                    }
                }
                else {
                    this.mWindow = this.mBulkCursor.getWindow(n2);
                }
            }
            else {
                this.mWindow = this.mBulkCursor.getWindow(n2);
            }
            return this.mWindow != null;
        }
        catch (RemoteException ex) {
            Log.e("BulkCursor", "Unable to get window because the remote process is dead");
            return false;
        }
    }
    
    @Override
    public void registerContentObserver(final ContentObserver contentObserver) {
    }
    
    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
    }
    
    @Override
    public boolean requery() {
        try {
            final int mCount = this.mCount;
            this.mCount = this.mBulkCursor.requery(this.getObserver(), new CursorWindow(false));
            if (this.mCount != -1) {
                this.mPos = -1;
                this.mWindow = null;
                super.requery();
                return true;
            }
            this.deactivate();
            return false;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to requery because the remote process exception ");
            sb.append(ex.getMessage());
            Log.e("BulkCursor", sb.toString());
            this.deactivate();
            return false;
        }
    }
    
    @Override
    public Bundle respond(Bundle respond) {
        try {
            respond = this.mBulkCursor.respond(respond);
            return respond;
        }
        catch (RemoteException ex) {
            Log.w("BulkCursor", "respond() threw RemoteException, returning an empty bundle.", (Throwable)ex);
            return Bundle.EMPTY;
        }
    }
    
    public void set(final IBulkCursor mBulkCursor) {
        this.mBulkCursor = mBulkCursor;
        try {
            this.mCount = this.mBulkCursor.count();
            this.mWantsAllOnMoveCalls = this.mBulkCursor.getWantsAllOnMoveCalls();
            this.mColumns = this.mBulkCursor.getColumnNames();
            this.mRowIdColumnIndex = findRowIdColumnIndex(this.mColumns);
        }
        catch (RemoteException ex) {
            Log.e("BulkCursor", "Setup failed because the remote process is dead");
        }
    }
    
    public void set(final IBulkCursor mBulkCursor, final int mCount, final int mRowIdColumnIndex) {
        this.mBulkCursor = mBulkCursor;
        this.mColumns = null;
        this.mCount = mCount;
        this.mRowIdColumnIndex = mRowIdColumnIndex;
    }
    
    @Override
    public void unregisterContentObserver(final ContentObserver contentObserver) {
    }
    
    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
    }
}
