package com.adjust.sdk;

import java.util.Locale;
import org.json.JSONObject;

public class ResponseData {
    public ActivityKind activityKind;
    public String adid;
    public AdjustAttribution attribution;
    public JSONObject jsonResponse;
    public String message;
    public boolean success;
    public String timestamp;
    public boolean willRetry;

    protected ResponseData() {
    }

    public static ResponseData buildResponseData(ActivityPackage activityPackage) {
        ResponseData sessionResponseData;
        ActivityKind activityKind = activityPackage.getActivityKind();
        switch (activityKind) {
            case SESSION:
                sessionResponseData = new SessionResponseData();
                break;
            case CLICK:
                sessionResponseData = new SdkClickResponseData();
                break;
            case ATTRIBUTION:
                sessionResponseData = new AttributionResponseData();
                break;
            case EVENT:
                sessionResponseData = new EventResponseData(activityPackage);
                break;
            default:
                sessionResponseData = new ResponseData();
                break;
        }
        sessionResponseData.activityKind = activityKind;
        return sessionResponseData;
    }

    public String toString() {
        return String.format(Locale.US, "message:%s timestamp:%s json:%s", new Object[]{this.message, this.timestamp, this.jsonResponse});
    }
}
