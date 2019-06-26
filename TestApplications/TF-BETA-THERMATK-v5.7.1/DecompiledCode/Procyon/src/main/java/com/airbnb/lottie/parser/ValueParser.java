// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonReader;

interface ValueParser<V>
{
    V parse(final JsonReader p0, final float p1) throws IOException;
}
