// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.android;

import java.util.HashMap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.util.EnumMap;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import android.content.Intent;
import java.util.regex.Pattern;

public final class DecodeHintManager
{
    private static final Pattern COMMA;
    private static final String TAG;
    
    static {
        TAG = DecodeHintManager.class.getSimpleName();
        COMMA = Pattern.compile(",");
    }
    
    private DecodeHintManager() {
    }
    
    public static Map<DecodeHintType, Object> parseDecodeHints(final Intent intent) {
        final Bundle extras = intent.getExtras();
        Map<DecodeHintType, Object> obj;
        if (extras == null || extras.isEmpty()) {
            obj = null;
        }
        else {
            obj = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
            for (final DecodeHintType obj2 : DecodeHintType.values()) {
                if (obj2 != DecodeHintType.CHARACTER_SET && obj2 != DecodeHintType.NEED_RESULT_POINT_CALLBACK && obj2 != DecodeHintType.POSSIBLE_FORMATS) {
                    final String name = obj2.name();
                    if (extras.containsKey(name)) {
                        if (obj2.getValueType().equals(Void.class)) {
                            obj.put(obj2, Boolean.TRUE);
                        }
                        else {
                            final Object value = extras.get(name);
                            if (obj2.getValueType().isInstance(value)) {
                                obj.put(obj2, value);
                            }
                            else {
                                Log.w(DecodeHintManager.TAG, "Ignoring hint " + obj2 + " because it is not assignable from " + value);
                            }
                        }
                    }
                }
            }
            Log.i(DecodeHintManager.TAG, "Hints from the Intent: " + obj);
        }
        return obj;
    }
    
    static Map<DecodeHintType, ?> parseDecodeHints(final Uri uri) {
        final String encodedQuery = uri.getEncodedQuery();
        Map<DecodeHintType, ?> map;
        if (encodedQuery == null || encodedQuery.isEmpty()) {
            map = null;
        }
        else {
            final Map<String, String> splitQuery = splitQuery(encodedQuery);
            final EnumMap<DecodeHintType, Object> obj = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
            for (final DecodeHintType decodeHintType : DecodeHintType.values()) {
                if (decodeHintType != DecodeHintType.CHARACTER_SET && decodeHintType != DecodeHintType.NEED_RESULT_POINT_CALLBACK && decodeHintType != DecodeHintType.POSSIBLE_FORMATS) {
                    final String anotherString = splitQuery.get(decodeHintType.name());
                    if (anotherString != null) {
                        if (decodeHintType.getValueType().equals(Object.class)) {
                            obj.put(decodeHintType, (Object)anotherString);
                        }
                        else if (decodeHintType.getValueType().equals(Void.class)) {
                            obj.put(decodeHintType, (Object)Boolean.TRUE);
                        }
                        else if (decodeHintType.getValueType().equals(String.class)) {
                            obj.put(decodeHintType, (Object)anotherString);
                        }
                        else if (decodeHintType.getValueType().equals(Boolean.class)) {
                            if (anotherString.isEmpty()) {
                                obj.put(decodeHintType, (Object)Boolean.TRUE);
                            }
                            else if ("0".equals(anotherString) || "false".equalsIgnoreCase(anotherString) || "no".equalsIgnoreCase(anotherString)) {
                                obj.put(decodeHintType, (Object)Boolean.FALSE);
                            }
                            else {
                                obj.put(decodeHintType, (Object)Boolean.TRUE);
                            }
                        }
                        else if (decodeHintType.getValueType().equals(int[].class)) {
                            String substring = anotherString;
                            if (!anotherString.isEmpty()) {
                                substring = anotherString;
                                if (anotherString.charAt(anotherString.length() - 1) == ',') {
                                    substring = anotherString.substring(0, anotherString.length() - 1);
                                }
                            }
                            final String[] split = DecodeHintManager.COMMA.split(substring);
                            final int[] array = new int[split.length];
                            int n = 0;
                            Object o;
                            while (true) {
                                o = array;
                                if (n < split.length) {
                                    try {
                                        array[n] = Integer.parseInt(split[n]);
                                        ++n;
                                        continue;
                                    }
                                    catch (NumberFormatException ex) {
                                        Log.w(DecodeHintManager.TAG, "Skipping array of integers hint " + decodeHintType + " due to invalid numeric value: '" + split[n] + '\'');
                                        o = null;
                                    }
                                    break;
                                }
                                break;
                            }
                            if (o != null) {
                                obj.put(decodeHintType, (Object)o);
                            }
                        }
                        else {
                            Log.w(DecodeHintManager.TAG, "Unsupported hint type '" + decodeHintType + "' of type " + decodeHintType.getValueType());
                        }
                    }
                }
            }
            Log.i(DecodeHintManager.TAG, "Hints from the URI: " + obj);
            map = obj;
        }
        return map;
    }
    
    private static Map<String, String> splitQuery(String decode) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        int i = 0;
        while (i < decode.length()) {
            if (decode.charAt(i) == '&') {
                ++i;
            }
            else {
                final int index = decode.indexOf(38, i);
                final int index2 = decode.indexOf(61, i);
                if (index < 0) {
                    String decode2;
                    if (index2 < 0) {
                        decode = Uri.decode(decode.substring(i).replace('+', ' '));
                        decode2 = "";
                    }
                    else {
                        final String decode3 = Uri.decode(decode.substring(i, index2).replace('+', ' '));
                        decode2 = Uri.decode(decode.substring(index2 + 1).replace('+', ' '));
                        decode = decode3;
                    }
                    if (!hashMap.containsKey(decode)) {
                        hashMap.put(decode, decode2);
                        break;
                    }
                    break;
                }
                else if (index2 < 0 || index2 > index) {
                    final String decode4 = Uri.decode(decode.substring(i, index).replace('+', ' '));
                    if (!hashMap.containsKey(decode4)) {
                        hashMap.put(decode4, "");
                    }
                    i = index + 1;
                }
                else {
                    final String decode5 = Uri.decode(decode.substring(i, index2).replace('+', ' '));
                    final String decode6 = Uri.decode(decode.substring(index2 + 1, index).replace('+', ' '));
                    if (!hashMap.containsKey(decode5)) {
                        hashMap.put(decode5, decode6);
                    }
                    i = index + 1;
                }
            }
        }
        return hashMap;
    }
}
