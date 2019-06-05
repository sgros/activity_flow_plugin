package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.Observable;
import rx.Scheduler;

@Internal
class RxBase {
   protected final Scheduler scheduler;

   RxBase() {
      this.scheduler = null;
   }

   @Experimental
   RxBase(Scheduler var1) {
      this.scheduler = var1;
   }

   @Experimental
   public Scheduler getScheduler() {
      return this.scheduler;
   }

   protected Observable wrap(Callable var1) {
      return this.wrap(RxUtils.fromCallable(var1));
   }

   protected Observable wrap(Observable var1) {
      return this.scheduler != null ? var1.subscribeOn(this.scheduler) : var1;
   }
}
