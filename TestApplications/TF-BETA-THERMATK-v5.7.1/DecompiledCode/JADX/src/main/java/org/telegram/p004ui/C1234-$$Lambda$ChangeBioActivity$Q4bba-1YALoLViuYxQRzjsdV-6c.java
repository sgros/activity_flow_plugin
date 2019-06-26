package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangeBioActivity$Q4bba-1YALoLViuYxQRzjsdV-6c */
public final /* synthetic */ class C1234-$$Lambda$ChangeBioActivity$Q4bba-1YALoLViuYxQRzjsdV-6c implements OnCancelListener {
    private final /* synthetic */ ChangeBioActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1234-$$Lambda$ChangeBioActivity$Q4bba-1YALoLViuYxQRzjsdV-6c(ChangeBioActivity changeBioActivity, int i) {
        this.f$0 = changeBioActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$saveName$5$ChangeBioActivity(this.f$1, dialogInterface);
    }
}
