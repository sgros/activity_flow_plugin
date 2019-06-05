// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig.formats;

import se.krka.kahlua.vm.LuaState;
import cz.matejcik.openwig.Cartridge;
import java.io.UTFDataFormatException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import cz.matejcik.openwig.Serializable;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.JavaFunction;
import java.io.IOException;
import se.krka.kahlua.vm.UpValue;
import se.krka.kahlua.vm.LuaPrototype;
import cz.matejcik.openwig.Engine;
import se.krka.kahlua.vm.LuaClosure;
import java.io.DataInputStream;
import cz.matejcik.openwig.platform.FileHandle;
import java.util.Hashtable;

public class Savegame
{
    private static final byte LUATABLE_END = 17;
    private static final byte LUATABLE_PAIR = 16;
    private static final byte LUA_BOOLEAN = 3;
    private static final byte LUA_CLOSURE = 5;
    private static final byte LUA_DOUBLE = 1;
    private static final byte LUA_JAVAFUNC = 8;
    private static final byte LUA_NIL = 0;
    private static final byte LUA_OBJECT = 6;
    private static final byte LUA_REFERENCE = 7;
    private static final byte LUA_STRING = 2;
    private static final byte LUA_TABLE = 4;
    private static final String SIGNATURE = "openWIG savegame\n";
    private int currentId;
    private int currentJavafunc;
    protected boolean debug;
    private Hashtable idToJavafuncMap;
    private Hashtable javafuncToIdMap;
    int level;
    private Hashtable objectStore;
    private FileHandle saveFile;
    
    protected Savegame() {
        this.debug = false;
        this.idToJavafuncMap = new Hashtable(128);
        this.javafuncToIdMap = new Hashtable(128);
        this.currentJavafunc = 0;
        this.level = 0;
    }
    
    public Savegame(final FileHandle saveFile) {
        this.debug = false;
        this.idToJavafuncMap = new Hashtable(128);
        this.javafuncToIdMap = new Hashtable(128);
        this.currentJavafunc = 0;
        this.level = 0;
        if (saveFile == null) {
            throw new NullPointerException("savefile must not be null");
        }
        this.saveFile = saveFile;
    }
    
    private LuaClosure deserializeLuaClosure(final DataInputStream dataInputStream) throws IOException {
        final LuaClosure loadByteCode = LuaPrototype.loadByteCode(dataInputStream, Engine.state.getEnvironment());
        this.restCache(loadByteCode);
        for (int i = 0; i < loadByteCode.upvalues.length; ++i) {
            final UpValue upValue = new UpValue();
            upValue.value = this.restoreValue(dataInputStream, null);
            loadByteCode.upvalues[i] = upValue;
        }
        return loadByteCode;
    }
    
    private int findJavafuncId(final JavaFunction key) {
        final Integer n = this.javafuncToIdMap.get(key);
        if (n != null) {
            return n;
        }
        throw new RuntimeException("javafunc not found in map!");
    }
    
    private JavaFunction findJavafuncObject(final int value) {
        return this.idToJavafuncMap.get(new Integer(value));
    }
    
    private void restCache(final Object value) {
        final Integer key = new Integer(this.currentId++);
        this.objectStore.put(key, value);
        if (this.debug) {
            this.debug("(ref" + (int)key + ")");
        }
    }
    
    private Object restoreObject(DataInputStream dataInputStream, final byte i, Object o) throws IOException {
        switch (i) {
            default: {
                Engine.log("REST: found unknown type " + i, 2);
                if (this.debug) {
                    this.debug("UFO");
                }
                dataInputStream = null;
                break;
            }
            case 4: {
                LuaTable luaTable;
                if (o instanceof LuaTable) {
                    luaTable = (LuaTable)o;
                }
                else {
                    luaTable = new LuaTableImpl();
                }
                this.restCache(luaTable);
                if (this.debug) {
                    this.debug("table:\n");
                }
                dataInputStream = (DataInputStream)this.deserializeLuaTable(dataInputStream, luaTable);
                break;
            }
            case 5: {
                if (this.debug) {
                    this.debug("closure: ");
                }
                final DataInputStream dataInputStream2 = dataInputStream = (DataInputStream)this.deserializeLuaClosure(dataInputStream);
                if (this.debug) {
                    this.debug(((LuaClosure)dataInputStream2).toString());
                    dataInputStream = dataInputStream2;
                    break;
                }
                break;
            }
            case 6: {
                final String utf = dataInputStream.readUTF();
                final Serializable serializable = null;
                while (true) {
                    try {
                        if (this.debug) {
                            o = new StringBuilder();
                            this.debug(((StringBuilder)o).append("object of type ").append(utf).append("...\n").toString());
                        }
                        final Class classForName = this.classForName(utf);
                        Serializable serializable2 = serializable;
                        if (Serializable.class.isAssignableFrom(classForName)) {
                            serializable2 = (Serializable)classForName.newInstance();
                        }
                        if (serializable2 != null) {
                            this.restCache(serializable2);
                            serializable2.deserialize(dataInputStream);
                        }
                        dataInputStream = (DataInputStream)serializable2;
                        break;
                    }
                    catch (Throwable t) {
                        if (this.debug) {
                            this.debug("(failed to deserialize " + utf + ")\n");
                        }
                        Engine.log("REST: while trying to deserialize " + utf + ":\n" + t.toString(), 3);
                        final Serializable serializable2 = serializable;
                        continue;
                    }
                    break;
                }
            }
            case 7: {
                final Integer key = new Integer(dataInputStream.readInt());
                if (this.debug) {
                    this.debug("reference " + (int)key);
                }
                dataInputStream = (DataInputStream)this.objectStore.get(key);
                if (dataInputStream == null) {
                    Engine.log("REST: not found reference " + key.toString() + " in object store", 2);
                    if (this.debug) {
                        this.debug(" (which happens to be null?)");
                    }
                    dataInputStream = (DataInputStream)o;
                    break;
                }
                if (this.debug) {
                    this.debug(" : " + dataInputStream.toString());
                }
                break;
            }
        }
        return dataInputStream;
    }
    
    private void serializeLuaClosure(final LuaClosure luaClosure, final DataOutputStream dataOutputStream) throws IOException {
        luaClosure.prototype.dump(dataOutputStream);
        for (int i = 0; i < luaClosure.upvalues.length; ++i) {
            final UpValue upValue = luaClosure.upvalues[i];
            if (upValue.value == null) {
                Engine.log("STOR: unclosed upvalue in " + luaClosure.toString(), 2);
                upValue.value = upValue.thread.objectStack[upValue.index];
            }
            this.storeValue(upValue.value, dataOutputStream);
        }
    }
    
    private void storeObject(final Object o, final DataOutputStream dataOutputStream) throws IOException {
        if (o == null) {
            dataOutputStream.writeByte(0);
        }
        else {
            final Integer n = this.objectStore.get(o);
            if (n != null) {
                dataOutputStream.writeByte(7);
                if (this.debug) {
                    this.debug("reference " + (int)n + " (" + o.toString() + ")");
                }
                dataOutputStream.writeInt(n);
            }
            else {
                final Integer value = new Integer(this.currentId++);
                this.objectStore.put(o, value);
                if (this.debug) {
                    this.debug("(ref" + (int)value + ")");
                }
                if (o instanceof Serializable) {
                    dataOutputStream.writeByte(6);
                    dataOutputStream.writeUTF(o.getClass().getName());
                    if (this.debug) {
                        this.debug(o.getClass().getName() + " (" + o.toString() + ")");
                    }
                    ((Serializable)o).serialize(dataOutputStream);
                }
                else if (o instanceof LuaTable) {
                    dataOutputStream.writeByte(4);
                    if (this.debug) {
                        this.debug("table(" + o.toString() + "):\n");
                    }
                    this.serializeLuaTable((LuaTable)o, dataOutputStream);
                }
                else if (o instanceof LuaClosure) {
                    dataOutputStream.writeByte(5);
                    if (this.debug) {
                        this.debug("closure(" + o.toString() + ")");
                    }
                    this.serializeLuaClosure((LuaClosure)o, dataOutputStream);
                }
                else {
                    dataOutputStream.writeByte(0);
                    if (this.debug) {
                        this.debug("UFO");
                    }
                    Engine.log("STOR: unable to store object of type " + o.getClass().getName(), 2);
                }
            }
        }
    }
    
    public void addJavafunc(final JavaFunction javaFunction) {
        final Integer n = new Integer(this.currentJavafunc++);
        this.idToJavafuncMap.put(n, javaFunction);
        this.javafuncToIdMap.put(javaFunction, n);
    }
    
    public void buildJavafuncMap(final LuaTable luaTable) {
        final LuaTable[] array = { luaTable, (LuaTable)luaTable.rawget("string"), (LuaTable)luaTable.rawget("math"), (LuaTable)luaTable.rawget("coroutine"), (LuaTable)luaTable.rawget("os"), (LuaTable)luaTable.rawget("table") };
        for (int i = 0; i < array.length; ++i) {
            final LuaTable luaTable2 = array[i];
            Object o = null;
            while (true) {
                final Object next = luaTable2.next(o);
                if (next == null) {
                    break;
                }
                final Object rawget = luaTable2.rawget(next);
                o = next;
                if (!(rawget instanceof JavaFunction)) {
                    continue;
                }
                this.addJavafunc((JavaFunction)rawget);
                o = next;
            }
        }
    }
    
    protected Class classForName(final String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
    
    protected void debug(final String s) {
    }
    
    public LuaTable deserializeLuaTable(final DataInputStream dataInputStream, final LuaTable luaTable) throws IOException {
        ++this.level;
        while (dataInputStream.readByte() != 17) {
            if (this.debug) {
                for (int i = 0; i < this.level; ++i) {
                    this.debug("  ");
                }
            }
            final Object restoreValue = this.restoreValue(dataInputStream, null);
            if (this.debug) {
                this.debug(" : ");
            }
            final Object restoreValue2 = this.restoreValue(dataInputStream, luaTable.rawget(restoreValue));
            if (this.debug) {
                this.debug("\n");
            }
            luaTable.rawset(restoreValue, restoreValue2);
        }
        --this.level;
        return luaTable;
    }
    
    public boolean exists() throws IOException {
        return this.saveFile.exists();
    }
    
    protected void resetObjectStore() {
        this.objectStore = new Hashtable(256);
        this.currentId = 0;
        this.level = 0;
    }
    
    public void restore(final LuaTable luaTable) throws IOException {
        final DataInputStream openDataInputStream = this.saveFile.openDataInputStream();
        if (!"openWIG savegame\n".equals(openDataInputStream.readUTF())) {
            throw new IOException("Invalid savegame file: bad signature.");
        }
        try {
            if (!this.versionOk(openDataInputStream.readUTF())) {
                throw new IOException("Savegame is for different version.");
            }
        }
        catch (UTFDataFormatException ex2) {
            throw new IOException("Savegame is for different version.");
        }
        try {
            this.resetObjectStore();
            Engine.instance.cartridge = (Cartridge)this.restoreValue(openDataInputStream, null);
            this.restoreValue(openDataInputStream, luaTable);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Problem loading game: " + ex.getMessage());
        }
        finally {
            openDataInputStream.close();
        }
    }
    
    public Object restoreValue(final DataInputStream dataInputStream, final Object o) throws IOException {
        final byte byte1 = dataInputStream.readByte();
        Object obj = null;
        switch (byte1) {
            default: {
                obj = this.restoreObject(dataInputStream, byte1, o);
                break;
            }
            case 0: {
                if (this.debug) {
                    this.debug("nil");
                }
                obj = null;
                break;
            }
            case 1: {
                final double double1 = dataInputStream.readDouble();
                if (this.debug) {
                    this.debug(String.valueOf(double1));
                }
                obj = LuaState.toDouble(double1);
                break;
            }
            case 2: {
                final String str = (String)(obj = dataInputStream.readUTF());
                if (this.debug) {
                    this.debug("\"" + str + "\"");
                    obj = str;
                    break;
                }
                break;
            }
            case 3: {
                final boolean boolean1 = dataInputStream.readBoolean();
                if (this.debug) {
                    this.debug(String.valueOf(boolean1));
                }
                obj = LuaState.toBoolean(boolean1);
                break;
            }
            case 8: {
                final int int1 = dataInputStream.readInt();
                obj = this.findJavafuncObject(int1);
                if (this.debug) {
                    this.debug("javafunc(" + int1 + ")-" + obj);
                }
                break;
            }
        }
        return obj;
    }
    
    public void serializeLuaTable(final LuaTable luaTable, final DataOutputStream dataOutputStream) throws IOException {
        ++this.level;
        Object o = null;
        while (true) {
            final Object next = luaTable.next(o);
            if (next == null) {
                break;
            }
            final Object rawget = luaTable.rawget(next);
            dataOutputStream.writeByte(16);
            if (this.debug) {
                for (int i = 0; i < this.level; ++i) {
                    this.debug("  ");
                }
            }
            this.storeValue(next, dataOutputStream);
            if (this.debug) {
                this.debug(" : ");
            }
            this.storeValue(rawget, dataOutputStream);
            o = next;
            if (!this.debug) {
                continue;
            }
            this.debug("\n");
            o = next;
        }
        --this.level;
        dataOutputStream.writeByte(17);
    }
    
    public void store(final LuaTable p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_0        
        //     3: getfield        cz/matejcik/openwig/formats/Savegame.saveFile:Lcz/matejcik/openwig/platform/FileHandle;
        //     6: invokeinterface cz/matejcik/openwig/platform/FileHandle.exists:()Z
        //    11: ifeq            103
        //    14: aload_0        
        //    15: getfield        cz/matejcik/openwig/formats/Savegame.saveFile:Lcz/matejcik/openwig/platform/FileHandle;
        //    18: lconst_0       
        //    19: invokeinterface cz/matejcik/openwig/platform/FileHandle.truncate:(J)V
        //    24: aload_2        
        //    25: astore_3       
        //    26: ldc_w           "STOR: storing game"
        //    29: iconst_1       
        //    30: invokestatic    cz/matejcik/openwig/Engine.log:(Ljava/lang/String;I)V
        //    33: aload_2        
        //    34: astore_3       
        //    35: aload_0        
        //    36: getfield        cz/matejcik/openwig/formats/Savegame.saveFile:Lcz/matejcik/openwig/platform/FileHandle;
        //    39: invokeinterface cz/matejcik/openwig/platform/FileHandle.openDataOutputStream:()Ljava/io/DataOutputStream;
        //    44: astore_2       
        //    45: aload_2        
        //    46: astore_3       
        //    47: aload_2        
        //    48: ldc             "openWIG savegame\n"
        //    50: invokevirtual   java/io/DataOutputStream.writeUTF:(Ljava/lang/String;)V
        //    53: aload_2        
        //    54: astore_3       
        //    55: aload_2        
        //    56: ldc_w           "428"
        //    59: invokevirtual   java/io/DataOutputStream.writeUTF:(Ljava/lang/String;)V
        //    62: aload_2        
        //    63: astore_3       
        //    64: aload_0        
        //    65: invokevirtual   cz/matejcik/openwig/formats/Savegame.resetObjectStore:()V
        //    68: aload_2        
        //    69: astore_3       
        //    70: aload_0        
        //    71: getstatic       cz/matejcik/openwig/Engine.instance:Lcz/matejcik/openwig/Engine;
        //    74: getfield        cz/matejcik/openwig/Engine.cartridge:Lcz/matejcik/openwig/Cartridge;
        //    77: aload_2        
        //    78: invokevirtual   cz/matejcik/openwig/formats/Savegame.storeValue:(Ljava/lang/Object;Ljava/io/DataOutputStream;)V
        //    81: aload_2        
        //    82: astore_3       
        //    83: aload_0        
        //    84: aload_1        
        //    85: aload_2        
        //    86: invokevirtual   cz/matejcik/openwig/formats/Savegame.storeValue:(Ljava/lang/Object;Ljava/io/DataOutputStream;)V
        //    89: aload_2        
        //    90: astore_3       
        //    91: ldc_w           "STOR: store successful"
        //    94: iconst_1       
        //    95: invokestatic    cz/matejcik/openwig/Engine.log:(Ljava/lang/String;I)V
        //    98: aload_2        
        //    99: invokevirtual   java/io/DataOutputStream.close:()V
        //   102: return         
        //   103: aload_0        
        //   104: getfield        cz/matejcik/openwig/formats/Savegame.saveFile:Lcz/matejcik/openwig/platform/FileHandle;
        //   107: invokeinterface cz/matejcik/openwig/platform/FileHandle.create:()V
        //   112: goto            24
        //   115: astore_1       
        //   116: aload_3        
        //   117: invokevirtual   java/io/DataOutputStream.close:()V
        //   120: aload_1        
        //   121: athrow         
        //   122: astore_1       
        //   123: goto            102
        //   126: astore_3       
        //   127: goto            120
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  26     33     115    122    Any
        //  35     45     115    122    Any
        //  47     53     115    122    Any
        //  55     62     115    122    Any
        //  64     68     115    122    Any
        //  70     81     115    122    Any
        //  83     89     115    122    Any
        //  91     98     115    122    Any
        //  98     102    122    126    Ljava/lang/Exception;
        //  116    120    126    130    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 69 out-of-bounds for length 69
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void storeValue(final Object o, final DataOutputStream dataOutputStream) throws IOException {
        if (o == null) {
            if (this.debug) {
                this.debug("nil");
            }
            dataOutputStream.writeByte(0);
        }
        else if (o instanceof String) {
            dataOutputStream.writeByte(2);
            if (this.debug) {
                this.debug("\"" + o.toString() + "\"");
            }
            dataOutputStream.writeUTF((String)o);
        }
        else if (o instanceof Boolean) {
            if (this.debug) {
                this.debug(o.toString());
            }
            dataOutputStream.writeByte(3);
            dataOutputStream.writeBoolean((boolean)o);
        }
        else if (o instanceof Double) {
            dataOutputStream.writeByte(1);
            if (this.debug) {
                this.debug(o.toString());
            }
            dataOutputStream.writeDouble((double)o);
        }
        else if (o instanceof JavaFunction) {
            final int javafuncId = this.findJavafuncId((JavaFunction)o);
            if (this.debug) {
                this.debug("javafunc(" + javafuncId + ")-" + o.toString());
            }
            dataOutputStream.writeByte(8);
            dataOutputStream.writeInt(javafuncId);
        }
        else {
            this.storeObject(o, dataOutputStream);
        }
    }
    
    protected boolean versionOk(final String anObject) {
        return "428".equals(anObject);
    }
}
