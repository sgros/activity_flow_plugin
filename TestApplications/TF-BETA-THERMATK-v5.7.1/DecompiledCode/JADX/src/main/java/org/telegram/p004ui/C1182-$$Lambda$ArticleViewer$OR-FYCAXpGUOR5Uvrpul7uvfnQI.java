package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$OR-FYCAXpGUOR5Uvrpul7uvfnQI */
public final /* synthetic */ class C1182-$$Lambda$ArticleViewer$OR-FYCAXpGUOR5Uvrpul7uvfnQI implements OnClickListener {
    private final /* synthetic */ ArticleViewer f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C1182-$$Lambda$ArticleViewer$OR-FYCAXpGUOR5Uvrpul7uvfnQI(ArticleViewer articleViewer, String str) {
        this.f$0 = articleViewer;
        this.f$1 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showCopyPopup$0$ArticleViewer(this.f$1, dialogInterface, i);
    }
}