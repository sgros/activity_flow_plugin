package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.InternalUnitTestDaoAccess;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;

public abstract class AbstractDaoTest<D extends AbstractDao<T, K>, T, K> extends DbTest {
    protected D dao;
    protected InternalUnitTestDaoAccess<T, K> daoAccess;
    protected final Class<D> daoClass;
    protected IdentityScope<K, T> identityScopeForDao;
    protected Property pkColumn;

    public AbstractDaoTest(Class<D> cls) {
        this(cls, true);
    }

    public AbstractDaoTest(Class<D> cls, boolean z) {
        super(z);
        this.daoClass = cls;
    }

    public void setIdentityScopeBeforeSetUp(IdentityScope<K, T> identityScope) {
        this.identityScopeForDao = identityScope;
    }

    /* Access modifiers changed, original: protected */
    public void setUp() throws Exception {
        super.setUp();
        try {
            setUpTableForDao();
            this.daoAccess = new InternalUnitTestDaoAccess(this.f76db, this.daoClass, this.identityScopeForDao);
            this.dao = this.daoAccess.getDao();
        } catch (Exception e) {
            throw new RuntimeException("Could not prepare DAO Test", e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void setUpTableForDao() throws Exception {
        try {
            this.daoClass.getMethod("createTable", new Class[]{Database.class, Boolean.TYPE}).invoke(null, new Object[]{this.f76db, Boolean.valueOf(false)});
        } catch (NoSuchMethodException unused) {
            DaoLog.m15i("No createTable method");
        }
    }

    /* Access modifiers changed, original: protected */
    public void clearIdentityScopeIfAny() {
        if (this.identityScopeForDao != null) {
            this.identityScopeForDao.clear();
            DaoLog.m11d("Identity scope cleared");
            return;
        }
        DaoLog.m11d("No identity scope to clear");
    }

    /* Access modifiers changed, original: protected */
    public void logTableDump() {
        logTableDump(this.dao.getTablename());
    }
}
