package org.greenrobot.greendao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxTransaction;
import rx.schedulers.Schedulers;

public class AbstractDaoSession {
   private final Database db;
   private final Map entityToDao;
   private volatile RxTransaction rxTxIo;
   private volatile RxTransaction rxTxPlain;

   public AbstractDaoSession(Database var1) {
      this.db = var1;
      this.entityToDao = new HashMap();
   }

   public Object callInTx(Callable var1) throws Exception {
      this.db.beginTransaction();

      Object var4;
      try {
         var4 = var1.call();
         this.db.setTransactionSuccessful();
      } finally {
         this.db.endTransaction();
      }

      return var4;
   }

   public Object callInTxNoException(Callable var1) {
      this.db.beginTransaction();

      Object var8;
      try {
         try {
            var8 = var1.call();
         } catch (Exception var5) {
            DaoException var7 = new DaoException("Callable failed", var5);
            throw var7;
         }

         this.db.setTransactionSuccessful();
      } finally {
         this.db.endTransaction();
      }

      return var8;
   }

   public void delete(Object var1) {
      this.getDao(var1.getClass()).delete(var1);
   }

   public void deleteAll(Class var1) {
      this.getDao(var1).deleteAll();
   }

   public Collection getAllDaos() {
      return Collections.unmodifiableCollection(this.entityToDao.values());
   }

   public AbstractDao getDao(Class var1) {
      AbstractDao var2 = (AbstractDao)this.entityToDao.get(var1);
      if (var2 == null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("No DAO registered for ");
         var3.append(var1);
         throw new DaoException(var3.toString());
      } else {
         return var2;
      }
   }

   public Database getDatabase() {
      return this.db;
   }

   public long insert(Object var1) {
      return this.getDao(var1.getClass()).insert(var1);
   }

   public long insertOrReplace(Object var1) {
      return this.getDao(var1.getClass()).insertOrReplace(var1);
   }

   public Object load(Class var1, Object var2) {
      return this.getDao(var1).load(var2);
   }

   public List loadAll(Class var1) {
      return this.getDao(var1).loadAll();
   }

   public QueryBuilder queryBuilder(Class var1) {
      return this.getDao(var1).queryBuilder();
   }

   public List queryRaw(Class var1, String var2, String... var3) {
      return this.getDao(var1).queryRaw(var2, var3);
   }

   public void refresh(Object var1) {
      this.getDao(var1.getClass()).refresh(var1);
   }

   protected void registerDao(Class var1, AbstractDao var2) {
      this.entityToDao.put(var1, var2);
   }

   public void runInTx(Runnable var1) {
      this.db.beginTransaction();

      try {
         var1.run();
         this.db.setTransactionSuccessful();
      } finally {
         this.db.endTransaction();
      }

   }

   @Experimental
   public RxTransaction rxTx() {
      if (this.rxTxIo == null) {
         this.rxTxIo = new RxTransaction(this, Schedulers.io());
      }

      return this.rxTxIo;
   }

   @Experimental
   public RxTransaction rxTxPlain() {
      if (this.rxTxPlain == null) {
         this.rxTxPlain = new RxTransaction(this);
      }

      return this.rxTxPlain;
   }

   public AsyncSession startAsyncSession() {
      return new AsyncSession(this);
   }

   public void update(Object var1) {
      this.getDao(var1.getClass()).update(var1);
   }
}
