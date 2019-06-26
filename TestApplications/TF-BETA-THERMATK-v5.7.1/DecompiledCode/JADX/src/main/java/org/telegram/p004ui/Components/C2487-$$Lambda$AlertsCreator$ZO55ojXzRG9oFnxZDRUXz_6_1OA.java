package org.telegram.p004ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.Components.AlertsCreator.AccountSelectDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA */
public final /* synthetic */ class C2487-$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA implements OnClickListener {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ Runnable f$1;
    private final /* synthetic */ AccountSelectDelegate f$2;

    public /* synthetic */ C2487-$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA(AlertDialog[] alertDialogArr, Runnable runnable, AccountSelectDelegate accountSelectDelegate) {
        this.f$0 = alertDialogArr;
        this.f$1 = runnable;
        this.f$2 = accountSelectDelegate;
    }

    public final void onClick(View view) {
        AlertsCreator.lambda$createAccountSelectDialog$40(this.f$0, this.f$1, this.f$2, view);
    }
}
