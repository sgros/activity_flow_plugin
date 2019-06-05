// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import java.io.DataOutputStream;
import se.krka.kahlua.vm.LuaState;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.stdlib.MathLib;
import java.util.Hashtable;
import se.krka.kahlua.vm.LuaTable;

public class ZonePoint implements LuaTable, Serializable
{
    public static final double DEG_PI = 57.29577951308232;
    public static final double LATITUDE_COEF = 110940.00000395167;
    public static final double METRE_COEF = 9.013881377E-6;
    public static final double PI_180 = 0.017453292519943295;
    public static final double PI_2 = 1.5707963267948966;
    public static final Hashtable conversions;
    public double altitude;
    public double latitude;
    public double longitude;
    
    static {
        (conversions = new Hashtable(6)).put("feet", new Double(0.3048));
        ZonePoint.conversions.put("ft", new Double(0.3048));
        ZonePoint.conversions.put("miles", new Double(1609.344));
        ZonePoint.conversions.put("meters", new Double(1.0));
        ZonePoint.conversions.put("kilometers", new Double(1000.0));
        ZonePoint.conversions.put("nauticalmiles", new Double(1852.0));
    }
    
    public ZonePoint() {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
    }
    
    public ZonePoint(final double latitude, final double longitude, final double altitude) {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    public ZonePoint(final ZonePoint zonePoint) {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.latitude = zonePoint.latitude;
        this.longitude = zonePoint.longitude;
        this.altitude = zonePoint.altitude;
    }
    
    public static double angle2azimuth(double n) {
        n = -((n - 1.5707963267948966) * 57.29577951308232);
        double n2;
        while (true) {
            n2 = n;
            if (n >= 0.0) {
                break;
            }
            n += 360.0;
        }
        while (n2 >= 360.0) {
            n2 -= 360.0;
        }
        return n2;
    }
    
    public static double azimuth2angle(double n) {
        n = -(0.017453292519943295 * n) + 1.5707963267948966;
        double n2;
        while (true) {
            n2 = n;
            if (n <= 3.141592653589793) {
                break;
            }
            n -= 6.283185307179586;
        }
        while (n2 <= -3.141592653589793) {
            n2 += 6.283185307179586;
        }
        return n2;
    }
    
    public static double convertDistanceFrom(final double n, final String s) {
        double n2 = n;
        if (s != null) {
            n2 = n;
            if (ZonePoint.conversions.containsKey(s)) {
                n2 = n * ZonePoint.conversions.get(s);
            }
        }
        return n2;
    }
    
    public static double convertDistanceTo(final double n, final String s) {
        double n2 = n;
        if (s != null) {
            n2 = n;
            if (ZonePoint.conversions.containsKey(s)) {
                n2 = n / ZonePoint.conversions.get(s);
            }
        }
        return n2;
    }
    
    public static ZonePoint copy(ZonePoint zonePoint) {
        if (zonePoint == null) {
            zonePoint = null;
        }
        else {
            zonePoint = new ZonePoint(zonePoint);
        }
        return zonePoint;
    }
    
    public static double distance(double abs, double abs2, final double n, final double n2) {
        abs = Math.abs(lat2m(abs - n));
        abs2 = Math.abs(lon2m(n, abs2 - n2));
        return Math.sqrt(abs * abs + abs2 * abs2);
    }
    
    public static double lat2m(final double n) {
        return 110940.00000395167 * n;
    }
    
    public static double lon2m(final double n, final double n2) {
        return n2 * 0.017453292519943295 * Math.cos(0.017453292519943295 * n) * 6367449.0;
    }
    
    public static double m2lat(final double n) {
        return 9.013881377E-6 * n;
    }
    
    public static double m2lon(final double n, final double n2) {
        return n2 / (Math.cos(n * 0.017453292519943295) * 0.017453292519943295 * 6367449.0);
    }
    
    public static String makeFriendlyAngle(final double n) {
        boolean b = false;
        double n2 = n;
        if (n < 0.0) {
            b = true;
            n2 = n * -1.0;
        }
        final int i = (int)n2;
        String str;
        final String s = str = String.valueOf((n2 - i) * 60.0);
        if (s.indexOf(46) != -1) {
            str = s.substring(0, Math.min(s.length(), s.indexOf(46) + 5));
        }
        final StringBuilder sb = new StringBuilder();
        String str2;
        if (b) {
            str2 = "- ";
        }
        else {
            str2 = "+ ";
        }
        return sb.append(str2).append(String.valueOf(i)).append("° ").append(str).toString();
    }
    
    public static String makeFriendlyDistance(double n) {
        String s;
        if (n > 1500.0) {
            n = (long)(n / 10.0) / 100.0;
            s = Double.toString(n) + " km";
        }
        else if (n > 100.0) {
            s = Double.toString((double)(long)n) + " m";
        }
        else {
            n = (long)(n * 100.0) / 100.0;
            s = Double.toString(n) + " m";
        }
        return s;
    }
    
    public static String makeFriendlyLatitude(final double n) {
        return makeFriendlyAngle(n).replace('+', 'N').replace('-', 'S');
    }
    
    public static String makeFriendlyLongitude(final double n) {
        return makeFriendlyAngle(n).replace('+', 'E').replace('-', 'W');
    }
    
    public double bearing(final double n, final double n2) {
        return MathLib.atan2(lat2m(this.latitude - n), lon2m(n, this.longitude - n2));
    }
    
    public double bearing(final ZonePoint zonePoint) {
        return this.bearing(zonePoint.latitude, zonePoint.longitude);
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        this.latitude = dataInputStream.readDouble();
        this.longitude = dataInputStream.readDouble();
        this.altitude = dataInputStream.readDouble();
    }
    
    public double distance(final double n, final double n2) {
        return distance(n, n2, this.latitude, this.longitude);
    }
    
    public double distance(final ZonePoint zonePoint) {
        return distance(zonePoint.latitude, zonePoint.longitude, this.latitude, this.longitude);
    }
    
    public String friendlyDistance(final double n, final double n2) {
        return makeFriendlyDistance(this.distance(n, n2));
    }
    
    @Override
    public LuaTable getMetatable() {
        return null;
    }
    
    @Override
    public int len() {
        return 3;
    }
    
    @Override
    public Object next(final Object o) {
        return null;
    }
    
    @Override
    public Object rawget(final Object o) {
        final Object o2 = null;
        Object o3;
        if (o == null) {
            o3 = o2;
        }
        else {
            final String string = o.toString();
            if ("latitude".equals(string)) {
                o3 = LuaState.toDouble(this.latitude);
            }
            else if ("longitude".equals(string)) {
                o3 = LuaState.toDouble(this.longitude);
            }
            else {
                o3 = o2;
                if ("altitude".equals(string)) {
                    o3 = LuaState.toDouble(this.altitude);
                }
            }
        }
        return o3;
    }
    
    @Override
    public void rawset(final Object o, final Object o2) {
        if (o != null) {
            final String string = o.toString();
            if ("latitude".equals(string)) {
                this.latitude = LuaState.fromDouble(o2);
            }
            else if ("longitude".equals(string)) {
                this.longitude = LuaState.fromDouble(o2);
            }
            else if ("altitude".equals(string)) {
                this.altitude = LuaState.fromDouble(o2);
            }
        }
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeDouble(this.latitude);
        dataOutputStream.writeDouble(this.longitude);
        dataOutputStream.writeDouble(this.altitude);
    }
    
    @Override
    public void setMetatable(final LuaTable luaTable) {
    }
    
    public void sync(final ZonePoint zonePoint) {
        this.latitude = zonePoint.latitude;
        this.longitude = zonePoint.longitude;
    }
    
    @Override
    public String toString() {
        return "ZonePoint(" + this.latitude + "," + this.longitude + "," + this.altitude + ")";
    }
    
    public ZonePoint translate(double m2lat, double m2lon) {
        final double azimuth2angle = azimuth2angle(m2lat);
        m2lat = m2lat(Math.sin(azimuth2angle) * m2lon);
        m2lon = m2lon(this.latitude, Math.cos(azimuth2angle) * m2lon);
        return new ZonePoint(this.latitude + m2lat, this.longitude + m2lon, this.altitude);
    }
    
    public void updateWeakSettings(final boolean b, final boolean b2) {
    }
}
