// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import android.text.Html;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.text.DecimalFormat;

public class UtilsFormat
{
    public static final float ENERGY_CAL_TO_J = 4.185f;
    public static final double UNIT_KILOMETER_TO_METER = 1000.0;
    public static final double UNIT_METER_TO_FEET = 3.2808;
    public static final double UNIT_MILE_TO_METER = 1609.344;
    public static final double UNIT_NMILE_TO_METER = 1852.0;
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
    public static double angleInAngularMi;
    public static double angleInRussianMil;
    public static double angleInUsArttileryMil;
    private static DecimalFormat[][] formats;
    
    static {
        UtilsFormat.angleInAngularMi = 17.453292519943293;
        UtilsFormat.angleInRussianMil = 16.666666666666668;
        UtilsFormat.angleInUsArttileryMil = 17.77777777777778;
        UtilsFormat.formats = new DecimalFormat[][] { { new DecimalFormat("#"), new DecimalFormat("#.0"), new DecimalFormat("#.00"), new DecimalFormat("#.000"), new DecimalFormat("#.0000"), new DecimalFormat("#.00000"), new DecimalFormat("#.000000") }, { new DecimalFormat("#0"), new DecimalFormat("#0.0"), new DecimalFormat("#0.00"), new DecimalFormat("#0.000"), new DecimalFormat("#0.0000"), new DecimalFormat("#0.00000"), new DecimalFormat("#0.000000") }, { new DecimalFormat("#00"), new DecimalFormat("#00.0"), new DecimalFormat("#00.00"), new DecimalFormat("#00.000"), new DecimalFormat("#00.0000"), new DecimalFormat("#00.00000"), new DecimalFormat("#00.000000") }, { new DecimalFormat("#000"), new DecimalFormat("#000.0"), new DecimalFormat("#000.00"), new DecimalFormat("#000.000"), new DecimalFormat("#000.0000"), new DecimalFormat("#000.00000"), new DecimalFormat("#000.000000") }, { new DecimalFormat("#0000"), new DecimalFormat("#0000.0"), new DecimalFormat("#0000.00"), new DecimalFormat("#0000.000"), new DecimalFormat("#0000.0000"), new DecimalFormat("#0000.00000"), new DecimalFormat("#0000.000000") }, { new DecimalFormat("#00000"), new DecimalFormat("#00000.0"), new DecimalFormat("#00000.00"), new DecimalFormat("#00000.000"), new DecimalFormat("#00000.0000"), new DecimalFormat("#00000.00000"), new DecimalFormat("#00000.000000") }, { new DecimalFormat("#000000"), new DecimalFormat("#000000.0"), new DecimalFormat("#000000.00"), new DecimalFormat("#000000.000"), new DecimalFormat("#000000.0000"), new DecimalFormat("#000000.00000"), new DecimalFormat("#000000.000000") }, { new DecimalFormat("#0000000"), new DecimalFormat("#0000000.0"), new DecimalFormat("#0000000.00"), new DecimalFormat("#0000000.000"), new DecimalFormat("#0000000.0000"), new DecimalFormat("#0000000.00000"), new DecimalFormat("#0000000.000000") } };
        for (final DecimalFormat[] array : UtilsFormat.formats) {
            for (int length2 = array.length, j = 0; j < length2; ++j) {
                array[j].setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
            }
        }
    }
    
    public static String formatAltitude(final int n, final double n2, final boolean b) {
        String str = formatDouble(formatAltitudeValue(n, n2), 0);
        if (b) {
            str = str + " " + formatAltitudeUnits(n);
        }
        return str;
    }
    
    public static String formatAltitudeUnits(final int n) {
        String s;
        if (n == 1) {
            s = "ft";
        }
        else {
            s = "m";
        }
        return s;
    }
    
    public static double formatAltitudeValue(final int n, final double n2) {
        double n3 = n2;
        if (n == 1) {
            n3 = n2 * 3.2808;
        }
        return n3;
    }
    
    public static String formatAngle(final int n, final float n2, final boolean b, final int n3) {
        return formatDouble(formatAngleValue(n, n2, b, n3), n3) + formatAngleUnits(n);
    }
    
    public static String formatAngleUnits(final int n) {
        String s;
        if (n == 0) {
            s = "°";
        }
        else {
            s = "";
        }
        return s;
    }
    
    public static double formatAngleValue(final int n, double n2, final boolean b, final int n3) {
        double optimizeAngleValue = n2;
        if (b) {
            optimizeAngleValue = optimizeAngleValue(n2, n3);
        }
        if (n == 1) {
            n2 = optimizeAngleValue * UtilsFormat.angleInAngularMi;
        }
        else if (n == 2) {
            n2 = optimizeAngleValue * UtilsFormat.angleInRussianMil;
        }
        else {
            n2 = optimizeAngleValue;
            if (n == 3) {
                n2 = optimizeAngleValue * UtilsFormat.angleInUsArttileryMil;
            }
        }
        return n2;
    }
    
    public static CharSequence formatArea(final int n, final double n2, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        if (!b) {
            sb.append(formatAreaValue(n, n2));
        }
        else {
            sb.append(formatAreaValue(n, n2)).append(" ").append(formatAreaUnit(n, n2));
        }
        return (CharSequence)Html.fromHtml(sb.toString());
    }
    
    public static CharSequence formatAreaUnit(final int n, final double n2) {
        String s = "";
        switch (n) {
            case 0: {
                s = "m&sup2;";
                break;
            }
            case 1: {
                s = "ha";
                break;
            }
            case 2: {
                s = "km&sup2;";
                break;
            }
            case 3: {
                s = "ft&sup2;";
                break;
            }
            case 4: {
                s = "yd&sup2;";
                break;
            }
            case 5: {
                s = "acre";
                break;
            }
            case 6: {
                s = "mi&sup2;";
                break;
            }
            case 7: {
                s = "nm&sup2;";
                break;
            }
        }
        return (CharSequence)Html.fromHtml(s);
    }
    
    public static String formatAreaValue(final int n, double n2) {
        Label_0069: {
            String s = null;
            switch (n) {
                default: {
                    s = "";
                    break;
                }
                case 0: {
                    s = formatDouble(n2, 0);
                    break;
                }
                case 1: {
                    n2 /= 10000.0;
                    break Label_0069;
                }
                case 2: {
                    n2 /= 1000000.0;
                    break Label_0069;
                }
                case 3: {
                    s = formatDouble(n2 / 0.09290304, 0);
                    break;
                }
                case 4: {
                    s = formatDouble(n2 / 0.83612736, 0);
                    break;
                }
                case 5: {
                    n2 /= 4046.8564224;
                    break Label_0069;
                }
                case 6: {
                    n2 /= 2589988.110336;
                    break Label_0069;
                }
                case 7: {
                    n2 /= 3429904.0;
                    break Label_0069;
                }
            }
            return s;
        }
        if (n2 < 100.0) {
            return formatDouble(n2, 2);
        }
        if (n2 < 1000.0) {
            return formatDouble(n2, 1);
        }
        return formatDouble(n2, 0);
    }
    
    private static String formatDistance(final double n, int n2) {
        String s;
        if (n < 10.0) {
            s = formatDouble(n, n2);
        }
        else {
            if (n2 > 0) {
                --n2;
            }
            else {
                n2 = 0;
            }
            s = formatDouble(n, n2);
        }
        return s;
    }
    
    public static String formatDistance(final int n, final double n2, final UnitsPrecision unitsPrecision, final boolean b) {
        final String s = null;
        String str = null;
        Label_0051: {
            switch (n) {
                default: {
                    str = s;
                    break;
                }
                case 0: {
                    switch (unitsPrecision) {
                        default: {
                            str = s;
                            break Label_0051;
                        }
                        case LOW:
                        case MEDIUM: {
                            str = formatDouble(n2, 0);
                            break Label_0051;
                        }
                        case HIGH: {
                            str = formatDouble(n2, 1);
                            break Label_0051;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (unitsPrecision) {
                        default: {
                            str = s;
                            break Label_0051;
                        }
                        case LOW:
                        case MEDIUM: {
                            if (n2 < 1000.0) {
                                str = formatDistance(n2, 0);
                                break Label_0051;
                            }
                            final double n3 = n2 / 1000.0;
                            if (n3 >= 100.0) {
                                str = formatDouble(n3, 0);
                                break Label_0051;
                            }
                            str = formatDouble(n3, 1);
                            break Label_0051;
                        }
                        case HIGH: {
                            if (n2 < 1000.0) {
                                str = formatDouble(n2, 1);
                                break Label_0051;
                            }
                            final double n4 = n2 / 1000.0;
                            if (n4 >= 100.0) {
                                str = formatDouble(n4, 1);
                                break Label_0051;
                            }
                            str = formatDouble(n4, 2);
                            break Label_0051;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (unitsPrecision) {
                        default: {
                            str = s;
                            break Label_0051;
                        }
                        case LOW:
                        case MEDIUM: {
                            str = formatDistance(3.2808 * n2, 0);
                            break Label_0051;
                        }
                        case HIGH: {
                            str = formatDistance(3.2808 * n2, 1);
                            break Label_0051;
                        }
                    }
                    break;
                }
                case 3: {
                    str = formatDistanceImperial(n2, 3.2808 * n2, unitsPrecision);
                    break;
                }
                case 4: {
                    switch (unitsPrecision) {
                        default: {
                            str = s;
                            break Label_0051;
                        }
                        case LOW:
                        case MEDIUM: {
                            str = formatDistance(1.0936 * n2, 0);
                            break Label_0051;
                        }
                        case HIGH: {
                            str = formatDistance(1.0936 * n2, 1);
                            break Label_0051;
                        }
                    }
                    break;
                }
                case 5: {
                    str = formatDistanceImperial(n2, 1.0936 * n2, unitsPrecision);
                    break;
                }
                case 6: {
                    switch (unitsPrecision) {
                        default: {
                            str = s;
                            break Label_0051;
                        }
                        case LOW:
                        case MEDIUM: {
                            if (n2 <= 1852.0) {
                                str = formatDistance(n2, 0);
                                break Label_0051;
                            }
                            final double n5 = n2 / 1852.0;
                            if (n5 >= 100.0) {
                                str = formatDouble(n5, 0);
                                break Label_0051;
                            }
                            str = formatDouble(n5, 1);
                            break Label_0051;
                        }
                        case HIGH: {
                            if (n2 <= 1852.0) {
                                str = formatDistance(n2, 1);
                                break Label_0051;
                            }
                            final double n6 = n2 / 1852.0;
                            if (n6 >= 100.0) {
                                str = formatDouble(n6, 1);
                                break Label_0051;
                            }
                            str = formatDouble(n6, 2);
                            break Label_0051;
                        }
                    }
                    break;
                }
            }
        }
        String string = str;
        if (b) {
            string = str + " " + formatDistanceUnits(n, n2);
        }
        return string;
    }
    
    public static String formatDistance(final int n, final double n2, final boolean b) {
        return formatDistance(n, n2, UnitsPrecision.MEDIUM, !b);
    }
    
    private static String formatDistanceImperial(double n, final double n2, final UnitsPrecision unitsPrecision) {
        String s = null;
        switch (unitsPrecision) {
            default: {
                s = "";
                break;
            }
            case LOW:
            case MEDIUM: {
                if (n2 < 1000.0) {
                    s = formatDistance(n2, 0);
                    break;
                }
                n /= 1609.344;
                if (n >= 100.0) {
                    s = formatDouble(n, 0);
                    break;
                }
                if (n >= 1.0) {
                    s = formatDouble(n, 1);
                    break;
                }
                s = formatDouble(n, 2);
                break;
            }
            case HIGH: {
                if (n2 < 1000.0) {
                    s = formatDistance(n2, 1);
                    break;
                }
                n /= 1609.344;
                if (n >= 100.0) {
                    s = formatDouble(n, 1);
                    break;
                }
                if (n >= 1.0) {
                    s = formatDouble(n, 2);
                    break;
                }
                s = formatDouble(n, 3);
                break;
            }
        }
        return s;
    }
    
    public static String formatDistanceUnits(final int n, final double n2) {
        String s = null;
        switch (n) {
            default: {
                s = "";
                break;
            }
            case 0: {
                s = "m";
                break;
            }
            case 1: {
                if (n2 >= 1000.0) {
                    s = "km";
                    break;
                }
                s = "m";
                break;
            }
            case 2: {
                s = " ft";
                break;
            }
            case 3: {
                if (n2 * 3.2808 >= 1000.0) {
                    s = "mi";
                    break;
                }
                s = "ft";
                break;
            }
            case 4: {
                s = "yd";
                break;
            }
            case 5: {
                if (n2 * 1.0936 >= 1000.0) {
                    s = "mi";
                    break;
                }
                s = "yd";
                break;
            }
            case 6: {
                if (n2 >= 1852.0) {
                    s = "nmi";
                    break;
                }
                s = "m";
                break;
            }
        }
        return s;
    }
    
    public static double formatDistanceValue(final int n, final double n2) {
        double n3 = n2;
        switch (n) {
            default: {
                n3 = n2;
                return n3;
            }
            case 4: {
                n3 = n2 * 1.0936;
                return n3;
            }
            case 2: {
                n3 = n2 * 3.2808;
            }
            case 0: {
                return n3;
            }
            case 1: {
                n3 = n2;
                if (n2 >= 1000.0) {
                    n3 = n2 / 1000.0;
                    return n3;
                }
                return n3;
            }
            case 3: {
                n3 = n2 * 3.2808;
                if (n3 >= 1000.0) {
                    n3 = n2 / 1609.344;
                    return n3;
                }
                return n3;
            }
            case 5: {
                n3 = n2 * 1.0936;
                if (n3 >= 1000.0) {
                    n3 = n2 / 1609.344;
                    return n3;
                }
                return n3;
            }
            case 6: {
                n3 = n2;
                if (n2 >= 1852.0) {
                    n3 = n2 / 1852.0;
                    return n3;
                }
                return n3;
            }
        }
    }
    
    public static String formatDouble(final double n, final int n2) {
        return formatDouble(n, n2, 1);
    }
    
    public static String formatDouble(final double number, final int n, int n2) {
        int n3;
        if (n2 < 0) {
            n3 = 0;
        }
        else if ((n3 = n2) > UtilsFormat.formats.length - 1) {
            n3 = UtilsFormat.formats.length - 1;
        }
        if (n < 0) {
            n2 = 0;
        }
        else if ((n2 = n) > UtilsFormat.formats[0].length - 1) {
            n2 = UtilsFormat.formats[0].length - 1;
        }
        return UtilsFormat.formats[n3][n2].format(number);
    }
    
    public static String formatEnergy(final int n, final int n2, final boolean b) {
        String str = formatEnergyValue(n, n2);
        if (b) {
            str = str + " " + formatEnergyUnit(n);
        }
        return str;
    }
    
    public static String formatEnergyUnit(final int n) {
        String s;
        if (n == 0) {
            s = "KJ";
        }
        else {
            s = "kcal";
        }
        return s;
    }
    
    public static String formatEnergyValue(final int n, final int n2) {
        String s;
        if (n == 0) {
            s = formatDouble(n2 * 1.0 / 1000.0, 0);
        }
        else {
            s = formatDouble(n2 * 1.0 / 4184.999942779541, 0);
        }
        return s;
    }
    
    public static String formatSlope(final int n, final double n2, final boolean b) {
        String str = formatSlopeValue(n, n2);
        if (b) {
            str = str + " " + formatSlopeUnit(n);
        }
        return str;
    }
    
    public static String formatSlopeUnit(final int n) {
        String s;
        if (n == 0) {
            s = "%";
        }
        else {
            s = "°";
        }
        return s;
    }
    
    public static String formatSlopeValue(final int n, final double a) {
        String s;
        if (n == 0) {
            s = formatDouble(100.0 * a, 0);
        }
        else {
            s = formatDouble(Math.atan(a) * 57.29577951308232, 0);
        }
        return s;
    }
    
    public static String formatSpeed(final int n, double formatSpeedValue, final boolean b) {
        String str;
        if (formatSpeedValue < 0.0) {
            str = "--";
        }
        else {
            formatSpeedValue = formatSpeedValue(n, formatSpeedValue);
            int n2;
            if (formatSpeedValue > 100.0) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
            str = formatDouble(formatSpeedValue, n2);
        }
        if (!b) {
            str = str + " " + formatSpeedUnits(n);
        }
        return str;
    }
    
    public static String formatSpeedUnits(final int n) {
        String s;
        if (n == 1) {
            s = "mi/h";
        }
        else if (n == 2) {
            s = "nmi/h";
        }
        else if (n == 3) {
            s = "kn";
        }
        else {
            s = "km/h";
        }
        return s;
    }
    
    public static double formatSpeedValue(final int n, double n2) {
        if (n == 1) {
            n2 *= 2.237;
        }
        else if (n == 2 || n == 3) {
            n2 *= 1.9438444924406046;
        }
        else {
            n2 *= 3.6;
        }
        return n2;
    }
    
    public static String formatTemperature(final int n, final float n2, final boolean b) {
        String str = formatTemperatureValue(n, n2);
        if (b) {
            str = str + " " + formatTemperatureUnit(n);
        }
        return str;
    }
    
    public static String formatTemperatureUnit(final int n) {
        String s;
        if (n == 0) {
            s = "°C";
        }
        else {
            s = "F";
        }
        return s;
    }
    
    public static String formatTemperatureValue(final int n, final float n2) {
        String s;
        if (n == 0) {
            s = formatDouble(n2, 1);
        }
        else {
            s = formatDouble(9.0f * n2 / 5.0f + 32.0f, 1);
        }
        return s;
    }
    
    public static String formatWeight(final int n, final double n2, final boolean b) {
        String str = formatWeightValue(n, n2);
        if (b) {
            str = str + " " + formatWeightUnit(n);
        }
        return str;
    }
    
    public static String formatWeightUnit(final int n) {
        String s;
        if (n == 0) {
            s = "kg";
        }
        else if (n == 1) {
            s = "lb";
        }
        else {
            s = "???";
        }
        return s;
    }
    
    public static String formatWeightValue(final int n, final double n2) {
        String s;
        if (n == 0) {
            s = formatDouble(1.0 * n2 / 1000.0, 1);
        }
        else {
            s = formatDouble(2.2046220302581787 * n2 / 1000.0, 1);
        }
        return s;
    }
    
    private static double optimizeAngleValue(double n, final int n2) {
        final int n3 = (int)Math.pow(10.0, n2);
        n = (double)Math.round(n3 * n);
        if (n2 == 0) {
            double n4 = n;
            if (n < -0.5) {
                n4 = n + 360.0;
            }
            n = n4;
            if (n4 >= 359.5) {
                n = n4 - 360.0;
            }
        }
        else {
            double n5 = n;
            if (n < 0.0) {
                n5 = n + n3 * 360.0;
            }
            n = n5;
            if (n5 >= n3 * 360.0) {
                n = n5 - 360.0f * n3;
            }
        }
        return n / n3;
    }
    
    public enum UnitsPrecision
    {
        HIGH, 
        LOW, 
        MEDIUM;
    }
}
