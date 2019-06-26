package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.Cells.CheckBoxCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$wvYiDIx4eLsIf8wqj2bNoytWsyQ */
public final /* synthetic */ class C1381-$$Lambda$ChatActivity$wvYiDIx4eLsIf8wqj2bNoytWsyQ implements OnClickListener {
    private final /* synthetic */ CheckBoxCell[] f$0;

    public /* synthetic */ C1381-$$Lambda$ChatActivity$wvYiDIx4eLsIf8wqj2bNoytWsyQ(CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = checkBoxCellArr;
    }

    public final void onClick(View view) {
        ChatActivity.lambda$showRequestUrlAlert$85(this.f$0, view);
    }
}
