// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.parser;

import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.value.ScaleXY;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import android.graphics.PointF;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import java.io.IOException;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.LottieComposition;
import android.util.JsonReader;

public class AnimatableValueParser
{
    private static <T> List<Keyframe<T>> parse(final JsonReader jsonReader, final float n, final LottieComposition lottieComposition, final ValueParser<T> valueParser) throws IOException {
        return KeyframesParser.parse(jsonReader, lottieComposition, n, valueParser);
    }
    
    private static <T> List<Keyframe<T>> parse(final JsonReader jsonReader, final LottieComposition lottieComposition, final ValueParser<T> valueParser) throws IOException {
        return KeyframesParser.parse(jsonReader, lottieComposition, 1.0f, valueParser);
    }
    
    static AnimatableColorValue parseColor(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatableColorValue(parse(jsonReader, lottieComposition, (ValueParser<Integer>)ColorParser.INSTANCE));
    }
    
    static AnimatableTextFrame parseDocumentData(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatableTextFrame(parse(jsonReader, lottieComposition, (ValueParser<DocumentData>)DocumentDataParser.INSTANCE));
    }
    
    public static AnimatableFloatValue parseFloat(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return parseFloat(jsonReader, lottieComposition, true);
    }
    
    public static AnimatableFloatValue parseFloat(final JsonReader jsonReader, final LottieComposition lottieComposition, final boolean b) throws IOException {
        float dpScale;
        if (b) {
            dpScale = Utils.dpScale();
        }
        else {
            dpScale = 1.0f;
        }
        return new AnimatableFloatValue(parse(jsonReader, dpScale, lottieComposition, (ValueParser<Float>)FloatParser.INSTANCE));
    }
    
    static AnimatableGradientColorValue parseGradientColor(final JsonReader jsonReader, final LottieComposition lottieComposition, final int n) throws IOException {
        return new AnimatableGradientColorValue(parse(jsonReader, lottieComposition, (ValueParser<GradientColor>)new GradientColorParser(n)));
    }
    
    static AnimatableIntegerValue parseInteger(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatableIntegerValue(parse(jsonReader, lottieComposition, (ValueParser<Integer>)IntegerParser.INSTANCE));
    }
    
    static AnimatablePointValue parsePoint(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatablePointValue(parse(jsonReader, Utils.dpScale(), lottieComposition, (ValueParser<PointF>)PointFParser.INSTANCE));
    }
    
    static AnimatableScaleValue parseScale(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatableScaleValue(parse(jsonReader, lottieComposition, (ValueParser<ScaleXY>)ScaleXYParser.INSTANCE));
    }
    
    static AnimatableShapeValue parseShapeData(final JsonReader jsonReader, final LottieComposition lottieComposition) throws IOException {
        return new AnimatableShapeValue(parse(jsonReader, Utils.dpScale(), lottieComposition, (ValueParser<ShapeData>)ShapeDataParser.INSTANCE));
    }
}
