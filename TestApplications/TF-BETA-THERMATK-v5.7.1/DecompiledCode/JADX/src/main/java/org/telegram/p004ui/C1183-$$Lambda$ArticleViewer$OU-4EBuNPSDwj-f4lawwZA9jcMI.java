package org.telegram.p004ui;

import org.telegram.p004ui.ArticleViewer.BlockChannelCell;
import org.telegram.p004ui.ArticleViewer.WebpageAdapter;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$OU-4EBuNPSDwj-f4lawwZA9jcMI */
public final /* synthetic */ class C1183-$$Lambda$ArticleViewer$OU-4EBuNPSDwj-f4lawwZA9jcMI implements Runnable {
    private final /* synthetic */ ArticleViewer f$0;
    private final /* synthetic */ WebpageAdapter f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ BlockChannelCell f$5;

    public /* synthetic */ C1183-$$Lambda$ArticleViewer$OU-4EBuNPSDwj-f4lawwZA9jcMI(ArticleViewer articleViewer, WebpageAdapter webpageAdapter, TL_error tL_error, TLObject tLObject, int i, BlockChannelCell blockChannelCell) {
        this.f$0 = articleViewer;
        this.f$1 = webpageAdapter;
        this.f$2 = tL_error;
        this.f$3 = tLObject;
        this.f$4 = i;
        this.f$5 = blockChannelCell;
    }

    public final void run() {
        this.f$0.lambda$null$30$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
