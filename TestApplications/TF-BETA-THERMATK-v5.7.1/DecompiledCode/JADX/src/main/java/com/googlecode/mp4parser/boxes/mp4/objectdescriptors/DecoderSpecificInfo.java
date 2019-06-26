package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Descriptor(tags = {5})
public class DecoderSpecificInfo extends BaseDescriptor {
    byte[] bytes;

    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        int i = this.sizeOfInstance;
        if (i > 0) {
            this.bytes = new byte[i];
            byteBuffer.get(this.bytes);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DecoderSpecificInfo");
        stringBuilder.append("{bytes=");
        byte[] bArr = this.bytes;
        stringBuilder.append(bArr == null ? "null" : Hex.encodeHex(bArr));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DecoderSpecificInfo.class != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.bytes, ((DecoderSpecificInfo) obj).bytes);
    }

    public int hashCode() {
        byte[] bArr = this.bytes;
        return bArr != null ? Arrays.hashCode(bArr) : 0;
    }
}
