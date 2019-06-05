package org.mozilla.focus.download;

import android.view.View;
import java.util.List;
import org.mozilla.focus.download.DownloadInfoManager.AsyncQueryListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.download.-$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA */
public final /* synthetic */ class C0683-$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA implements AsyncQueryListener {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ View f$1;

    public /* synthetic */ C0683-$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA(String str, View view) {
        this.f$0 = str;
        this.f$1 = view;
    }

    public final void onQueryComplete(List list) {
        DownloadInfoManager.lambda$showOpenDownloadSnackBar$1(this.f$0, this.f$1, list);
    }
}
