package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.stdlib.MathLib;

/* renamed from: cz.matejcik.openwig.ZonePoint */
public class ZonePoint implements LuaTable, Serializable {
    public static final double DEG_PI = 57.29577951308232d;
    public static final double LATITUDE_COEF = 110940.00000395167d;
    public static final double METRE_COEF = 9.013881377E-6d;
    public static final double PI_180 = 0.017453292519943295d;
    public static final double PI_2 = 1.5707963267948966d;
    public static final Hashtable conversions = new Hashtable(6);
    public double altitude = 0.0d;
    public double latitude = 0.0d;
    public double longitude = 0.0d;

    public static ZonePoint copy(ZonePoint z) {
        if (z == null) {
            return null;
        }
        return new ZonePoint(z);
    }

    public ZonePoint(ZonePoint z) {
        this.latitude = z.latitude;
        this.longitude = z.longitude;
        this.altitude = z.altitude;
    }

    public ZonePoint(double lat, double lon, double alt) {
        this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
    }

    public ZonePoint translate(double angle, double dist) {
        double rad = ZonePoint.azimuth2angle(angle);
        return new ZonePoint(this.latitude + ZonePoint.m2lat(Math.sin(rad) * dist), this.longitude + ZonePoint.m2lon(this.latitude, Math.cos(rad) * dist), this.altitude);
    }

    public void sync(ZonePoint z) {
        this.latitude = z.latitude;
        this.longitude = z.longitude;
    }

    public static double lat2m(double degrees) {
        return 110940.00000395167d * degrees;
    }

    public static double lon2m(double latitude, double degrees) {
        return ((degrees * 0.017453292519943295d) * Math.cos(0.017453292519943295d * latitude)) * 6367449.0d;
    }

    public static double m2lat(double metres) {
        return 9.013881377E-6d * metres;
    }

    public static double m2lon(double latitude, double metres) {
        return metres / ((Math.cos(latitude * 0.017453292519943295d) * 0.017453292519943295d) * 6367449.0d);
    }

    public double distance(double lat, double lon) {
        return ZonePoint.distance(lat, lon, this.latitude, this.longitude);
    }

    public double distance(ZonePoint z) {
        return ZonePoint.distance(z.latitude, z.longitude, this.latitude, this.longitude);
    }

    static {
        conversions.put("feet", new Double(0.3048d));
        conversions.put("ft", new Double(0.3048d));
        conversions.put("miles", new Double(1609.344d));
        conversions.put("meters", new Double(1.0d));
        conversions.put("kilometers", new Double(1000.0d));
        conversions.put("nauticalmiles", new Double(1852.0d));
    }

    public static double convertDistanceTo(double value, String unit) {
        if (unit == null || !conversions.containsKey(unit)) {
            return value;
        }
        return value / ((Double) conversions.get(unit)).doubleValue();
    }

    public static double convertDistanceFrom(double value, String unit) {
        if (unit == null || !conversions.containsKey(unit)) {
            return value;
        }
        return value * ((Double) conversions.get(unit)).doubleValue();
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double mx = Math.abs(ZonePoint.lat2m(lat1 - lat2));
        double my = Math.abs(ZonePoint.lon2m(lat2, lon1 - lon2));
        return Math.sqrt((mx * mx) + (my * my));
    }

    public String friendlyDistance(double lat, double lon) {
        return ZonePoint.makeFriendlyDistance(distance(lat, lon));
    }

    public static String makeFriendlyDistance(double dist) {
        if (dist > 1500.0d) {
            return Double.toString(((double) ((long) (dist / 10.0d))) / 100.0d) + " km";
        } else if (dist > 100.0d) {
            return Double.toString((double) ((long) dist)) + " m";
        } else {
            return Double.toString(((double) ((long) (dist * 100.0d))) / 100.0d) + " m";
        }
    }

    public static String makeFriendlyAngle(double angle) {
        boolean neg = false;
        if (angle < 0.0d) {
            neg = true;
            angle *= -1.0d;
        }
        int degrees = (int) angle;
        String an = String.valueOf((angle - ((double) degrees)) * 60.0d);
        if (an.indexOf(46) != -1) {
            an = an.substring(0, Math.min(an.length(), an.indexOf(46) + 5));
        }
        return (neg ? "- " : "+ ") + String.valueOf(degrees) + "° " + an;
    }

    public static String makeFriendlyLatitude(double angle) {
        return ZonePoint.makeFriendlyAngle(angle).replace('+', 'N').replace('-', 'S');
    }

    public static String makeFriendlyLongitude(double angle) {
        return ZonePoint.makeFriendlyAngle(angle).replace('+', 'E').replace('-', 'W');
    }

    public double bearing(double lat, double lon) {
        return MathLib.atan2(ZonePoint.lat2m(this.latitude - lat), ZonePoint.lon2m(lat, this.longitude - lon));
    }

    public double bearing(ZonePoint zp) {
        return bearing(zp.latitude, zp.longitude);
    }

    public static double angle2azimuth(double angle) {
        double degrees = -((angle - 1.5707963267948966d) * 57.29577951308232d);
        while (degrees < 0.0d) {
            degrees += 360.0d;
        }
        while (degrees >= 360.0d) {
            degrees -= 360.0d;
        }
        return degrees;
    }

    public static double azimuth2angle(double azim) {
        double ret = (-(0.017453292519943295d * azim)) + 1.5707963267948966d;
        while (ret > 3.141592653589793d) {
            ret -= 6.283185307179586d;
        }
        while (ret <= -3.141592653589793d) {
            ret += 6.283185307179586d;
        }
        return ret;
    }

    public void setMetatable(LuaTable metatable) {
    }

    public LuaTable getMetatable() {
        return null;
    }

    public void rawset(Object key, Object value) {
        if (key != null) {
            String name = key.toString();
            if ("latitude".equals(name)) {
                this.latitude = LuaState.fromDouble(value);
            } else if ("longitude".equals(name)) {
                this.longitude = LuaState.fromDouble(value);
            } else if ("altitude".equals(name)) {
                this.altitude = LuaState.fromDouble(value);
            }
        }
    }

    public Object rawget(Object key) {
        if (key == null) {
            return null;
        }
        String name = key.toString();
        if ("latitude".equals(name)) {
            return LuaState.toDouble(this.latitude);
        }
        if ("longitude".equals(name)) {
            return LuaState.toDouble(this.longitude);
        }
        if ("altitude".equals(name)) {
            return LuaState.toDouble(this.altitude);
        }
        return null;
    }

    public Object next(Object key) {
        return null;
    }

    public int len() {
        return 3;
    }

    public void updateWeakSettings(boolean weakKeys, boolean weakValues) {
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeDouble(this.latitude);
        out.writeDouble(this.longitude);
        out.writeDouble(this.altitude);
    }

    public void deserialize(DataInputStream in) throws IOException {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.altitude = in.readDouble();
    }

    public String toString() {
        return "ZonePoint(" + this.latitude + "," + this.longitude + "," + this.altitude + ")";
    }
}
