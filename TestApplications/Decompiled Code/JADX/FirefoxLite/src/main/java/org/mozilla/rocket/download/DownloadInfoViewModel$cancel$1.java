package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$cancel$1 implements OnQueryItemCompleteListener {
    final /* synthetic */ long $rowId;
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$cancel$1(DownloadInfoViewModel downloadInfoViewModel, long j) {
        this.this$0 = downloadInfoViewModel;
        this.$rowId = j;
    }

    public void onComplete(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        if (downloadInfo.existInDownloadManager()) {
            long j = this.$rowId;
            Long rowId = downloadInfo.getRowId();
            if (rowId != null && j == rowId.longValue() && 8 != downloadInfo.getStatus()) {
                this.this$0.getToastMessageObservable().setValue(Integer.valueOf(C0769R.string.download_cancel));
                DownloadInfoRepository access$getRepository$p = this.this$0.repository;
                Long downloadId = downloadInfo.getDownloadId();
                Intrinsics.checkExpressionValueIsNotNull(downloadId, "download.downloadId");
                access$getRepository$p.deleteFromDownloadManager(downloadId.longValue());
                this.this$0.remove(this.$rowId);
            }
        }
    }
}
