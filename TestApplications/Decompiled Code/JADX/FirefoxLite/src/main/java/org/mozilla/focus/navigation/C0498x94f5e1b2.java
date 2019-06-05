package org.mozilla.focus.navigation;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.navigation.-$$Lambda$TransactionHelper$BackStackListener$KqWYXvDV_PCHcqgGsMvvJBFNPvE */
public final /* synthetic */ class C0498x94f5e1b2 implements Runnable {
    private final /* synthetic */ BackStackListener f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C0498x94f5e1b2(BackStackListener backStackListener, boolean z) {
        this.f$0 = backStackListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.setBrowserForegroundState(this.f$1);
    }
}
