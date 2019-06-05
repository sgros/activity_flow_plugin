package net.sqlcipher;

import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.Collator;
import java.util.HashMap;
import net.sqlcipher.database.SQLiteAbortException;
import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseCorruptException;
import net.sqlcipher.database.SQLiteDiskIOException;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteFullException;
import net.sqlcipher.database.SQLiteProgram;
import net.sqlcipher.database.SQLiteStatement;

public class DatabaseUtils {
   private static final boolean DEBUG = false;
   private static final char[] HEX_DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final boolean LOCAL_LOGV = false;
   private static final String TAG = "DatabaseUtils";
   private static final String[] countProjection = new String[]{"count(*)"};
   private static Collator mColl;

   public static void appendEscapedSQLString(StringBuilder var0, String var1) {
      var0.append('\'');
      if (var1.indexOf(39) != -1) {
         int var2 = var1.length();

         for(int var3 = 0; var3 < var2; ++var3) {
            char var4 = var1.charAt(var3);
            if (var4 == '\'') {
               var0.append('\'');
            }

            var0.append(var4);
         }
      } else {
         var0.append(var1);
      }

      var0.append('\'');
   }

   public static final void appendValueToSql(StringBuilder var0, Object var1) {
      if (var1 == null) {
         var0.append("NULL");
      } else if (var1 instanceof Boolean) {
         if ((Boolean)var1) {
            var0.append('1');
         } else {
            var0.append('0');
         }
      } else {
         appendEscapedSQLString(var0, var1.toString());
      }

   }

   public static void bindObjectToProgram(SQLiteProgram var0, int var1, Object var2) {
      if (var2 == null) {
         var0.bindNull(var1);
      } else if (!(var2 instanceof Double) && !(var2 instanceof Float)) {
         if (var2 instanceof Number) {
            var0.bindLong(var1, ((Number)var2).longValue());
         } else if (var2 instanceof Boolean) {
            if ((Boolean)var2) {
               var0.bindLong(var1, 1L);
            } else {
               var0.bindLong(var1, 0L);
            }
         } else if (var2 instanceof byte[]) {
            var0.bindBlob(var1, (byte[])var2);
         } else {
            var0.bindString(var1, var2.toString());
         }
      } else {
         var0.bindDouble(var1, ((Number)var2).doubleValue());
      }

   }

   public static String concatenateWhere(String var0, String var1) {
      if (TextUtils.isEmpty(var0)) {
         return var1;
      } else if (TextUtils.isEmpty(var1)) {
         return var0;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("(");
         var2.append(var0);
         var2.append(") AND (");
         var2.append(var1);
         var2.append(")");
         return var2.toString();
      }
   }

   public static void cursorDoubleToContentValues(Cursor var0, String var1, ContentValues var2, String var3) {
      int var4 = var0.getColumnIndex(var1);
      if (!var0.isNull(var4)) {
         var2.put(var3, var0.getDouble(var4));
      } else {
         var2.put(var3, (Double)null);
      }

   }

   public static void cursorDoubleToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getDouble(var3));
      }

   }

   public static void cursorDoubleToCursorValues(Cursor var0, String var1, ContentValues var2) {
      cursorDoubleToContentValues(var0, var1, var2, var1);
   }

   public static void cursorFillWindow(Cursor var0, int var1, android.database.CursorWindow var2) {
      if (var1 >= 0 && var1 < var0.getCount()) {
         int var3 = var0.getPosition();
         int var4 = var0.getColumnCount();
         var2.clear();
         var2.setStartPosition(var1);
         var2.setNumColumns(var4);
         if (var0.moveToPosition(var1)) {
            while(var2.allocRow()) {
               for(int var5 = 0; var5 < var4; ++var5) {
                  int var6 = var0.getType(var5);
                  boolean var8;
                  if (var6 != 4) {
                     switch(var6) {
                     case 0:
                        var8 = var2.putNull(var1, var5);
                        break;
                     case 1:
                        var8 = var2.putLong(var0.getLong(var5), var1, var5);
                        break;
                     case 2:
                        var8 = var2.putDouble(var0.getDouble(var5), var1, var5);
                        break;
                     default:
                        String var7 = var0.getString(var5);
                        if (var7 != null) {
                           var8 = var2.putString(var7, var1, var5);
                        } else {
                           var8 = var2.putNull(var1, var5);
                        }
                     }
                  } else {
                     byte[] var9 = var0.getBlob(var5);
                     if (var9 != null) {
                        var8 = var2.putBlob(var9, var1, var5);
                     } else {
                        var8 = var2.putNull(var1, var5);
                     }
                  }

                  if (!var8) {
                     var2.freeLastRow();
                     break;
                  }
               }

               ++var1;
               if (!var0.moveToNext()) {
                  break;
               }
            }
         }

         var0.moveToPosition(var3);
      }
   }

   public static void cursorFloatToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getFloat(var3));
      }

   }

   public static void cursorIntToContentValues(Cursor var0, String var1, ContentValues var2) {
      cursorIntToContentValues(var0, var1, var2, var1);
   }

   public static void cursorIntToContentValues(Cursor var0, String var1, ContentValues var2, String var3) {
      int var4 = var0.getColumnIndex(var1);
      if (!var0.isNull(var4)) {
         var2.put(var3, var0.getInt(var4));
      } else {
         var2.put(var3, (Integer)null);
      }

   }

   public static void cursorIntToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getInt(var3));
      }

   }

   public static void cursorLongToContentValues(Cursor var0, String var1, ContentValues var2) {
      cursorLongToContentValues(var0, var1, var2, var1);
   }

   public static void cursorLongToContentValues(Cursor var0, String var1, ContentValues var2, String var3) {
      int var4 = var0.getColumnIndex(var1);
      if (!var0.isNull(var4)) {
         var2.put(var3, var0.getLong(var4));
      } else {
         var2.put(var3, (Long)null);
      }

   }

   public static void cursorLongToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getLong(var3));
      }

   }

   public static void cursorRowToContentValues(Cursor var0, ContentValues var1) {
      AbstractWindowedCursor var2;
      if (var0 instanceof AbstractWindowedCursor) {
         var2 = (AbstractWindowedCursor)var0;
      } else {
         var2 = null;
      }

      String[] var3 = var0.getColumnNames();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var2 != null && var2.isBlob(var5)) {
            var1.put(var3[var5], var0.getBlob(var5));
         } else {
            var1.put(var3[var5], var0.getString(var5));
         }
      }

   }

   public static void cursorShortToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getShort(var3));
      }

   }

   public static void cursorStringToContentValues(Cursor var0, String var1, ContentValues var2) {
      cursorStringToContentValues(var0, var1, var2, var1);
   }

   public static void cursorStringToContentValues(Cursor var0, String var1, ContentValues var2, String var3) {
      var2.put(var3, var0.getString(var0.getColumnIndexOrThrow(var1)));
   }

   public static void cursorStringToContentValuesIfPresent(Cursor var0, ContentValues var1, String var2) {
      int var3 = var0.getColumnIndexOrThrow(var2);
      if (!var0.isNull(var3)) {
         var1.put(var2, var0.getString(var3));
      }

   }

   public static void cursorStringToInsertHelper(Cursor var0, String var1, DatabaseUtils.InsertHelper var2, int var3) {
      var2.bind(var3, var0.getString(var0.getColumnIndexOrThrow(var1)));
   }

   public static void dumpCurrentRow(Cursor var0) {
      dumpCurrentRow(var0, System.out);
   }

   public static void dumpCurrentRow(Cursor var0, PrintStream var1) {
      String[] var2 = var0.getColumnNames();
      StringBuilder var3 = new StringBuilder();
      var3.append("");
      var3.append(var0.getPosition());
      var3.append(" {");
      var1.println(var3.toString());
      int var4 = 0;

      for(int var5 = var2.length; var4 < var5; ++var4) {
         String var8;
         try {
            var8 = var0.getString(var4);
         } catch (SQLiteException var7) {
            var8 = "<unprintable>";
         }

         StringBuilder var6 = new StringBuilder();
         var6.append("   ");
         var6.append(var2[var4]);
         var6.append('=');
         var6.append(var8);
         var1.println(var6.toString());
      }

      var1.println("}");
   }

   public static void dumpCurrentRow(Cursor var0, StringBuilder var1) {
      String[] var2 = var0.getColumnNames();
      StringBuilder var3 = new StringBuilder();
      var3.append("");
      var3.append(var0.getPosition());
      var3.append(" {\n");
      var1.append(var3.toString());
      int var4 = 0;

      for(int var5 = var2.length; var4 < var5; ++var4) {
         String var8;
         try {
            var8 = var0.getString(var4);
         } catch (SQLiteException var7) {
            var8 = "<unprintable>";
         }

         StringBuilder var6 = new StringBuilder();
         var6.append("   ");
         var6.append(var2[var4]);
         var6.append('=');
         var6.append(var8);
         var6.append("\n");
         var1.append(var6.toString());
      }

      var1.append("}\n");
   }

   public static String dumpCurrentRowToString(Cursor var0) {
      StringBuilder var1 = new StringBuilder();
      dumpCurrentRow(var0, var1);
      return var1.toString();
   }

   public static void dumpCursor(Cursor var0) {
      dumpCursor(var0, System.out);
   }

   public static void dumpCursor(Cursor var0, PrintStream var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(">>>>> Dumping cursor ");
      var2.append(var0);
      var1.println(var2.toString());
      if (var0 != null) {
         int var3 = var0.getPosition();
         var0.moveToPosition(-1);

         while(var0.moveToNext()) {
            dumpCurrentRow(var0, var1);
         }

         var0.moveToPosition(var3);
      }

      var1.println("<<<<<");
   }

   public static void dumpCursor(Cursor var0, StringBuilder var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(">>>>> Dumping cursor ");
      var2.append(var0);
      var2.append("\n");
      var1.append(var2.toString());
      if (var0 != null) {
         int var3 = var0.getPosition();
         var0.moveToPosition(-1);

         while(var0.moveToNext()) {
            dumpCurrentRow(var0, var1);
         }

         var0.moveToPosition(var3);
      }

      var1.append("<<<<<\n");
   }

   public static String dumpCursorToString(Cursor var0) {
      StringBuilder var1 = new StringBuilder();
      dumpCursor(var0, var1);
      return var1.toString();
   }

   private static char[] encodeHex(byte[] var0, char[] var1) {
      int var2 = 0;
      int var3 = var0.length;
      char[] var4 = new char[var3 << 1];

      for(int var5 = 0; var2 < var3; ++var2) {
         int var6 = var5 + 1;
         var4[var5] = (char)var1[(240 & var0[var2]) >>> 4];
         var5 = var6 + 1;
         var4[var6] = (char)var1[15 & var0[var2]];
      }

      return var4;
   }

   public static String getCollationKey(String var0) {
      byte[] var2 = getCollationKeyInBytes(var0);

      try {
         var0 = new String(var2, 0, getKeyLen(var2), "ISO8859_1");
         return var0;
      } catch (Exception var1) {
         return "";
      }
   }

   private static byte[] getCollationKeyInBytes(String var0) {
      if (mColl == null) {
         mColl = Collator.getInstance();
         mColl.setStrength(0);
      }

      return mColl.getCollationKey(var0).toByteArray();
   }

   public static String getHexCollationKey(String var0) {
      byte[] var1 = getCollationKeyInBytes(var0);
      return new String(encodeHex(var1, HEX_DIGITS_LOWER), 0, getKeyLen(var1) * 2);
   }

   private static int getKeyLen(byte[] var0) {
      return var0[var0.length - 1] != 0 ? var0.length : var0.length - 1;
   }

   public static int getTypeOfObject(Object var0) {
      if (var0 == null) {
         return 0;
      } else if (var0 instanceof byte[]) {
         return 4;
      } else if (!(var0 instanceof Float) && !(var0 instanceof Double)) {
         return !(var0 instanceof Long) && !(var0 instanceof Integer) ? 3 : 1;
      } else {
         return 2;
      }
   }

   public static long longForQuery(SQLiteDatabase var0, String var1, String[] var2) {
      SQLiteStatement var7 = var0.compileStatement(var1);

      long var3;
      try {
         var3 = longForQuery(var7, var2);
      } finally {
         var7.close();
      }

      return var3;
   }

   public static long longForQuery(SQLiteStatement var0, String[] var1) {
      if (var1 != null) {
         int var2 = var1.length;

         int var4;
         for(int var3 = 0; var3 < var2; var3 = var4) {
            var4 = var3 + 1;
            bindObjectToProgram(var0, var4, var1[var3]);
         }
      }

      return var0.simpleQueryForLong();
   }

   public static long queryNumEntries(SQLiteDatabase var0, String var1) {
      Cursor var6 = var0.query(var1, countProjection, (String)null, (String[])null, (String)null, (String)null, (String)null);

      long var2;
      try {
         var6.moveToFirst();
         var2 = var6.getLong(0);
      } finally {
         var6.close();
      }

      return var2;
   }

   public static final void readExceptionFromParcel(Parcel var0) {
      int var1 = var0.readInt();
      if (var1 != 0) {
         readExceptionFromParcel(var0, var0.readString(), var1);
      }
   }

   private static final void readExceptionFromParcel(Parcel var0, String var1, int var2) {
      switch(var2) {
      case 2:
         throw new IllegalArgumentException(var1);
      case 3:
         throw new UnsupportedOperationException(var1);
      case 4:
         throw new SQLiteAbortException(var1);
      case 5:
         throw new SQLiteConstraintException(var1);
      case 6:
         throw new SQLiteDatabaseCorruptException(var1);
      case 7:
         throw new SQLiteFullException(var1);
      case 8:
         throw new SQLiteDiskIOException(var1);
      case 9:
         throw new SQLiteException(var1);
      default:
         var0.readException(var2, var1);
      }
   }

   public static void readExceptionWithFileNotFoundExceptionFromParcel(Parcel var0) throws FileNotFoundException {
      int var1 = var0.readInt();
      if (var1 != 0) {
         String var2 = var0.readString();
         if (var1 == 1) {
            throw new FileNotFoundException(var2);
         } else {
            readExceptionFromParcel(var0, var2, var1);
         }
      }
   }

   public static void readExceptionWithOperationApplicationExceptionFromParcel(Parcel var0) throws OperationApplicationException {
      int var1 = var0.readInt();
      if (var1 != 0) {
         String var2 = var0.readString();
         if (var1 == 10) {
            throw new OperationApplicationException(var2);
         } else {
            readExceptionFromParcel(var0, var2, var1);
         }
      }
   }

   public static String sqlEscapeString(String var0) {
      StringBuilder var1 = new StringBuilder();
      appendEscapedSQLString(var1, var0);
      return var1.toString();
   }

   public static String stringForQuery(SQLiteDatabase var0, String var1, String[] var2) {
      SQLiteStatement var5 = var0.compileStatement(var1);

      try {
         var1 = stringForQuery(var5, var2);
      } finally {
         var5.close();
      }

      return var1;
   }

   public static String stringForQuery(SQLiteStatement var0, String[] var1) {
      if (var1 != null) {
         int var2 = var1.length;

         int var4;
         for(int var3 = 0; var3 < var2; var3 = var4) {
            var4 = var3 + 1;
            bindObjectToProgram(var0, var4, var1[var3]);
         }
      }

      return var0.simpleQueryForString();
   }

   public static final void writeExceptionToParcel(Parcel var0, Exception var1) {
      boolean var2 = var1 instanceof FileNotFoundException;
      boolean var3 = true;
      byte var4;
      if (var2) {
         var3 = false;
         var4 = 1;
      } else if (var1 instanceof IllegalArgumentException) {
         var4 = 2;
      } else if (var1 instanceof UnsupportedOperationException) {
         var4 = 3;
      } else if (var1 instanceof SQLiteAbortException) {
         var4 = 4;
      } else if (var1 instanceof SQLiteConstraintException) {
         var4 = 5;
      } else if (var1 instanceof SQLiteDatabaseCorruptException) {
         var4 = 6;
      } else if (var1 instanceof SQLiteFullException) {
         var4 = 7;
      } else if (var1 instanceof SQLiteDiskIOException) {
         var4 = 8;
      } else if (var1 instanceof SQLiteException) {
         var4 = 9;
      } else {
         if (!(var1 instanceof OperationApplicationException)) {
            var0.writeException(var1);
            Log.e("DatabaseUtils", "Writing exception to parcel", var1);
            return;
         }

         var4 = 10;
      }

      var0.writeInt(var4);
      var0.writeString(var1.getMessage());
      if (var3) {
         Log.e("DatabaseUtils", "Writing exception to parcel", var1);
      }

   }

   public static class InsertHelper {
      public static final int TABLE_INFO_PRAGMA_COLUMNNAME_INDEX = 1;
      public static final int TABLE_INFO_PRAGMA_DEFAULT_INDEX = 4;
      private HashMap mColumns;
      private final SQLiteDatabase mDb;
      private String mInsertSQL = null;
      private SQLiteStatement mInsertStatement = null;
      private SQLiteStatement mPreparedStatement = null;
      private SQLiteStatement mReplaceStatement = null;
      private final String mTableName;

      public InsertHelper(SQLiteDatabase var1, String var2) {
         this.mDb = var1;
         this.mTableName = var2;
      }

      private void buildSQL() throws SQLException {
         StringBuilder var1 = new StringBuilder(128);
         var1.append("INSERT INTO ");
         var1.append(this.mTableName);
         var1.append(" (");
         StringBuilder var2 = new StringBuilder(128);
         var2.append("VALUES (");

         Cursor var79;
         try {
            SQLiteDatabase var3 = this.mDb;
            StringBuilder var4 = new StringBuilder();
            var4.append("PRAGMA table_info(");
            var4.append(this.mTableName);
            var4.append(")");
            var79 = var3.rawQuery(var4.toString(), (String[])null);
         } finally {
            ;
         }

         label761: {
            Throwable var10000;
            label762: {
               boolean var10001;
               try {
                  HashMap var80 = new HashMap(var79.getCount());
                  this.mColumns = var80;
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break label762;
               }

               int var5 = 1;

               while(true) {
                  String var81;
                  try {
                     if (!var79.moveToNext()) {
                        break label761;
                     }

                     String var6 = var79.getString(1);
                     var81 = var79.getString(4);
                     this.mColumns.put(var6, var5);
                     var1.append("'");
                     var1.append(var6);
                     var1.append("'");
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break;
                  }

                  if (var81 == null) {
                     try {
                        var2.append("?");
                     } catch (Throwable var74) {
                        var10000 = var74;
                        var10001 = false;
                        break;
                     }
                  } else {
                     try {
                        var2.append("COALESCE(?, ");
                        var2.append(var81);
                        var2.append(")");
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break;
                     }
                  }

                  label749: {
                     label748: {
                        try {
                           if (var5 == var79.getCount()) {
                              break label748;
                           }
                        } catch (Throwable var77) {
                           var10000 = var77;
                           var10001 = false;
                           break;
                        }

                        var81 = ", ";
                        break label749;
                     }

                     var81 = ") ";
                  }

                  label742: {
                     label741: {
                        try {
                           var1.append(var81);
                           if (var5 != var79.getCount()) {
                              break label741;
                           }
                        } catch (Throwable var76) {
                           var10000 = var76;
                           var10001 = false;
                           break;
                        }

                        var81 = ");";
                        break label742;
                     }

                     var81 = ", ";
                  }

                  try {
                     var2.append(var81);
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break;
                  }

                  ++var5;
               }
            }

            Throwable var82 = var10000;
            if (var79 != null) {
               var79.close();
            }

            throw var82;
         }

         if (var79 != null) {
            var79.close();
         }

         var1.append(var2);
         this.mInsertSQL = var1.toString();
      }

      private SQLiteStatement getStatement(boolean var1) throws SQLException {
         if (var1) {
            if (this.mReplaceStatement == null) {
               if (this.mInsertSQL == null) {
                  this.buildSQL();
               }

               StringBuilder var2 = new StringBuilder();
               var2.append("INSERT OR REPLACE");
               var2.append(this.mInsertSQL.substring(6));
               String var3 = var2.toString();
               this.mReplaceStatement = this.mDb.compileStatement(var3);
            }

            return this.mReplaceStatement;
         } else {
            if (this.mInsertStatement == null) {
               if (this.mInsertSQL == null) {
                  this.buildSQL();
               }

               this.mInsertStatement = this.mDb.compileStatement(this.mInsertSQL);
            }

            return this.mInsertStatement;
         }
      }

      private long insertInternal(ContentValues param1, boolean param2) {
         // $FF: Couldn't be decompiled
      }

      public void bind(int var1, double var2) {
         this.mPreparedStatement.bindDouble(var1, var2);
      }

      public void bind(int var1, float var2) {
         this.mPreparedStatement.bindDouble(var1, (double)var2);
      }

      public void bind(int var1, int var2) {
         this.mPreparedStatement.bindLong(var1, (long)var2);
      }

      public void bind(int var1, long var2) {
         this.mPreparedStatement.bindLong(var1, var2);
      }

      public void bind(int var1, String var2) {
         if (var2 == null) {
            this.mPreparedStatement.bindNull(var1);
         } else {
            this.mPreparedStatement.bindString(var1, var2);
         }

      }

      public void bind(int var1, boolean var2) {
         SQLiteStatement var3 = this.mPreparedStatement;
         long var4;
         if (var2) {
            var4 = 1L;
         } else {
            var4 = 0L;
         }

         var3.bindLong(var1, var4);
      }

      public void bind(int var1, byte[] var2) {
         if (var2 == null) {
            this.mPreparedStatement.bindNull(var1);
         } else {
            this.mPreparedStatement.bindBlob(var1, var2);
         }

      }

      public void bindNull(int var1) {
         this.mPreparedStatement.bindNull(var1);
      }

      public void close() {
         if (this.mInsertStatement != null) {
            this.mInsertStatement.close();
            this.mInsertStatement = null;
         }

         if (this.mReplaceStatement != null) {
            this.mReplaceStatement.close();
            this.mReplaceStatement = null;
         }

         this.mInsertSQL = null;
         this.mColumns = null;
      }

      public long execute() {
         if (this.mPreparedStatement == null) {
            throw new IllegalStateException("you must prepare this inserter before calling execute");
         } else {
            try {
               long var1 = this.mPreparedStatement.executeInsert();
               return var1;
            } catch (SQLException var7) {
               StringBuilder var4 = new StringBuilder();
               var4.append("Error executing InsertHelper with table ");
               var4.append(this.mTableName);
               Log.e("DatabaseUtils", var4.toString(), var7);
            } finally {
               this.mPreparedStatement = null;
            }

            return -1L;
         }
      }

      public int getColumnIndex(String var1) {
         this.getStatement(false);
         Integer var2 = (Integer)this.mColumns.get(var1);
         if (var2 == null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("column '");
            var3.append(var1);
            var3.append("' is invalid");
            throw new IllegalArgumentException(var3.toString());
         } else {
            return var2;
         }
      }

      public long insert(ContentValues var1) {
         return this.insertInternal(var1, false);
      }

      public void prepareForInsert() {
         this.mPreparedStatement = this.getStatement(false);
         this.mPreparedStatement.clearBindings();
      }

      public void prepareForReplace() {
         this.mPreparedStatement = this.getStatement(true);
         this.mPreparedStatement.clearBindings();
      }

      public long replace(ContentValues var1) {
         return this.insertInternal(var1, true);
      }
   }
}
