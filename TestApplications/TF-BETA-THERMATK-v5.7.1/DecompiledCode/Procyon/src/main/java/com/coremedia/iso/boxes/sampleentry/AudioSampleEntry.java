// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes.sampleentry;

import java.io.IOException;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class AudioSampleEntry extends AbstractSampleEntry
{
    private long bytesPerFrame;
    private long bytesPerPacket;
    private long bytesPerSample;
    private int channelCount;
    private int compressionId;
    private int packetSize;
    private int reserved1;
    private long reserved2;
    private long sampleRate;
    private int sampleSize;
    private long samplesPerPacket;
    private int soundVersion;
    private byte[] soundVersion2Data;
    
    public AudioSampleEntry(final String s) {
        super(s);
    }
    
    @Override
    public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(this.getHeader());
        final int soundVersion = this.soundVersion;
        int n = 0;
        int n2;
        if (soundVersion == 1) {
            n2 = 16;
        }
        else {
            n2 = 0;
        }
        if (this.soundVersion == 2) {
            n = 36;
        }
        final ByteBuffer allocate = ByteBuffer.allocate(n2 + 28 + n);
        allocate.position(6);
        IsoTypeWriter.writeUInt16(allocate, super.dataReferenceIndex);
        IsoTypeWriter.writeUInt16(allocate, this.soundVersion);
        IsoTypeWriter.writeUInt16(allocate, this.reserved1);
        IsoTypeWriter.writeUInt32(allocate, this.reserved2);
        IsoTypeWriter.writeUInt16(allocate, this.channelCount);
        IsoTypeWriter.writeUInt16(allocate, this.sampleSize);
        IsoTypeWriter.writeUInt16(allocate, this.compressionId);
        IsoTypeWriter.writeUInt16(allocate, this.packetSize);
        if (super.type.equals("mlpa")) {
            IsoTypeWriter.writeUInt32(allocate, this.getSampleRate());
        }
        else {
            IsoTypeWriter.writeUInt32(allocate, this.getSampleRate() << 16);
        }
        if (this.soundVersion == 1) {
            IsoTypeWriter.writeUInt32(allocate, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerSample);
        }
        if (this.soundVersion == 2) {
            IsoTypeWriter.writeUInt32(allocate, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(allocate, this.bytesPerSample);
            allocate.put(this.soundVersion2Data);
        }
        writableByteChannel.write((ByteBuffer)allocate.rewind());
        this.writeContainer(writableByteChannel);
    }
    
    public int getChannelCount() {
        return this.channelCount;
    }
    
    public long getSampleRate() {
        return this.sampleRate;
    }
    
    @Override
    public long getSize() {
        final int soundVersion = this.soundVersion;
        final int n = 16;
        int n2 = 0;
        int n3;
        if (soundVersion == 1) {
            n3 = 16;
        }
        else {
            n3 = 0;
        }
        if (this.soundVersion == 2) {
            n2 = 36;
        }
        final long n4 = n3 + 28 + n2 + this.getContainerSize();
        int n5 = n;
        if (!super.largeBox) {
            if (8L + n4 >= 4294967296L) {
                n5 = n;
            }
            else {
                n5 = 8;
            }
        }
        return n4 + n5;
    }
    
    public void setChannelCount(final int channelCount) {
        this.channelCount = channelCount;
    }
    
    public void setSampleRate(final long sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    public void setSampleSize(final int sampleSize) {
        this.sampleSize = sampleSize;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AudioSampleEntry{bytesPerSample=");
        sb.append(this.bytesPerSample);
        sb.append(", bytesPerFrame=");
        sb.append(this.bytesPerFrame);
        sb.append(", bytesPerPacket=");
        sb.append(this.bytesPerPacket);
        sb.append(", samplesPerPacket=");
        sb.append(this.samplesPerPacket);
        sb.append(", packetSize=");
        sb.append(this.packetSize);
        sb.append(", compressionId=");
        sb.append(this.compressionId);
        sb.append(", soundVersion=");
        sb.append(this.soundVersion);
        sb.append(", sampleRate=");
        sb.append(this.sampleRate);
        sb.append(", sampleSize=");
        sb.append(this.sampleSize);
        sb.append(", channelCount=");
        sb.append(this.channelCount);
        sb.append(", boxes=");
        sb.append(this.getBoxes());
        sb.append('}');
        return sb.toString();
    }
}
