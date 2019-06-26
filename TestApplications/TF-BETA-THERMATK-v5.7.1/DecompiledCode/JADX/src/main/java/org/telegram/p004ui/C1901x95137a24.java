package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.p004ui.Cells.PollEditTextCell;
import org.telegram.p004ui.PollCreateActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PollCreateActivity$ListAdapter$fHXG5XRT2mioX4wywMDd12jepYQ */
public final /* synthetic */ class C1901x95137a24 implements OnEditorActionListener {
    private final /* synthetic */ ListAdapter f$0;
    private final /* synthetic */ PollEditTextCell f$1;

    public /* synthetic */ C1901x95137a24(ListAdapter listAdapter, PollEditTextCell pollEditTextCell) {
        this.f$0 = listAdapter;
        this.f$1 = pollEditTextCell;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$onCreateViewHolder$1$PollCreateActivity$ListAdapter(this.f$1, textView, i, keyEvent);
    }
}
