package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC.TL_document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MediaController$0rWtUTn2QUBBNpu8veTtOVfGYSQ */
public final /* synthetic */ class C0535-$$Lambda$MediaController$0rWtUTn2QUBBNpu8veTtOVfGYSQ implements Runnable {
    private final /* synthetic */ MediaController f$0;
    private final /* synthetic */ TL_document f$1;
    private final /* synthetic */ File f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0535-$$Lambda$MediaController$0rWtUTn2QUBBNpu8veTtOVfGYSQ(MediaController mediaController, TL_document tL_document, File file, int i) {
        this.f$0 = mediaController;
        this.f$1 = tL_document;
        this.f$2 = file;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$null$19$MediaController(this.f$1, this.f$2, this.f$3);
    }
}
