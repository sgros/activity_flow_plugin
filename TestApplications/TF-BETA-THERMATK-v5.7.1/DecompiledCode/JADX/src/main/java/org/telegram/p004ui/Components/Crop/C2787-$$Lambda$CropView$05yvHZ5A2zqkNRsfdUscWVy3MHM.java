package org.telegram.p004ui.Components.Crop;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.Crop.-$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM */
public final /* synthetic */ class C2787-$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM implements OnClickListener {
    private final /* synthetic */ CropView f$0;
    private final /* synthetic */ Integer[][] f$1;

    public /* synthetic */ C2787-$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM(CropView cropView, Integer[][] numArr) {
        this.f$0 = cropView;
        this.f$1 = numArr;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showAspectRatioDialog$2$CropView(this.f$1, dialogInterface, i);
    }
}
