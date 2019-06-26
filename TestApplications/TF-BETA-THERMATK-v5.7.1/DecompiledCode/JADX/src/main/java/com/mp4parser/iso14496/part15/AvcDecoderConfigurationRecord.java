package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AvcDecoderConfigurationRecord {
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
        this.sequenceParameterSets = new ArrayList();
        this.pictureParameterSets = new ArrayList();
        this.hasExts = true;
        this.chromaFormat = 1;
        this.bitDepthLumaMinus8 = 0;
        this.bitDepthChromaMinus8 = 0;
        this.sequenceParameterSetExts = new ArrayList();
        this.lengthSizeMinusOnePaddingBits = 63;
        this.numberOfSequenceParameterSetsPaddingBits = 7;
        this.chromaFormatPaddingBits = 31;
        this.bitDepthLumaMinus8PaddingBits = 31;
        this.bitDepthChromaMinus8PaddingBits = 31;
    }

    public AvcDecoderConfigurationRecord(ByteBuffer byteBuffer) {
        this.sequenceParameterSets = new ArrayList();
        this.pictureParameterSets = new ArrayList();
        this.hasExts = true;
        this.chromaFormat = 1;
        int i = 0;
        this.bitDepthLumaMinus8 = 0;
        this.bitDepthChromaMinus8 = 0;
        this.sequenceParameterSetExts = new ArrayList();
        this.lengthSizeMinusOnePaddingBits = 63;
        this.numberOfSequenceParameterSetsPaddingBits = 7;
        this.chromaFormatPaddingBits = 31;
        this.bitDepthLumaMinus8PaddingBits = 31;
        this.bitDepthChromaMinus8PaddingBits = 31;
        this.configurationVersion = IsoTypeReader.readUInt8(byteBuffer);
        this.avcProfileIndication = IsoTypeReader.readUInt8(byteBuffer);
        this.profileCompatibility = IsoTypeReader.readUInt8(byteBuffer);
        this.avcLevelIndication = IsoTypeReader.readUInt8(byteBuffer);
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(byteBuffer);
        this.lengthSizeMinusOnePaddingBits = bitReaderBuffer.readBits(6);
        this.lengthSizeMinusOne = bitReaderBuffer.readBits(2);
        this.numberOfSequenceParameterSetsPaddingBits = bitReaderBuffer.readBits(3);
        int readBits = bitReaderBuffer.readBits(5);
        for (int i2 = 0; i2 < readBits; i2++) {
            byte[] bArr = new byte[IsoTypeReader.readUInt16(byteBuffer)];
            byteBuffer.get(bArr);
            this.sequenceParameterSets.add(bArr);
        }
        long readUInt8 = (long) IsoTypeReader.readUInt8(byteBuffer);
        for (readBits = 0; ((long) readBits) < readUInt8; readBits++) {
            byte[] bArr2 = new byte[IsoTypeReader.readUInt16(byteBuffer)];
            byteBuffer.get(bArr2);
            this.pictureParameterSets.add(bArr2);
        }
        if (byteBuffer.remaining() < 4) {
            this.hasExts = false;
        }
        if (this.hasExts) {
            readBits = this.avcProfileIndication;
            if (readBits == 100 || readBits == 110 || readBits == 122 || readBits == 144) {
                bitReaderBuffer = new BitReaderBuffer(byteBuffer);
                this.chromaFormatPaddingBits = bitReaderBuffer.readBits(6);
                this.chromaFormat = bitReaderBuffer.readBits(2);
                this.bitDepthLumaMinus8PaddingBits = bitReaderBuffer.readBits(5);
                this.bitDepthLumaMinus8 = bitReaderBuffer.readBits(3);
                this.bitDepthChromaMinus8PaddingBits = bitReaderBuffer.readBits(5);
                this.bitDepthChromaMinus8 = bitReaderBuffer.readBits(3);
                long readUInt82 = (long) IsoTypeReader.readUInt8(byteBuffer);
                while (((long) i) < readUInt82) {
                    byte[] bArr3 = new byte[IsoTypeReader.readUInt16(byteBuffer)];
                    byteBuffer.get(bArr3);
                    this.sequenceParameterSetExts.add(bArr3);
                    i++;
                }
                return;
            }
        }
        this.chromaFormat = -1;
        this.bitDepthLumaMinus8 = -1;
        this.bitDepthChromaMinus8 = -1;
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.configurationVersion);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcProfileIndication);
        IsoTypeWriter.writeUInt8(byteBuffer, this.profileCompatibility);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcLevelIndication);
        BitWriterBuffer bitWriterBuffer = new BitWriterBuffer(byteBuffer);
        bitWriterBuffer.writeBits(this.lengthSizeMinusOnePaddingBits, 6);
        bitWriterBuffer.writeBits(this.lengthSizeMinusOne, 2);
        bitWriterBuffer.writeBits(this.numberOfSequenceParameterSetsPaddingBits, 3);
        bitWriterBuffer.writeBits(this.pictureParameterSets.size(), 5);
        for (byte[] bArr : this.sequenceParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, bArr.length);
            byteBuffer.put(bArr);
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.pictureParameterSets.size());
        for (byte[] bArr2 : this.pictureParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, bArr2.length);
            byteBuffer.put(bArr2);
        }
        if (this.hasExts) {
            int i = this.avcProfileIndication;
            if (i == 100 || i == 110 || i == 122 || i == 144) {
                bitWriterBuffer = new BitWriterBuffer(byteBuffer);
                bitWriterBuffer.writeBits(this.chromaFormatPaddingBits, 6);
                bitWriterBuffer.writeBits(this.chromaFormat, 2);
                bitWriterBuffer.writeBits(this.bitDepthLumaMinus8PaddingBits, 5);
                bitWriterBuffer.writeBits(this.bitDepthLumaMinus8, 3);
                bitWriterBuffer.writeBits(this.bitDepthChromaMinus8PaddingBits, 5);
                bitWriterBuffer.writeBits(this.bitDepthChromaMinus8, 3);
                for (byte[] bArr3 : this.sequenceParameterSetExts) {
                    IsoTypeWriter.writeUInt16(byteBuffer, bArr3.length);
                    byteBuffer.put(bArr3);
                }
            }
        }
    }

    public long getContentSize() {
        long j = 6;
        for (byte[] length : this.sequenceParameterSets) {
            j = (j + 2) + ((long) length.length);
        }
        j++;
        for (byte[] length2 : this.pictureParameterSets) {
            j = (j + 2) + ((long) length2.length);
        }
        if (this.hasExts) {
            int i = this.avcProfileIndication;
            if (i == 100 || i == 110 || i == 122 || i == 144) {
                j += 4;
                for (byte[] length3 : this.sequenceParameterSetExts) {
                    j = (j + 2) + ((long) length3.length);
                }
            }
        }
        return j;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AvcDecoderConfigurationRecord{configurationVersion=");
        stringBuilder.append(this.configurationVersion);
        stringBuilder.append(", avcProfileIndication=");
        stringBuilder.append(this.avcProfileIndication);
        stringBuilder.append(", profileCompatibility=");
        stringBuilder.append(this.profileCompatibility);
        stringBuilder.append(", avcLevelIndication=");
        stringBuilder.append(this.avcLevelIndication);
        stringBuilder.append(", lengthSizeMinusOne=");
        stringBuilder.append(this.lengthSizeMinusOne);
        stringBuilder.append(", hasExts=");
        stringBuilder.append(this.hasExts);
        stringBuilder.append(", chromaFormat=");
        stringBuilder.append(this.chromaFormat);
        stringBuilder.append(", bitDepthLumaMinus8=");
        stringBuilder.append(this.bitDepthLumaMinus8);
        stringBuilder.append(", bitDepthChromaMinus8=");
        stringBuilder.append(this.bitDepthChromaMinus8);
        stringBuilder.append(", lengthSizeMinusOnePaddingBits=");
        stringBuilder.append(this.lengthSizeMinusOnePaddingBits);
        stringBuilder.append(", numberOfSequenceParameterSetsPaddingBits=");
        stringBuilder.append(this.numberOfSequenceParameterSetsPaddingBits);
        stringBuilder.append(", chromaFormatPaddingBits=");
        stringBuilder.append(this.chromaFormatPaddingBits);
        stringBuilder.append(", bitDepthLumaMinus8PaddingBits=");
        stringBuilder.append(this.bitDepthLumaMinus8PaddingBits);
        stringBuilder.append(", bitDepthChromaMinus8PaddingBits=");
        stringBuilder.append(this.bitDepthChromaMinus8PaddingBits);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
