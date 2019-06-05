package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.download.DownloadInfoManager.AsyncQueryListener;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoRepository.kt */
final class DownloadInfoRepository$queryByDownloadId$1 implements AsyncQueryListener {
    final /* synthetic */ OnQueryItemCompleteListener $listenerItem;

    DownloadInfoRepository$queryByDownloadId$1(OnQueryItemCompleteListener onQueryItemCompleteListener) {
        this.$listenerItem = onQueryItemCompleteListener;
    }

    public final void onQueryComplete(List<DownloadInfo> list) {
        if (list.size() > 0) {
            DownloadInfo downloadInfo = (DownloadInfo) list.get(0);
            OnQueryItemCompleteListener onQueryItemCompleteListener = this.$listenerItem;
            Intrinsics.checkExpressionValueIsNotNull(downloadInfo, "downloadInfo");
            onQueryItemCompleteListener.onComplete(downloadInfo);
        }
    }
}
