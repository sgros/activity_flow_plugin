package menion.android.whereyougo.geo.location;

public class Location extends locus.api.objects.extra.Location {
   public Location() {
   }

   public Location(android.location.Location var1) {
      this(var1.getProvider(), var1.getLatitude(), var1.getLongitude());
      this.setTime(var1.getTime());
      if (var1.hasAccuracy()) {
         this.setAccuracy(var1.getAccuracy());
      }

      if (var1.hasAltitude()) {
         this.setAltitude(var1.getAltitude());
      }

      if (var1.hasBearing()) {
         this.setBearing(var1.getBearing());
      }

      if (var1.hasSpeed()) {
         this.setSpeed(var1.getSpeed());
      }

   }

   public Location(String var1) {
      super(var1);
   }

   public Location(String var1, double var2, double var4) {
      super(var1, var2, var4);
   }

   public Location(Location var1) {
      super((locus.api.objects.extra.Location)var1);
   }
}
