// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

public class SecureDocumentKey
{
    public byte[] file_iv;
    public byte[] file_key;
    
    public SecureDocumentKey(final byte[] file_key, final byte[] file_iv) {
        this.file_key = file_key;
        this.file_iv = file_iv;
    }
}
