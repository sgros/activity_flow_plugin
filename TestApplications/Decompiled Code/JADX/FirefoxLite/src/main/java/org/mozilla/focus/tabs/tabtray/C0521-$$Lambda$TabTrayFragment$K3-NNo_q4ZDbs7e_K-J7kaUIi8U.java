package org.mozilla.focus.tabs.tabtray;

import android.support.p001v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.tabs.tabtray.-$$Lambda$TabTrayFragment$K3-NNo_q4ZDbs7e_K-J7kaUIi8U */
public final /* synthetic */ class C0521-$$Lambda$TabTrayFragment$K3-NNo_q4ZDbs7e_K-J7kaUIi8U implements OnTouchListener {
    private final /* synthetic */ GestureDetectorCompat f$0;

    public /* synthetic */ C0521-$$Lambda$TabTrayFragment$K3-NNo_q4ZDbs7e_K-J7kaUIi8U(GestureDetectorCompat gestureDetectorCompat) {
        this.f$0 = gestureDetectorCompat;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return TabTrayFragment.lambda$setupTapBackgroundToExpand$5(this.f$0, view, motionEvent);
    }
}
