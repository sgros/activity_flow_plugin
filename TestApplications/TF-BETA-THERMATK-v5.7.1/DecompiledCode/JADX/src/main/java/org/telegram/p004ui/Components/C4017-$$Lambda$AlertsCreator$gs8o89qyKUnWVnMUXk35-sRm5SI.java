package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.NumberPicker.OnScrollListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$gs8o89qyKUnWVnMUXk35-sRm5SI */
public final /* synthetic */ class C4017-$$Lambda$AlertsCreator$gs8o89qyKUnWVnMUXk35-sRm5SI implements OnScrollListener {
    private final /* synthetic */ boolean f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ NumberPicker f$2;
    private final /* synthetic */ NumberPicker f$3;

    public /* synthetic */ C4017-$$Lambda$AlertsCreator$gs8o89qyKUnWVnMUXk35-sRm5SI(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        this.f$0 = z;
        this.f$1 = numberPicker;
        this.f$2 = numberPicker2;
        this.f$3 = numberPicker3;
    }

    public final void onScrollStateChange(NumberPicker numberPicker, int i) {
        AlertsCreator.lambda$createDatePickerDialog$12(this.f$0, this.f$1, this.f$2, this.f$3, numberPicker, i);
    }
}