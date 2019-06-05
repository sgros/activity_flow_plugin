// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.ping;

import java.util.Map;

public class TelemetryPing
{
    private final String documentId;
    private final Map<String, Object> measurementResults;
    private final String type;
    private final String uploadPath;
    
    TelemetryPing(final String type, final String documentId, final String uploadPath, final Map<String, Object> measurementResults) {
        this.type = type;
        this.documentId = documentId;
        this.uploadPath = uploadPath;
        this.measurementResults = measurementResults;
    }
    
    public String getDocumentId() {
        return this.documentId;
    }
    
    public Map<String, Object> getMeasurementResults() {
        return this.measurementResults;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getUploadPath() {
        return this.uploadPath;
    }
}
