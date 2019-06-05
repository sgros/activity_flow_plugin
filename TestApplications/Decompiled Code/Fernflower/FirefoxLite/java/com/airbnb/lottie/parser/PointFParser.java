package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;

public class PointFParser implements ValueParser {
   public static final PointFParser INSTANCE = new PointFParser();

   private PointFParser() {
   }

   public PointF parse(JsonReader var1, float var2) throws IOException {
      JsonToken var3 = var1.peek();
      if (var3 == JsonToken.BEGIN_ARRAY) {
         return JsonUtils.jsonToPoint(var1, var2);
      } else if (var3 == JsonToken.BEGIN_OBJECT) {
         return JsonUtils.jsonToPoint(var1, var2);
      } else if (var3 != JsonToken.NUMBER) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Cannot convert json to point. Next token is ");
         var4.append(var3);
         throw new IllegalArgumentException(var4.toString());
      } else {
         PointF var5 = new PointF((float)var1.nextDouble() * var2, (float)var1.nextDouble() * var2);

         while(var1.hasNext()) {
            var1.skipValue();
         }

         return var5;
      }
   }
}
