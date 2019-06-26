package org.telegram.messenger.camera;

import android.hardware.Camera;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.camera.-$$Lambda$CameraController$9retvXsm_Oz3JB3x45Buo-LxHfU */
public final /* synthetic */ class C1081-$$Lambda$CameraController$9retvXsm_Oz3JB3x45Buo-LxHfU implements Runnable {
    private final /* synthetic */ Camera f$0;
    private final /* synthetic */ CameraSession f$1;

    public /* synthetic */ C1081-$$Lambda$CameraController$9retvXsm_Oz3JB3x45Buo-LxHfU(Camera camera, CameraSession cameraSession) {
        this.f$0 = camera;
        this.f$1 = cameraSession;
    }

    public final void run() {
        CameraController.lambda$null$12(this.f$0, this.f$1);
    }
}
