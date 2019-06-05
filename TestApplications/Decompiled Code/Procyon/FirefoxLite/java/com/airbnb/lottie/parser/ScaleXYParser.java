// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonToken;
import android.util.JsonReader;
import com.airbnb.lottie.value.ScaleXY;

public class ScaleXYParser implements ValueParser<ScaleXY>
{
    public static final ScaleXYParser INSTANCE;
    
    static {
        INSTANCE = new ScaleXYParser();
    }
    
    private ScaleXYParser() {
    }
    
    @Override
    public ScaleXY parse(final JsonReader jsonReader, final float n) throws IOException {
        final boolean b = jsonReader.peek() == JsonToken.BEGIN_ARRAY;
        if (b) {
            jsonReader.beginArray();
        }
        final float n2 = (float)jsonReader.nextDouble();
        final float n3 = (float)jsonReader.nextDouble();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        if (b) {
            jsonReader.endArray();
        }
        return new ScaleXY(n2 / 100.0f * n, n3 / 100.0f * n);
    }
}
