package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.AndroidUtilities.VcardItem;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhonebookShareActivity$g-17B0C2dU4u_JSxmNJh3l3Yxbg */
public final /* synthetic */ class C1829-$$Lambda$PhonebookShareActivity$g-17B0C2dU4u_JSxmNJh3l3Yxbg implements OnClickListener {
    private final /* synthetic */ PhonebookShareActivity f$0;
    private final /* synthetic */ VcardItem f$1;

    public /* synthetic */ C1829-$$Lambda$PhonebookShareActivity$g-17B0C2dU4u_JSxmNJh3l3Yxbg(PhonebookShareActivity phonebookShareActivity, VcardItem vcardItem) {
        this.f$0 = phonebookShareActivity;
        this.f$1 = vcardItem;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$0$PhonebookShareActivity(this.f$1, dialogInterface, i);
    }
}
