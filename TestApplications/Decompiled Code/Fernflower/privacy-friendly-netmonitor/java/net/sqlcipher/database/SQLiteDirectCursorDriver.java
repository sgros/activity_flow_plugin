package net.sqlcipher.database;

import net.sqlcipher.Cursor;

public class SQLiteDirectCursorDriver implements SQLiteCursorDriver {
   private Cursor mCursor;
   private SQLiteDatabase mDatabase;
   private String mEditTable;
   private SQLiteQuery mQuery;
   private String mSql;

   public SQLiteDirectCursorDriver(SQLiteDatabase var1, String var2, String var3) {
      this.mDatabase = var1;
      this.mEditTable = var3;
      this.mSql = var2;
   }

   public void cursorClosed() {
      this.mCursor = null;
   }

   public void cursorDeactivated() {
   }

   public void cursorRequeried(android.database.Cursor var1) {
   }

   public Cursor query(SQLiteDatabase.CursorFactory var1, Object[] var2) {
      SQLiteQuery var3 = new SQLiteQuery(this.mDatabase, this.mSql, 0, var2);
      SQLiteQuery var4 = var3;

      Throwable var10000;
      label481: {
         boolean var10001;
         try {
            var3.bindArguments(var2);
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label481;
         }

         if (var1 == null) {
            var4 = var3;

            SQLiteCursor var61;
            try {
               var61 = new SQLiteCursor;
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label481;
            }

            var4 = var3;

            try {
               var61.<init>(this.mDatabase, this, this.mEditTable, var3);
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label481;
            }

            var4 = var3;

            try {
               this.mCursor = var61;
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label481;
            }
         } else {
            var4 = var3;

            try {
               this.mCursor = var1.newCursor(this.mDatabase, this, this.mEditTable, var3);
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label481;
            }
         }

         var4 = var3;

         try {
            this.mQuery = var3;
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label481;
         }

         var4 = null;

         label456:
         try {
            Cursor var63 = this.mCursor;
            return var63;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            break label456;
         }
      }

      Throwable var62 = var10000;
      if (var4 != null) {
         var4.close();
      }

      throw var62;
   }

   public Cursor query(SQLiteDatabase.CursorFactory var1, String[] var2) {
      Throwable var10000;
      SQLiteQuery var84;
      label672: {
         SQLiteDatabase var3 = this.mDatabase;
         String var4 = this.mSql;
         int var5 = 0;
         SQLiteQuery var83 = new SQLiteQuery(var3, var4, 0, var2);
         int var6;
         boolean var10001;
         if (var2 == null) {
            var6 = 0;
         } else {
            var84 = var83;

            try {
               var6 = var2.length;
            } catch (Throwable var79) {
               var10000 = var79;
               var10001 = false;
               break label672;
            }
         }

         while(true) {
            if (var5 >= var6) {
               if (var1 == null) {
                  var84 = var83;

                  SQLiteCursor var80;
                  try {
                     var80 = new SQLiteCursor;
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break;
                  }

                  var84 = var83;

                  try {
                     var80.<init>(this.mDatabase, this, this.mEditTable, var83);
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break;
                  }

                  var84 = var83;

                  try {
                     this.mCursor = var80;
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break;
                  }
               } else {
                  var84 = var83;

                  try {
                     this.mCursor = var1.newCursor(this.mDatabase, this, this.mEditTable, var83);
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     break;
                  }
               }

               var84 = var83;

               try {
                  this.mQuery = var83;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break;
               }

               var84 = null;

               try {
                  Cursor var82 = this.mCursor;
                  return var82;
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break;
               }
            }

            int var7 = var5 + 1;
            var84 = var83;

            try {
               var83.bindString(var7, var2[var5]);
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break;
            }

            var5 = var7;
         }
      }

      Throwable var81 = var10000;
      if (var84 != null) {
         var84.close();
      }

      throw var81;
   }

   public void setBindArguments(String[] var1) {
      int var2 = 0;

      int var5;
      for(int var3 = var1.length; var2 < var3; var2 = var5) {
         SQLiteQuery var4 = this.mQuery;
         var5 = var2 + 1;
         var4.bindString(var5, var1[var2]);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("SQLiteDirectCursorDriver: ");
      var1.append(this.mSql);
      return var1.toString();
   }
}
