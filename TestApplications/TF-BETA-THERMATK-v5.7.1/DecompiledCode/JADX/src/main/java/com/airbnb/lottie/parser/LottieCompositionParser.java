package com.airbnb.lottie.parser;

import android.util.JsonReader;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.model.layer.Layer.LayerType;
import com.airbnb.lottie.utils.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LottieCompositionParser {
    public static com.airbnb.lottie.LottieComposition parse(android.util.JsonReader r27) throws java.io.IOException {
        /*
        r0 = r27;
        r1 = com.airbnb.lottie.utils.Utils.dpScale();
        r8 = new androidx.collection.LongSparseArray;
        r8.<init>();
        r7 = new java.util.ArrayList;
        r7.<init>();
        r9 = new java.util.HashMap;
        r9.<init>();
        r10 = new java.util.HashMap;
        r10.<init>();
        r12 = new java.util.HashMap;
        r12.<init>();
        r13 = new java.util.ArrayList;
        r13.<init>();
        r11 = new androidx.collection.SparseArrayCompat;
        r11.<init>();
        r14 = new com.airbnb.lottie.LottieComposition;
        r14.<init>();
        r27.beginObject();
        r2 = 0;
        r2 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r15 = 0;
    L_0x0037:
        r16 = r27.hasNext();
        if (r16 == 0) goto L_0x0167;
    L_0x003d:
        r3 = r27.nextName();
        r17 = -1;
        r18 = r3.hashCode();
        r19 = 2;
        r20 = 1;
        switch(r18) {
            case -1408207997: goto L_0x00d0;
            case -1109732030: goto L_0x00c4;
            case 104: goto L_0x00b8;
            case 118: goto L_0x00ac;
            case 119: goto L_0x00a0;
            case 3276: goto L_0x0094;
            case 3367: goto L_0x0088;
            case 3553: goto L_0x007c;
            case 94623709: goto L_0x006e;
            case 97615364: goto L_0x0060;
            case 839250809: goto L_0x0052;
            default: goto L_0x004e;
        };
    L_0x004e:
        r18 = r15;
        goto L_0x00dc;
    L_0x0052:
        r18 = r15;
        r15 = "markers";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x005c:
        r3 = 10;
        goto L_0x00dd;
    L_0x0060:
        r18 = r15;
        r15 = "fonts";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x006a:
        r3 = 8;
        goto L_0x00dd;
    L_0x006e:
        r18 = r15;
        r15 = "chars";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x0078:
        r3 = 9;
        goto L_0x00dd;
    L_0x007c:
        r18 = r15;
        r15 = "op";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x0086:
        r3 = 3;
        goto L_0x00dd;
    L_0x0088:
        r18 = r15;
        r15 = "ip";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x0092:
        r3 = 2;
        goto L_0x00dd;
    L_0x0094:
        r18 = r15;
        r15 = "fr";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x009e:
        r3 = 4;
        goto L_0x00dd;
    L_0x00a0:
        r18 = r15;
        r15 = "w";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x00aa:
        r3 = 0;
        goto L_0x00dd;
    L_0x00ac:
        r18 = r15;
        r15 = "v";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x00b6:
        r3 = 5;
        goto L_0x00dd;
    L_0x00b8:
        r18 = r15;
        r15 = "h";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x00c2:
        r3 = 1;
        goto L_0x00dd;
    L_0x00c4:
        r18 = r15;
        r15 = "layers";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x00ce:
        r3 = 6;
        goto L_0x00dd;
    L_0x00d0:
        r18 = r15;
        r15 = "assets";
        r3 = r3.equals(r15);
        if (r3 == 0) goto L_0x00dc;
    L_0x00da:
        r3 = 7;
        goto L_0x00dd;
    L_0x00dc:
        r3 = -1;
    L_0x00dd:
        switch(r3) {
            case 0: goto L_0x0159;
            case 1: goto L_0x0150;
            case 2: goto L_0x0147;
            case 3: goto L_0x0139;
            case 4: goto L_0x012e;
            case 5: goto L_0x00fc;
            case 6: goto L_0x00f8;
            case 7: goto L_0x00f4;
            case 8: goto L_0x00f0;
            case 9: goto L_0x00ec;
            case 10: goto L_0x00e8;
            default: goto L_0x00e0;
        };
    L_0x00e0:
        r15 = r12;
        r17 = r13;
        r27.skipValue();
        goto L_0x0160;
    L_0x00e8:
        parseMarkers(r0, r14, r13);
        goto L_0x012a;
    L_0x00ec:
        parseChars(r0, r14, r11);
        goto L_0x012a;
    L_0x00f0:
        parseFonts(r0, r12);
        goto L_0x012a;
    L_0x00f4:
        parseAssets(r0, r14, r9, r10);
        goto L_0x012a;
    L_0x00f8:
        parseLayers(r0, r14, r7, r8);
        goto L_0x012a;
    L_0x00fc:
        r3 = r27.nextString();
        r15 = "\\.";
        r3 = r3.split(r15);
        r15 = 0;
        r17 = r3[r15];
        r21 = java.lang.Integer.parseInt(r17);
        r15 = r3[r20];
        r22 = java.lang.Integer.parseInt(r15);
        r3 = r3[r19];
        r23 = java.lang.Integer.parseInt(r3);
        r24 = 4;
        r25 = 4;
        r26 = 0;
        r3 = com.airbnb.lottie.utils.Utils.isAtLeastVersion(r21, r22, r23, r24, r25, r26);
        if (r3 != 0) goto L_0x012a;
    L_0x0125:
        r3 = "Lottie only supports bodymovin >= 4.4.0";
        r14.addWarning(r3);
    L_0x012a:
        r15 = r12;
        r17 = r13;
        goto L_0x0160;
    L_0x012e:
        r15 = r12;
        r17 = r13;
        r12 = r27.nextDouble();
        r3 = (float) r12;
        r18 = r3;
        goto L_0x0160;
    L_0x0139:
        r15 = r12;
        r17 = r13;
        r12 = r27.nextDouble();
        r3 = (float) r12;
        r6 = 1008981770; // 0x3c23d70a float:0.01 double:4.9850323E-315;
        r6 = r3 - r6;
        goto L_0x0160;
    L_0x0147:
        r15 = r12;
        r17 = r13;
        r12 = r27.nextDouble();
        r5 = (float) r12;
        goto L_0x0160;
    L_0x0150:
        r15 = r12;
        r17 = r13;
        r3 = r27.nextInt();
        r4 = r3;
        goto L_0x0160;
    L_0x0159:
        r15 = r12;
        r17 = r13;
        r2 = r27.nextInt();
    L_0x0160:
        r12 = r15;
        r13 = r17;
        r15 = r18;
        goto L_0x0037;
    L_0x0167:
        r17 = r13;
        r18 = r15;
        r15 = r12;
        r27.endObject();
        r0 = (float) r2;
        r0 = r0 * r1;
        r0 = (int) r0;
        r2 = (float) r4;
        r2 = r2 * r1;
        r1 = (int) r2;
        r3 = new android.graphics.Rect;
        r2 = 0;
        r3.<init>(r2, r2, r0, r1);
        r2 = r14;
        r4 = r5;
        r5 = r6;
        r6 = r18;
        r2.init(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        return r14;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.LottieCompositionParser.parse(android.util.JsonReader):com.airbnb.lottie.LottieComposition");
    }

    private static void parseLayers(JsonReader jsonReader, LottieComposition lottieComposition, List<Layer> list, LongSparseArray<Layer> longSparseArray) throws IOException {
        jsonReader.beginArray();
        int i = 0;
        while (jsonReader.hasNext()) {
            Layer parse = LayerParser.parse(jsonReader, lottieComposition);
            if (parse.getLayerType() == LayerType.IMAGE) {
                i++;
            }
            list.add(parse);
            longSparseArray.put(parse.getId(), parse);
            if (i > 4) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("You have ");
                stringBuilder.append(i);
                stringBuilder.append(" images. Lottie should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
                Logger.warning(stringBuilder.toString());
            }
        }
        jsonReader.endArray();
    }

    private static void parseAssets(JsonReader jsonReader, LottieComposition lottieComposition, Map<String, List<Layer>> map, Map<String, LottieImageAsset> map2) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            ArrayList arrayList = new ArrayList();
            LongSparseArray longSparseArray = new LongSparseArray();
            jsonReader.beginObject();
            String str = null;
            String str2 = str;
            String str3 = str2;
            int i = 0;
            int i2 = 0;
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                int i3 = -1;
                int hashCode = nextName.hashCode();
                if (hashCode != -1109732030) {
                    if (hashCode != 104) {
                        if (hashCode != 112) {
                            if (hashCode != 117) {
                                if (hashCode != 119) {
                                    if (hashCode == 3355 && nextName.equals("id")) {
                                        i3 = 0;
                                    }
                                } else if (nextName.equals("w")) {
                                    i3 = 2;
                                }
                            } else if (nextName.equals("u")) {
                                i3 = 5;
                            }
                        } else if (nextName.equals("p")) {
                            i3 = 4;
                        }
                    } else if (nextName.equals("h")) {
                        i3 = 3;
                    }
                } else if (nextName.equals("layers")) {
                    i3 = 1;
                }
                if (i3 == 0) {
                    str = jsonReader.nextString();
                } else if (i3 == 1) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        Layer parse = LayerParser.parse(jsonReader, lottieComposition);
                        longSparseArray.put(parse.getId(), parse);
                        arrayList.add(parse);
                    }
                    jsonReader.endArray();
                } else if (i3 == 2) {
                    i = jsonReader.nextInt();
                } else if (i3 == 3) {
                    i2 = jsonReader.nextInt();
                } else if (i3 == 4) {
                    str2 = jsonReader.nextString();
                } else if (i3 != 5) {
                    jsonReader.skipValue();
                } else {
                    str3 = jsonReader.nextString();
                }
            }
            jsonReader.endObject();
            if (str2 != null) {
                LottieImageAsset lottieImageAsset = new LottieImageAsset(i, i2, str, str2, str3);
                map2.put(lottieImageAsset.getId(), lottieImageAsset);
                Map<String, List<Layer>> map3 = map;
            } else {
                Map<String, LottieImageAsset> map4 = map2;
                map.put(str, arrayList);
            }
        }
        jsonReader.endArray();
    }

    private static void parseFonts(JsonReader jsonReader, Map<String, Font> map) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            Object obj = -1;
            if (nextName.hashCode() == 3322014 && nextName.equals("list")) {
                obj = null;
            }
            if (obj != null) {
                jsonReader.skipValue();
            } else {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    Font parse = FontParser.parse(jsonReader);
                    map.put(parse.getName(), parse);
                }
                jsonReader.endArray();
            }
        }
        jsonReader.endObject();
    }

    private static void parseChars(JsonReader jsonReader, LottieComposition lottieComposition, SparseArrayCompat<FontCharacter> sparseArrayCompat) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            FontCharacter parse = FontCharacterParser.parse(jsonReader, lottieComposition);
            sparseArrayCompat.put(parse.hashCode(), parse);
        }
        jsonReader.endArray();
    }

    private static void parseMarkers(JsonReader jsonReader, LottieComposition lottieComposition, List<Marker> list) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            String str = null;
            jsonReader.beginObject();
            float f = 0.0f;
            float f2 = 0.0f;
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                int i = -1;
                int hashCode = nextName.hashCode();
                if (hashCode != 3178) {
                    if (hashCode != 3214) {
                        if (hashCode == 3705 && nextName.equals("tm")) {
                            i = 1;
                        }
                    } else if (nextName.equals("dr")) {
                        i = 2;
                    }
                } else if (nextName.equals("cm")) {
                    i = 0;
                }
                if (i == 0) {
                    str = jsonReader.nextString();
                } else if (i == 1) {
                    f = (float) jsonReader.nextDouble();
                } else if (i != 2) {
                    jsonReader.skipValue();
                } else {
                    f2 = (float) jsonReader.nextDouble();
                }
            }
            jsonReader.endObject();
            list.add(new Marker(str, f, f2));
        }
        jsonReader.endArray();
    }
}
