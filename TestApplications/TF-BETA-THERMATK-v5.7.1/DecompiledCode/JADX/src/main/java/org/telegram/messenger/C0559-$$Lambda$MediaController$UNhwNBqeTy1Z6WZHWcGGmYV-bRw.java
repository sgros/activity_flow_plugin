package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MediaController$UNhwNBqeTy1Z6WZHWcGGmYV-bRw */
public final /* synthetic */ class C0559-$$Lambda$MediaController$UNhwNBqeTy1Z6WZHWcGGmYV-bRw implements Runnable {
    private final /* synthetic */ MessageObject f$0;

    public /* synthetic */ C0559-$$Lambda$MediaController$UNhwNBqeTy1Z6WZHWcGGmYV-bRw(MessageObject messageObject) {
        this.f$0 = messageObject;
    }

    public final void run() {
        NotificationCenter.getInstance(this.f$0.currentAccount).postNotificationName(NotificationCenter.fileDidLoad, FileLoader.getAttachFileName(this.f$0.getDocument()));
    }
}
