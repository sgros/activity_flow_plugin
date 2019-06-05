package org.greenrobot.greendao.query;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.rx.RxQuery;

public class QueryBuilder {
   public static boolean LOG_SQL;
   public static boolean LOG_VALUES;
   private final AbstractDao dao;
   private boolean distinct;
   private final List joins;
   private Integer limit;
   private Integer offset;
   private StringBuilder orderBuilder;
   private String stringOrderCollation;
   private final String tablePrefix;
   private final List values;
   private final WhereCollector whereCollector;

   protected QueryBuilder(AbstractDao var1) {
      this(var1, "T");
   }

   protected QueryBuilder(AbstractDao var1, String var2) {
      this.dao = var1;
      this.tablePrefix = var2;
      this.values = new ArrayList();
      this.joins = new ArrayList();
      this.whereCollector = new WhereCollector(var1, var2);
      this.stringOrderCollation = " COLLATE NOCASE";
   }

   private Join addJoin(String var1, Property var2, AbstractDao var3, Property var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("J");
      var5.append(this.joins.size() + 1);
      Join var6 = new Join(var1, var2, var3, var4, var5.toString());
      this.joins.add(var6);
      return var6;
   }

   private void appendJoinsAndWheres(StringBuilder var1, String var2) {
      this.values.clear();
      Iterator var3 = this.joins.iterator();

      while(var3.hasNext()) {
         Join var4 = (Join)var3.next();
         var1.append(" JOIN ");
         var1.append(var4.daoDestination.getTablename());
         var1.append(' ');
         var1.append(var4.tablePrefix);
         var1.append(" ON ");
         SqlUtils.appendProperty(var1, var4.sourceTablePrefix, var4.joinPropertySource).append('=');
         SqlUtils.appendProperty(var1, var4.tablePrefix, var4.joinPropertyDestination);
      }

      boolean var5 = this.whereCollector.isEmpty() ^ true;
      if (var5) {
         var1.append(" WHERE ");
         this.whereCollector.appendWhereClause(var1, var2, this.values);
      }

      Iterator var6 = this.joins.iterator();

      while(var6.hasNext()) {
         Join var7 = (Join)var6.next();
         if (!var7.whereCollector.isEmpty()) {
            if (!var5) {
               var1.append(" WHERE ");
               var5 = true;
            } else {
               var1.append(" AND ");
            }

            var7.whereCollector.appendWhereClause(var1, var7.tablePrefix, this.values);
         }
      }

   }

   private int checkAddLimit(StringBuilder var1) {
      int var2;
      if (this.limit != null) {
         var1.append(" LIMIT ?");
         this.values.add(this.limit);
         var2 = this.values.size() - 1;
      } else {
         var2 = -1;
      }

      return var2;
   }

   private int checkAddOffset(StringBuilder var1) {
      int var2;
      if (this.offset != null) {
         if (this.limit == null) {
            throw new IllegalStateException("Offset cannot be set without limit");
         }

         var1.append(" OFFSET ?");
         this.values.add(this.offset);
         var2 = this.values.size() - 1;
      } else {
         var2 = -1;
      }

      return var2;
   }

   private void checkLog(String var1) {
      if (LOG_SQL) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Built SQL for query: ");
         var2.append(var1);
         DaoLog.d(var2.toString());
      }

      if (LOG_VALUES) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Values for query: ");
         var3.append(this.values);
         DaoLog.d(var3.toString());
      }

   }

   private void checkOrderBuilder() {
      if (this.orderBuilder == null) {
         this.orderBuilder = new StringBuilder();
      } else if (this.orderBuilder.length() > 0) {
         this.orderBuilder.append(",");
      }

   }

   private StringBuilder createSelectBuilder() {
      StringBuilder var1 = new StringBuilder(SqlUtils.createSqlSelect(this.dao.getTablename(), this.tablePrefix, this.dao.getAllColumns(), this.distinct));
      this.appendJoinsAndWheres(var1, this.tablePrefix);
      if (this.orderBuilder != null && this.orderBuilder.length() > 0) {
         var1.append(" ORDER BY ");
         var1.append(this.orderBuilder);
      }

      return var1;
   }

   public static QueryBuilder internalCreate(AbstractDao var0) {
      return new QueryBuilder(var0);
   }

   private void orderAscOrDesc(String var1, Property... var2) {
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         Property var5 = var2[var3];
         this.checkOrderBuilder();
         this.append(this.orderBuilder, var5);
         if (String.class.equals(var5.type) && this.stringOrderCollation != null) {
            this.orderBuilder.append(this.stringOrderCollation);
         }

         this.orderBuilder.append(var1);
      }

   }

   public WhereCondition and(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      return this.whereCollector.combineWhereConditions(" AND ", var1, var2, var3);
   }

   protected StringBuilder append(StringBuilder var1, Property var2) {
      this.whereCollector.checkProperty(var2);
      var1.append(this.tablePrefix);
      var1.append('.');
      var1.append('\'');
      var1.append(var2.columnName);
      var1.append('\'');
      return var1;
   }

   public Query build() {
      StringBuilder var1 = this.createSelectBuilder();
      int var2 = this.checkAddLimit(var1);
      int var3 = this.checkAddOffset(var1);
      String var4 = var1.toString();
      this.checkLog(var4);
      return Query.create(this.dao, var4, this.values.toArray(), var2, var3);
   }

   public CountQuery buildCount() {
      StringBuilder var1 = new StringBuilder(SqlUtils.createSqlSelectCountStar(this.dao.getTablename(), this.tablePrefix));
      this.appendJoinsAndWheres(var1, this.tablePrefix);
      String var2 = var1.toString();
      this.checkLog(var2);
      return CountQuery.create(this.dao, var2, this.values.toArray());
   }

   public CursorQuery buildCursor() {
      StringBuilder var1 = this.createSelectBuilder();
      int var2 = this.checkAddLimit(var1);
      int var3 = this.checkAddOffset(var1);
      String var4 = var1.toString();
      this.checkLog(var4);
      return CursorQuery.create(this.dao, var4, this.values.toArray(), var2, var3);
   }

   public DeleteQuery buildDelete() {
      if (!this.joins.isEmpty()) {
         throw new DaoException("JOINs are not supported for DELETE queries");
      } else {
         String var1 = this.dao.getTablename();
         StringBuilder var2 = new StringBuilder(SqlUtils.createSqlDelete(var1, (String[])null));
         this.appendJoinsAndWheres(var2, this.tablePrefix);
         String var5 = var2.toString();
         StringBuilder var3 = new StringBuilder();
         var3.append(this.tablePrefix);
         var3.append(".\"");
         String var6 = var3.toString();
         StringBuilder var4 = new StringBuilder();
         var4.append('"');
         var4.append(var1);
         var4.append("\".\"");
         var1 = var5.replace(var6, var4.toString());
         this.checkLog(var1);
         return DeleteQuery.create(this.dao, var1, this.values.toArray());
      }
   }

   public long count() {
      return this.buildCount().count();
   }

   public QueryBuilder distinct() {
      this.distinct = true;
      return this;
   }

   public Join join(Class var1, Property var2) {
      return this.join(this.dao.getPkProperty(), var1, var2);
   }

   public Join join(Property var1, Class var2) {
      AbstractDao var4 = this.dao.getSession().getDao(var2);
      Property var3 = var4.getPkProperty();
      return this.addJoin(this.tablePrefix, var1, var4, var3);
   }

   public Join join(Property var1, Class var2, Property var3) {
      AbstractDao var4 = this.dao.getSession().getDao(var2);
      return this.addJoin(this.tablePrefix, var1, var4, var3);
   }

   public Join join(Join var1, Property var2, Class var3, Property var4) {
      AbstractDao var5 = this.dao.getSession().getDao(var3);
      return this.addJoin(var1.tablePrefix, var2, var5, var4);
   }

   public QueryBuilder limit(int var1) {
      this.limit = var1;
      return this;
   }

   public List list() {
      return this.build().list();
   }

   public CloseableListIterator listIterator() {
      return this.build().listIterator();
   }

   public LazyList listLazy() {
      return this.build().listLazy();
   }

   public LazyList listLazyUncached() {
      return this.build().listLazyUncached();
   }

   public QueryBuilder offset(int var1) {
      this.offset = var1;
      return this;
   }

   public WhereCondition or(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      return this.whereCollector.combineWhereConditions(" OR ", var1, var2, var3);
   }

   public QueryBuilder orderAsc(Property... var1) {
      this.orderAscOrDesc(" ASC", var1);
      return this;
   }

   public QueryBuilder orderCustom(Property var1, String var2) {
      this.checkOrderBuilder();
      this.append(this.orderBuilder, var1).append(' ');
      this.orderBuilder.append(var2);
      return this;
   }

   public QueryBuilder orderDesc(Property... var1) {
      this.orderAscOrDesc(" DESC", var1);
      return this;
   }

   public QueryBuilder orderRaw(String var1) {
      this.checkOrderBuilder();
      this.orderBuilder.append(var1);
      return this;
   }

   public QueryBuilder preferLocalizedStringOrder() {
      if (this.dao.getDatabase().getRawDatabase() instanceof SQLiteDatabase) {
         this.stringOrderCollation = " COLLATE LOCALIZED";
      }

      return this;
   }

   @Experimental
   public RxQuery rx() {
      return this.build().__InternalRx();
   }

   @Experimental
   public RxQuery rxPlain() {
      return this.build().__internalRxPlain();
   }

   public QueryBuilder stringOrderCollation(String var1) {
      if (this.dao.getDatabase().getRawDatabase() instanceof SQLiteDatabase) {
         String var2 = var1;
         if (var1 != null) {
            if (var1.startsWith(" ")) {
               var2 = var1;
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append(" ");
               var3.append(var1);
               var2 = var3.toString();
            }
         }

         this.stringOrderCollation = var2;
      }

      return this;
   }

   public Object unique() {
      return this.build().unique();
   }

   public Object uniqueOrThrow() {
      return this.build().uniqueOrThrow();
   }

   public QueryBuilder where(WhereCondition var1, WhereCondition... var2) {
      this.whereCollector.add(var1, var2);
      return this;
   }

   public QueryBuilder whereOr(WhereCondition var1, WhereCondition var2, WhereCondition... var3) {
      this.whereCollector.add(this.or(var1, var2, var3));
      return this;
   }
}
