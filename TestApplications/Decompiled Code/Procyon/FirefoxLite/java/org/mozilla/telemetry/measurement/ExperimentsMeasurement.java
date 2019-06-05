// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import org.json.JSONArray;

public class ExperimentsMeasurement extends TelemetryMeasurement
{
    private JSONArray experimentsIds;
    
    public ExperimentsMeasurement() {
        super("experiments");
        this.experimentsIds = new JSONArray();
    }
    
    @Override
    public Object flush() {
        return this.experimentsIds;
    }
}
