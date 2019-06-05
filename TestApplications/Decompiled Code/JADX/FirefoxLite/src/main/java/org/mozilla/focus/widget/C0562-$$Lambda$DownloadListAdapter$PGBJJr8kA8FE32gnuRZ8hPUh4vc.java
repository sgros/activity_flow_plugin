package org.mozilla.focus.widget;

import android.view.View;
import org.mozilla.focus.download.DownloadInfo;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.widget.-$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc */
public final /* synthetic */ class C0562-$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc implements Runnable {
    private final /* synthetic */ DownloadListAdapter f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ View f$2;
    private final /* synthetic */ DownloadInfo f$3;

    public /* synthetic */ C0562-$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc(DownloadListAdapter downloadListAdapter, boolean z, View view, DownloadInfo downloadInfo) {
        this.f$0 = downloadListAdapter;
        this.f$1 = z;
        this.f$2 = view;
        this.f$3 = downloadInfo;
    }

    public final void run() {
        DownloadListAdapter.lambda$null$2(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
