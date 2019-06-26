// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.AbstractDao;

public class DeleteQuery<T> extends AbstractQuery<T>
{
    private final QueryData<T> queryData;
    
    private DeleteQuery(final QueryData<T> queryData, final AbstractDao<T, ?> abstractDao, final String s, final String[] array) {
        super(abstractDao, s, array);
        this.queryData = queryData;
    }
    
    static <T2> DeleteQuery<T2> create(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array) {
        return (DeleteQuery<T2>)new QueryData((AbstractDao)abstractDao, s, AbstractQuery.toStringArray(array)).forCurrentThread();
    }
    
    public void executeDeleteWithoutDetachingEntities() {
        this.checkThread();
        final Database database = this.dao.getDatabase();
        if (database.isDbLockedByCurrentThread()) {
            this.dao.getDatabase().execSQL(this.sql, this.parameters);
            return;
        }
        database.beginTransaction();
        try {
            this.dao.getDatabase().execSQL(this.sql, this.parameters);
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }
    
    public DeleteQuery<T> forCurrentThread() {
        return (DeleteQuery<T>)this.queryData.forCurrentThread((DeleteQuery<T2>)this);
    }
    
    @Override
    public DeleteQuery<T> setParameter(final int n, final Boolean b) {
        return (DeleteQuery)super.setParameter(n, b);
    }
    
    @Override
    public DeleteQuery<T> setParameter(final int n, final Object o) {
        return (DeleteQuery)super.setParameter(n, o);
    }
    
    @Override
    public DeleteQuery<T> setParameter(final int n, final Date date) {
        return (DeleteQuery)super.setParameter(n, date);
    }
    
    private static final class QueryData<T2> extends AbstractQueryData<T2, DeleteQuery<T2>>
    {
        private QueryData(final AbstractDao<T2, ?> abstractDao, final String s, final String[] array) {
            super(abstractDao, s, array);
        }
        
        @Override
        protected DeleteQuery<T2> createQuery() {
            return new DeleteQuery<T2>(this, this.dao, this.sql, this.initialValues.clone(), null);
        }
    }
}
