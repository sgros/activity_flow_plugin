package kotlin.sequences;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.Intrinsics;

public final class ConstrainedOnceSequence implements Sequence {
   private final AtomicReference sequenceRef;

   public ConstrainedOnceSequence(Sequence var1) {
      Intrinsics.checkParameterIsNotNull(var1, "sequence");
      super();
      this.sequenceRef = new AtomicReference(var1);
   }

   public Iterator iterator() {
      Sequence var1 = (Sequence)this.sequenceRef.getAndSet((Object)null);
      if (var1 != null) {
         return var1.iterator();
      } else {
         throw (Throwable)(new IllegalStateException("This sequence can be consumed only once."));
      }
   }
}
