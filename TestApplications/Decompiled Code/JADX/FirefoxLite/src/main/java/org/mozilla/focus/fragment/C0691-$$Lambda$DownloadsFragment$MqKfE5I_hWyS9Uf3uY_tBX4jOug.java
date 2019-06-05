package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import android.support.design.widget.BaseTransientBottomBar.BaseCallback;
import android.support.design.widget.Snackbar;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.rocket.C0769R;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug */
public final /* synthetic */ class C0691-$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug implements Observer {
    private final /* synthetic */ DownloadsFragment f$0;

    public /* synthetic */ C0691-$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug(DownloadsFragment downloadsFragment) {
        this.f$0 = downloadsFragment;
    }

    public final void onChanged(Object obj) {
        ((Snackbar) Snackbar.make(this.f$0.recyclerView, this.f$0.getString(C0769R.string.download_deleted, ((DownloadInfo) obj).getFileName()), -1).addCallback(new BaseCallback<Snackbar>((DownloadInfo) obj) {
            public void onDismissed(Snackbar snackbar, int i) {
                super.onDismissed(snackbar, i);
                if (i != 1) {
                    DownloadsFragment.this.viewModel.confirmDelete(r4);
                }
            }

            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                DownloadsFragment.this.viewModel.hide(r4.getRowId().longValue());
            }
        })).setAction((int) C0769R.string.undo, new C0461-$$Lambda$DownloadsFragment$0VaTEEPTvN8aDQrBVULu865voMw(this.f$0, (DownloadInfo) obj)).show();
    }
}
