package org.telegram.p004ui.Components;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AudioPlayerAlert$bZwv44or3-083Y0MYIMNRo1ORcQ */
public final /* synthetic */ class C4025-$$Lambda$AudioPlayerAlert$bZwv44or3-083Y0MYIMNRo1ORcQ implements DialogsActivityDelegate {
    private final /* synthetic */ AudioPlayerAlert f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C4025-$$Lambda$AudioPlayerAlert$bZwv44or3-083Y0MYIMNRo1ORcQ(AudioPlayerAlert audioPlayerAlert, ArrayList arrayList) {
        this.f$0 = audioPlayerAlert;
        this.f$1 = arrayList;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onSubItemClick$10$AudioPlayerAlert(this.f$1, dialogsActivity, arrayList, charSequence, z);
    }
}
