package kotlin.ranges;

/* compiled from: _Ranges.kt */
class RangesKt___RangesKt extends RangesKt__RangesKt {
    public static final int coerceAtLeast(int i, int i2) {
        return i < i2 ? i2 : i;
    }

    public static final long coerceAtLeast(long j, long j2) {
        return j < j2 ? j2 : j;
    }

    public static final int coerceAtMost(int i, int i2) {
        return i > i2 ? i2 : i;
    }

    public static final long coerceAtMost(long j, long j2) {
        return j > j2 ? j2 : j;
    }

    public static final IntProgression downTo(int i, int i2) {
        return IntProgression.Companion.fromClosedRange(i, i2, -1);
    }

    public static final IntRange until(int i, int i2) {
        if (i2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(i, i2 - 1);
    }

    public static final int coerceIn(int i, int i2, int i3) {
        if (i2 > i3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot coerce value to an empty range: maximum ");
            stringBuilder.append(i3);
            stringBuilder.append(" is less than minimum ");
            stringBuilder.append(i2);
            stringBuilder.append('.');
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (i < i2) {
            return i2;
        } else {
            return i > i3 ? i3 : i;
        }
    }
}
