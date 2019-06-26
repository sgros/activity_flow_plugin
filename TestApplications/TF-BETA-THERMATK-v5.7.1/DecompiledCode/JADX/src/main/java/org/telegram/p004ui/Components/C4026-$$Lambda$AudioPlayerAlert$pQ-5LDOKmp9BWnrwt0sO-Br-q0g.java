package org.telegram.p004ui.Components;

import org.telegram.messenger.MediaController;
import org.telegram.p004ui.Components.SeekBarView.SeekBarViewDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AudioPlayerAlert$pQ-5LDOKmp9BWnrwt0sO-Br-q0g */
public final /* synthetic */ class C4026-$$Lambda$AudioPlayerAlert$pQ-5LDOKmp9BWnrwt0sO-Br-q0g implements SeekBarViewDelegate {
    public static final /* synthetic */ C4026-$$Lambda$AudioPlayerAlert$pQ-5LDOKmp9BWnrwt0sO-Br-q0g INSTANCE = new C4026-$$Lambda$AudioPlayerAlert$pQ-5LDOKmp9BWnrwt0sO-Br-q0g();

    private /* synthetic */ C4026-$$Lambda$AudioPlayerAlert$pQ-5LDOKmp9BWnrwt0sO-Br-q0g() {
    }

    public final void onSeekBarDrag(float f) {
        MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f);
    }
}
