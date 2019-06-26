// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import java.io.IOException;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import com.googlecode.mp4parser.AbstractContainerBox;

public class DataReferenceBox extends AbstractContainerBox implements FullBox
{
    private int flags;
    private int version;
    
    public DataReferenceBox() {
        super("dref");
    }
    
    @Override
    public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(this.getHeader());
        final ByteBuffer allocate = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt8(allocate, this.version);
        IsoTypeWriter.writeUInt24(allocate, this.flags);
        IsoTypeWriter.writeUInt32(allocate, this.getBoxes().size());
        writableByteChannel.write((ByteBuffer)allocate.rewind());
        this.writeContainer(writableByteChannel);
    }
    
    @Override
    public long getSize() {
        final long n = this.getContainerSize() + 8L;
        int n2;
        if (!super.largeBox && 8L + n < 4294967296L) {
            n2 = 8;
        }
        else {
            n2 = 16;
        }
        return n + n2;
    }
}
