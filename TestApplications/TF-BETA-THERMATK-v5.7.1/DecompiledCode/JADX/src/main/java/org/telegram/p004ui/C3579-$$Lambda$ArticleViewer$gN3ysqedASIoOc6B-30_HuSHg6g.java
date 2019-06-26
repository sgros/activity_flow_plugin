package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemLongClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$gN3ysqedASIoOc6B-30_HuSHg6g */
public final /* synthetic */ class C3579-$$Lambda$ArticleViewer$gN3ysqedASIoOc6B-30_HuSHg6g implements OnItemLongClickListener {
    private final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ C3579-$$Lambda$ArticleViewer$gN3ysqedASIoOc6B-30_HuSHg6g(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    public final boolean onItemClick(View view, int i) {
        return this.f$0.lambda$setParentActivity$8$ArticleViewer(view, i);
    }
}
