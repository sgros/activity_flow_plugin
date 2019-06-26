package com.airbnb.lottie.parser;

class ContentModelParser {
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0042 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0042 A:{SYNTHETIC} */
    /* JADX WARNING: Missing block: B:49:0x00b2, code skipped:
            if (r2.equals("gs") != false) goto L_0x00de;
     */
    static com.airbnb.lottie.model.content.ContentModel parse(android.util.JsonReader r9, com.airbnb.lottie.LottieComposition r10) throws java.io.IOException {
        /*
        r9.beginObject();
        r0 = 2;
        r1 = 2;
    L_0x0005:
        r2 = r9.hasNext();
        r3 = 0;
        r4 = -1;
        r5 = 1;
        r6 = 0;
        if (r2 == 0) goto L_0x0047;
    L_0x000f:
        r2 = r9.nextName();
        r7 = r2.hashCode();
        r8 = 100;
        if (r7 == r8) goto L_0x002a;
    L_0x001b:
        r8 = 3717; // 0xe85 float:5.209E-42 double:1.8364E-320;
        if (r7 == r8) goto L_0x0020;
    L_0x001f:
        goto L_0x0034;
    L_0x0020:
        r7 = "ty";
        r2 = r2.equals(r7);
        if (r2 == 0) goto L_0x0034;
    L_0x0028:
        r2 = 0;
        goto L_0x0035;
    L_0x002a:
        r7 = "d";
        r2 = r2.equals(r7);
        if (r2 == 0) goto L_0x0034;
    L_0x0032:
        r2 = 1;
        goto L_0x0035;
    L_0x0034:
        r2 = -1;
    L_0x0035:
        if (r2 == 0) goto L_0x0042;
    L_0x0037:
        if (r2 == r5) goto L_0x003d;
    L_0x0039:
        r9.skipValue();
        goto L_0x0005;
    L_0x003d:
        r1 = r9.nextInt();
        goto L_0x0005;
    L_0x0042:
        r2 = r9.nextString();
        goto L_0x0048;
    L_0x0047:
        r2 = r6;
    L_0x0048:
        if (r2 != 0) goto L_0x004b;
    L_0x004a:
        return r6;
    L_0x004b:
        r7 = r2.hashCode();
        switch(r7) {
            case 3239: goto L_0x00d3;
            case 3270: goto L_0x00c9;
            case 3295: goto L_0x00bf;
            case 3307: goto L_0x00b5;
            case 3308: goto L_0x00ac;
            case 3488: goto L_0x00a1;
            case 3633: goto L_0x0096;
            case 3646: goto L_0x008b;
            case 3669: goto L_0x0081;
            case 3679: goto L_0x0076;
            case 3681: goto L_0x006b;
            case 3705: goto L_0x005f;
            case 3710: goto L_0x0054;
            default: goto L_0x0052;
        };
    L_0x0052:
        goto L_0x00dd;
    L_0x0054:
        r0 = "tr";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x005c:
        r0 = 5;
        goto L_0x00de;
    L_0x005f:
        r0 = "tm";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x0067:
        r0 = 9;
        goto L_0x00de;
    L_0x006b:
        r0 = "st";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x0073:
        r0 = 1;
        goto L_0x00de;
    L_0x0076:
        r0 = "sr";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x007e:
        r0 = 10;
        goto L_0x00de;
    L_0x0081:
        r0 = "sh";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x0089:
        r0 = 6;
        goto L_0x00de;
    L_0x008b:
        r0 = "rp";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x0093:
        r0 = 12;
        goto L_0x00de;
    L_0x0096:
        r0 = "rc";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x009e:
        r0 = 8;
        goto L_0x00de;
    L_0x00a1:
        r0 = "mm";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x00a9:
        r0 = 11;
        goto L_0x00de;
    L_0x00ac:
        r3 = "gs";
        r3 = r2.equals(r3);
        if (r3 == 0) goto L_0x00dd;
    L_0x00b4:
        goto L_0x00de;
    L_0x00b5:
        r0 = "gr";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x00bd:
        r0 = 0;
        goto L_0x00de;
    L_0x00bf:
        r0 = "gf";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x00c7:
        r0 = 4;
        goto L_0x00de;
    L_0x00c9:
        r0 = "fl";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x00d1:
        r0 = 3;
        goto L_0x00de;
    L_0x00d3:
        r0 = "el";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x00dd;
    L_0x00db:
        r0 = 7;
        goto L_0x00de;
    L_0x00dd:
        r0 = -1;
    L_0x00de:
        switch(r0) {
            case 0: goto L_0x0137;
            case 1: goto L_0x0132;
            case 2: goto L_0x012d;
            case 3: goto L_0x0128;
            case 4: goto L_0x0123;
            case 5: goto L_0x011e;
            case 6: goto L_0x0119;
            case 7: goto L_0x0114;
            case 8: goto L_0x010f;
            case 9: goto L_0x010a;
            case 10: goto L_0x0105;
            case 11: goto L_0x00fb;
            case 12: goto L_0x00f6;
            default: goto L_0x00e1;
        };
    L_0x00e1:
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r0 = "Unknown shape type ";
        r10.append(r0);
        r10.append(r2);
        r10 = r10.toString();
        com.airbnb.lottie.utils.Logger.warning(r10);
        goto L_0x013b;
    L_0x00f6:
        r6 = com.airbnb.lottie.parser.RepeaterParser.parse(r9, r10);
        goto L_0x013b;
    L_0x00fb:
        r6 = com.airbnb.lottie.parser.MergePathsParser.parse(r9);
        r0 = "Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().";
        r10.addWarning(r0);
        goto L_0x013b;
    L_0x0105:
        r6 = com.airbnb.lottie.parser.PolystarShapeParser.parse(r9, r10);
        goto L_0x013b;
    L_0x010a:
        r6 = com.airbnb.lottie.parser.ShapeTrimPathParser.parse(r9, r10);
        goto L_0x013b;
    L_0x010f:
        r6 = com.airbnb.lottie.parser.RectangleShapeParser.parse(r9, r10);
        goto L_0x013b;
    L_0x0114:
        r6 = com.airbnb.lottie.parser.CircleShapeParser.parse(r9, r10, r1);
        goto L_0x013b;
    L_0x0119:
        r6 = com.airbnb.lottie.parser.ShapePathParser.parse(r9, r10);
        goto L_0x013b;
    L_0x011e:
        r6 = com.airbnb.lottie.parser.AnimatableTransformParser.parse(r9, r10);
        goto L_0x013b;
    L_0x0123:
        r6 = com.airbnb.lottie.parser.GradientFillParser.parse(r9, r10);
        goto L_0x013b;
    L_0x0128:
        r6 = com.airbnb.lottie.parser.ShapeFillParser.parse(r9, r10);
        goto L_0x013b;
    L_0x012d:
        r6 = com.airbnb.lottie.parser.GradientStrokeParser.parse(r9, r10);
        goto L_0x013b;
    L_0x0132:
        r6 = com.airbnb.lottie.parser.ShapeStrokeParser.parse(r9, r10);
        goto L_0x013b;
    L_0x0137:
        r6 = com.airbnb.lottie.parser.ShapeGroupParser.parse(r9, r10);
    L_0x013b:
        r10 = r9.hasNext();
        if (r10 == 0) goto L_0x0145;
    L_0x0141:
        r9.skipValue();
        goto L_0x013b;
    L_0x0145:
        r9.endObject();
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ContentModelParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.ContentModel");
    }
}
