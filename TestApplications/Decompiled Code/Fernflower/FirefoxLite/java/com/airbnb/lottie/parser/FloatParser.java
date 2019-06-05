package com.airbnb.lottie.parser;

import android.util.JsonReader;
import java.io.IOException;

public class FloatParser implements ValueParser {
   public static final FloatParser INSTANCE = new FloatParser();

   private FloatParser() {
   }

   public Float parse(JsonReader var1, float var2) throws IOException {
      return JsonUtils.valueFromObject(var1) * var2;
   }
}
