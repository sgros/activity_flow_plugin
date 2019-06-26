package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM */
public final /* synthetic */ class C3458-$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM implements RequestDelegate {
    public static final /* synthetic */ C3458-$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM INSTANCE = new C3458-$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM();

    private /* synthetic */ C3458-$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$markMentionMessageAsRead$142(tLObject, tL_error);
    }
}
