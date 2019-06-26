package org.telegram.p004ui;

import org.telegram.p004ui.Cells.ManageChatUserCell;
import org.telegram.p004ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate;
import org.telegram.p004ui.ChatUsersActivity.SearchAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$SearchAdapter$fSIulZwDi8NAXDLh6m3-GQxlD8U */
public final /* synthetic */ class C3661x9739b102 implements ManageChatUserCellDelegate {
    private final /* synthetic */ SearchAdapter f$0;

    public /* synthetic */ C3661x9739b102(SearchAdapter searchAdapter) {
        this.f$0 = searchAdapter;
    }

    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        return this.f$0.lambda$onCreateViewHolder$4$ChatUsersActivity$SearchAdapter(manageChatUserCell, z);
    }
}
