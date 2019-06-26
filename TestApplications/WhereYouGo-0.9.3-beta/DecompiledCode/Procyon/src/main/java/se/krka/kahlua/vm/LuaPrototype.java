// 
// Decompiled by Procyon v0.5.34
// 

package se.krka.kahlua.vm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public final class LuaPrototype
{
    public int[] code;
    public Object[] constants;
    public boolean isVararg;
    public int[] lines;
    public int maxStacksize;
    public String name;
    public int numParams;
    public int numUpvalues;
    public LuaPrototype[] prototypes;
    
    public LuaPrototype() {
    }
    
    public LuaPrototype(final DataInputStream dataInputStream, final boolean b, String name, final int n) throws IOException {
        this.name = readLuaString(dataInputStream, n, b);
        if (this.name == null) {
            this.name = name;
        }
        dataInputStream.readInt();
        dataInputStream.readInt();
        this.numUpvalues = dataInputStream.read();
        this.numParams = dataInputStream.read();
        this.isVararg = ((dataInputStream.read() & 0x2) != 0x0);
        this.maxStacksize = dataInputStream.read();
        final int int1 = toInt(dataInputStream.readInt(), b);
        this.code = new int[int1];
        for (int i = 0; i < int1; ++i) {
            this.code[i] = toInt(dataInputStream.readInt(), b);
        }
        final int int2 = toInt(dataInputStream.readInt(), b);
        this.constants = new Object[int2];
        int j = 0;
        while (j < int2) {
            name = null;
            final int read = dataInputStream.read();
            Label_0251: {
                switch (read) {
                    default: {
                        throw new IOException("unknown constant type: " + read);
                    }
                    case 1: {
                        if (dataInputStream.read() == 0) {
                            name = (String)Boolean.FALSE;
                            break Label_0251;
                        }
                        name = (String)Boolean.TRUE;
                        break Label_0251;
                    }
                    case 4: {
                        name = readLuaString(dataInputStream, n, b);
                        break Label_0251;
                    }
                    case 3: {
                        long n2 = dataInputStream.readLong();
                        if (b) {
                            n2 = rev(n2);
                        }
                        name = (String)LuaState.toDouble(Double.longBitsToDouble(n2));
                    }
                    case 0: {
                        this.constants[j] = name;
                        ++j;
                        continue;
                    }
                }
            }
        }
        final int int3 = toInt(dataInputStream.readInt(), b);
        this.prototypes = new LuaPrototype[int3];
        for (int k = 0; k < int3; ++k) {
            this.prototypes[k] = new LuaPrototype(dataInputStream, b, this.name, n);
        }
        final int int4 = toInt(dataInputStream.readInt(), b);
        this.lines = new int[int4];
        for (int l = 0; l < int4; ++l) {
            this.lines[l] = toInt(dataInputStream.readInt(), b);
        }
        for (int int5 = toInt(dataInputStream.readInt(), b), n3 = 0; n3 < int5; ++n3) {
            readLuaString(dataInputStream, n, b);
            dataInputStream.readInt();
            dataInputStream.readInt();
        }
        for (int int6 = toInt(dataInputStream.readInt(), b), n4 = 0; n4 < int6; ++n4) {
            readLuaString(dataInputStream, n, b);
        }
    }
    
    private void dumpPrototype(final DataOutputStream dataOutputStream) throws IOException {
        dumpString(this.name, dataOutputStream);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.write(this.numUpvalues);
        dataOutputStream.write(this.numParams);
        int b;
        if (this.isVararg) {
            b = 2;
        }
        else {
            b = 0;
        }
        dataOutputStream.write(b);
        dataOutputStream.write(this.maxStacksize);
        final int length = this.code.length;
        dataOutputStream.writeInt(length);
        for (int i = 0; i < length; ++i) {
            dataOutputStream.writeInt(this.code[i]);
        }
        final int length2 = this.constants.length;
        dataOutputStream.writeInt(length2);
        for (int j = 0; j < length2; ++j) {
            final Object o = this.constants[j];
            if (o == null) {
                dataOutputStream.write(0);
            }
            else if (o instanceof Boolean) {
                dataOutputStream.write(1);
                int b2;
                if (o) {
                    b2 = 1;
                }
                else {
                    b2 = 0;
                }
                dataOutputStream.write(b2);
            }
            else if (o instanceof Double) {
                dataOutputStream.write(3);
                dataOutputStream.writeLong(Double.doubleToLongBits((double)o));
            }
            else {
                if (!(o instanceof String)) {
                    throw new RuntimeException("Bad type in constant pool");
                }
                dataOutputStream.write(4);
                dumpString((String)o, dataOutputStream);
            }
        }
        final int length3 = this.prototypes.length;
        dataOutputStream.writeInt(length3);
        for (int k = 0; k < length3; ++k) {
            this.prototypes[k].dumpPrototype(dataOutputStream);
        }
        final int length4 = this.lines.length;
        dataOutputStream.writeInt(length4);
        for (int l = 0; l < length4; ++l) {
            dataOutputStream.writeInt(this.lines[l]);
        }
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
    }
    
    private static void dumpString(final String str, final DataOutputStream dataOutputStream) throws IOException {
        if (str == null) {
            dataOutputStream.writeShort(0);
        }
        else {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            new DataOutputStream(out).writeUTF(str);
            final byte[] byteArray = out.toByteArray();
            final int len = byteArray.length - 2;
            dataOutputStream.writeInt(len + 1);
            dataOutputStream.write(byteArray, 2, len);
            dataOutputStream.write(0);
        }
    }
    
    private static void loadAssert(final boolean b, final String str) throws IOException {
        if (!b) {
            throw new IOException("Could not load bytecode:" + str);
        }
    }
    
    public static LuaClosure loadByteCode(final DataInputStream dataInputStream, final LuaTable luaTable) throws IOException {
        final boolean b = true;
        loadAssert(dataInputStream.read() == 27, "Signature 1");
        loadAssert(dataInputStream.read() == 76, "Signature 2");
        loadAssert(dataInputStream.read() == 117, "Signature 3");
        loadAssert(dataInputStream.read() == 97, "Signature 4");
        loadAssert(dataInputStream.read() == 81, "Version");
        loadAssert(dataInputStream.read() == 0, "Format");
        final boolean b2 = dataInputStream.read() == 1;
        loadAssert(dataInputStream.read() == 4, "Size int");
        final int read = dataInputStream.read();
        loadAssert(read == 4 || read == 8, "Size t");
        loadAssert(dataInputStream.read() == 4, "Size instr");
        loadAssert(dataInputStream.read() == 8, "Size number");
        loadAssert(dataInputStream.read() == 0 && b, "Integral");
        return new LuaClosure(new LuaPrototype(dataInputStream, b2, null, read), luaTable);
    }
    
    public static LuaClosure loadByteCode(final InputStream in, final LuaTable luaTable) throws IOException {
        InputStream inputStream = in;
        if (!(in instanceof DataInputStream)) {
            inputStream = new DataInputStream(in);
        }
        return loadByteCode((DataInputStream)inputStream, luaTable);
    }
    
    private static String loadUndecodable(final byte[] bytes) {
        for (int i = 2; i < bytes.length; ++i) {
            if ((bytes[i] & 0x80) == 0x80) {
                bytes[i] = 63;
            }
        }
        return new String(bytes, 2, bytes.length - 2);
    }
    
    private static String readLuaString(final DataInputStream dataInputStream, int n, final boolean b) throws IOException {
        long long1 = 0L;
        if (n == 4) {
            long1 = toInt(dataInputStream.readInt(), b);
        }
        else if (n == 8) {
            long1 = toLong(dataInputStream.readLong(), b);
        }
        else {
            loadAssert(false, "Bad string size");
        }
        String s;
        if (long1 == 0L) {
            s = null;
        }
        else {
            final long lng = long1 - 1L;
            loadAssert(lng < 65536L, "Too long string:" + lng);
            n = (int)lng;
            final byte[] array = new byte[n + 3];
            array[0] = (byte)(n >> 8 & 0xFF);
            array[1] = (byte)(n & 0xFF);
            dataInputStream.readFully(array, 2, n + 1);
            loadAssert(array[n + 2] == 0, "String loading");
            try {
                final DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(array));
                s = dataInputStream2.readUTF();
                dataInputStream2.close();
            }
            catch (IOException ex) {
                s = loadUndecodable(array);
            }
        }
        return s;
    }
    
    public static int rev(final int n) {
        return (n & 0xFF) << 24 | (n >>> 8 & 0xFF) << 16 | (n >>> 16 & 0xFF) << 8 | (n >>> 24 & 0xFF);
    }
    
    public static long rev(final long n) {
        return (n & 0xFFL) << 56 | (n >>> 8 & 0xFFL) << 48 | (n >>> 16 & 0xFFL) << 40 | (n >>> 24 & 0xFFL) << 32 | (n >>> 32 & 0xFFL) << 24 | (n >>> 40 & 0xFFL) << 16 | (n >>> 48 & 0xFFL) << 8 | (n >>> 56 & 0xFFL);
    }
    
    public static int toInt(final int n, final boolean b) {
        int rev = n;
        if (b) {
            rev = rev(n);
        }
        return rev;
    }
    
    public static long toLong(final long n, final boolean b) {
        long rev = n;
        if (b) {
            rev = rev(n);
        }
        return rev;
    }
    
    public void dump(final OutputStream out) throws IOException {
        DataOutputStream dataOutputStream;
        if (out instanceof DataOutputStream) {
            dataOutputStream = (DataOutputStream)out;
        }
        else {
            dataOutputStream = new DataOutputStream(out);
        }
        dataOutputStream.write(27);
        dataOutputStream.write(76);
        dataOutputStream.write(117);
        dataOutputStream.write(97);
        dataOutputStream.write(81);
        dataOutputStream.write(0);
        dataOutputStream.write(0);
        dataOutputStream.write(4);
        dataOutputStream.write(4);
        dataOutputStream.write(4);
        dataOutputStream.write(8);
        dataOutputStream.write(0);
        this.dumpPrototype(dataOutputStream);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
