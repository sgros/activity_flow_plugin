package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU */
public final /* synthetic */ class C3511-$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU implements RequestDelegate {
    public static final /* synthetic */ C3511-$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU INSTANCE = new C3511-$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU();

    private /* synthetic */ C3511-$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$completeReadTask$147(tLObject, tL_error);
    }
}
