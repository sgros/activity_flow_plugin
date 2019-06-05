// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import java.io.InputStream;
import se.krka.kahlua.vm.LuaPrototype;
import java.io.ByteArrayInputStream;
import se.krka.kahlua.vm.LuaTable;
import java.util.Calendar;
import se.krka.kahlua.vm.LuaClosure;
import java.io.OutputStream;
import java.io.IOException;
import cz.matejcik.openwig.formats.Savegame;
import java.io.PrintStream;
import cz.matejcik.openwig.formats.CartridgeFile;
import util.BackgroundRunner;
import cz.matejcik.openwig.platform.UI;
import se.krka.kahlua.vm.LuaState;
import cz.matejcik.openwig.platform.LocationService;

public class Engine implements Runnable
{
    public static final int LOG_CALL = 1;
    public static final int LOG_ERROR = 3;
    public static final int LOG_PROP = 0;
    public static final int LOG_WARN = 2;
    public static final String VERSION = "428";
    public static LocationService gps;
    public static Engine instance;
    public static LuaState state;
    public static UI ui;
    public Cartridge cartridge;
    private boolean doRestore;
    private boolean end;
    protected BackgroundRunner eventRunner;
    public CartridgeFile gwcfile;
    private PrintStream log;
    private int loglevel;
    public Player player;
    private Runnable refresh;
    private boolean refreshScheduled;
    public Savegame savegame;
    private Runnable store;
    private Thread thread;
    
    protected Engine() {
        this.savegame = null;
        this.player = new Player();
        this.doRestore = false;
        this.end = false;
        this.loglevel = 2;
        this.thread = null;
        this.refresh = new Runnable() {
            @Override
            public void run() {
                synchronized (Engine.instance) {
                    Engine.ui.refresh();
                    Engine.this.refreshScheduled = false;
                }
            }
        };
        this.refreshScheduled = false;
        this.store = new Runnable() {
            @Override
            public void run() {
                try {
                    Engine.ui.blockForSaving();
                    Engine.this.savegame.store(Engine.state.getEnvironment());
                }
                catch (IOException ex) {
                    Engine.log("STOR: save failed: " + ex.toString(), 2);
                    Engine.ui.showError("Sync failed.\n" + ex.getMessage());
                    Engine.ui.unblock();
                }
                finally {
                    Engine.ui.unblock();
                }
            }
        };
    }
    
    protected Engine(final CartridgeFile gwcfile, final OutputStream out) throws IOException {
        this.savegame = null;
        this.player = new Player();
        this.doRestore = false;
        this.end = false;
        this.loglevel = 2;
        this.thread = null;
        this.refresh = new Runnable() {
            @Override
            public void run() {
                synchronized (Engine.instance) {
                    Engine.ui.refresh();
                    Engine.this.refreshScheduled = false;
                }
            }
        };
        this.refreshScheduled = false;
        this.store = new Runnable() {
            @Override
            public void run() {
                try {
                    Engine.ui.blockForSaving();
                    Engine.this.savegame.store(Engine.state.getEnvironment());
                }
                catch (IOException ex) {
                    Engine.log("STOR: save failed: " + ex.toString(), 2);
                    Engine.ui.showError("Sync failed.\n" + ex.getMessage());
                    Engine.ui.unblock();
                }
                finally {
                    Engine.ui.unblock();
                }
            }
        };
        this.gwcfile = gwcfile;
        this.savegame = gwcfile.getSavegame();
        if (out != null) {
            this.log = new PrintStream(out);
        }
        if (this.gwcfile != null && this.gwcfile.device != null) {
            WherigoLib.env.put("Device", this.gwcfile.device);
        }
    }
    
    public static void callEvent(final EventTable eventTable, final String s, final Object o) {
        if (eventTable.hasEvent(s)) {
            Engine.instance.eventRunner.perform(new Runnable() {
                @Override
                public void run() {
                    eventTable.callEvent(s, o);
                }
            });
        }
    }
    
    public static void dialog(final String[] array, final Media[] array2) {
        if (array.length > 0) {
            log("CALL: Dialog - " + array[0].substring(0, Math.min(100, array[0].length())), 1);
        }
        Engine.ui.pushDialog(array, array2, null, null, null);
    }
    
    public static void input(final EventTable eventTable) {
        log("CALL: GetInput - " + eventTable.name, 1);
        Engine.ui.pushInput(eventTable);
    }
    
    public static void invokeCallback(final LuaClosure luaClosure, final Object o) {
        Engine.instance.eventRunner.perform(new Runnable() {
            @Override
            public void run() {
                try {
                    final StringBuilder append = new StringBuilder().append("BTTN: ");
                    String string;
                    if (o == null) {
                        string = "(cancel)";
                    }
                    else {
                        string = o.toString();
                    }
                    Engine.log(append.append(string).append(" pressed").toString(), 1);
                    Engine.state.call(luaClosure, o, null, null);
                    Engine.log("BTTN END", 1);
                }
                catch (Throwable t) {
                    Engine.stacktrace(t);
                    Engine.log("BTTN FAIL", 1);
                }
            }
        });
    }
    
    public static void kill() {
        if (Engine.instance != null) {
            Timer.kill();
            Engine.instance.end = true;
        }
    }
    
    public static void log(final String x, final int n) {
        if (Engine.instance != null && Engine.instance.log != null && n >= Engine.instance.loglevel) {
            synchronized (Engine.instance.log) {
                final Calendar instance = Calendar.getInstance();
                Engine.instance.log.print(instance.get(11));
                Engine.instance.log.print(':');
                Engine.instance.log.print(instance.get(12));
                Engine.instance.log.print(':');
                Engine.instance.log.print(instance.get(13));
                Engine.instance.log.print('|');
                Engine.instance.log.print((int)(Engine.gps.getLatitude() * 10000.0 + 0.5) / 10000.0);
                Engine.instance.log.print('|');
                Engine.instance.log.print((int)(Engine.gps.getLongitude() * 10000.0 + 0.5) / 10000.0);
                Engine.instance.log.print('|');
                Engine.instance.log.print(Engine.gps.getAltitude());
                Engine.instance.log.print('|');
                Engine.instance.log.print(Engine.gps.getPrecision());
                Engine.instance.log.print("|:: ");
                Engine.instance.log.println(x);
                Engine.instance.log.flush();
            }
        }
    }
    
    private void mainloop() {
        Label_0151: {
            try {
                while (!this.end) {
                    try {
                        if (Engine.gps.getLatitude() != this.player.position.latitude || Engine.gps.getLongitude() != this.player.position.longitude || Engine.gps.getAltitude() != this.player.position.altitude) {
                            this.player.refreshLocation();
                        }
                        this.cartridge.tick();
                        try {
                            Thread.sleep(1000L);
                            continue;
                        }
                        catch (InterruptedException ex2) {
                            continue;
                        }
                    }
                    catch (Exception ex) {
                        stacktrace(ex);
                    }
                }
                break Label_0151;
            }
            catch (Throwable t) {
                Engine.ui.end();
                stacktrace(t);
                return;
            Label_0165_Outer:
                while (true) {
                    this.eventRunner = null;
                    return;
                Block_11:
                    while (true) {
                        Engine.instance = null;
                        Engine.state = null;
                        break Block_11;
                        this.log.close();
                        continue;
                    }
                    this.eventRunner.kill();
                    continue Label_0165_Outer;
                }
            }
            // iftrue(Label_0187:, this.eventRunner == null)
            // iftrue(Label_0165:, this.log == null)
            finally {
                Engine.instance = null;
                Engine.state = null;
                if (this.eventRunner != null) {
                    this.eventRunner.kill();
                }
                this.eventRunner = null;
            }
        }
    }
    
    public static byte[] mediaFile(final Media media) throws IOException {
        return Engine.instance.gwcfile.getFile(media.id);
    }
    
    public static void message(final LuaTable luaTable) {
        final String[] array = { removeHtml((String)luaTable.rawget("Text")) };
        log("CALL: MessageBox - " + array[0].substring(0, Math.min(100, array[0].length())), 1);
        final Media media = (Media)luaTable.rawget("Media");
        String s = null;
        String s2 = null;
        final LuaTable luaTable2 = (LuaTable)luaTable.rawget("Buttons");
        if (luaTable2 != null) {
            s = (String)luaTable2.rawget(new Double(1.0));
            s2 = (String)luaTable2.rawget(new Double(2.0));
        }
        Engine.ui.pushDialog(array, new Media[] { media }, s, s2, (LuaClosure)luaTable.rawget("Callback"));
    }
    
    private void newGame() throws IOException {
        Engine.ui.debugMsg("Loading gwc...");
        if (this.gwcfile == null) {
            throw new IOException("invalid cartridge file");
        }
        Engine.ui.debugMsg("pre-setting properties...");
        this.player.rawset("CompletionCode", this.gwcfile.code);
        this.player.rawset("Name", this.gwcfile.member);
        Engine.ui.debugMsg("loading code...");
        final byte[] bytecode = this.gwcfile.getBytecode();
        Engine.ui.debugMsg("parsing...");
        final LuaClosure loadByteCode = LuaPrototype.loadByteCode(new ByteArrayInputStream(bytecode), Engine.state.getEnvironment());
        Engine.ui.debugMsg("calling...\n");
        Engine.state.call(loadByteCode, null, null, null);
    }
    
    public static Engine newInstance(final CartridgeFile cartridgeFile, final OutputStream outputStream, final UI ui, final LocationService gps) throws IOException {
        ui.debugMsg("Creating engine...\n");
        Engine.ui = ui;
        Engine.gps = gps;
        return Engine.instance = new Engine(cartridgeFile, outputStream);
    }
    
    public static void refreshUI() {
        synchronized (Engine.instance) {
            if (!Engine.instance.refreshScheduled) {
                Engine.instance.refreshScheduled = true;
                Engine.instance.eventRunner.perform(Engine.instance.refresh);
            }
        }
    }
    
    public static String removeHtml(String string) {
        if (string == null) {
            string = "";
        }
        else {
            final StringBuffer sb = new StringBuffer(string.length());
            replace(string, "<BR>", "\n", sb);
            replace(sb.toString(), "&nbsp;", " ", sb);
            replace(sb.toString(), "&lt;", "<", sb);
            replace(sb.toString(), "&gt;", ">", sb);
            replace(sb.toString(), "&amp;", "&", sb);
            string = sb.toString();
        }
        return string;
    }
    
    private static void replace(final String s, final String str, final String str2, final StringBuffer sb) {
        int i = 0;
        final int length = str.length();
        sb.delete(0, sb.length());
        while (i < s.length()) {
            final int index = s.indexOf(str, i);
            if (index == -1) {
                break;
            }
            sb.append(s.substring(i, index));
            sb.append(str2);
            i = index + length;
        }
        sb.append(s.substring(i));
    }
    
    public static void requestSync() {
        Engine.instance.eventRunner.perform(Engine.instance.store);
    }
    
    private void restoreGame() throws IOException {
        Engine.ui.debugMsg("Restoring saved state...");
        this.cartridge = new Cartridge();
        this.savegame.restore(Engine.state.getEnvironment());
    }
    
    public static void stacktrace(final Throwable t) {
        t.printStackTrace();
        String str;
        if (Engine.state != null) {
            System.out.println(Engine.state.currentThread.stackTrace);
            str = t.toString() + "\n\nstack trace: " + Engine.state.currentThread.stackTrace;
        }
        else {
            str = t.toString();
        }
        log(str, 3);
        Engine.ui.showError("you hit a bug! please report at openwig.googlecode.com and i'll fix it for you!\n" + str);
    }
    
    protected void prepareState() throws IOException {
        Engine.ui.debugMsg("Creating state...\n");
        Engine.state = new LuaState(System.out);
        Engine.ui.debugMsg("Building javafunc map...\n");
        this.savegame.buildJavafuncMap(Engine.state.getEnvironment());
        Engine.ui.debugMsg("Loading stdlib...");
        final InputStream resourceAsStream = this.getClass().getResourceAsStream("/cz/matejcik/openwig/stdlib.lbc");
        final LuaClosure loadByteCode = LuaPrototype.loadByteCode(resourceAsStream, Engine.state.getEnvironment());
        Engine.ui.debugMsg("calling...\n");
        Engine.state.call(loadByteCode, null, null, null);
        resourceAsStream.close();
        Engine.ui.debugMsg("Registering WIG libs...\n");
        WherigoLib.register(Engine.state);
        Engine.ui.debugMsg("Building event queue...\n");
        (this.eventRunner = new BackgroundRunner(true)).setQueueListener(new Runnable() {
            @Override
            public void run() {
                Engine.ui.refresh();
            }
        });
    }
    
    public void restore() {
        this.doRestore = true;
        this.start();
    }
    
    @Override
    public void run() {
        try {
            if (this.log != null) {
                this.log.println("-------------------\ncartridge " + this.gwcfile.name + " started (openWIG r" + "428" + ")\n-------------------");
            }
            this.prepareState();
            if (this.doRestore) {
                this.restoreGame();
            }
            else {
                this.newGame();
            }
            this.loglevel = 0;
            Engine.ui.debugMsg("Starting game...\n");
            Engine.ui.start();
            this.player.refreshLocation();
            final Cartridge cartridge = this.cartridge;
            if (this.doRestore) {
                cartridge.callEvent("OnRestore", null);
                Engine.ui.refresh();
                this.eventRunner.unpause();
                this.mainloop();
                return;
            }
            goto Label_0211;
        }
        catch (IOException ex) {
            Engine.ui.showError("Could not load cartridge: " + ex.getMessage());
        }
        catch (Throwable t) {
            stacktrace(t);
        }
        finally {
            Engine.ui.end();
        }
    }
    
    public void start() {
        (this.thread = new Thread(this)).start();
    }
    
    public void store() {
        this.store.run();
    }
}
