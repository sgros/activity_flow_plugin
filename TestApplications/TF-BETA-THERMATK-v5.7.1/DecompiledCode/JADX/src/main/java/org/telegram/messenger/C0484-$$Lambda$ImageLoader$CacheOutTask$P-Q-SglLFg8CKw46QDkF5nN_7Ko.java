package org.telegram.messenger;

import android.graphics.drawable.Drawable;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$CacheOutTask$P-Q-SglLFg8CKw46QDkF5nN_7Ko */
public final /* synthetic */ class C0484-$$Lambda$ImageLoader$CacheOutTask$P-Q-SglLFg8CKw46QDkF5nN_7Ko implements Runnable {
    private final /* synthetic */ CacheOutTask f$0;
    private final /* synthetic */ Drawable f$1;

    public /* synthetic */ C0484-$$Lambda$ImageLoader$CacheOutTask$P-Q-SglLFg8CKw46QDkF5nN_7Ko(CacheOutTask cacheOutTask, Drawable drawable) {
        this.f$0 = cacheOutTask;
        this.f$1 = drawable;
    }

    public final void run() {
        this.f$0.lambda$onPostExecute$1$ImageLoader$CacheOutTask(this.f$1);
    }
}
