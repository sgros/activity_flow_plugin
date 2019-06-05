// 
// Decompiled by Procyon v0.5.34
// 

package de.bjoernr.ssllabs;

import java.util.HashMap;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class Api
{
    private static final String API_URL = "https://api.ssllabs.com/api/v2";
    private static final String VERSION = "0.0.1-SNAPSHOT";
    
    private String booleanToOnOffString(final boolean b) {
        String s;
        if (b) {
            s = "on";
        }
        else {
            s = "off";
        }
        return s;
    }
    
    private String buildGetParameterString(final Map<String, String> map) {
        final String s = "";
        final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        String string = s;
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue() == null) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            String str;
            if (string.length() < 1) {
                str = "?";
            }
            else {
                str = "&";
            }
            sb.append(str);
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(entry.getKey());
            sb2.append("=");
            sb2.append(entry.getValue());
            string = sb2.toString();
        }
        return string;
    }
    
    public static String getApiUrl() {
        return "https://api.ssllabs.com/api/v2";
    }
    
    public static String getVersion() {
        return "0.0.1-SNAPSHOT";
    }
    
    private String sendApiRequest(final String str, final Map<String, String> map) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://api.ssllabs.com/api/v2/");
        sb.append(str);
        URL url = new URL(sb.toString());
        if (map != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(url.toString());
            sb2.append(this.buildGetParameterString(map));
            url = new URL(sb2.toString());
        }
        final InputStream openStream = url.openStream();
        final StringBuffer sb3 = new StringBuffer();
        while (true) {
            final int read = openStream.read();
            if (read == -1) {
                break;
            }
            sb3.append((char)read);
        }
        openStream.close();
        return sb3.toString();
    }
    
    public JSONObject fetchApiInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(this.sendApiRequest("info", null));
            return jsonObject;
        }
        catch (Exception ex) {
            return jsonObject;
        }
    }
    
    public JSONObject fetchEndpointData(final String s, String sendApiRequest, final boolean b) {
        final JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2;
        try {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("host", s);
            hashMap.put("s", sendApiRequest);
            hashMap.put("fromCache", this.booleanToOnOffString(b));
            sendApiRequest = this.sendApiRequest("getEndpointData", hashMap);
            jsonObject2 = new JSONObject(sendApiRequest);
        }
        catch (Exception ex) {
            jsonObject2 = jsonObject;
        }
        return jsonObject2;
    }
    
    public JSONObject fetchHostInformation(final String s, final boolean b, final boolean b2, final boolean b3, String sendApiRequest, final String s2, final boolean b4) {
        final JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2;
        try {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("host", s);
            hashMap.put("publish", this.booleanToOnOffString(b));
            hashMap.put("startNew", this.booleanToOnOffString(b2));
            hashMap.put("fromCache", this.booleanToOnOffString(b3));
            hashMap.put("maxAge", sendApiRequest);
            hashMap.put("all", s2);
            hashMap.put("ignoreMismatch", this.booleanToOnOffString(b4));
            sendApiRequest = this.sendApiRequest("analyze", hashMap);
            jsonObject2 = new JSONObject(sendApiRequest);
        }
        catch (Exception ex) {
            jsonObject2 = jsonObject;
        }
        return jsonObject2;
    }
    
    public JSONObject fetchHostInformationCached(final String s, final String s2, final boolean b, final boolean b2) {
        return this.fetchHostInformation(s, b, false, true, s2, "done", b2);
    }
    
    public JSONObject fetchStatusCodes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(this.sendApiRequest("getStatusCodes", null));
            return jsonObject;
        }
        catch (Exception ex) {
            return jsonObject;
        }
    }
    
    public String sendCustomApiRequest(String sendApiRequest, final Map<String, String> map) {
        try {
            sendApiRequest = this.sendApiRequest(sendApiRequest, map);
        }
        catch (Exception ex) {
            sendApiRequest = "";
        }
        return sendApiRequest;
    }
}
