package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;

public interface DispatchedTask extends Runnable {
   Continuation getDelegate();

   Throwable getExceptionalResult(Object var1);

   int getResumeMode();

   Object getSuccessfulResult(Object var1);

   Object takeState();

   public static final class DefaultImpls {
      public static Throwable getExceptionalResult(DispatchedTask var0, Object var1) {
         boolean var2 = var1 instanceof CompletedExceptionally;
         Throwable var3 = null;
         if (!var2) {
            var1 = null;
         }

         CompletedExceptionally var4 = (CompletedExceptionally)var1;
         if (var4 != null) {
            var3 = var4.cause;
         }

         return var3;
      }

      public static Object getSuccessfulResult(DispatchedTask var0, Object var1) {
         return var1;
      }

      public static void run(DispatchedTask var0) {
         Throwable var73;
         Throwable var10000;
         label536: {
            Continuation var1;
            boolean var10001;
            try {
               var1 = var0.getDelegate();
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label536;
            }

            if (var1 != null) {
               label542: {
                  Continuation var2;
                  CoroutineContext var3;
                  Job var71;
                  label528: {
                     try {
                        var2 = ((DispatchedContinuation)var1).continuation;
                        var3 = var2.getContext();
                        if (ResumeModeKt.isCancellableMode(var0.getResumeMode())) {
                           var71 = (Job)var3.get((CoroutineContext.Key)Job.Key);
                           break label528;
                        }
                     } catch (Throwable var68) {
                        var10000 = var68;
                        var10001 = false;
                        break label542;
                     }

                     var71 = null;
                  }

                  Object var4;
                  String var75;
                  try {
                     var4 = var0.takeState();
                     var75 = CoroutineContextKt.updateThreadContext(var3);
                  } catch (Throwable var67) {
                     var10000 = var67;
                     var10001 = false;
                     break label542;
                  }

                  label539: {
                     label515: {
                        label543: {
                           if (var71 != null) {
                              try {
                                 if (!var71.isActive()) {
                                    var2.resumeWithException((Throwable)var71.getCancellationException());
                                    break label543;
                                 }
                              } catch (Throwable var66) {
                                 var10000 = var66;
                                 var10001 = false;
                                 break label515;
                              }
                           }

                           try {
                              var73 = var0.getExceptionalResult(var4);
                           } catch (Throwable var65) {
                              var10000 = var65;
                              var10001 = false;
                              break label515;
                           }

                           if (var73 != null) {
                              try {
                                 var2.resumeWithException(var73);
                              } catch (Throwable var64) {
                                 var10000 = var64;
                                 var10001 = false;
                                 break label515;
                              }
                           } else {
                              try {
                                 var2.resume(var0.getSuccessfulResult(var4));
                              } catch (Throwable var63) {
                                 var10000 = var63;
                                 var10001 = false;
                                 break label515;
                              }
                           }
                        }

                        label498:
                        try {
                           Unit var74 = Unit.INSTANCE;
                           break label539;
                        } catch (Throwable var62) {
                           var10000 = var62;
                           var10001 = false;
                           break label498;
                        }
                     }

                     var73 = var10000;

                     try {
                        CoroutineContextKt.restoreThreadContext(var75);
                        throw var73;
                     } catch (Throwable var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label542;
                     }
                  }

                  try {
                     CoroutineContextKt.restoreThreadContext(var75);
                     return;
                  } catch (Throwable var61) {
                     var10000 = var61;
                     var10001 = false;
                  }
               }
            } else {
               try {
                  TypeCastException var76 = new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.DispatchedContinuation<T>");
                  throw var76;
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
               }
            }
         }

         var73 = var10000;
         StringBuilder var72 = new StringBuilder();
         var72.append("Unexpected exception running ");
         var72.append(var0);
         throw (Throwable)(new DispatchException(var72.toString(), var73));
      }
   }
}
