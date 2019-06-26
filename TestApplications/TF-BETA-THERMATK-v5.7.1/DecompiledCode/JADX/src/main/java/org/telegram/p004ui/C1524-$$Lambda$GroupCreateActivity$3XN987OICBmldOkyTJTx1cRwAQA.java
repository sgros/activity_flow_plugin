package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.Cells.CheckBoxCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$GroupCreateActivity$3XN987OICBmldOkyTJTx1cRwAQA */
public final /* synthetic */ class C1524-$$Lambda$GroupCreateActivity$3XN987OICBmldOkyTJTx1cRwAQA implements OnClickListener {
    private final /* synthetic */ CheckBoxCell[] f$0;

    public /* synthetic */ C1524-$$Lambda$GroupCreateActivity$3XN987OICBmldOkyTJTx1cRwAQA(CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = checkBoxCellArr;
    }

    public final void onClick(View view) {
        this.f$0[0].setChecked(this.f$0[0].isChecked() ^ 1, true);
    }
}
