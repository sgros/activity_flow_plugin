// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

public abstract class TelemetryMeasurement
{
    private final String fieldName;
    
    public TelemetryMeasurement(final String fieldName) {
        this.fieldName = fieldName;
    }
    
    public abstract Object flush();
    
    public String getFieldName() {
        return this.fieldName;
    }
}
