// 
// Decompiled by Procyon v0.5.34
// 

package com.mp4parser.iso14496.part15;

import java.util.Iterator;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.coremedia.iso.IsoTypeReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AvcDecoderConfigurationRecord
{
    public int avcLevelIndication;
    public int avcProfileIndication;
    public int bitDepthChromaMinus8;
    public int bitDepthChromaMinus8PaddingBits;
    public int bitDepthLumaMinus8;
    public int bitDepthLumaMinus8PaddingBits;
    public int chromaFormat;
    public int chromaFormatPaddingBits;
    public int configurationVersion;
    public boolean hasExts;
    public int lengthSizeMinusOne;
    public int lengthSizeMinusOnePaddingBits;
    public int numberOfSequenceParameterSetsPaddingBits;
    public List<byte[]> pictureParameterSets;
    public int profileCompatibility;
    public List<byte[]> sequenceParameterSetExts;
    public List<byte[]> sequenceParameterSets;
    
    public AvcDecoderConfigurationRecord() {
        this.sequenceParameterSets = new ArrayList<byte[]>();
        this.pictureParameterSets = new ArrayList<byte[]>();
        this.hasExts = true;
        this.chromaFormat = 1;
        this.bitDepthLumaMinus8 = 0;
        this.bitDepthChromaMinus8 = 0;
        this.sequenceParameterSetExts = new ArrayList<byte[]>();
        this.lengthSizeMinusOnePaddingBits = 63;
        this.numberOfSequenceParameterSetsPaddingBits = 7;
        this.chromaFormatPaddingBits = 31;
        this.bitDepthLumaMinus8PaddingBits = 31;
        this.bitDepthChromaMinus8PaddingBits = 31;
    }
    
    public AvcDecoderConfigurationRecord(final ByteBuffer byteBuffer) {
        this.sequenceParameterSets = new ArrayList<byte[]>();
        this.pictureParameterSets = new ArrayList<byte[]>();
        this.hasExts = true;
        this.chromaFormat = 1;
        final int n = 0;
        this.bitDepthLumaMinus8 = 0;
        this.bitDepthChromaMinus8 = 0;
        this.sequenceParameterSetExts = new ArrayList<byte[]>();
        this.lengthSizeMinusOnePaddingBits = 63;
        this.numberOfSequenceParameterSetsPaddingBits = 7;
        this.chromaFormatPaddingBits = 31;
        this.bitDepthLumaMinus8PaddingBits = 31;
        this.bitDepthChromaMinus8PaddingBits = 31;
        this.configurationVersion = IsoTypeReader.readUInt8(byteBuffer);
        this.avcProfileIndication = IsoTypeReader.readUInt8(byteBuffer);
        this.profileCompatibility = IsoTypeReader.readUInt8(byteBuffer);
        this.avcLevelIndication = IsoTypeReader.readUInt8(byteBuffer);
        final BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(byteBuffer);
        this.lengthSizeMinusOnePaddingBits = bitReaderBuffer.readBits(6);
        this.lengthSizeMinusOne = bitReaderBuffer.readBits(2);
        this.numberOfSequenceParameterSetsPaddingBits = bitReaderBuffer.readBits(3);
        for (int bits = bitReaderBuffer.readBits(5), i = 0; i < bits; ++i) {
            final byte[] dst = new byte[IsoTypeReader.readUInt16(byteBuffer)];
            byteBuffer.get(dst);
            this.sequenceParameterSets.add(dst);
        }
        final long n2 = IsoTypeReader.readUInt8(byteBuffer);
        for (int n3 = 0; n3 < n2; ++n3) {
            final byte[] dst2 = new byte[IsoTypeReader.readUInt16(byteBuffer)];
            byteBuffer.get(dst2);
            this.pictureParameterSets.add(dst2);
        }
        if (byteBuffer.remaining() < 4) {
            this.hasExts = false;
        }
        if (this.hasExts) {
            final int avcProfileIndication = this.avcProfileIndication;
            if (avcProfileIndication == 100 || avcProfileIndication == 110 || avcProfileIndication == 122 || avcProfileIndication == 144) {
                final BitReaderBuffer bitReaderBuffer2 = new BitReaderBuffer(byteBuffer);
                this.chromaFormatPaddingBits = bitReaderBuffer2.readBits(6);
                this.chromaFormat = bitReaderBuffer2.readBits(2);
                this.bitDepthLumaMinus8PaddingBits = bitReaderBuffer2.readBits(5);
                this.bitDepthLumaMinus8 = bitReaderBuffer2.readBits(3);
                this.bitDepthChromaMinus8PaddingBits = bitReaderBuffer2.readBits(5);
                this.bitDepthChromaMinus8 = bitReaderBuffer2.readBits(3);
                final long n4 = IsoTypeReader.readUInt8(byteBuffer);
                for (int n5 = n; n5 < n4; ++n5) {
                    final byte[] dst3 = new byte[IsoTypeReader.readUInt16(byteBuffer)];
                    byteBuffer.get(dst3);
                    this.sequenceParameterSetExts.add(dst3);
                }
                return;
            }
        }
        this.chromaFormat = -1;
        this.bitDepthLumaMinus8 = -1;
        this.bitDepthChromaMinus8 = -1;
    }
    
    public void getContent(final ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.configurationVersion);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcProfileIndication);
        IsoTypeWriter.writeUInt8(byteBuffer, this.profileCompatibility);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcLevelIndication);
        final BitWriterBuffer bitWriterBuffer = new BitWriterBuffer(byteBuffer);
        bitWriterBuffer.writeBits(this.lengthSizeMinusOnePaddingBits, 6);
        bitWriterBuffer.writeBits(this.lengthSizeMinusOne, 2);
        bitWriterBuffer.writeBits(this.numberOfSequenceParameterSetsPaddingBits, 3);
        bitWriterBuffer.writeBits(this.pictureParameterSets.size(), 5);
        for (final byte[] src : this.sequenceParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, src.length);
            byteBuffer.put(src);
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.pictureParameterSets.size());
        for (final byte[] src2 : this.pictureParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, src2.length);
            byteBuffer.put(src2);
        }
        if (this.hasExts) {
            final int avcProfileIndication = this.avcProfileIndication;
            if (avcProfileIndication == 100 || avcProfileIndication == 110 || avcProfileIndication == 122 || avcProfileIndication == 144) {
                final BitWriterBuffer bitWriterBuffer2 = new BitWriterBuffer(byteBuffer);
                bitWriterBuffer2.writeBits(this.chromaFormatPaddingBits, 6);
                bitWriterBuffer2.writeBits(this.chromaFormat, 2);
                bitWriterBuffer2.writeBits(this.bitDepthLumaMinus8PaddingBits, 5);
                bitWriterBuffer2.writeBits(this.bitDepthLumaMinus8, 3);
                bitWriterBuffer2.writeBits(this.bitDepthChromaMinus8PaddingBits, 5);
                bitWriterBuffer2.writeBits(this.bitDepthChromaMinus8, 3);
                for (final byte[] src3 : this.sequenceParameterSetExts) {
                    IsoTypeWriter.writeUInt16(byteBuffer, src3.length);
                    byteBuffer.put(src3);
                }
            }
        }
    }
    
    public long getContentSize() {
        final Iterator<byte[]> iterator = this.sequenceParameterSets.iterator();
        long n = 6L;
        while (iterator.hasNext()) {
            n = n + 2L + iterator.next().length;
        }
        long n2 = n + 1L;
        final Iterator<byte[]> iterator2 = this.pictureParameterSets.iterator();
        while (iterator2.hasNext()) {
            n2 = n2 + 2L + iterator2.next().length;
        }
        long n3 = n2;
        if (this.hasExts) {
            final int avcProfileIndication = this.avcProfileIndication;
            if (avcProfileIndication != 100 && avcProfileIndication != 110 && avcProfileIndication != 122) {
                n3 = n2;
                if (avcProfileIndication != 144) {
                    return n3;
                }
            }
            long n4 = n2 + 4L;
            final Iterator<byte[]> iterator3 = this.sequenceParameterSetExts.iterator();
            while (iterator3.hasNext()) {
                n4 = n4 + 2L + iterator3.next().length;
            }
            n3 = n4;
        }
        return n3;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AvcDecoderConfigurationRecord{configurationVersion=");
        sb.append(this.configurationVersion);
        sb.append(", avcProfileIndication=");
        sb.append(this.avcProfileIndication);
        sb.append(", profileCompatibility=");
        sb.append(this.profileCompatibility);
        sb.append(", avcLevelIndication=");
        sb.append(this.avcLevelIndication);
        sb.append(", lengthSizeMinusOne=");
        sb.append(this.lengthSizeMinusOne);
        sb.append(", hasExts=");
        sb.append(this.hasExts);
        sb.append(", chromaFormat=");
        sb.append(this.chromaFormat);
        sb.append(", bitDepthLumaMinus8=");
        sb.append(this.bitDepthLumaMinus8);
        sb.append(", bitDepthChromaMinus8=");
        sb.append(this.bitDepthChromaMinus8);
        sb.append(", lengthSizeMinusOnePaddingBits=");
        sb.append(this.lengthSizeMinusOnePaddingBits);
        sb.append(", numberOfSequenceParameterSetsPaddingBits=");
        sb.append(this.numberOfSequenceParameterSetsPaddingBits);
        sb.append(", chromaFormatPaddingBits=");
        sb.append(this.chromaFormatPaddingBits);
        sb.append(", bitDepthLumaMinus8PaddingBits=");
        sb.append(this.bitDepthLumaMinus8PaddingBits);
        sb.append(", bitDepthChromaMinus8PaddingBits=");
        sb.append(this.bitDepthChromaMinus8PaddingBits);
        sb.append('}');
        return sb.toString();
    }
}
