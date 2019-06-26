package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4 */
public final /* synthetic */ class C3498-$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4 implements RequestDelegate {
    public static final /* synthetic */ C3498-$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4 INSTANCE = new C3498-$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4();

    private /* synthetic */ C3498-$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$markMentionsAsRead$149(tLObject, tL_error);
    }
}
