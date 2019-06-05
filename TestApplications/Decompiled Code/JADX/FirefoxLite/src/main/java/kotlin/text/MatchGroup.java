package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* compiled from: Regex.kt */
public final class MatchGroup {
    private final IntRange range;
    private final String value;

    /* JADX WARNING: Missing block: B:6:0x001a, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r2.range, r3.range) != false) goto L_0x001f;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        if (r2 == r3) goto L_0x001f;
    L_0x0002:
        r0 = r3 instanceof kotlin.text.MatchGroup;
        if (r0 == 0) goto L_0x001d;
    L_0x0006:
        r3 = (kotlin.text.MatchGroup) r3;
        r0 = r2.value;
        r1 = r3.value;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x001d;
    L_0x0012:
        r0 = r2.range;
        r3 = r3.range;
        r3 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r3);
        if (r3 == 0) goto L_0x001d;
    L_0x001c:
        goto L_0x001f;
    L_0x001d:
        r3 = 0;
        return r3;
    L_0x001f:
        r3 = 1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.MatchGroup.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        String str = this.value;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        IntRange intRange = this.range;
        if (intRange != null) {
            i = intRange.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MatchGroup(value=");
        stringBuilder.append(this.value);
        stringBuilder.append(", range=");
        stringBuilder.append(this.range);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public MatchGroup(String str, IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(str, "value");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        this.value = str;
        this.range = intRange;
    }

    public final String getValue() {
        return this.value;
    }
}
