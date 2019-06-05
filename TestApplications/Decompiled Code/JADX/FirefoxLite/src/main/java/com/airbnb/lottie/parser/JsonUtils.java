package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class JsonUtils {

    /* renamed from: com.airbnb.lottie.parser.JsonUtils$1 */
    static /* synthetic */ class C03831 {
        static final /* synthetic */ int[] $SwitchMap$android$util$JsonToken = new int[JsonToken.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        static {
            /*
            r0 = android.util.JsonToken.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$android$util$JsonToken = r0;
            r0 = $SwitchMap$android$util$JsonToken;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = android.util.JsonToken.NUMBER;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = $SwitchMap$android$util$JsonToken;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = android.util.JsonToken.BEGIN_ARRAY;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = $SwitchMap$android$util$JsonToken;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = android.util.JsonToken.BEGIN_OBJECT;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.JsonUtils$C03831.<clinit>():void");
        }
    }

    static int jsonToColor(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        int nextDouble = (int) (jsonReader.nextDouble() * 255.0d);
        int nextDouble2 = (int) (jsonReader.nextDouble() * 255.0d);
        int nextDouble3 = (int) (jsonReader.nextDouble() * 255.0d);
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        jsonReader.endArray();
        return Color.argb(255, nextDouble, nextDouble2, nextDouble3);
    }

    static List<PointF> jsonToPoints(JsonReader jsonReader, float f) throws IOException {
        ArrayList arrayList = new ArrayList();
        jsonReader.beginArray();
        while (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
            arrayList.add(jsonToPoint(jsonReader, f));
            jsonReader.endArray();
        }
        jsonReader.endArray();
        return arrayList;
    }

    static PointF jsonToPoint(JsonReader jsonReader, float f) throws IOException {
        switch (C03831.$SwitchMap$android$util$JsonToken[jsonReader.peek().ordinal()]) {
            case 1:
                return jsonNumbersToPoint(jsonReader, f);
            case 2:
                return jsonArrayToPoint(jsonReader, f);
            case 3:
                return jsonObjectToPoint(jsonReader, f);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown point starts with ");
                stringBuilder.append(jsonReader.peek());
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private static PointF jsonNumbersToPoint(JsonReader jsonReader, float f) throws IOException {
        float nextDouble = (float) jsonReader.nextDouble();
        float nextDouble2 = (float) jsonReader.nextDouble();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        return new PointF(nextDouble * f, nextDouble2 * f);
    }

    private static PointF jsonArrayToPoint(JsonReader jsonReader, float f) throws IOException {
        jsonReader.beginArray();
        float nextDouble = (float) jsonReader.nextDouble();
        float nextDouble2 = (float) jsonReader.nextDouble();
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            jsonReader.skipValue();
        }
        jsonReader.endArray();
        return new PointF(nextDouble * f, nextDouble2 * f);
    }

    private static PointF jsonObjectToPoint(JsonReader jsonReader, float f) throws IOException {
        jsonReader.beginObject();
        float f2 = 0.0f;
        float f3 = 0.0f;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            Object obj = -1;
            switch (nextName.hashCode()) {
                case 120:
                    if (nextName.equals("x")) {
                        obj = null;
                        break;
                    }
                    break;
                case 121:
                    if (nextName.equals("y")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    f2 = valueFromObject(jsonReader);
                    break;
                case 1:
                    f3 = valueFromObject(jsonReader);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return new PointF(f2 * f, f3 * f);
    }

    static float valueFromObject(JsonReader jsonReader) throws IOException {
        JsonToken peek = jsonReader.peek();
        switch (C03831.$SwitchMap$android$util$JsonToken[peek.ordinal()]) {
            case 1:
                return (float) jsonReader.nextDouble();
            case 2:
                jsonReader.beginArray();
                float nextDouble = (float) jsonReader.nextDouble();
                while (jsonReader.hasNext()) {
                    jsonReader.skipValue();
                }
                jsonReader.endArray();
                return nextDouble;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown value for token of type ");
                stringBuilder.append(peek);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
