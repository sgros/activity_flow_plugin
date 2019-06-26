package org.telegram.p004ui.Components;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import org.telegram.p004ui.Components.InstantCameraView.CameraGLThread;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$InstantCameraView$CameraGLThread$owshQSrJ0yE90p_o4TfGsfbd_z0 */
public final /* synthetic */ class C2594x8cccee34 implements OnFrameAvailableListener {
    private final /* synthetic */ CameraGLThread f$0;

    public /* synthetic */ C2594x8cccee34(CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$initGL$0$InstantCameraView$CameraGLThread(surfaceTexture);
    }
}
