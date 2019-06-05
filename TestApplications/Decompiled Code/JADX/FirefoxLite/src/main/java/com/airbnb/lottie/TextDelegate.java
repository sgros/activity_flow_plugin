package com.airbnb.lottie;

import java.util.Map;

public class TextDelegate {
    private boolean cacheText;
    private final Map<String, String> stringMap;

    private String getText(String str) {
        return str;
    }

    public final String getTextInternal(String str) {
        if (this.cacheText && this.stringMap.containsKey(str)) {
            return (String) this.stringMap.get(str);
        }
        String text = getText(str);
        if (this.cacheText) {
            this.stringMap.put(str, text);
        }
        return text;
    }
}
