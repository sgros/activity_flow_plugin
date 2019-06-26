// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.Box;

public class AbstractContainerBox extends BasicContainer implements Box
{
    protected boolean largeBox;
    Container parent;
    protected String type;
    
    public AbstractContainerBox(final String type) {
        this.type = type;
    }
    
    @Override
    public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(this.getHeader());
        this.writeContainer(writableByteChannel);
    }
    
    protected ByteBuffer getHeader() {
        ByteBuffer byteBuffer;
        if (!this.largeBox && this.getSize() < 4294967296L) {
            byteBuffer = ByteBuffer.wrap(new byte[] { 0, 0, 0, 0, this.type.getBytes()[0], this.type.getBytes()[1], this.type.getBytes()[2], this.type.getBytes()[3] });
            IsoTypeWriter.writeUInt32(byteBuffer, this.getSize());
        }
        else {
            byteBuffer = ByteBuffer.wrap(new byte[] { 0, 0, 0, 1, this.type.getBytes()[0], this.type.getBytes()[1], this.type.getBytes()[2], this.type.getBytes()[3], 0, 0, 0, 0, 0, 0, 0, 0 });
            byteBuffer.position(8);
            IsoTypeWriter.writeUInt64(byteBuffer, this.getSize());
        }
        byteBuffer.rewind();
        return byteBuffer;
    }
    
    @Override
    public long getSize() {
        final long containerSize = this.getContainerSize();
        int n;
        if (!this.largeBox && 8L + containerSize < 4294967296L) {
            n = 8;
        }
        else {
            n = 16;
        }
        return containerSize + n;
    }
    
    @Override
    public void setParent(final Container parent) {
        this.parent = parent;
    }
}
