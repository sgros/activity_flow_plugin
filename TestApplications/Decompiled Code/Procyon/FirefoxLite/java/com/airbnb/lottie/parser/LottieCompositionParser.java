// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import com.airbnb.lottie.L;
import java.io.IOException;
import android.graphics.Rect;
import java.util.Map;
import com.airbnb.lottie.model.FontCharacter;
import android.support.v4.util.SparseArrayCompat;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.LottieImageAsset;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.airbnb.lottie.model.layer.Layer;
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class LottieCompositionParser
{
    public static LottieComposition parse(final JsonReader jsonReader) throws IOException {
        final float dpScale = Utils.dpScale();
        final LongSparseArray<Layer> longSparseArray = new LongSparseArray<Layer>();
        final ArrayList<Layer> list = new ArrayList<Layer>();
        final HashMap<String, List<Layer>> hashMap = new HashMap<String, List<Layer>>();
        final HashMap<String, LottieImageAsset> hashMap2 = new HashMap<String, LottieImageAsset>();
        final HashMap<String, Font> hashMap3 = new HashMap<String, Font>();
        final SparseArrayCompat<FontCharacter> sparseArrayCompat = new SparseArrayCompat<FontCharacter>();
        final LottieComposition lottieComposition = new LottieComposition();
        jsonReader.beginObject();
        int nextInt = 0;
        int nextInt2 = 0;
        float n = 0.0f;
        float n2 = 0.0f;
        float n3 = 0.0f;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n4 = 0;
            Label_0362: {
                switch (nextName.hashCode()) {
                    case 97615364: {
                        if (nextName.equals("fonts")) {
                            n4 = 8;
                            break Label_0362;
                        }
                        break;
                    }
                    case 94623709: {
                        if (nextName.equals("chars")) {
                            n4 = 9;
                            break Label_0362;
                        }
                        break;
                    }
                    case 3553: {
                        if (nextName.equals("op")) {
                            n4 = 3;
                            break Label_0362;
                        }
                        break;
                    }
                    case 3367: {
                        if (nextName.equals("ip")) {
                            n4 = 2;
                            break Label_0362;
                        }
                        break;
                    }
                    case 3276: {
                        if (nextName.equals("fr")) {
                            n4 = 4;
                            break Label_0362;
                        }
                        break;
                    }
                    case 119: {
                        if (nextName.equals("w")) {
                            n4 = 0;
                            break Label_0362;
                        }
                        break;
                    }
                    case 118: {
                        if (nextName.equals("v")) {
                            n4 = 5;
                            break Label_0362;
                        }
                        break;
                    }
                    case 104: {
                        if (nextName.equals("h")) {
                            n4 = 1;
                            break Label_0362;
                        }
                        break;
                    }
                    case -1109732030: {
                        if (nextName.equals("layers")) {
                            n4 = 6;
                            break Label_0362;
                        }
                        break;
                    }
                    case -1408207997: {
                        if (nextName.equals("assets")) {
                            n4 = 7;
                            break Label_0362;
                        }
                        break;
                    }
                }
                n4 = -1;
            }
            float n5 = 0.0f;
            switch (n4) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 9: {
                    parseChars(jsonReader, lottieComposition, sparseArrayCompat);
                    n5 = n3;
                    break;
                }
                case 8: {
                    parseFonts(jsonReader, hashMap3);
                    n5 = n3;
                    break;
                }
                case 7: {
                    parseAssets(jsonReader, lottieComposition, hashMap, hashMap2);
                    n5 = n3;
                    break;
                }
                case 6: {
                    parseLayers(jsonReader, lottieComposition, list, longSparseArray);
                    n5 = n3;
                    break;
                }
                case 5: {
                    final String[] split = jsonReader.nextString().split("\\.");
                    n5 = n3;
                    if (!Utils.isAtLeastVersion(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 4, 4, 0)) {
                        lottieComposition.addWarning("Lottie only supports bodymovin >= 4.4.0");
                        n5 = n3;
                        break;
                    }
                    break;
                }
                case 4: {
                    n5 = (float)jsonReader.nextDouble();
                    break;
                }
                case 3: {
                    n2 = (float)jsonReader.nextDouble() - 0.01f;
                    continue;
                }
                case 2: {
                    n = (float)jsonReader.nextDouble();
                    continue;
                }
                case 1: {
                    nextInt2 = jsonReader.nextInt();
                    continue;
                }
                case 0: {
                    nextInt = jsonReader.nextInt();
                    continue;
                }
            }
            n3 = n5;
        }
        jsonReader.endObject();
        lottieComposition.init(new Rect(0, 0, (int)(nextInt * dpScale), (int)(nextInt2 * dpScale)), n, n2, n3, list, longSparseArray, hashMap, hashMap2, sparseArrayCompat, hashMap3);
        return lottieComposition;
    }
    
    private static void parseAssets(final JsonReader jsonReader, final LottieComposition lottieComposition, final Map<String, List<Layer>> map, final Map<String, LottieImageAsset> map2) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            final ArrayList<Layer> list = new ArrayList<Layer>();
            final LongSparseArray<Layer> longSparseArray = new LongSparseArray<Layer>();
            jsonReader.beginObject();
            String nextString = null;
            String nextString3;
            String nextString2 = nextString3 = nextString;
            int nextInt = 0;
            int nextInt2 = 0;
            while (jsonReader.hasNext()) {
                final String nextName = jsonReader.nextName();
                final int hashCode = nextName.hashCode();
                int n = 0;
                Label_0215: {
                    if (hashCode != -1109732030) {
                        if (hashCode != 104) {
                            if (hashCode != 112) {
                                if (hashCode != 117) {
                                    if (hashCode != 119) {
                                        if (hashCode == 3355) {
                                            if (nextName.equals("id")) {
                                                n = 0;
                                                break Label_0215;
                                            }
                                        }
                                    }
                                    else if (nextName.equals("w")) {
                                        n = 2;
                                        break Label_0215;
                                    }
                                }
                                else if (nextName.equals("u")) {
                                    n = 5;
                                    break Label_0215;
                                }
                            }
                            else if (nextName.equals("p")) {
                                n = 4;
                                break Label_0215;
                            }
                        }
                        else if (nextName.equals("h")) {
                            n = 3;
                            break Label_0215;
                        }
                    }
                    else if (nextName.equals("layers")) {
                        n = 1;
                        break Label_0215;
                    }
                    n = -1;
                }
                switch (n) {
                    default: {
                        jsonReader.skipValue();
                        continue;
                    }
                    case 5: {
                        nextString3 = jsonReader.nextString();
                        continue;
                    }
                    case 4: {
                        nextString2 = jsonReader.nextString();
                        continue;
                    }
                    case 3: {
                        nextInt2 = jsonReader.nextInt();
                        continue;
                    }
                    case 2: {
                        nextInt = jsonReader.nextInt();
                        continue;
                    }
                    case 1: {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            final Layer parse = LayerParser.parse(jsonReader, lottieComposition);
                            longSparseArray.put(parse.getId(), parse);
                            list.add(parse);
                        }
                        jsonReader.endArray();
                        continue;
                    }
                    case 0: {
                        nextString = jsonReader.nextString();
                        continue;
                    }
                }
            }
            jsonReader.endObject();
            if (nextString2 != null) {
                final LottieImageAsset lottieImageAsset = new LottieImageAsset(nextInt, nextInt2, nextString, nextString2, nextString3);
                map2.put(lottieImageAsset.getId(), lottieImageAsset);
            }
            else {
                map.put(nextString, list);
            }
        }
        jsonReader.endArray();
    }
    
    private static void parseChars(final JsonReader jsonReader, final LottieComposition lottieComposition, final SparseArrayCompat<FontCharacter> sparseArrayCompat) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            final FontCharacter parse = FontCharacterParser.parse(jsonReader, lottieComposition);
            sparseArrayCompat.put(parse.hashCode(), parse);
        }
        jsonReader.endArray();
    }
    
    private static void parseFonts(final JsonReader jsonReader, final Map<String, Font> map) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            if (nextName.hashCode() == 3322014) {
                if (nextName.equals("list")) {
                    n = 0;
                }
            }
            if (n != 0) {
                jsonReader.skipValue();
            }
            else {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    final Font parse = FontParser.parse(jsonReader);
                    map.put(parse.getName(), parse);
                }
                jsonReader.endArray();
            }
        }
        jsonReader.endObject();
    }
    
    private static void parseLayers(final JsonReader jsonReader, final LottieComposition lottieComposition, final List<Layer> list, final LongSparseArray<Layer> longSparseArray) throws IOException {
        jsonReader.beginArray();
        int n = 0;
        while (jsonReader.hasNext()) {
            final Layer parse = LayerParser.parse(jsonReader, lottieComposition);
            int i = n;
            if (parse.getLayerType() == Layer.LayerType.Image) {
                i = n + 1;
            }
            list.add(parse);
            longSparseArray.put(parse.getId(), parse);
            if ((n = i) > 4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("You have ");
                sb.append(i);
                sb.append(" images. Lottie should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
                L.warn(sb.toString());
                n = i;
            }
        }
        jsonReader.endArray();
    }
}
