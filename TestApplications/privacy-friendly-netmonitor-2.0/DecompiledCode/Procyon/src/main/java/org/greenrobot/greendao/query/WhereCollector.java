// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;

class WhereCollector<T>
{
    private final AbstractDao<T, ?> dao;
    private final String tablePrefix;
    private final List<WhereCondition> whereConditions;
    
    WhereCollector(final AbstractDao<T, ?> dao, final String tablePrefix) {
        this.dao = dao;
        this.tablePrefix = tablePrefix;
        this.whereConditions = new ArrayList<WhereCondition>();
    }
    
    void add(WhereCondition whereCondition, final WhereCondition... array) {
        this.checkCondition(whereCondition);
        this.whereConditions.add(whereCondition);
        for (int i = 0; i < array.length; ++i) {
            whereCondition = array[i];
            this.checkCondition(whereCondition);
            this.whereConditions.add(whereCondition);
        }
    }
    
    void addCondition(final StringBuilder sb, final List<Object> list, final WhereCondition whereCondition) {
        this.checkCondition(whereCondition);
        whereCondition.appendTo(sb, this.tablePrefix);
        whereCondition.appendValuesTo(list);
    }
    
    void appendWhereClause(final StringBuilder sb, final String s, final List<Object> list) {
        final ListIterator<WhereCondition> listIterator = this.whereConditions.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.hasPrevious()) {
                sb.append(" AND ");
            }
            final WhereCondition whereCondition = listIterator.next();
            whereCondition.appendTo(sb, s);
            whereCondition.appendValuesTo(list);
        }
    }
    
    void checkCondition(final WhereCondition whereCondition) {
        if (whereCondition instanceof WhereCondition.PropertyCondition) {
            this.checkProperty(((WhereCondition.PropertyCondition)whereCondition).property);
        }
    }
    
    void checkProperty(final Property property) {
        if (this.dao != null) {
            final Property[] properties = this.dao.getProperties();
            final int n = 0;
            final int length = properties.length;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                if (property == properties[n2]) {
                    n3 = 1;
                    break;
                }
                ++n2;
            }
            if (n3 == 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Property '");
                sb.append(property.name);
                sb.append("' is not part of ");
                sb.append(this.dao);
                throw new DaoException(sb.toString());
            }
        }
    }
    
    WhereCondition combineWhereConditions(final String s, WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        final StringBuilder sb = new StringBuilder("(");
        final ArrayList<Object> list = new ArrayList<Object>();
        this.addCondition(sb, list, whereCondition);
        sb.append(s);
        this.addCondition(sb, list, whereCondition2);
        for (int i = 0; i < array.length; ++i) {
            whereCondition = array[i];
            sb.append(s);
            this.addCondition(sb, list, whereCondition);
        }
        sb.append(')');
        return new WhereCondition.StringCondition(sb.toString(), list.toArray());
    }
    
    boolean isEmpty() {
        return this.whereConditions.isEmpty();
    }
}
