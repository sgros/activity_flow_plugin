// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import java.io.IOException;
import android.util.JsonToken;
import android.graphics.PointF;
import android.util.JsonReader;

class JsonUtils
{
    private static PointF jsonArrayToPoint(final JsonReader jsonReader, final float n) throws IOException {
        jsonReader.beginArray();
        final float n2 = (float)jsonReader.nextDouble();
        final float n3 = (float)jsonReader.nextDouble();
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            jsonReader.skipValue();
        }
        jsonReader.endArray();
        return new PointF(n2 * n, n3 * n);
    }
    
    private static PointF jsonNumbersToPoint(final JsonReader jsonReader, final float n) throws IOException {
        final float n2 = (float)jsonReader.nextDouble();
        final float n3 = (float)jsonReader.nextDouble();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        return new PointF(n2 * n, n3 * n);
    }
    
    private static PointF jsonObjectToPoint(final JsonReader jsonReader, final float n) throws IOException {
        jsonReader.beginObject();
        float valueFromObject = 0.0f;
        float valueFromObject2 = 0.0f;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            switch (nextName) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case "y": {
                    valueFromObject2 = valueFromObject(jsonReader);
                    continue;
                }
                case "x": {
                    valueFromObject = valueFromObject(jsonReader);
                    continue;
                }
            }
        }
        jsonReader.endObject();
        return new PointF(valueFromObject * n, valueFromObject2 * n);
    }
    
    static int jsonToColor(final JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        final int n = (int)(jsonReader.nextDouble() * 255.0);
        final int n2 = (int)(jsonReader.nextDouble() * 255.0);
        final int n3 = (int)(jsonReader.nextDouble() * 255.0);
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        jsonReader.endArray();
        return Color.argb(255, n, n2, n3);
    }
    
    static PointF jsonToPoint(final JsonReader jsonReader, final float n) throws IOException {
        switch (JsonUtils$1.$SwitchMap$android$util$JsonToken[jsonReader.peek().ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown point starts with ");
                sb.append(jsonReader.peek());
                throw new IllegalArgumentException(sb.toString());
            }
            case 3: {
                return jsonObjectToPoint(jsonReader, n);
            }
            case 2: {
                return jsonArrayToPoint(jsonReader, n);
            }
            case 1: {
                return jsonNumbersToPoint(jsonReader, n);
            }
        }
    }
    
    static List<PointF> jsonToPoints(final JsonReader jsonReader, final float n) throws IOException {
        final ArrayList<PointF> list = new ArrayList<PointF>();
        jsonReader.beginArray();
        while (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
            list.add(jsonToPoint(jsonReader, n));
            jsonReader.endArray();
        }
        jsonReader.endArray();
        return list;
    }
    
    static float valueFromObject(final JsonReader jsonReader) throws IOException {
        final JsonToken peek = jsonReader.peek();
        switch (JsonUtils$1.$SwitchMap$android$util$JsonToken[peek.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown value for token of type ");
                sb.append(peek);
                throw new IllegalArgumentException(sb.toString());
            }
            case 2: {
                jsonReader.beginArray();
                final float n = (float)jsonReader.nextDouble();
                while (jsonReader.hasNext()) {
                    jsonReader.skipValue();
                }
                jsonReader.endArray();
                return n;
            }
            case 1: {
                return (float)jsonReader.nextDouble();
            }
        }
    }
}
