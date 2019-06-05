// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import java.util.regex.Matcher;
import kotlin.jvm.internal.Intrinsics;
import java.util.regex.Pattern;
import java.io.Serializable;

public final class Regex implements Serializable
{
    public static final Companion Companion;
    private final Pattern nativePattern;
    
    static {
        Companion = new Companion(null);
    }
    
    public Regex(final String regex) {
        Intrinsics.checkParameterIsNotNull(regex, "pattern");
        final Pattern compile = Pattern.compile(regex);
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern)");
        this(compile);
    }
    
    public Regex(final Pattern nativePattern) {
        Intrinsics.checkParameterIsNotNull(nativePattern, "nativePattern");
        this.nativePattern = nativePattern;
    }
    
    public final MatchResult find(final CharSequence input, final int n) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        final Matcher matcher = this.nativePattern.matcher(input);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.access$findNext(matcher, n, input);
    }
    
    public final String replace(final CharSequence input, final String replacement) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        final String replaceAll = this.nativePattern.matcher(input).replaceAll(replacement);
        Intrinsics.checkExpressionValueIsNotNull(replaceAll, "nativePattern.matcher(in\u2026).replaceAll(replacement)");
        return replaceAll;
    }
    
    @Override
    public String toString() {
        final String string = this.nativePattern.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "nativePattern.toString()");
        return string;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
