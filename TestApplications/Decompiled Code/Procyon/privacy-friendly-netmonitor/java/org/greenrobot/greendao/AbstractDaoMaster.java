// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import org.greenrobot.greendao.identityscope.IdentityScopeType;
import java.util.HashMap;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;
import java.util.Map;

public abstract class AbstractDaoMaster
{
    protected final Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap;
    protected final Database db;
    protected final int schemaVersion;
    
    public AbstractDaoMaster(final Database db, final int schemaVersion) {
        this.db = db;
        this.schemaVersion = schemaVersion;
        this.daoConfigMap = new HashMap<Class<? extends AbstractDao<?, ?>>, DaoConfig>();
    }
    
    public Database getDatabase() {
        return this.db;
    }
    
    public int getSchemaVersion() {
        return this.schemaVersion;
    }
    
    public abstract AbstractDaoSession newSession();
    
    public abstract AbstractDaoSession newSession(final IdentityScopeType p0);
    
    protected void registerDaoClass(final Class<? extends AbstractDao<?, ?>> clazz) {
        this.daoConfigMap.put(clazz, new DaoConfig(this.db, clazz));
    }
}
