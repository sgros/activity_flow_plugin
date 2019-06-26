package org.telegram.p004ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import org.telegram.p004ui.PassportActivity.PhoneConfirmationView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$PhoneConfirmationView$Hi5vGNsckWBWmshMz8y5BhPDMNw */
public final /* synthetic */ class C1747xbeb2de8e implements OnKeyListener {
    private final /* synthetic */ PhoneConfirmationView f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1747xbeb2de8e(PhoneConfirmationView phoneConfirmationView, int i) {
        this.f$0 = phoneConfirmationView;
        this.f$1 = i;
    }

    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$setParams$4$PassportActivity$PhoneConfirmationView(this.f$1, view, i, keyEvent);
    }
}
