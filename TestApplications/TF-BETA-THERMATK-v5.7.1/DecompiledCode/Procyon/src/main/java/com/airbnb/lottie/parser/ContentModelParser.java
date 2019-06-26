// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import java.io.IOException;
import com.airbnb.lottie.model.content.MergePaths;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

class ContentModelParser
{
    static ContentModel parse(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        jsonReader.beginObject();
        final int n = 2;
        int nextInt = 2;
        ContentModel contentModel;
        String nextString;
        while (true) {
            final boolean hasNext = jsonReader.hasNext();
            contentModel = null;
            if (!hasNext) {
                nextString = null;
                break;
            }
            final String nextName = jsonReader.nextName();
            final int hashCode = nextName.hashCode();
            int n2 = 0;
            Label_0088: {
                if (hashCode != 100) {
                    if (hashCode == 3717) {
                        if (nextName.equals("ty")) {
                            n2 = 0;
                            break Label_0088;
                        }
                    }
                }
                else if (nextName.equals("d")) {
                    n2 = 1;
                    break Label_0088;
                }
                n2 = -1;
            }
            if (n2 == 0) {
                nextString = jsonReader.nextString();
                break;
            }
            if (n2 != 1) {
                jsonReader.skipValue();
            }
            else {
                nextInt = jsonReader.nextInt();
            }
        }
        if (nextString == null) {
            return null;
        }
        int n3 = 0;
        Label_0473: {
            switch (nextString.hashCode()) {
                case 3710: {
                    if (nextString.equals("tr")) {
                        n3 = 5;
                        break Label_0473;
                    }
                    break;
                }
                case 3705: {
                    if (nextString.equals("tm")) {
                        n3 = 9;
                        break Label_0473;
                    }
                    break;
                }
                case 3681: {
                    if (nextString.equals("st")) {
                        n3 = 1;
                        break Label_0473;
                    }
                    break;
                }
                case 3679: {
                    if (nextString.equals("sr")) {
                        n3 = 10;
                        break Label_0473;
                    }
                    break;
                }
                case 3669: {
                    if (nextString.equals("sh")) {
                        n3 = 6;
                        break Label_0473;
                    }
                    break;
                }
                case 3646: {
                    if (nextString.equals("rp")) {
                        n3 = 12;
                        break Label_0473;
                    }
                    break;
                }
                case 3633: {
                    if (nextString.equals("rc")) {
                        n3 = 8;
                        break Label_0473;
                    }
                    break;
                }
                case 3488: {
                    if (nextString.equals("mm")) {
                        n3 = 11;
                        break Label_0473;
                    }
                    break;
                }
                case 3308: {
                    if (nextString.equals("gs")) {
                        n3 = n;
                        break Label_0473;
                    }
                    break;
                }
                case 3307: {
                    if (nextString.equals("gr")) {
                        n3 = 0;
                        break Label_0473;
                    }
                    break;
                }
                case 3295: {
                    if (nextString.equals("gf")) {
                        n3 = 4;
                        break Label_0473;
                    }
                    break;
                }
                case 3270: {
                    if (nextString.equals("fl")) {
                        n3 = 3;
                        break Label_0473;
                    }
                    break;
                }
                case 3239: {
                    if (nextString.equals("el")) {
                        n3 = 7;
                        break Label_0473;
                    }
                    break;
                }
            }
            n3 = -1;
        }
        ContentModel contentModel2 = null;
        switch (n3) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown shape type ");
                sb.append(nextString);
                Logger.warning(sb.toString());
                contentModel2 = contentModel;
                break;
            }
            case 12: {
                contentModel2 = RepeaterParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 11: {
                final MergePaths parse = MergePathsParser.parse(jsonReader);
                lottieComposition.addWarning("Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().");
                contentModel2 = parse;
                break;
            }
            case 10: {
                contentModel2 = PolystarShapeParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 9: {
                contentModel2 = ShapeTrimPathParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 8: {
                contentModel2 = RectangleShapeParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 7: {
                contentModel2 = CircleShapeParser.parse(jsonReader, lottieComposition, nextInt);
                break;
            }
            case 6: {
                contentModel2 = ShapePathParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 5: {
                contentModel2 = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 4: {
                contentModel2 = GradientFillParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 3: {
                contentModel2 = ShapeFillParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 2: {
                contentModel2 = GradientStrokeParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 1: {
                contentModel2 = ShapeStrokeParser.parse(jsonReader, lottieComposition);
                break;
            }
            case 0: {
                contentModel2 = ShapeGroupParser.parse(jsonReader, lottieComposition);
                break;
            }
        }
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        jsonReader.endObject();
        return contentModel2;
    }
}
