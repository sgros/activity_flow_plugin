package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.photos_Photos;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$L4Om6tbm2x7Gby-R4CSBHE04Yg4 */
public final /* synthetic */ class C0795-$$Lambda$MessagesStorage$L4Om6tbm2x7Gby-R4CSBHE04Yg4 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ photos_Photos f$2;

    public /* synthetic */ C0795-$$Lambda$MessagesStorage$L4Om6tbm2x7Gby-R4CSBHE04Yg4(MessagesStorage messagesStorage, int i, photos_Photos photos_photos) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = photos_photos;
    }

    public final void run() {
        this.f$0.lambda$putDialogPhotos$53$MessagesStorage(this.f$1, this.f$2);
    }
}
