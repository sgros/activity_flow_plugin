// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.util.concurrent.TimeUnit;
import java.util.Calendar;

public class TimezoneOffsetMeasurement extends TelemetryMeasurement
{
    public TimezoneOffsetMeasurement() {
        super("tz");
    }
    
    private static int getTimezoneOffsetInMinutesForGivenDate(final Calendar calendar) {
        return (int)TimeUnit.MILLISECONDS.toMinutes(calendar.get(15) + calendar.get(16));
    }
    
    @Override
    public Object flush() {
        return getTimezoneOffsetInMinutesForGivenDate(this.now());
    }
    
    Calendar now() {
        return Calendar.getInstance();
    }
}
