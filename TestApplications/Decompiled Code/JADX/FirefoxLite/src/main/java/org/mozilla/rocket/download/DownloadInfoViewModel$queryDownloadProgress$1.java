package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryListCompleteListener;
import org.mozilla.rocket.download.DownloadInfoViewModel.OnProgressUpdateListener;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$queryDownloadProgress$1 implements OnQueryListCompleteListener {
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$queryDownloadProgress$1(DownloadInfoViewModel downloadInfoViewModel) {
        this.this$0 = downloadInfoViewModel;
    }

    public void onComplete(List<? extends DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        OnProgressUpdateListener access$getProgressUpdateListener$p;
        if ((list.isEmpty() ^ 1) != 0) {
            ThreadUtils.postToMainThread(new DownloadInfoViewModel$queryDownloadProgress$1$onComplete$1(this, list));
            access$getProgressUpdateListener$p = this.this$0.progressUpdateListener;
            if (access$getProgressUpdateListener$p != null) {
                access$getProgressUpdateListener$p.onCompleteUpdate();
                return;
            }
            return;
        }
        access$getProgressUpdateListener$p = this.this$0.progressUpdateListener;
        if (access$getProgressUpdateListener$p != null) {
            access$getProgressUpdateListener$p.onStopUpdate();
        }
    }
}
