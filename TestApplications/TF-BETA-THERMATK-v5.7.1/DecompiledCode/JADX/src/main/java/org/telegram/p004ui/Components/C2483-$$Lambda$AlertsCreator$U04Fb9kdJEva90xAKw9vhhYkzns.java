package org.telegram.p004ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns */
public final /* synthetic */ class C2483-$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns implements OnClickListener {
    private final /* synthetic */ int[] f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Builder f$2;
    private final /* synthetic */ Runnable f$3;

    public /* synthetic */ C2483-$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns(int[] iArr, int i, Builder builder, Runnable runnable) {
        this.f$0 = iArr;
        this.f$1 = i;
        this.f$2 = builder;
        this.f$3 = runnable;
    }

    public final void onClick(View view) {
        AlertsCreator.lambda$createPopupSelectDialog$36(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
