// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.AbstractDaoMaster;

public abstract class AbstractDaoSessionTest<T extends AbstractDaoMaster, S extends AbstractDaoSession> extends DbTest
{
    protected T daoMaster;
    private final Class<T> daoMasterClass;
    protected S daoSession;
    
    public AbstractDaoSessionTest(final Class<T> clazz) {
        this(clazz, true);
    }
    
    public AbstractDaoSessionTest(final Class<T> daoMasterClass, final boolean b) {
        super(b);
        this.daoMasterClass = daoMasterClass;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.daoMaster = this.daoMasterClass.getConstructor(Database.class).newInstance(this.db);
            this.daoMasterClass.getMethod("createAllTables", Database.class, Boolean.TYPE).invoke(null, this.db, false);
            this.daoSession = (S)this.daoMaster.newSession();
        }
        catch (Exception cause) {
            throw new RuntimeException("Could not prepare DAO session test", cause);
        }
    }
}
