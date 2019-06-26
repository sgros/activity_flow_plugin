package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$-OkER2xuwjfE0-6Y_fzH2uLkAhM */
public final /* synthetic */ class C1702-$$Lambda$PassportActivity$-OkER2xuwjfE0-6Y_fzH2uLkAhM implements OnClickListener {
    private final /* synthetic */ PassportActivity f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C1702-$$Lambda$PassportActivity$-OkER2xuwjfE0-6Y_fzH2uLkAhM(PassportActivity passportActivity, ArrayList arrayList) {
        this.f$0 = passportActivity;
        this.f$1 = arrayList;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$openAddDocumentAlert$23$PassportActivity(this.f$1, dialogInterface, i);
    }
}
