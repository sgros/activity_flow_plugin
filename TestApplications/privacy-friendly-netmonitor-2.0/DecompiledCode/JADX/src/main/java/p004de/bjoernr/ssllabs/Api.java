package p004de.bjoernr.ssllabs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;

/* renamed from: de.bjoernr.ssllabs.Api */
public class Api {
    private static final String API_URL = "https://api.ssllabs.com/api/v2";
    private static final String VERSION = "0.0.1-SNAPSHOT";

    private String booleanToOnOffString(boolean z) {
        return z ? "on" : "off";
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static String getVersion() {
        return VERSION;
    }

    public JSONObject fetchApiInfo() {
        try {
            return new JSONObject(sendApiRequest("info", null));
        } catch (Exception unused) {
            return new JSONObject();
        }
    }

    public JSONObject fetchHostInformation(String str, boolean z, boolean z2, boolean z3, String str2, String str3, boolean z4) {
        JSONObject jSONObject = new JSONObject();
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("host", str);
            hashMap.put("publish", booleanToOnOffString(z));
            hashMap.put("startNew", booleanToOnOffString(z2));
            hashMap.put("fromCache", booleanToOnOffString(z3));
            hashMap.put("maxAge", str2);
            hashMap.put("all", str3);
            hashMap.put("ignoreMismatch", booleanToOnOffString(z4));
            return new JSONObject(sendApiRequest("analyze", hashMap));
        } catch (Exception unused) {
            return jSONObject;
        }
    }

    public JSONObject fetchHostInformationCached(String str, String str2, boolean z, boolean z2) {
        return fetchHostInformation(str, z, false, true, str2, "done", z2);
    }

    public JSONObject fetchEndpointData(String str, String str2, boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("host", str);
            hashMap.put("s", str2);
            hashMap.put("fromCache", booleanToOnOffString(z));
            return new JSONObject(sendApiRequest("getEndpointData", hashMap));
        } catch (Exception unused) {
            return jSONObject;
        }
    }

    public JSONObject fetchStatusCodes() {
        try {
            return new JSONObject(sendApiRequest("getStatusCodes", null));
        } catch (Exception unused) {
            return new JSONObject();
        }
    }

    public String sendCustomApiRequest(String str, Map<String, String> map) {
        try {
            return sendApiRequest(str, map);
        } catch (Exception unused) {
            return "";
        }
    }

    private String sendApiRequest(String str, Map<String, String> map) throws IOException {
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://api.ssllabs.com/api/v2/");
        stringBuilder.append(str);
        URL url2 = new URL(stringBuilder.toString());
        if (map != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(url2.toString());
            stringBuilder.append(buildGetParameterString(map));
            url = new URL(stringBuilder.toString());
        } else {
            url = url2;
        }
        InputStream openStream = url.openStream();
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int read = openStream.read();
            if (read != -1) {
                stringBuffer.append((char) read);
            } else {
                openStream.close();
                return stringBuffer.toString();
            }
        }
    }

    private String buildGetParameterString(Map<String, String> map) {
        String str = "";
        for (Entry entry : map.entrySet()) {
            if (entry.getValue() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(str.length() < 1 ? "?" : "&");
                str = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append((String) entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append((String) entry.getValue());
                str = stringBuilder.toString();
            }
        }
        return str;
    }
}
