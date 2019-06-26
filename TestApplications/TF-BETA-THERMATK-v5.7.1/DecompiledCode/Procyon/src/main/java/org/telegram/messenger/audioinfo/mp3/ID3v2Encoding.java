// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.mp3;

import java.nio.charset.Charset;

public enum ID3v2Encoding
{
    ISO_8859_1(Charset.forName("ISO-8859-1"), 1), 
    UTF_16(Charset.forName("UTF-16"), 2), 
    UTF_16BE(Charset.forName("UTF-16BE"), 2), 
    UTF_8(Charset.forName("UTF-8"), 1);
    
    private final Charset charset;
    private final int zeroBytes;
    
    private ID3v2Encoding(final Charset charset, final int zeroBytes) {
        this.charset = charset;
        this.zeroBytes = zeroBytes;
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public int getZeroBytes() {
        return this.zeroBytes;
    }
}
