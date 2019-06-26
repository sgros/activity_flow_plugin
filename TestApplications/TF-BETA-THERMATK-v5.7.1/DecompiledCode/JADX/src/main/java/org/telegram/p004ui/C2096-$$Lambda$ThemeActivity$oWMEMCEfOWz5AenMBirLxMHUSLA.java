package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.messenger.AndroidUtilities;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA */
public final /* synthetic */ class C2096-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA implements OnEditorActionListener {
    public static final /* synthetic */ C2096-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA INSTANCE = new C2096-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA();

    private /* synthetic */ C2096-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA() {
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return AndroidUtilities.hideKeyboard(textView);
    }
}
