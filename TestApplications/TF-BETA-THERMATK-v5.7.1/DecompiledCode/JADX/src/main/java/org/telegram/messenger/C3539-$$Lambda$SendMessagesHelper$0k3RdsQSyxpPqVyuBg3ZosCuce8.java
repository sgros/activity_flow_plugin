package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_editMessage;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8 */
public final /* synthetic */ class C3539-$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8 implements RequestDelegate {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ TL_messages_editMessage f$2;
    private final /* synthetic */ Runnable f$3;

    public /* synthetic */ C3539-$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8(SendMessagesHelper sendMessagesHelper, BaseFragment baseFragment, TL_messages_editMessage tL_messages_editMessage, Runnable runnable) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = baseFragment;
        this.f$2 = tL_messages_editMessage;
        this.f$3 = runnable;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$editMessage$11$SendMessagesHelper(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
