package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.util.List;

public class AnimatableValueParser {
   private static List parse(JsonReader var0, float var1, LottieComposition var2, ValueParser var3) throws IOException {
      return KeyframesParser.parse(var0, var2, var1, var3);
   }

   private static List parse(JsonReader var0, LottieComposition var1, ValueParser var2) throws IOException {
      return KeyframesParser.parse(var0, var1, 1.0F, var2);
   }

   static AnimatableColorValue parseColor(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatableColorValue(parse(var0, var1, ColorParser.INSTANCE));
   }

   static AnimatableTextFrame parseDocumentData(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatableTextFrame(parse(var0, var1, DocumentDataParser.INSTANCE));
   }

   public static AnimatableFloatValue parseFloat(JsonReader var0, LottieComposition var1) throws IOException {
      return parseFloat(var0, var1, true);
   }

   public static AnimatableFloatValue parseFloat(JsonReader var0, LottieComposition var1, boolean var2) throws IOException {
      float var3;
      if (var2) {
         var3 = Utils.dpScale();
      } else {
         var3 = 1.0F;
      }

      return new AnimatableFloatValue(parse(var0, var3, var1, FloatParser.INSTANCE));
   }

   static AnimatableGradientColorValue parseGradientColor(JsonReader var0, LottieComposition var1, int var2) throws IOException {
      return new AnimatableGradientColorValue(parse(var0, var1, new GradientColorParser(var2)));
   }

   static AnimatableIntegerValue parseInteger(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatableIntegerValue(parse(var0, var1, IntegerParser.INSTANCE));
   }

   static AnimatablePointValue parsePoint(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatablePointValue(parse(var0, Utils.dpScale(), var1, PointFParser.INSTANCE));
   }

   static AnimatableScaleValue parseScale(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatableScaleValue(parse(var0, var1, ScaleXYParser.INSTANCE));
   }

   static AnimatableShapeValue parseShapeData(JsonReader var0, LottieComposition var1) throws IOException {
      return new AnimatableShapeValue(parse(var0, Utils.dpScale(), var1, ShapeDataParser.INSTANCE));
   }
}
