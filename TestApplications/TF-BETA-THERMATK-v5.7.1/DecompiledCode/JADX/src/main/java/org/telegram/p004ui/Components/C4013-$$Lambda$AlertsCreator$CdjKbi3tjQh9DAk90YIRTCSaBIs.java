package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.NumberPicker.OnScrollListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$CdjKbi3tjQh9DAk90YIRTCSaBIs */
public final /* synthetic */ class C4013-$$Lambda$AlertsCreator$CdjKbi3tjQh9DAk90YIRTCSaBIs implements OnScrollListener {
    private final /* synthetic */ boolean f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ NumberPicker f$2;
    private final /* synthetic */ NumberPicker f$3;

    public /* synthetic */ C4013-$$Lambda$AlertsCreator$CdjKbi3tjQh9DAk90YIRTCSaBIs(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        this.f$0 = z;
        this.f$1 = numberPicker;
        this.f$2 = numberPicker2;
        this.f$3 = numberPicker3;
    }

    public final void onScrollStateChange(NumberPicker numberPicker, int i) {
        AlertsCreator.lambda$createDatePickerDialog$15(this.f$0, this.f$1, this.f$2, this.f$3, numberPicker, i);
    }
}
