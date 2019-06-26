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
            int n = 0;
            Label_0210: {
                switch (nextName.hashCode()) {
                    case 109780401: {
                        if (nextName.equals("style")) {
                            n = 3;
                            break Label_0210;
                        }
                        break;
                    }
                    case 3530753: {
                        if (nextName.equals("size")) {
                            n = 1;
                            break Label_0210;
                        }
                        break;
                    }
                    case 3076010: {
                        if (nextName.equals("data")) {
                            n = 5;
                            break Label_0210;
                        }
                        break;
                    }
                    case 3173: {
                        if (nextName.equals("ch")) {
                            n = 0;
                            break Label_0210;
                        }
                        break;
                    }
                    case 119: {
                        if (nextName.equals("w")) {
                            n = 2;
                            break Label_0210;
                        }
                        break;
                    }
                    case -1866931350: {
                        if (nextName.equals("fFamily")) {
                            n = 4;
                            break Label_0210;
                        }
                        break;
                    }
                }
                n = -1;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    jsonReader.skipValue();
                                }
                                else {
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
                    else {
                        nextDouble2 = jsonReader.nextDouble();
                    }
                }
                else {
                    nextDouble = jsonReader.nextDouble();
                }
            }
            else {
                char1 = jsonReader.nextString().charAt(0);
            }
        }
        jsonReader.endObject();
        return new FontCharacter(list, char1, nextDouble, nextDouble2, nextString, nextString2);
    }
}
