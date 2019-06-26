package org.telegram.p004ui;

import org.telegram.p004ui.Cells.ManageChatUserCell;
import org.telegram.p004ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate;
import org.telegram.p004ui.ChatUsersActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$ListAdapter$eixJJWW-1mDLHNoI-EEjSfsGLFc */
public final /* synthetic */ class C3659xde0325c0 implements ManageChatUserCellDelegate {
    private final /* synthetic */ ListAdapter f$0;

    public /* synthetic */ C3659xde0325c0(ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        return this.f$0.lambda$onCreateViewHolder$0$ChatUsersActivity$ListAdapter(manageChatUserCell, z);
    }
}
