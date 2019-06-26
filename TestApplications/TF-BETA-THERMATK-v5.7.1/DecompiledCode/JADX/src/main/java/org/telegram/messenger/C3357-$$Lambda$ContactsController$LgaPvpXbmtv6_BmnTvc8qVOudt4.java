package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4 */
public final /* synthetic */ class C3357-$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4 implements RequestDelegate {
    private final /* synthetic */ ContactsController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3357-$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4(ContactsController contactsController, int i) {
        this.f$0 = contactsController;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadContacts$27$ContactsController(this.f$1, tLObject, tL_error);
    }
}
