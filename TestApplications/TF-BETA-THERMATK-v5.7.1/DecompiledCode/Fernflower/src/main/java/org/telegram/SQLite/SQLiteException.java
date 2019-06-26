package org.telegram.SQLite;

public class SQLiteException extends Exception {
   private static final long serialVersionUID = -2398298479089615621L;
   public final int errorCode;

   public SQLiteException() {
      this.errorCode = 0;
   }

   public SQLiteException(int var1, String var2) {
      super(var2);
      this.errorCode = var1;
   }

   public SQLiteException(String var1) {
      this(0, var1);
   }
}
