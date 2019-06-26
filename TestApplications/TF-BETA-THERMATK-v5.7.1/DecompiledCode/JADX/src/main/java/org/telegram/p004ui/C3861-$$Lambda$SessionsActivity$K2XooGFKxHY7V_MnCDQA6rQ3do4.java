package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4 */
public final /* synthetic */ class C3861-$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4 implements RequestDelegate {
    private final /* synthetic */ SessionsActivity f$0;

    public /* synthetic */ C3861-$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4(SessionsActivity sessionsActivity) {
        this.f$0 = sessionsActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadSessions$15$SessionsActivity(tLObject, tL_error);
    }
}
