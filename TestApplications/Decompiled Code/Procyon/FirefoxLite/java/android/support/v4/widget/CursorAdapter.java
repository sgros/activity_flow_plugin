// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.os.Handler;
import android.database.ContentObserver;
import android.widget.Filter;
import android.view.ViewGroup;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.database.DataSetObserver;
import android.database.Cursor;
import android.content.Context;
import android.widget.Filterable;
import android.widget.BaseAdapter;

public abstract class CursorAdapter extends BaseAdapter implements CursorFilterClient, Filterable
{
    protected boolean mAutoRequery;
    protected ChangeObserver mChangeObserver;
    protected Context mContext;
    protected Cursor mCursor;
    protected CursorFilter mCursorFilter;
    protected DataSetObserver mDataSetObserver;
    protected boolean mDataValid;
    protected FilterQueryProvider mFilterQueryProvider;
    protected int mRowIDColumn;
    
    public CursorAdapter(final Context context, final Cursor cursor, final boolean b) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        this.init(context, cursor, n);
    }
    
    public abstract void bindView(final View p0, final Context p1, final Cursor p2);
    
    public void changeCursor(Cursor swapCursor) {
        swapCursor = this.swapCursor(swapCursor);
        if (swapCursor != null) {
            swapCursor.close();
        }
    }
    
    public CharSequence convertToString(final Cursor cursor) {
        String string;
        if (cursor == null) {
            string = "";
        }
        else {
            string = cursor.toString();
        }
        return string;
    }
    
    public int getCount() {
        if (this.mDataValid && this.mCursor != null) {
            return this.mCursor.getCount();
        }
        return 0;
    }
    
    public Cursor getCursor() {
        return this.mCursor;
    }
    
    public View getDropDownView(final int n, final View view, final ViewGroup viewGroup) {
        if (this.mDataValid) {
            this.mCursor.moveToPosition(n);
            View dropDownView;
            if ((dropDownView = view) == null) {
                dropDownView = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            }
            this.bindView(dropDownView, this.mContext, this.mCursor);
            return dropDownView;
        }
        return null;
    }
    
    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            this.mCursorFilter = new CursorFilter((CursorFilter.CursorFilterClient)this);
        }
        return this.mCursorFilter;
    }
    
    public Object getItem(final int n) {
        if (this.mDataValid && this.mCursor != null) {
            this.mCursor.moveToPosition(n);
            return this.mCursor;
        }
        return null;
    }
    
    public long getItemId(final int n) {
        if (!this.mDataValid || this.mCursor == null) {
            return 0L;
        }
        if (this.mCursor.moveToPosition(n)) {
            return this.mCursor.getLong(this.mRowIDColumn);
        }
        return 0L;
    }
    
    public View getView(final int i, final View view, final ViewGroup viewGroup) {
        if (!this.mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (this.mCursor.moveToPosition(i)) {
            View view2;
            if ((view2 = view) == null) {
                view2 = this.newView(this.mContext, this.mCursor, viewGroup);
            }
            this.bindView(view2, this.mContext, this.mCursor);
            return view2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("couldn't move cursor to position ");
        sb.append(i);
        throw new IllegalStateException(sb.toString());
    }
    
    public boolean hasStableIds() {
        return true;
    }
    
    void init(final Context mContext, final Cursor mCursor, int n) {
        boolean mDataValid = false;
        if ((n & 0x1) == 0x1) {
            n |= 0x2;
            this.mAutoRequery = true;
        }
        else {
            this.mAutoRequery = false;
        }
        if (mCursor != null) {
            mDataValid = true;
        }
        this.mCursor = mCursor;
        this.mDataValid = mDataValid;
        this.mContext = mContext;
        int columnIndexOrThrow;
        if (mDataValid) {
            columnIndexOrThrow = mCursor.getColumnIndexOrThrow("_id");
        }
        else {
            columnIndexOrThrow = -1;
        }
        this.mRowIDColumn = columnIndexOrThrow;
        if ((n & 0x2) == 0x2) {
            this.mChangeObserver = new ChangeObserver();
            this.mDataSetObserver = new MyDataSetObserver();
        }
        else {
            this.mChangeObserver = null;
            this.mDataSetObserver = null;
        }
        if (mDataValid) {
            if (this.mChangeObserver != null) {
                mCursor.registerContentObserver((ContentObserver)this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                mCursor.registerDataSetObserver(this.mDataSetObserver);
            }
        }
    }
    
    public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        return this.newView(context, cursor, viewGroup);
    }
    
    public abstract View newView(final Context p0, final Cursor p1, final ViewGroup p2);
    
    protected void onContentChanged() {
        if (this.mAutoRequery && this.mCursor != null && !this.mCursor.isClosed()) {
            this.mDataValid = this.mCursor.requery();
        }
    }
    
    public Cursor runQueryOnBackgroundThread(final CharSequence charSequence) {
        if (this.mFilterQueryProvider != null) {
            return this.mFilterQueryProvider.runQuery(charSequence);
        }
        return this.mCursor;
    }
    
    public Cursor swapCursor(final Cursor mCursor) {
        if (mCursor == this.mCursor) {
            return null;
        }
        final Cursor mCursor2 = this.mCursor;
        if (mCursor2 != null) {
            if (this.mChangeObserver != null) {
                mCursor2.unregisterContentObserver((ContentObserver)this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                mCursor2.unregisterDataSetObserver(this.mDataSetObserver);
            }
        }
        if ((this.mCursor = mCursor) != null) {
            if (this.mChangeObserver != null) {
                mCursor.registerContentObserver((ContentObserver)this.mChangeObserver);
            }
            if (this.mDataSetObserver != null) {
                mCursor.registerDataSetObserver(this.mDataSetObserver);
            }
            this.mRowIDColumn = mCursor.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
        }
        else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            this.notifyDataSetInvalidated();
        }
        return mCursor2;
    }
    
    private class ChangeObserver extends ContentObserver
    {
        ChangeObserver() {
            super(new Handler());
        }
        
        public boolean deliverSelfNotifications() {
            return true;
        }
        
        public void onChange(final boolean b) {
            CursorAdapter.this.onContentChanged();
        }
    }
    
    private class MyDataSetObserver extends DataSetObserver
    {
        MyDataSetObserver() {
        }
        
        public void onChanged() {
            CursorAdapter.this.mDataValid = true;
            CursorAdapter.this.notifyDataSetChanged();
        }
        
        public void onInvalidated() {
            CursorAdapter.this.mDataValid = false;
            CursorAdapter.this.notifyDataSetInvalidated();
        }
    }
}
