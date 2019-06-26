// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonToken;
import android.util.JsonReader;
import android.graphics.PointF;

public class PointFParser implements ValueParser<PointF>
{
    public static final PointFParser INSTANCE;
    
    static {
        INSTANCE = new PointFParser();
    }
    
    private PointFParser() {
    }
    
    @Override
    public PointF parse(final JsonReader jsonReader, final float n) throws IOException {
        final JsonToken peek = jsonReader.peek();
        if (peek == JsonToken.BEGIN_ARRAY) {
            return JsonUtils.jsonToPoint(jsonReader, n);
        }
        if (peek == JsonToken.BEGIN_OBJECT) {
            return JsonUtils.jsonToPoint(jsonReader, n);
        }
        if (peek == JsonToken.NUMBER) {
            final PointF pointF = new PointF((float)jsonReader.nextDouble() * n, (float)jsonReader.nextDouble() * n);
            while (jsonReader.hasNext()) {
                jsonReader.skipValue();
            }
            return pointF;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot convert json to point. Next token is ");
        sb.append(peek);
        throw new IllegalArgumentException(sb.toString());
    }
}
