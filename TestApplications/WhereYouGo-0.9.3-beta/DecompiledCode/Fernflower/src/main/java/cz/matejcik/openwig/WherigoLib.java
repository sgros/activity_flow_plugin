package cz.matejcik.openwig;

import java.util.Enumeration;
import java.util.Hashtable;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

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
   public static final Hashtable env;
   private static WherigoLib[] functions;
   private static final String[] names = new String[24];
   private int index;
   private Class klass;

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
      env = new Hashtable();
      env.put("Device", "undefined");
      env.put("DeviceID", "undefined");
      env.put("Platform", "MIDP-2.0/CLDC-1.1");
      env.put("CartFolder", "c:/what/is/it/to/you");
      env.put("SyncFolder", "c:/what/is/it/to/you");
      env.put("LogFolder", "c:/what/is/it/to/you");
      env.put("CartFilename", "cartridge.gwc");
      env.put("PathSep", "/");
      env.put("Version", "2.11-compatible(r428)");
      env.put("Downloaded", new Double(0.0D));
      functions = new WherigoLib[24];

      for(int var0 = 0; var0 < 24; ++var0) {
         functions[var0] = new WherigoLib(var0);
      }

   }

   public WherigoLib(int var1) {
      this.index = var1;
      this.klass = this.assignClass();
   }

   private Class assignClass() {
      Class var1;
      switch(this.index) {
      case 1:
         var1 = ZonePoint.class;
         break;
      case 2:
         var1 = Double.class;
         break;
      case 3:
         var1 = Cartridge.class;
         break;
      case 4:
      case 6:
      default:
         var1 = this.getClass();
         break;
      case 5:
         var1 = Zone.class;
         break;
      case 7:
      case 8:
         var1 = Thing.class;
         break;
      case 9:
         var1 = Action.class;
         break;
      case 10:
         var1 = Media.class;
         break;
      case 11:
         var1 = EventTable.class;
         break;
      case 12:
         var1 = Timer.class;
         break;
      case 13:
         var1 = Task.class;
      }

      return var1;
   }

   private int command(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      if (var2 < 1) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "insufficient arguments for Command");
      String var4 = (String)var1.get(0);
      String var5 = var4;
      if (var4 != null) {
         var5 = var4;
         if (var4.length() == 0) {
            var5 = null;
         }
      }

      Engine.ui.command(var5);
      return 0;
   }

   private int construct(EventTable var1, LuaCallFrame var2, int var3) {
      Object var4 = var2.get(0);
      Cartridge var5 = null;
      Cartridge var6;
      if (var4 instanceof Cartridge) {
         var5 = (Cartridge)var4;
      } else if (var4 instanceof LuaTable) {
         LuaTable var7 = (LuaTable)var4;
         var6 = (Cartridge)var7.rawget("Cartridge");
         var1.setTable((LuaTable)var4);
         var5 = var6;
         if (var1 instanceof Container) {
            Container var8 = (Container)var1;
            Container var9 = (Container)var7.rawget("Container");
            var5 = var6;
            if (var9 != null) {
               var8.moveTo(var9);
               var5 = var6;
            }
         }
      }

      var6 = var5;
      if (var5 == null) {
         var6 = Engine.instance.cartridge;
      }

      var6.addObject(var1);
      return var2.push(var1);
   }

   private int dialog(LuaCallFrame var1, int var2) {
      LuaTable var7 = (LuaTable)var1.get(0);
      int var3 = var7.len();
      String[] var4 = new String[var3];
      Media[] var5 = new Media[var3];

      for(var2 = 1; var2 <= var3; ++var2) {
         LuaTable var6 = (LuaTable)var7.rawget(new Double((double)var2));
         var4[var2 - 1] = Engine.removeHtml((String)var6.rawget("Text"));
         var5[var2 - 1] = (Media)var6.rawget("Media");
      }

      Engine.dialog(var4, var5);
      return 0;
   }

   private int distance(LuaCallFrame var1, int var2) {
      var1.push(LuaState.toDouble(ZonePoint.convertDistanceFrom(LuaState.fromDouble(var1.get(0)), (String)var1.get(1))));
      return 1;
   }

   private int distanceGetValue(LuaCallFrame var1, int var2) {
      var1.push(LuaState.toDouble(ZonePoint.convertDistanceTo(LuaState.fromDouble(var1.get(0)), (String)var1.get(1))));
      return 1;
   }

   private int getinput(LuaCallFrame var1, int var2) {
      Engine.input((EventTable)var1.get(0));
      return 1;
   }

   private int logMessage(LuaCallFrame var1, int var2) {
      if (var2 >= 1) {
         Object var3 = var1.get(0);
         String var4;
         if (var3 instanceof LuaTable) {
            var4 = (String)((LuaTable)var3).rawget("Text");
         } else {
            var4 = var3.toString();
         }

         if (var4 == null || var4.length() != 0) {
            Engine.log("CUST: " + var4, 1);
         }
      }

      return 0;
   }

   private int made(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      boolean var4;
      if (var2 >= 2) {
         var4 = true;
      } else {
         var4 = false;
      }

      BaseLib.luaAssert(var4, "insufficient arguments for object:made");

      boolean var10001;
      label30: {
         label29: {
            try {
               WherigoLib var5 = (WherigoLib)var1.get(0);
               Object var6 = var1.get(1);
               if (var5.klass == var6.getClass()) {
                  break label29;
               }
            } catch (ClassCastException var8) {
               var10001 = false;
               throw new RuntimeException("bad arguments to object:made");
            }

            var4 = false;
            break label30;
         }

         var4 = var3;
      }

      try {
         var2 = var1.push(LuaState.toBoolean(var4));
         return var2;
      } catch (ClassCastException var7) {
         var10001 = false;
         throw new RuntimeException("bad arguments to object:made");
      }
   }

   private int messageBox(LuaCallFrame var1, int var2) {
      Engine.message((LuaTable)var1.get(0));
      return 0;
   }

   private int nocaseequals(LuaCallFrame var1, int var2) {
      String var3 = null;
      boolean var4 = false;
      Object var5 = var1.get(0);
      Object var6 = var1.get(1);
      String var8;
      if (var5 == null) {
         var8 = null;
      } else {
         var8 = var5.toString();
      }

      if (var6 != null) {
         var3 = var6.toString();
      }

      boolean var7;
      label22: {
         if (var8 != var3) {
            var7 = var4;
            if (var8 == null) {
               break label22;
            }

            var7 = var4;
            if (!var8.equalsIgnoreCase(var3)) {
               break label22;
            }
         }

         var7 = true;
      }

      var1.push(LuaState.toBoolean(var7));
      return 1;
   }

   private int playAudio(LuaCallFrame var1, int var2) {
      ((Media)var1.get(0)).play();
      return 0;
   }

   public static void register(LuaState var0) {
      if (env.get("DeviceID") == null) {
         throw new RuntimeException("set your DeviceID! WherigoLib.env.put(WherigoLib.DEVICE_ID, \"some value\")");
      } else {
         LuaTable var1 = var0.getEnvironment();
         LuaTableImpl var2 = new LuaTableImpl();
         var1.rawset("Wherigo", var2);

         for(int var3 = 0; var3 < 24; ++var3) {
            Engine.instance.savegame.addJavafunc(functions[var3]);
            var2.rawset(names[var3], functions[var3]);
         }

         LuaTableImpl var4 = new LuaTableImpl();
         var4.rawset("__index", var4);
         var4.rawset("__call", functions[23]);
         var4.rawset(names[23], functions[23]);
         var0.setClassMetatable(Double.class, var4);
         var0.setClassMetatable(WherigoLib.class, var2);
         var2.rawset("__index", var2);
         var2.rawset("Player", Engine.instance.player);
         var2.rawset("INVALID_ZONEPOINT", (Object)null);
         var2.rawset("MAINSCREEN", new Double(0.0D));
         var2.rawset("DETAILSCREEN", new Double(1.0D));
         var2.rawset("ITEMSCREEN", new Double(3.0D));
         var2.rawset("INVENTORYSCREEN", new Double(2.0D));
         var2.rawset("LOCATIONSCREEN", new Double(4.0D));
         var2.rawset("TASKSCREEN", new Double(5.0D));
         ((LuaTable)((LuaTable)var1.rawget("package")).rawget("loaded")).rawset("Wherigo", var2);
         var2 = new LuaTableImpl();
         Enumeration var6 = env.keys();

         while(var6.hasMoreElements()) {
            String var5 = (String)var6.nextElement();
            var2.rawset(var5, env.get(var5));
         }

         var2.rawset("Device", Engine.instance.gwcfile.device);
         var1.rawset("Env", var2);
         Cartridge.register();
         Container.register();
         Player.register();
         Timer.register();
         Media.reset();
      }
   }

   private int showStatusText(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      if (var2 < 1) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "insufficient arguments for ShowStatusText");
      String var4 = (String)var1.get(0);
      String var5 = var4;
      if (var4 != null) {
         var5 = var4;
         if (var4.length() == 0) {
            var5 = null;
         }
      }

      Engine.ui.setStatusText(var5);
      return 0;
   }

   private int showscreen(LuaCallFrame var1, int var2) {
      int var3 = (int)LuaState.fromDouble(var1.get(0));
      StringBuilder var4 = null;
      EventTable var5 = var4;
      if (var2 > 1) {
         Object var6 = var1.get(1);
         var5 = var4;
         if (var6 instanceof EventTable) {
            var5 = (EventTable)var6;
         }
      }

      var4 = (new StringBuilder()).append("CALL: ShowScreen(").append(var3).append(") ");
      String var7;
      if (var5 == null) {
         var7 = "";
      } else {
         var7 = var5.name;
      }

      Engine.log(var4.append(var7).toString(), 1);
      Engine.ui.showScreen(var3, var5);
      return 0;
   }

   private int translatePoint(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 3) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "insufficient arguments for TranslatePoint");
      ZonePoint var4 = (ZonePoint)var1.get(0);
      double var5 = LuaState.fromDouble(var1.get(1));
      var1.push(var4.translate(LuaState.fromDouble(var1.get(2)), var5));
      return 1;
   }

   private int vectorToPoint(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "insufficient arguments for VectorToPoint");
      ZonePoint var4 = (ZonePoint)var1.get(0);
      ZonePoint var5 = (ZonePoint)var1.get(1);
      double var6 = ZonePoint.angle2azimuth(var5.bearing(var4));
      var1.push(LuaState.toDouble(var5.distance(var4)));
      var1.push(LuaState.toDouble(var6));
      return 2;
   }

   private int zonePoint(LuaCallFrame var1, int var2) {
      if (var2 == 0) {
         var1.push(new ZonePoint());
      } else {
         boolean var3;
         if (var2 >= 2) {
            var3 = true;
         } else {
            var3 = false;
         }

         BaseLib.luaAssert(var3, "insufficient arguments for ZonePoint");
         double var4 = LuaState.fromDouble(var1.get(0));
         double var6 = LuaState.fromDouble(var1.get(1));
         double var8 = 0.0D;
         if (var2 > 2) {
            var8 = LuaState.fromDouble(var1.get(2));
         }

         var1.push(new ZonePoint(var4, var6, var8));
      }

      return 1;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         var2 = this.command(var1, var2);
         break;
      case 1:
         var2 = this.zonePoint(var1, var2);
         break;
      case 2:
         var2 = this.distance(var1, var2);
         break;
      case 3:
         Engine var3 = Engine.instance;
         Cartridge var4 = new Cartridge();
         var3.cartridge = var4;
         var2 = this.construct(var4, var1, var2);
         break;
      case 4:
         var2 = this.messageBox(var1, var2);
         break;
      case 5:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
         try {
            var2 = this.construct((EventTable)this.klass.newInstance(), var1, var2);
         } catch (InstantiationException var5) {
            var2 = 0;
         } catch (IllegalAccessException var6) {
            var2 = 0;
         }
         break;
      case 6:
         var2 = this.dialog(var1, var2);
         break;
      case 7:
         var2 = this.construct(new Thing(true), var1, var2);
         break;
      case 8:
         var2 = this.construct(new Thing(false), var1, var2);
         break;
      case 14:
         var2 = this.playAudio(var1, var2);
         break;
      case 15:
         var2 = this.getinput(var1, var2);
         break;
      case 16:
         var2 = this.nocaseequals(var1, var2);
         break;
      case 17:
         var2 = this.showscreen(var1, var2);
         break;
      case 18:
         var2 = this.translatePoint(var1, var2);
         break;
      case 19:
         var2 = this.showStatusText(var1, var2);
         break;
      case 20:
         var2 = this.vectorToPoint(var1, var2);
         break;
      case 21:
         var2 = this.logMessage(var1, var2);
         break;
      case 22:
         var2 = this.made(var1, var2);
         break;
      case 23:
         var2 = this.distanceGetValue(var1, var2);
         break;
      default:
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      return names[this.index];
   }
}
