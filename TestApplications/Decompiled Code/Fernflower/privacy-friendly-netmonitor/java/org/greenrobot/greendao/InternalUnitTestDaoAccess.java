package org.greenrobot.greendao;

import android.database.Cursor;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.internal.DaoConfig;

public class InternalUnitTestDaoAccess {
   private final AbstractDao dao;

   public InternalUnitTestDaoAccess(Database var1, Class var2, IdentityScope var3) throws Exception {
      DaoConfig var4 = new DaoConfig(var1, var2);
      var4.setIdentityScope(var3);
      this.dao = (AbstractDao)var2.getConstructor(DaoConfig.class).newInstance(var4);
   }

   public AbstractDao getDao() {
      return this.dao;
   }

   public Object getKey(Object var1) {
      return this.dao.getKey(var1);
   }

   public Property[] getProperties() {
      return this.dao.getProperties();
   }

   public boolean isEntityUpdateable() {
      return this.dao.isEntityUpdateable();
   }

   public Object readEntity(Cursor var1, int var2) {
      return this.dao.readEntity(var1, var2);
   }

   public Object readKey(Cursor var1, int var2) {
      return this.dao.readKey(var1, var2);
   }
}
