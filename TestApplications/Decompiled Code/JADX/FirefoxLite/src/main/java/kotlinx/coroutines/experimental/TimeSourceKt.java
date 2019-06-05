package kotlinx.coroutines.experimental;

/* compiled from: TimeSource.kt */
public final class TimeSourceKt {
    private static TimeSource timeSource = DefaultTimeSource.INSTANCE;

    public static final TimeSource getTimeSource() {
        return timeSource;
    }
}
