package org.mozilla.telemetry.ping;

import java.util.Map;

public class TelemetryPing {
    private final String documentId;
    private final Map<String, Object> measurementResults;
    private final String type;
    private final String uploadPath;

    TelemetryPing(String str, String str2, String str3, Map<String, Object> map) {
        this.type = str;
        this.documentId = str2;
        this.uploadPath = str3;
        this.measurementResults = map;
    }

    public String getType() {
        return this.type;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public String getUploadPath() {
        return this.uploadPath;
    }

    public Map<String, Object> getMeasurementResults() {
        return this.measurementResults;
    }
}
