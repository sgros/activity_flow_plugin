// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import org.json.JSONObject;

public class DefaultSearchMeasurement extends TelemetryMeasurement
{
    private DefaultSearchEngineProvider provider;
    
    public DefaultSearchMeasurement() {
        super("defaultSearch");
    }
    
    @Override
    public Object flush() {
        if (this.provider == null) {
            return JSONObject.NULL;
        }
        Object o = this.provider.getDefaultSearchEngineIdentifier();
        if (o == null) {
            o = JSONObject.NULL;
        }
        return o;
    }
    
    public void setDefaultSearchEngineProvider(final DefaultSearchEngineProvider provider) {
        this.provider = provider;
    }
    
    public interface DefaultSearchEngineProvider
    {
        String getDefaultSearchEngineIdentifier();
    }
}
