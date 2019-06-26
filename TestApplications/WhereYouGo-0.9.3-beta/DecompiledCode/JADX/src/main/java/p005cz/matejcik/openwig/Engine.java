package p005cz.matejcik.openwig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import p005cz.matejcik.openwig.formats.CartridgeFile;
import p005cz.matejcik.openwig.formats.Savegame;
import p005cz.matejcik.openwig.platform.C0237UI;
import p005cz.matejcik.openwig.platform.LocationService;
import p009se.krka.kahlua.p010vm.LuaClosure;
import p009se.krka.kahlua.p010vm.LuaPrototype;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import util.BackgroundRunner;

/* renamed from: cz.matejcik.openwig.Engine */
public class Engine implements Runnable {
    public static final int LOG_CALL = 1;
    public static final int LOG_ERROR = 3;
    public static final int LOG_PROP = 0;
    public static final int LOG_WARN = 2;
    public static final String VERSION = "428";
    public static LocationService gps;
    public static Engine instance;
    public static LuaState state;
    /* renamed from: ui */
    public static C0237UI f39ui;
    public Cartridge cartridge;
    private boolean doRestore = false;
    private boolean end = false;
    protected BackgroundRunner eventRunner;
    public CartridgeFile gwcfile;
    private PrintStream log;
    private int loglevel = 2;
    public Player player = new Player();
    private Runnable refresh = new C02354();
    private boolean refreshScheduled = false;
    public Savegame savegame = null;
    private Runnable store = new C02365();
    private Thread thread = null;

    /* renamed from: cz.matejcik.openwig.Engine$1 */
    class C02321 implements Runnable {
        C02321() {
        }

        public void run() {
            Engine.f39ui.refresh();
        }
    }

    /* renamed from: cz.matejcik.openwig.Engine$4 */
    class C02354 implements Runnable {
        C02354() {
        }

        public void run() {
            synchronized (Engine.instance) {
                Engine.f39ui.refresh();
                Engine.this.refreshScheduled = false;
            }
        }
    }

    /* renamed from: cz.matejcik.openwig.Engine$5 */
    class C02365 implements Runnable {
        C02365() {
        }

        public void run() {
            try {
                Engine.f39ui.blockForSaving();
                Engine.this.savegame.store(Engine.state.getEnvironment());
            } catch (IOException e) {
                Engine.log("STOR: save failed: " + e.toString(), 2);
                Engine.f39ui.showError("Sync failed.\n" + e.getMessage());
            } finally {
                Engine.f39ui.unblock();
            }
        }
    }

    public static Engine newInstance(CartridgeFile cf, OutputStream log, C0237UI ui, LocationService service) throws IOException {
        ui.debugMsg("Creating engine...\n");
        f39ui = ui;
        gps = service;
        instance = new Engine(cf, log);
        return instance;
    }

    protected Engine(CartridgeFile cf, OutputStream out) throws IOException {
        this.gwcfile = cf;
        this.savegame = cf.getSavegame();
        if (out != null) {
            this.log = new PrintStream(out);
        }
        if (this.gwcfile != null && this.gwcfile.device != null) {
            WherigoLib.env.put("Device", this.gwcfile.device);
        }
    }

    protected Engine() {
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void restore() {
        this.doRestore = true;
        start();
    }

    /* Access modifiers changed, original: protected */
    public void prepareState() throws IOException {
        f39ui.debugMsg("Creating state...\n");
        state = new LuaState(System.out);
        f39ui.debugMsg("Building javafunc map...\n");
        this.savegame.buildJavafuncMap(state.getEnvironment());
        f39ui.debugMsg("Loading stdlib...");
        InputStream stdlib = getClass().getResourceAsStream("/cz/matejcik/openwig/stdlib.lbc");
        LuaClosure closure = LuaPrototype.loadByteCode(stdlib, state.getEnvironment());
        f39ui.debugMsg("calling...\n");
        state.call(closure, null, null, null);
        stdlib.close();
        f39ui.debugMsg("Registering WIG libs...\n");
        WherigoLib.register(state);
        f39ui.debugMsg("Building event queue...\n");
        this.eventRunner = new BackgroundRunner(true);
        this.eventRunner.setQueueListener(new C02321());
    }

    private void restoreGame() throws IOException {
        f39ui.debugMsg("Restoring saved state...");
        this.cartridge = new Cartridge();
        this.savegame.restore(state.getEnvironment());
    }

    private void newGame() throws IOException {
        f39ui.debugMsg("Loading gwc...");
        if (this.gwcfile == null) {
            throw new IOException("invalid cartridge file");
        }
        f39ui.debugMsg("pre-setting properties...");
        this.player.rawset("CompletionCode", this.gwcfile.code);
        this.player.rawset("Name", this.gwcfile.member);
        f39ui.debugMsg("loading code...");
        byte[] lbc = this.gwcfile.getBytecode();
        f39ui.debugMsg("parsing...");
        LuaClosure closure = LuaPrototype.loadByteCode(new ByteArrayInputStream(lbc), state.getEnvironment());
        f39ui.debugMsg("calling...\n");
        state.call(closure, null, null, null);
    }

    private void mainloop() {
        while (!this.end) {
            try {
                if (!(gps.getLatitude() == this.player.position.latitude && gps.getLongitude() == this.player.position.longitude && gps.getAltitude() == this.player.position.altitude)) {
                    this.player.refreshLocation();
                }
                this.cartridge.tick();
            } catch (Exception e) {
                Engine.stacktrace(e);
            } catch (Throwable t) {
                try {
                    f39ui.end();
                    Engine.stacktrace(t);
                    return;
                } finally {
                    instance = null;
                    state = null;
                    if (this.eventRunner != null) {
                        this.eventRunner.kill();
                    }
                    this.eventRunner = null;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
            }
        }
        if (this.log != null) {
            this.log.close();
        }
        instance = null;
        state = null;
        if (this.eventRunner != null) {
            this.eventRunner.kill();
        }
        this.eventRunner = null;
    }

    public void run() {
        try {
            if (this.log != null) {
                this.log.println("-------------------\ncartridge " + this.gwcfile.name + " started (openWIG r" + VERSION + ")\n-------------------");
            }
            prepareState();
            if (this.doRestore) {
                restoreGame();
            } else {
                newGame();
            }
            this.loglevel = 0;
            f39ui.debugMsg("Starting game...\n");
            f39ui.start();
            this.player.refreshLocation();
            this.cartridge.callEvent(this.doRestore ? "OnRestore" : "OnStart", null);
            f39ui.refresh();
            this.eventRunner.unpause();
            mainloop();
        } catch (IOException e) {
            f39ui.showError("Could not load cartridge: " + e.getMessage());
        } catch (Throwable t) {
            Engine.stacktrace(t);
        } finally {
            f39ui.end();
        }
    }

    public static void stacktrace(Throwable e) {
        String msg;
        e.printStackTrace();
        if (state != null) {
            System.out.println(state.currentThread.stackTrace);
            msg = e.toString() + "\n\nstack trace: " + state.currentThread.stackTrace;
        } else {
            msg = e.toString();
        }
        Engine.log(msg, 3);
        f39ui.showError("you hit a bug! please report at openwig.googlecode.com and i'll fix it for you!\n" + msg);
    }

    public static void kill() {
        if (instance != null) {
            Timer.kill();
            instance.end = true;
        }
    }

    public static void message(LuaTable message) {
        String[] texts = new String[]{Engine.removeHtml((String) message.rawget("Text"))};
        Engine.log("CALL: MessageBox - " + texts[0].substring(0, Math.min(100, texts[0].length())), 1);
        Media[] media = new Media[]{(Media) message.rawget("Media")};
        String button1 = null;
        String button2 = null;
        LuaTable buttons = (LuaTable) message.rawget("Buttons");
        if (buttons != null) {
            button1 = (String) buttons.rawget(new Double(1.0d));
            button2 = (String) buttons.rawget(new Double(2.0d));
        }
        f39ui.pushDialog(texts, media, button1, button2, (LuaClosure) message.rawget("Callback"));
    }

    public static void dialog(String[] texts, Media[] media) {
        if (texts.length > 0) {
            Engine.log("CALL: Dialog - " + texts[0].substring(0, Math.min(100, texts[0].length())), 1);
        }
        f39ui.pushDialog(texts, media, null, null, null);
    }

    public static void input(EventTable input) {
        Engine.log("CALL: GetInput - " + input.name, 1);
        f39ui.pushInput(input);
    }

    public static void callEvent(final EventTable subject, final String name, final Object param) {
        if (subject.hasEvent(name)) {
            instance.eventRunner.perform(new Runnable() {
                public void run() {
                    subject.callEvent(name, param);
                }
            });
        }
    }

    public static void invokeCallback(final LuaClosure callback, final Object value) {
        instance.eventRunner.perform(new Runnable() {
            public void run() {
                try {
                    Engine.log("BTTN: " + (value == null ? "(cancel)" : value.toString()) + " pressed", 1);
                    Engine.state.call(callback, value, null, null);
                    Engine.log("BTTN END", 1);
                } catch (Throwable t) {
                    Engine.stacktrace(t);
                    Engine.log("BTTN FAIL", 1);
                }
            }
        });
    }

    public static byte[] mediaFile(Media media) throws IOException {
        return instance.gwcfile.getFile(media.f98id);
    }

    public static void log(String s, int level) {
        if (instance != null && instance.log != null && level >= instance.loglevel) {
            synchronized (instance.log) {
                Calendar now = Calendar.getInstance();
                instance.log.print(now.get(11));
                instance.log.print(':');
                instance.log.print(now.get(12));
                instance.log.print(':');
                instance.log.print(now.get(13));
                instance.log.print('|');
                instance.log.print(((double) ((int) ((gps.getLatitude() * 10000.0d) + 0.5d))) / 10000.0d);
                instance.log.print('|');
                instance.log.print(((double) ((int) ((gps.getLongitude() * 10000.0d) + 0.5d))) / 10000.0d);
                instance.log.print('|');
                instance.log.print(gps.getAltitude());
                instance.log.print('|');
                instance.log.print(gps.getPrecision());
                instance.log.print("|:: ");
                instance.log.println(s);
                instance.log.flush();
            }
        }
    }

    private static void replace(String source, String pattern, String replace, StringBuffer builder) {
        int pos = 0;
        int pl = pattern.length();
        builder.delete(0, builder.length());
        while (pos < source.length()) {
            int np = source.indexOf(pattern, pos);
            if (np == -1) {
                break;
            }
            builder.append(source.substring(pos, np));
            builder.append(replace);
            pos = np + pl;
        }
        builder.append(source.substring(pos));
    }

    public static String removeHtml(String s) {
        if (s == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(s.length());
        Engine.replace(s, "<BR>", "\n", sb);
        Engine.replace(sb.toString(), "&nbsp;", " ", sb);
        Engine.replace(sb.toString(), "&lt;", "<", sb);
        Engine.replace(sb.toString(), "&gt;", ">", sb);
        Engine.replace(sb.toString(), "&amp;", "&", sb);
        return sb.toString();
    }

    public static void refreshUI() {
        synchronized (instance) {
            if (!instance.refreshScheduled) {
                instance.refreshScheduled = true;
                instance.eventRunner.perform(instance.refresh);
            }
        }
    }

    public void store() {
        this.store.run();
    }

    public static void requestSync() {
        instance.eventRunner.perform(instance.store);
    }
}
