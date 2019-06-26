package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.Observable;
import rx.Scheduler;

@Experimental
public class RxTransaction extends RxBase {
   private final AbstractDaoSession daoSession;

   public RxTransaction(AbstractDaoSession var1) {
      this.daoSession = var1;
   }

   public RxTransaction(AbstractDaoSession var1, Scheduler var2) {
      super(var2);
      this.daoSession = var1;
   }

   @Experimental
   public Observable call(final Callable var1) {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            return RxTransaction.this.daoSession.callInTx(var1);
         }
      });
   }

   @Experimental
   public AbstractDaoSession getDaoSession() {
      return this.daoSession;
   }

   @Experimental
   public Observable run(final Runnable var1) {
      return this.wrap(new Callable() {
         public Void call() throws Exception {
            RxTransaction.this.daoSession.runInTx(var1);
            return null;
         }
      });
   }
}
