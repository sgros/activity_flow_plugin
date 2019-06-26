package org.telegram.p004ui;

import org.telegram.p004ui.Components.SeekBar.SeekBarDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js */
public final /* synthetic */ class C3578-$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js implements SeekBarDelegate {
    private final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ C3578-$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    public final void onSeekBarDrag(float f) {
        this.f$0.lambda$setParentActivity$19$ArticleViewer(f);
    }
}
