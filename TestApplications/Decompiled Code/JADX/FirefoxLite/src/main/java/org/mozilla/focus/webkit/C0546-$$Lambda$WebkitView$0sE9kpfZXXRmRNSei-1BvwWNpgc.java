package org.mozilla.focus.webkit;

import android.webkit.ValueCallback;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.webkit.-$$Lambda$WebkitView$0sE9kpfZXXRmRNSei-1BvwWNpgc */
public final /* synthetic */ class C0546-$$Lambda$WebkitView$0sE9kpfZXXRmRNSei-1BvwWNpgc implements ValueCallback {
    private final /* synthetic */ WebkitView f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C0546-$$Lambda$WebkitView$0sE9kpfZXXRmRNSei-1BvwWNpgc(WebkitView webkitView, String str) {
        this.f$0 = webkitView;
        this.f$1 = str;
    }

    public final void onReceiveValue(Object obj) {
        WebkitView.lambda$insertBrowsingHistory$0(this.f$0, this.f$1, (String) obj);
    }
}
