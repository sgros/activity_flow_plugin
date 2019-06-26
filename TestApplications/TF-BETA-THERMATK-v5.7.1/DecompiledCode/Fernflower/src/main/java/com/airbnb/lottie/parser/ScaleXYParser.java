package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.JsonToken;
import com.airbnb.lottie.value.ScaleXY;
import java.io.IOException;

public class ScaleXYParser implements ValueParser {
   public static final ScaleXYParser INSTANCE = new ScaleXYParser();

   private ScaleXYParser() {
   }

   public ScaleXY parse(JsonReader var1, float var2) throws IOException {
      boolean var3;
      if (var1.peek() == JsonToken.BEGIN_ARRAY) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var1.beginArray();
      }

      float var4 = (float)var1.nextDouble();
      float var5 = (float)var1.nextDouble();

      while(var1.hasNext()) {
         var1.skipValue();
      }

      if (var3) {
         var1.endArray();
      }

      return new ScaleXY(var4 / 100.0F * var2, var5 / 100.0F * var2);
   }
}
