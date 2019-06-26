package net.sqlcipher;

import android.util.Log;
import java.io.File;
import net.sqlcipher.database.SQLiteDatabase;

public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
   private final String TAG = this.getClass().getSimpleName();

   private void deleteDatabaseFile(String var1) {
      if (!var1.equalsIgnoreCase(":memory:") && var1.trim().length() != 0) {
         String var2 = this.TAG;
         StringBuilder var3 = new StringBuilder();
         var3.append("deleting the database file: ");
         var3.append(var1);
         Log.e(var2, var3.toString());

         try {
            File var5 = new File(var1);
            var5.delete();
         } catch (Exception var4) {
            var1 = this.TAG;
            var3 = new StringBuilder();
            var3.append("delete failed: ");
            var3.append(var4.getMessage());
            Log.w(var1, var3.toString());
         }

      }
   }

   public void onCorruption(SQLiteDatabase var1) {
      String var2 = this.TAG;
      StringBuilder var3 = new StringBuilder();
      var3.append("Corruption reported by sqlite on database, deleting: ");
      var3.append(var1.getPath());
      Log.e(var2, var3.toString());
      if (var1.isOpen()) {
         Log.e(this.TAG, "Database object for corrupted database is already open, closing");

         try {
            var1.close();
         } catch (Exception var4) {
            Log.e(this.TAG, "Exception closing Database object for corrupted database, ignored", var4);
         }
      }

      this.deleteDatabaseFile(var1.getPath());
   }
}
