// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.io;

import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import java.io.BufferedReader;
import kotlin.sequences.Sequence;

final class LinesSequence implements Sequence<String>
{
    private final BufferedReader reader;
    
    public LinesSequence(final BufferedReader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "reader");
        this.reader = reader;
    }
    
    @Override
    public Iterator<String> iterator() {
        return (Iterator<String>)new LinesSequence$iterator.LinesSequence$iterator$1(this);
    }
}
