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
        LayerType layerType = LayerType.PreComp;
        List emptyList = Collections.emptyList();
        AnimatableTransform animatableTransform = r5;
        AnimatableTransform animatableTransform2 = new AnimatableTransform();
        return new Layer(Collections.emptyList(), lottieComposition2, "__container", -1, layerType, -1, null, emptyList, animatableTransform, 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), MatteType.None, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:115:0x0241  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0262  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0241  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0262  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0245  */
    public static com.airbnb.lottie.model.layer.Layer parse(android.util.JsonReader r36, com.airbnb.lottie.LottieComposition r37) throws java.io.IOException {
        /*
        r7 = r37;
        r0 = "UNSET";
        r1 = com.airbnb.lottie.model.layer.Layer.MatteType.None;
        r10 = new java.util.ArrayList;
        r10.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r36.beginObject();
        r2 = 0;
        r11 = 0;
        r3 = 0;
        r4 = 0;
        r12 = -1;
        r30 = r1;
        r14 = r2;
        r20 = r14;
        r21 = r20;
        r28 = r21;
        r29 = r28;
        r31 = r29;
        r16 = r4;
        r18 = r12;
        r1 = 0;
        r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r12 = r0;
        r13 = r31;
        r0 = 0;
    L_0x003e:
        r2 = r36.hasNext();
        if (r2 == 0) goto L_0x031d;
    L_0x0044:
        r2 = r36.nextName();
        r4 = r2.hashCode();
        r5 = 1;
        switch(r4) {
            case -995424086: goto L_0x013f;
            case -903568142: goto L_0x0134;
            case 104: goto L_0x0129;
            case 116: goto L_0x011e;
            case 119: goto L_0x0113;
            case 3177: goto L_0x0108;
            case 3233: goto L_0x00fd;
            case 3367: goto L_0x00f2;
            case 3432: goto L_0x00e7;
            case 3519: goto L_0x00dc;
            case 3553: goto L_0x00d0;
            case 3664: goto L_0x00c5;
            case 3669: goto L_0x00ba;
            case 3679: goto L_0x00ae;
            case 3681: goto L_0x00a2;
            case 3684: goto L_0x0097;
            case 3705: goto L_0x008b;
            case 3712: goto L_0x007f;
            case 3717: goto L_0x0074;
            case 104415: goto L_0x0069;
            case 108390670: goto L_0x005e;
            case 1441620890: goto L_0x0052;
            default: goto L_0x0050;
        };
    L_0x0050:
        goto L_0x0149;
    L_0x0052:
        r4 = "masksProperties";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x005a:
        r2 = 10;
        goto L_0x014a;
    L_0x005e:
        r4 = "refId";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0066:
        r2 = 2;
        goto L_0x014a;
    L_0x0069:
        r4 = "ind";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0071:
        r2 = 1;
        goto L_0x014a;
    L_0x0074:
        r4 = "ty";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x007c:
        r2 = 3;
        goto L_0x014a;
    L_0x007f:
        r4 = "tt";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0087:
        r2 = 9;
        goto L_0x014a;
    L_0x008b:
        r4 = "tm";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0093:
        r2 = 20;
        goto L_0x014a;
    L_0x0097:
        r4 = "sw";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x009f:
        r2 = 5;
        goto L_0x014a;
    L_0x00a2:
        r4 = "st";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00aa:
        r2 = 15;
        goto L_0x014a;
    L_0x00ae:
        r4 = "sr";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00b6:
        r2 = 14;
        goto L_0x014a;
    L_0x00ba:
        r4 = "sh";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00c2:
        r2 = 6;
        goto L_0x014a;
    L_0x00c5:
        r4 = "sc";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00cd:
        r2 = 7;
        goto L_0x014a;
    L_0x00d0:
        r4 = "op";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00d8:
        r2 = 19;
        goto L_0x014a;
    L_0x00dc:
        r4 = "nm";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00e4:
        r2 = 0;
        goto L_0x014a;
    L_0x00e7:
        r4 = "ks";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00ef:
        r2 = 8;
        goto L_0x014a;
    L_0x00f2:
        r4 = "ip";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x00fa:
        r2 = 18;
        goto L_0x014a;
    L_0x00fd:
        r4 = "ef";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0105:
        r2 = 13;
        goto L_0x014a;
    L_0x0108:
        r4 = "cl";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0110:
        r2 = 21;
        goto L_0x014a;
    L_0x0113:
        r4 = "w";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x011b:
        r2 = 16;
        goto L_0x014a;
    L_0x011e:
        r4 = "t";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0126:
        r2 = 12;
        goto L_0x014a;
    L_0x0129:
        r4 = "h";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0131:
        r2 = 17;
        goto L_0x014a;
    L_0x0134:
        r4 = "shapes";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x013c:
        r2 = 11;
        goto L_0x014a;
    L_0x013f:
        r4 = "parent";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0149;
    L_0x0147:
        r2 = 4;
        goto L_0x014a;
    L_0x0149:
        r2 = -1;
    L_0x014a:
        switch(r2) {
            case 0: goto L_0x0314;
            case 1: goto L_0x030a;
            case 2: goto L_0x0303;
            case 3: goto L_0x02eb;
            case 4: goto L_0x02e1;
            case 5: goto L_0x02d0;
            case 6: goto L_0x02bf;
            case 7: goto L_0x02b4;
            case 8: goto L_0x02ac;
            case 9: goto L_0x029e;
            case 10: goto L_0x0286;
            case 11: goto L_0x026c;
            case 12: goto L_0x020d;
            case 13: goto L_0x01af;
            case 14: goto L_0x01a6;
            case 15: goto L_0x019b;
            case 16: goto L_0x0189;
            case 17: goto L_0x0177;
            case 18: goto L_0x016e;
            case 19: goto L_0x0165;
            case 20: goto L_0x015d;
            case 21: goto L_0x0154;
            default: goto L_0x014d;
        };
    L_0x014d:
        r2 = r36;
        r36.skipValue();
        goto L_0x031a;
    L_0x0154:
        r2 = r36.nextString();
        r13 = r2;
        r2 = r36;
        goto L_0x031a;
    L_0x015d:
        r2 = r36;
        r31 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r2, r7, r3);
        goto L_0x031a;
    L_0x0165:
        r2 = r36;
        r4 = r36.nextDouble();
        r1 = (float) r4;
        goto L_0x031a;
    L_0x016e:
        r2 = r36;
        r4 = r36.nextDouble();
        r0 = (float) r4;
        goto L_0x031a;
    L_0x0177:
        r2 = r36;
        r4 = r36.nextInt();
        r4 = (float) r4;
        r5 = com.airbnb.lottie.utils.Utils.dpScale();
        r4 = r4 * r5;
        r4 = (int) r4;
        r27 = r4;
        goto L_0x031a;
    L_0x0189:
        r2 = r36;
        r4 = r36.nextInt();
        r4 = (float) r4;
        r5 = com.airbnb.lottie.utils.Utils.dpScale();
        r4 = r4 * r5;
        r4 = (int) r4;
        r26 = r4;
        goto L_0x031a;
    L_0x019b:
        r2 = r36;
        r4 = r36.nextDouble();
        r4 = (float) r4;
        r25 = r4;
        goto L_0x031a;
    L_0x01a6:
        r2 = r36;
        r4 = r36.nextDouble();
        r15 = (float) r4;
        goto L_0x031a;
    L_0x01af:
        r2 = r36;
        r36.beginArray();
        r4 = new java.util.ArrayList;
        r4.<init>();
    L_0x01b9:
        r5 = r36.hasNext();
        if (r5 == 0) goto L_0x01f4;
    L_0x01bf:
        r36.beginObject();
    L_0x01c2:
        r5 = r36.hasNext();
        if (r5 == 0) goto L_0x01ef;
    L_0x01c8:
        r5 = r36.nextName();
        r3 = r5.hashCode();
        r6 = 3519; // 0xdbf float:4.931E-42 double:1.7386E-320;
        if (r3 == r6) goto L_0x01d5;
    L_0x01d4:
        goto L_0x01df;
    L_0x01d5:
        r3 = "nm";
        r3 = r5.equals(r3);
        if (r3 == 0) goto L_0x01df;
    L_0x01dd:
        r3 = 0;
        goto L_0x01e0;
    L_0x01df:
        r3 = -1;
    L_0x01e0:
        if (r3 == 0) goto L_0x01e6;
    L_0x01e2:
        r36.skipValue();
        goto L_0x01ed;
    L_0x01e6:
        r3 = r36.nextString();
        r4.add(r3);
    L_0x01ed:
        r3 = 0;
        goto L_0x01c2;
    L_0x01ef:
        r36.endObject();
        r3 = 0;
        goto L_0x01b9;
    L_0x01f4:
        r36.endArray();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ";
        r3.append(r5);
        r3.append(r4);
        r3 = r3.toString();
        r7.addWarning(r3);
        goto L_0x031a;
    L_0x020d:
        r2 = r36;
        r36.beginObject();
    L_0x0212:
        r3 = r36.hasNext();
        if (r3 == 0) goto L_0x0267;
    L_0x0218:
        r3 = r36.nextName();
        r4 = r3.hashCode();
        r6 = 97;
        if (r4 == r6) goto L_0x0233;
    L_0x0224:
        r6 = 100;
        if (r4 == r6) goto L_0x0229;
    L_0x0228:
        goto L_0x023d;
    L_0x0229:
        r4 = "d";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x023d;
    L_0x0231:
        r3 = 0;
        goto L_0x023e;
    L_0x0233:
        r4 = "a";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x023d;
    L_0x023b:
        r3 = 1;
        goto L_0x023e;
    L_0x023d:
        r3 = -1;
    L_0x023e:
        switch(r3) {
            case 0: goto L_0x0262;
            case 1: goto L_0x0245;
            default: goto L_0x0241;
        };
    L_0x0241:
        r36.skipValue();
        goto L_0x0212;
    L_0x0245:
        r36.beginArray();
        r3 = r36.hasNext();
        if (r3 == 0) goto L_0x0254;
    L_0x024e:
        r3 = com.airbnb.lottie.parser.AnimatableTextPropertiesParser.parse(r36, r37);
        r29 = r3;
    L_0x0254:
        r3 = r36.hasNext();
        if (r3 == 0) goto L_0x025e;
    L_0x025a:
        r36.skipValue();
        goto L_0x0254;
    L_0x025e:
        r36.endArray();
        goto L_0x0212;
    L_0x0262:
        r28 = com.airbnb.lottie.parser.AnimatableValueParser.parseDocumentData(r36, r37);
        goto L_0x0212;
    L_0x0267:
        r36.endObject();
        goto L_0x031a;
    L_0x026c:
        r2 = r36;
        r36.beginArray();
    L_0x0271:
        r3 = r36.hasNext();
        if (r3 == 0) goto L_0x0281;
    L_0x0277:
        r3 = com.airbnb.lottie.parser.ContentModelParser.parse(r36, r37);
        if (r3 == 0) goto L_0x0271;
    L_0x027d:
        r8.add(r3);
        goto L_0x0271;
    L_0x0281:
        r36.endArray();
        goto L_0x031a;
    L_0x0286:
        r2 = r36;
        r36.beginArray();
    L_0x028b:
        r3 = r36.hasNext();
        if (r3 == 0) goto L_0x0299;
    L_0x0291:
        r3 = com.airbnb.lottie.parser.MaskParser.parse(r36, r37);
        r10.add(r3);
        goto L_0x028b;
    L_0x0299:
        r36.endArray();
        goto L_0x031a;
    L_0x029e:
        r2 = r36;
        r3 = com.airbnb.lottie.model.layer.Layer.MatteType.values();
        r4 = r36.nextInt();
        r30 = r3[r4];
        goto L_0x031a;
    L_0x02ac:
        r2 = r36;
        r21 = com.airbnb.lottie.parser.AnimatableTransformParser.parse(r36, r37);
        goto L_0x031a;
    L_0x02b4:
        r2 = r36;
        r3 = r36.nextString();
        r24 = android.graphics.Color.parseColor(r3);
        goto L_0x031a;
    L_0x02bf:
        r2 = r36;
        r3 = r36.nextInt();
        r3 = (float) r3;
        r4 = com.airbnb.lottie.utils.Utils.dpScale();
        r3 = r3 * r4;
        r3 = (int) r3;
        r23 = r3;
        goto L_0x031a;
    L_0x02d0:
        r2 = r36;
        r3 = r36.nextInt();
        r3 = (float) r3;
        r4 = com.airbnb.lottie.utils.Utils.dpScale();
        r3 = r3 * r4;
        r3 = (int) r3;
        r22 = r3;
        goto L_0x031a;
    L_0x02e1:
        r2 = r36;
        r3 = r36.nextInt();
        r3 = (long) r3;
        r18 = r3;
        goto L_0x031a;
    L_0x02eb:
        r2 = r36;
        r3 = r36.nextInt();
        r4 = com.airbnb.lottie.model.layer.Layer.LayerType.Unknown;
        r4 = r4.ordinal();
        if (r3 >= r4) goto L_0x0300;
    L_0x02f9:
        r4 = com.airbnb.lottie.model.layer.Layer.LayerType.values();
        r14 = r4[r3];
        goto L_0x031a;
    L_0x0300:
        r14 = com.airbnb.lottie.model.layer.Layer.LayerType.Unknown;
        goto L_0x031a;
    L_0x0303:
        r2 = r36;
        r20 = r36.nextString();
        goto L_0x031a;
    L_0x030a:
        r2 = r36;
        r3 = r36.nextInt();
        r3 = (long) r3;
        r16 = r3;
        goto L_0x031a;
    L_0x0314:
        r2 = r36;
        r12 = r36.nextString();
    L_0x031a:
        r3 = 0;
        goto L_0x003e;
    L_0x031d:
        r2 = r36;
        r36.endObject();
        r32 = r0 / r15;
        r33 = r1 / r15;
        r6 = new java.util.ArrayList;
        r6.<init>();
        r0 = (r32 > r11 ? 1 : (r32 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0350;
    L_0x032f:
        r5 = new com.airbnb.lottie.value.Keyframe;
        r2 = java.lang.Float.valueOf(r11);
        r3 = java.lang.Float.valueOf(r11);
        r4 = 0;
        r34 = 0;
        r35 = java.lang.Float.valueOf(r32);
        r0 = r5;
        r1 = r37;
        r9 = r5;
        r5 = r34;
        r11 = r6;
        r6 = r35;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r11.add(r9);
        goto L_0x0351;
    L_0x0350:
        r11 = r6;
    L_0x0351:
        r0 = 0;
        r1 = (r33 > r0 ? 1 : (r33 == r0 ? 0 : -1));
        if (r1 <= 0) goto L_0x0357;
    L_0x0356:
        goto L_0x035d;
    L_0x0357:
        r0 = r37.getEndFrame();
        r33 = r0;
    L_0x035d:
        r9 = new com.airbnb.lottie.value.Keyframe;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = java.lang.Float.valueOf(r0);
        r3 = java.lang.Float.valueOf(r0);
        r4 = 0;
        r6 = java.lang.Float.valueOf(r33);
        r0 = r9;
        r1 = r37;
        r5 = r32;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r11.add(r9);
        r9 = new com.airbnb.lottie.value.Keyframe;
        r0 = 0;
        r2 = java.lang.Float.valueOf(r0);
        r3 = java.lang.Float.valueOf(r0);
        r0 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
        r6 = java.lang.Float.valueOf(r0);
        r0 = r9;
        r5 = r33;
        r0.<init>(r1, r2, r3, r4, r5, r6);
        r11.add(r9);
        r0 = ".ai";
        r0 = r12.endsWith(r0);
        if (r0 != 0) goto L_0x03a4;
    L_0x039c:
        r0 = "ai";
        r0 = r0.equals(r13);
        if (r0 == 0) goto L_0x03a9;
    L_0x03a4:
        r0 = "Convert your Illustrator layers to shape layers.";
        r7.addWarning(r0);
    L_0x03a9:
        r32 = new com.airbnb.lottie.model.layer.Layer;
        r0 = r32;
        r1 = r8;
        r2 = r37;
        r3 = r12;
        r4 = r16;
        r6 = r14;
        r7 = r18;
        r9 = r20;
        r33 = r11;
        r11 = r21;
        r12 = r22;
        r13 = r23;
        r14 = r24;
        r16 = r25;
        r17 = r26;
        r18 = r27;
        r19 = r28;
        r20 = r29;
        r21 = r33;
        r22 = r30;
        r23 = r31;
        r0.<init>(r1, r2, r3, r4, r6, r7, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23);
        return r32;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.LayerParser.parse(android.util.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.layer.Layer");
    }
}
