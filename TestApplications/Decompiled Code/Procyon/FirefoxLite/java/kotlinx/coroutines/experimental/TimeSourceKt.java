// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

public final class TimeSourceKt
{
    private static TimeSource timeSource;
    
    static {
        TimeSourceKt.timeSource = DefaultTimeSource.INSTANCE;
    }
    
    public static final TimeSource getTimeSource() {
        return TimeSourceKt.timeSource;
    }
}
