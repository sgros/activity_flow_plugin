package org.telegram.p004ui;

import org.telegram.p004ui.PollCreateActivity.PollCreateActivityDelegate;
import org.telegram.tgnet.TLRPC.TL_messageMediaPoll;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$6WXJc4YH8LQTBuHben4e3iABxOY */
public final /* synthetic */ class C3623-$$Lambda$ChatActivity$6WXJc4YH8LQTBuHben4e3iABxOY implements PollCreateActivityDelegate {
    private final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ C3623-$$Lambda$ChatActivity$6WXJc4YH8LQTBuHben4e3iABxOY(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    public final void sendPoll(TL_messageMediaPoll tL_messageMediaPoll) {
        this.f$0.lambda$processSelectedAttach$44$ChatActivity(tL_messageMediaPoll);
    }
}
