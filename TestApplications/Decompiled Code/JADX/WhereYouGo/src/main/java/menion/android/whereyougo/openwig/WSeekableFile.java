package menion.android.whereyougo.openwig;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.platform.SeekableFile;

public class WSeekableFile implements SeekableFile {
    private static final String TAG = "WSeekableFile";
    private RandomAccessFile raf;

    public WSeekableFile(File file) {
        try {
            this.raf = new RandomAccessFile(file, "rw");
        } catch (Exception e) {
            Logger.m22e(TAG, "WSeekableFile(" + file.getAbsolutePath() + ")", e);
        }
    }

    private static double readDouble(byte[] buffer, int start, int len) {
        long result = 0;
        for (int i = 0; i < len; i++) {
            result |= ((long) (buffer[start + i] & 255)) << (i * 8);
        }
        return Double.longBitsToDouble(result);
    }

    private static int readInt(byte[] buffer, int start, int len) {
        int result = 0;
        for (int i = 0; i < len; i++) {
            result += (buffer[start + i] & 255) << (i * 8);
        }
        return result;
    }

    private static long readLong(byte[] buffer, int start, int len) {
        long result = 0;
        for (int i = 0; i < len; i++) {
            result |= ((long) (buffer[start + i] & 255)) << (i * 8);
        }
        return result;
    }

    public long position() throws IOException {
        return this.raf.getFilePointer();
    }

    public int read() throws IOException {
        return this.raf.read();
    }

    public double readDouble() throws IOException {
        try {
            byte[] data = new byte[8];
            this.raf.read(data);
            return readDouble(data, 0, 8);
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public void readFully(byte[] buf) throws IOException {
        this.raf.read(buf);
    }

    public int readInt() throws IOException {
        int i = 0;
        try {
            byte[] data = new byte[4];
            this.raf.read(data);
            return readInt(data, 0, 4);
        } catch (Exception e) {
            return i;
        }
    }

    public long readLong() throws IOException {
        byte[] buffer = new byte[8];
        this.raf.read(buffer);
        return readLong(buffer, 0, 8);
    }

    public short readShort() throws IOException {
        byte[] r = new byte[2];
        this.raf.read(r);
        return (short) ((r[1] << 8) | (r[0] & 255));
    }

    public String readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        int b = this.raf.read();
        while (b > 0) {
            sb.append((char) b);
            b = this.raf.read();
        }
        return sb.toString();
    }

    public void seek(long pos) throws IOException {
        this.raf.seek(pos);
    }

    public long skip(long what) throws IOException {
        return (long) this.raf.skipBytes((int) what);
    }
}
