package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ */
public final /* synthetic */ class C3359-$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ implements RequestDelegate {
    private final /* synthetic */ ContactsController f$0;

    public /* synthetic */ C3359-$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ(ContactsController contactsController) {
        this.f$0 = contactsController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadPrivacySettings$57$ContactsController(tLObject, tL_error);
    }
}
