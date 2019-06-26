// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import java.util.List;
import com.airbnb.lottie.model.content.ContentModel;
import java.util.ArrayList;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ShapeGroupParser
{
    static ShapeGroup parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList<ContentModel> list = new ArrayList<ContentModel>();
        String nextString = null;
        boolean nextBoolean = false;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 3324) {
                if (hashCode != 3371) {
                    if (hashCode == 3519) {
                        if (nextName.equals("nm")) {
                            n = 0;
                        }
                    }
                }
                else if (nextName.equals("it")) {
                    n = 2;
                }
            }
            else if (nextName.equals("hd")) {
                n = 1;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        jsonReader.skipValue();
                    }
                    else {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            final ContentModel parse = ContentModelParser.parse(jsonReader, lottieComposition);
                            if (parse != null) {
                                list.add(parse);
                            }
                        }
                        jsonReader.endArray();
                    }
                }
                else {
                    nextBoolean = jsonReader.nextBoolean();
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        return new ShapeGroup(nextString, list, nextBoolean);
    }
}
