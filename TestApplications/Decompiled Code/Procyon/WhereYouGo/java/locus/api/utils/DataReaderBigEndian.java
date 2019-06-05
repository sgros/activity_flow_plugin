// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.utils;

import java.util.ArrayList;
import java.util.List;
import locus.api.objects.Storable;
import java.io.IOException;

public class DataReaderBigEndian
{
    private static final String TAG;
    private byte[] mBuffer;
    private int mPosition;
    
    static {
        TAG = DataReaderBigEndian.class.getSimpleName();
    }
    
    public DataReaderBigEndian(final byte[] mBuffer) throws IOException {
        if (mBuffer == null) {
            throw new IOException("Invalid parameter");
        }
        this.mPosition = 0;
        this.mBuffer = mBuffer;
    }
    
    private void checkPosition(final int i) {
        this.mPosition += i;
        if (this.mPosition > this.mBuffer.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid position for data load. Current:" + this.mPosition + ", " + "length:" + this.mBuffer.length + ", " + "increment:" + i);
        }
    }
    
    public int available() {
        return this.mBuffer.length - this.mPosition;
    }
    
    public long length() {
        return this.mBuffer.length;
    }
    
    public boolean readBoolean() {
        boolean b = true;
        this.checkPosition(1);
        if (this.mBuffer[this.mPosition - 1] == 0) {
            b = false;
        }
        return b;
    }
    
    public void readBytes(final byte[] array) {
        this.checkPosition(array.length);
        System.arraycopy(this.mBuffer, this.mPosition - array.length, array, 0, array.length);
    }
    
    public byte[] readBytes(final int n) {
        this.checkPosition(n);
        final byte[] array = new byte[n];
        System.arraycopy(this.mBuffer, this.mPosition - n, array, 0, n);
        return array;
    }
    
    public final double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }
    
    public final float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }
    
    public int readInt() {
        this.checkPosition(4);
        return this.mBuffer[this.mPosition - 4] << 24 | (this.mBuffer[this.mPosition - 3] & 0xFF) << 16 | (this.mBuffer[this.mPosition - 2] & 0xFF) << 8 | (this.mBuffer[this.mPosition - 1] & 0xFF);
    }
    
    public List<? extends Storable> readListStorable(final Class<? extends Storable> clazz) throws IOException {
        final ArrayList<Storable> list = new ArrayList<Storable>();
        final int int1 = this.readInt();
        if (int1 != 0) {
            int i = 0;
            while (i < int1) {
                while (true) {
                    try {
                        final Storable storable = (Storable)clazz.newInstance();
                        storable.read(this);
                        list.add(storable);
                        ++i;
                    }
                    catch (InstantiationException ex) {
                        Logger.logE(DataReaderBigEndian.TAG, "readList(" + clazz + ")", ex);
                        continue;
                    }
                    catch (IllegalAccessException ex2) {
                        Logger.logE(DataReaderBigEndian.TAG, "readList(" + clazz + ")", ex2);
                        continue;
                    }
                    break;
                }
            }
        }
        return list;
    }
    
    public List<String> readListString() throws IOException {
        final ArrayList<String> list = new ArrayList<String>();
        final int int1 = this.readInt();
        if (int1 != 0) {
            for (int i = 0; i < int1; ++i) {
                list.add(this.readString());
            }
        }
        return list;
    }
    
    public long readLong() {
        this.checkPosition(8);
        return ((long)this.mBuffer[this.mPosition - 8] & 0xFFL) << 56 | ((long)this.mBuffer[this.mPosition - 7] & 0xFFL) << 48 | ((long)this.mBuffer[this.mPosition - 6] & 0xFFL) << 40 | ((long)this.mBuffer[this.mPosition - 5] & 0xFFL) << 32 | ((long)this.mBuffer[this.mPosition - 4] & 0xFFL) << 24 | ((long)this.mBuffer[this.mPosition - 3] & 0xFFL) << 16 | ((long)this.mBuffer[this.mPosition - 2] & 0xFFL) << 8 | ((long)this.mBuffer[this.mPosition - 1] & 0xFFL);
    }
    
    public short readShort() {
        this.checkPosition(2);
        return (short)((this.mBuffer[this.mPosition - 2] & 0xFF) << 8 | (this.mBuffer[this.mPosition - 1] & 0xFF));
    }
    
    public Storable readStorable(final Class<? extends Storable> clazz) throws InstantiationException, IllegalAccessException, IOException {
        return Storable.read(clazz, this);
    }
    
    public String readString() throws IOException {
        final int int1 = this.readInt();
        String s;
        if (int1 == 0) {
            s = "";
        }
        else {
            this.checkPosition(int1);
            s = new String(this.mBuffer, this.mPosition - int1, int1, "UTF-8");
        }
        return s;
    }
    
    @Deprecated
    public String readStringDis() throws IOException {
        final short short1 = this.readShort();
        String s;
        if (short1 == 0) {
            s = "";
        }
        else {
            this.checkPosition(short1);
            s = new String(this.mBuffer, this.mPosition - short1, short1, "UTF-8");
        }
        return s;
    }
    
    public void seek(final int mPosition) {
        this.mPosition = mPosition;
    }
}
