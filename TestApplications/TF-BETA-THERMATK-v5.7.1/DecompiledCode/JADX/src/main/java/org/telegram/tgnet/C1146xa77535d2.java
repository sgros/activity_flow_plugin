package org.telegram.tgnet;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$DnsTxtLoadTask$Y_uiONB1DXfH_CyjIpbAd-79WxM */
public final /* synthetic */ class C1146xa77535d2 implements Runnable {
    private final /* synthetic */ DnsTxtLoadTask f$0;
    private final /* synthetic */ NativeByteBuffer f$1;

    public /* synthetic */ C1146xa77535d2(DnsTxtLoadTask dnsTxtLoadTask, NativeByteBuffer nativeByteBuffer) {
        this.f$0 = dnsTxtLoadTask;
        this.f$1 = nativeByteBuffer;
    }

    public final void run() {
        this.f$0.lambda$onPostExecute$1$ConnectionsManager$DnsTxtLoadTask(this.f$1);
    }
}
