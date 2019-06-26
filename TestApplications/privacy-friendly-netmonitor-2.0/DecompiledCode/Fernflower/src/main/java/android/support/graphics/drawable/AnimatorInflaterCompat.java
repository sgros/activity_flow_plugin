package android.support.graphics.drawable;

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
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
   private static final boolean DBG_ANIMATOR_INFLATER = false;
   private static final int MAX_NUM_POINTS = 100;
   private static final String TAG = "AnimatorInflater";
   private static final int TOGETHER = 0;
   private static final int VALUE_TYPE_COLOR = 3;
   private static final int VALUE_TYPE_FLOAT = 0;
   private static final int VALUE_TYPE_INT = 1;
   private static final int VALUE_TYPE_PATH = 2;
   private static final int VALUE_TYPE_UNDEFINED = 4;

   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, float var4) throws XmlPullParserException, IOException {
      return createAnimatorFromXml(var0, var1, var2, var3, Xml.asAttributeSet(var3), (AnimatorSet)null, 0, var4);
   }

   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, AttributeSet var4, AnimatorSet var5, int var6, float var7) throws XmlPullParserException, IOException {
      int var8 = var3.getDepth();
      Object var9 = null;
      ArrayList var10 = null;

      while(true) {
         int var11 = var3.next();
         boolean var12 = false;
         int var19;
         if (var11 == 3 && var3.getDepth() <= var8 || var11 == 1) {
            if (var5 != null && var10 != null) {
               Animator[] var16 = new Animator[var10.size()];
               Iterator var17 = var10.iterator();

               for(var19 = 0; var17.hasNext(); ++var19) {
                  var16[var19] = (Animator)var17.next();
               }

               if (var6 == 0) {
                  var5.playTogether(var16);
               } else {
                  var5.playSequentially(var16);
               }
            }

            return (Animator)var9;
         }

         if (var11 == 2) {
            String var13 = var3.getName();
            Object var20;
            if (var13.equals("objectAnimator")) {
               var20 = loadObjectAnimator(var0, var1, var2, var4, var7, var3);
            } else if (var13.equals("animator")) {
               var20 = loadAnimator(var0, var1, var2, var4, (ValueAnimator)null, var7, var3);
            } else if (var13.equals("set")) {
               var20 = new AnimatorSet();
               TypedArray var18 = TypedArrayUtils.obtainAttributes(var1, var2, var4, AndroidResources.STYLEABLE_ANIMATOR_SET);
               var19 = TypedArrayUtils.getNamedInt(var18, var3, "ordering", 0, 0);
               createAnimatorFromXml(var0, var1, var2, var3, var4, (AnimatorSet)var20, var19, var7);
               var18.recycle();
               var12 = false;
            } else {
               if (!var13.equals("propertyValuesHolder")) {
                  StringBuilder var15 = new StringBuilder();
                  var15.append("Unknown animator name: ");
                  var15.append(var3.getName());
                  throw new RuntimeException(var15.toString());
               }

               PropertyValuesHolder[] var21 = loadValues(var0, var1, var2, var3, Xml.asAttributeSet(var3));
               if (var21 != null && var9 != null && var9 instanceof ValueAnimator) {
                  ((ValueAnimator)var9).setValues(var21);
               }

               var12 = true;
               var20 = var9;
            }

            var9 = var20;
            if (var5 != null) {
               var9 = var20;
               if (!var12) {
                  ArrayList var14 = var10;
                  if (var10 == null) {
                     var14 = new ArrayList();
                  }

                  var14.add(var20);
                  var9 = var20;
                  var10 = var14;
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

   private static void dumpKeyframes(Object[] var0, String var1) {
      if (var0 != null && var0.length != 0) {
         Log.d("AnimatorInflater", var1);
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Keyframe var4 = (Keyframe)var0[var3];
            StringBuilder var5 = new StringBuilder();
            var5.append("Keyframe ");
            var5.append(var3);
            var5.append(": fraction ");
            Object var6;
            if (var4.getFraction() < 0.0F) {
               var6 = "null";
            } else {
               var6 = var4.getFraction();
            }

            var5.append(var6);
            var5.append(", ");
            var5.append(", value : ");
            if (var4.hasValue()) {
               var6 = var4.getValue();
            } else {
               var6 = "null";
            }

            var5.append(var6);
            Log.d("AnimatorInflater", var5.toString());
         }

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

   public static Animator loadAnimator(Context var0, @AnimatorRes int var1) throws NotFoundException {
      Animator var2;
      if (VERSION.SDK_INT >= 24) {
         var2 = AnimatorInflater.loadAnimator(var0, var1);
      } else {
         var2 = loadAnimator(var0, var0.getResources(), var0.getTheme(), var1);
      }

      return var2;
   }

   public static Animator loadAnimator(Context var0, Resources var1, Theme var2, @AnimatorRes int var3) throws NotFoundException {
      return loadAnimator(var0, var1, var2, var3, 1.0F);
   }

   public static Animator loadAnimator(Context var0, Resources var1, Theme var2, @AnimatorRes int var3, float var4) throws NotFoundException {
      Object var5 = null;
      Object var6 = null;
      XmlResourceParser var7 = null;

      XmlResourceParser var8;
      Animator var428;
      label2954: {
         Throwable var10000;
         label2955: {
            boolean var10001;
            XmlResourceParser var427;
            XmlPullParserException var430;
            label2948: {
               label2956: {
                  IOException var429;
                  label2957: {
                     label2945: {
                        try {
                           try {
                              var8 = var1.getAnimation(var3);
                              break label2945;
                           } catch (XmlPullParserException var424) {
                              var430 = var424;
                              break label2956;
                           } catch (IOException var425) {
                              var429 = var425;
                           }
                        } catch (Throwable var426) {
                           var10000 = var426;
                           var10001 = false;
                           break label2955;
                        }

                        var427 = (XmlResourceParser)var5;
                        break label2957;
                     }

                     label2937: {
                        try {
                           var428 = createAnimatorFromXml(var0, var1, var2, var8, var4);
                           break label2954;
                        } catch (XmlPullParserException var421) {
                           var430 = var421;
                        } catch (IOException var422) {
                           var429 = var422;
                           break label2937;
                        } finally {
                           ;
                        }

                        var427 = var8;
                        break label2948;
                     }

                     var427 = var8;
                  }

                  var7 = var427;

                  NotFoundException var431;
                  try {
                     var431 = new NotFoundException;
                  } catch (Throwable var415) {
                     var10000 = var415;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  StringBuilder var434;
                  try {
                     var434 = new StringBuilder;
                  } catch (Throwable var414) {
                     var10000 = var414;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     var434.<init>();
                  } catch (Throwable var413) {
                     var10000 = var413;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     var434.append("Can't load animation resource ID #0x");
                  } catch (Throwable var412) {
                     var10000 = var412;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     var434.append(Integer.toHexString(var3));
                  } catch (Throwable var411) {
                     var10000 = var411;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     var431.<init>(var434.toString());
                  } catch (Throwable var410) {
                     var10000 = var410;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     var431.initCause(var429);
                  } catch (Throwable var409) {
                     var10000 = var409;
                     var10001 = false;
                     break label2955;
                  }

                  var7 = var427;

                  try {
                     throw var431;
                  } catch (Throwable var405) {
                     var10000 = var405;
                     var10001 = false;
                     break label2955;
                  }
               }

               var427 = (XmlResourceParser)var6;
            }

            var7 = var427;

            NotFoundException var435;
            try {
               var435 = new NotFoundException;
            } catch (Throwable var420) {
               var10000 = var420;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            StringBuilder var433;
            try {
               var433 = new StringBuilder;
            } catch (Throwable var419) {
               var10000 = var419;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            try {
               var433.<init>();
            } catch (Throwable var418) {
               var10000 = var418;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            try {
               var433.append("Can't load animation resource ID #0x");
            } catch (Throwable var417) {
               var10000 = var417;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            try {
               var433.append(Integer.toHexString(var3));
            } catch (Throwable var416) {
               var10000 = var416;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            try {
               var435.<init>(var433.toString());
            } catch (Throwable var408) {
               var10000 = var408;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            try {
               var435.initCause(var430);
            } catch (Throwable var407) {
               var10000 = var407;
               var10001 = false;
               break label2955;
            }

            var7 = var427;

            label2873:
            try {
               throw var435;
            } catch (Throwable var406) {
               var10000 = var406;
               var10001 = false;
               break label2873;
            }
         }

         Throwable var432 = var10000;
         if (var7 != null) {
            var7.close();
         }

         throw var432;
      }

      if (var8 != null) {
         var8.close();
      }

      return var428;
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
         label33: {
            if (var8 != 3) {
               switch(var8) {
               case 0:
                  var10 = Keyframe.ofFloat(var6, TypedArrayUtils.getNamedFloat(var11, var5, "value", 0, 0.0F));
                  break label33;
               case 1:
                  break;
               default:
                  var10 = null;
                  break label33;
               }
            }

            var10 = Keyframe.ofInt(var6, TypedArrayUtils.getNamedInt(var11, var5, "value", 0, 0));
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
                  Keyframe var18 = (Keyframe)var7.get(0);
                  Keyframe var17 = (Keyframe)var7.get(var11 - 1);
                  float var13 = var17.getFraction();
                  var5 = var11;
                  if (var13 < 1.0F) {
                     if (var13 < 0.0F) {
                        var17.setFraction(1.0F);
                        var5 = var11;
                     } else {
                        var7.add(var7.size(), createNewKeyframe(var17, 1.0F));
                        var5 = var11 + 1;
                     }
                  }

                  var13 = var18.getFraction();
                  var11 = var5;
                  if (var13 != 0.0F) {
                     if (var13 < 0.0F) {
                        var18.setFraction(0.0F);
                        var11 = var5;
                     } else {
                        var7.add(0, createNewKeyframe(var18, 0.0F));
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
      ObjectAnimator var5 = (ObjectAnimator)var0;
      String var6 = TypedArrayUtils.getNamedString(var1, var4, "pathData", 1);
      if (var6 != null) {
         String var7 = TypedArrayUtils.getNamedString(var1, var4, "propertyXName", 2);
         String var9 = TypedArrayUtils.getNamedString(var1, var4, "propertyYName", 3);
         if (var2 != 2) {
         }

         if (var7 == null && var9 == null) {
            StringBuilder var8 = new StringBuilder();
            var8.append(var1.getPositionDescription());
            var8.append(" propertyXName or propertyYName is needed for PathData");
            throw new InflateException(var8.toString());
         }

         setupPathMotion(PathParser.createPathFromPathData(var6), var5, 0.5F * var3, var7, var9);
      } else {
         var5.setPropertyName(TypedArrayUtils.getNamedString(var1, var4, "propertyName", 0));
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
      var8 /= (float)(var10 - 1);
      int var13 = 0;
      var2 = 0.0F;
      int var14 = var13;

      while(true) {
         var5 = null;
         if (var14 >= var10) {
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

         var9.getPosTan(var2, var17, (float[])null);
         var9.getPosTan(var2, var17, (float[])null);
         var11[var14] = var17[0];
         var12[var14] = var17[1];
         var7 = var2 + var8;
         int var15 = var13 + 1;
         var2 = var7;
         int var16 = var13;
         if (var15 < var6.size()) {
            var2 = var7;
            var16 = var13;
            if (var7 > (Float)var6.get(var15)) {
               var2 = var7 - (Float)var6.get(var15);
               var9.nextContour();
               var16 = var15;
            }
         }

         ++var14;
         var13 = var16;
      }
   }

   private static class PathDataEvaluator implements TypeEvaluator {
      private PathParser.PathDataNode[] mNodeArray;

      private PathDataEvaluator() {
      }

      // $FF: synthetic method
      PathDataEvaluator(Object var1) {
         this();
      }

      PathDataEvaluator(PathParser.PathDataNode[] var1) {
         this.mNodeArray = var1;
      }

      public PathParser.PathDataNode[] evaluate(float var1, PathParser.PathDataNode[] var2, PathParser.PathDataNode[] var3) {
         if (!PathParser.canMorph(var2, var3)) {
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
         } else {
            if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, var2)) {
               this.mNodeArray = PathParser.deepCopyNodes(var2);
            }

            for(int var4 = 0; var4 < var2.length; ++var4) {
               this.mNodeArray[var4].interpolatePathDataNode(var2[var4], var3[var4], var1);
            }

            return this.mNodeArray;
         }
      }
   }
}
