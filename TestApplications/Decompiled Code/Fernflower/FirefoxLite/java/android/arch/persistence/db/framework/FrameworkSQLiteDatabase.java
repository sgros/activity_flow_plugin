package android.arch.persistence.db.framework;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class FrameworkSQLiteDatabase implements SupportSQLiteDatabase {
   private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private final SQLiteDatabase mDelegate;

   FrameworkSQLiteDatabase(SQLiteDatabase var1) {
      this.mDelegate = var1;
   }

   public void beginTransaction() {
      this.mDelegate.beginTransaction();
   }

   public void close() throws IOException {
      this.mDelegate.close();
   }

   public SupportSQLiteStatement compileStatement(String var1) {
      return new FrameworkSQLiteStatement(this.mDelegate.compileStatement(var1));
   }

   public int delete(String var1, String var2, Object[] var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append("DELETE FROM ");
      var4.append(var1);
      if (TextUtils.isEmpty(var2)) {
         var1 = "";
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append(" WHERE ");
         var5.append(var2);
         var1 = var5.toString();
      }

      var4.append(var1);
      SupportSQLiteStatement var6 = this.compileStatement(var4.toString());
      SimpleSQLiteQuery.bind(var6, var3);
      return var6.executeUpdateDelete();
   }

   public void endTransaction() {
      this.mDelegate.endTransaction();
   }

   public void execSQL(String var1) throws SQLException {
      this.mDelegate.execSQL(var1);
   }

   public List getAttachedDbs() {
      return this.mDelegate.getAttachedDbs();
   }

   public String getPath() {
      return this.mDelegate.getPath();
   }

   public boolean inTransaction() {
      return this.mDelegate.inTransaction();
   }

   public long insert(String var1, int var2, ContentValues var3) throws SQLException {
      return this.mDelegate.insertWithOnConflict(var1, (String)null, var3, var2);
   }

   public boolean isOpen() {
      return this.mDelegate.isOpen();
   }

   public Cursor query(final SupportSQLiteQuery var1) {
      return this.mDelegate.rawQueryWithFactory(new CursorFactory() {
         public Cursor newCursor(SQLiteDatabase var1x, SQLiteCursorDriver var2, String var3, SQLiteQuery var4) {
            var1.bindTo(new FrameworkSQLiteProgram(var4));
            return new SQLiteCursor(var2, var3, var4);
         }
      }, var1.getSql(), EMPTY_STRING_ARRAY, (String)null);
   }

   public Cursor query(String var1) {
      return this.query((SupportSQLiteQuery)(new SimpleSQLiteQuery(var1)));
   }

   public void setTransactionSuccessful() {
      this.mDelegate.setTransactionSuccessful();
   }

   public int update(String var1, int var2, ContentValues var3, String var4, Object[] var5) {
      if (var3 != null && var3.size() != 0) {
         StringBuilder var6 = new StringBuilder(120);
         var6.append("UPDATE ");
         var6.append(CONFLICT_VALUES[var2]);
         var6.append(var1);
         var6.append(" SET ");
         var2 = var3.size();
         int var7;
         if (var5 == null) {
            var7 = var2;
         } else {
            var7 = var5.length + var2;
         }

         Object[] var8 = new Object[var7];
         int var9 = 0;

         for(Iterator var10 = var3.keySet().iterator(); var10.hasNext(); ++var9) {
            String var11 = (String)var10.next();
            if (var9 > 0) {
               var1 = ",";
            } else {
               var1 = "";
            }

            var6.append(var1);
            var6.append(var11);
            var8[var9] = var3.get(var11);
            var6.append("=?");
         }

         if (var5 != null) {
            for(var9 = var2; var9 < var7; ++var9) {
               var8[var9] = var5[var9 - var2];
            }
         }

         if (!TextUtils.isEmpty(var4)) {
            var6.append(" WHERE ");
            var6.append(var4);
         }

         SupportSQLiteStatement var12 = this.compileStatement(var6.toString());
         SimpleSQLiteQuery.bind(var12, var8);
         return var12.executeUpdateDelete();
      } else {
         throw new IllegalArgumentException("Empty values");
      }
   }
}
