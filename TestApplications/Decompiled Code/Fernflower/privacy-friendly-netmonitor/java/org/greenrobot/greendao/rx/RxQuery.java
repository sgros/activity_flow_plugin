package org.greenrobot.greendao.rx;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.exceptions.Exceptions;

@Experimental
public class RxQuery extends RxBase {
   private final Query query;

   public RxQuery(Query var1) {
      this.query = var1;
   }

   public RxQuery(Query var1, Scheduler var2) {
      super(var2);
      this.query = var1;
   }

   @Experimental
   public Observable list() {
      return this.wrap(new Callable() {
         public List call() throws Exception {
            return RxQuery.this.query.forCurrentThread().list();
         }
      });
   }

   public Observable oneByOne() {
      return this.wrap(Observable.create(new OnSubscribe() {
         public void call(Subscriber var1) {
            Throwable var10000;
            label215: {
               LazyList var2;
               boolean var10001;
               try {
                  var2 = RxQuery.this.query.forCurrentThread().listLazyUncached();
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label215;
               }

               label216: {
                  label207: {
                     Iterator var3;
                     try {
                        var3 = var2.iterator();
                     } catch (Throwable var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label207;
                     }

                     while(true) {
                        Object var4;
                        try {
                           if (!var3.hasNext()) {
                              break label216;
                           }

                           var4 = var3.next();
                           if (var1.isUnsubscribed()) {
                              break label216;
                           }
                        } catch (Throwable var26) {
                           var10000 = var26;
                           var10001 = false;
                           break;
                        }

                        try {
                           var1.onNext(var4);
                        } catch (Throwable var25) {
                           var10000 = var25;
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  Throwable var30 = var10000;

                  try {
                     var2.close();
                     throw var30;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label215;
                  }
               }

               try {
                  var2.close();
                  if (!var1.isUnsubscribed()) {
                     var1.onCompleted();
                  }

                  return;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
               }
            }

            Throwable var29 = var10000;
            Exceptions.throwIfFatal(var29);
            var1.onError(var29);
         }
      }));
   }

   @Experimental
   public Observable unique() {
      return this.wrap(new Callable() {
         public Object call() throws Exception {
            return RxQuery.this.query.forCurrentThread().unique();
         }
      });
   }
}
