package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.WallpaperParallaxEffect.Callback;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$SizeNotifierFrameLayout$9xVW1u9E8sqKGYKEgYUca0gqvJA */
public final /* synthetic */ class C4057-$$Lambda$SizeNotifierFrameLayout$9xVW1u9E8sqKGYKEgYUca0gqvJA implements Callback {
    private final /* synthetic */ SizeNotifierFrameLayout f$0;

    public /* synthetic */ C4057-$$Lambda$SizeNotifierFrameLayout$9xVW1u9E8sqKGYKEgYUca0gqvJA(SizeNotifierFrameLayout sizeNotifierFrameLayout) {
        this.f$0 = sizeNotifierFrameLayout;
    }

    public final void onOffsetsChanged(int i, int i2) {
        this.f$0.lambda$setBackgroundImage$0$SizeNotifierFrameLayout(i, i2);
    }
}
