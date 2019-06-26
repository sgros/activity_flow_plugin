// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable
{
    private static final byte[] DIGITS;
    static final int REPLACEMENT_CHARACTER = 65533;
    Segment head;
    long size;
    
    static {
        DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
    }
    
    private ByteString digest(final String algorithm) {
        try {
            final MessageDigest instance = MessageDigest.getInstance(algorithm);
            if (this.head != null) {
                instance.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                for (Segment segment = this.head.next; segment != this.head; segment = segment.next) {
                    instance.update(segment.data, segment.pos, segment.limit - segment.pos);
                }
            }
            return ByteString.of(instance.digest());
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError();
        }
    }
    
    private ByteString hmac(final String s, final ByteString byteString) {
        try {
            final Mac instance = Mac.getInstance(s);
            instance.init(new SecretKeySpec(byteString.toByteArray(), s));
            if (this.head != null) {
                instance.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                for (Segment segment = this.head.next; segment != this.head; segment = segment.next) {
                    instance.update(segment.data, segment.pos, segment.limit - segment.pos);
                }
            }
            return ByteString.of(instance.doFinal());
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError();
        }
        catch (InvalidKeyException cause) {
            throw new IllegalArgumentException(cause);
        }
    }
    
    private boolean rangeEquals(Segment segment, int n, final ByteString byteString, int i, final int n2) {
        int limit = segment.limit;
        byte[] array = segment.data;
        while (i < n2) {
            int limit2 = limit;
            Segment next = segment;
            int pos;
            if ((pos = n) == limit) {
                next = segment.next;
                array = next.data;
                pos = next.pos;
                limit2 = next.limit;
            }
            if (array[pos] != byteString.getByte(i)) {
                return false;
            }
            n = pos + 1;
            ++i;
            limit = limit2;
            segment = next;
        }
        return true;
    }
    
    private void readFrom(final InputStream inputStream, long a, final boolean b) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        while (a > 0L || b) {
            final Segment writableSegment = this.writableSegment(1);
            final int read = inputStream.read(writableSegment.data, writableSegment.limit, (int)Math.min(a, 8192 - writableSegment.limit));
            if (read == -1) {
                if (b) {
                    break;
                }
                throw new EOFException();
            }
            else {
                writableSegment.limit += read;
                this.size += read;
                a -= read;
            }
        }
    }
    
    @Override
    public Buffer buffer() {
        return this;
    }
    
    public void clear() {
        try {
            this.skip(this.size);
        }
        catch (EOFException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    public Buffer clone() {
        final Buffer buffer = new Buffer();
        if (this.size != 0L) {
            buffer.head = new Segment(this.head);
            final Segment head = buffer.head;
            final Segment head2 = buffer.head;
            final Segment head3 = buffer.head;
            head2.prev = head3;
            head.next = head3;
            for (Segment segment = this.head.next; segment != this.head; segment = segment.next) {
                buffer.head.prev.push(new Segment(segment));
            }
            buffer.size = this.size;
        }
        return buffer;
    }
    
    @Override
    public void close() {
    }
    
    public long completeSegmentByteCount() {
        long n = 0L;
        final long size = this.size;
        if (size != 0L) {
            final Segment prev = this.head.prev;
            n = size;
            if (prev.limit < 8192) {
                n = size;
                if (prev.owner) {
                    n = size - (prev.limit - prev.pos);
                }
            }
        }
        return n;
    }
    
    public Buffer copyTo(final OutputStream outputStream) throws IOException {
        return this.copyTo(outputStream, 0L, this.size);
    }
    
    public Buffer copyTo(final OutputStream outputStream, long n, final long n2) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, n, n2);
        if (n2 != 0L) {
            Segment segment = this.head;
            Segment next;
            long n3;
            long b;
            while (true) {
                next = segment;
                n3 = n;
                b = n2;
                if (n < segment.limit - segment.pos) {
                    break;
                }
                n -= segment.limit - segment.pos;
                segment = segment.next;
            }
            while (b > 0L) {
                final int off = (int)(next.pos + n3);
                final int len = (int)Math.min(next.limit - off, b);
                outputStream.write(next.data, off, len);
                b -= len;
                n3 = 0L;
                next = next.next;
            }
        }
        return this;
    }
    
    public Buffer copyTo(final Buffer buffer, long n, final long n2) {
        if (buffer == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, n, n2);
        if (n2 != 0L) {
            buffer.size += n2;
            Segment segment = this.head;
            Segment next;
            long n3;
            long n4;
            while (true) {
                next = segment;
                n3 = n;
                n4 = n2;
                if (n < segment.limit - segment.pos) {
                    break;
                }
                n -= segment.limit - segment.pos;
                segment = segment.next;
            }
            while (n4 > 0L) {
                final Segment head = new Segment(next);
                head.pos += (int)n3;
                head.limit = Math.min(head.pos + (int)n4, head.limit);
                if (buffer.head == null) {
                    head.prev = head;
                    head.next = head;
                    buffer.head = head;
                }
                else {
                    buffer.head.prev.push(head);
                }
                n4 -= head.limit - head.pos;
                n3 = 0L;
                next = next.next;
            }
        }
        return this;
    }
    
    @Override
    public BufferedSink emit() {
        return this;
    }
    
    @Override
    public Buffer emitCompleteSegments() {
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b;
        if (this == o) {
            b = true;
        }
        else if (!(o instanceof Buffer)) {
            b = false;
        }
        else {
            final Buffer buffer = (Buffer)o;
            if (this.size != buffer.size) {
                b = false;
            }
            else if (this.size == 0L) {
                b = true;
            }
            else {
                Segment segment = this.head;
                Segment segment2 = buffer.head;
                int pos = segment.pos;
                int pos2 = segment2.pos;
                long n2;
                int pos3;
                int pos4;
                for (long n = 0L; n < this.size; n += n2, pos = pos3, pos2 = pos4) {
                    n2 = Math.min(segment.limit - pos, segment2.limit - pos2);
                    final int n3 = 0;
                    final int n4 = pos;
                    int n5 = pos2;
                    pos3 = n4;
                    for (int n6 = n3; n6 < n2; ++n6, ++n5, ++pos3) {
                        if (segment.data[pos3] != segment2.data[n5]) {
                            b = false;
                            return b;
                        }
                    }
                    if (pos3 == segment.limit) {
                        segment = segment.next;
                        pos3 = segment.pos;
                    }
                    if (n5 == segment2.limit) {
                        segment2 = segment2.next;
                        pos4 = segment2.pos;
                    }
                    else {
                        pos4 = n5;
                    }
                }
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public boolean exhausted() {
        return this.size == 0L;
    }
    
    @Override
    public void flush() {
    }
    
    public byte getByte(long n) {
        Util.checkOffsetAndCount(this.size, n, 1L);
        Segment segment = this.head;
        while (true) {
            final int n2 = segment.limit - segment.pos;
            if (n < n2) {
                break;
            }
            n -= n2;
            segment = segment.next;
        }
        return segment.data[segment.pos + (int)n];
    }
    
    @Override
    public int hashCode() {
        Segment head = this.head;
        int n;
        if (head == null) {
            n = 0;
        }
        else {
            int n2 = 1;
            Segment next;
            do {
                final int pos = head.pos;
                final int limit = head.limit;
                n = n2;
                for (int i = pos; i < limit; ++i) {
                    n = n * 31 + head.data[i];
                }
                next = head.next;
                n2 = n;
            } while ((head = next) != this.head);
        }
        return n;
    }
    
    public ByteString hmacSha1(final ByteString byteString) {
        return this.hmac("HmacSHA1", byteString);
    }
    
    public ByteString hmacSha256(final ByteString byteString) {
        return this.hmac("HmacSHA256", byteString);
    }
    
    @Override
    public long indexOf(final byte b) {
        return this.indexOf(b, 0L);
    }
    
    @Override
    public long indexOf(final byte b, long n) {
        final long n2 = -1L;
        if (n < 0L) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            n = n2;
        }
        else {
            long n3;
            Segment next;
            long n4;
            if (this.size - n < n) {
                long size = this.size;
                while (true) {
                    n3 = size;
                    next = segment;
                    n4 = n;
                    if (size <= n) {
                        break;
                    }
                    segment = segment.prev;
                    size -= segment.limit - segment.pos;
                }
            }
            else {
                n3 = 0L;
                while (true) {
                    final long n5 = n3 + (segment.limit - segment.pos);
                    next = segment;
                    n4 = n;
                    if (n5 >= n) {
                        break;
                    }
                    segment = segment.next;
                    n3 = n5;
                }
            }
            int i = 0;
        Block_8:
            while (true) {
                n = n2;
                if (n3 >= this.size) {
                    return n;
                }
                final byte[] data = next.data;
                for (i = (int)(next.pos + n4 - n3); i < next.limit; ++i) {
                    if (data[i] == b) {
                        break Block_8;
                    }
                }
                n3 = (n4 = n3 + (next.limit - next.pos));
                next = next.next;
            }
            n = i - next.pos + n3;
        }
        return n;
    }
    
    @Override
    public long indexOf(final ByteString byteString) throws IOException {
        return this.indexOf(byteString, 0L);
    }
    
    @Override
    public long indexOf(final ByteString byteString, long n) throws IOException {
        if (byteString.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        }
        if (n < 0L) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            n = -1L;
        }
        else {
            Segment next;
            long n2;
            if (this.size - n < n) {
                long size = this.size;
                while (true) {
                    next = segment;
                    n2 = size;
                    if (size <= n) {
                        break;
                    }
                    segment = segment.prev;
                    size -= segment.limit - segment.pos;
                }
            }
            else {
                n2 = 0L;
                while (true) {
                    final long n3 = n2 + (segment.limit - segment.pos);
                    next = segment;
                    if (n3 >= n) {
                        break;
                    }
                    segment = segment.next;
                    n2 = n3;
                }
            }
            final byte byte1 = byteString.getByte(0);
            final int size2 = byteString.size();
            for (long n4 = this.size - size2 + 1L; n2 < n4; n2 = (n = n2 + (next.limit - next.pos)), next = next.next) {
                final byte[] data = next.data;
                for (int n5 = (int)Math.min(next.limit, next.pos + n4 - n2), i = (int)(next.pos + n - n2); i < n5; ++i) {
                    if (data[i] == byte1 && this.rangeEquals(next, i + 1, byteString, 1, size2)) {
                        n = i - next.pos + n2;
                        return n;
                    }
                }
            }
            n = -1L;
        }
        return n;
    }
    
    @Override
    public long indexOfElement(final ByteString byteString) {
        return this.indexOfElement(byteString, 0L);
    }
    
    @Override
    public long indexOfElement(final ByteString byteString, long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            n = -1L;
        }
        else {
            long n2;
            Segment segment2;
            if (this.size - n < n) {
                long size = this.size;
                while (true) {
                    n2 = size;
                    segment2 = segment;
                    if (size <= n) {
                        break;
                    }
                    segment = segment.prev;
                    size -= segment.limit - segment.pos;
                }
            }
            else {
                n2 = 0L;
                while (true) {
                    final long n3 = n2 + (segment.limit - segment.pos);
                    segment2 = segment;
                    if (n3 >= n) {
                        break;
                    }
                    segment = segment.next;
                    n2 = n3;
                }
            }
            if (byteString.size() == 2) {
                final byte byte1 = byteString.getByte(0);
                final byte byte2 = byteString.getByte(1);
                while (n2 < this.size) {
                    final byte[] data = segment2.data;
                    for (int i = (int)(segment2.pos + n - n2); i < segment2.limit; ++i) {
                        final byte b = data[i];
                        if (b == byte1 || b == byte2) {
                            n = i - segment2.pos + n2;
                            return n;
                        }
                    }
                    n2 = (n = n2 + (segment2.limit - segment2.pos));
                    segment2 = segment2.next;
                }
            }
            else {
                final byte[] internalArray = byteString.internalArray();
                while (n2 < this.size) {
                    final byte[] data2 = segment2.data;
                    for (int j = (int)(segment2.pos + n - n2); j < segment2.limit; ++j) {
                        final byte b2 = data2[j];
                        for (int length = internalArray.length, k = 0; k < length; ++k) {
                            if (b2 == internalArray[k]) {
                                n = j - segment2.pos + n2;
                                return n;
                            }
                        }
                    }
                    n2 = (n = n2 + (segment2.limit - segment2.pos));
                    segment2 = segment2.next;
                }
            }
            n = -1L;
        }
        return n;
    }
    
    @Override
    public InputStream inputStream() {
        return new InputStream() {
            @Override
            public int available() {
                return (int)Math.min(Buffer.this.size, 2147483647L);
            }
            
            @Override
            public void close() {
            }
            
            @Override
            public int read() {
                int n;
                if (Buffer.this.size > 0L) {
                    n = (Buffer.this.readByte() & 0xFF);
                }
                else {
                    n = -1;
                }
                return n;
            }
            
            @Override
            public int read(final byte[] array, final int n, final int n2) {
                return Buffer.this.read(array, n, n2);
            }
            
            @Override
            public String toString() {
                return Buffer.this + ".inputStream()";
            }
        };
    }
    
    public ByteString md5() {
        return this.digest("MD5");
    }
    
    @Override
    public OutputStream outputStream() {
        return new OutputStream() {
            @Override
            public void close() {
            }
            
            @Override
            public void flush() {
            }
            
            @Override
            public String toString() {
                return Buffer.this + ".outputStream()";
            }
            
            @Override
            public void write(final int n) {
                Buffer.this.writeByte((int)(byte)n);
            }
            
            @Override
            public void write(final byte[] array, final int n, final int n2) {
                Buffer.this.write(array, n, n2);
            }
        };
    }
    
    @Override
    public boolean rangeEquals(final long n, final ByteString byteString) {
        return this.rangeEquals(n, byteString, 0, byteString.size());
    }
    
    @Override
    public boolean rangeEquals(final long n, final ByteString byteString, final int n2, final int n3) {
        boolean b2;
        final boolean b = b2 = false;
        if (n >= 0L) {
            b2 = b;
            if (n2 >= 0) {
                b2 = b;
                if (n3 >= 0) {
                    b2 = b;
                    if (this.size - n >= n3) {
                        if (byteString.size() - n2 < n3) {
                            b2 = b;
                        }
                        else {
                            for (int i = 0; i < n3; ++i) {
                                b2 = b;
                                if (this.getByte(i + n) != byteString.getByte(n2 + i)) {
                                    return b2;
                                }
                            }
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public int read(final byte[] array) {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, int n, int min) {
        Util.checkOffsetAndCount(array.length, n, min);
        final Segment head = this.head;
        if (head == null) {
            n = -1;
        }
        else {
            min = Math.min(min, head.limit - head.pos);
            System.arraycopy(head.data, head.pos, array, n, min);
            head.pos += min;
            this.size -= min;
            n = min;
            if (head.pos == head.limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
                n = min;
            }
        }
        return n;
    }
    
    @Override
    public long read(final Buffer buffer, long lng) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (lng < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + lng);
        }
        if (this.size == 0L) {
            lng = -1L;
        }
        else {
            long size = lng;
            if (lng > this.size) {
                size = this.size;
            }
            buffer.write(this, size);
            lng = size;
        }
        return lng;
    }
    
    @Override
    public long readAll(final Sink sink) throws IOException {
        final long size = this.size;
        if (size > 0L) {
            sink.write(this, size);
        }
        return size;
    }
    
    @Override
    public byte readByte() {
        if (this.size == 0L) {
            throw new IllegalStateException("size == 0");
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        final byte[] data = head.data;
        final int pos2 = pos + 1;
        final byte b = data[pos];
        --this.size;
        if (pos2 == limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
        }
        else {
            head.pos = pos2;
        }
        return b;
    }
    
    @Override
    public byte[] readByteArray() {
        try {
            return this.readByteArray(this.size);
        }
        catch (EOFException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    @Override
    public byte[] readByteArray(final long lng) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, lng);
        if (lng > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + lng);
        }
        final byte[] array = new byte[(int)lng];
        this.readFully(array);
        return array;
    }
    
    @Override
    public ByteString readByteString() {
        return new ByteString(this.readByteArray());
    }
    
    @Override
    public ByteString readByteString(final long n) throws EOFException {
        return new ByteString(this.readByteArray(n));
    }
    
    @Override
    public long readDecimalLong() {
        if (this.size == 0L) {
            throw new IllegalStateException("size == 0");
        }
        long n = 0L;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        long n5 = -7L;
        long n6;
        int n7;
        int n9;
        do {
            final Segment head = this.head;
            final byte[] data = head.data;
            final int pos = head.pos;
            final int limit = head.limit;
            n6 = n;
            n7 = n2;
            long n8 = n5;
            int pos2 = pos;
            n9 = n3;
            int n10;
            while (true) {
                n10 = n4;
                if (pos2 >= limit) {
                    break;
                }
                final byte i = data[pos2];
                if (i >= 48 && i <= 57) {
                    final int n11 = 48 - i;
                    if (n6 < -922337203685477580L || (n6 == -922337203685477580L && n11 < n8)) {
                        final Buffer writeByte = new Buffer().writeDecimalLong(n6).writeByte((int)i);
                        if (n9 == 0) {
                            writeByte.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + writeByte.readUtf8());
                    }
                    n6 = n6 * 10L + n11;
                }
                else if (i == 45 && n7 == 0) {
                    n9 = 1;
                    --n8;
                }
                else {
                    if (n7 == 0) {
                        throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + Integer.toHexString(i));
                    }
                    n10 = 1;
                    break;
                }
                ++pos2;
                ++n7;
            }
            if (pos2 == limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
            }
            else {
                head.pos = pos2;
            }
            if (n10 != 0) {
                break;
            }
            n4 = n10;
            n3 = n9;
            n5 = n8;
            n2 = n7;
            n = n6;
        } while (this.head != null);
        this.size -= n7;
        if (n9 == 0) {
            n6 = -n6;
        }
        return n6;
    }
    
    public Buffer readFrom(final InputStream inputStream) throws IOException {
        this.readFrom(inputStream, Long.MAX_VALUE, true);
        return this;
    }
    
    public Buffer readFrom(final InputStream inputStream, final long lng) throws IOException {
        if (lng < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + lng);
        }
        this.readFrom(inputStream, lng, false);
        return this;
    }
    
    @Override
    public void readFully(final Buffer buffer, final long n) throws EOFException {
        if (this.size < n) {
            buffer.write(this, this.size);
            throw new EOFException();
        }
        buffer.write(this, n);
    }
    
    @Override
    public void readFully(final byte[] array) throws EOFException {
        int read;
        for (int i = 0; i < array.length; i += read) {
            read = this.read(array, i, array.length - i);
            if (read == -1) {
                throw new EOFException();
            }
        }
    }
    
    @Override
    public long readHexadecimalUnsignedLong() {
        if (this.size == 0L) {
            throw new IllegalStateException("size == 0");
        }
        long n = 0L;
        int n2 = 0;
        int n3 = 0;
        long n4;
        int n5;
        do {
            final Segment head = this.head;
            final byte[] data = head.data;
            int pos = head.pos;
            final int limit = head.limit;
            n4 = n;
            n5 = n2;
            int n6;
            while (true) {
                n6 = n3;
                if (pos >= limit) {
                    break;
                }
                final byte i = data[pos];
                int n7;
                if (i >= 48 && i <= 57) {
                    n7 = i - 48;
                }
                else if (i >= 97 && i <= 102) {
                    n7 = i - 97 + 10;
                }
                else if (i >= 65 && i <= 70) {
                    n7 = i - 65 + 10;
                }
                else {
                    if (n5 == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + Integer.toHexString(i));
                    }
                    n6 = 1;
                    break;
                }
                if ((0xF000000000000000L & n4) != 0x0L) {
                    throw new NumberFormatException("Number too large: " + new Buffer().writeHexadecimalUnsignedLong(n4).writeByte((int)i).readUtf8());
                }
                n4 = (n4 << 4 | (long)n7);
                ++pos;
                ++n5;
            }
            if (pos == limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
            }
            else {
                head.pos = pos;
            }
            if (n6 != 0) {
                break;
            }
            n3 = n6;
            n2 = n5;
            n = n4;
        } while (this.head != null);
        this.size -= n5;
        return n4;
    }
    
    @Override
    public int readInt() {
        if (this.size < 4L) {
            throw new IllegalStateException("size < 4: " + this.size);
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        int n;
        if (limit - pos < 4) {
            n = ((this.readByte() & 0xFF) << 24 | (this.readByte() & 0xFF) << 16 | (this.readByte() & 0xFF) << 8 | (this.readByte() & 0xFF));
        }
        else {
            final byte[] data = head.data;
            final int n2 = pos + 1;
            final byte b = data[pos];
            final int n3 = n2 + 1;
            final byte b2 = data[n2];
            final int n4 = n3 + 1;
            final byte b3 = data[n3];
            final int pos2 = n4 + 1;
            n = ((b & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (data[n4] & 0xFF));
            this.size -= 4L;
            if (pos2 == limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
            }
            else {
                head.pos = pos2;
            }
        }
        return n;
    }
    
    @Override
    public int readIntLe() {
        return Util.reverseBytesInt(this.readInt());
    }
    
    @Override
    public long readLong() {
        if (this.size < 8L) {
            throw new IllegalStateException("size < 8: " + this.size);
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        long n;
        if (limit - pos < 8) {
            n = (((long)this.readInt() & 0xFFFFFFFFL) << 32 | ((long)this.readInt() & 0xFFFFFFFFL));
        }
        else {
            final byte[] data = head.data;
            final int n2 = pos + 1;
            final long n3 = data[pos];
            final int n4 = n2 + 1;
            final long n5 = data[n2];
            final int n6 = n4 + 1;
            final long n7 = data[n4];
            final int n8 = n6 + 1;
            final long n9 = data[n6];
            final int n10 = n8 + 1;
            final long n11 = data[n8];
            final int n12 = n10 + 1;
            final long n13 = data[n10];
            final int n14 = n12 + 1;
            final long n15 = data[n12];
            final int pos2 = n14 + 1;
            n = ((n3 & 0xFFL) << 56 | (n5 & 0xFFL) << 48 | (n7 & 0xFFL) << 40 | (n9 & 0xFFL) << 32 | (n11 & 0xFFL) << 24 | (n13 & 0xFFL) << 16 | (n15 & 0xFFL) << 8 | ((long)data[n14] & 0xFFL));
            this.size -= 8L;
            if (pos2 == limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
            }
            else {
                head.pos = pos2;
            }
        }
        return n;
    }
    
    @Override
    public long readLongLe() {
        return Util.reverseBytesLong(this.readLong());
    }
    
    @Override
    public short readShort() {
        if (this.size < 2L) {
            throw new IllegalStateException("size < 2: " + this.size);
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        short n;
        if (limit - pos < 2) {
            n = (short)((this.readByte() & 0xFF) << 8 | (this.readByte() & 0xFF));
        }
        else {
            final byte[] data = head.data;
            final int n2 = pos + 1;
            final byte b = data[pos];
            final int pos2 = n2 + 1;
            final byte b2 = data[n2];
            this.size -= 2L;
            if (pos2 == limit) {
                this.head = head.pop();
                SegmentPool.recycle(head);
            }
            else {
                head.pos = pos2;
            }
            n = (short)((b & 0xFF) << 8 | (b2 & 0xFF));
        }
        return n;
    }
    
    @Override
    public short readShortLe() {
        return Util.reverseBytesShort(this.readShort());
    }
    
    @Override
    public String readString(final long lng, final Charset charset) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, lng);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        if (lng > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + lng);
        }
        String s;
        if (lng == 0L) {
            s = "";
        }
        else {
            final Segment head = this.head;
            if (head.pos + lng > head.limit) {
                s = new String(this.readByteArray(lng), charset);
            }
            else {
                final String s2 = new String(head.data, head.pos, (int)lng, charset);
                head.pos += (int)lng;
                this.size -= lng;
                s = s2;
                if (head.pos == head.limit) {
                    this.head = head.pop();
                    SegmentPool.recycle(head);
                    s = s2;
                }
            }
        }
        return s;
    }
    
    @Override
    public String readString(final Charset charset) {
        try {
            return this.readString(this.size, charset);
        }
        catch (EOFException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    @Override
    public String readUtf8() {
        try {
            return this.readString(this.size, Util.UTF_8);
        }
        catch (EOFException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    @Override
    public String readUtf8(final long n) throws EOFException {
        return this.readString(n, Util.UTF_8);
    }
    
    @Override
    public int readUtf8CodePoint() throws EOFException {
        if (this.size == 0L) {
            throw new EOFException();
        }
        final byte byte1 = this.getByte(0L);
        int n;
        int i;
        int n2;
        if ((byte1 & 0x80) == 0x0) {
            n = (byte1 & 0x7F);
            i = 1;
            n2 = 0;
        }
        else if ((byte1 & 0xE0) == 0xC0) {
            n = (byte1 & 0x1F);
            i = 2;
            n2 = 128;
        }
        else if ((byte1 & 0xF0) == 0xE0) {
            n = (byte1 & 0xF);
            i = 3;
            n2 = 2048;
        }
        else {
            if ((byte1 & 0xF8) != 0xF0) {
                this.skip(1L);
                return 65533;
            }
            n = (byte1 & 0x7);
            i = 4;
            n2 = 65536;
        }
        if (this.size < i) {
            throw new EOFException("size < " + i + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(byte1) + ")");
        }
        int j = 1;
        int n4 = n;
        while (j < i) {
            final byte byte2 = this.getByte(j);
            if ((byte2 & 0xC0) != 0x80) {
                this.skip(j);
                return 65533;
            }
            n4 = (n4 << 6 | (byte2 & 0x3F));
            ++j;
        }
        this.skip(i);
        int n3;
        if (n4 > 1114111) {
            n3 = 65533;
        }
        else if (n4 >= 55296 && n4 <= 57343) {
            n3 = 65533;
        }
        else if ((n3 = n4) < n2) {
            n3 = 65533;
        }
        return n3;
    }
    
    @Override
    public String readUtf8Line() throws EOFException {
        final long index = this.indexOf((byte)10);
        String s;
        if (index == -1L) {
            if (this.size != 0L) {
                s = this.readUtf8(this.size);
            }
            else {
                s = null;
            }
        }
        else {
            s = this.readUtf8Line(index);
        }
        return s;
    }
    
    String readUtf8Line(final long n) throws EOFException {
        String s;
        if (n > 0L && this.getByte(n - 1L) == 13) {
            s = this.readUtf8(n - 1L);
            this.skip(2L);
        }
        else {
            s = this.readUtf8(n);
            this.skip(1L);
        }
        return s;
    }
    
    @Override
    public String readUtf8LineStrict() throws EOFException {
        final long index = this.indexOf((byte)10);
        if (index == -1L) {
            final Buffer buffer = new Buffer();
            this.copyTo(buffer, 0L, Math.min(32L, this.size));
            throw new EOFException("\\n not found: size=" + this.size() + " content=" + buffer.readByteString().hex() + "\u2026");
        }
        return this.readUtf8Line(index);
    }
    
    @Override
    public boolean request(final long n) {
        return this.size >= n;
    }
    
    @Override
    public void require(final long n) throws EOFException {
        if (this.size < n) {
            throw new EOFException();
        }
    }
    
    List<Integer> segmentSizes() {
        List<Integer> emptyList;
        if (this.head == null) {
            emptyList = Collections.emptyList();
        }
        else {
            final ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(this.head.limit - this.head.pos);
            Segment segment = this.head.next;
            while (true) {
                emptyList = list;
                if (segment == this.head) {
                    break;
                }
                list.add(segment.limit - segment.pos);
                segment = segment.next;
            }
        }
        return emptyList;
    }
    
    @Override
    public int select(final Options options) {
        final Segment head = this.head;
        int i;
        if (head == null) {
            i = options.indexOf(ByteString.EMPTY);
        }
        else {
            ByteString[] byteStrings;
            ByteString byteString;
            for (byteStrings = options.byteStrings, i = 0; i < byteStrings.length; ++i) {
                byteString = byteStrings[i];
                if (this.size >= byteString.size() && this.rangeEquals(head, head.pos, byteString, 0, byteString.size())) {
                    try {
                        this.skip(byteString.size());
                        return i;
                    }
                    catch (EOFException detailMessage) {
                        throw new AssertionError((Object)detailMessage);
                    }
                }
            }
            i = -1;
        }
        return i;
    }
    
    int selectPrefix(final Options options) {
        final Segment head = this.head;
        final ByteString[] byteStrings = options.byteStrings;
        int i = 0;
        while (i < byteStrings.length) {
            final ByteString byteString = byteStrings[i];
            final int n = (int)Math.min(this.size, byteString.size());
            int n2 = i;
            if (n != 0) {
                if (!this.rangeEquals(head, head.pos, byteString, 0, n)) {
                    ++i;
                    continue;
                }
                n2 = i;
            }
            return n2;
        }
        return -1;
    }
    
    public ByteString sha1() {
        return this.digest("SHA-1");
    }
    
    public ByteString sha256() {
        return this.digest("SHA-256");
    }
    
    public long size() {
        return this.size;
    }
    
    @Override
    public void skip(long a) throws EOFException {
        while (a > 0L) {
            if (this.head == null) {
                throw new EOFException();
            }
            final int n = (int)Math.min(a, this.head.limit - this.head.pos);
            this.size -= n;
            final long n2 = a - n;
            final Segment head = this.head;
            head.pos += n;
            a = n2;
            if (this.head.pos != this.head.limit) {
                continue;
            }
            final Segment head2 = this.head;
            this.head = head2.pop();
            SegmentPool.recycle(head2);
            a = n2;
        }
    }
    
    public ByteString snapshot() {
        if (this.size > 2147483647L) {
            throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
        }
        return this.snapshot((int)this.size);
    }
    
    public ByteString snapshot(final int n) {
        ByteString empty;
        if (n == 0) {
            empty = ByteString.EMPTY;
        }
        else {
            empty = new SegmentedByteString(this, n);
        }
        return empty;
    }
    
    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }
    
    @Override
    public String toString() {
        return this.snapshot().toString();
    }
    
    Segment writableSegment(final int n) {
        if (n < 1 || n > 8192) {
            throw new IllegalArgumentException();
        }
        Segment segment;
        if (this.head == null) {
            this.head = SegmentPool.take();
            final Segment head = this.head;
            final Segment head2 = this.head;
            segment = this.head;
            head2.prev = segment;
            head.next = segment;
        }
        else {
            final Segment prev = this.head.prev;
            if (prev.limit + n <= 8192) {
                segment = prev;
                if (prev.owner) {
                    return segment;
                }
            }
            segment = prev.push(SegmentPool.take());
        }
        return segment;
    }
    
    @Override
    public Buffer write(final ByteString byteString) {
        if (byteString == null) {
            throw new IllegalArgumentException("byteString == null");
        }
        byteString.write(this);
        return this;
    }
    
    @Override
    public Buffer write(final byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("source == null");
        }
        return this.write(array, 0, array.length);
    }
    
    @Override
    public Buffer write(final byte[] array, int i, final int n) {
        if (array == null) {
            throw new IllegalArgumentException("source == null");
        }
        Util.checkOffsetAndCount(array.length, i, n);
        Segment writableSegment;
        int min;
        for (int n2 = i + n; i < n2; i += min, writableSegment.limit += min) {
            writableSegment = this.writableSegment(1);
            min = Math.min(n2 - i, 8192 - writableSegment.limit);
            System.arraycopy(array, i, writableSegment.data, writableSegment.limit, min);
        }
        this.size += n;
        return this;
    }
    
    @Override
    public BufferedSink write(final Source source, long n) throws IOException {
        while (n > 0L) {
            final long read = source.read(this, n);
            if (read == -1L) {
                throw new EOFException();
            }
            n -= read;
        }
        return this;
    }
    
    @Override
    public void write(final Buffer buffer, long n) {
        if (buffer == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (buffer == this) {
            throw new IllegalArgumentException("source == this");
        }
        Util.checkOffsetAndCount(buffer.size, 0L, n);
        while (n > 0L) {
            if (n < buffer.head.limit - buffer.head.pos) {
                Segment prev;
                if (this.head != null) {
                    prev = this.head.prev;
                }
                else {
                    prev = null;
                }
                if (prev != null && prev.owner) {
                    final long n2 = prev.limit;
                    int pos;
                    if (prev.shared) {
                        pos = 0;
                    }
                    else {
                        pos = prev.pos;
                    }
                    if (n + n2 - pos <= 8192L) {
                        buffer.head.writeTo(prev, (int)n);
                        buffer.size -= n;
                        this.size += n;
                        break;
                    }
                }
                buffer.head = buffer.head.split((int)n);
            }
            final Segment head = buffer.head;
            final long n3 = head.limit - head.pos;
            buffer.head = head.pop();
            if (this.head == null) {
                this.head = head;
                final Segment head2 = this.head;
                final Segment head3 = this.head;
                final Segment head4 = this.head;
                head3.prev = head4;
                head2.next = head4;
            }
            else {
                this.head.prev.push(head).compact();
            }
            buffer.size -= n3;
            this.size += n3;
            n -= n3;
        }
    }
    
    @Override
    public long writeAll(final Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long n = 0L;
        while (true) {
            final long read = source.read(this, 8192L);
            if (read == -1L) {
                break;
            }
            n += read;
        }
        return n;
    }
    
    @Override
    public Buffer writeByte(final int n) {
        final Segment writableSegment = this.writableSegment(1);
        writableSegment.data[writableSegment.limit++] = (byte)n;
        ++this.size;
        return this;
    }
    
    @Override
    public Buffer writeDecimalLong(final long n) {
        Buffer buffer;
        if (n == 0L) {
            buffer = this.writeByte(48);
        }
        else {
            boolean b = false;
            long n2 = n;
            if (n < 0L) {
                n2 = -n;
                if (n2 < 0L) {
                    buffer = this.writeUtf8("-9223372036854775808");
                    return buffer;
                }
                b = true;
            }
            int n3;
            if (n2 < 100000000L) {
                if (n2 < 10000L) {
                    if (n2 < 100L) {
                        if (n2 < 10L) {
                            n3 = 1;
                        }
                        else {
                            n3 = 2;
                        }
                    }
                    else if (n2 < 1000L) {
                        n3 = 3;
                    }
                    else {
                        n3 = 4;
                    }
                }
                else if (n2 < 1000000L) {
                    if (n2 < 100000L) {
                        n3 = 5;
                    }
                    else {
                        n3 = 6;
                    }
                }
                else if (n2 < 10000000L) {
                    n3 = 7;
                }
                else {
                    n3 = 8;
                }
            }
            else if (n2 < 1000000000000L) {
                if (n2 < 10000000000L) {
                    if (n2 < 1000000000L) {
                        n3 = 9;
                    }
                    else {
                        n3 = 10;
                    }
                }
                else if (n2 < 100000000000L) {
                    n3 = 11;
                }
                else {
                    n3 = 12;
                }
            }
            else if (n2 < 1000000000000000L) {
                if (n2 < 10000000000000L) {
                    n3 = 13;
                }
                else if (n2 < 100000000000000L) {
                    n3 = 14;
                }
                else {
                    n3 = 15;
                }
            }
            else if (n2 < 100000000000000000L) {
                if (n2 < 10000000000000000L) {
                    n3 = 16;
                }
                else {
                    n3 = 17;
                }
            }
            else if (n2 < 1000000000000000000L) {
                n3 = 18;
            }
            else {
                n3 = 19;
            }
            int n4 = n3;
            if (b) {
                n4 = n3 + 1;
            }
            final Segment writableSegment = this.writableSegment(n4);
            final byte[] data = writableSegment.data;
            int n5 = writableSegment.limit + n4;
            while (n2 != 0L) {
                final int n6 = (int)(n2 % 10L);
                --n5;
                data[n5] = Buffer.DIGITS[n6];
                n2 /= 10L;
            }
            if (b) {
                data[n5 - 1] = 45;
            }
            writableSegment.limit += n4;
            this.size += n4;
            buffer = this;
        }
        return buffer;
    }
    
    @Override
    public Buffer writeHexadecimalUnsignedLong(long i) {
        Buffer writeByte;
        if (i == 0L) {
            writeByte = this.writeByte(48);
        }
        else {
            final int n = Long.numberOfTrailingZeros(Long.highestOneBit(i)) / 4 + 1;
            final Segment writableSegment = this.writableSegment(n);
            final byte[] data = writableSegment.data;
            for (int j = writableSegment.limit + n - 1; j >= writableSegment.limit; --j) {
                data[j] = Buffer.DIGITS[(int)(0xFL & i)];
                i >>>= 4;
            }
            writableSegment.limit += n;
            this.size += n;
            writeByte = this;
        }
        return writeByte;
    }
    
    @Override
    public Buffer writeInt(final int n) {
        final Segment writableSegment = this.writableSegment(4);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 24 & 0xFF);
        final int n3 = n2 + 1;
        data[n2] = (byte)(n >>> 16 & 0xFF);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n >>> 8 & 0xFF);
        data[n4] = (byte)(n & 0xFF);
        writableSegment.limit = n4 + 1;
        this.size += 4L;
        return this;
    }
    
    @Override
    public Buffer writeIntLe(final int n) {
        return this.writeInt(Util.reverseBytesInt(n));
    }
    
    @Override
    public Buffer writeLong(final long n) {
        final Segment writableSegment = this.writableSegment(8);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 56 & 0xFFL);
        final int n3 = n2 + 1;
        data[n2] = (byte)(n >>> 48 & 0xFFL);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n >>> 40 & 0xFFL);
        final int n5 = n4 + 1;
        data[n4] = (byte)(n >>> 32 & 0xFFL);
        final int n6 = n5 + 1;
        data[n5] = (byte)(n >>> 24 & 0xFFL);
        final int n7 = n6 + 1;
        data[n6] = (byte)(n >>> 16 & 0xFFL);
        final int n8 = n7 + 1;
        data[n7] = (byte)(n >>> 8 & 0xFFL);
        data[n8] = (byte)(n & 0xFFL);
        writableSegment.limit = n8 + 1;
        this.size += 8L;
        return this;
    }
    
    @Override
    public Buffer writeLongLe(final long n) {
        return this.writeLong(Util.reverseBytesLong(n));
    }
    
    @Override
    public Buffer writeShort(final int n) {
        final Segment writableSegment = this.writableSegment(2);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 8 & 0xFF);
        data[n2] = (byte)(n & 0xFF);
        writableSegment.limit = n2 + 1;
        this.size += 2L;
        return this;
    }
    
    @Override
    public Buffer writeShortLe(final int n) {
        return this.writeShort((int)Util.reverseBytesShort((short)n));
    }
    
    @Override
    public Buffer writeString(final String s, final int beginIndex, final int endIndex, final Charset charset) {
        if (s == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (beginIndex < 0) {
            throw new IllegalAccessError("beginIndex < 0: " + beginIndex);
        }
        if (endIndex < beginIndex) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + endIndex + " < " + beginIndex);
        }
        if (endIndex > s.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + endIndex + " > " + s.length());
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        Buffer buffer;
        if (charset.equals(Util.UTF_8)) {
            buffer = this.writeUtf8(s, beginIndex, endIndex);
        }
        else {
            final byte[] bytes = s.substring(beginIndex, endIndex).getBytes(charset);
            buffer = this.write(bytes, 0, bytes.length);
        }
        return buffer;
    }
    
    @Override
    public Buffer writeString(final String s, final Charset charset) {
        return this.writeString(s, 0, s.length(), charset);
    }
    
    public Buffer writeTo(final OutputStream outputStream) throws IOException {
        return this.writeTo(outputStream, this.size);
    }
    
    public Buffer writeTo(final OutputStream outputStream, long a) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, 0L, a);
        Segment head = this.head;
        while (true) {
            final Segment segment = head;
            if (a <= 0L) {
                break;
            }
            final int len = (int)Math.min(a, segment.limit - segment.pos);
            outputStream.write(segment.data, segment.pos, len);
            segment.pos += len;
            this.size -= len;
            final long n = a - len;
            head = segment;
            a = n;
            if (segment.pos != segment.limit) {
                continue;
            }
            head = segment.pop();
            this.head = head;
            SegmentPool.recycle(segment);
            a = n;
        }
        return this;
    }
    
    @Override
    public Buffer writeUtf8(final String s) {
        return this.writeUtf8(s, 0, s.length());
    }
    
    @Override
    public Buffer writeUtf8(final String s, int i, final int a) {
        if (s == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalAccessError("beginIndex < 0: " + i);
        }
        if (a < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + a + " < " + i);
        }
        if (a > s.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + a + " > " + s.length());
        }
        while (i < a) {
            final char char1 = s.charAt(i);
            if (char1 < '\u0080') {
                final Segment writableSegment = this.writableSegment(1);
                final byte[] data = writableSegment.data;
                final int n = writableSegment.limit - i;
                final int min = Math.min(a, 8192 - n);
                data[n + i] = (byte)char1;
                ++i;
                while (i < min) {
                    final char char2 = s.charAt(i);
                    if (char2 >= '\u0080') {
                        break;
                    }
                    data[n + i] = (byte)char2;
                    ++i;
                }
                final int n2 = i + n - writableSegment.limit;
                writableSegment.limit += n2;
                this.size += n2;
            }
            else if (char1 < '\u0800') {
                this.writeByte(char1 >> 6 | 0xC0);
                this.writeByte((char1 & '?') | 0x80);
                ++i;
            }
            else if (char1 < '\ud800' || char1 > '\udfff') {
                this.writeByte(char1 >> 12 | 0xE0);
                this.writeByte((char1 >> 6 & 0x3F) | 0x80);
                this.writeByte((char1 & '?') | 0x80);
                ++i;
            }
            else {
                char char3;
                if (i + 1 < a) {
                    char3 = s.charAt(i + 1);
                }
                else {
                    char3 = '\0';
                }
                if (char1 > '\udbff' || char3 < '\udc00' || char3 > '\udfff') {
                    this.writeByte(63);
                    ++i;
                }
                else {
                    final int n3 = 65536 + ((0xFFFF27FF & char1) << 10 | (0xFFFF23FF & char3));
                    this.writeByte(n3 >> 18 | 0xF0);
                    this.writeByte((n3 >> 12 & 0x3F) | 0x80);
                    this.writeByte((n3 >> 6 & 0x3F) | 0x80);
                    this.writeByte((n3 & 0x3F) | 0x80);
                    i += 2;
                }
            }
        }
        return this;
    }
    
    @Override
    public Buffer writeUtf8CodePoint(final int n) {
        if (n < 128) {
            this.writeByte(n);
        }
        else if (n < 2048) {
            this.writeByte(n >> 6 | 0xC0);
            this.writeByte((n & 0x3F) | 0x80);
        }
        else if (n < 65536) {
            if (n >= 55296 && n <= 57343) {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(n));
            }
            this.writeByte(n >> 12 | 0xE0);
            this.writeByte((n >> 6 & 0x3F) | 0x80);
            this.writeByte((n & 0x3F) | 0x80);
        }
        else {
            if (n > 1114111) {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(n));
            }
            this.writeByte(n >> 18 | 0xF0);
            this.writeByte((n >> 12 & 0x3F) | 0x80);
            this.writeByte((n >> 6 & 0x3F) | 0x80);
            this.writeByte((n & 0x3F) | 0x80);
        }
        return this;
    }
}
