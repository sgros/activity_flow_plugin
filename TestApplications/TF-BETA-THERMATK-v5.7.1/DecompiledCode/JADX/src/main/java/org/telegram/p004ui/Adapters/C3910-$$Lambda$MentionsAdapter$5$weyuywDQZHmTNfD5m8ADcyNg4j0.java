package org.telegram.p004ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.p004ui.Adapters.MentionsAdapter.C22785;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0 */
public final /* synthetic */ class C3910-$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0 implements RequestDelegate {
    private final /* synthetic */ C22785 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ SparseArray f$3;
    private final /* synthetic */ MessagesController f$4;

    public /* synthetic */ C3910-$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0(C22785 c22785, int i, ArrayList arrayList, SparseArray sparseArray, MessagesController messagesController) {
        this.f$0 = c22785;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = sparseArray;
        this.f$4 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$1$MentionsAdapter$5(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
