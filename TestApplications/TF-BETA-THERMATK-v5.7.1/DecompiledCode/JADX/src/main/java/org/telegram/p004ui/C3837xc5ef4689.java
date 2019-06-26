package org.telegram.p004ui;

import org.telegram.p004ui.Cells.ManageChatUserCell;
import org.telegram.p004ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate;
import org.telegram.p004ui.PrivacyUsersActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacyUsersActivity$ListAdapter$ah_jQyMOHlRewlEcZgEQccTwPTg */
public final /* synthetic */ class C3837xc5ef4689 implements ManageChatUserCellDelegate {
    private final /* synthetic */ ListAdapter f$0;

    public /* synthetic */ C3837xc5ef4689(ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        return this.f$0.lambda$onCreateViewHolder$0$PrivacyUsersActivity$ListAdapter(manageChatUserCell, z);
    }
}
