package org.telegram.p004ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$EEBJwM4EJMpcSWE5sZNxj3sr1GA */
public final /* synthetic */ class C2471-$$Lambda$AlertsCreator$EEBJwM4EJMpcSWE5sZNxj3sr1GA implements OnClickListener {
    private final /* synthetic */ LinearLayout f$0;
    private final /* synthetic */ int[] f$1;

    public /* synthetic */ C2471-$$Lambda$AlertsCreator$EEBJwM4EJMpcSWE5sZNxj3sr1GA(LinearLayout linearLayout, int[] iArr) {
        this.f$0 = linearLayout;
        this.f$1 = iArr;
    }

    public final void onClick(View view) {
        AlertsCreator.lambda$createColorSelectDialog$23(this.f$0, this.f$1, view);
    }
}
