package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

/* compiled from: CoroutineContext.kt */
final class CoroutineId extends AbstractCoroutineContextElement {
    public static final Key Key = new Key();
    /* renamed from: id */
    private final long f76id;

    /* compiled from: CoroutineContext.kt */
    public static final class Key implements kotlin.coroutines.experimental.CoroutineContext.Key<CoroutineId> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CoroutineId(long j) {
        super(Key);
        this.f76id = j;
    }

    public final long getId() {
        return this.f76id;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CoroutineId(");
        stringBuilder.append(this.f76id);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
