package net.sqlcipher;

public class CursorWrapper extends android.database.CursorWrapper implements Cursor {
   private final Cursor mCursor;

   public CursorWrapper(Cursor var1) {
      super(var1);
      this.mCursor = var1;
   }

   public int getType(int var1) {
      return this.mCursor.getType(var1);
   }

   public Cursor getWrappedCursor() {
      return this.mCursor;
   }
}
