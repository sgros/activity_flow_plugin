package locus.api.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.objects.Storable;

public class DataReaderBigEndian {
    private static final String TAG = DataReaderBigEndian.class.getSimpleName();
    private byte[] mBuffer;
    private int mPosition;

    public DataReaderBigEndian(byte[] data) throws IOException {
        if (data == null) {
            throw new IOException("Invalid parameter");
        }
        this.mPosition = 0;
        this.mBuffer = data;
    }

    public long length() {
        return (long) this.mBuffer.length;
    }

    public int available() {
        return this.mBuffer.length - this.mPosition;
    }

    public void seek(int pos) {
        this.mPosition = pos;
    }

    public byte[] readBytes(int count) {
        checkPosition(count);
        byte[] newData = new byte[count];
        System.arraycopy(this.mBuffer, this.mPosition - count, newData, 0, count);
        return newData;
    }

    public void readBytes(byte[] data) {
        checkPosition(data.length);
        System.arraycopy(this.mBuffer, this.mPosition - data.length, data, 0, data.length);
    }

    public boolean readBoolean() {
        checkPosition(1);
        if (this.mBuffer[this.mPosition - 1] != (byte) 0) {
            return true;
        }
        return false;
    }

    public short readShort() {
        checkPosition(2);
        return (short) (((this.mBuffer[this.mPosition - 2] & 255) << 8) | (this.mBuffer[this.mPosition - 1] & 255));
    }

    public int readInt() {
        checkPosition(4);
        return (((this.mBuffer[this.mPosition - 4] << 24) | ((this.mBuffer[this.mPosition - 3] & 255) << 16)) | ((this.mBuffer[this.mPosition - 2] & 255) << 8)) | (this.mBuffer[this.mPosition - 1] & 255);
    }

    public long readLong() {
        checkPosition(8);
        return ((((((((((long) this.mBuffer[this.mPosition - 8]) & 255) << 56) | ((((long) this.mBuffer[this.mPosition - 7]) & 255) << 48)) | ((((long) this.mBuffer[this.mPosition - 6]) & 255) << 40)) | ((((long) this.mBuffer[this.mPosition - 5]) & 255) << 32)) | ((((long) this.mBuffer[this.mPosition - 4]) & 255) << 24)) | ((((long) this.mBuffer[this.mPosition - 3]) & 255) << 16)) | ((((long) this.mBuffer[this.mPosition - 2]) & 255) << 8)) | (((long) this.mBuffer[this.mPosition - 1]) & 255);
    }

    public final float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readString() throws IOException {
        int textLength = readInt();
        if (textLength == 0) {
            return "";
        }
        checkPosition(textLength);
        return new String(this.mBuffer, this.mPosition - textLength, textLength, "UTF-8");
    }

    @Deprecated
    public String readStringDis() throws IOException {
        int textLength = readShort();
        if (textLength == 0) {
            return "";
        }
        checkPosition(textLength);
        return new String(this.mBuffer, this.mPosition - textLength, textLength, "UTF-8");
    }

    public Storable readStorable(Class<? extends Storable> claz) throws InstantiationException, IllegalAccessException, IOException {
        return Storable.read(claz, this);
    }

    public List<String> readListString() throws IOException {
        List<String> objs = new ArrayList();
        int count = readInt();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                objs.add(readString());
            }
        }
        return objs;
    }

    public List<? extends Storable> readListStorable(Class<? extends Storable> claz) throws IOException {
        List<Storable> objs = new ArrayList();
        int count = readInt();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                try {
                    Storable item = (Storable) claz.newInstance();
                    item.read(this);
                    objs.add(item);
                } catch (InstantiationException e) {
                    Logger.logE(TAG, "readList(" + claz + ")", e);
                } catch (IllegalAccessException e2) {
                    Logger.logE(TAG, "readList(" + claz + ")", e2);
                }
            }
        }
        return objs;
    }

    private void checkPosition(int increment) {
        this.mPosition += increment;
        if (this.mPosition > this.mBuffer.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid position for data load. Current:" + this.mPosition + ", " + "length:" + this.mBuffer.length + ", " + "increment:" + increment);
        }
    }
}
