package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk */
public final /* synthetic */ class C3713-$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk implements DialogsActivityDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ String f$3;

    public /* synthetic */ C3713-$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk(LaunchActivity launchActivity, boolean z, int i, String str) {
        this.f$0 = launchActivity;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = str;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$runLinkRequest$18$LaunchActivity(this.f$1, this.f$2, this.f$3, dialogsActivity, arrayList, charSequence, z);
    }
}
