// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.Map;

public class TextDelegate
{
    private boolean cacheText;
    private final Map<String, String> stringMap;
    
    private String getText(final String s) {
        return s;
    }
    
    public final String getTextInternal(final String s) {
        if (this.cacheText && this.stringMap.containsKey(s)) {
            return this.stringMap.get(s);
        }
        final String text = this.getText(s);
        if (this.cacheText) {
            this.stringMap.put(s, text);
        }
        return text;
    }
}
