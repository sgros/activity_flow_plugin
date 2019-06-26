package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo */
public final /* synthetic */ class C0282-$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo implements Runnable {
    private final /* synthetic */ MusicPlayerService f$0;

    public /* synthetic */ C0282-$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo(MusicPlayerService musicPlayerService) {
        this.f$0 = musicPlayerService;
    }

    public final void run() {
        this.f$0.stopSelf();
    }
}
