package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.ArchMeasurement;
import org.mozilla.telemetry.measurement.CreatedDateMeasurement;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import org.mozilla.telemetry.measurement.DeviceMeasurement;
import org.mozilla.telemetry.measurement.ExperimentsMeasurement;
import org.mozilla.telemetry.measurement.FirstRunProfileDateMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.SearchesMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.measurement.SessionCountMeasurement;
import org.mozilla.telemetry.measurement.SessionDurationMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;

public class TelemetryCorePingBuilder extends TelemetryPingBuilder {
    private DefaultSearchMeasurement defaultSearchMeasurement;
    private ExperimentsMeasurement experimentsMeasurement;
    private SearchesMeasurement searchesMeasurement;
    private SessionCountMeasurement sessionCountMeasurement;
    private SessionDurationMeasurement sessionDurationMeasurement;

    public TelemetryCorePingBuilder(TelemetryConfiguration telemetryConfiguration) {
        super(telemetryConfiguration, "core", 7);
        addMeasurement(new SequenceMeasurement(telemetryConfiguration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new DeviceMeasurement());
        addMeasurement(new ArchMeasurement());
        addMeasurement(new FirstRunProfileDateMeasurement(telemetryConfiguration));
        DefaultSearchMeasurement defaultSearchMeasurement = new DefaultSearchMeasurement();
        this.defaultSearchMeasurement = defaultSearchMeasurement;
        addMeasurement(defaultSearchMeasurement);
        addMeasurement(new CreatedDateMeasurement());
        addMeasurement(new TimezoneOffsetMeasurement());
        SessionCountMeasurement sessionCountMeasurement = new SessionCountMeasurement(telemetryConfiguration);
        this.sessionCountMeasurement = sessionCountMeasurement;
        addMeasurement(sessionCountMeasurement);
        SessionDurationMeasurement sessionDurationMeasurement = new SessionDurationMeasurement(telemetryConfiguration);
        this.sessionDurationMeasurement = sessionDurationMeasurement;
        addMeasurement(sessionDurationMeasurement);
        SearchesMeasurement searchesMeasurement = new SearchesMeasurement(telemetryConfiguration);
        this.searchesMeasurement = searchesMeasurement;
        addMeasurement(searchesMeasurement);
        ExperimentsMeasurement experimentsMeasurement = new ExperimentsMeasurement();
        this.experimentsMeasurement = experimentsMeasurement;
        addMeasurement(experimentsMeasurement);
    }

    public SessionCountMeasurement getSessionCountMeasurement() {
        return this.sessionCountMeasurement;
    }

    public SessionDurationMeasurement getSessionDurationMeasurement() {
        return this.sessionDurationMeasurement;
    }

    public SearchesMeasurement getSearchesMeasurement() {
        return this.searchesMeasurement;
    }

    public DefaultSearchMeasurement getDefaultSearchMeasurement() {
        return this.defaultSearchMeasurement;
    }

    /* Access modifiers changed, original: protected */
    public String getUploadPath(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getUploadPath(str));
        stringBuilder.append("?v=4");
        return stringBuilder.toString();
    }
}
