package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0 */
public final /* synthetic */ class C0501-$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0 implements Runnable {
    private final /* synthetic */ ImageLoader f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C0501-$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0(ImageLoader imageLoader, String str) {
        this.f$0 = imageLoader;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$httpFileLoadError$6$ImageLoader(this.f$1);
    }
}