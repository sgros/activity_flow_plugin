// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonReader;
import android.graphics.PointF;

public class PathParser implements ValueParser<PointF>
{
    public static final PathParser INSTANCE;
    
    static {
        INSTANCE = new PathParser();
    }
    
    private PathParser() {
    }
    
    @Override
    public PointF parse(final JsonReader jsonReader, final float n) throws IOException {
        return JsonUtils.jsonToPoint(jsonReader, n);
    }
}
