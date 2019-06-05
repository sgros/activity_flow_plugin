// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import android.database.Cursor;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.database.Database;

public class InternalUnitTestDaoAccess<T, K>
{
    private final AbstractDao<T, K> dao;
    
    public InternalUnitTestDaoAccess(final Database database, final Class<AbstractDao<T, K>> clazz, final IdentityScope<?, ?> identityScope) throws Exception {
        final DaoConfig daoConfig = new DaoConfig(database, clazz);
        daoConfig.setIdentityScope(identityScope);
        this.dao = clazz.getConstructor(DaoConfig.class).newInstance(daoConfig);
    }
    
    public AbstractDao<T, K> getDao() {
        return this.dao;
    }
    
    public K getKey(final T t) {
        return this.dao.getKey(t);
    }
    
    public Property[] getProperties() {
        return this.dao.getProperties();
    }
    
    public boolean isEntityUpdateable() {
        return this.dao.isEntityUpdateable();
    }
    
    public T readEntity(final Cursor cursor, final int n) {
        return this.dao.readEntity(cursor, n);
    }
    
    public K readKey(final Cursor cursor, final int n) {
        return this.dao.readKey(cursor, n);
    }
}
