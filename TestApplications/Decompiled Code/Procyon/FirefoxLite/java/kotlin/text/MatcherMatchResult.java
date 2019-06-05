// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import java.util.regex.Matcher;

final class MatcherMatchResult implements MatchResult
{
    private final MatchGroupCollection groups;
    private final CharSequence input;
    private final java.util.regex.MatchResult matchResult;
    private final Matcher matcher;
    
    public MatcherMatchResult(final Matcher matcher, final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(matcher, "matcher");
        Intrinsics.checkParameterIsNotNull(input, "input");
        this.matcher = matcher;
        this.input = input;
        this.matchResult = this.matcher.toMatchResult();
        this.groups = (MatchGroupCollection)new MatcherMatchResult$groups.MatcherMatchResult$groups$1(this);
    }
    
    @Override
    public MatchGroupCollection getGroups() {
        return this.groups;
    }
}
