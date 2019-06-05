// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

public class StaticMeasurement extends TelemetryMeasurement
{
    private final Object value;
    
    public StaticMeasurement(final String s, final Object value) {
        super(s);
        this.value = value;
    }
    
    @Override
    public Object flush() {
        return this.value;
    }
}
