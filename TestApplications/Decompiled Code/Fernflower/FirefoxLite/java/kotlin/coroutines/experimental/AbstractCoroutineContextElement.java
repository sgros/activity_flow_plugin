package kotlin.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public abstract class AbstractCoroutineContextElement implements CoroutineContext.Element {
   private final CoroutineContext.Key key;

   public AbstractCoroutineContextElement(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      super();
      this.key = var1;
   }

   public Object fold(Object var1, Function2 var2) {
      Intrinsics.checkParameterIsNotNull(var2, "operation");
      return CoroutineContext.Element.DefaultImpls.fold(this, var1, var2);
   }

   public CoroutineContext.Element get(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return CoroutineContext.Element.DefaultImpls.get(this, var1);
   }

   public CoroutineContext.Key getKey() {
      return this.key;
   }

   public CoroutineContext minusKey(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return CoroutineContext.Element.DefaultImpls.minusKey(this, var1);
   }

   public CoroutineContext plus(CoroutineContext var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return CoroutineContext.Element.DefaultImpls.plus(this, var1);
   }
}
