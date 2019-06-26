package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$FileLoader$4dX6FY75qVi0nYcXFAFSA0OGeOs */
public final /* synthetic */ class C0444-$$Lambda$FileLoader$4dX6FY75qVi0nYcXFAFSA0OGeOs implements Runnable {
    private final /* synthetic */ ArrayList f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0444-$$Lambda$FileLoader$4dX6FY75qVi0nYcXFAFSA0OGeOs(ArrayList arrayList, int i) {
        this.f$0 = arrayList;
        this.f$1 = i;
    }

    public final void run() {
        FileLoader.lambda$deleteFiles$10(this.f$0, this.f$1);
    }
}
