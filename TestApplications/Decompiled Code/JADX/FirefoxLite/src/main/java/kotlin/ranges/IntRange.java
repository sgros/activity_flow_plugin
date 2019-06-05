package kotlin.ranges;

/* compiled from: Ranges.kt */
public final class IntRange extends IntProgression {
    public static final Companion Companion = new Companion();
    private static final IntRange EMPTY = new IntRange(1, 0);

    /* compiled from: Ranges.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final IntRange getEMPTY() {
            return IntRange.EMPTY;
        }
    }

    public IntRange(int i, int i2) {
        super(i, i2, 1);
    }

    public Integer getStart() {
        return Integer.valueOf(getFirst());
    }

    public Integer getEndInclusive() {
        return Integer.valueOf(getLast());
    }

    public boolean isEmpty() {
        return getFirst() > getLast();
    }

    /* JADX WARNING: Missing block: B:9:0x0027, code skipped:
            if (getLast() == r3.getLast()) goto L_0x0029;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        r0 = r3 instanceof kotlin.ranges.IntRange;
        if (r0 == 0) goto L_0x002b;
    L_0x0004:
        r0 = r2.isEmpty();
        if (r0 == 0) goto L_0x0013;
    L_0x000a:
        r0 = r3;
        r0 = (kotlin.ranges.IntRange) r0;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0029;
    L_0x0013:
        r0 = r2.getFirst();
        r3 = (kotlin.ranges.IntRange) r3;
        r1 = r3.getFirst();
        if (r0 != r1) goto L_0x002b;
    L_0x001f:
        r0 = r2.getLast();
        r3 = r3.getLast();
        if (r0 != r3) goto L_0x002b;
    L_0x0029:
        r3 = 1;
        goto L_0x002c;
    L_0x002b:
        r3 = 0;
    L_0x002c:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.ranges.IntRange.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return isEmpty() ? -1 : (getFirst() * 31) + getLast();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getFirst());
        stringBuilder.append("..");
        stringBuilder.append(getLast());
        return stringBuilder.toString();
    }
}
