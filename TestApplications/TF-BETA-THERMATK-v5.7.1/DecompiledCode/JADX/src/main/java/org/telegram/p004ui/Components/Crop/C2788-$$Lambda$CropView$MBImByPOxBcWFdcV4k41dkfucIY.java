package org.telegram.p004ui.Components.Crop;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.Crop.-$$Lambda$CropView$MBImByPOxBcWFdcV4k41dkfucIY */
public final /* synthetic */ class C2788-$$Lambda$CropView$MBImByPOxBcWFdcV4k41dkfucIY implements OnCancelListener {
    private final /* synthetic */ CropView f$0;

    public /* synthetic */ C2788-$$Lambda$CropView$MBImByPOxBcWFdcV4k41dkfucIY(CropView cropView) {
        this.f$0 = cropView;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$showAspectRatioDialog$3$CropView(dialogInterface);
    }
}
