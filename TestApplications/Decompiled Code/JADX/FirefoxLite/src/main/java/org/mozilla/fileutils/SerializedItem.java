package org.mozilla.fileutils;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class SerializedItem implements Serializable {
    private String className;
    private String key;
    private byte[] value;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String str) {
        this.className = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public byte[] getValue() {
        return this.value != null ? ByteBuffer.wrap(this.value).array() : null;
    }

    public void setValue(byte[] bArr) {
        this.value = bArr != null ? ByteBuffer.wrap(bArr).array() : null;
    }
}
