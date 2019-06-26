// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class SerializedData extends AbstractSerializedData
{
    private DataInputStream in;
    private ByteArrayInputStream inbuf;
    protected boolean isOut;
    private boolean justCalc;
    private int len;
    private DataOutputStream out;
    private ByteArrayOutputStream outbuf;
    
    public SerializedData() {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream();
        this.out = new DataOutputStream(this.outbuf);
    }
    
    public SerializedData(final int size) {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream(size);
        this.out = new DataOutputStream(this.outbuf);
    }
    
    public SerializedData(final File file) throws Exception {
        this.isOut = true;
        this.justCalc = false;
        final FileInputStream in = new FileInputStream(file);
        final byte[] array = new byte[(int)file.length()];
        new DataInputStream(in).readFully(array);
        in.close();
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(array);
        this.in = new DataInputStream(this.inbuf);
    }
    
    public SerializedData(final boolean justCalc) {
        this.isOut = true;
        this.justCalc = false;
        if (!justCalc) {
            this.outbuf = new ByteArrayOutputStream();
            this.out = new DataOutputStream(this.outbuf);
        }
        this.justCalc = justCalc;
        this.len = 0;
    }
    
    public SerializedData(final byte[] buf) {
        this.isOut = true;
        this.justCalc = false;
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(buf);
        this.in = new DataInputStream(this.inbuf);
        this.len = 0;
    }
    
    private void writeInt32(final int n, final DataOutputStream dataOutputStream) {
        int i = 0;
        while (i < 4) {
            try {
                dataOutputStream.write(n >> i * 8);
                ++i;
                continue;
            }
            catch (Exception ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("write int32 error");
                }
            }
            break;
        }
    }
    
    private void writeInt64(final long n, final DataOutputStream dataOutputStream) {
        int i = 0;
        while (i < 8) {
            final int b = (int)(n >> i * 8);
            try {
                dataOutputStream.write(b);
                ++i;
                continue;
            }
            catch (Exception ex) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("write int64 error");
                }
            }
            break;
        }
    }
    
    public void cleanup() {
        try {
            if (this.inbuf != null) {
                this.inbuf.close();
                this.inbuf = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            if (this.in != null) {
                this.in.close();
                this.in = null;
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        try {
            if (this.outbuf != null) {
                this.outbuf.close();
                this.outbuf = null;
            }
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
        try {
            if (this.out != null) {
                this.out.close();
                this.out = null;
            }
        }
        catch (Exception ex4) {
            FileLog.e(ex4);
        }
    }
    
    @Override
    public int getPosition() {
        return this.len;
    }
    
    @Override
    public int length() {
        if (!this.justCalc) {
            int n;
            if (this.isOut) {
                n = this.outbuf.size();
            }
            else {
                n = this.inbuf.available();
            }
            return n;
        }
        return this.len;
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
            int read = this.in.read();
            ++this.len;
            int n;
            if (read >= 254) {
                read = (this.in.read() | this.in.read() << 8 | this.in.read() << 16);
                this.len += 3;
                n = 4;
            }
            else {
                n = 1;
            }
            final byte[] b2 = new byte[read];
            this.in.read(b2);
            ++this.len;
            while ((read + n) % 4 != 0) {
                this.in.read();
                ++this.len;
                ++n;
            }
            return b2;
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
    
    @Override
    public NativeByteBuffer readByteBuffer(final boolean b) {
        return null;
    }
    
    @Override
    public void readBytes(final byte[] b, final boolean b2) {
        try {
            this.in.read(b);
            this.len += b.length;
        }
        catch (Exception cause) {
            if (b2) {
                throw new RuntimeException("read bytes error", cause);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read bytes error");
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
        int i = 0;
        int n = 0;
        while (i < 4) {
            try {
                n |= this.in.read() << i * 8;
                ++this.len;
                ++i;
                continue;
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
            break;
        }
        return n;
    }
    
    @Override
    public long readInt64(final boolean b) {
        int i = 0;
        long n = 0L;
        while (i < 8) {
            try {
                n |= (long)this.in.read() << i * 8;
                ++this.len;
                ++i;
                continue;
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
            break;
        }
        return n;
    }
    
    @Override
    public String readString(final boolean b) {
        try {
            int read = this.in.read();
            ++this.len;
            int n;
            if (read >= 254) {
                read = (this.in.read() | this.in.read() << 8 | this.in.read() << 16);
                this.len += 3;
                n = 4;
            }
            else {
                n = 1;
            }
            final byte[] array = new byte[read];
            this.in.read(array);
            ++this.len;
            while ((read + n) % 4 != 0) {
                this.in.read();
                ++this.len;
                ++n;
            }
            return new String(array, "UTF-8");
        }
        catch (Exception cause) {
            if (!b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read string error");
                }
                return null;
            }
            throw new RuntimeException("read string error", cause);
        }
    }
    
    @Override
    public int remaining() {
        try {
            return this.in.available();
        }
        catch (Exception ex) {
            return Integer.MAX_VALUE;
        }
    }
    
    protected void set(final byte[] buf) {
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(buf);
        this.in = new DataInputStream(this.inbuf);
    }
    
    @Override
    public void skip(final int n) {
        if (n == 0) {
            return;
        }
        if (!this.justCalc) {
            final DataInputStream in = this.in;
            if (in != null) {
                try {
                    in.skipBytes(n);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        else {
            this.len += n;
        }
    }
    
    public byte[] toByteArray() {
        return this.outbuf.toByteArray();
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
    public void writeByte(final byte v) {
        try {
            if (!this.justCalc) {
                this.out.writeByte(v);
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
        try {
            if (!this.justCalc) {
                this.out.writeByte((byte)n);
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
    public void writeByteArray(final byte[] b) {
        try {
            if (b.length <= 253) {
                if (!this.justCalc) {
                    this.out.write(b.length);
                }
                else {
                    ++this.len;
                }
            }
            else if (!this.justCalc) {
                this.out.write(254);
                this.out.write(b.length);
                this.out.write(b.length >> 8);
                this.out.write(b.length >> 16);
            }
            else {
                this.len += 4;
            }
            if (!this.justCalc) {
                this.out.write(b);
            }
            else {
                this.len += b.length;
            }
            int n;
            if (b.length <= 253) {
                n = 1;
            }
            else {
                n = 4;
            }
            while ((b.length + n) % 4 != 0) {
                if (!this.justCalc) {
                    this.out.write(0);
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
    public void writeByteArray(final byte[] b, int off, final int b2) {
        while (true) {
            if (b2 <= 253) {
                try {
                    if (!this.justCalc) {
                        this.out.write(b2);
                    }
                    else {
                        ++this.len;
                    }
                    while (true) {
                        while (true) {
                            if (!this.justCalc) {
                                this.out.write(b, off, b2);
                            }
                            else {
                                this.len += b2;
                            }
                            if (b2 <= 253) {
                                off = 1;
                            }
                            else {
                                off = 4;
                            }
                            while ((b2 + off) % 4 != 0) {
                                if (!this.justCalc) {
                                    this.out.write(0);
                                }
                                else {
                                    ++this.len;
                                }
                                ++off;
                            }
                            return;
                            Label_0088: {
                                this.len += 4;
                            }
                            continue;
                            this.out.write(254);
                            this.out.write(b2);
                            this.out.write(b2 >> 8);
                            this.out.write(b2 >> 16);
                            continue;
                        }
                        continue;
                    }
                }
                // iftrue(Label_0088:, this.justCalc)
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
    }
    
    @Override
    public void writeBytes(final byte[] b) {
        try {
            if (!this.justCalc) {
                this.out.write(b);
            }
            else {
                this.len += b.length;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
            }
        }
    }
    
    @Override
    public void writeBytes(final byte[] b, final int off, final int len) {
        try {
            if (!this.justCalc) {
                this.out.write(b, off, len);
            }
            else {
                this.len += len;
            }
        }
        catch (Exception ex) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write bytes error");
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
        if (!this.justCalc) {
            this.writeInt32(n, this.out);
        }
        else {
            this.len += 4;
        }
    }
    
    @Override
    public void writeInt64(final long n) {
        if (!this.justCalc) {
            this.writeInt64(n, this.out);
        }
        else {
            this.len += 8;
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
