package p005cz.matejcik.openwig.formats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Hashtable;
import p005cz.matejcik.openwig.Cartridge;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Serializable;
import p005cz.matejcik.openwig.platform.FileHandle;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaClosure;
import p009se.krka.kahlua.p010vm.LuaPrototype;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.p010vm.UpValue;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: cz.matejcik.openwig.formats.Savegame */
public class Savegame {
    private static final byte LUATABLE_END = (byte) 17;
    private static final byte LUATABLE_PAIR = (byte) 16;
    private static final byte LUA_BOOLEAN = (byte) 3;
    private static final byte LUA_CLOSURE = (byte) 5;
    private static final byte LUA_DOUBLE = (byte) 1;
    private static final byte LUA_JAVAFUNC = (byte) 8;
    private static final byte LUA_NIL = (byte) 0;
    private static final byte LUA_OBJECT = (byte) 6;
    private static final byte LUA_REFERENCE = (byte) 7;
    private static final byte LUA_STRING = (byte) 2;
    private static final byte LUA_TABLE = (byte) 4;
    private static final String SIGNATURE = "openWIG savegame\n";
    private int currentId;
    private int currentJavafunc = 0;
    protected boolean debug = false;
    private Hashtable idToJavafuncMap = new Hashtable(128);
    private Hashtable javafuncToIdMap = new Hashtable(128);
    int level = 0;
    private Hashtable objectStore;
    private FileHandle saveFile;

    public Savegame(FileHandle fc) {
        if (fc == null) {
            throw new NullPointerException("savefile must not be null");
        }
        this.saveFile = fc;
    }

    protected Savegame() {
    }

    public boolean exists() throws IOException {
        return this.saveFile.exists();
    }

    /* Access modifiers changed, original: protected */
    public void debug(String s) {
    }

    /* Access modifiers changed, original: protected */
    public Class classForName(String s) throws ClassNotFoundException {
        return Class.forName(s);
    }

    /* Access modifiers changed, original: protected */
    public boolean versionOk(String ver) {
        return Engine.VERSION.equals(ver);
    }

    public void store(LuaTable table) throws IOException {
        DataOutputStream out = null;
        if (this.saveFile.exists()) {
            this.saveFile.truncate(0);
        } else {
            this.saveFile.create();
        }
        try {
            Engine.log("STOR: storing game", 1);
            out = this.saveFile.openDataOutputStream();
            out.writeUTF(SIGNATURE);
            out.writeUTF(Engine.VERSION);
            resetObjectStore();
            storeValue(Engine.instance.cartridge, out);
            storeValue(table, out);
            Engine.log("STOR: store successful", 1);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void resetObjectStore() {
        this.objectStore = new Hashtable(256);
        this.currentId = 0;
        this.level = 0;
    }

    public void restore(LuaTable table) throws IOException {
        DataInputStream dis = this.saveFile.openDataInputStream();
        if (SIGNATURE.equals(dis.readUTF())) {
            try {
                if (versionOk(dis.readUTF())) {
                    try {
                        resetObjectStore();
                        Engine.instance.cartridge = (Cartridge) restoreValue(dis, null);
                        restoreValue(dis, table);
                        dis.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new IOException("Problem loading game: " + e.getMessage());
                    } catch (Throwable th) {
                        dis.close();
                    }
                } else {
                    throw new IOException("Savegame is for different version.");
                }
            } catch (UTFDataFormatException e2) {
                throw new IOException("Savegame is for different version.");
            }
        }
        throw new IOException("Invalid savegame file: bad signature.");
    }

    public void buildJavafuncMap(LuaTable environment) {
        LuaTable[] packages = new LuaTable[]{environment, (LuaTable) environment.rawget(BaseLib.TYPE_STRING), (LuaTable) environment.rawget("math"), (LuaTable) environment.rawget("coroutine"), (LuaTable) environment.rawget("os"), (LuaTable) environment.rawget(BaseLib.TYPE_TABLE)};
        for (LuaTable table : packages) {
            Object next = null;
            while (true) {
                next = table.next(next);
                if (next == null) {
                    break;
                }
                Object jf = table.rawget(next);
                if (jf instanceof JavaFunction) {
                    addJavafunc((JavaFunction) jf);
                }
            }
        }
    }

    public void addJavafunc(JavaFunction javafunc) {
        int i = this.currentJavafunc;
        this.currentJavafunc = i + 1;
        Integer id = new Integer(i);
        this.idToJavafuncMap.put(id, javafunc);
        this.javafuncToIdMap.put(javafunc, id);
    }

    private int findJavafuncId(JavaFunction javafunc) {
        Integer id = (Integer) this.javafuncToIdMap.get(javafunc);
        if (id != null) {
            return id.intValue();
        }
        throw new RuntimeException("javafunc not found in map!");
    }

    private JavaFunction findJavafuncObject(int id) {
        return (JavaFunction) this.idToJavafuncMap.get(new Integer(id));
    }

    private void storeObject(Object obj, DataOutputStream out) throws IOException {
        if (obj == null) {
            out.writeByte(0);
            return;
        }
        Integer i = (Integer) this.objectStore.get(obj);
        if (i != null) {
            out.writeByte(7);
            if (this.debug) {
                debug("reference " + i.intValue() + " (" + obj.toString() + ")");
            }
            out.writeInt(i.intValue());
            return;
        }
        int i2 = this.currentId;
        this.currentId = i2 + 1;
        i = new Integer(i2);
        this.objectStore.put(obj, i);
        if (this.debug) {
            debug("(ref" + i.intValue() + ")");
        }
        if (obj instanceof Serializable) {
            out.writeByte(6);
            out.writeUTF(obj.getClass().getName());
            if (this.debug) {
                debug(obj.getClass().getName() + " (" + obj.toString() + ")");
            }
            ((Serializable) obj).serialize(out);
        } else if (obj instanceof LuaTable) {
            out.writeByte(4);
            if (this.debug) {
                debug("table(" + obj.toString() + "):\n");
            }
            serializeLuaTable((LuaTable) obj, out);
        } else if (obj instanceof LuaClosure) {
            out.writeByte(5);
            if (this.debug) {
                debug("closure(" + obj.toString() + ")");
            }
            serializeLuaClosure((LuaClosure) obj, out);
        } else {
            out.writeByte(0);
            if (this.debug) {
                debug("UFO");
            }
            Engine.log("STOR: unable to store object of type " + obj.getClass().getName(), 2);
        }
    }

    public void storeValue(Object obj, DataOutputStream out) throws IOException {
        if (obj == null) {
            if (this.debug) {
                debug(BaseLib.TYPE_NIL);
            }
            out.writeByte(0);
        } else if (obj instanceof String) {
            out.writeByte(2);
            if (this.debug) {
                debug("\"" + obj.toString() + "\"");
            }
            out.writeUTF((String) obj);
        } else if (obj instanceof Boolean) {
            if (this.debug) {
                debug(obj.toString());
            }
            out.writeByte(3);
            out.writeBoolean(((Boolean) obj).booleanValue());
        } else if (obj instanceof Double) {
            out.writeByte(1);
            if (this.debug) {
                debug(obj.toString());
            }
            out.writeDouble(((Double) obj).doubleValue());
        } else if (obj instanceof JavaFunction) {
            int i = findJavafuncId((JavaFunction) obj);
            if (this.debug) {
                debug("javafunc(" + i + ")-" + obj.toString());
            }
            out.writeByte(8);
            out.writeInt(i);
        } else {
            storeObject(obj, out);
        }
    }

    public void serializeLuaTable(LuaTable table, DataOutputStream out) throws IOException {
        this.level++;
        Object next = null;
        while (true) {
            next = table.next(next);
            if (next != null) {
                Object value = table.rawget(next);
                out.writeByte(16);
                if (this.debug) {
                    for (int i = 0; i < this.level; i++) {
                        debug("  ");
                    }
                }
                storeValue(next, out);
                if (this.debug) {
                    debug(" : ");
                }
                storeValue(value, out);
                if (this.debug) {
                    debug("\n");
                }
            } else {
                this.level--;
                out.writeByte(17);
                return;
            }
        }
    }

    public Object restoreValue(DataInputStream in, Object target) throws IOException {
        byte type = in.readByte();
        switch (type) {
            case (byte) 0:
                if (this.debug) {
                    debug(BaseLib.TYPE_NIL);
                }
                return null;
            case (byte) 1:
                double d = in.readDouble();
                if (this.debug) {
                    debug(String.valueOf(d));
                }
                return LuaState.toDouble(d);
            case (byte) 2:
                Object s = in.readUTF();
                if (!this.debug) {
                    return s;
                }
                debug("\"" + s + "\"");
                return s;
            case (byte) 3:
                boolean b = in.readBoolean();
                if (this.debug) {
                    debug(String.valueOf(b));
                }
                return LuaState.toBoolean(b);
            case (byte) 8:
                int i = in.readInt();
                JavaFunction jf = findJavafuncObject(i);
                if (this.debug) {
                    debug("javafunc(" + i + ")-" + jf);
                }
                return jf;
            default:
                return restoreObject(in, type, target);
        }
    }

    private void restCache(Object o) {
        int i = this.currentId;
        this.currentId = i + 1;
        Integer i2 = new Integer(i);
        this.objectStore.put(i2, o);
        if (this.debug) {
            debug("(ref" + i2.intValue() + ")");
        }
    }

    private Object restoreObject(DataInputStream in, byte type, Object target) throws IOException {
        switch (type) {
            case (byte) 4:
                LuaTable lti;
                if (target instanceof LuaTable) {
                    lti = (LuaTable) target;
                } else {
                    lti = new LuaTableImpl();
                }
                restCache(lti);
                if (this.debug) {
                    debug("table:\n");
                }
                return deserializeLuaTable(in, lti);
            case (byte) 5:
                if (this.debug) {
                    debug("closure: ");
                }
                Object lc = deserializeLuaClosure(in);
                if (!this.debug) {
                    return lc;
                }
                debug(lc.toString());
                return lc;
            case (byte) 6:
                String cls = in.readUTF();
                Serializable s = null;
                try {
                    if (this.debug) {
                        debug("object of type " + cls + "...\n");
                    }
                    Class c = classForName(cls);
                    if (Serializable.class.isAssignableFrom(c)) {
                        s = (Serializable) c.newInstance();
                    }
                } catch (Throwable e) {
                    if (this.debug) {
                        debug("(failed to deserialize " + cls + ")\n");
                    }
                    Engine.log("REST: while trying to deserialize " + cls + ":\n" + e.toString(), 3);
                }
                if (s != null) {
                    restCache(s);
                    s.deserialize(in);
                }
                return s;
            case (byte) 7:
                Integer what = new Integer(in.readInt());
                if (this.debug) {
                    debug("reference " + what.intValue());
                }
                Object result = this.objectStore.get(what);
                if (result == null) {
                    Engine.log("REST: not found reference " + what.toString() + " in object store", 2);
                    if (this.debug) {
                        debug(" (which happens to be null?)");
                    }
                    return target;
                }
                if (this.debug) {
                    debug(" : " + result.toString());
                }
                return result;
            default:
                Engine.log("REST: found unknown type " + type, 2);
                if (this.debug) {
                    debug("UFO");
                }
                return null;
        }
    }

    public LuaTable deserializeLuaTable(DataInputStream in, LuaTable table) throws IOException {
        this.level++;
        while (in.readByte() != LUATABLE_END) {
            if (this.debug) {
                for (int i = 0; i < this.level; i++) {
                    debug("  ");
                }
            }
            Object key = restoreValue(in, null);
            if (this.debug) {
                debug(" : ");
            }
            Object value = restoreValue(in, table.rawget(key));
            if (this.debug) {
                debug("\n");
            }
            table.rawset(key, value);
        }
        this.level--;
        return table;
    }

    private void serializeLuaClosure(LuaClosure closure, DataOutputStream out) throws IOException {
        closure.prototype.dump(out);
        for (UpValue u : closure.upvalues) {
            if (u.value == null) {
                Engine.log("STOR: unclosed upvalue in " + closure.toString(), 2);
                u.value = u.thread.objectStack[u.index];
            }
            storeValue(u.value, out);
        }
    }

    private LuaClosure deserializeLuaClosure(DataInputStream in) throws IOException {
        LuaClosure closure = LuaPrototype.loadByteCode(in, Engine.state.getEnvironment());
        restCache(closure);
        for (int i = 0; i < closure.upvalues.length; i++) {
            UpValue u = new UpValue();
            u.value = restoreValue(in, null);
            closure.upvalues[i] = u;
        }
        return closure;
    }
}
