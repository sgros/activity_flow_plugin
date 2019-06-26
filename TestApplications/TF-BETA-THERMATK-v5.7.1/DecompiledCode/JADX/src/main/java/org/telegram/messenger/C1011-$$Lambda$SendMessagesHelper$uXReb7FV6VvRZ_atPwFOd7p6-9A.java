package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$uXReb7FV6VvRZ_atPwFOd7p6-9A */
public final /* synthetic */ class C1011-$$Lambda$SendMessagesHelper$uXReb7FV6VvRZ_atPwFOd7p6-9A implements Runnable {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C1011-$$Lambda$SendMessagesHelper$uXReb7FV6VvRZ_atPwFOd7p6-9A(String str, int i, long j) {
        this.f$0 = str;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        AndroidUtilities.runOnUIThread(new C1002-$$Lambda$SendMessagesHelper$oKRDK-mjDEAeKeMOR2bAQ2LKSjU(this.f$0, this.f$1, this.f$2));
    }
}
