package org.telegram.p004ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import org.telegram.p004ui.Cells.PollEditTextCell;
import org.telegram.p004ui.PollCreateActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PollCreateActivity$ListAdapter$0Cdu-jo6aZB372n9nZKgtvUqB-g */
public final /* synthetic */ class C1900x73330601 implements OnKeyListener {
    private final /* synthetic */ PollEditTextCell f$0;

    public /* synthetic */ C1900x73330601(PollEditTextCell pollEditTextCell) {
        this.f$0 = pollEditTextCell;
    }

    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        return ListAdapter.lambda$onCreateViewHolder$2(this.f$0, view, i, keyEvent);
    }
}
