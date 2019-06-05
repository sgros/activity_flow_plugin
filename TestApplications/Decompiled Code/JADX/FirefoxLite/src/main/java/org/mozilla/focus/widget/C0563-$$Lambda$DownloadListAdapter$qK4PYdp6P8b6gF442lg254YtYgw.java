package org.mozilla.focus.widget;

import android.view.View;
import java.io.File;
import java.net.URI;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.widget.-$$Lambda$DownloadListAdapter$qK4PYdp6P8b6gF442lg254YtYgw */
public final /* synthetic */ class C0563-$$Lambda$DownloadListAdapter$qK4PYdp6P8b6gF442lg254YtYgw implements Runnable {
    private final /* synthetic */ DownloadListAdapter f$0;
    private final /* synthetic */ DownloadInfo f$1;
    private final /* synthetic */ View f$2;

    public /* synthetic */ C0563-$$Lambda$DownloadListAdapter$qK4PYdp6P8b6gF442lg254YtYgw(DownloadListAdapter downloadListAdapter, DownloadInfo downloadInfo, View view) {
        this.f$0 = downloadListAdapter;
        this.f$1 = downloadInfo;
        this.f$2 = view;
    }

    public final void run() {
        ThreadUtils.postToMainThread(new C0562-$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc(this.f$0, new File(URI.create(this.f$1.getFileUri()).getPath()).exists(), this.f$2, this.f$1));
    }
}
