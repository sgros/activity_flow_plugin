// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.ranges.IntRange;
import java.util.regex.Matcher;

public final class RegexKt
{
    private static final MatchResult findNext(final Matcher matcher, final int start, final CharSequence charSequence) {
        MatchResult matchResult;
        if (!matcher.find(start)) {
            matchResult = null;
        }
        else {
            matchResult = new MatcherMatchResult(matcher, charSequence);
        }
        return matchResult;
    }
    
    private static final IntRange range(final java.util.regex.MatchResult matchResult, final int n) {
        return RangesKt___RangesKt.until(matchResult.start(n), matchResult.end(n));
    }
}
