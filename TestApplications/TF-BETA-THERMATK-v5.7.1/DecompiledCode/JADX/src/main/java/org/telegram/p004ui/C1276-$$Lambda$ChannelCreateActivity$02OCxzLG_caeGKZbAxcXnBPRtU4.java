package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelCreateActivity$02OCxzLG_caeGKZbAxcXnBPRtU4 */
public final /* synthetic */ class C1276-$$Lambda$ChannelCreateActivity$02OCxzLG_caeGKZbAxcXnBPRtU4 implements Runnable {
    private final /* synthetic */ ChannelCreateActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1276-$$Lambda$ChannelCreateActivity$02OCxzLG_caeGKZbAxcXnBPRtU4(ChannelCreateActivity channelCreateActivity, TL_error tL_error, TLObject tLObject) {
        this.f$0 = channelCreateActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$9$ChannelCreateActivity(this.f$1, this.f$2);
    }
}
