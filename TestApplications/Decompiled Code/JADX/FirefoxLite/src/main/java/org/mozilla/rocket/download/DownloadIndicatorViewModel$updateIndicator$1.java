package org.mozilla.rocket.download;

import android.arch.lifecycle.MutableLiveData;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.download.DownloadIndicatorViewModel.Status;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryListCompleteListener;

/* compiled from: DownloadIndicatorViewModel.kt */
public final class DownloadIndicatorViewModel$updateIndicator$1 implements OnQueryListCompleteListener {
    final /* synthetic */ DownloadIndicatorViewModel this$0;

    DownloadIndicatorViewModel$updateIndicator$1(DownloadIndicatorViewModel downloadIndicatorViewModel) {
        this.this$0 = downloadIndicatorViewModel;
    }

    public void onComplete(List<? extends DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        Object obj = null;
        Object obj2 = null;
        Object obj3 = null;
        for (DownloadInfo downloadInfo : list) {
            if (obj == null && (downloadInfo.getStatus() == 2 || downloadInfo.getStatus() == 1)) {
                obj = 1;
            }
            if (obj2 == null && downloadInfo.getStatus() == 8 && !downloadInfo.isRead()) {
                obj2 = 1;
            }
            if (obj3 == null && (downloadInfo.getStatus() == 4 || downloadInfo.getStatus() == 16)) {
                obj3 = 1;
            }
        }
        MutableLiveData downloadIndicatorObservable = this.this$0.getDownloadIndicatorObservable();
        if (obj != null) {
            obj = Status.DOWNLOADING;
        } else if (obj2 != null) {
            obj = Status.UNREAD;
        } else if (obj3 != null) {
            obj = Status.WARNING;
        } else {
            obj = Status.DEFAULT;
        }
        downloadIndicatorObservable.setValue(obj);
    }
}
