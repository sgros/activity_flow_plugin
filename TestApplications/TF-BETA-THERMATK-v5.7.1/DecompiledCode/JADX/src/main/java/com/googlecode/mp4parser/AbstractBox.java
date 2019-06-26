package com.googlecode.mp4parser;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBox implements Box {
    private static Logger LOG = Logger.getLogger(AbstractBox.class);
    private ByteBuffer content;
    long contentStartPosition;
    DataSource dataSource;
    private ByteBuffer deadBytes = null;
    boolean isParsed;
    boolean isRead;
    long memMapSize = -1;
    private Container parent;
    protected String type;
    private byte[] userType;

    public abstract void _parseDetails(ByteBuffer byteBuffer);

    public abstract void getContent(ByteBuffer byteBuffer);

    public abstract long getContentSize();

    private synchronized void readContent() {
        if (!this.isRead) {
            try {
                Logger logger = LOG;
                StringBuilder stringBuilder = new StringBuilder("mem mapping ");
                stringBuilder.append(getType());
                logger.logDebug(stringBuilder.toString());
                this.content = this.dataSource.map(this.contentStartPosition, this.memMapSize);
                this.isRead = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected AbstractBox(String str) {
        this.type = str;
        this.isRead = true;
        this.isParsed = true;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        String str = "uuid";
        int i = 8;
        int i2 = 0;
        int i3 = 16;
        ByteBuffer allocate;
        if (!this.isRead) {
            if (!isSmallBox()) {
                i = 16;
            }
            if (str.equals(getType())) {
                i2 = 16;
            }
            allocate = ByteBuffer.allocate(i + i2);
            getHeader(allocate);
            writableByteChannel.write((ByteBuffer) allocate.rewind());
            this.dataSource.transferTo(this.contentStartPosition, this.memMapSize, writableByteChannel);
        } else if (this.isParsed) {
            allocate = ByteBuffer.allocate(CastUtils.l2i(getSize()));
            getHeader(allocate);
            getContent(allocate);
            ByteBuffer byteBuffer = this.deadBytes;
            if (byteBuffer != null) {
                byteBuffer.rewind();
                while (this.deadBytes.remaining() > 0) {
                    allocate.put(this.deadBytes);
                }
            }
            writableByteChannel.write((ByteBuffer) allocate.rewind());
        } else {
            if (!isSmallBox()) {
                i = 16;
            }
            if (!str.equals(getType())) {
                i3 = 0;
            }
            allocate = ByteBuffer.allocate(i + i3);
            getHeader(allocate);
            writableByteChannel.write((ByteBuffer) allocate.rewind());
            writableByteChannel.write((ByteBuffer) this.content.position(0));
        }
    }

    public final synchronized void parseDetails() {
        readContent();
        Logger logger = LOG;
        StringBuilder stringBuilder = new StringBuilder("parsing details of ");
        stringBuilder.append(getType());
        logger.logDebug(stringBuilder.toString());
        if (this.content != null) {
            ByteBuffer byteBuffer = this.content;
            this.isParsed = true;
            byteBuffer.rewind();
            _parseDetails(byteBuffer);
            if (byteBuffer.remaining() > 0) {
                this.deadBytes = byteBuffer.slice();
            }
            this.content = null;
        }
    }

    public long getSize() {
        long j;
        ByteBuffer byteBuffer;
        int i = 0;
        if (!this.isRead) {
            j = this.memMapSize;
        } else if (this.isParsed) {
            j = getContentSize();
        } else {
            byteBuffer = this.content;
            j = (long) (byteBuffer != null ? byteBuffer.limit() : 0);
        }
        j += (long) (((j >= 4294967288L ? 8 : 0) + 8) + ("uuid".equals(getType()) ? 16 : 0));
        byteBuffer = this.deadBytes;
        if (byteBuffer != null) {
            i = byteBuffer.limit();
        }
        return j + ((long) i);
    }

    public String getType() {
        return this.type;
    }

    public byte[] getUserType() {
        return this.userType;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public boolean isParsed() {
        return this.isParsed;
    }

    private boolean isSmallBox() {
        int i = "uuid".equals(getType()) ? 24 : 8;
        if (!this.isRead) {
            return this.memMapSize + ((long) i) < 4294967296L;
        } else {
            if (!this.isParsed) {
                return ((long) (this.content.limit() + i)) < 4294967296L;
            } else {
                long contentSize = getContentSize();
                ByteBuffer byteBuffer = this.deadBytes;
                return (contentSize + ((long) (byteBuffer != null ? byteBuffer.limit() : 0))) + ((long) i) < 4294967296L;
            }
        }
    }

    private void getHeader(ByteBuffer byteBuffer) {
        if (isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1);
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        }
        if ("uuid".equals(getType())) {
            byteBuffer.put(getUserType());
        }
    }
}
