package org.greenrobot.greendao;

import java.util.HashMap;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public abstract class AbstractDaoMaster {
    protected final Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap = new HashMap();
    /* renamed from: db */
    protected final Database f70db;
    protected final int schemaVersion;

    public abstract AbstractDaoSession newSession();

    public abstract AbstractDaoSession newSession(IdentityScopeType identityScopeType);

    public AbstractDaoMaster(Database database, int i) {
        this.f70db = database;
        this.schemaVersion = i;
    }

    /* Access modifiers changed, original: protected */
    public void registerDaoClass(Class<? extends AbstractDao<?, ?>> cls) {
        this.daoConfigMap.put(cls, new DaoConfig(this.f70db, cls));
    }

    public int getSchemaVersion() {
        return this.schemaVersion;
    }

    public Database getDatabase() {
        return this.f70db;
    }
}
