package org.mozilla.rocket.privately.browse;

import android.content.Context;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.EnqueueDownloadTask;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.tabs.web.Download;

/* compiled from: BrowserFragment.kt */
public final class BrowserFragment$onAttach$1 implements PermissionHandle {
    final /* synthetic */ BrowserFragment this$0;

    public void doActionNoPermission(String str, int i, Parcelable parcelable) {
    }

    BrowserFragment$onAttach$1(BrowserFragment browserFragment) {
        this.this$0 = browserFragment;
    }

    public void doActionDirect(String str, int i, Parcelable parcelable) {
        Context context = this.this$0.getContext();
        if (context != null) {
            if (parcelable != null) {
                Download download = (Download) parcelable;
                if (ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    queueDownload(download);
                }
                if (context != null) {
                    return;
                }
            }
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.web.Download");
        }
        BrowserFragment$onAttach$1 browserFragment$onAttach$1 = this;
        Integer.valueOf(Log.e("BrowserFragment.kt", "No context to use, abort callback onDownloadStart"));
    }

    public final void actionDownloadGranted(Parcelable parcelable) {
        if (parcelable != null) {
            queueDownload((Download) parcelable);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.web.Download");
    }

    public void doActionGranted(String str, int i, Parcelable parcelable) {
        actionDownloadGranted(parcelable);
    }

    public void doActionSetting(String str, int i, Parcelable parcelable) {
        actionDownloadGranted(parcelable);
    }

    public Snackbar makeAskAgainSnackBar(int i) {
        FragmentActivity activity = this.this$0.getActivity();
        if (activity != null) {
            Snackbar makeAskAgainSnackBar = PermissionHandler.makeAskAgainSnackBar((Fragment) this.this$0, activity.findViewById(2131296374), (int) C0769R.string.permission_toast_storage);
            Intrinsics.checkExpressionValueIsNotNull(makeAskAgainSnackBar, "PermissionHandler.makeAs…age\n                    )");
            return makeAskAgainSnackBar;
        }
        throw new IllegalStateException("No Activity to show Snackbar.");
    }

    public void permissionDeniedToast(int i) {
        Toast.makeText(this.this$0.getContext(), C0769R.string.permission_toast_storage_deny, 1).show();
    }

    public void requestPermissions(int i) {
        this.this$0.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
    }

    private final void queueDownload(Download download) {
        FragmentActivity activity = this.this$0.getActivity();
        if (activity != null && download != null) {
            new EnqueueDownloadTask(activity, download, BrowserFragment.access$getDisplayUrlView$p(this.this$0).getText().toString()).execute(new Void[0]);
        }
    }
}
