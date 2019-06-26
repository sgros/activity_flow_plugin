// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = { 19 })
public class ExtensionProfileLevelDescriptor extends BaseDescriptor
{
    byte[] bytes;
    
    @Override
    public void parseDetail(final ByteBuffer byteBuffer) throws IOException {
        if (this.getSize() > 0) {
            byteBuffer.get(this.bytes = new byte[this.getSize()]);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ExtensionDescriptor");
        sb.append("{bytes=");
        final byte[] bytes = this.bytes;
        String encodeHex;
        if (bytes == null) {
            encodeHex = "null";
        }
        else {
            encodeHex = Hex.encodeHex(bytes);
        }
        sb.append(encodeHex);
        sb.append('}');
        return sb.toString();
    }
}
