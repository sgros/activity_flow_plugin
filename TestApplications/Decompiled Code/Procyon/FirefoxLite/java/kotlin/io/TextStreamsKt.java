// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.io;

import java.util.ArrayList;
import java.util.List;
import kotlin.sequences.Sequence;
import java.util.Iterator;
import java.io.Closeable;
import java.io.BufferedReader;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.io.Reader;

public final class TextStreamsKt
{
    public static final void forEachLine(Reader in, final Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(in, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        BufferedReader bufferedReader;
        if (in instanceof BufferedReader) {
            bufferedReader = (BufferedReader)in;
        }
        else {
            bufferedReader = new BufferedReader(in, 8192);
        }
        final BufferedReader bufferedReader2 = bufferedReader;
        final Object o = in = null;
        try {
            try {
                final Iterator<String> iterator = lineSequence(bufferedReader2).iterator();
                while (true) {
                    in = (Reader)o;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    in = (Reader)o;
                    function1.invoke(iterator.next());
                }
                in = (Reader)o;
                final Unit instance = Unit.INSTANCE;
                CloseableKt.closeFinally(bufferedReader2, (Throwable)o);
                return;
            }
            finally {}
        }
        catch (Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(bufferedReader2, (Throwable)in);
    }
    
    public static final Sequence<String> lineSequence(final BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "receiver$0");
        return SequencesKt__SequencesKt.constrainOnce((Sequence<? extends String>)new LinesSequence(bufferedReader));
    }
    
    public static final List<String> readLines(final Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "receiver$0");
        final ArrayList<String> list = new ArrayList<String>();
        forEachLine(reader, (Function1<? super String, Unit>)new TextStreamsKt$readLines.TextStreamsKt$readLines$1((ArrayList)list));
        return list;
    }
}
