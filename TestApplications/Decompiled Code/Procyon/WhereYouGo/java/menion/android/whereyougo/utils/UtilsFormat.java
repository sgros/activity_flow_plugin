// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import android.location.Location;
import org.mapsforge.core.model.GeoPoint;
import menion.android.whereyougo.preferences.Preferences;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UtilsFormat
{
    private static final String TAG = "UtilsFormat";
    private static final SimpleDateFormat dateFormat;
    private static final SimpleDateFormat datetimeFormat;
    private static final String degreeSign = "°";
    private static Date mDate;
    private static final String minuteSign = "'";
    private static final String secondSign = "''";
    private static final SimpleDateFormat timeFormat;
    
    static {
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public static String addZeros(String string, final int n) {
        String s;
        if (string == null || string.length() > n) {
            s = string;
        }
        else {
            int length = string.length();
            while (true) {
                s = string;
                if (length >= n) {
                    break;
                }
                string = "0" + string;
                ++length;
            }
        }
        return s;
    }
    
    public static String formatAltitude(final double n, final boolean b) {
        return locus.api.android.utils.UtilsFormat.formatAltitude(Preferences.FORMAT_ALTITUDE, n, b);
    }
    
    public static String formatAngle(final double n) {
        return locus.api.android.utils.UtilsFormat.formatAngle(Preferences.FORMAT_ANGLE, (float)(n % 360.0 + 360.0) % 360.0f, false, 0);
    }
    
    public static String formatCooByType(final double n, final double n2, final boolean b) {
        final StringBuilder append = new StringBuilder().append(formatLatitude(n));
        String str;
        if (b) {
            str = "<br />";
        }
        else {
            str = " ";
        }
        return append.append(str).append(formatLongitude(n2)).toString();
    }
    
    private static String formatCooLatLon(final double d, final int i) {
        while (true) {
            try {
                String s;
                if (Preferences.FORMAT_COO_LATLON == 0) {
                    s = formatDouble(d, 5, i) + "°";
                }
                else if (Preferences.FORMAT_COO_LATLON == 1) {
                    final double floor = Math.floor(d);
                    s = formatDouble(floor, 0, 2) + "°" + formatDouble((d - floor) * 60.0, 3, 2) + "'";
                }
                else {
                    if (Preferences.FORMAT_COO_LATLON != 2) {
                        return "";
                    }
                    final double floor2 = Math.floor(d);
                    final double floor3 = Math.floor((d - floor2) * 60.0);
                    s = formatDouble(floor2, 0, 2) + "°" + formatDouble(floor3, 0, 2) + "'" + formatDouble((d - floor2 - floor3 / 60.0) * 3600.0, 3) + "''";
                }
                return s;
            }
            catch (Exception ex) {
                Logger.e("UtilsFormat", "formatCoordinates(" + d + ", " + i + "), e:" + ex.toString());
            }
            return "";
        }
    }
    
    public static String formatDate(final long time) {
        if (UtilsFormat.mDate == null) {
            UtilsFormat.mDate = new Date();
        }
        UtilsFormat.mDate.setTime(time);
        return UtilsFormat.dateFormat.format(UtilsFormat.mDate);
    }
    
    public static String formatDatetime(final long time) {
        if (UtilsFormat.mDate == null) {
            UtilsFormat.mDate = new Date();
        }
        UtilsFormat.mDate.setTime(time);
        return UtilsFormat.datetimeFormat.format(UtilsFormat.mDate);
    }
    
    public static String formatDistance(final double n, final boolean b) {
        return locus.api.android.utils.UtilsFormat.formatDistance(Preferences.FORMAT_LENGTH, n, b);
    }
    
    public static String formatDouble(final double n, final int n2) {
        return locus.api.android.utils.UtilsFormat.formatDouble(n, n2);
    }
    
    public static String formatDouble(final double n, final int n2, final int n3) {
        return locus.api.android.utils.UtilsFormat.formatDouble(n, n2, n3);
    }
    
    public static String formatGeoPoint(final GeoPoint geoPoint) {
        return formatCooByType(geoPoint.latitude, geoPoint.longitude, false);
    }
    
    public static String formatGeoPointDefault(final GeoPoint geoPoint) {
        return String.format("N %s E %s", Location.convert(geoPoint.latitude, 1).replace(":", "°"), Location.convert(geoPoint.longitude, 1).replace(":", "°"));
    }
    
    public static String formatLatitude(final double a) {
        final StringBuilder sb = new StringBuilder();
        String str;
        if (a < 0.0) {
            str = "S";
        }
        else {
            str = "N";
        }
        return sb.append(str).append(" ").append(formatCooLatLon(Math.abs(a), 2)).toString();
    }
    
    public static String formatLongitude(final double a) {
        final StringBuilder sb = new StringBuilder();
        String str;
        if (a < 0.0) {
            str = "W";
        }
        else {
            str = "E";
        }
        return sb.append(str).append(" ").append(formatCooLatLon(Math.abs(a), 3)).toString();
    }
    
    public static String formatSpeed(final double n, final boolean b) {
        return locus.api.android.utils.UtilsFormat.formatSpeed(Preferences.FORMAT_SPEED, n, b);
    }
    
    public static String formatTime(final long time) {
        if (UtilsFormat.mDate == null) {
            UtilsFormat.mDate = new Date();
        }
        UtilsFormat.mDate.setTime(time);
        return UtilsFormat.timeFormat.format(UtilsFormat.mDate);
    }
    
    public static String formatTime(final boolean b, final long n) {
        return formatTime(b, n, true);
    }
    
    public static String formatTime(final boolean b, final long n, final boolean b2) {
        final long n2 = n / 3600000L;
        final long n3 = (n - 3600000L * n2) / 60000L;
        final double n4 = (n - 3600000L * n2 - 60000L * n3) / 1000.0;
        String s;
        if (b) {
            if (b2) {
                s = n2 + "h:" + formatDouble((double)n3, 0, 2) + "m:" + formatDouble(n4, 0, 2) + "s";
            }
            else {
                s = formatDouble((double)n2, 0, 2) + ":" + formatDouble((double)n3, 0, 2) + ":" + formatDouble(n4, 0, 2);
            }
        }
        else if (n2 == 0L) {
            if (n3 == 0L) {
                if (b2) {
                    s = formatDouble(n4, 0) + "s";
                }
                else {
                    s = formatDouble(n4, 0, 2);
                }
            }
            else if (b2) {
                s = n3 + "m:" + formatDouble(n4, 0) + "s";
            }
            else {
                s = formatDouble((double)n3, 0, 2) + ":" + formatDouble(n4, 0, 2);
            }
        }
        else if (b2) {
            s = n2 + "h:" + n3 + "m";
        }
        else {
            s = formatDouble((double)n2, 0, 2) + ":" + formatDouble((double)n3, 0, 2) + ":" + formatDouble(n4, 0, 2);
        }
        return s;
    }
}
