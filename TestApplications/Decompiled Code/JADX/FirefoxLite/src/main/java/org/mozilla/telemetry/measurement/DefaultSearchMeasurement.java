package org.mozilla.telemetry.measurement;

import org.json.JSONObject;

public class DefaultSearchMeasurement extends TelemetryMeasurement {
    private DefaultSearchEngineProvider provider;

    public interface DefaultSearchEngineProvider {
        String getDefaultSearchEngineIdentifier();
    }

    public DefaultSearchMeasurement() {
        super("defaultSearch");
    }

    public void setDefaultSearchEngineProvider(DefaultSearchEngineProvider defaultSearchEngineProvider) {
        this.provider = defaultSearchEngineProvider;
    }

    public Object flush() {
        if (this.provider == null) {
            return JSONObject.NULL;
        }
        Object defaultSearchEngineIdentifier = this.provider.getDefaultSearchEngineIdentifier();
        if (defaultSearchEngineIdentifier == null) {
            defaultSearchEngineIdentifier = JSONObject.NULL;
        }
        return defaultSearchEngineIdentifier;
    }
}
