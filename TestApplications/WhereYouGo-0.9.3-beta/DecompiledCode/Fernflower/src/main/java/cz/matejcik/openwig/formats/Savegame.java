package cz.matejcik.openwig.formats;

import cz.matejcik.openwig.Cartridge;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Serializable;
import cz.matejcik.openwig.platform.FileHandle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Hashtable;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaPrototype;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.UpValue;

public class Savegame {
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
   private int currentJavafunc = 0;
   protected boolean debug = false;
   private Hashtable idToJavafuncMap = new Hashtable(128);
   private Hashtable javafuncToIdMap = new Hashtable(128);
   int level = 0;
   private Hashtable objectStore;
   private FileHandle saveFile;

   protected Savegame() {
   }

   public Savegame(FileHandle var1) {
      if (var1 == null) {
         throw new NullPointerException("savefile must not be null");
      } else {
         this.saveFile = var1;
      }
   }

   private LuaClosure deserializeLuaClosure(DataInputStream var1) throws IOException {
      LuaClosure var2 = LuaPrototype.loadByteCode(var1, Engine.state.getEnvironment());
      this.restCache(var2);

      for(int var3 = 0; var3 < var2.upvalues.length; ++var3) {
         UpValue var4 = new UpValue();
         var4.value = this.restoreValue(var1, (Object)null);
         var2.upvalues[var3] = var4;
      }

      return var2;
   }

   private int findJavafuncId(JavaFunction var1) {
      Integer var2 = (Integer)this.javafuncToIdMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new RuntimeException("javafunc not found in map!");
      }
   }

   private JavaFunction findJavafuncObject(int var1) {
      return (JavaFunction)this.idToJavafuncMap.get(new Integer(var1));
   }

   private void restCache(Object var1) {
      int var2 = this.currentId++;
      Integer var3 = new Integer(var2);
      this.objectStore.put(var3, var1);
      if (this.debug) {
         this.debug("(ref" + var3 + ")");
      }

   }

   private Object restoreObject(DataInputStream var1, byte var2, Object var3) throws IOException {
      Integer var5;
      Object var10;
      switch(var2) {
      case 4:
         if (var3 instanceof LuaTable) {
            var3 = (LuaTable)var3;
         } else {
            var3 = new LuaTableImpl();
         }

         this.restCache(var3);
         if (this.debug) {
            this.debug("table:\n");
         }

         var10 = this.deserializeLuaTable(var1, (LuaTable)var3);
         break;
      case 5:
         if (this.debug) {
            this.debug("closure: ");
         }

         LuaClosure var14 = this.deserializeLuaClosure(var1);
         var10 = var14;
         if (this.debug) {
            this.debug(var14.toString());
            var10 = var14;
         }
         break;
      case 6:
         String var4 = var1.readUTF();
         var5 = null;

         Serializable var12;
         label88: {
            Throwable var10000;
            label89: {
               boolean var10001;
               try {
                  if (this.debug) {
                     StringBuilder var11 = new StringBuilder();
                     this.debug(var11.append("object of type ").append(var4).append("...\n").toString());
                  }
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label89;
               }

               Class var6;
               try {
                  var6 = this.classForName(var4);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label89;
               }

               var12 = var5;

               try {
                  if (Serializable.class.isAssignableFrom(var6)) {
                     var12 = (Serializable)var6.newInstance();
                  }
                  break label88;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            Throwable var13 = var10000;
            if (this.debug) {
               this.debug("(failed to deserialize " + var4 + ")\n");
            }

            Engine.log("REST: while trying to deserialize " + var4 + ":\n" + var13.toString(), 3);
            var12 = var5;
         }

         if (var12 != null) {
            this.restCache(var12);
            var12.deserialize(var1);
         }

         var10 = var12;
         break;
      case 7:
         var5 = new Integer(var1.readInt());
         if (this.debug) {
            this.debug("reference " + var5);
         }

         var10 = this.objectStore.get(var5);
         if (var10 == null) {
            Engine.log("REST: not found reference " + var5.toString() + " in object store", 2);
            if (this.debug) {
               this.debug(" (which happens to be null?)");
            }

            var10 = var3;
         } else if (this.debug) {
            this.debug(" : " + var10.toString());
         }
         break;
      default:
         Engine.log("REST: found unknown type " + var2, 2);
         if (this.debug) {
            this.debug("UFO");
         }

         var10 = null;
      }

      return var10;
   }

   private void serializeLuaClosure(LuaClosure var1, DataOutputStream var2) throws IOException {
      var1.prototype.dump(var2);

      for(int var3 = 0; var3 < var1.upvalues.length; ++var3) {
         UpValue var4 = var1.upvalues[var3];
         if (var4.value == null) {
            Engine.log("STOR: unclosed upvalue in " + var1.toString(), 2);
            var4.value = var4.thread.objectStack[var4.index];
         }

         this.storeValue(var4.value, var2);
      }

   }

   private void storeObject(Object var1, DataOutputStream var2) throws IOException {
      if (var1 == null) {
         var2.writeByte(0);
      } else {
         Integer var3 = (Integer)this.objectStore.get(var1);
         if (var3 != null) {
            var2.writeByte(7);
            if (this.debug) {
               this.debug("reference " + var3 + " (" + var1.toString() + ")");
            }

            var2.writeInt(var3);
         } else {
            int var4 = this.currentId++;
            var3 = new Integer(var4);
            this.objectStore.put(var1, var3);
            if (this.debug) {
               this.debug("(ref" + var3 + ")");
            }

            if (var1 instanceof Serializable) {
               var2.writeByte(6);
               var2.writeUTF(var1.getClass().getName());
               if (this.debug) {
                  this.debug(var1.getClass().getName() + " (" + var1.toString() + ")");
               }

               ((Serializable)var1).serialize(var2);
            } else if (var1 instanceof LuaTable) {
               var2.writeByte(4);
               if (this.debug) {
                  this.debug("table(" + var1.toString() + "):\n");
               }

               this.serializeLuaTable((LuaTable)var1, var2);
            } else if (var1 instanceof LuaClosure) {
               var2.writeByte(5);
               if (this.debug) {
                  this.debug("closure(" + var1.toString() + ")");
               }

               this.serializeLuaClosure((LuaClosure)var1, var2);
            } else {
               var2.writeByte(0);
               if (this.debug) {
                  this.debug("UFO");
               }

               Engine.log("STOR: unable to store object of type " + var1.getClass().getName(), 2);
            }
         }
      }

   }

   public void addJavafunc(JavaFunction var1) {
      int var2 = this.currentJavafunc++;
      Integer var3 = new Integer(var2);
      this.idToJavafuncMap.put(var3, var1);
      this.javafuncToIdMap.put(var1, var3);
   }

   public void buildJavafuncMap(LuaTable var1) {
      LuaTable[] var2 = new LuaTable[]{var1, (LuaTable)var1.rawget("string"), (LuaTable)var1.rawget("math"), (LuaTable)var1.rawget("coroutine"), (LuaTable)var1.rawget("os"), (LuaTable)var1.rawget("table")};

      for(int var3 = 0; var3 < var2.length; ++var3) {
         LuaTable var4 = var2[var3];
         Object var7 = null;

         while(true) {
            Object var5 = var4.next(var7);
            if (var5 == null) {
               break;
            }

            Object var6 = var4.rawget(var5);
            var7 = var5;
            if (var6 instanceof JavaFunction) {
               this.addJavafunc((JavaFunction)var6);
               var7 = var5;
            }
         }
      }

   }

   protected Class classForName(String var1) throws ClassNotFoundException {
      return Class.forName(var1);
   }

   protected void debug(String var1) {
   }

   public LuaTable deserializeLuaTable(DataInputStream var1, LuaTable var2) throws IOException {
      ++this.level;

      Object var4;
      Object var5;
      for(; var1.readByte() != 17; var2.rawset(var4, var5)) {
         if (this.debug) {
            for(int var3 = 0; var3 < this.level; ++var3) {
               this.debug("  ");
            }
         }

         var4 = this.restoreValue(var1, (Object)null);
         if (this.debug) {
            this.debug(" : ");
         }

         var5 = this.restoreValue(var1, var2.rawget(var4));
         if (this.debug) {
            this.debug("\n");
         }
      }

      --this.level;
      return var2;
   }

   public boolean exists() throws IOException {
      return this.saveFile.exists();
   }

   protected void resetObjectStore() {
      this.objectStore = new Hashtable(256);
      this.currentId = 0;
      this.level = 0;
   }

   public void restore(LuaTable var1) throws IOException {
      DataInputStream var2 = this.saveFile.openDataInputStream();
      if (!"openWIG savegame\n".equals(var2.readUTF())) {
         throw new IOException("Invalid savegame file: bad signature.");
      } else {
         try {
            if (!this.versionOk(var2.readUTF())) {
               IOException var12 = new IOException("Savegame is for different version.");
               throw var12;
            }
         } catch (UTFDataFormatException var10) {
            throw new IOException("Savegame is for different version.");
         }

         try {
            this.resetObjectStore();
            Engine.instance.cartridge = (Cartridge)this.restoreValue(var2, (Object)null);
            this.restoreValue(var2, var1);
         } catch (IOException var8) {
            var8.printStackTrace();
            StringBuilder var11 = new StringBuilder();
            IOException var4 = new IOException(var11.append("Problem loading game: ").append(var8.getMessage()).toString());
            throw var4;
         } finally {
            var2.close();
         }

      }
   }

   public Object restoreValue(DataInputStream var1, Object var2) throws IOException {
      byte var3 = var1.readByte();
      Object var8;
      switch(var3) {
      case 0:
         if (this.debug) {
            this.debug("nil");
         }

         var8 = null;
         break;
      case 1:
         double var4 = var1.readDouble();
         if (this.debug) {
            this.debug(String.valueOf(var4));
         }

         var8 = LuaState.toDouble(var4);
         break;
      case 2:
         String var9 = var1.readUTF();
         var8 = var9;
         if (this.debug) {
            this.debug("\"" + var9 + "\"");
            var8 = var9;
         }
         break;
      case 3:
         boolean var6 = var1.readBoolean();
         if (this.debug) {
            this.debug(String.valueOf(var6));
         }

         var8 = LuaState.toBoolean(var6);
         break;
      case 4:
      case 5:
      case 6:
      case 7:
      default:
         var8 = this.restoreObject(var1, var3, var2);
         break;
      case 8:
         int var7 = var1.readInt();
         var8 = this.findJavafuncObject(var7);
         if (this.debug) {
            this.debug("javafunc(" + var7 + ")-" + var8);
         }
      }

      return var8;
   }

   public void serializeLuaTable(LuaTable var1, DataOutputStream var2) throws IOException {
      ++this.level;
      Object var3 = null;

      while(true) {
         Object var4 = var1.next(var3);
         if (var4 == null) {
            --this.level;
            var2.writeByte(17);
            return;
         }

         var3 = var1.rawget(var4);
         var2.writeByte(16);
         if (this.debug) {
            for(int var5 = 0; var5 < this.level; ++var5) {
               this.debug("  ");
            }
         }

         this.storeValue(var4, var2);
         if (this.debug) {
            this.debug(" : ");
         }

         this.storeValue(var3, var2);
         var3 = var4;
         if (this.debug) {
            this.debug("\n");
            var3 = var4;
         }
      }
   }

   public void store(LuaTable var1) throws IOException {
      DataOutputStream var2 = null;
      if (this.saveFile.exists()) {
         this.saveFile.truncate(0L);
      } else {
         this.saveFile.create();
      }

      DataOutputStream var3 = var2;

      label739: {
         Throwable var10000;
         label740: {
            boolean var10001;
            try {
               Engine.log("STOR: storing game", 1);
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               var2 = this.saveFile.openDataOutputStream();
            } catch (Throwable var92) {
               var10000 = var92;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               var2.writeUTF("openWIG savegame\n");
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               var2.writeUTF("428");
            } catch (Throwable var90) {
               var10000 = var90;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               this.resetObjectStore();
            } catch (Throwable var89) {
               var10000 = var89;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               this.storeValue(Engine.instance.cartridge, var2);
            } catch (Throwable var88) {
               var10000 = var88;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            try {
               this.storeValue(var1, var2);
            } catch (Throwable var87) {
               var10000 = var87;
               var10001 = false;
               break label740;
            }

            var3 = var2;

            label706:
            try {
               Engine.log("STOR: store successful", 1);
               break label739;
            } catch (Throwable var86) {
               var10000 = var86;
               var10001 = false;
               break label706;
            }
         }

         Throwable var94 = var10000;

         try {
            var3.close();
         } catch (Exception var84) {
         }

         throw var94;
      }

      try {
         var2.close();
      } catch (Exception var85) {
      }

   }

   public void storeValue(Object var1, DataOutputStream var2) throws IOException {
      if (var1 == null) {
         if (this.debug) {
            this.debug("nil");
         }

         var2.writeByte(0);
      } else if (var1 instanceof String) {
         var2.writeByte(2);
         if (this.debug) {
            this.debug("\"" + var1.toString() + "\"");
         }

         var2.writeUTF((String)var1);
      } else if (var1 instanceof Boolean) {
         if (this.debug) {
            this.debug(var1.toString());
         }

         var2.writeByte(3);
         var2.writeBoolean((Boolean)var1);
      } else if (var1 instanceof Double) {
         var2.writeByte(1);
         if (this.debug) {
            this.debug(var1.toString());
         }

         var2.writeDouble((Double)var1);
      } else if (var1 instanceof JavaFunction) {
         int var3 = this.findJavafuncId((JavaFunction)var1);
         if (this.debug) {
            this.debug("javafunc(" + var3 + ")-" + var1.toString());
         }

         var2.writeByte(8);
         var2.writeInt(var3);
      } else {
         this.storeObject(var1, var2);
      }

   }

   protected boolean versionOk(String var1) {
      return "428".equals(var1);
   }
}
