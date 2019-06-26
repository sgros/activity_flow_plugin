package locus.api.objects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public abstract class Storable {
   private static final String TAG = "Storable";

   public Storable() {
      this.reset();
   }

   public Storable(DataReaderBigEndian var1) throws IOException {
      this();
      this.read(var1);
   }

   public Storable(byte[] var1) throws IOException {
      this(new DataReaderBigEndian(var1));
   }

   public static byte[] getAsBytes(List var0) {
      byte[] var3;
      byte[] var4;
      try {
         DataWriterBigEndian var1 = new DataWriterBigEndian();
         var1.writeListStorable(var0);
         var4 = var1.toByteArray();
      } catch (Exception var2) {
         Logger.logE("Storable", "getAsBytes(" + var0 + ")", var2);
         var3 = null;
         return var3;
      }

      var3 = var4;
      return var3;
   }

   public static Storable read(Class var0, DataReaderBigEndian var1) throws IOException, InstantiationException, IllegalAccessException {
      Storable.BodyContainer var3 = readHeader(var1);
      Storable var2 = (Storable)var0.newInstance();
      var2.readObject(var3.version, new DataReaderBigEndian(var3.data));
      return var2;
   }

   private static Storable.BodyContainer readHeader(DataInputStream var0) throws IOException {
      Storable.BodyContainer var1 = new Storable.BodyContainer();
      var1.version = var0.readInt();
      int var2 = var0.readInt();
      if (var2 >= 0 && var2 <= 10485760) {
         var1.data = new byte[var2];
         var0.read(var1.data);
         return var1;
      } else {
         throw new IOException("item size too big, size:" + var2 + ", max: 10MB");
      }
   }

   private static Storable.BodyContainer readHeader(DataReaderBigEndian var0) throws IOException {
      Storable.BodyContainer var1 = new Storable.BodyContainer();
      var1.version = var0.readInt();
      int var2 = var0.readInt();
      if (var2 >= 0 && var2 <= 20971520) {
         var1.data = var0.readBytes(var2);
         return var1;
      } else {
         throw new IOException("item size too big, size:" + var2 + ", max: 20MB");
      }
   }

   public static List readList(Class var0, DataInputStream var1) throws IOException {
      ArrayList var2 = new ArrayList();
      int var3 = var1.readInt();
      if (var3 != 0) {
         for(int var4 = 0; var4 < var3; ++var4) {
            try {
               Storable var5 = (Storable)var0.newInstance();
               var5.read(var1);
               var2.add(var5);
            } catch (InstantiationException var6) {
               Logger.logE("Storable", "readList(" + var0 + ", " + var1 + ")", var6);
            } catch (IllegalAccessException var7) {
               Logger.logE("Storable", "readList(" + var0 + ", " + var1 + ")", var7);
            }
         }
      }

      return var2;
   }

   public static List readList(Class var0, byte[] var1) throws IOException {
      return (new DataReaderBigEndian(var1)).readListStorable(var0);
   }

   public static void readUnknownObject(DataReaderBigEndian var0) throws IOException {
      readHeader(var0);
   }

   public static void writeList(List var0, DataOutputStream var1) throws IOException {
      int var2;
      if (var0 == null) {
         var2 = 0;
      } else {
         var2 = var0.size();
      }

      var1.writeInt(var2);
      if (var2 != 0) {
         var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            var1.write(((Storable)var0.get(var2)).getAsBytes());
         }
      }

   }

   public byte[] getAsBytes() {
      byte[] var1;
      try {
         DataWriterBigEndian var3 = new DataWriterBigEndian();
         this.write(var3);
         var1 = var3.toByteArray();
      } catch (IOException var2) {
         Logger.logE("Storable", "getAsBytes()", var2);
         var1 = null;
      }

      return var1;
   }

   public Storable getCopy() throws IOException, InstantiationException, IllegalAccessException {
      byte[] var1 = this.getAsBytes();
      return read(this.getClass(), new DataReaderBigEndian(var1));
   }

   protected abstract int getVersion();

   public void read(DataInputStream var1) throws IOException {
      Storable.BodyContainer var2 = readHeader(var1);
      this.readObject(var2.version, new DataReaderBigEndian(var2.data));
   }

   public void read(DataReaderBigEndian var1) throws IOException {
      Storable.BodyContainer var2 = readHeader(var1);
      this.readObject(var2.version, new DataReaderBigEndian(var2.data));
   }

   public void read(byte[] var1) throws IOException {
      this.read(new DataReaderBigEndian(var1));
   }

   protected abstract void readObject(int var1, DataReaderBigEndian var2) throws IOException;

   public abstract void reset();

   public void write(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.getVersion());
      var1.writeInt(0);
      int var2 = var1.size();
      this.writeObject(var1);
      int var3 = var1.size() - var2;
      if (var3 > 0) {
         var1.storePosition();
         var1.moveTo(var2 - 4);
         var1.writeInt(var3);
         var1.restorePosition();
      }

   }

   protected abstract void writeObject(DataWriterBigEndian var1) throws IOException;

   private static class BodyContainer {
      byte[] data;
      int version;

      private BodyContainer() {
      }

      // $FF: synthetic method
      BodyContainer(Object var1) {
         this();
      }
   }
}
