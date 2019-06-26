package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import java.io.IOException;

public class PathParser implements ValueParser {
   public static final PathParser INSTANCE = new PathParser();

   private PathParser() {
   }

   public PointF parse(JsonReader var1, float var2) throws IOException {
      return JsonUtils.jsonToPoint(var1, var2);
   }
}
