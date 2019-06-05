package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;

public class SqlUtils {
   private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

   public static StringBuilder appendColumn(StringBuilder var0, String var1) {
      var0.append('"');
      var0.append(var1);
      var0.append('"');
      return var0;
   }

   public static StringBuilder appendColumn(StringBuilder var0, String var1, String var2) {
      var0.append(var1);
      var0.append(".\"");
      var0.append(var2);
      var0.append('"');
      return var0;
   }

   public static StringBuilder appendColumns(StringBuilder var0, String var1, String[] var2) {
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         appendColumn(var0, var1, var2[var3]);
         if (var3 < var4 - 1) {
            var0.append(',');
         }
      }

      return var0;
   }

   public static StringBuilder appendColumns(StringBuilder var0, String[] var1) {
      int var2 = 0;

      for(int var3 = var1.length; var2 < var3; ++var2) {
         var0.append('"');
         var0.append(var1[var2]);
         var0.append('"');
         if (var2 < var3 - 1) {
            var0.append(',');
         }
      }

      return var0;
   }

   public static StringBuilder appendColumnsEqValue(StringBuilder var0, String var1, String[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         appendColumn(var0, var1, var2[var3]).append("=?");
         if (var3 < var2.length - 1) {
            var0.append(',');
         }
      }

      return var0;
   }

   public static StringBuilder appendColumnsEqualPlaceholders(StringBuilder var0, String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         appendColumn(var0, var1[var2]).append("=?");
         if (var2 < var1.length - 1) {
            var0.append(',');
         }
      }

      return var0;
   }

   public static StringBuilder appendPlaceholders(StringBuilder var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         if (var2 < var1 - 1) {
            var0.append("?,");
         } else {
            var0.append('?');
         }
      }

      return var0;
   }

   public static StringBuilder appendProperty(StringBuilder var0, String var1, Property var2) {
      if (var1 != null) {
         var0.append(var1);
         var0.append('.');
      }

      var0.append('"');
      var0.append(var2.columnName);
      var0.append('"');
      return var0;
   }

   public static String createSqlCount(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("SELECT COUNT(*) FROM \"");
      var1.append(var0);
      var1.append('"');
      return var1.toString();
   }

   public static String createSqlDelete(String var0, String[] var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append('"');
      var2.append(var0);
      var2.append('"');
      String var4 = var2.toString();
      StringBuilder var3 = new StringBuilder("DELETE FROM ");
      var3.append(var4);
      if (var1 != null && var1.length > 0) {
         var3.append(" WHERE ");
         appendColumnsEqValue(var3, var4, var1);
      }

      return var3.toString();
   }

   public static String createSqlInsert(String var0, String var1, String[] var2) {
      StringBuilder var3 = new StringBuilder(var0);
      var3.append('"');
      var3.append(var1);
      var3.append('"');
      var3.append(" (");
      appendColumns(var3, var2);
      var3.append(") VALUES (");
      appendPlaceholders(var3, var2.length);
      var3.append(')');
      return var3.toString();
   }

   public static String createSqlSelect(String var0, String var1, String[] var2, boolean var3) {
      if (var1 != null && var1.length() >= 0) {
         String var4;
         if (var3) {
            var4 = "SELECT DISTINCT ";
         } else {
            var4 = "SELECT ";
         }

         StringBuilder var5 = new StringBuilder(var4);
         appendColumns(var5, var1, var2).append(" FROM ");
         var5.append('"');
         var5.append(var0);
         var5.append('"');
         var5.append(' ');
         var5.append(var1);
         var5.append(' ');
         return var5.toString();
      } else {
         throw new DaoException("Table alias required");
      }
   }

   public static String createSqlSelectCountStar(String var0, String var1) {
      StringBuilder var2 = new StringBuilder("SELECT COUNT(*) FROM ");
      var2.append('"');
      var2.append(var0);
      var2.append('"');
      var2.append(' ');
      if (var1 != null) {
         var2.append(var1);
         var2.append(' ');
      }

      return var2.toString();
   }

   public static String createSqlUpdate(String var0, String[] var1, String[] var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append('"');
      var3.append(var0);
      var3.append('"');
      var0 = var3.toString();
      var3 = new StringBuilder("UPDATE ");
      var3.append(var0);
      var3.append(" SET ");
      appendColumnsEqualPlaceholders(var3, var1);
      var3.append(" WHERE ");
      appendColumnsEqValue(var3, var0, var2);
      return var3.toString();
   }

   public static String escapeBlobArgument(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("X'");
      var1.append(toHex(var0));
      var1.append('\'');
      return var1.toString();
   }

   public static String toHex(byte[] var0) {
      int var1 = 0;

      char[] var2;
      for(var2 = new char[var0.length * 2]; var1 < var0.length; ++var1) {
         int var3 = var0[var1] & 255;
         int var4 = var1 * 2;
         var2[var4] = (char)HEX_ARRAY[var3 >>> 4];
         var2[var4 + 1] = (char)HEX_ARRAY[var3 & 15];
      }

      return new String(var2);
   }
}
