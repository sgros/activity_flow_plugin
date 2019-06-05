package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public final class TransformingSequence implements Sequence {
   private final Sequence sequence;
   private final Function1 transformer;

   public TransformingSequence(Sequence var1, Function1 var2) {
      Intrinsics.checkParameterIsNotNull(var1, "sequence");
      Intrinsics.checkParameterIsNotNull(var2, "transformer");
      super();
      this.sequence = var1;
      this.transformer = var2;
   }

   public Iterator iterator() {
      return (Iterator)(new Iterator() {
         private final Iterator iterator;

         {
            this.iterator = TransformingSequence.this.sequence.iterator();
         }

         public boolean hasNext() {
            return this.iterator.hasNext();
         }

         public Object next() {
            return TransformingSequence.this.transformer.invoke(this.iterator.next());
         }

         public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
         }
      });
   }
}
