package com.airbnb.lottie.parser;

class GradientFillParser {
    /* JADX WARNING: Removed duplicated region for block: B:73:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0090  */
    static com.airbnb.lottie.model.content.GradientFill parse(android.util.JsonReader r15, com.airbnb.lottie.LottieComposition r16) throws java.io.IOException {
        /*
        r0 = android.graphics.Path.FillType.WINDING;
        r1 = 0;
        r2 = 0;
        r6 = r0;
        r4 = r2;
        r5 = r4;
        r7 = r5;
        r8 = r7;
        r9 = r8;
        r10 = r9;
        r13 = 0;
    L_0x000c:
        r0 = r15.hasNext();
        if (r0 == 0) goto L_0x0137;
    L_0x0012:
        r0 = r15.nextName();
        r2 = r0.hashCode();
        r3 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r11 = -1;
        r12 = 1;
        if (r2 == r3) goto L_0x007a;
    L_0x0020:
        r3 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        if (r2 == r3) goto L_0x0070;
    L_0x0024:
        r3 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r2 == r3) goto L_0x0066;
    L_0x0028:
        r3 = 3324; // 0xcfc float:4.658E-42 double:1.6423E-320;
        if (r2 == r3) goto L_0x005c;
    L_0x002c:
        r3 = 3519; // 0xdbf float:4.931E-42 double:1.7386E-320;
        if (r2 == r3) goto L_0x0052;
    L_0x0030:
        switch(r2) {
            case 114: goto L_0x0048;
            case 115: goto L_0x003e;
            case 116: goto L_0x0034;
            default: goto L_0x0033;
        };
    L_0x0033:
        goto L_0x0084;
    L_0x0034:
        r2 = "t";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x003c:
        r0 = 3;
        goto L_0x0085;
    L_0x003e:
        r2 = "s";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x0046:
        r0 = 4;
        goto L_0x0085;
    L_0x0048:
        r2 = "r";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x0050:
        r0 = 6;
        goto L_0x0085;
    L_0x0052:
        r2 = "nm";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x005a:
        r0 = 0;
        goto L_0x0085;
    L_0x005c:
        r2 = "hd";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x0064:
        r0 = 7;
        goto L_0x0085;
    L_0x0066:
        r2 = "o";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x006e:
        r0 = 2;
        goto L_0x0085;
    L_0x0070:
        r2 = "g";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x0078:
        r0 = 1;
        goto L_0x0085;
    L_0x007a:
        r2 = "e";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0084;
    L_0x0082:
        r0 = 5;
        goto L_0x0085;
    L_0x0084:
        r0 = -1;
    L_0x0085:
        switch(r0) {
            case 0: goto L_0x012d;
            case 1: goto L_0x00da;
            case 2: goto L_0x00d0;
            case 3: goto L_0x00bf;
            case 4: goto L_0x00b5;
            case 5: goto L_0x00ab;
            case 6: goto L_0x009a;
            case 7: goto L_0x0090;
            default: goto L_0x0088;
        };
    L_0x0088:
        r2 = r15;
        r3 = r16;
        r15.skipValue();
        goto L_0x000c;
    L_0x0090:
        r0 = r15.nextBoolean();
        r2 = r15;
        r3 = r16;
        r13 = r0;
        goto L_0x000c;
    L_0x009a:
        r0 = r15.nextInt();
        if (r0 != r12) goto L_0x00a3;
    L_0x00a0:
        r0 = android.graphics.Path.FillType.WINDING;
        goto L_0x00a5;
    L_0x00a3:
        r0 = android.graphics.Path.FillType.EVEN_ODD;
    L_0x00a5:
        r2 = r15;
        r3 = r16;
        r6 = r0;
        goto L_0x000c;
    L_0x00ab:
        r0 = com.airbnb.lottie.parser.AnimatableValueParser.parsePoint(r15, r16);
        r2 = r15;
        r3 = r16;
        r10 = r0;
        goto L_0x000c;
    L_0x00b5:
        r0 = com.airbnb.lottie.parser.AnimatableValueParser.parsePoint(r15, r16);
        r2 = r15;
        r3 = r16;
        r9 = r0;
        goto L_0x000c;
    L_0x00bf:
        r0 = r15.nextInt();
        if (r0 != r12) goto L_0x00c8;
    L_0x00c5:
        r0 = com.airbnb.lottie.model.content.GradientType.LINEAR;
        goto L_0x00ca;
    L_0x00c8:
        r0 = com.airbnb.lottie.model.content.GradientType.RADIAL;
    L_0x00ca:
        r2 = r15;
        r3 = r16;
        r5 = r0;
        goto L_0x000c;
    L_0x00d0:
        r0 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r15, r16);
        r2 = r15;
        r3 = r16;
        r8 = r0;
        goto L_0x000c;
    L_0x00da:
        r15.beginObject();
        r0 = -1;
    L_0x00de:
        r2 = r15.hasNext();
        if (r2 == 0) goto L_0x0125;
    L_0x00e4:
        r2 = r15.nextName();
        r3 = r2.hashCode();
        r14 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        if (r3 == r14) goto L_0x00ff;
    L_0x00f0:
        r14 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        if (r3 == r14) goto L_0x00f5;
    L_0x00f4:
        goto L_0x0109;
    L_0x00f5:
        r3 = "p";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0109;
    L_0x00fd:
        r2 = 0;
        goto L_0x010a;
    L_0x00ff:
        r3 = "k";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0109;
    L_0x0107:
        r2 = 1;
        goto L_0x010a;
    L_0x0109:
        r2 = -1;
    L_0x010a:
        if (r2 == 0) goto L_0x011d;
    L_0x010c:
        if (r2 == r12) goto L_0x0115;
    L_0x010e:
        r15.skipValue();
        r2 = r15;
        r3 = r16;
        goto L_0x00de;
    L_0x0115:
        r2 = r15;
        r3 = r16;
        r7 = com.airbnb.lottie.parser.AnimatableValueParser.parseGradientColor(r15, r3, r0);
        goto L_0x00de;
    L_0x011d:
        r2 = r15;
        r3 = r16;
        r0 = r15.nextInt();
        goto L_0x00de;
    L_0x0125:
        r2 = r15;
        r3 = r16;
        r15.endObject();
        goto L_0x000c;
    L_0x012d:
        r2 = r15;
        r3 = r16;
        r0 = r15.nextString();
        r4 = r0;
        goto L_0x000c;
    L_0x0137:
        r0 = new com.airbnb.lottie.model.content.GradientFill;
        r11 = 0;
        r12 = 0;
        r3 = r0;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.GradientFillParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.GradientFill");
    }
}
