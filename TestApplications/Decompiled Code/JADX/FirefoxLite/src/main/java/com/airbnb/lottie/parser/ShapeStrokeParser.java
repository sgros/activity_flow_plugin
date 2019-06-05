package com.airbnb.lottie.parser;

class ShapeStrokeParser {
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0092  */
    static com.airbnb.lottie.model.content.ShapeStroke parse(android.util.JsonReader r16, com.airbnb.lottie.LottieComposition r17) throws java.io.IOException {
        /*
        r3 = new java.util.ArrayList;
        r3.<init>();
        r1 = 0;
        r1 = 0;
        r2 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
    L_0x000e:
        r10 = r16.hasNext();
        if (r10 == 0) goto L_0x0166;
    L_0x0014:
        r10 = r16.nextName();
        r11 = r10.hashCode();
        r13 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        r0 = 1;
        if (r11 == r13) goto L_0x007f;
    L_0x0021:
        r12 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        if (r11 == r12) goto L_0x0075;
    L_0x0025:
        r12 = 3447; // 0xd77 float:4.83E-42 double:1.703E-320;
        if (r11 == r12) goto L_0x006b;
    L_0x0029:
        r12 = 3454; // 0xd7e float:4.84E-42 double:1.7065E-320;
        if (r11 == r12) goto L_0x0061;
    L_0x002d:
        r12 = 3487; // 0xd9f float:4.886E-42 double:1.723E-320;
        if (r11 == r12) goto L_0x0057;
    L_0x0031:
        r12 = 3519; // 0xdbf float:4.931E-42 double:1.7386E-320;
        if (r11 == r12) goto L_0x004d;
    L_0x0035:
        switch(r11) {
            case 99: goto L_0x0043;
            case 100: goto L_0x0039;
            default: goto L_0x0038;
        };
    L_0x0038:
        goto L_0x0089;
    L_0x0039:
        r11 = "d";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x0041:
        r12 = 7;
        goto L_0x008a;
    L_0x0043:
        r11 = "c";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x004b:
        r12 = 1;
        goto L_0x008a;
    L_0x004d:
        r11 = "nm";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x0055:
        r12 = 0;
        goto L_0x008a;
    L_0x0057:
        r11 = "ml";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x005f:
        r12 = 6;
        goto L_0x008a;
    L_0x0061:
        r11 = "lj";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x0069:
        r12 = 5;
        goto L_0x008a;
    L_0x006b:
        r11 = "lc";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x0073:
        r12 = 4;
        goto L_0x008a;
    L_0x0075:
        r11 = "w";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x007d:
        r12 = 2;
        goto L_0x008a;
    L_0x007f:
        r11 = "o";
        r10 = r10.equals(r11);
        if (r10 == 0) goto L_0x0089;
    L_0x0087:
        r12 = 3;
        goto L_0x008a;
    L_0x0089:
        r12 = -1;
    L_0x008a:
        switch(r12) {
            case 0: goto L_0x0160;
            case 1: goto L_0x015a;
            case 2: goto L_0x0154;
            case 3: goto L_0x014e;
            case 4: goto L_0x0141;
            case 5: goto L_0x0134;
            case 6: goto L_0x012d;
            case 7: goto L_0x0092;
            default: goto L_0x008d;
        };
    L_0x008d:
        r16.skipValue();
        goto L_0x000e;
    L_0x0092:
        r16.beginArray();
    L_0x0095:
        r10 = r16.hasNext();
        if (r10 == 0) goto L_0x011a;
    L_0x009b:
        r16.beginObject();
        r10 = 0;
        r11 = 0;
    L_0x00a0:
        r12 = r16.hasNext();
        if (r12 == 0) goto L_0x00dd;
    L_0x00a6:
        r12 = r16.nextName();
        r14 = r12.hashCode();
        r15 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r14 == r15) goto L_0x00c1;
    L_0x00b2:
        r15 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r14 == r15) goto L_0x00b7;
    L_0x00b6:
        goto L_0x00cb;
    L_0x00b7:
        r14 = "v";
        r12 = r12.equals(r14);
        if (r12 == 0) goto L_0x00cb;
    L_0x00bf:
        r12 = 1;
        goto L_0x00cc;
    L_0x00c1:
        r14 = "n";
        r12 = r12.equals(r14);
        if (r12 == 0) goto L_0x00cb;
    L_0x00c9:
        r12 = 0;
        goto L_0x00cc;
    L_0x00cb:
        r12 = -1;
    L_0x00cc:
        switch(r12) {
            case 0: goto L_0x00d8;
            case 1: goto L_0x00d3;
            default: goto L_0x00cf;
        };
    L_0x00cf:
        r16.skipValue();
        goto L_0x00a0;
    L_0x00d3:
        r11 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r16, r17);
        goto L_0x00a0;
    L_0x00d8:
        r10 = r16.nextString();
        goto L_0x00a0;
    L_0x00dd:
        r16.endObject();
        r12 = r10.hashCode();
        r14 = 100;
        if (r12 == r14) goto L_0x0103;
    L_0x00e8:
        r14 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        if (r12 == r14) goto L_0x00f9;
    L_0x00ec:
        if (r12 == r13) goto L_0x00ef;
    L_0x00ee:
        goto L_0x010d;
    L_0x00ef:
        r12 = "o";
        r10 = r10.equals(r12);
        if (r10 == 0) goto L_0x010d;
    L_0x00f7:
        r10 = 0;
        goto L_0x010e;
    L_0x00f9:
        r12 = "g";
        r10 = r10.equals(r12);
        if (r10 == 0) goto L_0x010d;
    L_0x0101:
        r10 = 2;
        goto L_0x010e;
    L_0x0103:
        r12 = "d";
        r10 = r10.equals(r12);
        if (r10 == 0) goto L_0x010d;
    L_0x010b:
        r10 = 1;
        goto L_0x010e;
    L_0x010d:
        r10 = -1;
    L_0x010e:
        switch(r10) {
            case 0: goto L_0x0117;
            case 1: goto L_0x0112;
            case 2: goto L_0x0112;
            default: goto L_0x0111;
        };
    L_0x0111:
        goto L_0x0095;
    L_0x0112:
        r3.add(r11);
        goto L_0x0095;
    L_0x0117:
        r2 = r11;
        goto L_0x0095;
    L_0x011a:
        r16.endArray();
        r10 = r3.size();
        if (r10 != r0) goto L_0x000e;
    L_0x0123:
        r0 = 0;
        r0 = r3.get(r0);
        r3.add(r0);
        goto L_0x000e;
    L_0x012d:
        r9 = r16.nextDouble();
        r9 = (float) r9;
        goto L_0x000e;
    L_0x0134:
        r8 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.values();
        r10 = r16.nextInt();
        r10 = r10 - r0;
        r8 = r8[r10];
        goto L_0x000e;
    L_0x0141:
        r7 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.values();
        r10 = r16.nextInt();
        r10 = r10 - r0;
        r7 = r7[r10];
        goto L_0x000e;
    L_0x014e:
        r5 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r16, r17);
        goto L_0x000e;
    L_0x0154:
        r6 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r16, r17);
        goto L_0x000e;
    L_0x015a:
        r4 = com.airbnb.lottie.parser.AnimatableValueParser.parseColor(r16, r17);
        goto L_0x000e;
    L_0x0160:
        r1 = r16.nextString();
        goto L_0x000e;
    L_0x0166:
        r10 = new com.airbnb.lottie.model.content.ShapeStroke;
        r0 = r10;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9);
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ShapeStrokeParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.ShapeStroke");
    }
}
