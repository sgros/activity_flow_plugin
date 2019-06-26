package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DialogsActivity$aS99ZGtwHsaaGFpbpdjdMEuMtKw */
public final /* synthetic */ class C1504-$$Lambda$DialogsActivity$aS99ZGtwHsaaGFpbpdjdMEuMtKw implements OnClickListener {
    private final /* synthetic */ DialogsActivity f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C1504-$$Lambda$DialogsActivity$aS99ZGtwHsaaGFpbpdjdMEuMtKw(DialogsActivity dialogsActivity, long j) {
        this.f$0 = dialogsActivity;
        this.f$1 = j;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$didSelectResult$18$DialogsActivity(this.f$1, dialogInterface, i);
    }
}
