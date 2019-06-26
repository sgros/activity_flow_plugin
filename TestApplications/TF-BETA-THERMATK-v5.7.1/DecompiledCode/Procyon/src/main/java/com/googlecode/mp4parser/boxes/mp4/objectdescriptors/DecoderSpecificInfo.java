// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Descriptor(tags = { 5 })
public class DecoderSpecificInfo extends BaseDescriptor
{
    byte[] bytes;
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && DecoderSpecificInfo.class == o.getClass() && Arrays.equals(this.bytes, ((DecoderSpecificInfo)o).bytes));
    }
    
    @Override
    public int hashCode() {
        final byte[] bytes = this.bytes;
        int hashCode;
        if (bytes != null) {
            hashCode = Arrays.hashCode(bytes);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    @Override
    public void parseDetail(final ByteBuffer byteBuffer) throws IOException {
        final int sizeOfInstance = super.sizeOfInstance;
        if (sizeOfInstance > 0) {
            byteBuffer.get(this.bytes = new byte[sizeOfInstance]);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DecoderSpecificInfo");
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
