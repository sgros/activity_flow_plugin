package org.telegram.p004ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$MGSGAQzWCEU3w9PF3AH-evurh-k */
public final /* synthetic */ class C1181-$$Lambda$ArticleViewer$MGSGAQzWCEU3w9PF3AH-evurh-k implements OnTouchListener {
    private final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ C1181-$$Lambda$ArticleViewer$MGSGAQzWCEU3w9PF3AH-evurh-k(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f$0.lambda$showPopup$1$ArticleViewer(view, motionEvent);
    }
}
