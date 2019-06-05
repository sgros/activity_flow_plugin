package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

public final class Symbol {
   private final String symbol;

   public Symbol(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "symbol");
      super();
      this.symbol = var1;
   }

   public String toString() {
      return this.symbol;
   }
}
