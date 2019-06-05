package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
    private final long contentLength;
    private int readSoFar;

    public static InputStream obtain(InputStream inputStream, long j) {
        return new ContentLengthInputStream(inputStream, j);
    }

    ContentLengthInputStream(InputStream inputStream, long j) {
        super(inputStream);
        this.contentLength = j;
    }

    public synchronized int available() throws IOException {
        return (int) Math.max(this.contentLength - ((long) this.readSoFar), (long) this.in.available());
    }

    public synchronized int read() throws IOException {
        int read;
        read = super.read();
        checkReadSoFarOrThrow(read >= 0 ? 1 : -1);
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        return checkReadSoFarOrThrow(super.read(bArr, i, i2));
    }

    private int checkReadSoFarOrThrow(int i) throws IOException {
        if (i >= 0) {
            this.readSoFar += i;
        } else if (this.contentLength - ((long) this.readSoFar) > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to read all expected data, expected: ");
            stringBuilder.append(this.contentLength);
            stringBuilder.append(", but read: ");
            stringBuilder.append(this.readSoFar);
            throw new IOException(stringBuilder.toString());
        }
        return i;
    }
}
