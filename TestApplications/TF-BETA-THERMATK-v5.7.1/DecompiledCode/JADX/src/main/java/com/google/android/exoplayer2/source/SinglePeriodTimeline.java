package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.util.Assertions;

public final class SinglePeriodTimeline extends Timeline {
    private static final Object UID = new Object();
    private final boolean isDynamic;
    private final boolean isSeekable;
    private final long periodDurationUs;
    private final long presentationStartTimeMs;
    private final Object tag;
    private final long windowDefaultStartPositionUs;
    private final long windowDurationUs;
    private final long windowPositionInPeriodUs;
    private final long windowStartTimeMs;

    public int getPeriodCount() {
        return 1;
    }

    public int getWindowCount() {
        return 1;
    }

    public SinglePeriodTimeline(long j, boolean z, boolean z2, Object obj) {
        this(j, j, 0, 0, z, z2, obj);
    }

    public SinglePeriodTimeline(long j, long j2, long j3, long j4, boolean z, boolean z2, Object obj) {
        this(-9223372036854775807L, -9223372036854775807L, j, j2, j3, j4, z, z2, obj);
    }

    public SinglePeriodTimeline(long j, long j2, long j3, long j4, long j5, long j6, boolean z, boolean z2, Object obj) {
        this.presentationStartTimeMs = j;
        this.windowStartTimeMs = j2;
        this.periodDurationUs = j3;
        this.windowDurationUs = j4;
        this.windowPositionInPeriodUs = j5;
        this.windowDefaultStartPositionUs = j6;
        this.isSeekable = z;
        this.isDynamic = z2;
        this.tag = obj;
    }

    /* JADX WARNING: Missing block: B:12:0x002d, code skipped:
            if (r1 > r7) goto L_0x0027;
     */
    public com.google.android.exoplayer2.Timeline.Window getWindow(int r19, com.google.android.exoplayer2.Timeline.Window r20, boolean r21, long r22) {
        /*
        r18 = this;
        r0 = r18;
        r1 = 0;
        r2 = 1;
        r3 = r19;
        com.google.android.exoplayer2.util.Assertions.checkIndex(r3, r1, r2);
        if (r21 == 0) goto L_0x000e;
    L_0x000b:
        r1 = r0.tag;
        goto L_0x000f;
    L_0x000e:
        r1 = 0;
    L_0x000f:
        r3 = r1;
        r1 = r0.windowDefaultStartPositionUs;
        r4 = r0.isDynamic;
        r5 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        if (r4 == 0) goto L_0x0030;
    L_0x001b:
        r7 = 0;
        r4 = (r22 > r7 ? 1 : (r22 == r7 ? 0 : -1));
        if (r4 == 0) goto L_0x0030;
    L_0x0021:
        r7 = r0.windowDurationUs;
        r4 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0029;
    L_0x0027:
        r10 = r5;
        goto L_0x0031;
    L_0x0029:
        r1 = r1 + r22;
        r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1));
        if (r4 <= 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0027;
    L_0x0030:
        r10 = r1;
    L_0x0031:
        r4 = r0.presentationStartTimeMs;
        r6 = r0.windowStartTimeMs;
        r8 = r0.isSeekable;
        r9 = r0.isDynamic;
        r12 = r0.windowDurationUs;
        r14 = 0;
        r15 = 0;
        r1 = r0.windowPositionInPeriodUs;
        r16 = r1;
        r2 = r20;
        r2.set(r3, r4, r6, r8, r9, r10, r12, r14, r15, r16);
        return r20;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.SinglePeriodTimeline.getWindow(int, com.google.android.exoplayer2.Timeline$Window, boolean, long):com.google.android.exoplayer2.Timeline$Window");
    }

    public Period getPeriod(int i, Period period, boolean z) {
        Assertions.checkIndex(i, 0, 1);
        period.set(null, z ? UID : null, 0, this.periodDurationUs, -this.windowPositionInPeriodUs);
        return period;
    }

    public int getIndexOfPeriod(Object obj) {
        return UID.equals(obj) ? 0 : -1;
    }

    public Object getUidOfPeriod(int i) {
        Assertions.checkIndex(i, 0, 1);
        return UID;
    }
}
