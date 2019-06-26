package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$4JOcfs_Jho1Vm_zEM2xMi0yW30I */
public final /* synthetic */ class C0340-$$Lambda$DataQuery$4JOcfs_Jho1Vm_zEM2xMi0yW30I implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0340-$$Lambda$DataQuery$4JOcfs_Jho1Vm_zEM2xMi0yW30I(DataQuery dataQuery, ArrayList arrayList, long j) {
        this.f$0 = dataQuery;
        this.f$1 = arrayList;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$clearBotKeyboard$104$DataQuery(this.f$1, this.f$2);
    }
}
