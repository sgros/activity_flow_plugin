package locus.api.objects.extra;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class Location extends Storable {
   private static final String TAG = "Location";
   public double latitude;
   public double longitude;
   private double mAltitude;
   private Location.ExtraBasic mExtraBasic;
   private Location.ExtraSensor mExtraSensor;
   private boolean mHasAltitude;
   private long mId;
   private String provider;
   private long time;

   public Location() {
      this("");
   }

   public Location(String var1) {
      this.setProvider(var1);
   }

   public Location(String var1, double var2, double var4) {
      this.setProvider(var1);
      this.setLatitude(var2);
      this.setLongitude(var4);
   }

   public Location(Location var1) {
      this.set(var1);
   }

   public Location(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public Location(byte[] var1) throws IOException {
      super(var1);
   }

   private void checkExtraBasic() {
      if (!this.mExtraBasic.hasData()) {
         this.mExtraBasic = null;
      }

   }

   private void checkExtraSensor() {
      if (!this.mExtraSensor.hasData()) {
         this.mExtraSensor = null;
      }

   }

   private void readSensorVersion1(DataReaderBigEndian var1) throws IOException {
      this.mExtraSensor = new Location.ExtraSensor();
      this.mExtraSensor.hasHr = var1.readBoolean();
      this.mExtraSensor.hr = var1.readInt();
      this.mExtraSensor.hasCadence = var1.readBoolean();
      this.mExtraSensor.cadence = var1.readInt();
      this.mExtraSensor.hasSpeed = var1.readBoolean();
      this.mExtraSensor.speed = var1.readFloat();
      this.mExtraSensor.hasPower = var1.readBoolean();
      this.mExtraSensor.power = var1.readFloat();
      if (!this.mExtraSensor.hasData()) {
         this.mExtraSensor = null;
      }

   }

   public float bearingTo(Location var1) {
      return (new LocationCompute(this)).bearingTo(var1);
   }

   public float[] distanceAndBearingTo(Location var1) {
      LocationCompute var2 = new LocationCompute(this);
      return new float[]{var2.distanceTo(var1), var2.bearingTo(var1)};
   }

   public float distanceTo(Location var1) {
      return (new LocationCompute(this)).distanceTo(var1);
   }

   public float getAccuracy() {
      float var1;
      if (this.hasAccuracy()) {
         var1 = this.mExtraBasic.accuracy;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public double getAltitude() {
      double var1;
      if (this.hasAltitude()) {
         var1 = this.mAltitude;
      } else {
         var1 = 0.0D;
      }

      return var1;
   }

   public float getBearing() {
      float var1;
      if (this.hasBearing()) {
         var1 = this.mExtraBasic.bearing;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public long getId() {
      return this.mId;
   }

   public double getLatitude() {
      return this.latitude;
   }

   public double getLongitude() {
      return this.longitude;
   }

   public String getProvider() {
      return this.provider;
   }

   public int getSensorCadence() {
      int var1;
      if (this.hasSensorCadence()) {
         var1 = this.mExtraSensor.cadence;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getSensorHeartRate() {
      int var1;
      if (this.hasSensorHeartRate()) {
         var1 = this.mExtraSensor.hr;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public float getSensorPower() {
      float var1;
      if (this.hasSensorPower()) {
         var1 = this.mExtraSensor.power;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public float getSensorSpeed() {
      float var1;
      if (this.hasSensorSpeed()) {
         var1 = this.mExtraSensor.speed;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public int getSensorStrides() {
      int var1;
      if (this.hasSensorStrides()) {
         var1 = this.mExtraSensor.strides;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public float getSensorTemperature() {
      float var1;
      if (this.hasSensorTemperature()) {
         var1 = this.mExtraSensor.temperature;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public float getSpeed() {
      float var1;
      if (this.hasSpeed()) {
         var1 = this.mExtraBasic.speed;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public float getSpeedOptimal() {
      float var1;
      if (this.hasSensorSpeed()) {
         var1 = this.getSensorSpeed();
      } else {
         var1 = this.getSpeed();
      }

      return var1;
   }

   public long getTime() {
      return this.time;
   }

   protected int getVersion() {
      return 2;
   }

   public boolean hasAccuracy() {
      boolean var1;
      if (this.mExtraBasic != null && this.mExtraBasic.hasAccuracy) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasAltitude() {
      return this.mHasAltitude;
   }

   public boolean hasBearing() {
      boolean var1;
      if (this.mExtraBasic != null && this.mExtraBasic.hasBearing) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorCadence() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasCadence) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorHeartRate() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasHr) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorPower() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasPower) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorSpeed() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasSpeed) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorStrides() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasStrides) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSensorTemperature() {
      boolean var1;
      if (this.mExtraSensor != null && this.mExtraSensor.hasTemperature) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSpeed() {
      boolean var1;
      if (this.mExtraBasic != null && this.mExtraBasic.hasSpeed) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasSpeedOptimal() {
      boolean var1;
      if (!this.hasSpeed() && !this.hasSensorSpeed()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readLong();
      this.provider = var2.readString();
      this.time = var2.readLong();
      this.latitude = var2.readDouble();
      this.longitude = var2.readDouble();
      this.mHasAltitude = var2.readBoolean();
      this.mAltitude = var2.readDouble();
      if (var2.readBoolean()) {
         this.mExtraBasic = new Location.ExtraBasic();
         this.mExtraBasic.hasAccuracy = var2.readBoolean();
         this.mExtraBasic.accuracy = var2.readFloat();
         this.mExtraBasic.hasBearing = var2.readBoolean();
         this.mExtraBasic.bearing = var2.readFloat();
         this.mExtraBasic.hasSpeed = var2.readBoolean();
         this.mExtraBasic.speed = var2.readFloat();
         if (!this.mExtraBasic.hasData()) {
            this.mExtraBasic = null;
         }
      }

      if (var1 >= 1 && var2.readBoolean()) {
         if (var1 == 1) {
            this.readSensorVersion1(var2);
         } else {
            this.mExtraSensor = new Location.ExtraSensor(var2);
         }
      }

   }

   public void removeAccuracy() {
      if (this.mExtraBasic != null) {
         this.mExtraBasic.accuracy = 0.0F;
         this.mExtraBasic.hasAccuracy = false;
         this.checkExtraBasic();
      }

   }

   public void removeAltitude() {
      this.mAltitude = 0.0D;
      this.mHasAltitude = false;
   }

   public void removeBearing() {
      if (this.mExtraBasic != null) {
         this.mExtraBasic.bearing = 0.0F;
         this.mExtraBasic.hasBearing = false;
         this.checkExtraBasic();
      }

   }

   public void removeSensorAll() {
      this.mExtraSensor = null;
   }

   public void removeSensorCadence() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.cadence = 0;
         this.mExtraSensor.hasCadence = false;
         this.checkExtraSensor();
      }

   }

   public void removeSensorHeartRate() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.hr = 0;
         this.mExtraSensor.hasHr = false;
         this.checkExtraSensor();
      }

   }

   public void removeSensorPower() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.power = 0.0F;
         this.mExtraSensor.hasPower = false;
         this.checkExtraSensor();
      }

   }

   public void removeSensorSpeed() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.speed = 0.0F;
         this.mExtraSensor.hasSpeed = false;
         this.checkExtraSensor();
      }

   }

   public void removeSensorStrides() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.strides = 0;
         this.mExtraSensor.hasStrides = false;
         this.checkExtraSensor();
      }

   }

   public void removeSensorTemperature() {
      if (this.mExtraSensor != null) {
         this.mExtraSensor.temperature = 0.0F;
         this.mExtraSensor.hasTemperature = false;
         this.checkExtraSensor();
      }

   }

   public void removeSpeed() {
      if (this.mExtraBasic != null) {
         this.mExtraBasic.speed = 0.0F;
         this.mExtraBasic.hasSpeed = false;
         this.checkExtraBasic();
      }

   }

   public void reset() {
      this.mId = -1L;
      this.provider = null;
      this.time = 0L;
      this.latitude = 0.0D;
      this.longitude = 0.0D;
      this.mExtraBasic = null;
      this.mExtraSensor = null;
   }

   public void set(Location var1) {
      this.mId = var1.mId;
      this.provider = var1.provider;
      this.time = var1.time;
      this.latitude = var1.latitude;
      this.longitude = var1.longitude;
      this.mHasAltitude = var1.hasAltitude();
      this.mAltitude = var1.getAltitude();
      if (var1.mExtraBasic != null && var1.mExtraBasic.hasData()) {
         this.mExtraBasic = var1.mExtraBasic.clone();
         if (!this.mExtraBasic.hasData()) {
            this.mExtraBasic = null;
         }
      } else {
         this.mExtraBasic = null;
      }

      if (var1.mExtraSensor != null && var1.mExtraSensor.hasData()) {
         this.mExtraSensor = var1.mExtraSensor.clone();
         if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
         }
      } else {
         this.mExtraSensor = null;
      }

   }

   public void setAccuracy(float var1) {
      if (this.mExtraBasic == null) {
         this.mExtraBasic = new Location.ExtraBasic();
      }

      this.mExtraBasic.accuracy = var1;
      this.mExtraBasic.hasAccuracy = true;
   }

   public void setAltitude(double var1) {
      this.mAltitude = var1;
      this.mHasAltitude = true;
   }

   public void setBearing(float var1) {
      while(true) {
         float var2 = var1;
         if (var1 >= 0.0F) {
            while(var2 >= 360.0F) {
               var2 -= 360.0F;
            }

            if (this.mExtraBasic == null) {
               this.mExtraBasic = new Location.ExtraBasic();
            }

            this.mExtraBasic.bearing = var2;
            this.mExtraBasic.hasBearing = true;
            return;
         }

         var1 += 360.0F;
      }
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public Location setLatitude(double var1) {
      double var3;
      if (var1 < -90.0D) {
         Logger.logE("Location", "setLatitude(" + var1 + "), " + "invalid latitude", new Exception(""));
         var3 = -90.0D;
      } else {
         var3 = var1;
         if (var1 > 90.0D) {
            Logger.logE("Location", "setLatitude(" + var1 + "), " + "invalid latitude", new Exception(""));
            var3 = 90.0D;
         }
      }

      this.latitude = var3;
      return this;
   }

   public Location setLongitude(double var1) {
      double var3;
      if (var1 < -180.0D) {
         var3 = var1 + 360.0D;
      } else {
         var3 = var1;
         if (var1 > 180.0D) {
            var3 = var1 - 360.0D;
         }
      }

      this.longitude = var3;
      return this;
   }

   public Location setProvider(String var1) {
      if (var1 == null) {
         this.provider = "";
      } else {
         this.provider = var1;
      }

      return this;
   }

   public void setSensorCadence(int var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.cadence = var1;
      this.mExtraSensor.hasCadence = true;
   }

   public void setSensorHeartRate(int var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.hr = var1;
      this.mExtraSensor.hasHr = true;
   }

   public void setSensorPower(float var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.power = var1;
      this.mExtraSensor.hasPower = true;
   }

   public void setSensorSpeed(float var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.speed = var1;
      this.mExtraSensor.hasSpeed = true;
   }

   public void setSensorStrides(int var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.strides = var1;
      this.mExtraSensor.hasStrides = true;
   }

   public void setSensorTemperature(float var1) {
      if (this.mExtraSensor == null) {
         this.mExtraSensor = new Location.ExtraSensor();
      }

      this.mExtraSensor.temperature = var1;
      this.mExtraSensor.hasTemperature = true;
   }

   public void setSpeed(float var1) {
      if (this.mExtraBasic == null) {
         this.mExtraBasic = new Location.ExtraBasic();
      }

      this.mExtraBasic.speed = var1;
      this.mExtraBasic.hasSpeed = true;
   }

   public void setTime(long var1) {
      this.time = var1;
   }

   public String toString() {
      return "Location [tag:" + this.provider + ", " + "lon:" + this.longitude + ", " + "lat:" + this.latitude + ", " + "alt:" + this.mAltitude + "]";
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.mId);
      var1.writeString(this.provider);
      var1.writeLong(this.time);
      var1.writeDouble(this.latitude);
      var1.writeDouble(this.longitude);
      var1.writeBoolean(this.mHasAltitude);
      var1.writeDouble(this.mAltitude);
      if (this.mExtraBasic != null && this.mExtraBasic.hasData()) {
         var1.writeBoolean(true);
         var1.writeBoolean(this.mExtraBasic.hasAccuracy);
         var1.writeFloat(this.mExtraBasic.accuracy);
         var1.writeBoolean(this.mExtraBasic.hasBearing);
         var1.writeFloat(this.mExtraBasic.bearing);
         var1.writeBoolean(this.mExtraBasic.hasSpeed);
         var1.writeFloat(this.mExtraBasic.speed);
      } else {
         var1.writeBoolean(false);
      }

      if (this.mExtraSensor != null && this.mExtraSensor.hasData()) {
         var1.writeBoolean(true);
         this.mExtraSensor.write(var1);
      } else {
         var1.writeBoolean(false);
      }

   }

   private static class ExtraBasic implements Cloneable {
      float accuracy = 0.0F;
      float bearing = 0.0F;
      boolean hasAccuracy = false;
      boolean hasBearing = false;
      boolean hasSpeed = false;
      float speed = 0.0F;

      ExtraBasic() {
      }

      public Location.ExtraBasic clone() {
         Location.ExtraBasic var1 = new Location.ExtraBasic();
         var1.hasSpeed = this.hasSpeed;
         var1.speed = this.speed;
         var1.hasBearing = this.hasBearing;
         var1.bearing = this.bearing;
         var1.hasAccuracy = this.hasAccuracy;
         var1.accuracy = this.accuracy;
         return var1;
      }

      boolean hasData() {
         boolean var1;
         if (!this.hasSpeed && !this.hasBearing && !this.hasAccuracy) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      public String toString() {
         return Utils.toString(this, "    ");
      }
   }

   private static class ExtraSensor extends Storable implements Cloneable {
      @Deprecated
      private int battery;
      int cadence;
      @Deprecated
      private boolean hasBattery;
      boolean hasCadence;
      boolean hasHr;
      boolean hasPower;
      boolean hasSpeed;
      boolean hasStrides;
      boolean hasTemperature;
      int hr;
      float power;
      float speed;
      int strides;
      float temperature;

      public ExtraSensor() {
      }

      public ExtraSensor(DataReaderBigEndian var1) throws IOException {
         super(var1);
      }

      public Location.ExtraSensor clone() {
         Location.ExtraSensor var1 = new Location.ExtraSensor();
         var1.hasHr = this.hasHr;
         var1.hr = this.hr;
         var1.hasCadence = this.hasCadence;
         var1.cadence = this.cadence;
         var1.hasSpeed = this.hasSpeed;
         var1.speed = this.speed;
         var1.hasPower = this.hasPower;
         var1.power = this.power;
         var1.hasStrides = this.hasStrides;
         var1.strides = this.strides;
         var1.hasTemperature = this.hasTemperature;
         var1.temperature = this.temperature;
         return var1;
      }

      protected int getVersion() {
         return 1;
      }

      boolean hasData() {
         boolean var1;
         if (!this.hasHr && !this.hasCadence && !this.hasSpeed && !this.hasPower && !this.hasStrides && !this.hasTemperature) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.hasHr = var2.readBoolean();
         this.hr = var2.readInt();
         this.hasCadence = var2.readBoolean();
         this.cadence = var2.readInt();
         this.hasSpeed = var2.readBoolean();
         this.speed = var2.readFloat();
         this.hasPower = var2.readBoolean();
         this.power = var2.readFloat();
         this.hasStrides = var2.readBoolean();
         this.strides = var2.readInt();
         this.hasBattery = var2.readBoolean();
         this.battery = var2.readInt();
         if (var1 >= 1) {
            this.hasTemperature = var2.readBoolean();
            this.temperature = var2.readFloat();
         }

      }

      public void reset() {
         this.hasHr = false;
         this.hr = 0;
         this.hasCadence = false;
         this.cadence = 0;
         this.hasSpeed = false;
         this.speed = 0.0F;
         this.hasPower = false;
         this.power = 0.0F;
         this.hasStrides = false;
         this.strides = 0;
         this.hasBattery = false;
         this.battery = 0;
         this.hasTemperature = false;
         this.temperature = 0.0F;
      }

      public String toString() {
         return Utils.toString(this, "    ");
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeBoolean(this.hasHr);
         var1.writeInt(this.hr);
         var1.writeBoolean(this.hasCadence);
         var1.writeInt(this.cadence);
         var1.writeBoolean(this.hasSpeed);
         var1.writeFloat(this.speed);
         var1.writeBoolean(this.hasPower);
         var1.writeFloat(this.power);
         var1.writeBoolean(this.hasStrides);
         var1.writeInt(this.strides);
         var1.writeBoolean(this.hasBattery);
         var1.writeInt(this.battery);
         var1.writeBoolean(this.hasTemperature);
         var1.writeFloat(this.temperature);
      }
   }
}
