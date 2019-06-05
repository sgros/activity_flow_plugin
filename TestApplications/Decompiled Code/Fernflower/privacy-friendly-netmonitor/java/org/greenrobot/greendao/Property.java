package org.greenrobot.greendao;

import java.util.Collection;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.query.WhereCondition;

public class Property {
   public final String columnName;
   public final String name;
   public final int ordinal;
   public final boolean primaryKey;
   public final Class type;

   public Property(int var1, Class var2, String var3, boolean var4, String var5) {
      this.ordinal = var1;
      this.type = var2;
      this.name = var3;
      this.primaryKey = var4;
      this.columnName = var5;
   }

   public WhereCondition between(Object var1, Object var2) {
      return new WhereCondition.PropertyCondition(this, " BETWEEN ? AND ?", new Object[]{var1, var2});
   }

   public WhereCondition eq(Object var1) {
      return new WhereCondition.PropertyCondition(this, "=?", var1);
   }

   public WhereCondition ge(Object var1) {
      return new WhereCondition.PropertyCondition(this, ">=?", var1);
   }

   public WhereCondition gt(Object var1) {
      return new WhereCondition.PropertyCondition(this, ">?", var1);
   }

   public WhereCondition in(Collection var1) {
      return this.in(var1.toArray());
   }

   public WhereCondition in(Object... var1) {
      StringBuilder var2 = new StringBuilder(" IN (");
      SqlUtils.appendPlaceholders(var2, var1.length).append(')');
      return new WhereCondition.PropertyCondition(this, var2.toString(), var1);
   }

   public WhereCondition isNotNull() {
      return new WhereCondition.PropertyCondition(this, " IS NOT NULL");
   }

   public WhereCondition isNull() {
      return new WhereCondition.PropertyCondition(this, " IS NULL");
   }

   public WhereCondition le(Object var1) {
      return new WhereCondition.PropertyCondition(this, "<=?", var1);
   }

   public WhereCondition like(String var1) {
      return new WhereCondition.PropertyCondition(this, " LIKE ?", var1);
   }

   public WhereCondition lt(Object var1) {
      return new WhereCondition.PropertyCondition(this, "<?", var1);
   }

   public WhereCondition notEq(Object var1) {
      return new WhereCondition.PropertyCondition(this, "<>?", var1);
   }

   public WhereCondition notIn(Collection var1) {
      return this.notIn(var1.toArray());
   }

   public WhereCondition notIn(Object... var1) {
      StringBuilder var2 = new StringBuilder(" NOT IN (");
      SqlUtils.appendPlaceholders(var2, var1.length).append(')');
      return new WhereCondition.PropertyCondition(this, var2.toString(), var1);
   }
}
