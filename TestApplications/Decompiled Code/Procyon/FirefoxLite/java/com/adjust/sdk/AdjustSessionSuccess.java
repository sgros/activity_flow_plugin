// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.Locale;
import org.json.JSONObject;

public class AdjustSessionSuccess
{
    public String adid;
    public JSONObject jsonResponse;
    public String message;
    public String timestamp;
    
    @Override
    public String toString() {
        return String.format(Locale.US, "Session Success msg:%s time:%s adid:%s json:%s", this.message, this.timestamp, this.adid, this.jsonResponse);
    }
}
