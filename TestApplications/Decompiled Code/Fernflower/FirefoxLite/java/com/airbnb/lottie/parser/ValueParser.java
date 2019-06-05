package com.airbnb.lottie.parser;

import android.util.JsonReader;
import java.io.IOException;

interface ValueParser {
   Object parse(JsonReader var1, float var2) throws IOException;
}
