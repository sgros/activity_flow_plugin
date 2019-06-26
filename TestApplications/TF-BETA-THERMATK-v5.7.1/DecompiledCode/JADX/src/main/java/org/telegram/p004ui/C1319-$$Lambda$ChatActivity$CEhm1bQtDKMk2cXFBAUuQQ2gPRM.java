package org.telegram.p004ui;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$CEhm1bQtDKMk2cXFBAUuQQ2gPRM */
public final /* synthetic */ class C1319-$$Lambda$ChatActivity$CEhm1bQtDKMk2cXFBAUuQQ2gPRM implements OnTouchListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ View f$1;
    private final /* synthetic */ Rect f$2;

    public /* synthetic */ C1319-$$Lambda$ChatActivity$CEhm1bQtDKMk2cXFBAUuQQ2gPRM(ChatActivity chatActivity, View view, Rect rect) {
        this.f$0 = chatActivity;
        this.f$1 = view;
        this.f$2 = rect;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f$0.lambda$createMenu$63$ChatActivity(this.f$1, this.f$2, view, motionEvent);
    }
}
