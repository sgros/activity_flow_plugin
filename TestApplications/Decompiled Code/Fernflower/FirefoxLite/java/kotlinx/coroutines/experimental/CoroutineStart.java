package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

public enum CoroutineStart {
   ATOMIC,
   DEFAULT,
   LAZY,
   UNDISPATCHED;

   static {
      CoroutineStart var0 = new CoroutineStart("DEFAULT", 0);
      DEFAULT = var0;
      CoroutineStart var1 = new CoroutineStart("LAZY", 1);
      LAZY = var1;
      CoroutineStart var2 = new CoroutineStart("ATOMIC", 2);
      ATOMIC = var2;
      CoroutineStart var3 = new CoroutineStart("UNDISPATCHED", 3);
      UNDISPATCHED = var3;
   }

   public final void invoke(Function2 var1, Object var2, Continuation var3) {
      // $FF: Couldn't be decompiled
   }

   public final boolean isLazy() {
      boolean var1;
      if ((CoroutineStart)this == LAZY) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
