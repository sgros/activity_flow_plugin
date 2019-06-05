package kotlin.text;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: StringsJVM.kt */
class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
    public static /* synthetic */ boolean startsWith$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return startsWith(str, str2, z);
    }

    public static final boolean startsWith(String str, String str2, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str2, "prefix");
        if (!z) {
            return str.startsWith(str2);
        }
        return regionMatches(str, 0, str2, 0, str2.length(), z);
    }

    public static final boolean isBlank(CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        if (charSequence.length() != 0) {
            Object obj;
            Iterable indices = StringsKt__StringsKt.getIndices(charSequence);
            if (!((indices instanceof Collection) && ((Collection) indices).isEmpty())) {
                Iterator it = indices.iterator();
                while (it.hasNext()) {
                    if (!CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(((IntIterator) it).nextInt()))) {
                        obj = null;
                        break;
                    }
                }
            }
            obj = 1;
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    public static final boolean regionMatches(String str, int i, String str2, int i2, int i3, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str2, "other");
        if (z) {
            return str.regionMatches(z, i, str2, i2, i3);
        }
        return str.regionMatches(i, str2, i2, i3);
    }
}
