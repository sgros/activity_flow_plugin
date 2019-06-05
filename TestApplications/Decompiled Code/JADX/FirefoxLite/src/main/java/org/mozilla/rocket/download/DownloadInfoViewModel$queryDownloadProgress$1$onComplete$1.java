package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;

/* compiled from: DownloadInfoViewModel.kt */
final class DownloadInfoViewModel$queryDownloadProgress$1$onComplete$1 implements Runnable {
    final /* synthetic */ List $list;
    final /* synthetic */ DownloadInfoViewModel$queryDownloadProgress$1 this$0;

    DownloadInfoViewModel$queryDownloadProgress$1$onComplete$1(DownloadInfoViewModel$queryDownloadProgress$1 downloadInfoViewModel$queryDownloadProgress$1, List list) {
        this.this$0 = downloadInfoViewModel$queryDownloadProgress$1;
        this.$list = list;
    }

    public final void run() {
        for (DownloadInfo downloadInfo : this.$list) {
            int size = this.this$0.this$0.downloadInfoPack.getList().size();
            for (int i = 0; i < size; i++) {
                Object obj = this.this$0.this$0.downloadInfoPack.getList().get(i);
                Intrinsics.checkExpressionValueIsNotNull(obj, "downloadInfoPack.list.get(i)");
                DownloadInfo downloadInfo2 = (DownloadInfo) obj;
                if (Intrinsics.areEqual(downloadInfo2.getDownloadId(), downloadInfo.getDownloadId())) {
                    downloadInfo2.setSizeTotal(downloadInfo.getSizeTotal());
                    downloadInfo2.setSizeSoFar(downloadInfo.getSizeSoFar());
                    this.this$0.this$0.downloadInfoPack.setNotifyType(4);
                    this.this$0.this$0.downloadInfoPack.setIndex((long) i);
                    this.this$0.this$0.getDownloadInfoObservable().setValue(this.this$0.this$0.downloadInfoPack);
                    break;
                }
            }
        }
    }
}
