package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.ActionBar.BottomSheet.Builder;
import org.telegram.p004ui.Cells.RadioButtonCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditActivity$Jz5JNMPNPO8bP_0CHPma3fBKfwA */
public final /* synthetic */ class C1393-$$Lambda$ChatEditActivity$Jz5JNMPNPO8bP_0CHPma3fBKfwA implements OnClickListener {
    private final /* synthetic */ ChatEditActivity f$0;
    private final /* synthetic */ RadioButtonCell[] f$1;
    private final /* synthetic */ Builder f$2;

    public /* synthetic */ C1393-$$Lambda$ChatEditActivity$Jz5JNMPNPO8bP_0CHPma3fBKfwA(ChatEditActivity chatEditActivity, RadioButtonCell[] radioButtonCellArr, Builder builder) {
        this.f$0 = chatEditActivity;
        this.f$1 = radioButtonCellArr;
        this.f$2 = builder;
    }

    public final void onClick(View view) {
        this.f$0.lambda$null$7$ChatEditActivity(this.f$1, this.f$2, view);
    }
}
