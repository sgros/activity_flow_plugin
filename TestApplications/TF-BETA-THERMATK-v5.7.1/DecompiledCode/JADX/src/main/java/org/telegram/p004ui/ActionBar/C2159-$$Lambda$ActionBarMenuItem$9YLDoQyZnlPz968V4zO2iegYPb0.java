package org.telegram.p004ui.ActionBar;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.ActionBar.-$$Lambda$ActionBarMenuItem$9YLDoQyZnlPz968V4zO2iegYPb0 */
public final /* synthetic */ class C2159-$$Lambda$ActionBarMenuItem$9YLDoQyZnlPz968V4zO2iegYPb0 implements OnKeyListener {
    private final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ C2159-$$Lambda$ActionBarMenuItem$9YLDoQyZnlPz968V4zO2iegYPb0(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$toggleSubMenu$6$ActionBarMenuItem(view, i, keyEvent);
    }
}
