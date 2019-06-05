package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
   public static final Iterable asIterable(final Sequence var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return (Iterable)(new Iterable() {
         public Iterator iterator() {
            return var0.iterator();
         }
      });
   }

   public static final Sequence map(Sequence var0, Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "transform");
      return (Sequence)(new TransformingSequence(var0, var1));
   }
}
