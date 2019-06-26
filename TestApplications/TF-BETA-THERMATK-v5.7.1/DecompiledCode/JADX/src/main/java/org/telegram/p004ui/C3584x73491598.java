package org.telegram.p004ui;

import org.telegram.messenger.MessageObject;
import org.telegram.p004ui.AudioSelectActivity.ListAdapter;
import org.telegram.p004ui.Cells.AudioCell.AudioCellDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$AudioSelectActivity$ListAdapter$_RHioXAj8YlHkv8lhkCbfXzL_JQ */
public final /* synthetic */ class C3584x73491598 implements AudioCellDelegate {
    private final /* synthetic */ ListAdapter f$0;

    public /* synthetic */ C3584x73491598(ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    public final void startedPlayingAudio(MessageObject messageObject) {
        this.f$0.lambda$onCreateViewHolder$0$AudioSelectActivity$ListAdapter(messageObject);
    }
}
