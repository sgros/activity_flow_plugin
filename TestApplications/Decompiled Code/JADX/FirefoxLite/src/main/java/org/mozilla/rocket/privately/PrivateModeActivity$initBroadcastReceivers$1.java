package org.mozilla.rocket.privately;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfoManager;

/* compiled from: PrivateModeActivity.kt */
public final class PrivateModeActivity$initBroadcastReceivers$1 extends BroadcastReceiver {
    final /* synthetic */ PrivateModeActivity this$0;

    PrivateModeActivity$initBroadcastReceivers$1(PrivateModeActivity privateModeActivity) {
        this.this$0 = privateModeActivity;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        if (Intrinsics.areEqual(intent.getAction(), "org.mozilla.action.RELOCATE_FINISH")) {
            DownloadInfoManager.getInstance().showOpenDownloadSnackBar(Long.valueOf(intent.getLongExtra("org.mozilla.extra.row_id", -1)), PrivateModeActivity.access$getSnackBarContainer$p(this.this$0), this.this$0.LOG_TAG);
        }
    }
}
