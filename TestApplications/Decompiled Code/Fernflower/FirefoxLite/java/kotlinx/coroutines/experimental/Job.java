package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public interface Job extends CoroutineContext.Element {
   Job.Key Key = Job.Key.$$INSTANCE;

   DisposableHandle attachChild(Job var1);

   boolean cancel(Throwable var1);

   CancellationException getCancellationException();

   DisposableHandle invokeOnCompletion(boolean var1, boolean var2, Function1 var3);

   boolean isActive();

   boolean start();

   public static final class DefaultImpls {
      public static Object fold(Job var0, Object var1, Function2 var2) {
         Intrinsics.checkParameterIsNotNull(var2, "operation");
         return CoroutineContext.Element.DefaultImpls.fold((CoroutineContext.Element)var0, var1, var2);
      }

      public static CoroutineContext.Element get(Job var0, CoroutineContext.Key var1) {
         Intrinsics.checkParameterIsNotNull(var1, "key");
         return CoroutineContext.Element.DefaultImpls.get((CoroutineContext.Element)var0, var1);
      }

      public static CoroutineContext minusKey(Job var0, CoroutineContext.Key var1) {
         Intrinsics.checkParameterIsNotNull(var1, "key");
         return CoroutineContext.Element.DefaultImpls.minusKey((CoroutineContext.Element)var0, var1);
      }

      public static CoroutineContext plus(Job var0, CoroutineContext var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         return CoroutineContext.Element.DefaultImpls.plus((CoroutineContext.Element)var0, var1);
      }
   }

   public static final class Key implements CoroutineContext.Key {
      // $FF: synthetic field
      static final Job.Key $$INSTANCE = new Job.Key();

      static {
         CoroutineExceptionHandler.Key var0 = CoroutineExceptionHandler.Key;
      }

      private Key() {
      }
   }
}
