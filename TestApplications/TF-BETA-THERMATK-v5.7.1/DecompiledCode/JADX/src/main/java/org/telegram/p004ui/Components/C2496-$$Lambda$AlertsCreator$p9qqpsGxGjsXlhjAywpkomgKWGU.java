package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.Components.AlertsCreator.DatePickerDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU */
public final /* synthetic */ class C2496-$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU implements OnClickListener {
    private final /* synthetic */ boolean f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ NumberPicker f$2;
    private final /* synthetic */ NumberPicker f$3;
    private final /* synthetic */ DatePickerDelegate f$4;

    public /* synthetic */ C2496-$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, DatePickerDelegate datePickerDelegate) {
        this.f$0 = z;
        this.f$1 = numberPicker;
        this.f$2 = numberPicker2;
        this.f$3 = numberPicker3;
        this.f$4 = datePickerDelegate;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createDatePickerDialog$18(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}
