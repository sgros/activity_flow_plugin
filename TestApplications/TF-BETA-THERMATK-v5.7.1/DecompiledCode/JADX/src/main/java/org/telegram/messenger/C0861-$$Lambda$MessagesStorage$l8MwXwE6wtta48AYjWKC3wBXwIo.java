package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$l8MwXwE6wtta48AYjWKC3wBXwIo */
public final /* synthetic */ class C0861-$$Lambda$MessagesStorage$l8MwXwE6wtta48AYjWKC3wBXwIo implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ IntCallback f$2;

    public /* synthetic */ C0861-$$Lambda$MessagesStorage$l8MwXwE6wtta48AYjWKC3wBXwIo(MessagesStorage messagesStorage, long j, IntCallback intCallback) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = intCallback;
    }

    public final void run() {
        this.f$0.lambda$getUnreadMention$96$MessagesStorage(this.f$1, this.f$2);
    }
}
