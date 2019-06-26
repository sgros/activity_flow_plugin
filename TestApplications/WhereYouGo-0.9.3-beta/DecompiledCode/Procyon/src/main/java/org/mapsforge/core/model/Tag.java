// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class Tag implements Serializable
{
    private static final char KEY_VALUE_SEPARATOR = '=';
    private static final long serialVersionUID = 1L;
    public final String key;
    public final String value;
    
    public Tag(final String s) {
        this(s, s.indexOf(61));
    }
    
    private Tag(String substring, final int endIndex) {
        final String substring2 = substring.substring(0, endIndex);
        if (endIndex == substring.length() - 1) {
            substring = "";
        }
        else {
            substring = substring.substring(endIndex + 1);
        }
        this(substring2, substring);
    }
    
    public Tag(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof Tag)) {
                b = false;
            }
            else {
                final Tag tag = (Tag)o;
                if (this.key == null) {
                    if (tag.key != null) {
                        b = false;
                    }
                }
                else if (!this.key.equals(tag.key)) {
                    b = false;
                }
                else if (this.value == null) {
                    if (tag.value != null) {
                        b = false;
                    }
                }
                else if (!this.value.equals(tag.value)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.key == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.key.hashCode();
        }
        if (this.value != null) {
            hashCode = this.value.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("key=");
        sb.append(this.key);
        sb.append(", value=");
        sb.append(this.value);
        return sb.toString();
    }
}
