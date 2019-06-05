package org.mozilla.focus.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.download.DownloadInfo;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$DownloadsFragment$0VaTEEPTvN8aDQrBVULu865voMw */
public final /* synthetic */ class C0461-$$Lambda$DownloadsFragment$0VaTEEPTvN8aDQrBVULu865voMw implements OnClickListener {
    private final /* synthetic */ DownloadsFragment f$0;
    private final /* synthetic */ DownloadInfo f$1;

    public /* synthetic */ C0461-$$Lambda$DownloadsFragment$0VaTEEPTvN8aDQrBVULu865voMw(DownloadsFragment downloadsFragment, DownloadInfo downloadInfo) {
        this.f$0 = downloadsFragment;
        this.f$1 = downloadInfo;
    }

    public final void onClick(View view) {
        this.f$0.viewModel.add(this.f$1);
    }
}
