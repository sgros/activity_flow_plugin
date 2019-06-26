package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$mEb_8CvzR9IFpSPa5BhRMXY7oRk */
public final /* synthetic */ class C0999-$$Lambda$SendMessagesHelper$mEb_8CvzR9IFpSPa5BhRMXY7oRk implements Runnable {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0999-$$Lambda$SendMessagesHelper$mEb_8CvzR9IFpSPa5BhRMXY7oRk(String str, int i, long j) {
        this.f$0 = str;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        Utilities.stageQueue.postRunnable(new C1011-$$Lambda$SendMessagesHelper$uXReb7FV6VvRZ_atPwFOd7p6-9A(this.f$0, this.f$1, this.f$2));
    }
}
