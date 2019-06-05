package org.greenrobot.greendao.rx;

import java.util.List;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.Observable;
import rx.Scheduler;

@Experimental
public class RxDao extends RxBase {
   private final AbstractDao dao;

   @Experimental
   public RxDao(AbstractDao var1) {
      this(var1, (Scheduler)null);
   }

   @Experimental
   public RxDao(AbstractDao var1, Scheduler var2) {
      super(var2);
      this.dao = var1;
   }

   @Experimental
   public Observable count() {
      return this.wrap(new Callable() {
         public Long call() throws Exception {
            return RxDao.this.dao.count();
         }
      });
   }

   @Experimental
   public Observable delete(final Object var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.delete(var1);
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteAll() {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteAll();
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteByKey(final Object var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteByKey(var1);
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteByKeyInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteByKeyInTx(var1);
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteByKeyInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteByKeyInTx(var1);
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteInTx(var1);
            return null;
         }
      });
   }

   @Experimental
   public Observable deleteInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxDao.this.dao.deleteInTx(var1);
            return null;
         }
      });
   }

   @Experimental
   public AbstractDao getDao() {
      return this.dao;
   }

   @Experimental
   public Observable insert(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            RxDao.this.dao.insert(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable insertInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Iterable call() throws Exception {
            RxDao.this.dao.insertInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable insertInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Object[] call() throws Exception {
            RxDao.this.dao.insertInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable insertOrReplace(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            RxDao.this.dao.insertOrReplace(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable insertOrReplaceInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Iterable call() throws Exception {
            RxDao.this.dao.insertOrReplaceInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable insertOrReplaceInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Object[] call() throws Exception {
            RxDao.this.dao.insertOrReplaceInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable load(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            return RxDao.this.dao.load(var1);
         }
      });
   }

   @Experimental
   public Observable loadAll() {
      return this.wrap(new Callable() {
         public List call() throws Exception {
            return RxDao.this.dao.loadAll();
         }
      });
   }

   @Experimental
   public Observable refresh(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            RxDao.this.dao.refresh(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable save(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            RxDao.this.dao.save(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable saveInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Iterable call() throws Exception {
            RxDao.this.dao.saveInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable saveInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Object[] call() throws Exception {
            RxDao.this.dao.saveInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable update(final Object var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            RxDao.this.dao.update(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable updateInTx(final Iterable var1) {
      return this.wrap(new Callable() {
         public Iterable call() throws Exception {
            RxDao.this.dao.updateInTx(var1);
            return var1;
         }
      });
   }

   @Experimental
   public Observable updateInTx(final Object... var1) {
      return this.wrap(new Callable() {
         public Object[] call() throws Exception {
            RxDao.this.dao.updateInTx(var1);
            return var1;
         }
      });
   }
}
