package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE */
public final /* synthetic */ class C3611-$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE implements RequestDelegate {
    private final /* synthetic */ ChannelCreateActivity f$0;

    public /* synthetic */ C3611-$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE(ChannelCreateActivity channelCreateActivity) {
        this.f$0 = channelCreateActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$new$1$ChannelCreateActivity(tLObject, tL_error);
    }
}
