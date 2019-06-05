// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.sequences;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt
{
    public static final <T> Iterable<T> asIterable(final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "receiver$0");
        return (Iterable<T>)new SequencesKt___SequencesKt$asIterable$$inlined$Iterable.SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1((Sequence)sequence);
    }
    
    public static final <T, R> Sequence<R> map(final Sequence<? extends T> sequence, final Function1<? super T, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(sequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return new TransformingSequence<Object, R>(sequence, function1);
    }
}
