package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

abstract class AbstractQuery {
   protected final AbstractDao dao;
   protected final InternalQueryDaoAccess daoAccess;
   protected final Thread ownerThread;
   protected final String[] parameters;
   protected final String sql;

   protected AbstractQuery(AbstractDao var1, String var2, String[] var3) {
      this.dao = var1;
      this.daoAccess = new InternalQueryDaoAccess(var1);
      this.sql = var2;
      this.parameters = var3;
      this.ownerThread = Thread.currentThread();
   }

   protected static String[] toStringArray(Object[] var0) {
      int var1 = 0;
      int var2 = var0.length;

      String[] var3;
      for(var3 = new String[var2]; var1 < var2; ++var1) {
         Object var4 = var0[var1];
         if (var4 != null) {
            var3[var1] = var4.toString();
         } else {
            var3[var1] = null;
         }
      }

      return var3;
   }

   protected void checkThread() {
      if (Thread.currentThread() != this.ownerThread) {
         throw new DaoException("Method may be called only in owner thread, use forCurrentThread to get an instance for this thread");
      }
   }

   public AbstractQuery setParameter(int var1, Boolean var2) {
      Integer var3;
      if (var2 != null) {
         var3 = Integer.valueOf(var2);
      } else {
         var3 = null;
      }

      return this.setParameter(var1, (Object)var3);
   }

   public AbstractQuery setParameter(int var1, Object var2) {
      this.checkThread();
      if (var2 != null) {
         this.parameters[var1] = var2.toString();
      } else {
         this.parameters[var1] = null;
      }

      return this;
   }

   public AbstractQuery setParameter(int var1, Date var2) {
      Long var3;
      if (var2 != null) {
         var3 = var2.getTime();
      } else {
         var3 = null;
      }

      return this.setParameter(var1, (Object)var3);
   }
}
