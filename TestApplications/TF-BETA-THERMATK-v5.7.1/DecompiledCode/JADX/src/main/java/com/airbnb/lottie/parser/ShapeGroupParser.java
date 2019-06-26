package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.io.IOException;
import java.util.ArrayList;

class ShapeGroupParser {
    static ShapeGroup parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        ArrayList arrayList = new ArrayList();
        String str = null;
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            int i = -1;
            int hashCode = nextName.hashCode();
            if (hashCode != 3324) {
                if (hashCode != 3371) {
                    if (hashCode == 3519 && nextName.equals("nm")) {
                        i = 0;
                    }
                } else if (nextName.equals("it")) {
                    i = 2;
                }
            } else if (nextName.equals("hd")) {
                i = 1;
            }
            if (i == 0) {
                str = jsonReader.nextString();
            } else if (i == 1) {
                z = jsonReader.nextBoolean();
            } else if (i != 2) {
                jsonReader.skipValue();
            } else {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    ContentModel parse = ContentModelParser.parse(jsonReader, lottieComposition);
                    if (parse != null) {
                        arrayList.add(parse);
                    }
                }
                jsonReader.endArray();
            }
        }
        return new ShapeGroup(str, arrayList, z);
    }
}
