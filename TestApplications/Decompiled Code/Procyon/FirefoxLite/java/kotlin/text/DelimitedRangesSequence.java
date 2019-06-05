// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;

final class DelimitedRangesSequence implements Sequence<IntRange>
{
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;
    
    public DelimitedRangesSequence(final CharSequence input, final int startIndex, final int limit, final Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> getNextMatch) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(getNextMatch, "getNextMatch");
        this.input = input;
        this.startIndex = startIndex;
        this.limit = limit;
        this.getNextMatch = (Function2<CharSequence, Integer, Pair<Integer, Integer>>)getNextMatch;
    }
    
    @Override
    public Iterator<IntRange> iterator() {
        return (Iterator<IntRange>)new DelimitedRangesSequence$iterator.DelimitedRangesSequence$iterator$1(this);
    }
}
