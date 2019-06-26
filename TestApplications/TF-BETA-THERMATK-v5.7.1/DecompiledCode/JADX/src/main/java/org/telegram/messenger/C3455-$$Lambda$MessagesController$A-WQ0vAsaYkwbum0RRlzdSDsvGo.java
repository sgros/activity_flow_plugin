package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$A-WQ0vAsaYkwbum0RRlzdSDsvGo */
public final /* synthetic */ class C3455-$$Lambda$MessagesController$A-WQ0vAsaYkwbum0RRlzdSDsvGo implements RequestDelegate {
    public static final /* synthetic */ C3455-$$Lambda$MessagesController$A-WQ0vAsaYkwbum0RRlzdSDsvGo INSTANCE = new C3455-$$Lambda$MessagesController$A-WQ0vAsaYkwbum0RRlzdSDsvGo();

    private /* synthetic */ C3455-$$Lambda$MessagesController$A-WQ0vAsaYkwbum0RRlzdSDsvGo() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$reportSpam$25(tLObject, tL_error);
    }
}
