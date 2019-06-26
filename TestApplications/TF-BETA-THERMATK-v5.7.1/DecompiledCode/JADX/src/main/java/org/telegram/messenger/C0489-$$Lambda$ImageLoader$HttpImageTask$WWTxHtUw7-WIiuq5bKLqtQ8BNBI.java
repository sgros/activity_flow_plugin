package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7-WIiuq5bKLqtQ8BNBI */
public final /* synthetic */ class C0489-$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7-WIiuq5bKLqtQ8BNBI implements Runnable {
    private final /* synthetic */ HttpImageTask f$0;
    private final /* synthetic */ float f$1;

    public /* synthetic */ C0489-$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7-WIiuq5bKLqtQ8BNBI(HttpImageTask httpImageTask, float f) {
        this.f$0 = httpImageTask;
        this.f$1 = f;
    }

    public final void run() {
        this.f$0.lambda$reportProgress$1$ImageLoader$HttpImageTask(this.f$1);
    }
}
