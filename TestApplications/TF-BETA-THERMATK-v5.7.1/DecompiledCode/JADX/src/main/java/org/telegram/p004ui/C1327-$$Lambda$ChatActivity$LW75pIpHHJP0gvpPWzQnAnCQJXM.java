package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$LW75pIpHHJP0gvpPWzQnAnCQJXM */
public final /* synthetic */ class C1327-$$Lambda$ChatActivity$LW75pIpHHJP0gvpPWzQnAnCQJXM implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ boolean[] f$2;

    public /* synthetic */ C1327-$$Lambda$ChatActivity$LW75pIpHHJP0gvpPWzQnAnCQJXM(ChatActivity chatActivity, int i, boolean[] zArr) {
        this.f$0 = chatActivity;
        this.f$1 = i;
        this.f$2 = zArr;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processSelectedOption$69$ChatActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
