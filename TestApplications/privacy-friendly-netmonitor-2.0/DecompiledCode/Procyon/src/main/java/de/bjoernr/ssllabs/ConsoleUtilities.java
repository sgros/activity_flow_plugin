// 
// Decompiled by Procyon v0.5.34
// 

package de.bjoernr.ssllabs;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import java.util.Iterator;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUtilities
{
    private static String newLine = "\n";
    
    public static String arrayValueMatchRegex(final String[] array, String compile) {
        compile = (String)Pattern.compile(compile);
        int n = 0;
        while (true) {
            Label_0044: {
                if (n >= array.length) {
                    break Label_0044;
                }
                final Matcher matcher = ((Pattern)compile).matcher(array[n]);
                while (true) {
                    Label_0038: {
                        if (!matcher.find()) {
                            break Label_0038;
                        }
                        try {
                            return matcher.group(1);
                            return null;
                            ++n;
                        }
                        catch (Exception ex) {
                            continue;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public static Map<String, Object> jsonToMap(final JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (jsonObject != JSONObject.NULL) {
            map = toMap(jsonObject);
        }
        return map;
    }
    
    public static String mapToConsoleOutput(final Map<String, Object> map) {
        final String s = "";
        final Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        String string = s;
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> entry = iterator.next();
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(entry.getKey());
            sb.append(" = ");
            sb.append(entry.getValue().toString());
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(ConsoleUtilities.newLine);
            string = sb2.toString();
        }
        return string;
    }
    
    public static List<Object> toList(final JSONArray jsonArray) throws JSONException {
        final ArrayList<List> list = new ArrayList<List>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final Object value = jsonArray.get(i);
            Object o;
            if (value instanceof JSONArray) {
                o = toList((JSONArray)value);
            }
            else {
                o = value;
                if (value instanceof JSONObject) {
                    o = toMap((JSONObject)value);
                }
            }
            list.add((List)o);
        }
        return (List<Object>)list;
    }
    
    public static Map<String, Object> toMap(final JSONObject jsonObject) throws JSONException {
        final HashMap<String, List> hashMap = new HashMap<String, List>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final Object value = jsonObject.get(s);
            Object o;
            if (value instanceof JSONArray) {
                o = toList((JSONArray)value);
            }
            else {
                o = value;
                if (value instanceof JSONObject) {
                    o = toMap((JSONObject)value);
                }
            }
            hashMap.put(s, (List)o);
        }
        return (Map<String, Object>)hashMap;
    }
}
