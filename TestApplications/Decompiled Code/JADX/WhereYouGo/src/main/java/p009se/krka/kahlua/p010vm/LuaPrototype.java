package p009se.krka.kahlua.p010vm;

import android.support.p000v4.media.session.PlaybackStateCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* renamed from: se.krka.kahlua.vm.LuaPrototype */
public final class LuaPrototype {
    public int[] code;
    public Object[] constants;
    public boolean isVararg;
    public int[] lines;
    public int maxStacksize;
    public String name;
    public int numParams;
    public int numUpvalues;
    public LuaPrototype[] prototypes;

    public LuaPrototype(DataInputStream in, boolean littleEndian, String parentName, int size_t) throws IOException {
        int i;
        this.name = LuaPrototype.readLuaString(in, size_t, littleEndian);
        if (this.name == null) {
            this.name = parentName;
        }
        in.readInt();
        in.readInt();
        this.numUpvalues = in.read();
        this.numParams = in.read();
        this.isVararg = (in.read() & 2) != 0;
        this.maxStacksize = in.read();
        int codeLen = LuaPrototype.toInt(in.readInt(), littleEndian);
        this.code = new int[codeLen];
        for (i = 0; i < codeLen; i++) {
            this.code[i] = LuaPrototype.toInt(in.readInt(), littleEndian);
        }
        int constantsLen = LuaPrototype.toInt(in.readInt(), littleEndian);
        this.constants = new Object[constantsLen];
        for (i = 0; i < constantsLen; i++) {
            Object o = null;
            int type = in.read();
            switch (type) {
                case 0:
                    break;
                case 1:
                    if (in.read() != 0) {
                        o = Boolean.TRUE;
                        break;
                    } else {
                        o = Boolean.FALSE;
                        break;
                    }
                case 3:
                    long bits = in.readLong();
                    if (littleEndian) {
                        bits = LuaPrototype.rev(bits);
                    }
                    o = LuaState.toDouble(Double.longBitsToDouble(bits));
                    break;
                case 4:
                    o = LuaPrototype.readLuaString(in, size_t, littleEndian);
                    break;
                default:
                    throw new IOException("unknown constant type: " + type);
            }
            this.constants[i] = o;
        }
        int prototypesLen = LuaPrototype.toInt(in.readInt(), littleEndian);
        this.prototypes = new LuaPrototype[prototypesLen];
        for (i = 0; i < prototypesLen; i++) {
            this.prototypes[i] = new LuaPrototype(in, littleEndian, this.name, size_t);
        }
        int tmp = LuaPrototype.toInt(in.readInt(), littleEndian);
        this.lines = new int[tmp];
        for (i = 0; i < tmp; i++) {
            this.lines[i] = LuaPrototype.toInt(in.readInt(), littleEndian);
        }
        tmp = LuaPrototype.toInt(in.readInt(), littleEndian);
        for (i = 0; i < tmp; i++) {
            LuaPrototype.readLuaString(in, size_t, littleEndian);
            in.readInt();
            in.readInt();
        }
        tmp = LuaPrototype.toInt(in.readInt(), littleEndian);
        for (i = 0; i < tmp; i++) {
            LuaPrototype.readLuaString(in, size_t, littleEndian);
        }
    }

    public String toString() {
        return this.name;
    }

    private static String readLuaString(DataInputStream in, int size_t, boolean littleEndian) throws IOException {
        long len = 0;
        if (size_t == 4) {
            len = (long) LuaPrototype.toInt(in.readInt(), littleEndian);
        } else if (size_t == 8) {
            len = LuaPrototype.toLong(in.readLong(), littleEndian);
        } else {
            LuaPrototype.loadAssert(false, "Bad string size");
        }
        if (len == 0) {
            return null;
        }
        len--;
        LuaPrototype.loadAssert(len < PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH, "Too long string:" + len);
        int iLen = (int) len;
        byte[] stringData = new byte[(iLen + 3)];
        stringData[0] = (byte) ((iLen >> 8) & 255);
        stringData[1] = (byte) (iLen & 255);
        in.readFully(stringData, 2, iLen + 1);
        LuaPrototype.loadAssert(stringData[iLen + 2] == (byte) 0, "String loading");
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(stringData));
            String s = dis.readUTF();
            dis.close();
            return s;
        } catch (IOException e) {
            return LuaPrototype.loadUndecodable(stringData);
        }
    }

    private static String loadUndecodable(byte[] bytes) {
        for (int i = 2; i < bytes.length; i++) {
            if ((bytes[i] & 128) == 128) {
                bytes[i] = (byte) 63;
            }
        }
        return new String(bytes, 2, bytes.length - 2);
    }

    public static int rev(int v) {
        return ((((v & 255) << 24) | (((v >>> 8) & 255) << 16)) | (((v >>> 16) & 255) << 8)) | ((v >>> 24) & 255);
    }

    public static long rev(long v) {
        return ((((((((v & 255) << 56) | (((v >>> 8) & 255) << 48)) | (((v >>> 16) & 255) << 40)) | (((v >>> 24) & 255) << 32)) | (((v >>> 32) & 255) << 24)) | (((v >>> 40) & 255) << 16)) | (((v >>> 48) & 255) << 8)) | ((v >>> 56) & 255);
    }

    public static int toInt(int bits, boolean littleEndian) {
        return littleEndian ? LuaPrototype.rev(bits) : bits;
    }

    public static long toLong(long bits, boolean littleEndian) {
        return littleEndian ? LuaPrototype.rev(bits) : bits;
    }

    public static LuaClosure loadByteCode(DataInputStream in, LuaTable env) throws IOException {
        boolean z;
        boolean littleEndian;
        boolean z2 = true;
        if (in.read() == 27) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Signature 1");
        if (in.read() == 76) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Signature 2");
        if (in.read() == 117) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Signature 3");
        if (in.read() == 97) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Signature 4");
        if (in.read() == 81) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Version");
        if (in.read() == 0) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Format");
        if (in.read() == 1) {
            littleEndian = true;
        } else {
            littleEndian = false;
        }
        if (in.read() == 4) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Size int");
        int size_t = in.read();
        if (size_t == 4 || size_t == 8) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Size t");
        if (in.read() == 4) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Size instr");
        if (in.read() == 8) {
            z = true;
        } else {
            z = false;
        }
        LuaPrototype.loadAssert(z, "Size number");
        if (in.read() != 0) {
            z2 = false;
        }
        LuaPrototype.loadAssert(z2, "Integral");
        return new LuaClosure(new LuaPrototype(in, littleEndian, null, size_t), env);
    }

    private static void loadAssert(boolean c, String message) throws IOException {
        if (!c) {
            throw new IOException("Could not load bytecode:" + message);
        }
    }

    public static LuaClosure loadByteCode(InputStream in, LuaTable env) throws IOException {
        if (!(in instanceof DataInputStream)) {
            in = new DataInputStream(in);
        }
        return LuaPrototype.loadByteCode((DataInputStream) in, env);
    }

    public void dump(OutputStream os) throws IOException {
        DataOutputStream dos;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream) os;
        } else {
            dos = new DataOutputStream(os);
        }
        dos.write(27);
        dos.write(76);
        dos.write(117);
        dos.write(97);
        dos.write(81);
        dos.write(0);
        dos.write(0);
        dos.write(4);
        dos.write(4);
        dos.write(4);
        dos.write(8);
        dos.write(0);
        dumpPrototype(dos);
    }

    private void dumpPrototype(DataOutputStream dos) throws IOException {
        int i;
        LuaPrototype.dumpString(this.name, dos);
        dos.writeInt(0);
        dos.writeInt(0);
        dos.write(this.numUpvalues);
        dos.write(this.numParams);
        if (this.isVararg) {
            i = 2;
        } else {
            i = 0;
        }
        dos.write(i);
        dos.write(this.maxStacksize);
        dos.writeInt(codeLen);
        for (int i2 : this.code) {
            dos.writeInt(i2);
        }
        dos.writeInt(constantsLen);
        for (Double o : this.constants) {
            if (o == null) {
                dos.write(0);
            } else if (o instanceof Boolean) {
                dos.write(1);
                dos.write(((Boolean) o).booleanValue() ? 1 : 0);
            } else if (o instanceof Double) {
                dos.write(3);
                dos.writeLong(Double.doubleToLongBits(o.doubleValue()));
            } else if (o instanceof String) {
                dos.write(4);
                LuaPrototype.dumpString((String) o, dos);
            } else {
                throw new RuntimeException("Bad type in constant pool");
            }
        }
        dos.writeInt(prototypesLen);
        for (LuaPrototype dumpPrototype : this.prototypes) {
            dumpPrototype.dumpPrototype(dos);
        }
        dos.writeInt(linesLen);
        for (int i22 : this.lines) {
            dos.writeInt(i22);
        }
        dos.writeInt(0);
        dos.writeInt(0);
    }

    private static void dumpString(String name, DataOutputStream dos) throws IOException {
        if (name == null) {
            dos.writeShort(0);
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DataOutputStream(baos).writeUTF(name);
        byte[] bytes = baos.toByteArray();
        int numBytes = bytes.length - 2;
        dos.writeInt(numBytes + 1);
        dos.write(bytes, 2, numBytes);
        dos.write(0);
    }
}
