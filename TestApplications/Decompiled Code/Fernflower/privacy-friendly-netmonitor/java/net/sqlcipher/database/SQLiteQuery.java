package net.sqlcipher.database;

import android.os.SystemClock;
import android.util.Log;
import net.sqlcipher.CursorWindow;

public class SQLiteQuery extends SQLiteProgram {
   private static final String TAG = "Cursor";
   private String[] mBindArgs;
   private Object[] mObjectBindArgs;
   private int mOffsetIndex;

   SQLiteQuery(SQLiteDatabase var1, String var2, int var3, Object[] var4) {
      super(var1, var2);
      this.mOffsetIndex = var3;
      this.mObjectBindArgs = var4;
      if (this.mObjectBindArgs != null) {
         var3 = this.mObjectBindArgs.length;
      } else {
         var3 = 0;
      }

      this.mBindArgs = new String[var3];
   }

   SQLiteQuery(SQLiteDatabase var1, String var2, int var3, String[] var4) {
      super(var1, var2);
      this.mOffsetIndex = var3;
      this.mBindArgs = var4;
   }

   private final native int native_column_count();

   private final native String native_column_name(int var1);

   private final native int native_fill_window(CursorWindow var1, int var2, int var3, int var4, int var5);

   public void bindArguments(Object[] var1) {
      if (var1 != null && var1.length > 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            Object var3 = var1[var2];
            if (var3 == null) {
               this.bindNull(var2 + 1);
            } else if (var3 instanceof Double) {
               this.bindDouble(var2 + 1, (Double)var3);
            } else if (var3 instanceof Float) {
               this.bindDouble(var2 + 1, Double.valueOf((double)((Number)var3).floatValue()));
            } else if (var3 instanceof Long) {
               this.bindLong(var2 + 1, (Long)var3);
            } else if (var3 instanceof Integer) {
               this.bindLong(var2 + 1, Long.valueOf((long)((Number)var3).intValue()));
            } else if (var3 instanceof Boolean) {
               long var4;
               if ((Boolean)var3) {
                  var4 = 1L;
               } else {
                  var4 = 0L;
               }

               this.bindLong(var2 + 1, var4);
            } else if (var3 instanceof byte[]) {
               this.bindBlob(var2 + 1, (byte[])var3);
            } else {
               this.bindString(var2 + 1, var3.toString());
            }
         }
      }

   }

   public void bindDouble(int var1, double var2) {
      this.mBindArgs[var1 - 1] = Double.toString(var2);
      if (!this.mClosed) {
         super.bindDouble(var1, var2);
      }

   }

   public void bindLong(int var1, long var2) {
      this.mBindArgs[var1 - 1] = Long.toString(var2);
      if (!this.mClosed) {
         super.bindLong(var1, var2);
      }

   }

   public void bindNull(int var1) {
      this.mBindArgs[var1 - 1] = null;
      if (!this.mClosed) {
         super.bindNull(var1);
      }

   }

   public void bindString(int var1, String var2) {
      this.mBindArgs[var1 - 1] = var2;
      if (!this.mClosed) {
         super.bindString(var1, var2);
      }

   }

   int columnCountLocked() {
      this.acquireReference();

      int var1;
      try {
         var1 = this.native_column_count();
      } finally {
         this.releaseReference();
      }

      return var1;
   }

   String columnNameLocked(int var1) {
      this.acquireReference();

      String var2;
      try {
         var2 = this.native_column_name(var1);
      } finally {
         this.releaseReference();
      }

      return var2;
   }

   int fillWindow(CursorWindow var1, int var2, int var3) {
      SystemClock.uptimeMillis();
      this.mDatabase.lock();

      label410: {
         Throwable var10000;
         label414: {
            boolean var10001;
            try {
               this.acquireReference();
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label414;
            }

            label415: {
               label416: {
                  label403: {
                     SQLiteDatabaseCorruptException var4;
                     try {
                        try {
                           var1.acquireReference();
                           var2 = this.native_fill_window(var1, var1.getStartPosition(), this.mOffsetIndex, var2, var3);
                           if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
                              StringBuilder var63 = new StringBuilder();
                              var63.append("fillWindow(): ");
                              var63.append(this.mSql);
                              Log.d("Cursor", var63.toString());
                           }
                           break label415;
                        } catch (IllegalStateException var57) {
                           break label416;
                        } catch (SQLiteDatabaseCorruptException var58) {
                           var4 = var58;
                        }
                     } catch (Throwable var59) {
                        var10000 = var59;
                        var10001 = false;
                        break label403;
                     }

                     label393:
                     try {
                        this.mDatabase.onCorruption();
                        throw var4;
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label393;
                     }
                  }

                  Throwable var62 = var10000;

                  try {
                     var1.releaseReference();
                     throw var62;
                  } catch (Throwable var54) {
                     var10000 = var54;
                     var10001 = false;
                     break label414;
                  }
               }

               var2 = 0;

               try {
                  var1.releaseReference();
                  break label410;
               } catch (Throwable var53) {
                  var10000 = var53;
                  var10001 = false;
                  break label414;
               }
            }

            label395:
            try {
               var1.releaseReference();
               break label410;
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label395;
            }
         }

         Throwable var61 = var10000;
         this.releaseReference();
         this.mDatabase.unlock();
         throw var61;
      }

      this.releaseReference();
      this.mDatabase.unlock();
      return var2;
   }

   void requery() {
      if (this.mBindArgs != null) {
         int var1 = this.mBindArgs.length;
         byte var2 = 0;

         SQLiteMisuseException var10000;
         int var3;
         label52: {
            boolean var10001;
            try {
               if (this.mObjectBindArgs != null) {
                  this.bindArguments(this.mObjectBindArgs);
                  return;
               }
            } catch (SQLiteMisuseException var8) {
               var10000 = var8;
               var10001 = false;
               break label52;
            }

            var3 = 0;

            while(true) {
               if (var3 >= var1) {
                  return;
               }

               int var4 = var3 + 1;

               try {
                  super.bindString(var4, this.mBindArgs[var3]);
               } catch (SQLiteMisuseException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }

               var3 = var4;
            }
         }

         SQLiteMisuseException var5 = var10000;
         StringBuilder var6 = new StringBuilder();
         var6.append("mSql ");
         var6.append(this.mSql);
         var6 = new StringBuilder(var6.toString());

         for(var3 = var2; var3 < var1; ++var3) {
            var6.append(" ");
            var6.append(this.mBindArgs[var3]);
         }

         var6.append(" ");
         throw new IllegalStateException(var6.toString(), var5);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SQLiteQuery: ");
      var1.append(this.mSql);
      return var1.toString();
   }
}
