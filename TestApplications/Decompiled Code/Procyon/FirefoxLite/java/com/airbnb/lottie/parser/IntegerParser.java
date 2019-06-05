// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonReader;

public class IntegerParser implements ValueParser<Integer>
{
    public static final IntegerParser INSTANCE;
    
    static {
        INSTANCE = new IntegerParser();
    }
    
    private IntegerParser() {
    }
    
    @Override
    public Integer parse(final JsonReader jsonReader, final float n) throws IOException {
        return Math.round(JsonUtils.valueFromObject(jsonReader) * n);
    }
}
