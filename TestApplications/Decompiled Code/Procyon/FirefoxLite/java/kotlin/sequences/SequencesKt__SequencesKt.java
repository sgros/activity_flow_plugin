// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.sequences;

import kotlin.jvm.internal.Intrinsics;

class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt
{
    public static final <T> Sequence<T> constrainOnce(Sequence<? extends T> constrainedOnceSequence) {
        Intrinsics.checkParameterIsNotNull(constrainedOnceSequence, "receiver$0");
        if (!(constrainedOnceSequence instanceof ConstrainedOnceSequence)) {
            constrainedOnceSequence = new ConstrainedOnceSequence<T>(constrainedOnceSequence);
        }
        return constrainedOnceSequence;
    }
}
