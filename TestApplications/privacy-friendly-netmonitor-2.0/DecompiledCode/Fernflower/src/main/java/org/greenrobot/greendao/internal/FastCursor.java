package org.greenrobot.greendao.internal;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public final class FastCursor implements Cursor {
   private final int count;
   private int position;
   private final CursorWindow window;

   public FastCursor(CursorWindow var1) {
      this.window = var1;
      this.count = var1.getNumRows();
   }

   public void close() {
      throw new UnsupportedOperationException();
   }

   public void copyStringToBuffer(int var1, CharArrayBuffer var2) {
      throw new UnsupportedOperationException();
   }

   public void deactivate() {
      throw new UnsupportedOperationException();
   }

   public byte[] getBlob(int var1) {
      return this.window.getBlob(this.position, var1);
   }

   public int getColumnCount() {
      throw new UnsupportedOperationException();
   }

   public int getColumnIndex(String var1) {
      throw new UnsupportedOperationException();
   }

   public int getColumnIndexOrThrow(String var1) throws IllegalArgumentException {
      throw new UnsupportedOperationException();
   }

   public String getColumnName(int var1) {
      throw new UnsupportedOperationException();
   }

   public String[] getColumnNames() {
      throw new UnsupportedOperationException();
   }

   public int getCount() {
      return this.window.getNumRows();
   }

   public double getDouble(int var1) {
      return this.window.getDouble(this.position, var1);
   }

   public Bundle getExtras() {
      throw new UnsupportedOperationException();
   }

   public float getFloat(int var1) {
      return this.window.getFloat(this.position, var1);
   }

   public int getInt(int var1) {
      return this.window.getInt(this.position, var1);
   }

   public long getLong(int var1) {
      return this.window.getLong(this.position, var1);
   }

   public Uri getNotificationUri() {
      return null;
   }

   public int getPosition() {
      return this.position;
   }

   public short getShort(int var1) {
      return this.window.getShort(this.position, var1);
   }

   public String getString(int var1) {
      return this.window.getString(this.position, var1);
   }

   public int getType(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean getWantsAllOnMoveCalls() {
      throw new UnsupportedOperationException();
   }

   public boolean isAfterLast() {
      throw new UnsupportedOperationException();
   }

   public boolean isBeforeFirst() {
      throw new UnsupportedOperationException();
   }

   public boolean isClosed() {
      throw new UnsupportedOperationException();
   }

   public boolean isFirst() {
      boolean var1;
      if (this.position == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isLast() {
      int var1 = this.position;
      int var2 = this.count;
      boolean var3 = true;
      if (var1 != var2 - 1) {
         var3 = false;
      }

      return var3;
   }

   public boolean isNull(int var1) {
      return this.window.isNull(this.position, var1);
   }

   public boolean move(int var1) {
      return this.moveToPosition(this.position + var1);
   }

   public boolean moveToFirst() {
      boolean var1 = false;
      this.position = 0;
      if (this.count > 0) {
         var1 = true;
      }

      return var1;
   }

   public boolean moveToLast() {
      if (this.count > 0) {
         this.position = this.count - 1;
         return true;
      } else {
         return false;
      }
   }

   public boolean moveToNext() {
      if (this.position < this.count - 1) {
         ++this.position;
         return true;
      } else {
         return false;
      }
   }

   public boolean moveToPosition(int var1) {
      if (var1 >= 0 && var1 < this.count) {
         this.position = var1;
         return true;
      } else {
         return false;
      }
   }

   public boolean moveToPrevious() {
      if (this.position > 0) {
         --this.position;
         return true;
      } else {
         return false;
      }
   }

   public void registerContentObserver(ContentObserver var1) {
      throw new UnsupportedOperationException();
   }

   public void registerDataSetObserver(DataSetObserver var1) {
      throw new UnsupportedOperationException();
   }

   public boolean requery() {
      throw new UnsupportedOperationException();
   }

   public Bundle respond(Bundle var1) {
      throw new UnsupportedOperationException();
   }

   public void setNotificationUri(ContentResolver var1, Uri var2) {
      throw new UnsupportedOperationException();
   }

   public void unregisterContentObserver(ContentObserver var1) {
      throw new UnsupportedOperationException();
   }

   public void unregisterDataSetObserver(DataSetObserver var1) {
      throw new UnsupportedOperationException();
   }
}
