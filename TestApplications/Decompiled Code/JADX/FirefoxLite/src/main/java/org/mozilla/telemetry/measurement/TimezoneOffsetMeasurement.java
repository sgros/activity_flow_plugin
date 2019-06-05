package org.mozilla.telemetry.measurement;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimezoneOffsetMeasurement extends TelemetryMeasurement {
    public TimezoneOffsetMeasurement() {
        super("tz");
    }

    public Object flush() {
        return Integer.valueOf(getTimezoneOffsetInMinutesForGivenDate(now()));
    }

    private static int getTimezoneOffsetInMinutesForGivenDate(Calendar calendar) {
        return (int) TimeUnit.MILLISECONDS.toMinutes((long) (calendar.get(15) + calendar.get(16)));
    }

    /* Access modifiers changed, original: 0000 */
    public Calendar now() {
        return Calendar.getInstance();
    }
}
