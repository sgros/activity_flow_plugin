// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.m4a;

import java.io.EOFException;
import java.math.BigDecimal;
import java.io.IOException;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class MP4Atom extends MP4Box<RangeInputStream>
{
    public MP4Atom(final RangeInputStream rangeInputStream, final MP4Box<?> mp4Box, final String s) {
        super(rangeInputStream, mp4Box, s);
    }
    
    private StringBuffer appendPath(final StringBuffer sb, final MP4Box<?> mp4Box) {
        if (mp4Box.getParent() != null) {
            this.appendPath(sb, mp4Box.getParent());
            sb.append("/");
        }
        sb.append(mp4Box.getType());
        return sb;
    }
    
    public long getLength() {
        return this.getInput().getPosition() + this.getInput().getRemainingLength();
    }
    
    public long getOffset() {
        return this.getParent().getPosition() - this.getPosition();
    }
    
    public String getPath() {
        final StringBuffer sb = new StringBuffer();
        this.appendPath(sb, this);
        return sb.toString();
    }
    
    public long getRemaining() {
        return this.getInput().getRemainingLength();
    }
    
    public boolean hasMoreChildren() {
        long remaining;
        if (this.getChild() != null) {
            remaining = this.getChild().getRemaining();
        }
        else {
            remaining = 0L;
        }
        return remaining < this.getRemaining();
    }
    
    public MP4Atom nextChildUpTo(final String s) throws IOException {
        while (this.getRemaining() > 0L) {
            final MP4Atom nextChild = this.nextChild();
            if (nextChild.getType().matches(s)) {
                return nextChild;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("atom type mismatch, not found: ");
        sb.append(s);
        throw new IOException(sb.toString());
    }
    
    public boolean readBoolean() throws IOException {
        return super.data.readBoolean();
    }
    
    public byte readByte() throws IOException {
        return super.data.readByte();
    }
    
    public byte[] readBytes() throws IOException {
        return this.readBytes((int)this.getRemaining());
    }
    
    public byte[] readBytes(final int n) throws IOException {
        final byte[] array = new byte[n];
        super.data.readFully(array);
        return array;
    }
    
    public int readInt() throws IOException {
        return super.data.readInt();
    }
    
    public BigDecimal readIntegerFixedPoint() throws IOException {
        final short short1 = super.data.readShort();
        final int unsignedShort = super.data.readUnsignedShort();
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(short1));
        sb.append("");
        sb.append(String.valueOf(unsignedShort));
        return new BigDecimal(sb.toString());
    }
    
    public long readLong() throws IOException {
        return super.data.readLong();
    }
    
    public short readShort() throws IOException {
        return super.data.readShort();
    }
    
    public BigDecimal readShortFixedPoint() throws IOException {
        final byte byte1 = super.data.readByte();
        final int unsignedByte = super.data.readUnsignedByte();
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(byte1));
        sb.append("");
        sb.append(String.valueOf(unsignedByte));
        return new BigDecimal(sb.toString());
    }
    
    public String readString(int index, String substring) throws IOException {
        substring = new String(this.readBytes(index), substring);
        index = substring.indexOf(0);
        if (index >= 0) {
            substring = substring.substring(0, index);
        }
        return substring;
    }
    
    public String readString(final String s) throws IOException {
        return this.readString((int)this.getRemaining(), s);
    }
    
    public void skip() throws IOException {
        while (this.getRemaining() > 0L) {
            if (this.getInput().skip(this.getRemaining()) != 0L) {
                continue;
            }
            throw new EOFException("Cannot skip atom");
        }
    }
    
    public void skip(final int n) throws IOException {
        int skipBytes;
        for (int i = 0; i < n; i += skipBytes) {
            skipBytes = super.data.skipBytes(n - i);
            if (skipBytes <= 0) {
                throw new EOFException();
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        this.appendPath(sb, this);
        sb.append("[off=");
        sb.append(this.getOffset());
        sb.append(",pos=");
        sb.append(this.getPosition());
        sb.append(",len=");
        sb.append(this.getLength());
        sb.append("]");
        return sb.toString();
    }
}
