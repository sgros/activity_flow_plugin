// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.AbstractDao;

public abstract class AbstractDaoTestLongPk<D extends AbstractDao<T, Long>, T> extends AbstractDaoTestSinglePk<D, T, Long>
{
    public AbstractDaoTestLongPk(final Class<D> clazz) {
        super(clazz);
    }
    
    @Override
    protected Long createRandomPk() {
        return this.random.nextLong();
    }
    
    public void testAssignPk() {
        if (this.daoAccess.isEntityUpdateable()) {
            final Object entity = this.createEntity(null);
            if (entity != null) {
                final Object entity2 = this.createEntity(null);
                ((AbstractDao<T, K>)this.dao).insert((T)entity);
                ((AbstractDao<T, K>)this.dao).insert((T)entity2);
                final Long n = (Long)this.daoAccess.getKey((T)entity);
                assertNotNull((Object)n);
                final Long obj = (Long)this.daoAccess.getKey((T)entity2);
                assertNotNull((Object)obj);
                assertFalse(n.equals(obj));
                assertNotNull(((AbstractDao<Object, K>)this.dao).load((K)n));
                assertNotNull(((AbstractDao<Object, K>)this.dao).load((K)obj));
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("Skipping testAssignPk for ");
                sb.append(this.daoClass);
                sb.append(" (createEntity returned null for null key)");
                DaoLog.d(sb.toString());
            }
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Skipping testAssignPk for not updateable ");
            sb2.append(this.daoClass);
            DaoLog.d(sb2.toString());
        }
    }
}
