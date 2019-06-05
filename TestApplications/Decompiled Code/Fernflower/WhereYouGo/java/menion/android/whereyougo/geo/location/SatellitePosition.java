package menion.android.whereyougo.geo.location;

public class SatellitePosition {
   float azimuth = 0.0F;
   float elevation = 0.0F;
   boolean fixed;
   Integer prn = 0;
   int snr = 0;

   public float getAzimuth() {
      return this.azimuth;
   }

   public float getElevation() {
      return this.elevation;
   }

   public Integer getPrn() {
      return this.prn;
   }

   public int getSnr() {
      return this.snr;
   }

   public boolean isFixed() {
      return this.fixed;
   }
}