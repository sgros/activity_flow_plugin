// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.Date;
import android.database.Cursor;
import org.greenrobot.greendao.AbstractDao;

public class CursorQuery<T> extends AbstractQueryWithLimit<T>
{
    private final QueryData<T> queryData;
    
    private CursorQuery(final QueryData<T> queryData, final AbstractDao<T, ?> abstractDao, final String s, final String[] array, final int n, final int n2) {
        super(abstractDao, s, array, n, n2);
        this.queryData = queryData;
    }
    
    static <T2> CursorQuery<T2> create(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array, final int n, final int n2) {
        return (CursorQuery<T2>)new QueryData(abstractDao, s, AbstractQuery.toStringArray(array), n, n2).forCurrentThread();
    }
    
    public static <T2> CursorQuery<T2> internalCreate(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array) {
        return create(abstractDao, s, array, -1, -1);
    }
    
    public CursorQuery forCurrentThread() {
        return this.queryData.forCurrentThread((CursorQuery<T2>)this);
    }
    
    public Cursor query() {
        this.checkThread();
        return this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    }
    
    @Override
    public CursorQuery<T> setParameter(final int n, final Boolean b) {
        return (CursorQuery)super.setParameter(n, b);
    }
    
    @Override
    public CursorQuery<T> setParameter(final int n, final Object o) {
        return (CursorQuery)super.setParameter(n, o);
    }
    
    @Override
    public CursorQuery<T> setParameter(final int n, final Date date) {
        return (CursorQuery)super.setParameter(n, date);
    }
    
    private static final class QueryData<T2> extends AbstractQueryData<T2, CursorQuery<T2>>
    {
        private final int limitPosition;
        private final int offsetPosition;
        
        QueryData(final AbstractDao abstractDao, final String s, final String[] array, final int limitPosition, final int offsetPosition) {
            super(abstractDao, s, array);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }
        
        @Override
        protected CursorQuery<T2> createQuery() {
            return new CursorQuery<T2>(this, this.dao, this.sql, this.initialValues.clone(), this.limitPosition, this.offsetPosition, null);
        }
    }
}
