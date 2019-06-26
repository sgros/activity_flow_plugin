package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.Cells.CheckBoxCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI */
public final /* synthetic */ class C1529-$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI implements OnClickListener {
    private final /* synthetic */ GroupCreateActivity f$0;
    private final /* synthetic */ CheckBoxCell[] f$1;

    public /* synthetic */ C1529-$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI(GroupCreateActivity groupCreateActivity, CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = groupCreateActivity;
        this.f$1 = checkBoxCellArr;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onDonePressed$6$GroupCreateActivity(this.f$1, dialogInterface, i);
    }
}
