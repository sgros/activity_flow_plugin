package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class ChangeBounds extends Transition {
   private static final Property BOTTOM_RIGHT_ONLY_PROPERTY = new Property(PointF.class, "bottomRight") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         ViewUtils.setLeftTopRightBottom(var1, var1.getLeft(), var1.getTop(), Math.round(var2.x), Math.round(var2.y));
      }
   };
   private static final Property BOTTOM_RIGHT_PROPERTY = new Property(PointF.class, "bottomRight") {
      public PointF get(ChangeBounds.ViewBounds var1) {
         return null;
      }

      public void set(ChangeBounds.ViewBounds var1, PointF var2) {
         var1.setBottomRight(var2);
      }
   };
   private static final Property DRAWABLE_ORIGIN_PROPERTY = new Property(PointF.class, "boundsOrigin") {
      private Rect mBounds = new Rect();

      public PointF get(Drawable var1) {
         var1.copyBounds(this.mBounds);
         return new PointF((float)this.mBounds.left, (float)this.mBounds.top);
      }

      public void set(Drawable var1, PointF var2) {
         var1.copyBounds(this.mBounds);
         this.mBounds.offsetTo(Math.round(var2.x), Math.round(var2.y));
         var1.setBounds(this.mBounds);
      }
   };
   private static final Property POSITION_PROPERTY = new Property(PointF.class, "position") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         int var3 = Math.round(var2.x);
         int var4 = Math.round(var2.y);
         ViewUtils.setLeftTopRightBottom(var1, var3, var4, var1.getWidth() + var3, var1.getHeight() + var4);
      }
   };
   private static final Property TOP_LEFT_ONLY_PROPERTY = new Property(PointF.class, "topLeft") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         ViewUtils.setLeftTopRightBottom(var1, Math.round(var2.x), Math.round(var2.y), var1.getRight(), var1.getBottom());
      }
   };
   private static final Property TOP_LEFT_PROPERTY = new Property(PointF.class, "topLeft") {
      public PointF get(ChangeBounds.ViewBounds var1) {
         return null;
      }

      public void set(ChangeBounds.ViewBounds var1, PointF var2) {
         var1.setTopLeft(var2);
      }
   };
   private static RectEvaluator sRectEvaluator = new RectEvaluator();
   private static final String[] sTransitionProperties = new String[]{"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
   private boolean mReparent = false;
   private boolean mResizeClip = false;
   private int[] mTempLocation = new int[2];

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      if (ViewCompat.isLaidOut(var2) || var2.getWidth() != 0 || var2.getHeight() != 0) {
         var1.values.put("android:changeBounds:bounds", new Rect(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom()));
         var1.values.put("android:changeBounds:parent", var1.view.getParent());
         if (this.mReparent) {
            var1.view.getLocationInWindow(this.mTempLocation);
            var1.values.put("android:changeBounds:windowX", this.mTempLocation[0]);
            var1.values.put("android:changeBounds:windowY", this.mTempLocation[1]);
         }

         if (this.mResizeClip) {
            var1.values.put("android:changeBounds:clip", ViewCompat.getClipBounds(var2));
         }
      }

   }

   private boolean parentMatches(View var1, View var2) {
      boolean var3 = this.mReparent;
      boolean var4 = true;
      boolean var5 = var4;
      if (var3) {
         TransitionValues var6 = this.getMatchedTransitionValues(var1, true);
         if (var6 == null) {
            if (var1 == var2) {
               var5 = var4;
               return var5;
            }
         } else if (var2 == var6.view) {
            var5 = var4;
            return var5;
         }

         var5 = false;
      }

      return var5;
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(final ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null && var3 != null) {
         Map var4 = var2.values;
         Map var5 = var3.values;
         ViewGroup var33 = (ViewGroup)var4.get("android:changeBounds:parent");
         ViewGroup var6 = (ViewGroup)var5.get("android:changeBounds:parent");
         if (var33 != null && var6 != null) {
            final View var36 = var3.view;
            int var15;
            int var17;
            int var19;
            int var20;
            ObjectAnimator var30;
            if (this.parentMatches(var33, var6)) {
               final Rect var35 = (Rect)var2.values.get("android:changeBounds:bounds");
               Rect var22 = (Rect)var3.values.get("android:changeBounds:bounds");
               int var7 = var35.left;
               final int var8 = var22.left;
               int var9 = var35.top;
               final int var10 = var22.top;
               int var11 = var35.right;
               final int var12 = var22.right;
               int var13 = var35.bottom;
               final int var14 = var22.bottom;
               var15 = var11 - var7;
               int var16 = var13 - var9;
               var17 = var12 - var8;
               int var18 = var14 - var10;
               Rect var27 = (Rect)var2.values.get("android:changeBounds:clip");
               var35 = (Rect)var3.values.get("android:changeBounds:clip");
               if (var15 != 0 && var16 != 0 || var17 != 0 && var18 != 0) {
                  label146: {
                     byte var40;
                     if (var7 == var8 && var9 == var10) {
                        var40 = 0;
                     } else {
                        var40 = 1;
                     }

                     if (var11 == var12) {
                        var20 = var40;
                        if (var13 == var14) {
                           break label146;
                        }
                     }

                     var20 = var40 + 1;
                  }
               } else {
                  var20 = 0;
               }

               label149: {
                  if (var27 == null || var27.equals(var35)) {
                     var19 = var20;
                     if (var27 != null) {
                        break label149;
                     }

                     var19 = var20;
                     if (var35 == null) {
                        break label149;
                     }
                  }

                  var19 = var20 + 1;
               }

               if (var19 > 0) {
                  Path var23;
                  Object var25;
                  if (!this.mResizeClip) {
                     ViewUtils.setLeftTopRightBottom(var36, var7, var9, var11, var13);
                     Path var31;
                     if (var19 == 2) {
                        if (var15 == var17 && var16 == var18) {
                           var31 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                           var25 = ObjectAnimatorUtils.ofPointF(var36, POSITION_PROPERTY, var31);
                        } else {
                           final ChangeBounds.ViewBounds var29 = new ChangeBounds.ViewBounds(var36);
                           var23 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                           var30 = ObjectAnimatorUtils.ofPointF(var29, TOP_LEFT_PROPERTY, var23);
                           var23 = this.getPathMotion().getPath((float)var11, (float)var13, (float)var12, (float)var14);
                           ObjectAnimator var37 = ObjectAnimatorUtils.ofPointF(var29, BOTTOM_RIGHT_PROPERTY, var23);
                           var25 = new AnimatorSet();
                           ((AnimatorSet)var25).playTogether(new Animator[]{var30, var37});
                           ((AnimatorSet)var25).addListener(new AnimatorListenerAdapter() {
                              private ChangeBounds.ViewBounds mViewBounds = var29;
                           });
                        }
                     } else if (var7 == var8 && var9 == var10) {
                        var31 = this.getPathMotion().getPath((float)var11, (float)var13, (float)var12, (float)var14);
                        var25 = ObjectAnimatorUtils.ofPointF(var36, BOTTOM_RIGHT_ONLY_PROPERTY, var31);
                     } else {
                        var31 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                        var25 = ObjectAnimatorUtils.ofPointF(var36, TOP_LEFT_ONLY_PROPERTY, var31);
                     }
                  } else {
                     ViewUtils.setLeftTopRightBottom(var36, var7, var9, Math.max(var15, var17) + var7, Math.max(var16, var18) + var9);
                     ObjectAnimator var32;
                     if (var7 == var8 && var9 == var10) {
                        var32 = null;
                     } else {
                        var23 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                        var32 = ObjectAnimatorUtils.ofPointF(var36, POSITION_PROPERTY, var23);
                     }

                     if (var27 == null) {
                        var27 = new Rect(0, 0, var15, var16);
                     }

                     Rect var34;
                     if (var35 == null) {
                        var34 = new Rect(0, 0, var17, var18);
                     } else {
                        var34 = var35;
                     }

                     ObjectAnimator var38;
                     if (!var27.equals(var34)) {
                        ViewCompat.setClipBounds(var36, var27);
                        var38 = ObjectAnimator.ofObject(var36, "clipBounds", sRectEvaluator, new Object[]{var27, var34});
                        var38.addListener(new AnimatorListenerAdapter() {
                           private boolean mIsCanceled;

                           public void onAnimationCancel(Animator var1) {
                              this.mIsCanceled = true;
                           }

                           public void onAnimationEnd(Animator var1) {
                              if (!this.mIsCanceled) {
                                 ViewCompat.setClipBounds(var36, var35);
                                 ViewUtils.setLeftTopRightBottom(var36, var8, var10, var12, var14);
                              }

                           }
                        });
                     } else {
                        var38 = null;
                     }

                     var25 = TransitionUtils.mergeAnimators(var32, var38);
                  }

                  if (var36.getParent() instanceof ViewGroup) {
                     final ViewGroup var39 = (ViewGroup)var36.getParent();
                     ViewGroupUtils.suppressLayout(var39, true);
                     this.addListener(new TransitionListenerAdapter() {
                        boolean mCanceled = false;

                        public void onTransitionEnd(Transition var1) {
                           if (!this.mCanceled) {
                              ViewGroupUtils.suppressLayout(var39, false);
                           }

                           var1.removeListener(this);
                        }

                        public void onTransitionPause(Transition var1) {
                           ViewGroupUtils.suppressLayout(var39, false);
                        }

                        public void onTransitionResume(Transition var1) {
                           ViewGroupUtils.suppressLayout(var39, true);
                        }
                     });
                  }

                  return (Animator)var25;
               }
            } else {
               var19 = (Integer)var2.values.get("android:changeBounds:windowX");
               var15 = (Integer)var2.values.get("android:changeBounds:windowY");
               var20 = (Integer)var3.values.get("android:changeBounds:windowX");
               var17 = (Integer)var3.values.get("android:changeBounds:windowY");
               if (var19 != var20 || var15 != var17) {
                  var1.getLocationInWindow(this.mTempLocation);
                  Bitmap var24 = Bitmap.createBitmap(var36.getWidth(), var36.getHeight(), Config.ARGB_8888);
                  var36.draw(new Canvas(var24));
                  final BitmapDrawable var26 = new BitmapDrawable(var24);
                  final float var21 = ViewUtils.getTransitionAlpha(var36);
                  ViewUtils.setTransitionAlpha(var36, 0.0F);
                  ViewUtils.getOverlay(var1).add(var26);
                  Path var28 = this.getPathMotion().getPath((float)(var19 - this.mTempLocation[0]), (float)(var15 - this.mTempLocation[1]), (float)(var20 - this.mTempLocation[0]), (float)(var17 - this.mTempLocation[1]));
                  var30 = ObjectAnimator.ofPropertyValuesHolder(var26, new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, var28)});
                  var30.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        ViewUtils.getOverlay(var1).remove(var26);
                        ViewUtils.setTransitionAlpha(var36, var21);
                     }
                  });
                  return var30;
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   private static class ViewBounds {
      private int mBottom;
      private int mBottomRightCalls;
      private int mLeft;
      private int mRight;
      private int mTop;
      private int mTopLeftCalls;
      private View mView;

      ViewBounds(View var1) {
         this.mView = var1;
      }

      private void setLeftTopRightBottom() {
         ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
         this.mTopLeftCalls = 0;
         this.mBottomRightCalls = 0;
      }

      void setBottomRight(PointF var1) {
         this.mRight = Math.round(var1.x);
         this.mBottom = Math.round(var1.y);
         ++this.mBottomRightCalls;
         if (this.mTopLeftCalls == this.mBottomRightCalls) {
            this.setLeftTopRightBottom();
         }

      }

      void setTopLeft(PointF var1) {
         this.mLeft = Math.round(var1.x);
         this.mTop = Math.round(var1.y);
         ++this.mTopLeftCalls;
         if (this.mTopLeftCalls == this.mBottomRightCalls) {
            this.setLeftTopRightBottom();
         }

      }
   }
}
