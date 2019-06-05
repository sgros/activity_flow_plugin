package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.measurement.EventsMeasurement;

public class TelemetryMobileEventPingBuilder extends TelemetryPingBuilder {
    private EventsMeasurement eventsMeasurement;

    public EventsMeasurement getEventsMeasurement() {
        return this.eventsMeasurement;
    }

    public boolean canBuild() {
        return this.eventsMeasurement.getEventCount() >= ((long) getConfiguration().getMinimumEventsForUpload());
    }

    /* Access modifiers changed, original: protected */
    public String getUploadPath(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getUploadPath(str));
        stringBuilder.append("?v=4");
        return stringBuilder.toString();
    }
}
