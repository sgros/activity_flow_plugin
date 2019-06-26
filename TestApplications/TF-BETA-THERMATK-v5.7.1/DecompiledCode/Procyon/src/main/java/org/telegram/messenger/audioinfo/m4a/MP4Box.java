// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.m4a;

import java.io.IOException;
import org.telegram.messenger.audioinfo.util.RangeInputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataInput;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public class MP4Box<I extends PositionInputStream>
{
    protected static final String ASCII = "ISO8859_1";
    private MP4Atom child;
    protected final DataInput data;
    private final I input;
    private final MP4Box<?> parent;
    private final String type;
    
    public MP4Box(final I n, final MP4Box<?> parent, final String type) {
        this.input = n;
        this.parent = parent;
        this.type = type;
        this.data = new DataInputStream(n);
    }
    
    protected MP4Atom getChild() {
        return this.child;
    }
    
    public I getInput() {
        return this.input;
    }
    
    public MP4Box<?> getParent() {
        return this.parent;
    }
    
    public long getPosition() {
        return this.input.getPosition();
    }
    
    public String getType() {
        return this.type;
    }
    
    public MP4Atom nextChild() throws IOException {
        final MP4Atom child = this.child;
        if (child != null) {
            child.skip();
        }
        final int int1 = this.data.readInt();
        final byte[] bytes = new byte[4];
        this.data.readFully(bytes);
        final String s = new String(bytes, "ISO8859_1");
        RangeInputStream rangeInputStream;
        if (int1 == 1) {
            rangeInputStream = new RangeInputStream(this.input, 16L, this.data.readLong() - 16L);
        }
        else {
            rangeInputStream = new RangeInputStream(this.input, 8L, int1 - 8);
        }
        return this.child = new MP4Atom(rangeInputStream, this, s);
    }
    
    public MP4Atom nextChild(final String s) throws IOException {
        final MP4Atom nextChild = this.nextChild();
        if (nextChild.getType().matches(s)) {
            return nextChild;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("atom type mismatch, expected ");
        sb.append(s);
        sb.append(", got ");
        sb.append(nextChild.getType());
        throw new IOException(sb.toString());
    }
}
