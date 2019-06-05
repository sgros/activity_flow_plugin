package kotlinx.coroutines.experimental;

/* compiled from: CancellableContinuation.kt */
final class CompletedIdempotentResult {
    public final Object result;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CompletedIdempotentResult[");
        stringBuilder.append(this.result);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
