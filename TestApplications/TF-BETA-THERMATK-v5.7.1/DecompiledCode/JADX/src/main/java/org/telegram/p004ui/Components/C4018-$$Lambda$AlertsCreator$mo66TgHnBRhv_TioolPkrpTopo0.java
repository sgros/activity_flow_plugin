package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.NumberPicker.OnValueChangeListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$mo66TgHnBRhv_TioolPkrpTopo0 */
public final /* synthetic */ class C4018-$$Lambda$AlertsCreator$mo66TgHnBRhv_TioolPkrpTopo0 implements OnValueChangeListener {
    private final /* synthetic */ NumberPicker f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ NumberPicker f$2;

    public /* synthetic */ C4018-$$Lambda$AlertsCreator$mo66TgHnBRhv_TioolPkrpTopo0(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        this.f$0 = numberPicker;
        this.f$1 = numberPicker2;
        this.f$2 = numberPicker3;
    }

    public final void onValueChange(NumberPicker numberPicker, int i, int i2) {
        AlertsCreator.updateDayPicker(this.f$0, this.f$1, this.f$2);
    }
}
