// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import java.util.Iterator;
import kotlin.collections.IntIterator;
import java.util.Collection;
import kotlin.ranges.IntRange;
import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt
{
    public static final boolean isBlank(final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        final int length = charSequence.length();
        boolean b = false;
        if (length != 0) {
            final IntRange intRange = StringsKt__StringsKt.getIndices(charSequence);
            boolean b2 = false;
            Label_0088: {
                if (!(intRange instanceof Collection) || !((Collection<Object>)intRange).isEmpty()) {
                    final Iterator<Object> iterator = ((Iterable<Object>)intRange).iterator();
                    while (iterator.hasNext()) {
                        if (!CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(((IntIterator)iterator).nextInt()))) {
                            b2 = false;
                            break Label_0088;
                        }
                    }
                }
                b2 = true;
            }
            if (!b2) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    public static final boolean regionMatches(final String s, final int n, final String s2, final int n2, final int n3, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull(s, "receiver$0");
        Intrinsics.checkParameterIsNotNull(s2, "other");
        boolean b;
        if (!ignoreCase) {
            b = s.regionMatches(n, s2, n2, n3);
        }
        else {
            b = s.regionMatches(ignoreCase, n, s2, n2, n3);
        }
        return b;
    }
    
    public static final boolean startsWith(final String s, final String prefix, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "receiver$0");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!b) {
            return s.startsWith(prefix);
        }
        return regionMatches(s, 0, prefix, 0, prefix.length(), b);
    }
}
