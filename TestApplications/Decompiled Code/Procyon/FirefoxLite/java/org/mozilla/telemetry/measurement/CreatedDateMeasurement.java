// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CreatedDateMeasurement extends TelemetryMeasurement
{
    public CreatedDateMeasurement() {
        super("created");
    }
    
    @Override
    public Object flush() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().getTime());
    }
}
