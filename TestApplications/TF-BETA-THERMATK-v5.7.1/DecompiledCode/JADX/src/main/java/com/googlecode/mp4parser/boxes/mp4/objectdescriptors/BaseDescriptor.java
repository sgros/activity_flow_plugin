package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {0})
public abstract class BaseDescriptor {
    int sizeBytes;
    int sizeOfInstance;
    int tag;

    public abstract void parseDetail(ByteBuffer byteBuffer) throws IOException;

    public int getSize() {
        return (this.sizeOfInstance + 1) + this.sizeBytes;
    }

    public int getSizeOfInstance() {
        return this.sizeOfInstance;
    }

    public int getSizeBytes() {
        return this.sizeBytes;
    }

    public final void parse(int i, ByteBuffer byteBuffer) throws IOException {
        this.tag = i;
        i = IsoTypeReader.readUInt8(byteBuffer);
        this.sizeOfInstance = i & 127;
        int i2 = 1;
        while ((i >>> 7) == 1) {
            i = IsoTypeReader.readUInt8(byteBuffer);
            i2++;
            this.sizeOfInstance = (this.sizeOfInstance << 7) | (i & 127);
        }
        this.sizeBytes = i2;
        ByteBuffer slice = byteBuffer.slice();
        slice.limit(this.sizeOfInstance);
        parseDetail(slice);
        byteBuffer.position(byteBuffer.position() + this.sizeOfInstance);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BaseDescriptor");
        stringBuilder.append("{tag=");
        stringBuilder.append(this.tag);
        stringBuilder.append(", sizeOfInstance=");
        stringBuilder.append(this.sizeOfInstance);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
