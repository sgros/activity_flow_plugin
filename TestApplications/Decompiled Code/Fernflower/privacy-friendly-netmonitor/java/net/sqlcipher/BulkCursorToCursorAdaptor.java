package net.sqlcipher;

import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.util.Map;

public final class BulkCursorToCursorAdaptor extends AbstractWindowedCursor {
   private static final String TAG = "BulkCursor";
   private IBulkCursor mBulkCursor;
   private String[] mColumns;
   private int mCount;
   private AbstractCursor.SelfContentObserver mObserverBridge;
   private boolean mWantsAllOnMoveCalls;

   public static int findRowIdColumnIndex(String[] var0) {
      int var1 = 0;

      for(int var2 = var0.length; var1 < var2; ++var1) {
         if (var0[var1].equals("_id")) {
            return var1;
         }
      }

      return -1;
   }

   public void close() {
      super.close();

      try {
         this.mBulkCursor.close();
      } catch (RemoteException var2) {
         Log.w("BulkCursor", "Remote process exception when closing");
      }

      this.mWindow = null;
   }

   public boolean commitUpdates(Map param1) {
      // $FF: Couldn't be decompiled
   }

   public void copyStringToBuffer(int var1, CharArrayBuffer var2) {
   }

   public void deactivate() {
      super.deactivate();

      try {
         this.mBulkCursor.deactivate();
      } catch (RemoteException var2) {
         Log.w("BulkCursor", "Remote process exception when deactivating");
      }

      this.mWindow = null;
   }

   public boolean deleteRow() {
      label44: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.mBulkCursor.deleteRow(this.mPos);
         } catch (RemoteException var7) {
            var10001 = false;
            break label44;
         }

         if (!var1) {
            return var1;
         }

         label34: {
            try {
               this.mWindow = null;
               this.mCount = this.mBulkCursor.count();
               if (this.mPos < this.mCount) {
                  int var2 = this.mPos;
                  this.mPos = -1;
                  this.moveToPosition(var2);
                  break label34;
               }
            } catch (RemoteException var6) {
               var10001 = false;
               break label44;
            }

            try {
               this.mPos = this.mCount;
            } catch (RemoteException var5) {
               var10001 = false;
               break label44;
            }
         }

         try {
            this.onChange(true);
            return var1;
         } catch (RemoteException var4) {
            var10001 = false;
         }
      }

      Log.e("BulkCursor", "Unable to delete row because the remote process is dead");
      return false;
   }

   public String[] getColumnNames() {
      if (this.mColumns == null) {
         try {
            this.mColumns = this.mBulkCursor.getColumnNames();
         } catch (RemoteException var2) {
            Log.e("BulkCursor", "Unable to fetch column names because the remote process is dead");
            return null;
         }
      }

      return this.mColumns;
   }

   public int getCount() {
      return this.mCount;
   }

   public Bundle getExtras() {
      try {
         Bundle var1 = this.mBulkCursor.getExtras();
         return var1;
      } catch (RemoteException var2) {
         throw new RuntimeException(var2);
      }
   }

   public IContentObserver getObserver() {
      synchronized(this){}

      try {
         if (this.mObserverBridge == null) {
            AbstractCursor.SelfContentObserver var1 = new AbstractCursor.SelfContentObserver(this);
            this.mObserverBridge = var1;
         }
      } finally {
         ;
      }

      return null;
   }

   public boolean onMove(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void registerContentObserver(ContentObserver var1) {
   }

   public void registerDataSetObserver(DataSetObserver var1) {
   }

   public boolean requery() {
      try {
         int var1 = this.mCount;
         IBulkCursor var2 = this.mBulkCursor;
         IContentObserver var3 = this.getObserver();
         CursorWindow var6 = new CursorWindow(false);
         this.mCount = var2.requery(var3, var6);
         if (this.mCount != -1) {
            this.mPos = -1;
            this.mWindow = null;
            super.requery();
            return true;
         } else {
            this.deactivate();
            return false;
         }
      } catch (Exception var5) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unable to requery because the remote process exception ");
         var4.append(var5.getMessage());
         Log.e("BulkCursor", var4.toString());
         this.deactivate();
         return false;
      }
   }

   public Bundle respond(Bundle var1) {
      try {
         var1 = this.mBulkCursor.respond(var1);
         return var1;
      } catch (RemoteException var2) {
         Log.w("BulkCursor", "respond() threw RemoteException, returning an empty bundle.", var2);
         return Bundle.EMPTY;
      }
   }

   public void set(IBulkCursor var1) {
      this.mBulkCursor = var1;

      try {
         this.mCount = this.mBulkCursor.count();
         this.mWantsAllOnMoveCalls = this.mBulkCursor.getWantsAllOnMoveCalls();
         this.mColumns = this.mBulkCursor.getColumnNames();
         this.mRowIdColumnIndex = findRowIdColumnIndex(this.mColumns);
      } catch (RemoteException var2) {
         Log.e("BulkCursor", "Setup failed because the remote process is dead");
      }

   }

   public void set(IBulkCursor var1, int var2, int var3) {
      this.mBulkCursor = var1;
      this.mColumns = null;
      this.mCount = var2;
      this.mRowIdColumnIndex = var3;
   }

   public void unregisterContentObserver(ContentObserver var1) {
   }

   public void unregisterDataSetObserver(DataSetObserver var1) {
   }
}
