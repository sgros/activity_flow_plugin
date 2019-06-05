package kotlin.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class EmptyCoroutineContext implements CoroutineContext {
   public static final EmptyCoroutineContext INSTANCE = new EmptyCoroutineContext();

   private EmptyCoroutineContext() {
   }

   public Object fold(Object var1, Function2 var2) {
      Intrinsics.checkParameterIsNotNull(var2, "operation");
      return var1;
   }

   public CoroutineContext.Element get(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return null;
   }

   public int hashCode() {
      return 0;
   }

   public CoroutineContext minusKey(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return (CoroutineContext)this;
   }

   public CoroutineContext plus(CoroutineContext var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return var1;
   }

   public String toString() {
      return "EmptyCoroutineContext";
   }
}
