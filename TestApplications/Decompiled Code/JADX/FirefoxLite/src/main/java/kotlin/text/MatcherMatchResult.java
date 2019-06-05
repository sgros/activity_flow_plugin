package kotlin.text;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Regex.kt */
final class MatcherMatchResult implements MatchResult {
    private final MatchGroupCollection groups = new MatcherMatchResult$groups$1(this);
    private final CharSequence input;
    private final MatchResult matchResult = this.matcher.toMatchResult();
    private final Matcher matcher;

    public MatcherMatchResult(Matcher matcher, CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(matcher, "matcher");
        Intrinsics.checkParameterIsNotNull(charSequence, "input");
        this.matcher = matcher;
        this.input = charSequence;
    }

    public MatchGroupCollection getGroups() {
        return this.groups;
    }
}
