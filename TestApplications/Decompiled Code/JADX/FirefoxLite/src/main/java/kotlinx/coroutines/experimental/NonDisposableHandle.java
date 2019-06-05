package kotlinx.coroutines.experimental;

/* compiled from: Job.kt */
public final class NonDisposableHandle implements DisposableHandle {
    public static final NonDisposableHandle INSTANCE = new NonDisposableHandle();

    public void dispose() {
    }

    public String toString() {
        return "NonDisposableHandle";
    }

    private NonDisposableHandle() {
    }
}
