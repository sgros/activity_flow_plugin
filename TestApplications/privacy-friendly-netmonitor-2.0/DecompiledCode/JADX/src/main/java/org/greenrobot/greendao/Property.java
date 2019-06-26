package org.greenrobot.greendao;

import java.util.Collection;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.query.WhereCondition;
import org.greenrobot.greendao.query.WhereCondition.PropertyCondition;

public class Property {
    public final String columnName;
    public final String name;
    public final int ordinal;
    public final boolean primaryKey;
    public final Class<?> type;

    public Property(int i, Class<?> cls, String str, boolean z, String str2) {
        this.ordinal = i;
        this.type = cls;
        this.name = str;
        this.primaryKey = z;
        this.columnName = str2;
    }

    /* renamed from: eq */
    public WhereCondition mo6971eq(Object obj) {
        return new PropertyCondition(this, "=?", obj);
    }

    public WhereCondition notEq(Object obj) {
        return new PropertyCondition(this, "<>?", obj);
    }

    public WhereCondition like(String str) {
        return new PropertyCondition(this, " LIKE ?", (Object) str);
    }

    public WhereCondition between(Object obj, Object obj2) {
        return new PropertyCondition(this, " BETWEEN ? AND ?", new Object[]{obj, obj2});
    }

    /* renamed from: in */
    public WhereCondition mo6975in(Object... objArr) {
        StringBuilder stringBuilder = new StringBuilder(" IN (");
        SqlUtils.appendPlaceholders(stringBuilder, objArr.length).append(')');
        return new PropertyCondition(this, stringBuilder.toString(), objArr);
    }

    /* renamed from: in */
    public WhereCondition mo6974in(Collection<?> collection) {
        return mo6975in(collection.toArray());
    }

    public WhereCondition notIn(Object... objArr) {
        StringBuilder stringBuilder = new StringBuilder(" NOT IN (");
        SqlUtils.appendPlaceholders(stringBuilder, objArr.length).append(')');
        return new PropertyCondition(this, stringBuilder.toString(), objArr);
    }

    public WhereCondition notIn(Collection<?> collection) {
        return notIn(collection.toArray());
    }

    /* renamed from: gt */
    public WhereCondition mo6973gt(Object obj) {
        return new PropertyCondition(this, ">?", obj);
    }

    /* renamed from: lt */
    public WhereCondition mo6980lt(Object obj) {
        return new PropertyCondition(this, "<?", obj);
    }

    /* renamed from: ge */
    public WhereCondition mo6972ge(Object obj) {
        return new PropertyCondition(this, ">=?", obj);
    }

    /* renamed from: le */
    public WhereCondition mo6978le(Object obj) {
        return new PropertyCondition(this, "<=?", obj);
    }

    public WhereCondition isNull() {
        return new PropertyCondition(this, " IS NULL");
    }

    public WhereCondition isNotNull() {
        return new PropertyCondition(this, " IS NOT NULL");
    }
}
