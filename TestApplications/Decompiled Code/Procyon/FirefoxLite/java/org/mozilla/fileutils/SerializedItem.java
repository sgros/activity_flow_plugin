// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.fileutils;

import java.nio.ByteBuffer;
import java.io.Serializable;

public class SerializedItem implements Serializable
{
    private String className;
    private String key;
    private byte[] value;
    
    public String getClassName() {
        return this.className;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public byte[] getValue() {
        byte[] array;
        if (this.value != null) {
            array = ByteBuffer.wrap(this.value).array();
        }
        else {
            array = null;
        }
        return array;
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setValue(byte[] array) {
        if (array != null) {
            array = ByteBuffer.wrap(array).array();
        }
        else {
            array = null;
        }
        this.value = array;
    }
}
