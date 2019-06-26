package org.telegram.p004ui.Components;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import org.telegram.p004ui.Components.InstantCameraView.CameraGLThread;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$InstantCameraView$CameraGLThread$8CYV7PTMH7zxhUymqIagAcuf5w8 */
public final /* synthetic */ class C2593x60a77276 implements OnFrameAvailableListener {
    private final /* synthetic */ CameraGLThread f$0;

    public /* synthetic */ C2593x60a77276(CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$handleMessage$1$InstantCameraView$CameraGLThread(surfaceTexture);
    }
}
