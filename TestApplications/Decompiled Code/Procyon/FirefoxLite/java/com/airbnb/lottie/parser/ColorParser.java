// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.graphics.Color;
import android.util.JsonToken;
import android.util.JsonReader;

public class ColorParser implements ValueParser<Integer>
{
    public static final ColorParser INSTANCE;
    
    static {
        INSTANCE = new ColorParser();
    }
    
    private ColorParser() {
    }
    
    @Override
    public Integer parse(final JsonReader jsonReader, final float n) throws IOException {
        final boolean b = jsonReader.peek() == JsonToken.BEGIN_ARRAY;
        if (b) {
            jsonReader.beginArray();
        }
        final double nextDouble = jsonReader.nextDouble();
        final double nextDouble2 = jsonReader.nextDouble();
        final double nextDouble3 = jsonReader.nextDouble();
        final double nextDouble4 = jsonReader.nextDouble();
        if (b) {
            jsonReader.endArray();
        }
        double n2 = nextDouble;
        double n3 = nextDouble2;
        double n4 = nextDouble3;
        double n5 = nextDouble4;
        if (nextDouble <= 1.0) {
            n2 = nextDouble;
            n3 = nextDouble2;
            n4 = nextDouble3;
            n5 = nextDouble4;
            if (nextDouble2 <= 1.0) {
                n2 = nextDouble;
                n3 = nextDouble2;
                n4 = nextDouble3;
                n5 = nextDouble4;
                if (nextDouble3 <= 1.0) {
                    n2 = nextDouble;
                    n3 = nextDouble2;
                    n4 = nextDouble3;
                    n5 = nextDouble4;
                    if (nextDouble4 <= 1.0) {
                        n2 = nextDouble * 255.0;
                        n3 = nextDouble2 * 255.0;
                        n4 = nextDouble3 * 255.0;
                        n5 = nextDouble4 * 255.0;
                    }
                }
            }
        }
        return Color.argb((int)n5, (int)n2, (int)n3, (int)n4);
    }
}
