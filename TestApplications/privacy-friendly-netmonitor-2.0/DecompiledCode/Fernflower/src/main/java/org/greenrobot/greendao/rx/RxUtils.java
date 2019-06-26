package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.Observable;
import rx.functions.Func0;

@Internal
class RxUtils {
   @Internal
   static Observable fromCallable(final Callable var0) {
      return Observable.defer(new Func0() {
         public Observable call() {
            Object var1;
            try {
               var1 = var0.call();
            } catch (Exception var2) {
               return Observable.error(var2);
            }

            return Observable.just(var1);
         }
      });
   }
}
