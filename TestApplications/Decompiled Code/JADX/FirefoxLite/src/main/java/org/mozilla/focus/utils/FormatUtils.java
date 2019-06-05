package org.mozilla.focus.utils;

import java.text.DecimalFormat;

public class FormatUtils {
    /* renamed from: DF */
    private static final DecimalFormat f52DF = new DecimalFormat("0.0");

    public static String getReadableStringFromFileSize(long j) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        if (j < 1048576) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(f52DF.format(j / 1024));
            stringBuilder.append(" KB");
            return stringBuilder.toString();
        } else if (j < 1073741824) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(f52DF.format(j / 1048576));
            stringBuilder2.append(" MB");
            return stringBuilder2.toString();
        } else if (j < 1099511627776L) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(f52DF.format(j / 1073741824));
            stringBuilder.append(" GB");
            return stringBuilder.toString();
        } else {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(f52DF.format(j / 1099511627776L));
            stringBuilder2.append(" TB");
            return stringBuilder2.toString();
        }
    }
}
