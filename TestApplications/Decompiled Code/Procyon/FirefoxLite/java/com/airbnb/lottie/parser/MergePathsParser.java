// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.content.MergePaths;
import android.util.JsonReader;

class MergePathsParser
{
    static MergePaths parse(final JsonReader jsonReader) throws IOException {
        String nextString = null;
        MergePaths.MergePathsMode forId = null;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            int n = -1;
            final int hashCode = nextName.hashCode();
            if (hashCode != 3488) {
                if (hashCode == 3519) {
                    if (nextName.equals("nm")) {
                        n = 0;
                    }
                }
            }
            else if (nextName.equals("mm")) {
                n = 1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 1: {
                    forId = MergePaths.MergePathsMode.forId(jsonReader.nextInt());
                    continue;
                }
                case 0: {
                    nextString = jsonReader.nextString();
                    continue;
                }
            }
        }
        return new MergePaths(nextString, forId);
    }
}
