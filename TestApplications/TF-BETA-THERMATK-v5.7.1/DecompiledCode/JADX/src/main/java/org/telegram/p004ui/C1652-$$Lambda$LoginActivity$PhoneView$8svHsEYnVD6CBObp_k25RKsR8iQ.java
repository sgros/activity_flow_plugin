package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.LoginActivity.PhoneView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$PhoneView$8svHsEYnVD6CBObp_k25RKsR8iQ */
public final /* synthetic */ class C1652-$$Lambda$LoginActivity$PhoneView$8svHsEYnVD6CBObp_k25RKsR8iQ implements OnClickListener {
    private final /* synthetic */ PhoneView f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1652-$$Lambda$LoginActivity$PhoneView$8svHsEYnVD6CBObp_k25RKsR8iQ(PhoneView phoneView, int i) {
        this.f$0 = phoneView;
        this.f$1 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onNextPressed$7$LoginActivity$PhoneView(this.f$1, dialogInterface, i);
    }
}
