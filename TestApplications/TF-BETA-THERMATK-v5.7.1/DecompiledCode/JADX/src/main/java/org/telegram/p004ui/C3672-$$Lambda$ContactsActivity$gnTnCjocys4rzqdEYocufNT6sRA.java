package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA */
public final /* synthetic */ class C3672-$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA implements OnItemClickListener {
    private final /* synthetic */ ContactsActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3672-$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA(ContactsActivity contactsActivity, int i) {
        this.f$0 = contactsActivity;
        this.f$1 = i;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$1$ContactsActivity(this.f$1, view, i);
    }
}
