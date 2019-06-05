// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import android.util.JsonReader;
import com.airbnb.lottie.model.DocumentData;

public class DocumentDataParser implements ValueParser<DocumentData>
{
    public static final DocumentDataParser INSTANCE;
    
    static {
        INSTANCE = new DocumentDataParser();
    }
    
    private DocumentDataParser() {
    }
    
    @Override
    public DocumentData parse(final JsonReader jsonReader, final float n) throws IOException {
        jsonReader.beginObject();
        String nextString2;
        String nextString = nextString2 = null;
        double nextDouble2;
        double nextDouble = nextDouble2 = 0.0;
        double nextDouble4;
        double nextDouble3 = nextDouble4 = nextDouble2;
        int nextInt = 0;
        int nextInt2 = 0;
        int jsonToColor = 0;
        int jsonToColor2 = 0;
        boolean nextBoolean = true;
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n2 = 0;
            Label_0339: {
                if (hashCode != 102) {
                    if (hashCode != 106) {
                        if (hashCode != 3261) {
                            if (hashCode != 3452) {
                                if (hashCode != 3463) {
                                    if (hashCode != 3543) {
                                        if (hashCode != 3664) {
                                            if (hashCode != 3684) {
                                                if (hashCode != 3710) {
                                                    switch (hashCode) {
                                                        case 116: {
                                                            if (nextName.equals("t")) {
                                                                n2 = 0;
                                                                break Label_0339;
                                                            }
                                                            break;
                                                        }
                                                        case 115: {
                                                            if (nextName.equals("s")) {
                                                                n2 = 2;
                                                                break Label_0339;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                                else if (nextName.equals("tr")) {
                                                    n2 = 4;
                                                    break Label_0339;
                                                }
                                            }
                                            else if (nextName.equals("sw")) {
                                                n2 = 9;
                                                break Label_0339;
                                            }
                                        }
                                        else if (nextName.equals("sc")) {
                                            n2 = 8;
                                            break Label_0339;
                                        }
                                    }
                                    else if (nextName.equals("of")) {
                                        n2 = 10;
                                        break Label_0339;
                                    }
                                }
                                else if (nextName.equals("ls")) {
                                    n2 = 6;
                                    break Label_0339;
                                }
                            }
                            else if (nextName.equals("lh")) {
                                n2 = 5;
                                break Label_0339;
                            }
                        }
                        else if (nextName.equals("fc")) {
                            n2 = 7;
                            break Label_0339;
                        }
                    }
                    else if (nextName.equals("j")) {
                        n2 = 3;
                        break Label_0339;
                    }
                }
                else if (nextName.equals("f")) {
                    n2 = 1;
                    break Label_0339;
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    jsonReader.skipValue();
                    continue;
                }
                case 10: {
                    nextBoolean = jsonReader.nextBoolean();
                    continue;
                }
                case 9: {
                    nextDouble4 = jsonReader.nextDouble();
                    continue;
                }
                case 8: {
                    jsonToColor2 = JsonUtils.jsonToColor(jsonReader);
                    continue;
                }
                case 7: {
                    jsonToColor = JsonUtils.jsonToColor(jsonReader);
                    continue;
                }
                case 6: {
                    nextDouble3 = jsonReader.nextDouble();
                    continue;
                }
                case 5: {
                    nextDouble2 = jsonReader.nextDouble();
                    continue;
                }
                case 4: {
                    nextInt2 = jsonReader.nextInt();
                    continue;
                }
                case 3: {
                    nextInt = jsonReader.nextInt();
                    continue;
                }
                case 2: {
                    nextDouble = jsonReader.nextDouble();
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
        return new DocumentData(nextString, nextString2, nextDouble, nextInt, nextInt2, nextDouble2, nextDouble3, jsonToColor, jsonToColor2, nextDouble4, nextBoolean);
    }
}
