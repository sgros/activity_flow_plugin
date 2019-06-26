package com.google.android.exoplayer2.util;

import java.util.Comparator;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.util.-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U */
public final /* synthetic */ class C0229-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U implements Comparator {
    public static final /* synthetic */ C0229-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U INSTANCE = new C0229-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U();

    private /* synthetic */ C0229-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U() {
    }

    public final int compare(Object obj, Object obj2) {
        return (((Sample) obj).index - ((Sample) obj2).index);
    }
}
