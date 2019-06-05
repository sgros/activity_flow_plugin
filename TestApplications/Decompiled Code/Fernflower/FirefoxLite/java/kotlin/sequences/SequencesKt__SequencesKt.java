package kotlin.sequences;

import kotlin.jvm.internal.Intrinsics;

class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt {
   public static final Sequence constrainOnce(Sequence var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (!(var0 instanceof ConstrainedOnceSequence)) {
         var0 = (Sequence)(new ConstrainedOnceSequence(var0));
      }

      return var0;
   }
}
