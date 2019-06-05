// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import java.util.List;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.ArrayList;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class FontCharacterParser
{
    static FontCharacter parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        final ArrayList<ShapeGroup> list = new ArrayList<ShapeGroup>();
        jsonReader.beginObject();
        String nextString2;
        String nextString = nextString2 = null;
        double nextDouble2;
        double nextDouble = nextDouble2 = 0.0;
        char char1 = '\0';
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n = 0;
            Label_0196: {
                if (hashCode != -1866931350) {
                    if (hashCode != 119) {
                        if (hashCode != 3173) {
                            if (hashCode != 3076010) {
                                if (hashCode != 3530753) {
                                    if (hashCode == 109780401) {
                                        if (nextName.equals("style")) {
                                            n = 3;
                                            break Label_0196;
                                        }
                                    }
                                }
                                else if (nextName.equals("size")) {
                                    n = 1;
                                    break Label_0196;
                                }
                            }
                            else if (nextName.equals("data")) {
                                n = 5;
                                break Label_0196;
                            }
                        }
                        else if (nextName.equals("ch")) {
                            n = 0;
                            break Label_0196;
                        }
                    }
                    else if (nextName.equals("w")) {
                        n = 2;
                        break Label_0196;
                    }
                }
                else if (nextName.equals("fFamily")) {
                    n = 4;
                    break Label_0196;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 5: {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        if ("shapes".equals(jsonReader.nextName())) {
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                list.add((ShapeGroup)ContentModelParser.parse(jsonReader, lottieComposition));
                            }
                            jsonReader.endArray();
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    continue;
                }
                case 4: {
                    nextString2 = jsonReader.nextString();
                    continue;
                }
                case 3: {
                    nextString = jsonReader.nextString();
                    continue;
                }
                case 2: {
                    nextDouble2 = jsonReader.nextDouble();
                    continue;
                }
                case 1: {
                    nextDouble = jsonReader.nextDouble();
                    continue;
                }
                case 0: {
                    char1 = jsonReader.nextString().charAt(0);
                    continue;
                }
            }
        }
        jsonReader.endObject();
        return new FontCharacter(list, char1, nextDouble, nextDouble2, nextString, nextString2);
    }
}
