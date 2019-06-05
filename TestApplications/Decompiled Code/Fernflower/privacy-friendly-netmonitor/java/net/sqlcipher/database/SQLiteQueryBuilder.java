package net.sqlcipher.database;

import android.text.TextUtils;
import android.util.Log;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.sqlcipher.Cursor;
import net.sqlcipher.DatabaseUtils;

public class SQLiteQueryBuilder {
   private static final String TAG = "SQLiteQueryBuilder";
   private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
   private boolean mDistinct = false;
   private SQLiteDatabase.CursorFactory mFactory = null;
   private Map mProjectionMap = null;
   private boolean mStrictProjectionMap;
   private String mTables = "";
   private StringBuilder mWhereClause = null;

   private static void appendClause(StringBuilder var0, String var1, String var2) {
      if (!TextUtils.isEmpty(var2)) {
         var0.append(var1);
         var0.append(var2);
      }

   }

   private static void appendClauseEscapeClause(StringBuilder var0, String var1, String var2) {
      if (!TextUtils.isEmpty(var2)) {
         var0.append(var1);
         DatabaseUtils.appendEscapedSQLString(var0, var2);
      }

   }

   public static void appendColumns(StringBuilder var0, String[] var1) {
      int var2 = 0;

      for(int var3 = var1.length; var2 < var3; ++var2) {
         String var4 = var1[var2];
         if (var4 != null) {
            if (var2 > 0) {
               var0.append(", ");
            }

            var0.append(var4);
         }
      }

      var0.append(' ');
   }

   public static String buildQueryString(boolean var0, String var1, String[] var2, String var3, String var4, String var5, String var6, String var7) {
      if (TextUtils.isEmpty(var4) && !TextUtils.isEmpty(var5)) {
         throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
      } else if (!TextUtils.isEmpty(var7) && !sLimitPattern.matcher(var7).matches()) {
         StringBuilder var9 = new StringBuilder();
         var9.append("invalid LIMIT clauses:");
         var9.append(var7);
         throw new IllegalArgumentException(var9.toString());
      } else {
         StringBuilder var8 = new StringBuilder(120);
         var8.append("SELECT ");
         if (var0) {
            var8.append("DISTINCT ");
         }

         if (var2 != null && var2.length != 0) {
            appendColumns(var8, var2);
         } else {
            var8.append("* ");
         }

         var8.append("FROM ");
         var8.append(var1);
         appendClause(var8, " WHERE ", var3);
         appendClause(var8, " GROUP BY ", var4);
         appendClause(var8, " HAVING ", var5);
         appendClause(var8, " ORDER BY ", var6);
         appendClause(var8, " LIMIT ", var7);
         return var8.toString();
      }
   }

   private String[] computeProjection(String[] var1) {
      byte var2 = 0;
      int var3 = 0;
      if (var1 != null && var1.length > 0) {
         if (this.mProjectionMap == null) {
            return var1;
         } else {
            String[] var4 = new String[var1.length];

            for(int var7 = var1.length; var3 < var7; ++var3) {
               String var8 = var1[var3];
               String var10 = (String)this.mProjectionMap.get(var8);
               if (var10 != null) {
                  var4[var3] = var10;
               } else {
                  if (this.mStrictProjectionMap || !var8.contains(" AS ") && !var8.contains(" as ")) {
                     StringBuilder var11 = new StringBuilder();
                     var11.append("Invalid column ");
                     var11.append(var1[var3]);
                     throw new IllegalArgumentException(var11.toString());
                  }

                  var4[var3] = var8;
               }
            }

            return var4;
         }
      } else if (this.mProjectionMap != null) {
         Set var6 = this.mProjectionMap.entrySet();
         var1 = new String[var6.size()];
         Iterator var5 = var6.iterator();
         var3 = var2;

         while(var5.hasNext()) {
            Entry var9 = (Entry)var5.next();
            if (!((String)var9.getKey()).equals("_count")) {
               var1[var3] = (String)var9.getValue();
               ++var3;
            }
         }

         return var1;
      } else {
         return null;
      }
   }

   public void appendWhere(CharSequence var1) {
      if (this.mWhereClause == null) {
         this.mWhereClause = new StringBuilder(var1.length() + 16);
      }

      if (this.mWhereClause.length() == 0) {
         this.mWhereClause.append('(');
      }

      this.mWhereClause.append(var1);
   }

   public void appendWhereEscapeString(String var1) {
      if (this.mWhereClause == null) {
         this.mWhereClause = new StringBuilder(var1.length() + 16);
      }

      if (this.mWhereClause.length() == 0) {
         this.mWhereClause.append('(');
      }

      DatabaseUtils.appendEscapedSQLString(this.mWhereClause, var1);
   }

   public String buildQuery(String[] var1, String var2, String[] var3, String var4, String var5, String var6, String var7) {
      var3 = this.computeProjection(var1);
      StringBuilder var9 = new StringBuilder();
      boolean var8;
      if (this.mWhereClause != null && this.mWhereClause.length() > 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      if (var8) {
         var9.append(this.mWhereClause.toString());
         var9.append(')');
      }

      if (var2 != null && var2.length() > 0) {
         if (var8) {
            var9.append(" AND ");
         }

         var9.append('(');
         var9.append(var2);
         var9.append(')');
      }

      return buildQueryString(this.mDistinct, this.mTables, var3, var9.toString(), var4, var5, var6, var7);
   }

   public String buildUnionQuery(String[] var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder(128);
      int var5 = var1.length;
      String var6;
      if (this.mDistinct) {
         var6 = " UNION ";
      } else {
         var6 = " UNION ALL ";
      }

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var7 > 0) {
            var4.append(var6);
         }

         var4.append(var1[var7]);
      }

      appendClause(var4, " ORDER BY ", var2);
      appendClause(var4, " LIMIT ", var3);
      return var4.toString();
   }

   public String buildUnionSubQuery(String var1, String[] var2, Set var3, int var4, String var5, String var6, String[] var7, String var8, String var9) {
      int var10 = 0;
      int var11 = var2.length;

      String[] var12;
      for(var12 = new String[var11]; var10 < var11; ++var10) {
         String var13 = var2[var10];
         if (var13.equals(var1)) {
            StringBuilder var15 = new StringBuilder();
            var15.append("'");
            var15.append(var5);
            var15.append("' AS ");
            var15.append(var1);
            var12[var10] = var15.toString();
         } else if (var10 > var4 && !var3.contains(var13)) {
            StringBuilder var14 = new StringBuilder();
            var14.append("NULL AS ");
            var14.append(var13);
            var12[var10] = var14.toString();
         } else {
            var12[var10] = var13;
         }
      }

      return this.buildQuery(var12, var6, var7, var8, var9, (String)null, (String)null);
   }

   public String getTables() {
      return this.mTables;
   }

   public Cursor query(SQLiteDatabase var1, String[] var2, String var3, String[] var4, String var5, String var6, String var7) {
      return this.query(var1, var2, var3, var4, var5, var6, var7, (String)null);
   }

   public Cursor query(SQLiteDatabase var1, String[] var2, String var3, String[] var4, String var5, String var6, String var7, String var8) {
      if (this.mTables == null) {
         return null;
      } else {
         String var9 = this.buildQuery(var2, var3, var4, var5, var6, var7, var8);
         if (Log.isLoggable("SQLiteQueryBuilder", 3)) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Performing query: ");
            var10.append(var9);
            Log.d("SQLiteQueryBuilder", var10.toString());
         }

         return var1.rawQueryWithFactory(this.mFactory, var9, var4, SQLiteDatabase.findEditTable(this.mTables));
      }
   }

   public void setCursorFactory(SQLiteDatabase.CursorFactory var1) {
      this.mFactory = var1;
   }

   public void setDistinct(boolean var1) {
      this.mDistinct = var1;
   }

   public void setProjectionMap(Map var1) {
      this.mProjectionMap = var1;
   }

   public void setStrictProjectionMap(boolean var1) {
      this.mStrictProjectionMap = var1;
   }

   public void setTables(String var1) {
      this.mTables = var1;
   }
}
