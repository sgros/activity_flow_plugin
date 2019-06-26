package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ-pkFKO1Y */
public final /* synthetic */ class C2553-$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ-pkFKO1Y implements OnClickListener {
    private final /* synthetic */ EditTextCaption f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ EditTextBoldCursor f$3;

    public /* synthetic */ C2553-$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ-pkFKO1Y(EditTextCaption editTextCaption, int i, int i2, EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextCaption;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = editTextBoldCursor;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$makeSelectedUrl$0$EditTextCaption(this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}
