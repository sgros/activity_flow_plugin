package kotlinx.coroutines.experimental.android;

import android.support.annotation.Keep;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineExceptionHandler;

@Keep
public final class AndroidExceptionPreHandler extends AbstractCoroutineContextElement implements CoroutineExceptionHandler {
   public AndroidExceptionPreHandler() {
      super((CoroutineContext.Key)CoroutineExceptionHandler.Key);
   }

   public void handleException(CoroutineContext var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "exception");
      Method var4 = AndroidExceptionPreHandlerKt.access$getGetter$p();
      Object var5;
      if (var4 != null) {
         var5 = var4.invoke((Object)null);
      } else {
         var5 = null;
      }

      Object var3 = var5;
      if (!(var5 instanceof UncaughtExceptionHandler)) {
         var3 = null;
      }

      UncaughtExceptionHandler var6 = (UncaughtExceptionHandler)var3;
      if (var6 != null) {
         var6.uncaughtException(Thread.currentThread(), var2);
      }

   }
}
