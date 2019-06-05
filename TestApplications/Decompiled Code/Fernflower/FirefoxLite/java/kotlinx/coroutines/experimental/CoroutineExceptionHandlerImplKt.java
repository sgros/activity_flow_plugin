package kotlinx.coroutines.experimental;

import java.util.Iterator;
import java.util.ServiceLoader;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutineExceptionHandlerImplKt {
   public static final void handleCoroutineExceptionImpl(CoroutineContext var0, Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      ServiceLoader var2 = ServiceLoader.load(CoroutineExceptionHandler.class);
      Intrinsics.checkExpressionValueIsNotNull(var2, "ServiceLoader.load(Corou…ptionHandler::class.java)");
      Iterator var4 = ((Iterable)var2).iterator();

      while(var4.hasNext()) {
         ((CoroutineExceptionHandler)var4.next()).handleException(var0, var1);
      }

      Thread var3 = Thread.currentThread();
      Intrinsics.checkExpressionValueIsNotNull(var3, "currentThread");
      var3.getUncaughtExceptionHandler().uncaughtException(var3, var1);
   }
}
