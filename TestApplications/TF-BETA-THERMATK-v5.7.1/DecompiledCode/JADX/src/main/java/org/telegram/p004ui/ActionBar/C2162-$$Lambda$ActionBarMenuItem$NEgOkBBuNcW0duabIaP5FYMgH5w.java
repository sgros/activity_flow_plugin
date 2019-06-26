package org.telegram.p004ui.ActionBar;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.ActionBar.-$$Lambda$ActionBarMenuItem$NEgOkBBuNcW0duabIaP5FYMgH5w */
public final /* synthetic */ class C2162-$$Lambda$ActionBarMenuItem$NEgOkBBuNcW0duabIaP5FYMgH5w implements OnTouchListener {
    private final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ C2162-$$Lambda$ActionBarMenuItem$NEgOkBBuNcW0duabIaP5FYMgH5w(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f$0.lambda$createPopupLayout$1$ActionBarMenuItem(view, motionEvent);
    }
}
