package p005cz.matejcik.openwig.platform;

/* renamed from: cz.matejcik.openwig.platform.LocationService */
public interface LocationService {
    public static final int CONNECTING = 1;
    public static final int NO_FIX = 2;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 3;

    void connect();

    void disconnect();

    double getAltitude();

    double getHeading();

    double getLatitude();

    double getLongitude();

    double getPrecision();

    int getState();
}
