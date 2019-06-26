package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileLoader$iP93DCpFk-1vNZP-nXjq8znzYAg */
public final /* synthetic */ class C0453-$$Lambda$FileLoader$iP93DCpFk-1vNZP-nXjq8znzYAg implements Runnable {
    private final /* synthetic */ FileLoader f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C0453-$$Lambda$FileLoader$iP93DCpFk-1vNZP-nXjq8znzYAg(FileLoader fileLoader, boolean z, String str) {
        this.f$0 = fileLoader;
        this.f$1 = z;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$cancelUploadFile$2$FileLoader(this.f$1, this.f$2);
    }
}
