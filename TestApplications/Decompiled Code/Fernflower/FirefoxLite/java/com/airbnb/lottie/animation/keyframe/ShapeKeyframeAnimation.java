package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class ShapeKeyframeAnimation extends BaseKeyframeAnimation {
   private final Path tempPath = new Path();
   private final ShapeData tempShapeData = new ShapeData();

   public ShapeKeyframeAnimation(List var1) {
      super(var1);
   }

   public Path getValue(Keyframe var1, float var2) {
      ShapeData var3 = (ShapeData)var1.startValue;
      ShapeData var4 = (ShapeData)var1.endValue;
      this.tempShapeData.interpolateBetween(var3, var4, var2);
      MiscUtils.getPathFromData(this.tempShapeData, this.tempPath);
      return this.tempPath;
   }
}
