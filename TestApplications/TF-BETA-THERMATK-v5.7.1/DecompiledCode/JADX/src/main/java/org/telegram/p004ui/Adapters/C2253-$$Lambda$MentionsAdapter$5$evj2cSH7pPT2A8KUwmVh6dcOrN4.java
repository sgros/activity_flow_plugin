package org.telegram.p004ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.p004ui.Adapters.MentionsAdapter.C22785;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4 */
public final /* synthetic */ class C2253-$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4 implements Runnable {
    private final /* synthetic */ C22785 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ SparseArray f$3;
    private final /* synthetic */ TL_error f$4;
    private final /* synthetic */ TLObject f$5;
    private final /* synthetic */ MessagesController f$6;

    public /* synthetic */ C2253-$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4(C22785 c22785, int i, ArrayList arrayList, SparseArray sparseArray, TL_error tL_error, TLObject tLObject, MessagesController messagesController) {
        this.f$0 = c22785;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = sparseArray;
        this.f$4 = tL_error;
        this.f$5 = tLObject;
        this.f$6 = messagesController;
    }

    public final void run() {
        this.f$0.lambda$null$0$MentionsAdapter$5(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
