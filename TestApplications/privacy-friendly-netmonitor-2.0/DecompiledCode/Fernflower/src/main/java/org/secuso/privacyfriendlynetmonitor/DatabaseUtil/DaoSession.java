package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import java.util.Map;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public class DaoSession extends AbstractDaoSession {
   private final ReportEntityDao reportEntityDao;
   private final DaoConfig reportEntityDaoConfig;

   public DaoSession(Database var1, IdentityScopeType var2, Map var3) {
      super(var1);
      this.reportEntityDaoConfig = ((DaoConfig)var3.get(ReportEntityDao.class)).clone();
      this.reportEntityDaoConfig.initIdentityScope(var2);
      this.reportEntityDao = new ReportEntityDao(this.reportEntityDaoConfig, this);
      this.registerDao(ReportEntity.class, this.reportEntityDao);
   }

   public void clear() {
      this.reportEntityDaoConfig.clearIdentityScope();
   }

   public ReportEntityDao getReportEntityDao() {
      return this.reportEntityDao;
   }
}
