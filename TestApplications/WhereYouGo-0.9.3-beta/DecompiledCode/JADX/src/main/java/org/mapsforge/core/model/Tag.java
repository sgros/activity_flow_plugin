package org.mapsforge.core.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private static final char KEY_VALUE_SEPARATOR = '=';
    private static final long serialVersionUID = 1;
    public final String key;
    public final String value;

    public Tag(String tag) {
        this(tag, tag.indexOf(61));
    }

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private Tag(String tag, int splitPosition) {
        this(tag.substring(0, splitPosition), splitPosition == tag.length() + -1 ? "" : tag.substring(splitPosition + 1));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) obj;
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
            return true;
        } else if (!this.key.equals(other.key)) {
            return false;
        } else {
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
                return true;
            } else if (this.value.equals(other.value)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.key == null ? 0 : this.key.hashCode()) + 31) * 31;
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("key=");
        stringBuilder.append(this.key);
        stringBuilder.append(", value=");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }
}
