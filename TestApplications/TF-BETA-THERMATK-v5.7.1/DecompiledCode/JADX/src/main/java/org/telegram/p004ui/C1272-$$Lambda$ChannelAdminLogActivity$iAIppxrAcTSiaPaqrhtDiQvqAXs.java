package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelAdminLogActivity$iAIppxrAcTSiaPaqrhtDiQvqAXs */
public final /* synthetic */ class C1272-$$Lambda$ChannelAdminLogActivity$iAIppxrAcTSiaPaqrhtDiQvqAXs implements OnClickListener {
    private final /* synthetic */ ChannelAdminLogActivity f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C1272-$$Lambda$ChannelAdminLogActivity$iAIppxrAcTSiaPaqrhtDiQvqAXs(ChannelAdminLogActivity channelAdminLogActivity, String str) {
        this.f$0 = channelAdminLogActivity;
        this.f$1 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showOpenUrlAlert$14$ChannelAdminLogActivity(this.f$1, dialogInterface, i);
    }
}
