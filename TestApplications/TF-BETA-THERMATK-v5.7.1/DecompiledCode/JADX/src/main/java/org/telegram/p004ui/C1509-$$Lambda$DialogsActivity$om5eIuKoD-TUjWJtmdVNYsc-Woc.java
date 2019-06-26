package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesController;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DialogsActivity$om5eIuKoD-TUjWJtmdVNYsc-Woc */
public final /* synthetic */ class C1509-$$Lambda$DialogsActivity$om5eIuKoD-TUjWJtmdVNYsc-Woc implements OnClickListener {
    public static final /* synthetic */ C1509-$$Lambda$DialogsActivity$om5eIuKoD-TUjWJtmdVNYsc-Woc INSTANCE = new C1509-$$Lambda$DialogsActivity$om5eIuKoD-TUjWJtmdVNYsc-Woc();

    private /* synthetic */ C1509-$$Lambda$DialogsActivity$om5eIuKoD-TUjWJtmdVNYsc-Woc() {
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askedAboutMiuiLockscreen", true).commit();
    }
}
