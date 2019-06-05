package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;

public class Join {
   final AbstractDao daoDestination;
   final Property joinPropertyDestination;
   final Property joinPropertySource;
   final String sourceTablePrefix;
   final String tablePrefix;
   final WhereCollector whereCollector;

   public Join(String var1, Property var2, AbstractDao var3, Property var4, String var5) {
      this.sourceTablePrefix = var1;
      this.joinPropertySource = var2;
      this.daoDestination = var3;
      this.joinPropertyDestination = var4;
      this.tablePrefix = var5;
      this.whereCollector = new WhereCollector(var3, var5);
   }

   public WhereCondition and(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      return this.whereCollector.combineWhereConditions(" AND ", var1, var2, var3);
   }

   public String getTablePrefix() {
      return this.tablePrefix;
   }

   public WhereCondition or(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      return this.whereCollector.combineWhereConditions(" OR ", var1, var2, var3);
   }

   public Join where(WhereCondition var1, WhereCondition... var2) {
      this.whereCollector.add(var1, var2);
      return this;
   }

   public Join whereOr(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      this.whereCollector.add(this.or(var1, var2, var3));
      return this;
   }
}
