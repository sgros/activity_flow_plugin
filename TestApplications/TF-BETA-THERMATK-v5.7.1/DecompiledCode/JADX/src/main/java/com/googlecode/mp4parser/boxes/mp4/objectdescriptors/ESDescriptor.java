package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {3})
public class ESDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
    int URLFlag;
    int URLLength = 0;
    String URLString;
    DecoderConfigDescriptor decoderConfigDescriptor;
    int dependsOnEsId;
    int esId;
    int oCREsId;
    int oCRstreamFlag;
    List<BaseDescriptor> otherDescriptors = new ArrayList();
    int remoteODFlag;
    SLConfigDescriptor slConfigDescriptor;
    int streamDependenceFlag;
    int streamPriority;

    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        BaseDescriptor createFrom;
        long position;
        Logger logger;
        StringBuilder stringBuilder;
        this.esId = IsoTypeReader.readUInt16(byteBuffer);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        this.streamDependenceFlag = readUInt8 >>> 7;
        this.URLFlag = (readUInt8 >>> 6) & 1;
        this.oCRstreamFlag = (readUInt8 >>> 5) & 1;
        this.streamPriority = readUInt8 & 31;
        if (this.streamDependenceFlag == 1) {
            this.dependsOnEsId = IsoTypeReader.readUInt16(byteBuffer);
        }
        if (this.URLFlag == 1) {
            this.URLLength = IsoTypeReader.readUInt8(byteBuffer);
            this.URLString = IsoTypeReader.readString(byteBuffer, this.URLLength);
        }
        if (this.oCRstreamFlag == 1) {
            this.oCREsId = IsoTypeReader.readUInt16(byteBuffer);
        }
        int i = 0;
        readUInt8 = ((((getSizeBytes() + 1) + 2) + 1) + (this.streamDependenceFlag == 1 ? 2 : 0)) + (this.URLFlag == 1 ? this.URLLength + 1 : 0);
        if (this.oCRstreamFlag == 1) {
            i = 2;
        }
        readUInt8 += i;
        int position2 = byteBuffer.position();
        String str = ", size: ";
        if (getSize() > readUInt8 + 2) {
            createFrom = ObjectDescriptorFactory.createFrom(-1, byteBuffer);
            position = (long) (byteBuffer.position() - position2);
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(createFrom);
            stringBuilder.append(" - ESDescriptor1 read: ");
            stringBuilder.append(position);
            stringBuilder.append(str);
            stringBuilder.append(createFrom != null ? Integer.valueOf(createFrom.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (createFrom != null) {
                i = createFrom.getSize();
                byteBuffer.position(position2 + i);
                readUInt8 += i;
            } else {
                readUInt8 = (int) (((long) readUInt8) + position);
            }
            if (createFrom instanceof DecoderConfigDescriptor) {
                this.decoderConfigDescriptor = (DecoderConfigDescriptor) createFrom;
            }
        }
        position2 = byteBuffer.position();
        if (getSize() > readUInt8 + 2) {
            createFrom = ObjectDescriptorFactory.createFrom(-1, byteBuffer);
            position = (long) (byteBuffer.position() - position2);
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(createFrom);
            stringBuilder.append(" - ESDescriptor2 read: ");
            stringBuilder.append(position);
            stringBuilder.append(str);
            stringBuilder.append(createFrom != null ? Integer.valueOf(createFrom.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (createFrom != null) {
                i = createFrom.getSize();
                byteBuffer.position(position2 + i);
                readUInt8 += i;
            } else {
                readUInt8 = (int) (((long) readUInt8) + position);
            }
            if (createFrom instanceof SLConfigDescriptor) {
                this.slConfigDescriptor = (SLConfigDescriptor) createFrom;
            }
        } else {
            log.warning("SLConfigDescriptor is missing!");
        }
        while (getSize() - readUInt8 > 2) {
            position2 = byteBuffer.position();
            createFrom = ObjectDescriptorFactory.createFrom(-1, byteBuffer);
            position = (long) (byteBuffer.position() - position2);
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(createFrom);
            stringBuilder.append(" - ESDescriptor3 read: ");
            stringBuilder.append(position);
            stringBuilder.append(str);
            stringBuilder.append(createFrom != null ? Integer.valueOf(createFrom.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (createFrom != null) {
                i = createFrom.getSize();
                byteBuffer.position(position2 + i);
                readUInt8 += i;
            } else {
                readUInt8 = (int) (((long) readUInt8) + position);
            }
            this.otherDescriptors.add(createFrom);
        }
    }

    public int serializedSize() {
        int i = this.streamDependenceFlag > 0 ? 7 : 5;
        if (this.URLFlag > 0) {
            i += this.URLLength + 1;
        }
        if (this.oCRstreamFlag > 0) {
            i += 2;
        }
        return (i + this.decoderConfigDescriptor.serializedSize()) + this.slConfigDescriptor.serializedSize();
    }

    public ByteBuffer serialize() {
        ByteBuffer allocate = ByteBuffer.allocate(serializedSize());
        IsoTypeWriter.writeUInt8(allocate, 3);
        IsoTypeWriter.writeUInt8(allocate, serializedSize() - 2);
        IsoTypeWriter.writeUInt16(allocate, this.esId);
        IsoTypeWriter.writeUInt8(allocate, (((this.streamDependenceFlag << 7) | (this.URLFlag << 6)) | (this.oCRstreamFlag << 5)) | (this.streamPriority & 31));
        if (this.streamDependenceFlag > 0) {
            IsoTypeWriter.writeUInt16(allocate, this.dependsOnEsId);
        }
        if (this.URLFlag > 0) {
            IsoTypeWriter.writeUInt8(allocate, this.URLLength);
            IsoTypeWriter.writeUtf8String(allocate, this.URLString);
        }
        if (this.oCRstreamFlag > 0) {
            IsoTypeWriter.writeUInt16(allocate, this.oCREsId);
        }
        ByteBuffer serialize = this.decoderConfigDescriptor.serialize();
        ByteBuffer serialize2 = this.slConfigDescriptor.serialize();
        allocate.put(serialize.array());
        allocate.put(serialize2.array());
        return allocate;
    }

    public void setDecoderConfigDescriptor(DecoderConfigDescriptor decoderConfigDescriptor) {
        this.decoderConfigDescriptor = decoderConfigDescriptor;
    }

    public void setSlConfigDescriptor(SLConfigDescriptor sLConfigDescriptor) {
        this.slConfigDescriptor = sLConfigDescriptor;
    }

    public void setEsId(int i) {
        this.esId = i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ESDescriptor");
        stringBuilder.append("{esId=");
        stringBuilder.append(this.esId);
        stringBuilder.append(", streamDependenceFlag=");
        stringBuilder.append(this.streamDependenceFlag);
        stringBuilder.append(", URLFlag=");
        stringBuilder.append(this.URLFlag);
        stringBuilder.append(", oCRstreamFlag=");
        stringBuilder.append(this.oCRstreamFlag);
        stringBuilder.append(", streamPriority=");
        stringBuilder.append(this.streamPriority);
        stringBuilder.append(", URLLength=");
        stringBuilder.append(this.URLLength);
        stringBuilder.append(", URLString='");
        stringBuilder.append(this.URLString);
        stringBuilder.append('\'');
        stringBuilder.append(", remoteODFlag=");
        stringBuilder.append(this.remoteODFlag);
        stringBuilder.append(", dependsOnEsId=");
        stringBuilder.append(this.dependsOnEsId);
        stringBuilder.append(", oCREsId=");
        stringBuilder.append(this.oCREsId);
        stringBuilder.append(", decoderConfigDescriptor=");
        stringBuilder.append(this.decoderConfigDescriptor);
        stringBuilder.append(", slConfigDescriptor=");
        stringBuilder.append(this.slConfigDescriptor);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ESDescriptor.class != obj.getClass()) {
            return false;
        }
        ESDescriptor eSDescriptor = (ESDescriptor) obj;
        if (this.URLFlag != eSDescriptor.URLFlag || this.URLLength != eSDescriptor.URLLength || this.dependsOnEsId != eSDescriptor.dependsOnEsId || this.esId != eSDescriptor.esId || this.oCREsId != eSDescriptor.oCREsId || this.oCRstreamFlag != eSDescriptor.oCRstreamFlag || this.remoteODFlag != eSDescriptor.remoteODFlag || this.streamDependenceFlag != eSDescriptor.streamDependenceFlag || this.streamPriority != eSDescriptor.streamPriority) {
            return false;
        }
        String str = this.URLString;
        if (!str == null ? str.equals(eSDescriptor.URLString) : eSDescriptor.URLString == null) {
            return false;
        }
        DecoderConfigDescriptor decoderConfigDescriptor = this.decoderConfigDescriptor;
        if (!decoderConfigDescriptor == null ? decoderConfigDescriptor.equals(eSDescriptor.decoderConfigDescriptor) : eSDescriptor.decoderConfigDescriptor == null) {
            return false;
        }
        List list = this.otherDescriptors;
        if (!list == null ? list.equals(eSDescriptor.otherDescriptors) : eSDescriptor.otherDescriptors == null) {
            return false;
        }
        SLConfigDescriptor sLConfigDescriptor = this.slConfigDescriptor;
        return sLConfigDescriptor == null ? eSDescriptor.slConfigDescriptor == null : sLConfigDescriptor.equals(eSDescriptor.slConfigDescriptor);
    }

    public int hashCode() {
        int i = ((((((((((this.esId * 31) + this.streamDependenceFlag) * 31) + this.URLFlag) * 31) + this.oCRstreamFlag) * 31) + this.streamPriority) * 31) + this.URLLength) * 31;
        String str = this.URLString;
        int i2 = 0;
        i = (((((((i + (str != null ? str.hashCode() : 0)) * 31) + this.remoteODFlag) * 31) + this.dependsOnEsId) * 31) + this.oCREsId) * 31;
        DecoderConfigDescriptor decoderConfigDescriptor = this.decoderConfigDescriptor;
        i = (i + (decoderConfigDescriptor != null ? decoderConfigDescriptor.hashCode() : 0)) * 31;
        SLConfigDescriptor sLConfigDescriptor = this.slConfigDescriptor;
        i = (i + (sLConfigDescriptor != null ? sLConfigDescriptor.hashCode() : 0)) * 31;
        List list = this.otherDescriptors;
        if (list != null) {
            i2 = list.hashCode();
        }
        return i + i2;
    }
}
