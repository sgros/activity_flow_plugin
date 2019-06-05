// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.Locale;
import org.json.JSONObject;

public class ResponseData
{
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
    
    public static ResponseData buildResponseData(final ActivityPackage activityPackage) {
        final ActivityKind activityKind = activityPackage.getActivityKind();
        ResponseData responseData = null;
        switch (ResponseData$1.$SwitchMap$com$adjust$sdk$ActivityKind[activityKind.ordinal()]) {
            default: {
                responseData = new ResponseData();
                break;
            }
            case 4: {
                responseData = new EventResponseData(activityPackage);
                break;
            }
            case 3: {
                responseData = new AttributionResponseData();
                break;
            }
            case 2: {
                responseData = new SdkClickResponseData();
                break;
            }
            case 1: {
                responseData = new SessionResponseData();
                break;
            }
        }
        responseData.activityKind = activityKind;
        return responseData;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "message:%s timestamp:%s json:%s", this.message, this.timestamp, this.jsonResponse);
    }
}
