package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE */
public final /* synthetic */ class C3510-$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE implements RequestDelegate {
    public static final /* synthetic */ C3510-$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE INSTANCE = new C3510-$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE();

    private /* synthetic */ C3510-$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$deleteUserPhoto$58(tLObject, tL_error);
    }
}
