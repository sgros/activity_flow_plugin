// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.text.TextUtils;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Matrix;
import android.graphics.Bitmap$Config;
import android.content.res.AssetManager;
import java.util.Calendar;
import java.util.HashMap;
import android.graphics.Rect;
import android.graphics.Bitmap;

public class MrzRecognizer
{
    private static native Rect[][] binarizeAndFindCharacters(final Bitmap p0, final Bitmap p1);
    
    private static String capitalize(final String s) {
        if (s == null) {
            return null;
        }
        final char[] charArray = s.toCharArray();
        int i = 0;
        int n = 1;
        while (i < charArray.length) {
            if (n == 0 && Character.isLetter(charArray[i])) {
                charArray[i] = Character.toLowerCase(charArray[i]);
            }
            else if (charArray[i] == ' ') {
                n = 1;
            }
            else {
                n = 0;
            }
            ++i;
        }
        return new String(charArray);
    }
    
    private static int checksum(final String s) {
        final char[] charArray = s.toCharArray();
        final int[] array2;
        final int[] array = array2 = new int[3];
        array2[0] = 7;
        array2[1] = 3;
        array2[2] = 1;
        int i = 0;
        int n = 0;
        while (i < charArray.length) {
            int n2;
            if (charArray[i] >= '0' && charArray[i] <= '9') {
                n2 = charArray[i] - '0';
            }
            else if (charArray[i] >= 'A' && charArray[i] <= 'Z') {
                n2 = charArray[i] - 'A' + 10;
            }
            else {
                n2 = 0;
            }
            n += n2 * array[i % array.length];
            ++i;
        }
        return n % 10;
    }
    
    private static String cyrillicToLatin(String replace) {
        final String[] array = new String[33];
        int i = 0;
        array[0] = "A";
        array[1] = "B";
        array[2] = "V";
        array[3] = "G";
        array[4] = "D";
        array[6] = (array[5] = "E");
        array[7] = "ZH";
        array[8] = "Z";
        array[10] = (array[9] = "I");
        array[11] = "K";
        array[12] = "L";
        array[13] = "M";
        array[14] = "N";
        array[15] = "O";
        array[16] = "P";
        array[17] = "R";
        array[18] = "S";
        array[19] = "T";
        array[20] = "U";
        array[21] = "F";
        array[22] = "KH";
        array[23] = "TS";
        array[24] = "CH";
        array[25] = "SH";
        array[26] = "SHCH";
        array[27] = "IE";
        array[28] = "Y";
        array[29] = "";
        array[30] = "E";
        array[31] = "IU";
        array[32] = "IA";
        while (i < array.length) {
            final int endIndex = i + 1;
            replace = replace.replace("\u0410\u0411\u0412\u0413\u0414\u0415\u0401\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f".substring(i, endIndex), array[i]);
            i = endIndex;
        }
        return replace;
    }
    
    private static native int[] findCornerPoints(final Bitmap p0);
    
    private static HashMap<String, String> getCountriesMap() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("AFG", "AF");
        hashMap.put("ALA", "AX");
        hashMap.put("ALB", "AL");
        hashMap.put("DZA", "DZ");
        hashMap.put("ASM", "AS");
        hashMap.put("AND", "AD");
        hashMap.put("AGO", "AO");
        hashMap.put("AIA", "AI");
        hashMap.put("ATA", "AQ");
        hashMap.put("ATG", "AG");
        hashMap.put("ARG", "AR");
        hashMap.put("ARM", "AM");
        hashMap.put("ABW", "AW");
        hashMap.put("AUS", "AU");
        hashMap.put("AUT", "AT");
        hashMap.put("AZE", "AZ");
        hashMap.put("BHS", "BS");
        hashMap.put("BHR", "BH");
        hashMap.put("BGD", "BD");
        hashMap.put("BRB", "BB");
        hashMap.put("BLR", "BY");
        hashMap.put("BEL", "BE");
        hashMap.put("BLZ", "BZ");
        hashMap.put("BEN", "BJ");
        hashMap.put("BMU", "BM");
        hashMap.put("BTN", "BT");
        hashMap.put("BOL", "BO");
        hashMap.put("BES", "BQ");
        hashMap.put("BIH", "BA");
        hashMap.put("BWA", "BW");
        hashMap.put("BVT", "BV");
        hashMap.put("BRA", "BR");
        hashMap.put("IOT", "IO");
        hashMap.put("BRN", "BN");
        hashMap.put("BGR", "BG");
        hashMap.put("BFA", "BF");
        hashMap.put("BDI", "BI");
        hashMap.put("CPV", "CV");
        hashMap.put("KHM", "KH");
        hashMap.put("CMR", "CM");
        hashMap.put("CAN", "CA");
        hashMap.put("CYM", "KY");
        hashMap.put("CAF", "CF");
        hashMap.put("TCD", "TD");
        hashMap.put("CHL", "CL");
        hashMap.put("CHN", "CN");
        hashMap.put("CXR", "CX");
        hashMap.put("CCK", "CC");
        hashMap.put("COL", "CO");
        hashMap.put("COM", "KM");
        hashMap.put("COG", "CG");
        hashMap.put("COD", "CD");
        hashMap.put("COK", "CK");
        hashMap.put("CRI", "CR");
        hashMap.put("CIV", "CI");
        hashMap.put("HRV", "HR");
        hashMap.put("CUB", "CU");
        hashMap.put("CUW", "CW");
        hashMap.put("CYP", "CY");
        hashMap.put("CZE", "CZ");
        hashMap.put("DNK", "DK");
        hashMap.put("DJI", "DJ");
        hashMap.put("DMA", "DM");
        hashMap.put("DOM", "DO");
        hashMap.put("ECU", "EC");
        hashMap.put("EGY", "EG");
        hashMap.put("SLV", "SV");
        hashMap.put("GNQ", "GQ");
        hashMap.put("ERI", "ER");
        hashMap.put("EST", "EE");
        hashMap.put("ETH", "ET");
        hashMap.put("FLK", "FK");
        hashMap.put("FRO", "FO");
        hashMap.put("FJI", "FJ");
        hashMap.put("FIN", "FI");
        hashMap.put("FRA", "FR");
        hashMap.put("GUF", "GF");
        hashMap.put("PYF", "PF");
        hashMap.put("ATF", "TF");
        hashMap.put("GAB", "GA");
        hashMap.put("GMB", "GM");
        hashMap.put("GEO", "GE");
        hashMap.put("D<<", "DE");
        hashMap.put("GHA", "GH");
        hashMap.put("GIB", "GI");
        hashMap.put("GRC", "GR");
        hashMap.put("GRL", "GL");
        hashMap.put("GRD", "GD");
        hashMap.put("GLP", "GP");
        hashMap.put("GUM", "GU");
        hashMap.put("GTM", "GT");
        hashMap.put("GGY", "GG");
        hashMap.put("GIN", "GN");
        hashMap.put("GNB", "GW");
        hashMap.put("GUY", "GY");
        hashMap.put("HTI", "HT");
        hashMap.put("HMD", "HM");
        hashMap.put("VAT", "VA");
        hashMap.put("HND", "HN");
        hashMap.put("HKG", "HK");
        hashMap.put("HUN", "HU");
        hashMap.put("ISL", "IS");
        hashMap.put("IND", "IN");
        hashMap.put("IDN", "ID");
        hashMap.put("IRN", "IR");
        hashMap.put("IRQ", "IQ");
        hashMap.put("IRL", "IE");
        hashMap.put("IMN", "IM");
        hashMap.put("ISR", "IL");
        hashMap.put("ITA", "IT");
        hashMap.put("JAM", "JM");
        hashMap.put("JPN", "JP");
        hashMap.put("JEY", "JE");
        hashMap.put("JOR", "JO");
        hashMap.put("KAZ", "KZ");
        hashMap.put("KEN", "KE");
        hashMap.put("KIR", "KI");
        hashMap.put("PRK", "KP");
        hashMap.put("KOR", "KR");
        hashMap.put("KWT", "KW");
        hashMap.put("KGZ", "KG");
        hashMap.put("LAO", "LA");
        hashMap.put("LVA", "LV");
        hashMap.put("LBN", "LB");
        hashMap.put("LSO", "LS");
        hashMap.put("LBR", "LR");
        hashMap.put("LBY", "LY");
        hashMap.put("LIE", "LI");
        hashMap.put("LTU", "LT");
        hashMap.put("LUX", "LU");
        hashMap.put("MAC", "MO");
        hashMap.put("MKD", "MK");
        hashMap.put("MDG", "MG");
        hashMap.put("MWI", "MW");
        hashMap.put("MYS", "MY");
        hashMap.put("MDV", "MV");
        hashMap.put("MLI", "ML");
        hashMap.put("MLT", "MT");
        hashMap.put("MHL", "MH");
        hashMap.put("MTQ", "MQ");
        hashMap.put("MRT", "MR");
        hashMap.put("MUS", "MU");
        hashMap.put("MYT", "YT");
        hashMap.put("MEX", "MX");
        hashMap.put("FSM", "FM");
        hashMap.put("MDA", "MD");
        hashMap.put("MCO", "MC");
        hashMap.put("MNG", "MN");
        hashMap.put("MNE", "ME");
        hashMap.put("MSR", "MS");
        hashMap.put("MAR", "MA");
        hashMap.put("MOZ", "MZ");
        hashMap.put("MMR", "MM");
        hashMap.put("NAM", "NA");
        hashMap.put("NRU", "NR");
        hashMap.put("NPL", "NP");
        hashMap.put("NLD", "NL");
        hashMap.put("NCL", "NC");
        hashMap.put("NZL", "NZ");
        hashMap.put("NIC", "NI");
        hashMap.put("NER", "NE");
        hashMap.put("NGA", "NG");
        hashMap.put("NIU", "NU");
        hashMap.put("NFK", "NF");
        hashMap.put("MNP", "MP");
        hashMap.put("NOR", "NO");
        hashMap.put("OMN", "OM");
        hashMap.put("PAK", "PK");
        hashMap.put("PLW", "PW");
        hashMap.put("PSE", "PS");
        hashMap.put("PAN", "PA");
        hashMap.put("PNG", "PG");
        hashMap.put("PRY", "PY");
        hashMap.put("PER", "PE");
        hashMap.put("PHL", "PH");
        hashMap.put("PCN", "PN");
        hashMap.put("POL", "PL");
        hashMap.put("PRT", "PT");
        hashMap.put("PRI", "PR");
        hashMap.put("QAT", "QA");
        hashMap.put("REU", "RE");
        hashMap.put("ROU", "RO");
        hashMap.put("RUS", "RU");
        hashMap.put("RWA", "RW");
        hashMap.put("BLM", "BL");
        hashMap.put("SHN", "SH");
        hashMap.put("KNA", "KN");
        hashMap.put("LCA", "LC");
        hashMap.put("MAF", "MF");
        hashMap.put("SPM", "PM");
        hashMap.put("VCT", "VC");
        hashMap.put("WSM", "WS");
        hashMap.put("SMR", "SM");
        hashMap.put("STP", "ST");
        hashMap.put("SAU", "SA");
        hashMap.put("SEN", "SN");
        hashMap.put("SRB", "RS");
        hashMap.put("SYC", "SC");
        hashMap.put("SLE", "SL");
        hashMap.put("SGP", "SG");
        hashMap.put("SXM", "SX");
        hashMap.put("SVK", "SK");
        hashMap.put("SVN", "SI");
        hashMap.put("SLB", "SB");
        hashMap.put("SOM", "SO");
        hashMap.put("ZAF", "ZA");
        hashMap.put("SGS", "GS");
        hashMap.put("SSD", "SS");
        hashMap.put("ESP", "ES");
        hashMap.put("LKA", "LK");
        hashMap.put("SDN", "SD");
        hashMap.put("SUR", "SR");
        hashMap.put("SJM", "SJ");
        hashMap.put("SWZ", "SZ");
        hashMap.put("SWE", "SE");
        hashMap.put("CHE", "CH");
        hashMap.put("SYR", "SY");
        hashMap.put("TWN", "TW");
        hashMap.put("TJK", "TJ");
        hashMap.put("TZA", "TZ");
        hashMap.put("THA", "TH");
        hashMap.put("TLS", "TL");
        hashMap.put("TGO", "TG");
        hashMap.put("TKL", "TK");
        hashMap.put("TON", "TO");
        hashMap.put("TTO", "TT");
        hashMap.put("TUN", "TN");
        hashMap.put("TUR", "TR");
        hashMap.put("TKM", "TM");
        hashMap.put("TCA", "TC");
        hashMap.put("TUV", "TV");
        hashMap.put("UGA", "UG");
        hashMap.put("UKR", "UA");
        hashMap.put("ARE", "AE");
        hashMap.put("GBR", "GB");
        hashMap.put("USA", "US");
        hashMap.put("UMI", "UM");
        hashMap.put("URY", "UY");
        hashMap.put("UZB", "UZ");
        hashMap.put("VUT", "VU");
        hashMap.put("VEN", "VE");
        hashMap.put("VNM", "VN");
        hashMap.put("VGB", "VG");
        hashMap.put("VIR", "VI");
        hashMap.put("WLF", "WF");
        hashMap.put("ESH", "EH");
        hashMap.put("YEM", "YE");
        hashMap.put("ZMB", "ZM");
        hashMap.put("ZWE", "ZW");
        return hashMap;
    }
    
    private static int getNumber(final char c) {
        if (c == 'O') {
            return 0;
        }
        if (c == 'I') {
            return 1;
        }
        if (c == 'B') {
            return 8;
        }
        return c - '0';
    }
    
    private static void parseBirthDate(final String s, final Result result) {
        try {
            result.birthYear = Integer.parseInt(s.substring(0, 2));
            int birthYear;
            if (result.birthYear < Calendar.getInstance().get(1) % 100 - 5) {
                birthYear = result.birthYear + 2000;
            }
            else {
                birthYear = result.birthYear + 1900;
            }
            result.birthYear = birthYear;
            result.birthMonth = Integer.parseInt(s.substring(2, 4));
            result.birthDay = Integer.parseInt(s.substring(4));
        }
        catch (NumberFormatException ex) {}
    }
    
    private static void parseExpiryDate(final String anObject, final Result result) {
        try {
            if ("<<<<<<".equals(anObject)) {
                result.doesNotExpire = true;
            }
            else {
                result.expiryYear = Integer.parseInt(anObject.substring(0, 2)) + 2000;
                result.expiryMonth = Integer.parseInt(anObject.substring(2, 4));
                result.expiryDay = Integer.parseInt(anObject.substring(4));
            }
        }
        catch (NumberFormatException ex) {}
    }
    
    private static int parseGender(final char c) {
        if (c == 'F') {
            return 2;
        }
        if (c != 'M') {
            return 0;
        }
        return 1;
    }
    
    private static native String performRecognition(final Bitmap p0, final int p1, final int p2, final AssetManager p3);
    
    public static Result recognize(Bitmap recognizeBarcode, final boolean b) {
        if (b) {
            final Result recognizeBarcode2 = recognizeBarcode(recognizeBarcode);
            if (recognizeBarcode2 != null) {
                return recognizeBarcode2;
            }
        }
        while (true) {
            try {
                final Result recognizeMRZ = recognizeMRZ(recognizeBarcode);
                if (recognizeMRZ != null) {
                    return recognizeMRZ;
                }
                if (!b) {
                    recognizeBarcode = (Bitmap)recognizeBarcode(recognizeBarcode);
                    if (recognizeBarcode != null) {
                        return (Result)recognizeBarcode;
                    }
                }
                return null;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public static Result recognize(final byte[] array, int a, int b, int n) {
        final Bitmap bitmap = Bitmap.createBitmap(a, b, Bitmap$Config.ARGB_8888);
        setYuvBitmapPixels(bitmap, array);
        final Matrix matrix = new Matrix();
        matrix.setRotate((float)n);
        int min = Math.min(a, b);
        final int round = Math.round(min * 0.704f);
        if (n != 90 && n != 270) {
            n = 0;
        }
        else {
            n = 1;
        }
        if (n != 0) {
            a = a / 2 - round / 2;
        }
        else {
            a = 0;
        }
        if (n != 0) {
            b = 0;
        }
        else {
            b = b / 2 - round / 2;
        }
        int n2;
        if (n != 0) {
            n2 = round;
        }
        else {
            n2 = min;
        }
        if (n == 0) {
            min = round;
        }
        return recognize(Bitmap.createBitmap(bitmap, a, b, n2, min, matrix, false), false);
    }
    
    private static Result recognizeBarcode(final Bitmap bitmap) {
        return null;
    }
    
    private static Result recognizeMRZ(Bitmap scaledBitmap) {
        final Bitmap bitmap = scaledBitmap;
        float n;
        Bitmap scaledBitmap2;
        if (scaledBitmap.getWidth() <= 512 && scaledBitmap.getHeight() <= 512) {
            n = 1.0f;
            scaledBitmap2 = bitmap;
        }
        else {
            n = 512.0f / Math.max(scaledBitmap.getWidth(), scaledBitmap.getHeight());
            scaledBitmap2 = Bitmap.createScaledBitmap(bitmap, Math.round(scaledBitmap.getWidth() * n), Math.round(scaledBitmap.getHeight() * n), true);
        }
        final int[] cornerPoints = findCornerPoints(scaledBitmap2);
        final float n2 = 1.0f / n;
        Label_0757: {
            Bitmap bitmap2 = null;
            Label_0709: {
                if (cornerPoints == null) {
                    if (scaledBitmap.getWidth() <= 1500) {
                        bitmap2 = bitmap;
                        if (scaledBitmap.getHeight() <= 1500) {
                            break Label_0709;
                        }
                    }
                    final float n3 = 1500.0f / Math.max(scaledBitmap.getWidth(), scaledBitmap.getHeight());
                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(scaledBitmap.getWidth() * n3), Math.round(scaledBitmap.getHeight() * n3), true);
                    break Label_0757;
                }
                Point point = new Point(cornerPoints[0], cornerPoints[1]);
                final Point point2 = new Point(cornerPoints[2], cornerPoints[3]);
                Point point3 = new Point(cornerPoints[4], cornerPoints[5]);
                Point point4 = new Point(cornerPoints[6], cornerPoints[7]);
                Point point5;
                if (point2.x < point.x) {
                    point5 = point2;
                }
                else {
                    point5 = point;
                    point = point2;
                    final Point point6 = point4;
                    point4 = point3;
                    point3 = point6;
                }
                final double hypot = Math.hypot(point.x - point5.x, point.y - point5.y);
                final double hypot2 = Math.hypot(point3.x - point4.x, point3.y - point4.y);
                final double hypot3 = Math.hypot(point4.x - point5.x, point4.y - point5.y);
                final double hypot4 = Math.hypot(point3.x - point.x, point3.y - point.y);
                final double n4 = hypot / hypot3;
                final double n5 = hypot / hypot4;
                final double n6 = hypot2 / hypot3;
                final double n7 = hypot2 / hypot4;
                bitmap2 = bitmap;
                if (n4 >= 1.35) {
                    bitmap2 = bitmap;
                    if (n4 <= 1.75) {
                        bitmap2 = bitmap;
                        if (n6 >= 1.35) {
                            bitmap2 = bitmap;
                            if (n6 <= 1.75) {
                                bitmap2 = bitmap;
                                if (n5 >= 1.35) {
                                    bitmap2 = bitmap;
                                    if (n5 <= 1.75) {
                                        bitmap2 = bitmap;
                                        if (n7 >= 1.35) {
                                            bitmap2 = bitmap;
                                            if (n7 <= 1.75) {
                                                bitmap2 = Bitmap.createBitmap(1024, (int)Math.round(1024.0 / ((n4 + n5 + n6 + n7) / 4.0)), Bitmap$Config.ARGB_8888);
                                                final Canvas canvas = new Canvas(bitmap2);
                                                final float n8 = (float)bitmap2.getWidth();
                                                final float n9 = (float)bitmap2.getWidth();
                                                final float n10 = (float)bitmap2.getHeight();
                                                final float n11 = (float)bitmap2.getHeight();
                                                final float[] array = { point5.x * n2, point5.y * n2, point.x * n2, point.y * n2, point3.x * n2, point3.y * n2, point4.x * n2, point4.y * n2 };
                                                final Matrix matrix = new Matrix();
                                                matrix.setPolyToPoly(array, 0, new float[] { 0.0f, 0.0f, n8, 0.0f, n9, n10, 0.0f, n11 }, 0, array.length >> 1);
                                                canvas.drawBitmap(bitmap, matrix, new Paint(2));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            scaledBitmap = bitmap2;
        }
        Rect[][] binarizeAndFindCharacters;
        Object bitmap3 = binarizeAndFindCharacters = null;
        int i = 0;
        int max = 0;
        int n12 = 0;
        while (i < 3) {
            Matrix matrix2;
            if (i != 1) {
                if (i != 2) {
                    matrix2 = null;
                }
                else {
                    matrix2 = new Matrix();
                    matrix2.setRotate(-1.0f, (float)(scaledBitmap.getWidth() / 2), (float)(scaledBitmap.getHeight() / 2));
                }
            }
            else {
                matrix2 = new Matrix();
                matrix2.setRotate(1.0f, (float)(scaledBitmap.getWidth() / 2), (float)(scaledBitmap.getHeight() / 2));
            }
            Bitmap bitmap4;
            if (matrix2 != null) {
                bitmap4 = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix2, true);
            }
            else {
                bitmap4 = scaledBitmap;
            }
            bitmap3 = Bitmap.createBitmap(bitmap4.getWidth(), bitmap4.getHeight(), Bitmap$Config.ALPHA_8);
            binarizeAndFindCharacters = binarizeAndFindCharacters(bitmap4, (Bitmap)bitmap3);
            if (binarizeAndFindCharacters == null) {
                return null;
            }
            int n13;
            for (int length = binarizeAndFindCharacters.length, j = 0; j < length; ++j, n12 = n13) {
                final Rect[] array2 = binarizeAndFindCharacters[j];
                max = Math.max(array2.length, max);
                n13 = n12;
                if (array2.length > 0) {
                    n13 = n12 + 1;
                }
            }
            if (n12 >= 2 && max >= 30) {
                break;
            }
            ++i;
        }
        if (max < 30 || n12 < 2) {
            return null;
        }
        final Bitmap bitmap5 = Bitmap.createBitmap(binarizeAndFindCharacters[0].length * 10, binarizeAndFindCharacters.length * 15, Bitmap$Config.ALPHA_8);
        final Canvas canvas2 = new Canvas(bitmap5);
        final Paint paint = new Paint(2);
        final Rect rect = new Rect(0, 0, 10, 15);
        final int length2 = binarizeAndFindCharacters.length;
        int k = 0;
        int n14 = 0;
        while (k < length2) {
            final Rect[] array3 = binarizeAndFindCharacters[k];
            final int length3 = array3.length;
            int l = 0;
            int n15 = 0;
            while (l < length3) {
                final Rect rect2 = array3[l];
                final int n16 = n15 * 10;
                final int n17 = n14 * 15;
                rect.set(n16, n17, n16 + 10, n17 + 15);
                canvas2.drawBitmap((Bitmap)bitmap3, rect2, rect, paint);
                ++n15;
                ++l;
            }
            ++n14;
            ++k;
        }
        final String performRecognition = performRecognition(bitmap5, binarizeAndFindCharacters.length, binarizeAndFindCharacters[0].length, ApplicationLoader.applicationContext.getAssets());
        if (performRecognition == null) {
            return null;
        }
        final String[] split = TextUtils.split(performRecognition, "\n");
        final Result result = new Result();
        if (split.length < 2 || split[0].length() < 30 || split[1].length() != split[0].length()) {
            return null;
        }
        result.rawMRZ = TextUtils.join((CharSequence)"\n", (Object[])split);
        final HashMap<String, String> countriesMap = getCountriesMap();
        final char char1 = split[0].charAt(0);
        if (char1 == 'P') {
            result.type = 1;
            if (split[0].length() == 44) {
                result.issuingCountry = split[0].substring(2, 5);
                final int index = split[0].indexOf("<<", 6);
                if (index != -1) {
                    result.lastName = split[0].substring(5, index).replace('<', ' ').replace('0', 'O').trim();
                    result.firstName = split[0].substring(index + 2).replace('<', ' ').replace('0', 'O').trim();
                    if (result.firstName.contains("   ")) {
                        final String firstName = result.firstName;
                        result.firstName = firstName.substring(0, firstName.indexOf("   "));
                    }
                }
                final String trim = split[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                if (checksum(trim) == getNumber(split[1].charAt(9))) {
                    result.number = trim;
                }
                result.nationality = split[1].substring(10, 13);
                final String replace = split[1].substring(13, 19).replace('O', '0').replace('I', '1');
                if (checksum(replace) == getNumber(split[1].charAt(19))) {
                    parseBirthDate(replace, result);
                }
                result.gender = parseGender(split[1].charAt(20));
                final String replace2 = split[1].substring(21, 27).replace('O', '0').replace('I', '1');
                if (checksum(replace2) == getNumber(split[1].charAt(27)) || split[1].charAt(27) == '<') {
                    parseExpiryDate(replace2, result);
                }
                if ("RUS".equals(result.issuingCountry) && split[0].charAt(1) == 'N') {
                    result.type = 3;
                    final String[] split2 = result.firstName.split(" ");
                    result.firstName = cyrillicToLatin(russianPassportTranslit(split2[0]));
                    if (split2.length > 1) {
                        result.middleName = cyrillicToLatin(russianPassportTranslit(split2[1]));
                    }
                    result.lastName = cyrillicToLatin(russianPassportTranslit(result.lastName));
                    if (result.number != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(result.number.substring(0, 3));
                        sb.append(split[1].charAt(28));
                        sb.append(result.number.substring(3));
                        result.number = sb.toString();
                    }
                }
                else {
                    result.firstName = result.firstName.replace('8', 'B');
                    result.lastName = result.lastName.replace('8', 'B');
                }
                result.lastName = capitalize(result.lastName);
                result.firstName = capitalize(result.firstName);
                result.middleName = capitalize(result.middleName);
            }
        }
        else {
            if (char1 != 'I' && char1 != 'A' && char1 != 'C') {
                return null;
            }
            result.type = 2;
            if (split.length == 3 && split[0].length() == 30 && split[2].length() == 30) {
                result.issuingCountry = split[0].substring(2, 5);
                final String trim2 = split[0].substring(5, 14).replace('<', ' ').replace('O', '0').trim();
                if (checksum(trim2) == split[0].charAt(14) - '0') {
                    result.number = trim2;
                }
                final String replace3 = split[1].substring(0, 6).replace('O', '0').replace('I', '1');
                if (checksum(replace3) == getNumber(split[1].charAt(6))) {
                    parseBirthDate(replace3, result);
                }
                result.gender = parseGender(split[1].charAt(7));
                final String replace4 = split[1].substring(8, 14).replace('O', '0').replace('I', '1');
                if (checksum(replace4) == getNumber(split[1].charAt(14)) || split[1].charAt(14) == '<') {
                    parseExpiryDate(replace4, result);
                }
                result.nationality = split[1].substring(15, 18);
                final int index2 = split[2].indexOf("<<");
                if (index2 != -1) {
                    result.lastName = split[2].substring(0, index2).replace('<', ' ').trim();
                    result.firstName = split[2].substring(index2 + 2).replace('<', ' ').trim();
                }
            }
            else if (split.length == 2 && split[0].length() == 36) {
                result.issuingCountry = split[0].substring(2, 5);
                if ("FRA".equals(result.issuingCountry) && char1 == 'I' && split[0].charAt(1) == 'D') {
                    result.nationality = "FRA";
                    result.lastName = split[0].substring(5, 30).replace('<', ' ').trim();
                    result.firstName = split[1].substring(13, 27).replace("<<", ", ").replace('<', ' ').trim();
                    final String replace5 = split[1].substring(0, 12).replace('O', '0');
                    if (checksum(replace5) == getNumber(split[1].charAt(12))) {
                        result.number = replace5;
                    }
                    final String replace6 = split[1].substring(27, 33).replace('O', '0').replace('I', '1');
                    if (checksum(replace6) == getNumber(split[1].charAt(33))) {
                        parseBirthDate(replace6, result);
                    }
                    result.gender = parseGender(split[1].charAt(34));
                    result.doesNotExpire = true;
                }
                else {
                    final int index3 = split[0].indexOf("<<");
                    if (index3 != -1) {
                        result.lastName = split[0].substring(5, index3).replace('<', ' ').trim();
                        result.firstName = split[0].substring(index3 + 2).replace('<', ' ').trim();
                    }
                    final String trim3 = split[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                    if (checksum(trim3) == getNumber(split[1].charAt(9))) {
                        result.number = trim3;
                    }
                    result.nationality = split[1].substring(10, 13);
                    final String replace7 = split[1].substring(13, 19).replace('O', '0').replace('I', '1');
                    if (checksum(replace7) == getNumber(split[1].charAt(19))) {
                        parseBirthDate(replace7, result);
                    }
                    result.gender = parseGender(split[1].charAt(20));
                    final String replace8 = split[1].substring(21, 27).replace('O', '0').replace('I', '1');
                    if (checksum(replace8) == getNumber(split[1].charAt(27)) || split[1].charAt(27) == '<') {
                        parseExpiryDate(replace8, result);
                    }
                }
            }
            result.firstName = capitalize(result.firstName.replace('0', 'O').replace('8', 'B'));
            result.lastName = capitalize(result.lastName.replace('0', 'O').replace('8', 'B'));
        }
        if (TextUtils.isEmpty((CharSequence)result.firstName) && TextUtils.isEmpty((CharSequence)result.lastName)) {
            return null;
        }
        result.issuingCountry = countriesMap.get(result.issuingCountry);
        result.nationality = countriesMap.get(result.nationality);
        return result;
    }
    
    private static String russianPassportTranslit(final String s) {
        final char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; ++i) {
            final int index = "ABVGDE2JZIQKLMNOPRSTUFHC34WXY9678".indexOf(charArray[i]);
            if (index != -1) {
                charArray[i] = "\u0410\u0411\u0412\u0413\u0414\u0415\u0401\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f".charAt(index);
            }
        }
        return new String(charArray);
    }
    
    private static native void setYuvBitmapPixels(final Bitmap p0, final byte[] p1);
    
    public static class Result
    {
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_UNKNOWN = 0;
        public static final int TYPE_DRIVER_LICENSE = 4;
        public static final int TYPE_ID = 2;
        public static final int TYPE_INTERNAL_PASSPORT = 3;
        public static final int TYPE_PASSPORT = 1;
        public int birthDay;
        public int birthMonth;
        public int birthYear;
        public boolean doesNotExpire;
        public int expiryDay;
        public int expiryMonth;
        public int expiryYear;
        public String firstName;
        public int gender;
        public String issuingCountry;
        public String lastName;
        public boolean mainCheckDigitIsValid;
        public String middleName;
        public String nationality;
        public String number;
        public String rawMRZ;
        public int type;
    }
}
