package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import android.view.ViewGroup;
import org.mozilla.rocket.download.DownloadIndicatorViewModel.Status;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$BrowserFragment$l94-q85PEWz_WW25q4mSUZ5WGEE */
public final /* synthetic */ class C0689-$$Lambda$BrowserFragment$l94-q85PEWz_WW25q4mSUZ5WGEE implements Observer {
    private final /* synthetic */ BrowserFragment f$0;
    private final /* synthetic */ ViewGroup f$1;

    public /* synthetic */ C0689-$$Lambda$BrowserFragment$l94-q85PEWz_WW25q4mSUZ5WGEE(BrowserFragment browserFragment, ViewGroup viewGroup) {
        this.f$0 = browserFragment;
        this.f$1 = viewGroup;
    }

    public final void onChanged(Object obj) {
        BrowserFragment.lambda$onCreateView$2(this.f$0, this.f$1, (Status) obj);
    }
}
