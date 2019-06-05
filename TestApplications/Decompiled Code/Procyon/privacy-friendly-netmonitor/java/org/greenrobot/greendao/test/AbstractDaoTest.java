// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.InternalUnitTestDaoAccess;
import org.greenrobot.greendao.AbstractDao;

public abstract class AbstractDaoTest<D extends AbstractDao<T, K>, T, K> extends DbTest
{
    protected D dao;
    protected InternalUnitTestDaoAccess<T, K> daoAccess;
    protected final Class<D> daoClass;
    protected IdentityScope<K, T> identityScopeForDao;
    protected Property pkColumn;
    
    public AbstractDaoTest(final Class<D> clazz) {
        this(clazz, true);
    }
    
    public AbstractDaoTest(final Class<D> daoClass, final boolean b) {
        super(b);
        this.daoClass = daoClass;
    }
    
    protected void clearIdentityScopeIfAny() {
        if (this.identityScopeForDao != null) {
            this.identityScopeForDao.clear();
            DaoLog.d("Identity scope cleared");
        }
        else {
            DaoLog.d("No identity scope to clear");
        }
    }
    
    protected void logTableDump() {
        this.logTableDump(this.dao.getTablename());
    }
    
    public void setIdentityScopeBeforeSetUp(final IdentityScope<K, T> identityScopeForDao) {
        this.identityScopeForDao = identityScopeForDao;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.setUpTableForDao();
            this.daoAccess = new InternalUnitTestDaoAccess<T, K>(this.db, (Class<AbstractDao<T, K>>)this.daoClass, this.identityScopeForDao);
            this.dao = (D)this.daoAccess.getDao();
        }
        catch (Exception cause) {
            throw new RuntimeException("Could not prepare DAO Test", cause);
        }
    }
    
    protected void setUpTableForDao() throws Exception {
        try {
            this.daoClass.getMethod("createTable", Database.class, Boolean.TYPE).invoke(null, this.db, false);
        }
        catch (NoSuchMethodException ex) {
            DaoLog.i("No createTable method");
        }
    }
}
