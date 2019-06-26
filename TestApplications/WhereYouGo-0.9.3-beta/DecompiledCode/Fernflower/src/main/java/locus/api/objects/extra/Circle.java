package locus.api.objects.extra;

import java.io.IOException;
import java.io.InvalidObjectException;
import locus.api.objects.GeoData;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class Circle extends GeoData {
   private boolean drawPrecise;
   private Location loc;
   private float radius;

   public Circle() {
   }

   public Circle(Location var1, float var2) throws IOException {
      this(var1, var2, false);
   }

   public Circle(Location var1, float var2, boolean var3) throws IOException {
      this.loc = var1;
      this.radius = var2;
      this.drawPrecise = var3;
      this.checkData();
   }

   public Circle(byte[] var1) throws IOException {
      super(var1);
      this.checkData();
   }

   private void checkData() throws InvalidObjectException {
      if (this.loc == null) {
         throw new InvalidObjectException("Location cannot be 'null'");
      } else if (this.radius <= 0.0F) {
         throw new InvalidObjectException("radius have to be bigger then 0");
      }
   }

   public Location getLocation() {
      return this.loc;
   }

   public float getRadius() {
      return this.radius;
   }

   protected int getVersion() {
      return 1;
   }

   public boolean isDrawPrecise() {
      return this.drawPrecise;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.id = var2.readLong();
      this.name = var2.readString();
      this.readExtraData(var2);
      this.readStyles(var2);
      this.loc = new Location(var2);
      this.radius = var2.readFloat();
      this.drawPrecise = var2.readBoolean();
      if (var1 >= 1) {
         this.timeCreated = var2.readLong();
      }

   }

   public void reset() {
      this.loc = null;
      this.radius = 0.0F;
      this.drawPrecise = false;
      this.timeCreated = System.currentTimeMillis();
   }

   public void setDrawPrecise(boolean var1) {
      this.drawPrecise = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.id);
      var1.writeString(this.name);
      this.writeExtraData(var1);
      this.writeStyles(var1);
      this.loc.write(var1);
      var1.writeFloat(this.radius);
      var1.writeBoolean(this.drawPrecise);
      var1.writeLong(this.timeCreated);
   }
}
