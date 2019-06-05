package org.greenrobot.greendao;

import java.util.HashMap;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public abstract class AbstractDaoMaster {
   protected final Map daoConfigMap;
   protected final Database db;
   protected final int schemaVersion;

   public AbstractDaoMaster(Database var1, int var2) {
      this.db = var1;
      this.schemaVersion = var2;
      this.daoConfigMap = new HashMap();
   }

   public Database getDatabase() {
      return this.db;
   }

   public int getSchemaVersion() {
      return this.schemaVersion;
   }

   public abstract AbstractDaoSession newSession();

   public abstract AbstractDaoSession newSession(IdentityScopeType var1);

   protected void registerDaoClass(Class var1) {
      DaoConfig var2 = new DaoConfig(this.db, var1);
      this.daoConfigMap.put(var1, var2);
   }
}
