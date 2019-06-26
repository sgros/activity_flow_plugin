package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileUploadOperation$AsRwZThcGZTT5evyde1j2fU_PqE */
public final /* synthetic */ class C0465-$$Lambda$FileUploadOperation$AsRwZThcGZTT5evyde1j2fU_PqE implements Runnable {
    private final /* synthetic */ FileUploadOperation f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C0465-$$Lambda$FileUploadOperation$AsRwZThcGZTT5evyde1j2fU_PqE(FileUploadOperation fileUploadOperation, boolean z) {
        this.f$0 = fileUploadOperation;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$onNetworkChanged$1$FileUploadOperation(this.f$1);
    }
}
