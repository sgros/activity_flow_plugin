package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn-IXbR-DnQ0 */
public final /* synthetic */ class C0488-$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn-IXbR-DnQ0 implements Runnable {
    private final /* synthetic */ HttpImageTask f$0;
    private final /* synthetic */ Boolean f$1;

    public /* synthetic */ C0488-$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn-IXbR-DnQ0(HttpImageTask httpImageTask, Boolean bool) {
        this.f$0 = httpImageTask;
        this.f$1 = bool;
    }

    public final void run() {
        this.f$0.lambda$onPostExecute$4$ImageLoader$HttpImageTask(this.f$1);
    }
}
