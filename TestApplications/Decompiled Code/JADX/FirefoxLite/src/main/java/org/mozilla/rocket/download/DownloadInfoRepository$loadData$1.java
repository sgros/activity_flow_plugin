package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.download.DownloadInfoManager.AsyncQueryListener;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryListCompleteListener;

/* compiled from: DownloadInfoRepository.kt */
final class DownloadInfoRepository$loadData$1 implements AsyncQueryListener {
    final /* synthetic */ OnQueryListCompleteListener $listenerList;

    DownloadInfoRepository$loadData$1(OnQueryListCompleteListener onQueryListCompleteListener) {
        this.$listenerList = onQueryListCompleteListener;
    }

    public final void onQueryComplete(List<DownloadInfo> list) {
        OnQueryListCompleteListener onQueryListCompleteListener = this.$listenerList;
        Intrinsics.checkExpressionValueIsNotNull(list, "downloadInfoList");
        onQueryListCompleteListener.onComplete(list);
    }
}
