package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU */
public final /* synthetic */ class C0404-$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ ArrayList f$3;
    private final /* synthetic */ ArrayList f$4;
    private final /* synthetic */ SparseArray f$5;
    private final /* synthetic */ SparseArray f$6;
    private final /* synthetic */ SparseArray f$7;
    private final /* synthetic */ long f$8;

    public /* synthetic */ C0404-$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU(DataQuery dataQuery, ArrayList arrayList, boolean z, ArrayList arrayList2, ArrayList arrayList3, SparseArray sparseArray, SparseArray sparseArray2, SparseArray sparseArray3, long j) {
        this.f$0 = dataQuery;
        this.f$1 = arrayList;
        this.f$2 = z;
        this.f$3 = arrayList2;
        this.f$4 = arrayList3;
        this.f$5 = sparseArray;
        this.f$6 = sparseArray2;
        this.f$7 = sparseArray3;
        this.f$8 = j;
    }

    public final void run() {
        this.f$0.lambda$broadcastReplyMessages$96$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
    }
}