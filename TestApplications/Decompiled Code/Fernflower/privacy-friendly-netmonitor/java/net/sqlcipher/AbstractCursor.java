package net.sqlcipher;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.CrossProcessCursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCursor implements CrossProcessCursor, Cursor {
   private static final String TAG = "Cursor";
   protected boolean mClosed;
   ContentObservable mContentObservable = new ContentObservable();
   protected ContentResolver mContentResolver;
   protected Long mCurrentRowID;
   DataSetObservable mDataSetObservable = new DataSetObservable();
   private Bundle mExtras;
   private Uri mNotifyUri;
   protected int mPos;
   protected int mRowIdColumnIndex;
   private ContentObserver mSelfObserver;
   private final Object mSelfObserverLock;
   private boolean mSelfObserverRegistered;
   protected HashMap mUpdatedRows;

   public AbstractCursor() {
      this.mExtras = Bundle.EMPTY;
      this.mClosed = false;
      this.mSelfObserverLock = new Object();
      this.mPos = -1;
      this.mRowIdColumnIndex = -1;
      this.mCurrentRowID = null;
      this.mUpdatedRows = new HashMap();
   }

   public void abortUpdates() {
      // $FF: Couldn't be decompiled
   }

   protected void checkPosition() {
      if (-1 == this.mPos || this.getCount() == this.mPos) {
         throw new CursorIndexOutOfBoundsException(this.mPos, this.getCount());
      }
   }

   public void close() {
      this.mClosed = true;
      this.mContentObservable.unregisterAll();
      this.deactivateInternal();
   }

   public boolean commitUpdates() {
      return this.commitUpdates((Map)null);
   }

   public boolean commitUpdates(Map var1) {
      return false;
   }

   public void copyStringToBuffer(int var1, CharArrayBuffer var2) {
      String var3 = this.getString(var1);
      if (var3 != null) {
         char[] var4 = var2.data;
         if (var4 != null && var4.length >= var3.length()) {
            var3.getChars(0, var3.length(), var4, 0);
         } else {
            var2.data = var3.toCharArray();
         }

         var2.sizeCopied = var3.length();
      } else {
         var2.sizeCopied = 0;
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

   public void fillWindow(int var1, android.database.CursorWindow var2) {
      DatabaseUtils.cursorFillWindow(this, var1, var2);
   }

   protected void finalize() {
      if (this.mSelfObserver != null && this.mSelfObserverRegistered) {
         this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
      }

   }

   public byte[] getBlob(int var1) {
      throw new UnsupportedOperationException("getBlob is not supported");
   }

   public int getColumnCount() {
      return this.getColumnNames().length;
   }

   public int getColumnIndex(String var1) {
      int var2 = var1.lastIndexOf(46);
      String var3 = var1;
      if (var2 != -1) {
         Exception var4 = new Exception();
         StringBuilder var7 = new StringBuilder();
         var7.append("requesting column name with table name -- ");
         var7.append(var1);
         Log.e("Cursor", var7.toString(), var4);
         var3 = var1.substring(var2 + 1);
      }

      String[] var6 = this.getColumnNames();
      int var5 = var6.length;

      for(var2 = 0; var2 < var5; ++var2) {
         if (var6[var2].equalsIgnoreCase(var3)) {
            return var2;
         }
      }

      return -1;
   }

   public int getColumnIndexOrThrow(String var1) {
      int var2 = this.getColumnIndex(var1);
      if (var2 < 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append("column '");
         var3.append(var1);
         var3.append("' does not exist");
         throw new IllegalArgumentException(var3.toString());
      } else {
         return var2;
      }
   }

   public String getColumnName(int var1) {
      return this.getColumnNames()[var1];
   }

   public abstract String[] getColumnNames();

   public abstract int getCount();

   protected DataSetObservable getDataSetObservable() {
      return this.mDataSetObservable;
   }

   public abstract double getDouble(int var1);

   public Bundle getExtras() {
      return this.mExtras;
   }

   public abstract float getFloat(int var1);

   public abstract int getInt(int var1);

   public abstract long getLong(int var1);

   public Uri getNotificationUri() {
      return this.mNotifyUri;
   }

   public final int getPosition() {
      return this.mPos;
   }

   public abstract short getShort(int var1);

   public abstract String getString(int var1);

   public abstract int getType(int var1);

   protected Object getUpdatedField(int var1) {
      return ((Map)this.mUpdatedRows.get(this.mCurrentRowID)).get(this.getColumnNames()[var1]);
   }

   public boolean getWantsAllOnMoveCalls() {
      return false;
   }

   public CursorWindow getWindow() {
      return null;
   }

   public boolean hasUpdates() {
      HashMap var1 = this.mUpdatedRows;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var2;
         label133: {
            label132: {
               try {
                  if (this.mUpdatedRows.size() > 0) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var2 = false;
               break label133;
            }

            var2 = true;
         }

         label126:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public final boolean isAfterLast() {
      int var1 = this.getCount();
      boolean var2 = true;
      if (var1 == 0) {
         return true;
      } else {
         if (this.mPos != this.getCount()) {
            var2 = false;
         }

         return var2;
      }
   }

   public final boolean isBeforeFirst() {
      int var1 = this.getCount();
      boolean var2 = true;
      if (var1 == 0) {
         return true;
      } else {
         if (this.mPos != -1) {
            var2 = false;
         }

         return var2;
      }
   }

   public boolean isClosed() {
      return this.mClosed;
   }

   protected boolean isFieldUpdated(int var1) {
      if (this.mRowIdColumnIndex != -1 && this.mUpdatedRows.size() > 0) {
         Map var2 = (Map)this.mUpdatedRows.get(this.mCurrentRowID);
         if (var2 != null && var2.containsKey(this.getColumnNames()[var1])) {
            return true;
         }
      }

      return false;
   }

   public final boolean isFirst() {
      boolean var1;
      if (this.mPos == 0 && this.getCount() != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isLast() {
      int var1 = this.getCount();
      boolean var2;
      if (this.mPos == var1 - 1 && var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public abstract boolean isNull(int var1);

   public final boolean move(int var1) {
      return this.moveToPosition(this.mPos + var1);
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

   public final boolean moveToPosition(int var1) {
      int var2 = this.getCount();
      if (var1 >= var2) {
         this.mPos = var2;
         return false;
      } else if (var1 < 0) {
         this.mPos = -1;
         return false;
      } else if (var1 == this.mPos) {
         return true;
      } else {
         boolean var3 = this.onMove(this.mPos, var1);
         if (!var3) {
            this.mPos = -1;
         } else {
            this.mPos = var1;
            if (this.mRowIdColumnIndex != -1) {
               this.mCurrentRowID = this.getLong(this.mRowIdColumnIndex);
            }
         }

         return var3;
      }
   }

   public final boolean moveToPrevious() {
      return this.moveToPosition(this.mPos - 1);
   }

   protected void notifyDataSetChange() {
      this.mDataSetObservable.notifyChanged();
   }

   protected void onChange(boolean var1) {
      Object var2 = this.mSelfObserverLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label173: {
         label172: {
            try {
               this.mContentObservable.dispatchChange(var1);
               if (this.mNotifyUri == null) {
                  break label172;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label173;
            }

            if (var1) {
               try {
                  this.mContentResolver.notifyChange(this.mNotifyUri, this.mSelfObserver);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label173;
               }
            }
         }

         label165:
         try {
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean onMove(int var1, int var2) {
      return true;
   }

   public void registerContentObserver(ContentObserver var1) {
      this.mContentObservable.registerObserver(var1);
   }

   public void registerDataSetObserver(DataSetObserver var1) {
      this.mDataSetObservable.registerObserver(var1);
   }

   public boolean requery() {
      if (this.mSelfObserver != null && !this.mSelfObserverRegistered) {
         this.mContentResolver.registerContentObserver(this.mNotifyUri, true, this.mSelfObserver);
         this.mSelfObserverRegistered = true;
      }

      this.mDataSetObservable.notifyChanged();
      return true;
   }

   public Bundle respond(Bundle var1) {
      return Bundle.EMPTY;
   }

   public void setExtras(Bundle var1) {
      Bundle var2 = var1;
      if (var1 == null) {
         var2 = Bundle.EMPTY;
      }

      this.mExtras = var2;
   }

   public void setNotificationUri(ContentResolver var1, Uri var2) {
      Object var3 = this.mSelfObserverLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.mNotifyUri = var2;
            this.mContentResolver = var1;
            if (this.mSelfObserver != null) {
               this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            AbstractCursor.SelfContentObserver var17 = new AbstractCursor.SelfContentObserver(this);
            this.mSelfObserver = var17;
            this.mContentResolver.registerContentObserver(this.mNotifyUri, true, this.mSelfObserver);
            this.mSelfObserverRegistered = true;
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean supportsUpdates() {
      boolean var1;
      if (this.mRowIdColumnIndex != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void unregisterContentObserver(ContentObserver var1) {
      if (!this.mClosed) {
         this.mContentObservable.unregisterObserver(var1);
      }

   }

   public void unregisterDataSetObserver(DataSetObserver var1) {
      this.mDataSetObservable.unregisterObserver(var1);
   }

   public boolean update(int var1, Object var2) {
      if (!this.supportsUpdates()) {
         return false;
      } else {
         Long var3 = this.getLong(this.mRowIdColumnIndex);
         if (var3 == null) {
            StringBuilder var28 = new StringBuilder();
            var28.append("null rowid. mRowIdColumnIndex = ");
            var28.append(this.mRowIdColumnIndex);
            throw new IllegalStateException(var28.toString());
         } else {
            HashMap var4 = this.mUpdatedRows;
            synchronized(var4){}

            Throwable var10000;
            boolean var10001;
            label216: {
               Map var5;
               try {
                  var5 = (Map)this.mUpdatedRows.get(var3);
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label216;
               }

               Object var6 = var5;
               if (var5 == null) {
                  try {
                     var6 = new HashMap();
                     this.mUpdatedRows.put(var3, var6);
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label216;
                  }
               }

               label202:
               try {
                  ((Map)var6).put(this.getColumnNames()[var1], var2);
                  return true;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label202;
               }
            }

            while(true) {
               Throwable var27 = var10000;

               try {
                  throw var27;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public boolean updateBlob(int var1, byte[] var2) {
      return this.update(var1, var2);
   }

   public boolean updateDouble(int var1, double var2) {
      return this.update(var1, var2);
   }

   public boolean updateFloat(int var1, float var2) {
      return this.update(var1, var2);
   }

   public boolean updateInt(int var1, int var2) {
      return this.update(var1, var2);
   }

   public boolean updateLong(int var1, long var2) {
      return this.update(var1, var2);
   }

   public boolean updateShort(int var1, short var2) {
      return this.update(var1, var2);
   }

   public boolean updateString(int var1, String var2) {
      return this.update(var1, var2);
   }

   public boolean updateToNull(int var1) {
      return this.update(var1, (Object)null);
   }

   protected static class SelfContentObserver extends ContentObserver {
      WeakReference mCursor;

      public SelfContentObserver(AbstractCursor var1) {
         super((Handler)null);
         this.mCursor = new WeakReference(var1);
      }

      public boolean deliverSelfNotifications() {
         return false;
      }

      public void onChange(boolean var1) {
         AbstractCursor var2 = (AbstractCursor)this.mCursor.get();
         if (var2 != null) {
            var2.onChange(false);
         }

      }
   }
}
