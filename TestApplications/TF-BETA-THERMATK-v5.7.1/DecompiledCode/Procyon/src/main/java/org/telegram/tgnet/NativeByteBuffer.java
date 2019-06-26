// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class NativeByteBuffer extends AbstractSerializedData
{
    private static final ThreadLocal<NativeByteBuffer> addressWrapper;
    protected long address;
    public ByteBuffer buffer;
    private boolean justCalc;
    private int len;
    public boolean reused;
    
    static {
        addressWrapper = new ThreadLocal<NativeByteBuffer>() {
            @Override
            protected NativeByteBuffer initialValue() {
                return new NativeByteBuffer(0, true, null);
            }
        };
    }
    
    public NativeByteBuffer(final int n) throws Exception {
        this.reused = true;
        if (n >= 0) {
            this.address = native_getFreeBuffer(n);
            final long address = this.address;
            if (address != 0L) {
                (this.buffer = native_getJavaByteBuffer(address)).position(0);
                this.buffer.limit(n);
                this.buffer.order(ByteOrder.LITTLE_ENDIAN);
            }
            return;
        }
        throw new Exception("invalid NativeByteBuffer size");
    }
    
    private NativeByteBuffer(final int n, final boolean b) {
        this.reused = true;
    }
    
    public NativeByteBuffer(final boolean justCalc) {
        this.reused = true;
        this.justCalc = justCalc;
    }
    
    public static native long native_getFreeBuffer(final int p0);
    
    public static native ByteBuffer native_getJavaByteBuffer(final long p0);
    
    public static native int native_limit(final long p0);
    
    public static native int native_position(final long p0);
    
    public static native void native_reuse(final long p0);
    
    public static NativeByteBuffer wrap(final long address) {
        final NativeByteBuffer nativeByteBuffer = NativeByteBuffer.addressWrapper.get();
        if (address != 0L) {
            if (!nativeByteBuffer.reused && BuildVars.LOGS_ENABLED) {
                FileLog.e("forgot to reuse?");
            }
            nativeByteBuffer.address = address;
            nativeByteBuffer.reused = false;
            (nativeByteBuffer.buffer = native_getJavaByteBuffer(address)).limit(native_limit(address));
            final int native_position = native_position(address);
            if (native_position <= nativeByteBuffer.buffer.limit()) {
                nativeByteBuffer.buffer.position(native_position);
            }
            nativeByteBuffer.buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return nativeByteBuffer;
    }
    
    public int capacity() {
        return this.buffer.capacity();
    }
    
    public void compact() {
        this.buffer.compact();
    }
    
    public int getIntFromByte(byte b) {
        if (b < 0) {
            b += 256;
        }
        return b;
    }
    
    @Override
    public int getPosition() {
        return this.buffer.position();
    }
    
    public boolean hasRemaining() {
        return this.buffer.hasRemaining();
    }
    
    @Override
    public int length() {
        if (!this.justCalc) {
            return this.buffer.position();
        }
        return this.len;
    }
    
    public int limit() {
        return this.buffer.limit();
    }
    
    public void limit(final int n) {
        this.buffer.limit(n);
    }
    
    public int position() {
        return this.buffer.position();
    }
    
    public void position(final int n) {
        this.buffer.position(n);
    }
    
    public void put(final ByteBuffer src) {
        this.buffer.put(src);
    }
    
    @Override
    public boolean readBool(final boolean b) {
        final int int32 = this.readInt32(b);
        if (int32 == -1720552011) {
            return true;
        }
        if (int32 == -1132882121) {
            return false;
        }
        if (!b) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Not bool value!");
            }
            return false;
        }
        throw new RuntimeException("Not bool value!");
    }
    
    @Override
    public byte[] readByteArray(final boolean b) {
        try {
            int intFromByte = this.getIntFromByte(this.buffer.get());
            int n;
            if (intFromByte >= 254) {
                intFromByte = (this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16);
                n = 4;
            }
            else {
                n = 1;
            }
            final byte[] dst = new byte[intFromByte];
            this.buffer.get(dst);
            while ((intFromByte + n) % 4 != 0) {
                this.buffer.get();
                ++n;
            }
            return dst;
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read byte array error");
                }
                return new byte[0];
            }
            throw new RuntimeException("read byte array error", cause);
        }
    }
    
    @Override
    public NativeByteBuffer readByteBuffer(final boolean b) {
        try {
            int intFromByte = this.getIntFromByte(this.buffer.get());
            int n;
            if (intFromByte >= 254) {
                intFromByte = (this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16);
                n = 4;
            }
            else {
                n = 1;
            }
            final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(intFromByte);
            final int limit = this.buffer.limit();
            this.buffer.limit(this.buffer.position() + intFromByte);
            nativeByteBuffer.buffer.put(this.buffer);
            this.buffer.limit(limit);
            nativeByteBuffer.buffer.position(0);
            while ((intFromByte + n) % 4 != 0) {
                this.buffer.get();
                ++n;
            }
            return nativeByteBuffer;
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read byte array error");
                }
                return null;
            }
            throw new RuntimeException("read byte array error", cause);
        }
    }
    
    public void readBytes(final byte[] dst, final int offset, final int length, final boolean b) {
        try {
            this.buffer.get(dst, offset, length);
        }
        catch (Exception cause) {
            if (b) {
                throw new RuntimeException("read raw error", cause);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read raw error");
            }
        }
    }
    
    @Override
    public void readBytes(final byte[] dst, final boolean b) {
        try {
            this.buffer.get(dst);
        }
        catch (Exception cause) {
            if (b) {
                throw new RuntimeException("read raw error", cause);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read raw error");
            }
        }
    }
    
    @Override
    public byte[] readData(final int n, final boolean b) {
        final byte[] array = new byte[n];
        this.readBytes(array, b);
        return array;
    }
    
    @Override
    public double readDouble(final boolean b) {
        try {
            return Double.longBitsToDouble(this.readInt64(b));
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read double error");
                }
                return 0.0;
            }
            throw new RuntimeException("read double error", cause);
        }
    }
    
    @Override
    public int readInt32(final boolean b) {
        try {
            return this.buffer.getInt();
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read int32 error");
                }
                return 0;
            }
            throw new RuntimeException("read int32 error", cause);
        }
    }
    
    @Override
    public long readInt64(final boolean b) {
        try {
            return this.buffer.getLong();
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read int64 error");
                }
                return 0L;
            }
            throw new RuntimeException("read int64 error", cause);
        }
    }
    
    @Override
    public String readString(final boolean b) {
        final int position = this.getPosition();
        try {
            int intFromByte = this.getIntFromByte(this.buffer.get());
            int n;
            if (intFromByte >= 254) {
                intFromByte = (this.getIntFromByte(this.buffer.get()) | this.getIntFromByte(this.buffer.get()) << 8 | this.getIntFromByte(this.buffer.get()) << 16);
                n = 4;
            }
            else {
                n = 1;
            }
            final byte[] array = new byte[intFromByte];
            this.buffer.get(array);
            while ((intFromByte + n) % 4 != 0) {
                this.buffer.get();
                ++n;
            }
            return new String(array, "UTF-8");
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read string error");
                }
                this.position(position);
                return "";
            }
            throw new RuntimeException("read string error", cause);
        }
    }
    
    @Override
    public int remaining() {
        return this.buffer.remaining();
    }
    
    public void reuse() {
        final long address = this.address;
        if (address != 0L) {
            this.reused = true;
            native_reuse(address);
        }
    }
    
    public void rewind() {
        if (this.justCalc) {
            this.len = 0;
        }
        else {
            this.buffer.rewind();
        }
    }
    
    @Override
    public void skip(final int n) {
        if (n == 0) {
            return;
        }
        if (!this.justCalc) {
            final ByteBuffer buffer = this.buffer;
            buffer.position(buffer.position() + n);
        }
        else {
            this.len += n;
        }
    }
    
    @Override
    public void writeBool(final boolean b) {
        if (!this.justCalc) {
            if (b) {
                this.writeInt32(-1720552011);
            }
            else {
                this.writeInt32(-1132882121);
            }
        }
        else {
            this.len += 4;
        }
    }
    
    @Override
    public void writeByte(final byte b) {
        try {
            if (!this.justCalc) {
                this.buffer.put(b);
            }
            else {
                ++this.len;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
            }
        }
    }
    
    @Override
    public void writeByte(final int n) {
        this.writeByte((byte)n);
    }
    
    @Override
    public void writeByteArray(final byte[] src) {
        try {
            if (src.length <= 253) {
                if (!this.justCalc) {
                    this.buffer.put((byte)src.length);
                }
                else {
                    ++this.len;
                }
            }
            else if (!this.justCalc) {
                this.buffer.put((byte)(-2));
                this.buffer.put((byte)src.length);
                this.buffer.put((byte)(src.length >> 8));
                this.buffer.put((byte)(src.length >> 16));
            }
            else {
                this.len += 4;
            }
            if (!this.justCalc) {
                this.buffer.put(src);
            }
            else {
                this.len += src.length;
            }
            int n;
            if (src.length <= 253) {
                n = 1;
            }
            else {
                n = 4;
            }
            while ((src.length + n) % 4 != 0) {
                if (!this.justCalc) {
                    this.buffer.put((byte)0);
                }
                else {
                    ++this.len;
                }
                ++n;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte array error");
            }
        }
    }
    
    @Override
    public void writeByteArray(final byte[] src, int offset, final int length) {
        while (true) {
            if (length <= 253) {
                try {
                    if (!this.justCalc) {
                        this.buffer.put((byte)length);
                    }
                    else {
                        ++this.len;
                    }
                    while (true) {
                        while (true) {
                            if (!this.justCalc) {
                                this.buffer.put(src, offset, length);
                            }
                            else {
                                this.len += length;
                            }
                            if (length <= 253) {
                                offset = 1;
                            }
                            else {
                                offset = 4;
                            }
                            while ((length + offset) % 4 != 0) {
                                if (!this.justCalc) {
                                    this.buffer.put((byte)0);
                                }
                                else {
                                    ++this.len;
                                }
                                ++offset;
                            }
                            return;
                            Label_0096: {
                                this.len += 4;
                            }
                            continue;
                            this.buffer.put((byte)(-2));
                            this.buffer.put((byte)length);
                            this.buffer.put((byte)(length >> 8));
                            this.buffer.put((byte)(length >> 16));
                            continue;
                        }
                        continue;
                    }
                }
                // iftrue(Label_0096:, this.justCalc)
                catch (Exception ex) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("write byte array error");
                    }
                }
                return;
            }
            continue;
        }
    }
    
    @Override
    public void writeByteBuffer(final NativeByteBuffer nativeByteBuffer) {
        try {
            final int limit = nativeByteBuffer.limit();
            if (limit <= 253) {
                if (!this.justCalc) {
                    this.buffer.put((byte)limit);
                }
                else {
                    ++this.len;
                }
            }
            else if (!this.justCalc) {
                this.buffer.put((byte)(-2));
                this.buffer.put((byte)limit);
                this.buffer.put((byte)(limit >> 8));
                this.buffer.put((byte)(limit >> 16));
            }
            else {
                this.len += 4;
            }
            if (!this.justCalc) {
                nativeByteBuffer.rewind();
                this.buffer.put(nativeByteBuffer.buffer);
            }
            else {
                this.len += limit;
            }
            int n;
            if (limit <= 253) {
                n = 1;
            }
            else {
                n = 4;
            }
            while ((limit + n) % 4 != 0) {
                if (!this.justCalc) {
                    this.buffer.put((byte)0);
                }
                else {
                    ++this.len;
                }
                ++n;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void writeBytes(final NativeByteBuffer nativeByteBuffer) {
        if (this.justCalc) {
            this.len += nativeByteBuffer.limit();
        }
        else {
            nativeByteBuffer.rewind();
            this.buffer.put(nativeByteBuffer.buffer);
        }
    }
    
    @Override
    public void writeBytes(final byte[] src) {
        try {
            if (!this.justCalc) {
                this.buffer.put(src);
            }
            else {
                this.len += src.length;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
            }
        }
    }
    
    @Override
    public void writeBytes(final byte[] src, final int offset, final int length) {
        try {
            if (!this.justCalc) {
                this.buffer.put(src, offset, length);
            }
            else {
                this.len += length;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
            }
        }
    }
    
    @Override
    public void writeDouble(final double n) {
        try {
            this.writeInt64(Double.doubleToRawLongBits(n));
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write double error");
            }
        }
    }
    
    @Override
    public void writeInt32(final int n) {
        try {
            if (!this.justCalc) {
                this.buffer.putInt(n);
            }
            else {
                this.len += 4;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write int32 error");
            }
        }
    }
    
    @Override
    public void writeInt64(final long n) {
        try {
            if (!this.justCalc) {
                this.buffer.putLong(n);
            }
            else {
                this.len += 8;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write int64 error");
            }
        }
    }
    
    @Override
    public void writeString(final String s) {
        try {
            this.writeByteArray(s.getBytes("UTF-8"));
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write string error");
            }
        }
    }
}
