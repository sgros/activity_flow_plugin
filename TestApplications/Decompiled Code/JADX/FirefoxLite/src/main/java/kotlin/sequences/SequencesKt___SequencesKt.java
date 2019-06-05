package kotlin.sequences;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: _Sequences.kt */
class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
    public static final <T, R> Sequence<R> map(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(sequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return new TransformingSequence(sequence, function1);
    }

    public static final <T> Iterable<T> asIterable(Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "receiver$0");
        return new SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1(sequence);
    }
}
