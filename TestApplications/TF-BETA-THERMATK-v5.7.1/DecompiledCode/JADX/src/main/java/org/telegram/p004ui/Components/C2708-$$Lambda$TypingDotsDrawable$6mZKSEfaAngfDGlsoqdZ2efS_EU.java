package org.telegram.p004ui.Components;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$TypingDotsDrawable$6mZKSEfaAngfDGlsoqdZ2efS_EU */
public final /* synthetic */ class C2708-$$Lambda$TypingDotsDrawable$6mZKSEfaAngfDGlsoqdZ2efS_EU implements Runnable {
    private final /* synthetic */ TypingDotsDrawable f$0;

    public /* synthetic */ C2708-$$Lambda$TypingDotsDrawable$6mZKSEfaAngfDGlsoqdZ2efS_EU(TypingDotsDrawable typingDotsDrawable) {
        this.f$0 = typingDotsDrawable;
    }

    public final void run() {
        this.f$0.checkUpdate();
    }
}
