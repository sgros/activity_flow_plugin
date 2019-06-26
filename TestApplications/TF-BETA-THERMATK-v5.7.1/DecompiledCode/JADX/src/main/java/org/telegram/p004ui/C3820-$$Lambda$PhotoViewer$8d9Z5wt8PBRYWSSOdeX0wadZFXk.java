package org.telegram.p004ui;

import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk */
public final /* synthetic */ class C3820-$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk implements ImageReceiverDelegate {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C3820-$$Lambda$PhotoViewer$8d9Z5wt8PBRYWSSOdeX0wadZFXk(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2) {
        this.f$0.lambda$setParentActivity$22$PhotoViewer(imageReceiver, z, z2);
    }
}
