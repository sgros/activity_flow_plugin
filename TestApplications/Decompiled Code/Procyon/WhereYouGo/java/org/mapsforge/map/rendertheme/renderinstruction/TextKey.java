// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.renderinstruction;

import org.mapsforge.core.model.Tag;
import java.util.List;

final class TextKey
{
    private static final String KEY_ELEVATION = "ele";
    private static final String KEY_HOUSENUMBER = "addr:housenumber";
    private static final String KEY_NAME = "name";
    private static final String KEY_REF = "ref";
    private static final TextKey TEXT_KEY_ELEVATION;
    private static final TextKey TEXT_KEY_HOUSENUMBER;
    private static final TextKey TEXT_KEY_NAME;
    private static final TextKey TEXT_KEY_REF;
    private final String key;
    
    static {
        TEXT_KEY_ELEVATION = new TextKey("ele");
        TEXT_KEY_HOUSENUMBER = new TextKey("addr:housenumber");
        TEXT_KEY_NAME = new TextKey("name");
        TEXT_KEY_REF = new TextKey("ref");
    }
    
    private TextKey(final String key) {
        this.key = key;
    }
    
    static TextKey getInstance(final String str) {
        TextKey textKey;
        if ("ele".equals(str)) {
            textKey = TextKey.TEXT_KEY_ELEVATION;
        }
        else if ("addr:housenumber".equals(str)) {
            textKey = TextKey.TEXT_KEY_HOUSENUMBER;
        }
        else if ("name".equals(str)) {
            textKey = TextKey.TEXT_KEY_NAME;
        }
        else {
            if (!"ref".equals(str)) {
                throw new IllegalArgumentException("invalid key: " + str);
            }
            textKey = TextKey.TEXT_KEY_REF;
        }
        return textKey;
    }
    
    String getValue(final List<Tag> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (this.key.equals(list.get(i).key)) {
                return list.get(i).value;
            }
        }
        return null;
    }
}
