package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY */
public final /* synthetic */ class C0874-$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ long f$3;

    public /* synthetic */ C0874-$$Lambda$MessagesStorage$s2Q7bjKkmqsnurRGdapKRuUwrkY(MessagesStorage messagesStorage, int i, int i2, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = j;
    }

    public final void run() {
        this.f$0.lambda$markMentionMessageAsRead$58$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
