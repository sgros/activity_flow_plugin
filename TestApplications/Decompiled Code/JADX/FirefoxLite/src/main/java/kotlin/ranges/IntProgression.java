package kotlin.ranges;

import kotlin.collections.IntIterator;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: Progressions.kt */
public class IntProgression implements Iterable<Integer>, KMappedMarker {
    public static final Companion Companion = new Companion();
    private final int first;
    private final int last;
    private final int step;

    /* compiled from: Progressions.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final IntProgression fromClosedRange(int i, int i2, int i3) {
            return new IntProgression(i, i2, i3);
        }
    }

    public IntProgression(int i, int i2, int i3) {
        if (i3 == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        } else if (i3 != Integer.MIN_VALUE) {
            this.first = i;
            this.last = ProgressionUtilKt.getProgressionLastElement(i, i2, i3);
            this.step = i3;
        } else {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
    }

    public final int getFirst() {
        return this.first;
    }

    public final int getLast() {
        return this.last;
    }

    public final int getStep() {
        return this.step;
    }

    public IntIterator iterator() {
        return new IntProgressionIterator(this.first, this.last, this.step);
    }

    public boolean isEmpty() {
        if (this.step > 0) {
            if (this.first <= this.last) {
                return false;
            }
        } else if (this.first >= this.last) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:11:0x0025, code skipped:
            if (r2.step == r3.step) goto L_0x0027;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        r0 = r3 instanceof kotlin.ranges.IntProgression;
        if (r0 == 0) goto L_0x0029;
    L_0x0004:
        r0 = r2.isEmpty();
        if (r0 == 0) goto L_0x0013;
    L_0x000a:
        r0 = r3;
        r0 = (kotlin.ranges.IntProgression) r0;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0027;
    L_0x0013:
        r0 = r2.first;
        r3 = (kotlin.ranges.IntProgression) r3;
        r1 = r3.first;
        if (r0 != r1) goto L_0x0029;
    L_0x001b:
        r0 = r2.last;
        r1 = r3.last;
        if (r0 != r1) goto L_0x0029;
    L_0x0021:
        r0 = r2.step;
        r3 = r3.step;
        if (r0 != r3) goto L_0x0029;
    L_0x0027:
        r3 = 1;
        goto L_0x002a;
    L_0x0029:
        r3 = 0;
    L_0x002a:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.ranges.IntProgression.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return isEmpty() ? -1 : (((this.first * 31) + this.last) * 31) + this.step;
    }

    public String toString() {
        StringBuilder stringBuilder;
        int i;
        if (this.step > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.first);
            stringBuilder.append("..");
            stringBuilder.append(this.last);
            stringBuilder.append(" step ");
            i = this.step;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.first);
            stringBuilder.append(" downTo ");
            stringBuilder.append(this.last);
            stringBuilder.append(" step ");
            i = -this.step;
        }
        stringBuilder.append(i);
        return stringBuilder.toString();
    }
}
