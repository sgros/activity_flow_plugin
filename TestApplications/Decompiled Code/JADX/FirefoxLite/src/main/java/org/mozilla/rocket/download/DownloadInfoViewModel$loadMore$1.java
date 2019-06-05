package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryListCompleteListener;
import org.mozilla.rocket.download.DownloadInfoViewModel.OnProgressUpdateListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$loadMore$1 implements OnQueryListCompleteListener {
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$loadMore$1(DownloadInfoViewModel downloadInfoViewModel) {
        this.this$0 = downloadInfoViewModel;
    }

    public void onComplete(List<? extends DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        this.this$0.downloadInfoPack.getList().addAll(list);
        this.this$0.downloadInfoPack.setNotifyType(1);
        this.this$0.itemCount = this.this$0.downloadInfoPack.getList().size();
        this.this$0.getDownloadInfoObservable().setValue(this.this$0.downloadInfoPack);
        this.this$0.setOpening(false);
        this.this$0.isLoading = false;
        this.this$0.isLastPage = list.isEmpty();
        if (this.this$0.isDownloading()) {
            OnProgressUpdateListener access$getProgressUpdateListener$p = this.this$0.progressUpdateListener;
            if (access$getProgressUpdateListener$p != null) {
                access$getProgressUpdateListener$p.onStartUpdate();
            }
        }
    }
}
