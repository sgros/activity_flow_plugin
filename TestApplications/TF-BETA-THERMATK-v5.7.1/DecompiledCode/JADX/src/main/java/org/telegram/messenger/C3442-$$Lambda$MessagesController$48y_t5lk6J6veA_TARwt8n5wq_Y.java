package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y */
public final /* synthetic */ class C3442-$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y implements RequestDelegate {
    public static final /* synthetic */ C3442-$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y INSTANCE = new C3442-$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y();

    private /* synthetic */ C3442-$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$hideReportSpam$23(tLObject, tL_error);
    }
}
