package android.arch.persistence.db;

import java.util.regex.Pattern;

public final class SupportSQLiteQueryBuilder {
   private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
   private Object[] mBindArgs;
   private String[] mColumns = null;
   private boolean mDistinct = false;
   private String mGroupBy = null;
   private String mHaving = null;
   private String mLimit = null;
   private String mOrderBy = null;
   private String mSelection;
   private final String mTable;

   private SupportSQLiteQueryBuilder(String var1) {
      this.mTable = var1;
   }

   private static void appendClause(StringBuilder var0, String var1, String var2) {
      if (!isEmpty(var2)) {
         var0.append(var1);
         var0.append(var2);
      }

   }

   private static void appendColumns(StringBuilder var0, String[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (var3 > 0) {
            var0.append(", ");
         }

         var0.append(var4);
      }

      var0.append(' ');
   }

   public static SupportSQLiteQueryBuilder builder(String var0) {
      return new SupportSQLiteQueryBuilder(var0);
   }

   private static boolean isEmpty(String var0) {
      boolean var1;
      if (var0 != null && var0.length() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public SupportSQLiteQueryBuilder columns(String[] var1) {
      this.mColumns = var1;
      return this;
   }

   public SupportSQLiteQuery create() {
      if (isEmpty(this.mGroupBy) && !isEmpty(this.mHaving)) {
         throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
      } else {
         StringBuilder var1 = new StringBuilder(120);
         var1.append("SELECT ");
         if (this.mDistinct) {
            var1.append("DISTINCT ");
         }

         if (this.mColumns != null && this.mColumns.length != 0) {
            appendColumns(var1, this.mColumns);
         } else {
            var1.append(" * ");
         }

         var1.append(" FROM ");
         var1.append(this.mTable);
         appendClause(var1, " WHERE ", this.mSelection);
         appendClause(var1, " GROUP BY ", this.mGroupBy);
         appendClause(var1, " HAVING ", this.mHaving);
         appendClause(var1, " ORDER BY ", this.mOrderBy);
         appendClause(var1, " LIMIT ", this.mLimit);
         return new SimpleSQLiteQuery(var1.toString(), this.mBindArgs);
      }
   }

   public SupportSQLiteQueryBuilder groupBy(String var1) {
      this.mGroupBy = var1;
      return this;
   }

   public SupportSQLiteQueryBuilder having(String var1) {
      this.mHaving = var1;
      return this;
   }

   public SupportSQLiteQueryBuilder limit(String var1) {
      if (!isEmpty(var1) && !sLimitPattern.matcher(var1).matches()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("invalid LIMIT clauses:");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      } else {
         this.mLimit = var1;
         return this;
      }
   }

   public SupportSQLiteQueryBuilder orderBy(String var1) {
      this.mOrderBy = var1;
      return this;
   }

   public SupportSQLiteQueryBuilder selection(String var1, Object[] var2) {
      this.mSelection = var1;
      this.mBindArgs = var2;
      return this;
   }
}
