package org.telegram.messenger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.DataQuery.KeywordResultCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA */
public final /* synthetic */ class C0399-$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ String[] f$1;
    private final /* synthetic */ KeywordResultCallback f$2;
    private final /* synthetic */ String f$3;
    private final /* synthetic */ boolean f$4;
    private final /* synthetic */ ArrayList f$5;
    private final /* synthetic */ CountDownLatch f$6;

    public /* synthetic */ C0399-$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA(DataQuery dataQuery, String[] strArr, KeywordResultCallback keywordResultCallback, String str, boolean z, ArrayList arrayList, CountDownLatch countDownLatch) {
        this.f$0 = dataQuery;
        this.f$1 = strArr;
        this.f$2 = keywordResultCallback;
        this.f$3 = str;
        this.f$4 = z;
        this.f$5 = arrayList;
        this.f$6 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$getEmojiSuggestions$122$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}