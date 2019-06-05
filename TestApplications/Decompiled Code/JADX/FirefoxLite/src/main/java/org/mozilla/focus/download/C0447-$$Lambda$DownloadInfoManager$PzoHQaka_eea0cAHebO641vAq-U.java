package org.mozilla.focus.download;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.utils.IntentUtils;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.download.-$$Lambda$DownloadInfoManager$PzoHQaka_eea0cAHebO641vAq-U */
public final /* synthetic */ class C0447-$$Lambda$DownloadInfoManager$PzoHQaka_eea0cAHebO641vAq-U implements OnClickListener {
    private final /* synthetic */ View f$0;
    private final /* synthetic */ DownloadInfo f$1;

    public /* synthetic */ C0447-$$Lambda$DownloadInfoManager$PzoHQaka_eea0cAHebO641vAq-U(View view, DownloadInfo downloadInfo) {
        this.f$0 = view;
        this.f$1 = downloadInfo;
    }

    public final void onClick(View view) {
        IntentUtils.intentOpenFile(this.f$0.getContext(), this.f$1.getFileUri(), this.f$1.getMimeType());
    }
}
