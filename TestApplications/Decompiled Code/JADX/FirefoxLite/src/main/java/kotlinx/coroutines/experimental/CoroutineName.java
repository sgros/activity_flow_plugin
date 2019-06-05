package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

/* compiled from: CoroutineName.kt */
public final class CoroutineName extends AbstractCoroutineContextElement {
    public static final Key Key = new Key();
    private final String name;

    /* compiled from: CoroutineName.kt */
    public static final class Key implements kotlin.coroutines.experimental.CoroutineContext.Key<CoroutineName> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARNING: Missing block: B:4:0x0010, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r1.name, ((kotlinx.coroutines.experimental.CoroutineName) r2).name) != false) goto L_0x0015;
     */
    public boolean equals(java.lang.Object r2) {
        /*
        r1 = this;
        if (r1 == r2) goto L_0x0015;
    L_0x0002:
        r0 = r2 instanceof kotlinx.coroutines.experimental.CoroutineName;
        if (r0 == 0) goto L_0x0013;
    L_0x0006:
        r2 = (kotlinx.coroutines.experimental.CoroutineName) r2;
        r0 = r1.name;
        r2 = r2.name;
        r2 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r2);
        if (r2 == 0) goto L_0x0013;
    L_0x0012:
        goto L_0x0015;
    L_0x0013:
        r2 = 0;
        return r2;
    L_0x0015:
        r2 = 1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.CoroutineName.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        String str = this.name;
        return str != null ? str.hashCode() : 0;
    }

    public final String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CoroutineName(");
        stringBuilder.append(this.name);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
