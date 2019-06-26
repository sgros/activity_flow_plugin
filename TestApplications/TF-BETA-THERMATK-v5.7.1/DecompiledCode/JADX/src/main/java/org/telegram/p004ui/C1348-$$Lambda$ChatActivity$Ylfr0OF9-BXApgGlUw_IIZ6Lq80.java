package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.Cells.CheckBoxCell;
import org.telegram.tgnet.TLRPC.TL_messages_requestUrlAuth;
import org.telegram.tgnet.TLRPC.TL_urlAuthResultRequest;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$Ylfr0OF9-BXApgGlUw_IIZ6Lq80 */
public final /* synthetic */ class C1348-$$Lambda$ChatActivity$Ylfr0OF9-BXApgGlUw_IIZ6Lq80 implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ CheckBoxCell[] f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ TL_messages_requestUrlAuth f$3;
    private final /* synthetic */ TL_urlAuthResultRequest f$4;

    public /* synthetic */ C1348-$$Lambda$ChatActivity$Ylfr0OF9-BXApgGlUw_IIZ6Lq80(ChatActivity chatActivity, CheckBoxCell[] checkBoxCellArr, String str, TL_messages_requestUrlAuth tL_messages_requestUrlAuth, TL_urlAuthResultRequest tL_urlAuthResultRequest) {
        this.f$0 = chatActivity;
        this.f$1 = checkBoxCellArr;
        this.f$2 = str;
        this.f$3 = tL_messages_requestUrlAuth;
        this.f$4 = tL_urlAuthResultRequest;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showRequestUrlAlert$90$ChatActivity(this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}
