package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM */
public final /* synthetic */ class C3363-$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM implements RequestDelegate {
    public static final /* synthetic */ C3363-$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM INSTANCE = new C3363-$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM();

    private /* synthetic */ C3363-$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        ContactsController.lambda$resetImportedContacts$9(tLObject, tL_error);
    }
}
