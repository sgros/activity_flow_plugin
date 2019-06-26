package org.telegram.p004ui.Components;

import android.graphics.SurfaceTexture;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$InstantCameraView$RBsoQ3f2-7L3ZL_CYLuRpS20Mko */
public final /* synthetic */ class C2596-$$Lambda$InstantCameraView$RBsoQ3f2-7L3ZL_CYLuRpS20Mko implements Runnable {
    private final /* synthetic */ InstantCameraView f$0;
    private final /* synthetic */ SurfaceTexture f$1;

    public /* synthetic */ C2596-$$Lambda$InstantCameraView$RBsoQ3f2-7L3ZL_CYLuRpS20Mko(InstantCameraView instantCameraView, SurfaceTexture surfaceTexture) {
        this.f$0 = instantCameraView;
        this.f$1 = surfaceTexture;
    }

    public final void run() {
        this.f$0.lambda$createCamera$4$InstantCameraView(this.f$1);
    }
}
