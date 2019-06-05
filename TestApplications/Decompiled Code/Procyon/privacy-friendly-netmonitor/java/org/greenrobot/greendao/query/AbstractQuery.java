// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQuery<T>
{
    protected final AbstractDao<T, ?> dao;
    protected final InternalQueryDaoAccess<T> daoAccess;
    protected final Thread ownerThread;
    protected final String[] parameters;
    protected final String sql;
    
    protected AbstractQuery(final AbstractDao<T, ?> dao, final String sql, final String[] parameters) {
        this.dao = dao;
        this.daoAccess = new InternalQueryDaoAccess<T>(dao);
        this.sql = sql;
        this.parameters = parameters;
        this.ownerThread = Thread.currentThread();
    }
    
    protected static String[] toStringArray(final Object[] array) {
        int i = 0;
        final int length = array.length;
        final String[] array2 = new String[length];
        while (i < length) {
            final Object o = array[i];
            if (o != null) {
                array2[i] = o.toString();
            }
            else {
                array2[i] = null;
            }
            ++i;
        }
        return array2;
    }
    
    protected void checkThread() {
        if (Thread.currentThread() != this.ownerThread) {
            throw new DaoException("Method may be called only in owner thread, use forCurrentThread to get an instance for this thread");
        }
    }
    
    public AbstractQuery<T> setParameter(final int n, final Boolean b) {
        Integer value;
        if (b != null) {
            value = (((boolean)b) ? 1 : 0);
        }
        else {
            value = null;
        }
        return this.setParameter(n, value);
    }
    
    public AbstractQuery<T> setParameter(final int n, final Object o) {
        this.checkThread();
        if (o != null) {
            this.parameters[n] = o.toString();
        }
        else {
            this.parameters[n] = null;
        }
        return this;
    }
    
    public AbstractQuery<T> setParameter(final int n, final Date date) {
        Long value;
        if (date != null) {
            value = date.getTime();
        }
        else {
            value = null;
        }
        return this.setParameter(n, value);
    }
}
