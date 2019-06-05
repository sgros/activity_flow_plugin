package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements CursorFilter.CursorFilterClient, Filterable {
   protected boolean mAutoRequery;
   protected CursorAdapter.ChangeObserver mChangeObserver;
   protected Context mContext;
   protected Cursor mCursor;
   protected CursorFilter mCursorFilter;
   protected DataSetObserver mDataSetObserver;
   protected boolean mDataValid;
   protected FilterQueryProvider mFilterQueryProvider;
   protected int mRowIDColumn;

   public CursorAdapter(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
   }

   public abstract void bindView(View var1, Context var2, Cursor var3);

   public void changeCursor(Cursor var1) {
      var1 = this.swapCursor(var1);
      if (var1 != null) {
         var1.close();
      }

   }

   public CharSequence convertToString(Cursor var1) {
      String var2;
      if (var1 == null) {
         var2 = "";
      } else {
         var2 = var1.toString();
      }

      return var2;
   }

   public int getCount() {
      return this.mDataValid && this.mCursor != null ? this.mCursor.getCount() : 0;
   }

   public Cursor getCursor() {
      return this.mCursor;
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         this.mCursor.moveToPosition(var1);
         View var4 = var2;
         if (var2 == null) {
            var4 = this.newDropDownView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var4, this.mContext, this.mCursor);
         return var4;
      } else {
         return null;
      }
   }

   public Filter getFilter() {
      if (this.mCursorFilter == null) {
         this.mCursorFilter = new CursorFilter(this);
      }

      return this.mCursorFilter;
   }

   public Object getItem(int var1) {
      if (this.mDataValid && this.mCursor != null) {
         this.mCursor.moveToPosition(var1);
         return this.mCursor;
      } else {
         return null;
      }
   }

   public long getItemId(int var1) {
      if (this.mDataValid && this.mCursor != null) {
         return this.mCursor.moveToPosition(var1) ? this.mCursor.getLong(this.mRowIDColumn) : 0L;
      } else {
         return 0L;
      }
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         if (this.mCursor.moveToPosition(var1)) {
            View var4 = var2;
            if (var2 == null) {
               var4 = this.newView(this.mContext, this.mCursor, var3);
            }

            this.bindView(var4, this.mContext, this.mCursor);
            return var4;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("couldn't move cursor to position ");
            var5.append(var1);
            throw new IllegalStateException(var5.toString());
         }
      } else {
         throw new IllegalStateException("this should only be called when the cursor is valid");
      }
   }

   public boolean hasStableIds() {
      return true;
   }

   void init(Context var1, Cursor var2, int var3) {
      boolean var4 = false;
      if ((var3 & 1) == 1) {
         var3 |= 2;
         this.mAutoRequery = true;
      } else {
         this.mAutoRequery = false;
      }

      if (var2 != null) {
         var4 = true;
      }

      this.mCursor = var2;
      this.mDataValid = var4;
      this.mContext = var1;
      int var5;
      if (var4) {
         var5 = var2.getColumnIndexOrThrow("_id");
      } else {
         var5 = -1;
      }

      this.mRowIDColumn = var5;
      if ((var3 & 2) == 2) {
         this.mChangeObserver = new CursorAdapter.ChangeObserver();
         this.mDataSetObserver = new CursorAdapter.MyDataSetObserver();
      } else {
         this.mChangeObserver = null;
         this.mDataSetObserver = null;
      }

      if (var4) {
         if (this.mChangeObserver != null) {
            var2.registerContentObserver(this.mChangeObserver);
         }

         if (this.mDataSetObserver != null) {
            var2.registerDataSetObserver(this.mDataSetObserver);
         }
      }

   }

   public View newDropDownView(Context var1, Cursor var2, ViewGroup var3) {
      return this.newView(var1, var2, var3);
   }

   public abstract View newView(Context var1, Cursor var2, ViewGroup var3);

   protected void onContentChanged() {
      if (this.mAutoRequery && this.mCursor != null && !this.mCursor.isClosed()) {
         this.mDataValid = this.mCursor.requery();
      }

   }

   public Cursor runQueryOnBackgroundThread(CharSequence var1) {
      return this.mFilterQueryProvider != null ? this.mFilterQueryProvider.runQuery(var1) : this.mCursor;
   }

   public Cursor swapCursor(Cursor var1) {
      if (var1 == this.mCursor) {
         return null;
      } else {
         Cursor var2 = this.mCursor;
         if (var2 != null) {
            if (this.mChangeObserver != null) {
               var2.unregisterContentObserver(this.mChangeObserver);
            }

            if (this.mDataSetObserver != null) {
               var2.unregisterDataSetObserver(this.mDataSetObserver);
            }
         }

         this.mCursor = var1;
         if (var1 != null) {
            if (this.mChangeObserver != null) {
               var1.registerContentObserver(this.mChangeObserver);
            }

            if (this.mDataSetObserver != null) {
               var1.registerDataSetObserver(this.mDataSetObserver);
            }

            this.mRowIDColumn = var1.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
         } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            this.notifyDataSetInvalidated();
         }

         return var2;
      }
   }

   private class ChangeObserver extends ContentObserver {
      ChangeObserver() {
         super(new Handler());
      }

      public boolean deliverSelfNotifications() {
         return true;
      }

      public void onChange(boolean var1) {
         CursorAdapter.this.onContentChanged();
      }
   }

   private class MyDataSetObserver extends DataSetObserver {
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
