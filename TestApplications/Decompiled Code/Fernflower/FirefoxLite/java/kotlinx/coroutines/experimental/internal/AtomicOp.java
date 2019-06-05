package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class AtomicOp extends OpDescriptor {
   private static final AtomicReferenceFieldUpdater _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
   private volatile Object _consensus = AtomicKt.access$getNO_DECISION$p();

   private final Object decide(Object var1) {
      if (!this.tryDecide(var1)) {
         var1 = this._consensus;
      }

      return var1;
   }

   public abstract void complete(Object var1, Object var2);

   public final Object perform(Object var1) {
      Object var2 = this._consensus;
      Object var3 = var2;
      if (var2 == AtomicKt.access$getNO_DECISION$p()) {
         var3 = this.decide(this.prepare(var1));
      }

      this.complete(var1, var3);
      return var3;
   }

   public abstract Object prepare(Object var1);

   public final boolean tryDecide(Object var1) {
      boolean var2;
      if (var1 != AtomicKt.access$getNO_DECISION$p()) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         return _consensus$FU.compareAndSet(this, AtomicKt.access$getNO_DECISION$p(), var1);
      } else {
         throw (Throwable)(new IllegalStateException("Check failed.".toString()));
      }
   }
}
