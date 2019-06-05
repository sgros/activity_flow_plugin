package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;

public abstract class AbstractDaoSessionTest extends DbTest {
   protected AbstractDaoMaster daoMaster;
   private final Class daoMasterClass;
   protected AbstractDaoSession daoSession;

   public AbstractDaoSessionTest(Class var1) {
      this(var1, true);
   }

   public AbstractDaoSessionTest(Class var1, boolean var2) {
      super(var2);
      this.daoMasterClass = var1;
   }

   protected void setUp() throws Exception {
      super.setUp();

      try {
         this.daoMaster = (AbstractDaoMaster)this.daoMasterClass.getConstructor(Database.class).newInstance(this.db);
         this.daoMasterClass.getMethod("createAllTables", Database.class, Boolean.TYPE).invoke((Object)null, this.db, false);
      } catch (Exception var2) {
         throw new RuntimeException("Could not prepare DAO session test", var2);
      }

      this.daoSession = this.daoMaster.newSession();
   }
}
