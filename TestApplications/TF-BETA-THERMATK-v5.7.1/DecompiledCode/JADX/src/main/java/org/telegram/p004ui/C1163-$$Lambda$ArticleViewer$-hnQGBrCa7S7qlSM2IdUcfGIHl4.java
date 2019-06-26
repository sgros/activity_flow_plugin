package org.telegram.p004ui;

import org.telegram.p004ui.ArticleViewer.BlockChannelCell;
import org.telegram.tgnet.TLRPC.TL_channels_joinChannel;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$-hnQGBrCa7S7qlSM2IdUcfGIHl4 */
public final /* synthetic */ class C1163-$$Lambda$ArticleViewer$-hnQGBrCa7S7qlSM2IdUcfGIHl4 implements Runnable {
    private final /* synthetic */ ArticleViewer f$0;
    private final /* synthetic */ BlockChannelCell f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ TL_error f$3;
    private final /* synthetic */ TL_channels_joinChannel f$4;

    public /* synthetic */ C1163-$$Lambda$ArticleViewer$-hnQGBrCa7S7qlSM2IdUcfGIHl4(ArticleViewer articleViewer, BlockChannelCell blockChannelCell, int i, TL_error tL_error, TL_channels_joinChannel tL_channels_joinChannel) {
        this.f$0 = articleViewer;
        this.f$1 = blockChannelCell;
        this.f$2 = i;
        this.f$3 = tL_error;
        this.f$4 = tL_channels_joinChannel;
    }

    public final void run() {
        this.f$0.lambda$null$32$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
