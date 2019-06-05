// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.Locale;
import org.json.JSONObject;

public class AdjustEventSuccess
{
    public String adid;
    public String eventToken;
    public JSONObject jsonResponse;
    public String message;
    public String timestamp;
    
    @Override
    public String toString() {
        return String.format(Locale.US, "Event Success msg:%s time:%s adid:%s event:%s json:%s", this.message, this.timestamp, this.adid, this.eventToken, this.jsonResponse);
    }
}
