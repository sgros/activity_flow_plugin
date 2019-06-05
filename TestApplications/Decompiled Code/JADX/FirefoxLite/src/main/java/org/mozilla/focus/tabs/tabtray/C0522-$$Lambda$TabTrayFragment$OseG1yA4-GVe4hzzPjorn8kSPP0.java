package org.mozilla.focus.tabs.tabtray;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.tabs.tabtray.-$$Lambda$TabTrayFragment$OseG1yA4-GVe4hzzPjorn8kSPP0 */
public final /* synthetic */ class C0522-$$Lambda$TabTrayFragment$OseG1yA4-GVe4hzzPjorn8kSPP0 implements Runnable {
    private final /* synthetic */ TabTrayFragment f$0;
    private final /* synthetic */ Runnable f$1;

    public /* synthetic */ C0522-$$Lambda$TabTrayFragment$OseG1yA4-GVe4hzzPjorn8kSPP0(TabTrayFragment tabTrayFragment, Runnable runnable) {
        this.f$0 = tabTrayFragment;
        this.f$1 = runnable;
    }

    public final void run() {
        this.f$0.uiHandler.post(this.f$1);
    }
}
