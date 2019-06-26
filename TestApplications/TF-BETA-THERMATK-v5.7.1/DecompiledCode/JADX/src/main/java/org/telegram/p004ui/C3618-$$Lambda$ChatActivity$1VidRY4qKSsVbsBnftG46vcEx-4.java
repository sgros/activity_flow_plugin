package org.telegram.p004ui;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$1VidRY4qKSsVbsBnftG46vcEx-4 */
public final /* synthetic */ class C3618-$$Lambda$ChatActivity$1VidRY4qKSsVbsBnftG46vcEx-4 implements RequestDelegate {
    public static final /* synthetic */ C3618-$$Lambda$ChatActivity$1VidRY4qKSsVbsBnftG46vcEx-4 INSTANCE = new C3618-$$Lambda$ChatActivity$1VidRY4qKSsVbsBnftG46vcEx-4();

    private /* synthetic */ C3618-$$Lambda$ChatActivity$1VidRY4qKSsVbsBnftG46vcEx-4() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1354-$$Lambda$ChatActivity$cWCA5x0DAJ_SJsezlQnjVOHPVuQ(tLObject));
    }
}
