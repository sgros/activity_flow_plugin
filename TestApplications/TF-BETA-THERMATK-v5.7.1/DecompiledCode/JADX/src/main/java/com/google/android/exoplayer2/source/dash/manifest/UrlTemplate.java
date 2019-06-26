package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Locale;

public final class UrlTemplate {
    private final int identifierCount;
    private final String[] identifierFormatTags;
    private final int[] identifiers;
    private final String[] urlPieces;

    public static UrlTemplate compile(String str) {
        String[] strArr = new String[5];
        int[] iArr = new int[4];
        String[] strArr2 = new String[4];
        return new UrlTemplate(strArr, iArr, strArr2, parseTemplate(str, strArr, iArr, strArr2));
    }

    private UrlTemplate(String[] strArr, int[] iArr, String[] strArr2, int i) {
        this.urlPieces = strArr;
        this.identifiers = iArr;
        this.identifierFormatTags = strArr2;
        this.identifierCount = i;
    }

    public String buildUri(String str, long j, int i, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = this.identifierCount;
            if (i2 < i3) {
                stringBuilder.append(this.urlPieces[i2]);
                int[] iArr = this.identifiers;
                if (iArr[i2] == 1) {
                    stringBuilder.append(str);
                } else if (iArr[i2] == 2) {
                    stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Long.valueOf(j)}));
                } else if (iArr[i2] == 3) {
                    stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Integer.valueOf(i)}));
                } else if (iArr[i2] == 4) {
                    stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Long.valueOf(j2)}));
                }
                i2++;
            } else {
                stringBuilder.append(this.urlPieces[i3]);
                return stringBuilder.toString();
            }
        }
    }

    private static int parseTemplate(String str, String[] strArr, int[] iArr, String[] strArr2) {
        String str2 = "";
        strArr[0] = str2;
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            String str3 = "$";
            int indexOf = str.indexOf(str3, i);
            Object obj = -1;
            StringBuilder stringBuilder;
            if (indexOf == -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(strArr[i2]);
                stringBuilder.append(str.substring(i));
                strArr[i2] = stringBuilder.toString();
                i = str.length();
            } else if (indexOf != i) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(strArr[i2]);
                stringBuilder.append(str.substring(i, indexOf));
                strArr[i2] = stringBuilder.toString();
                i = indexOf;
            } else if (str.startsWith("$$", i)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(strArr[i2]);
                stringBuilder2.append(str3);
                strArr[i2] = stringBuilder2.toString();
                i += 2;
            } else {
                i++;
                int indexOf2 = str.indexOf(str3, i);
                String substring = str.substring(i, indexOf2);
                if (substring.equals("RepresentationID")) {
                    iArr[i2] = 1;
                } else {
                    String substring2;
                    indexOf = substring.indexOf("%0");
                    if (indexOf != -1) {
                        substring2 = substring.substring(indexOf);
                        String str4 = "d";
                        if (!substring2.endsWith(str4)) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(substring2);
                            stringBuilder3.append(str4);
                            substring2 = stringBuilder3.toString();
                        }
                        substring = substring.substring(0, indexOf);
                    } else {
                        substring2 = "%01d";
                    }
                    indexOf = substring.hashCode();
                    if (indexOf != -1950496919) {
                        if (indexOf != 2606829) {
                            if (indexOf == 38199441 && substring.equals("Bandwidth")) {
                                obj = 1;
                            }
                        } else if (substring.equals("Time")) {
                            obj = 2;
                        }
                    } else if (substring.equals("Number")) {
                        obj = null;
                    }
                    if (obj == null) {
                        iArr[i2] = 2;
                    } else if (obj == 1) {
                        iArr[i2] = 3;
                    } else if (obj == 2) {
                        iArr[i2] = 4;
                    } else {
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("Invalid template: ");
                        stringBuilder4.append(str);
                        throw new IllegalArgumentException(stringBuilder4.toString());
                    }
                    strArr2[i2] = substring2;
                }
                i2++;
                strArr[i2] = str2;
                i = indexOf2 + 1;
            }
        }
        return i2;
    }
}
