// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.CastUtils;
import java.nio.channels.WritableByteChannel;
import java.io.IOException;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Container;
import java.nio.ByteBuffer;
import com.googlecode.mp4parser.util.Logger;
import com.coremedia.iso.boxes.Box;

public abstract class AbstractBox implements Box
{
    private static Logger LOG;
    private ByteBuffer content;
    long contentStartPosition;
    DataSource dataSource;
    private ByteBuffer deadBytes;
    boolean isParsed;
    boolean isRead;
    long memMapSize;
    private Container parent;
    protected String type;
    private byte[] userType;
    
    static {
        AbstractBox.LOG = Logger.getLogger(AbstractBox.class);
    }
    
    protected AbstractBox(final String type) {
        this.memMapSize = -1L;
        this.deadBytes = null;
        this.type = type;
        this.isRead = true;
        this.isParsed = true;
    }
    
    private void getHeader(final ByteBuffer byteBuffer) {
        if (this.isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(this.getType()));
        }
        else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1L);
            byteBuffer.put(IsoFile.fourCCtoBytes(this.getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, this.getSize());
        }
        if ("uuid".equals(this.getType())) {
            byteBuffer.put(this.getUserType());
        }
    }
    
    private boolean isSmallBox() {
        int n;
        if ("uuid".equals(this.getType())) {
            n = 24;
        }
        else {
            n = 8;
        }
        if (!this.isRead) {
            return this.memMapSize + n < 4294967296L;
        }
        if (this.isParsed) {
            final long contentSize = this.getContentSize();
            final ByteBuffer deadBytes = this.deadBytes;
            int limit;
            if (deadBytes != null) {
                limit = deadBytes.limit();
            }
            else {
                limit = 0;
            }
            return contentSize + limit + n < 4294967296L;
        }
        return this.content.limit() + n < 4294967296L;
    }
    
    private void readContent() {
        synchronized (this) {
            if (!this.isRead) {
                try {
                    final Logger log = AbstractBox.LOG;
                    final StringBuilder sb = new StringBuilder("mem mapping ");
                    sb.append(this.getType());
                    log.logDebug(sb.toString());
                    this.content = this.dataSource.map(this.contentStartPosition, this.memMapSize);
                    this.isRead = true;
                }
                catch (IOException cause) {
                    throw new RuntimeException(cause);
                }
            }
        }
    }
    
    protected abstract void _parseDetails(final ByteBuffer p0);
    
    @Override
    public void getBox(final WritableByteChannel writableByteChannel) throws IOException {
        final boolean isRead = this.isRead;
        int n = 8;
        final int n2 = 0;
        int n3 = 16;
        if (isRead) {
            if (this.isParsed) {
                final ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(this.getSize()));
                this.getHeader(allocate);
                this.getContent(allocate);
                final ByteBuffer deadBytes = this.deadBytes;
                if (deadBytes != null) {
                    deadBytes.rewind();
                    while (this.deadBytes.remaining() > 0) {
                        allocate.put(this.deadBytes);
                    }
                }
                writableByteChannel.write((ByteBuffer)allocate.rewind());
            }
            else {
                if (!this.isSmallBox()) {
                    n = 16;
                }
                if (!"uuid".equals(this.getType())) {
                    n3 = 0;
                }
                final ByteBuffer allocate2 = ByteBuffer.allocate(n + n3);
                this.getHeader(allocate2);
                writableByteChannel.write((ByteBuffer)allocate2.rewind());
                writableByteChannel.write((ByteBuffer)this.content.position(0));
            }
        }
        else {
            if (!this.isSmallBox()) {
                n = 16;
            }
            int n4 = n2;
            if ("uuid".equals(this.getType())) {
                n4 = 16;
            }
            final ByteBuffer allocate3 = ByteBuffer.allocate(n + n4);
            this.getHeader(allocate3);
            writableByteChannel.write((ByteBuffer)allocate3.rewind());
            this.dataSource.transferTo(this.contentStartPosition, this.memMapSize, writableByteChannel);
        }
    }
    
    protected abstract void getContent(final ByteBuffer p0);
    
    protected abstract long getContentSize();
    
    @Override
    public long getSize() {
        final boolean isRead = this.isRead;
        final int n = 0;
        long n2;
        if (isRead) {
            if (this.isParsed) {
                n2 = this.getContentSize();
            }
            else {
                final ByteBuffer content = this.content;
                int limit;
                if (content != null) {
                    limit = content.limit();
                }
                else {
                    limit = 0;
                }
                n2 = limit;
            }
        }
        else {
            n2 = this.memMapSize;
        }
        int n3;
        if (n2 >= 4294967288L) {
            n3 = 8;
        }
        else {
            n3 = 0;
        }
        int n4;
        if ("uuid".equals(this.getType())) {
            n4 = 16;
        }
        else {
            n4 = 0;
        }
        final long n5 = n3 + 8 + n4;
        final ByteBuffer deadBytes = this.deadBytes;
        int limit2;
        if (deadBytes == null) {
            limit2 = n;
        }
        else {
            limit2 = deadBytes.limit();
        }
        return n2 + n5 + limit2;
    }
    
    public String getType() {
        return this.type;
    }
    
    public byte[] getUserType() {
        return this.userType;
    }
    
    public boolean isParsed() {
        return this.isParsed;
    }
    
    public final void parseDetails() {
        synchronized (this) {
            this.readContent();
            final Logger log = AbstractBox.LOG;
            final StringBuilder sb = new StringBuilder("parsing details of ");
            sb.append(this.getType());
            log.logDebug(sb.toString());
            if (this.content != null) {
                final ByteBuffer content = this.content;
                this.isParsed = true;
                content.rewind();
                this._parseDetails(content);
                if (content.remaining() > 0) {
                    this.deadBytes = content.slice();
                }
                this.content = null;
            }
        }
    }
    
    @Override
    public void setParent(final Container parent) {
        this.parent = parent;
    }
}
