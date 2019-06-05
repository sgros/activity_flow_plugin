package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public class DaoSession extends AbstractDaoSession {
    private final ReportEntityDao reportEntityDao = new ReportEntityDao(this.reportEntityDaoConfig, this);
    private final DaoConfig reportEntityDaoConfig;

    public DaoSession(Database database, IdentityScopeType identityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(database);
        this.reportEntityDaoConfig = ((DaoConfig) map.get(ReportEntityDao.class)).clone();
        this.reportEntityDaoConfig.initIdentityScope(identityScopeType);
        registerDao(ReportEntity.class, this.reportEntityDao);
    }

    public void clear() {
        this.reportEntityDaoConfig.clearIdentityScope();
    }

    public ReportEntityDao getReportEntityDao() {
        return this.reportEntityDao;
    }
}
