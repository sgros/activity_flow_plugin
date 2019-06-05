// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.Date;
import android.database.Cursor;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.AbstractDao;

public class CountQuery<T> extends AbstractQuery<T>
{
    private final QueryData<T> queryData;
    
    private CountQuery(final QueryData<T> queryData, final AbstractDao<T, ?> abstractDao, final String s, final String[] array) {
        super(abstractDao, s, array);
        this.queryData = queryData;
    }
    
    static <T2> CountQuery<T2> create(final AbstractDao<T2, ?> abstractDao, final String s, final Object[] array) {
        return (CountQuery<T2>)new QueryData((AbstractDao)abstractDao, s, AbstractQuery.toStringArray(array)).forCurrentThread();
    }
    
    public long count() {
        this.checkThread();
        final Cursor rawQuery = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        try {
            if (!rawQuery.moveToNext()) {
                throw new DaoException("No result for count");
            }
            if (!rawQuery.isLast()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected row count: ");
                sb.append(rawQuery.getCount());
                throw new DaoException(sb.toString());
            }
            if (rawQuery.getColumnCount() != 1) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unexpected column count: ");
                sb2.append(rawQuery.getColumnCount());
                throw new DaoException(sb2.toString());
            }
            return rawQuery.getLong(0);
        }
        finally {
            rawQuery.close();
        }
    }
    
    public CountQuery<T> forCurrentThread() {
        return (CountQuery<T>)this.queryData.forCurrentThread((CountQuery<T2>)this);
    }
    
    @Override
    public CountQuery<T> setParameter(final int n, final Boolean b) {
        return (CountQuery)super.setParameter(n, b);
    }
    
    @Override
    public CountQuery<T> setParameter(final int n, final Object o) {
        return (CountQuery)super.setParameter(n, o);
    }
    
    @Override
    public CountQuery<T> setParameter(final int n, final Date date) {
        return (CountQuery)super.setParameter(n, date);
    }
    
    private static final class QueryData<T2> extends AbstractQueryData<T2, CountQuery<T2>>
    {
        private QueryData(final AbstractDao<T2, ?> abstractDao, final String s, final String[] array) {
            super(abstractDao, s, array);
        }
        
        @Override
        protected CountQuery<T2> createQuery() {
            return new CountQuery<T2>(this, this.dao, this.sql, this.initialValues.clone(), null);
        }
    }
}
