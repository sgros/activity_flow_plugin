package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.AndroidUtilities.VcardItem;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhonebookShareActivity$0f5AxQdt4YhBWK3SWXLFBDqSogw */
public final /* synthetic */ class C1828-$$Lambda$PhonebookShareActivity$0f5AxQdt4YhBWK3SWXLFBDqSogw implements OnClickListener {
    private final /* synthetic */ PhonebookShareActivity f$0;
    private final /* synthetic */ VcardItem f$1;

    public /* synthetic */ C1828-$$Lambda$PhonebookShareActivity$0f5AxQdt4YhBWK3SWXLFBDqSogw(PhonebookShareActivity phonebookShareActivity, VcardItem vcardItem) {
        this.f$0 = phonebookShareActivity;
        this.f$1 = vcardItem;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$2$PhonebookShareActivity(this.f$1, dialogInterface, i);
    }
}
