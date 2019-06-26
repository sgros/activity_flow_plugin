package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflaterCompat {
   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, float var4) throws XmlPullParserException, IOException {
      return createAnimatorFromXml(var0, var1, var2, var3, Xml.asAttributeSet(var3), (AnimatorSet)null, 0, var4);
   }

   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, AttributeSet var4, AnimatorSet var5, int var6, float var7) throws XmlPullParserException, IOException {
      int var8 = var3.getDepth();
      Object var9 = null;
      ArrayList var10 = null;

      while(true) {
         int var11 = var3.next();
         byte var12 = 0;
         boolean var13 = false;
         if (var11 == 3 && var3.getDepth() <= var8 || var11 == 1) {
            if (var5 != null && var10 != null) {
               Animator[] var17 = new Animator[var10.size()];
               Iterator var18 = var10.iterator();

               for(int var20 = var12; var18.hasNext(); ++var20) {
                  var17[var20] = (Animator)var18.next();
               }

               if (var6 == 0) {
                  var5.playTogether(var17);
               } else {
                  var5.playSequentially(var17);
               }
            }

            return (Animator)var9;
         }

         if (var11 == 2) {
            String var14 = var3.getName();
            Object var21;
            if (var14.equals("objectAnimator")) {
               var21 = loadObjectAnimator(var0, var1, var2, var4, var7, var3);
            } else if (var14.equals("animator")) {
               var21 = loadAnimator(var0, var1, var2, var4, (ValueAnimator)null, var7, var3);
            } else if (var14.equals("set")) {
               var21 = new AnimatorSet();
               TypedArray var19 = TypedArrayUtils.obtainAttributes(var1, var2, var4, AndroidResources.STYLEABLE_ANIMATOR_SET);
               createAnimatorFromXml(var0, var1, var2, var3, var4, (AnimatorSet)var21, TypedArrayUtils.getNamedInt(var19, var3, "ordering", 0, 0), var7);
               var19.recycle();
            } else {
               if (!var14.equals("propertyValuesHolder")) {
                  StringBuilder var16 = new StringBuilder();
                  var16.append("Unknown animator name: ");
                  var16.append(var3.getName());
                  throw new RuntimeException(var16.toString());
               }

               PropertyValuesHolder[] var22 = loadValues(var0, var1, var2, var3, Xml.asAttributeSet(var3));
               if (var22 != null && var9 != null && var9 instanceof ValueAnimator) {
                  ((ValueAnimator)var9).setValues(var22);
               }

               var13 = true;
               var21 = var9;
            }

            var9 = var21;
            if (var5 != null) {
               var9 = var21;
               if (!var13) {
                  ArrayList var15 = var10;
                  if (var10 == null) {
                     var15 = new ArrayList();
                  }

                  var15.add(var21);
                  var9 = var21;
                  var10 = var15;
               }
            }
         }
      }
   }

   private static Keyframe createNewKeyframe(Keyframe var0, float var1) {
      if (var0.getType() == Float.TYPE) {
         var0 = Keyframe.ofFloat(var1);
      } else if (var0.getType() == Integer.TYPE) {
         var0 = Keyframe.ofInt(var1);
      } else {
         var0 = Keyframe.ofObject(var1);
      }

      return var0;
   }

   private static void distributeKeyframes(Keyframe[] var0, float var1, int var2, int var3) {
      for(var1 /= (float)(var3 - var2 + 2); var2 <= var3; ++var2) {
         var0[var2].setFraction(var0[var2 - 1].getFraction() + var1);
      }

   }

   private static PropertyValuesHolder getPVH(TypedArray var0, int var1, int var2, int var3, String var4) {
      TypedValue var5 = var0.peekValue(var2);
      boolean var6;
      if (var5 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var7;
      if (var6) {
         var7 = var5.type;
      } else {
         var7 = 0;
      }

      var5 = var0.peekValue(var3);
      boolean var8;
      if (var5 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var9;
      if (var8) {
         var9 = var5.type;
      } else {
         var9 = 0;
      }

      int var10 = var1;
      if (var1 == 4) {
         if ((!var6 || !isColorType(var7)) && (!var8 || !isColorType(var9))) {
            var10 = 0;
         } else {
            var10 = 3;
         }
      }

      boolean var19;
      if (var10 == 0) {
         var19 = true;
      } else {
         var19 = false;
      }

      var5 = null;
      String var11 = null;
      PropertyValuesHolder var17;
      if (var10 == 2) {
         String var12 = var0.getString(var2);
         var11 = var0.getString(var3);
         PathParser.PathDataNode[] var13 = PathParser.createNodesFromPathData(var12);
         PathParser.PathDataNode[] var14 = PathParser.createNodesFromPathData(var11);
         if (var13 == null) {
            var17 = var5;
            if (var14 == null) {
               return var17;
            }
         }

         if (var13 != null) {
            AnimatorInflaterCompat.PathDataEvaluator var18 = new AnimatorInflaterCompat.PathDataEvaluator();
            if (var14 != null) {
               if (!PathParser.canMorph(var13, var14)) {
                  StringBuilder var20 = new StringBuilder();
                  var20.append(" Can't morph from ");
                  var20.append(var12);
                  var20.append(" to ");
                  var20.append(var11);
                  throw new InflateException(var20.toString());
               }

               var17 = PropertyValuesHolder.ofObject(var4, var18, new Object[]{var13, var14});
            } else {
               var17 = PropertyValuesHolder.ofObject(var4, var18, new Object[]{var13});
            }
         } else {
            var17 = var5;
            if (var14 != null) {
               var17 = PropertyValuesHolder.ofObject(var4, new AnimatorInflaterCompat.PathDataEvaluator(), new Object[]{var14});
            }
         }
      } else {
         ArgbEvaluator var22;
         if (var10 == 3) {
            var22 = ArgbEvaluator.getInstance();
         } else {
            var22 = null;
         }

         PropertyValuesHolder var21;
         if (var19) {
            float var15;
            if (var6) {
               if (var7 == 5) {
                  var15 = var0.getDimension(var2, 0.0F);
               } else {
                  var15 = var0.getFloat(var2, 0.0F);
               }

               if (var8) {
                  float var16;
                  if (var9 == 5) {
                     var16 = var0.getDimension(var3, 0.0F);
                  } else {
                     var16 = var0.getFloat(var3, 0.0F);
                  }

                  var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var15, var16});
               } else {
                  var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var15});
               }
            } else {
               if (var9 == 5) {
                  var15 = var0.getDimension(var3, 0.0F);
               } else {
                  var15 = var0.getFloat(var3, 0.0F);
               }

               var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var15});
            }

            var21 = var17;
         } else if (var6) {
            if (var7 == 5) {
               var1 = (int)var0.getDimension(var2, 0.0F);
            } else if (isColorType(var7)) {
               var1 = var0.getColor(var2, 0);
            } else {
               var1 = var0.getInt(var2, 0);
            }

            if (var8) {
               if (var9 == 5) {
                  var2 = (int)var0.getDimension(var3, 0.0F);
               } else if (isColorType(var9)) {
                  var2 = var0.getColor(var3, 0);
               } else {
                  var2 = var0.getInt(var3, 0);
               }

               var21 = PropertyValuesHolder.ofInt(var4, new int[]{var1, var2});
            } else {
               var21 = PropertyValuesHolder.ofInt(var4, new int[]{var1});
            }
         } else {
            var21 = var11;
            if (var8) {
               if (var9 == 5) {
                  var1 = (int)var0.getDimension(var3, 0.0F);
               } else if (isColorType(var9)) {
                  var1 = var0.getColor(var3, 0);
               } else {
                  var1 = var0.getInt(var3, 0);
               }

               var21 = PropertyValuesHolder.ofInt(var4, new int[]{var1});
            }
         }

         var17 = var21;
         if (var21 != null) {
            var17 = var21;
            if (var22 != null) {
               var21.setEvaluator(var22);
               var17 = var21;
            }
         }
      }

      return var17;
   }

   private static int inferValueTypeFromValues(TypedArray var0, int var1, int var2) {
      TypedValue var3 = var0.peekValue(var1);
      boolean var4 = true;
      byte var5 = 0;
      boolean var8;
      if (var3 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var6;
      if (var8) {
         var6 = var3.type;
      } else {
         var6 = 0;
      }

      TypedValue var7 = var0.peekValue(var2);
      boolean var9;
      if (var7 != null) {
         var9 = var4;
      } else {
         var9 = false;
      }

      int var11;
      if (var9) {
         var11 = var7.type;
      } else {
         var11 = 0;
      }

      byte var10;
      if (!var8 || !isColorType(var6)) {
         var10 = var5;
         if (!var9) {
            return var10;
         }

         var10 = var5;
         if (!isColorType(var11)) {
            return var10;
         }
      }

      var10 = 3;
      return var10;
   }

   private static int inferValueTypeOfKeyframe(Resources var0, Theme var1, AttributeSet var2, XmlPullParser var3) {
      TypedArray var7 = TypedArrayUtils.obtainAttributes(var0, var1, var2, AndroidResources.STYLEABLE_KEYFRAME);
      byte var4 = 0;
      TypedValue var8 = TypedArrayUtils.peekNamedValue(var7, var3, "value", 0);
      boolean var5;
      if (var8 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      byte var6 = var4;
      if (var5) {
         var6 = var4;
         if (isColorType(var8.type)) {
            var6 = 3;
         }
      }

      var7.recycle();
      return var6;
   }

   private static boolean isColorType(int var0) {
      boolean var1;
      if (var0 >= 28 && var0 <= 31) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static Animator loadAnimator(Context var0, int var1) throws NotFoundException {
      Animator var2;
      if (VERSION.SDK_INT >= 24) {
         var2 = AnimatorInflater.loadAnimator(var0, var1);
      } else {
         var2 = loadAnimator(var0, var0.getResources(), var0.getTheme(), var1);
      }

      return var2;
   }

   public static Animator loadAnimator(Context var0, Resources var1, Theme var2, int var3) throws NotFoundException {
      return loadAnimator(var0, var1, var2, var3, 1.0F);
   }

   public static Animator loadAnimator(Context param0, Resources param1, Theme param2, int param3, float param4) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }

   private static ValueAnimator loadAnimator(Context var0, Resources var1, Theme var2, AttributeSet var3, ValueAnimator var4, float var5, XmlPullParser var6) throws NotFoundException {
      TypedArray var7 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_ANIMATOR);
      TypedArray var10 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
      ValueAnimator var9 = var4;
      if (var4 == null) {
         var9 = new ValueAnimator();
      }

      parseAnimatorFromTypeArray(var9, var7, var10, var5, var6);
      int var8 = TypedArrayUtils.getNamedResourceId(var7, var6, "interpolator", 0, 0);
      if (var8 > 0) {
         var9.setInterpolator(AnimationUtilsCompat.loadInterpolator(var0, var8));
      }

      var7.recycle();
      if (var10 != null) {
         var10.recycle();
      }

      return var9;
   }

   private static Keyframe loadKeyframe(Context var0, Resources var1, Theme var2, AttributeSet var3, int var4, XmlPullParser var5) throws XmlPullParserException, IOException {
      TypedArray var11 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_KEYFRAME);
      float var6 = TypedArrayUtils.getNamedFloat(var11, var5, "fraction", 3, -1.0F);
      TypedValue var9 = TypedArrayUtils.peekNamedValue(var11, var5, "value", 0);
      boolean var7;
      if (var9 != null) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var8 = var4;
      if (var4 == 4) {
         if (var7 && isColorType(var9.type)) {
            var8 = 3;
         } else {
            var8 = 0;
         }
      }

      Keyframe var10;
      if (var7) {
         if (var8 != 0) {
            if (var8 != 1 && var8 != 3) {
               var10 = null;
            } else {
               var10 = Keyframe.ofInt(var6, TypedArrayUtils.getNamedInt(var11, var5, "value", 0, 0));
            }
         } else {
            var10 = Keyframe.ofFloat(var6, TypedArrayUtils.getNamedFloat(var11, var5, "value", 0, 0.0F));
         }
      } else if (var8 == 0) {
         var10 = Keyframe.ofFloat(var6);
      } else {
         var10 = Keyframe.ofInt(var6);
      }

      var4 = TypedArrayUtils.getNamedResourceId(var11, var5, "interpolator", 1, 0);
      if (var4 > 0) {
         var10.setInterpolator(AnimationUtilsCompat.loadInterpolator(var0, var4));
      }

      var11.recycle();
      return var10;
   }

   private static ObjectAnimator loadObjectAnimator(Context var0, Resources var1, Theme var2, AttributeSet var3, float var4, XmlPullParser var5) throws NotFoundException {
      ObjectAnimator var6 = new ObjectAnimator();
      loadAnimator(var0, var1, var2, var3, var6, var4, var5);
      return var6;
   }

   private static PropertyValuesHolder loadPvh(Context var0, Resources var1, Theme var2, XmlPullParser var3, String var4, int var5) throws XmlPullParserException, IOException {
      Object var6 = null;
      ArrayList var7 = null;
      int var8 = var5;

      while(true) {
         var5 = var3.next();
         if (var5 == 3 || var5 == 1) {
            PropertyValuesHolder var16 = (PropertyValuesHolder)var6;
            if (var7 != null) {
               int var11 = var7.size();
               var16 = (PropertyValuesHolder)var6;
               if (var11 > 0) {
                  byte var12 = 0;
                  Keyframe var17 = (Keyframe)var7.get(0);
                  Keyframe var18 = (Keyframe)var7.get(var11 - 1);
                  float var13 = var18.getFraction();
                  var5 = var11;
                  if (var13 < 1.0F) {
                     if (var13 < 0.0F) {
                        var18.setFraction(1.0F);
                        var5 = var11;
                     } else {
                        var7.add(var7.size(), createNewKeyframe(var18, 1.0F));
                        var5 = var11 + 1;
                     }
                  }

                  var13 = var17.getFraction();
                  var11 = var5;
                  if (var13 != 0.0F) {
                     if (var13 < 0.0F) {
                        var17.setFraction(0.0F);
                        var11 = var5;
                     } else {
                        var7.add(0, createNewKeyframe(var17, 0.0F));
                        var11 = var5 + 1;
                     }
                  }

                  Keyframe[] var19 = new Keyframe[var11];
                  var7.toArray(var19);

                  for(var5 = var12; var5 < var11; ++var5) {
                     var18 = var19[var5];
                     if (var18.getFraction() < 0.0F) {
                        if (var5 == 0) {
                           var18.setFraction(0.0F);
                        } else {
                           int var14 = var11 - 1;
                           if (var5 == var14) {
                              var18.setFraction(1.0F);
                           } else {
                              int var21 = var5 + 1;

                              int var15;
                              for(var15 = var5; var21 < var14 && var19[var21].getFraction() < 0.0F; var15 = var21++) {
                              }

                              distributeKeyframes(var19, var19[var15 + 1].getFraction() - var19[var5 - 1].getFraction(), var5, var15);
                           }
                        }
                     }
                  }

                  PropertyValuesHolder var20 = PropertyValuesHolder.ofKeyframe(var4, var19);
                  var16 = var20;
                  if (var8 == 3) {
                     var20.setEvaluator(ArgbEvaluator.getInstance());
                     var16 = var20;
                  }
               }
            }

            return var16;
         }

         if (var3.getName().equals("keyframe")) {
            var5 = var8;
            if (var8 == 4) {
               var5 = inferValueTypeOfKeyframe(var1, var2, Xml.asAttributeSet(var3), var3);
            }

            Keyframe var9 = loadKeyframe(var0, var1, var2, Xml.asAttributeSet(var3), var5, var3);
            ArrayList var10 = var7;
            if (var9 != null) {
               var10 = var7;
               if (var7 == null) {
                  var10 = new ArrayList();
               }

               var10.add(var9);
            }

            var3.next();
            var8 = var5;
            var7 = var10;
         }
      }
   }

   private static PropertyValuesHolder[] loadValues(Context var0, Resources var1, Theme var2, XmlPullParser var3, AttributeSet var4) throws XmlPullParserException, IOException {
      Object var5 = null;
      ArrayList var6 = null;

      while(true) {
         int var7 = var3.getEventType();
         int var8 = 0;
         if (var7 == 3 || var7 == 1) {
            PropertyValuesHolder[] var13 = (PropertyValuesHolder[])var5;
            if (var6 != null) {
               var7 = var6.size();
               PropertyValuesHolder[] var14 = new PropertyValuesHolder[var7];

               while(true) {
                  var13 = var14;
                  if (var8 >= var7) {
                     break;
                  }

                  var14[var8] = (PropertyValuesHolder)var6.get(var8);
                  ++var8;
               }
            }

            return var13;
         }

         if (var7 != 2) {
            var3.next();
         } else {
            if (var3.getName().equals("propertyValuesHolder")) {
               TypedArray var9 = TypedArrayUtils.obtainAttributes(var1, var2, var4, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
               String var10 = TypedArrayUtils.getNamedString(var9, var3, "propertyName", 3);
               var8 = TypedArrayUtils.getNamedInt(var9, var3, "valueType", 2, 4);
               PropertyValuesHolder var11 = loadPvh(var0, var1, var2, var3, var10, var8);
               PropertyValuesHolder var12 = var11;
               if (var11 == null) {
                  var12 = getPVH(var9, var8, 0, 1, var10);
               }

               ArrayList var15 = var6;
               if (var12 != null) {
                  var15 = var6;
                  if (var6 == null) {
                     var15 = new ArrayList();
                  }

                  var15.add(var12);
               }

               var9.recycle();
               var6 = var15;
            }

            var3.next();
         }
      }
   }

   private static void parseAnimatorFromTypeArray(ValueAnimator var0, TypedArray var1, TypedArray var2, float var3, XmlPullParser var4) {
      long var5 = (long)TypedArrayUtils.getNamedInt(var1, var4, "duration", 1, 300);
      long var7 = (long)TypedArrayUtils.getNamedInt(var1, var4, "startOffset", 2, 0);
      int var9 = TypedArrayUtils.getNamedInt(var1, var4, "valueType", 7, 4);
      int var10 = var9;
      if (TypedArrayUtils.hasAttribute(var4, "valueFrom")) {
         var10 = var9;
         if (TypedArrayUtils.hasAttribute(var4, "valueTo")) {
            int var11 = var9;
            if (var9 == 4) {
               var11 = inferValueTypeFromValues(var1, 5, 6);
            }

            PropertyValuesHolder var12 = getPVH(var1, var11, 5, 6, "");
            var10 = var11;
            if (var12 != null) {
               var0.setValues(new PropertyValuesHolder[]{var12});
               var10 = var11;
            }
         }
      }

      var0.setDuration(var5);
      var0.setStartDelay(var7);
      var0.setRepeatCount(TypedArrayUtils.getNamedInt(var1, var4, "repeatCount", 3, 0));
      var0.setRepeatMode(TypedArrayUtils.getNamedInt(var1, var4, "repeatMode", 4, 1));
      if (var2 != null) {
         setupObjectAnimator(var0, var2, var10, var3, var4);
      }

   }

   private static void setupObjectAnimator(ValueAnimator var0, TypedArray var1, int var2, float var3, XmlPullParser var4) {
      ObjectAnimator var7 = (ObjectAnimator)var0;
      String var5 = TypedArrayUtils.getNamedString(var1, var4, "pathData", 1);
      if (var5 != null) {
         String var6 = TypedArrayUtils.getNamedString(var1, var4, "propertyXName", 2);
         String var9 = TypedArrayUtils.getNamedString(var1, var4, "propertyYName", 3);
         if (var2 != 2) {
         }

         if (var6 == null && var9 == null) {
            StringBuilder var8 = new StringBuilder();
            var8.append(var1.getPositionDescription());
            var8.append(" propertyXName or propertyYName is needed for PathData");
            throw new InflateException(var8.toString());
         }

         setupPathMotion(PathParser.createPathFromPathData(var5), var7, var3 * 0.5F, var6, var9);
      } else {
         var7.setPropertyName(TypedArrayUtils.getNamedString(var1, var4, "propertyName", 0));
      }

   }

   private static void setupPathMotion(Path var0, ObjectAnimator var1, float var2, String var3, String var4) {
      PathMeasure var5 = new PathMeasure(var0, false);
      ArrayList var6 = new ArrayList();
      var6.add(0.0F);
      float var7 = 0.0F;

      float var8;
      do {
         var8 = var7 + var5.getLength();
         var6.add(var8);
         var7 = var8;
      } while(var5.nextContour());

      PathMeasure var9 = new PathMeasure(var0, false);
      int var10 = Math.min(100, (int)(var8 / var2) + 1);
      float[] var11 = new float[var10];
      float[] var12 = new float[var10];
      float[] var17 = new float[2];
      var7 = var8 / (float)(var10 - 1);
      int var13 = 0;
      var2 = 0.0F;
      int var14 = 0;

      while(true) {
         var5 = null;
         if (var13 >= var10) {
            PropertyValuesHolder var18;
            if (var3 != null) {
               var18 = PropertyValuesHolder.ofFloat(var3, var11);
            } else {
               var18 = null;
            }

            PropertyValuesHolder var19 = var5;
            if (var4 != null) {
               var19 = PropertyValuesHolder.ofFloat(var4, var12);
            }

            if (var18 == null) {
               var1.setValues(new PropertyValuesHolder[]{var19});
            } else if (var19 == null) {
               var1.setValues(new PropertyValuesHolder[]{var18});
            } else {
               var1.setValues(new PropertyValuesHolder[]{var18, var19});
            }

            return;
         }

         var9.getPosTan(var2 - (Float)var6.get(var14), var17, (float[])null);
         var11[var13] = var17[0];
         var12[var13] = var17[1];
         var2 += var7;
         int var15 = var14 + 1;
         int var16 = var14;
         if (var15 < var6.size()) {
            var16 = var14;
            if (var2 > (Float)var6.get(var15)) {
               var9.nextContour();
               var16 = var15;
            }
         }

         ++var13;
         var14 = var16;
      }
   }

   private static class PathDataEvaluator implements TypeEvaluator {
      private PathParser.PathDataNode[] mNodeArray;

      PathDataEvaluator() {
      }

      public PathParser.PathDataNode[] evaluate(float var1, PathParser.PathDataNode[] var2, PathParser.PathDataNode[] var3) {
         if (!PathParser.canMorph(var2, var3)) {
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
         } else {
            PathParser.PathDataNode[] var4 = this.mNodeArray;
            if (var4 == null || !PathParser.canMorph(var4, var2)) {
               this.mNodeArray = PathParser.deepCopyNodes(var2);
            }

            for(int var5 = 0; var5 < var2.length; ++var5) {
               this.mNodeArray[var5].interpolatePathDataNode(var2[var5], var3[var5], var1);
            }

            return this.mNodeArray;
         }
      }
   }
}
