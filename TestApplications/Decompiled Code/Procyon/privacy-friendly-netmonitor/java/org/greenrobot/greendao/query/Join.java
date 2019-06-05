// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.AbstractDao;

public class Join<SRC, DST>
{
    final AbstractDao<DST, ?> daoDestination;
    final Property joinPropertyDestination;
    final Property joinPropertySource;
    final String sourceTablePrefix;
    final String tablePrefix;
    final WhereCollector<DST> whereCollector;
    
    public Join(final String sourceTablePrefix, final Property joinPropertySource, final AbstractDao<DST, ?> daoDestination, final Property joinPropertyDestination, final String tablePrefix) {
        this.sourceTablePrefix = sourceTablePrefix;
        this.joinPropertySource = joinPropertySource;
        this.daoDestination = daoDestination;
        this.joinPropertyDestination = joinPropertyDestination;
        this.tablePrefix = tablePrefix;
        this.whereCollector = new WhereCollector<DST>(daoDestination, tablePrefix);
    }
    
    public WhereCondition and(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        return this.whereCollector.combineWhereConditions(" AND ", whereCondition, whereCondition2, array);
    }
    
    public String getTablePrefix() {
        return this.tablePrefix;
    }
    
    public WhereCondition or(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        return this.whereCollector.combineWhereConditions(" OR ", whereCondition, whereCondition2, array);
    }
    
    public Join<SRC, DST> where(final WhereCondition whereCondition, final WhereCondition... array) {
        this.whereCollector.add(whereCondition, array);
        return this;
    }
    
    public Join<SRC, DST> whereOr(final WhereCondition whereCondition, final WhereCondition whereCondition2, final WhereCondition... array) {
        this.whereCollector.add(this.or(whereCondition, whereCondition2, array), new WhereCondition[0]);
        return this;
    }
}
