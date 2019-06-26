package org.telegram.p004ui.Adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb-uXrprc */
public final /* synthetic */ class C2254-$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb-uXrprc implements OnClickListener {
    private final /* synthetic */ MentionsAdapter f$0;
    private final /* synthetic */ boolean[] f$1;

    public /* synthetic */ C2254-$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb-uXrprc(MentionsAdapter mentionsAdapter, boolean[] zArr) {
        this.f$0 = mentionsAdapter;
        this.f$1 = zArr;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processFoundUser$1$MentionsAdapter(this.f$1, dialogInterface, i);
    }
}
