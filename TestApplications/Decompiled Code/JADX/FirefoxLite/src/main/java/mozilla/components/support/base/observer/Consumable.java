package mozilla.components.support.base.observer;

/* compiled from: Consumable.kt */
public final class Consumable<T> {
    public static final Companion Companion = new Companion();
    private T value;

    /* compiled from: Consumable.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final <T> Consumable<T> from(T t) {
            return new Consumable(t, null);
        }

        public final <T> Consumable<T> empty() {
            return new Consumable(null, null);
        }
    }

    private Consumable(T t) {
        this.value = t;
    }

    public /* synthetic */ Consumable(Object obj, DefaultConstructorMarker defaultConstructorMarker) {
        this(obj);
    }

    /* JADX WARNING: Missing block: B:13:0x004f, code skipped:
            return r5;
     */
    public final synchronized boolean consumeBy(java.util.List<? extends kotlin.jvm.functions.Function1<? super T, java.lang.Boolean>> r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = "consumers";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r0);	 Catch:{ all -> 0x0052 }
        r0 = r4.value;	 Catch:{ all -> 0x0052 }
        r1 = 0;
        if (r0 == 0) goto L_0x0050;
    L_0x000b:
        r5 = (java.lang.Iterable) r5;	 Catch:{ all -> 0x0052 }
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0052 }
        r3 = 10;
        r3 = kotlin.collections.CollectionsKt__IterablesKt.collectionSizeOrDefault(r5, r3);	 Catch:{ all -> 0x0052 }
        r2.<init>(r3);	 Catch:{ all -> 0x0052 }
        r2 = (java.util.Collection) r2;	 Catch:{ all -> 0x0052 }
        r5 = r5.iterator();	 Catch:{ all -> 0x0052 }
    L_0x001e:
        r3 = r5.hasNext();	 Catch:{ all -> 0x0052 }
        if (r3 == 0) goto L_0x003c;
    L_0x0024:
        r3 = r5.next();	 Catch:{ all -> 0x0052 }
        r3 = (kotlin.jvm.functions.Function1) r3;	 Catch:{ all -> 0x0052 }
        r3 = r3.invoke(r0);	 Catch:{ all -> 0x0052 }
        r3 = (java.lang.Boolean) r3;	 Catch:{ all -> 0x0052 }
        r3 = r3.booleanValue();	 Catch:{ all -> 0x0052 }
        r3 = java.lang.Boolean.valueOf(r3);	 Catch:{ all -> 0x0052 }
        r2.add(r3);	 Catch:{ all -> 0x0052 }
        goto L_0x001e;
    L_0x003c:
        r2 = (java.util.List) r2;	 Catch:{ all -> 0x0052 }
        r5 = 1;
        r0 = java.lang.Boolean.valueOf(r5);	 Catch:{ all -> 0x0052 }
        r0 = r2.contains(r0);	 Catch:{ all -> 0x0052 }
        if (r0 == 0) goto L_0x004d;
    L_0x0049:
        r0 = 0;
        r4.value = r0;	 Catch:{ all -> 0x0052 }
        goto L_0x004e;
    L_0x004d:
        r5 = 0;
    L_0x004e:
        monitor-exit(r4);
        return r5;
    L_0x0050:
        monitor-exit(r4);
        return r1;
    L_0x0052:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: mozilla.components.support.base.observer.Consumable.consumeBy(java.util.List):boolean");
    }
}
