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

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 != this) {
         if (var1 == null) {
            var2 = false;
         } else if (!(var1 instanceof Location)) {
            var2 = false;
         } else {
            Location var3 = (Location)var1;
            if (this.getLatitude() != var3.getLatitude() || this.getLongitude() != var3.getLongitude()) {
               var2 = false;
            }
         }
      }

      return var2;
   }
}
