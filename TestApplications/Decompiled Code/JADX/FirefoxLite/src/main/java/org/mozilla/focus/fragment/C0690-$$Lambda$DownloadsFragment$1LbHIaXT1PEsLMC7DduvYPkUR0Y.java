package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import org.mozilla.rocket.download.DownloadInfoPack;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y */
public final /* synthetic */ class C0690-$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y implements Observer {
    private final /* synthetic */ DownloadsFragment f$0;

    public /* synthetic */ C0690-$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y(DownloadsFragment downloadsFragment) {
        this.f$0 = downloadsFragment;
    }

    public final void onChanged(Object obj) {
        DownloadsFragment.lambda$onCreateView$0(this.f$0, (DownloadInfoPack) obj);
    }
}
