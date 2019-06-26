package se.krka.kahlua.vm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

   public LuaPrototype() {
   }

   public LuaPrototype(DataInputStream var1, boolean var2, String var3, int var4) throws IOException {
      this.name = readLuaString(var1, var4, var2);
      if (this.name == null) {
         this.name = var3;
      }

      var1.readInt();
      var1.readInt();
      this.numUpvalues = var1.read();
      this.numParams = var1.read();
      boolean var5;
      if ((var1.read() & 2) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.isVararg = var5;
      this.maxStacksize = var1.read();
      int var6 = toInt(var1.readInt(), var2);
      this.code = new int[var6];

      int var7;
      int var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = toInt(var1.readInt(), var2);
         this.code[var7] = var8;
      }

      var6 = toInt(var1.readInt(), var2);
      this.constants = new Object[var6];

      for(var7 = 0; var7 < var6; ++var7) {
         Object var13 = null;
         var8 = var1.read();
         switch(var8) {
         case 0:
            break;
         case 1:
            if (var1.read() == 0) {
               var13 = Boolean.FALSE;
            } else {
               var13 = Boolean.TRUE;
            }
            break;
         case 2:
         default:
            throw new IOException("unknown constant type: " + var8);
         case 3:
            long var9 = var1.readLong();
            long var11 = var9;
            if (var2) {
               var11 = rev(var9);
            }

            var13 = LuaState.toDouble(Double.longBitsToDouble(var11));
            break;
         case 4:
            var13 = readLuaString(var1, var4, var2);
         }

         this.constants[var7] = var13;
      }

      var6 = toInt(var1.readInt(), var2);
      this.prototypes = new LuaPrototype[var6];

      for(var7 = 0; var7 < var6; ++var7) {
         this.prototypes[var7] = new LuaPrototype(var1, var2, this.name, var4);
      }

      var6 = toInt(var1.readInt(), var2);
      this.lines = new int[var6];

      for(var7 = 0; var7 < var6; ++var7) {
         var8 = toInt(var1.readInt(), var2);
         this.lines[var7] = var8;
      }

      var6 = toInt(var1.readInt(), var2);

      for(var7 = 0; var7 < var6; ++var7) {
         readLuaString(var1, var4, var2);
         var1.readInt();
         var1.readInt();
      }

      var6 = toInt(var1.readInt(), var2);

      for(var7 = 0; var7 < var6; ++var7) {
         readLuaString(var1, var4, var2);
      }

   }

   private void dumpPrototype(DataOutputStream var1) throws IOException {
      dumpString(this.name, var1);
      var1.writeInt(0);
      var1.writeInt(0);
      var1.write(this.numUpvalues);
      var1.write(this.numParams);
      byte var2;
      if (this.isVararg) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      var1.write(var2);
      var1.write(this.maxStacksize);
      int var3 = this.code.length;
      var1.writeInt(var3);

      int var6;
      for(var6 = 0; var6 < var3; ++var6) {
         var1.writeInt(this.code[var6]);
      }

      int var4 = this.constants.length;
      var1.writeInt(var4);

      for(var6 = 0; var6 < var4; ++var6) {
         Object var5 = this.constants[var6];
         if (var5 == null) {
            var1.write(0);
         } else if (var5 instanceof Boolean) {
            var1.write(1);
            byte var7;
            if ((Boolean)var5) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var1.write(var7);
         } else if (var5 instanceof Double) {
            var1.write(3);
            var1.writeLong(Double.doubleToLongBits((Double)var5));
         } else {
            if (!(var5 instanceof String)) {
               throw new RuntimeException("Bad type in constant pool");
            }

            var1.write(4);
            dumpString((String)var5, var1);
         }
      }

      var3 = this.prototypes.length;
      var1.writeInt(var3);

      for(var6 = 0; var6 < var3; ++var6) {
         this.prototypes[var6].dumpPrototype(var1);
      }

      var3 = this.lines.length;
      var1.writeInt(var3);

      for(var6 = 0; var6 < var3; ++var6) {
         var1.writeInt(this.lines[var6]);
      }

      var1.writeInt(0);
      var1.writeInt(0);
   }

   private static void dumpString(String var0, DataOutputStream var1) throws IOException {
      if (var0 == null) {
         var1.writeShort(0);
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         (new DataOutputStream(var2)).writeUTF(var0);
         byte[] var4 = var2.toByteArray();
         int var3 = var4.length - 2;
         var1.writeInt(var3 + 1);
         var1.write(var4, 2, var3);
         var1.write(0);
      }

   }

   private static void loadAssert(boolean var0, String var1) throws IOException {
      if (!var0) {
         throw new IOException("Could not load bytecode:" + var1);
      }
   }

   public static LuaClosure loadByteCode(DataInputStream var0, LuaTable var1) throws IOException {
      boolean var2 = true;
      boolean var3;
      if (var0.read() == 27) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Signature 1");
      if (var0.read() == 76) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Signature 2");
      if (var0.read() == 117) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Signature 3");
      if (var0.read() == 97) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Signature 4");
      if (var0.read() == 81) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Version");
      if (var0.read() == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      loadAssert(var3, "Format");
      if (var0.read() == 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4;
      if (var0.read() == 4) {
         var4 = true;
      } else {
         var4 = false;
      }

      loadAssert(var4, "Size int");
      int var5 = var0.read();
      if (var5 != 4 && var5 != 8) {
         var4 = false;
      } else {
         var4 = true;
      }

      loadAssert(var4, "Size t");
      if (var0.read() == 4) {
         var4 = true;
      } else {
         var4 = false;
      }

      loadAssert(var4, "Size instr");
      if (var0.read() == 8) {
         var4 = true;
      } else {
         var4 = false;
      }

      loadAssert(var4, "Size number");
      if (var0.read() == 0) {
         var4 = var2;
      } else {
         var4 = false;
      }

      loadAssert(var4, "Integral");
      return new LuaClosure(new LuaPrototype(var0, var3, (String)null, var5), var1);
   }

   public static LuaClosure loadByteCode(InputStream var0, LuaTable var1) throws IOException {
      Object var2 = var0;
      if (!(var0 instanceof DataInputStream)) {
         var2 = new DataInputStream(var0);
      }

      return loadByteCode((DataInputStream)var2, var1);
   }

   private static String loadUndecodable(byte[] var0) {
      for(int var1 = 2; var1 < var0.length; ++var1) {
         if ((var0[var1] & 128) == 128) {
            var0[var1] = (byte)63;
         }
      }

      return new String(var0, 2, var0.length - 2);
   }

   private static String readLuaString(DataInputStream var0, int var1, boolean var2) throws IOException {
      long var3 = 0L;
      if (var1 == 4) {
         var3 = (long)toInt(var0.readInt(), var2);
      } else if (var1 == 8) {
         var3 = toLong(var0.readLong(), var2);
      } else {
         loadAssert(false, "Bad string size");
      }

      String var8;
      if (var3 == 0L) {
         var8 = null;
      } else {
         --var3;
         if (var3 < 65536L) {
            var2 = true;
         } else {
            var2 = false;
         }

         loadAssert(var2, "Too long string:" + var3);
         var1 = (int)var3;
         byte[] var5 = new byte[var1 + 3];
         var5[0] = (byte)((byte)(var1 >> 8 & 255));
         var5[1] = (byte)((byte)(var1 & 255));
         var0.readFully(var5, 2, var1 + 1);
         if (var5[var1 + 2] == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         loadAssert(var2, "String loading");

         try {
            ByteArrayInputStream var9 = new ByteArrayInputStream(var5);
            DataInputStream var6 = new DataInputStream(var9);
            var8 = var6.readUTF();
            var6.close();
         } catch (IOException var7) {
            var8 = loadUndecodable(var5);
         }
      }

      return var8;
   }

   public static int rev(int var0) {
      return (var0 & 255) << 24 | (var0 >>> 8 & 255) << 16 | (var0 >>> 16 & 255) << 8 | var0 >>> 24 & 255;
   }

   public static long rev(long var0) {
      return (var0 & 255L) << 56 | (var0 >>> 8 & 255L) << 48 | (var0 >>> 16 & 255L) << 40 | (var0 >>> 24 & 255L) << 32 | (var0 >>> 32 & 255L) << 24 | (var0 >>> 40 & 255L) << 16 | (var0 >>> 48 & 255L) << 8 | var0 >>> 56 & 255L;
   }

   public static int toInt(int var0, boolean var1) {
      int var2 = var0;
      if (var1) {
         var2 = rev(var0);
      }

      return var2;
   }

   public static long toLong(long var0, boolean var2) {
      long var3 = var0;
      if (var2) {
         var3 = rev(var0);
      }

      return var3;
   }

   public void dump(OutputStream var1) throws IOException {
      DataOutputStream var2;
      if (var1 instanceof DataOutputStream) {
         var2 = (DataOutputStream)var1;
      } else {
         var2 = new DataOutputStream(var1);
      }

      var2.write(27);
      var2.write(76);
      var2.write(117);
      var2.write(97);
      var2.write(81);
      var2.write(0);
      var2.write(0);
      var2.write(4);
      var2.write(4);
      var2.write(4);
      var2.write(8);
      var2.write(0);
      this.dumpPrototype(var2);
   }

   public String toString() {
      return this.name;
   }
}
