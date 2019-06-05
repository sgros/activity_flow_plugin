package locus.api.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import locus.api.objects.Storable;

public class DataWriterBigEndian {
    private byte[] mBuf;
    private int mCount;
    private int mCurrentPos;
    private int mSavedPos;
    private byte[] mWriteBuffer;

    public DataWriterBigEndian() {
        this(256);
    }

    public DataWriterBigEndian(int capacity) {
        this.mWriteBuffer = new byte[8];
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative initial size: " + capacity);
        }
        this.mBuf = new byte[capacity];
        reset();
    }

    public synchronized void reset() {
        this.mCount = 0;
        this.mCurrentPos = 0;
        this.mSavedPos = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - this.mBuf.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int newCapacity = this.mBuf.length << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity < 0) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            newCapacity = Integer.MAX_VALUE;
        }
        this.mBuf = Utils.copyOf(this.mBuf, newCapacity);
    }

    private void setNewPositions(int bytesWrote) {
        if (this.mCurrentPos + bytesWrote < this.mCount) {
            this.mCurrentPos += bytesWrote;
            return;
        }
        this.mCurrentPos += bytesWrote;
        this.mCount = this.mCurrentPos;
    }

    public void storePosition() {
        this.mSavedPos = this.mCurrentPos;
    }

    public void restorePosition() {
        this.mCurrentPos = this.mSavedPos;
    }

    public void moveTo(int index) {
        if (index < 0 || index > this.mCount) {
            throw new IllegalArgumentException("Invalid move index:" + index + ", count:" + this.mCount);
        }
        this.mCurrentPos = index;
    }

    public synchronized void write(int b) {
        ensureCapacity(this.mCurrentPos + 1);
        this.mBuf[this.mCurrentPos] = (byte) b;
        setNewPositions(1);
    }

    public synchronized void write(byte[] b) {
        write(b, 0, b.length);
    }

    public synchronized void write(byte[] b, int off, int len) {
        if (off >= 0) {
            if (off <= b.length && len >= 0 && (off + len) - b.length <= 0) {
                ensureCapacity(this.mCurrentPos + len);
                System.arraycopy(b, off, this.mBuf, this.mCurrentPos, len);
                setNewPositions(len);
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public final void writeBoolean(boolean v) throws IOException {
        write(v ? 1 : 0);
    }

    public final void writeShort(int v) throws IOException {
        write((v >>> 8) & 255);
        write((v >>> 0) & 255);
    }

    public final void writeInt(int v) throws IOException {
        this.mWriteBuffer[0] = (byte) ((v >>> 24) & 255);
        this.mWriteBuffer[1] = (byte) ((v >>> 16) & 255);
        this.mWriteBuffer[2] = (byte) ((v >>> 8) & 255);
        this.mWriteBuffer[3] = (byte) ((v >>> 0) & 255);
        write(this.mWriteBuffer, 0, 4);
    }

    public final void writeLong(long v) throws IOException {
        this.mWriteBuffer[0] = (byte) ((int) (v >>> 56));
        this.mWriteBuffer[1] = (byte) ((int) (v >>> 48));
        this.mWriteBuffer[2] = (byte) ((int) (v >>> 40));
        this.mWriteBuffer[3] = (byte) ((int) (v >>> 32));
        this.mWriteBuffer[4] = (byte) ((int) (v >>> 24));
        this.mWriteBuffer[5] = (byte) ((int) (v >>> 16));
        this.mWriteBuffer[6] = (byte) ((int) (v >>> 8));
        this.mWriteBuffer[7] = (byte) ((int) (v >>> 0));
        write(this.mWriteBuffer, 0, 8);
    }

    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public final void writeString(String string) throws IOException {
        if (string == null || string.length() == 0) {
            writeInt(0);
            return;
        }
        byte[] bytes = string.getBytes("UTF-8");
        writeInt(bytes.length);
        write(bytes, 0, bytes.length);
    }

    @Deprecated
    public final void writeStringDos(String string) throws IOException {
        if (string == null || string.length() == 0) {
            writeShort(0);
            return;
        }
        byte[] bytes = string.getBytes("UTF-8");
        writeShort(bytes.length);
        write(bytes, 0, bytes.length);
    }

    public final void writeStorable(Storable obj) throws IOException {
        obj.write(this);
    }

    public void writeListString(List<String> objs) throws IOException {
        if (objs == null || objs.size() == 0) {
            writeInt(0);
            return;
        }
        writeInt(objs.size());
        int n = objs.size();
        for (int i = 0; i < n; i++) {
            writeString((String) objs.get(i));
        }
    }

    public void writeListStorable(List<? extends Storable> objs) throws IOException {
        int size;
        if (objs == null) {
            size = 0;
        } else {
            size = objs.size();
        }
        writeInt(size);
        if (size != 0) {
            int n = objs.size();
            for (int i = 0; i < n; i++) {
                ((Storable) objs.get(i)).write(this);
            }
        }
    }

    public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(this.mBuf, 0, this.mCount);
    }

    public synchronized byte[] toByteArray() {
        return Utils.copyOf(this.mBuf, this.mCount);
    }

    public synchronized int size() {
        return this.mCount;
    }
}
