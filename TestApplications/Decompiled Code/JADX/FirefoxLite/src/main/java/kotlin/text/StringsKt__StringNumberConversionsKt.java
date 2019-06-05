package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: StringNumberConversions.kt */
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    public static final Integer toIntOrNull(String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return toIntOrNull(str, 10);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0037 A:{LOOP_START, PHI: r2 r3 , LOOP:0: B:18:0x0037->B:28:0x004f} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0054  */
    public static final java.lang.Integer toIntOrNull(java.lang.String r9, int r10) {
        /*
        r0 = "receiver$0";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r0);
        kotlin.text.CharsKt__CharJVMKt.checkRadix(r10);
        r0 = r9.length();
        r1 = 0;
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return r1;
    L_0x0010:
        r2 = 0;
        r3 = r9.charAt(r2);
        r4 = 48;
        r5 = -2147483647; // 0xffffffff80000001 float:-1.4E-45 double:NaN;
        r6 = 1;
        if (r3 >= r4) goto L_0x0030;
    L_0x001d:
        if (r0 != r6) goto L_0x0020;
    L_0x001f:
        return r1;
    L_0x0020:
        r4 = 45;
        if (r3 != r4) goto L_0x0029;
    L_0x0024:
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3 = 1;
        r4 = 1;
        goto L_0x0032;
    L_0x0029:
        r4 = 43;
        if (r3 != r4) goto L_0x002f;
    L_0x002d:
        r3 = 1;
        goto L_0x0031;
    L_0x002f:
        return r1;
    L_0x0030:
        r3 = 0;
    L_0x0031:
        r4 = 0;
    L_0x0032:
        r7 = r5 / r10;
        r0 = r0 - r6;
        if (r3 > r0) goto L_0x0052;
    L_0x0037:
        r6 = r9.charAt(r3);
        r6 = kotlin.text.CharsKt__CharJVMKt.digitOf(r6, r10);
        if (r6 >= 0) goto L_0x0042;
    L_0x0041:
        return r1;
    L_0x0042:
        if (r2 >= r7) goto L_0x0045;
    L_0x0044:
        return r1;
    L_0x0045:
        r2 = r2 * r10;
        r8 = r5 + r6;
        if (r2 >= r8) goto L_0x004c;
    L_0x004b:
        return r1;
    L_0x004c:
        r2 = r2 - r6;
        if (r3 == r0) goto L_0x0052;
    L_0x004f:
        r3 = r3 + 1;
        goto L_0x0037;
    L_0x0052:
        if (r4 == 0) goto L_0x0059;
    L_0x0054:
        r9 = java.lang.Integer.valueOf(r2);
        goto L_0x005e;
    L_0x0059:
        r9 = -r2;
        r9 = java.lang.Integer.valueOf(r9);
    L_0x005e:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toIntOrNull(java.lang.String, int):java.lang.Integer");
    }
}
