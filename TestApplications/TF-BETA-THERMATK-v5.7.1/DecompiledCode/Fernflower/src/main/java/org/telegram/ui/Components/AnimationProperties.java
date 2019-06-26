package org.telegram.ui.Components;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Property;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Cells.DialogCell;

public class AnimationProperties {
   public static final Property CLIPPING_IMAGE_VIEW_PROGRESS = new AnimationProperties.FloatProperty("animationProgress") {
      public Float get(ClippingImageView var1) {
         return var1.getAnimationProgress();
      }

      public void setValue(ClippingImageView var1, float var2) {
         var1.setAnimationProgress(var2);
      }
   };
   public static final Property CLIP_DIALOG_CELL_PROGRESS = new AnimationProperties.FloatProperty("clipProgress") {
      public Float get(DialogCell var1) {
         return var1.getClipProgress();
      }

      public void setValue(DialogCell var1, float var2) {
         var1.setClipProgress(var2);
      }
   };
   public static final Property COLOR_DRAWABLE_ALPHA = new AnimationProperties.IntProperty("alpha") {
      public Integer get(ColorDrawable var1) {
         return var1.getAlpha();
      }

      public void setValue(ColorDrawable var1, int var2) {
         var1.setAlpha(var2);
      }
   };
   public static final Property PAINT_ALPHA = new AnimationProperties.IntProperty("alpha") {
      public Integer get(Paint var1) {
         return var1.getAlpha();
      }

      public void setValue(Paint var1, int var2) {
         var1.setAlpha(var2);
      }
   };
   public static final Property PHOTO_VIEWER_ANIMATION_VALUE = new AnimationProperties.FloatProperty("animationValue") {
      public Float get(PhotoViewer var1) {
         return var1.getAnimationValue();
      }

      public void setValue(PhotoViewer var1, float var2) {
         var1.setAnimationValue(var2);
      }
   };

   public abstract static class FloatProperty extends Property {
      public FloatProperty(String var1) {
         super(Float.class, var1);
      }

      public final void set(Object var1, Float var2) {
         this.setValue(var1, var2);
      }

      public abstract void setValue(Object var1, float var2);
   }

   public abstract static class IntProperty extends Property {
      public IntProperty(String var1) {
         super(Integer.class, var1);
      }

      public final void set(Object var1, Integer var2) {
         this.setValue(var1, var2);
      }

      public abstract void setValue(Object var1, int var2);
   }
}
