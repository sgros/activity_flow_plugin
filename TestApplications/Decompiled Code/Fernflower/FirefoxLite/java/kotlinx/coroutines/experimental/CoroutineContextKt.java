package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicLong;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutineContextKt {
   private static final AtomicLong COROUTINE_ID;
   private static final boolean DEBUG;
   private static final CoroutineDispatcher DefaultDispatcher;

   static {
      String var0;
      try {
         var0 = System.getProperty("kotlinx.coroutines.debug");
      } catch (SecurityException var4) {
         var0 = null;
      }

      boolean var2;
      label45: {
         label44: {
            label43: {
               if (var0 != null) {
                  int var1 = var0.hashCode();
                  if (var1 == 0) {
                     if (!var0.equals("")) {
                        break label44;
                     }
                     break label43;
                  }

                  if (var1 == 3551) {
                     if (!var0.equals("on")) {
                        break label44;
                     }
                     break label43;
                  }

                  if (var1 == 109935) {
                     if (var0.equals("off")) {
                        var2 = false;
                        break label45;
                     }
                     break label44;
                  }

                  if (var1 != 3005871 || !var0.equals("auto")) {
                     break label44;
                  }
               }

               var2 = CoroutineId.class.desiredAssertionStatus();
               break label45;
            }

            var2 = true;
            break label45;
         }

         StringBuilder var3 = new StringBuilder();
         var3.append("System property 'kotlinx.coroutines.debug' has unrecognized value '");
         var3.append(var0);
         var3.append('\'');
         throw (Throwable)(new IllegalStateException(var3.toString().toString()));
      }

      DEBUG = var2;
      COROUTINE_ID = new AtomicLong();
      DefaultDispatcher = (CoroutineDispatcher)CommonPool.INSTANCE;
   }

   public static final String getCoroutineName(CoroutineContext var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      if (!DEBUG) {
         return null;
      } else {
         CoroutineId var1 = (CoroutineId)var0.get((CoroutineContext.Key)CoroutineId.Key);
         if (var1 == null) {
            return null;
         } else {
            String var4;
            label17: {
               CoroutineName var3 = (CoroutineName)var0.get((CoroutineContext.Key)CoroutineName.Key);
               if (var3 != null) {
                  var4 = var3.getName();
                  if (var4 != null) {
                     break label17;
                  }
               }

               var4 = "coroutine";
            }

            StringBuilder var2 = new StringBuilder();
            var2.append(var4);
            var2.append('#');
            var2.append(var1.getId());
            return var2.toString();
         }
      }
   }

   public static final boolean getDEBUG() {
      return DEBUG;
   }

   public static final CoroutineDispatcher getDefaultDispatcher() {
      return DefaultDispatcher;
   }

   public static final CoroutineContext newCoroutineContext(CoroutineContext var0, Job var1) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      CoroutineContext var2;
      if (DEBUG) {
         var2 = var0.plus((CoroutineContext)(new CoroutineId(COROUTINE_ID.incrementAndGet())));
      } else {
         var2 = var0;
      }

      if (var1 != null) {
         var2 = var2.plus((CoroutineContext)var1);
      }

      CoroutineContext var3 = var2;
      if (var0 != DefaultDispatcher) {
         var3 = var2;
         if (var0.get((CoroutineContext.Key)ContinuationInterceptor.Key) == null) {
            var3 = var2.plus((CoroutineContext)DefaultDispatcher);
         }
      }

      return var3;
   }

   public static final void restoreThreadContext(String var0) {
      if (var0 != null) {
         Thread var1 = Thread.currentThread();
         Intrinsics.checkExpressionValueIsNotNull(var1, "Thread.currentThread()");
         var1.setName(var0);
      }

   }

   public static final String updateThreadContext(CoroutineContext var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      if (!DEBUG) {
         return null;
      } else {
         CoroutineId var1 = (CoroutineId)var0.get((CoroutineContext.Key)CoroutineId.Key);
         if (var1 == null) {
            return null;
         } else {
            String var6;
            label17: {
               CoroutineName var5 = (CoroutineName)var0.get((CoroutineContext.Key)CoroutineName.Key);
               if (var5 != null) {
                  var6 = var5.getName();
                  if (var6 != null) {
                     break label17;
                  }
               }

               var6 = "coroutine";
            }

            Thread var2 = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(var2, "currentThread");
            String var3 = var2.getName();
            StringBuilder var4 = new StringBuilder(var3.length() + var6.length() + 10);
            var4.append(var3);
            var4.append(" @");
            var4.append(var6);
            var4.append('#');
            var4.append(var1.getId());
            var6 = var4.toString();
            Intrinsics.checkExpressionValueIsNotNull(var6, "StringBuilder(capacity).…builderAction).toString()");
            var2.setName(var6);
            return var3;
         }
      }
   }
}
