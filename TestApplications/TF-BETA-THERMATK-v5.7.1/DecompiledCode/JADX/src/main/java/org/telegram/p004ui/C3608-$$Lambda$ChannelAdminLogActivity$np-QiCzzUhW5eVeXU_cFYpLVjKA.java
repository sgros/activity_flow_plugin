package org.telegram.p004ui;

import android.util.SparseArray;
import org.telegram.p004ui.Components.AdminLogFilterAlert.AdminLogFilterAlertDelegate;
import org.telegram.tgnet.TLRPC.TL_channelAdminLogEventsFilter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelAdminLogActivity$np-QiCzzUhW5eVeXU_cFYpLVjKA */
public final /* synthetic */ class C3608-$$Lambda$ChannelAdminLogActivity$np-QiCzzUhW5eVeXU_cFYpLVjKA implements AdminLogFilterAlertDelegate {
    private final /* synthetic */ ChannelAdminLogActivity f$0;

    public /* synthetic */ C3608-$$Lambda$ChannelAdminLogActivity$np-QiCzzUhW5eVeXU_cFYpLVjKA(ChannelAdminLogActivity channelAdminLogActivity) {
        this.f$0 = channelAdminLogActivity;
    }

    public final void didSelectRights(TL_channelAdminLogEventsFilter tL_channelAdminLogEventsFilter, SparseArray sparseArray) {
        this.f$0.lambda$null$4$ChannelAdminLogActivity(tL_channelAdminLogEventsFilter, sparseArray);
    }
}
