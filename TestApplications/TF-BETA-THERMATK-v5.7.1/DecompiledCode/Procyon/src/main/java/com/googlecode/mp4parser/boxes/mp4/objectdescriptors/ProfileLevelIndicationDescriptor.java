// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.io.IOException;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;

@Descriptor(tags = { 20 })
public class ProfileLevelIndicationDescriptor extends BaseDescriptor
{
    int profileLevelIndicationIndex;
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && ProfileLevelIndicationDescriptor.class == o.getClass() && this.profileLevelIndicationIndex == ((ProfileLevelIndicationDescriptor)o).profileLevelIndicationIndex);
    }
    
    @Override
    public int hashCode() {
        return this.profileLevelIndicationIndex;
    }
    
    @Override
    public void parseDetail(final ByteBuffer byteBuffer) throws IOException {
        this.profileLevelIndicationIndex = IsoTypeReader.readUInt8(byteBuffer);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ProfileLevelIndicationDescriptor");
        sb.append("{profileLevelIndicationIndex=");
        sb.append(Integer.toHexString(this.profileLevelIndicationIndex));
        sb.append('}');
        return sb.toString();
    }
}
