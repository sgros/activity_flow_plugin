package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ChangeClipBounds extends Transition {
   private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
   private static final String PROPNAME_CLIP = "android:clipBounds:clip";
   private static final String[] sTransitionProperties = new String[]{"android:clipBounds:clip"};

   public ChangeClipBounds() {
   }

   public ChangeClipBounds(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      if (var2.getVisibility() != 8) {
         Rect var3 = ViewCompat.getClipBounds(var2);
         var1.values.put("android:clipBounds:clip", var3);
         if (var3 == null) {
            Rect var4 = new Rect(0, 0, var2.getWidth(), var2.getHeight());
            var1.values.put("android:clipBounds:bounds", var4);
         }

      }
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(@NonNull ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null && var3 != null && var2.values.containsKey("android:clipBounds:clip") && var3.values.containsKey("android:clipBounds:clip")) {
         Rect var4 = (Rect)var2.values.get("android:clipBounds:clip");
         Rect var5 = (Rect)var3.values.get("android:clipBounds:clip");
         boolean var6;
         if (var5 == null) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (var4 == null && var5 == null) {
            return null;
         } else {
            Rect var7;
            Rect var8;
            if (var4 == null) {
               var7 = (Rect)var2.values.get("android:clipBounds:bounds");
               var8 = var5;
            } else {
               var7 = var4;
               var8 = var5;
               if (var5 == null) {
                  var8 = (Rect)var3.values.get("android:clipBounds:bounds");
                  var7 = var4;
               }
            }

            if (var7.equals(var8)) {
               return null;
            } else {
               ViewCompat.setClipBounds(var3.view, var7);
               RectEvaluator var10 = new RectEvaluator(new Rect());
               ObjectAnimator var9 = ObjectAnimator.ofObject(var3.view, ViewUtils.CLIP_BOUNDS, var10, new Rect[]{var7, var8});
               if (var6) {
                  var9.addListener(new AnimatorListenerAdapter(var3.view) {
                     // $FF: synthetic field
                     final View val$endView;

                     {
                        this.val$endView = var2;
                     }

                     public void onAnimationEnd(Animator var1) {
                        ViewCompat.setClipBounds(this.val$endView, (Rect)null);
                     }
                  });
               }

               return var9;
            }
         }
      } else {
         return null;
      }
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }
}
