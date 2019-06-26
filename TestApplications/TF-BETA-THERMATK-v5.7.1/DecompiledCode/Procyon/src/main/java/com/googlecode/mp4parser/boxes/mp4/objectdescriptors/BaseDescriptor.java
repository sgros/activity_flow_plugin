// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.io.IOException;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;

@Descriptor(tags = { 0 })
public abstract class BaseDescriptor
{
    int sizeBytes;
    int sizeOfInstance;
    int tag;
    
    public int getSize() {
        return this.sizeOfInstance + 1 + this.sizeBytes;
    }
    
    public int getSizeBytes() {
        return this.sizeBytes;
    }
    
    public int getSizeOfInstance() {
        return this.sizeOfInstance;
    }
    
    public final void parse(int n, final ByteBuffer byteBuffer) throws IOException {
        this.tag = n;
        int n2 = IsoTypeReader.readUInt8(byteBuffer);
        this.sizeOfInstance = (n2 & 0x7F);
        for (n = 1; n2 >>> 7 == 1; n2 = IsoTypeReader.readUInt8(byteBuffer), ++n, this.sizeOfInstance = (this.sizeOfInstance << 7 | (n2 & 0x7F))) {}
        this.sizeBytes = n;
        final ByteBuffer slice = byteBuffer.slice();
        slice.limit(this.sizeOfInstance);
        this.parseDetail(slice);
        byteBuffer.position(byteBuffer.position() + this.sizeOfInstance);
    }
    
    public abstract void parseDetail(final ByteBuffer p0) throws IOException;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BaseDescriptor");
        sb.append("{tag=");
        sb.append(this.tag);
        sb.append(", sizeOfInstance=");
        sb.append(this.sizeOfInstance);
        sb.append('}');
        return sb.toString();
    }
}
