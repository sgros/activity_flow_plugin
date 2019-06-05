package net.sqlcipher;

import java.util.ArrayList;
import java.util.Iterator;

public class MatrixCursor extends AbstractCursor {
   private final int columnCount;
   private final String[] columnNames;
   private Object[] data;
   private int rowCount;

   public MatrixCursor(String[] var1) {
      this(var1, 16);
   }

   public MatrixCursor(String[] var1, int var2) {
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
         StringBuilder var6 = new StringBuilder();
         var6.append("columnNames.length = ");
         var6.append(this.columnCount);
         var6.append(", columnValues.size() = ");
         var6.append(var3);
         throw new IllegalArgumentException(var6.toString());
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
         if (var3 >= var1) {
            var1 = var3;
         }

         this.data = new Object[var1];
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
         StringBuilder var2 = new StringBuilder();
         var2.append("Requested column: ");
         var2.append(var1);
         var2.append(", # of columns: ");
         var2.append(this.columnCount);
         throw new CursorIndexOutOfBoundsException(var2.toString());
      }
   }

   public void addRow(Iterable var1) {
      int var2 = this.rowCount * this.columnCount;
      int var3 = this.columnCount + var2;
      this.ensureCapacity(var3);
      if (var1 instanceof ArrayList) {
         this.addRow((ArrayList)var1, var2);
      } else {
         Object[] var4 = this.data;

         for(Iterator var5 = var1.iterator(); var5.hasNext(); ++var2) {
            Object var6 = var5.next();
            if (var2 == var3) {
               throw new IllegalArgumentException("columnValues.size() > columnNames.length");
            }

            var4[var2] = var6;
         }

         if (var2 != var3) {
            throw new IllegalArgumentException("columnValues.size() < columnNames.length");
         } else {
            ++this.rowCount;
         }
      }
   }

   public void addRow(Object[] var1) {
      if (var1.length != this.columnCount) {
         StringBuilder var2 = new StringBuilder();
         var2.append("columnNames.length = ");
         var2.append(this.columnCount);
         var2.append(", columnValues.length = ");
         var2.append(var1.length);
         throw new IllegalArgumentException(var2.toString());
      } else {
         int var3 = this.rowCount++;
         var3 *= this.columnCount;
         this.ensureCapacity(this.columnCount + var3);
         System.arraycopy(var1, 0, this.data, var3, this.columnCount);
      }
   }

   public String[] getColumnNames() {
      return this.columnNames;
   }

   public int getCount() {
      return this.rowCount;
   }

   public double getDouble(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         return 0.0D;
      } else {
         return var2 instanceof Number ? ((Number)var2).doubleValue() : Double.parseDouble(var2.toString());
      }
   }

   public float getFloat(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         return 0.0F;
      } else {
         return var2 instanceof Number ? ((Number)var2).floatValue() : Float.parseFloat(var2.toString());
      }
   }

   public int getInt(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         return 0;
      } else {
         return var2 instanceof Number ? ((Number)var2).intValue() : Integer.parseInt(var2.toString());
      }
   }

   public long getLong(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         return 0L;
      } else {
         return var2 instanceof Number ? ((Number)var2).longValue() : Long.parseLong(var2.toString());
      }
   }

   public short getShort(int var1) {
      Object var2 = this.get(var1);
      if (var2 == null) {
         return 0;
      } else {
         return var2 instanceof Number ? ((Number)var2).shortValue() : Short.parseShort(var2.toString());
      }
   }

   public String getString(int var1) {
      Object var2 = this.get(var1);
      return var2 == null ? null : var2.toString();
   }

   public int getType(int var1) {
      return DatabaseUtils.getTypeOfObject(this.get(var1));
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

   public MatrixCursor.RowBuilder newRow() {
      ++this.rowCount;
      int var1 = this.rowCount * this.columnCount;
      this.ensureCapacity(var1);
      return new MatrixCursor.RowBuilder(var1 - this.columnCount, var1);
   }

   public class RowBuilder {
      private final int endIndex;
      private int index;

      RowBuilder(int var2, int var3) {
         this.index = var2;
         this.endIndex = var3;
      }

      public MatrixCursor.RowBuilder add(Object var1) {
         if (this.index == this.endIndex) {
            throw new CursorIndexOutOfBoundsException("No more columns left.");
         } else {
            Object[] var2 = MatrixCursor.this.data;
            int var3 = this.index++;
            var2[var3] = var1;
            return this;
         }
      }
   }
}
