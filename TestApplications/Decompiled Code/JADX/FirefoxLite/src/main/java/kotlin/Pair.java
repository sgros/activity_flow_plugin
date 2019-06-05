package kotlin;

import java.io.Serializable;

/* compiled from: Tuples.kt */
public final class Pair<A, B> implements Serializable {
    private final A first;
    private final B second;

    public final A component1() {
        return this.first;
    }

    public final B component2() {
        return this.second;
    }

    /* JADX WARNING: Missing block: B:6:0x001a, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r2.second, r3.second) != false) goto L_0x001f;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        if (r2 == r3) goto L_0x001f;
    L_0x0002:
        r0 = r3 instanceof kotlin.Pair;
        if (r0 == 0) goto L_0x001d;
    L_0x0006:
        r3 = (kotlin.Pair) r3;
        r0 = r2.first;
        r1 = r3.first;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x001d;
    L_0x0012:
        r0 = r2.second;
        r3 = r3.second;
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
        throw new UnsupportedOperationException("Method not decompiled: kotlin.Pair.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        Object obj = this.first;
        int i = 0;
        int hashCode = (obj != null ? obj.hashCode() : 0) * 31;
        Object obj2 = this.second;
        if (obj2 != null) {
            i = obj2.hashCode();
        }
        return hashCode + i;
    }

    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        stringBuilder.append(this.first);
        stringBuilder.append(", ");
        stringBuilder.append(this.second);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
