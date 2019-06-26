package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelAdminLogActivity$eETtXTeGU9y5fKwho2AV8a_Izoc */
public final /* synthetic */ class C3607-$$Lambda$ChannelAdminLogActivity$eETtXTeGU9y5fKwho2AV8a_Izoc implements RequestDelegate {
    private final /* synthetic */ ChannelAdminLogActivity f$0;

    public /* synthetic */ C3607-$$Lambda$ChannelAdminLogActivity$eETtXTeGU9y5fKwho2AV8a_Izoc(ChannelAdminLogActivity channelAdminLogActivity) {
        this.f$0 = channelAdminLogActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadMessages$1$ChannelAdminLogActivity(tLObject, tL_error);
    }
}
