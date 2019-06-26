package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_help_proxyDataPromo;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$0mxeOwB8KketmB2b8T1pp9iml0o */
public final /* synthetic */ class C0580-$$Lambda$MessagesController$0mxeOwB8KketmB2b8T1pp9iml0o implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ TL_help_proxyDataPromo f$2;

    public /* synthetic */ C0580-$$Lambda$MessagesController$0mxeOwB8KketmB2b8T1pp9iml0o(MessagesController messagesController, long j, TL_help_proxyDataPromo tL_help_proxyDataPromo) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = tL_help_proxyDataPromo;
    }

    public final void run() {
        this.f$0.lambda$null$93$MessagesController(this.f$1, this.f$2);
    }
}
