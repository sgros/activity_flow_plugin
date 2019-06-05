package locus.api.android.utils;

import android.database.AbstractCursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import java.util.ArrayList;
import java.util.Iterator;

public class MatrixCursorWithBlob extends AbstractCursor {
   private final int columnCount;
   private final String[] columnNames;
   private Object[] data;
   private int rowCount;

   public MatrixCursorWithBlob(String[] var1) {
      this(var1, 16);
   }

   public MatrixCursorWithBlob(String[] var1, int var2) {
      this.rowCount = 0;
      this.columnNames = var1;
      this.columnCount = var1.length;
      int var3 = var2;
      if (var2 < 1) {
         var3 = 1;
      }

      this.data = new Object[this.columnCount * var3];
   }

   private void addRow(ArrayList var1, int var2) {
      int var3 = var1.size();
      if (var3 != this.columnCount) {
         throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.size() = " + var3);
      } else {
         ++this.rowCount;
         Object[] var4 = this.data;

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var2 + var5] = var1.get(var5);
         }

      }
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.data.length) {
         Object[] var2 = this.data;
         int var3 = this.data.length * 2;
         int var4 = var3;
         if (var3 < var1) {
            var4 = var1;
         }

         this.data = new Object[var4];
         System.arraycopy(var2, 0, this.data, 0, var2.length);
      }

   }

   private Object get(int var1) {
      if (var1 >= 0 && var1 < this.columnCount) {
         if (this.mPos < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
         } else if (this.mPos >= this.rowCount) {
            throw new CursorIndexOutOfBoundsException("After last row.");
         } else {
            return this.data[this.mPos * this.columnCount + var1];
         }
      } else {
         throw new CursorIndexOutOfBoundsException("Requested column: " + var1 + ", # of columns: " + this.columnCount);
      }
   }

   public void addRow(Iterable var1) {
      int var2 = this.rowCount * this.columnCount;
      int var3 = var2 + this.columnCount;
      this.ensureCapacity(var3);
      if (var1 instanceof ArrayList) {
         this.addRow((ArrayList)var1, var2);
      } else {
         Object[] var4 = this.data;

         for(Iterator var6 = var1.iterator(); var6.hasNext(); ++var2) {
            Object var5 = var6.next();
            if (var2 == var3) {
               throw new IllegalArgumentException("columnValues.size() > columnNames.length");
            }

            var4[var2] = var5;
         }

         if (var2 != var3) {
            throw new IllegalArgumentException("columnValues.size() < columnNames.length");
         }

         ++this.rowCount;
      }

   }

   public void addRow(Object[] var1) {
      if (var1.length != this.columnCount) {
         throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.length = " + var1.length);
      } else {
         int var2 = this.rowCount++;
         var2 *= this.columnCount;
         this.ensureCapacity(this.columnCount + var2);
         System.arraycopy(var1, 0, this.data, var2, this.columnCount);
      }
   }

   public void fillWindow(int param1, CursorWindow param2) {
      // $FF: Couldn't be decompiled
   }

   public byte[] getBlob(int var1) {
      Object var2 = this.get(var1);
      byte[] var3;
      if (var2 == null) {
         var3 = new byte[0];
      } else if (var2 instanceof byte[]) {
         var3 = (byte[])var2;
      } else {
         var3 = new byte[0];
      }

      return var3;
   }

   public String[] getColumnNames() {
      return this.columnNames;
   }

   public int getCount() {
      return this.rowCount;
   }

   public double getDouble(int var1) {
      Object var2 = this.get(var1);
      double var3;
      if (var2 == null) {
         var3 = 0.0D;
      } else if (var2 instanceof Number) {
         var3 = ((Number)var2).doubleValue();
      } else {
         var3 = Double.parseDouble(var2.toString());
      }

      return var3;
   }

   public float getFloat(int var1) {
      Object var2 = this.get(var1);
      float var3;
      if (var2 == null) {
         var3 = 0.0F;
      } else if (var2 instanceof Number) {
         var3 = ((Number)var2).floatValue();
      } else {
         var3 = Float.parseFloat(var2.toString());
      }

      return var3;
   }

   public int getInt(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         var1 = 0;
      } else if (var2 instanceof Number) {
         var1 = ((Number)var2).intValue();
      } else {
         var1 = Integer.parseInt(var2.toString());
      }

      return var1;
   }

   public long getLong(int var1) {
      Object var2 = this.get(var1);
      long var3;
      if (var2 == null) {
         var3 = 0L;
      } else if (var2 instanceof Number) {
         var3 = ((Number)var2).longValue();
      } else {
         var3 = Long.parseLong(var2.toString());
      }

      return var3;
   }

   public short getShort(int var1) {
      Object var2 = this.get(var1);
      short var3;
      if (var2 == null) {
         byte var4 = 0;
         var3 = var4;
      } else {
         short var5;
         if (var2 instanceof Number) {
            var5 = ((Number)var2).shortValue();
            var3 = var5;
         } else {
            var5 = Short.parseShort(var2.toString());
            var3 = var5;
         }
      }

      return var3;
   }

   public String getString(int var1) {
      Object var2 = this.get(var1);
      String var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = var2.toString();
      }

      return var3;
   }

   public boolean isNull(int var1) {
      boolean var2;
      if (this.get(var1) == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public MatrixCursorWithBlob.RowBuilder newRow() {
      ++this.rowCount;
      int var1 = this.rowCount * this.columnCount;
      this.ensureCapacity(var1);
      return new MatrixCursorWithBlob.RowBuilder(var1 - this.columnCount, var1);
   }

   public class RowBuilder {
      private final int endIndex;
      private int index;

      RowBuilder(int var2, int var3) {
         this.index = var2;
         this.endIndex = var3;
      }

      public MatrixCursorWithBlob.RowBuilder add(Object var1) {
         if (this.index == this.endIndex) {
            throw new CursorIndexOutOfBoundsException("No more columns left.");
         } else {
            Object[] var2 = MatrixCursorWithBlob.this.data;
            int var3 = this.index++;
            var2[var3] = var1;
            return this;
         }
      }
   }
}
