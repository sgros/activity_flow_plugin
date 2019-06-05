package p005cz.matejcik.openwig.platform;

import java.io.IOException;

/* renamed from: cz.matejcik.openwig.platform.SeekableFile */
public interface SeekableFile {
    long position() throws IOException;

    int read() throws IOException;

    double readDouble() throws IOException;

    void readFully(byte[] bArr) throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    short readShort() throws IOException;

    String readString() throws IOException;

    void seek(long j) throws IOException;

    long skip(long j) throws IOException;
}
