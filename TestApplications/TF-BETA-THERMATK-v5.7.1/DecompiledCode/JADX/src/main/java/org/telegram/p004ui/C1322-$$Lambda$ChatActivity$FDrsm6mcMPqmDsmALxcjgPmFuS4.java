package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$FDrsm6mcMPqmDsmALxcjgPmFuS4 */
public final /* synthetic */ class C1322-$$Lambda$ChatActivity$FDrsm6mcMPqmDsmALxcjgPmFuS4 implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C1322-$$Lambda$ChatActivity$FDrsm6mcMPqmDsmALxcjgPmFuS4(ChatActivity chatActivity, String str) {
        this.f$0 = chatActivity;
        this.f$1 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showOpenUrlAlert$84$ChatActivity(this.f$1, dialogInterface, i);
    }
}
