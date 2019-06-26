package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g */
public final /* synthetic */ class C0500-$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g implements Runnable {
    private final /* synthetic */ ImageLoader f$0;
    private final /* synthetic */ HttpFileTask f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0500-$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g(ImageLoader imageLoader, HttpFileTask httpFileTask, int i) {
        this.f$0 = imageLoader;
        this.f$1 = httpFileTask;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$runHttpFileLoadTasks$11$ImageLoader(this.f$1, this.f$2);
    }
}
