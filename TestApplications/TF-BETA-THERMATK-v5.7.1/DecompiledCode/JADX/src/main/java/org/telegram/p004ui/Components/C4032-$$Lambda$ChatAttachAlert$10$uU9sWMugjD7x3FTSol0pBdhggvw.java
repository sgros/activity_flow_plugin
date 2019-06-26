package org.telegram.p004ui.Components;

import org.telegram.messenger.camera.CameraController.VideoTakeCallback;
import org.telegram.p004ui.Components.ChatAttachAlert.C408210;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatAttachAlert$10$uU9sWMugjD7x3FTSol0pBdhggvw */
public final /* synthetic */ class C4032-$$Lambda$ChatAttachAlert$10$uU9sWMugjD7x3FTSol0pBdhggvw implements VideoTakeCallback {
    private final /* synthetic */ C408210 f$0;

    public /* synthetic */ C4032-$$Lambda$ChatAttachAlert$10$uU9sWMugjD7x3FTSol0pBdhggvw(C408210 c408210) {
        this.f$0 = c408210;
    }

    public final void onFinishVideoRecording(String str, long j) {
        this.f$0.lambda$shutterLongPressed$1$ChatAttachAlert$10(str, j);
    }
}
