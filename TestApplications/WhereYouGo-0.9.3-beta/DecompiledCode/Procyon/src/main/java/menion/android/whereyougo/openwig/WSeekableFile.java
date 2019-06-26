// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.openwig;

import java.io.IOException;
import menion.android.whereyougo.utils.Logger;
import java.io.File;
import java.io.RandomAccessFile;
import cz.matejcik.openwig.platform.SeekableFile;

public class WSeekableFile implements SeekableFile
{
    private static final String TAG = "WSeekableFile";
    private RandomAccessFile raf;
    
    public WSeekableFile(final File file) {
        try {
            this.raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception ex) {
            Logger.e("WSeekableFile", "WSeekableFile(" + file.getAbsolutePath() + ")", ex);
        }
    }
    
    private static double readDouble(final byte[] array, final int n, final int n2) {
        long n3 = 0L;
        for (int i = 0; i < n2; ++i) {
            n3 |= (long)(array[n + i] & 0xFF) << i * 8;
        }
        return Double.longBitsToDouble(n3);
    }
    
    private static int readInt(final byte[] array, final int n, final int n2) {
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            n3 += (array[n + i] & 0xFF) << i * 8;
        }
        return n3;
    }
    
    private static long readLong(final byte[] array, final int n, final int n2) {
        long n3 = 0L;
        for (int i = 0; i < n2; ++i) {
            n3 |= (long)(array[n + i] & 0xFF) << i * 8;
        }
        return n3;
    }
    
    @Override
    public long position() throws IOException {
        return this.raf.getFilePointer();
    }
    
    @Override
    public int read() throws IOException {
        return this.raf.read();
    }
    
    @Override
    public double readDouble() throws IOException {
        try {
            final byte[] b = new byte[8];
            this.raf.read(b);
            return readDouble(b, 0, 8);
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    @Override
    public void readFully(final byte[] b) throws IOException {
        this.raf.read(b);
    }
    
    @Override
    public int readInt() throws IOException {
        final int n = 0;
        try {
            final byte[] b = new byte[4];
            this.raf.read(b);
            return readInt(b, 0, 4);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    @Override
    public long readLong() throws IOException {
        final byte[] b = new byte[8];
        this.raf.read(b);
        return readLong(b, 0, 8);
    }
    
    @Override
    public short readShort() throws IOException {
        final byte[] b = new byte[2];
        this.raf.read(b);
        return (short)(b[1] << 8 | (b[0] & 0xFF));
    }
    
    @Override
    public String readString() throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i = this.raf.read(); i > 0; i = this.raf.read()) {
            sb.append((char)i);
        }
        return sb.toString();
    }
    
    @Override
    public void seek(final long pos) throws IOException {
        this.raf.seek(pos);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.raf.skipBytes((int)n);
    }
}
