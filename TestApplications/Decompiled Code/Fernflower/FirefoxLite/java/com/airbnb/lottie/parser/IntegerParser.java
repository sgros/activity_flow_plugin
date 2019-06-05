package com.airbnb.lottie.parser;

import android.util.JsonReader;
import java.io.IOException;

public class IntegerParser implements ValueParser {
   public static final IntegerParser INSTANCE = new IntegerParser();

   private IntegerParser() {
   }

   public Integer parse(JsonReader var1, float var2) throws IOException {
      return Math.round(JsonUtils.valueFromObject(var1) * var2);
   }
}
