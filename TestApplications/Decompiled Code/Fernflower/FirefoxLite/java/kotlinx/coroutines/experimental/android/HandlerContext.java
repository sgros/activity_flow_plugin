package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineDispatcher;

public final class HandlerContext extends CoroutineDispatcher {
   private final Handler handler;
   private final String name;

   public HandlerContext(Handler var1, String var2) {
      Intrinsics.checkParameterIsNotNull(var1, "handler");
      super();
      this.handler = var1;
      this.name = var2;
   }

   public void dispatch(CoroutineContext var1, Runnable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "block");
      this.handler.post(var2);
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof HandlerContext && ((HandlerContext)var1).handler == this.handler) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return System.identityHashCode(this.handler);
   }

   public String toString() {
      String var1 = this.name;
      if (var1 == null) {
         var1 = this.handler.toString();
         Intrinsics.checkExpressionValueIsNotNull(var1, "handler.toString()");
      }

      return var1;
   }
}
