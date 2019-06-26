package org.telegram.p004ui;

import org.telegram.p004ui.Components.RecyclerListView;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ContentPreviewViewer$EMKDqwNyTHEkiYf1BXP5lN4E1U8 */
public final /* synthetic */ class C1476-$$Lambda$ContentPreviewViewer$EMKDqwNyTHEkiYf1BXP5lN4E1U8 implements Runnable {
    private final /* synthetic */ RecyclerListView f$0;
    private final /* synthetic */ Object f$1;

    public /* synthetic */ C1476-$$Lambda$ContentPreviewViewer$EMKDqwNyTHEkiYf1BXP5lN4E1U8(RecyclerListView recyclerListView, Object obj) {
        this.f$0 = recyclerListView;
        this.f$1 = obj;
    }

    public final void run() {
        ContentPreviewViewer.lambda$onTouch$0(this.f$0, this.f$1);
    }
}
