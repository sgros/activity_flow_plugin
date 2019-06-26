package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.InternalUnitTestDaoAccess;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;

public abstract class AbstractDaoTest extends DbTest {
   protected AbstractDao dao;
   protected InternalUnitTestDaoAccess daoAccess;
   protected final Class daoClass;
   protected IdentityScope identityScopeForDao;
   protected Property pkColumn;

   public AbstractDaoTest(Class var1) {
      this(var1, true);
   }

   public AbstractDaoTest(Class var1, boolean var2) {
      super(var2);
      this.daoClass = var1;
   }

   protected void clearIdentityScopeIfAny() {
      if (this.identityScopeForDao != null) {
         this.identityScopeForDao.clear();
         DaoLog.d("Identity scope cleared");
      } else {
         DaoLog.d("No identity scope to clear");
      }

   }

   protected void logTableDump() {
      this.logTableDump(this.dao.getTablename());
   }

   public void setIdentityScopeBeforeSetUp(IdentityScope var1) {
      this.identityScopeForDao = var1;
   }

   protected void setUp() throws Exception {
      super.setUp();

      try {
         this.setUpTableForDao();
         InternalUnitTestDaoAccess var1 = new InternalUnitTestDaoAccess(this.db, this.daoClass, this.identityScopeForDao);
         this.daoAccess = var1;
         this.dao = this.daoAccess.getDao();
      } catch (Exception var2) {
         throw new RuntimeException("Could not prepare DAO Test", var2);
      }
   }

   protected void setUpTableForDao() throws Exception {
      try {
         this.daoClass.getMethod("createTable", Database.class, Boolean.TYPE).invoke((Object)null, this.db, false);
      } catch (NoSuchMethodException var2) {
         DaoLog.i("No createTable method");
      }

   }
}
