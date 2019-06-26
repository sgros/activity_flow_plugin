package menion.android.whereyougo.utils;

import android.location.Location;
import java.text.SimpleDateFormat;
import java.util.Date;
import menion.android.whereyougo.preferences.Preferences;
import org.mapsforge.core.model.GeoPoint;

public class UtilsFormat {
    private static final String TAG = "UtilsFormat";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String degreeSign = "°";
    private static Date mDate = null;
    private static final String minuteSign = "'";
    private static final String secondSign = "''";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String formatAltitude(double altitude, boolean addUnits) {
        return locus.api.android.utils.UtilsFormat.formatAltitude(Preferences.FORMAT_ALTITUDE, altitude, addUnits);
    }

    public static String formatAngle(double angle) {
        return locus.api.android.utils.UtilsFormat.formatAngle(Preferences.FORMAT_ANGLE, ((float) ((angle % 360.0d) + 360.0d)) % 360.0f, false, 0);
    }

    public static String formatSpeed(double speed, boolean withoutUnits) {
        return locus.api.android.utils.UtilsFormat.formatSpeed(Preferences.FORMAT_SPEED, speed, withoutUnits);
    }

    public static String formatDistance(double dist, boolean withoutUnits) {
        return locus.api.android.utils.UtilsFormat.formatDistance(Preferences.FORMAT_LENGTH, dist, withoutUnits);
    }

    public static String formatDouble(double value, int precision) {
        return locus.api.android.utils.UtilsFormat.formatDouble(value, precision);
    }

    public static String formatDouble(double value, int precision, int minlen) {
        return locus.api.android.utils.UtilsFormat.formatDouble(value, precision, minlen);
    }

    public static String addZeros(String text, int count) {
        if (text == null || text.length() > count) {
            return text;
        }
        String res = text;
        for (int i = res.length(); i < count; i++) {
            res = "0" + res;
        }
        return res;
    }

    public static String formatLatitude(double latitude) {
        return (latitude < 0.0d ? "S" : "N") + " " + formatCooLatLon(Math.abs(latitude), 2);
    }

    public static String formatLongitude(double longitude) {
        return (longitude < 0.0d ? "W" : "E") + " " + formatCooLatLon(Math.abs(longitude), 3);
    }

    public static String formatCooByType(double lat, double lon, boolean twoLines) {
        return formatLatitude(lat) + (twoLines ? "<br />" : " ") + formatLongitude(lon);
    }

    private static String formatCooLatLon(double value, int minLen) {
        try {
            if (Preferences.FORMAT_COO_LATLON == 0) {
                return formatDouble(value, 5, minLen) + degreeSign;
            }
            double deg;
            if (Preferences.FORMAT_COO_LATLON == 1) {
                deg = Math.floor(value);
                return formatDouble(deg, 0, 2) + degreeSign + formatDouble((value - deg) * 60.0d, 3, 2) + minuteSign;
            }
            if (Preferences.FORMAT_COO_LATLON == 2) {
                deg = Math.floor(value);
                double min = Math.floor((value - deg) * 60.0d);
                return formatDouble(deg, 0, 2) + degreeSign + formatDouble(min, 0, 2) + minuteSign + formatDouble(((value - deg) - (min / 60.0d)) * 3600.0d, 3) + secondSign;
            }
            return "";
        } catch (Exception e) {
            Logger.m21e(TAG, "formatCoordinates(" + value + ", " + minLen + "), e:" + e.toString());
        }
    }

    public static String formatGeoPoint(GeoPoint geoPoint) {
        return formatCooByType(geoPoint.latitude, geoPoint.longitude, false);
    }

    public static String formatGeoPointDefault(GeoPoint geoPoint) {
        String strLatitude = Location.convert(geoPoint.latitude, 1).replace(":", degreeSign);
        String strLongitude = Location.convert(geoPoint.longitude, 1).replace(":", degreeSign);
        return String.format("N %s E %s", new Object[]{strLatitude, strLongitude});
    }

    public static String formatTime(long time) {
        if (mDate == null) {
            mDate = new Date();
        }
        mDate.setTime(time);
        return timeFormat.format(mDate);
    }

    public static String formatDate(long time) {
        if (mDate == null) {
            mDate = new Date();
        }
        mDate.setTime(time);
        return dateFormat.format(mDate);
    }

    public static String formatDatetime(long time) {
        if (mDate == null) {
            mDate = new Date();
        }
        mDate.setTime(time);
        return datetimeFormat.format(mDate);
    }

    public static String formatTime(boolean full, long tripTime) {
        return formatTime(full, tripTime, true);
    }

    public static String formatTime(boolean full, long tripTime, boolean withUnits) {
        long hours = tripTime / 3600000;
        long mins = (tripTime - (3600000 * hours)) / 60000;
        double sec = ((double) ((tripTime - (3600000 * hours)) - (60000 * mins))) / 1000.0d;
        if (full) {
            if (withUnits) {
                return hours + "h:" + formatDouble((double) mins, 0, 2) + "m:" + formatDouble(sec, 0, 2) + "s";
            }
            return formatDouble((double) hours, 0, 2) + ":" + formatDouble((double) mins, 0, 2) + ":" + formatDouble(sec, 0, 2);
        } else if (hours == 0) {
            if (mins == 0) {
                if (withUnits) {
                    return formatDouble(sec, 0) + "s";
                }
                return formatDouble(sec, 0, 2);
            } else if (withUnits) {
                return mins + "m:" + formatDouble(sec, 0) + "s";
            } else {
                return formatDouble((double) mins, 0, 2) + ":" + formatDouble(sec, 0, 2);
            }
        } else if (withUnits) {
            return hours + "h:" + mins + "m";
        } else {
            return formatDouble((double) hours, 0, 2) + ":" + formatDouble((double) mins, 0, 2) + ":" + formatDouble(sec, 0, 2);
        }
    }
}
