// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryWithLimit<T> extends AbstractQuery<T>
{
    protected final int limitPosition;
    protected final int offsetPosition;
    
    protected AbstractQueryWithLimit(final AbstractDao<T, ?> abstractDao, final String s, final String[] array, final int limitPosition, final int offsetPosition) {
        super(abstractDao, s, array);
        this.limitPosition = limitPosition;
        this.offsetPosition = offsetPosition;
    }
    
    public void setLimit(final int i) {
        this.checkThread();
        if (this.limitPosition == -1) {
            throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.limitPosition] = Integer.toString(i);
    }
    
    public void setOffset(final int i) {
        this.checkThread();
        if (this.offsetPosition == -1) {
            throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.offsetPosition] = Integer.toString(i);
    }
    
    @Override
    public AbstractQueryWithLimit<T> setParameter(final int i, final Object o) {
        if (i >= 0 && (i == this.limitPosition || i == this.offsetPosition)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Illegal parameter index: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        return (AbstractQueryWithLimit)super.setParameter(i, o);
    }
}
