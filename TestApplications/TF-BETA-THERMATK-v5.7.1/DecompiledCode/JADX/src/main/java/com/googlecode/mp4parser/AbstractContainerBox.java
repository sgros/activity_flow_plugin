package com.googlecode.mp4parser;

import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class AbstractContainerBox extends BasicContainer implements Box {
    protected boolean largeBox;
    Container parent;
    protected String type;

    public AbstractContainerBox(String str) {
        this.type = str;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public long getSize() {
        long containerSize = getContainerSize();
        int i = (this.largeBox || 8 + containerSize >= 4294967296L) ? 16 : 8;
        return containerSize + ((long) i);
    }

    /* Access modifiers changed, original: protected */
    public ByteBuffer getHeader() {
        ByteBuffer wrap;
        byte[] bArr;
        if (this.largeBox || getSize() >= 4294967296L) {
            bArr = new byte[16];
            bArr[3] = (byte) 1;
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            wrap = ByteBuffer.wrap(bArr);
            wrap.position(8);
            IsoTypeWriter.writeUInt64(wrap, getSize());
        } else {
            bArr = new byte[8];
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            wrap = ByteBuffer.wrap(bArr);
            IsoTypeWriter.writeUInt32(wrap, getSize());
        }
        wrap.rewind();
        return wrap;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        writeContainer(writableByteChannel);
    }
}
