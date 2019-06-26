package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileLoader$hhzyW9_Gs7B9dgoeAzy05Nfkj9g */
public final /* synthetic */ class C0452-$$Lambda$FileLoader$hhzyW9_Gs7B9dgoeAzy05Nfkj9g implements Runnable {
    private final /* synthetic */ FileLoader f$0;
    private final /* synthetic */ Document f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0452-$$Lambda$FileLoader$hhzyW9_Gs7B9dgoeAzy05Nfkj9g(FileLoader fileLoader, Document document, boolean z) {
        this.f$0 = fileLoader;
        this.f$1 = document;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$removeLoadingVideo$1$FileLoader(this.f$1, this.f$2);
    }
}
