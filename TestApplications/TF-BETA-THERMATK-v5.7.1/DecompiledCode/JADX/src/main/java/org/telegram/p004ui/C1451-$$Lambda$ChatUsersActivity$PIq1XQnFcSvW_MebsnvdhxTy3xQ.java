package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_chatAdminRights;
import org.telegram.tgnet.TLRPC.TL_chatBannedRights;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ */
public final /* synthetic */ class C1451-$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ implements OnClickListener {
    private final /* synthetic */ ChatUsersActivity f$0;
    private final /* synthetic */ CharSequence[] f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ TL_chatAdminRights f$3;
    private final /* synthetic */ TLObject f$4;
    private final /* synthetic */ TL_chatBannedRights f$5;

    public /* synthetic */ C1451-$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ(ChatUsersActivity chatUsersActivity, CharSequence[] charSequenceArr, int i, TL_chatAdminRights tL_chatAdminRights, TLObject tLObject, TL_chatBannedRights tL_chatBannedRights) {
        this.f$0 = chatUsersActivity;
        this.f$1 = charSequenceArr;
        this.f$2 = i;
        this.f$3 = tL_chatAdminRights;
        this.f$4 = tLObject;
        this.f$5 = tL_chatBannedRights;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createMenuForParticipant$14$ChatUsersActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, dialogInterface, i);
    }
}
