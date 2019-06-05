package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor {
   public CoroutineDispatcher() {
      super((CoroutineContext.Key)ContinuationInterceptor.Key);
   }

   public abstract void dispatch(CoroutineContext var1, Runnable var2);

   public Continuation interceptContinuation(Continuation var1) {
      Intrinsics.checkParameterIsNotNull(var1, "continuation");
      return (Continuation)(new DispatchedContinuation(this, var1));
   }

   public boolean isDispatchNeeded(CoroutineContext var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(DebugKt.getClassSimpleName(this));
      var1.append('@');
      var1.append(DebugKt.getHexAddress(this));
      return var1.toString();
   }
}
