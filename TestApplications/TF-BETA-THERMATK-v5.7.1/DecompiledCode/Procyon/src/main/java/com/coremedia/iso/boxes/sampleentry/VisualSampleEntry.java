// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes.sampleentry;

import java.io.IOException;
import com.coremedia.iso.Utf8;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import com.coremedia.iso.boxes.Container;

public final class VisualSampleEntry extends AbstractSampleEntry implements Container
{
    private String compressorname;
    private int depth;
    private int frameCount;
    private int height;
    private double horizresolution;
    private long[] predefined;
    private double vertresolution;
    private int width;
    
    public VisualSampleEntry(final String s) {
        super(s);
        this.horizresolution = 72.0;
        this.vertresolution = 72.0;
        this.frameCount = 1;
        this.compressorname = "";
        this.depth = 24;
        this.predefined = new long[3];
    }
    
    @Override
    public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(this.getHeader());
        final ByteBuffer allocate = ByteBuffer.allocate(78);
        allocate.position(6);
        IsoTypeWriter.writeUInt16(allocate, super.dataReferenceIndex);
        IsoTypeWriter.writeUInt16(allocate, 0);
        IsoTypeWriter.writeUInt16(allocate, 0);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[0]);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[1]);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[2]);
        IsoTypeWriter.writeUInt16(allocate, this.getWidth());
        IsoTypeWriter.writeUInt16(allocate, this.getHeight());
        IsoTypeWriter.writeFixedPoint1616(allocate, this.getHorizresolution());
        IsoTypeWriter.writeFixedPoint1616(allocate, this.getVertresolution());
        IsoTypeWriter.writeUInt32(allocate, 0L);
        IsoTypeWriter.writeUInt16(allocate, this.getFrameCount());
        IsoTypeWriter.writeUInt8(allocate, Utf8.utf8StringLengthInBytes(this.getCompressorname()));
        allocate.put(Utf8.convert(this.getCompressorname()));
        int i = Utf8.utf8StringLengthInBytes(this.getCompressorname());
        while (i < 31) {
            ++i;
            allocate.put((byte)0);
        }
        IsoTypeWriter.writeUInt16(allocate, this.getDepth());
        IsoTypeWriter.writeUInt16(allocate, 65535);
        writableByteChannel.write((ByteBuffer)allocate.rewind());
        this.writeContainer(writableByteChannel);
    }
    
    public String getCompressorname() {
        return this.compressorname;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getFrameCount() {
        return this.frameCount;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public double getHorizresolution() {
        return this.horizresolution;
    }
    
    @Override
    public long getSize() {
        final long n = this.getContainerSize() + 78L;
        int n2;
        if (!super.largeBox && 8L + n < 4294967296L) {
            n2 = 8;
        }
        else {
            n2 = 16;
        }
        return n + n2;
    }
    
    public double getVertresolution() {
        return this.vertresolution;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setDepth(final int depth) {
        this.depth = depth;
    }
    
    public void setFrameCount(final int frameCount) {
        this.frameCount = frameCount;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setHorizresolution(final double horizresolution) {
        this.horizresolution = horizresolution;
    }
    
    public void setVertresolution(final double vertresolution) {
        this.vertresolution = vertresolution;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
