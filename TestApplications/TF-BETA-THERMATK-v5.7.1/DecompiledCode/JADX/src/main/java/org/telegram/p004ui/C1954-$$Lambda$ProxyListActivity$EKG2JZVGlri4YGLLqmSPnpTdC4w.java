package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.SharedConfig.ProxyInfo;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w */
public final /* synthetic */ class C1954-$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w implements OnClickListener {
    private final /* synthetic */ ProxyListActivity f$0;
    private final /* synthetic */ ProxyInfo f$1;

    public /* synthetic */ C1954-$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w(ProxyListActivity proxyListActivity, ProxyInfo proxyInfo) {
        this.f$0 = proxyListActivity;
        this.f$1 = proxyInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$1$ProxyListActivity(this.f$1, dialogInterface, i);
    }
}
