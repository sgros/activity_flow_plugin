package org.mozilla.fileutils;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/* compiled from: lambda */
/* renamed from: org.mozilla.fileutils.-$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U */
public final /* synthetic */ class C0424-$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U implements Callable {
    private final /* synthetic */ GetFile f$0;
    private final /* synthetic */ WeakReference f$1;

    public /* synthetic */ C0424-$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U(GetFile getFile, WeakReference weakReference) {
        this.f$0 = getFile;
        this.f$1 = weakReference;
    }

    public final Object call() {
        return GetFile.lambda$new$0(this.f$0, this.f$1);
    }
}
