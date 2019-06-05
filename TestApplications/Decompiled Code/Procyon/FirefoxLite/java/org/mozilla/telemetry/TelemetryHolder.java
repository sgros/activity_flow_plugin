// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry;

public class TelemetryHolder
{
    private static Telemetry telemetry;
    
    public static Telemetry get() {
        if (TelemetryHolder.telemetry != null) {
            return TelemetryHolder.telemetry;
        }
        throw new IllegalStateException("You need to call set() on TelemetryHolder in your application class");
    }
    
    public static void set(final Telemetry telemetry) {
        TelemetryHolder.telemetry = telemetry;
    }
}
