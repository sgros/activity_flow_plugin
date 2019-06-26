package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.CursorFilterClient {
   @Deprecated
   public static final int FLAG_AUTO_REQUERY = 1;
   public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected boolean mAutoRequery;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected CursorAdapter.ChangeObserver mChangeObserver;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected Context mContext;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected Cursor mCursor;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected CursorFilter mCursorFilter;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected DataSetObserver mDataSetObserver;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected boolean mDataValid;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected FilterQueryProvider mFilterQueryProvider;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected int mRowIDColumn;

   @Deprecated
   public CursorAdapter(Context var1, Cursor var2) {
      this.init(var1, var2, 1);
   }

   public CursorAdapter(Context var1, Cursor var2, int var3) {
      this.init(var1, var2, var3);
   }

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
      int var1;
      if (this.mDataValid && this.mCursor != null) {
         var1 = this.mCursor.getCount();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Cursor getCursor() {
      return this.mCursor;
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         this.mCursor.moveToPosition(var1);
         if (var2 == null) {
            var2 = this.newDropDownView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var2, this.mContext, this.mCursor);
      } else {
         var2 = null;
      }

      return var2;
   }

   public Filter getFilter() {
      if (this.mCursorFilter == null) {
         this.mCursorFilter = new CursorFilter(this);
      }

      return this.mCursorFilter;
   }

   public FilterQueryProvider getFilterQueryProvider() {
      return this.mFilterQueryProvider;
   }

   public Object getItem(int var1) {
      Cursor var2;
      if (this.mDataValid && this.mCursor != null) {
         this.mCursor.moveToPosition(var1);
         var2 = this.mCursor;
      } else {
         var2 = null;
      }

      return var2;
   }

   public long getItemId(int var1) {
      long var2 = 0L;
      long var4 = var2;
      if (this.mDataValid) {
         var4 = var2;
         if (this.mCursor != null) {
            var4 = var2;
            if (this.mCursor.moveToPosition(var1)) {
               var4 = this.mCursor.getLong(this.mRowIDColumn);
            }
         }
      }

      return var4;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (!this.mDataValid) {
         throw new IllegalStateException("this should only be called when the cursor is valid");
      } else if (!this.mCursor.moveToPosition(var1)) {
         throw new IllegalStateException("couldn't move cursor to position " + var1);
      } else {
         if (var2 == null) {
            var2 = this.newView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var2, this.mContext, this.mCursor);
         return var2;
      }
   }

   public boolean hasStableIds() {
      return true;
   }

   void init(Context var1, Cursor var2, int var3) {
      boolean var4 = true;
      if ((var3 & 1) == 1) {
         var3 |= 2;
         this.mAutoRequery = true;
      } else {
         this.mAutoRequery = false;
      }

      if (var2 == null) {
         var4 = false;
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

   @Deprecated
   protected void init(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
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
      Cursor var2;
      if (this.mFilterQueryProvider != null) {
         var2 = this.mFilterQueryProvider.runQuery(var1);
      } else {
         var2 = this.mCursor;
      }

      return var2;
   }

   public void setFilterQueryProvider(FilterQueryProvider var1) {
      this.mFilterQueryProvider = var1;
   }

   public Cursor swapCursor(Cursor var1) {
      if (var1 == this.mCursor) {
         var1 = null;
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
            var1 = var2;
         } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            this.notifyDataSetInvalidated();
            var1 = var2;
         }
      }

      return var1;
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
