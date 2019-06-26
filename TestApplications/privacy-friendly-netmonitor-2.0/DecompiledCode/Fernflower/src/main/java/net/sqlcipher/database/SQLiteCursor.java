package net.sqlcipher.database;

import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import net.sqlcipher.AbstractWindowedCursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.SQLException;

public class SQLiteCursor extends AbstractWindowedCursor {
   static final int NO_COUNT = -1;
   static final String TAG = "Cursor";
   private Map mColumnNameMap;
   private String[] mColumns;
   private int mCount = -1;
   private int mCursorState;
   private SQLiteDatabase mDatabase;
   private SQLiteCursorDriver mDriver;
   private String mEditTable;
   private int mInitialRead = Integer.MAX_VALUE;
   private ReentrantLock mLock;
   private int mMaxRead = Integer.MAX_VALUE;
   protected SQLiteCursor.MainThreadNotificationHandler mNotificationHandler;
   private boolean mPendingData;
   private SQLiteQuery mQuery;
   private Throwable mStackTrace;

   public SQLiteCursor(SQLiteDatabase var1, SQLiteCursorDriver var2, String var3, SQLiteQuery var4) {
      int var5 = 0;
      this.mCursorState = 0;
      this.mLock = null;
      this.mPendingData = false;
      this.mStackTrace = (new DatabaseObjectNotClosedException()).fillInStackTrace();
      this.mDatabase = var1;
      this.mDriver = var2;
      this.mEditTable = var3;
      this.mColumnNameMap = null;
      this.mQuery = var4;

      Throwable var10000;
      label89: {
         int var6;
         boolean var10001;
         try {
            var1.lock();
            var6 = this.mQuery.columnCountLocked();
            this.mColumns = new String[var6];
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label89;
         }

         while(true) {
            if (var5 >= var6) {
               var1.unlock();
               return;
            }

            try {
               String var14 = this.mQuery.columnNameLocked(var5);
               this.mColumns[var5] = var14;
               if ("_id".equals(var14)) {
                  this.mRowIdColumnIndex = var5;
               }
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      Throwable var13 = var10000;
      var1.unlock();
      throw var13;
   }

   // $FF: synthetic method
   static CursorWindow access$100(SQLiteCursor var0) {
      return var0.mWindow;
   }

   // $FF: synthetic method
   static ReentrantLock access$200(SQLiteCursor var0) {
      return var0.mLock;
   }

   // $FF: synthetic method
   static int access$300(SQLiteCursor var0) {
      return var0.mCursorState;
   }

   // $FF: synthetic method
   static int access$400(SQLiteCursor var0) {
      return var0.mMaxRead;
   }

   // $FF: synthetic method
   static int access$500(SQLiteCursor var0) {
      return var0.mCount;
   }

   // $FF: synthetic method
   static int access$502(SQLiteCursor var0, int var1) {
      var0.mCount = var1;
      return var1;
   }

   // $FF: synthetic method
   static SQLiteQuery access$600(SQLiteCursor var0) {
      return var0.mQuery;
   }

   private void deactivateCommon() {
      this.mCursorState = 0;
      if (this.mWindow != null) {
         this.mWindow.close();
         this.mWindow = null;
      }

   }

   private void fillWindow(int var1) {
      if (this.mWindow == null) {
         this.mWindow = new CursorWindow(true);
      } else {
         ++this.mCursorState;
         this.queryThreadLock();

         try {
            this.mWindow.clear();
         } finally {
            this.queryThreadUnlock();
         }
      }

      this.mWindow.setStartPosition(var1);
      this.mCount = this.mQuery.fillWindow(this.mWindow, this.mInitialRead, 0);
      if (this.mCount == -1) {
         this.mCount = var1 + this.mInitialRead;
         (new Thread(new SQLiteCursor.QueryThread(this.mCursorState), "query thread")).start();
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

   public void close() {
      super.close();
      this.deactivateCommon();
      this.mQuery.close();
      this.mDriver.cursorClosed();
   }

   public boolean commitUpdates(Map var1) {
      if (!this.supportsUpdates()) {
         Log.e("Cursor", "commitUpdates not supported on this cursor, did you include the _id column?");
         return false;
      } else {
         Throwable var10000;
         boolean var10001;
         Throwable var221;
         label1912: {
            HashMap var2 = this.mUpdatedRows;
            synchronized(var2){}
            if (var1 != null) {
               try {
                  this.mUpdatedRows.putAll(var1);
               } catch (Throwable var218) {
                  var10000 = var218;
                  var10001 = false;
                  break label1912;
               }
            }

            try {
               if (this.mUpdatedRows.size() == 0) {
                  return true;
               }
            } catch (Throwable var219) {
               var10000 = var219;
               var10001 = false;
               break label1912;
            }

            try {
               this.mDatabase.beginTransaction();
            } catch (Throwable var217) {
               var10000 = var217;
               var10001 = false;
               break label1912;
            }

            label1913: {
               Iterator var3;
               StringBuilder var220;
               try {
                  var220 = new StringBuilder(128);
                  var3 = this.mUpdatedRows.entrySet().iterator();
               } catch (Throwable var213) {
                  var10000 = var213;
                  var10001 = false;
                  break label1913;
               }

               while(true) {
                  Map var5;
                  Long var223;
                  try {
                     if (!var3.hasNext()) {
                        break;
                     }

                     Entry var4 = (Entry)var3.next();
                     var5 = (Map)var4.getValue();
                     var223 = (Long)var4.getKey();
                  } catch (Throwable var216) {
                     var10000 = var216;
                     var10001 = false;
                     break label1913;
                  }

                  if (var223 == null || var5 == null) {
                     try {
                        var220 = new StringBuilder();
                        var220.append("null rowId or values found! rowId = ");
                        var220.append(var223);
                        var220.append(", values = ");
                        var220.append(var5);
                        IllegalStateException var222 = new IllegalStateException(var220.toString());
                        throw var222;
                     } catch (Throwable var208) {
                        var10000 = var208;
                        var10001 = false;
                        break label1913;
                     }
                  }

                  try {
                     if (var5.size() == 0) {
                        continue;
                     }
                  } catch (Throwable var215) {
                     var10000 = var215;
                     var10001 = false;
                     break label1913;
                  }

                  long var6;
                  Iterator var224;
                  Object[] var226;
                  try {
                     var6 = var223;
                     var224 = var5.entrySet().iterator();
                     var220.setLength(0);
                     StringBuilder var8 = new StringBuilder();
                     var8.append("UPDATE ");
                     var8.append(this.mEditTable);
                     var8.append(" SET ");
                     var220.append(var8.toString());
                     var226 = new Object[var5.size()];
                  } catch (Throwable var212) {
                     var10000 = var212;
                     var10001 = false;
                     break label1913;
                  }

                  int var9 = 0;

                  while(true) {
                     try {
                        if (!var224.hasNext()) {
                           break;
                        }

                        Entry var227 = (Entry)var224.next();
                        var220.append((String)var227.getKey());
                        var220.append("=?");
                        var226[var9] = var227.getValue();
                        if (var224.hasNext()) {
                           var220.append(", ");
                        }
                     } catch (Throwable var214) {
                        var10000 = var214;
                        var10001 = false;
                        break label1913;
                     }

                     ++var9;
                  }

                  try {
                     StringBuilder var225 = new StringBuilder();
                     var225.append(" WHERE ");
                     var225.append(this.mColumns[this.mRowIdColumnIndex]);
                     var225.append('=');
                     var225.append(var6);
                     var220.append(var225.toString());
                     var220.append(';');
                     this.mDatabase.execSQL(var220.toString(), var226);
                     this.mDatabase.rowUpdated(this.mEditTable, var6);
                  } catch (Throwable var211) {
                     var10000 = var211;
                     var10001 = false;
                     break label1913;
                  }
               }

               try {
                  this.mDatabase.setTransactionSuccessful();
               } catch (Throwable var210) {
                  var10000 = var210;
                  var10001 = false;
                  break label1913;
               }

               try {
                  this.mDatabase.endTransaction();
                  this.mUpdatedRows.clear();
               } catch (Throwable var209) {
                  var10000 = var209;
                  var10001 = false;
                  break label1912;
               }

               this.onChange(true);
               return true;
            }

            var221 = var10000;

            label1851:
            try {
               this.mDatabase.endTransaction();
               throw var221;
            } catch (Throwable var207) {
               var10000 = var207;
               var10001 = false;
               break label1851;
            }
         }

         while(true) {
            var221 = var10000;

            try {
               throw var221;
            } catch (Throwable var206) {
               var10000 = var206;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void deactivate() {
      super.deactivate();
      this.deactivateCommon();
      this.mDriver.cursorDeactivated();
   }

   public boolean deleteRow() {
      this.checkPosition();
      if (this.mRowIdColumnIndex != -1 && this.mCurrentRowID != null) {
         this.mDatabase.lock();

         boolean var4;
         label117: {
            Throwable var10000;
            label116: {
               boolean var10001;
               label115: {
                  label114: {
                     try {
                        try {
                           SQLiteDatabase var1 = this.mDatabase;
                           String var2 = this.mEditTable;
                           StringBuilder var3 = new StringBuilder();
                           var3.append(this.mColumns[this.mRowIdColumnIndex]);
                           var3.append("=?");
                           var1.delete(var2, var3.toString(), new String[]{this.mCurrentRowID.toString()});
                           break label114;
                        } catch (SQLException var13) {
                        }
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label116;
                     }

                     var4 = false;
                     break label115;
                  }

                  var4 = true;
               }

               label107:
               try {
                  int var5 = this.mPos;
                  this.requery();
                  this.moveToPosition(var5);
                  break label117;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label107;
               }
            }

            Throwable var15 = var10000;
            this.mDatabase.unlock();
            throw var15;
         }

         this.mDatabase.unlock();
         if (var4) {
            this.onChange(true);
            return true;
         } else {
            return false;
         }
      } else {
         Log.e("Cursor", "Could not delete row because either the row ID column is not available or ithas not been read.");
         return false;
      }
   }

   public void fillWindow(int var1, android.database.CursorWindow var2) {
      if (this.mWindow == null) {
         this.mWindow = new CursorWindow(true);
      } else {
         ++this.mCursorState;
         this.queryThreadLock();

         try {
            this.mWindow.clear();
         } finally {
            this.queryThreadUnlock();
         }
      }

      this.mWindow.setStartPosition(var1);
      this.mCount = this.mQuery.fillWindow(this.mWindow, this.mInitialRead, 0);
      if (this.mCount == -1) {
         this.mCount = var1 + this.mInitialRead;
         (new Thread(new SQLiteCursor.QueryThread(this.mCursorState), "query thread")).start();
      }

   }

   protected void finalize() {
      // $FF: Couldn't be decompiled
   }

   public int getColumnIndex(String var1) {
      int var5;
      if (this.mColumnNameMap == null) {
         String[] var2 = this.mColumns;
         int var3 = var2.length;
         HashMap var4 = new HashMap(var3, 1.0F);

         for(var5 = 0; var5 < var3; ++var5) {
            var4.put(var2[var5], var5);
         }

         this.mColumnNameMap = var4;
      }

      var5 = var1.lastIndexOf(46);
      String var7 = var1;
      if (var5 != -1) {
         Exception var9 = new Exception();
         StringBuilder var8 = new StringBuilder();
         var8.append("requesting column name with table name -- ");
         var8.append(var1);
         Log.e("Cursor", var8.toString(), var9);
         var7 = var1.substring(var5 + 1);
      }

      Integer var6 = (Integer)this.mColumnNameMap.get(var7);
      return var6 != null ? var6 : -1;
   }

   public String[] getColumnNames() {
      return this.mColumns;
   }

   public int getCount() {
      if (this.mCount == -1) {
         this.fillWindow(0);
      }

      return this.mCount;
   }

   public SQLiteDatabase getDatabase() {
      return this.mDatabase;
   }

   public boolean onMove(int var1, int var2) {
      if (this.mWindow == null || var2 < this.mWindow.getStartPosition() || var2 >= this.mWindow.getStartPosition() + this.mWindow.getNumRows()) {
         this.fillWindow(var2);
      }

      return true;
   }

   public void registerDataSetObserver(DataSetObserver var1) {
      super.registerDataSetObserver(var1);
      if ((Integer.MAX_VALUE != this.mMaxRead || Integer.MAX_VALUE != this.mInitialRead) && this.mNotificationHandler == null) {
         this.queryThreadLock();

         try {
            SQLiteCursor.MainThreadNotificationHandler var4 = new SQLiteCursor.MainThreadNotificationHandler(this);
            this.mNotificationHandler = var4;
            if (this.mPendingData) {
               this.notifyDataSetChange();
               this.mPendingData = false;
            }
         } finally {
            this.queryThreadUnlock();
         }
      }

   }

   public boolean requery() {
      if (this.isClosed()) {
         return false;
      } else {
         this.mDatabase.lock();

         label249: {
            Throwable var10000;
            label255: {
               boolean var10001;
               try {
                  if (this.mWindow != null) {
                     this.mWindow.clear();
                  }
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label255;
               }

               try {
                  this.mPos = -1;
                  this.mDriver.cursorRequeried(this);
                  this.mCount = -1;
                  ++this.mCursorState;
                  this.queryThreadLock();
               } catch (Throwable var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label255;
               }

               try {
                  this.mQuery.requery();
                  break label249;
               } finally {
                  label239:
                  try {
                     this.queryThreadUnlock();
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label239;
                  }
               }
            }

            Throwable var1 = var10000;
            this.mDatabase.unlock();
            throw var1;
         }

         this.mDatabase.unlock();
         return super.requery();
      }
   }

   public void setLoadStyle(int var1, int var2) {
      this.mMaxRead = var2;
      this.mInitialRead = var1;
      this.mLock = new ReentrantLock(true);
   }

   public void setSelectionArguments(String[] var1) {
      this.mDriver.setBindArguments(var1);
   }

   public void setWindow(CursorWindow var1) {
      if (this.mWindow != null) {
         ++this.mCursorState;
         this.queryThreadLock();

         try {
            this.mWindow.close();
         } finally {
            this.queryThreadUnlock();
         }

         this.mCount = -1;
      }

      this.mWindow = var1;
   }

   public boolean supportsUpdates() {
      return TextUtils.isEmpty(this.mEditTable) ^ true;
   }

   protected static class MainThreadNotificationHandler extends Handler {
      private final WeakReference wrappedCursor;

      MainThreadNotificationHandler(SQLiteCursor var1) {
         this.wrappedCursor = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         SQLiteCursor var2 = (SQLiteCursor)this.wrappedCursor.get();
         if (var2 != null) {
            var2.notifyDataSetChange();
         }

      }
   }

   private final class QueryThread implements Runnable {
      private final int mThreadState;

      QueryThread(int var2) {
         this.mThreadState = var2;
      }

      private void sendMessage() {
         if (SQLiteCursor.this.mNotificationHandler != null) {
            SQLiteCursor.this.mNotificationHandler.sendEmptyMessage(1);
            SQLiteCursor.this.mPendingData = false;
         } else {
            SQLiteCursor.this.mPendingData = true;
         }

      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
