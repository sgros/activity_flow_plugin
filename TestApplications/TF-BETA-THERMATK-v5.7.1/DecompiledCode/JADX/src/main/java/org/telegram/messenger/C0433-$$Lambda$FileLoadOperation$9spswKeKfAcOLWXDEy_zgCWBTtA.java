package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileLoadOperation$9spswKeKfAcOLWXDEy_zgCWBTtA */
public final /* synthetic */ class C0433-$$Lambda$FileLoadOperation$9spswKeKfAcOLWXDEy_zgCWBTtA implements Runnable {
    private final /* synthetic */ FileLoadOperation f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0433-$$Lambda$FileLoadOperation$9spswKeKfAcOLWXDEy_zgCWBTtA(FileLoadOperation fileLoadOperation, int i) {
        this.f$0 = fileLoadOperation;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onFail$9$FileLoadOperation(this.f$1);
    }
}