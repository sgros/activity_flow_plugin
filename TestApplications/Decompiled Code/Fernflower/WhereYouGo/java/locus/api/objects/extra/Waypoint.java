package locus.api.objects.extra;

import java.io.IOException;
import locus.api.objects.GeoData;
import locus.api.objects.geocaching.GeocachingData;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class Waypoint extends GeoData {
   private static final String TAG = "Waypoint";
   public static final String TAG_EXTRA_CALLBACK = "TAG_EXTRA_CALLBACK";
   public static final String TAG_EXTRA_ON_DISPLAY = "TAG_EXTRA_ON_DISPLAY";
   public GeocachingData gcData;
   private Location loc;

   public Waypoint() {
      this("", new Location(""));
   }

   public Waypoint(String var1, Location var2) {
      this.setName(var1);
      this.loc = var2;
   }

   public Waypoint(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public Waypoint(byte[] var1) throws IOException {
      super(var1);
   }

   public static GeocachingData readGeocachingData(DataReaderBigEndian var0) throws IOException {
      GeocachingData var1;
      if (var0.readBoolean()) {
         var1 = new GeocachingData(var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   private void writeGeocachingData(DataWriterBigEndian var1) throws IOException {
      if (this.gcData != null) {
         var1.writeBoolean(true);
         this.gcData.write(var1);
      } else {
         var1.writeBoolean(false);
      }

   }

   public String getExtraCallback() {
      String var1;
      if (this.extraData != null) {
         var1 = this.extraData.getParameter(20);
      } else {
         var1 = null;
      }

      return var1;
   }

   public String getExtraOnDisplay() {
      String var1;
      if (this.extraData != null) {
         var1 = this.extraData.getParameter(21);
      } else {
         var1 = null;
      }

      return var1;
   }

   public byte[] getGeocachingData() {
      byte[] var1;
      try {
         DataWriterBigEndian var3 = new DataWriterBigEndian();
         this.writeGeocachingData(var3);
         var1 = var3.toByteArray();
      } catch (IOException var2) {
         Logger.logE("Waypoint", "getGeocachingData()", var2);
         var1 = null;
      }

      return var1;
   }

   public Location getLocation() {
      return this.loc;
   }

   protected int getVersion() {
      return 2;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.id = var2.readLong();
      this.name = var2.readString();
      this.loc = new Location(var2);
      this.readExtraData(var2);
      this.readStyles(var2);
      this.gcData = readGeocachingData(var2);
      if (var1 >= 1) {
         this.timeCreated = var2.readLong();
      }

      if (var1 >= 2) {
         this.setReadWriteMode(GeoData.ReadWriteMode.values()[var2.readInt()]);
      }

   }

   public void removeExtraCallback() {
      this.addParameter(20, "clear");
   }

   public void removeExtraOnDisplay() {
      this.addParameter(21, "clear");
   }

   public void reset() {
      this.id = -1L;
      this.name = "";
      this.loc = null;
      this.extraData = null;
      this.styleNormal = null;
      this.styleHighlight = null;
      this.gcData = null;
      this.timeCreated = System.currentTimeMillis();
      this.setReadWriteMode(GeoData.ReadWriteMode.READ_WRITE);
   }

   public void setExtraCallback(String var1, String var2, String var3, String var4, String var5) {
      var2 = ExtraData.generateCallbackString(var1, var2, var3, var4, var5);
      if (var2.length() != 0) {
         StringBuilder var6 = new StringBuilder();
         var6.append("TAG_EXTRA_CALLBACK").append(";");
         var6.append(var2).append(";");
         this.addParameter(20, var6.toString());
      }

   }

   public void setExtraOnDisplay(String var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("TAG_EXTRA_ON_DISPLAY").append(";");
      var5.append(var1).append(";");
      var5.append(var2).append(";");
      var5.append(var3).append(";");
      var5.append(var4).append(";");
      this.addParameter(21, var5.toString());
   }

   public void setGeocachingData(byte[] var1) {
      try {
         DataReaderBigEndian var2 = new DataReaderBigEndian(var1);
         this.gcData = readGeocachingData(var2);
      } catch (Exception var3) {
         Logger.logE("Waypoint", "setGeocachingData(" + var1 + ")", var3);
         this.gcData = null;
      }

   }

   public void setLocation(Location var1) {
      if (var1 == null) {
         Logger.logW("Waypoint", "setLocation(null), unable to set invalid Location object");
      } else {
         this.loc = var1;
      }

   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.id);
      var1.writeString(this.name);
      this.loc.write(var1);
      this.writeExtraData(var1);
      this.writeStyles(var1);
      this.writeGeocachingData(var1);
      var1.writeLong(this.timeCreated);
      var1.writeInt(this.getReadWriteMode().ordinal());
   }
}
