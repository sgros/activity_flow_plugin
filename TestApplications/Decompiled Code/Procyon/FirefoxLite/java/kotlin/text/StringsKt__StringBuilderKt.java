// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;

class StringsKt__StringBuilderKt extends StringsKt__StringBuilderJVMKt
{
    public static final <T> void appendElement(final Appendable appendable, final T obj, final Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(appendable, "receiver$0");
        if (function1 != null) {
            appendable.append((CharSequence)function1.invoke(obj));
        }
        else if (obj == null || obj instanceof CharSequence) {
            appendable.append((CharSequence)obj);
        }
        else if (obj instanceof Character) {
            appendable.append((char)obj);
        }
        else {
            appendable.append(String.valueOf(obj));
        }
    }
}
