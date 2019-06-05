package kotlin.p005io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* compiled from: ReadWrite.kt */
/* renamed from: kotlin.io.TextStreamsKt */
public final class TextStreamsKt {
    public static final List<String> readLines(Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "receiver$0");
        ArrayList arrayList = new ArrayList();
        TextStreamsKt.forEachLine(reader, new TextStreamsKt$readLines$1(arrayList));
        return arrayList;
    }

    public static final Sequence<String> lineSequence(BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "receiver$0");
        return SequencesKt__SequencesKt.constrainOnce(new LinesSequence(bufferedReader));
    }

    public static final void forEachLine(Reader reader, Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(reader, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Closeable bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, 8192);
        Throwable th = (Throwable) null;
        try {
            for (Object invoke : TextStreamsKt.lineSequence((BufferedReader) bufferedReader)) {
                function1.invoke(invoke);
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(bufferedReader, th);
        } catch (Throwable th2) {
            CloseableKt.closeFinally(bufferedReader, th);
        }
    }
}
