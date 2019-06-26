package org.telegram.messenger;

import java.io.File;
import org.telegram.messenger.ImageLoader.C10313;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r-c */
public final /* synthetic */ class C0471-$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r-c implements Runnable {
    private final /* synthetic */ C10313 f$0;
    private final /* synthetic */ File f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C0471-$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r-c(C10313 c10313, File file, String str, int i, int i2) {
        this.f$0 = c10313;
        this.f$1 = file;
        this.f$2 = str;
        this.f$3 = i;
        this.f$4 = i2;
    }

    public final void run() {
        this.f$0.lambda$fileDidLoaded$5$ImageLoader$3(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
