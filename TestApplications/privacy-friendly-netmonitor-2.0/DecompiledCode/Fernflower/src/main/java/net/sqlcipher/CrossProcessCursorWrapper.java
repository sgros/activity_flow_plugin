package net.sqlcipher;

import android.database.CrossProcessCursor;

public class CrossProcessCursorWrapper extends CursorWrapper implements CrossProcessCursor {
   public CrossProcessCursorWrapper(Cursor var1) {
      super(var1);
   }

   public void fillWindow(int var1, android.database.CursorWindow var2) {
      DatabaseUtils.cursorFillWindow(this, var1, var2);
   }

   public android.database.CursorWindow getWindow() {
      return null;
   }

   public boolean onMove(int var1, int var2) {
      return true;
   }
}
