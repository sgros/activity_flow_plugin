package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.Components.EditTextBoldCursor;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA */
public final /* synthetic */ class C2097-$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA implements OnShowListener {
    private final /* synthetic */ EditTextBoldCursor f$0;

    public /* synthetic */ C2097-$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA(EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextBoldCursor;
    }

    public final void onShow(DialogInterface dialogInterface) {
        AndroidUtilities.runOnUIThread(new C2095-$$Lambda$ThemeActivity$m4EmqHR619o6faN5Q_KiPQtpKTk(this.f$0));
    }
}
