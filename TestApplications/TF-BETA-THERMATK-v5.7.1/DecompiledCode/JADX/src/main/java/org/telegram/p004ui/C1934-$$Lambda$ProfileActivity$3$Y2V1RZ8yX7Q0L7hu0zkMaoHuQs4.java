package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.p004ui.ProfileActivity.C43083;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$3$Y2V1RZ8yX7Q0L7hu0zkMaoHuQs4 */
public final /* synthetic */ class C1934-$$Lambda$ProfileActivity$3$Y2V1RZ8yX7Q0L7hu0zkMaoHuQs4 implements OnCancelListener {
    private final /* synthetic */ C43083 f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1934-$$Lambda$ProfileActivity$3$Y2V1RZ8yX7Q0L7hu0zkMaoHuQs4(C43083 c43083, int i) {
        this.f$0 = c43083;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$onItemClick$5$ProfileActivity$3(this.f$1, dialogInterface);
    }
}
