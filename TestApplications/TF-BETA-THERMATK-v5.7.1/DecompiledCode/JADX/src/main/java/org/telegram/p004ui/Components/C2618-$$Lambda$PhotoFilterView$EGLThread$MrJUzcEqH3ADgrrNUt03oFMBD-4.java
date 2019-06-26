package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.PhotoFilterView.EGLThread;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD-4 */
public final /* synthetic */ class C2618-$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD-4 implements Runnable {
    private final /* synthetic */ EGLThread f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C2618-$$Lambda$PhotoFilterView$EGLThread$MrJUzcEqH3ADgrrNUt03oFMBD-4(EGLThread eGLThread, boolean z, boolean z2) {
        this.f$0 = eGLThread;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void run() {
        this.f$0.lambda$requestRender$2$PhotoFilterView$EGLThread(this.f$1, this.f$2);
    }
}
