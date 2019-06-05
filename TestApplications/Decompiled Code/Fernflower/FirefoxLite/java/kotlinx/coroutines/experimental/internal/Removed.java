package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

final class Removed {
   public final LockFreeLinkedListNode ref;

   public Removed(LockFreeLinkedListNode var1) {
      Intrinsics.checkParameterIsNotNull(var1, "ref");
      super();
      this.ref = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Removed[");
      var1.append(this.ref);
      var1.append(']');
      return var1.toString();
   }
}
