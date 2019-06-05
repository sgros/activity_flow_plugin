// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

public class CreatedTimestampMeasurement extends TelemetryMeasurement
{
    public CreatedTimestampMeasurement() {
        super("created");
    }
    
    @Override
    public Object flush() {
        return System.currentTimeMillis();
    }
}
