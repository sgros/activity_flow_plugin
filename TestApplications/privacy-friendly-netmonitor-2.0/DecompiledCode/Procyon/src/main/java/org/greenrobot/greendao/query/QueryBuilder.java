// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.rx.RxQuery;
import android.database.sqlite.SQLiteDatabase;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import java.util.Iterator;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.Property;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;

public class QueryBuilder<T>
{
    public static boolean LOG_SQL;
    public static boolean LOG_VALUES;
    private final AbstractDao<T, ?> dao;
    private boolean distinct;
    private final List<Join<T, ?>> joins;
    private Integer limit;
    private Integer offset;
    private StringBuilder orderBuilder;
    private String stringOrderCollation;
    private final String tablePrefix;
    private final List<Object> values;
    private final WhereCollector<T> whereCollector;
    
    protected QueryBuilder(final AbstractDao<T, ?> abstractDao) {
        this(abstractDao, "T");
    }
    
    protected QueryBuilder(final AbstractDao<T, ?> dao, final String tablePrefix) {
        this.dao = dao;
        this.tablePrefix = tablePrefix;
        this.values = new ArrayList<Object>();
        this.joins = new ArrayList<Join<T, ?>>();
        this.whereCollector = new WhereCollector<T>(dao, tablePrefix);
        this.stringOrderCollation = " COLLATE NOCASE";
    }
    
    private <J> Join<T, J> addJoin(final String s, final Property property, final AbstractDao<J, ?> abstractDao, final Property property2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("J");
        sb.append(this.joins.size() + 1);
        final Join join = new Join<T, Object>(s, property, (AbstractDao<Object, ?>)abstractDao, property2, sb.toString());
        this.joins.add((Join<T, ?>)join);
        return (Join<T, J>)join;
    }
    
    private void appendJoinsAndWheres(final StringBuilder sb, final String s) {
        this.values.clear();
        for (final Join<T, ?> join : this.joins) {
            sb.append(" JOIN ");
            sb.append(join.daoDestination.getTablename());
            sb.append(' ');
            sb.append(join.tablePrefix);
            sb.append(" ON ");
            SqlUtils.appendProperty(sb, join.sourceTablePrefix, join.joinPropertySource).append('=');
            SqlUtils.appendProperty(sb, join.tablePrefix, join.joinPropertyDestination);
        }
        int n = (this.whereCollector.isEmpty() ^ true) ? 1 : 0;
        if (n != 0) {
            sb.append(" WHERE ");
            this.whereCollector.appendWhereClause(sb, s, this.values);
        }
        for (final Join<T, ?> join2 : this.joins) {
            if (!join2.whereCollector.isEmpty()) {
                if (n == 0) {
                    sb.append(" WHERE ");
                    n = 1;
                }
                else {
                    sb.append(" AND ");
                }
                join2.whereCollector.appendWhereClause(sb, join2.tablePrefix, this.values);
            }
        }
    }
    
    private int checkAddLimit(final StringBuilder sb) {
        int n;
        if (this.limit != null) {
            sb.append(" LIMIT ?");
            this.values.add(this.limit);
            n = this.values.size() - 1;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    private int checkAddOffset(final StringBuilder sb) {
        int n;
        if (this.offset != null) {
            if (this.limit == null) {
                throw new IllegalStateException("Offset cannot be set without limit");
            }
            sb.append(" OFFSET ?");
            this.values.add(this.offset);
            n = this.values.size() - 1;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    private void checkLog(final String str) {
        if (QueryBuilder.LOG_SQL) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Built SQL for query: ");
            sb.append(str);
            DaoLog.d(sb.toString());
        }
        if (QueryBuilder.LOG_VALUES) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Values for query: ");
            sb2.append(this.values);
            DaoLog.d(sb2.toString());
        }
    }
    
    private void checkOrderBuilder() {
        if (this.orderBuilder == null) {
            this.orderBuilder = new StringBuilder();
        }
        else if (this.orderBuilder.length() > 0) {
            this.orderBuilder.append(",");
        }
    }
    
    private StringBuilder createSelectBuilder() {
        final StringBuilder sb = new StringBuilder(SqlUtils.createSqlSelect(this.dao.getTablename(), this.tablePrefix, this.dao.getAllColumns(), this.distinct));
        this.appendJoinsAndWheres(sb, this.tablePrefix);
        if (this.orderBuilder != null && this.orderBuilder.length() > 0) {
            sb.append(" ORDER BY ");
            sb.append((CharSequence)this.orderBuilder);
        }
        return sb;
    }
    
    public static <T2> QueryBuilder<T2> internalCreate(final AbstractDao<T2, ?> abstractDao) {
        return new QueryBuilder<T2>(abstractDao);
    }
    
    private void orderAscOrDesc(final String str, final Property... array) {
        for (int i = 0; i < array.length; ++i) {
            final Property property = array[i];
            this.checkOrderBuilder();
            this.append(this.orderBuilder, property);
            if (String.class.equals(property.type) && this.stringOrderCollation != null) {
                this.orderBuilder.append(this.stringOrderCollation);
            }
            this.orderBuilder.append(str);
        }
    }
    
    public WhereCondition and(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        return this.whereCollector.combineWhereConditions(" AND ", whereCondition, whereCondition2, array);
    }
    
    protected StringBuilder append(final StringBuilder sb, final Property property) {
        this.whereCollector.checkProperty(property);
        sb.append(this.tablePrefix);
        sb.append('.');
        sb.append('\'');
        sb.append(property.columnName);
        sb.append('\'');
        return sb;
    }
    
    public Query<T> build() {
        final StringBuilder selectBuilder = this.createSelectBuilder();
        final int checkAddLimit = this.checkAddLimit(selectBuilder);
        final int checkAddOffset = this.checkAddOffset(selectBuilder);
        final String string = selectBuilder.toString();
        this.checkLog(string);
        return Query.create(this.dao, string, this.values.toArray(), checkAddLimit, checkAddOffset);
    }
    
    public CountQuery<T> buildCount() {
        final StringBuilder sb = new StringBuilder(SqlUtils.createSqlSelectCountStar(this.dao.getTablename(), this.tablePrefix));
        this.appendJoinsAndWheres(sb, this.tablePrefix);
        final String string = sb.toString();
        this.checkLog(string);
        return CountQuery.create(this.dao, string, this.values.toArray());
    }
    
    public CursorQuery buildCursor() {
        final StringBuilder selectBuilder = this.createSelectBuilder();
        final int checkAddLimit = this.checkAddLimit(selectBuilder);
        final int checkAddOffset = this.checkAddOffset(selectBuilder);
        final String string = selectBuilder.toString();
        this.checkLog(string);
        return CursorQuery.create(this.dao, string, this.values.toArray(), checkAddLimit, checkAddOffset);
    }
    
    public DeleteQuery<T> buildDelete() {
        if (!this.joins.isEmpty()) {
            throw new DaoException("JOINs are not supported for DELETE queries");
        }
        final String tablename = this.dao.getTablename();
        final StringBuilder sb = new StringBuilder(SqlUtils.createSqlDelete(tablename, null));
        this.appendJoinsAndWheres(sb, this.tablePrefix);
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.tablePrefix);
        sb2.append(".\"");
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append('\"');
        sb3.append(tablename);
        sb3.append("\".\"");
        final String replace = string.replace(string2, sb3.toString());
        this.checkLog(replace);
        return DeleteQuery.create(this.dao, replace, this.values.toArray());
    }
    
    public long count() {
        return this.buildCount().count();
    }
    
    public QueryBuilder<T> distinct() {
        this.distinct = true;
        return this;
    }
    
    public <J> Join<T, J> join(final Class<J> clazz, final Property property) {
        return this.join(this.dao.getPkProperty(), clazz, property);
    }
    
    public <J> Join<T, J> join(final Property property, final Class<J> clazz) {
        final AbstractDao<?, ?> dao = this.dao.getSession().getDao(clazz);
        return this.addJoin(this.tablePrefix, property, dao, dao.getPkProperty());
    }
    
    public <J> Join<T, J> join(final Property property, final Class<J> clazz, final Property property2) {
        return this.addJoin(this.tablePrefix, property, this.dao.getSession().getDao(clazz), property2);
    }
    
    public <J> Join<T, J> join(final Join<?, T> join, final Property property, final Class<J> clazz, final Property property2) {
        return this.addJoin(join.tablePrefix, property, this.dao.getSession().getDao(clazz), property2);
    }
    
    public QueryBuilder<T> limit(final int i) {
        this.limit = i;
        return this;
    }
    
    public List<T> list() {
        return this.build().list();
    }
    
    public CloseableListIterator<T> listIterator() {
        return this.build().listIterator();
    }
    
    public LazyList<T> listLazy() {
        return this.build().listLazy();
    }
    
    public LazyList<T> listLazyUncached() {
        return this.build().listLazyUncached();
    }
    
    public QueryBuilder<T> offset(final int i) {
        this.offset = i;
        return this;
    }
    
    public WhereCondition or(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        return this.whereCollector.combineWhereConditions(" OR ", whereCondition, whereCondition2, array);
    }
    
    public QueryBuilder<T> orderAsc(final Property... array) {
        this.orderAscOrDesc(" ASC", array);
        return this;
    }
    
    public QueryBuilder<T> orderCustom(final Property property, final String str) {
        this.checkOrderBuilder();
        this.append(this.orderBuilder, property).append(' ');
        this.orderBuilder.append(str);
        return this;
    }
    
    public QueryBuilder<T> orderDesc(final Property... array) {
        this.orderAscOrDesc(" DESC", array);
        return this;
    }
    
    public QueryBuilder<T> orderRaw(final String str) {
        this.checkOrderBuilder();
        this.orderBuilder.append(str);
        return this;
    }
    
    public QueryBuilder<T> preferLocalizedStringOrder() {
        if (this.dao.getDatabase().getRawDatabase() instanceof SQLiteDatabase) {
            this.stringOrderCollation = " COLLATE LOCALIZED";
        }
        return this;
    }
    
    @Experimental
    public RxQuery<T> rx() {
        return (RxQuery<T>)this.build().__InternalRx();
    }
    
    @Experimental
    public RxQuery<T> rxPlain() {
        return (RxQuery<T>)this.build().__internalRxPlain();
    }
    
    public QueryBuilder<T> stringOrderCollation(final String str) {
        if (this.dao.getDatabase().getRawDatabase() instanceof SQLiteDatabase) {
            String string;
            if ((string = str) != null) {
                if (str.startsWith(" ")) {
                    string = str;
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(" ");
                    sb.append(str);
                    string = sb.toString();
                }
            }
            this.stringOrderCollation = string;
        }
        return this;
    }
    
    public T unique() {
        return this.build().unique();
    }
    
    public T uniqueOrThrow() {
        return this.build().uniqueOrThrow();
    }
    
    public QueryBuilder<T> where(final WhereCondition whereCondition, final WhereCondition... array) {
        this.whereCollector.add(whereCondition, array);
        return this;
    }
    
    public QueryBuilder<T> whereOr(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        this.whereCollector.add(this.or(whereCondition, whereCondition2, array), new WhereCondition[0]);
        return this;
    }
}
