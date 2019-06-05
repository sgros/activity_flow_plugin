package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

class LazyKt__LazyJVMKt {
   public static final Lazy lazy(Function0 var0) {
      Intrinsics.checkParameterIsNotNull(var0, "initializer");
      return (Lazy)(new SynchronizedLazyImpl(var0, (Object)null, 2, (DefaultConstructorMarker)null));
   }
}
