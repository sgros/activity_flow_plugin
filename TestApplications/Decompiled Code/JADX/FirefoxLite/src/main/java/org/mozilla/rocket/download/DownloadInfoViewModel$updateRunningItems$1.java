package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$updateRunningItems$1 implements OnQueryItemCompleteListener {
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$updateRunningItems$1(DownloadInfoViewModel downloadInfoViewModel) {
        this.this$0 = downloadInfoViewModel;
    }

    public void onComplete(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        int size = this.this$0.downloadInfoPack.getList().size();
        for (int i = 0; i < size; i++) {
            Object obj = this.this$0.downloadInfoPack.getList().get(i);
            Intrinsics.checkExpressionValueIsNotNull(obj, "downloadInfoPack.list[j]");
            DownloadInfo downloadInfo2 = (DownloadInfo) obj;
            if (Intrinsics.areEqual(downloadInfo.getDownloadId(), downloadInfo2.getDownloadId())) {
                downloadInfo2.setStatusInt(downloadInfo.getStatus());
                this.this$0.downloadInfoPack.setNotifyType(4);
                this.this$0.downloadInfoPack.setIndex((long) i);
                this.this$0.getDownloadInfoObservable().setValue(this.this$0.downloadInfoPack);
            }
        }
    }
}
