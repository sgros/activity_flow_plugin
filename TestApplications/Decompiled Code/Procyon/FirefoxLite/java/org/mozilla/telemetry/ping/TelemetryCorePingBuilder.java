// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;
import org.mozilla.telemetry.measurement.CreatedDateMeasurement;
import org.mozilla.telemetry.measurement.FirstRunProfileDateMeasurement;
import org.mozilla.telemetry.measurement.ArchMeasurement;
import org.mozilla.telemetry.measurement.DeviceMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.SessionDurationMeasurement;
import org.mozilla.telemetry.measurement.SessionCountMeasurement;
import org.mozilla.telemetry.measurement.SearchesMeasurement;
import org.mozilla.telemetry.measurement.ExperimentsMeasurement;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;

public class TelemetryCorePingBuilder extends TelemetryPingBuilder
{
    private DefaultSearchMeasurement defaultSearchMeasurement;
    private ExperimentsMeasurement experimentsMeasurement;
    private SearchesMeasurement searchesMeasurement;
    private SessionCountMeasurement sessionCountMeasurement;
    private SessionDurationMeasurement sessionDurationMeasurement;
    
    public TelemetryCorePingBuilder(final TelemetryConfiguration telemetryConfiguration) {
        super(telemetryConfiguration, "core", 7);
        this.addMeasurement(new SequenceMeasurement(telemetryConfiguration, this));
        this.addMeasurement(new LocaleMeasurement());
        this.addMeasurement(new OperatingSystemMeasurement());
        this.addMeasurement(new OperatingSystemVersionMeasurement());
        this.addMeasurement(new DeviceMeasurement());
        this.addMeasurement(new ArchMeasurement());
        this.addMeasurement(new FirstRunProfileDateMeasurement(telemetryConfiguration));
        this.addMeasurement(this.defaultSearchMeasurement = new DefaultSearchMeasurement());
        this.addMeasurement(new CreatedDateMeasurement());
        this.addMeasurement(new TimezoneOffsetMeasurement());
        this.addMeasurement(this.sessionCountMeasurement = new SessionCountMeasurement(telemetryConfiguration));
        this.addMeasurement(this.sessionDurationMeasurement = new SessionDurationMeasurement(telemetryConfiguration));
        this.addMeasurement(this.searchesMeasurement = new SearchesMeasurement(telemetryConfiguration));
        this.addMeasurement(this.experimentsMeasurement = new ExperimentsMeasurement());
    }
    
    public DefaultSearchMeasurement getDefaultSearchMeasurement() {
        return this.defaultSearchMeasurement;
    }
    
    public SearchesMeasurement getSearchesMeasurement() {
        return this.searchesMeasurement;
    }
    
    public SessionCountMeasurement getSessionCountMeasurement() {
        return this.sessionCountMeasurement;
    }
    
    public SessionDurationMeasurement getSessionDurationMeasurement() {
        return this.sessionDurationMeasurement;
    }
    
    @Override
    protected String getUploadPath(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.getUploadPath(s));
        sb.append("?v=4");
        return sb.toString();
    }
}
