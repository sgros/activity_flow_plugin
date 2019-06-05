package p005cz.matejcik.openwig;

import java.util.Enumeration;
import java.util.Hashtable;
import p009se.krka.kahlua.p010vm.JavaFunction;
import p009se.krka.kahlua.p010vm.LuaCallFrame;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.p010vm.LuaTableImpl;
import p009se.krka.kahlua.stdlib.BaseLib;

/* renamed from: cz.matejcik.openwig.WherigoLib */
public class WherigoLib implements JavaFunction {
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
    public static final Hashtable env = new Hashtable();
    private static WherigoLib[] functions = new WherigoLib[24];
    private static final String[] names = new String[24];
    private int index;
    private Class klass = assignClass();

    static {
        names[1] = "ZonePoint";
        names[2] = "Distance";
        names[3] = "ZCartridge";
        names[4] = "MessageBox";
        names[5] = "Zone";
        names[6] = "Dialog";
        names[7] = "ZCharacter";
        names[8] = "ZItem";
        names[9] = "ZCommand";
        names[10] = "ZMedia";
        names[11] = "ZInput";
        names[12] = "ZTimer";
        names[13] = "ZTask";
        names[14] = "PlayAudio";
        names[15] = "GetInput";
        names[16] = "NoCaseEquals";
        names[17] = "ShowScreen";
        names[18] = "TranslatePoint";
        names[19] = "ShowStatusText";
        names[20] = "VectorToPoint";
        names[0] = "Command";
        names[21] = "LogMessage";
        names[22] = "made";
        names[23] = "GetValue";
        env.put("Device", "undefined");
        env.put(DEVICE_ID, "undefined");
        env.put(PLATFORM, "MIDP-2.0/CLDC-1.1");
        env.put("CartFolder", "c:/what/is/it/to/you");
        env.put("SyncFolder", "c:/what/is/it/to/you");
        env.put("LogFolder", "c:/what/is/it/to/you");
        env.put("CartFilename", "cartridge.gwc");
        env.put("PathSep", "/");
        env.put("Version", "2.11-compatible(r428)");
        env.put("Downloaded", new Double(0.0d));
        for (int i = 0; i < 24; i++) {
            functions[i] = new WherigoLib(i);
        }
    }

    private Class assignClass() {
        switch (this.index) {
            case 1:
                return ZonePoint.class;
            case 2:
                return Double.class;
            case 3:
                return Cartridge.class;
            case 5:
                return Zone.class;
            case 7:
            case 8:
                return Thing.class;
            case 9:
                return Action.class;
            case 10:
                return Media.class;
            case 11:
                return EventTable.class;
            case 12:
                return Timer.class;
            case 13:
                return Task.class;
            default:
                return getClass();
        }
    }

    public WherigoLib(int index) {
        this.index = index;
    }

    public static void register(LuaState state) {
        if (env.get(DEVICE_ID) == null) {
            throw new RuntimeException("set your DeviceID! WherigoLib.env.put(WherigoLib.DEVICE_ID, \"some value\")");
        }
        LuaTable environment = state.getEnvironment();
        LuaTable wig = new LuaTableImpl();
        environment.rawset("Wherigo", wig);
        for (int i = 0; i < 24; i++) {
            Engine.instance.savegame.addJavafunc(functions[i]);
            wig.rawset(names[i], functions[i]);
        }
        LuaTable distanceMetatable = new LuaTableImpl();
        distanceMetatable.rawset("__index", distanceMetatable);
        distanceMetatable.rawset("__call", functions[23]);
        distanceMetatable.rawset(names[23], functions[23]);
        state.setClassMetatable(Double.class, distanceMetatable);
        state.setClassMetatable(WherigoLib.class, wig);
        wig.rawset("__index", wig);
        wig.rawset("Player", Engine.instance.player);
        wig.rawset("INVALID_ZONEPOINT", null);
        wig.rawset("MAINSCREEN", new Double(0.0d));
        wig.rawset("DETAILSCREEN", new Double(1.0d));
        wig.rawset("ITEMSCREEN", new Double(3.0d));
        wig.rawset("INVENTORYSCREEN", new Double(2.0d));
        wig.rawset("LOCATIONSCREEN", new Double(4.0d));
        wig.rawset("TASKSCREEN", new Double(5.0d));
        ((LuaTable) ((LuaTable) environment.rawget("package")).rawget("loaded")).rawset("Wherigo", wig);
        LuaTable envtable = new LuaTableImpl();
        Enumeration e = env.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            envtable.rawset(key, env.get(key));
        }
        envtable.rawset("Device", Engine.instance.gwcfile.device);
        environment.rawset("Env", envtable);
        Cartridge.register();
        Container.register();
        Player.register();
        Timer.register();
        Media.reset();
    }

    public String toString() {
        return names[this.index];
    }

    public int call(LuaCallFrame callFrame, int nArguments) {
        switch (this.index) {
            case 0:
                return command(callFrame, nArguments);
            case 1:
                return zonePoint(callFrame, nArguments);
            case 2:
                return distance(callFrame, nArguments);
            case 3:
                Engine engine = Engine.instance;
                Cartridge cartridge = new Cartridge();
                engine.cartridge = cartridge;
                return construct(cartridge, callFrame, nArguments);
            case 4:
                return messageBox(callFrame, nArguments);
            case 5:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                try {
                    return construct((EventTable) this.klass.newInstance(), callFrame, nArguments);
                } catch (InstantiationException e) {
                    return 0;
                } catch (IllegalAccessException e2) {
                    return 0;
                }
            case 6:
                return dialog(callFrame, nArguments);
            case 7:
                return construct(new Thing(true), callFrame, nArguments);
            case 8:
                return construct(new Thing(false), callFrame, nArguments);
            case 14:
                return playAudio(callFrame, nArguments);
            case 15:
                return getinput(callFrame, nArguments);
            case 16:
                return nocaseequals(callFrame, nArguments);
            case 17:
                return showscreen(callFrame, nArguments);
            case 18:
                return translatePoint(callFrame, nArguments);
            case 19:
                return showStatusText(callFrame, nArguments);
            case 20:
                return vectorToPoint(callFrame, nArguments);
            case 21:
                return logMessage(callFrame, nArguments);
            case 22:
                return made(callFrame, nArguments);
            case 23:
                return distanceGetValue(callFrame, nArguments);
            default:
                return 0;
        }
    }

    private int made(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        boolean z2 = true;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "insufficient arguments for object:made");
        try {
            if (((WherigoLib) callFrame.get(0)).klass != callFrame.get(1).getClass()) {
                z2 = false;
            }
            return callFrame.push(LuaState.toBoolean(z2));
        } catch (ClassCastException e) {
            throw new RuntimeException("bad arguments to object:made");
        }
    }

    private int construct(EventTable what, LuaCallFrame callFrame, int nArguments) {
        Cartridge param = callFrame.get(0);
        Cartridge c = null;
        if (param instanceof Cartridge) {
            c = param;
        } else if (param instanceof LuaTable) {
            LuaTable lt = param;
            c = (Cartridge) lt.rawget("Cartridge");
            what.setTable(param);
            if (what instanceof Container) {
                Container cont = (Container) what;
                Container target = (Container) lt.rawget("Container");
                if (target != null) {
                    cont.moveTo(target);
                }
            }
        }
        if (c == null) {
            c = Engine.instance.cartridge;
        }
        c.addObject(what);
        return callFrame.push(what);
    }

    private int zonePoint(LuaCallFrame callFrame, int nArguments) {
        if (nArguments == 0) {
            callFrame.push(new ZonePoint());
        } else {
            boolean z;
            if (nArguments >= 2) {
                z = true;
            } else {
                z = false;
            }
            BaseLib.luaAssert(z, "insufficient arguments for ZonePoint");
            double a = LuaState.fromDouble(callFrame.get(0));
            double b = LuaState.fromDouble(callFrame.get(1));
            double c = 0.0d;
            if (nArguments > 2) {
                c = LuaState.fromDouble(callFrame.get(2));
            }
            callFrame.push(new ZonePoint(a, b, c));
        }
        return 1;
    }

    private int distance(LuaCallFrame callFrame, int nArguments) {
        callFrame.push(LuaState.toDouble(ZonePoint.convertDistanceFrom(LuaState.fromDouble(callFrame.get(0)), (String) callFrame.get(1))));
        return 1;
    }

    private int distanceGetValue(LuaCallFrame callFrame, int nArguments) {
        callFrame.push(LuaState.toDouble(ZonePoint.convertDistanceTo(LuaState.fromDouble(callFrame.get(0)), (String) callFrame.get(1))));
        return 1;
    }

    private int messageBox(LuaCallFrame callFrame, int nArguments) {
        Engine.message((LuaTable) callFrame.get(0));
        return 0;
    }

    private int dialog(LuaCallFrame callFrame, int nArguments) {
        LuaTable lt = (LuaTable) callFrame.get(0);
        int n = lt.len();
        String[] texts = new String[n];
        Media[] media = new Media[n];
        for (int i = 1; i <= n; i++) {
            LuaTable item = (LuaTable) lt.rawget(new Double((double) i));
            texts[i - 1] = Engine.removeHtml((String) item.rawget("Text"));
            media[i - 1] = (Media) item.rawget("Media");
        }
        Engine.dialog(texts, media);
        return 0;
    }

    private int nocaseequals(LuaCallFrame callFrame, int nArguments) {
        String bb = null;
        boolean result = false;
        Object a = callFrame.get(0);
        Object b = callFrame.get(1);
        String aa = a == null ? null : a.toString();
        if (b != null) {
            bb = b.toString();
        }
        if (aa == bb || (aa != null && aa.equalsIgnoreCase(bb))) {
            result = true;
        }
        callFrame.push(LuaState.toBoolean(result));
        return 1;
    }

    private int getinput(LuaCallFrame callFrame, int nArguments) {
        Engine.input((EventTable) callFrame.get(0));
        return 1;
    }

    private int showscreen(LuaCallFrame callFrame, int nArguments) {
        int screen = (int) LuaState.fromDouble(callFrame.get(0));
        EventTable et = null;
        if (nArguments > 1) {
            EventTable o = callFrame.get(1);
            if (o instanceof EventTable) {
                et = o;
            }
        }
        Engine.log("CALL: ShowScreen(" + screen + ") " + (et == null ? "" : et.name), 1);
        Engine.f39ui.showScreen(screen, et);
        return 0;
    }

    private int translatePoint(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 3) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "insufficient arguments for TranslatePoint");
        callFrame.push(((ZonePoint) callFrame.get(0)).translate(LuaState.fromDouble(callFrame.get(2)), LuaState.fromDouble(callFrame.get(1))));
        return 1;
    }

    private int vectorToPoint(LuaCallFrame callFrame, int nArguments) {
        boolean z;
        if (nArguments >= 2) {
            z = true;
        } else {
            z = false;
        }
        BaseLib.luaAssert(z, "insufficient arguments for VectorToPoint");
        ZonePoint a = (ZonePoint) callFrame.get(0);
        ZonePoint b = (ZonePoint) callFrame.get(1);
        double bearing = ZonePoint.angle2azimuth(b.bearing(a));
        callFrame.push(LuaState.toDouble(b.distance(a)));
        callFrame.push(LuaState.toDouble(bearing));
        return 2;
    }

    private int playAudio(LuaCallFrame callFrame, int nArguments) {
        ((Media) callFrame.get(0)).play();
        return 0;
    }

    private int showStatusText(LuaCallFrame callFrame, int nArguments) {
        boolean z = true;
        if (nArguments < 1) {
            z = false;
        }
        BaseLib.luaAssert(z, "insufficient arguments for ShowStatusText");
        String text = (String) callFrame.get(0);
        if (text != null && text.length() == 0) {
            text = null;
        }
        Engine.f39ui.setStatusText(text);
        return 0;
    }

    private int logMessage(LuaCallFrame callFrame, int nArguments) {
        if (nArguments >= 1) {
            String text;
            LuaTable arg = callFrame.get(0);
            if (arg instanceof LuaTable) {
                text = (String) arg.rawget("Text");
            } else {
                text = arg.toString();
            }
            if (text == null || text.length() != 0) {
                Engine.log("CUST: " + text, 1);
            }
        }
        return 0;
    }

    private int command(LuaCallFrame callFrame, int nArguments) {
        boolean z = true;
        if (nArguments < 1) {
            z = false;
        }
        BaseLib.luaAssert(z, "insufficient arguments for Command");
        String cmd = (String) callFrame.get(0);
        if (cmd != null && cmd.length() == 0) {
            cmd = null;
        }
        Engine.f39ui.command(cmd);
        return 0;
    }
}
