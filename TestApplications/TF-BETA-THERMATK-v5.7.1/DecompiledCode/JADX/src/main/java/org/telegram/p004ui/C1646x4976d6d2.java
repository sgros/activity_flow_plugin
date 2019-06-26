package org.telegram.p004ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import org.telegram.p004ui.LoginActivity.LoginActivitySmsView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivitySmsView$W4f4bbr6ANrn17O_fi8CZ_C8uvk */
public final /* synthetic */ class C1646x4976d6d2 implements OnKeyListener {
    private final /* synthetic */ LoginActivitySmsView f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1646x4976d6d2(LoginActivitySmsView loginActivitySmsView, int i) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = i;
    }

    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$setParams$3$LoginActivity$LoginActivitySmsView(this.f$1, view, i, keyEvent);
    }
}
