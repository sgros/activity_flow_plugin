package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.Components.NumberPicker;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PasscodeActivity$gpdG6ADQUdojRTN0OUtZLDO-4yI */
public final /* synthetic */ class C1698-$$Lambda$PasscodeActivity$gpdG6ADQUdojRTN0OUtZLDO-4yI implements OnClickListener {
    private final /* synthetic */ PasscodeActivity f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1698-$$Lambda$PasscodeActivity$gpdG6ADQUdojRTN0OUtZLDO-4yI(PasscodeActivity passcodeActivity, NumberPicker numberPicker, int i) {
        this.f$0 = passcodeActivity;
        this.f$1 = numberPicker;
        this.f$2 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$3$PasscodeActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
