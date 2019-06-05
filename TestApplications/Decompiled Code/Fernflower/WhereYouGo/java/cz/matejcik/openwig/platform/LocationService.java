package cz.matejcik.openwig.platform;

public interface LocationService {
   int CONNECTING = 1;
   int NO_FIX = 2;
   int OFFLINE = 0;
   int ONLINE = 3;

   void connect();

   void disconnect();

   double getAltitude();

   double getHeading();

   double getLatitude();

   double getLongitude();

   double getPrecision();

   int getState();
}
