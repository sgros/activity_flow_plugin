package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw */
public final /* synthetic */ class C1487-$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw implements OnClickListener {
    private final /* synthetic */ DataSettingsActivity f$0;
    private final /* synthetic */ SharedPreferences f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1487-$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw(DataSettingsActivity dataSettingsActivity, SharedPreferences sharedPreferences, int i) {
        this.f$0 = dataSettingsActivity;
        this.f$1 = sharedPreferences;
        this.f$2 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$1$DataSettingsActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
