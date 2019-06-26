// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import java.util.List;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.model.CubicCurveData;
import java.util.ArrayList;
import java.util.Collections;
import android.graphics.PointF;
import android.util.JsonToken;
import android.util.JsonReader;
import com.airbnb.lottie.model.content.ShapeData;

public class ShapeDataParser implements ValueParser<ShapeData>
{
    public static final ShapeDataParser INSTANCE;
    
    static {
        INSTANCE = new ShapeDataParser();
    }
    
    private ShapeDataParser() {
    }
    
    @Override
    public ShapeData parse(final JsonReader jsonReader, final float n) throws IOException {
        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
        }
        jsonReader.beginObject();
        List<PointF> jsonToPoints = null;
        List<PointF> jsonToPoints3;
        List<PointF> jsonToPoints2 = jsonToPoints3 = null;
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n2 = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 99) {
                if (hashCode != 105) {
                    if (hashCode != 111) {
                        if (hashCode == 118) {
                            if (nextName.equals("v")) {
                                n2 = 1;
                            }
                        }
                    }
                    else if (nextName.equals("o")) {
                        n2 = 3;
                    }
                }
                else if (nextName.equals("i")) {
                    n2 = 2;
                }
            }
            else if (nextName.equals("c")) {
                n2 = 0;
            }
            if (n2 != 0) {
                if (n2 != 1) {
                    if (n2 != 2) {
                        if (n2 != 3) {
                            continue;
                        }
                        jsonToPoints3 = JsonUtils.jsonToPoints(jsonReader, n);
                    }
                    else {
                        jsonToPoints2 = JsonUtils.jsonToPoints(jsonReader, n);
                    }
                }
                else {
                    jsonToPoints = JsonUtils.jsonToPoints(jsonReader, n);
                }
            }
            else {
                nextBoolean = jsonReader.nextBoolean();
            }
        }
        jsonReader.endObject();
        if (jsonReader.peek() == JsonToken.END_ARRAY) {
            jsonReader.endArray();
        }
        if (jsonToPoints == null || jsonToPoints2 == null || jsonToPoints3 == null) {
            throw new IllegalArgumentException("Shape data was missing information.");
        }
        if (jsonToPoints.isEmpty()) {
            return new ShapeData(new PointF(), false, Collections.emptyList());
        }
        final int size = jsonToPoints.size();
        final PointF pointF = jsonToPoints.get(0);
        final ArrayList list = new ArrayList<CubicCurveData>(size);
        for (int i = 1; i < size; ++i) {
            final PointF pointF2 = jsonToPoints.get(i);
            final int n3 = i - 1;
            list.add(new CubicCurveData(MiscUtils.addPoints(jsonToPoints.get(n3), jsonToPoints3.get(n3)), MiscUtils.addPoints(pointF2, jsonToPoints2.get(i)), pointF2));
        }
        if (nextBoolean) {
            final PointF pointF3 = jsonToPoints.get(0);
            final int n4 = size - 1;
            list.add(new CubicCurveData(MiscUtils.addPoints(jsonToPoints.get(n4), jsonToPoints3.get(n4)), MiscUtils.addPoints(pointF3, jsonToPoints2.get(0)), pointF3));
        }
        return new ShapeData(pointF, nextBoolean, (List<CubicCurveData>)list);
    }
}
