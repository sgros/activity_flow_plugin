package org.telegram.messenger;

import android.util.SparseArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$54eJN0C_gHRDy8W6-JUJzMnPHt0 */
public final /* synthetic */ class C0479-$$Lambda$ImageLoader$54eJN0C_gHRDy8W6-JUJzMnPHt0 implements Runnable {
    private final /* synthetic */ SparseArray f$0;

    public /* synthetic */ C0479-$$Lambda$ImageLoader$54eJN0C_gHRDy8W6-JUJzMnPHt0(SparseArray sparseArray) {
        this.f$0 = sparseArray;
    }

    public final void run() {
        FileLoader.setMediaDirs(this.f$0);
    }
}
