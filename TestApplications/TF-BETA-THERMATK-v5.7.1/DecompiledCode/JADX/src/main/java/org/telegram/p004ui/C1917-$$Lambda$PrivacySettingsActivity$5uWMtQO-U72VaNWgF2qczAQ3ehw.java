package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.Cells.TextCheckCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacySettingsActivity$5uWMtQO-U72VaNWgF2qczAQ3ehw */
public final /* synthetic */ class C1917-$$Lambda$PrivacySettingsActivity$5uWMtQO-U72VaNWgF2qczAQ3ehw implements OnClickListener {
    private final /* synthetic */ PrivacySettingsActivity f$0;
    private final /* synthetic */ TextCheckCell f$1;

    public /* synthetic */ C1917-$$Lambda$PrivacySettingsActivity$5uWMtQO-U72VaNWgF2qczAQ3ehw(PrivacySettingsActivity privacySettingsActivity, TextCheckCell textCheckCell) {
        this.f$0 = privacySettingsActivity;
        this.f$1 = textCheckCell;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$11$PrivacySettingsActivity(this.f$1, dialogInterface, i);
    }
}
