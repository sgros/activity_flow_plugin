package kotlin.text;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Regex.kt */
public final class Regex implements Serializable {
    public static final Companion Companion = new Companion();
    private final Pattern nativePattern;

    /* compiled from: Regex.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public Regex(Pattern pattern) {
        Intrinsics.checkParameterIsNotNull(pattern, "nativePattern");
        this.nativePattern = pattern;
    }

    public Regex(String str) {
        Intrinsics.checkParameterIsNotNull(str, "pattern");
        Pattern compile = Pattern.compile(str);
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern)");
        this(compile);
    }

    public static /* synthetic */ MatchResult find$default(Regex regex, CharSequence charSequence, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return regex.find(charSequence, i);
    }

    public final MatchResult find(CharSequence charSequence, int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "input");
        Matcher matcher = this.nativePattern.matcher(charSequence);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.findNext(matcher, i, charSequence);
    }

    public final String replace(CharSequence charSequence, String str) {
        Intrinsics.checkParameterIsNotNull(charSequence, "input");
        Intrinsics.checkParameterIsNotNull(str, "replacement");
        String replaceAll = this.nativePattern.matcher(charSequence).replaceAll(str);
        Intrinsics.checkExpressionValueIsNotNull(replaceAll, "nativePattern.matcher(in…).replaceAll(replacement)");
        return replaceAll;
    }

    public String toString() {
        String pattern = this.nativePattern.toString();
        Intrinsics.checkExpressionValueIsNotNull(pattern, "nativePattern.toString()");
        return pattern;
    }
}
