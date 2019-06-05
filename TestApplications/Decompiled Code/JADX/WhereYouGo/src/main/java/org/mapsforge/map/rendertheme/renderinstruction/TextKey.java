package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;

final class TextKey {
    private static final String KEY_ELEVATION = "ele";
    private static final String KEY_HOUSENUMBER = "addr:housenumber";
    private static final String KEY_NAME = "name";
    private static final String KEY_REF = "ref";
    private static final TextKey TEXT_KEY_ELEVATION = new TextKey(KEY_ELEVATION);
    private static final TextKey TEXT_KEY_HOUSENUMBER = new TextKey(KEY_HOUSENUMBER);
    private static final TextKey TEXT_KEY_NAME = new TextKey(KEY_NAME);
    private static final TextKey TEXT_KEY_REF = new TextKey(KEY_REF);
    private final String key;

    static TextKey getInstance(String key) {
        if (KEY_ELEVATION.equals(key)) {
            return TEXT_KEY_ELEVATION;
        }
        if (KEY_HOUSENUMBER.equals(key)) {
            return TEXT_KEY_HOUSENUMBER;
        }
        if (KEY_NAME.equals(key)) {
            return TEXT_KEY_NAME;
        }
        if (KEY_REF.equals(key)) {
            return TEXT_KEY_REF;
        }
        throw new IllegalArgumentException("invalid key: " + key);
    }

    private TextKey(String key) {
        this.key = key;
    }

    /* Access modifiers changed, original: 0000 */
    public String getValue(List<Tag> tags) {
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            if (this.key.equals(((Tag) tags.get(i)).key)) {
                return ((Tag) tags.get(i)).value;
            }
        }
        return null;
    }
}
