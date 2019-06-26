package com.airbnb.lottie.parser;

import android.graphics.Rect;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.model.layer.Layer.LayerType;
import com.airbnb.lottie.model.layer.Layer.MatteType;
import java.util.Collections;
import java.util.List;

public class LayerParser {
    public static Layer parse(LottieComposition lottieComposition) {
        LottieComposition lottieComposition2 = lottieComposition;
        Rect bounds = lottieComposition.getBounds();
        List emptyList = Collections.emptyList();
        LayerType layerType = LayerType.PRE_COMP;
        List emptyList2 = Collections.emptyList();
        AnimatableTransform animatableTransform = r4;
        AnimatableTransform animatableTransform2 = new AnimatableTransform();
        return new Layer(emptyList, lottieComposition2, "__container", -1, layerType, -1, null, emptyList2, animatableTransform, 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), MatteType.NONE, null, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:129:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0281  */
    public static com.airbnb.lottie.model.layer.Layer parse(android.util.JsonReader r39, com.airbnb.lottie.LottieComposition r40) throws java.io.IOException {
        /*
        r7 = r40;
        r0 = com.airbnb.lottie.model.layer.Layer.MatteType.NONE;
        r10 = new java.util.ArrayList;
        r10.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r39.beginObject();
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = java.lang.Float.valueOf(r1);
        r2 = 0;
        r11 = 0;
        r12 = java.lang.Float.valueOf(r11);
        r3 = 0;
        r4 = "UNSET";
        r5 = 0;
        r13 = -1;
        r28 = r0;
        r16 = r2;
        r17 = r16;
        r30 = r17;
        r31 = r30;
        r32 = r31;
        r33 = r32;
        r18 = r5;
        r25 = r13;
        r0 = 0;
        r1 = 0;
        r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r27 = 0;
        r29 = 0;
        r14 = r33;
        r13 = r4;
    L_0x004b:
        r2 = r39.hasNext();
        if (r2 == 0) goto L_0x0347;
    L_0x0051:
        r2 = r39.nextName();
        r4 = r2.hashCode();
        r5 = "nm";
        r6 = 1;
        switch(r4) {
            case -995424086: goto L_0x0158;
            case -903568142: goto L_0x014d;
            case 104: goto L_0x0142;
            case 116: goto L_0x0137;
            case 119: goto L_0x012c;
            case 3177: goto L_0x0121;
            case 3233: goto L_0x0116;
            case 3324: goto L_0x010b;
            case 3367: goto L_0x0100;
            case 3432: goto L_0x00f4;
            case 3519: goto L_0x00eb;
            case 3553: goto L_0x00df;
            case 3664: goto L_0x00d4;
            case 3669: goto L_0x00c9;
            case 3679: goto L_0x00bd;
            case 3681: goto L_0x00b1;
            case 3684: goto L_0x00a6;
            case 3705: goto L_0x009a;
            case 3712: goto L_0x008e;
            case 3717: goto L_0x0083;
            case 104415: goto L_0x0078;
            case 108390670: goto L_0x006d;
            case 1441620890: goto L_0x0061;
            default: goto L_0x005f;
        };
    L_0x005f:
        goto L_0x0162;
    L_0x0061:
        r4 = "masksProperties";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0069:
        r2 = 10;
        goto L_0x0163;
    L_0x006d:
        r4 = "refId";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0075:
        r2 = 2;
        goto L_0x0163;
    L_0x0078:
        r4 = "ind";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0080:
        r2 = 1;
        goto L_0x0163;
    L_0x0083:
        r4 = "ty";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x008b:
        r2 = 3;
        goto L_0x0163;
    L_0x008e:
        r4 = "tt";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0096:
        r2 = 9;
        goto L_0x0163;
    L_0x009a:
        r4 = "tm";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00a2:
        r2 = 20;
        goto L_0x0163;
    L_0x00a6:
        r4 = "sw";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00ae:
        r2 = 5;
        goto L_0x0163;
    L_0x00b1:
        r4 = "st";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00b9:
        r2 = 15;
        goto L_0x0163;
    L_0x00bd:
        r4 = "sr";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00c5:
        r2 = 14;
        goto L_0x0163;
    L_0x00c9:
        r4 = "sh";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00d1:
        r2 = 6;
        goto L_0x0163;
    L_0x00d4:
        r4 = "sc";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00dc:
        r2 = 7;
        goto L_0x0163;
    L_0x00df:
        r4 = "op";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00e7:
        r2 = 19;
        goto L_0x0163;
    L_0x00eb:
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x0162;
    L_0x00f1:
        r2 = 0;
        goto L_0x0163;
    L_0x00f4:
        r4 = "ks";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x00fc:
        r2 = 8;
        goto L_0x0163;
    L_0x0100:
        r4 = "ip";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0108:
        r2 = 18;
        goto L_0x0163;
    L_0x010b:
        r4 = "hd";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0113:
        r2 = 22;
        goto L_0x0163;
    L_0x0116:
        r4 = "ef";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x011e:
        r2 = 13;
        goto L_0x0163;
    L_0x0121:
        r4 = "cl";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0129:
        r2 = 21;
        goto L_0x0163;
    L_0x012c:
        r4 = "w";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0134:
        r2 = 16;
        goto L_0x0163;
    L_0x0137:
        r4 = "t";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x013f:
        r2 = 12;
        goto L_0x0163;
    L_0x0142:
        r4 = "h";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x014a:
        r2 = 17;
        goto L_0x0163;
    L_0x014d:
        r4 = "shapes";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0155:
        r2 = 11;
        goto L_0x0163;
    L_0x0158:
        r4 = "parent";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0162;
    L_0x0160:
        r2 = 4;
        goto L_0x0163;
    L_0x0162:
        r2 = -1;
    L_0x0163:
        switch(r2) {
            case 0: goto L_0x033d;
            case 1: goto L_0x0333;
            case 2: goto L_0x032c;
            case 3: goto L_0x0314;
            case 4: goto L_0x030a;
            case 5: goto L_0x02f9;
            case 6: goto L_0x02e8;
            case 7: goto L_0x02dd;
            case 8: goto L_0x02d5;
            case 9: goto L_0x02c4;
            case 10: goto L_0x02a5;
            case 11: goto L_0x028b;
            case 12: goto L_0x022b;
            case 13: goto L_0x01cd;
            case 14: goto L_0x01c4;
            case 15: goto L_0x01b9;
            case 16: goto L_0x01a7;
            case 17: goto L_0x0195;
            case 18: goto L_0x018c;
            case 19: goto L_0x0183;
            case 20: goto L_0x017b;
            case 21: goto L_0x0172;
            case 22: goto L_0x016d;
            default: goto L_0x0166;
        };
    L_0x0166:
        r2 = r39;
        r39.skipValue();
        goto L_0x0343;
    L_0x016d:
        r29 = r39.nextBoolean();
        goto L_0x0177;
    L_0x0172:
        r2 = r39.nextString();
        r14 = r2;
    L_0x0177:
        r2 = r39;
        goto L_0x0343;
    L_0x017b:
        r2 = r39;
        r33 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r2, r7, r3);
        goto L_0x0343;
    L_0x0183:
        r2 = r39;
        r4 = r39.nextDouble();
        r1 = (float) r4;
        goto L_0x0343;
    L_0x018c:
        r2 = r39;
        r4 = r39.nextDouble();
        r0 = (float) r4;
        goto L_0x0343;
    L_0x0195:
        r2 = r39;
        r4 = r39.nextInt();
        r4 = (float) r4;
        r5 = com.airbnb.lottie.utils.Utils.dpScale();
        r4 = r4 * r5;
        r4 = (int) r4;
        r24 = r4;
        goto L_0x0343;
    L_0x01a7:
        r2 = r39;
        r4 = r39.nextInt();
        r4 = (float) r4;
        r5 = com.airbnb.lottie.utils.Utils.dpScale();
        r4 = r4 * r5;
        r4 = (int) r4;
        r23 = r4;
        goto L_0x0343;
    L_0x01b9:
        r2 = r39;
        r4 = r39.nextDouble();
        r4 = (float) r4;
        r27 = r4;
        goto L_0x0343;
    L_0x01c4:
        r2 = r39;
        r4 = r39.nextDouble();
        r15 = (float) r4;
        goto L_0x0343;
    L_0x01cd:
        r2 = r39;
        r39.beginArray();
        r4 = new java.util.ArrayList;
        r4.<init>();
    L_0x01d7:
        r6 = r39.hasNext();
        if (r6 == 0) goto L_0x0212;
    L_0x01dd:
        r39.beginObject();
    L_0x01e0:
        r6 = r39.hasNext();
        if (r6 == 0) goto L_0x020c;
    L_0x01e6:
        r6 = r39.nextName();
        r3 = r6.hashCode();
        r11 = 3519; // 0xdbf float:4.931E-42 double:1.7386E-320;
        if (r3 == r11) goto L_0x01f3;
    L_0x01f2:
        goto L_0x01fb;
    L_0x01f3:
        r3 = r6.equals(r5);
        if (r3 == 0) goto L_0x01fb;
    L_0x01f9:
        r3 = 0;
        goto L_0x01fc;
    L_0x01fb:
        r3 = -1;
    L_0x01fc:
        if (r3 == 0) goto L_0x0202;
    L_0x01fe:
        r39.skipValue();
        goto L_0x0209;
    L_0x0202:
        r3 = r39.nextString();
        r4.add(r3);
    L_0x0209:
        r3 = 0;
        r11 = 0;
        goto L_0x01e0;
    L_0x020c:
        r39.endObject();
        r3 = 0;
        r11 = 0;
        goto L_0x01d7;
    L_0x0212:
        r39.endArray();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ";
        r3.append(r5);
        r3.append(r4);
        r3 = r3.toString();
        r7.addWarning(r3);
        goto L_0x0343;
    L_0x022b:
        r2 = r39;
        r39.beginObject();
    L_0x0230:
        r3 = r39.hasNext();
        if (r3 == 0) goto L_0x0286;
    L_0x0236:
        r3 = r39.nextName();
        r4 = r3.hashCode();
        r5 = 97;
        if (r4 == r5) goto L_0x0251;
    L_0x0242:
        r5 = 100;
        if (r4 == r5) goto L_0x0247;
    L_0x0246:
        goto L_0x025b;
    L_0x0247:
        r4 = "d";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x025b;
    L_0x024f:
        r3 = 0;
        goto L_0x025c;
    L_0x0251:
        r4 = "a";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x025b;
    L_0x0259:
        r3 = 1;
        goto L_0x025c;
    L_0x025b:
        r3 = -1;
    L_0x025c:
        if (r3 == 0) goto L_0x0281;
    L_0x025e:
        if (r3 == r6) goto L_0x0264;
    L_0x0260:
        r39.skipValue();
        goto L_0x0230;
    L_0x0264:
        r39.beginArray();
        r3 = r39.hasNext();
        if (r3 == 0) goto L_0x0273;
    L_0x026d:
        r3 = com.airbnb.lottie.parser.AnimatableTextPropertiesParser.parse(r39, r40);
        r32 = r3;
    L_0x0273:
        r3 = r39.hasNext();
        if (r3 == 0) goto L_0x027d;
    L_0x0279:
        r39.skipValue();
        goto L_0x0273;
    L_0x027d:
        r39.endArray();
        goto L_0x0230;
    L_0x0281:
        r31 = com.airbnb.lottie.parser.AnimatableValueParser.parseDocumentData(r39, r40);
        goto L_0x0230;
    L_0x0286:
        r39.endObject();
        goto L_0x0343;
    L_0x028b:
        r2 = r39;
        r39.beginArray();
    L_0x0290:
        r3 = r39.hasNext();
        if (r3 == 0) goto L_0x02a0;
    L_0x0296:
        r3 = com.airbnb.lottie.parser.ContentModelParser.parse(r39, r40);
        if (r3 == 0) goto L_0x0290;
    L_0x029c:
        r8.add(r3);
        goto L_0x0290;
    L_0x02a0:
        r39.endArray();
        goto L_0x0343;
    L_0x02a5:
        r2 = r39;
        r39.beginArray();
    L_0x02aa:
        r3 = r39.hasNext();
        if (r3 == 0) goto L_0x02b8;
    L_0x02b0:
        r3 = com.airbnb.lottie.parser.MaskParser.parse(r39, r40);
        r10.add(r3);
        goto L_0x02aa;
    L_0x02b8:
        r3 = r10.size();
        r7.incrementMatteOrMaskCount(r3);
        r39.endArray();
        goto L_0x0343;
    L_0x02c4:
        r2 = r39;
        r3 = com.airbnb.lottie.model.layer.Layer.MatteType.values();
        r4 = r39.nextInt();
        r28 = r3[r4];
        r7.incrementMatteOrMaskCount(r6);
        goto L_0x0343;
    L_0x02d5:
        r2 = r39;
        r30 = com.airbnb.lottie.parser.AnimatableTransformParser.parse(r39, r40);
        goto L_0x0343;
    L_0x02dd:
        r2 = r39;
        r3 = r39.nextString();
        r22 = android.graphics.Color.parseColor(r3);
        goto L_0x0343;
    L_0x02e8:
        r2 = r39;
        r3 = r39.nextInt();
        r3 = (float) r3;
        r4 = com.airbnb.lottie.utils.Utils.dpScale();
        r3 = r3 * r4;
        r3 = (int) r3;
        r21 = r3;
        goto L_0x0343;
    L_0x02f9:
        r2 = r39;
        r3 = r39.nextInt();
        r3 = (float) r3;
        r4 = com.airbnb.lottie.utils.Utils.dpScale();
        r3 = r3 * r4;
        r3 = (int) r3;
        r20 = r3;
        goto L_0x0343;
    L_0x030a:
        r2 = r39;
        r3 = r39.nextInt();
        r3 = (long) r3;
        r25 = r3;
        goto L_0x0343;
    L_0x0314:
        r2 = r39;
        r3 = r39.nextInt();
        r4 = com.airbnb.lottie.model.layer.Layer.LayerType.UNKNOWN;
        r4 = r4.ordinal();
        if (r3 >= r4) goto L_0x0329;
    L_0x0322:
        r4 = com.airbnb.lottie.model.layer.Layer.LayerType.values();
        r16 = r4[r3];
        goto L_0x0343;
    L_0x0329:
        r16 = com.airbnb.lottie.model.layer.Layer.LayerType.UNKNOWN;
        goto L_0x0343;
    L_0x032c:
        r2 = r39;
        r17 = r39.nextString();
        goto L_0x0343;
    L_0x0333:
        r2 = r39;
        r3 = r39.nextInt();
        r3 = (long) r3;
        r18 = r3;
        goto L_0x0343;
    L_0x033d:
        r2 = r39;
        r13 = r39.nextString();
    L_0x0343:
        r3 = 0;
        r11 = 0;
        goto L_0x004b;
    L_0x0347:
        r2 = r39;
        r39.endObject();
        r11 = r0 / r15;
        r34 = r1 / r15;
        r6 = new java.util.ArrayList;
        r6.<init>();
        r0 = 0;
        r1 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1));
        if (r1 <= 0) goto L_0x037a;
    L_0x035a:
        r5 = new com.airbnb.lottie.value.Keyframe;
        r4 = 0;
        r35 = 0;
        r37 = java.lang.Float.valueOf(r11);
        r0 = r5;
        r1 = r40;
        r2 = r12;
        r3 = r12;
        r38 = r5;
        r5 = r35;
        r35 = r15;
        r15 = r6;
        r6 = r37;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r0 = r38;
        r15.add(r0);
        goto L_0x037d;
    L_0x037a:
        r35 = r15;
        r15 = r6;
    L_0x037d:
        r0 = 0;
        r0 = (r34 > r0 ? 1 : (r34 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x0383;
    L_0x0382:
        goto L_0x0389;
    L_0x0383:
        r0 = r40.getEndFrame();
        r34 = r0;
    L_0x0389:
        r6 = new com.airbnb.lottie.value.Keyframe;
        r4 = 0;
        r36 = java.lang.Float.valueOf(r34);
        r0 = r6;
        r1 = r40;
        r2 = r9;
        r3 = r9;
        r5 = r11;
        r9 = r6;
        r6 = r36;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r15.add(r9);
        r9 = new com.airbnb.lottie.value.Keyframe;
        r0 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        r6 = java.lang.Float.valueOf(r0);
        r0 = r9;
        r2 = r12;
        r3 = r12;
        r5 = r34;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r15.add(r9);
        r0 = ".ai";
        r0 = r13.endsWith(r0);
        if (r0 != 0) goto L_0x03c3;
    L_0x03bb:
        r0 = "ai";
        r0 = r0.equals(r14);
        if (r0 == 0) goto L_0x03c8;
    L_0x03c3:
        r0 = "Convert your Illustrator layers to shape layers.";
        r7.addWarning(r0);
    L_0x03c8:
        r34 = new com.airbnb.lottie.model.layer.Layer;
        r0 = r34;
        r1 = r8;
        r2 = r40;
        r3 = r13;
        r4 = r18;
        r6 = r16;
        r7 = r25;
        r9 = r17;
        r11 = r30;
        r12 = r20;
        r13 = r21;
        r14 = r22;
        r21 = r15;
        r15 = r35;
        r16 = r27;
        r17 = r23;
        r18 = r24;
        r19 = r31;
        r20 = r32;
        r22 = r28;
        r23 = r33;
        r24 = r29;
        r0.<init>(r1, r2, r3, r4, r6, r7, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24);
        return r34;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.LayerParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.layer.Layer");
    }
}
