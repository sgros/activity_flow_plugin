// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt
{
    public static final Integer toIntOrNull(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "receiver$0");
        return toIntOrNull(s, 10);
    }
    
    public static final Integer toIntOrNull(final String s, final int n) {
        Intrinsics.checkParameterIsNotNull(s, "receiver$0");
        CharsKt__CharJVMKt.checkRadix(n);
        int length = s.length();
        if (length == 0) {
            return null;
        }
        int i = 0;
        final int n2 = 0;
        final char char1 = s.charAt(0);
        int n3 = -2147483647;
        int index = 0;
        boolean b = false;
        Label_0093: {
            if (char1 < '0') {
                if (length == 1) {
                    return null;
                }
                if (char1 == '-') {
                    n3 = Integer.MIN_VALUE;
                    index = 1;
                    b = true;
                    break Label_0093;
                }
                if (char1 != '+') {
                    return null;
                }
                index = 1;
            }
            else {
                index = 0;
            }
            b = false;
        }
        final int n4 = n3 / n;
        --length;
        if (index <= length) {
            int n5 = n2;
            while (true) {
                final int digit = CharsKt__CharJVMKt.digitOf(s.charAt(index), n);
                if (digit < 0) {
                    return null;
                }
                if (n5 < n4) {
                    return null;
                }
                final int n6 = n5 * n;
                if (n6 < n3 + digit) {
                    return null;
                }
                final int n7 = i = n6 - digit;
                if (index == length) {
                    break;
                }
                ++index;
                n5 = n7;
            }
        }
        Integer n8;
        if (b) {
            n8 = i;
        }
        else {
            n8 = -i;
        }
        return n8;
    }
}
