package com.airbnb.lottie.parser;

class GradientStrokeParser {
    /* JADX WARNING: Removed duplicated region for block: B:104:0x01b3  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01bb  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x01b3  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01bb  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e8  */
    static com.airbnb.lottie.model.content.GradientStroke parse(android.util.JsonReader r19, com.airbnb.lottie.LottieComposition r20) throws java.io.IOException {
        /*
        r11 = new java.util.ArrayList;
        r11.<init>();
        r1 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r12 = 0;
    L_0x0011:
        r13 = r19.hasNext();
        if (r13 == 0) goto L_0x01e2;
    L_0x0017:
        r13 = r19.nextName();
        r14 = r13.hashCode();
        r16 = -1;
        switch(r14) {
            case 100: goto L_0x008d;
            case 101: goto L_0x0083;
            case 103: goto L_0x0079;
            case 111: goto L_0x006f;
            case 115: goto L_0x0065;
            case 116: goto L_0x005b;
            case 119: goto L_0x0051;
            case 3447: goto L_0x0047;
            case 3454: goto L_0x003c;
            case 3487: goto L_0x0031;
            case 3519: goto L_0x0026;
            default: goto L_0x0024;
        };
    L_0x0024:
        goto L_0x0098;
    L_0x0026:
        r14 = "nm";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x002e:
        r13 = 0;
        goto L_0x0099;
    L_0x0031:
        r14 = "ml";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0039:
        r13 = 9;
        goto L_0x0099;
    L_0x003c:
        r14 = "lj";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0044:
        r13 = 8;
        goto L_0x0099;
    L_0x0047:
        r14 = "lc";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x004f:
        r13 = 7;
        goto L_0x0099;
    L_0x0051:
        r14 = "w";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0059:
        r13 = 6;
        goto L_0x0099;
    L_0x005b:
        r14 = "t";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0063:
        r13 = 3;
        goto L_0x0099;
    L_0x0065:
        r14 = "s";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x006d:
        r13 = 4;
        goto L_0x0099;
    L_0x006f:
        r14 = "o";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0077:
        r13 = 2;
        goto L_0x0099;
    L_0x0079:
        r14 = "g";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0081:
        r13 = 1;
        goto L_0x0099;
    L_0x0083:
        r14 = "e";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x008b:
        r13 = 5;
        goto L_0x0099;
    L_0x008d:
        r14 = "d";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x0098;
    L_0x0095:
        r13 = 10;
        goto L_0x0099;
    L_0x0098:
        r13 = -1;
    L_0x0099:
        switch(r13) {
            case 0: goto L_0x01d8;
            case 1: goto L_0x017e;
            case 2: goto L_0x0174;
            case 3: goto L_0x0166;
            case 4: goto L_0x0161;
            case 5: goto L_0x015c;
            case 6: goto L_0x0157;
            case 7: goto L_0x014a;
            case 8: goto L_0x013d;
            case 9: goto L_0x0137;
            case 10: goto L_0x00a5;
            default: goto L_0x009c;
        };
    L_0x009c:
        r0 = r19;
        r13 = r20;
        r19.skipValue();
        goto L_0x0011;
    L_0x00a5:
        r19.beginArray();
    L_0x00a8:
        r13 = r19.hasNext();
        if (r13 == 0) goto L_0x011b;
    L_0x00ae:
        r19.beginObject();
        r13 = 0;
        r14 = 0;
    L_0x00b3:
        r17 = r19.hasNext();
        if (r17 == 0) goto L_0x00f6;
    L_0x00b9:
        r15 = r19.nextName();
        r0 = r15.hashCode();
        r18 = r12;
        r12 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r0 == r12) goto L_0x00d6;
    L_0x00c7:
        r12 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r0 == r12) goto L_0x00cc;
    L_0x00cb:
        goto L_0x00e0;
    L_0x00cc:
        r0 = "v";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x00e0;
    L_0x00d4:
        r0 = 1;
        goto L_0x00e1;
    L_0x00d6:
        r0 = "n";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x00e0;
    L_0x00de:
        r0 = 0;
        goto L_0x00e1;
    L_0x00e0:
        r0 = -1;
    L_0x00e1:
        switch(r0) {
            case 0: goto L_0x00ee;
            case 1: goto L_0x00e8;
            default: goto L_0x00e4;
        };
    L_0x00e4:
        r19.skipValue();
        goto L_0x00f3;
    L_0x00e8:
        r0 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r19, r20);
        r14 = r0;
        goto L_0x00f3;
    L_0x00ee:
        r0 = r19.nextString();
        r13 = r0;
    L_0x00f3:
        r12 = r18;
        goto L_0x00b3;
    L_0x00f6:
        r18 = r12;
        r19.endObject();
        r0 = "o";
        r0 = r13.equals(r0);
        if (r0 == 0) goto L_0x0105;
    L_0x0103:
        r12 = r14;
        goto L_0x00a8;
    L_0x0105:
        r0 = "d";
        r0 = r13.equals(r0);
        if (r0 != 0) goto L_0x0115;
    L_0x010d:
        r0 = "g";
        r0 = r13.equals(r0);
        if (r0 == 0) goto L_0x0118;
    L_0x0115:
        r11.add(r14);
    L_0x0118:
        r12 = r18;
        goto L_0x00a8;
    L_0x011b:
        r18 = r12;
        r19.endArray();
        r0 = r11.size();
        r13 = 1;
        if (r0 != r13) goto L_0x012f;
    L_0x0127:
        r0 = 0;
        r0 = r11.get(r0);
        r11.add(r0);
    L_0x012f:
        r0 = r19;
        r13 = r20;
        r12 = r18;
        goto L_0x0011;
    L_0x0137:
        r13 = r19.nextDouble();
        r10 = (float) r13;
        goto L_0x0178;
    L_0x013d:
        r13 = 1;
        r0 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.values();
        r9 = r19.nextInt();
        r9 = r9 - r13;
        r9 = r0[r9];
        goto L_0x0178;
    L_0x014a:
        r13 = 1;
        r0 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.values();
        r8 = r19.nextInt();
        r8 = r8 - r13;
        r8 = r0[r8];
        goto L_0x0178;
    L_0x0157:
        r7 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r19, r20);
        goto L_0x0178;
    L_0x015c:
        r6 = com.airbnb.lottie.parser.AnimatableValueParser.parsePoint(r19, r20);
        goto L_0x0178;
    L_0x0161:
        r5 = com.airbnb.lottie.parser.AnimatableValueParser.parsePoint(r19, r20);
        goto L_0x0178;
    L_0x0166:
        r13 = 1;
        r0 = r19.nextInt();
        if (r0 != r13) goto L_0x0171;
    L_0x016d:
        r0 = com.airbnb.lottie.model.content.GradientType.Linear;
    L_0x016f:
        r2 = r0;
        goto L_0x0178;
    L_0x0171:
        r0 = com.airbnb.lottie.model.content.GradientType.Radial;
        goto L_0x016f;
    L_0x0174:
        r4 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r19, r20);
    L_0x0178:
        r0 = r19;
        r13 = r20;
        goto L_0x0011;
    L_0x017e:
        r0 = 0;
        r13 = 1;
        r19.beginObject();
        r14 = -1;
    L_0x0184:
        r15 = r19.hasNext();
        if (r15 == 0) goto L_0x01cf;
    L_0x018a:
        r15 = r19.nextName();
        r0 = r15.hashCode();
        r13 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        if (r0 == r13) goto L_0x01a5;
    L_0x0196:
        r13 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        if (r0 == r13) goto L_0x019b;
    L_0x019a:
        goto L_0x01af;
    L_0x019b:
        r0 = "p";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x01af;
    L_0x01a3:
        r0 = 0;
        goto L_0x01b0;
    L_0x01a5:
        r0 = "k";
        r0 = r15.equals(r0);
        if (r0 == 0) goto L_0x01af;
    L_0x01ad:
        r0 = 1;
        goto L_0x01b0;
    L_0x01af:
        r0 = -1;
    L_0x01b0:
        switch(r0) {
            case 0: goto L_0x01c4;
            case 1: goto L_0x01bb;
            default: goto L_0x01b3;
        };
    L_0x01b3:
        r0 = r19;
        r13 = r20;
        r19.skipValue();
        goto L_0x01cc;
    L_0x01bb:
        r0 = r19;
        r13 = r20;
        r3 = com.airbnb.lottie.parser.AnimatableValueParser.parseGradientColor(r0, r13, r14);
        goto L_0x01cc;
    L_0x01c4:
        r0 = r19;
        r13 = r20;
        r14 = r19.nextInt();
    L_0x01cc:
        r0 = 0;
        r13 = 1;
        goto L_0x0184;
    L_0x01cf:
        r0 = r19;
        r13 = r20;
        r19.endObject();
        goto L_0x0011;
    L_0x01d8:
        r0 = r19;
        r13 = r20;
        r1 = r19.nextString();
        goto L_0x0011;
    L_0x01e2:
        r13 = new com.airbnb.lottie.model.content.GradientStroke;
        r0 = r13;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        return r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.GradientStrokeParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.GradientStroke");
    }
}
