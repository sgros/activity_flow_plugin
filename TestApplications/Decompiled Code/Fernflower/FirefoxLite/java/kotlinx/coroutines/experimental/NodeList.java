package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

public final class NodeList extends LockFreeLinkedListHead implements Incomplete {
   private static final AtomicIntegerFieldUpdater _active$FU = AtomicIntegerFieldUpdater.newUpdater(NodeList.class, "_active");
   private volatile int _active;

   public NodeList(boolean var1) {
      this._active = var1;
   }

   public NodeList getList() {
      return this;
   }

   public boolean isActive() {
      boolean var1;
      if (this._active != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("List");
      String var2;
      if (this.isActive()) {
         var2 = "{Active}";
      } else {
         var2 = "{New}";
      }

      var1.append(var2);
      var1.append("[");
      Object var6 = this.getNext();
      if (var6 != null) {
         LockFreeLinkedListNode var7 = (LockFreeLinkedListNode)var6;

         boolean var4;
         for(boolean var3 = true; Intrinsics.areEqual(var7, (LockFreeLinkedListHead)this) ^ true; var3 = var4) {
            var4 = var3;
            if (var7 instanceof JobNode) {
               JobNode var5 = (JobNode)var7;
               if (var3) {
                  var3 = false;
               } else {
                  var1.append(", ");
               }

               var1.append(var5);
               var4 = var3;
            }

            var7 = var7.getNextNode();
         }

         var1.append("]");
         var2 = var1.toString();
         Intrinsics.checkExpressionValueIsNotNull(var2, "StringBuilder().apply(builderAction).toString()");
         return var2;
      } else {
         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
      }
   }

   public final int tryMakeActive() {
      if (this._active != 0) {
         return 0;
      } else {
         return _active$FU.compareAndSet(this, 0, 1) ? 1 : -1;
      }
   }
}
