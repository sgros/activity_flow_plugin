// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import org.greenrobot.greendao.AbstractDao;
import java.util.Map;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.AbstractDaoSession;

public class DaoSession extends AbstractDaoSession
{
    private final ReportEntityDao reportEntityDao;
    private final DaoConfig reportEntityDaoConfig;
    
    public DaoSession(final Database database, final IdentityScopeType identityScopeType, final Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(database);
        (this.reportEntityDaoConfig = map.get(ReportEntityDao.class).clone()).initIdentityScope(identityScopeType);
        this.registerDao(ReportEntity.class, this.reportEntityDao = new ReportEntityDao(this.reportEntityDaoConfig, this));
    }
    
    public void clear() {
        this.reportEntityDaoConfig.clearIdentityScope();
    }
    
    public ReportEntityDao getReportEntityDao() {
        return this.reportEntityDao;
    }
}
