package org.telegram.p004ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import org.telegram.p004ui.ContentPreviewViewer.ContentPreviewViewerDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$ihsVyPyraLc397NrumrLt-QVanE */
public final /* synthetic */ class C1364-$$Lambda$ChatActivity$ihsVyPyraLc397NrumrLt-QVanE implements OnTouchListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ ContentPreviewViewerDelegate f$1;

    public /* synthetic */ C1364-$$Lambda$ChatActivity$ihsVyPyraLc397NrumrLt-QVanE(ChatActivity chatActivity, ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
        this.f$0 = chatActivity;
        this.f$1 = contentPreviewViewerDelegate;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f$0.lambda$createView$24$ChatActivity(this.f$1, view, motionEvent);
    }
}
