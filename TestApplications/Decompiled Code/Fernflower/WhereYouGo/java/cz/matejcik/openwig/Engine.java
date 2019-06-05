package cz.matejcik.openwig;

import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.formats.Savegame;
import cz.matejcik.openwig.platform.LocationService;
import cz.matejcik.openwig.platform.UI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaPrototype;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import util.BackgroundRunner;

public class Engine implements Runnable {
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
   private boolean doRestore = false;
   private boolean end = false;
   protected BackgroundRunner eventRunner;
   public CartridgeFile gwcfile;
   private PrintStream log;
   private int loglevel = 2;
   public Player player = new Player();
   private Runnable refresh = new Runnable() {
      public void run() {
         // $FF: Couldn't be decompiled
      }
   };
   private boolean refreshScheduled = false;
   public Savegame savegame = null;
   private Runnable store = new Runnable() {
      public void run() {
         try {
            Engine.ui.blockForSaving();
            Engine.this.savegame.store(Engine.state.getEnvironment());
         } catch (IOException var6) {
            StringBuilder var2 = new StringBuilder();
            Engine.log(var2.append("STOR: save failed: ").append(var6.toString()).toString(), 2);
            UI var8 = Engine.ui;
            StringBuilder var3 = new StringBuilder();
            var8.showError(var3.append("Sync failed.\n").append(var6.getMessage()).toString());
         } finally {
            Engine.ui.unblock();
         }

      }
   };
   private Thread thread = null;

   protected Engine() {
   }

   protected Engine(CartridgeFile var1, OutputStream var2) throws IOException {
      this.gwcfile = var1;
      this.savegame = var1.getSavegame();
      if (var2 != null) {
         this.log = new PrintStream(var2);
      }

      if (this.gwcfile != null && this.gwcfile.device != null) {
         WherigoLib.env.put("Device", this.gwcfile.device);
      }

   }

   // $FF: synthetic method
   static boolean access$002(Engine var0, boolean var1) {
      var0.refreshScheduled = var1;
      return var1;
   }

   public static void callEvent(final EventTable var0, final String var1, final Object var2) {
      if (var0.hasEvent(var1)) {
         instance.eventRunner.perform(new Runnable() {
            public void run() {
               var0.callEvent(var1, var2);
            }
         });
      }

   }

   public static void dialog(String[] var0, Media[] var1) {
      if (var0.length > 0) {
         log("CALL: Dialog - " + var0[0].substring(0, Math.min(100, var0[0].length())), 1);
      }

      ui.pushDialog(var0, var1, (String)null, (String)null, (LuaClosure)null);
   }

   public static void input(EventTable var0) {
      log("CALL: GetInput - " + var0.name, 1);
      ui.pushInput(var0);
   }

   public static void invokeCallback(final LuaClosure var0, final Object var1) {
      instance.eventRunner.perform(new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      });
   }

   public static void kill() {
      if (instance != null) {
         Timer.kill();
         instance.end = true;
      }

   }

   public static void log(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   private void mainloop() {
      // $FF: Couldn't be decompiled
   }

   public static byte[] mediaFile(Media var0) throws IOException {
      return instance.gwcfile.getFile(var0.id);
   }

   public static void message(LuaTable var0) {
      String[] var1 = new String[]{removeHtml((String)var0.rawget("Text"))};
      log("CALL: MessageBox - " + var1[0].substring(0, Math.min(100, var1[0].length())), 1);
      Media var2 = (Media)var0.rawget("Media");
      String var3 = null;
      String var4 = null;
      LuaTable var5 = (LuaTable)var0.rawget("Buttons");
      if (var5 != null) {
         var3 = (String)var5.rawget(new Double(1.0D));
         var4 = (String)var5.rawget(new Double(2.0D));
      }

      LuaClosure var6 = (LuaClosure)var0.rawget("Callback");
      ui.pushDialog(var1, new Media[]{var2}, var3, var4, var6);
   }

   private void newGame() throws IOException {
      ui.debugMsg("Loading gwc...");
      if (this.gwcfile == null) {
         throw new IOException("invalid cartridge file");
      } else {
         ui.debugMsg("pre-setting properties...");
         this.player.rawset("CompletionCode", this.gwcfile.code);
         this.player.rawset("Name", this.gwcfile.member);
         ui.debugMsg("loading code...");
         byte[] var1 = this.gwcfile.getBytecode();
         ui.debugMsg("parsing...");
         LuaClosure var2 = LuaPrototype.loadByteCode((InputStream)(new ByteArrayInputStream(var1)), state.getEnvironment());
         ui.debugMsg("calling...\n");
         state.call(var2, (Object)null, (Object)null, (Object)null);
      }
   }

   public static Engine newInstance(CartridgeFile var0, OutputStream var1, UI var2, LocationService var3) throws IOException {
      var2.debugMsg("Creating engine...\n");
      ui = var2;
      gps = var3;
      instance = new Engine(var0, var1);
      return instance;
   }

   public static void refreshUI() {
      Engine var0 = instance;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!instance.refreshScheduled) {
               instance.refreshScheduled = true;
               instance.eventRunner.perform(instance.refresh);
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static String removeHtml(String var0) {
      if (var0 == null) {
         var0 = "";
      } else {
         StringBuffer var1 = new StringBuffer(var0.length());
         replace(var0, "<BR>", "\n", var1);
         replace(var1.toString(), "&nbsp;", " ", var1);
         replace(var1.toString(), "&lt;", "<", var1);
         replace(var1.toString(), "&gt;", ">", var1);
         replace(var1.toString(), "&amp;", "&", var1);
         var0 = var1.toString();
      }

      return var0;
   }

   private static void replace(String var0, String var1, String var2, StringBuffer var3) {
      int var4 = 0;
      int var5 = var1.length();
      var3.delete(0, var3.length());

      while(var4 < var0.length()) {
         int var6 = var0.indexOf(var1, var4);
         if (var6 == -1) {
            break;
         }

         var3.append(var0.substring(var4, var6));
         var3.append(var2);
         var4 = var6 + var5;
      }

      var3.append(var0.substring(var4));
   }

   public static void requestSync() {
      instance.eventRunner.perform(instance.store);
   }

   private void restoreGame() throws IOException {
      ui.debugMsg("Restoring saved state...");
      this.cartridge = new Cartridge();
      this.savegame.restore(state.getEnvironment());
   }

   public static void stacktrace(Throwable var0) {
      var0.printStackTrace();
      String var1;
      if (state != null) {
         System.out.println(state.currentThread.stackTrace);
         var1 = var0.toString() + "\n\nstack trace: " + state.currentThread.stackTrace;
      } else {
         var1 = var0.toString();
      }

      log(var1, 3);
      ui.showError("you hit a bug! please report at openwig.googlecode.com and i'll fix it for you!\n" + var1);
   }

   protected void prepareState() throws IOException {
      ui.debugMsg("Creating state...\n");
      state = new LuaState(System.out);
      ui.debugMsg("Building javafunc map...\n");
      this.savegame.buildJavafuncMap(state.getEnvironment());
      ui.debugMsg("Loading stdlib...");
      InputStream var1 = this.getClass().getResourceAsStream("/cz/matejcik/openwig/stdlib.lbc");
      LuaClosure var2 = LuaPrototype.loadByteCode(var1, state.getEnvironment());
      ui.debugMsg("calling...\n");
      state.call(var2, (Object)null, (Object)null, (Object)null);
      var1.close();
      ui.debugMsg("Registering WIG libs...\n");
      WherigoLib.register(state);
      ui.debugMsg("Building event queue...\n");
      this.eventRunner = new BackgroundRunner(true);
      this.eventRunner.setQueueListener(new Runnable() {
         public void run() {
            Engine.ui.refresh();
         }
      });
   }

   public void restore() {
      this.doRestore = true;
      this.start();
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void start() {
      this.thread = new Thread(this);
      this.thread.start();
   }

   public void store() {
      this.store.run();
   }
}
