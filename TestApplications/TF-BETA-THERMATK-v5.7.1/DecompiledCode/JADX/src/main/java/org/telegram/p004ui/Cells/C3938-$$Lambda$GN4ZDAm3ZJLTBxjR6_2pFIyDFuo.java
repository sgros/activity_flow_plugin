package org.telegram.p004ui.Cells;

import org.telegram.p004ui.Components.SeekBarView.SeekBarViewDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Cells.-$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo */
public final /* synthetic */ class C3938-$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo implements SeekBarViewDelegate {
    private final /* synthetic */ BrightnessControlCell f$0;

    public /* synthetic */ C3938-$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo(BrightnessControlCell brightnessControlCell) {
        this.f$0 = brightnessControlCell;
    }

    public final void onSeekBarDrag(float f) {
        this.f$0.didChangedValue(f);
    }
}
