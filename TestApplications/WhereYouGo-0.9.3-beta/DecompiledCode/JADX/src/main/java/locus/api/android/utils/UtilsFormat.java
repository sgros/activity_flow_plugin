package locus.api.android.utils;

import android.text.Html;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UtilsFormat {
    public static final float ENERGY_CAL_TO_J = 4.185f;
    public static final double UNIT_KILOMETER_TO_METER = 1000.0d;
    public static final double UNIT_METER_TO_FEET = 3.2808d;
    public static final double UNIT_MILE_TO_METER = 1609.344d;
    public static final double UNIT_NMILE_TO_METER = 1852.0d;
    public static final int VALUE_UNITS_ALTITUDE_FEET = 1;
    public static final int VALUE_UNITS_ALTITUDE_METRES = 0;
    public static final int VALUE_UNITS_ANGLE_ANGULAR_MIL = 1;
    public static final int VALUE_UNITS_ANGLE_DEGREE = 0;
    public static final int VALUE_UNITS_ANGLE_RUSSIAN_MIL = 2;
    public static final int VALUE_UNITS_ANGLE_US_ARTILLERY_MIL = 3;
    public static final int VALUE_UNITS_AREA_ACRE = 5;
    public static final int VALUE_UNITS_AREA_FT_SQ = 3;
    public static final int VALUE_UNITS_AREA_HA = 1;
    public static final int VALUE_UNITS_AREA_KM_SQ = 2;
    public static final int VALUE_UNITS_AREA_MI_SQ = 6;
    public static final int VALUE_UNITS_AREA_M_SQ = 0;
    public static final int VALUE_UNITS_AREA_NM_SQ = 7;
    public static final int VALUE_UNITS_AREA_YA_SQ = 4;
    public static final int VALUE_UNITS_DISTANCE_IM_F = 2;
    public static final int VALUE_UNITS_DISTANCE_IM_FM = 3;
    public static final int VALUE_UNITS_DISTANCE_IM_Y = 4;
    public static final int VALUE_UNITS_DISTANCE_IM_YM = 5;
    public static final int VALUE_UNITS_DISTANCE_ME_M = 0;
    public static final int VALUE_UNITS_DISTANCE_ME_MKM = 1;
    public static final int VALUE_UNITS_DISTANCE_NA_MNMI = 6;
    public static final int VALUE_UNITS_ENERGY_KCAL = 1;
    public static final int VALUE_UNITS_ENERGY_KJ = 0;
    public static final int VALUE_UNITS_SLOPE_DEGREE = 1;
    public static final int VALUE_UNITS_SLOPE_PERCENT = 0;
    public static final int VALUE_UNITS_SPEED_KMH = 0;
    public static final int VALUE_UNITS_SPEED_KNOT = 3;
    public static final int VALUE_UNITS_SPEED_MILH = 1;
    public static final int VALUE_UNITS_SPEED_NMIH = 2;
    public static final int VALUE_UNITS_TEMPERATURE_CELSIUS = 0;
    public static final int VALUE_UNITS_TEMPERATURE_FAHRENHEIT = 1;
    public static final int VALUE_UNITS_WEIGHT_KG = 0;
    public static final int VALUE_UNITS_WEIGHT_LB = 1;
    public static final float WEIGHT_KG_TO_LB = 2.204622f;
    public static double angleInAngularMi = 17.453292519943293d;
    public static double angleInRussianMil = 16.666666666666668d;
    public static double angleInUsArttileryMil = 17.77777777777778d;
    private static DecimalFormat[][] formats;

    public enum UnitsPrecision {
        LOW,
        MEDIUM,
        HIGH
    }

    public static String formatAltitude(int format, double altitude, boolean addUnits) {
        String res = formatDouble(formatAltitudeValue(format, altitude), 0);
        if (addUnits) {
            return res + " " + formatAltitudeUnits(format);
        }
        return res;
    }

    public static double formatAltitudeValue(int format, double altitude) {
        if (format == 1) {
            return altitude * 3.2808d;
        }
        return altitude;
    }

    public static String formatAltitudeUnits(int format) {
        if (format == 1) {
            return "ft";
        }
        return "m";
    }

    static {
        r2 = new DecimalFormat[8][];
        r2[0] = new DecimalFormat[]{new DecimalFormat("#"), new DecimalFormat("#.0"), new DecimalFormat("#.00"), new DecimalFormat("#.000"), new DecimalFormat("#.0000"), new DecimalFormat("#.00000"), new DecimalFormat("#.000000")};
        r2[1] = new DecimalFormat[]{new DecimalFormat("#0"), new DecimalFormat("#0.0"), new DecimalFormat("#0.00"), new DecimalFormat("#0.000"), new DecimalFormat("#0.0000"), new DecimalFormat("#0.00000"), new DecimalFormat("#0.000000")};
        r2[2] = new DecimalFormat[]{new DecimalFormat("#00"), new DecimalFormat("#00.0"), new DecimalFormat("#00.00"), new DecimalFormat("#00.000"), new DecimalFormat("#00.0000"), new DecimalFormat("#00.00000"), new DecimalFormat("#00.000000")};
        r2[3] = new DecimalFormat[]{new DecimalFormat("#000"), new DecimalFormat("#000.0"), new DecimalFormat("#000.00"), new DecimalFormat("#000.000"), new DecimalFormat("#000.0000"), new DecimalFormat("#000.00000"), new DecimalFormat("#000.000000")};
        r2[4] = new DecimalFormat[]{new DecimalFormat("#0000"), new DecimalFormat("#0000.0"), new DecimalFormat("#0000.00"), new DecimalFormat("#0000.000"), new DecimalFormat("#0000.0000"), new DecimalFormat("#0000.00000"), new DecimalFormat("#0000.000000")};
        r2[5] = new DecimalFormat[]{new DecimalFormat("#00000"), new DecimalFormat("#00000.0"), new DecimalFormat("#00000.00"), new DecimalFormat("#00000.000"), new DecimalFormat("#00000.0000"), new DecimalFormat("#00000.00000"), new DecimalFormat("#00000.000000")};
        r2[6] = new DecimalFormat[]{new DecimalFormat("#000000"), new DecimalFormat("#000000.0"), new DecimalFormat("#000000.00"), new DecimalFormat("#000000.000"), new DecimalFormat("#000000.0000"), new DecimalFormat("#000000.00000"), new DecimalFormat("#000000.000000")};
        r2[7] = new DecimalFormat[]{new DecimalFormat("#0000000"), new DecimalFormat("#0000000.0"), new DecimalFormat("#0000000.00"), new DecimalFormat("#0000000.000"), new DecimalFormat("#0000000.0000"), new DecimalFormat("#0000000.00000"), new DecimalFormat("#0000000.000000")};
        formats = r2;
        for (DecimalFormat[] format : formats) {
            for (DecimalFormat aFormat : r5[r4]) {
                aFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
            }
        }
    }

    public static String formatAngle(int format, float angle, boolean optimize, int minAccuracy) {
        double resValue = formatAngleValue(format, (double) angle, optimize, minAccuracy);
        return formatDouble(resValue, minAccuracy) + formatAngleUnits(format);
    }

    public static double formatAngleValue(int format, double angle, boolean optimize, int minAccuracy) {
        if (optimize) {
            angle = optimizeAngleValue(angle, minAccuracy);
        }
        if (format == 1) {
            return angle * angleInAngularMi;
        }
        if (format == 2) {
            return angle * angleInRussianMil;
        }
        if (format == 3) {
            return angle * angleInUsArttileryMil;
        }
        return angle;
    }

    public static String formatAngleUnits(int format) {
        if (format == 0) {
            return "°";
        }
        return "";
    }

    private static double optimizeAngleValue(double angle, int minAccuracy) {
        int divider = (int) Math.pow(10.0d, (double) minAccuracy);
        angle = (double) Math.round(((double) divider) * angle);
        if (minAccuracy == 0) {
            if (angle < -0.5d) {
                angle += 360.0d;
            }
            if (angle >= 359.5d) {
                angle -= 360.0d;
            }
        } else {
            if (angle < 0.0d) {
                angle += ((double) divider) * 360.0d;
            }
            if (angle >= ((double) divider) * 360.0d) {
                angle -= (double) (360.0f * ((float) divider));
            }
        }
        return angle / ((double) divider);
    }

    public static CharSequence formatArea(int format, double area, boolean addUnits) {
        StringBuilder sb = new StringBuilder();
        if (addUnits) {
            sb.append(formatAreaValue(format, area)).append(" ").append(formatAreaUnit(format, area));
        } else {
            sb.append(formatAreaValue(format, area));
        }
        return Html.fromHtml(sb.toString());
    }

    public static String formatAreaValue(int format, double area) {
        switch (format) {
            case 0:
                return formatDouble(area, 0);
            case 1:
                area /= 10000.0d;
                break;
            case 2:
                area /= 1000000.0d;
                break;
            case 3:
                return formatDouble(area / 0.09290304d, 0);
            case 4:
                return formatDouble(area / 0.83612736d, 0);
            case 5:
                area /= 4046.8564224d;
                break;
            case 6:
                area /= 2589988.110336d;
                break;
            case 7:
                area /= 3429904.0d;
                break;
            default:
                return "";
        }
        if (area < 100.0d) {
            return formatDouble(area, 2);
        }
        if (area < 1000.0d) {
            return formatDouble(area, 1);
        }
        return formatDouble(area, 0);
    }

    public static CharSequence formatAreaUnit(int unitType, double area) {
        String text = "";
        switch (unitType) {
            case 0:
                text = "m&sup2;";
                break;
            case 1:
                text = "ha";
                break;
            case 2:
                text = "km&sup2;";
                break;
            case 3:
                text = "ft&sup2;";
                break;
            case 4:
                text = "yd&sup2;";
                break;
            case 5:
                text = "acre";
                break;
            case 6:
                text = "mi&sup2;";
                break;
            case 7:
                text = "nm&sup2;";
                break;
        }
        return Html.fromHtml(text);
    }

    public static String formatDistance(int unitType, double dist, boolean withoutUnits) {
        return formatDistance(unitType, dist, UnitsPrecision.MEDIUM, !withoutUnits);
    }

    public static String formatDistance(int unitType, double dist, UnitsPrecision precision, boolean addUnits) {
        String value = null;
        switch (unitType) {
            case 0:
                switch (precision) {
                    case LOW:
                    case MEDIUM:
                        value = formatDouble(dist, 0);
                        break;
                    case HIGH:
                        value = formatDouble(dist, 1);
                        break;
                }
                break;
            case 1:
                double km;
                switch (precision) {
                    case LOW:
                    case MEDIUM:
                        if (dist < 1000.0d) {
                            value = formatDistance(dist, 0);
                            break;
                        }
                        km = dist / 1000.0d;
                        if (km < 100.0d) {
                            value = formatDouble(km, 1);
                            break;
                        }
                        value = formatDouble(km, 0);
                        break;
                    case HIGH:
                        if (dist < 1000.0d) {
                            value = formatDouble(dist, 1);
                            break;
                        }
                        km = dist / 1000.0d;
                        if (km < 100.0d) {
                            value = formatDouble(km, 2);
                            break;
                        }
                        value = formatDouble(km, 1);
                        break;
                }
                break;
            case 2:
                switch (precision) {
                    case LOW:
                    case MEDIUM:
                        value = formatDistance(3.2808d * dist, 0);
                        break;
                    case HIGH:
                        value = formatDistance(3.2808d * dist, 1);
                        break;
                }
                break;
            case 3:
                value = formatDistanceImperial(dist, 3.2808d * dist, precision);
                break;
            case 4:
                switch (precision) {
                    case LOW:
                    case MEDIUM:
                        value = formatDistance(1.0936d * dist, 0);
                        break;
                    case HIGH:
                        value = formatDistance(1.0936d * dist, 1);
                        break;
                }
                break;
            case 5:
                value = formatDistanceImperial(dist, 1.0936d * dist, precision);
                break;
            case 6:
                double nmi;
                switch (precision) {
                    case LOW:
                    case MEDIUM:
                        if (dist <= 1852.0d) {
                            value = formatDistance(dist, 0);
                            break;
                        }
                        nmi = dist / 1852.0d;
                        if (nmi < 100.0d) {
                            value = formatDouble(nmi, 1);
                            break;
                        }
                        value = formatDouble(nmi, 0);
                        break;
                    case HIGH:
                        if (dist <= 1852.0d) {
                            value = formatDistance(dist, 1);
                            break;
                        }
                        nmi = dist / 1852.0d;
                        if (nmi < 100.0d) {
                            value = formatDouble(nmi, 2);
                            break;
                        }
                        value = formatDouble(nmi, 1);
                        break;
                }
                break;
        }
        if (addUnits) {
            return value + " " + formatDistanceUnits(unitType, dist);
        }
        return value;
    }

    private static String formatDistance(double dist, int basePrecision) {
        if (dist < 10.0d) {
            return formatDouble(dist, basePrecision);
        }
        return formatDouble(dist, basePrecision > 0 ? basePrecision - 1 : 0);
    }

    private static String formatDistanceImperial(double dist, double distInUnit, UnitsPrecision precision) {
        double mi;
        switch (precision) {
            case LOW:
            case MEDIUM:
                if (distInUnit < 1000.0d) {
                    return formatDistance(distInUnit, 0);
                }
                mi = dist / 1609.344d;
                if (mi >= 100.0d) {
                    return formatDouble(mi, 0);
                }
                if (mi >= 1.0d) {
                    return formatDouble(mi, 1);
                }
                return formatDouble(mi, 2);
            case HIGH:
                if (distInUnit < 1000.0d) {
                    return formatDistance(distInUnit, 1);
                }
                mi = dist / 1609.344d;
                if (mi >= 100.0d) {
                    return formatDouble(mi, 1);
                }
                if (mi >= 1.0d) {
                    return formatDouble(mi, 2);
                }
                return formatDouble(mi, 3);
            default:
                return "";
        }
    }

    public static double formatDistanceValue(int unitType, double dist) {
        switch (unitType) {
            case 1:
                if (dist >= 1000.0d) {
                    return dist / 1000.0d;
                }
                return dist;
            case 2:
                return dist * 3.2808d;
            case 3:
                double feet = dist * 3.2808d;
                return feet >= 1000.0d ? dist / 1609.344d : feet;
            case 4:
                return dist * 1.0936d;
            case 5:
                double yards = dist * 1.0936d;
                return yards >= 1000.0d ? dist / 1609.344d : yards;
            case 6:
                if (dist >= 1852.0d) {
                    return dist / 1852.0d;
                }
                return dist;
            default:
                return dist;
        }
    }

    public static String formatDistanceUnits(int unitType, double dist) {
        switch (unitType) {
            case 0:
                return "m";
            case 1:
                if (dist >= 1000.0d) {
                    return "km";
                }
                return "m";
            case 2:
                return " ft";
            case 3:
                if (dist * 3.2808d >= 1000.0d) {
                    return "mi";
                }
                return "ft";
            case 4:
                return "yd";
            case 5:
                if (dist * 1.0936d >= 1000.0d) {
                    return "mi";
                }
                return "yd";
            case 6:
                if (dist >= 1852.0d) {
                    return "nmi";
                }
                return "m";
            default:
                return "";
        }
    }

    public static String formatEnergy(int unitType, int energy, boolean addUnits) {
        String res = formatEnergyValue(unitType, energy);
        if (addUnits) {
            return res + " " + formatEnergyUnit(unitType);
        }
        return res;
    }

    public static String formatEnergyValue(int format, int energy) {
        if (format == 0) {
            return formatDouble((((double) energy) * 1.0d) / 1000.0d, 0);
        }
        return formatDouble((((double) energy) * 1.0d) / 4184.999942779541d, 0);
    }

    public static String formatEnergyUnit(int format) {
        if (format == 0) {
            return "KJ";
        }
        return "kcal";
    }

    public static String formatSlope(int unitType, double slope, boolean addUnits) {
        String res = formatSlopeValue(unitType, slope);
        if (addUnits) {
            return res + " " + formatSlopeUnit(unitType);
        }
        return res;
    }

    public static String formatSlopeValue(int format, double slope) {
        if (format == 0) {
            return formatDouble(100.0d * slope, 0);
        }
        return formatDouble(Math.atan(slope) * 57.29577951308232d, 0);
    }

    public static String formatSlopeUnit(int format) {
        if (format == 0) {
            return "%";
        }
        return "°";
    }

    public static String formatSpeed(int unitType, double speed, boolean withoutUnits) {
        String result;
        if (speed < 0.0d) {
            result = "--";
        } else {
            speed = formatSpeedValue(unitType, speed);
            result = formatDouble(speed, speed > 100.0d ? 0 : 1);
        }
        return withoutUnits ? result : result + " " + formatSpeedUnits(unitType);
    }

    public static double formatSpeedValue(int format, double speed) {
        if (format == 1) {
            return speed * 2.237d;
        }
        if (format == 2 || format == 3) {
            return speed * 1.9438444924406046d;
        }
        return speed * 3.6d;
    }

    public static String formatSpeedUnits(int format) {
        if (format == 1) {
            return "mi/h";
        }
        if (format == 2) {
            return "nmi/h";
        }
        if (format == 3) {
            return "kn";
        }
        return "km/h";
    }

    public static String formatTemperature(int unitType, float tempC, boolean addUnits) {
        String res = formatTemperatureValue(unitType, tempC);
        if (addUnits) {
            return res + " " + formatTemperatureUnit(unitType);
        }
        return res;
    }

    public static String formatTemperatureValue(int format, float tempC) {
        if (format == 0) {
            return formatDouble((double) tempC, 1);
        }
        return formatDouble((double) (((9.0f * tempC) / 5.0f) + 32.0f), 1);
    }

    public static String formatTemperatureUnit(int format) {
        if (format == 0) {
            return "°C";
        }
        return "F";
    }

    public static String formatWeight(int unitType, double weight, boolean addUnits) {
        String res = formatWeightValue(unitType, weight);
        if (addUnits) {
            return res + " " + formatWeightUnit(unitType);
        }
        return res;
    }

    public static String formatWeightValue(int format, double weight) {
        if (format == 0) {
            return formatDouble((1.0d * weight) / 1000.0d, 1);
        }
        return formatDouble((2.2046220302581787d * weight) / 1000.0d, 1);
    }

    public static String formatWeightUnit(int format) {
        if (format == 0) {
            return "kg";
        }
        if (format == 1) {
            return "lb";
        }
        return "???";
    }

    public static String formatDouble(double value, int precision) {
        return formatDouble(value, precision, 1);
    }

    public static String formatDouble(double value, int precision, int minlen) {
        if (minlen < 0) {
            minlen = 0;
        } else if (minlen > formats.length - 1) {
            minlen = formats.length - 1;
        }
        if (precision < 0) {
            precision = 0;
        } else if (precision > formats[0].length - 1) {
            precision = formats[0].length - 1;
        }
        return formats[minlen][precision].format(value);
    }
}
