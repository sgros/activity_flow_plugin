package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$ilk1G7oNY7NesFkLXCjlacEAUV0 */
public final /* synthetic */ class C3635-$$Lambda$ChatActivity$ilk1G7oNY7NesFkLXCjlacEAUV0 implements RequestDelegate {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C3635-$$Lambda$ChatActivity$ilk1G7oNY7NesFkLXCjlacEAUV0(ChatActivity chatActivity, String str) {
        this.f$0 = chatActivity;
        this.f$1 = str;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$87$ChatActivity(this.f$1, tLObject, tL_error);
    }
}
