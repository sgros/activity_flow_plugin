// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.utils;

import java.io.OutputStream;
import locus.api.objects.Storable;
import java.util.List;
import java.io.IOException;

public class DataWriterBigEndian
{
    private byte[] mBuf;
    private int mCount;
    private int mCurrentPos;
    private int mSavedPos;
    private byte[] mWriteBuffer;
    
    public DataWriterBigEndian() {
        this(256);
    }
    
    public DataWriterBigEndian(final int i) {
        this.mWriteBuffer = new byte[8];
        if (i < 0) {
            throw new IllegalArgumentException("Negative initial size: " + i);
        }
        this.mBuf = new byte[i];
        this.reset();
    }
    
    private void ensureCapacity(final int n) {
        if (n - this.mBuf.length > 0) {
            this.grow(n);
        }
    }
    
    private void grow(final int n) {
        int n2;
        if ((n2 = this.mBuf.length << 1) - n < 0) {
            n2 = n;
        }
        int n3;
        if ((n3 = n2) < 0) {
            if (n < 0) {
                throw new OutOfMemoryError();
            }
            n3 = Integer.MAX_VALUE;
        }
        this.mBuf = Utils.copyOf(this.mBuf, n3);
    }
    
    private void setNewPositions(final int n) {
        if (this.mCurrentPos + n < this.mCount) {
            this.mCurrentPos += n;
        }
        else {
            this.mCurrentPos += n;
            this.mCount = this.mCurrentPos;
        }
    }
    
    public void moveTo(final int n) {
        if (n < 0 || n > this.mCount) {
            throw new IllegalArgumentException("Invalid move index:" + n + ", count:" + this.mCount);
        }
        this.mCurrentPos = n;
    }
    
    public void reset() {
        synchronized (this) {
            this.mCount = 0;
            this.mCurrentPos = 0;
            this.mSavedPos = 0;
        }
    }
    
    public void restorePosition() {
        this.mCurrentPos = this.mSavedPos;
    }
    
    public int size() {
        synchronized (this) {
            return this.mCount;
        }
    }
    
    public void storePosition() {
        this.mSavedPos = this.mCurrentPos;
    }
    
    public byte[] toByteArray() {
        synchronized (this) {
            return Utils.copyOf(this.mBuf, this.mCount);
        }
    }
    
    public void write(final int n) {
        synchronized (this) {
            this.ensureCapacity(this.mCurrentPos + 1);
            this.mBuf[this.mCurrentPos] = (byte)n;
            this.setNewPositions(1);
        }
    }
    
    public void write(final byte[] array) {
        synchronized (this) {
            this.write(array, 0, array.length);
        }
    }
    
    public void write(final byte[] array, final int n, final int newPositions) {
        // monitorenter(this)
        while (true) {
            if (n >= 0) {
                try {
                    if (n > array.length || newPositions < 0 || n + newPositions - array.length > 0) {
                        throw new IndexOutOfBoundsException();
                    }
                }
                finally {
                }
                // monitorexit(this)
                this.ensureCapacity(this.mCurrentPos + newPositions);
                final Throwable t;
                System.arraycopy(t, n, this.mBuf, this.mCurrentPos, newPositions);
                this.setNewPositions(newPositions);
                // monitorexit(this)
                return;
            }
            continue;
        }
    }
    
    public final void writeBoolean(final boolean b) throws IOException {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.write(n);
    }
    
    public final void writeDouble(final double value) throws IOException {
        this.writeLong(Double.doubleToLongBits(value));
    }
    
    public final void writeFloat(final float value) throws IOException {
        this.writeInt(Float.floatToIntBits(value));
    }
    
    public final void writeInt(final int n) throws IOException {
        this.mWriteBuffer[0] = (byte)(n >>> 24 & 0xFF);
        this.mWriteBuffer[1] = (byte)(n >>> 16 & 0xFF);
        this.mWriteBuffer[2] = (byte)(n >>> 8 & 0xFF);
        this.mWriteBuffer[3] = (byte)(n >>> 0 & 0xFF);
        this.write(this.mWriteBuffer, 0, 4);
    }
    
    public void writeListStorable(final List<? extends Storable> list) throws IOException {
        int size;
        if (list == null) {
            size = 0;
        }
        else {
            size = list.size();
        }
        this.writeInt(size);
        if (size != 0) {
            for (int i = 0; i < list.size(); ++i) {
                ((Storable)list.get(i)).write(this);
            }
        }
    }
    
    public void writeListString(final List<String> list) throws IOException {
        if (list == null || list.size() == 0) {
            this.writeInt(0);
        }
        else {
            this.writeInt(list.size());
            for (int i = 0; i < list.size(); ++i) {
                this.writeString(list.get(i));
            }
        }
    }
    
    public final void writeLong(final long n) throws IOException {
        this.mWriteBuffer[0] = (byte)(n >>> 56);
        this.mWriteBuffer[1] = (byte)(n >>> 48);
        this.mWriteBuffer[2] = (byte)(n >>> 40);
        this.mWriteBuffer[3] = (byte)(n >>> 32);
        this.mWriteBuffer[4] = (byte)(n >>> 24);
        this.mWriteBuffer[5] = (byte)(n >>> 16);
        this.mWriteBuffer[6] = (byte)(n >>> 8);
        this.mWriteBuffer[7] = (byte)(n >>> 0);
        this.write(this.mWriteBuffer, 0, 8);
    }
    
    public final void writeShort(final int n) throws IOException {
        this.write(n >>> 8 & 0xFF);
        this.write(n >>> 0 & 0xFF);
    }
    
    public final void writeStorable(final Storable storable) throws IOException {
        storable.write(this);
    }
    
    public final void writeString(final String s) throws IOException {
        if (s == null || s.length() == 0) {
            this.writeInt(0);
        }
        else {
            final byte[] bytes = s.getBytes("UTF-8");
            this.writeInt(bytes.length);
            this.write(bytes, 0, bytes.length);
        }
    }
    
    @Deprecated
    public final void writeStringDos(final String s) throws IOException {
        if (s == null || s.length() == 0) {
            this.writeShort(0);
        }
        else {
            final byte[] bytes = s.getBytes("UTF-8");
            this.writeShort(bytes.length);
            this.write(bytes, 0, bytes.length);
        }
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        synchronized (this) {
            outputStream.write(this.mBuf, 0, this.mCount);
        }
    }
}
