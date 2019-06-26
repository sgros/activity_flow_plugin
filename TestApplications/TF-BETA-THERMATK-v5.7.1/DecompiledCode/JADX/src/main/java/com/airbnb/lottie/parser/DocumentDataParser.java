package com.airbnb.lottie.parser;

import com.airbnb.lottie.model.DocumentData;

public class DocumentDataParser implements ValueParser<DocumentData> {
    public static final DocumentDataParser INSTANCE = new DocumentDataParser();

    private DocumentDataParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d1  */
    public com.airbnb.lottie.model.DocumentData parse(android.util.JsonReader r23, float r24) throws java.io.IOException {
        /*
        r22 = this;
        r0 = com.airbnb.lottie.model.DocumentData.Justification.CENTER;
        r23.beginObject();
        r1 = 1;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r11 = r0;
        r7 = r2;
        r8 = r7;
        r9 = r4;
        r13 = r9;
        r15 = r13;
        r19 = r15;
        r12 = 0;
        r17 = 0;
        r18 = 0;
        r21 = 1;
    L_0x0019:
        r0 = r23.hasNext();
        if (r0 == 0) goto L_0x0136;
    L_0x001f:
        r0 = r23.nextName();
        r2 = -1;
        r4 = r0.hashCode();
        r5 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r4 == r5) goto L_0x00be;
    L_0x002c:
        r5 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        if (r4 == r5) goto L_0x00b4;
    L_0x0030:
        r5 = 3261; // 0xcbd float:4.57E-42 double:1.611E-320;
        if (r4 == r5) goto L_0x00aa;
    L_0x0034:
        r5 = 3452; // 0xd7c float:4.837E-42 double:1.7055E-320;
        if (r4 == r5) goto L_0x00a0;
    L_0x0038:
        r5 = 3463; // 0xd87 float:4.853E-42 double:1.711E-320;
        if (r4 == r5) goto L_0x0096;
    L_0x003c:
        r5 = 3543; // 0xdd7 float:4.965E-42 double:1.7505E-320;
        if (r4 == r5) goto L_0x008b;
    L_0x0040:
        r5 = 3664; // 0xe50 float:5.134E-42 double:1.8103E-320;
        if (r4 == r5) goto L_0x0080;
    L_0x0044:
        r5 = 3684; // 0xe64 float:5.162E-42 double:1.82E-320;
        if (r4 == r5) goto L_0x0075;
    L_0x0048:
        r5 = 3710; // 0xe7e float:5.199E-42 double:1.833E-320;
        if (r4 == r5) goto L_0x006b;
    L_0x004c:
        r5 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r4 == r5) goto L_0x0061;
    L_0x0050:
        r5 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r4 == r5) goto L_0x0056;
    L_0x0054:
        goto L_0x00c8;
    L_0x0056:
        r4 = "t";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x005e:
        r0 = 0;
        goto L_0x00c9;
    L_0x0061:
        r4 = "s";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x0069:
        r0 = 2;
        goto L_0x00c9;
    L_0x006b:
        r4 = "tr";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x0073:
        r0 = 4;
        goto L_0x00c9;
    L_0x0075:
        r4 = "sw";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x007d:
        r0 = 9;
        goto L_0x00c9;
    L_0x0080:
        r4 = "sc";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x0088:
        r0 = 8;
        goto L_0x00c9;
    L_0x008b:
        r4 = "of";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x0093:
        r0 = 10;
        goto L_0x00c9;
    L_0x0096:
        r4 = "ls";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x009e:
        r0 = 6;
        goto L_0x00c9;
    L_0x00a0:
        r4 = "lh";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x00a8:
        r0 = 5;
        goto L_0x00c9;
    L_0x00aa:
        r4 = "fc";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x00b2:
        r0 = 7;
        goto L_0x00c9;
    L_0x00b4:
        r4 = "j";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x00bc:
        r0 = 3;
        goto L_0x00c9;
    L_0x00be:
        r4 = "f";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00c8;
    L_0x00c6:
        r0 = 1;
        goto L_0x00c9;
    L_0x00c8:
        r0 = -1;
    L_0x00c9:
        switch(r0) {
            case 0: goto L_0x012f;
            case 1: goto L_0x0128;
            case 2: goto L_0x0121;
            case 3: goto L_0x0106;
            case 4: goto L_0x00ff;
            case 5: goto L_0x00f8;
            case 6: goto L_0x00f1;
            case 7: goto L_0x00e9;
            case 8: goto L_0x00e1;
            case 9: goto L_0x00d9;
            case 10: goto L_0x00d1;
            default: goto L_0x00cc;
        };
    L_0x00cc:
        r23.skipValue();
        goto L_0x0019;
    L_0x00d1:
        r0 = r23.nextBoolean();
        r21 = r0;
        goto L_0x0019;
    L_0x00d9:
        r4 = r23.nextDouble();
        r19 = r4;
        goto L_0x0019;
    L_0x00e1:
        r0 = com.airbnb.lottie.parser.JsonUtils.jsonToColor(r23);
        r18 = r0;
        goto L_0x0019;
    L_0x00e9:
        r0 = com.airbnb.lottie.parser.JsonUtils.jsonToColor(r23);
        r17 = r0;
        goto L_0x0019;
    L_0x00f1:
        r4 = r23.nextDouble();
        r15 = r4;
        goto L_0x0019;
    L_0x00f8:
        r4 = r23.nextDouble();
        r13 = r4;
        goto L_0x0019;
    L_0x00ff:
        r0 = r23.nextInt();
        r12 = r0;
        goto L_0x0019;
    L_0x0106:
        r0 = r23.nextInt();
        r2 = com.airbnb.lottie.model.DocumentData.Justification.CENTER;
        r2 = r2.ordinal();
        if (r0 > r2) goto L_0x011c;
    L_0x0112:
        if (r0 >= 0) goto L_0x0115;
    L_0x0114:
        goto L_0x011c;
    L_0x0115:
        r2 = com.airbnb.lottie.model.DocumentData.Justification.values();
        r0 = r2[r0];
        goto L_0x011e;
    L_0x011c:
        r0 = com.airbnb.lottie.model.DocumentData.Justification.CENTER;
    L_0x011e:
        r11 = r0;
        goto L_0x0019;
    L_0x0121:
        r4 = r23.nextDouble();
        r9 = r4;
        goto L_0x0019;
    L_0x0128:
        r0 = r23.nextString();
        r8 = r0;
        goto L_0x0019;
    L_0x012f:
        r0 = r23.nextString();
        r7 = r0;
        goto L_0x0019;
    L_0x0136:
        r23.endObject();
        r0 = new com.airbnb.lottie.model.DocumentData;
        r6 = r0;
        r6.<init>(r7, r8, r9, r11, r12, r13, r15, r17, r18, r19, r21);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.DocumentDataParser.parse(android.util.JsonReader, float):com.airbnb.lottie.model.DocumentData");
    }
}
