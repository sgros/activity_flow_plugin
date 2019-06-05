package org.greenrobot.greendao.test;

import org.greenrobot.greendao.DaoLog;

public abstract class AbstractDaoTestLongPk extends AbstractDaoTestSinglePk {
   public AbstractDaoTestLongPk(Class var1) {
      super(var1);
   }

   protected Long createRandomPk() {
      return this.random.nextLong();
   }

   public void testAssignPk() {
      StringBuilder var5;
      if (this.daoAccess.isEntityUpdateable()) {
         Object var1 = this.createEntity((Object)null);
         if (var1 != null) {
            Object var2 = this.createEntity((Object)null);
            this.dao.insert(var1);
            this.dao.insert(var2);
            Long var3 = (Long)this.daoAccess.getKey(var1);
            assertNotNull(var3);
            Long var4 = (Long)this.daoAccess.getKey(var2);
            assertNotNull(var4);
            assertFalse(var3.equals(var4));
            assertNotNull(this.dao.load(var3));
            assertNotNull(this.dao.load(var4));
         } else {
            var5 = new StringBuilder();
            var5.append("Skipping testAssignPk for ");
            var5.append(this.daoClass);
            var5.append(" (createEntity returned null for null key)");
            DaoLog.d(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Skipping testAssignPk for not updateable ");
         var5.append(this.daoClass);
         DaoLog.d(var5.toString());
      }

   }
}
