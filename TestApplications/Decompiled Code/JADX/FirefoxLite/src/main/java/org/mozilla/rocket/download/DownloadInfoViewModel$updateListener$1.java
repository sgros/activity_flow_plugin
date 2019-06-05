package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$updateListener$1 implements OnQueryItemCompleteListener {
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$updateListener$1(DownloadInfoViewModel downloadInfoViewModel) {
        this.this$0 = downloadInfoViewModel;
    }

    public void onComplete(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        this.this$0.updateItem(downloadInfo);
    }
}
