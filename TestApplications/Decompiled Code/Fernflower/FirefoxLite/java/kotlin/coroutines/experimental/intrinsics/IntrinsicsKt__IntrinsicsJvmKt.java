package kotlin.coroutines.experimental.intrinsics;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

class IntrinsicsKt__IntrinsicsJvmKt {
   public static final Continuation createCoroutineUnchecked(final Function2 var0, final Object var1, final Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var2, "completion");
      Continuation var3;
      if (!(var0 instanceof CoroutineImpl)) {
         var3 = new Continuation() {
            public CoroutineContext getContext() {
               return var2.getContext();
            }

            public void resume(Unit var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "value");
               Continuation var8 = var2;

               Throwable var10000;
               label42: {
                  Function2 var2x;
                  boolean var10001;
                  try {
                     var2x = var0;
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label42;
                  }

                  TypeCastException var10;
                  if (var2x != null) {
                     label38: {
                        Object var9;
                        try {
                           var9 = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(var2x, 2)).invoke(var1, var2);
                           if (var9 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                              return;
                           }
                        } catch (Throwable var6) {
                           var10000 = var6;
                           var10001 = false;
                           break label38;
                        }

                        if (var8 != null) {
                           try {
                              var8.resume(var9);
                              return;
                           } catch (Throwable var5) {
                              var10000 = var5;
                              var10001 = false;
                           }
                        } else {
                           try {
                              var10 = new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                              throw var10;
                           } catch (Throwable var3) {
                              var10000 = var3;
                              var10001 = false;
                           }
                        }
                     }
                  } else {
                     try {
                        var10 = new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
                        throw var10;
                     } catch (Throwable var4) {
                        var10000 = var4;
                        var10001 = false;
                     }
                  }
               }

               Throwable var11 = var10000;
               var8.resumeWithException(var11);
            }

            public void resumeWithException(Throwable var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "exception");
               var2.resumeWithException(var1x);
            }
         };
         var3 = CoroutineIntrinsics.interceptContinuationIfNeeded(var2.getContext(), (Continuation)var3);
      } else {
         var3 = ((CoroutineImpl)var0).create(var1, var2);
         if (var3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
         }

         var3 = ((CoroutineImpl)var3).getFacade();
      }

      return var3;
   }

   public static final Object getCOROUTINE_SUSPENDED() {
      return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
   }
}
