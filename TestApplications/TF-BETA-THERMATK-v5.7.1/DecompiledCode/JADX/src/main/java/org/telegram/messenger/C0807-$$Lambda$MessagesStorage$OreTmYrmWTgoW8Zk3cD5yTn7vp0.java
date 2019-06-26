package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$OreTmYrmWTgoW8Zk3cD5yTn7vp0 */
public final /* synthetic */ class C0807-$$Lambda$MessagesStorage$OreTmYrmWTgoW8Zk3cD5yTn7vp0 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C0807-$$Lambda$MessagesStorage$OreTmYrmWTgoW8Zk3cD5yTn7vp0(MessagesStorage messagesStorage, String str, String str2) {
        this.f$0 = messagesStorage;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public final void run() {
        this.f$0.lambda$applyPhoneBookUpdates$88$MessagesStorage(this.f$1, this.f$2);
    }
}
