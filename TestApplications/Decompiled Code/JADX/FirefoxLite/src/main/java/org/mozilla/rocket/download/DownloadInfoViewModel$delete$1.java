package org.mozilla.rocket.download;

import java.io.File;
import java.net.URI;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadInfoRepository.OnQueryItemCompleteListener;

/* compiled from: DownloadInfoViewModel.kt */
public final class DownloadInfoViewModel$delete$1 implements OnQueryItemCompleteListener {
    final /* synthetic */ DownloadInfoViewModel this$0;

    DownloadInfoViewModel$delete$1(DownloadInfoViewModel downloadInfoViewModel) {
        this.this$0 = downloadInfoViewModel;
    }

    public void onComplete(DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "download");
        URI create = URI.create(downloadInfo.getFileUri());
        Intrinsics.checkExpressionValueIsNotNull(create, "URI.create(download.fileUri)");
        if (new File(create.getPath()).exists()) {
            this.this$0.getDeleteSnackbarObservable().setValue(downloadInfo);
        } else {
            this.this$0.getToastMessageObservable().setValue(Integer.valueOf(C0769R.string.cannot_find_the_file));
        }
    }
}
