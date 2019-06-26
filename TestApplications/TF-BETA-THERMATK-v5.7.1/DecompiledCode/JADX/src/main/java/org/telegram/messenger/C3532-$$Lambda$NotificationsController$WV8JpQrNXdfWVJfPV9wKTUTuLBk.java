package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk */
public final /* synthetic */ class C3532-$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk implements RequestDelegate {
    public static final /* synthetic */ C3532-$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk INSTANCE = new C3532-$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk();

    private /* synthetic */ C3532-$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        NotificationsController.lambda$updateServerNotificationsSettings$37(tLObject, tL_error);
    }
}
