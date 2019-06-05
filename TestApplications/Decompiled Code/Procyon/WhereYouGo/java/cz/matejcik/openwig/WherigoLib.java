// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import java.util.Enumeration;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.LuaCallFrame;
import java.io.Serializable;
import java.util.Hashtable;
import se.krka.kahlua.vm.JavaFunction;

public class WherigoLib implements JavaFunction
{
    private static final int AUDIO = 14;
    private static final int CARTRIDGE = 3;
    private static final int COMMAND = 0;
    public static final String DEVICE_ID = "DeviceID";
    private static final int DIALOG = 6;
    private static final int DISTANCE = 2;
    private static final int GETINPUT = 15;
    private static final int GETVALUE = 23;
    private static final int LOGMESSAGE = 21;
    private static final int MADE = 22;
    private static final int MESSAGEBOX = 4;
    private static final int NOCASEEQUALS = 16;
    private static final int NUM_FUNCTIONS = 24;
    public static final String PLATFORM = "Platform";
    private static final int SHOWSCREEN = 17;
    private static final int SHOWSTATUSTEXT = 19;
    private static final int TRANSLATEPOINT = 18;
    private static final int VECTORTOPOINT = 20;
    private static final int ZCHARACTER = 7;
    private static final int ZCOMMAND = 9;
    private static final int ZINPUT = 11;
    private static final int ZITEM = 8;
    private static final int ZMEDIA = 10;
    private static final int ZONE = 5;
    private static final int ZONEPOINT = 1;
    private static final int ZTASK = 13;
    private static final int ZTIMER = 12;
    public static final Hashtable env;
    private static WherigoLib[] functions;
    private static final String[] names;
    private int index;
    private Class klass;
    
    static {
        (names = new String[24])[1] = "ZonePoint";
        WherigoLib.names[2] = "Distance";
        WherigoLib.names[3] = "ZCartridge";
        WherigoLib.names[4] = "MessageBox";
        WherigoLib.names[5] = "Zone";
        WherigoLib.names[6] = "Dialog";
        WherigoLib.names[7] = "ZCharacter";
        WherigoLib.names[8] = "ZItem";
        WherigoLib.names[9] = "ZCommand";
        WherigoLib.names[10] = "ZMedia";
        WherigoLib.names[11] = "ZInput";
        WherigoLib.names[12] = "ZTimer";
        WherigoLib.names[13] = "ZTask";
        WherigoLib.names[14] = "PlayAudio";
        WherigoLib.names[15] = "GetInput";
        WherigoLib.names[16] = "NoCaseEquals";
        WherigoLib.names[17] = "ShowScreen";
        WherigoLib.names[18] = "TranslatePoint";
        WherigoLib.names[19] = "ShowStatusText";
        WherigoLib.names[20] = "VectorToPoint";
        WherigoLib.names[0] = "Command";
        WherigoLib.names[21] = "LogMessage";
        WherigoLib.names[22] = "made";
        WherigoLib.names[23] = "GetValue";
        (env = new Hashtable()).put("Device", "undefined");
        WherigoLib.env.put("DeviceID", "undefined");
        WherigoLib.env.put("Platform", "MIDP-2.0/CLDC-1.1");
        WherigoLib.env.put("CartFolder", "c:/what/is/it/to/you");
        WherigoLib.env.put("SyncFolder", "c:/what/is/it/to/you");
        WherigoLib.env.put("LogFolder", "c:/what/is/it/to/you");
        WherigoLib.env.put("CartFilename", "cartridge.gwc");
        WherigoLib.env.put("PathSep", "/");
        WherigoLib.env.put("Version", "2.11-compatible(r428)");
        WherigoLib.env.put("Downloaded", new Double(0.0));
        WherigoLib.functions = new WherigoLib[24];
        for (int i = 0; i < 24; ++i) {
            WherigoLib.functions[i] = new WherigoLib(i);
        }
    }
    
    public WherigoLib(final int index) {
        this.index = index;
        this.klass = this.assignClass();
    }
    
    private Class assignClass() {
        Serializable class1 = null;
        switch (this.index) {
            default: {
                class1 = this.getClass();
                break;
            }
            case 2: {
                class1 = Double.class;
                break;
            }
            case 1: {
                class1 = ZonePoint.class;
                break;
            }
            case 5: {
                class1 = Zone.class;
                break;
            }
            case 7:
            case 8: {
                class1 = Thing.class;
                break;
            }
            case 9: {
                class1 = Action.class;
                break;
            }
            case 10: {
                class1 = Media.class;
                break;
            }
            case 11: {
                class1 = EventTable.class;
                break;
            }
            case 12: {
                class1 = Timer.class;
                break;
            }
            case 13: {
                class1 = Task.class;
                break;
            }
            case 3: {
                class1 = Cartridge.class;
                break;
            }
        }
        return (Class)class1;
    }
    
    private int command(final LuaCallFrame luaCallFrame, final int n) {
        boolean b = true;
        if (n < 1) {
            b = false;
        }
        BaseLib.luaAssert(b, "insufficient arguments for Command");
        final String s = (String)luaCallFrame.get(0);
        String s2;
        if ((s2 = s) != null) {
            s2 = s;
            if (s.length() == 0) {
                s2 = null;
            }
        }
        Engine.ui.command(s2);
        return 0;
    }
    
    private int construct(final EventTable eventTable, final LuaCallFrame luaCallFrame, final int n) {
        final Object value = luaCallFrame.get(0);
        Cartridge cartridge = null;
        if (value instanceof Cartridge) {
            cartridge = (Cartridge)value;
        }
        else if (value instanceof LuaTable) {
            final Cartridge cartridge2 = (Cartridge)value;
            final Cartridge cartridge3 = (Cartridge)cartridge2.rawget("Cartridge");
            eventTable.setTable((LuaTable)value);
            cartridge = cartridge3;
            if (eventTable instanceof Container) {
                final Container container = (Container)eventTable;
                final Container container2 = (Container)cartridge2.rawget("Container");
                cartridge = cartridge3;
                if (container2 != null) {
                    container.moveTo(container2);
                    cartridge = cartridge3;
                }
            }
        }
        Cartridge cartridge4;
        if ((cartridge4 = cartridge) == null) {
            cartridge4 = Engine.instance.cartridge;
        }
        cartridge4.addObject(eventTable);
        return luaCallFrame.push(eventTable);
    }
    
    private int dialog(final LuaCallFrame luaCallFrame, int i) {
        final LuaTable luaTable = (LuaTable)luaCallFrame.get(0);
        final int len = luaTable.len();
        final String[] array = new String[len];
        final Media[] array2 = new Media[len];
        LuaTable luaTable2;
        for (i = 1; i <= len; ++i) {
            luaTable2 = (LuaTable)luaTable.rawget(new Double(i));
            array[i - 1] = Engine.removeHtml((String)luaTable2.rawget("Text"));
            array2[i - 1] = (Media)luaTable2.rawget("Media");
        }
        Engine.dialog(array, array2);
        return 0;
    }
    
    private int distance(final LuaCallFrame luaCallFrame, final int n) {
        luaCallFrame.push(LuaState.toDouble(ZonePoint.convertDistanceFrom(LuaState.fromDouble(luaCallFrame.get(0)), (String)luaCallFrame.get(1))));
        return 1;
    }
    
    private int distanceGetValue(final LuaCallFrame luaCallFrame, final int n) {
        luaCallFrame.push(LuaState.toDouble(ZonePoint.convertDistanceTo(LuaState.fromDouble(luaCallFrame.get(0)), (String)luaCallFrame.get(1))));
        return 1;
    }
    
    private int getinput(final LuaCallFrame luaCallFrame, final int n) {
        Engine.input((EventTable)luaCallFrame.get(0));
        return 1;
    }
    
    private int logMessage(final LuaCallFrame luaCallFrame, final int n) {
        if (n >= 1) {
            final Object value = luaCallFrame.get(0);
            String string;
            if (value instanceof LuaTable) {
                string = (String)((LuaTable)value).rawget("Text");
            }
            else {
                string = value.toString();
            }
            if (string == null || string.length() != 0) {
                Engine.log("CUST: " + string, 1);
            }
        }
        return 0;
    }
    
    private int made(final LuaCallFrame luaCallFrame, int push) {
        final boolean b = true;
        Label_0063: {
            if (push < 2) {
                break Label_0063;
            }
            boolean b2 = true;
            while (true) {
                BaseLib.luaAssert(b2, "insufficient arguments for object:made");
                try {
                    push = luaCallFrame.push(LuaState.toBoolean(((WherigoLib)luaCallFrame.get(0)).klass == luaCallFrame.get(1).getClass() && b));
                    return push;
                    b2 = false;
                }
                catch (ClassCastException ex) {
                    throw new RuntimeException("bad arguments to object:made");
                }
            }
        }
    }
    
    private int messageBox(final LuaCallFrame luaCallFrame, final int n) {
        Engine.message((LuaTable)luaCallFrame.get(0));
        return 0;
    }
    
    private int nocaseequals(final LuaCallFrame luaCallFrame, final int n) {
        String string = null;
        final boolean b = false;
        final Object value = luaCallFrame.get(0);
        final Object value2 = luaCallFrame.get(1);
        String string2;
        if (value == null) {
            string2 = null;
        }
        else {
            string2 = value.toString();
        }
        if (value2 != null) {
            string = value2.toString();
        }
        boolean b2 = false;
        Label_0063: {
            if (string2 != string) {
                b2 = b;
                if (string2 == null) {
                    break Label_0063;
                }
                b2 = b;
                if (!string2.equalsIgnoreCase(string)) {
                    break Label_0063;
                }
            }
            b2 = true;
        }
        luaCallFrame.push(LuaState.toBoolean(b2));
        return 1;
    }
    
    private int playAudio(final LuaCallFrame luaCallFrame, final int n) {
        ((Media)luaCallFrame.get(0)).play();
        return 0;
    }
    
    public static void register(final LuaState luaState) {
        if (WherigoLib.env.get("DeviceID") == null) {
            throw new RuntimeException("set your DeviceID! WherigoLib.env.put(WherigoLib.DEVICE_ID, \"some value\")");
        }
        final LuaTable environment = luaState.getEnvironment();
        final LuaTableImpl luaTableImpl = new LuaTableImpl();
        environment.rawset("Wherigo", luaTableImpl);
        for (int i = 0; i < 24; ++i) {
            Engine.instance.savegame.addJavafunc(WherigoLib.functions[i]);
            luaTableImpl.rawset(WherigoLib.names[i], WherigoLib.functions[i]);
        }
        final LuaTableImpl luaTableImpl2 = new LuaTableImpl();
        luaTableImpl2.rawset("__index", luaTableImpl2);
        luaTableImpl2.rawset("__call", WherigoLib.functions[23]);
        luaTableImpl2.rawset(WherigoLib.names[23], WherigoLib.functions[23]);
        luaState.setClassMetatable(Double.class, luaTableImpl2);
        luaState.setClassMetatable(WherigoLib.class, luaTableImpl);
        luaTableImpl.rawset("__index", luaTableImpl);
        luaTableImpl.rawset("Player", Engine.instance.player);
        luaTableImpl.rawset("INVALID_ZONEPOINT", null);
        luaTableImpl.rawset("MAINSCREEN", new Double(0.0));
        luaTableImpl.rawset("DETAILSCREEN", new Double(1.0));
        luaTableImpl.rawset("ITEMSCREEN", new Double(3.0));
        luaTableImpl.rawset("INVENTORYSCREEN", new Double(2.0));
        luaTableImpl.rawset("LOCATIONSCREEN", new Double(4.0));
        luaTableImpl.rawset("TASKSCREEN", new Double(5.0));
        ((LuaTable)((LuaTable)environment.rawget("package")).rawget("loaded")).rawset("Wherigo", luaTableImpl);
        final LuaTableImpl luaTableImpl3 = new LuaTableImpl();
        final Enumeration<String> keys = WherigoLib.env.keys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            luaTableImpl3.rawset(key, WherigoLib.env.get(key));
        }
        luaTableImpl3.rawset("Device", Engine.instance.gwcfile.device);
        environment.rawset("Env", luaTableImpl3);
        Cartridge.register();
        Container.register();
        Player.register();
        Timer.register();
        Media.reset();
    }
    
    private int showStatusText(final LuaCallFrame luaCallFrame, final int n) {
        boolean b = true;
        if (n < 1) {
            b = false;
        }
        BaseLib.luaAssert(b, "insufficient arguments for ShowStatusText");
        final String s = (String)luaCallFrame.get(0);
        String statusText;
        if ((statusText = s) != null) {
            statusText = s;
            if (s.length() == 0) {
                statusText = null;
            }
        }
        Engine.ui.setStatusText(statusText);
        return 0;
    }
    
    private int showscreen(final LuaCallFrame luaCallFrame, final int n) {
        final int i = (int)LuaState.fromDouble(luaCallFrame.get(0));
        EventTable eventTable2;
        final EventTable eventTable = eventTable2 = null;
        if (n > 1) {
            final Object value = luaCallFrame.get(1);
            eventTable2 = eventTable;
            if (value instanceof EventTable) {
                eventTable2 = (EventTable)value;
            }
        }
        final StringBuilder append = new StringBuilder().append("CALL: ShowScreen(").append(i).append(") ");
        String name;
        if (eventTable2 == null) {
            name = "";
        }
        else {
            name = eventTable2.name;
        }
        Engine.log(append.append(name).toString(), 1);
        Engine.ui.showScreen(i, eventTable2);
        return 0;
    }
    
    private int translatePoint(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 3, "insufficient arguments for TranslatePoint");
        luaCallFrame.push(((ZonePoint)luaCallFrame.get(0)).translate(LuaState.fromDouble(luaCallFrame.get(2)), LuaState.fromDouble(luaCallFrame.get(1))));
        return 1;
    }
    
    private int vectorToPoint(final LuaCallFrame luaCallFrame, final int n) {
        BaseLib.luaAssert(n >= 2, "insufficient arguments for VectorToPoint");
        final ZonePoint zonePoint = (ZonePoint)luaCallFrame.get(0);
        final ZonePoint zonePoint2 = (ZonePoint)luaCallFrame.get(1);
        final double angle2azimuth = ZonePoint.angle2azimuth(zonePoint2.bearing(zonePoint));
        luaCallFrame.push(LuaState.toDouble(zonePoint2.distance(zonePoint)));
        luaCallFrame.push(LuaState.toDouble(angle2azimuth));
        return 2;
    }
    
    private int zonePoint(final LuaCallFrame luaCallFrame, final int n) {
        if (n == 0) {
            luaCallFrame.push(new ZonePoint());
        }
        else {
            BaseLib.luaAssert(n >= 2, "insufficient arguments for ZonePoint");
            final double fromDouble = LuaState.fromDouble(luaCallFrame.get(0));
            final double fromDouble2 = LuaState.fromDouble(luaCallFrame.get(1));
            double fromDouble3 = 0.0;
            if (n > 2) {
                fromDouble3 = LuaState.fromDouble(luaCallFrame.get(2));
            }
            luaCallFrame.push(new ZonePoint(fromDouble, fromDouble2, fromDouble3));
        }
        return 1;
    }
    
    @Override
    public int call(final LuaCallFrame luaCallFrame, int n) {
        switch (this.index) {
            default: {
                n = 0;
                break;
            }
            case 22: {
                n = this.made(luaCallFrame, n);
                break;
            }
            case 1: {
                n = this.zonePoint(luaCallFrame, n);
                break;
            }
            case 2: {
                n = this.distance(luaCallFrame, n);
                break;
            }
            case 8: {
                n = this.construct(new Thing(false), luaCallFrame, n);
                break;
            }
            case 7: {
                n = this.construct(new Thing(true), luaCallFrame, n);
                break;
            }
            case 3: {
                final Engine instance = Engine.instance;
                final Cartridge cartridge = new Cartridge();
                instance.cartridge = cartridge;
                n = this.construct(cartridge, luaCallFrame, n);
                break;
            }
            case 5:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13: {
                try {
                    n = this.construct(this.klass.newInstance(), luaCallFrame, n);
                }
                catch (InstantiationException ex) {
                    n = 0;
                }
                catch (IllegalAccessException ex2) {
                    n = 0;
                }
                break;
            }
            case 4: {
                n = this.messageBox(luaCallFrame, n);
                break;
            }
            case 6: {
                n = this.dialog(luaCallFrame, n);
                break;
            }
            case 16: {
                n = this.nocaseequals(luaCallFrame, n);
                break;
            }
            case 15: {
                n = this.getinput(luaCallFrame, n);
                break;
            }
            case 17: {
                n = this.showscreen(luaCallFrame, n);
                break;
            }
            case 18: {
                n = this.translatePoint(luaCallFrame, n);
                break;
            }
            case 14: {
                n = this.playAudio(luaCallFrame, n);
                break;
            }
            case 20: {
                n = this.vectorToPoint(luaCallFrame, n);
                break;
            }
            case 0: {
                n = this.command(luaCallFrame, n);
                break;
            }
            case 19: {
                n = this.showStatusText(luaCallFrame, n);
                break;
            }
            case 21: {
                n = this.logMessage(luaCallFrame, n);
                break;
            }
            case 23: {
                n = this.distanceGetValue(luaCallFrame, n);
                break;
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        return WherigoLib.names[this.index];
    }
}
