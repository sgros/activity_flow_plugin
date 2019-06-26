// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.Font;
import android.util.JsonReader;

class FontParser
{
    static Font parse(final JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String nextString = null;
        String nextString3;
        String nextString2 = nextString3 = null;
        float n = 0.0f;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n2 = -1;
            switch (nextName.hashCode()) {
                case 96619537: {
                    if (nextName.equals("fName")) {
                        n2 = 1;
                        break;
                    }
                    break;
                }
                case -1294566165: {
                    if (nextName.equals("fStyle")) {
                        n2 = 2;
                        break;
                    }
                    break;
                }
                case -1408684838: {
                    if (nextName.equals("ascent")) {
                        n2 = 3;
                        break;
                    }
                    break;
                }
                case -1866931350: {
                    if (nextName.equals("fFamily")) {
                        n2 = 0;
                        break;
                    }
                    break;
                }
            }
            if (n2 != 0) {
                if (n2 != 1) {
                    if (n2 != 2) {
                        if (n2 != 3) {
                            jsonReader.skipValue();
                        }
                        else {
                            n = (float)jsonReader.nextDouble();
                        }
                    }
                    else {
                        nextString3 = jsonReader.nextString();
                    }
                }
                else {
                    nextString2 = jsonReader.nextString();
                }
            }
            else {
                nextString = jsonReader.nextString();
            }
        }
        jsonReader.endObject();
        return new Font(nextString, nextString2, nextString3, n);
    }
}
