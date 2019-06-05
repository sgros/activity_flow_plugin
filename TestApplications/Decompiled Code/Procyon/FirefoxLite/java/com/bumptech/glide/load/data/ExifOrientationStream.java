// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public final class ExifOrientationStream extends FilterInputStream
{
    private static final byte[] EXIF_SEGMENT;
    private static final int ORIENTATION_POSITION;
    private static final int SEGMENT_LENGTH;
    private final byte orientation;
    private int position;
    
    static {
        EXIF_SEGMENT = new byte[] { -1, -31, 0, 28, 69, 120, 105, 102, 0, 0, 77, 77, 0, 0, 0, 0, 0, 8, 0, 1, 1, 18, 0, 2, 0, 0, 0, 1, 0 };
        SEGMENT_LENGTH = ExifOrientationStream.EXIF_SEGMENT.length;
        ORIENTATION_POSITION = ExifOrientationStream.SEGMENT_LENGTH + 2;
    }
    
    public ExifOrientationStream(final InputStream in, final int i) {
        super(in);
        if (i >= -1 && i <= 8) {
            this.orientation = (byte)i;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot add invalid orientation: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public void mark(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        int n;
        if (this.position >= 2 && this.position <= ExifOrientationStream.ORIENTATION_POSITION) {
            if (this.position == ExifOrientationStream.ORIENTATION_POSITION) {
                n = this.orientation;
            }
            else {
                n = (ExifOrientationStream.EXIF_SEGMENT[this.position - 2] & 0xFF);
            }
        }
        else {
            n = super.read();
        }
        if (n != -1) {
            ++this.position;
        }
        return n;
    }
    
    @Override
    public int read(final byte[] array, int n, int min) throws IOException {
        if (this.position > ExifOrientationStream.ORIENTATION_POSITION) {
            n = super.read(array, n, min);
        }
        else if (this.position == ExifOrientationStream.ORIENTATION_POSITION) {
            array[n] = this.orientation;
            n = 1;
        }
        else if (this.position < 2) {
            n = super.read(array, n, 2 - this.position);
        }
        else {
            min = Math.min(ExifOrientationStream.ORIENTATION_POSITION - this.position, min);
            System.arraycopy(ExifOrientationStream.EXIF_SEGMENT, this.position - 2, array, n, min);
            n = min;
        }
        if (n > 0) {
            this.position += n;
        }
        return n;
    }
    
    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long skip(long skip) throws IOException {
        skip = super.skip(skip);
        if (skip > 0L) {
            this.position += (int)skip;
        }
        return skip;
    }
}
