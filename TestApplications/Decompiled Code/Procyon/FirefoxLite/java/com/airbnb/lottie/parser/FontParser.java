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
            final int hashCode = nextName.hashCode();
            if (hashCode != -1866931350) {
                if (hashCode != -1408684838) {
                    if (hashCode != -1294566165) {
                        if (hashCode == 96619537) {
                            if (nextName.equals("fName")) {
                                n2 = 1;
                            }
                        }
                    }
                    else if (nextName.equals("fStyle")) {
                        n2 = 2;
                    }
                }
                else if (nextName.equals("ascent")) {
                    n2 = 3;
                }
            }
            else if (nextName.equals("fFamily")) {
                n2 = 0;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 3: {
                    n = (float)jsonReader.nextDouble();
                    continue;
                }
                case 2: {
                    nextString3 = jsonReader.nextString();
                    continue;
                }
                case 1: {
                    nextString2 = jsonReader.nextString();
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        jsonReader.endObject();
        return new Font(nextString, nextString2, nextString3, n);
    }
}
