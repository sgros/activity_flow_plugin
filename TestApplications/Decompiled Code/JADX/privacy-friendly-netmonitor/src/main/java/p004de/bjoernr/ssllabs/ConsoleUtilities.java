package p004de.bjoernr.ssllabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: de.bjoernr.ssllabs.ConsoleUtilities */
public class ConsoleUtilities {
    private static String newLine = "\n";

    public static Map<String, Object> jsonToMap(JSONObject jSONObject) throws JSONException {
        return jSONObject != JSONObject.NULL ? ConsoleUtilities.toMap(jSONObject) : new HashMap();
    }

    public static Map<String, Object> toMap(JSONObject jSONObject) throws JSONException {
        HashMap hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            Object obj = jSONObject.get(str);
            if (obj instanceof JSONArray) {
                obj = ConsoleUtilities.toList((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = ConsoleUtilities.toMap((JSONObject) obj);
            }
            hashMap.put(str, obj);
        }
        return hashMap;
    }

    public static List<Object> toList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONArray) {
                obj = ConsoleUtilities.toList((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = ConsoleUtilities.toMap((JSONObject) obj);
            }
            arrayList.add(obj);
        }
        return arrayList;
    }

    public static String mapToConsoleOutput(Map<String, Object> map) {
        String str = "";
        for (Entry entry : map.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(" = ");
            stringBuilder.append(entry.getValue().toString());
            str = stringBuilder.toString();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(newLine);
            str = stringBuilder2.toString();
        }
        return str;
    }

    public static String arrayValueMatchRegex(String[] strArr, String str) {
        Pattern compile = Pattern.compile(str);
        for (CharSequence matcher : strArr) {
            Matcher matcher2 = compile.matcher(matcher);
            while (matcher2.find()) {
                try {
                    return matcher2.group(1);
                } catch (Exception unused) {
                }
            }
        }
        return null;
    }
}
