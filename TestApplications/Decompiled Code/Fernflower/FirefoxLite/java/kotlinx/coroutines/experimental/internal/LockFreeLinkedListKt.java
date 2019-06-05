package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

public final class LockFreeLinkedListKt {
   private static final Object ALREADY_REMOVED = new Symbol("ALREADY_REMOVED");
   private static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");
   private static final Object LIST_EMPTY = new Symbol("LIST_EMPTY");
   private static final Object REMOVE_PREPARED = new Symbol("REMOVE_PREPARED");

   public static final Object getCONDITION_FALSE() {
      return CONDITION_FALSE;
   }

   public static final LockFreeLinkedListNode unwrap(Object var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Object var1;
      if (!(var0 instanceof Removed)) {
         var1 = null;
      } else {
         var1 = var0;
      }

      Removed var3 = (Removed)var1;
      LockFreeLinkedListNode var2;
      if (var3 != null) {
         LockFreeLinkedListNode var4 = var3.ref;
         if (var4 != null) {
            var2 = var4;
            return var2;
         }
      }

      var2 = (LockFreeLinkedListNode)var0;
      return var2;
   }
}
