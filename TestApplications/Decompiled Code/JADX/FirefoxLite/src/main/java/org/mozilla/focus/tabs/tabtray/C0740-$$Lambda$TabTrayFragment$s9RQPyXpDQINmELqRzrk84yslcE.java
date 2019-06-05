package org.mozilla.focus.tabs.tabtray;

import android.support.p004v7.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.tabs.tabtray.-$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE */
public final /* synthetic */ class C0740-$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE implements ItemAnimatorFinishedListener {
    private final /* synthetic */ TabTrayFragment f$0;
    private final /* synthetic */ Runnable f$1;

    public /* synthetic */ C0740-$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE(TabTrayFragment tabTrayFragment, Runnable runnable) {
        this.f$0 = tabTrayFragment;
        this.f$1 = runnable;
    }

    public final void onAnimationsFinished() {
        this.f$0.uiHandler.post(this.f$1);
    }
}
