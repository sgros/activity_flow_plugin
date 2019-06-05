package kotlin.text;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.ranges.IntRange;

/* compiled from: Regex.kt */
public final class RegexKt {
    private static final MatchResult findNext(Matcher matcher, int i, CharSequence charSequence) {
        return !matcher.find(i) ? null : new MatcherMatchResult(matcher, charSequence);
    }

    private static final IntRange range(MatchResult matchResult, int i) {
        return RangesKt___RangesKt.until(matchResult.start(i), matchResult.end(i));
    }
}
