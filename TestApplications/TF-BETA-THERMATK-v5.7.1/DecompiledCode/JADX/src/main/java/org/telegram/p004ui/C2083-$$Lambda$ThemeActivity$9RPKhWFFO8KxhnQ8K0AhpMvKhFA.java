package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.Components.EditTextBoldCursor;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA */
public final /* synthetic */ class C2083-$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA implements OnClickListener {
    private final /* synthetic */ ThemeActivity f$0;
    private final /* synthetic */ EditTextBoldCursor f$1;
    private final /* synthetic */ AlertDialog f$2;

    public /* synthetic */ C2083-$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA(ThemeActivity themeActivity, EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog) {
        this.f$0 = themeActivity;
        this.f$1 = editTextBoldCursor;
        this.f$2 = alertDialog;
    }

    public final void onClick(View view) {
        this.f$0.lambda$openThemeCreate$10$ThemeActivity(this.f$1, this.f$2, view);
    }
}
