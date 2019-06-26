package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelAdminLogActivity$v_o39r5RzWtyA17rINStweDzJAI */
public final /* synthetic */ class C3609-$$Lambda$ChannelAdminLogActivity$v_o39r5RzWtyA17rINStweDzJAI implements RequestDelegate {
    private final /* synthetic */ ChannelAdminLogActivity f$0;

    public /* synthetic */ C3609-$$Lambda$ChannelAdminLogActivity$v_o39r5RzWtyA17rINStweDzJAI(ChannelAdminLogActivity channelAdminLogActivity) {
        this.f$0 = channelAdminLogActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadAdmins$13$ChannelAdminLogActivity(tLObject, tL_error);
    }
}
