// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonReader;

public class FloatParser implements ValueParser<Float>
{
    public static final FloatParser INSTANCE;
    
    static {
        INSTANCE = new FloatParser();
    }
    
    private FloatParser() {
    }
    
    @Override
    public Float parse(final JsonReader jsonReader, final float n) throws IOException {
        return JsonUtils.valueFromObject(jsonReader) * n;
    }
}
