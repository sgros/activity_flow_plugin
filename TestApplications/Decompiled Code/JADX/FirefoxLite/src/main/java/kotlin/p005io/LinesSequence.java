package kotlin.p005io;

import java.io.BufferedReader;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* compiled from: ReadWrite.kt */
/* renamed from: kotlin.io.LinesSequence */
final class LinesSequence implements Sequence<String> {
    private final BufferedReader reader;

    public LinesSequence(BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "reader");
        this.reader = bufferedReader;
    }

    public Iterator<String> iterator() {
        return new LinesSequence$iterator$1(this);
    }
}
