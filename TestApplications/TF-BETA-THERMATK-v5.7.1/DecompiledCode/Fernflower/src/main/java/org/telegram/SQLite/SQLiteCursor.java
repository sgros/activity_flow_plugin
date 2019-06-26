package org.telegram.SQLite;

import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.NativeByteBuffer;

public class SQLiteCursor {
   public static final int FIELD_TYPE_BYTEARRAY = 4;
   public static final int FIELD_TYPE_FLOAT = 2;
   public static final int FIELD_TYPE_INT = 1;
   public static final int FIELD_TYPE_NULL = 5;
   public static final int FIELD_TYPE_STRING = 3;
   private boolean inRow = false;
   private SQLitePreparedStatement preparedStatement;

   public SQLiteCursor(SQLitePreparedStatement var1) {
      this.preparedStatement = var1;
   }

   public byte[] byteArrayValue(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnByteArrayValue(this.preparedStatement.getStatementHandle(), var1);
   }

   public NativeByteBuffer byteBufferValue(int var1) throws SQLiteException {
      this.checkRow();
      long var2 = this.columnByteBufferValue(this.preparedStatement.getStatementHandle(), var1);
      return var2 != 0L ? NativeByteBuffer.wrap(var2) : null;
   }

   void checkRow() throws SQLiteException {
      if (!this.inRow) {
         throw new SQLiteException("You must call next before");
      }
   }

   native byte[] columnByteArrayValue(long var1, int var3);

   native long columnByteBufferValue(long var1, int var3);

   native int columnCount(long var1);

   native double columnDoubleValue(long var1, int var3);

   native int columnIntValue(long var1, int var3);

   native int columnIsNull(long var1, int var3);

   native long columnLongValue(long var1, int var3);

   native String columnStringValue(long var1, int var3);

   native int columnType(long var1, int var3);

   public void dispose() {
      this.preparedStatement.dispose();
   }

   public double doubleValue(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnDoubleValue(this.preparedStatement.getStatementHandle(), var1);
   }

   public int getColumnCount() {
      return this.columnCount(this.preparedStatement.getStatementHandle());
   }

   public SQLitePreparedStatement getPreparedStatement() {
      return this.preparedStatement;
   }

   public long getStatementHandle() {
      return this.preparedStatement.getStatementHandle();
   }

   public int getTypeOf(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnType(this.preparedStatement.getStatementHandle(), var1);
   }

   public int intValue(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnIntValue(this.preparedStatement.getStatementHandle(), var1);
   }

   public boolean isNull(int var1) throws SQLiteException {
      this.checkRow();
      var1 = this.columnIsNull(this.preparedStatement.getStatementHandle(), var1);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public long longValue(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnLongValue(this.preparedStatement.getStatementHandle(), var1);
   }

   public boolean next() throws SQLiteException {
      SQLitePreparedStatement var1 = this.preparedStatement;
      int var2 = var1.step(var1.getStatementHandle());
      int var3 = var2;
      if (var2 == -1) {
         int var4 = 6;

         while(true) {
            var3 = var2;
            if (var4 == 0) {
               break;
            }

            label36: {
               try {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("sqlite busy, waiting...");
                  }

                  Thread.sleep(500L);
                  var3 = this.preparedStatement.step();
               } catch (Exception var6) {
                  FileLog.e((Throwable)var6);
                  break label36;
               }

               var2 = var3;
               if (var3 == 0) {
                  break;
               }
            }

            --var4;
         }

         if (var3 == -1) {
            throw new SQLiteException("sqlite busy");
         }
      }

      boolean var5;
      if (var3 == 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.inRow = var5;
      return this.inRow;
   }

   public String stringValue(int var1) throws SQLiteException {
      this.checkRow();
      return this.columnStringValue(this.preparedStatement.getStatementHandle(), var1);
   }
}
