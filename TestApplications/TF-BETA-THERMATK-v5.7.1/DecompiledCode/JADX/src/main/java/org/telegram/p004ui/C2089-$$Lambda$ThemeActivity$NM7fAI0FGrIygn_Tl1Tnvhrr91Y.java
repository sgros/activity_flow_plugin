package org.telegram.p004ui;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.TimePicker;
import org.telegram.p004ui.Cells.TextSettingsCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y */
public final /* synthetic */ class C2089-$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y implements OnTimeSetListener {
    private final /* synthetic */ ThemeActivity f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TextSettingsCell f$2;

    public /* synthetic */ C2089-$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y(ThemeActivity themeActivity, int i, TextSettingsCell textSettingsCell) {
        this.f$0 = themeActivity;
        this.f$1 = i;
        this.f$2 = textSettingsCell;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.f$0.lambda$null$4$ThemeActivity(this.f$1, this.f$2, timePicker, i, i2);
    }
}
