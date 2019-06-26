package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$3$lo1j3M5K6zygAvdIFH_K82igfdE */
public final /* synthetic */ class C0477-$$Lambda$ImageLoader$3$lo1j3M5K6zygAvdIFH_K82igfdE implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0477-$$Lambda$ImageLoader$3$lo1j3M5K6zygAvdIFH_K82igfdE(int i, String str, boolean z) {
        this.f$0 = i;
        this.f$1 = str;
        this.f$2 = z;
    }

    public final void run() {
        NotificationCenter.getInstance(this.f$0).postNotificationName(NotificationCenter.FileDidFailUpload, this.f$1, Boolean.valueOf(this.f$2));
    }
}
