// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import org.greenrobot.greendao.DaoException;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.schedulers.Schedulers;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.rx.RxQuery;

public class Query<T> extends AbstractQueryWithLimit<T>
{
    private final QueryData<T> queryData;
    private volatile RxQuery rxTxIo;
    private volatile RxQuery rxTxPlain;
    
    private Query(final QueryData<T> queryData, final AbstractDao<T, ?> abstractDao, final String s, final String[] array, final int n, final int n2) {
        super(abstractDao, s, array, n, n2);
        this.queryData = queryData;
    }
    
    static <T2> Query<T2> create(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array, final int n, final int n2) {
        return (Query<T2>)new QueryData((AbstractDao<Object, ?>)abstractDao, s, AbstractQuery.toStringArray(array), n, n2).forCurrentThread();
    }
    
    public static <T2> Query<T2> internalCreate(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array) {
        return create(abstractDao, s, array, -1, -1);
    }
    
    @Internal
    public RxQuery __InternalRx() {
        if (this.rxTxIo == null) {
            this.rxTxIo = new RxQuery((Query<T>)this, Schedulers.io());
        }
        return this.rxTxIo;
    }
    
    @Internal
    public RxQuery __internalRxPlain() {
        if (this.rxTxPlain == null) {
            this.rxTxPlain = new RxQuery((Query<T>)this);
        }
        return this.rxTxPlain;
    }
    
    public Query<T> forCurrentThread() {
        return (Query<T>)this.queryData.forCurrentThread((Query<T2>)this);
    }
    
    public List<T> list() {
        this.checkThread();
        return this.daoAccess.loadAllAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }
    
    public CloseableListIterator<T> listIterator() {
        return this.listLazyUncached().listIteratorAutoClose();
    }
    
    public LazyList<T> listLazy() {
        this.checkThread();
        return new LazyList<T>(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), true);
    }
    
    public LazyList<T> listLazyUncached() {
        this.checkThread();
        return new LazyList<T>(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), false);
    }
    
    @Override
    public Query<T> setParameter(final int n, final Boolean b) {
        return (Query)super.setParameter(n, b);
    }
    
    @Override
    public Query<T> setParameter(final int n, final Object o) {
        return (Query)super.setParameter(n, o);
    }
    
    @Override
    public Query<T> setParameter(final int n, final Date date) {
        return (Query)super.setParameter(n, date);
    }
    
    public T unique() {
        this.checkThread();
        return this.daoAccess.loadUniqueAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }
    
    public T uniqueOrThrow() {
        final T unique = this.unique();
        if (unique == null) {
            throw new DaoException("No entity found for query");
        }
        return unique;
    }
    
    private static final class QueryData<T2> extends AbstractQueryData<T2, Query<T2>>
    {
        private final int limitPosition;
        private final int offsetPosition;
        
        QueryData(final AbstractDao<T2, ?> abstractDao, final String s, final String[] array, final int limitPosition, final int offsetPosition) {
            super(abstractDao, s, array);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }
        
        @Override
        protected Query<T2> createQuery() {
            return new Query<T2>(this, this.dao, this.sql, this.initialValues.clone(), this.limitPosition, this.offsetPosition, null);
        }
    }
}
